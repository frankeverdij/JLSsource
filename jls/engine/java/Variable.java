package jls.engine.java;

import java.util.Arrays;

public class Variable implements StackObject
{
    // possible variable states
    private static final int OFF = 0;
    private static final int ON = 1;
    private static final int UNSET = 2;
    
    private static Variable[] list = new Variable[0];
    private static int listLength = list.length;
    private static Variable[] searchOrder = null;
    private static int searchPointer = 0; // pointer to searchOrder, used to find next variable to search
    
    // support for search pruning
    //private static int combinationsLeft = 0;
    //private static int combinationsUnequal = 0;
    // coefficient for search with pruning
    // equals sum of:
    // - unset cells with combination
    // - set cells with combination which are set differently than their combination value
    // when the value is 0, we need to backtrack
    private static int pruningCoefficient = 0;
    private static boolean pruningConsistent = false;
    
    // a constant to be used as search index in a non-searched variable
    // it is a big number to prevent such variables affecting searchPointer
    private static final int SEARCHINDEX_NOT_SEARCHED = Integer.MAX_VALUE;
    
    // initialize variable set to given number of variables
    
    public static void Initialize(int count)
    {
        int i = listLength;
        while (i > 0)
        {
            --i;
            list[i].reset();
        }
        if (count > list.length)
        {
            i = list.length;
            list = Arrays.copyOf(list, count);
            while (i < list.length)
            {
                list[i] = new Variable();
                ++i;
            }
        }
        listLength = count;

        searchOrder = null;
    }
    
    public static int getNumVariables()
    {
        return listLength;
    }

    // returns variable by index in the list
    
    public static Variable getVariable(int index)
    {
        return list[index];
    }

    // processes current variable states into their combination values
    // set=true -> just copy the state
    // set=false -> merge state with previous combination

    public static void processCombination(boolean set)
    {
        int i = listLength;
        if (set)
        {
            while (i > 0)
            {
                --i;
                list[i].setToCombination();
            }
        }
        else
        {
            // int cu = combinationsUnequal;
            // int cl = combinationsLeft;
            while (i > 0)
            {
                --i;
                list[i].mergeToCombination();
            }
            // System.out.println("mergeToCombination in: combinationsUnequal: " + cu + " -> " + combinationsUnequal + ", combinationsLeft: " + cl + " -> " + combinationsLeft);
            pruningConsistent = (pruningCoefficient != 0);
        }
    }

    public static boolean isPruningConsistent()
    {
        return pruningConsistent;
    }
    
    // set up new order in which variables are searched
    // it may come in the middle of a search
    // although if that happens, it should be the same subset of variables
    
    public static void setSearchOrder(int [] sortedVarIndex)
    {
        if (searchOrder != null)
        {
            // order already exists - let's unregister all variables
            int i = searchOrder.length;
            while (i > 0)
            {
                --i;
                // mark the variable as not searched
                searchOrder[i].searchIndex = SEARCHINDEX_NOT_SEARCHED;
            }
        }
        // now we can implement new order
        searchOrder = new Variable[sortedVarIndex.length];

        int i = sortedVarIndex.length;
        while (i > 0)
        {
            --i;
            Variable var = list[sortedVarIndex[i]];
            searchOrder[i] = var;
            if (var.searchIndex == SEARCHINDEX_NOT_SEARCHED)
            {
                // put the search order index into the variable
                var.searchIndex = i;
            }
            else
            {
                throw new RuntimeException("Variable is in search list twice");
            }
        }
        // reset searchPointer, too
        searchPointer = 0;
    }
    
    // returns next variable (in search order) to be searched
    // throws an ArrayIndexOutOfBoundsException when there's no more variables
    // that means we got a solution

    public static Variable getVariableToSearch() throws ArrayIndexOutOfBoundsException
    {
        Variable var = searchOrder[searchPointer++];
        while (!var.isUnset())
        {
            var = searchOrder[searchPointer++];
        }
        return var;
    }

    // initialize variables supporting search with pruning by combination

    public static void initializePruning()
    {
        pruningCoefficient = 0;
        //combinationsLeft = 0;
        //combinationsUnequal = 0;
        int i = listLength;
        while (i > 0)
        {
            --i;
            if (!list[i].isCombinationUnset())
            {
                if (list[i].isUnset())
                {
                    //++combinationsLeft;
                    ++pruningCoefficient;
                }
                else
                {
                    if (list[i].state != list[i].combination)
                    {
                        //++combinationsUnequal;
                        ++pruningCoefficient;
                    }
                }
            }
        }
        //pruningConsistent = (combinationsLeft != 0) || (combinationsUnequal != 0);
        pruningConsistent = pruningCoefficient != 0;
    }

    // ----------------------------------------------------------------------------
    // object stuff
    // ----------------------------------------------------------------------------
    
    private int searchIndex = SEARCHINDEX_NOT_SEARCHED;
    private int state = UNSET;
    private int combination = UNSET;
    private Constraint[] constraintList = new Constraint[0];
    private int[] valueList = new int[0];
    
    private Variable()
    {
    }

