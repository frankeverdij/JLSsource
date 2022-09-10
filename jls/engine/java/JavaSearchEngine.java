package jls.engine.java;

import java.util.Arrays;


// Implementation of SearchEngine interface towards JAVA implementation
public class JavaSearchEngine implements jls.engine.SearchEngine
{
    // specifies whether the search should be pruned by combination of previous results or not
    boolean pruneSearch = false;
    
    // set to true if the last variable state is a solution
    boolean solution = false;
    
    private volatile boolean interrupted = false;

    @Override
    public String getName()
    {
        return "JAVA Search Engine";
    }

    @Override
    public void reset(boolean[] ruleBirth, boolean[] ruleSurvival, int numVariables)
    {
        StateTable.Initialize(ruleBirth, ruleSurvival);
        Variable.Initialize(numVariables);
        // set initial size of the stack to number of variables
        // cone portions will be added later
        Stack.resetTo(numVariables);
        solution = false;
    }
    
    @Override
    public boolean addActiveCellConstraint(int limit, int[][] varList, boolean[] onCells, boolean[] offCells)
    {
        ActiveColumnConstraint[] columns = new ActiveColumnConstraint[varList.length];
        ActiveLayerConstraint master = new ActiveLayerConstraint();
        int stackItemsToAdd = 0; // each column needs 1 or 2 stack items depending on its initial state so we need to iterate through that
        int columnNo = varList.length;
        int columnCount = 0;
        while (columnNo > 0)
        {
            --columnNo;
            int[] columnVarIndexes = varList[columnNo];
            Variable[] columnVars = new Variable[columnVarIndexes.length];
            int varNo = columnVarIndexes.length;
            while (varNo > 0)
            {
                --varNo;
                columnVars[varNo] = Variable.getVariable(columnVarIndexes[varNo]);
                if (!columnVars[varNo].isUnset())
                {
                    throw new RuntimeException("Variable is set in creating active layer constraint");
                }
            }
            ActiveColumnConstraint col;
            int stackItemsDelta;
            if (onCells[columnNo])
            {
                if (offCells[columnNo])
                {
                    throw new RuntimeException("Cell is already active in creating active layer constraint");
                }
                stackItemsDelta = 1;
                col = new ActiveColumnConstraint(master, ActiveColumnConstraint.STATE_ON, columnVars);
            }
            else if (offCells[columnNo])
            {
                stackItemsDelta = 1;
                col = new ActiveColumnConstraint(master, ActiveColumnConstraint.STATE_OFF, columnVars);
            }
            else
            {
                stackItemsDelta = 2;
                col = new ActiveColumnConstraint(master, ActiveColumnConstraint.STATE_EMPTY, columnVars);
            }
            int i = columnCount;
            while (i > 0)
            {
                --i;
                if (columns[i].equals(col))
                {
                    columns[i].incWeight();
                    col.destroy();
                    col = null;
                    i = 0;
                }
            }
            if (col != null)
            {
                stackItemsToAdd += stackItemsDelta;
                columns[columnCount] = col;
                ++columnCount;
            }
        }
        if (columnCount < columns.length)
        {
            columns = Arrays.copyOf(columns, columnCount);
        }
        // let's finish the structure first
        master.setColumnList(columns);
        master.setState(limit);
        if (limit > columns.length)
        {
            stackItemsToAdd += columns.length;
        }
        else
        {
            stackItemsToAdd += limit;
        }
        // check consequences
        Stack.resetAndAdjust(stackItemsToAdd);
        if (master.propagate())
        {
            if (Stack.propagate())
            {
                // all okay, let's optimize and finish
                Stack.optimize();
                master.optimize();
                return true;
            }
        }
        // take it back
        Stack.backtrackAll();
        Stack.resetAndAdjust(-stackItemsToAdd);
        master.destroy();
        return false;
    }
    
