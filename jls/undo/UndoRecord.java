package jls.undo;

public interface UndoRecord
{
    // method to perform undo action(s)
    public void undo();

    // method to perform redo action(s)
    public void redo();

    // returns relative size of the record (number of cells stored in the record)
    // to allow discarding undo records if they would take too much space
    public int size();

    // returns true if this record is worth storing in the queue
    // can also perform internal optimization as after this call there will be
    // no further data changes in the record
    public boolean isStoreable();
}