    // reset the contents of the variable
    
    public void reset()
    {
        searchIndex = SEARCHINDEX_NOT_SEARCHED;
        state = UNSET;
        constraintList = Arrays.copyOf(constraintList, 0);
        valueList = Arrays.copyOf(valueList, 0);
    }
    
    // add a constraint for a variable
    
    public void addConstraint(Constraint constraint, int value)
    {
        int len = constraintList.length;
        constraintList = Arrays.copyOf(constraintList, len + 1);
        constraintList[len] = constraint;
        valueList = Arrays.copyOf(valueList, len + 1);
        valueList[len] = value;
    }
    
    // check for duplicite constraints
    
    public boolean isDuplicite(Constraint constraint)
    {
        int i = constraintList.length;
        while (i > 0)
        {
            --i;
            if (constraint.equals(constraintList[i]))
            {
                return true;
            }
        }
        return false;
    }
    
    // remove a constraint from a variable
    
    public void removeConstraint(Constraint constraint)
    {
        int i = constraintList.length;
        while (i > 0)
        {
            --i;
            if (constraint == constraintList[i])
            {
                int len = constraintList.length - 1;
                constraintList[i] = constraintList[len];
                constraintList = Arrays.copyOf(constraintList, len);
                valueList[i] = valueList[len];
                valueList = Arrays.copyOf(valueList, len);
                return;
            }
        }
        throw new RuntimeException("Attempt to remove constraint not related to the Variable");
    }
    
    // return state of the variable
    
    public boolean isOn()
    {
        return state == ON;
    }

    public boolean isOff()
    {
        return state == OFF;
    }

    public boolean isUnset()
    {
        return state == UNSET;
    }

    // check if there is at least one constraint
    // (there is no point in searching variables with no constraints)
    
    public boolean hasConstraints()
    {
        return constraintList.length > 0;
    }

    // set current state to combination
    
    public void setToCombination()
    {
        combination = state;
    }

    // combine the current state into the combination
    // combination variables are only set if they are set that way in all solutions
    
    public void mergeToCombination()
    {
        if ((state != combination) && (combination != UNSET))
        {
//            if (state == UNSET)
//            {
//                --combinationsLeft;
//            }
//            else
//            {
//                --combinationsUnequal;
//            }
            --pruningCoefficient;
            combination = UNSET;
        }
    }

    // return state of the combination
    
    public boolean isCombinationOn()
    {
        return combination == ON;
    }

    public boolean isCombinationOff()
    {
        return combination == OFF;
    }

    public boolean isCombinationUnset()
    {
        return combination == UNSET;
    }

    // direct setting combination state (load state)
    
    public void setCombinationOn()
    {
        combination = ON;
    }

    public void setCombinationOff()
    {
        combination = OFF;
    }

    public void setCombinationUnset()
    {
        combination = UNSET;
    }

    // set a variable and fire all constraints
    // used only to set the variable to ON or OFF, not to set it to UNSET!
    // returns false if the variable is already set to different value
    // when actually setting the variable, all related constraints are fired
    // they are supposed to put their previous state on state stack
    // and eventual actions on action stack
    
    public boolean setToOn()
    {
        if (state == UNSET)
        {
            // store information that this variable needs to be restored
            Stack.push(this, Stack.FORK_NONE);
            state = ON;
            return true;
        }
        return (state == ON);
    }
    
    public boolean setToOff()
    {
        if (state == UNSET)
        {
            // store information that this variable needs to be restored
            Stack.push(this, Stack.FORK_NONE);
            state = OFF;
            return true;
        }
        return (state == OFF);
    }
    
    // set a new selected variable
    // technically this procedure can decide whether to use ON or OFF
    // for now use just OFF
    
    public boolean setToFork()
    {
        state = OFF;
        // store information that this variable needs to be restored
        Stack.push(this, Stack.FORK_POINT);
        return Stack.propagate();
    }
    
    // setting to ON leaving a FORK point (for load status)
    public boolean setToForkOn()
    {
        state = ON;
        // store information that this variable needs to be restored
        Stack.push(this, Stack.FORK_POINT);
        return Stack.propagate();
    }
    
    // setting to OFF leaving a FORK point (for load status)
    public boolean setToForkOff()
    {
        state = OFF;
        // store information that this variable needs to be restored
        Stack.push(this, Stack.FORK_POINT);
        return Stack.propagate();
    }
    
    // intended to be used on FORK points
    // set the variable to the opposite value
    // this does not leave a fork point as the variable is now not a fork anymore

    public boolean setOpposite()
    {
        state = (ON + OFF) - state;
        // store information that this variable needs to be restored
        Stack.push(this, Stack.FORK_NONE);
        // and initiate propagation
        return Stack.propagate();
    }
    
    
    public void optimize()
    {
        // make a copy of constraint list
        // to ensure nothing will break when constraints unregister etc
        Constraint[] clCopy = Arrays.copyOf(constraintList, constraintList.length);
        int i = clCopy.length;
        while (i > 0)
        {
            --i;
            clCopy[i].optimize();
        }
    }