    @Override
    public boolean addLiveCellConstraint(int limit, int[][] varList)
    {
        LiveColumnConstraint[] columns = new LiveColumnConstraint[varList.length];
        LiveLayerConstraint master = new LiveLayerConstraint();
        int stackItemsToAdd = varList.length;
        int columnNo = varList.length;
        int columnCount = 0;
        while (columnNo > 0)
        {
            --columnNo;
            int[] columnVarIndexes = varList[columnNo];
            Variable[] columnVars = new Variable[columnVarIndexes.length];
            int varNo = columnVarIndexes.length;
            while (varNo > 0)
            {
                --varNo;
                columnVars[varNo] = Variable.getVariable(columnVarIndexes[varNo]);
                if (!columnVars[varNo].isUnset())
                {
                    throw new RuntimeException("Variable is set in creating live layer constraint");
                }
            }
            LiveColumnConstraint col = new LiveColumnConstraint(master, columnVars);
            int i = columnCount;
            while (i > 0)
            {
                --i;
                if (columns[i].equals(col))
                {
                    columns[i].incWeight();
                    col.destroy();
                    col = null;
                    --stackItemsToAdd;
                    i = 0;
                }
            }
            if (col != null)
            {
                columns[columnCount] = col;
                ++columnCount;
            }
        }
        if (columnCount < columns.length)
        {
            columns = Arrays.copyOf(columns, columnCount);
        }
        // let's finish the structure first
        master.setColumnList(columns);
        master.setState(limit);
        if (limit > columns.length)
        {
            stackItemsToAdd += columns.length;
        }
        else
        {
            stackItemsToAdd += limit;
        }
        // check if constraint is not broken initially
        Stack.resetAndAdjust(stackItemsToAdd);
        if (master.propagate())
        {
            if (Stack.propagate())
            {
                // all okay, let's optimize and finish
                Stack.optimize();
                master.optimize();
                return true;
            }
        }
        // take it back
        Stack.backtrackAll();
        Stack.resetAndAdjust(-stackItemsToAdd);
        master.destroy();
        return false;
    }

    @Override
    public boolean addGenZeroConstraint(int limit, int[] varList)
    {
        if (varList.length < 1)
        {
            throw new RuntimeException("Attempt to make generation zero constraint with no variables");
        }
            
        // make a sorted copy of the list to allow
        // setting up counts for each variable
        int[] myVarList = Arrays.copyOf(varList, varList.length);
        Arrays.sort(myVarList);
        // find out how many variables will we actually need
        int varCount = 1;
        int i = myVarList.length;
        while (i > 1)
        {
            --i;
            if (myVarList[i] != myVarList[i - 1])
            {
                ++varCount;
            }
        }
        
        Variable[] vars = new Variable[varCount];
        GenZeroConstraint gzc = new GenZeroConstraint(limit);
        
        int cnt = 1;
        i = myVarList.length;
        while (i > 0)
        {
            --i;
            if ((i == 0) || (myVarList[i] != myVarList[i - 1]))
            {
                --varCount;
                Variable var = Variable.getVariable(myVarList[i]);
                vars[varCount] = var;
                var.addConstraint(gzc, cnt);
                cnt = 1;
            }
            else
            {
                ++cnt;
            }
        }

        if (varCount > 0)
        {
            throw new RuntimeException("Mismatch enumerating variables for GenZeroConstraint");
        }
        
        gzc.setVarList(vars);

        Stack.resetAndAdjust(vars.length);
        
        if (gzc.propagate())
        {
            if (Stack.propagate())
            {
                Stack.optimize();
                return true;
            }
        }

        Stack.backtrackAll();
        Stack.resetAndAdjust(-vars.length);
        gzc.destroy();
        return false;
    }
    
