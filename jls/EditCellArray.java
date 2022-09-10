package jls;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.LinkedList;

import jls.undo.DoubleListUndoRecord;
import jls.undo.LosslessManipulationUndoRecord;
import jls.undo.SetPeriodUndoRecord;
import jls.undo.UndoRecord;

public class EditCellArray extends CellArray
{
    private boolean readOnly = false;
    private static final String READ_ONLY_NAME = "read_only";

    private LinkedList<UndoRecord> undoStack = new LinkedList<UndoRecord>();
    private LinkedList<UndoRecord> redoStack = new LinkedList<UndoRecord>();
    private int undoStackSize = 0;
    private static final int MAX_UNDO_STACK_RECORDS = 500;
    private static final int MAX_UNDO_STACK_SIZE = 5000000;
    
    
    private void resetUndo()
    {
        undoStack.clear();
        redoStack.clear();
        undoStackSize = 0;
    }
    
    private void recordUndo(UndoRecord rec)
    {
        if (rec.isStoreable())
        {
            redoStack.clear();
            undoStack.addFirst(rec);
            undoStackSize += rec.size();
            while ((undoStack.size() > MAX_UNDO_STACK_RECORDS) || (undoStackSize > MAX_UNDO_STACK_SIZE)) 
            {
                rec = undoStack.removeLast();
                undoStackSize -= rec.size();
            }
        }
    }
    
    public boolean isUndoAvailable()
    {
        return undoStack.size() > 0;
    }

    public boolean isRedoAvailable()
    {
        return redoStack.size() > 0;
    }
    
    public void undo()
    {
        if (undoStack.size() > 0)
        {
            UndoRecord undoRec = undoStack.removeFirst();
            undoStackSize -= undoRec.size();
            redoStack.addFirst(undoRec);
            undoRec.undo();
        }
    }
    
    public void redo()
    {
        if (redoStack.size() > 0)
        {
            UndoRecord undoRec = redoStack.removeFirst();
            undoStackSize += undoRec.size();
            undoStack.addFirst(undoRec);
            undoRec.redo();
        }
    }

    @Override
    public boolean readStatusParameter(StatusFileHandler handler, String name,
            int[] details, String value) throws IOException
    {
        if (name.equals(READ_ONLY_NAME))
        {
            handler.checkDetailsLength(0);
            readOnly = handler.parseBoolean(value); 
        }
        else 
        {
            return super.readStatusParameter(handler, name, details, value);
        }
        return true;
    }

    @Override
    public String finishReadStatus()
    {
        resetUndo();
        return null;
    }

