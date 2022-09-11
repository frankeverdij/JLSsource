package jls;

import java.awt.Point;
import java.io.IOException;

public class CellArray
{
    protected byte [] cells;
    protected static final String CELLS_NAME = "cells";
    protected byte [] stacks;
    protected static final String STACKS_NAME = "stacks";
    protected Properties properties;
    protected int version;

    public CellArray()
    {
        cells = new byte [Properties.CELLS_MAX];
        stacks = new byte [Properties.CELLS_MAX];
        properties = null;
        version = 0;
    }
    
    public boolean readStatusParameter(StatusFileHandler handler, String name,
            int[] details, String value) throws IOException
    {
        if (name.equals(CELLS_NAME))
        {
            handler.checkDetailsLength(2);
            int gen = details[0];
            int row = details[1];
            int [] list = handler.parseIntegerList(value);
            if (list.length != properties.getColumns())
            {
                return false;
            }
            int col = list.length;
            while (col > 0)
            {
                --col;
                cells[getCellPtr(col, row, gen)] = (byte) list[col];
            }
        }
        else if (name.equals(STACKS_NAME))
        {
            handler.checkDetailsLength(1);
            int row = details[0];
            int [] list = handler.parseIntegerList(value);
            if (list.length != properties.getColumns())
            {
                return false;
            }
            int col = list.length;
            while (col > 0)
            {
                --col;
                stacks[getStackPtr(col, row)] = (byte) list[col];
            }
        }
        return true;
    }
    
    public String finishReadStatus()
    {
        return null;
    }
    
    public void writeStatus(StatusFileHandler handler) throws IOException
    {
        int [] singleRow = new int[properties.getColumns()];
        handler.newLine();
        for (int gen = 0; gen < properties.getGenerations(); ++gen)
        {
            for (int row = 0; row < properties.getRows(); ++row)
            {
                for (int col = 0; col < properties.getColumns(); ++col)
                {
                    singleRow[col] = getCellValue(col, row, gen);
                }
                handler.putParameter(CELLS_NAME + "{" + gen + "," + row + "}",
                        singleRow, singleRow.length);
            }
            handler.newLine();
        }
        for (int row = 0; row < properties.getRows(); ++row)
        {
            for (int col = 0; col < properties.getColumns(); ++col)
            {
                singleRow[col] = getStackValue(col, row);
            }
            handler.putParameter(STACKS_NAME + "{" + row + "}", singleRow, singleRow.length);
        }
    }
    
    public int getVersion()
    {
        return version;
    }
    
    public void incrementVersion()
    {
        version = (version + 1) & 0xFFFFFF;
    }

    public Properties getProperties()
    {
        return properties;
    }

    protected byte [] getCells()
    {
        return cells;
    }
    
    protected byte [] getStacks()
    {
        return stacks;
    }

    // returns stack index in the stacks array
    // stacks are organized in rows
    protected int getStackPtr(int col, int row)
    {
        return row * properties.getColumns() + col;
    }

    protected int getStackPtr(int cellPtr)
    {
        return cellPtr / properties.getGenerations();
    }

    // returns cell index in the cells array
    // cells are organized in stacks, these are in rows
    protected int getCellPtr(int col, int row, int gen)
    {
        return (row * properties.getColumns() + col) * properties.getGenerations() + gen;
    }
    
    public int getCols()
    {
        return properties.getColumns();
    }

    public int getGens()
    {
        return properties.getGenerations();
    }

    public int getRows()
    {
        return properties.getRows();
    }

