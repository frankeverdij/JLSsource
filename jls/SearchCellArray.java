package jls;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import jls.engine.java.JavaSearchEngine;
import jls.engine.java.Stack;

public class SearchCellArray extends CellArray
{
    // owning search thread
    private SearchThread owner = null;
    // number of cells in the array
    private int cellCount = 0;
    private static final String CELL_COUNT_NAME = "cell_count";
    // search engine
    private JavaSearchEngine searchEngine = null;
    // active search options
    private SearchOptions searchOptions = null;
    // false = preprocess mode
    // true = full search mode
    private boolean searchMode = false;
    private static final String SEARCH_MODE_NAME = "search_mode";
    // if true, then the search should be started from scratch
    // works for both full and fast search
    private boolean searchReset = true;
    // indicates that search options were changed
    // and should be propagated to search engine
    private boolean optionsChanged = true;
    
    private Timer timer = new Timer();
    private TimerTask saveTask = null;
    private TimerTask displayTask = null;

    private int errorType;
    private static final int ERROR_NO_ENGINE = 0;
    private static final int ERROR_IN_RULES = 1;
    private static final int ERROR_IN_G0_CONSTRAINT = 2;
    private static final int ERROR_IN_ACTIVE_LAYERS = 3;
    private static final int ERROR_IN_LIVE_LAYERS = 4;
    private static final int ERROR_IN_TRAINING = 5;
    
    // processing data (both fast and full)

    // cell merging array
    // after processing, each cell either points to its representative in the main array
    // or after more processing, to its representative in the array of search cells
    // in the second stage, there may be some cells that don't have a representative
    // (e.g. ON, OFF, or hard unchecked cells)
    // such cells have -1 in their index
    private int[] representative = null;
    private static final String REPRESENTATIVE_NAME = "representative";

    // number of search cells (variables)
    private int variableCount = 0;
    private static final String VARIABLE_COUNT_NAME = "variable_count";
    private static final String COMBINATION_NAME = "combination";

    // searched variable queue
    // contains index of each searched variable
    // in the order in which it will be searched
    private int[] searchedVariables = null;
    
    // indicator that we should display search progress
    private boolean timeToDisplay = false;
    // indicator that we should save search progress
    private boolean timeToSave = false;
    // time passed searching
    private long timePassedNs = 0;
    private static final String TIME_PASSED_NS_NAME = "time_passed_ns";
    // iterations done searching
    private long iterationsDone = 0;
    private static final String ITERATIONS_DONE_NAME = "iterations_done";
    // solutions found
    private long solutionsFound = 0;
    private static final String SOLUTIONS_FOUND_NAME = "solutions_found";
    
    private static final String STACK_NAME = "stack";
    
    public SearchCellArray(SearchThread owner, SearchOptions options)
    {
        this.owner = owner;
        searchOptions = options;
        representative = new int[Properties.CELLS_MAX];
    }

    // method to read search status from saved file
    
    public boolean readStatusParameter(StatusFileHandler handler, String name,
            int[] details, String value) throws IOException
    {
        if (CELL_COUNT_NAME.equals(name))
        {
            handler.checkDetailsLength(0);
            cellCount = handler.parseInteger(value);
        }
        else if (SEARCH_MODE_NAME.equals(name))
        {
            handler.checkDetailsLength(0);
            searchMode = handler.parseBoolean(value);
        }
        else if (VARIABLE_COUNT_NAME.equals(name))
        {
            handler.checkDetailsLength(0);
            variableCount = handler.parseInteger(value);
            prepareSearchEngine();
            searchEngine.reset(properties.getRuleBirth(), properties.getRuleSurvival(), variableCount);
        }
        else if (TIME_PASSED_NS_NAME.equals(name))
        {
            handler.checkDetailsLength(0);
            timePassedNs = handler.parseLong(value);
        }
        else if (ITERATIONS_DONE_NAME.equals(name))
        {
            handler.checkDetailsLength(0);
            iterationsDone = handler.parseLong(value);
        }
        else if (SOLUTIONS_FOUND_NAME.equals(name))
        {
            handler.checkDetailsLength(0);
            solutionsFound = handler.parseLong(value);
        }
        else if (REPRESENTATIVE_NAME.equals(name))
        {
            handler.checkDetailsLength(2);
            int gen = details[0];
            if ((gen < 0) || (gen >= properties.getGenerations()))
            {
                return false;
            }
            int row = details[1];
            if ((row < 0) || (row >= properties.getRows()))
            {
                return false;
            }
            int [] line = handler.parseIntegerList(value);
            int col = line.length;
            if (col != properties.getColumns())
            {
                return false;
            }
            while (col > 0)
            {
                --col;
                representative[getCellPtr(col, row, gen)] = line[col];
            }
        }
        else if (COMBINATION_NAME.equals(name))
        {
            handler.checkDetailsLength(1);
            int i = details[0];
            if (i < 0)
            {
                return false;
            }
            else if (i == 0)
            {
                // prepare search engine before setting up variables
                // make sure we have a search engine
                prepareSearchEngine();
                // prepare it for used rules and number of variables
                searchEngine.reset(properties.getRuleBirth(), properties.getRuleSurvival(), variableCount);
                // inject constraints and set up initial state
                try
                {
                    if (!prepareConstraints(false))
                    {
                        return false;
                    }
                }
                catch (SearchThreadInterruptedException e)
                {
                    throw new RuntimeException("Thread interrupted when loading status");
                }
                // empty the stack
                Stack.reset();
                // prepare list of variables
                prepareSearchedVariables();
            }
            int [] line = handler.parseIntegerList(value);
            int len = line.length;
            if (i + len > variableCount)
            {
                return false;
            }
            while (len > 0)
            {
                --len;
                switch (line[len])
                {
                case 0:
                    searchEngine.setCombinationOff(i + len);
                    break;
                case 1:
                    searchEngine.setCombinationOn(i + len);
                    break;
                case 2:
                    searchEngine.setCombinationUnset(i + len);
                    break;
                default:
                    return false;
                }
            }
        }
        else if (STACK_NAME.equals(name))
        {
            handler.checkDetailsLength(1);
            int [] line = handler.parseIntegerList(value);
            if (line.length != 3)
            {
                return false;
            }
            searchEngine.pushOnStack(line[0], line[1] == 1, line[2] == 1);
        }
        else 
        {
            return super.readStatusParameter(handler, name, details, value);
        }
        return true;
    }

    // finish reading state from a file

    public String finishReadStatus()
    {
        String result = super.finishReadStatus();
        if (null != result)
        {
            return result;
        }
        searchReset = false;
        // convince the searcher to sort variables on first access
        optionsChanged = true;
        searchEngine.setSearchPruning(searchOptions.isPruneWithCombination() && (solutionsFound > 0));
        publishVariables();
        return null;
    }
    
