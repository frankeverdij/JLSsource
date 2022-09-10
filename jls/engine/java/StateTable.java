package jls.engine.java;

import java.util.Arrays;
import java.util.TreeMap;

public class StateTable
{

    // base state for given number of variables
    // all variables are UNSET
    // that is 0, 1, 11, 111, ... 1111111111 in base-3
    public static final int [] BASE_STATE = {0, 1, 4, 13, 40, 121, 364, 1093, 3280, 9841, 29524};

    // state value change for variable of given order
    // variable 0 uses state change value stateValue[0] etc
    // values are 1, 10, 100, ... 100000000 in base-3
    // this value is added to the base (unset) state to get state where the variable is ON (2 in base-3)
    // and is subtracted from the state to get state where the variable is OFF (0 in base-3)
    public static final int [] STATE_CHANGE_VALUE = {1, 3, 9, 27, 81, 243, 729, 2187, 6561, 19683};

    // number of states for given number of variables
    private static final int [] TOTAL_STATES = {1, 3, 9, 27, 81, 243, 729, 2187, 6561, 19683, 59049};

    // special state values
    private static final int STATE_UNSET = -2;
    private static final int STATE_INVALID = -1;
    
    // actions to take in state
    public static final int CHANGE_NONE = 0;
    public static final int CHANGE_ON = 16;
    public static final int CHANGE_OFF = 32;
    private static final int CHANGE_MASK = CHANGE_ON | CHANGE_OFF;
    private static final int VAR_MASK = ~CHANGE_MASK;

    private static TreeMap<Long, StateTable> tableMap = new TreeMap<Long, StateTable>();
    
    private static boolean[] birth = new boolean[9];
    private static boolean[] survival = new boolean[9];
    
    public static void Initialize(boolean[] newBirth, boolean[] newSurvival)
    {
        boolean change = false;
        for (int i = 0; i < 9; i++)
        {
            if (newBirth[i] != birth[i])
            {
                change = true;
                birth[i] = newBirth[i];
            }
            if (newSurvival[i] != survival[i])
            {
                change = true;
                survival[i] = newSurvival[i];
            }
        }
        if (change)
        {
            tableMap.clear();
        }
    }

    public static StateTable getTable(int type, int onNeighbors, int offNeighbors, int[] varCounts)
    {
        // first let's calculate a signature for the table
        // let's make it human readable, i.e. in decimal
        // VVVVVVVVVVUDTT
        // TT - type (0..12)
        // D .. number of OFF neighbors
        // U .. number of ON neighbors
        // V...V ... for each variable (last on left, first on right), number how many times is it in neighbors
        // ... these include variables given by type (first one or two from right)
        // note: no collision may happen as "extra" variables are all at least 1

        long signature = 0;
        
        int i = varCounts.length;
        while (i > 0)
        {
            --i;
            signature = signature * 10 + varCounts[i];
        }
        signature = signature * 10 + onNeighbors;
        signature = signature * 10 + offNeighbors;
        signature = signature * 100 + type;
        
        // signature finished
        
        StateTable table = tableMap.get(signature);
        
        if (table == null)
        {
            // table doesn't exist, duh
            // let's make one
            
            table = new StateTable(type, onNeighbors, offNeighbors, varCounts);
//            if (table.table == null)
//            {
//                System.out.println("Created new state table for signature " + signature + " (degraded)");
//            }
//            else
//            {
//                System.out.println("Created new state table for signature " + signature + " size = " + table.table.length);
//            }
            
            tableMap.put(signature, table);
        }
        
        return table;
    }
    
    // object stuff (non-static)
    
    // the state table itself
    // goes through several phases
    // initial:
    //   each state contains STATE_UNSET value
    // later: 
    //   each state contains "invalid" if the state is invalid, 
    //   or id of following state if there is anything common on all non-invalid following states
    //   or its own id if the state is okay and there are no immediate restrictions on variable values
    // finally:
    //   each state contains "invalid" value if it is invalid
    //   or identifier of variable to change and type of change (to ON or OFF)
    //   ... each state comes with only one variable change as that will send it to another state where another change is waiting
    //   or zero if there is nothing to change
    private int[] table;
    