    protected Point [] getSymmetricStacks(int x, int y)
    {
        Point [] result;
        int xMax = properties.getColumns() - 1;
        int yMax = properties.getRows() - 1;

        switch (properties.getSymmetry())
        {
        case Properties.SYMM_NONE:
            result = new Point [1];
            result[0] = new Point(x, y);
            break;
        case Properties.SYMM_MIRROR_HORZ:
            result = new Point [2];
            result[0] = new Point(x, y);
            result[1] = new Point(xMax - x, y);
            break;
        case Properties.SYMM_MIRROR_VERT:
            result = new Point [2];
            result[0] = new Point(x, y);
            result[1] = new Point(x, yMax - y);
            break;
        case Properties.SYMM_MIRROR_DIAG_FWD:
            result = new Point [2];
            result[0] = new Point(x, y);
            result[1] = new Point(yMax - y, xMax - x);
            break;
        case Properties.SYMM_MIRROR_DIAG_BWD:
            result = new Point [2];
            result[0] = new Point(x, y);
            result[1] = new Point(y, x);
            break;
        case Properties.SYMM_ROT_180:
            result = new Point [2];
            result[0] = new Point(x, y);
            result[1] = new Point(xMax - x, yMax - y);
            break;
        case Properties.SYMM_4FOLD:
            result = new Point [4];
            result[0] = new Point(x, y);
            result[1] = new Point(xMax - x, y);
            result[2] = new Point(x, yMax - y);
            result[3] = new Point(xMax - x, yMax - y);
            break;
        case Properties.SYMM_4FOLD_DIAG:
            result = new Point [4];
            result[0] = new Point(x, y);
            result[1] = new Point(y, x);
            result[2] = new Point(xMax - x, yMax - y);
            result[3] = new Point(yMax - y, xMax - x);
            break;
        case Properties.SYMM_ROT_90:
            result = new Point [4];
            result[0] = new Point(x, y);
            result[1] = new Point(xMax - x, yMax - y);
            result[2] = new Point(yMax - y, x);
            result[3] = new Point(y, xMax - x);
            break;
        default: // Properties.SYMM_8FOLD:
            result = new Point [8];
            result[0] = new Point(x, y);
            result[1] = new Point(xMax - x, y);
            result[2] = new Point(x, yMax - y);
            result[3] = new Point(xMax - x, yMax - y);
            result[4] = new Point(y, x);
            result[5] = new Point(yMax - y, xMax - x);
            result[6] = new Point(yMax - y, x);
            result[7] = new Point(y, xMax - x);
            break;
        }
        return result;
    }

    // returns the cell value
    protected byte getCellValue(int col, int row, int gen)
    {
        return cells[getCellPtr(col, row, gen)];
    }
    
    // returns the stack value
    
    protected byte getStackValue(int col, int row)
    {
        int sptr = getStackPtr(col, row);
        byte stackValue = stacks[sptr];
        if (CellStack.isChanged(stackValue))
        {
            int cptr = sptr * properties.getGenerations();
            stackValue &= CellStack.PERIOD_MASK;
            byte firstCellValue = cells[cptr];
            if (Cell.isError(firstCellValue))
            {
                stackValue |= CellStack.ERROR_ON;
            }
            firstCellValue &= Cell.DIVERSE_MASK;
            int i = properties.getGenerations() - 1;
            byte cellValue;
            while (i > 0)
            {
                ++cptr;
                --i;
                cellValue = cells[cptr];
                if (Cell.isError(cellValue))
                {
                    stackValue |= CellStack.ERROR_ON;
                }
                if ((cellValue & Cell.DIVERSE_MASK) != firstCellValue)
                {
                    stackValue |= CellStack.DIVERSE_ON;
                }
            }
            stacks[sptr] = stackValue;
        }
        return stackValue;
    }
    
