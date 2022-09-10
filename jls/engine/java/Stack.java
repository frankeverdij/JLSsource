package jls.engine.java;

public class Stack
{
    // this setting is intentional
    // values zero and above are used by non-forked objects, such as enforced variables, cones, or other constraints
    // below zero indicates a fork point
    public static final int FORK_POINT = -1;
    public static final int FORK_NONE = 0;
    
    // stack used by variables and constraints to store their previous state to allow backtracking
    // also used by search control to store search fork points
    
    // variables and constraints use it on forced changes
    
 // the stack itself is static to allow access to it from all other objects
    private static StackItem startPointer = new StackItem();
    private static StackItem stackPointer = startPointer;
    private static StackItem propagatePointer = stackPointer; // this one is used to invoke change propagation
    // the one allocated item works as sentinel - it will be always there for the stackPointer to point to when the stack is full
    // that's why we set allocated to zero rather than 1
    private static int allocated = 0; 
    private static int claimed = 0;
    
    // reset the stack, remove references to all objects and adjust stack size to defined 
    
    public static void resetTo(int count)
    {
        // adjust to new size
        resetAndAdjust(count - claimed);
        
        while (stackPointer != null)
        {
            stackPointer.resetObject();
            stackPointer = stackPointer.getNext();
        }
        stackPointer = startPointer;
        propagatePointer = stackPointer;
    }
    
    // reset and update the stack size relatively to current size
    
    public static void resetAndAdjust(int diff)
    {
        // adjust the size
        claimed += diff;
        // System.out.println("Stack adjusted to " + claimed);
        // fill up items if needed
        while (claimed > allocated)
        {
            StackItem it = new StackItem();
            it.setNext(startPointer);
            startPointer.setPrev(it);
            startPointer = it;
            ++allocated;
        }
        // drop some leaving a reserve
        while (claimed < (allocated - 100))
        {
            StackItem it = startPointer;
            startPointer = startPointer.getNext();
            startPointer.setPrev(null);
            it.setNext(null);
            it.resetObject(); // remove the reference to make GC easier
            --allocated;
        }
        stackPointer = startPointer;
        propagatePointer = startPointer;
    }
    
    // reset without changing size of the stack, leave set items as they are
    
    public static void reset()
    {
        // just move the pointer to the start
        stackPointer = startPointer;
        propagatePointer = stackPointer;
    }

    // method used by variables to store information that they have been set
    
    public static void push(StackObject object, int state)
    {
        stackPointer.set(object, state);
        stackPointer = stackPointer.getNext();
    }
    
    // returns whether the stack is empty or not
    
    public static boolean isEmpty()
    {
        return stackPointer == startPointer;
    }
    
    // pops stack records 

    public static void backtrackAll()
    {
        while (stackPointer != startPointer)
        {
            stackPointer = stackPointer.getPrev();
            stackPointer.backtrack();
        }
        propagatePointer = startPointer;
    }
    
    // pops stack records and instead of restoring state requests all variables on it to optimize themselves 

    public static void optimize()
    {
        while (stackPointer != startPointer)
        {
            stackPointer = stackPointer.getPrev();
            if (stackPointer.getObject() instanceof Variable)
            {
                ((Variable) stackPointer.getObject()).optimize();
            }
            --claimed; // decrease number of items on the stack by these items like they were to stay forever
        }
        // System.out.println("Stack optimized to " + claimed);
        propagatePointer = startPointer;
    }
    
    // pops all stack records up to first fork
    // it will throw a NullPointerException when we run out of stack items!!!
    
    public static Variable backtrackToFork() throws NullPointerException
    {
        stackPointer = stackPointer.getPrev();
        while (stackPointer.getState() != FORK_POINT) // <-- this will throw the exception 
        {
            stackPointer.backtrack();
            stackPointer = stackPointer.getPrev();
        }
        propagatePointer = stackPointer;
        // do not backtrack the fork; leave the variable set so that it can be set the other way
        // but the stack item is still removed from the stack
        return (Variable) stackPointer.getObject();
    }
    
    public static Variable backtrackToForkWithPruning() throws NullPointerException
    {
        stackPointer = stackPointer.getPrev();
        while (stackPointer.getState() != FORK_POINT) // <-- this will throw the exception 
        {
            stackPointer.backtrackWithPruning();
            stackPointer = stackPointer.getPrev();
        }
        propagatePointer = stackPointer;
        // do not backtrack the fork; leave the variable set so that it can be set the other way
        // but the stack item is still removed from the stack
        return (Variable) stackPointer.getObject();
    }
    
    public static boolean propagate()
    {
        while (propagatePointer != stackPointer)
        {
            if (!propagatePointer.propagate())
            {
                return false;
            }
            propagatePointer = propagatePointer.getNext();
        }
        return true;
    }

    public static boolean propagateWithPruning()
    {
        while (propagatePointer != stackPointer)
        {
            if (!propagatePointer.propagateWithPruning())
            {
                return false;
            }
            propagatePointer = propagatePointer.getNext();
        }
        return true;
    }
    
    public static void prepareWalk()
    {
        propagatePointer = startPointer;
    }

    public static StackItem stepWalk()
    {
        while (propagatePointer != stackPointer)
        {
            StackItem si = propagatePointer;
            propagatePointer = propagatePointer.getNext();
            if (si.getObject() instanceof Variable)
            {
                return si;
            }
        }
        return null;
    }

    public static void finishWalk()
    {
        propagatePointer = stackPointer;
    }
}