    // write status to a file
    public void writeStatus(StatusFileHandler handler) throws IOException
    {
        handler.putParameter(CELL_COUNT_NAME, cellCount);
        handler.putParameter(SEARCH_MODE_NAME, searchMode);
        handler.putParameter(VARIABLE_COUNT_NAME, variableCount);
        handler.putParameter(TIME_PASSED_NS_NAME, timePassedNs);
        handler.putParameter(ITERATIONS_DONE_NAME, iterationsDone);
        handler.putParameter(SOLUTIONS_FOUND_NAME, solutionsFound);

        if (!searchMode)
        {
            super.writeStatus(handler);
        }
        else
        {
            // here we must be careful about cells set up by search
            // and only save cells which don't have representatives
            // the rest will be set up by search
            int [] singleRow = new int[properties.getColumns()];
            handler.newLine();
            for (int gen = 0; gen < properties.getGenerations(); ++gen)
            {
                for (int row = 0; row < properties.getRows(); ++row)
                {
                    for (int col = 0; col < properties.getColumns(); ++col)
                    {
                        if (representative[getCellPtr(col, row, gen)] == -1)
                        {
                            singleRow[col] = getCellValue(col, row, gen);
                        }
                        else
                        {
                            singleRow[col] = Cell.setState(getCellValue(col, row, gen), Cell.STATE_EMPTY);
                        }
                    }
                    handler.putParameter(CELLS_NAME + "{" + gen + "," + row + "}",
                            singleRow, singleRow.length);
                }
                handler.newLine();
            }
            // save stacks normally
            for (int row = 0; row < properties.getRows(); ++row)
            {
                for (int col = 0; col < properties.getColumns(); ++col)
                {
                    singleRow[col] = getStackValue(col, row);
                }
                handler.putParameter(STACKS_NAME + "{" + row + "}", singleRow, singleRow.length);
            }
            
            handler.newLine();
            handler.putComment("Representatives:");
            handler.putComment("Variable index for each cell, -1 for cells without a variable");
            handler.newLine();
            int [] temp = new int [properties.getColumns()];
            for (int gen = 0; gen < properties.getGenerations(); ++gen)
            {
                for (int row = 0; row < properties.getRows(); ++row)
                {
                    for (int col = 0; col < properties.getColumns(); ++col)
                    {
                        int cptr = getCellPtr(col, row, gen);
                        temp[col] = representative[cptr];
                    }
                    handler.putParameter(REPRESENTATIVE_NAME + "{" + gen + "," + row + "}", temp, temp.length);
                }
                handler.newLine();
            }
            handler.newLine();
            handler.putComment("Variable combination states:");
            handler.newLine();
            temp = new int[100];
            int i = 0;
            while (i < variableCount)
            {
                int len = variableCount - i;
                if (len > temp.length)
                {
                    len = temp.length;
                }
                int j = 0;
                while (j < len)
                {
                    if (searchEngine.isCombinationUnset(i))
                    {
                        temp[j] = 2;
                    }
                    else if (searchEngine.isCombinationOn(i))
                    {
                        temp[j] = 1;
                    }
                    else
                    {
                        temp[j] = 0;
                    }
                    ++j;
                    ++i;
                }
                handler.putParameter(COMBINATION_NAME + "{" + (i - len) + "}", temp, len);
            }
            handler.newLine();
            handler.putComment("Stack:");
            handler.putComment("- Variable index");
            handler.putComment("- Variable value, 0 = OFF, 1 = ON");
            handler.putComment("- Item type, 0 = closed, 1 = open (i.e. the other state was not tried yet)");
            handler.newLine();
            try
            {
                searchEngine.stackWalkPrepare();
                temp = new int [3];
                i = 0;
                while (searchEngine.stackWalkStep(temp))
                {
                    handler.putParameter(STACK_NAME + "{" + i + "}", temp, 3);
                    ++i;
                }
            }
            finally
            {
                // if IOException occurred, finish walking stack!
                searchEngine.stackWalkFinish();
            }
        }
    }

    private static final int EXTERNAL_EVENT_TIMED_SAVE = 1;
    private static final int EXTERNAL_EVENT_TIMED_DISPLAY = 2;
    public static final int EXTERNAL_EVENT_GUI_INTERRUPT = 3;

    public void setSearchMode()
    {
        searchMode = true;
    }
    
    public void setEditMode()
    {
        searchMode = false;
        stopTimerTasks();
    }
    
    public boolean isSearchMode()
    {
        return searchMode;
    }
    
    private void startTimerTasks()
    {
        if (displayTask == null)
        {
            if (searchOptions.isDisplayStatus())
            {
                displayTask = new SearchTimerTask(this, EXTERNAL_EVENT_TIMED_DISPLAY);
                timer.scheduleAtFixedRate(displayTask, searchOptions.getDisplayStatusPeriod() * 1000, searchOptions.getDisplayStatusPeriod() * 1000);
            }
        }
        if (saveTask == null)
        {
            if (searchOptions.isSaveStatus())
            {
                saveTask = new SearchTimerTask(this, EXTERNAL_EVENT_TIMED_SAVE);
                timer.scheduleAtFixedRate(saveTask, searchOptions.getSaveStatusPeriod() * 1000, searchOptions.getSaveStatusPeriod() * 1000);
            }
        }
    }
    
    public void stopTimerTasks()
    {
        if (displayTask != null)
        {
            displayTask.cancel();
            displayTask = null;
            timeToDisplay = false;
        }
        if (saveTask != null)
        {
            saveTask.cancel();
            saveTask = null;
            timeToSave = false;
        }
    }
    
    public void externalAction(int eventId)
    {
        if (eventId == EXTERNAL_EVENT_TIMED_DISPLAY)
        {
            timeToDisplay = true;
            searchEngine.interrupt();
        }
        else if (eventId == EXTERNAL_EVENT_TIMED_SAVE)
        {
            timeToSave = true;
            searchEngine.interrupt();
        }
        else if (eventId == EXTERNAL_EVENT_GUI_INTERRUPT)
        {
            if (searchEngine != null)
            {
                searchEngine.interrupt();
            }
        }
        else
        {
            throw new RuntimeException("Unhandled Time action ID: " + eventId);
        }
        
    }
    
    // this method is called by GUI thread to copy the edit array to the search array
    public void setArray(CellArray array)
    {
        //System.out.println(Thread.currentThread().getName() + " setArray()");
        // first copy properties, version, and contents
        super.load(array);

        // and do some our stuff
        cellCount = properties.getColumns() * properties.getRows() * properties.getGenerations();

        // started or not, after reloading the current search is lost
        searchReset = true;
    }
    
    public void setOptions(SearchOptions options)
    {
        stopTimerTasks();
        searchOptions = options;
        optionsChanged = true;
    }
    
    public boolean isReset()
    {
        return searchReset;
    }
    
    public int process() throws SearchThreadInterruptedException
    {
        //System.out.println(Thread.currentThread().getName() + " process() started");
        if (searchReset)
        {
            if (prepareSearch())
            {
                // preparations finished
                if (searchMode)
                {
                    prepareSearchedVariables();
                    searchEngine.processCombination(true);
                    searchReset = false;
                    // convince the searcher to sort variables on first access
                    optionsChanged = true;
                    iterationsDone = 0;
                    solutionsFound = 0;
                    timePassedNs = 0;
                    // display and continue
                    return ThreadMessage.MSG_DISPLAY;
                }
                else
                {
                    searchReset = false;
                    // stop
                    publishString("Ready");
                    return ThreadMessage.MSG_DONE;
                }
            }
            else
            {
                switch(errorType)
                {
                case ERROR_NO_ENGINE:
                    publishString("Couldn't initialize search engine");
                    break;
                case ERROR_IN_RULES:
                    publishString("Ready, errors in rules");
                    break;
                case ERROR_IN_G0_CONSTRAINT:
                    publishString("Ready, errors in generation zero constraint");
                    break;
                case ERROR_IN_ACTIVE_LAYERS:
                    publishString("Ready, errors in active cell constraints");
                    break;
                case ERROR_IN_LIVE_LAYERS:
                    publishString("Ready, errors in live cell constraints");
                    break;
                case ERROR_IN_TRAINING:
                    publishString("Ready, some cells cannot be set");
                    break;
                default:
                    publishString("Unknown error occurred");
                }
                if (searchMode)
                {
                    publishDialog("Cannot start search, errors found");
                    return ThreadMessage.MSG_ERROR;
                }
                return ThreadMessage.MSG_DONE;
            }
        }
        else
        {
            if (searchMode)
            {
                // search is prepared, just continue
                return fullSearch();
            }
            else
            {
                publishString("Ready");
                return ThreadMessage.MSG_DONE;
            }
        }
    }
    
    private void checkInterrupt() throws SearchThreadInterruptedException
    {
        owner.checkInterrupt();
    }
    
    private boolean isInterrupt()
    {
        return owner.isCommand();
    }

    private void publishString(String str)
    {
        owner.publish(ThreadMessage.MSG_STRING, str);
    }
    
    private void publishDialog(String str)
    {
        owner.publish(ThreadMessage.MSG_DIALOG, str);
    }
    