    // set if the table is degraded (i.e. all states ok or all states invalid)
    private boolean degraded = false;

    // this is used if the table is degraded - gives value of all states
    private boolean valid = false;

    // constructor
    // type ... specifies cone/table type, see Cone.TYPE_ definitions
    // onNeighbors ... number of neighbor cells with are ON initially (fixed to ON)
    // offNeighbors ... number of neighbor cells which are OFF initially (fixed to OFF)
    // varCounts ... for each variable number of neighbors set by it
    private StateTable(int type, int onNeighbors, int offNeighbors, int[] varCounts)
    {
        table = new int[TOTAL_STATES[varCounts.length]];
        // fill the table with "UNSET" marker
        Arrays.fill(table, STATE_UNSET);
        
        int state = setupState(varCounts.length, BASE_STATE[varCounts.length], type, onNeighbors, offNeighbors, varCounts);
        
        // now we've went through first stage of table creation but we need to inspect it further
        // if all states are invalid then the root will be invalid as well
        
        if (state == STATE_INVALID)
        {
            // let's forget the table, we won't need it
            table = null;
            degraded = true;
            valid = false;
            return;
        }
        
        // inspecting for 'ok but still degraded' is harder
        // we need to check all states
        
        int i = table.length;
        boolean allValid = true;
        while (i > 0)
        {
            --i;
            if (table[i] == STATE_INVALID)
            {
                allValid = false;
                i = 0;
            }
        }
        if (allValid)
        {
            // again somewhat useless
            table = null;
            degraded = true;
            valid = true;
            return;
        }

        // and last, we need to go through the table once more and change states
        // so that they suggest what should be changed
        // if a change is enforced by the state, that is
        
        i = table.length;
        while (i > 0)
        {
            --i;
            if (table[i] != STATE_INVALID)
            {
                // invalid states are not interesting now, they may stay as they are
                if (table[i] != i)
                {
                    // Now THIS is interesting as that's state that leads to a different state
                    // let's find out which variable is changed
                    // we need just one
                    int newState = table[i];
                    int varNo = 0;
                    int order = 1;
                    while (((newState / order) % 3) == ((i / order) % 3))
                    {
                        ++varNo;
                        order *= 3;
                    }
                    // note: there must be a difference as they are different
                    // sanity check - original state must be 1 (unset), new must be not 1 (ON or OFF)
                    if (((i / order) % 3) != 1)
                    {
                        throw new RuntimeException("State change requested on set variable");
                    }
                    newState = (newState / order) % 3;
                    
                    // number of variable to set is in j
                    if (newState == 0)
                    {
                        table[i] = varNo + CHANGE_OFF;
                    } else {
                        table[i] = varNo + CHANGE_ON;
                    }
                } else {
                    // no change requested
                    table[i] = CHANGE_NONE;
                }
            }
        }
        // we're finally done!
    }
    
    // recursive setting up of the whole state table
    // unsetVars - number of variables unset in the inspected state
    // state - the state, in "normal" encoding (base-3, 0 = OFF, 1 = UNSET, 2 = ON)
    // type - type of the table, see Cone.TYPE_ values. Used at final state resolution when all variables are set
    // onNeighbors - number of ON neighbor cells in this state with current variable settings
    // offNeighbors - ditto for OFF neighbors
    // varCounts - number of neighbors set by each variable
    //
    // returns state value
    
