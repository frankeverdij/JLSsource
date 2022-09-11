package jls;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class JlsAction extends AbstractAction
{

    private static final long serialVersionUID = 1;

    private ContentPane mainPane;

    public JlsAction(ContentPane mainPane)
    {
        this.mainPane = mainPane;
    }

    public void actionPerformed(ActionEvent arg0)
    {
        mainPane.handleAction(this);
    }
}