    private void publishEngine(String str)
    {
        owner.publish(ThreadMessage.MSG_ENGINE, str);
    }
    
    // merge cells according to symmetry, subperiod, and if cells are frozen
    // merging is done through 'cellRepresentative' array
    // at the end, each cell points to one representative
    // all cells are merged, including set cells and "hard unchecked"
    // but when merging, empty cells are preferred as a representative
    // and soft unchecked go second
    // this ensures the 'best available' cell is the representative

    private void mergeCells()
    {
        int i = cellCount;
        // initialize representatives - each cell points to itself
        while (i > 0)
        {
            --i;
            representative[i] = i;
        }

        // now merge cells depending on period, symmetry, and if it is frozen
        int x;
        int g;
        int y = properties.getRows();
        while (y > 0)
        {
            --y;
            x = properties.getColumns();
            while (x > 0)
            {
                --x;
                int period = properties.getPeriod(CellStack.getPeriodIndex(getStackValue(x, y)));
                g = properties.getGenerations();
                while (g > 0)
                {
                    --g;
                    int cptr = getCellPtr(x, y, g);
                    if (g >= period)
                    {
                        mergeCells(cptr, getCellPtr(x, y, g - period));
                    }
                    Point [] symms = getSymmetricStacks(x, y);
                    i = symms.length;
                    while (i > 0)
                    {
                        --i;
                        mergeCells(cptr, getCellPtr(symms[i].x, symms[i].y, g));
                    }
                    if (Cell.isFrozen(cells[cptr]))
                    {
                        int cptr1 = getCellPtrEx(x, y, g - 1);
                        if (-1 != cptr1)
                        {
                            mergeCells(cptr, cptr1);
                        }
                    }
                }
            }
        }
        // now pass the array once again to ensure that all pointers are of length 1
        i = cellCount;
        while (i > 0)
        {
            --i;
            getRepresentative(i);
        }
    }

    // merge two cells
    // while checking precedence
    private void mergeCells(int cptr1, int cptr2)
    {
        cptr1 = getRepresentative(cptr1);
        cptr2 = getRepresentative(cptr2);
        if (cptr1 != cptr2)
        {
            byte cval1 = cells[cptr1];
            // when merging, prefer empty cells over those that are set
            // and prefer checked cells over unchecked
            if (Cell.isUnset(cval1) || !Cell.isEmpty(cval1))
            {
                // cell 1 is not suitable, use cell2
                representative[cptr1] = cptr2;
                return;
            }
            byte cval2 = cells[cptr2];
            if (Cell.isUnset(cval2) || !Cell.isEmpty(cval2))
            {
                // cell 2 is not suitable, use cell1
                representative[cptr2] = cptr1;
                return;
            }
            // both cells are good but one might be better
            if (Cell.isUnchecked(cval1))
            {
                // use cell 2, maybe it isn't unchecked
                representative[cptr1] = cptr2;
            }
            else
            {
                // cell 1 is fine
                representative[cptr2] = cptr1;
            }
        }
    }

    // find representative of a cell
    // traverse and shorten the recursion if necessary

    private int getRepresentative(int cptr)
    {
        int cpx = representative[cptr];
        if (cpx != representative[cpx]) // is cpx the real representative?
        {
            cpx = getRepresentative(cpx);
            representative[cptr] = cpx;
        }
        return cpx;
    }

    // try all variables in ON and OFF state
    // set all that can only be set one way
    // returns false if error occurred (variables cannot be set either way) 

    private boolean trainVariables(boolean watchInterrupt) throws SearchThreadInterruptedException
    {
        boolean success = true;
        boolean change = true;
        while (change && success)
        {
            change = false;
            int i = variableCount;
            while (i > 0)
            {
                --i;
                if (watchInterrupt)
                {   
                    if (i % 10000 == 0) checkInterrupt();
                }
                
                if (searchEngine.isVariableUnset(i))
                {
                    if (!searchEngine.trainVariable(i))
                    {
                        success = false;
                        // now mark all occurrences of this variable with error
                        int j = cellCount;
                        while (j > 0)
                        {
                            --j;
                            if (representative[j] == i)
                            {
                                cells[j] = Cell.setError(cells[j]);
                                int sptr = getStackPtr(j);
                                stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                            }
                        }
                    }
                    else if (!searchEngine.isVariableUnset(i))
                    {
                        change = true;
                    }
                }
            }
        }
        return success;
    }

    // prepare a full search
    // stops if errors found
    private boolean prepareSearch() throws SearchThreadInterruptedException
    {
        publishString("Processing...");
        // instantiate search engine
        if (!prepareSearchEngine())
        {
            errorType = ERROR_NO_ENGINE;
            return false;
        }
        // and go processing
        mergeCells();
        checkInterrupt();
        countVariables();
        checkInterrupt();
        assignVariables();
        checkInterrupt();
        searchEngine.reset(properties.getRuleBirth(), properties.getRuleSurvival(), variableCount);
        boolean success = prepareConstraints(true);
        publishVariables();
        return success;
    }

    // setup of the searchEngine variable
    // we only have JAVA engine now so it's easy
    // later we may perhaps get other engines, too?

    private boolean prepareSearchEngine()
    {
        if (null == searchEngine)
        {
            searchEngine = new JavaSearchEngine();
            /*
            try 
            {
                searchEngine = new NativeSearchEngine(SEARCH_CELLS_MAX);
            }
            catch (Throwable t)
            {
                searchEngine = new JavaSearchEngine(SEARCH_CELLS_MAX);
            }
            */
            publishEngine(searchEngine.getName());
        }
        return true;
    }
    
    // set representative to -1 for all cells that are not changeable (ON, OFF, HARD UNCHECKED)
    // note that due to the merging method, sets containing at least one changeable
    // cell have such cell as a representative
    // and all chains have length 1 (cell points to itself or directly to the representative)
    // at the same time, determine the number of variables

    private void countVariables()
    {
        int i = cellCount;
        variableCount = 0;
        while (i > 0)
        {
            --i;
            byte cval = cells[i];
            if (Cell.isUnset(cval) || !Cell.isEmpty(cval))
            {
                // not changeable cell
                representative[i] = -1;
            }
            else if (representative[i] == i)
            {
                // this cell points to itself
                ++variableCount;
            }
        }
    }

    // changes the representative [] array so that each cell contains index of
    // its assigned search cell
    private void assignVariables()
    {
        // done in two steps
        // at the beginning, the representative[] contains either -1 (for cells which do not have a variable)
        // or positive integer (incl. 0) of the cell's representative
        // all chains to the representative are of length 1 (cell either points to itself ot to the representative)
        // now we'll use values -2 and lower to assign cells variable index
        // with -2 being variable 0, -3 variable 1 etc
        
        // in second step all variable indexes will be changes back to positive indexes

        int varIndex = -2; //  -2 means first variable will be 0 
        int i = cellCount;
        while (i > 0)
        {
            --i;
            // get the representative of the current cell
            int repre = representative[i];
            // check if it is pointing anywhere (non-negative)
            // pointing nowhere (-1)
            // or already assigned representative
            if (repre >= 0)
            {
                // definitely pointing at a representative
                // let's work with the representative
                int repix = representative[repre];
                if (repix >= 0)
                {
                    // this representative doesn't have its variable index assigned yet
                    // sanity check: it should point to itself
                    if (repix != repre)
                    {
                        throw new RuntimeException("Representative cell does not point to itself");
                    }
                    repix = varIndex;
                    --varIndex;
                    // assign the new index to the representative
                    representative[repre] = repix;
                }
                // assign the index to the cell
                representative[i] = repix;
            }
        }
        
        // now convert all numbers -2 and below to 0 and above appropriately
        // to get real variable indexes
        i = cellCount;
        while (i > 0)
        {
            --i;
            int index = representative[i];
            if (-1 != index)
            {
                representative[i] = -index - 2; // change -2 to 0, -3 to 1, ...
                // funny fact: it would work even if we didn't check it's not -1
                // but I think it's cleaner this way
            }
        }
    }