    @Override
    public boolean addCone(boolean topOn, boolean topOff, int topVar, boolean midOn, boolean midOff, int midVar, int neighOn, int neighOff, int[] neighVarList)
    {
        // some sanity checks
        // exactly one of topOn, topOff, and (topVar != -1) is true
        if ((topOn && topOff) || (topOn && (topVar != -1)) || (topOff && (topVar != -1)) || (!topOn && !topOff && (topVar == -1)))
        {
            throw new RuntimeException("addCone inconsistent call: topOn=" + topOn + " topOff=" + topOff + " topVar=" + topVar);
        }
        // either none or one of midOn, midOff, and (midVar != -1) is true
        if ((midOn && midOff) || (midOn && (midVar != -1)) || (midOff && (midVar != -1)))
        {
            throw new RuntimeException("addCone inconsistent call: midOn=" + midOn + " midOff=" + midOff + " midVar=" + midVar);
        }
        // 0 <= neighOn + neighOff + neighVarList.length <= 8
        // not testing that they're positive
        if ((neighOn + neighOff + neighVarList.length) > 8)
        {
            throw new RuntimeException("addCone inconsistent call: neighOn=" + neighOn + " neighOff=" + neighOff + " neighVarList=" + neighVarList.length);
        }
        // no -1s in neighVarList - not testing
        
        // now we need to prepare input in the form Cone factory expects it
        // i.e. type, variables, and counts

        // first let's sort the neighbor list, it will be easier to process and ensures consistent ordering
        // as required by cone factory
        Arrays.sort(neighVarList);
        
        // now let's start creating the variable list
        // start at full length, we will shrink them later if needed
        Variable [] varList = new Variable[10];
        int [] varCount = new int[10];
        
        int cnt = 0; // number of variables added so far
        int midIdx = 0; // where is mid variable in the list if it is there

        if (topVar != -1)
        {
            varList[cnt] = Variable.getVariable(topVar);
            varCount[cnt] = 0; // it is top, not in neighbors
            ++cnt;
        }
        
        if ((midVar != -1) && (midVar != topVar)) // if it is the same as top then its record is already created
        {
            varList[cnt] = Variable.getVariable(midVar);
            varCount[cnt] = 0; // it is middle, not in neighbors
            midIdx = cnt; // it may be 0 or 1
            ++cnt;
        }
        
        int i = 0;
        int varIdx = -1;
        while (i < neighVarList.length)
        {
            // the array is sorted so if we run to a new id, we'll get it as many times as many times it is in there
            if (neighVarList[i] == topVar)
            {
                // if we're here, topVar is not -1 and it is at position 0
                ++varCount[0];
            }
            else if (neighVarList[i] == midVar)
            {
                // if we're here, midVar is not -1 and it is different from topVar
                // it is at position midIdx
                ++varCount[midIdx];
            }
            else if (neighVarList[i] != varIdx)
            {
                // we've run into a new variable
                varIdx = neighVarList[i];
                varCount[cnt] = 1;
                varList[cnt] = Variable.getVariable(varIdx);
                ++cnt;
            }
            else
            {
                // we found another occurrence of already known variable
                ++varCount[cnt - 1];
            }
            ++i;
        }
        
        // and now shrink both fields to their final size
        if (cnt < varList.length)
        {
            varList = Arrays.copyOf(varList, cnt);
            varCount = Arrays.copyOf(varCount, cnt);
        }
        
        // finally recognize the type
        
        if (topOn)
        {
            if (midOn)
            {
                return Cone.add(Cone.TYPE_ONON, neighOn, neighOff, varList, varCount);
            }
            else if (midOff)
            {
                return Cone.add(Cone.TYPE_ONOFF, neighOn, neighOff, varList, varCount);
            }
            else if (midVar == -1)
            {
                return Cone.add(Cone.TYPE_ONUNS, neighOn, neighOff, varList, varCount);
            }
            else
            {
                return Cone.add(Cone.TYPE_ONV0, neighOn, neighOff, varList, varCount);
            }
        }
        else if (topOff)
        {
            if (midOn)
            {
                return Cone.add(Cone.TYPE_OFFON, neighOn, neighOff, varList, varCount);
            }
            else if (midOff)
            {
                return Cone.add(Cone.TYPE_OFFOFF, neighOn, neighOff, varList, varCount);
            }
            else if (midVar == -1)
            {
                return Cone.add(Cone.TYPE_OFFUNS, neighOn, neighOff, varList, varCount);
            }
            else
            {
                return Cone.add(Cone.TYPE_OFFV0, neighOn, neighOff, varList, varCount);
            }
        }
        else 
        {
            if (midOn)
            {
                return Cone.add(Cone.TYPE_V0ON, neighOn, neighOff, varList, varCount);
            }
            else if (midOff)
            {
                return Cone.add(Cone.TYPE_V0OFF, neighOn, neighOff, varList, varCount);
            }
            else if (midVar == -1)
            {
                return Cone.add(Cone.TYPE_V0UNS, neighOn, neighOff, varList, varCount);
            }
            else if (topVar == midVar)
            {
                return Cone.add(Cone.TYPE_V0V0, neighOn, neighOff, varList, varCount);
            }
            else
            {
                return Cone.add(Cone.TYPE_V0V1, neighOn, neighOff, varList, varCount);
            }
        }
    }

    @Override
    public boolean isVariableOn(int varIdx)
    {
        return Variable.getVariable(varIdx).isOn();
    }
    
    @Override
    public boolean isVariableOff(int varIdx)
    {
        return Variable.getVariable(varIdx).isOff();
    }

    @Override
    public boolean isVariableUnset(int varIdx)
    {
        return Variable.getVariable(varIdx).isUnset();
    }

    @Override
    public boolean isCombinationOn(int varIdx)
    {
        return Variable.getVariable(varIdx).isCombinationOn();
    }
    
