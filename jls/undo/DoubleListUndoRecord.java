package jls.undo;

import java.util.Arrays;

import jls.EditCellArray;

public class DoubleListUndoRecord implements UndoRecord
{
    EditCellArray master = null;
    int[] ptrList = new int[1];
    byte[] oldStateList = new byte[1];
    byte[] newStateList = new byte[1];
    int count = 0;

    public DoubleListUndoRecord(EditCellArray master)
    {
        this.master = master;
    }

    @Override
    public boolean isStoreable()
    {
        if (count == 0)
        {
            return false;
        }
        if (count < ptrList.length)
        {
            ptrList = Arrays.copyOf(ptrList, count);
            oldStateList = Arrays.copyOf(oldStateList, count);
            newStateList = Arrays.copyOf(newStateList, count);
        }
        return true;
    }

    @Override
    public void undo()
    {
        master.setCellsImpl(ptrList, oldStateList);
    }

    @Override
    public void redo()
    {
        master.setCellsImpl(ptrList, newStateList);
    }

    @Override
    public int size()
    {
        return count;
    }

    public void record(int ptr, byte oldState, byte newState)
    {
        if (oldState != newState)
        {
            if (count == ptrList.length)
            {
                ptrList = Arrays.copyOf(ptrList, count * 2);
                oldStateList = Arrays.copyOf(oldStateList, count * 2);
                newStateList = Arrays.copyOf(newStateList, count * 2);
            }
            ptrList[count] = ptr;
            oldStateList[count] = oldState;
            newStateList[count] = newState;
            ++count;
        }
    }
}