    private boolean prepareConstraints(boolean watchInterrupt) throws SearchThreadInterruptedException
    {
        if (!prepareCones(watchInterrupt))
        {
            errorType = ERROR_IN_RULES;
            return false;
        }
        if (watchInterrupt)
        {
            checkInterrupt();
        }
        if (!prepareGenZeroConstraint(watchInterrupt))
        {
            errorType = ERROR_IN_G0_CONSTRAINT;
            return false;
        }
        if (watchInterrupt)
        {
            checkInterrupt();
        }
        if (!prepareLiveCellConstraints(watchInterrupt))
        {
            errorType = ERROR_IN_LIVE_LAYERS;
            return false;
        }
        if (watchInterrupt)
        {
            checkInterrupt();
        }
        if (!prepareActiveCellConstraints(watchInterrupt))
        {
            errorType = ERROR_IN_ACTIVE_LAYERS;
            return false;
        }
        if (watchInterrupt)
        {
            checkInterrupt();
        }
        if (!trainVariables(watchInterrupt))
        {
            errorType = ERROR_IN_TRAINING;
            return false;
        }
        return true;
    }
    
    // add constraints limiting live or active cells in each layer
    // live cell is live in any generation
    // active is sometimes on, sometimes off
    // the constraint may watch either or both, based on configuration
    
    private boolean prepareActiveCellConstraints(boolean watchInterrupt) throws SearchThreadInterruptedException
    {
        if (!searchOptions.isLayersActiveCellConstraint() || (searchOptions.getLayersActiveCells() <= 0))
        {
            return true;
        }
        return prepareLayerConstraints(watchInterrupt, true, searchOptions.getLayersActiveCells(), searchOptions.isLayersActiveCellsVarsOnly());
    }

    private boolean prepareLiveCellConstraints(boolean watchInterrupt) throws SearchThreadInterruptedException
    {
        if (!searchOptions.isLayersLiveCellConstraint() || (searchOptions.getLayersLiveCells() <= 0))
        {
            return true;
        }
        return prepareLayerConstraints(watchInterrupt, false, searchOptions.getLayersLiveCells(), searchOptions.isLayersLiveCellsVarsOnly());
    }

    private boolean prepareLayerConstraints(boolean watchInterrupt, boolean active, int limit, boolean varsOnly) throws SearchThreadInterruptedException
    {
        if (limit <= 0)
        {
            // ignore if the constraint is unreasonable
            return true;
        }
        
        // create list of all layers
        // for each layer list of [x,y] coordinates of columns in that layer
        
        int minLayer = CellStack.getMinLayerNo(searchOptions, properties);
        int numLayers = CellStack.getMaxLayerNo(searchOptions, properties) + 1 - minLayer;
        
        // create empty list
        Point[][] layers = new Point[numLayers][];
        int[] column = new int[properties.getGenerations()];
        // add empty list of coordinates to each layer record
        // no problem it's the same for all, it will not be used
        Arrays.fill(layers, new Point[0]);
        
        // now go through all columns in the array and put their coordinates to appropriate layer records
        int y;
        int x = properties.getColumns();
        while (x > 0)
        {
            --x;
            if (watchInterrupt)
            {
                checkInterrupt();
            }
            y = properties.getRows();
            while (y > 0)
            {
                --y;
                int layer = CellStack.getLayerNo(searchOptions, x, y) - minLayer;
                int len = layers[layer].length;
                layers[layer] = Arrays.copyOf(layers[layer], len + 1);
                layers[layer][len] = new Point(x, y);
            }
        }
        
        // now go through layers and process them one by one
        boolean success = true;
        int layerNo = numLayers;
        while (layerNo > 0)
        {
            --layerNo;
            if (watchInterrupt)
            {
                checkInterrupt();
            }
            // get the list of points in this layer
            Point[] points = layers[layerNo];
            int layerLimit = limit;
            
            // convert this list of points into list of variables
            int[][] varList = new int[points.length][];
            boolean[] columnOn = new boolean[points.length];
            boolean[] columnOff = new boolean[points.length];
            int varListUsed = 0;
            int pointNo = points.length;
            while (pointNo > 0)
            {
                --pointNo;
                if (watchInterrupt)
                {
                    checkInterrupt();
                }
                Point point = points[pointNo];
                int g = properties.getGenerations();
                // count on and off cells and variables in this column
                int onCells = 0;
                int onVars = 0;
                int offCells = 0;
                int offVars = 0;
                int unsetVars = 0;
                while (g > 0)
                {
                    --g;
                    int cptr = getCellPtr(point.x, point.y, g);
                    if (representative[cptr] != -1)
                    {
                        if (!Cell.isUnchecked(cells[cptr]))
                        {
                            if (searchEngine.isVariableOn(representative[cptr]))
                            {
                                ++onVars;
                            }
                            else if (searchEngine.isVariableOff(representative[cptr]))
                            {
                                ++offVars;
                            }
                            else
                            {
                                column[unsetVars] = representative[cptr];
                                ++unsetVars;
                            }
                        }
                    }
                    else if (Cell.isOn(cells[cptr]))
                    {
                        ++onCells;
                    }
                    else if (Cell.isOff(cells[cptr]))
                    {
                        ++offCells;
                    }
                }
                
                if (varsOnly)
                {
                    onCells = onVars;
                    offCells = offVars;
                }
                else
                {
                    onCells += onVars;
                    offCells += offVars;
                }

                if (active)
                {
                    // looking for active columns, i.e. mix of ON and OFF cells
                    if ((onCells > 0) && (offCells > 0))
                    {
                        unsetVars = 0; // we don't need to care about variables in this column, it is already active
                        --layerLimit; // and decrease the limit for this layer
                    }
                }
                else
                {
                    // looking for live columns, i.e. at least one ON cell
                    if (onCells > 0)
                    {
                        unsetVars = 0; // we don't need to care about variables in this column, it is already live
                        --layerLimit; // and decrease the limit for this layer
                    }
                }

                if (unsetVars > 0)
                {
                    // sort and remove duplicates
                    Arrays.sort(column, 0, unsetVars - 1);
                    int p1 = 1;
                    int p2 = unsetVars;
                    unsetVars = 1;
                    while (p1 < p2)
                    {
                        if (column[p1] != column[p1 - 1])
                        {
                            column[unsetVars] = column[p1];
                            ++unsetVars;
                        }
                        ++p1;
                    }
                    
                    // second check for vars number - if it is active constraint
                    // and there are no off or on cells
                    // then number of variables must be at least two
                    // otherwise the column cannot become active
                    
                    // so... well
                    // to register it
                    // it must be not active
                    // or at least two variables
                    // or some on or off cells
                    
                    if (!active || (unsetVars > 1) || (onCells + offCells > 0))
                    {
                        // always make a copy - the original column[] is reused
                        varList[varListUsed] = Arrays.copyOf(column, p2);
                        columnOn[varListUsed] = onCells > 0;
                        columnOff[varListUsed] = offCells > 0;
                        ++varListUsed;
                    }
                }
            }
            
            // shrink arrays if needed
            if (varListUsed < varList.length)
            {
                varList = Arrays.copyOf(varList, varListUsed);
                columnOn = Arrays.copyOf(columnOn, varListUsed);
                columnOff = Arrays.copyOf(columnOff, varListUsed);
            }
            
            // check if it is reasonable to try adding the constraint
            
            boolean successNow = layerLimit >= 0;
            
            if (successNow)
            {
                if (limit < varList.length)
                {
                    if (active)
                    {
                        successNow = searchEngine.addActiveCellConstraint(layerLimit, varList, columnOn, columnOff);
                    }
                    else
                    {
                        successNow = searchEngine.addLiveCellConstraint(layerLimit, varList);
                    }
                }
            }
            // merge the result with the rest
            success = success && successNow;
            // if we failed for this layer, mark appropriate cells
            // if varsOnly, mark variables only, otherwise mark normal cells too
            // if active, mark all set cells
            // if not active, mark ON cells only
            if (!successNow)
            {
                pointNo = points.length;
                while (pointNo > 0)
                {
                    --pointNo;
                    Point point = points[pointNo];
                    // mark any variables on these coordinates with error
                    int g = properties.getGenerations();
                    while (g > 0)
                    {
                        --g;
                        int cptr = getCellPtr(point.x, point.y, g);

                        if (representative[cptr] != -1)
                        {
                            if (!Cell.isUnchecked(cells[cptr]))
                            {
                                if (searchEngine.isVariableOn(representative[cptr]) || (searchEngine.isVariableOff(representative[cptr]) && !active))
                                {
                                    cells[cptr] = Cell.setError(cells[cptr]);
                                    int sptr = getStackPtr(cptr);
                                    stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                                }
                            }
                        }
                        else if (!varsOnly && (Cell.isOn(cells[cptr]) || (Cell.isOff(cells[cptr]) && !active)))
                        {
                            cells[cptr] = Cell.setError(cells[cptr]);
                            int sptr = getStackPtr(cptr);
                            stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                        }
                    }
                }
            }
        }
        return success;
    }