    @Override
    public boolean isCombinationOff(int varIdx)
    {
        return Variable.getVariable(varIdx).isCombinationOff();
    }

    @Override
    public boolean isCombinationUnset(int varIdx)
    {
        return Variable.getVariable(varIdx).isCombinationUnset();
    }

    @Override
    public void setCombinationOn(int varIdx)
    {
        Variable.getVariable(varIdx).setCombinationOn();
    }
    
    @Override
    public void setCombinationOff(int varIdx)
    {
        Variable.getVariable(varIdx).setCombinationOff();
    }

    @Override
    public void setCombinationUnset(int varIdx)
    {
        Variable.getVariable(varIdx).setCombinationUnset();
    }

    @Override
    public boolean hasVariableConstraints(int varIdx)
    {
        return Variable.getVariable(varIdx).hasConstraints();
    }

    @Override
    public boolean trainVariable(int varIdx)
    {
        Variable var = Variable.getVariable(varIdx); 
        if (!var.isUnset())
        {
            // this variable cannot be trained but it's ok
            return true;
        }
        else
        {
            // reset the stack to discard any previous variable changes on it - we don't want to change anything there
            Stack.reset();
            
            // try the variable OFF (with propagation)
            if (var.setToFork())
            {
                // can be set to OFF; backtrack to try the other state
                Stack.backtrackToFork();
                if (var.setOpposite()) // try the other value and propagate
                {
                    // can be set to ON, too
                    Stack.backtrackAll();
                    return true;
                }
                else
                {
                    Stack.backtrackAll();
                    // can be set OFF only
                    var.setToFork(); // set OFF again and propagate
                    Stack.optimize();
                    return true;
                }
            }
            else
            {
                // cannot be set OFF
                Stack.backtrackToFork();
                if (var.setOpposite()) // try the other value and propagate
                {
                    // it can be set to ON
                    Stack.optimize();
                    return true;
                }
                else
                {
                    // cannot be set either way, backtrack and report error
                    Stack.backtrackAll();
                    return false;
                }
            }                
        }
    }

    @Override
    public void setSearchOrder(int[] sortedVarIndex)
    {
        Variable.setSearchOrder(sortedVarIndex);
    }

    @Override
    public void setSearchPruning(boolean prune)
    {
        if (pruneSearch != prune)
        {
            if (prune)
            {
                Variable.initializePruning();
            }
            pruneSearch = prune;
        }
    }

    @Override
    public boolean isEmptyStack()
    {
        return Stack.isEmpty();
    }

    @Override
    public void processCombination(boolean set)
    {
        Variable.processCombination(set);
    }

    @Override
    public boolean isSolution()
    {
        return solution;
    }