    @Override
    public void writeStatus(StatusFileHandler handler) throws IOException
    {
        handler.putParameter(READ_ONLY_NAME, readOnly);
        super.writeStatus(handler);
    }
    
    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }
    
    public boolean isReadOnly()
    {
        return readOnly;
    }
    
    public void reconfigure(Properties newProps)
    {
        if (isReadOnly())
        {
            throw new RuntimeException("Cannot change properties when search is running");
        }

        if (null != properties)
        {
            if ((properties.getRows() != newProps.getRows())
                    || (properties.getColumns() != newProps.getColumns())
                    || (properties.getGenerations() != newProps.getGenerations()))
            {
                resetUndo();
                // let's do the transform in two steps
                // 1/ shrink
                // 2/ expand
                
                int index1;
                int index2;
                int limit;
                
                int oldRowCount = properties.getRows();
                int newRowCount = newProps.getRows();
                if (oldRowCount > newRowCount)
                {
                    // discard extra rows
                    oldRowCount = newRowCount;
                }
                int midRowCount = oldRowCount;
                // row count remains the same in the first step
                
                int oldColCount = properties.getColumns();
                int newColCount = newProps.getColumns();
                int midColCount = oldColCount < newColCount ? oldColCount : newColCount;
                
                int oldGenCount = properties.getGenerations();
                int newGenCount = newProps.getGenerations();
                int midGenCount = oldGenCount < newGenCount ? oldGenCount : newGenCount;
                
                int row;
                int col;
                int genDelta;
                int colDelta;
                // check if we have to do something in the first step
                // remember that all three are shrinking, so if the product is the same,
                // then all three are the same
                // no need to compare rows, they're the same
                if (oldColCount * oldGenCount != midColCount * midGenCount)
                {
                    index1 = 0;
                    index2 = 0;
                    genDelta = oldGenCount - midGenCount;
                    colDelta = (oldColCount - midColCount) * oldGenCount;
                    for (row = 0; row < midRowCount; ++row)
                    {
                        for (col = 0; col < midColCount; ++col)
                        {
                            limit = index1 + midGenCount;
                            while (index1 < limit)
                            {
                                cells[index1] = cells[index2];
                                ++index1;
                                ++index2;
                            }
                            index2 += genDelta;
                        }
                        index2 += colDelta;
                    }
                }
                
                // shrinking done, now expand
                // no need to compare rows, the new ones will be appended afterwards
                if (midColCount * midGenCount != newColCount * newGenCount) 
                {
                    index1 = newColCount * newGenCount * midRowCount;
                    index2 = midColCount * midGenCount * midRowCount;
                    genDelta = newGenCount - midGenCount;
                    colDelta = (newColCount - midColCount) * newGenCount;
                    while (index1 > 0)
                    {
                        // fill gap for new columns
                        limit = index1 - colDelta;
                        while (index1 > limit)
                        {
                            --index1;
                            cells[index1] = Cell.INIT_STATE;
                        }
                        for (col = 0; col < midColCount; ++col)
                        {
                            // fill gap for new generations
                            limit = index1 - genDelta;
                            while (index1 > limit)
                            {
                                --index1;
                                cells[index1] = Cell.INIT_STATE;
                            }
                            // now copy the stack
                            limit = index1 - midGenCount;
                            while (index1 > limit)
                            {
                                --index1;
                                --index2;
                                cells[index1] = cells[index2];
                            }
                        }
                    }
                }
                // and fill remaining rows
                index1 = newColCount * newGenCount * midRowCount;
                limit = newColCount * newGenCount * newRowCount;
                while (index1 < limit)
                {
                    cells[index1] = Cell.INIT_STATE;
                    ++index1;
                }
                
                // now the same but easier for stacks
                // 1/ shrink
                
                if (oldColCount != midColCount)
                {
                    index1 = 0;
                    index2 = 0;
                    colDelta = oldColCount - midColCount;
                    for (row = 0; row < midRowCount; ++row)
                    {
                        limit = index1 + midColCount;
                        while (index1 < limit)
                        {
                            stacks[index1] = stacks[index2];
                            ++index1;
                            ++index2;
                        }
                        index2 += colDelta;
                    }
                }
                
                // 2/ expand
                if (midColCount != newColCount)
                {
                    index1 = newColCount * midRowCount;
                    index2 = midColCount * midRowCount;
                    colDelta = newColCount - midColCount;
                    while (index1 > 0)
                    {
                        limit = index1 - colDelta;
                        while (index1 > limit)
                        {
                            --index1;
                            stacks[index1] = CellStack.INIT_STATE;
                        }
                        limit = index1 - midColCount;
                        while (index1 > limit)
                        {
                            --index1;
                            --index2;
                            stacks[index1] = stacks[index2];
                        }
                    }
                }
                // 3/ fill remaining rows
                index1 = newColCount * midRowCount;
                limit = newColCount * newRowCount;
                while (index1 < limit)
                {
                    stacks[index1] = CellStack.INIT_STATE;
                    ++index1;
                }
                // if number of generations changed, mark all stacks for update
                if (newProps.getGenerations() != properties.getGenerations())
                {
                    while (index1 > 0)
                    {
                        --index1;
                        stacks[index1] = CellStack.setChanged(stacks[index1]);
                    }
                }
            }
        }
        else
        {
            // just fill the field
            resetUndo();

            int index = newProps.getColumns() * newProps.getRows() * newProps.getGenerations();
            while (index > 0)
            {
                --index;
                cells[index] = Cell.INIT_STATE;
            }
            index = newProps.getColumns() * newProps.getRows();
            while (index > 0)
            {
                --index;
                stacks[index] = CellStack.INIT_STATE;
            }
        }
        properties = newProps;
    }
    
    @Override
    public void incrementVersion()
    {
        if (isReadOnly())
        {
            return;
        }
        super.incrementVersion();
    }
    
    public void setPeriod(Rectangle rect, int index)
    {
        if (isReadOnly())
        {
            return;
        }
        if (null != rect)
        {
            SetPeriodUndoRecord undoRec = new SetPeriodUndoRecord(this);
            for (int row = rect.y + rect.height - 1; row >= rect.y; --row)
            {
                for (int col = rect.x + rect.width - 1; col >= rect.x; --col)
                {
                    Point [] symms = getSymmetricStacks(col, row);
                    for (int i = 0; i < symms.length; ++i)
                    {
                        int ptr = getStackPtr(symms[i].x, symms[i].y);
                        byte newState = CellStack.setPeriodIndex(stacks[ptr], index);
                        undoRec.record(ptr, stacks[ptr], newState);
                        stacks[ptr] = newState;
                    }
                }
            }
            recordUndo(undoRec);
        }
    }

    public void setCells(Rectangle rect, int gen, byte newState)
    {
        if (isReadOnly())
        {
            return;
        }
        if (null != rect)
        {
            DoubleListUndoRecord undoRec = new DoubleListUndoRecord(this);

            for (int row = rect.y + rect.height - 1; row >= rect.y; --row)
            {
                for (int col = rect.x + rect.width - 1; col >= rect.x; --col)
                {
                    Point [] symms = getSymmetricStacks(col, row);
                    for (int i = 0; i < symms.length; ++i)
                    {
                        int x = symms[i].x;
                        int y = symms[i].y;
                        int sptr = getStackPtr(x, y);
                        stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                        int gLimit;
                        int gStep;
                        if (-1 == gen)
                        {
                            gStep = 1;
                            gLimit = 0;
                        }
                        else
                        {
                            gStep = properties.getPeriod(CellStack.getPeriodIndex(stacks[sptr]));
                            gLimit = gen % gStep;
                        }
                        int cptr = getCellPtr(x, y, gLimit);
                        gLimit = cptr - gLimit + properties.getGenerations();
                        while (cptr < gLimit)
                        {
                            undoRec.record(cptr, cells[cptr], newState);
                            cells[cptr] = newState;
                            cptr += gStep;
                        }
                    }
                }
            }
            recordUndo(undoRec);
        }
    }

    public void changeCells(Rectangle rect, int gen, boolean toEmpty)
    {
        if (isReadOnly())
        {
            return;
        }
        if (null != rect)
        {
            DoubleListUndoRecord undoRec = new DoubleListUndoRecord(this);
            
            for (int row = rect.y + rect.height - 1; row >= rect.y; --row)
            {
                for (int col = rect.x + rect.width - 1; col >= rect.x; --col)
                {
                    Point [] symms = getSymmetricStacks(col, row);
                    for (int i = 0; i < symms.length; ++i)
                    {
                        int x = symms[i].x;
                        int y = symms[i].y;
                        int sptr = getStackPtr(x, y);
                        stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                        int gLimit;
                        int gStep;
                        if (-1 == gen)
                        {
                            gStep = 1;
                            gLimit = 0;
                        }
                        else
                        {
                            gStep = properties.getPeriod(CellStack.getPeriodIndex(stacks[sptr]));
                            gLimit = gen % gStep;
                        }
                        int cptr = getCellPtr(x, y, gLimit);
                        gLimit = cptr - gLimit + properties.getGenerations();
                        while (cptr < gLimit)
                        {
                            byte cellValue = cells[cptr];
                            if (toEmpty)
                            {
                                if (Cell.getType(cellValue) == Cell.TYPE_UNCHECKED)
                                {
                                    byte newState = Cell.setType(cellValue, Cell.TYPE_NORMAL); 
                                    undoRec.record(cptr, cells[cptr], newState);
                                    cells[cptr] = newState;
                                }
                            }
                            else
                            {
                                if ((Cell.getType(cellValue) == Cell.TYPE_NORMAL) && (Cell.getState(cellValue) == Cell.STATE_EMPTY))
                                {
                                    byte newState = Cell.setType(cellValue, Cell.TYPE_UNCHECKED);
                                    undoRec.record(cptr, cells[cptr], newState);
                                    cells[cptr] = newState;
                                }
                            }
                            cptr += gStep;
                        }
                    }
                }
            }
            recordUndo(undoRec);
        }
    }

    public void shiftDown(Rectangle rect, int gen)
    {
        if (isReadOnly())
        {
            return;
        }
        if (null != rect)
        {
            recordUndo(new LosslessManipulationUndoRecord(this, rect, gen, LosslessManipulationUndoRecord.TYPE_SHIFT_DOWN));
            shiftDownImpl(rect, gen);
        }
    }

    public void shiftDownImpl(Rectangle rect, int gen)
    {
        int cptr;
        byte cell1;
        byte cell2;

        int x;
        int y;
        int g;
        int xMin = rect.x;
        int xMax = rect.x + rect.width - 1;
        int yMin = rect.y;
        int yMax = rect.y + rect.height - 1;
        int gMin = gen < 0 ? 0 : gen;
        int gMax = gen < 0 ? properties.getGenerations() - 1 : gen;

        // first mark all stacks
        for (y = yMin; y <= yMax; ++y)
        {
            for (x = xMin; x <= xMax; ++x)
            {
                int sptr = getStackPtr(x, y);
                stacks[sptr] = CellStack.setChanged(stacks[sptr]);
            }
        }
        // then process cells
        for (g = gMin; g <= gMax; ++g)
        {
            for (x = xMin; x <= xMax; ++x)
            {
                cell2 = cells[getCellPtr(x, yMax, g)];
                for (y = yMin; y <= yMax; ++y)
                {
                    cptr = getCellPtr(x, y, g);
                    cell1 = cells[cptr];
                    cells[cptr] = cell2;
                    cell2 = cell1; 
                }
            }
        }
    }

    public void shiftUp(Rectangle rect, int gen)
    {
        if (isReadOnly())
        {
            return;
        }
        if (null != rect)
        {
            recordUndo(new LosslessManipulationUndoRecord(this, rect, gen, LosslessManipulationUndoRecord.TYPE_SHIFT_UP));
            shiftUpImpl(rect, gen);
        }
    }
    
    public void shiftUpImpl(Rectangle rect, int gen)
    {
        int cptr;
        byte cell1;
        byte cell2;

        int x;
        int y;
        int g;
        int xMin = rect.x;
        int xMax = rect.x + rect.width - 1;
        int yMin = rect.y;
        int yMax = rect.y + rect.height - 1;
        int gMin = gen < 0 ? 0 : gen;
        int gMax = gen < 0 ? properties.getGenerations() - 1 : gen;
        // first mark all stacks
        for (y = yMin; y <= yMax; ++y)
        {
            for (x = xMin; x <= xMax; ++x)
            {
                int sptr = getStackPtr(x, y);
                stacks[sptr] = CellStack.setChanged(stacks[sptr]);
            }
        }
        // then process cells
        for (g = gMin; g <= gMax; ++g)
        {
            for (x = xMin; x <= xMax; ++x)
            {
                cell2 = cells[getCellPtr(x, yMin, g)];
                for (y = yMax; y >= yMin; --y)
                {
                    cptr = getCellPtr(x, y, g);
                    cell1 = cells[cptr];
                    cells[cptr] = cell2;
                    cell2 = cell1; 
                }
            }
        }
    }
    
    public void shiftLeft(Rectangle rect, int gen)
    {
        if (isReadOnly())
        {
            return;
        }
        if (null != rect)
        {
            recordUndo(new LosslessManipulationUndoRecord(this, rect, gen, LosslessManipulationUndoRecord.TYPE_SHIFT_LEFT));
            shiftLeftImpl(rect, gen);
        }
    }

    public void shiftLeftImpl(Rectangle rect, int gen)
    {
        int cptr;
        byte cell1;
        byte cell2;

        int x;
        int y;
        int g;
        int xMin = rect.x;
        int xMax = rect.x + rect.width - 1;
        int yMin = rect.y;
        int yMax = rect.y + rect.height - 1;
        int gMin = gen < 0 ? 0 : gen;
        int gMax = gen < 0 ? properties.getGenerations() - 1 : gen;
        // first mark all stacks
        for (y = yMin; y <= yMax; ++y)
        {
            for (x = xMin; x <= xMax; ++x)
            {
                int sptr = getStackPtr(x, y);
                stacks[sptr] = CellStack.setChanged(stacks[sptr]);
            }
        }
        // then process cells
        for (g = gMin; g <= gMax; ++g)
        {
            for (y = yMin; y <= yMax; ++y)
            {
                cell2 = cells[getCellPtr(xMin, y, g)];
                for (x = xMax; x >= xMin; --x)
                {
                    cptr = getCellPtr(x, y, g);
                    cell1 = cells[cptr];
                    cells[cptr] = cell2;
                    cell2 = cell1; 
                }
            }
        }
    }

    public void shiftRight(Rectangle rect, int gen)
    {
        if (isReadOnly())
        {
            return;
        }
        if (null != rect)
        {
            recordUndo(new LosslessManipulationUndoRecord(this, rect, gen, LosslessManipulationUndoRecord.TYPE_SHIFT_RIGHT));
            shiftRightImpl(rect, gen);
        }
    }
    
    public void shiftRightImpl(Rectangle rect, int gen)
    {
        int cptr;
        byte cell1;
        byte cell2;

        int x;
        int y;
        int g;
        int xMin = rect.x;
        int xMax = rect.x + rect.width - 1;
        int yMin = rect.y;
        int yMax = rect.y + rect.height - 1;
        int gMin = gen < 0 ? 0 : gen;
        int gMax = gen < 0 ? properties.getGenerations() - 1 : gen;
        // first mark all stacks
        for (y = yMin; y <= yMax; ++y)
        {
            for (x = xMin; x <= xMax; ++x)
            {
                int sptr = getStackPtr(x, y);
                stacks[sptr] = CellStack.setChanged(stacks[sptr]);
            }
        }
        // then process cells
        for (g = gMin; g <= gMax; ++g)
        {
            for (y = yMin; y <= yMax; ++y)
            {
                cell2 = cells[getCellPtr(xMax, y, g)];
                for (x = xMin; x <= xMax; ++x)
                {
                    cptr = getCellPtr(x, y, g);
                    cell1 = cells[cptr];
                    cells[cptr] = cell2;
                    cell2 = cell1; 
                }
            }
        }
    }
    
    public void shiftFuture(Rectangle rect)
    {
        if (isReadOnly())
        {
            return;
        }
        if (null != rect)
        {
            recordUndo(new LosslessManipulationUndoRecord(this, rect, -1, LosslessManipulationUndoRecord.TYPE_SHIFT_FUTURE));
            shiftFutureImpl(rect);
        }
    }

    public void shiftFutureImpl(Rectangle rect)
    {
        int cptr;
        byte cell1;
        byte cell2;

        int x;
        int y;
        int g;
        int xMin = rect.x;
        int xMax = rect.x + rect.width - 1;
        int yMin = rect.y;
        int yMax = rect.y + rect.height - 1;
        for (y = yMin; y <= yMax; ++y)
        {
            for (x = xMin; x <= xMax; ++x)
            {
                cptr = getCellPtr(x, y, 0);
                cell2 = cells[getCellPtr(x, y, properties.getGenerations() - 1)];
                for (g = properties.getGenerations(); g > 0; --g)
                {
                    cell1 = cells[cptr];
                    cells[cptr] = cell2;
                    cell2 = cell1;
                    ++cptr;
                }
            }
        }
    }
    
    public void shiftPast(Rectangle rect)
    {
        if (isReadOnly())
        {
            return;
        }
        if (null != rect)
        {
            recordUndo(new LosslessManipulationUndoRecord(this, rect, -1, LosslessManipulationUndoRecord.TYPE_SHIFT_PAST));
            shiftPastImpl(rect);
        }
    }

    public void shiftPastImpl(Rectangle rect)
    {
        int cptr;
        byte cell1;
        byte cell2;

        int x;
        int y;
        int g;
        int xMin = rect.x;
        int xMax = rect.x + rect.width - 1;
        int yMin = rect.y;
        int yMax = rect.y + rect.height - 1;
        for (y = yMin; y <= yMax; ++y)
        {
            for (x = xMin; x <= xMax; ++x)
            {
                cptr = getCellPtr(x, y, properties.getGenerations() - 1);
                cell2 = cells[getCellPtr(x, y, 0)];
                for (g = properties.getGenerations(); g > 0; --g)
                {
                    cell1 = cells[cptr];
                    cells[cptr] = cell2;
                    cell2 = cell1;
                    --cptr;
                }
            }
        }
    }

    public void transpose(Rectangle rect, int gen)
    {
        if (isReadOnly())
        {
            return;
        }
        if ((null != rect) && (rect.width == rect.height))
        {
            recordUndo(new LosslessManipulationUndoRecord(this, rect,gen, LosslessManipulationUndoRecord.TYPE_TRANSPOSE));
            transposeImpl(rect, gen);
        }
    }
    
    public void transposeImpl(Rectangle rect, int gen)
    {
        int cptr1;
        int cptr2;
        byte cell;

        int x;
        int y;
        int g;
        int xMin = rect.x;
        int xMax = rect.x + rect.width - 1;
        int yMin = rect.y;
        int yMax = rect.y + rect.height - 1;
        int gMin = gen < 0 ? 0 : gen;
        int gMax = gen < 0 ? properties.getGenerations() - 1 : gen;
        // first mark all stacks
        for (y = yMin; y <= yMax; ++y)
        {
            for (x = xMin; x <= xMax; ++x)
            {
                int sptr = getStackPtr(x, y);
                stacks[sptr] = CellStack.setChanged(stacks[sptr]);
            }
        }
        // then process cells
        for (g = gMin; g <= gMax; ++g)
        {
            for (y = yMin; y <= yMax; ++y)
            {
                for (x = xMin + y - yMin - 1; x >= xMin; --x)
                {
                    cptr1 = getCellPtr(x, y, g);
                    cptr2 = getCellPtr(y - yMin + xMin, x - xMin + yMin, g);
                    cell = cells[cptr1];
                    cells[cptr1] = cells[cptr2];
                    cells[cptr2] = cell;
                }
            }
        }
    }
    
    // I'm lazy, rotations will be implemented by combining transpose and mirror 
    public void rotateLeft(Rectangle rect, int gen)
    {
        if (isReadOnly())
        {
            return;
        }
        if ((null != rect) && (rect.width == rect.height))
        {
            recordUndo(new LosslessManipulationUndoRecord(this, rect,gen, LosslessManipulationUndoRecord.TYPE_ROTATE_LEFT));
            transposeImpl(rect, gen);
            mirrorVertImpl(rect, gen);
        }
    }
    
    public void rotateRight(Rectangle rect, int gen)
    {
        if (isReadOnly())
        {
            return;
        }
        if ((null != rect) && (rect.width == rect.height))
        {
            recordUndo(new LosslessManipulationUndoRecord(this, rect,gen, LosslessManipulationUndoRecord.TYPE_ROTATE_RIGHT));
            transposeImpl(rect, gen);
            mirrorHorzImpl(rect, gen);
        }
    }
    
    public void rotate180(Rectangle rect, int gen)
    {
        if (isReadOnly())
        {
            return;
        }
        recordUndo(new LosslessManipulationUndoRecord(this, rect,gen, LosslessManipulationUndoRecord.TYPE_ROTATE_180));
        mirrorHorzImpl(rect, gen);
        mirrorVertImpl(rect, gen);
    }

    public void mirrorHorz(Rectangle rect, int gen)
    {
        if (isReadOnly())
        {
            return;
        }
        if (null != rect)
        {
            recordUndo(new LosslessManipulationUndoRecord(this, rect,gen, LosslessManipulationUndoRecord.TYPE_MIRROR_HORZ));
            mirrorHorzImpl(rect, gen);
        }
    }
    
    public void mirrorHorzImpl(Rectangle rect, int gen)
    {
        int cptr1;
        int cptr2;
        byte cell;

        int x;
        int y;
        int g;
        int xMin = rect.x;
        int xMax = rect.x + rect.width - 1;
        int yMin = rect.y;
        int yMax = rect.y + rect.height - 1;
        int gMin = gen < 0 ? 0 : gen;
        int gMax = gen < 0 ? properties.getGenerations() - 1 : gen;
        // first mark all stacks
        for (y = yMin; y <= yMax; ++y)
        {
            for (x = xMin; x <= xMax; ++x)
            {
                int sptr = getStackPtr(x, y);
                stacks[sptr] = CellStack.setChanged(stacks[sptr]);
            }
        }
        // then process cells
        for (g = gMin; g <= gMax; ++g)
        {
            for (y = yMin; y <= yMax; ++y)
            {
                for (x = (rect.width - 1) / 2; x >= 0; --x)
                {
                    cptr1 = getCellPtr(x + xMin, y, g);
                    cptr2 = getCellPtr(xMax - x, y, g);
                    cell = cells[cptr1];
                    cells[cptr1] = cells[cptr2];
                    cells[cptr2] = cell;
                }
            }
        }
    }
    
    public void mirrorVert(Rectangle rect, int gen)
    {
        if (isReadOnly())
        {
            return;
        }
        if (null != rect)
        {
            recordUndo(new LosslessManipulationUndoRecord(this, rect,gen, LosslessManipulationUndoRecord.TYPE_MIRROR_VERT));
            mirrorVertImpl(rect, gen);
        }
    }
    
    public void mirrorVertImpl(Rectangle rect, int gen)
    {
        int cptr1;
        int cptr2;
        byte cell;

        int x;
        int y;
        int g;
        int xMin = rect.x;
        int xMax = rect.x + rect.width - 1;
        int yMin = rect.y;
        int yMax = rect.y + rect.height - 1;
        int gMin = gen < 0 ? 0 : gen;
        int gMax = gen < 0 ? properties.getGenerations() - 1 : gen;
        // first mark all stacks
        for (y = yMin; y <= yMax; ++y)
        {
            for (x = xMin; x <= xMax; ++x)
            {
                int sptr = getStackPtr(x, y);
                stacks[sptr] = CellStack.setChanged(stacks[sptr]);
            }
        }
        // then process cells
        for (g = gMin; g <= gMax; ++g)
        {
            for (x = xMin; x <= xMax; ++x)
            {
                for (y = (rect.height - 1) / 2; y >= 0; --y)
                {
                    cptr1 = getCellPtr(x, y + yMin, g);
                    cptr2 = getCellPtr(x, yMax - y, g);
                    cell = cells[cptr1];
                    cells[cptr1] = cells[cptr2];
                    cells[cptr2] = cell;
                }
            }
        }
    }
    
    public void acceptResult(CellArray array)
    {
        if (array.getVersion() == version)
        {
            DoubleListUndoRecord undoRec = new DoubleListUndoRecord(this);

            byte [] acells = array.getCells();
            int i = properties.getColumns() * properties.getRows() * properties.getGenerations();
            while (i > 0)
            {
                --i;
                byte cVal = cells[i];
                if ((Cell.getType(cVal) != Cell.TYPE_UNSET) && (Cell.getState(cVal) == Cell.STATE_EMPTY))
                {
                    byte cStat = Cell.getState(acells[i]);
                    if (cStat != Cell.STATE_EMPTY)
                    {
                        byte newState = Cell.setFrozen(Cell.setType(Cell.setState(cVal, cStat), Cell.TYPE_NORMAL), false);
                        undoRec.record(i, cells[i], newState);
                        cells[i] = newState;
                        int sptr = i / properties.getGenerations();
                        stacks[sptr] = CellStack.setChanged(stacks[sptr]);
                    }
                }
            }
            recordUndo(undoRec);
        }
        incrementVersion();
    }

    public boolean pasteString(Rectangle rect, int gen, String str)
    {
        DoubleListUndoRecord undoRec = new DoubleListUndoRecord(this);
        boolean result = pasteString(rect, gen, str, undoRec);
        recordUndo(undoRec);
        return result;
    }

    public boolean pasteString(Rectangle rect, int gen, String str, DoubleListUndoRecord undoRec)
    {
        boolean change = false;
        try
        {
            // skip all comment lines at the start
            int pos = 0;
            while (str.charAt(pos) == '#')
            {
                while (str.charAt(pos) != '\n')
                {
                    ++pos;
                }
                ++pos;
            }
            // skip "x = ..." line
            if ((str.charAt(pos) == 'x')
                    && (str.charAt(pos + 1) == ' ')
                    && (str.charAt(pos + 2) == '='))
            {
                while (str.charAt(pos) != '\n')
                {
                    ++pos;
                }
                ++pos;
            }
            char ch = str.charAt(pos);
            int col = rect.x;
            int maxCol = rect.x + rect.width - 1;
            int row = rect.y;
            int maxRow = rect.y + rect.height - 1;
            if ((ch == '.') || (ch == '*'))
            {
                // behave like it's a bitmap
                while (pos < str.length())
                {
                    ch = str.charAt(pos);
                    switch(ch)
                    {
                    case '.':
                        ++col;
                        break;
                    case '*':
                        if (col <= maxCol)
                        {
                            setSingleCell(col, row, gen, Cell.CELL_ON, undoRec);
                            change = true;
                        }
                        ++col;
                        break;
                    case '\r':
                        // ignore
                        break;
                    case '\n':
                        col = rect.x;
                        ++row;
                        if (row > maxRow)
                        {
                            return change;
                        }
                        break;
                    default: 
                        return change;
                    }
                    ++pos;
                }
            }
            else
            {
                int number = 0;
                while (pos < str.length())
                {
                    ch = str.charAt(pos);
                    if (('0' <= ch) && ('9' >= ch))
                    {
                        if (number < 1000)
                        {
                            number = number * 10 + (ch - '0');
                        }
                    }
                    else if ('b' == ch)
                    {
                        if (0 == number)
                        {
                            ++col;
                        }
                        else
                        {
                            col += number;
                            number = 0;
                        }
                    }
                    else if ('o' == ch)
                    {
                        if (0 == number)
                        {
                            number = 1;
                        }
                        if (number > maxCol - col + 1)
                        {
                            number = maxCol - col + 1;
                        }
                        if (number > 0)
                        {
                            while (number > 0)
                            {
                                --number;
                                setSingleCell(col, row, gen, Cell.CELL_ON, undoRec);
                                ++col;
                            }
                            change = true;
                            // col is shifted
                        }
                        // number is already 0
                    }
                    else if ('$' == ch)
                    {
                        col = rect.x;
                        if (0 == number)
                        {
                            ++row;
                        }
                        else
                        {
                            row += number;
                            number = 0;
                        }
                        if (row > maxRow)
                        {
                            return change;
                        }
                    }
                    else if (('\n' == ch) || ('\r' == ch) || (' ' == ch))
                    {
                        // ignore
                    }
                    else
                    {
                        return change;
                    }
                    ++pos;
                }
                // behave like it's an RLE
            }
        }
        catch (IndexOutOfBoundsException e)
        {
            // do nothing
        }
        return change;
    }
    
    private void setSingleCell(int col, int row, int gen, byte state, DoubleListUndoRecord undoRec)
    {
        int cptr = getCellPtr(col, row, gen);
        if (cells[cptr] != state)
        {
            undoRec.record(cptr, cells[cptr], state);
            cells[cptr] = state;
            int sptr = cptr / properties.getGenerations();
            stacks[sptr] = CellStack.setChanged(stacks[sptr]);
        }
    }
    
    /////////////////////////////////////////////
    // implementation procedures for Undo support
    
    public void setCellsImpl(int[] ptrs, byte[] states)
    {
        int i = ptrs.length;
        while (i > 0)
        {
            --i;
            cells[ptrs[i]] = states[i];
            int sptr = ptrs[i] / properties.getGenerations();
            stacks[sptr] = CellStack.setChanged(stacks[sptr]);
        }
    }
    
    public void setStacksImpl(int[] ptrs, byte[] states)
    {
        int i = ptrs.length;
        while (i > 0)
        {
            --i;
            stacks[ptrs[i]] = CellStack.setChanged(states[i]);
        }
    }
}
