package jls.undo;

import java.awt.Rectangle;

import jls.EditCellArray;

public class LosslessManipulationUndoRecord implements UndoRecord
{
    public static final int TYPE_SHIFT_DOWN   = 0;
    public static final int TYPE_SHIFT_UP     = 1;
    public static final int TYPE_SHIFT_LEFT   = 2;
    public static final int TYPE_SHIFT_RIGHT  = 3;
    public static final int TYPE_SHIFT_FUTURE = 4;
    public static final int TYPE_SHIFT_PAST   = 5;
    public static final int TYPE_TRANSPOSE    = 6;
    public static final int TYPE_ROTATE_LEFT  = 7;
    public static final int TYPE_ROTATE_RIGHT = 8;
    public static final int TYPE_ROTATE_180   = 9;
    public static final int TYPE_MIRROR_HORZ  = 10;
    public static final int TYPE_MIRROR_VERT  = 11;
    
    private EditCellArray master;
    private Rectangle rectangle;
    private int generation;
    private int type;
    
    public LosslessManipulationUndoRecord(EditCellArray array, Rectangle rect, int gen, int type)
    {
        master = array;
        rectangle = new Rectangle(rect);
        generation = gen;
        this.type = type;
    }

    @Override
    public boolean isStoreable()
    {
        return true;
    }

    @Override
    public int size()
    {
        return 0;
    }

    @Override
    public void undo()
    {
        switch (type)
        {
        case TYPE_SHIFT_DOWN:
            master.shiftUpImpl(rectangle, generation);
            break;
        case TYPE_SHIFT_UP:
            master.shiftDownImpl(rectangle, generation);
            break;
        case TYPE_SHIFT_LEFT:
            master.shiftRightImpl(rectangle, generation);
            break;
        case TYPE_SHIFT_RIGHT:
            master.shiftLeftImpl(rectangle, generation);
            break;
        case TYPE_SHIFT_FUTURE:
            master.shiftPastImpl(rectangle);
            break;
        case TYPE_SHIFT_PAST:
            master.shiftFutureImpl(rectangle);
            break;
        case TYPE_TRANSPOSE:
            master.transposeImpl(rectangle, generation);
            break;
        case TYPE_ROTATE_LEFT:
            master.transposeImpl(rectangle, generation);
            master.mirrorHorzImpl(rectangle, generation);
            break;
        case TYPE_ROTATE_RIGHT:
            master.transposeImpl(rectangle, generation);
            master.mirrorVertImpl(rectangle, generation);
            break;
        case TYPE_ROTATE_180:
            master.mirrorHorzImpl(rectangle, generation);
            master.mirrorVertImpl(rectangle, generation);
            break;
        case TYPE_MIRROR_HORZ:
            master.mirrorHorzImpl(rectangle, generation);
            break;
        case TYPE_MIRROR_VERT:
            master.mirrorVertImpl(rectangle, generation);
            break;
        }
    }

    @Override
    public void redo()
    {
        switch (type)
        {
        case TYPE_SHIFT_DOWN:
            master.shiftDownImpl(rectangle, generation);
            break;
        case TYPE_SHIFT_UP:
            master.shiftUpImpl(rectangle, generation);
            break;
        case TYPE_SHIFT_LEFT:
            master.shiftLeftImpl(rectangle, generation);
            break;
        case TYPE_SHIFT_RIGHT:
            master.shiftRightImpl(rectangle, generation);
            break;
        case TYPE_SHIFT_FUTURE:
            master.shiftFutureImpl(rectangle);
            break;
        case TYPE_SHIFT_PAST:
            master.shiftPastImpl(rectangle);
            break;
        case TYPE_TRANSPOSE:
            master.transposeImpl(rectangle, generation);
            break;
        case TYPE_ROTATE_LEFT:
            master.transposeImpl(rectangle, generation);
            master.mirrorVertImpl(rectangle, generation);
            break;
        case TYPE_ROTATE_RIGHT:
            master.transposeImpl(rectangle, generation);
            master.mirrorHorzImpl(rectangle, generation);
            break;
        case TYPE_ROTATE_180:
            master.mirrorHorzImpl(rectangle, generation);
            master.mirrorVertImpl(rectangle, generation);
            break;
        case TYPE_MIRROR_HORZ:
            master.mirrorHorzImpl(rectangle, generation);
            break;
        case TYPE_MIRROR_VERT:
            master.mirrorVertImpl(rectangle, generation);
            break;
        }
    }
}