    @Override
    public int iterate(boolean continuous)
    {
        int iterated = 0;
        interrupted = false;
        try
        {
            if (pruneSearch)
            {
                if (solution)
                {
                    solution = false;
                    
                    
                    // let's do some backtracking and trying opposite states on the stack
                    Variable var = Stack.backtrackToForkWithPruning();
                    while (!Variable.isPruningConsistent())
                    {
                        var.backtrackWithPruning(0);
                        var = Stack.backtrackToForkWithPruning();
                    }
                        
                    while (!var.setOppositeWithPruning())
                    {
                        var = Stack.backtrackToForkWithPruning();
                    }
                    // this counts as a successful iteration
                }
                else
                {
                    // it was not a solution but we're in a consistent state
                    // so we need to find a variable to set and just try to set it
                    // trying just one state is straightforward, later we may try some more sophisticated schemes
                    Variable var = Variable.getVariableToSearch(); 
                    if (!var.setToForkWithPruning())
                    {
                        // can't be set OFF so let's start backtracking
                        // the backtrack is to the same variable, no need to assign it
                        var = Stack.backtrackToForkWithPruning();
                        while (!var.setOppositeWithPruning())
                        {
                            var = Stack.backtrackToForkWithPruning();
                        }
                    }
                    // this counts as a successful iteration
                }
                // we did a successful iteration now
                ++iterated;
                if (continuous)
                {
                    while (!interrupted)
                    {
                        // it was not a solution but we're in a consistent state
                        // so we need to find a variable to set and just try to set it
                        // trying just one state is straightforward, later we may try some more sophisticated schemes
                        Variable var = Variable.getVariableToSearch(); 
                        if (!var.setToForkWithPruning())
                        {
                            // can't be set OFF so let's start backtracking
                            // the backtrack is to the same variable, no need to assign it
                            var = Stack.backtrackToForkWithPruning();
                            while (!var.setOppositeWithPruning())
                            {
                                var = Stack.backtrackToForkWithPruning();
                            }
                        }
                        // this counts as a successful iteration
                        ++iterated;
                    }
                }
            }
            else
            {
                if (solution)
                {
                    solution = false;
                    
                    
                    // let's do some backtracking and trying opposite states on the stack
                    Variable var = Stack.backtrackToFork();
                    while (!var.setOpposite())
                    {
                        var = Stack.backtrackToFork();
                    }
                    // this counts as a successful iteration
                }
                else
                {
                    // it was not a solution but we're in a consistent state
                    // so we need to find a variable to set and just try to set it
                    // trying just one state is straightforward, later we may try some more sophisticated schemes
                    Variable var = Variable.getVariableToSearch(); 
                    if (!var.setToFork())
                    {
                        // can't be set OFF so let's start backtracking
                        // the backtrack is to the same variable, no need to assign it
                        var = Stack.backtrackToFork();
                        while (!var.setOpposite())
                        {
                            var = Stack.backtrackToFork();
                        }
                    }
                    // this counts as a successful iteration
                }
                // we did a successful iteration now
                ++iterated;
                if (continuous)
                {
                    while (!interrupted)
                    {
                        // it was not a solution but we're in a consistent state
                        // so we need to find a variable to set and just try to set it
                        // trying just one state is straightforward, later we may try some more sophisticated schemes
                        Variable var = Variable.getVariableToSearch(); 
                        if (!var.setToFork())
                        {
                            // can't be set OFF so let's start backtracking
                            // the backtrack is to the same variable, no need to assign it
                            var = Stack.backtrackToFork();
                            while (!var.setOpposite())
                            {
                                var = Stack.backtrackToFork();
                            }
                        }
                        // this counts as a successful iteration
                        ++iterated;
                    }
                }
            }
        }
        catch (NullPointerException e)
        {
            // this is thrown by Stack.backtrackToFork()
            // now we have ran out of stack
            // that means search is finished
            // just reset the stack (so that the check for empty stack works)
            Stack.reset();
        }
        catch (ArrayIndexOutOfBoundsException a)
        {
            // this is thrown by Variable.getVariableToSearch()
            // solution found
            solution = true;
            if (iterated == 0)
            {
                // this can only happen if there are no variables
                // make sure we make at least 1 iteration
                iterated = 1;
            }
        }
        return iterated;
    }
    
    public void interrupt()
    {
        interrupted = true;
    }

    // prepare walking the stack
    @Override
    public void stackWalkPrepare()
    {
        Stack.prepareWalk();
    }
    
    // walks the stack, returns item on the stack from bottom (oldest) to top (newest)
    // structure:
    // [0] ... variable index
    // [1] ... variable state 0/1
    // [2] ... item type, 0 for enforced, 1 for fork point
    // returns true if ok, false if end of stack
    @Override
    public boolean stackWalkStep(int [] array)
    {
        StackItem si = Stack.stepWalk();
        if (si == null)
        {
            return false;
        }
        else
        {
            Variable var = (Variable) si.getObject();
            int i = Variable.getNumVariables();
            while (i > 0)
            {
                --i;
                if (Variable.getVariable(i) == var)
                {
                    array[0] = i;
                    array[1] = var.isOn() ? 1 : 0;
                    array[2] = (si.getState() == Stack.FORK_POINT) ? 1 : 0;
                    return true;
                }
            }
        }
        throw new RuntimeException("Variable on stack not found in list of variables");
    }
    
    // safely finish stack walk, clean up 
    @Override
    public void stackWalkFinish()
    {
        Stack.finishWalk();
    }
    
    // returns false if failed (i.e. variable is already set another way
    @Override
    public boolean pushOnStack(int varIndex, boolean on, boolean fork)
    {
        Variable var = Variable.getVariable(varIndex);
        if (on)
        {
            if (var.isUnset())
            {
                if (fork)
                {
                    var.setToForkOn();
                }
                else
                {
                    var.setToOn();
                    Stack.propagate();
                }
                return true;
            }
            else 
            {
                // just check that it is already set that way
                return var.isOn();
            }
        }
        else
        {
            if (var.isUnset())
            {
                if (fork)
                {
                    var.setToForkOff();
                }
                else
                {
                    var.setToOff();
                    Stack.propagate();
                }
                return true;
            }
            else
            {
                return var.isOff();
            }
        }
    }
}
