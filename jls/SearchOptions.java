package jls;

import java.io.IOException;

public class SearchOptions
{
    public static final int SORT_HORZ = 0;
    public static final int SORT_VERT = 1;
    public static final int SORT_DIAG_FWD = 2;
    public static final int SORT_DIAG_BWD = 3;
    public static final int SORT_BOX = 4;
    public static final int SORT_DIAMOND = 5;
    public static final int SORT_CIRCLE = 6;
    public static final String[] SORT_NAMES = { "Horizontal", "Vertical",
            "Diagonal", "Diagonal-Backwards", "Box", "Diamond", "Circle" };
    
    public static final int LAYER_COLUMN = 0;
    public static final int LAYER_ROW = 1;
    public static final int LAYER_DIAG_FWD = 2;
    public static final int LAYER_DIAG_BWD = 3;
    public static final int LAYER_BOX = 4;
    public static final int LAYER_DIAMOND = 5;
    public static final int LAYER_CIRCLE = 6;
    public static final String[] LAYER_NAMES = { "Columns", "Rows",
            "Diagonals", "Back-Diagonals", "Boxes", "Diamonds", "Circles" };
    
    private boolean validated = false;

    private boolean sortGenFirst = true;
    private static final String SORT_GEN_FIRST_NAME = "sort_generations_first";
    private boolean sortToFuture = true;
    private static final String SORT_TO_FUTURE_NAME = "sort_to_future";
    private int sortStartColumn = 0;
    private static final String SORT_START_COLUMN_NAME = "sort_start_column";
    private int sortStartRow = 0;
    private static final String SORT_START_ROW_NAME = "sort_start_row";
    private int sortType = SORT_HORZ;
    private static final String SORT_TYPE_NAME = "sort_type";
    private boolean sortReverse = false;
    private static final String SORT_REVERSE_NAME = "sort_reverse";
    
    private boolean prepareInBackground = true;
    private static final String PREPARE_IN_BACKGROUND_NAME = "prepare_in_background";
    private boolean ignoreSubperiods = false;
    private static final String IGNORE_SUBPERIODS_NAME = "ignore_subperiods"; 
    private boolean pruneWithCombination = false;
    private static final String PRUNE_WITH_COMBINATION_NAME = "prune_with_combination";
    private boolean pauseEachIteration = false;
    private static final String PAUSE_EACH_ITERATION_NAME = "pause_each_iteration";
    private boolean pauseEachSolution = true;
    private static final String PAUSE_ON_SOLUTION_NAME = "pause_on_solution";
    private boolean saveSolutions = false;
    private static final String SAVE_SOLUTIONS_NAME = "save_solutions";
    private String solutionFile = "";
    private static final String SAVE_SOLUTIONS_FILE_NAME = "save_solutions_file";
    private int solutionSpacing = 20;
    private static final String SAVE_SOLUTIONS_SPACING_NAME = "save_solutions_spacing";
    private boolean saveAllGen = false;
    private static final String SAVE_SOLUTIONS_ALL_GEN_NAME = "save_solutions_all_generations";
    private boolean saveStatus = false;
    private static final String SAVE_STATUS_NAME = "save_status";
    private String saveStatusFile = "";
    private static final String SAVE_STATUS_FILE_NAME = "save_status_file";
    private int saveStatusPeriod = 60;
    private static final String SAVE_STATUS_PERIOD_NAME = "save_status_period";
    private boolean displayStatus = true;
    private static final String DISPLAY_STATUS_NAME = "display_status";
    private int displayStatusPeriod = 5;
    private static final String DISPLAY_STATUS_PERIOD_NAME = "display_status_period";

    private boolean limitGenZero = false;
    private static final String LIMIT_GEN_ZERO = "limit_generation_0";
    private int limitGenZeroCells = 1;
    private static final String LIMIT_GEN_ZERO_CELLS = "limit_generation_0_cells";
    private boolean limitGenZeroVarsOnly = false;
    private static final String LIMIT_GEN_ZERO_VARS_ONLY = "limit_generation_0_variables_only";
    