    // add the constraint limiting number of ON cells in generation zero
    
    private boolean prepareGenZeroConstraint(boolean watchInterrupt) throws SearchThreadInterruptedException
    {
        if (!searchOptions.isLimitGenZero())
        {
            return true;
        }
        int x;
        int y;
        int cptr;
        
        int limit = searchOptions.getLimitGenZeroCells();

        if (limit <= 0)
        {
            // ignore the constraint if it doesn't make sense
            return true;
        }
        
        // first count cells
        int varCount = 0;
        y = properties.getRows();
        while (y > 0)
        {
            --y;
            if (watchInterrupt)
            {
                checkInterrupt();
            }
            x = properties.getColumns();
            while (x > 0)
            {
                --x;
                // top cell
                cptr = getCellPtr(x, y, 0);

                if (representative[cptr] != -1)
                {
                    if (!Cell.isUnchecked(cells[cptr]))
                    {
                        // skip soft unchecked cells
                        if (searchEngine.isVariableOn(representative[cptr]))
                        {
                            --limit;
                        }
                        else if (searchEngine.isVariableUnset(representative[cptr]))
                        {
                            ++varCount;
                        }
                    }
                }
                else if (!searchOptions.isLimitGenZeroVarsOnly() && Cell.isOn(cells[cptr]))
                {
                    --limit;
                }
            }
        }
        
        if (limit >= varCount)
        {
            // no need to make a constraint if it cannot be broken
            return true;
        }
        
        if (limit >= 0)
        {
            int[] varList = new int[varCount];
            
            y = properties.getRows();
            while (y > 0)
            {
                --y;
                if (watchInterrupt)
                {
                    checkInterrupt();
                }
                x = properties.getColumns();
                while (x > 0)
                {
                    --x;
                    // top cell
                    cptr = getCellPtr(x, y, 0);

                    if (representative[cptr] != -1)
                    {
                        if (!Cell.isUnchecked(cells[cptr]))
                        {
                            if (searchEngine.isVariableUnset(representative[cptr]))
                            {
                                --varCount;
                                varList[varCount] = representative[cptr];
                            }
                        }
                    }
                }
            }

            if (searchEngine.addGenZeroConstraint(limit, varList))
            {
                return true;
            }
        }
        
        // failed
        // let's mark all generation 0 ON variables or cells (if it's not vars only)

        y = properties.getRows();
        while (y > 0)
        {
            --y;
            if (watchInterrupt)
            {
                checkInterrupt();
            }
            x = properties.getColumns();
            while (x > 0)
            {
                --x;
                // top cell
                cptr = getCellPtr(x, y, 0);

                if (representative[cptr] != -1)
                {
                    if (!Cell.isUnchecked(cells[cptr]))
                    {
                        // skip soft unchecked cells
                        if (searchEngine.isVariableOn(representative[cptr]))
                        {
                            cells[cptr] = Cell.setError(cells[cptr]);
                            int sptr = getStackPtr(cptr);
                            stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                        }
                    }
                }
                else if (!searchOptions.isLimitGenZeroVarsOnly() && Cell.isOn(cells[cptr]))
                {
                    cells[cptr] = Cell.setError(cells[cptr]);
                    int sptr = getStackPtr(cptr);
                    stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                }
            }
        }
        return false;
    }
    
    
    // add all "cones" to the search engine
    // mark top cell with ERROR flag if it fails
    // set the errorsFound flag if the failed cone contains a variable
    
    private boolean prepareCones(boolean watchInterrupt) throws SearchThreadInterruptedException
    {
        boolean success = true;
        int xmin;
        int xmax;
        int ymin;
        int ymax;
        
        if (properties.isOuterSpaceUnset())
        {
            xmin = 0;
            xmax = properties.getColumns() - 1;
            ymin = 0;
            ymax = properties.getRows() - 1;
        }
        else
        {
            xmin = -1;
            xmax = properties.getColumns();
            ymin = -1;
            ymax = properties.getRows();
        }
        
        // first make cones for all internal cells except generation 0
        
        int g = 1;
        while(g < properties.getGenerations())
        {
            success = prepareConesFor(g, xmin, xmax, ymin, ymax, success, watchInterrupt);
            ++g;
        }

        if (properties.isTileGen())
        {
            // temporal tiling (periodic) is on, so cover last generation plus 1
            success = prepareConesFor(properties.getGenerations(), xmin, xmax, ymin, ymax, success, watchInterrupt);
            if ((properties.getTileGenShiftDown() != 0) || (properties.getTileGenShiftRight() != 0))
            {
                // if there is any shift, also cover generation 0 predecessors to cover whatever was not covered by last generation
                success = prepareConesFor(0, 0, properties.getColumns() - 1, 0, properties.getRows() - 1, success, watchInterrupt);
            }
        }

        return success;
    }    
        
