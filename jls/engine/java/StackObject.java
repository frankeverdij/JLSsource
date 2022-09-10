package jls.engine.java;

public interface StackObject
{
    // restore old state
    public void backtrack(int oldState);

    // restore old state and update variables supporting the "prune by combination" search
    public void backtrackWithPruning(int oldState);
    
    // propagate latest state change to related objects
    public boolean propagate();

    // propagate latest state change to related objects and update search pruning variables
    public boolean propagateWithPruning();
}