    private boolean layersLiveCellConstraint = false;
    private static final String LAYERS_LIVE_CELL_CONSTRAINT = "layers_live_constraint"; 
    private int layersLiveCells = 1;
    private static final String LAYERS_LIVE_CELLS = "layers_live_cells";
    private boolean layersLiveCellsVarsOnly = false;
    private static final String LAYERS_LIVE_CELLS_VARS_ONLY = "layers_live_cells_variables_only";
    
    private boolean layersActiveCellConstraint = false;
    private static final String LAYERS_ACTIVE_CELL_CONSTRAINT = "layers_active_constraint"; 
    private int layersActiveCells = 1;
    private static final String LAYERS_ACTIVE_CELLS = "layers_active_cells";
    private boolean layersActiveCellsVarsOnly = false;
    private static final String LAYERS_ACTIVE_CELLS_VARS_ONLY = "layers_active_cells_variables_only";
    
    private boolean layersFromSorting = true;
    private static final String LAYERS_FROM_SORTING = "layers_from_sorting";  
    private int layersStartColumn = 0;
    private static final String LAYERS_START_COLUMN = "layers_start_column";
    private int layersStartRow = 0;
    private static final String LAYERS_START_ROW = "layers_start_row";
    private int layersType = LAYER_COLUMN;
    private static final String LAYERS_TYPE = "layers_type"; 
    
