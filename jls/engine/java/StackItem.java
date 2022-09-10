package jls.engine.java;

// kind of objects to be used on the Stack

public class StackItem
{
    private StackObject object;
    private int state;
    private StackItem next = null;
    private StackItem prev = null;
    
    public void set(StackObject object, int state)
    {
        this.object = object;
        this.state = state;
    }

    public void backtrack()
    {
        object.backtrack(state);
    }
    
    public void backtrackWithPruning()
    {
        object.backtrackWithPruning(state);
    }
    
    public boolean propagate()
    {
        return object.propagate();
    }
    
    public boolean propagateWithPruning()
    {
        return object.propagateWithPruning();
    }
    
    public void setNext(StackItem next)
    {
        this.next = next;
    }
    
    public StackItem getNext()
    {
        return next;
    }
    
    public void setPrev(StackItem prev)
    {
        this.prev = prev;
    }
    
    public StackItem getPrev()
    {
        return prev;
    }
    
    public int getState()
    {
        return state;
    }
    
    public StackObject getObject()
    {
        return object;
    }
    
    public void resetObject()
    {
        object = null;
    }
}
