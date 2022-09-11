package jls;

import javax.swing.JPanel;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JTabbedPane;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Font;
import java.awt.Color;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JSeparator;

public class SearchOptionsDialog extends JDialog implements ActionListener, ChangeListener
{

    private static final long serialVersionUID = 1L;
    
    private boolean searchMode = false;
    
    private JFileChooser saveFileChooser = null;
    
    private JFileChooser solutionFileChooser = null;
    
    private SearchOptions result = null;

    private JPanel jContentPane = null;

    private JPanel buttonsPanel = null;

    private JPanel jPanel = null;

    private JButton okButton = null;

    private JButton cancelButton = null;

    private JTabbedPane jTabbedPane = null;

    private JPanel sortingTabPanel = null;

    private JPanel processingTabPanel = null;

    private JPanel overallPanel = null;

    private JRadioButton sortAreaFirst = null;

    private JRadioButton sortGensFirst = null;

    private JPanel generationsPanel = null;

    private JRadioButton sortToPast = null;

    private JRadioButton sortToFuture = null;

    private JPanel areaPanel = null;

    private JPanel areaUpperPanel = null;

    private JPanel areaLowerPanel = null;

    private JLabel jLabel = null;

    private JSpinner sortStartColumn = null;

    private JLabel jLabel1 = null;

    private JSpinner sortStartRow = null;

    private JLabel jLabel2 = null;

    private JRadioButton sortHorizontal = null;

    private JRadioButton sortDiagonal = null;

    private JRadioButton sortBox = null;

    private JRadioButton sortCircle = null;

    private JRadioButton sortVertical = null;

    private JRadioButton sortDiagonalBack = null;

    private JRadioButton sortDiamond = null;

    private JCheckBox sortReverse;
    
    private JCheckBox runInBackground = null;

    private JCheckBox stopEachIter = null;

    private JCheckBox stopEachSolution = null;

    private JCheckBox saveSolutionsToFile = null;

    private JTextField solutionFileName = null;

    private JCheckBox saveStatus = null;

    private JCheckBox useCombination = null;
    
    private JCheckBox ignoreSubperiods = null;

    private JSpinner screenUpdatePeriod = null;

    private JButton browseResultFile = null;

    private JSpinner statusSavePeriod = null;

    private JTextField statusFileName = null;

    private JButton browseStatusFile = null;

    private JCheckBox displaySearchStatus = null;

    private JPanel saveResultPanel = null;

    private JCheckBox saveResultsAllGen = null;

    private JSpinner cellsBetweenPatterns = null;

    private JPanel jPanel3 = null;

    private JLabel jLabel3 = null;

    private JLabel jLabel5 = null;

    private JPanel saveStatusPanel = null;

    private JPanel jPanel4 = null;

    private JLabel jLabel6 = null;

    private JPanel jPanel5 = null;

    private JLabel jLabel7 = null;

    private JPanel constraintPanel = null;

    private JCheckBox limitLiveCells = null;

    private JPanel layerConstraintPanel = null;

    private JPanel layerUpperPanel = null;

    private JLabel jLabel8 = null;

    private JSpinner layerStartColumn = null;

    private JLabel jLabel11 = null;

    private JSpinner layerStartRow = null;

    private JPanel layerLowerPanel = null;

    private JRadioButton layerCol = null;

    private JRadioButton layerRow = null;

    private JRadioButton layerBox = null;

    private JRadioButton layerCircle = null;

    private JRadioButton layerDiag = null;

    private JRadioButton layerDiagBack = null;

    private JRadioButton layerDiamond = null;

    private JPanel jPanel6 = null;

    private JCheckBox layerUseSort = null;

    private JSeparator jSeparator4 = null;

    private JPanel gen0ConstraintPanel = null;

    private JPanel jPanel9 = null;

    private JPanel jPanel10 = null;

    private JSpinner maxLiveCells = null;

    private JCheckBox limitLiveCellsVarsOnly = null;

    private JLabel jLabel10 = null;

    private JSpinner maxActiveCells = null;

    private JCheckBox limitActiveCellsVarsOnly = null;

    private JLabel jLabel13 = null;

    private JSeparator jSeparator51 = null;

    private JCheckBox limitActiveCells = null;

    private JPanel jPanel7 = null;

    private JCheckBox limitGenZero = null;
    
    private JSpinner limitGenZeroCells = null;

    private JCheckBox limitGenZeroVarsOnly = null;