    public boolean readStatusParameter(StatusFileHandler handler, String name,
            int[] details, String value) throws IOException
    {
        if (name.equals(SORT_GEN_FIRST_NAME))
        {
            handler.checkDetailsLength(0);
            sortGenFirst = handler.parseBoolean(value);
        }        
        else if (name.equals(SORT_TO_FUTURE_NAME))
        {
            handler.checkDetailsLength(0);
            sortToFuture = handler.parseBoolean(value);
        }        
        else if (name.equals(SORT_START_COLUMN_NAME))
        {
            handler.checkDetailsLength(0);
            sortStartColumn = handler.parseInteger(value);
        }        
        else if (name.equals(SORT_START_ROW_NAME))
        {
            handler.checkDetailsLength(0);
            sortStartRow = handler.parseInteger(value);
        }        
        else if (name.equals(SORT_TYPE_NAME))
        {
            handler.checkDetailsLength(0);
            sortType = handler.parseEnum(value, SORT_NAMES);
        }    
        else if(name.equals(SORT_REVERSE_NAME))
        {
            handler.checkDetailsLength(0);
            sortReverse = handler.parseBoolean(value);
        }
        else if (name.equals(PREPARE_IN_BACKGROUND_NAME))
        {
            handler.checkDetailsLength(0);
            prepareInBackground = handler.parseBoolean(value);
        }        
        else if (name.equals(IGNORE_SUBPERIODS_NAME))
        {
            handler.checkDetailsLength(0);
            ignoreSubperiods = handler.parseBoolean(value);
        }        
        else if (name.equals(PRUNE_WITH_COMBINATION_NAME))
        {
            handler.checkDetailsLength(0);
            pruneWithCombination = handler.parseBoolean(value);
        }        
        else if (name.equals(PAUSE_EACH_ITERATION_NAME))
        {
            handler.checkDetailsLength(0);
            pauseEachIteration = handler.parseBoolean(value);
        }        
        else if (name.equals(PAUSE_ON_SOLUTION_NAME))
        {
            handler.checkDetailsLength(0);
            pauseEachSolution = handler.parseBoolean(value);
        }        
        else if (name.equals(SAVE_SOLUTIONS_NAME))
        {
            handler.checkDetailsLength(0);
            saveSolutions = handler.parseBoolean(value);
        }        
        else if (name.equals(SAVE_SOLUTIONS_FILE_NAME))
        {
            handler.checkDetailsLength(0);
            solutionFile = value;
        }       
        else if (name.equals(SAVE_SOLUTIONS_SPACING_NAME))
        {
            handler.checkDetailsLength(0);
            solutionSpacing = handler.parseInteger(value);
        }        
        else if (name.equals(SAVE_SOLUTIONS_ALL_GEN_NAME))
        {
            handler.checkDetailsLength(0);
            saveAllGen = handler.parseBoolean(value);
        }        
        else if (name.equals(SAVE_STATUS_NAME))
        {
            handler.checkDetailsLength(0);
            saveStatus = handler.parseBoolean(value);
        }        
        else if (name.equals(SAVE_STATUS_FILE_NAME))
        {
            handler.checkDetailsLength(0);
            saveStatusFile = value;
        }        
        else if (name.equals(SAVE_STATUS_PERIOD_NAME))
        {
            handler.checkDetailsLength(0);
            saveStatusPeriod = handler.parseInteger(value);
        }        
        else if (name.equals(DISPLAY_STATUS_NAME))
        {
            handler.checkDetailsLength(0);
            displayStatus = handler.parseBoolean(value);
        }        
        else if (name.equals(DISPLAY_STATUS_PERIOD_NAME))
        {
            handler.checkDetailsLength(0);
            displayStatusPeriod = handler.parseInteger(value);
        }
        else if (name.equals(LIMIT_GEN_ZERO))
        {
            handler.checkDetailsLength(0);
            limitGenZero = handler.parseBoolean(value);
        }
        else if (name.equals(LIMIT_GEN_ZERO_CELLS))
        {
            handler.checkDetailsLength(0);
            limitGenZeroCells = handler.parseInteger(value);
        }
        else if (name.equals(LIMIT_GEN_ZERO_VARS_ONLY))
        {
            handler.checkDetailsLength(0);
            limitGenZeroVarsOnly = handler.parseBoolean(value);
        }
        else if (name.equals(LAYERS_LIVE_CELL_CONSTRAINT))
        {
            handler.checkDetailsLength(0);
            layersLiveCellConstraint = handler.parseBoolean(value); 
        }
        else if (name.equals(LAYERS_LIVE_CELLS))
        {
            handler.checkDetailsLength(0);
            layersLiveCells = handler.parseInteger(value);
        }
        else if (name.equals(LAYERS_LIVE_CELLS_VARS_ONLY))
        {
            handler.checkDetailsLength(0);
            layersLiveCellsVarsOnly = handler.parseBoolean(value);
        }
        else if (name.equals(LAYERS_ACTIVE_CELL_CONSTRAINT))
        {
            handler.checkDetailsLength(0);
            layersActiveCellConstraint = handler.parseBoolean(value); 
        }
        else if (name.equals(LAYERS_ACTIVE_CELLS))
        {
            handler.checkDetailsLength(0);
            layersActiveCells = handler.parseInteger(value);
        }
        else if (name.equals(LAYERS_ACTIVE_CELLS_VARS_ONLY))
        {
            handler.checkDetailsLength(0);
            layersActiveCellsVarsOnly = handler.parseBoolean(value);
        }
        else if (name.equals(LAYERS_FROM_SORTING))
        {
            handler.checkDetailsLength(0);
            layersFromSorting = handler.parseBoolean(value);  
        }
        else if (name.equals(LAYERS_START_COLUMN))
        {
            handler.checkDetailsLength(0);
            layersStartColumn = handler.parseInteger(value);
        }
        else if (name.equals(LAYERS_START_ROW))
        {
            handler.checkDetailsLength(0);
            layersStartRow = handler.parseInteger(value);
        }
        else if (name.equals(LAYERS_TYPE))
        {
            handler.checkDetailsLength(0);
            layersType = handler.parseEnum(value, LAYER_NAMES); 
        }
        return true;
    }
    
    public String finishReadStatus()
    {
        return validate();
    }

