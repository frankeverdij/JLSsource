package jls.engine.java;

import java.util.Arrays;

public class LiveColumnConstraint implements Constraint, StackObject
{

    public static final int STATE_EMPTY = 0; // unset or OFF variables only
    public static final int STATE_ON = 1; // at least one ON variable
    public static final int STATE_LOCKED = 2; // locked against any variables turning ON
    
    
    int state;
    int weight;
    LiveLayerConstraint master;
    Variable[] varList; 
    
    public LiveColumnConstraint(LiveLayerConstraint master, Variable[] varList)
    {
        this.master = master;
        this.varList = varList;
        this.state = STATE_EMPTY;
        this.weight = 1;
        int i = varList.length;
        while (i > 0)
        {
            --i;
            varList[i].addConstraint(this, 0);
        }
    }
    
    public void incWeight()
    {
        ++weight;
    }
    
    public int getWeight()
    {
        return weight;
    }
    
    public boolean equals(LiveColumnConstraint cons)
    {
        if (state == cons.state)
        {
            if (varList.length == cons.varList.length)
            {
                int i = varList.length;
                while (i > 0)
                {
                    --i;
                    if (varList[i] != cons.varList[i])
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean fireOff(int stateChange)
    {
        return true;
    }

    @Override
    public boolean fireOn(int stateChange)
    {
        switch (state)
        {
        case STATE_EMPTY:
            Stack.push(this, state);
            state = STATE_ON;
            return master.fire(weight);
        case STATE_LOCKED:
            return false;
        case STATE_ON:
            return true;
        }
        throw new RuntimeException("Unhandled state: " + state);
    }
    
    public void lock()
    {
        switch(state)
        {
        case STATE_ON:
        case STATE_LOCKED:
            return;
        
        case STATE_EMPTY:
            Stack.push(this, state);
            state = STATE_LOCKED;
            return;
        }
        throw new RuntimeException("Unhandled state: " + state);
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
    public void optimize()
    {
        if (master != null)
        {
            if (state == STATE_ON)
            {
                // ON column can be removed completely
                master.removeColumn(this);
                destroy();
            }
            else
            {
                int i = 0;
                int remainingVars = varList.length;
                // remove any set variables
                while (i < remainingVars)
                {
                    if (varList[i].isUnset())
                    {
                        ++i;
                    }
                    else
                    {
                        varList[i].removeConstraint(this);
                        --remainingVars;
                        varList[i] = varList[remainingVars];
                    }
                }
                if (remainingVars == 0)
                {
                    master.removeColumn(this);
                    varList = null;
                    master = null;
                }
                else if (remainingVars < varList.length)
                {
                    varList = Arrays.copyOf(varList, remainingVars);
                }
            }
        }
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
            varList = null;
        }
        master = null;
    }

    @Override
    public boolean propagate()
    {
        if (state == STATE_LOCKED)
        {
            int i = varList.length;
            while (i > 0)
            {
                --i;
                if (!varList[i].setToOff())
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean propagateWithPruning()
    {
        if (state == STATE_LOCKED)
        {
            int i = varList.length;
            while (i > 0)
            {
                --i;
                if (!varList[i].setToOffWithPruning())
                {
                    return false;
                }
            }
        }
        return true;
    }

}