    private JLabel jLabel9 = null;

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == browseResultFile)
        {
            if (JFileChooser.APPROVE_OPTION == solutionFileChooser.showSaveDialog(this))
            {
                solutionFileName.setText(solutionFileChooser.getSelectedFile().getAbsolutePath());
            }
        }
        else if (e.getSource() == browseStatusFile)
        {
            if (JFileChooser.APPROVE_OPTION == saveFileChooser.showSaveDialog(this))
            {
                statusFileName.setText(saveFileChooser.getSelectedFile().getAbsolutePath());
            }
            // TODO: overwrite question
        }
        else if (e.getSource() == okButton)
        {
            result.setSortToFuture(sortToFuture.isSelected());
            result.setSortGenFirst(sortGensFirst.isSelected());
            result.setSortStartColumn((Integer)sortStartColumn.getValue());
            result.setSortStartRow((Integer)sortStartRow.getValue());
            if (sortHorizontal.isSelected())
            {
                result.setSortType(SearchOptions.SORT_HORZ);
            }
            else if (sortVertical.isSelected())
            {
                result.setSortType(SearchOptions.SORT_VERT);
            }
            else if (sortBox.isSelected())
            {
                result.setSortType(SearchOptions.SORT_BOX);
            }
            else if (sortCircle.isSelected())
            {
                result.setSortType(SearchOptions.SORT_CIRCLE);
            }
            else if (sortDiagonal.isSelected())
            {
                result.setSortType(SearchOptions.SORT_DIAG_FWD);
            }
            else if (sortDiagonalBack.isSelected())
            {
                result.setSortType(SearchOptions.SORT_DIAG_BWD);
            }
            else // if (sortDiamond.isSelected())
            {
                result.setSortType(SearchOptions.SORT_DIAMOND);
            }
            result.setSortReverse(sortReverse.isSelected());
            result.setPauseEachIteration(stopEachIter.isSelected());
            result.setPauseEachSolution(stopEachSolution.isSelected());
            result.setSaveSolutions(saveSolutionsToFile.isSelected());
            result.setSaveStatus(saveStatus.isSelected());
            result.setPruneWithCombination(useCombination.isSelected());
            result.setPrepareInBackground(runInBackground.isSelected());
            result.setIgnoreSubperiods(ignoreSubperiods.isSelected());
            result.setDisplayStatus(displaySearchStatus.isSelected());
            result.setSolutionFile(solutionFileName.getText());
            result.setSolutionSpacing((Integer)cellsBetweenPatterns.getValue());
            result.setSaveAllGen(saveResultsAllGen.isSelected());
            result.setSaveStatusFile(statusFileName.getText());
            result.setSaveStatusPeriod((Integer)statusSavePeriod.getValue());
            result.setDisplayStatusPeriod((Integer)screenUpdatePeriod.getValue());
            result.setLimitGenZero(limitGenZero.isSelected());
            result.setLimitGenZeroCells((Integer)limitGenZeroCells.getValue());
            result.setLimitGenZeroVarsOnly(limitGenZeroVarsOnly.isSelected());
            result.setLayersLiveCellConstraint(limitLiveCells.isSelected());
            result.setLayersLiveCells((Integer)maxLiveCells.getValue());
            result.setLayersLiveCellsVarsOnly(limitLiveCellsVarsOnly.isSelected());
            result.setLayersActiveCellConstraint(limitActiveCells.isSelected());
            result.setLayersActiveCells((Integer)maxActiveCells.getValue());
            result.setLayersActiveCellsVarsOnly(limitActiveCellsVarsOnly.isSelected());
            result.setLayersFromSorting(layerUseSort.isSelected());
            result.setLayersStartColumn((Integer)layerStartColumn.getValue());
            result.setLayersStartRow((Integer)layerStartRow.getValue());
            if (layerBox.isSelected())
            {
                result.setLayersType(SearchOptions.LAYER_BOX);
            }
            else if (layerCol.isSelected())
            {
                result.setLayersType(SearchOptions.LAYER_COLUMN);
            }
            else if (layerDiag.isSelected())
            {
                result.setLayersType(SearchOptions.LAYER_DIAG_BWD);
            }
            else if (layerDiagBack.isSelected())
            {
                result.setLayersType(SearchOptions.LAYER_DIAG_FWD);
            }
            else if (layerDiamond.isSelected())
            {
                result.setLayersType(SearchOptions.LAYER_DIAMOND);
            }
            else if (layerRow.isSelected())
            {
                result.setLayersType(SearchOptions.LAYER_ROW);
            }
            else // layerCircle.isSelected()
            {
                result.setLayersType(SearchOptions.LAYER_CIRCLE);
            }

            String error = result.validate();
            if (null != error)
            {
                JOptionPane.showMessageDialog(this,
                        error,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                setVisible(false);
            }
        }
        else if (e.getSource() == cancelButton)
        {
            setVisible(false);
        }
    }

    /**
     * @param owner
     */
    public SearchOptionsDialog(Frame owner, SearchOptions origOpts,
            JFileChooser saveFileChooser, JFileChooser resultFileChooser,
            boolean searchMode, SearchOptions result)
    {
        super(owner);
        this.searchMode = searchMode;
        this.saveFileChooser = saveFileChooser;
        this.solutionFileChooser = resultFileChooser;
        this.result = result;
        initialize();
        sortToFuture.setSelected(origOpts.isSortToFuture());
        sortToPast.setSelected(!origOpts.isSortToFuture());
        sortAreaFirst.setSelected(!origOpts.isSortGenFirst());
        sortGensFirst.setSelected(origOpts.isSortGenFirst());
        sortStartColumn.setValue(origOpts.getSortStartColumn());
        sortStartRow.setValue(origOpts.getSortStartRow());
        sortHorizontal.setSelected(origOpts.getSortType() == SearchOptions.SORT_HORZ);
        sortVertical.setSelected(origOpts.getSortType() == SearchOptions.SORT_VERT);
        sortBox.setSelected(origOpts.getSortType() == SearchOptions.SORT_BOX);
        sortCircle.setSelected(origOpts.getSortType() == SearchOptions.SORT_CIRCLE);
        sortDiagonal.setSelected(origOpts.getSortType() == SearchOptions.SORT_DIAG_FWD);
        sortDiagonalBack.setSelected(origOpts.getSortType() == SearchOptions.SORT_DIAG_BWD);
        sortDiamond.setSelected(origOpts.getSortType() == SearchOptions.SORT_DIAMOND);
        sortReverse.setSelected(origOpts.isSortReverse());
        stopEachIter.setSelected(origOpts.isPauseEachIteration());
        stopEachSolution.setSelected(origOpts.isPauseEachSolution());
        saveSolutionsToFile.setSelected(origOpts.isSaveSolutions());
        saveStatus.setSelected(origOpts.isSaveStatus());
        useCombination.setSelected(origOpts.isPruneWithCombination());
        runInBackground.setSelected(origOpts.isPrepareInBackground());
        ignoreSubperiods.setSelected(origOpts.isIgnoreSubperiods());
        displaySearchStatus.setSelected(origOpts.isDisplayStatus());
        solutionFileName.setText(origOpts.getSolutionFile());
        cellsBetweenPatterns.setValue(origOpts.getSolutionSpacing());
        saveResultsAllGen.setSelected(origOpts.isSaveAllGen());
        statusFileName.setText(origOpts.getSaveStatusFile());
        statusSavePeriod.setValue(origOpts.getSaveStatusPeriod());
        screenUpdatePeriod.setValue(origOpts.getDisplayStatusPeriod());
        limitGenZero.setSelected(origOpts.isLimitGenZero());
        limitGenZeroCells.setValue(origOpts.getLimitGenZeroCells());
        limitGenZeroVarsOnly.setSelected(origOpts.isLimitGenZeroVarsOnly());
        limitLiveCells.setSelected(origOpts.isLayersLiveCellConstraint());
        maxLiveCells.setValue(origOpts.getLayersLiveCells());
        limitLiveCellsVarsOnly.setSelected(origOpts.isLayersLiveCellsVarsOnly());
        limitActiveCells.setSelected(origOpts.isLayersActiveCellConstraint());
        maxActiveCells.setValue(origOpts.getLayersActiveCells());
        limitActiveCellsVarsOnly.setSelected(origOpts.isLayersActiveCellsVarsOnly());
        layerUseSort.setSelected(origOpts.isLayersFromSorting());
        layerStartColumn.setValue(origOpts.getLayersStartColumn());
        layerStartRow.setValue(origOpts.getLayersStartRow());
        layerBox.setSelected(origOpts.getLayersType() == SearchOptions.LAYER_BOX);
        layerCircle.setSelected(origOpts.getLayersType() == SearchOptions.LAYER_CIRCLE);
        layerCol.setSelected(origOpts.getLayersType() == SearchOptions.LAYER_COLUMN);
        layerDiag.setSelected(origOpts.getLayersType() == SearchOptions.LAYER_DIAG_BWD);
        layerDiagBack.setSelected(origOpts.getLayersType() == SearchOptions.LAYER_DIAG_FWD);
        layerDiamond.setSelected(origOpts.getLayersType() == SearchOptions.LAYER_DIAMOND);
        layerRow.setSelected(origOpts.getLayersType() == SearchOptions.LAYER_ROW);
        
        ButtonGroup group = new ButtonGroup();
        group.add(sortHorizontal);
        group.add(sortVertical);
        group.add(sortBox);
        group.add(sortCircle);
        group.add(sortDiagonal);
        group.add(sortDiagonalBack);
        group.add(sortDiamond);
        
        group = new ButtonGroup();
        group.add(sortAreaFirst);
        group.add(sortGensFirst);
        
        group = new ButtonGroup();
        group.add(sortToFuture);
        group.add(sortToPast);
        
        group = new ButtonGroup();
        group.add(layerBox);
        group.add(layerCircle);
        group.add(layerCol);
        group.add(layerDiag);
        group.add(layerDiagBack);
        group.add(layerDiamond);
        group.add(layerRow);
        
        limitLiveCells.setEnabled(!searchMode);
        maxLiveCells.setEnabled(!searchMode);
        limitLiveCellsVarsOnly.setEnabled(!searchMode);
        limitActiveCells.setEnabled(!searchMode);
        maxActiveCells.setEnabled(!searchMode);
        limitActiveCellsVarsOnly.setEnabled(!searchMode);
        layerUseSort.setEnabled(!searchMode);
        limitGenZero.setEnabled(!searchMode);
        limitGenZeroCells.setEnabled(!searchMode);
        limitGenZeroVarsOnly.setEnabled(!searchMode);
        syncLayers();

        setResizable(false);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        JRootPane rootPane = getRootPane();
        Action escapeAction = new AbstractAction() 
        {
            private static final long serialVersionUID = 1;
            
            public void actionPerformed(ActionEvent actionEvent) 
            {
                setVisible(false);
            }
        };
        rootPane.setDefaultButton(okButton);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "ESCAPE");
        rootPane.getActionMap().put("ESCAPE", escapeAction);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize()
    {
        this.setTitle("Search Options");
        this.setContentPane(getJContentPane());
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
            jContentPane.add(getButtonsPanel(), BorderLayout.SOUTH);
            jContentPane.add(getJTabbedPane(), BorderLayout.NORTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes buttonsPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getButtonsPanel()
    {
        if (buttonsPanel == null)
        {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(FlowLayout.RIGHT);
            buttonsPanel = new JPanel();
            buttonsPanel.setLayout(flowLayout);
            buttonsPanel.add(getJPanel(), null);
        }
        return buttonsPanel;
    }

    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel()
    {
        if (jPanel == null)
        {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            gridLayout.setHgap(5);
            jPanel = new JPanel();
            jPanel.setLayout(gridLayout);
            jPanel.add(getOkButton(), null);
            jPanel.add(getCancelButton(), null);
        }
        return jPanel;
    }

    /**
     * This method initializes okButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getOkButton()
    {
        if (okButton == null)
        {
            okButton = new JButton();
            okButton.setText("OK");
            okButton.addActionListener(this);
        }
        return okButton;
    }

    /**
     * This method initializes cancelButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getCancelButton()
    {
        if (cancelButton == null)
        {
            cancelButton = new JButton();
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(this);
        }
        return cancelButton;
    }

    /**
     * This method initializes jTabbedPane	
     * 	
     * @return javax.swing.JTabbedPane	
     */
    private JTabbedPane getJTabbedPane()
    {
        if (jTabbedPane == null)
        {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.addTab("Processing", null, getProcessingTabPanel(), null);
            jTabbedPane.addTab("Sorting", null, getSortingTabPanel(), null);
            jTabbedPane.addTab("Constraints", null, getConstraintPanel(), null);
        }
        return jTabbedPane;
    }

    /**
     * This method initializes jPanel1	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getSortingTabPanel()
    {
        if (sortingTabPanel == null)
        {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 2;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridy = 0;
            sortingTabPanel = new JPanel();
            sortingTabPanel.setLayout(new GridBagLayout());
            sortingTabPanel.add(getGenerationsPanel(), gridBagConstraints2);
            sortingTabPanel.add(getOverallPanel(), gridBagConstraints);
            sortingTabPanel.add(getAreaPanel(), gridBagConstraints1);
        }
        return sortingTabPanel;
    }

    /**
     * This method initializes jPanel2	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getProcessingTabPanel()
    {
        if (processingTabPanel == null)
        {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 1;
            gridBagConstraints1.gridwidth = 3;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridy = 2;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridwidth = 3;
            gridBagConstraints3.anchor = GridBagConstraints.WEST;
            gridBagConstraints3.gridy = 3;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridwidth = 3;
            gridBagConstraints4.anchor = GridBagConstraints.WEST;
            gridBagConstraints4.gridy = 4;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridy = 5;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.gridwidth = 3;
            gridBagConstraints6.anchor = GridBagConstraints.WEST;
            gridBagConstraints6.gridy = 6;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 0;
            gridBagConstraints7.gridwidth = 3;
            gridBagConstraints7.anchor = GridBagConstraints.WEST;
            gridBagConstraints7.gridy = 7;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.gridy = 8;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.gridwidth = 3;
            gridBagConstraints9.anchor = GridBagConstraints.WEST;
            gridBagConstraints9.gridy = 9;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.anchor = GridBagConstraints.WEST;
            gridBagConstraints10.insets = new Insets(0, 50, 0, 0);
            gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.gridy = 10;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 11;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 0;
            gridBagConstraints12.anchor = GridBagConstraints.WEST;
            gridBagConstraints12.gridy = 12;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.insets = new Insets(0, 50, 5, 0);
            gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints13.gridy = 13;
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.gridx = 0;
            gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints14.gridy = 14;
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.anchor = GridBagConstraints.WEST;
            gridBagConstraints15.gridy = 15;
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.gridx = 0;
            gridBagConstraints16.anchor = GridBagConstraints.WEST;
            gridBagConstraints16.insets = new Insets(0, 50, 0, 0);
            gridBagConstraints16.gridy = 16;
            processingTabPanel = new JPanel();
            processingTabPanel.setLayout(new GridBagLayout());
            processingTabPanel.add(getRunInBackground(), gridBagConstraints1);
            processingTabPanel.add(new JSeparator(), gridBagConstraints2);
            processingTabPanel.add(getIgnoreSubperiods(), gridBagConstraints3);
            processingTabPanel.add(getUseCombination(), gridBagConstraints4);
            processingTabPanel.add(new JSeparator(), gridBagConstraints5);
            processingTabPanel.add(getStopEachIter(), gridBagConstraints6);
            processingTabPanel.add(getStopEachSolution(), gridBagConstraints7);
            processingTabPanel.add(new JSeparator(), gridBagConstraints8);
            processingTabPanel.add(getSaveSolutionsToFile(), gridBagConstraints9);
            processingTabPanel.add(getSaveResultPanel(), gridBagConstraints10);
            processingTabPanel.add(new JSeparator(), gridBagConstraints11);
            processingTabPanel.add(getSaveStatus(), gridBagConstraints12);
            processingTabPanel.add(getSaveStatusPanel(), gridBagConstraints13);
            processingTabPanel.add(new JSeparator(), gridBagConstraints14);
            processingTabPanel.add(getDisplaySearchStatus(), gridBagConstraints15);
            processingTabPanel.add(getJPanel5(), gridBagConstraints16);
        }
        return processingTabPanel;
    }

    /**
     * This method initializes overallPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getOverallPanel()
    {
        if (overallPanel == null)
        {
            overallPanel = new JPanel();
            overallPanel.setLayout(new BoxLayout(getOverallPanel(), BoxLayout.Y_AXIS));
            overallPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Overall", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            overallPanel.setName("overall");
            overallPanel.add(getSortAreaFirst(), null);
            overallPanel.add(getSortGensFirst(), null);
        }
        return overallPanel;
    }

    /**
     * This method initializes sortAreaFirst	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getSortAreaFirst()
    {
        if (sortAreaFirst == null)
        {
            sortAreaFirst = new JRadioButton();
            sortAreaFirst.setText("Process all cells in a generation first, then go to another generation");
        }
        return sortAreaFirst;
    }

    /**
     * This method initializes sortGensFirst	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getSortGensFirst()
    {
        if (sortGensFirst == null)
        {
            sortGensFirst = new JRadioButton();
            sortGensFirst.setText("Process all generations of a cell first, then go to another cell");
        }
        return sortGensFirst;
    }

    /**
     * This method initializes generationsPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getGenerationsPanel()
    {
        if (generationsPanel == null)
        {
            generationsPanel = new JPanel();
            generationsPanel.setLayout(new BoxLayout(getGenerationsPanel(), BoxLayout.Y_AXIS));
            generationsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Generations", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            generationsPanel.setName("generations");
            generationsPanel.add(getSortToFuture(), null);
            generationsPanel.add(getSortToPast(), null);
        }
        return generationsPanel;
    }

    /**
     * This method initializes sortToPast	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getSortToPast()
    {
        if (sortToPast == null)
        {
            sortToPast = new JRadioButton();
            sortToPast.setText("Start with last generation, continue to past");
        }
        return sortToPast;
    }

    /**
     * This method initializes sortToFuture	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getSortToFuture()
    {
        if (sortToFuture == null)
        {
            sortToFuture = new JRadioButton();
            sortToFuture.setText("Start with first generation, continue to future");
        }
        return sortToFuture;
    }

    /**
     * This method initializes areaPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getAreaPanel()
    {
        if (areaPanel == null)
        {
            areaPanel = new JPanel();
            areaPanel.setLayout(new BoxLayout(getAreaPanel(), BoxLayout.Y_AXIS));
            areaPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Area", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            areaPanel.setName("area");
            areaPanel.add(getAreaUpperPanel(), null);
            areaPanel.add(getAreaLowerPanel(), null);
        }
        return areaPanel;
    }

    /**
     * This method initializes areaUpperPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getAreaUpperPanel()
    {
        if (areaUpperPanel == null)
        {
            FlowLayout flowLayout1 = new FlowLayout();
            flowLayout1.setAlignment(FlowLayout.LEFT);
            jLabel2 = new JLabel();
            jLabel2.setText(", then expand...");
            jLabel1 = new JLabel();
            jLabel1.setText(", row");
            jLabel = new JLabel();
            jLabel.setText("Start at column");
            areaUpperPanel = new JPanel();
            areaUpperPanel.setLayout(flowLayout1);
            areaUpperPanel.add(jLabel, null);
            areaUpperPanel.add(getSortStartColumn(), null);
            areaUpperPanel.add(jLabel1, null);
            areaUpperPanel.add(getSortStartRow(), null);
            areaUpperPanel.add(jLabel2, null);
        }
        return areaUpperPanel;
    }

    /**
     * This method initializes areaLowerPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getAreaLowerPanel()
    {
        if (areaLowerPanel == null)
        {
            GridLayout gridLayout1 = new GridLayout();
            gridLayout1.setRows(2);
            areaLowerPanel = new JPanel();
            areaLowerPanel.setLayout(gridLayout1);
            areaLowerPanel.add(getSortHorizontal(), null);
            areaLowerPanel.add(getSortVertical(), null);
            areaLowerPanel.add(getSortBox(), null);
            areaLowerPanel.add(getSortCircle(), null);
            areaLowerPanel.add(getSortDiagonal(), null);
            areaLowerPanel.add(getSortDiagonalBack(), null);
            areaLowerPanel.add(getSortDiamond(), null);
            areaLowerPanel.add(getSortReverse(), null);
        }
        return areaLowerPanel;
    }

    /**
     * This method initializes sortStartColumn	
     * 	
     * @return javax.swing.JSpinner	
     */
    private JSpinner getSortStartColumn()
    {
        if (sortStartColumn == null)
        {
            sortStartColumn = new JSpinner();
            sortStartColumn.setPreferredSize(new Dimension(70, 20));
            sortStartColumn.addChangeListener(this);
            sortStartColumn.setModel(new SpinnerNumberModel());
        }
        return sortStartColumn;
    }

    /**
     * This method initializes sortStartRow	
     * 	
     * @return javax.swing.JSpinner	
     */
    private JSpinner getSortStartRow()
    {
        if (sortStartRow == null)
        {
            sortStartRow = new JSpinner();
            sortStartRow.setPreferredSize(new Dimension(70, 20));
            sortStartRow.addChangeListener(this);
            sortStartRow.setModel(new SpinnerNumberModel());
        }
        return sortStartRow;
    }

    /**
     * This method initializes sortHorizontal	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getSortHorizontal()
    {
        if (sortHorizontal == null)
        {
            sortHorizontal = new JRadioButton();
            sortHorizontal.setText("Horizontal");
            sortHorizontal.setIcon(new ImageIcon(getClass().getResource("/images/sort_horz.gif")));
            sortHorizontal.setSelectedIcon(new ImageIcon(getClass().getResource("/images/sort_horz_sel.gif")));
            sortHorizontal.addChangeListener(this);
        }
        return sortHorizontal;
    }

    /**
     * This method initializes sortDiagonal	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getSortDiagonal()
    {
        if (sortDiagonal == null)
        {
            sortDiagonal = new JRadioButton();
            sortDiagonal.setText("Diagonal");
            sortDiagonal.setIcon(new ImageIcon(getClass().getResource("/images/sort_diag1.gif")));
            sortDiagonal.setSelectedIcon(new ImageIcon(getClass().getResource("/images/sort_diag1_sel.gif")));
            sortDiagonal.addChangeListener(this);
        }
        return sortDiagonal;
    }

    /**
     * This method initializes sortBox	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getSortBox()
    {
        if (sortBox == null)
        {
            sortBox = new JRadioButton();
            sortBox.setText("Box");
            sortBox.setIcon(new ImageIcon(getClass().getResource("/images/sort_box.gif")));
            sortBox.setSelectedIcon(new ImageIcon(getClass().getResource("/images/sort_box_sel.gif")));
            sortBox.addChangeListener(this);
        }
        return sortBox;
    }

    /**
     * This method initializes sortCircle	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getSortCircle()
    {
        if (sortCircle == null)
        {
            sortCircle = new JRadioButton();
            sortCircle.setText("Circle");
            sortCircle.setIcon(new ImageIcon(getClass().getResource("/images/sort_circle.gif")));
            sortCircle.setSelectedIcon(new ImageIcon(getClass().getResource("/images/sort_circle_sel.gif")));
            sortCircle.addChangeListener(this);
        }
        return sortCircle;
    }

    /**
     * This method initializes sortVertical	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getSortVertical()
    {
        if (sortVertical == null)
        {
            sortVertical = new JRadioButton();
            sortVertical.setText("Vertical");
            sortVertical.setIcon(new ImageIcon(getClass().getResource("/images/sort_vert.gif")));
            sortVertical.setSelectedIcon(new ImageIcon(getClass().getResource("/images/sort_vert_sel.gif")));
            sortVertical.addChangeListener(this);
        }
        return sortVertical;
    }

    /**
     * This method initializes sortDiagonalBack	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getSortDiagonalBack()
    {
        if (sortDiagonalBack == null)
        {
            sortDiagonalBack = new JRadioButton();
            sortDiagonalBack.setText("Back diagonal");
            sortDiagonalBack.setIcon(new ImageIcon(getClass().getResource("/images/sort_diag2.gif")));
            sortDiagonalBack.setSelectedIcon(new ImageIcon(getClass().getResource("/images/sort_diag2_sel.gif")));
            sortDiagonalBack.addChangeListener(this);
        }
        return sortDiagonalBack;
    }

    /**
     * This method initializes sortDiamond	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getSortDiamond()
    {
        if (sortDiamond == null)
        {
            sortDiamond = new JRadioButton();
            sortDiamond.setText("Diamond");
            sortDiamond.setIcon(new ImageIcon(getClass().getResource("/images/sort_diamond.gif")));
            sortDiamond.setSelectedIcon(new ImageIcon(getClass().getResource("/images/sort_diamond_sel.gif")));
            sortDiamond.addChangeListener(this);
        }
        return sortDiamond;
    }
    
    private JCheckBox getSortReverse()
    {
        if (sortReverse == null)
        {
            sortReverse = new JCheckBox();
            sortReverse.setText("Reverse order");
        }
        return sortReverse;
    }

    /**
     * This method initializes runInBackground	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getRunInBackground()
    {
        if (runInBackground == null)
        {
            runInBackground = new JCheckBox();
            runInBackground.setText("Preprocess while editing");
        }
        return runInBackground;
    }

    /**
     * This method initializes stopEachIter	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getStopEachIter()
    {
        if (stopEachIter == null)
        {
            stopEachIter = new JCheckBox();
            stopEachIter.setText("Pause search after each iteration");
        }
        return stopEachIter;
    }

    /**
     * This method initializes stopEachSolution	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getStopEachSolution()
    {
        if (stopEachSolution == null)
        {
            stopEachSolution = new JCheckBox();
            stopEachSolution.setText("Pause search after each solution");
        }
        return stopEachSolution;
    }

    /**
     * This method initializes saveSolutionsToFile	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getSaveSolutionsToFile()
    {
        if (saveSolutionsToFile == null)
        {
            saveSolutionsToFile = new JCheckBox();
            saveSolutionsToFile.setText("Append solutions to file");
        }
        return saveSolutionsToFile;
    }

    /**
     * This method initializes solutionFileName	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getSolutionFileName()
    {
        if (solutionFileName == null)
        {
            solutionFileName = new JTextField();
            solutionFileName.setPreferredSize(new Dimension(300, 20));
        }
        return solutionFileName;
    }

    /**
     * This method initializes saveStatus	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getSaveStatus()
    {
        if (saveStatus == null)
        {
            saveStatus = new JCheckBox();
            saveStatus.setText("Periodically save search status to file");
        }
        return saveStatus;
    }

    /**
     * This method initializes useCombination	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getUseCombination()
    {
        if (useCombination == null)
        {
            useCombination = new JCheckBox();
            useCombination.setText("Use combination of previous solutions to prune search");
            useCombination.setActionCommand("Use combination of previous results to prune search");
        }
        return useCombination;
    }
    
    private JCheckBox getIgnoreSubperiods()
    {
        if (ignoreSubperiods == null)
        {
            ignoreSubperiods = new JCheckBox();
            ignoreSubperiods.setText("Ignore subperiodic solutions");
        }
        return ignoreSubperiods;    
    }

    /**
     * This method initializes screenUpdatePeriod	
     * 	
     * @return javax.swing.JSpinner	
     */
    private JSpinner getScreenUpdatePeriod()
    {
        if (screenUpdatePeriod == null)
        {
            screenUpdatePeriod = new JSpinner();
            screenUpdatePeriod.setPreferredSize(new Dimension(50, 20));
            screenUpdatePeriod.setModel(new SpinnerNumberModel(1, 1, 3600, 1));
        }
        return screenUpdatePeriod;
    }

    /**
     * This method initializes browseResultFile	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBrowseResultFile()
    {
        if (browseResultFile == null)
        {
            browseResultFile = new JButton();
            browseResultFile.setText("Browse...");
            browseResultFile.addActionListener(this);
        }
        return browseResultFile;
    }

    /**
     * This method initializes statusSavePeriod	
     * 	
     * @return javax.swing.JSpinner	
     */
    private JSpinner getStatusSavePeriod()
    {
        if (statusSavePeriod == null)
        {
            statusSavePeriod = new JSpinner();
            statusSavePeriod.setPreferredSize(new Dimension(50, 20));
            statusSavePeriod.setModel(new SpinnerNumberModel(1, 1, 86400, 1));
        }
        return statusSavePeriod;
    }

    /**
     * This method initializes statusFileName	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getStatusFileName()
    {
        if (statusFileName == null)
        {
            statusFileName = new JTextField();
            statusFileName.setPreferredSize(new Dimension(300, 20));
        }
        return statusFileName;
    }

    /**
     * This method initializes browseStatusFile	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getBrowseStatusFile()
    {
        if (browseStatusFile == null)
        {
            browseStatusFile = new JButton();
            browseStatusFile.setText("Browse...");
            browseStatusFile.addActionListener(this);
        }
        return browseStatusFile;
    }

    /**
     * This method initializes displaySearchStatus	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getDisplaySearchStatus()
    {
        if (displaySearchStatus == null)
        {
            displaySearchStatus = new JCheckBox();
            displaySearchStatus.setText("Display search status on screen");
        }
        return displaySearchStatus;
    }

    /**
     * This method initializes saveResultPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getSaveResultPanel()
    {
        if (saveResultPanel == null)
        {
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.anchor = GridBagConstraints.WEST;
            gridBagConstraints15.gridy = 3;
            GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
            gridBagConstraints29.gridx = 0;
            gridBagConstraints29.anchor = GridBagConstraints.WEST;
            gridBagConstraints29.gridy = 2;
            GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
            gridBagConstraints27.gridx = 1;
            gridBagConstraints27.gridy = 0;
            GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
            gridBagConstraints26.fill = GridBagConstraints.NONE;
            gridBagConstraints26.insets = new Insets(0, 5, 0, 5);
            gridBagConstraints26.weightx = 1.0;
            saveResultPanel = new JPanel();
            saveResultPanel.setLayout(new GridBagLayout());
            saveResultPanel.add(getSolutionFileName(), gridBagConstraints26);
            saveResultPanel.add(getBrowseResultFile(), gridBagConstraints27);
            saveResultPanel.add(getJPanel3(), gridBagConstraints29);
            saveResultPanel.add(getSaveResultsAllGen(), gridBagConstraints15);
        }
        return saveResultPanel;
    }

    /**
     * This method initializes saveResultsAllGen	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getSaveResultsAllGen()
    {
        if (saveResultsAllGen == null)
        {
            saveResultsAllGen = new JCheckBox();
            saveResultsAllGen.setText("Save all generations");
        }
        return saveResultsAllGen;
    }

    /**
     * This method initializes cellsBetweenPatterns	
     * 	
     * @return javax.swing.JSpinner	
     */
    private JSpinner getCellsBetweenPatterns()
    {
        if (cellsBetweenPatterns == null)
        {
            cellsBetweenPatterns = new JSpinner();
            cellsBetweenPatterns.setPreferredSize(new Dimension(50, 20));
            cellsBetweenPatterns.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        }
        return cellsBetweenPatterns;
    }

    /**
     * This method initializes jPanel3	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel3()
    {
        if (jPanel3 == null)
        {
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.insets = new Insets(0, 5, 0, 0);
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.anchor = GridBagConstraints.WEST;
            gridBagConstraints6.gridy = 3;
            gridBagConstraints6.gridwidth = 3;
            gridBagConstraints6.gridx = 0;
            GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
            gridBagConstraints30.gridx = 2;
            gridBagConstraints30.gridy = 0;
            jLabel5 = new JLabel();
            jLabel5.setText("cells space between patterns");
            GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
            gridBagConstraints28.anchor = GridBagConstraints.WEST;
            gridBagConstraints28.gridy = 0;
            gridBagConstraints28.insets = new Insets(0, 5, 0, 5);
            gridBagConstraints28.gridx = 1;
            jLabel3 = new JLabel();
            jLabel3.setText("Leave");
            jPanel3 = new JPanel();
            jPanel3.setLayout(new GridBagLayout());
            jPanel3.add(jLabel3, gridBagConstraints12);
            jPanel3.add(getCellsBetweenPatterns(), gridBagConstraints28);
            jPanel3.add(jLabel5, gridBagConstraints30);
        }
        return jPanel3;
    }

    /**
     * This method initializes saveStatusPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getSaveStatusPanel()
    {
        if (saveStatusPanel == null)
        {
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.gridx = 0;
            gridBagConstraints20.anchor = GridBagConstraints.WEST;
            gridBagConstraints20.gridy = 1;
            GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
            gridBagConstraints32.fill = GridBagConstraints.NONE;
            gridBagConstraints32.weightx = 1.0;
            saveStatusPanel = new JPanel();
            saveStatusPanel.setLayout(new GridBagLayout());
            saveStatusPanel.add(getStatusFileName(), gridBagConstraints32);
            saveStatusPanel.add(getBrowseStatusFile(), new GridBagConstraints());
            saveStatusPanel.add(getJPanel4(), gridBagConstraints20);
        }
        return saveStatusPanel;
    }

    /**
     * This method initializes jPanel4	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel4()
    {
        if (jPanel4 == null)
        {
            GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
            gridBagConstraints33.gridx = 0;
            gridBagConstraints33.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints33.gridy = 0;
            jLabel6 = new JLabel();
            jLabel6.setText("Every");
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.gridx = 2;
            gridBagConstraints14.gridy = -1;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 1;
            gridBagConstraints13.insets = new Insets(0, 5, 0, 5);
            gridBagConstraints13.gridy = -1;
            jPanel4 = new JPanel();
            jPanel4.setLayout(new GridBagLayout());
            jPanel4.add(getStatusSavePeriod(), gridBagConstraints13);
            jPanel4.add(new JLabel("seconds"), gridBagConstraints14);
            jPanel4.add(jLabel6, gridBagConstraints33);
        }
        return jPanel4;
    }

    /**
     * This method initializes jPanel5	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel5()
    {
        if (jPanel5 == null)
        {
            GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
            gridBagConstraints35.gridx = 0;
            gridBagConstraints35.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints35.gridy = 0;
            jLabel7 = new JLabel();
            jLabel7.setText("Every");
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 2;
            gridBagConstraints11.gridy = -1;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 1;
            gridBagConstraints10.insets = new Insets(0, 5, 0, 5);
            gridBagConstraints10.gridy = -1;
            jPanel5 = new JPanel();
            jPanel5.setLayout(new GridBagLayout());
            jPanel5.add(getScreenUpdatePeriod(), gridBagConstraints10);
            jPanel5.add(new JLabel("seconds"), gridBagConstraints11);
            jPanel5.add(jLabel7, gridBagConstraints35);
        }
        return jPanel5;
    }

    /**
     * This method initializes constraintPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getConstraintPanel()
    {
        if (constraintPanel == null)
        {
            GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
            gridBagConstraints36.gridx = 0;
            gridBagConstraints36.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints36.gridy = 0;
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
            gridBagConstraints19.gridx = 0;
            gridBagConstraints19.gridy = 2;
            constraintPanel = new JPanel();
            constraintPanel.setLayout(new GridBagLayout());
            constraintPanel.add(getLayerConstraintPanel(), gridBagConstraints19);
            constraintPanel.add(getGen0ConstraintPanel(), gridBagConstraints36);
        }
        return constraintPanel;
    }

    /**
     * This method initializes limitLiveCells	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getLimitLiveCells()
    {
        if (limitLiveCells == null)
        {
            limitLiveCells = new JCheckBox();
            limitLiveCells.setText("No more than");
            limitLiveCells.setToolTipText("Turns constraint usage on and off");
        }
        return limitLiveCells;
    }

    /**
     * This method initializes layerConstraintPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getLayerConstraintPanel()
    {
        if (layerConstraintPanel == null)
        {
            layerConstraintPanel = new JPanel();
            layerConstraintPanel.setLayout(new BoxLayout(getLayerConstraintPanel(), BoxLayout.Y_AXIS));
            layerConstraintPanel.setName("area");
            layerConstraintPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Layers", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            layerConstraintPanel.add(getJPanel9(), null);
            layerConstraintPanel.add(getJPanel10(), null);
            layerConstraintPanel.add(getJSeparator51(), null);
            layerConstraintPanel.add(getJPanel6(), null);
            layerConstraintPanel.add(getJSeparator4(), null);
            layerConstraintPanel.add(getLayerUpperPanel(), null);
            layerConstraintPanel.add(getLayerLowerPanel(), null);
        }
        return layerConstraintPanel;
    }

    /**
     * This method initializes layerUpperPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getLayerUpperPanel()
    {
        if (layerUpperPanel == null)
        {
            jLabel11 = new JLabel();
            jLabel11.setText(", row");
            jLabel8 = new JLabel();
            jLabel8.setText("Start at column");
            FlowLayout flowLayout11 = new FlowLayout();
            flowLayout11.setAlignment(FlowLayout.LEFT);
            layerUpperPanel = new JPanel();
            layerUpperPanel.setLayout(flowLayout11);
            layerUpperPanel.add(jLabel8, null);
            layerUpperPanel.add(getLayerStartColumn(), null);
            layerUpperPanel.add(jLabel11, null);
            layerUpperPanel.add(getLayerStartRow(), null);
        }
        return layerUpperPanel;
    }

    /**
     * This method initializes layerStartColumn	
     * 	
     * @return javax.swing.JSpinner	
     */
    private JSpinner getLayerStartColumn()
    {
        if (layerStartColumn == null)
        {
            layerStartColumn = new JSpinner();
            layerStartColumn.setPreferredSize(new Dimension(70, 20));
            layerStartColumn.setToolTipText("");
            layerStartColumn.setModel(new SpinnerNumberModel());
        }
        return layerStartColumn;
    }

    /**
     * This method initializes layerStartRow	
     * 	
     * @return javax.swing.JSpinner	
     */
    private JSpinner getLayerStartRow()
    {
        if (layerStartRow == null)
        {
            layerStartRow = new JSpinner();
            layerStartRow.setPreferredSize(new Dimension(70, 20));
            layerStartRow.setModel(new SpinnerNumberModel());
        }
        return layerStartRow;
    }

    /**
     * This method initializes layerLowerPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getLayerLowerPanel()
    {
        if (layerLowerPanel == null)
        {
            GridLayout gridLayout11 = new GridLayout();
            gridLayout11.setRows(2);
            layerLowerPanel = new JPanel();
            layerLowerPanel.setLayout(gridLayout11);
            layerLowerPanel.add(getLayerCol(), null);
            layerLowerPanel.add(getLayerRow(), null);
            layerLowerPanel.add(getLayerBox(), null);
            layerLowerPanel.add(getLayerCircle(), null);
            layerLowerPanel.add(getLayerDiag(), null);
            layerLowerPanel.add(getLayerDiagBack(), null);
            layerLowerPanel.add(getLayerDiamond(), null);
        }
        return layerLowerPanel;
    }

    /**
     * This method initializes layerCol	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getLayerCol()
    {
        if (layerCol == null)
        {
            layerCol = new JRadioButton();
            layerCol.setIcon(new ImageIcon(getClass().getResource("/images/layer_vert.gif")));
            layerCol.setText("Columns");
            layerCol.setSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_vert_sel.gif")));
            layerCol.setDisabledIcon(new ImageIcon(getClass().getResource("/images/layer_vert_dis.gif")));
            layerCol.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_vert_sel_dis.gif")));
        }
        return layerCol;
    }

    /**
     * This method initializes layerRow	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getLayerRow()
    {
        if (layerRow == null)
        {
            layerRow = new JRadioButton();
            layerRow.setIcon(new ImageIcon(getClass().getResource("/images/layer_horz.gif")));
            layerRow.setText("Rows");
            layerRow.setSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_horz_sel.gif")));
            layerRow.setDisabledIcon(new ImageIcon(getClass().getResource("/images/layer_horz_dis.gif")));
            layerRow.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_horz_sel_dis.gif")));
        }
        return layerRow;
    }

    /**
     * This method initializes layerBox	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getLayerBox()
    {
        if (layerBox == null)
        {
            layerBox = new JRadioButton();
            layerBox.setIcon(new ImageIcon(getClass().getResource("/images/layer_box.gif")));
            layerBox.setText("Boxes");
            layerBox.setSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_box_sel.gif")));
            layerBox.setDisabledIcon(new ImageIcon(getClass().getResource("/images/layer_box_dis.gif")));
            layerBox.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_box_sel_dis.gif")));
        }
        return layerBox;
    }

    /**
     * This method initializes layerCircle	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getLayerCircle()
    {
        if (layerCircle == null)
        {
            layerCircle = new JRadioButton();
            layerCircle.setIcon(new ImageIcon(getClass().getResource("/images/layer_circle.gif")));
            layerCircle.setText("Circles");
            layerCircle.setSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_circle_sel.gif")));
            layerCircle.setDisabledIcon(new ImageIcon(getClass().getResource("/images/layer_circle_dis.gif")));
            layerCircle.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_circle_sel_dis.gif")));
        }
        return layerCircle;
    }

    /**
     * This method initializes layerDiag	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getLayerDiag()
    {
        if (layerDiag == null)
        {
            layerDiag = new JRadioButton();
            layerDiag.setIcon(new ImageIcon(getClass().getResource("/images/layer_diag1.gif")));
            layerDiag.setText("Diagonals");
            layerDiag.setSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_diag1_sel.gif")));
            layerDiag.setDisabledIcon(new ImageIcon(getClass().getResource("/images/layer_diag1_dis.gif")));
            layerDiag.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_diag1_sel_dis.gif")));
        }
        return layerDiag;
    }

    /**
     * This method initializes layerDiagBack	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getLayerDiagBack()
    {
        if (layerDiagBack == null)
        {
            layerDiagBack = new JRadioButton();
            layerDiagBack.setIcon(new ImageIcon(getClass().getResource("/images/layer_diag2.gif")));
            layerDiagBack.setText("Back diagonals");
            layerDiagBack.setSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_diag2_sel.gif")));
            layerDiagBack.setDisabledIcon(new ImageIcon(getClass().getResource("/images/layer_diag2_dis.gif")));
            layerDiagBack.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_diag2_sel_dis.gif")));
        }
        return layerDiagBack;
    }

    /**
     * This method initializes layerDiamond	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getLayerDiamond()
    {
        if (layerDiamond == null)
        {
            layerDiamond = new JRadioButton();
            layerDiamond.setIcon(new ImageIcon(getClass().getResource("/images/layer_diamond.gif")));
            layerDiamond.setText("Diamonds");
            layerDiamond.setSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_diamond_sel.gif")));
            layerDiamond.setDisabledIcon(new ImageIcon(getClass().getResource("/images/layer_diamond_dis.gif")));
            layerDiamond.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/layer_diamond_sel_dis.gif")));
        }
        return layerDiamond;
    }

    /**
     * This method initializes jPanel6	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel6()
    {
        if (jPanel6 == null)
        {
            FlowLayout flowLayout2 = new FlowLayout();
            flowLayout2.setAlignment(FlowLayout.LEFT);
            jPanel6 = new JPanel();
            jPanel6.setLayout(flowLayout2);
            jPanel6.add(getLayerUseSort(), null);
        }
        return jPanel6;
    }

    /**
     * This method initializes layerUseSort	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getLayerUseSort()
    {
        if (layerUseSort == null)
        {
            layerUseSort = new JCheckBox();
            layerUseSort.setText("Define layers through sorting");
            layerUseSort.setToolTipText("If checked, the starting position and layers geography can be changed in sorting tab");
            layerUseSort.addChangeListener(this);
        }
        return layerUseSort;
    }

    /**
     * This method initializes jSeparator4	
     * 	
     * @return javax.swing.JSeparator	
     */
    private JSeparator getJSeparator4()
    {
        if (jSeparator4 == null)
        {
            jSeparator4 = new JSeparator();
        }
        return jSeparator4;
    }

    /**
     * This method initializes gen0ConstraintPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getGen0ConstraintPanel()
    {
        if (gen0ConstraintPanel == null)
        {
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.gridx = -1;
            gridBagConstraints16.gridy = -1;
            gen0ConstraintPanel = new JPanel();
            gen0ConstraintPanel.setLayout(new BoxLayout(getGen0ConstraintPanel(), BoxLayout.Y_AXIS));
            gen0ConstraintPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Generation 0", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
            gen0ConstraintPanel.add(getJPanel7(), null);
        }
        return gen0ConstraintPanel;
    }

    /**
     * This method initializes jPanel9	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel9()
    {
        if (jPanel9 == null)
        {
            jLabel10 = new JLabel();
            jLabel10.setText("live cells in each layer");
            FlowLayout flowLayout4 = new FlowLayout();
            flowLayout4.setAlignment(FlowLayout.LEFT);
            jPanel9 = new JPanel();
            jPanel9.setLayout(flowLayout4);
            jPanel9.add(getLimitLiveCells(), null);
            jPanel9.add(getMaxLiveCells(), null);
            jPanel9.add(jLabel10, null);
            jPanel9.add(getLimitLiveCellsVarsOnly(), null);
        }
        return jPanel9;
    }

    /**
     * This method initializes jPanel10	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel10()
    {
        if (jPanel10 == null)
        {
            jLabel13 = new JLabel();
            jLabel13.setText("active cells in each layer");
            FlowLayout flowLayout5 = new FlowLayout();
            flowLayout5.setAlignment(FlowLayout.LEFT);
            jPanel10 = new JPanel();
            jPanel10.setLayout(flowLayout5);
            jPanel10.add(getLimitActiveCells(), null);
            jPanel10.add(getMaxActiveCells(), null);
            jPanel10.add(jLabel13, null);
            jPanel10.add(getLimitActiveCellsVarsOnly(), null);
        }
        return jPanel10;
    }

    /**
     * This method initializes maxLiveCells	
     * 	
     * @return javax.swing.JSpinner	
     */
    private JSpinner getMaxLiveCells()
    {
        if (maxLiveCells == null)
        {
            maxLiveCells = new JSpinner();
            maxLiveCells.setPreferredSize(new Dimension(50, 20));
            maxLiveCells.setToolTipText("This number limits the number of cells in each layer that can be ON in any generation");
            maxLiveCells.setModel(new SpinnerNumberModel(1, 1, 10000, 1));
        }
        return maxLiveCells;
    }

    private JCheckBox getLimitLiveCellsVarsOnly()
    {
        if (limitLiveCellsVarsOnly == null)
        {
            limitLiveCellsVarsOnly = new JCheckBox();
            limitLiveCellsVarsOnly.setText("excluding manually set cells");
        }
        return limitLiveCellsVarsOnly;
    }

    /**
     * This method initializes maxActiveCells	
     * 	
     * @return javax.swing.JSpinner	
     */
    private JSpinner getMaxActiveCells()
    {
        if (maxActiveCells == null)
        {
            maxActiveCells = new JSpinner();
            maxActiveCells.setPreferredSize(new Dimension(50, 20));
            maxActiveCells.setToolTipText("This number limits the number of cells in each layer that can change state in any generation");
            maxActiveCells.setModel(new SpinnerNumberModel(1, 1, 10000, 1));
        }
        return maxActiveCells;
    }

    private JCheckBox getLimitActiveCellsVarsOnly()
    {
        if (limitActiveCellsVarsOnly == null)
        {
            limitActiveCellsVarsOnly = new JCheckBox();
            limitActiveCellsVarsOnly.setText("excluding manually set cells");
        }
        return limitActiveCellsVarsOnly;
    }


    /**
     * This method initializes jSeparator51	
     * 	
     * @return javax.swing.JSeparator	
     */
    private JSeparator getJSeparator51()
    {
        if (jSeparator51 == null)
        {
            jSeparator51 = new JSeparator();
        }
        return jSeparator51;
    }

    /**
     * This method initializes limitActiveCells	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getLimitActiveCells()
    {
        if (limitActiveCells == null)
        {
            limitActiveCells = new JCheckBox();
            limitActiveCells.setText("No more than");
        }
        return limitActiveCells;
    }

    /**
     * This method initializes jPanel7	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel7()
    {
        if (jPanel7 == null)
        {
            jLabel9 = new JLabel();
            jLabel9.setText("ON cells in generation 0");
            FlowLayout flowLayout3 = new FlowLayout();
            flowLayout3.setAlignment(FlowLayout.LEFT);
            jPanel7 = new JPanel();
            jPanel7.setLayout(flowLayout3);
            jPanel7.add(getLimitGen0(), null);
            jPanel7.add(getLimitGen0Cells(), null);
            jPanel7.add(jLabel9, null);
            jPanel7.add(getLimitGen0VarsOnly(), null);
        }
        return jPanel7;
    }

    /**
     * This method initializes limitGen0	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getLimitGen0()
    {
        if (limitGenZero == null)
        {
            limitGenZero = new JCheckBox();
            limitGenZero.setText("No more than");
        }
        return limitGenZero;
    }
    
    private JCheckBox getLimitGen0VarsOnly()
    {
        if (limitGenZeroVarsOnly == null)
        {
            limitGenZeroVarsOnly = new JCheckBox();
            limitGenZeroVarsOnly.setText("excluding manually set cells");
        }
        return limitGenZeroVarsOnly;
    }

    /**
     * This method initializes limitGen0Cells	
     * 	
     * @return javax.swing.JSpinner	
     */
    private JSpinner getLimitGen0Cells()
    {
        if (limitGenZeroCells == null)
        {
            limitGenZeroCells = new JSpinner();
            limitGenZeroCells.setPreferredSize(new Dimension(50, 20));
            limitGenZeroCells.setModel(new SpinnerNumberModel(1, 1, 1000000, 1));
        }
        return limitGenZeroCells;
    }
    
    private void syncLayers()
    {
        boolean useSort = layerUseSort.isSelected();
        boolean enable = !searchMode && !useSort;
        layerStartColumn.setEnabled(enable);
        layerStartRow.setEnabled(enable);
        layerCol.setEnabled(enable);
        layerRow.setEnabled(enable);
        layerBox.setEnabled(enable);
        layerCircle.setEnabled(enable);
        layerDiag.setEnabled(enable);
        layerDiagBack.setEnabled(enable);
        layerDiamond.setEnabled(enable);
        if (useSort && !searchMode)
        {
            layerStartColumn.setValue(sortStartColumn.getValue());
            layerStartRow.setValue(sortStartRow.getValue());
            layerCol.setSelected(sortHorizontal.isSelected());
            layerRow.setSelected(sortVertical.isSelected());
            layerBox.setSelected(sortBox.isSelected());
            layerCircle.setSelected(sortCircle.isSelected());
            layerDiag.setSelected(sortDiagonal.isSelected());
            layerDiagBack.setSelected(sortDiagonalBack.isSelected());
            layerDiamond.setSelected(sortDiamond.isSelected());
        }
        
    }

    public void stateChanged(ChangeEvent e)
    {
        if ((e.getSource() == sortStartColumn)
                || (e.getSource() == sortStartRow)
                || (e.getSource() == sortHorizontal)
                || (e.getSource() == sortVertical)
                || (e.getSource() == sortBox)
                || (e.getSource() == sortCircle)
                || (e.getSource() == sortDiagonal)
                || (e.getSource() == sortDiagonalBack)
                || (e.getSource() == sortDiamond)
                || (e.getSource() == layerUseSort))
        {
            syncLayers();
        }
    }
}