    public void writeStatus(StatusFileHandler handler) throws IOException
    {
        handler.putParameter(SORT_GEN_FIRST_NAME, sortGenFirst);
        handler.putParameter(SORT_TO_FUTURE_NAME, sortToFuture);
        handler.putParameter(SORT_START_COLUMN_NAME, sortStartColumn);
        handler.putParameter(SORT_START_ROW_NAME, sortStartRow);
        handler.putParameter(SORT_TYPE_NAME, SORT_NAMES[sortType]);
        handler.putParameter(SORT_REVERSE_NAME, sortReverse);
        handler.putParameter(PREPARE_IN_BACKGROUND_NAME, prepareInBackground);
        handler.putParameter(IGNORE_SUBPERIODS_NAME, ignoreSubperiods);
        handler.putParameter(PRUNE_WITH_COMBINATION_NAME, pruneWithCombination);
        handler.putParameter(PAUSE_EACH_ITERATION_NAME, pauseEachIteration);
        handler.putParameter(PAUSE_ON_SOLUTION_NAME, pauseEachSolution);
        handler.putParameter(SAVE_SOLUTIONS_NAME, saveSolutions);
        handler.putParameter(SAVE_SOLUTIONS_FILE_NAME, solutionFile);
        handler.putParameter(SAVE_SOLUTIONS_SPACING_NAME, solutionSpacing);
        handler.putParameter(SAVE_SOLUTIONS_ALL_GEN_NAME, saveAllGen);
        handler.putParameter(SAVE_STATUS_NAME, saveStatus);
        handler.putParameter(SAVE_STATUS_FILE_NAME, saveStatusFile);
        handler.putParameter(SAVE_STATUS_PERIOD_NAME, saveStatusPeriod);
        handler.putParameter(DISPLAY_STATUS_NAME, displayStatus);
        handler.putParameter(DISPLAY_STATUS_PERIOD_NAME, displayStatusPeriod);
        handler.putParameter(LIMIT_GEN_ZERO, limitGenZero);
        handler.putParameter(LIMIT_GEN_ZERO_CELLS, limitGenZeroCells);
        handler.putParameter(LIMIT_GEN_ZERO_VARS_ONLY, limitGenZeroVarsOnly);
        handler.putParameter(LAYERS_LIVE_CELL_CONSTRAINT, layersLiveCellConstraint); 
        handler.putParameter(LAYERS_LIVE_CELLS, layersLiveCells);
        handler.putParameter(LAYERS_LIVE_CELLS_VARS_ONLY, layersLiveCellsVarsOnly);
        handler.putParameter(LAYERS_ACTIVE_CELL_CONSTRAINT, layersActiveCellConstraint); 
        handler.putParameter(LAYERS_ACTIVE_CELLS, layersActiveCells);
        handler.putParameter(LAYERS_ACTIVE_CELLS_VARS_ONLY, layersActiveCellsVarsOnly);
        handler.putParameter(LAYERS_FROM_SORTING, layersFromSorting);  
        handler.putParameter(LAYERS_START_COLUMN, layersStartColumn);
        handler.putParameter(LAYERS_START_ROW, layersStartRow);
        handler.putParameter(LAYERS_TYPE, LAYER_NAMES[layersType]); 
    }

    private void checkValidated()
    {
        if (validated)
        {
            throw(new RuntimeException("Validated Options cannot be changed."));
        }
    }
    
    public boolean isValidated()
    {
        return validated;
    }
    
    public String validate()
    {
        if (saveSolutions && ((null == solutionFile) || solutionFile.equals("")))
        {
            return "Please specify the file to save solutions.";
        }
        if (saveSolutions && (solutionSpacing < 0))
        {
            return "Solution spacing should not be negative.";
        }
        if (saveStatus && ((null == saveStatusFile) || saveStatusFile.equals("")))
        {
            return "Please specify the file to save search status.";
        }
        if (saveStatus && saveStatusPeriod < 1)
        {
            return "Status save period should be positive.";
        }
        if (displayStatus && displayStatusPeriod < 1)
        {
            return "Status display period should be positive.";
        }
        if ((sortStartColumn < -10000) || (sortStartColumn > 10000))
        {
            return "Sorting start column must be within <-10000, 10000>.";
        }
        if ((sortStartRow < -10000) || (sortStartRow > 10000))
        {
            return "Sorting start row must be within <-10000, 10000>.";
        }
        if ((layersStartColumn < -10000) || (layersStartColumn > 10000))
        {
            return "Layers start column must be within <-10000, 10000>.";
        }
        if ((layersStartRow < -10000) || (layersStartRow > 10000))
        {
            return "Layers start row must be within <-10000, 10000>.";
        }
        validated = true;
        return null;
    }
    
