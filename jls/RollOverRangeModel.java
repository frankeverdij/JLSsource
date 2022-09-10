package jls;

import javax.swing.DefaultBoundedRangeModel;

public class RollOverRangeModel extends DefaultBoundedRangeModel
{
    private static final long serialVersionUID = 1;

    public void setValue(int n)
    {
        if (n < getMinimum())
        {
            super.setValue(getMaximum() - getExtent());
        }
        else if (n > getMaximum() - getExtent())
        {
            super.setValue(getMinimum());
        }
        else
        {
            super.setValue(n);
        }
    }

}
