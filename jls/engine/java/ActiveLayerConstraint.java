package jls.engine.java;

import java.util.Arrays;

public class ActiveLayerConstraint implements StackObject
{
    int state;
    ActiveColumnConstraint[] columnList;
    
    public void setState(int newState)
    {
        state = newState;
    }
    
    public void setColumnList(ActiveColumnConstraint[] newColumnList)
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
    
    public void removeColumn(ActiveColumnConstraint column)
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