    // unset a variable as a reaction to stack backtrack
    // the oldState value is irrelevant in this case
    
    @Override
    public void backtrack(int oldState)
    {
        state = UNSET;
        // update search pointer, too
        if (searchPointer > searchIndex)
        {
            searchPointer = searchIndex;
        }
    }

    @Override
    public boolean propagate()
    {
        int i = constraintList.length;
        if (state == OFF)
        {
            while (i > 0)
            {
                --i;
                if (!constraintList[i].fireOff(valueList[i]))
                {
                    return false;
                }
            }
        }
        else
        {
            while (i > 0)
            {
                --i;
                if (!constraintList[i].fireOn(valueList[i]))
                {
                    return false;
                }
            }
        }
        return true;
    }

    // ----------------------------------------------------------------------------

    public boolean setToOnWithPruning()
    {
        if (state == UNSET)
        {
            // store information that this variable needs to be restored
            Stack.push(this, Stack.FORK_NONE);
            state = ON;
            switch(combination)
            {
            case UNSET:
                return pruningConsistent;
            case OFF:
                //--combinationsLeft;
                //++combinationsUnequal;
                // pruningCoefficient -> no change
                // pruningConsistent = true; no need to set it, it must have been true before, too
                return true;
            }
            //--combinationsLeft;
            --pruningCoefficient;
            //pruningConsistent = (combinationsLeft != 0) || (combinationsUnequal != 0); 
            pruningConsistent = (pruningCoefficient != 0); 
            return pruningConsistent;
        }
        return (state == ON);
    }
    
    public boolean setToOffWithPruning()
    {
        if (state == UNSET)
        {
            // store information that this variable needs to be restored
            Stack.push(this, Stack.FORK_NONE);
            state = OFF;
            switch(combination)
            {
            case UNSET:
                return pruningConsistent;
            case ON:
                //--combinationsLeft;
                //++combinationsUnequal;
                // pruningCoefficient -> no change
                // pruningConsistent = true; no need to set it, it must have been true before, too
                return true;
            }
            //--combinationsLeft;
            --pruningCoefficient;
            //pruningConsistent = (combinationsLeft != 0) || (combinationsUnequal != 0); 
            pruningConsistent = (pruningCoefficient != 0); 
            return pruningConsistent;
        }
        return (state == OFF);
    }
    
    // set variable some way, leaving a FORK point on the stack
    
    public boolean setToForkWithPruning()
    {
        Stack.push(this, Stack.FORK_POINT);
        if (combination == UNSET)
        {
            state = OFF;
//            if (pruningConsistent)
//            {
//                return Stack.propagateWithPruning();
//            }
//            return false;
            return pruningConsistent && Stack.propagateWithPruning();
        }
        else
        {
            // it has a combination, so try the other way first
            state = (ON + OFF) - combination;
            //--combinationsLeft;
            //++combinationsUnequal;
            // pruningCoefficient -> no change
            // pruningConsistent = true; no need to set it, it must have been true before, too
            return Stack.propagateWithPruning();
        }
    }
    
    // intended to be used on FORK points
    // set the variable to the opposite value
    // this does not leave a fork point as the variable is now not a fork anymore

    public boolean setOppositeWithPruning()
    {
        if (combination != UNSET)
        {
            if (state == combination)
            {
                //++combinationsUnequal;
                ++pruningCoefficient;
                pruningConsistent = true;
            }
            else
            {
                //--combinationsUnequal;
                --pruningCoefficient;
                //pruningConsistent = (combinationsLeft != 0) || (combinationsUnequal != 0); 
                pruningConsistent = (pruningCoefficient != 0); 
            }
        }
        
        state = (ON + OFF) - state;
        // store information that this variable needs to be restored
        Stack.push(this, Stack.FORK_NONE);

//        if (pruningConsistent)
//        {
//            return Stack.propagateWithPruning();
//        }
//        return false;
        return pruningConsistent && Stack.propagateWithPruning();
    }
    
    // unset a variable as a reaction to stack backtrack
    // the oldState value is irrelevant in this case
    
    @Override
    public void backtrackWithPruning(int oldState)
    {
//        if (combination != UNSET)
//        {
//            ++combinationsLeft;
//            if (state != combination)
//            {
//                --combinationsUnequal;
//            }
//            pruningConsistent = true;
//        }
        if ((combination != UNSET) && (state == combination))
        {
            ++pruningCoefficient;
            pruningConsistent = true;
        }
        state = UNSET;
        // update search pointer, too
        if (searchPointer > searchIndex)
        {
            searchPointer = searchIndex;
        }
    }

    @Override
    public boolean propagateWithPruning()
    {
        int i = constraintList.length;
        if (state == OFF)
        {
            while (i > 0)
            {
                --i;
                if (!constraintList[i].fireOff(valueList[i]))
                {
                    return false;
                }
            }
        }
        else
        {
            while (i > 0)
            {
                --i;
                if (!constraintList[i].fireOn(valueList[i]))
                {
                    return false;
                }
            }
        }
        return true;
    }
}
