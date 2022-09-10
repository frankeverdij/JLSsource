package jls.engine.java;

public interface Constraint
{
    // request the object to optimize itself according to
    // new variable settings
    public void optimize();

    // inform the constraint that a variable has changed ot ON
    public boolean fireOn(int stateChange);

    // inform the constraint that a variable has changed ot OFF
    public boolean fireOff(int stateChange);
}
