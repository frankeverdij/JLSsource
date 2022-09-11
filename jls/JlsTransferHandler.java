package jls;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class JlsTransferHandler extends TransferHandler
{
    static final long serialVersionUID = 1;

    protected Transferable createTransferable(JComponent c) 
    {
        return new StringSelection(exportString(c));
    }

    public int getSourceActions(JComponent c) 
    {
        return COPY;
    }

    public boolean importData(JComponent c, Transferable t) 
    {
        if (canImport(c, t.getTransferDataFlavors())) 
        {
            try 
            {
                String str = (String)t.getTransferData(DataFlavor.stringFlavor);
                importString(c, str);
                return true;
            } 
            catch (UnsupportedFlavorException ufe) 
            {
            } 
            catch (IOException ioe) 
            {
            }
        }
        return false;
    }    

    public boolean canImport(JComponent c, DataFlavor[] flavors) 
    {
        for (int i = 0; i < flavors.length; i++) 
        {
            if (DataFlavor.stringFlavor.equals(flavors[i])) 
            {
                return true;
            }
        }
        return false;
    }
    
    protected void exportDone(JComponent c, Transferable data, int action) 
    {
        cleanup(c, action == MOVE);
    }

    protected void cleanup(JComponent c, boolean remove)
    {
        
    }
    
    protected void importString(JComponent c, String str) 
    {
        ((ContentPane)c).pasteString(str);
    }

    protected String exportString(JComponent c) 
    {
        return ((ContentPane)c).getRleString();
    }
}
