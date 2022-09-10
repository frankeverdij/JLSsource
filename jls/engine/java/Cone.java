package jls.engine.java;

public class Cone implements Constraint, StackObject
{
    // a cone is a pyramid of 10 cells consisting of
    // 1 TOP cell (result of previous generation)
    // 1 MIDDLE cell (previous generation of TOP cell)
    // 8 NEIGHBOR cells (neighbors of MIDDLE)
    
    // each cell may be ON, OFF, UNSET, or VARIABLE
    // VARIABLE cell has a corresponding Variable object

    // cone types based on how TOP and MIDDLE cells are set

    public static final int TYPE_V0V1   = 0; // top = var0, middle = var1
    public static final int TYPE_V0V0   = 1; // top = middle = var0
    public static final int TYPE_V0ON   = 2; // top = var0, middle = ON
    public static final int TYPE_V0OFF  = 3; // top = var0, middle = OFF
    public static final int TYPE_V0UNS  = 4; // top = var0, middle = UNSET
    public static final int TYPE_ONV0   = 5; // top = ON, middle = var0
    public static final int TYPE_ONON   = 6; // top = ON, middle = ON
    public static final int TYPE_ONOFF  = 7; // top = ON, middle = OFF
    public static final int TYPE_ONUNS  = 8; // top = ON, middle = UNSET
    public static final int TYPE_OFFV0  = 9; // top = OFF, middle = var0
    public static final int TYPE_OFFON  = 10; // top = OFF, middle = ON
    public static final int TYPE_OFFOFF = 11; // top = OFF, middle = OFF
    public static final int TYPE_OFFUNS = 12; // top = OFF, middle = UNSET
    

    // cone factory
    // creates a cone, and integrates it into search structure
    // returns false if the cone is in inconsistent state (no cone created in such case)
    //
    // type ... cone type, see list of types above
    // onNeighbors, offNeighbors ... number of neighbors set to ON resp. OFF state (not variables)
    // variable ... list of variables used for unset cells. Each variable is present only once
    // numInNeighbors ... for each variable, number how many times it appears in neighbors
    //
    // it is responsibility of the caller to always provide variable references in consistent manner
    // to allow detection and removal of duplicite cones
    // depending on type, v0 and v1 might be fixed
    // but the rest should be always sorted the same way

    public static boolean add(int type, int onNeighbors, int offNeighbors, Variable[] variableList, int[] numInNeighbors)
    {
        // first sort variables by their count in neighbors (from highest to lowest)
        // skip fixed variables (v0 or v1)
        // to ensure consistent StateTable signature
        
        // future optimization: we may leave out any already set variables
        // but that might hinder the ability to remove duplicite cones
        // so let's leave it as is for now
                
        int b;
        switch(type)
        {
        case TYPE_V0V1:
            // first two variables are fixed
            b = 2;
            break;
        case TYPE_V0V0:
        case TYPE_V0ON:
        case TYPE_V0OFF:
        case TYPE_V0UNS:
        case TYPE_ONV0:
        case TYPE_OFFV0:
            // first variable is fixed
            b = 1;
            break;
        default:
            b = 0;
        }
        int i = b;
        // bubblesort, but on up to 10 items
        // and not to be used often
        // it keeps order of items of the same value, too
        while (i < variableList.length - 1)
        {
            if (numInNeighbors[i] < numInNeighbors[i + 1])
            {
                int t = numInNeighbors[i];
                numInNeighbors[i] = numInNeighbors[i + 1];
                numInNeighbors[i + 1] = t;
                Variable s = variableList[i];
                variableList[i] = variableList[i + 1];
                variableList[i + 1] = s;
                
                if (i > b)
                {
                    // let's try to "bubble" the last switched item further down
                    --i;
                } else {
                    // we're at the bottom so let's go up instead
                    ++i;
                }
            } else {
                // no change needed, let's go up
                ++i;
            }
        }
        
        // now variables are in their final order
        // let's get the state table
        StateTable table = StateTable.getTable(type, onNeighbors, offNeighbors, numInNeighbors);
        
        if (table.isAllInvalid())
        {
            // we can't make the cone in this situation
            return false;
        }
        
        if (table.isAllValid())
        {
            // there's no reason to continue making the cone, it won't constrain anything
            return true;
        }
        
        // now let's enumerate cone's initial state
        
        int state = StateTable.BASE_STATE[variableList.length];
        i = variableList.length;
        while (i > 0)
        {
            --i;
            if (variableList[i].isOn())
            {
                state += StateTable.STATE_CHANGE_VALUE[i];
            } 
            else if (variableList[i].isOff()) 
            {
                state -= StateTable.STATE_CHANGE_VALUE[i];
            }
        }
        
        // and check if the state is ok
        
        if (table.isInvalid(state))
        {
            // no point to continue making the cone
            return false;
        }
        
        if (variableList.length < 1)
        {
            // sanity check - we shouldn't get here with no variables
            // (it would be all invalid or all valid)
            throw new RuntimeException("No variables to continue processing - ON:" + onNeighbors + " OFF:" + offNeighbors + " TYPE:" + type + " VARS:" + variableList.length + " NUMS: " + numInNeighbors.length);
        }

        Cone cone = new Cone(table, state, variableList);

        // check if this same constraint isn't already in place
        // (may happen in various symmetries)
        // it's enough to check with just one variable
        if (variableList[0].isDuplicite(cone))
        {
            return true;
        }
        
        // now it's time to add all the variable references
        
        int unsetVars = 0;
        i = variableList.length;
        while (i > 0)
        {
            --i;
            variableList[i].addConstraint(cone, StateTable.STATE_CHANGE_VALUE[i]);
            if (variableList[i].isUnset())
            {
                ++unsetVars;
            }
        }

        // now we have to check changes enforced by the cone
        // and project them to variables
        // but if it fails, we need to backtrack
        
        // reset the stack and add new items 
        Stack.resetAndAdjust(unsetVars);

        // a "hack": perform state change without actually changing the state, just to bring out the consequences
        
        if (cone.propagate())
        {
            // state is okay, now let's propagate it 
            if (Stack.propagate())
            {
                // all is bright, we're done
                Stack.optimize();
                return true;
            }
        }
        // nope it doesn't work
        // let's backtrack
        Stack.backtrackAll();
        // and remove the cone from the structure
        i = variableList.length;
        while (i > 0)
        {
            --i;
            variableList[i].removeConstraint(cone);
        }
        // reset the stack size back
        Stack.resetAndAdjust(-unsetVars);
        return false;
    }
    