    private boolean prepareConesFor(int g, int xmin, int xmax, int ymin, int ymax, boolean success, boolean watchInterrupt) throws SearchThreadInterruptedException
    {   
        int x;
        int y;
        int cptr;

        y = ymax + 1;
        while (y > ymin)
        {
            --y;
            if (watchInterrupt)
            {
                checkInterrupt();
            }
            x = xmax + 1;
            while (x > xmin)
            {
                
                --x;
                // top cell
                cptr = getCellPtrEx(x, y, g);

                byte cellValue = getCellValueEx(cptr, g);
                // there's no reason to investigate it if top cell is hard unchecked
                // because all combinations of the bottom are then allright
                if (!Cell.isUnset(cellValue))
                {
                    // top cell
                    boolean topOn = Cell.isOn(cellValue);
                    boolean topOff = Cell.isOff(cellValue);
                    int topVar;
                    if (cptr != -1)
                    {
                        topVar = representative[cptr];
                    }
                    else
                    {
                        topVar = -1;
                    }
                    
                    // middle cell
                    boolean midOn = false;
                    boolean midOff = false;
                    int midVar = -1;
                    int gm1 = g - 1;
                    cptr = getCellPtrEx(x, y, gm1);
                    cellValue = getCellValueEx(cptr, gm1);
                    if ((-1 != cptr) && (representative[cptr] != -1))
                    {
                        midVar = representative[cptr];
                    }
                    else if (Cell.isOn(cellValue))
                    {
                        midOn = true;
                    }
                    else if (Cell.isOff(cellValue))
                    {
                        midOff = true;
                    }

                    // neighbor cells
                    int neighOn = 0;;
                    int neighOff = 0;
                    int vars = 0;
                    int[] varList = new int[8];
                    // first neighbor
                    cptr = getCellPtrEx(x - 1, y, gm1);
                    cellValue = getCellValueEx(cptr, gm1);
                    if ((-1 != cptr) && (representative[cptr] != -1))
                    {
                        varList[vars] = representative[cptr];
                        ++vars;
                    }
                    else if (Cell.isOn(cellValue))
                    {
                        ++neighOn;
                    }
                    else if (Cell.isOff(cellValue))
                    {
                        ++neighOff;
                    }
                    // second neighbor
                    cptr = getCellPtrEx(x, y - 1, gm1);
                    cellValue = getCellValueEx(cptr, gm1);
                    if ((-1 != cptr) && (representative[cptr] != -1))
                    {
                        varList[vars] = representative[cptr];
                        ++vars;
                    }
                    else if (Cell.isOn(cellValue))
                    {
                        ++neighOn;
                    }
                    else if (Cell.isOff(cellValue))
                    {
                        ++neighOff;
                    }
                    // third neighbor
                    cptr = getCellPtrEx(x + 1, y, gm1);
                    cellValue = getCellValueEx(cptr, gm1);
                    if ((-1 != cptr) && (representative[cptr] != -1))
                    {
                        varList[vars] = representative[cptr];
                        ++vars;
                    }
                    else if (Cell.isOn(cellValue))
                    {
                        ++neighOn;
                    }
                    else if (Cell.isOff(cellValue))
                    {
                        ++neighOff;
                    }
                    // fourth neighbor
                    cptr = getCellPtrEx(x, y + 1, gm1);
                    cellValue = getCellValueEx(cptr, gm1);
                    if ((-1 != cptr) && (representative[cptr] != -1))
                    {
                        varList[vars] = representative[cptr];
                        ++vars;
                    }
                    else if (Cell.isOn(cellValue))
                    {
                        ++neighOn;
                    }
                    else if (Cell.isOff(cellValue))
                    {
                        ++neighOff;
                    }
                    // fifth neighbor
                    cptr = getCellPtrEx(x - 1, y - 1, gm1);
                    cellValue = getCellValueEx(cptr, gm1);
                    if ((-1 != cptr) && (representative[cptr] != -1))
                    {
                        varList[vars] = representative[cptr];
                        ++vars;
                    }
                    else if (Cell.isOn(cellValue))
                    {
                        ++neighOn;
                    }
                    else if (Cell.isOff(cellValue))
                    {
                        ++neighOff;
                    }
                    // sixth neighbor
                    cptr = getCellPtrEx(x - 1, y + 1, gm1);
                    cellValue = getCellValueEx(cptr, gm1);
                    if ((-1 != cptr) && (representative[cptr] != -1))
                    {
                        varList[vars] = representative[cptr];
                        ++vars;
                    }
                    else if (Cell.isOn(cellValue))
                    {
                        ++neighOn;
                    }
                    else if (Cell.isOff(cellValue))
                    {
                        ++neighOff;
                    }
                    // seventh neighbor
                    cptr = getCellPtrEx(x + 1, y - 1, gm1);
                    cellValue = getCellValueEx(cptr, gm1);
                    if ((-1 != cptr) && (representative[cptr] != -1))
                    {
                        varList[vars] = representative[cptr];
                        ++vars;
                    }
                    else if (Cell.isOn(cellValue))
                    {
                        ++neighOn;
                    }
                    else if (Cell.isOff(cellValue))
                    {
                        ++neighOff;
                    }
                    // eighth neighbor
                    cptr = getCellPtrEx(x + 1, y + 1, gm1);
                    cellValue = getCellValueEx(cptr, gm1);
                    if ((-1 != cptr) && (representative[cptr] != -1))
                    {
                        varList[vars] = representative[cptr];
                        ++vars;
                    }
                    else if (Cell.isOn(cellValue))
                    {
                        ++neighOn;
                    }
                    else if (Cell.isOff(cellValue))
                    {
                        ++neighOff;
                    }
                    
                    if (vars < varList.length)
                    {
                        varList = Arrays.copyOf(varList, vars);
                    }
                    
                    if (!searchEngine.addCone(topOn, topOff, topVar, midOn, midOff, midVar, neighOn, neighOff, varList))
                    {
                        if ((x < 0) || (y < 0) || (x >= properties.getColumns()) || (y >= properties.getRows()))
                        {
                            int xerr = x;
                            int yerr = y;
                            while (xerr < 0)
                            {
                                ++xerr;
                            }
                            while (xerr >= properties.getColumns())
                            {
                                --xerr;
                            }
                            while (yerr < 0)
                            {
                                ++yerr;
                            }
                            while (yerr >= properties.getRows())
                            {
                                --yerr;
                            }
                            cptr = getCellPtrEx(xerr, yerr, g - 1);
                        }
                        else
                        {
                            cptr = getCellPtrEx(x, y, g);
                        }
                        cells[cptr] = Cell.setError(cells[cptr]);
                        int sptr = getStackPtr(cptr);
                        stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                        
                        // regard it as an error if there is at least one variable involved
                        if ((topVar != -1) || (midVar != -1) || (varList.length > 0))
                        {
                            success = false;
                        }
                    }
                }
            }
        }
        return success;
    }

    // returns value of the cell
    // if the cell is not inside the field, calculates appropriate value
    // according to settings
    
    public byte getCellValueEx(int cptr, int gen)
    {
        if (cptr != -1)
        {
            return cells[cptr];
        }
        if (properties.isOuterSpaceUnset())
        {
            // unset
            return Cell.CELL_UNSET;
        }
        else if (properties.getRuleBirth(0))
        {
            if (properties.getRuleSurvival(8))
            {
                // rule-based, B0 and S8 -> ON 
                return Cell.CELL_ON;
            }
            else if ((gen & 1) == 0)
            {
                // rule-based, B0 and not S8 -> OFF in even generations
                return Cell.CELL_OFF;
            }
            else
            {
                // rule-based, B0 and not S8 -> ON in odd generations
                return Cell.CELL_ON;
            }
        }
        else
        {
            // rule-based, not B0 -> OFF
            return Cell.CELL_OFF;
        }
    }

    // Make variable contents visible
    
    public void publishVariables()
    {
        // populate the search cell's states to the main array
        int i = cellCount;
        while (i > 0)
        {
            --i;
            int varidx = representative[i];
            if (varidx != -1)
            {
                byte cval = cells[i];
                if (searchEngine.isVariableOn(varidx))
                {
                    if (!Cell.isOn(cval))
                    {
                        cells[i] = Cell.setState(cval,Cell.STATE_ON);
                        int sptr = getStackPtr(i);
                        stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                    }
                }
                else if (searchEngine.isVariableOff(varidx))
                {
                    if (!Cell.isOff(cval))
                    {
                        cells[i] = Cell.setState(cval,Cell.STATE_OFF);
                        int sptr = getStackPtr(i);
                        stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                    }
                }
                else
                {
                    if (!Cell.isEmpty(cval))
                    {
                        cells[i] = Cell.setState(cval,Cell.STATE_EMPTY);
                        int sptr = getStackPtr(i);
                        stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                    }
                }
            }
        }
    }


    public void publishCombination()
    {
        // populate the variable combination states to the main array
        int i = cellCount;
        while (i > 0)
        {
            --i;
            int varIdx = representative[i];
            if (varIdx != -1)
            {
                byte cVal = cells[i];
                if (searchEngine.isCombinationOn(varIdx))
                {
                    if (!Cell.isOn(cVal))
                    {
                        cells[i] = Cell.setState(cVal, Cell.STATE_ON);
                        int sptr = getStackPtr(i);
                        stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                    }
                }
                else if (searchEngine.isCombinationOff(varIdx))
                {
                    if (!Cell.isOff(cVal))
                    {
                        cells[i] = Cell.setState(cVal, Cell.STATE_OFF);
                        int sptr = getStackPtr(i);
                        stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                    }
                }
                else
                {
                    if (!Cell.isEmpty(cVal))
                    {
                        cells[i] = Cell.setState(cVal, Cell.STATE_EMPTY);
                        int sptr = getStackPtr(i);
                        stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                    }
                }
            }
        }
    }

    // this  determines which variables will be searched (excluding "soft unchecked" cells which also have their variables)
    // doesn't sort them
    // fills searchedVariables[] with indexes of variables which will be searched

