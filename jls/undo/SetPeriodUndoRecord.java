package jls.undo;

import jls.EditCellArray;

public class SetPeriodUndoRecord extends DoubleListUndoRecord
{
    public SetPeriodUndoRecord(EditCellArray master)
    {
        super(master);
    }

    @Override
    public void undo()
    {
        master.setStacksImpl(ptrList, oldStateList);
    }

    @Override
    public void redo()
    {
        master.setStacksImpl(ptrList, newStateList);
    }
}