    //
    // non-static stuff
    //
    
    private StateTable stateTable;
    private int state;
    private Variable[] variableList = null;
    
    private Cone(StateTable stateTable, int state, Variable[] variableList)
    {
        this.stateTable = stateTable;
        this.state = state;
        this.variableList = variableList;
    }
    
    // compare with another cone (or constraint) to find duplicities
    
    public boolean equals(Object o)
    {
        if (o instanceof Cone)
        {
            Cone c = (Cone) o;
            if (c.stateTable == stateTable) // same stateTable automatically means the same number of variables 
            {
                int i = variableList.length;
                while (i > 0)
                {
                    --i;
                    if (c.variableList[i] != variableList[i])
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    
    // from Constraint interface
    @Override
    public boolean fireOn(int stateChange)
    {
        // it should never happen that the variable sending us change is already set in our state vector
        // so let's just make the change
        int newState = state + stateChange;
        if (stateTable.isInvalid(newState))
        {
            return false;
        }
        Stack.push(this, state);
        state = newState;
        return true;
    }
    
    // from Constraint interface
    @Override
    public boolean fireOff(int stateChange)
    {
        int newState = state - stateChange;
        if (stateTable.isInvalid(newState))
        {
            return false;
        }
        Stack.push(this, state);
        state = newState;
        return true;
    }

    
    // from StackObject interface
    @Override
    public void optimize()
    {
        // technically we may remove references to any set variables
        // and change the cone state table appropriately
        // and of course update registration to remaining variables (they will change the state differently)
        // for starters let's just remove any cones with all variables set
        
        if (variableList == null)
        {
            // don't try to optimize with no variables - we're already optimized
            return;
        }

        int i = variableList.length;
        while (i > 0)
        {
            --i;
            if (variableList[i].isUnset())
            {
                // there is an unset variable, so no optimization
                return;
            }
        }
        
        i = variableList.length;
        while (i > 0)
        {
            --i;
            variableList[i].removeConstraint(this);
        }
        
        // clear up variable list for the case we get here again from another stack item
        variableList = null;
    }

    // from StackObject interface
    @Override
    public void backtrack(int oldState)
    {
        state = oldState;
    }

    // from StackObject interface
    @Override
    public void backtrackWithPruning(int oldState)
    {
        state = oldState;
    }

    // from StackObject interface
    @Override
    public boolean propagate()
    {
        switch (stateTable.getChangeAction(state))
        {
        case StateTable.CHANGE_NONE:
            return true;
        case StateTable.CHANGE_ON:
            return variableList[stateTable.getChangeVar(state)].setToOn();
        }
        return variableList[stateTable.getChangeVar(state)].setToOff();
    }

    // from StackObject interface
    @Override
    public boolean propagateWithPruning()
    {
        switch (stateTable.getChangeAction(state))
        {
        case StateTable.CHANGE_NONE:
            return true;
        case StateTable.CHANGE_ON:
            return variableList[stateTable.getChangeVar(state)].setToOnWithPruning();
        }
        return variableList[stateTable.getChangeVar(state)].setToOffWithPruning();
    }
}
