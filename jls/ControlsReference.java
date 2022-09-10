package jls;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.Font;
import javax.swing.JScrollPane;
import java.awt.Dimension;

public class ControlsReference extends JFrame
{

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JTextArea jTextArea = null;

    private JScrollPane jScrollPane = null;

    /**
     * This is the default constructor
     */
    public ControlsReference(JComponent owner)
    {
        super();
        initialize(owner);
        
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(JComponent owner)
    {
        this.setSize(386, 205);
        this.setPreferredSize(new Dimension(600, 400));
        this.setContentPane(getJContentPane());
        this.setTitle("Controls Reference");
        this.setLocationRelativeTo(owner);
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane()
    {
        if (jContentPane == null)
        {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    /**
     * This method initializes jTextArea	
     * 	
     * @return javax.swing.JTextArea	
     */
    private JTextArea getJTextArea()
    {
        if (jTextArea == null)
        {
            jTextArea = new JTextArea();
            jTextArea.setEditable(false);
            jTextArea.setFont(new Font("DialogInput", Font.PLAIN, 12));
            jTextArea.setLineWrap(true);
            jTextArea.setWrapStyleWord(true);
            jTextArea.setText(
                    "=== Display ===\n"
                    + "\n"
                    + "-- Cells --\n"
                    + "\n"
                    + "[ ] ... EMPTY cell, may be set to ON or OFF during search.\n"
                    + "[O] ... SET cell, ON for full circle, OFF for empty circle.\n"
                    + "        State determined by search in an otherwise empty cell\n"
                    + "        is displayed in dimmed color.\n"
                    + "        Red state color means there's an error in this cell.\n"
                    + "[F] ... FROZEN cell, will be set to the same value as previous\n"
                    + "        generation cell (only if that cell is EMPTY).\n"
                    + "[X] ... UNCHECKED cell, not searched but may be set by rules\n"
                    + "        and surrounding cells\n"
                    + "        Note: cell may be soft unchecked and frozen at the same time.\n"
                    + "[#] ... UNSET cell, counts as empty and will never be set.\n"
                    + "[v] ... goes with error, marks a cell which cannot be set\n"
                    + "        neither ON nor OFF\n"
                    + "\n"
                    + "-- Cell border --\n"
                    + "\n"
                    + "Black\n"
                    + "  Normal cell border\n"
                    + "Blue or Green\n"
                    + "  Also normal, used to mark different constraint layers\n"
                    + "Red\n"
                    + "  Indicates an error somewhere in the cell column\n"
                    + "\n"
                    + "Thin border\n"
                    + "   All cells in the column have the same state\n"
                    + "Thick border\n"
                    + "   There are different cell states in the column\n"
                    + "\n"
                    + "-- Cell background --\n"
                    + "\n"
                    + "Gray\n"
                    + "  Standard background, cells have full period\n"
                    + "Colored (respectively: yellow, green, cyan, purple, orange, blue)\n"
                    + "  Subperiod according to Properties.\n"
                    + "\n"
                    + "Selected cells are displayed in darker colors\n"
                    + "\n"
                    + "=== Controls ===\n"
                    + "\n"
                    + "-- Mouse --\n"
                    + "\n"
                    + "Click left on cell to set it ON\n"
                    + "Click left on ON cell to set it EMPTY\n"
                    + "Click right on cell to set it OFF\n"
                    + "Click right on OFF cell to set it EMPTY\n"
                    + "Click middle on cell to set it as is (useful with block or SHIFT)\n"
                    + "Click & drag to select block\n"
                    + "Click outside block to deselect it\n"
                    + "Click on a cell inside block to set all cells in the block the same way\n"
                    + "Hold SHIFT to extend the operation to all generations\n"
                    + "Hold SHIFT when selecting to select square field\n"
                    + "Mouse wheel changes displayed generation\n"
                    + "\n"
                    + "-- Keyboard --\n"
                    + "\n"
                    + "Basic cell editing:\n"
                    + "(works for cell under cursor or for selected block)\n"
                    + "(hold SHIFT to apply to all generations)\n"
                    + "\n"
                    + "S ... set cells ON\n"
                    + "A ... set cells OFF\n"
                    + "C ... set cells EMPTY\n"
                    + "F ... set cells FROZEN\n"
                    + "X ... set cells UNCHECKED\n"
                    + "U ... set cells UNSET\n"
                    + "I ... set UNCHECKED cells EMPTY\n"
                    + "O ... set EMPTY cells UNCHECKED\n"
                    + "\n"
                    + "Period settings:\n"
                    + "(works for cell under cursor or for selected block)\n"
                    + "(use numeric keys on main keyboard)\n"
                    + "\n"
                    + "0 ... set base period\n"
                    + "1-6 ... set period 1-6\n"
                    + "\n"
                    + "Block commands:\n"
                    + "(a block must be selected for these to work)\n"
                    + "(hold SHIFT to apply to all generations)\n"
                    + "\n"
                    + "Ctrl + Arrow keys ... shift block contents\n"
                    + "Shift + PageDown ... shift block contents to future (all gen only)\n"
                    + "Shift + PageUp ... shift block contents to past (all gen only)\n"
                    + "H ... mirror left <-> right\n"
                    + "V ... mirror up <-> down\n"
                    + "T ... transpose (only for square selection)\n"
                    + "R ... rotate block clockwise (square selection)\n"
                    + "E ... rotate block anti-clockwise (square selection)\n"
                    + "W ... rotate  block 180 degrees\n"
                    + "\n"
                    + "Miscellaneous:\n"
                    + "\n"
                    + "Arrow keys ... panning"
                    );
        }
        return jTextArea;
    }

    /**
     * This method initializes jScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPane()
    {
        if (jScrollPane == null)
        {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJTextArea());
        }
        return jScrollPane;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
