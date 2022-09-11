package jls;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ContentPane extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, AdjustmentListener, ActionListener
{
    private static final long serialVersionUID = 1;

    private static final int USERSTATE_EDIT = 0;
    private static final int USERSTATE_SEARCH = 1;
    private static final int USERSTATE_PAUSE = 2;
    
    private static final int THREADSTATE_STOPPED = 0;
    private static final int THREADSTATE_RUNNING = 1;
    private static final int THREADSTATE_STOPPING = 2;
    private static final int THREADSTATE_PAUSED = 3;
    private static final int THREADSTATE_PAUSING = 4;

    private static final String[] THREADSTATE_NAMES = { "THREADSTATE_STOPPED", "THREADSTATE_RUNNING", "THREADSTATE_STOPPING", "THREADSTATE_PAUSED", "THREADSTATE_PAUSING" };
    
    public static final int USRCMD_PREPROCESS = 0;
    public static final int USRCMD_SEARCH = 1;
    public static final int USRCMD_PAUSE = 2;
    public static final int USRCMD_CONTINUE = 3;
    public static final int USRCMD_ACCEPT_STATE = 4;
    public static final int USRCMD_SHOW_RESULT = 5;
    public static final int USRCMD_SHOW_COMBINATION = 6;
    public static final int USRCMD_RESET = 7;
    public static final int USRCMD_SAVE = 8;
    public static final int USRCMD_LOAD = 9;
    public static final int USRCMD_OPTIONS = 10;
    
    public static final String[] USRCMD_NAMES = { "USRCMD_PREPROCESS",
            "USRCMD_SEARCH", "USRCMD_PAUSE", "USRCMD_CONTINUE",
            "USRCMD_ACCEPT_STATE", "USRCMD_SHOW_RESULT", "USRCMD_SHOW_COMBINATION",
            "USRCMD_RESET", "USRCMD_SAVE", "USRCMD_LOAD", "USRCMD_OPTIONS" };
    
    // name of file to save/load
    private String fileName;
    
    private int mousePosX = -1;
    private int mousePosY = -1;
    
    private int savedGen = 0;

    private JFileChooser stateFileChooser = null;
    private JFileChooser resultFileChooser = null;
    private JFrame mainFrame;

    private Properties properties;
    private SearchThread searchThread;

    private CellPane cellPane;
    private EditCellArray cellArray;
    private CellArray resultArray;
    private SearchOptions searchOptions;

    private int userState = USERSTATE_EDIT;
    private int threadState = THREADSTATE_STOPPED;
    
    private List<Integer> commandList = new LinkedList<Integer>();
    
    private JTextField genNoField;
    private JScrollBar genScroll;
    private JPanel statusPanel;
    private JScrollPane scrollPane;
    private JTextField mousePosField;
    private JTextField searchThreadMessage;

    private JMenuItem openFileMenuItem;
    private JMenuItem saveFileMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem pasteMenuItem;
    private JMenuItem nextGenMenuItem;
    private JMenuItem prevGenMenuItem;
    private JMenuItem firstGenMenuItem;
    private JMenuItem lastGenMenuItem;
    private JMenuItem selAllMenuItem;
    private JMenuItem selNoneMenuItem;
    private JMenuItem propertiesMenuItem;
    private JMenuItem incSizeMenuItem;
    private JMenuItem decSizeMenuItem;
    private JMenuItem controlsReferenceMenuItem;
    private JMenuItem searchOptionsMenuItem;
    private JMenuItem searchPrepareMenuItem;
    private JMenuItem searchStartMenuItem;
    private JMenuItem searchPauseMenuItem;
    private JMenuItem searchContinueMenuItem;
    private JMenuItem searchResetMenuItem;
    private JMenuItem searchAcceptStateMenuItem;
    private JMenuItem searchShowResultMenuItem; 
    private JMenuItem searchShowCombinationMenuItem; 

    private Action setOnAction;
    private Action setOnAllAction;
    private Action setOffAction;
    private Action setOffAllAction;
    private Action setEmptyAction;
    private Action setEmptyAllAction;
    private Action setFrozenAction;
    private Action setFrozenAllAction;
    private Action setUncheckedAction;
    private Action setUncheckedAllAction;
    private Action setHardAction;
    private Action setHardAllAction;
    private Action changeUncheckedAction;
    private Action changeUncheckedAllAction;
    private Action changeEmptyAction;
    private Action changeEmptyAllAction;
    private Action setPeriod0Action;
    private Action setPeriod1Action;
    private Action setPeriod2Action;
    private Action setPeriod3Action;
    private Action setPeriod4Action;
    private Action setPeriod5Action;
    private Action setPeriod6Action;
    private Action shiftDownAction;
    private Action shiftDownAllAction;
    private Action shiftUpAction;
    private Action shiftUpAllAction;
    private Action shiftLeftAction;
    private Action shiftLeftAllAction;
    private Action shiftRightAction;
    private Action shiftRightAllAction;
    private Action shiftFutureAction;
    private Action shiftPastAction;
    private Action transposeAction;
    private Action transposeAllAction;
    private Action rotateRightAction;
    private Action rotateRightAllAction;
    private Action rotateLeftAction;
    private Action rotateLeftAllAction;
    private Action rotate180Action;
    private Action rotate180AllAction;
    private Action mirrorHorizontalAction;
    private Action mirrorHorizontalAllAction;
    private Action mirrorVerticalAction;
    private Action mirrorVerticalAllAction;
    private Action panUpAction;
    private Action panDownAction;
    private Action panLeftAction;
    private Action panRightAction;
    
    private Action registerAction(String name, KeyStroke key)
    {
        Action action = new JlsAction(this);
        getInputMap().put(key, name);
        getActionMap().put(name, action);
        return action;
    }

    public ContentPane(JFrame frame, String fileToLoad)
    {
        super(new BorderLayout());

        mainFrame = frame;
        properties = new Properties();
        searchOptions = new SearchOptions();
        searchThread = new SearchThread(this, searchOptions);
        searchThread.execute();
        cellArray = new EditCellArray();
        cellArray.reconfigure(properties);
        resultArray = new CellArray();
        stateFileChooser = new JFileChooser();
        stateFileChooser.setFileFilter(new FileNameExtensionFilter("JavaLifeSearch Status file (*.jdf)", "jdf"));
        resultFileChooser = new JFileChooser();
        resultFileChooser.setFileFilter(new FileNameExtensionFilter("Life object file (*.lif)", "lif"));

        setOnAction = registerAction("set ON", KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
        setOnAllAction = registerAction("set ON all", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_DOWN_MASK));
        setOffAction = registerAction("set OFF", KeyStroke.getKeyStroke(KeyEvent.VK_A, 0));
        setOffAllAction = registerAction("set OFF all", KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_DOWN_MASK));
        setEmptyAction = registerAction("set EMP", KeyStroke.getKeyStroke(KeyEvent.VK_C, 0));
        setEmptyAllAction = registerAction("set EMP all", KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.SHIFT_DOWN_MASK));
        setFrozenAction = registerAction("set FRZ", KeyStroke.getKeyStroke(KeyEvent.VK_F, 0));
        setFrozenAllAction = registerAction("set FRZ all", KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.SHIFT_DOWN_MASK));
        setUncheckedAction = registerAction("set UNC", KeyStroke.getKeyStroke(KeyEvent.VK_X, 0));
        setUncheckedAllAction = registerAction("set UNC all", KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.SHIFT_DOWN_MASK));
        setHardAction = registerAction("set HAR", KeyStroke.getKeyStroke(KeyEvent.VK_U, 0));
        setHardAllAction = registerAction("set HAR all", KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.SHIFT_DOWN_MASK));
        changeUncheckedAction = registerAction("EMP2UNC", KeyStroke.getKeyStroke(KeyEvent.VK_O, 0));
        changeUncheckedAllAction = registerAction("EMP2UNC all", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.SHIFT_DOWN_MASK));
        changeEmptyAction = registerAction("UNC2EMP", KeyStroke.getKeyStroke(KeyEvent.VK_I, 0));
        changeEmptyAllAction = registerAction("UNC2EMP all", KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.SHIFT_DOWN_MASK));
        setPeriod0Action = registerAction("set period 0", KeyStroke.getKeyStroke(KeyEvent.VK_0, 0));
        setPeriod1Action = registerAction("set period 1", KeyStroke.getKeyStroke(KeyEvent.VK_1, 0));
        setPeriod2Action = registerAction("set period 2", KeyStroke.getKeyStroke(KeyEvent.VK_2, 0));
        setPeriod3Action = registerAction("set period 3", KeyStroke.getKeyStroke(KeyEvent.VK_3, 0));
        setPeriod4Action = registerAction("set period 4", KeyStroke.getKeyStroke(KeyEvent.VK_4, 0));
        setPeriod5Action = registerAction("set period 5", KeyStroke.getKeyStroke(KeyEvent.VK_5, 0));
        setPeriod6Action = registerAction("set period 6", KeyStroke.getKeyStroke(KeyEvent.VK_6, 0));
        shiftDownAction = registerAction("shift down", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK));
        shiftDownAllAction = registerAction("shift down all", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK + InputEvent.CTRL_DOWN_MASK));
        shiftUpAction = registerAction("shift up", KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK));
        shiftUpAllAction = registerAction("shift up all", KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK + InputEvent.CTRL_DOWN_MASK));
        shiftLeftAction = registerAction("shift left", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK));
        shiftLeftAllAction = registerAction("shift left all", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK + InputEvent.CTRL_DOWN_MASK));
        shiftRightAction = registerAction("shift right", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK));
        shiftRightAllAction = registerAction("shift right all", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK + InputEvent.CTRL_DOWN_MASK));
        shiftFutureAction = registerAction("shift future", KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.SHIFT_DOWN_MASK));
        shiftPastAction = registerAction("shift past", KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.SHIFT_DOWN_MASK));
        transposeAction = registerAction("transpose", KeyStroke.getKeyStroke(KeyEvent.VK_T, 0));
        transposeAllAction = registerAction("transpose all", KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.SHIFT_DOWN_MASK));
        rotateRightAction = registerAction("rotate CW", KeyStroke.getKeyStroke(KeyEvent.VK_R, 0));
        rotateRightAllAction = registerAction("rotate CW all", KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_DOWN_MASK));
        rotateLeftAction = registerAction("rotate ACW", KeyStroke.getKeyStroke(KeyEvent.VK_E, 0));
        rotateLeftAllAction = registerAction("rotate ACW all", KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_DOWN_MASK));
        rotate180Action = registerAction("rotate 180", KeyStroke.getKeyStroke(KeyEvent.VK_W, 0));
        rotate180AllAction = registerAction("rotate 180 all", KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.SHIFT_DOWN_MASK));
        mirrorHorizontalAction = registerAction("mirror horz", KeyStroke.getKeyStroke(KeyEvent.VK_H, 0));
        mirrorHorizontalAllAction = registerAction("mirror horz all", KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.SHIFT_DOWN_MASK));
        mirrorVerticalAction = registerAction("mirror vert", KeyStroke.getKeyStroke(KeyEvent.VK_V, 0));
        mirrorVerticalAllAction = registerAction("mirror vert all", KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.SHIFT_DOWN_MASK));
        panDownAction = registerAction("pan down", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        panUpAction = registerAction("pan up", KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        panLeftAction = registerAction("pan left", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        panRightAction = registerAction("pan right", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        openFileMenuItem = new JMenuItem("Open...");
        openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openFileMenuItem.setMnemonic(KeyEvent.VK_O);
        openFileMenuItem.addActionListener(this);
        menu.add(openFileMenuItem);;
        saveFileMenuItem = new JMenuItem("Save...");
        saveFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveFileMenuItem.setMnemonic(KeyEvent.VK_S);
        saveFileMenuItem.addActionListener(this);
        menu.add(saveFileMenuItem);
        menu.addSeparator();
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.addActionListener(this);
        menu.add(exitMenuItem);
        menuBar.add(menu);

        menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);

        undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        undoMenuItem.setMnemonic(KeyEvent.VK_U);
        undoMenuItem.addActionListener(this);
        menu.add(undoMenuItem);
        redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        redoMenuItem.setMnemonic(KeyEvent.VK_R);
        redoMenuItem.addActionListener(this);
        menu.add(redoMenuItem);
        menu.addSeparator();
        
        copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.setActionCommand((String)TransferHandler.getCopyAction().getValue(Action.NAME));
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        copyMenuItem.setMnemonic(KeyEvent.VK_C);
        copyMenuItem.addActionListener(this);
        menu.add(copyMenuItem);
        pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.setActionCommand((String)TransferHandler.getPasteAction().getValue(Action.NAME));
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        pasteMenuItem.setMnemonic(KeyEvent.VK_P);
        pasteMenuItem.addActionListener(this);
        menu.add(pasteMenuItem);
        menu.addSeparator();

        selAllMenuItem = new JMenuItem("Select all");
        selAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        selAllMenuItem.setMnemonic(KeyEvent.VK_A);
        selAllMenuItem.addActionListener(this);
        menu.add(selAllMenuItem);
        selNoneMenuItem = new JMenuItem("Unselect");
        selNoneMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
        selNoneMenuItem.setMnemonic(KeyEvent.VK_N);
        selNoneMenuItem.addActionListener(this);
        menu.add(selNoneMenuItem);
        menu.addSeparator();
        propertiesMenuItem = new JMenuItem("Properties...");
        propertiesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        propertiesMenuItem.setMnemonic(KeyEvent.VK_R);
        propertiesMenuItem.addActionListener(this);
        menu.add(propertiesMenuItem);
        menuBar.add(menu);

        menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);
        nextGenMenuItem = new JMenuItem("Next generation");
        nextGenMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0));
        nextGenMenuItem.setMnemonic(KeyEvent.VK_N);
        nextGenMenuItem.addActionListener(this);
        menu.add(nextGenMenuItem);
        prevGenMenuItem = new JMenuItem("Previous generation");
        prevGenMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0));
        prevGenMenuItem.setMnemonic(KeyEvent.VK_P);
        prevGenMenuItem.addActionListener(this);
        menu.add(prevGenMenuItem);
        menu.addSeparator();
        firstGenMenuItem = new JMenuItem("First generation / back");
        firstGenMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0));
        firstGenMenuItem.setMnemonic(KeyEvent.VK_F);
        firstGenMenuItem.addActionListener(this);
        menu.add(firstGenMenuItem);
        lastGenMenuItem = new JMenuItem("Last generation / back");
        lastGenMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0));
        lastGenMenuItem.setMnemonic(KeyEvent.VK_L);
        lastGenMenuItem.addActionListener(this);
        menu.add(lastGenMenuItem);
        menu.addSeparator();
        incSizeMenuItem = new JMenuItem("Increase cell size");
        incSizeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0));
        incSizeMenuItem.setMnemonic(KeyEvent.VK_I);
        incSizeMenuItem.addActionListener(this);
        menu.add(incSizeMenuItem);
        decSizeMenuItem = new JMenuItem("Decrease cell size");
        decSizeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0));
        decSizeMenuItem.setMnemonic(KeyEvent.VK_D);
        decSizeMenuItem.addActionListener(this);
        menu.add(decSizeMenuItem);
        menuBar.add(menu);
        
        menu = new JMenu("Search");
        menu.setMnemonic(KeyEvent.VK_S);
        searchOptionsMenuItem = new JMenuItem("Options...");
        searchOptionsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
        searchOptionsMenuItem.setMnemonic(KeyEvent.VK_O);
        searchOptionsMenuItem.addActionListener(this);
        menu.add(searchOptionsMenuItem);
        menu.addSeparator();
        searchPrepareMenuItem = new JMenuItem("Prepare");
        searchPrepareMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        searchPrepareMenuItem.setMnemonic(KeyEvent.VK_E);
        searchPrepareMenuItem.addActionListener(this);
        menu.add(searchPrepareMenuItem);
        searchStartMenuItem = new JMenuItem("Start");
        searchStartMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
        searchStartMenuItem.setMnemonic(KeyEvent.VK_S);
        searchStartMenuItem.addActionListener(this);
        menu.add(searchStartMenuItem);
        menu.addSeparator();
        searchPauseMenuItem = new JMenuItem("Pause");
        searchPauseMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        searchPauseMenuItem.setMnemonic(KeyEvent.VK_P);
        searchPauseMenuItem.addActionListener(this);
        menu.add(searchPauseMenuItem);
        searchContinueMenuItem = new JMenuItem("Continue");
        searchContinueMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
        searchContinueMenuItem.setMnemonic(KeyEvent.VK_C);
        searchContinueMenuItem.addActionListener(this);
        menu.add(searchContinueMenuItem);
        menu.addSeparator();
        searchResetMenuItem = new JMenuItem("Reset");
        searchResetMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        searchResetMenuItem.setMnemonic(KeyEvent.VK_R);
        searchResetMenuItem.addActionListener(this);
        menu.add(searchResetMenuItem);
        searchAcceptStateMenuItem = new JMenuItem("Accept displayed state");
        searchAcceptStateMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK));
        searchAcceptStateMenuItem.setMnemonic(KeyEvent.VK_A);
        searchAcceptStateMenuItem.addActionListener(this);
        menu.add(searchAcceptStateMenuItem);
        searchShowResultMenuItem = new JMenuItem("Show search state"); 
        searchShowResultMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_DOWN_MASK)); 
        searchShowResultMenuItem.setMnemonic(KeyEvent.VK_H); 
        searchShowResultMenuItem.addActionListener(this); 
        menu.add(searchShowResultMenuItem); 
        searchShowCombinationMenuItem = new JMenuItem("Show combination"); 
        searchShowCombinationMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK)); 
        searchShowCombinationMenuItem.setMnemonic(KeyEvent.VK_M); 
        searchShowCombinationMenuItem.addActionListener(this); 
        menu.add(searchShowCombinationMenuItem); 
        menuBar.add(menu);

        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        controlsReferenceMenuItem = new JMenuItem("Controls Reference");
        controlsReferenceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        controlsReferenceMenuItem.setMnemonic(KeyEvent.VK_C);
        controlsReferenceMenuItem.addActionListener(this);
        menu.add(controlsReferenceMenuItem);
        menuBar.add(menu);

        add(menuBar, BorderLayout.NORTH);

        SpringLayout statusLayout = new SpringLayout();
        statusPanel = new JPanel(statusLayout);
        add(statusPanel, BorderLayout.SOUTH);
        JLabel genLabel = new JLabel("Gen.");
        statusPanel.add(genLabel);
        
        genNoField = new JTextField(2);
        genNoField.setEditable(false);
        genNoField.setText("0");
        genNoField.setFocusable(false);
        genNoField.setHorizontalAlignment(JTextField.RIGHT);
        statusPanel.add(genNoField);

        genScroll = new JScrollBar(JScrollBar.HORIZONTAL);
        genScroll.setModel(new RollOverRangeModel());
        genScroll.setValues(0, 1, 0, properties.getGenerations());
        genScroll.setPreferredSize(new Dimension(150, genScroll.getPreferredSize().height));
        genScroll.setBlockIncrement(1);
        genScroll.addAdjustmentListener(this);
        statusPanel.add(genScroll);

        mousePosField = new JTextField(11);
        mousePosField.setEditable(false);
        mousePosField.setText("");
        mousePosField.setFocusable(false);
        mousePosField.setHorizontalAlignment(JTextField.LEFT);
        statusPanel.add(mousePosField);

        searchThreadMessage = new JTextField(20);
        searchThreadMessage.setEditable(false);
        searchThreadMessage.setText("");
        searchThreadMessage.setFocusable(false);
        statusPanel.add(searchThreadMessage, BorderLayout.CENTER);
        
        statusLayout.putConstraint(SpringLayout.WEST, genLabel, 5, SpringLayout.WEST, statusPanel);
        statusLayout.putConstraint(SpringLayout.WEST, genNoField, 5, SpringLayout.EAST, genLabel);
        statusLayout.putConstraint(SpringLayout.EAST, genNoField, genNoField.getPreferredSize().width, SpringLayout.WEST, genNoField);
        statusLayout.putConstraint(SpringLayout.WEST, genScroll, 5, SpringLayout.EAST, genNoField);
        statusLayout.putConstraint(SpringLayout.EAST, genScroll, genScroll.getPreferredSize().width, SpringLayout.WEST, genScroll);
        statusLayout.putConstraint(SpringLayout.WEST, mousePosField, 5, SpringLayout.EAST, genScroll);
        statusLayout.putConstraint(SpringLayout.EAST, mousePosField, mousePosField.getPreferredSize().width, SpringLayout.WEST, mousePosField);
        statusLayout.putConstraint(SpringLayout.WEST, searchThreadMessage, 5, SpringLayout.EAST, mousePosField);
        statusLayout.putConstraint(SpringLayout.EAST, statusPanel, 5, SpringLayout.EAST, searchThreadMessage);

        int h1 = genLabel.getPreferredSize().height;
        int h2 = genNoField.getPreferredSize().height;
        int h3 = genScroll.getPreferredSize().height;
        int h4 = mousePosField.getPreferredSize().height;
        int h5 = searchThreadMessage.getPreferredSize().height;
        int hmax = Math.max(Math.max(h1, h2), Math.max(h3, Math.max(h4, h5)));
        h1 = (hmax + 1 - h1) / 2;
        h2 = (hmax + 1 - h2) / 2;
        h3 = (hmax + 1 - h3) / 2;
        h4 = (hmax + 1 - h4) / 2;
        h5 = (hmax + 1 - h5) / 2;
        
        
        statusLayout.putConstraint(SpringLayout.SOUTH, statusPanel, 10 + hmax, SpringLayout.NORTH, statusPanel);
        statusLayout.putConstraint(SpringLayout.NORTH, genLabel, 5 + h1, SpringLayout.NORTH, statusPanel);
        statusLayout.putConstraint(SpringLayout.NORTH, genNoField, 5 + h2, SpringLayout.NORTH, statusPanel);
        statusLayout.putConstraint(SpringLayout.NORTH, genScroll, 5 + h3, SpringLayout.NORTH, statusPanel);
        statusLayout.putConstraint(SpringLayout.NORTH, mousePosField, 5 + h4, SpringLayout.NORTH, statusPanel);
        statusLayout.putConstraint(SpringLayout.NORTH, searchThreadMessage, 5 + h5, SpringLayout.NORTH, statusPanel);
        
        cellPane = new CellPane(properties, cellArray, resultArray);
        cellPane.addMouseListener(this);
        cellPane.addMouseMotionListener(this);
        cellPane.addMouseWheelListener(this);
        cellPane.setSearchOptions(searchOptions);

        scrollPane = new JScrollPane(cellPane);
        scrollPane.setPreferredSize(new Dimension(650, 300));
        add(scrollPane, BorderLayout.CENTER);
        
        setTransferHandler(new JlsTransferHandler());
        ActionMap map = this.getActionMap();
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
                TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
                TransferHandler.getPasteAction());


        updateMenuItems();
        if (null != fileToLoad)
        {
            fileName = fileToLoad;
            sendCommand(USRCMD_LOAD);
        }
        else
        {
            sendCommand(USRCMD_PREPROCESS);
        }
    }

    private Point mousePressedAt = null;
    private Point mousePosition = null;
    private boolean selecting = false;

    public void mousePressed(MouseEvent e)
    {
        mousePressedAt = cellPane.getCellAt(e.getX(), e.getY());
    }

    public void mouseReleased(MouseEvent e)
    {
        if (selecting)
        {
            selecting = false;
        }
        else
        {
            Point pt = cellPane.getCellAt(e.getX(), e.getY());
            // System.out.println("Mouse pressed: [" + mousePressedAt.x + "," + mousePressedAt.y + "] -> [" + pt.x + "," + pt.y + "]");
            if ((null != pt) && (null != mousePressedAt) && pt.equals(mousePressedAt))
            {
                Rectangle selection = cellPane.getSelection();
                if (null == selection)
                {
                    selection = new Rectangle(pt.x, pt.y, 1, 1);
                    // System.out.println("new selection");
                }

                if (selection.contains(pt))
                {
                    byte newState;
                    byte currState = Cell.getState(cellArray.getCellValue(pt.x, pt.y, genScroll.getValue()));
                    if (SwingUtilities.isRightMouseButton(e))
                    {
                        if (currState == Cell.STATE_OFF)
                        {
                            newState = Cell.CELL_EMPTY;
                        }
                        else
                        {
                            newState = Cell.CELL_OFF;
                        }
                    }
                    else if (SwingUtilities.isLeftMouseButton(e))
                    {
                        if (currState == Cell.STATE_ON)
                        {
                            newState = Cell.CELL_EMPTY;
                        }
                        else
                        {
                            newState = Cell.CELL_ON;
                        }
                    }
                    else
                    {
                        newState = currState;
                    }

                    cellArray.setCells(selection, e.isShiftDown() ? -1 : genScroll.getValue(), newState);
                }
                cellPane.setSelection(null);
                handleEdit();
            }
            else
            {
                if (null != cellPane.getSelection())
                {
                    cellPane.setSelection(null);
                }
            }
        }
        mousePressedAt = null;
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
        updateMousePos(-1, -1);
        mousePressedAt = null;
        mousePosition = null;
        if (selecting)
        {
            cellPane.setSelection(null);
            selecting = false;
        }
    }

    public void mouseDragged(MouseEvent e)
    {
        // TODO: Panning using right mouse button
        if (null != mousePressedAt)
        {
            Point pt = cellPane.getCellAtUnchecked(e.getX(), e.getY());
            updateMousePos(pt.x, pt.y);
            if (selecting || (!mousePressedAt.equals(pt)))
            {
                int x1 = mousePressedAt.x;
                int x2 = pt.x;
                int y1 = mousePressedAt.y;
                int y2 = pt.y;

                if (e.isShiftDown())
                {
                    int xd = x2 - x1;
                    int yd = y2 - y1;
                    if (xd > 0)
                    {
                        if (yd > 0)
                        {
                            if (xd > yd)
                            {
                                y2 = y1 + xd;
                            }
                            else
                            {
                                x2 = x1 + yd;
                            }
                        }
                        else
                        {
                            if (xd > -yd)
                            {
                                y2 = y1 - xd;
                            }
                            else
                            {
                                x2 = x1 - yd;
                            }
                        }
                    }
                    else
                    {
                        if (yd > 0)
                        {
                            if (-xd > yd)
                            {
                                y2 = y1 - xd;
                            }
                            else
                            {
                                x2 = x1 - yd;
                            }
                        }
                        else
                        {
                            if (xd < yd)
                            {
                                y2 = y1 + xd;
                            }
                            else
                            {
                                x2 = x1 + yd;
                            }
                        }
                    }
                }
                if (x1 > x2)
                {
                    int t = x1;
                    x1 = x2;
                    x2 = t;
                }
                if (y1 > y2)
                {
                    int t = y1;
                    y1 = y2;
                    y2 = t;
                }
                cellPane.setSelection(new Rectangle(x1, y1, x2 - x1 + 1, y2 - y1 + 1));
                selecting = true;
            }
        }
    }

    public void mouseMoved(MouseEvent e)
    {
        mousePosition = cellPane.getCellAt(e.getX(), e.getY());
        if (null != mousePosition)
        {
            updateMousePos(mousePosition.x, mousePosition.y);
        }
        else
        {
            updateMousePos(-1, -1);
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        genScroll.setValue(genScroll.getValue() + e.getWheelRotation());
    }

    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        if (e.getAdjustable() == genScroll)
        {
            int currGen = genScroll.getValue();
            genNoField.setText(Integer.toString(currGen));
            cellPane.setCurrGen(currGen);
        }
    }
    
    private void updateMousePos(int col, int row)
    {
        if ((col < 0) || (col >= properties.getColumns())
                || (row < 0) || (row >= properties.getRows()))
        {
            col = -1;
            row = -1;
        }
        if ((col != mousePosX) || (row != mousePosY))
        {
            mousePosX = col;
            mousePosY = row;
            if (col < 0)
            {
                mousePosField.setText("");
            }
            else
            {
                mousePosField.setText("column " + col + ", row " + row);
            }
        }
    }
    

    private Rectangle prepareSelection()
    {
        if (null != cellPane.getSelection())
        {
            Rectangle result = cellPane.getSelection();
            cellPane.setSelection(null);
            return result;
        }
        if (null != mousePosition)
        {
            return new Rectangle(mousePosition.x, mousePosition.y, 1, 1);
        }
        return null;
    }

    public void handleAction(Action a)
    {
        if (a == setOnAction)
        {
            cellArray.setCells(prepareSelection(), genScroll.getValue(), Cell.CELL_ON);
            handleEdit();
        }
        else if (a == setOnAllAction)
        {
            cellArray.setCells(prepareSelection(), -1, Cell.CELL_ON);
            handleEdit();
        }
        else if (a == setOffAction)
        {
            cellArray.setCells(prepareSelection(), genScroll.getValue(), Cell.CELL_OFF);
            handleEdit();
        }
        else if (a == setOffAllAction)
        {
            cellArray.setCells(prepareSelection(), -1, Cell.CELL_OFF);
            handleEdit();
        }
        else if (a == setEmptyAction)
        {
            cellArray.setCells(prepareSelection(), genScroll.getValue(), Cell.CELL_EMPTY);
            handleEdit();
        }
        else if (a == setEmptyAllAction)
        {
            cellArray.setCells(prepareSelection(), -1, Cell.CELL_EMPTY);
            handleEdit();
        }
        else if (a == setFrozenAction)
        {
            cellArray.setCells(prepareSelection(), genScroll.getValue(), Cell.CELL_FROZEN);
            handleEdit();
        }
        else if (a == setFrozenAllAction)
        {
            cellArray.setCells(prepareSelection(), -1, Cell.CELL_FROZEN);
            handleEdit();
        }
        else if (a == setUncheckedAction)
        {
            cellArray.setCells(prepareSelection(), genScroll.getValue(), Cell.CELL_UNCHECKED);
            handleEdit();
        }
        else if (a == setUncheckedAllAction)
        {
            cellArray.setCells(prepareSelection(), -1, Cell.CELL_UNCHECKED);
            handleEdit();
        }
        else if (a == setHardAction)
        {
            cellArray.setCells(prepareSelection(), genScroll.getValue(), Cell.CELL_UNSET);
            handleEdit();
        }
        else if (a == setHardAllAction)
        {
            cellArray.setCells(prepareSelection(), -1, Cell.CELL_UNSET);
            handleEdit();
        }
        else if (a == changeUncheckedAction)
        {
            cellArray.changeCells(prepareSelection(), genScroll.getValue(), false);
            handleEdit();
        }
        else if (a == changeUncheckedAllAction)
        {
            cellArray.changeCells(prepareSelection(), -1, false);
            handleEdit();
        }
        else if (a == changeEmptyAction)
        {
            cellArray.changeCells(prepareSelection(), genScroll.getValue(), true);
            handleEdit();
        }
        else if (a == changeEmptyAllAction)
        {
            cellArray.changeCells(prepareSelection(), -1, true);
            handleEdit();
        }
        else if (a == setPeriod0Action)
        {
            cellArray.setPeriod(prepareSelection(), 0);
            handleEdit();
        }
        else if (a == setPeriod1Action)
        {
            cellArray.setPeriod(prepareSelection(), 1);
            handleEdit();
        }
        else if (a == setPeriod2Action)
        {
            cellArray.setPeriod(prepareSelection(), 2);
            handleEdit();
        }
        else if (a == setPeriod3Action)
        {
            cellArray.setPeriod(prepareSelection(), 3);
            handleEdit();
        }
        else if (a == setPeriod4Action)
        {
            cellArray.setPeriod(prepareSelection(), 4);
            handleEdit();
        }
        else if (a == setPeriod5Action)
        {
            cellArray.setPeriod(prepareSelection(), 5);
            handleEdit();
        }
        else if (a == setPeriod6Action)
        {
            cellArray.setPeriod(prepareSelection(), 6);
            handleEdit();
        }
        else if (a == shiftDownAction)
        {
            cellArray.shiftDown(cellPane.getSelection(), genScroll.getValue());
            handleEdit();
        }
        else if (a == shiftDownAllAction)
        {
            cellArray.shiftDown(cellPane.getSelection(), -1);
            handleEdit();
        }
        else if (a == shiftUpAction)
        {
            cellArray.shiftUp(cellPane.getSelection(), genScroll.getValue());
            handleEdit();
        }
        else if (a == shiftUpAllAction)
        {
            cellArray.shiftUp(cellPane.getSelection(), -1);
            handleEdit();
        }
        else if (a == shiftLeftAction)
        {
            cellArray.shiftLeft(cellPane.getSelection(), genScroll.getValue());
            handleEdit();
        }
        else if (a == shiftLeftAllAction)
        {
            cellArray.shiftLeft(cellPane.getSelection(), -1);
            handleEdit();
        }
        else if (a == shiftRightAction)
        {
            cellArray.shiftRight(cellPane.getSelection(), genScroll.getValue());
            handleEdit();
        }
        else if (a == shiftRightAllAction)
        {
            cellArray.shiftRight(cellPane.getSelection(), -1);
            handleEdit();
        }
        else if (a == shiftFutureAction)
        {
            cellArray.shiftFuture(cellPane.getSelection());
            handleEdit();
        }
        else if (a == shiftPastAction)
        {
            cellArray.shiftPast(cellPane.getSelection());
            handleEdit();
        }
        else if (a == transposeAction)
        {
            cellArray.transpose(cellPane.getSelection(), genScroll.getValue());
            handleEdit();
        }
        else if (a == transposeAllAction)
        {
            cellArray.transpose(cellPane.getSelection(), -1);
            handleEdit();
        }
        else if (a == rotateRightAction)
        {
            cellArray.rotateRight(cellPane.getSelection(), genScroll.getValue());
            handleEdit();
        }
        else if (a == rotateRightAllAction)
        {
            cellArray.rotateRight(cellPane.getSelection(), -1);
            handleEdit();
        }
        else if (a == rotateLeftAction)
        {
            cellArray.rotateLeft(cellPane.getSelection(), genScroll.getValue());
            handleEdit();
        }
        else if (a == rotateLeftAllAction)
        {
            cellArray.rotateLeft(cellPane.getSelection(), -1);
            handleEdit();
        }
        else if (a == rotate180Action)
        {
            cellArray.rotate180(cellPane.getSelection(), genScroll.getValue());
            handleEdit();
        }
        else if (a == rotate180AllAction)
        {
            cellArray.rotate180(cellPane.getSelection(), -1);
            handleEdit();
        }
        else if (a == mirrorHorizontalAction)
        {
            cellArray.mirrorHorz(cellPane.getSelection(), genScroll.getValue());
            handleEdit();
        }
        else if (a == mirrorHorizontalAllAction)
        {
            cellArray.mirrorHorz(cellPane.getSelection(), -1);
            handleEdit();
        }
        else if (a == mirrorVerticalAction)
        {
            cellArray.mirrorVert(cellPane.getSelection(), genScroll.getValue());
            handleEdit();
        }
        else if (a == mirrorVerticalAllAction)
        {
            cellArray.mirrorVert(cellPane.getSelection(), -1);
            handleEdit();
        }
        else if (a == panUpAction)
        {
            JScrollBar sb = scrollPane.getVerticalScrollBar();
            sb.setValue(sb.getValue() - 50);
        }
        else if (a == panDownAction)
        {
            JScrollBar sb = scrollPane.getVerticalScrollBar();
            sb.setValue(sb.getValue() + 50);
        }
        else if (a == panLeftAction)
        {
            JScrollBar sb = scrollPane.getHorizontalScrollBar();
            sb.setValue(sb.getValue() - 50);
        }
        else if (a == panRightAction)
        {
            JScrollBar sb = scrollPane.getHorizontalScrollBar();
            sb.setValue(sb.getValue() + 50);
        }
    }
    
    public String getRleString()
    {
        CellArray source = cellArray;
        if (resultArray.getVersion() == cellArray.getVersion())
        {
            source = resultArray;
        }
        Rectangle rect = cellPane.getSelection();
        if (null == rect)
        {
            rect = new Rectangle(0, 0, cellArray.getCols(), cellArray.getRows());
        }
        // first shrink the rectangle
        int gen = genScroll.getValue();
        int row = rect.y;
  
        int minRow = rect.y;
        int maxRow = rect.y + rect.height - 1;;
        int minCol = rect.x;
        int maxCol = rect.x + rect.width - 1;

        int numNewLines = 0;
        int numOffCells = 0;
        int numOnCells = 0;
        String result = "x = " + (maxCol - minCol + 1) + ", y = " + (maxRow - minRow + 1) + "\n";
        int lineLen = 0;
        for (row = minRow; row <= maxRow; ++row)
        {
            for (int col = minCol; col <= maxCol; ++col)
            {
                if (Cell.getState(source.getCellValue(col, row, gen)) == Cell.STATE_ON)
                {
                    // ON state
                    if (numNewLines > 0)
                    {
                        String app = expandState('$', numNewLines);
                        if (lineLen + app.length() > 80)
                        {
                            result += "\n";
                            lineLen = 0;
                        }
                        result += app;
                        lineLen += app.length();
                        numNewLines = 0;
                    }

                    if (numOffCells > 0)
                    {
                        String app = expandState('b', numOffCells);
                        if (lineLen + app.length() > 80)
                        {
                            result += "\n";
                            lineLen = 0;
                        }
                        result += app;
                        lineLen += app.length();
                        numOffCells = 0;
                    }
                    ++numOnCells;
                }
                else
                {
                    // OFF state
                    if (numOnCells > 0)
                    {
                        String app = expandState('o', numOnCells);
                        if (lineLen + app.length() > 80)
                        {
                            result += "\n";
                            lineLen = 0;
                        }
                        result += app;
                        lineLen += app.length();
                        numOnCells = 0;
                    }
                    ++numOffCells;
                }
            }
            if (numOnCells > 0)
            {
                String app = expandState('o', numOnCells);
                if (lineLen + app.length() > 80)
                {
                    result += "\n";
                    lineLen = 0;
                }
                result += app;
                lineLen += app.length();
                numOnCells = 0;
            }
            numOffCells = 0;
            ++numNewLines;
        }
        if (lineLen + 1 > 80)
        {
            result += "\n";
        }
        result += "!\n";
        return result;
    }
    
    private String expandState(char state, int count)
    {
        switch (count)
        {
        case 0:
            return "";
        case 1:
            return "" + state;
        case 2:
            return "" + state + state;
        default:
            return "" + count + state;
        }
    }
    
    public void pasteString(String str)
    {
        if (pasteStringEx(str))
        {
            handleEdit();
        }
    }
    public boolean pasteStringEx(String str)
    {
        // no pasting if not editing
        if (userState != USERSTATE_EDIT)
        {
            return false;
        }
        Rectangle rect = cellPane.getSelection();
        if (null == rect)
        {
            rect = new Rectangle(0, 0, cellArray.getCols(), cellArray.getRows());
        }
        return cellArray.pasteString(rect, genScroll.getValue(), str);
    }

    
    public void actionPerformed(ActionEvent e)
    {
        Object source =  e.getSource();
        if (source == openFileMenuItem)
        {
            if (userState == USERSTATE_EDIT)
            {
                if (JFileChooser.APPROVE_OPTION == stateFileChooser.showOpenDialog(this))
                {
                    fileName = stateFileChooser.getSelectedFile().getAbsolutePath();
                    sendCommand(USRCMD_LOAD);
                }
            }
        }
        else if (source == saveFileMenuItem)
        {
            if (JFileChooser.APPROVE_OPTION == stateFileChooser.showSaveDialog(this))
            {
                fileName = stateFileChooser.getSelectedFile().getAbsolutePath();
                sendCommand(USRCMD_SAVE);
            }
        }
        else if (source == exitMenuItem)
        {
            System.exit(0);
        }
        else if (source == undoMenuItem)
        {
            cellArray.undo();
            handleEdit();
        }
        else if (source == redoMenuItem)
        {
            cellArray.redo();
            handleEdit();
        }
        else if (source == copyMenuItem)
        {
            String action = (String) e.getActionCommand();
            Action a = getActionMap().get(action);
            if (a != null)
            {
                a.actionPerformed(new ActionEvent(this,
                        ActionEvent.ACTION_PERFORMED, null));
            }
        }
        else if (source == pasteMenuItem)
        {
            String action = (String) e.getActionCommand();
            Action a = getActionMap().get(action);
            if (a != null)
            {
                a.actionPerformed(new ActionEvent(this,
                        ActionEvent.ACTION_PERFORMED, null));
            }
        }
        else if (source == selAllMenuItem)
        {
            cellPane.setSelection(new Rectangle(0, 0, cellArray.getCols(), cellArray.getRows()));
        }
        else if (source == selNoneMenuItem)
        {
            cellPane.setSelection(null);
        }
        else if (source == nextGenMenuItem)
        {
            genScroll.setValue(genScroll.getValue() + 1);
        }
        else if (source == prevGenMenuItem)
        {
            genScroll.setValue(genScroll.getValue() - 1);
        }
        else if (source == firstGenMenuItem)
        {
            if (genScroll.getValue() > 0)
            {
                savedGen = genScroll.getValue();
                genScroll.setValue(0);
            }
            else
            {
                genScroll.setValue(savedGen);
            }
        }
        else if (source == lastGenMenuItem)
        {
            if (genScroll.getValue() < genScroll.getMaximum() - 1)
            {
                savedGen = genScroll.getValue();
                genScroll.setValue(genScroll.getMaximum() - 1);
            }
            else
            {
                genScroll.setValue(savedGen);
            }
        }
        else if (source == incSizeMenuItem)
        {
            cellPane.incCellSize();
        }
        else if (source == decSizeMenuItem)
        {
            cellPane.decCellSize();
        }
        else if (source == propertiesMenuItem)
        {
            Properties result = new Properties();
            PropertiesDialog dialog = new PropertiesDialog(mainFrame, properties, !cellArray.isReadOnly(), result);
            dialog.pack();
            dialog.setLocationRelativeTo(mainFrame);
            dialog.setVisible(true);
            if (result.isValidated())
            {
                properties = result;
                cellArray.reconfigure(properties);
                cellPane.reconfigure(properties);
                genScroll.setMaximum(properties.getGenerations());
                if (genScroll.getValue() > properties.getGenerations())
                {
                    genScroll.setValue(properties.getGenerations());
                }
                handleEdit();
            }
        }
        else if (source == searchOptionsMenuItem)
        {
            SearchOptions result = new SearchOptions();
            SearchOptionsDialog dlg = new SearchOptionsDialog(mainFrame,
                    searchOptions, stateFileChooser, resultFileChooser,
                    userState != USERSTATE_EDIT, result);
            dlg.pack();
            dlg.setLocationRelativeTo(mainFrame);
            dlg.setVisible(true);
            if (result.isValidated())
            {
                searchOptions = result;
                sendCommand(USRCMD_OPTIONS);
                cellPane.setSearchOptions(searchOptions);
                handleEdit();
            }
        }
        else if (source == searchPrepareMenuItem)
        {
            sendCommand(USRCMD_PREPROCESS);
        }
        else if (source == searchStartMenuItem)
        {
            sendCommand(USRCMD_SEARCH);
        }
        else if (source == searchPauseMenuItem)
        {
            sendCommand(USRCMD_PAUSE);
        }
        else if (source == searchContinueMenuItem)
        {
            sendCommand(USRCMD_CONTINUE);
        }
        else if (source == searchResetMenuItem)
        {
            sendCommand(USRCMD_RESET);
        }
        else if (source == searchAcceptStateMenuItem)
        {
            sendCommand(USRCMD_ACCEPT_STATE);
        }
        else if (source == searchShowCombinationMenuItem) 
        {
            sendCommand(USRCMD_SHOW_COMBINATION);
        }
        else if (source == searchShowResultMenuItem) 
        {
            sendCommand(USRCMD_SHOW_RESULT);
        }
        else if (source == controlsReferenceMenuItem)
        {
            ControlsReference keyRef = new ControlsReference(this);
            keyRef.pack();
            keyRef.setVisible(true);
        }
    }

    private void handleEdit()
    {
        if (!cellArray.isReadOnly())
        {
            // cellArray contents has changed
            cellArray.incrementVersion();
            cellPane.setDisplayMode(CellPane.DISPLAY_NORMAL); // -> repaint
            searchThreadMessage.setText("");
            // don't send more than one interrupt in a row
            if (searchOptions.isPrepareInBackground())
            {
                sendCommand(USRCMD_PREPROCESS);
            }
            updateMenuItems();
        }
    }

    private void handleLoadStatus()
    {
        if (!cellArray.isReadOnly())
        {
            // cellArray contents has changed
            cellArray.incrementVersion();
            cellPane.setDisplayMode(CellPane.DISPLAY_NORMAL); // -> repaint
            searchThreadMessage.setText("");
        }
        updateMenuItems();
    }

    // update enabled/disabled menu items depending on current state
    public void updateMenuItems()
    {
        cellArray.setReadOnly(userState != USERSTATE_EDIT);
        openFileMenuItem.setEnabled(userState == USERSTATE_EDIT);
        undoMenuItem.setEnabled((userState == USERSTATE_EDIT) && (cellArray.isUndoAvailable()));
        redoMenuItem.setEnabled((userState == USERSTATE_EDIT) && (cellArray.isRedoAvailable()));
        pasteMenuItem.setEnabled(userState == USERSTATE_EDIT);
        searchPrepareMenuItem.setEnabled(userState == USERSTATE_EDIT);
        searchStartMenuItem.setEnabled(userState == USERSTATE_EDIT);
        searchShowCombinationMenuItem.setEnabled(userState == USERSTATE_PAUSE);
        searchShowResultMenuItem.setEnabled(userState == USERSTATE_PAUSE);
        searchAcceptStateMenuItem.setEnabled(userState != USERSTATE_SEARCH);
        searchContinueMenuItem.setEnabled(userState == USERSTATE_PAUSE);
        searchPauseMenuItem.setEnabled(userState == USERSTATE_SEARCH);
        searchResetMenuItem.setEnabled(userState != USERSTATE_EDIT);
    }
    
    private void loadStatus(String filename, SearchCellArray searchCellArray)
    {
        FileReader fin = null;
        LineNumberReader reader = null;
        Properties newProps = new Properties();
        SearchOptions newOpts = new SearchOptions();
        boolean error = false;
        try 
        {
            try 
            {
                fin = new FileReader(filename);
                reader = new LineNumberReader(fin);
                StatusFileHandler sfh = new StatusFileHandler();
                sfh.readStatus(newProps, newOpts, cellArray, searchCellArray, reader);
            }
            finally
            {
                if (null != reader)
                {
                    reader.close();
                }
                if (null != fin)
                {
                    fin.close();
                }
            }
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, e, "Error reading status file", JOptionPane.ERROR_MESSAGE);
            error = true;
        }
        if (newProps.isValidated() && newOpts.isValidated())
        {
            properties = newProps;
            searchOptions = newOpts;
            cellPane.reconfigure(properties);
            cellPane.setSearchOptions(newOpts);
            genScroll.setMaximum(properties.getGenerations());
            if (genScroll.getValue() > properties.getGenerations())
            {
                genScroll.setValue(properties.getGenerations());
            }
        }
        if (error || !cellArray.isReadOnly())
        {
            cellArray.setReadOnly(false);
            userState = USERSTATE_EDIT;
            handleLoadStatus();
            searchThreadMessage.setText("Pattern loaded");
        }
        else
        {
            // try to recover to search state
            resultArray.load(searchCellArray);
            cellPane.setDisplayMode(CellPane.DISPLAY_RESULT); // -> repaint

            userState = USERSTATE_PAUSE;
            updateMenuItems();
            searchThreadMessage.setText("Paused");
        }
    }

    private void saveStatus(String filename, SearchCellArray searchCellArray)
    {
        FileWriter fout = null;
        BufferedWriter writer = null;
        try 
        {
            try 
            {
                fout = new FileWriter(filename);
                writer = new BufferedWriter(fout);
                StatusFileHandler sfh = new StatusFileHandler();
                sfh.saveStatus(properties, searchOptions, cellArray, searchCellArray, writer);
            }
            finally
            {
                if (null != writer)
                {
                    writer.close();
                }
                if (null != fout)
                {
                    fout.close();
                }
            }
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, e, "Error writing status file", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    // send a command to the background thread
    // behave accordingly to the current state, update the state
    private void sendCommand(int command)
    {
        // first update user status accordingly
        //System.out.println("sendCommand: " + USRCMD_NAMES[command]);
        switch (command)
        {
        case USRCMD_PREPROCESS:
            if (userState != USERSTATE_EDIT)
            {
                return;
            }
            break;
        case USRCMD_SEARCH:
            if (userState != USERSTATE_EDIT)
            {
                return;
            }
            userState = USERSTATE_SEARCH;
            break;
        case USRCMD_PAUSE:
            if (userState != USERSTATE_SEARCH)
            {
                return;
            }
            userState = USERSTATE_PAUSE;
            break;
        case USRCMD_CONTINUE:
            if (userState != USERSTATE_PAUSE)
            {
                return;
            }
            userState = USERSTATE_SEARCH;
            break;
        case USRCMD_ACCEPT_STATE:
            userState = USERSTATE_EDIT;
            break;
        case USRCMD_RESET:
            if (userState == USERSTATE_EDIT)
            {
                return;
            }
            userState = USERSTATE_EDIT;
            break;
        case USRCMD_SHOW_COMBINATION:
        case USRCMD_SHOW_RESULT:
        case USRCMD_SAVE:
        case USRCMD_LOAD:
        case USRCMD_OPTIONS:
            break;
        }
        
        updateMenuItems();
        // now that we've taken care of GUI state
        // look if we can actually do what we promised
        
        commandList.add(command);
        
        switch (threadState)
        {
        case THREADSTATE_RUNNING:
        case THREADSTATE_PAUSED:
            threadState = THREADSTATE_PAUSING;
            //System.out.println("sending CMD_STOP");
            //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
            searchThread.sendCommand(new GuiCommand(GuiCommand.CMD_STOP));
            break;
        case THREADSTATE_STOPPED:
            threadState = THREADSTATE_STOPPING;
            //System.out.println("sending CMD_STOP");
            //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
            searchThread.sendCommand(new GuiCommand(GuiCommand.CMD_STOP));
            break;
        }
    }

    // this is called after Thread reported that it's stopped
    private void processCommands(SearchCellArray array)
    {
        boolean arraySet = false;
        while (true)
        {
            // threadState is set to PAUSED or STOPPED at this point
            // we may change STOPPED to PAUSED if we want it to start again after processing ends
            if (commandList.isEmpty())
            {
                //System.out.println("processCommands END");
                return;
            }
            int command = commandList.remove(0);
            //System.out.println("processCommands: " + USRCMD_NAMES[command]);
            // see if we need to process the command in more stages
            switch (command)
            {
            case USRCMD_PREPROCESS:
                if (!arraySet)
                {
                    array.setArray(cellArray);
                    arraySet = true;
                }
                array.setEditMode();
                threadState = THREADSTATE_PAUSED;
                //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                break;
            case USRCMD_SEARCH:
                if (!arraySet)
                {
                    array.setArray(cellArray);
                    arraySet = true;
                }
                array.setSearchMode();
                threadState = THREADSTATE_PAUSED;
                //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                break;
            case USRCMD_PAUSE:
                array.publishVariables();
                resultArray.load(array);
                cellPane.setDisplayMode(CellPane.DISPLAY_RESULT); // -> repaint
                // prevent restart
                threadState = THREADSTATE_STOPPED;
                //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                break;
            case USRCMD_CONTINUE:
                // enforce restart
                threadState = THREADSTATE_PAUSED;
                //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                break;
            case USRCMD_ACCEPT_STATE:
                if (resultArray.getVersion() == cellArray.getVersion())
                {
                    cellArray.acceptResult(resultArray);
                    array.setArray(cellArray);
                    arraySet = true;
                    cellPane.setDisplayMode(CellPane.DISPLAY_NORMAL); // -> repaint
                    searchThreadMessage.setText("");
                    array.setEditMode();
                    if (searchOptions.isPrepareInBackground())
                    {
                        // enforce restart
                        threadState = THREADSTATE_PAUSED;
                        //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                    }
                }
                break;
            case USRCMD_SHOW_COMBINATION:
                if (resultArray.getVersion() == cellArray.getVersion())
                {
                    array.publishCombination();
                    resultArray.load(array);
                    cellPane.setDisplayMode(CellPane.DISPLAY_RESULT); // -> repaint
                }
                break;
            case USRCMD_SHOW_RESULT:
                if (resultArray.getVersion() == cellArray.getVersion())
                {
                    array.publishVariables();
                    resultArray.load(array);
                    cellPane.setDisplayMode(CellPane.DISPLAY_RESULT); // -> repaint
                }
                break;
            case USRCMD_RESET:
                array.setArray(cellArray);
                arraySet = true;
                cellPane.setDisplayMode(CellPane.DISPLAY_NORMAL); // -> repaint
                searchThreadMessage.setText("");
                array.setEditMode();
                if (searchOptions.isPrepareInBackground())
                {
                    // enforce restart
                    threadState = THREADSTATE_PAUSED;
                    //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                }
                break;
            case USRCMD_SAVE:
                saveStatus(fileName, array);
                break;
            case USRCMD_LOAD:
                loadStatus(fileName, array);
                if (!array.isSearchMode())
                {
                    // make the field silently preprocess
                    commandList.add(USRCMD_PREPROCESS);
                }
                break;
            case USRCMD_OPTIONS:
                array.setOptions(searchOptions);
                break;
            }
        }
    }
    
    public void finishProcessingSearchThreadMessages()
    {
        //System.out.println("finishProcessingSearchThreadMessages() ");
        if (threadState == THREADSTATE_PAUSED)
        {
            //System.out.println(Thread.currentThread().getName() + " finishProcessingSearchThreadMessages(): resuming from paused ");
            searchThread.sendCommand(new GuiCommand(GuiCommand.CMD_CONTINUE));
            threadState = THREADSTATE_RUNNING;
            //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
        }
    }
    
    public void processSearchThreadMessage(ThreadMessage message)
    {
        //System.out.println("processSearchThreadMessage: " + message.getMessageName());
        
        switch(message.getMessage())
        {
        case ThreadMessage.MSG_STRING:
            searchThreadMessage.setText((String) message.getData());
            break;
        case ThreadMessage.MSG_DIALOG:
            JOptionPane.showMessageDialog(mainFrame, (String) message.getData());
            break;
        case ThreadMessage.MSG_ENGINE:
            // mainFrame.setTitle(mainFrame.getTitle() + " (" + (String) message.getData() + ")");
            break;
        case ThreadMessage.MSG_SAVE:
            // !!! this may not be running when search is paused!!!
            saveStatus(searchOptions.getSaveStatusFile(), (SearchCellArray) message.getData());
            switch (threadState)
            {
            case THREADSTATE_RUNNING:
                threadState = THREADSTATE_PAUSED;
                //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                break;
            case THREADSTATE_PAUSING:
                break;
            case THREADSTATE_STOPPED:
            case THREADSTATE_STOPPING:
            case THREADSTATE_PAUSED:
                throw new RuntimeException("MSG_SAVE in incorrect threadState " + threadState + " (" + THREADSTATE_NAMES[threadState] + ")");
            }
            break;
        case ThreadMessage.MSG_DISPLAY:
            resultArray.load((SearchCellArray) message.getData());
            cellPane.setDisplayMode(CellPane.DISPLAY_RESULT);
            switch (threadState)
            {
            case THREADSTATE_RUNNING:
                threadState = THREADSTATE_PAUSED;
                //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                break;
            case THREADSTATE_PAUSING:
                break;
            case THREADSTATE_STOPPED:
            case THREADSTATE_STOPPING:
            case THREADSTATE_PAUSED:
                throw new RuntimeException("MSG_DISPLAY in incorrect threadState " + threadState + " (" + THREADSTATE_NAMES[threadState] + ")");
            }
            break;
        case ThreadMessage.MSG_DONE:
            resultArray.load((SearchCellArray) message.getData());
            cellPane.setDisplayMode(CellPane.DISPLAY_RESULT);
            if (userState == USERSTATE_SEARCH)
            {
                userState = USERSTATE_PAUSE;
                updateMenuItems();
            }
            switch (threadState)
            {
            case THREADSTATE_RUNNING:
                threadState = THREADSTATE_STOPPED;
                //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                break;
            case THREADSTATE_PAUSING:
                threadState = THREADSTATE_STOPPING;
                //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                break;
            case THREADSTATE_STOPPING:
            case THREADSTATE_STOPPED:
            case THREADSTATE_PAUSED:
                throw new RuntimeException("MSG_DONE in incorrect threadState " + threadState + " (" + THREADSTATE_NAMES[threadState] + ")");
            }
            break;
        case ThreadMessage.MSG_ERROR:
            resultArray.load((SearchCellArray) message.getData());
            cellPane.setDisplayMode(CellPane.DISPLAY_RESULT);
            if (userState != USERSTATE_EDIT)
            {
                userState = USERSTATE_EDIT;
                updateMenuItems();
                ((SearchCellArray) message.getData()).setEditMode();
            }
            switch (threadState)
            {
            case THREADSTATE_RUNNING:
                threadState = THREADSTATE_STOPPED;
                //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                break;
            case THREADSTATE_PAUSING:
                threadState = THREADSTATE_STOPPING;
                //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                break;
            case THREADSTATE_STOPPING:
            case THREADSTATE_STOPPED:
            case THREADSTATE_PAUSED:
                throw new RuntimeException("MSG_ERROR in incorrect threadState " + threadState + " (" + THREADSTATE_NAMES[threadState] + ")");
            }
            break;
        case ThreadMessage.MSG_STOPPED:
            switch (threadState)
            {
            case THREADSTATE_PAUSING:
                threadState = THREADSTATE_PAUSED;
                //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                break;
            case THREADSTATE_STOPPING:
                threadState = THREADSTATE_STOPPED;
                //System.out.println("threadState set to " + THREADSTATE_NAMES[threadState]);
                break;
            case THREADSTATE_RUNNING:
            case THREADSTATE_STOPPED:
            case THREADSTATE_PAUSED:
                throw new RuntimeException("MSG_STOPPED in incorrect threadState " + threadState + " (" + THREADSTATE_NAMES[threadState] + ")");
            }
            SearchCellArray array = (SearchCellArray) message.getData(); 
            // now process buffered commands
            processCommands(array);
            break;
        }
    }
}
