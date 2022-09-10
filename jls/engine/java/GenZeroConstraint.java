package jls.engine.java;

import java.util.Arrays;

public class GenZeroConstraint implements Constraint, StackObject
{
    // state contains number of variables left which can be set ON
    int state;
    // references to all variables
    Variable[] varList;
    
    public GenZeroConstraint(int initialState)
    {
        state = initialState;
    }
    
    public void setVarList(Variable[] newList)
    {
        varList = newList;
    }

    public void destroy()
    {
        if (varList != null)
        {
            int i = varList.length;
            while (i > 0)
            {
                --i;
                varList[i].removeConstraint(this);
            }
        }
        varList = null;
    }

    @Override
    public boolean fireOff(int stateChange)
    {
        // no need to store anything on stack
        return state >= 0;
    }

    @Override
    public boolean fireOn(int stateChange)
    {
        if (state < stateChange)
        {
            return false;
        }
        Stack.push(this, state);
        state -= stateChange;
        return true;
    }

    @Override
    public void optimize()
    {
        // pass through related variables
        // and remove any of them which are set
        int numVars = varList.length;
        int i = 0;
        while (i < numVars)
        {
            if (varList[i].isUnset())
            {
                // variable is not set, let's go to next variable
                ++i;
            }
            else
            {
                // variable is set, let's remove it
                varList[i].removeConstraint(this);
                // and replace it by the last variable on the list
                // (order is unimportant)
                --numVars;
                varList[i] = varList[numVars];
            }
        }
        // reduce the array if we removed any variables
        if (numVars < varList.length)
        {
            varList = Arrays.copyOf(varList, numVars);
        }
    }

    @Override
    public void backtrack(int oldState)
    {
        state = oldState;
    }

    @Override
    public void backtrackWithPruning(int oldState)
    {
        state = oldState;
    }

    @Override
    public boolean propagate()
    {
        if (state == 0)
        {
            int i = varList.length;
            while (i > 0)
            {
                --i;
                if (varList[i].isUnset())
                {
                    if (!varList[i].setToOff())
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean propagateWithPruning()
    {
        if (state == 0)
        {
            int i = varList.length;
            while (i > 0)
            {
                --i;
                if (varList[i].isUnset())
                {
                    if (!varList[i].setToOffWithPruning())
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
