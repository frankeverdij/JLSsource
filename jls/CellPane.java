package jls;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public class CellPane extends JPanel
{
    private static final long serialVersionUID = 1;
    
    public static final int DISPLAY_NORMAL = 0;
    public static final int DISPLAY_RESULT = 1;

    private int displayMode = DISPLAY_NORMAL;
    private int currGen = 0;
    private Rectangle selection = null;
    private Properties properties = null;
    private SearchOptions searchOptions = null;
    private EditCellArray cellArray = null;
    private CellArray resultArray = null;
    private ImageCache imgCache = null;

    private int cellSize = 18;

    private Color symmetryColor = Color.magenta;

    public CellPane(Properties props, EditCellArray array, CellArray result)
    {
        super();
        setBackground(Color.white);
        cellArray = array;
        resultArray = result;
        reconfigure(props);
    }

    public void reconfigure(Properties props)
    {
        properties = props;
        setSelection(null);
        recalcSize();
    }
    
    public void setSearchOptions(SearchOptions opts)
    {
        searchOptions = opts;
        repaint();
    }
    
    public void setDisplayMode(int mode)
    {
        displayMode = mode;
        repaint();
    }

    public void setSelection(Rectangle newSel)
    {
        if (null == newSel)
        {
            selection = null;
        }
        else
        {
            selection = new Rectangle(newSel);
            if (selection.x < 0)
            {
                selection.width += selection.x;
                selection.x = 0;
            }
            if (selection.y < 0)
            {
                selection.height += selection.y;
                selection.y = 0;
            }
            if (selection.x + selection.width > cellArray.getCols())
            {
                selection.width = cellArray.getCols() - selection.x;
            }
            if (selection.y + selection.height > cellArray.getRows())
            {
                selection.height = cellArray.getRows() - selection.y;
            }
            if ((selection.width < 1) || (selection.height < 1))
            {
                selection = null;
            }
        }
        repaint();
    }

    public Rectangle getSelection()
    {
        return selection;
    }

    public Point getCellAt(int x, int y)
    {
        x = x / cellSize;
        y = y / cellSize;
        if ((x >= 0) && (y >= 0) && (x < cellArray.getCols()) && (y < cellArray.getRows()))
        {
            return new Point(x, y);
        }
        return null;
    }

    public Point getCellAtUnchecked(int x, int y)
    {
        x = x / cellSize;
        y = y / cellSize;
        return new Point(x, y);
    }
    
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        if (resultArray.getVersion() != cellArray.getVersion())
        {
            displayMode = DISPLAY_NORMAL;
        }
        
        Rectangle clipRect = g.getClipBounds();

        Point upLeft = getCellAtUnchecked(clipRect.x, clipRect.y);
        Point bottomRight = getCellAtUnchecked(clipRect.x + clipRect.width + 1, clipRect.y + clipRect.height + 1);
        if (bottomRight.x >= cellArray.getCols())
        {
            bottomRight.x = cellArray.getCols() - 1;
        }
        if (bottomRight.y >= cellArray.getRows())
        {
            bottomRight.y = cellArray.getRows() - 1;
        }
        if (upLeft.x < 0)
        {
            upLeft.x = 0;
        }
        if (upLeft.y < 0)
        {
            upLeft.y = 0;
        }

        int minLayer = CellStack.getMinLayerNo(searchOptions, properties);

        for (int y = upLeft.y; y <= bottomRight.y; ++y)
        {
            int sy = 1 + y * cellSize;

            for (int x = upLeft.x; x <= bottomRight.x; ++x)
            {
                byte stackValue = cellArray.getStackValue(x, y);
                byte cellValue = cellArray.getCellValue(x, y, currGen);
                byte pstackValue = stackValue;
                byte pcellValue = cellValue;
                if (DISPLAY_NORMAL != displayMode)
                {
                    pstackValue = resultArray.getStackValue(x, y);
                    pcellValue = resultArray.getCellValue(x, y, currGen);
                }

                byte cellState = Cell.getState(cellValue);

                // potential state
                // equals cellState if it's on or off
                // any state if cellState is empty
                int cellPotentialState = Cell.STATE_EMPTY;
                if (cellState == Cell.STATE_EMPTY)
                {
                    cellPotentialState = Cell.getState(pcellValue);
                }

                // false ... cell is not frozen
                // true ... cell is frozen
                boolean cellFrozen = Cell.isFrozen(cellValue);

                // cell type
                // 0 ... normal
                // 1 ... unknown (soft)
                // 2 ... unknown (hard)
                int cellType = Cell.getType(cellValue);

                // column state
                // false ... all cells share the same state
                // true ... different cell states in column
                /*
                boolean stackDiverse = CellStack.isDiverse(stackValue) 
                        || CellStack.isDiverse(pstackValue) 
                        || CellStack.isDiverse(stackValue, pstackValue);
                        */
                boolean stackDiverse = CellStack.isDiverse(pstackValue); 

                // true ... there is an error cell in the column
                boolean stackError = CellStack.isError(pstackValue);

                // true ... the cell has error state
                boolean cellError = Cell.isError(pcellValue);

                // true ... the cell is in a selected block
                boolean cellSelected = false;
                if (null != selection)
                {
                    cellSelected = (x >= selection.x) && (x < selection.x + selection.width) && (y >= selection.y) && (y < selection.y + selection.height);
                }

                // period class 0..6
                int cellPeriod = CellStack.getPeriodIndex(stackValue);
                
                int layerIndex = (CellStack.getLayerNo(searchOptions, x, y) - minLayer) % 3;
                
                Image img = imgCache.getImage(cellState, cellPotentialState,
                        cellFrozen, cellType, cellPeriod, stackDiverse,
                        stackError, cellError, cellSelected, layerIndex);

                g.drawImage(img, x * cellSize + 1, sy, null);
            }
        }
        g.setColor(symmetryColor);
        int xmax = cellArray.getCols() * cellSize - 1;
        int xhalf = (xmax + 1) / 2;
        int ymax = cellArray.getRows() * cellSize - 1;
        int yhalf = (ymax + 1) / 2;
        switch (properties.getSymmetry())
        {
        case Properties.SYMM_4FOLD:
            g.drawLine(1, yhalf, xmax, yhalf);
            g.drawLine(xhalf, 1, xhalf, ymax);
            break;
        case Properties.SYMM_4FOLD_DIAG:
            g.drawLine(1, 1, xmax, ymax);
            g.drawLine(xmax, 1, 1, ymax);
            break;
        case Properties.SYMM_8FOLD:
            g.drawLine(1, yhalf, xmax, yhalf);
            g.drawLine(xhalf, 1, xhalf, ymax);
            g.drawLine(1, 1, xmax, ymax);
            g.drawLine(xmax, 1, 1, ymax);
            break;
        case Properties.SYMM_MIRROR_DIAG_BWD:
            g.drawLine(1, 1, xmax, ymax);
            break;
        case Properties.SYMM_MIRROR_DIAG_FWD:
            g.drawLine(xmax, 1, 1, ymax);
            break;
        case Properties.SYMM_MIRROR_HORZ:
            g.drawLine(xhalf, 1, xhalf, ymax);
            break;
        case Properties.SYMM_MIRROR_VERT:
            g.drawLine(1, yhalf, xmax, yhalf);
            break;
        case Properties.SYMM_ROT_180:
            g.drawOval(1, 1, xmax - 1, ymax - 1);
            g.drawLine(xmax, 1, 1, ymax);
            break;
        case Properties.SYMM_ROT_90:
            g.drawOval(1, 1, xmax - 1, ymax - 1);
            g.drawLine(1, 1, xmax, ymax);
            g.drawLine(xmax, 1, 1, ymax);
            break;
        }

    }

    public void setCurrGen(int currGen)
    {
        this.currGen = currGen;
        repaint();
    }
    
    public void recalcSize()
    {
        imgCache = new ImageCache(this, cellSize);
        setPreferredSize(new Dimension(cellArray.getCols() * cellSize + 2, cellArray.getRows() * cellSize + 2));
        revalidate();
        repaint();
    }
    
    public void incCellSize()
    {
        if (cellSize < 50)
        {
            ++cellSize;
            recalcSize();
        }
    }

    public void decCellSize()
    {
        if (cellSize > 10)
        {
            --cellSize;
            recalcSize();
        }
    }
}