    // returns cell inside the field normally
    // returns cell outside the field with honor to set tiling, shifting, and translation
    // returns -1 if there is 'unknown'
    public int getCellPtrEx(int col, int row, int gen)
    {
        // cell inside main field is the most common case, make it quick 
        if ((col >= 0) && (row >= 0) && (gen >= 0) && (col < properties.getColumns()
                && (row < properties.getRows()) && (gen < properties.getGenerations())))
        {
            return getCellPtr(col, row, gen);
        }
        // prepare shifts in all planes
        int shiftHorzDown = properties.isTileHorz() ? properties.getTileHorzShiftDown() : 0;
        int shiftHorzFuture = properties.isTileHorz() ? properties.getTileHorzShiftFuture() : 0;
        int shiftVertRight = properties.isTileVert() ? properties.getTileVertShiftRight() : 0;
        int shiftVertFuture = properties.isTileVert() ? properties.getTileVertShiftFuture() : 0;
        int shiftGenRight = properties.isTileGen() ? properties.getTileGenShiftRight() : 0;
        int shiftGenDown = properties.isTileGen() ? properties.getTileGenShiftDown() : 0;
        
        int jumpsRight = 0;
        int jumpsDown = 0;
        int jumpsFuture = 0;

        int cols = properties.getColumns();
        int rows = properties.getRows();
        int gens = properties.getGenerations();

        // one plane must have both shifts zero
        // one plane may have one shift set
        // one plane may have two shifts set
        // first decide which is which
        
        if ((0 == shiftHorzDown) && (0 == shiftHorzFuture))
        {
            // the 'horizontal tile' plane doesn't shift
            if ((0 != shiftVertRight) && (0 != shiftVertFuture))
            {
                // the 'vertical tile' plane shifts in both directions
                // the 'future tile' plane shifts 'right'
                // the 'horizontal tile' plane doesn't shift
                if (row < 0)
                {
                    jumpsDown = ((row + 1) / rows) - 1;
                }
                else
                {
                    jumpsDown = row / rows;
                }
                row -= jumpsDown * rows;
                col -= jumpsDown * shiftVertRight;
                gen -= jumpsDown * shiftVertFuture;
                if (gen < 0)
                {
                    jumpsFuture = ((gen + 1) / gens) - 1;
                }
                else
                {
                    jumpsFuture = gen / gens;
                }
                gen -= jumpsFuture * gens;
                col -= jumpsFuture * shiftGenRight;
                if (col < 0)
                {
                    jumpsRight = ((col + 1) / cols) - 1;
                }
                else
                {
                    jumpsRight = col / cols;
                }
                col -= jumpsRight * cols;
            }
            else
            {
                // the 'future tile' plane shifts in both directions
                // the 'vertical tile' plane shifts 'right'
                // the 'horizontal tile' plane doesn't shift
                if (gen < 0)
                {
                    jumpsFuture = ((gen + 1) / gens) - 1;
                }
                else
                {
                    jumpsFuture = gen / gens;
                }
                gen -= jumpsFuture * gens;
                col -= jumpsFuture * shiftGenRight;
                row -= jumpsFuture * shiftGenDown;
                if (row < 0)
                {
                    jumpsDown = ((row + 1) / rows) - 1;
                }
                else
                {
                    jumpsDown = row / rows;
                }
                row -= jumpsDown * rows;
                col -= jumpsDown * shiftVertRight;
                if (col < 0)
                {
                    jumpsRight = ((col + 1) / cols) - 1;
                }
                else
                {
                    jumpsRight = col / cols;
                }
                col -= jumpsRight * cols;
            }
        }
        else if ((0 == shiftVertRight) && (0 == shiftVertFuture))
        {
            // the 'vertical tile' plane doesn't shift
            if ((0 != shiftHorzDown) && (0 != shiftHorzFuture))
            {
                // the 'horizontal tile' plane shifts in both directions
                // the 'future tile' plane shifts 'up'
                // the 'vertical tile' plane doesn't shift
                if (col < 0)
                {
                    jumpsRight = ((col + 1) / cols) - 1;
                }
                else
                {
                    jumpsRight = col / cols;
                }
                col -= jumpsRight * cols;
                row -= jumpsRight * shiftHorzDown;
                gen -= jumpsRight * shiftHorzFuture;
                if (gen < 0)
                {
                    jumpsFuture = ((gen + 1) / gens) - 1;
                }
                else
                {
                    jumpsFuture = gen / gens;
                }
                gen -= jumpsFuture * gens;
                row -= jumpsFuture * shiftGenDown;
                if (row < 0)
                {
                    jumpsDown = ((row + 1) / rows) - 1;
                }
                else
                {
                    jumpsDown = row / rows;
                }
                row -= jumpsDown * rows;
            }
            else
            {
                // the 'future tile' plane shifts in both directions
                // the 'horizontal tile' plane shifts 'up'
                // the 'vertical tile' plane doesn't shift
                if (gen < 0)
                {
                    jumpsFuture = ((gen + 1) / gens) - 1;
                }
                else
                {
                    jumpsFuture = gen / gens;
                }
                gen -= jumpsFuture * gens;
                col -= jumpsFuture * shiftGenRight;
                row -= jumpsFuture * shiftGenDown;
                if (col < 0)
                {
                    jumpsRight = ((col + 1) / cols) - 1;
                }
                else
                {
                    jumpsRight = col / cols;
                }
                col -= jumpsRight * cols;
                row -= jumpsRight * shiftHorzDown;
                jumpsDown = row / rows;
                if (row < 0)
                {
                    jumpsDown = ((row + 1) / rows) - 1;
                }
                else
                {
                    jumpsDown = row / rows;
                }
                row -= jumpsDown * rows;
            }
        }
        else
        {
           // the 'future tile' doesn't shift ... by definition
            if ((0 != shiftHorzDown) && (0 != shiftHorzFuture))
            {
                // the 'horizontal tile' plane shifts in both directions
                // the 'vertical tile' plane shifts 'future'
                // the 'future tile' plane doesn't shift
                if (col < 0)
                {
                    jumpsRight = ((col + 1) / cols) - 1;
                }
                else
                {
                    jumpsRight = col / cols;
                }
                col -= jumpsRight * cols;
                row -= jumpsRight * shiftHorzDown;
                gen -= jumpsRight * shiftHorzFuture;
                if (row < 0)
                {
                    jumpsDown = ((row + 1) / rows) - 1;
                }
                else
                {
                    jumpsDown = row / rows;
                }
                row -= jumpsDown * rows;
                gen -= jumpsDown * shiftVertFuture;
                if (gen < 0)
                {
                    jumpsFuture = ((gen + 1) / gens) - 1;
                }
                else
                {
                    jumpsFuture = gen / gens;
                }
                gen -= jumpsFuture * gens;
                   
            }
            else
            {
                // the 'vertical tile' plane shifts in both directions
                // the 'horizontal tile' plane shifts 'future'
                // the 'future tile' plane doesn't shift
                if (row < 0)
                {
                    jumpsDown = ((row + 1) / rows) - 1;
                }
                else
                {
                    jumpsDown = row / rows;
                }
                row -= jumpsDown * rows;
                col -= jumpsDown * shiftVertRight;
                gen -= jumpsDown * shiftVertFuture;
                if (col < 0)
                {
                    jumpsRight = ((col + 1) / cols) - 1;
                }
                else
                {
                    jumpsRight = col / cols;
                }
                col -= jumpsRight * cols;
                gen -= jumpsRight * shiftHorzFuture;
                jumpsFuture = gen / gens;
                if (gen < 0)
                {
                    --jumpsFuture;
                }
                gen -= jumpsFuture * gens;
            }
        }
        if ((properties.isTileHorz() || (0 == jumpsRight))
                && (properties.isTileVert() || (0 == jumpsDown))
                && (properties.isTileGen() || (0 == jumpsFuture)))
        {
            // okay, we got to a defined area
            // now solve the translation, if there is any

            // translations are three types
            // XLAT_NONE does nothing at all
            // all other except ROT_90 and ROT_270 repeat themselves every two tiles
            // the two repeat themselves every four tiles
            int x = col;
            int y = row;
            int xmax = cols - 1;
            int ymax = rows - 1;
            
            switch(properties.getTranslation())
            {
            case Properties.XLAT_NONE:
                break;
            case Properties.XLAT_FLIP_HORZ:
                if (0 != (jumpsFuture & 1))
                {
                    x = xmax - col;
                }
                break;
            case Properties.XLAT_FLIP_VERT:
                if (0 != (jumpsFuture & 1))
                {
                    y = ymax - row;
                }
                break;
            case Properties.XLAT_FLIP_DIAG_FWD:
                if (0 != (jumpsFuture & 1))
                {
                    x = ymax - row;
                    y = xmax - col;
                }
                break;
            case Properties.XLAT_FLIP_DIAG_BWD:
                if (0 != (jumpsFuture & 1))
                {
                    x = row;
                    y = col;
                }
                break;
            case Properties.XLAT_ROT_180:
                if (0 != (jumpsFuture & 1))
                {
                    x = xmax - col;
                    y = ymax - row;
                }
                break;
            case Properties.XLAT_ROT_90:
                switch (jumpsFuture & 3)
                {
                case 0:
                    break;
                case 1:
                    x = ymax - row;
                    y = col;
                    break;
                case 2:
                    x = xmax - col;
                    y = ymax - row;
                    break;
                case 3:
                    x = row;
                    y = xmax - col;
                    break;
                }
                break;
            case Properties.XLAT_ROT_270:
                switch (jumpsFuture & 3)
                {
                case 0:
                    break;
                case 1:
                    x = row;
                    y = xmax - col;
                    break;
                case 2:
                    x = xmax - col;
                    y = ymax - row;
                    break;
                case 3:
                    x = ymax - row;
                    y = col;
                    break;
                }
                break;
            }
            return getCellPtr(x, y, gen);
        }
        return -1;
    }
    
    public void load(CellArray source)
    {
        properties = source.getProperties();
        version = source.getVersion();
        System.arraycopy(source.getCells(), 0, cells, 0, properties.getColumns() * properties.getRows() * properties.getGenerations());
        System.arraycopy(source.getStacks(), 0, stacks, 0, properties.getColumns() * properties.getRows());
    }
    
}