    private void prepareSearchedVariables()
    {
        int [] varSortPosition = new int[variableCount];
        int count = 0;
        // first mark all variables as 'not searched'
        Arrays.fill(varSortPosition, -1);
        // now process the cell array and determine which cells are going to be searched
        int i = cellCount;
        while (i > 0)
        {
            --i;
            int varIdx = representative[i];
            // the cell must be mapped to a variable
            if ((-1 != varIdx)
                    // must be NORMAL/EMPTY
                    && (Cell.getType(cells[i]) == Cell.TYPE_NORMAL)
                    && (Cell.getState(cells[i]) == Cell.STATE_EMPTY)
                    // must not be set
                    && searchEngine.isVariableUnset(varIdx)
                    // must be in at least one constraint
                    && searchEngine.hasVariableConstraints(varIdx)
                    // and must not be among sorted cells yet
                    && (-1 == varSortPosition[varIdx]))
            {
                // all ok, so mark this variable as 'searched' and assign it a position in the list
                varSortPosition[varIdx] = count;
                ++count;
            }
        }
        // now that we know how many cells there are, let's make the array
        searchedVariables = new int[count];
        i = variableCount;
        while (i > 0)
        {
            --i;
            if (varSortPosition[i] != -1)
            {
                searchedVariables[varSortPosition[i]] = i;
            }
        }
    }

    
    // this compares the two cells
    // returns true if cell on cptr1 should be before cell on cptr2
    // otherwise returns false

    private boolean isBetterInSorting(int cptr1, int cptr2)
    {
        // first, reverse-engineer both cell's coordinates
        int cg1 = cptr1 % properties.getGenerations();
        int cg2 = cptr2 % properties.getGenerations();
        if (!searchOptions.isSortGenFirst() && (cg1 != cg2))
        {
            return (cg1 < cg2) == searchOptions.isSortToFuture();
        }
        cptr1 = cptr1 / properties.getGenerations();
        cptr2 = cptr2 / properties.getGenerations();
        if (cptr1 == cptr2)
        {
            // the two cells are on the same [X,Y] coordinates
            // now apply the generations
            if (cg1 == cg2)
            {
                // it's the same cell - by definition it can't lie ahead of itself
                return false;
            }
            return (cg1 < cg2) == searchOptions.isSortToFuture();
        }
        else
        {
            // they are at different coordinates
            // let's calculate these coordinates relative to start of sorting
            
            int cx1 = (cptr1 % properties.getColumns()) - searchOptions.getSortStartColumn();
            int cy1 = (cptr1 / properties.getColumns()) - searchOptions.getSortStartRow();
            int cx2 = (cptr2 % properties.getColumns()) - searchOptions.getSortStartColumn();
            int cy2 = (cptr2 / properties.getColumns()) - searchOptions.getSortStartRow();

            // first compare sorting distance
            int d1 = getSortDistance(cx1, cy1);
            int d2 = getSortDistance(cx2, cy2);
            
            if (d1 != d2)
            {
                return (d1 < d2)  != searchOptions.isSortReverse();
            }

            // now absolute distance from the origin

            d1 = cx1 * cx1 + cy1 * cy1;
            d2 = cx2 * cx2 + cy2 * cy2;
            
            if (d1 != d2)
            {
                return (d1 < d2)  != searchOptions.isSortReverse();
            }
            
            // let's just put one ahead of the other deterministically
            
            if (cx1 != cx2)
            {
                return (cx1 < cx2)  != searchOptions.isSortReverse();
            }
            
            // they are at different coordinates
            // so if X coordinate the same (test above), the Y coordinate is different
            
            return (cy1 < cy2)  != searchOptions.isSortReverse();
        }
    }

    // for aboslute cell position [X,Y] return absolute sorting distance from 
    // the sorting origin
    
    private int getSortDistance(int x, int y)
    {
        switch(searchOptions.getSortType())
        {
        case SearchOptions.SORT_HORZ:
            return Math.abs(x);
        case SearchOptions.SORT_VERT:
            return Math.abs(y);
        case SearchOptions.SORT_BOX:
            return Math.max(Math.abs(x), Math.abs(y)); 
        case SearchOptions.SORT_DIAG_FWD:
            return Math.abs(x + y);
        case SearchOptions.SORT_DIAG_BWD:
            return Math.abs(x - y);
        case SearchOptions.SORT_DIAMOND:
            return Math.max(Math.abs(x + y), Math.abs(x - y));
        case SearchOptions.SORT_CIRCLE:
            return x * x + y * y;
        default:
            throw new RuntimeException("Trying to sort with unsupported sorting type " + searchOptions.getSortType());
        }
    }

    // sort searched variables according to settings
    
    private void sortSearchedVariables()
    {
        // prepare a reference array
        // for each sorted variable it will contain
        // reference to its earliest appearance between cells
        int[] varRepre = new int[variableCount];
        // first fill it with -1s to indicate no representative
        Arrays.fill(varRepre, -1);
        // now pass through searched variables and mark them there
        int i = searchedVariables.length;
        while (i > 0)
        {
            --i;
            varRepre[searchedVariables[i]] = -2;
        }
        
        // now go through cells and find best representant for each variable
        
        i = cellCount;
        while (i > 0)
        {
            --i;
            int varIdx = representative[i];
            if (varIdx != -1)
            {
                // varIdx is index of the variable
                if (varRepre[varIdx] != -1)
                {
                    // it is a searched variable
                    // either it's -2 or it already points to some cell
                    if ((varRepre[varIdx] == -2) || isBetterInSorting(i, varRepre[varIdx]))
                    {
                        // the new one is better
                        varRepre[varIdx] = i;
                    }
                }
            }
        }

        quickSortVariables(0, searchedVariables.length - 1, varRepre);
    }

    // recursive part of the sorting
    private void quickSortVariables(int first, int last, int[] varRepre)
    {
        // use cycle to get rid of most of the recursion
        while (true)
        {
            if (first + 1 == last)
            {
                if (isBetterInSorting(varRepre[searchedVariables[last]], varRepre[searchedVariables[first]]))
                {
                    int t = searchedVariables[first];
                    searchedVariables[first] = searchedVariables[last];
                    searchedVariables[last] = t;
                }
                return;
            }
            else if (first >= last)
            {
                // nothing left to sort
                return;
            }
            else
            {
                // select pivot and move it out of the way
                int index = (first + last) / 2;
                int pivot = searchedVariables[index]; // pivot contains value from the array, not index to the array!
                searchedVariables[index] = searchedVariables[last];
                // initialize pointers
                index = last;
                int store = last;
                // isBetter(x,y) = true means x < y
                // isBetter(x,y) = false means x >= y
                
                while (index > first)
                {
                    --index;
                    if (!isBetterInSorting(varRepre[searchedVariables[index]], varRepre[pivot]))
                    {
                        --store;
                        int t = searchedVariables[index];
                        searchedVariables[index] = searchedVariables[store];
                        searchedVariables[store] = t;
                    }
                }
                // move pivot in between
                searchedVariables[last] = searchedVariables[store];
                searchedVariables[store] = pivot;
                // walk finished, finish recursively
                if ((store - first) < (last - store))
                {
                    // do smaller part recursively, bigger in cycle
                    quickSortVariables(first, store - 1, varRepre);
                    first = store + 1;
                }
                else
                {
                    // do smaller part recursively, bigger in cycle
                    quickSortVariables(store + 1, last, varRepre);
                    last = store - 1;
                }
            }
        }
    }


