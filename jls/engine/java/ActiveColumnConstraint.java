package jls.engine.java;

import java.util.Arrays;

public class ActiveColumnConstraint implements Constraint, StackObject
{

    public static final int STATE_EMPTY = 0;
    public static final int STATE_OFF = 1;
    public static final int STATE_ON = 2;
    public static final int STATE_ACTIVE = 3;
    public static final int STATE_LOCKED_EMPTY = 4;
    public static final int STATE_LOCKED_ON = 5;
    public static final int STATE_LOCKED_OFF = 6;
    
    
    int state;
    ActiveLayerConstraint master;
    Variable[] varList; 
    int weight;

    public ActiveColumnConstraint(ActiveLayerConstraint master, int state, Variable[] varList)
    {
        this.state = state;
        this.master = master;
        this.varList = varList;
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
    
    public boolean equals(ActiveColumnConstraint cons)
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
        switch(state)
        {
        case STATE_ACTIVE:
            return true;
        
        case STATE_OFF:
            return true;

        case STATE_ON:
            Stack.push(this, state);
            state = STATE_ACTIVE;
            return master.fire(weight);
        
        case STATE_EMPTY:
            Stack.push(this, state);
            state = STATE_OFF;
            return true;

        case STATE_LOCKED_EMPTY:
            Stack.push(this, state);
            state = STATE_LOCKED_OFF;
            return true;
        
        case STATE_LOCKED_OFF:
            return true;
        
        case STATE_LOCKED_ON:
            return false;
        }
        throw new RuntimeException("Unhandled state: " + state);
    }

    @Override
    public boolean fireOn(int stateChange)
    {
        switch(state)
        {
        case STATE_ACTIVE:
            return true;
        
        case STATE_ON:
            return true;

        case STATE_OFF:
            Stack.push(this, state);
            state = STATE_ACTIVE;
            return master.fire(weight);
        
        case STATE_EMPTY:
            Stack.push(this, state);
            state = STATE_ON;
            return true;

        case STATE_LOCKED_EMPTY:
            Stack.push(this, state);
            state = STATE_LOCKED_ON;
            return true;
        
        case STATE_LOCKED_ON:
            return true;
        
        case STATE_LOCKED_OFF:
            return false;
        }
        throw new RuntimeException("Unhandled state: " + state);
    }
    
    public void lock()
    {
        switch(state)
        {
        case STATE_ACTIVE:
            return;
        
        case STATE_ON:
            Stack.push(this, state);
            state = STATE_LOCKED_ON;
            return;

        case STATE_OFF:
            Stack.push(this, state);
            state = STATE_LOCKED_OFF;
            return;
        
        case STATE_EMPTY:
            Stack.push(this, state);
            state = STATE_LOCKED_EMPTY;
            return;
        case STATE_LOCKED_EMPTY:
        case STATE_LOCKED_ON:
        case STATE_LOCKED_OFF:
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
            if (state == STATE_ACTIVE)
            {
                // active column can be removed completely
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
        switch (state)
        {
        case STATE_LOCKED_ON:
            {
                int i = varList.length;
                while (i > 0)
                {
                    --i;
                    if (!varList[i].setToOn())
                    {
                        return false;
                    }
                }
            }
            return true;
        case STATE_LOCKED_OFF:
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
        return true;
    }

    @Override
    public boolean propagateWithPruning()
    {
        switch (state)
        {
        case STATE_LOCKED_ON:
            {
                int i = varList.length;
                while (i > 0)
                {
                    --i;
                    if (!varList[i].setToOnWithPruning())
                    {
                        return false;
                    }
                }
            }
            return true;
        case STATE_LOCKED_OFF:
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
        return true;
    }

}
