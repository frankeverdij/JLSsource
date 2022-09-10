package jls.engine.java;

import java.util.Arrays;

public class LiveLayerConstraint implements StackObject
{
    // state contains number of columns which can still turn live
    // when zero, all remaining columns need to be locked
    int state;
    // list of columns
    LiveColumnConstraint[] columnList;
    
    public void setState(int newState)
    {
        state = newState;
    }
    
    public void setColumnList(LiveColumnConstraint[] newColumnList)
    {
        columnList = newColumnList;
    }
    
    public void destroy()
    {
        int i = columnList.length;
        while (i > 0)
        {
            --i;
            columnList[i].destroy();
            columnList[i] = null;
        }
    }
    
    // method for column constraint to inform that it has become live
    public boolean fire(int weight)
    {
        if (state >= weight)
        {
            Stack.push(this, state);
            state -= weight;
            return true;
        }
        return false;
    }

    @Override
    public void backtrack(int oldState)
    {
        state = oldState;
    }

    @Override
    public void backtrackWithPruning(int oldState)
    {
        state = oldState;
    }

    public void optimize()
    {
        int i = columnList.length;
        int w = 0;
        while (i > 0)
        {
            --i;
            w += columnList[i].getWeight();
        }

        if (state >= w)
        {
            i = columnList.length;
            while (i > 0)
            {
                --i;
                columnList[i].destroy();
                columnList[i] = null;
            }
        }
    }
    
    // here a column can ask us to get removed from the list
    // because it decided it's not needed anymore (during optimization)
    public void removeColumn(LiveColumnConstraint column)
    {
        int i = columnList.length;
        while (i > 0)
        {
            --i;
            if (columnList[i] == column)
            {
                columnList[i] = columnList[columnList.length - 1];
                columnList = Arrays.copyOf(columnList, columnList.length - 1);
                optimize();
                return;
            }
        }
        throw new RuntimeException("Column is not registered");
    }

    @Override
    public boolean propagate()
    {
        if (state == 0)
        {
            int i = columnList.length;
            while (i > 0)
            {
                --i;
                columnList[i].lock();
            }
        }
        return true;
    }

    @Override
    public boolean propagateWithPruning()
    {
        if (state == 0)
        {
            int i = columnList.length;
            while (i > 0)
            {
                --i;
                columnList[i].lock();
            }
        }
        return true;
    }

}