    private int fullSearch() throws SearchThreadInterruptedException 
    {
        if (optionsChanged)
        {
            // update sorting
            sortSearchedVariables();
            searchEngine.setSearchOrder(searchedVariables);
            // only set this if there is already some combination found
            searchEngine.setSearchPruning(searchOptions.isPruneWithCombination() && (solutionsFound > 0));
            optionsChanged = false;
        }
        // check if search ended
        if ((iterationsDone != 0) && searchEngine.isEmptyStack())
        {
            // search ended, display combination and return
            publishCombination();
            publishResultDialog();
            publishString("Search finished (" + solutionsFound + " / " + iterationsDone + ")");
            stopTimerTasks();
            return ThreadMessage.MSG_DONE;
        }
        // start timer tasks if they're not running already
        if (searchOptions.isPauseEachIteration())
        {
            stopTimerTasks();
        }
        else
        {
            startTimerTasks();
        }
        publishString("Searching (" + solutionsFound + " / " + iterationsDone + ")...");
        long preIterTime = System.nanoTime();
        int its;
        while (true)
        {
            // first check if timers did not deliver some action while we were doing something different

            // check if we should just display
            if (timeToDisplay)
            {
                timeToDisplay = false;
                publishVariables();
                publishString("Searching (" + solutionsFound + " / " + iterationsDone + ")...");
                return ThreadMessage.MSG_DISPLAY;
            }
            // and check for saving status
            if (timeToSave)
            {
                timeToSave = false;
                publishVariables();
                publishString("Saving...");
                return ThreadMessage.MSG_SAVE;
            }

            its = searchEngine.iterate(!searchOptions.isPauseEachIteration());
            long postIterTime = System.nanoTime();
            iterationsDone += its;
            long iterTime = postIterTime - preIterTime;
            preIterTime = postIterTime;
            timePassedNs += iterTime;
            // adjust the number of iterations
            // check if we found a solution
            if (searchEngine.isSolution())
            {
                if ((!searchOptions.isIgnoreSubperiods()) || solutionNotSubperiod())
                {
                    if (solutionsFound == 0)
                    {
                        searchEngine.processCombination(true);
                        searchEngine.setSearchPruning(searchOptions.isPruneWithCombination());
                    }
                    else
                    {
                        searchEngine.processCombination(false);
                    }
                    ++solutionsFound;
                    boolean published = false;
                    if (searchOptions.isSaveSolutions())
                    {
                        publishVariables();
                        published = true;
                        try
                        {
                            saveSolution();
                        }
                        catch (IOException e)
                        {
                            publishDialog("Error writing solution to file: " + e.toString());
                            publishString("Paused (file error)");
                            stopTimerTasks();
                            return ThreadMessage.MSG_DONE;
                        }
                    }
                    if (searchOptions.isPauseEachSolution())
                    {
                        if (!published)
                        {
                            publishVariables();
                        }
                        publishString("Solution " + solutionsFound + " found");
                        stopTimerTasks();
                        return ThreadMessage.MSG_DONE;
                    }
                }
            }
            // check if search finished
            if (searchEngine.isEmptyStack())
            {
                // search ended, display combination
                publishCombination();
                publishResultDialog();
                publishString("Search finished");
                stopTimerTasks();
                return ThreadMessage.MSG_DONE;
            }
            // check if we should pause
            if (searchOptions.isPauseEachIteration())
            {
                publishVariables();
                publishString("Paused on iteration " + iterationsDone);
                return ThreadMessage.MSG_DONE;
            }
            // yeah, and check for interrupt
            if (isInterrupt())
            {
                publishVariables();
                publishString("Paused (" + solutionsFound + " / " + iterationsDone + ")");
                throw(new SearchThreadInterruptedException());
            }
        }
    }
    
    // returns true if no subperiod generation is equal to generation 0
    private boolean solutionNotSubperiod()
    {
        // compare generation 0 with any generation which divides total generations
        int g = properties.getGenerations() / 2;
        while (g > 0)
        {
            if ((properties.getGenerations() % g) == 0)
            {
                boolean different = false;
                int y = properties.getRows();
                while (y > 0)
                {
                    --y;
                    int x = properties.getColumns();
                    while (x > 0)
                    {
                        --x;
                        int cptr0 = getCellPtr(x, y, 0);
                        int cptrg = getCellPtr(x, y, g);

                        if (representative[cptr0] == -1)
                        {
                            // note: the state when both cells are not variables is not tested
                            // and it is assumed they are the same
                            if (representative[cptrg] != -1)
                            {
                                // generation 0 is not a variable, generation g is a variable
                                switch (Cell.getState(cells[cptr0]))
                                {
                                case Cell.STATE_ON:
                                    // on or unset variable is considered equal
                                    if (searchEngine.isVariableOff(representative[cptrg]))
                                    {
                                        different = true;
                                        x = 0;
                                        y = 0;
                                    }
                                    break;
                                case Cell.STATE_OFF:
                                    // off or unset variable is considered equal
                                    if (searchEngine.isVariableOn(representative[cptrg]))
                                    {
                                        different = true;
                                        x = 0;
                                        y = 0;
                                    }
                                    break;
                                // unset, unchecked or empty cell is equal to anything
                                }
                            }
                        }
                        else
                        {
                            if (representative[cptrg] == -1)
                            {
                                // generation 0 is a variable, generation g isn't
                                switch (Cell.getState(cells[cptrg]))
                                {
                                case Cell.STATE_ON:
                                    // on or unset variable is considered equal
                                    if (searchEngine.isVariableOff(representative[cptr0]))
                                    {
                                        different = true;
                                        x = 0;
                                        y = 0;
                                    }
                                    break;
                                case Cell.STATE_OFF:
                                    // off or unset variable is considered equal
                                    if (searchEngine.isVariableOn(representative[cptr0]))
                                    {
                                        different = true;
                                        x = 0;
                                        y = 0;
                                    }
                                    break;
                                // unset, unchecked or empty cell is equal to anything
                                }
                            }
                            else
                            {
                                // both are variables
                                if (representative[cptr0] != representative[cptrg])
                                {
                                    // they are different variables
                                    // but they count as different only if one is ON and the other is OFF
                                    // if either is unset then they are considered equal
                                    if (searchEngine.isVariableOn(representative[cptr0]))
                                    {
                                        if (searchEngine.isVariableOff(representative[cptrg]))
                                        {
                                            different = true;
                                            x = 0;
                                            y = 0;
                                        }
                                    }
                                    else if (searchEngine.isVariableOff(representative[cptr0]))
                                    {
                                        if (searchEngine.isVariableOn(representative[cptrg]))
                                        {
                                            different = true;
                                            x = 0;
                                            y = 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!different)
                {
                    // two equal generations found, i.e. solution is subperiodic
                    return false;
                }
            }
            --g;
        }
        return true;
    }
    
    private void publishResultDialog()
    {
        long ms = timePassedNs / 1000000L;
        String seconds;
        if (ms <= 1000000)
        {
            seconds = "" + ms;
            while (seconds.length() < 4)
            {
                seconds = "0" + seconds;
            }
            seconds = seconds.substring(0, seconds.length() - 3) + "." + seconds.substring(seconds.length() - 3);
        }
        else
        {
            seconds = "" + (ms / 1000L);
        }
        publishDialog("Search finished: " + solutionsFound + " solutions found,\n"
                + iterationsDone + " iterations in " + seconds + " second(s).");
    }

    private void saveSolution () throws IOException
    {
        FileWriter fout = null;
        BufferedWriter writer = null;
        try 
        {
            fout = new FileWriter(searchOptions.getSolutionFile(), true);
            writer = new BufferedWriter(fout);
            for (int row = 0; row < properties.getRows(); ++row)
            {
                int maxGen = searchOptions.isSaveAllGen() ? properties.getGenerations() : 1;
                for (int gen = 0; gen < maxGen; ++gen)
                {
                    for (int col = 0; col < properties.getColumns(); ++col)
                    {
                        byte val = getCellValue(col, row, gen);
                        writer.write(val == Cell.STATE_ON ? "*" : ".");
                    }
                    if (gen + 1 < maxGen)
                    {
                        for (int i = 0; i < searchOptions.getSolutionSpacing(); ++i)
                        {
                            writer.write(".");
                        }
                    }
                }
                writer.newLine();
            }
            for (int i = 0; i < searchOptions.getSolutionSpacing(); ++i)
            {
                writer.write("..");
                writer.newLine();
            }
            
        }
        finally
        {
            if (null != writer)
            {
                writer.close();
            }
            if (null != fout)
            {
                fout.close();
            }
        }
    }
}
