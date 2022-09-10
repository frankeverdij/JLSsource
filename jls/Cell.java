package jls;

public class Cell
{
    public static final byte STATE_OFF = 0;
    public static final byte STATE_ON = 1;
    public static final byte STATE_EMPTY = 2;
    public static final byte STATE_MASK = 3;
    public static final byte STATE_UMASK = ~STATE_MASK;

    public static final byte TYPE_NORMAL = 0;
    public static final byte TYPE_UNCHECKED = 4;
    public static final byte TYPE_UNSET = 8;
    public static final byte TYPE_MASK = 12;
    public static final byte TYPE_UMASK = ~TYPE_MASK;
    
    public static final byte FROZEN_OFF = 0;
    public static final byte FROZEN_ON = 16;
    public static final byte FROZEN_MASK = 16;
    public static final byte FROZEN_UMASK = ~FROZEN_MASK;
    
    public static final byte DIVERSE_MASK = STATE_MASK | TYPE_MASK | FROZEN_MASK;
    
    public static final byte ERROR_OFF = 0;
    public static final byte ERROR_ON = 32;
    public static final byte ERROR_MASK = 32;
    public static final byte ERROR_UMASK = ~ERROR_MASK;
    
    public static final byte INIT_STATE = STATE_EMPTY | TYPE_NORMAL | FROZEN_OFF | ERROR_OFF;

    public static final byte CELL_EMPTY     = STATE_EMPTY | TYPE_NORMAL    | FROZEN_OFF | ERROR_OFF;
    public static final byte CELL_OFF       = STATE_OFF   | TYPE_NORMAL    | FROZEN_OFF | ERROR_OFF;
    public static final byte CELL_ON        = STATE_ON    | TYPE_NORMAL    | FROZEN_OFF | ERROR_OFF;
    public static final byte CELL_UNCHECKED = STATE_EMPTY | TYPE_UNCHECKED | FROZEN_OFF | ERROR_OFF;
    public static final byte CELL_UNSET     = STATE_EMPTY | TYPE_UNSET     | FROZEN_OFF | ERROR_OFF;
    public static final byte CELL_FROZEN    = STATE_EMPTY | TYPE_NORMAL    | FROZEN_ON  | ERROR_OFF;
    
    private Cell()
    {
        // this will never be instantiated
    }
    
    public static byte getType(byte cellValue)
    {
        return (byte)(cellValue & TYPE_MASK);
    }

    public static byte setType(byte cellValue, byte type)
    {
        return (byte)((cellValue & TYPE_UMASK) | type);
    }
    
    public static boolean isUnchecked(byte cellValue)
    {
        return (cellValue & TYPE_MASK) == TYPE_UNCHECKED;
    }

    public static boolean isUnset(byte cellValue)
    {
        return (cellValue & TYPE_MASK) == TYPE_UNSET;
    }

    public static boolean isError(byte cellValue)
    {
        return (cellValue & ERROR_MASK) != 0;
    }

    public static byte setError(byte cellValue, boolean error)
    {
        if (error)
        {
            return (byte)(cellValue | ERROR_ON);
        }
        else
        {
            return (byte)(cellValue & ERROR_UMASK);
        }
    }

    public static byte setError(byte cellValue)
    {
        return (byte)(cellValue | ERROR_ON);
    }

    public static byte getState(byte cellValue)
    {
        return (byte)(cellValue & STATE_MASK);
    }
    
    public static boolean isEmpty(byte cellValue)
    {
        return (cellValue & STATE_MASK) == STATE_EMPTY;
    }

    public static boolean isOn(byte cellValue)
    {
        return (cellValue & STATE_MASK) == STATE_ON;
    }

    public static boolean isOff(byte cellValue)
    {
        return (cellValue & STATE_MASK) == STATE_OFF;
    }

    public static byte setState(byte cellValue, byte state)
    {
        return (byte)((cellValue & STATE_UMASK) | state);
    }

    public static boolean isFrozen(byte cellValue)
    {
        return (cellValue & FROZEN_MASK) != 0;
    }

    public static byte setFrozen(byte cellValue, boolean frozen)
    {
        if (frozen)
        {
            return (byte)(cellValue | FROZEN_ON);
        }
        else
        {
            return (byte)(cellValue & FROZEN_UMASK);
        }
    }
}