    public boolean isSortGenFirst()
    {
        return sortGenFirst;
    }

    public void setSortGenFirst(boolean sortGenFirst)
    {
        checkValidated();
        this.sortGenFirst = sortGenFirst;
    }

    public int getSortStartColumn()
    {
        return sortStartColumn;
    }

    public void setSortStartColumn(int sortStartColumn)
    {
        checkValidated();
        this.sortStartColumn = sortStartColumn;
    }

    public int getSortStartRow()
    {
        return sortStartRow;
    }

    public void setSortStartRow(int sortStartRow)
    {
        checkValidated();
        this.sortStartRow = sortStartRow;
    }

    public boolean isSortToFuture()
    {
        return sortToFuture;
    }

    public void setSortToFuture(boolean sortToFuture)
    {
        checkValidated();
        this.sortToFuture = sortToFuture;
    }

    public int getSortType()
    {
        return sortType;
    }

    public void setSortType(int sortType)
    {
        checkValidated();
        this.sortType = sortType;
    }

    public boolean isPrepareInBackground()
    {
        return prepareInBackground;
    }

    public void setPrepareInBackground(boolean prepareInBackground)
    {
        checkValidated();
        this.prepareInBackground = prepareInBackground;
    }

    public boolean isSaveStatus()
    {
        return saveStatus;
    }

    public void setSaveStatus(boolean saveStatus)
    {
        checkValidated();
        this.saveStatus = saveStatus;
    }

    public String getSaveStatusFile()
    {
        return saveStatusFile;
    }

    public void setSaveStatusFile(String saveStatusFile)
    {
        checkValidated();
        this.saveStatusFile = saveStatusFile;
    }

    public int getSaveStatusPeriod()
    {
        return saveStatusPeriod;
    }
    
    public long getSaveStatusPeriodNs()
    {
        return saveStatus ? 1000000000L * saveStatusPeriod : Long.MAX_VALUE;
    }

    public void setSaveStatusPeriod(int saveStatusPeriod)
    {
        checkValidated();
        this.saveStatusPeriod = saveStatusPeriod;
    }

    public String getSolutionFile()
    {
        return solutionFile;
    }

    public void setSolutionFile(String solutionFile)
    {
        checkValidated();
        this.solutionFile = solutionFile;
    }

    public boolean isDisplayStatus()
    {
        return displayStatus;
    }

    public void setDisplayStatus(boolean displayStatus)
    {
        checkValidated();
        this.displayStatus = displayStatus;
    }

    public int getDisplayStatusPeriod()
    {
        return displayStatusPeriod;
    }
    
    public long getDisplayStatusPeriodNs()
    {
        return displayStatus ? 1000000000L * displayStatusPeriod : Long.MAX_VALUE;
    }

    public void setDisplayStatusPeriod(int displayStatusPeriod)
    {
        checkValidated();
        this.displayStatusPeriod = displayStatusPeriod;
    }

    public boolean isPauseEachIteration()
    {
        return pauseEachIteration;
    }

    public void setPauseEachIteration(boolean pauseEachIteration)
    {
        checkValidated();
        this.pauseEachIteration = pauseEachIteration;
    }

    public boolean isPauseEachSolution()
    {
        return pauseEachSolution;
    }

    public void setPauseEachSolution(boolean pauseEachSolution)
    {
        checkValidated();
        this.pauseEachSolution = pauseEachSolution;
    }

    public boolean isPruneWithCombination()
    {
        return pruneWithCombination;
    }

    public void setPruneWithCombination(boolean pruneWithCombination)
    {
        checkValidated();
        this.pruneWithCombination = pruneWithCombination;
    }

    public boolean isSaveAllGen()
    {
        return saveAllGen;
    }

    public void setSaveAllGen(boolean saveAllGen)
    {
        checkValidated();
        this.saveAllGen = saveAllGen;
    }

    public boolean isSaveSolutions()
    {
        return saveSolutions;
    }