    private int setupState(int unsetVars, int state, int type, int onNeighbors, int offNeighbors, int [] varCounts)
    {
        if (table[state] == STATE_UNSET)
        {
            // state is not set so let's inspect it and set it up
            if (unsetVars == 0)
            {
                // all variables are set, so let's inspect it as final state
                int top; // 0 = off, 1 = on
                int middle; // 0 = off, 1 = on, 2 = unset
                switch (type)
                {
                case Cone.TYPE_V0V1:
                    top = (state % 3) / 2; // v0 state is 0 (OFF) or 2 (ON)
                    middle = ((state / 3) % 3) / 2; // v1 state ditto
                    break;
                case Cone.TYPE_V0V0:
                    top = (state % 3) / 2; // v0 state is 0 (OFF) or 2 (ON)
                    middle = top;
                    break;
                case Cone.TYPE_V0ON:
                    top = (state % 3) / 2; // v0 state is 0 (OFF) or 2 (ON)
                    middle = 1;
                    break;
                case Cone.TYPE_V0OFF:
                    top = (state % 3) / 2; // v0 state is 0 (OFF) or 2 (ON)
                    middle = 0;
                    break;
                case Cone.TYPE_V0UNS:
                    top = (state % 3) / 2; // v0 state is 0 (OFF) or 2 (ON)
                    middle = 2;
                    break;
                case Cone.TYPE_ONV0:
                    top = 1;
                    middle = (state % 3) / 2; // v0 state is 0 (OFF) or 2 (ON)
                    break;
                case Cone.TYPE_ONON:
                    top = 1;
                    middle = 1;
                    break;
                case Cone.TYPE_ONOFF:
                    top = 1;
                    middle = 0;
                    break;
                case Cone.TYPE_ONUNS:
                    top = 1;
                    middle = 2;
                    break;
                case Cone.TYPE_OFFV0:
                    top = 0;
                    middle = (state % 3) / 2; // v0 state is 0 (OFF) or 2 (ON)
                    break;
                case Cone.TYPE_OFFON:
                    top = 0;
                    middle = 1;
                    break;
                case Cone.TYPE_OFFOFF:
                    top = 0;
                    middle = 0;
                    break;
                case Cone.TYPE_OFFUNS:
                    top = 0;
                    middle = 2;
                    break;
                default:
                    throw new RuntimeException("Unhandled state table type: " + type);
                }
                int i;
                boolean valid = false; // invalid by default, will turn to valid if rules allow
                switch (middle)
                {
                case 0: // middle cell is OFF so we're looking at the birth table
                    if (top == 0) // top is OFF
                    {
                        i = 8 - offNeighbors;
                        while (i >= onNeighbors)
                        {
                            if (!birth[i])
                            {
                                // this number of ON neighbors does not support birth
                                // so it's possible to have top cell OFF
                                valid = true; 
                            }
                            --i;
                        }
                    } else { // top is ON
                        i = 8 - offNeighbors;
                        while (i >= onNeighbors)
                        {
                            if (birth[i])
                            {
                                // this number of ON neighbors supports birth
                                // so it's possible to have top cell ON
                                valid = true; 
                            }
                            --i;
                        }
                    }
                    break;
                case 1: // middle cell is ON so we're looking at the survival table
                    if (top == 0) // top is OFF
                    {
                        i = 8 - offNeighbors;
                        while (i >= onNeighbors)
                        {
                            if (!survival[i])
                            {
                                // this number of ON neighbors does not support survival
                                // so it's possible to have top cell OFF
                                valid = true; 
                            }
                            --i;
                        }
                    } else { // top is ON
                        i = 8 - offNeighbors;
                        while (i >= onNeighbors)
                        {
                            if (survival[i])
                            {
                                // this number of ON neighbors supports survival
                                // so it's possible to have top cell ON
                                valid = true; 
                            }
                            --i;
                        }
                    }
                    break;
                case 2: // middle cell is UNSET so we need to look at both birth and survival, either will work for us
                    if (top == 0) // top is OFF
                    {
                        i = 8 - offNeighbors;
                        while (i >= onNeighbors)
                        {
                            if (!birth[i])
                            {
                                // this number of ON neighbors does not support birth
                                // so it's possible to have top cell OFF
                                valid = true; 
                            }
                            else if (!survival[i])
                            {
                                // this number of ON neighbors does not support survival
                                // so it's possible to have top cell OFF
                                valid = true; 
                            }
                            --i;
                        }
                    } else { // top is ON
                        i = 8 - offNeighbors;
                        while (i >= onNeighbors)
                        {
                            if (birth[i])
                            {
                                // this number of ON neighbors supports birth
                                // so it's possible to have top cell ON
                                valid = true; 
                            }
                            else if (survival[i])
                            {
                                // this number of ON neighbors supports survival
                                // so it's possible to have top cell ON
                                valid = true; 
                            }
                            --i;
                        }
                    }
                    break;
                }
                // now set up the resulting state
                if (valid)
                {
                    // point to itself if it is correct
                    table[state] = state;
                } else {
                    // invalid marker if it is not valid
                    table[state] = STATE_INVALID;
                }
                // and we're done
            } else {
                // not all variables are set, so let's set them one by one and merge results
                int i = 0;
                int order = 1;
                int combination = STATE_INVALID;
                while (i < varCounts.length)
                {
                    int set = (state / order) % 3;
                    if (set == 1)
                    {
                        // this order variable is not set so let's check what happens if we set it ON or OFF
                        int onState = setupState(unsetVars - 1, state + STATE_CHANGE_VALUE[i], type, onNeighbors + varCounts[i], offNeighbors, varCounts);
                        int offState = setupState(unsetVars - 1, state - STATE_CHANGE_VALUE[i], type, onNeighbors, offNeighbors + varCounts[i], varCounts);
                        combination = mergeState(varCounts.length, state, combination, onState);
                        combination = mergeState(varCounts.length, state, combination, offState);
                    }
                    order *= 3;
                    ++i;
                }
                // we've tried all variables one by one and we got a combined result
                table[state] = combination;
            }
        }
        // in the end return the state value
        return table[state];
    }
    
