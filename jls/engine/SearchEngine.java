package jls.engine;

public interface SearchEngine
{
    // returns the engine name
    public String getName();

    // (re-)initialize engine with the given number of cell variables
    // note that the number might be 0
    public void reset(boolean[] ruleBirth, boolean[] ruleSurvival, int numVariables);

    // create a new cone (or check validity)
    // parameters:
    // topOn - true if top cell is ON
    // topOff - true if top cell is OFF
    // topVar - index of variable in the place of top cell if it is not ON or OFF
    // midOn - true if middle cell is ON
    // midOff - true if middle cell is OFF
    // midVar - index of variable in the place of middle cell. May be -1 if middle cell is not set and is not a variable
    // neighOn - number of neighbors which are ON
    // neighOff - number of neighbors which are OFF
    // neighVarList - list of variables in neighbors
    // if a variable is multiple times in neighbors it should be the same number of times in the list (or in topVar, midVar etc)
    public boolean addCone(boolean topOn, boolean topOff, int topVar, boolean midOn, boolean midOff, int midVar, int neighOn, int neighOff, int[] neighVarList);

    // create a new constraint on active cells
    // parameters:
    // configured limit of active cells
    // list of variables for each column
    // and indicators for each column that it contains ON or OFF cells
    // ... there should be up to one of each for each column
    // variables referenced should be all unset
    public boolean addActiveCellConstraint(int limit, int[][] varList, boolean[] onCells, boolean[] offCells);
    
    // create a new constraint on live cells
    // parameters:
    // configured limit of live cells
    // list of variables to use for each column
    // variables referenced should be all unset
    public boolean addLiveCellConstraint(int limit, int[][] varList);

    // create a new constraint on generation 0 ON cells
    // parameters:
    // configured limit
    // sorted array of variable indexes, if a variable is multiple times in g0, it is multiple times in the list
    // returns true if constraint was successfully added
    public boolean addGenZeroConstraint(int limit, int[] varList);
    
    // check for variable value
    public boolean isVariableOn(int varIdx);
    public boolean isVariableOff(int varIdx);
    public boolean isVariableUnset(int varIdx);
    public boolean isCombinationOn(int varIdx);
    public boolean isCombinationOff(int varIdx);
    public boolean isCombinationUnset(int varIdx);
    public boolean hasVariableConstraints(int varIdx);

    // direct setting of combination state (when loading)
    public void setCombinationOn(int index);
    public void setCombinationOff(int index);
    public void setCombinationUnset(int index);
    

    // try a variable on and off
    // return true if the variable is ok (i.e. can be set both or at least one way
    // return false if it cannot be set either way
    // if it can be set only one way, leave it set that way
    public boolean trainVariable(int varIdx);

    // accept specified search order
    // the array contains indexes of variables
    // the array does not have to contain all variables, the rest will not be searched
    public void setSearchOrder(int[] sortedVarIndex);
    
    // turns search pruning by combination on/off
    public void setSearchPruning(boolean prune);

    // returns true if the stack is empty
    // which after at least one iteration means search is finished
    public boolean isEmptyStack();
    
    // combine current variable status (a solution)
    // into combination states
    // set=true means that the state should be just set
    // (used for first solution)
    // set=false means that the solution should be combined with the
    // current state (used for second and further solutions)
    public void processCombination(boolean set);

    // returns true if current variable settings are solution
    public boolean isSolution();
    
    // do the specified number of iterations
    // except if a solution is found
    // or if search ends
    // in  which cases stop immediately
    // returns number of iterations actually done
    public int iterate(boolean continuous);

    // asynchronously interrupts continuous search
    public void interrupt();

    // prepare walking the stack
    public void stackWalkPrepare();
    
    // walks the stack, returns item on the stack from bottom (oldest) to top (newest)
    // structure:
    // [0] ... variable index
    // [1] ... variable state 0/1
    // [2] ... item type, 0 for enforced, 1 for fork point
    // returns true if ok, false if end of stack
    public boolean stackWalkStep(int [] array);
    
    // safely finish stack walk, clean up 
    public void stackWalkFinish();
    
    // push a record on stack (load status)
    // returns false if failed (i.e. variable is already set another way
    public boolean pushOnStack(int varIndex, boolean on, boolean fork);
}