    public void setSaveSolutions(boolean saveSolutions)
    {
        checkValidated();
        this.saveSolutions = saveSolutions;
    }

    public int getSolutionSpacing()
    {
        return solutionSpacing;
    }

    public void setSolutionSpacing(int solutionSpacing)
    {
        checkValidated();
        this.solutionSpacing = solutionSpacing;
    }

    public boolean isLayersActiveCellConstraint()
    {
        return layersActiveCellConstraint;
    }

    public void setLayersActiveCellConstraint(boolean layersActiveCellConstraint)
    {
        checkValidated();
        this.layersActiveCellConstraint = layersActiveCellConstraint;
    }

    public int getLayersActiveCells()
    {
        return layersActiveCells;
    }

    public void setLayersActiveCells(int layersActiveCells)
    {
        checkValidated();
        this.layersActiveCells = layersActiveCells;
    }

    public boolean isLayersFromSorting()
    {
        return layersFromSorting;
    }

    public void setLayersFromSorting(boolean layersFromSorting)
    {
        checkValidated();
        this.layersFromSorting = layersFromSorting;
    }

    public boolean isLayersLiveCellConstraint()
    {
        return layersLiveCellConstraint;
    }

    public void setLayersLiveCellConstraint(boolean layersLiveCellConstraint)
    {
        checkValidated();
        this.layersLiveCellConstraint = layersLiveCellConstraint;
    }

    public int getLayersLiveCells()
    {
        return layersLiveCells;
    }

    public void setLayersLiveCells(int layersLiveCells)
    {
        checkValidated();
        this.layersLiveCells = layersLiveCells;
    }

    public int getLayersStartColumn()
    {
        return layersStartColumn;
    }

    public void setLayersStartColumn(int layersStartColumn)
    {
        checkValidated();
        this.layersStartColumn = layersStartColumn;
    }

    public int getLayersStartRow()
    {
        return layersStartRow;
    }

    public void setLayersStartRow(int layersStartRow)
    {
        checkValidated();
        this.layersStartRow = layersStartRow;
    }

    public int getLayersType()
    {
        return layersType;
    }

    public void setLayersType(int layersType)
    {
        checkValidated();
        this.layersType = layersType;
    }

    public boolean isLimitGenZero()
    {
        return limitGenZero;
    }

    public void setLimitGenZero(boolean limitGenZero)
    {
        checkValidated();
        this.limitGenZero = limitGenZero;
    }

    public int getLimitGenZeroCells()
    {
        return limitGenZeroCells;
    }

    public void setLimitGenZeroCells(int limitGenZeroCells)
    {
        checkValidated();
        this.limitGenZeroCells = limitGenZeroCells;
    }
    
    public boolean isUseLayers()
    {
        return layersActiveCellConstraint || layersLiveCellConstraint;
    }

    public boolean isLimitGenZeroVarsOnly()
    {
        return limitGenZeroVarsOnly;
    }

    public void setLimitGenZeroVarsOnly(boolean limitGenZeroVarsOnly)
    {
        this.limitGenZeroVarsOnly = limitGenZeroVarsOnly;
    }

    public boolean isLayersLiveCellsVarsOnly()
    {
        return layersLiveCellsVarsOnly;
    }

    public void setLayersLiveCellsVarsOnly(boolean layersLiveCellsVarsOnly)
    {
        this.layersLiveCellsVarsOnly = layersLiveCellsVarsOnly;
    }

    public boolean isLayersActiveCellsVarsOnly()
    {
        return layersActiveCellsVarsOnly;
    }

    public void setLayersActiveCellsVarsOnly(boolean layersActiveCellsVarsOnly)
    {
        this.layersActiveCellsVarsOnly = layersActiveCellsVarsOnly;
    }
    
    public boolean isSortReverse()
    {
        return sortReverse;
    }
    
    public void setSortReverse(boolean reverse)
    {
        sortReverse = reverse;
    }
    
    public boolean isIgnoreSubperiods()
    {
        return ignoreSubperiods;
    }
    
    public void setIgnoreSubperiods(boolean ignore)
    {
        ignoreSubperiods = ignore;
    }
}