    // merge non-failed state to the combination so far
    // numVars - number of variables (both set and unset)
    // state - the inspected state. If at least one sucessive valid state exists, this is the worst value we can get
    // combination - combined result of successive states so far. Set to INVALID at the start and until first valid successive state is found
    // addedState - new state to be added to the combination. May be invalid
    // returns new combination
    private int mergeState(int numVars, int state, int combination, int addedState)
    {
        if (combination == state)
        {
            // it can't get any worse
            return state;
        }
        if (addedState == STATE_INVALID)
        {
            // nothing to merge
            return combination;
        }
        if (combination == STATE_INVALID)
        {
            // our first success
            return addedState;
        }
        // now we actually need to combine
        int i = 0;
        int order = 1;
        while (i < numVars)
        {
            // let's check the value of the variable at the current order
            int stc = (combination / order) % 3;
            if (stc != 1)
            {
                // this variable is set in the combination
                if (stc != (addedState / order) % 3)
                {
                    // and the added value is different
                    // so let's clear it
                    combination = ((combination / (order * 3)) * 3 + 1) * order + (combination % order);
                }
            }
            order *= 3;
            ++i;
        }
        return combination;
    }
    
    // returns true if the table is degraded and no valid state exists in it
    public boolean isAllInvalid()
    {
        return degraded && !valid;
    }
    
    // returns true if the table is degraded and all states are valid
    public boolean isAllValid()
    {
        return degraded && valid;
    }
    
    // checks validity of the state
    public boolean isInvalid(int state)
    {
        return table[state] == STATE_INVALID;
    }
    
    // returns one of the CHANGE_ constants
    // assumes the state is valid
    public int getChangeAction(int state)
    {
        return table[state] & CHANGE_MASK;
    }
    
    // returns number of variable to be changed
    // assumes the state is valid and it is about changing the variable
    public int getChangeVar(int state)
    {
        return table[state] & VAR_MASK;
    }


}