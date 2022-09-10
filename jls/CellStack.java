package jls;

public class CellStack
{
    public static final byte PERIOD_MASK = 7; 
    public static final byte PERIOD_UMASK = ~PERIOD_MASK; 

    public static final byte ERROR_OFF = 0;
    public static final byte ERROR_ON = 8;
    public static final byte ERROR_MASK = 8;
    public static final byte ERROR_UMASK = ~ERROR_MASK;

    public static final byte DIVERSE_OFF = 0; 
    public static final byte DIVERSE_ON = 16;
    public static final byte DIVERSE_MASK = 16;
    public static final byte DIVERSE_UMASK = ~DIVERSE_MASK;

    public static final byte CHANGED_OFF = 0; 
    public static final byte CHANGED_ON = 32;
    public static final byte CHANGED_MASK = 32;
    public static final byte CHANGED_UMASK = ~CHANGED_MASK;
    
    public static final byte INIT_STATE = ERROR_OFF | DIVERSE_OFF | CHANGED_ON;
    
    private CellStack()
    {
        // don't instantiate
    }

    public static boolean isError(byte stackValue)
    {
        return (stackValue & ERROR_MASK) != 0;
    }

    public static boolean isDiverse(byte stackValue)
    {
        return (stackValue & DIVERSE_MASK) != 0;
    }
    
    public static boolean isDiverse(byte stackValue1, byte stackValue2)
    {
        return 0 != ((stackValue1 ^ stackValue2) & DIVERSE_MASK);
    }

    public static int getPeriodIndex(byte stackValue)
    {
        return stackValue & PERIOD_MASK;
    }

    public static byte setPeriodIndex(byte stackValue, int index)
    {
        return (byte)((stackValue & PERIOD_UMASK) | index);
    }

    public static byte setChanged(byte stackValue)
    {
        return (byte)(stackValue | CHANGED_ON);
    }
    
    public static byte resetChanged(byte stackValue)
    {
        return (byte)(stackValue & CHANGED_UMASK);
    }
    
    public static boolean isChanged(byte stackValue)
    {
        return (stackValue & CHANGED_MASK) != 0;
    }
    
    public static int getLayerNo(SearchOptions searchOptions, int col, int row)
    {
        if (searchOptions.isUseLayers())
        {
            int dx = col - searchOptions.getLayersStartColumn();
            int dy = row - searchOptions.getLayersStartRow();

            switch(searchOptions.getLayersType())
            {
            case SearchOptions.LAYER_BOX:
                return Math.max(Math.abs(dx), Math.abs(dy));
            case SearchOptions.LAYER_COLUMN:
                return dx;
            case SearchOptions.LAYER_DIAG_BWD:
                return dx + dy;
            case SearchOptions.LAYER_DIAG_FWD:
                return dx - dy;
            case SearchOptions.LAYER_DIAMOND:
                return Math.max(Math.abs(dx + dy), Math.abs(dx - dy));
            case SearchOptions.LAYER_ROW:
                return dy;
            case SearchOptions.LAYER_CIRCLE:
                return (int)(Math.sqrt(dx * dx + dy * dy) + 0.5);
            }
        }
        return 0;
    }
    
    // returns minimum layer number
    // note that it can be a negative number
    
    public static int getMinLayerNo(SearchOptions searchOptions, Properties properties)
    {
        int x = searchOptions.getLayersStartColumn();
        int y = searchOptions.getLayersStartRow();
        // it can be the closest point to the layers startpoint
        if (x < 0)
        {
            x = 0;
        }
        else if (x >= properties.getColumns())
        {
            x = properties.getColumns() - 1;
        }
        if (y < 0)
        {
            y = 0;
        }
        else if (y >= properties.getRows())
        {
            y = properties.getRows() - 1;
        }
        int minLayer = getLayerNo(searchOptions, x, y);
        // or it can be one of corners
        int layer = getLayerNo(searchOptions, 0, 0);
        if (layer < minLayer)
        {
            minLayer = layer;
        }
        layer = getLayerNo(searchOptions, 0, properties.getRows() - 1);
        if (layer < minLayer)
        {
            minLayer = layer;
        }
        layer = getLayerNo(searchOptions, properties.getColumns() - 1, 0);
        if (layer < minLayer)
        {
            minLayer = layer;
        }
        layer = getLayerNo(searchOptions, properties.getColumns() - 1, properties.getRows() - 1);
        if (layer < minLayer)
        {
            minLayer = layer;
        }
        return minLayer;
    }

    // returns maximum layer number
    // note that it can be a negative number
    // but is definitely bigger than minimum layer number
    
    public static int getMaxLayerNo(SearchOptions searchOptions, Properties properties)
    {
        int maxLayer = getLayerNo(searchOptions, 0, 0);
        int layer = getLayerNo(searchOptions, 0, properties.getRows() - 1);
        if (layer > maxLayer)
        {
            maxLayer = layer;
        }
        layer = getLayerNo(searchOptions, properties.getColumns() - 1, 0);
        if (layer > maxLayer)
        {
            maxLayer = layer;
        }
        layer = getLayerNo(searchOptions, properties.getColumns() - 1, properties.getRows() - 1);
        if (layer > maxLayer)
        {
            maxLayer = layer;
        }
        return maxLayer;
    }
}
