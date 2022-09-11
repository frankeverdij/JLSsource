package jls;

import javax.swing.JPanel;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.BorderLayout;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;

import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JSpinner;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import java.awt.GridLayout;

public class PropertiesDialog extends JDialog implements ActionListener, ChangeListener
{

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JTabbedPane jTabbedPane = null;

    private JPanel jPanel = null;

    private JButton okButton = null;

    private JButton cancelButton = null;

    private JPanel editPanel = null;

    private JSpinner columns = null;

    private JSpinner rows = null;

    private JSpinner generations = null;

    private JSpinner period1 = null;

    private JSpinner period2 = null;

    private JSpinner period3 = null;

    private JSpinner period4 = null;

    private JSpinner period5 = null;

    private JSpinner period6 = null;

    private Properties result = null;

    private JPanel dimensionsPanel = null;

    private JPanel outerSpacePanel = null;

    private JPanel periodPanel = null;

    private JPanel tilingPanel = null;

    private JPanel horizontalTilePanel = null;

    private JRadioButton tileRightNo = null;

    private JRadioButton tileRightYes = null;

    private JSpinner tileRightShiftDown = null;

    private JSpinner tileRightShiftFuture = null;

    private JPanel verticalTilePanel = null;

    private JRadioButton tileBelowNo = null;

    private JRadioButton tileBelowYes = null;

    private JSpinner tileBelowShiftRight = null;

    private JSpinner tileBelowShiftFuture = null;

    private JPanel generationTilePanel = null;

    private JRadioButton tileAfterNo = null;

    private JRadioButton tileAfterYes = null;

    private JRadioButton outerSpaceUnset = null;

    private JRadioButton outerSpaceSet = null;

    private JSpinner tileAfterShiftRight = null;

    private JSpinner tileAfterShiftDown = null;

    private JRadioButton xlatNone = null;

    private JRadioButton xlatHorz = null;

    private JRadioButton xlatVert = null;

    private JRadioButton xlatDiagFwd = null;

    private JRadioButton xlatRot90 = null;

    private JRadioButton xlatRot180 = null;

    private JRadioButton xlatRot270 = null;

    private JRadioButton xlatDiagBwd = null;

    private JRadioButton xlatGlideDiagFwd = null;

    private JRadioButton xlatGlideDiagBwd = null;

    private JPanel translationPanel = null;

    private JPanel symmetryPanel = null;

    private JPanel jPanel2 = null;

    private JRadioButton symmNone = null;

    private JRadioButton symmMirrorHorz = null;

    private JRadioButton symmMirrorVert = null;

    private JRadioButton symmMirrorDiagFwd = null;

    private JRadioButton symmMirrorDiagBwd = null;

    private JPanel jPanel3 = null;

    private JRadioButton symmRot180 = null;

    private JRadioButton symm4fold = null;

    private JRadioButton symm4foldDiag = null;

    private JRadioButton symmRot90 = null;

    private JRadioButton symm8fold = null;

    private JPanel rulePanel = null;

    private JLabel jLabel7 = null;

    private JLabel jLabel8 = null;

    private JCheckBox birth0 = null;

    private JCheckBox birth1 = null;

    private JCheckBox birth2 = null;

    private JCheckBox birth3 = null;

    private JCheckBox birth4 = null;

    private JCheckBox birth5 = null;

    private JCheckBox birth6 = null;

    private JCheckBox birth7 = null;

    private JCheckBox birth8 = null;

    private JCheckBox survival0 = null;

    private JCheckBox survival1 = null;

    private JCheckBox survival2 = null;

    private JCheckBox survival3 = null;

    private JCheckBox survival4 = null;

    private JCheckBox survival5 = null;

    private JCheckBox survival6 = null;

    private JCheckBox survival7 = null;

    private JCheckBox survival8 = null;

    private JLabel jLabel71 = null;

    private JLabel jLabel72 = null;

    private JLabel jLabel73 = null;

    private JLabel jLabel74 = null;

    private JLabel jLabel75 = null;

    private JLabel jLabel76 = null;

    private JLabel jLabel77 = null;

    private JLabel jLabel78 = null;

    private JLabel jLabel79 = null;

    private JPanel jPanel1 = null;

    private JPanel jPanel5 = null;

    private JButton conwayLife = null;

    private JPanel jPanel6 = null;

    private JButton highLife = null;

    private JButton dayNight = null;

    private JButton diamoeba = null;

    private JButton seeds = null;

    private JButton life34 = null;

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == conwayLife)
        {
            birth0.setSelected(false);
            birth1.setSelected(false);
            birth2.setSelected(false);
            birth3.setSelected(true);
            birth4.setSelected(false);
            birth5.setSelected(false);
            birth6.setSelected(false);
            birth7.setSelected(false);
            birth8.setSelected(false);
            survival0.setSelected(false);
            survival1.setSelected(false);
            survival2.setSelected(true);
            survival3.setSelected(true);
            survival4.setSelected(false);
            survival5.setSelected(false);
            survival6.setSelected(false);
            survival7.setSelected(false);
            survival8.setSelected(false);
        }
        else if (e.getSource() == highLife)
        {
            birth0.setSelected(false);
            birth1.setSelected(false);
            birth2.setSelected(false);
            birth3.setSelected(true);
            birth4.setSelected(false);
            birth5.setSelected(false);
            birth6.setSelected(true);
            birth7.setSelected(false);
            birth8.setSelected(false);
            survival0.setSelected(false);
            survival1.setSelected(false);
            survival2.setSelected(true);
            survival3.setSelected(true);
            survival4.setSelected(false);
            survival5.setSelected(false);
            survival6.setSelected(false);
            survival7.setSelected(false);
            survival8.setSelected(false);
        }
        else if (e.getSource() == dayNight)
        {
            birth0.setSelected(false);
            birth1.setSelected(false);
            birth2.setSelected(false);
            birth3.setSelected(true);
            birth4.setSelected(false);
            birth5.setSelected(false);
            birth6.setSelected(true);
            birth7.setSelected(true);
            birth8.setSelected(true);
            survival0.setSelected(false);
            survival1.setSelected(false);
            survival2.setSelected(false);
            survival3.setSelected(true);
            survival4.setSelected(true);
            survival5.setSelected(false);
            survival6.setSelected(true);
            survival7.setSelected(true);
            survival8.setSelected(true);
        }
        else if (e.getSource() == diamoeba)
        {
            birth0.setSelected(false);
            birth1.setSelected(false);
            birth2.setSelected(false);
            birth3.setSelected(true);
            birth4.setSelected(false);
            birth5.setSelected(true);
            birth6.setSelected(true);
            birth7.setSelected(true);
            birth8.setSelected(true);
            survival0.setSelected(false);
            survival1.setSelected(false);
            survival2.setSelected(false);
            survival3.setSelected(false);
            survival4.setSelected(false);
            survival5.setSelected(true);
            survival6.setSelected(true);
            survival7.setSelected(true);
            survival8.setSelected(true);
        }
        else if (e.getSource() == seeds)
        {
            birth0.setSelected(false);
            birth1.setSelected(false);
            birth2.setSelected(true);
            birth3.setSelected(false);
            birth4.setSelected(false);
            birth5.setSelected(false);
            birth6.setSelected(false);
            birth7.setSelected(false);
            birth8.setSelected(false);
            survival0.setSelected(false);
            survival1.setSelected(false);
            survival2.setSelected(false);
            survival3.setSelected(false);
            survival4.setSelected(false);
            survival5.setSelected(false);
            survival6.setSelected(false);
            survival7.setSelected(false);
            survival8.setSelected(false);
        }
        else if (e.getSource() == life34)
        {
            birth0.setSelected(false);
            birth1.setSelected(false);
            birth2.setSelected(false);
            birth3.setSelected(true);
            birth4.setSelected(true);
            birth5.setSelected(false);
            birth6.setSelected(false);
            birth7.setSelected(false);
            birth8.setSelected(false);
            survival0.setSelected(false);
            survival1.setSelected(false);
            survival2.setSelected(false);
            survival3.setSelected(true);
            survival4.setSelected(true);
            survival5.setSelected(false);
            survival6.setSelected(false);
            survival7.setSelected(false);
            survival8.setSelected(false);
        }
        else if (e.getSource() == okButton)
        {
            result.setColumns(((Number)columns.getModel().getValue()).intValue());
            result.setRows(((Number)rows.getModel().getValue()).intValue());
            result.setGenerations(((Number)generations.getModel().getValue()).intValue());
            result.setPeriod(1,((Number)period1.getModel().getValue()).intValue());
            result.setPeriod(2,((Number)period2.getModel().getValue()).intValue());
            result.setPeriod(3,((Number)period3.getModel().getValue()).intValue());
            result.setPeriod(4,((Number)period4.getModel().getValue()).intValue());
            result.setPeriod(5,((Number)period5.getModel().getValue()).intValue());
            result.setPeriod(6,((Number)period6.getModel().getValue()).intValue());

            result.setOuterSpaceUnset(outerSpaceUnset.isSelected());

            if (symmNone.isSelected())
            {
                result.setSymmetry(Properties.SYMM_NONE);
            }
            else if (symm4fold.isSelected())
            {
                result.setSymmetry(Properties.SYMM_4FOLD);
            }
            else if (symm4foldDiag.isSelected())
            {
                result.setSymmetry(Properties.SYMM_4FOLD_DIAG);
            }
            else if (symm8fold.isSelected())
            {
                result.setSymmetry(Properties.SYMM_8FOLD);
            }
            else if (symmMirrorDiagBwd.isSelected())
            {
                result.setSymmetry(Properties.SYMM_MIRROR_DIAG_BWD);
            }
            else if (symmMirrorDiagFwd.isSelected())
            {
                result.setSymmetry(Properties.SYMM_MIRROR_DIAG_FWD);
            }
            else if (symmMirrorHorz.isSelected())
            {
                result.setSymmetry(Properties.SYMM_MIRROR_HORZ);
            }
            else if (symmMirrorVert.isSelected())
            {
                result.setSymmetry(Properties.SYMM_MIRROR_VERT);
            }
            else if (symmRot180.isSelected())
            {
                result.setSymmetry(Properties.SYMM_ROT_180);
            }
            else if (symmRot90.isSelected())
            {
                result.setSymmetry(Properties.SYMM_ROT_90);
            }
            else
            {
                result.setSymmetry(Properties.SYMM_NONE);
            }

            result.setTileGen(tileAfterYes.isSelected());
            result.setTileGenShiftRight(((Integer)tileAfterShiftRight.getValue()).intValue());
            result.setTileGenShiftDown(((Integer)tileAfterShiftDown.getValue()).intValue());
            result.setTileHorz(tileRightYes.isSelected());
            result.setTileHorzShiftDown(((Integer)tileRightShiftDown.getValue()).intValue());
            result.setTileHorzShiftFuture(((Integer)tileRightShiftFuture.getValue()).intValue());
            result.setTileVert(tileBelowYes.isSelected());
            result.setTileVertShiftRight(((Integer)tileBelowShiftRight.getValue()).intValue());
            result.setTileVertShiftFuture(((Integer)tileBelowShiftFuture.getValue()).intValue());

            if (xlatNone.isSelected())
            {
                result.setTranslation(Properties.XLAT_NONE);
            }
            else if (xlatHorz.isSelected())
            {
                result.setTranslation(Properties.XLAT_FLIP_HORZ);
            }
            else if (xlatVert.isSelected())
            {
                result.setTranslation(Properties.XLAT_FLIP_VERT);
            }
            else if (xlatDiagFwd.isSelected())
            {
                result.setTranslation(Properties.XLAT_FLIP_DIAG_FWD);
            }
            else if (xlatDiagBwd.isSelected())
            {
                result.setTranslation(Properties.XLAT_FLIP_DIAG_BWD);
            }
            else if (xlatRot90.isSelected())
            {
                result.setTranslation(Properties.XLAT_ROT_90);
            }
            else if (xlatRot180.isSelected())
            {
                result.setTranslation(Properties.XLAT_ROT_180);
            }
            else if (xlatRot270.isSelected())
            {
                result.setTranslation(Properties.XLAT_ROT_270);
            }
            else if (xlatGlideDiagFwd.isSelected())
            {
                result.setTranslation(Properties.XLAT_GLIDE_DIAG_FWD);
            }
            else if (xlatGlideDiagBwd.isSelected())
            {
                result.setTranslation(Properties.XLAT_GLIDE_DIAG_BWD);
            }
            else
            {
                result.setTranslation(Properties.XLAT_NONE);
            }

            result.setRuleBirth(0, birth0.isSelected());
            result.setRuleBirth(1, birth1.isSelected());
            result.setRuleBirth(2, birth2.isSelected());
            result.setRuleBirth(3, birth3.isSelected());
            result.setRuleBirth(4, birth4.isSelected());
            result.setRuleBirth(5, birth5.isSelected());
            result.setRuleBirth(6, birth6.isSelected());
            result.setRuleBirth(7, birth7.isSelected());
            result.setRuleBirth(8, birth8.isSelected());
            result.setRuleSurvival(0, survival0.isSelected());
            result.setRuleSurvival(1, survival1.isSelected());
            result.setRuleSurvival(2, survival2.isSelected());
            result.setRuleSurvival(3, survival3.isSelected());
            result.setRuleSurvival(4, survival4.isSelected());
            result.setRuleSurvival(5, survival5.isSelected());
            result.setRuleSurvival(6, survival6.isSelected());
            result.setRuleSurvival(7, survival7.isSelected());
            result.setRuleSurvival(8, survival8.isSelected());

            String error = result.validate();
            if (null == error)
            {
                setVisible(false);
            }
            else
            {
                JOptionPane.showMessageDialog(this,
                        error,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
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
    public PropertiesDialog(Frame owner, Properties props, boolean editable, Properties result)
    {
        super(owner);
        this.result = result;
        initialize();
        columns.setValue(new Integer(props.getColumns()));
        //((JSpinner.DefaultEditor)columns.getEditor()).getTextField().selectAll();
        rows.setValue(new Integer(props.getRows()));
        generations.setValue(new Integer(props.getGenerations()));
        getPeriod1().setValue(new Integer(props.getPeriod(1)));
        getPeriod2().setValue(new Integer(props.getPeriod(2)));
        getPeriod3().setValue(new Integer(props.getPeriod(3)));
        getPeriod4().setValue(new Integer(props.getPeriod(4)));
        getPeriod5().setValue(new Integer(props.getPeriod(5)));
        getPeriod6().setValue(new Integer(props.getPeriod(6)));
        int symmetry = props.getSymmetry();
        symmNone.setSelected(symmetry == Properties.SYMM_NONE);
        symm4fold.setSelected(symmetry == Properties.SYMM_4FOLD);
        symm4foldDiag.setSelected(symmetry == Properties.SYMM_4FOLD_DIAG);
        symm8fold.setSelected(symmetry == Properties.SYMM_8FOLD);
        symmMirrorDiagBwd.setSelected(symmetry == Properties.SYMM_MIRROR_DIAG_BWD);
        symmMirrorDiagFwd.setSelected(symmetry == Properties.SYMM_MIRROR_DIAG_FWD);
        symmMirrorHorz.setSelected(symmetry == Properties.SYMM_MIRROR_HORZ);
        symmMirrorVert.setSelected(symmetry == Properties.SYMM_MIRROR_VERT);
        symmRot180.setSelected(symmetry == Properties.SYMM_ROT_180);
        symmRot90.setSelected(symmetry == Properties.SYMM_ROT_90);
        ButtonGroup group = new ButtonGroup();
        group.add(symmNone);
        group.add(symm4fold);
        group.add(symm4foldDiag);
        group.add(symm8fold);
        group.add(symmMirrorDiagBwd);
        group.add(symmMirrorDiagFwd);
        group.add(symmMirrorHorz);
        group.add(symmMirrorVert);
        group.add(symmRot180);
        group.add(symmRot90);
        outerSpaceUnset.setSelected(props.isOuterSpaceUnset());
        outerSpaceSet.setSelected(!props.isOuterSpaceUnset());
        group = new ButtonGroup();
        group.add(outerSpaceUnset);
        group.add(outerSpaceSet);
        tileAfterNo.setSelected(!props.isTileGen());
        tileAfterYes.setSelected(props.isTileGen());
        group = new ButtonGroup();
        group.add(tileAfterNo);
        group.add(tileAfterYes);
        tileAfterShiftRight.setValue(new Integer(props.getTileGenShiftRight()));
        tileAfterShiftDown.setValue(new Integer(props.getTileGenShiftDown()));
        tileRightNo.setSelected(!props.isTileHorz());
        tileRightYes.setSelected(props.isTileHorz());
        group = new ButtonGroup();
        group.add(tileRightNo);
        group.add(tileRightYes);
        tileRightShiftDown.setValue(new Integer(props.getTileHorzShiftDown()));
        tileRightShiftFuture.setValue(new Integer(props.getTileHorzShiftFuture()));
        tileBelowNo.setSelected(!props.isTileVert());
        tileBelowYes.setSelected(props.isTileVert());
        group = new ButtonGroup();
        group.add(tileBelowNo);
        group.add(tileBelowYes);
        tileBelowShiftRight.setValue(new Integer(props.getTileVertShiftRight()));
        tileBelowShiftFuture.setValue(new Integer(props.getTileVertShiftFuture()));
        xlatNone.setSelected(props.getTranslation() == Properties.XLAT_NONE);
        xlatHorz.setSelected(props.getTranslation() == Properties.XLAT_FLIP_HORZ);
        xlatVert.setSelected(props.getTranslation() == Properties.XLAT_FLIP_VERT);
        xlatDiagFwd.setSelected(props.getTranslation() == Properties.XLAT_FLIP_DIAG_FWD);
        xlatDiagBwd.setSelected(props.getTranslation() == Properties.XLAT_FLIP_DIAG_BWD);
        xlatRot90.setSelected(props.getTranslation() == Properties.XLAT_ROT_90);
        xlatRot180.setSelected(props.getTranslation() == Properties.XLAT_ROT_180);
        xlatRot270.setSelected(props.getTranslation() == Properties.XLAT_ROT_270);
        xlatGlideDiagFwd.setSelected(props.getTranslation() == Properties.XLAT_GLIDE_DIAG_FWD);
        xlatGlideDiagBwd.setSelected(props.getTranslation() == Properties.XLAT_GLIDE_DIAG_BWD);
        group = new ButtonGroup();
        group.add(xlatNone);
        group.add(xlatHorz);
        group.add(xlatVert);
        group.add(xlatDiagFwd);
        group.add(xlatDiagBwd);
        group.add(xlatRot90);
        group.add(xlatRot180);
        group.add(xlatRot270);
        group.add(xlatGlideDiagFwd);
        group.add(xlatGlideDiagBwd);
        birth0.setSelected(props.getRuleBirth(0));
        birth1.setSelected(props.getRuleBirth(1));
        birth2.setSelected(props.getRuleBirth(2));
        birth3.setSelected(props.getRuleBirth(3));
        birth4.setSelected(props.getRuleBirth(4));
        birth5.setSelected(props.getRuleBirth(5));
        birth6.setSelected(props.getRuleBirth(6));
        birth7.setSelected(props.getRuleBirth(7));
        birth8.setSelected(props.getRuleBirth(8));
        survival0.setSelected(props.getRuleSurvival(0));
        survival1.setSelected(props.getRuleSurvival(1));
        survival2.setSelected(props.getRuleSurvival(2));
        survival3.setSelected(props.getRuleSurvival(3));
        survival4.setSelected(props.getRuleSurvival(4));
        survival5.setSelected(props.getRuleSurvival(5));
        survival6.setSelected(props.getRuleSurvival(6));
        survival7.setSelected(props.getRuleSurvival(7));
        survival8.setSelected(props.getRuleSurvival(8));
        
        okButton.setEnabled(editable);
        symmNone.setEnabled(editable);
        symmMirrorHorz.setEnabled(editable);
        symmMirrorVert.setEnabled(editable);
        symmMirrorDiagFwd.setEnabled(editable);
        symmMirrorDiagBwd.setEnabled(editable);
        symmRot180.setEnabled(editable);
        symmRot90.setEnabled(editable);
        symm4fold.setEnabled(editable);
        symm4foldDiag.setEnabled(editable);
        symm8fold.setEnabled(editable);
        generations.setEnabled(editable);
        rows.setEnabled(editable);
        columns.setEnabled(editable);
        period1.setEnabled(editable);
        period2.setEnabled(editable);
        period3.setEnabled(editable);
        period4.setEnabled(editable);
        period5.setEnabled(editable);
        period6.setEnabled(editable);
        outerSpaceUnset.setEnabled(editable);
        outerSpaceSet.setEnabled(editable);
        xlatNone.setEnabled(editable);
        xlatHorz.setEnabled(editable);
        xlatVert.setEnabled(editable);
        xlatDiagFwd.setEnabled(editable);
        xlatDiagBwd.setEnabled(editable);
        xlatRot90.setEnabled(editable);
        xlatRot180.setEnabled(editable);
        xlatRot270.setEnabled(editable);
        xlatGlideDiagFwd.setEnabled(editable);
        xlatGlideDiagBwd.setEnabled(editable);
        tileRightNo.setEnabled(editable);
        tileRightYes.setEnabled(editable);
        tileRightShiftDown.setEnabled(editable);
        tileRightShiftFuture.setEnabled(editable);
        tileAfterNo.setEnabled(editable);
        tileAfterYes.setEnabled(editable);
        tileAfterShiftDown.setEnabled(editable);
        tileAfterShiftRight.setEnabled(editable);
        tileBelowNo.setEnabled(editable);
        tileBelowYes.setEnabled(editable);
        tileBelowShiftFuture.setEnabled(editable);
        tileBelowShiftRight.setEnabled(editable);
        birth0.setEnabled(editable);
        birth1.setEnabled(editable);
        birth2.setEnabled(editable);
        birth3.setEnabled(editable);
        birth4.setEnabled(editable);
        birth5.setEnabled(editable);
        birth6.setEnabled(editable);
        birth7.setEnabled(editable);
        birth8.setEnabled(editable);
        survival0.setEnabled(editable);
        survival1.setEnabled(editable);
        survival2.setEnabled(editable);
        survival3.setEnabled(editable);
        survival4.setEnabled(editable);
        survival5.setEnabled(editable);
        survival6.setEnabled(editable);
        survival7.setEnabled(editable);
        survival8.setEnabled(editable);
        conwayLife.setEnabled(editable);
        highLife.setEnabled(editable);
        dayNight.setEnabled(editable);
        diamoeba.setEnabled(editable);
        seeds.setEnabled(editable);
        life34.setEnabled(editable);
        setOuterSpaceText();
        
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
        this.setTitle("Properties");
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
            jContentPane.add(getJTabbedPane(), BorderLayout.NORTH);
            jContentPane.add(getJPanel(), BorderLayout.SOUTH);
        }
        return jContentPane;
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
            jTabbedPane.addTab("Edit field", null, getEditPanel(), null);
            jTabbedPane.addTab("Tiling/translation", null, getTilingPanel(), null);
            jTabbedPane.addTab("Rule", null, getRulePanel(), null);
        }
        return jTabbedPane;
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
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(FlowLayout.RIGHT);
            jPanel = new JPanel();
            jPanel.setLayout(flowLayout);
            jPanel.add(getJPanel1(), null);
        }
        return jPanel;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getJButton()
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
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getJButton1()
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
     * This method initializes period4
     *
     * @return javax.swing.JPanel
     */
    private JPanel getEditPanel()
    {
        if (editPanel == null)
        {
            editPanel = new JPanel();
            editPanel.setLayout(new GridBagLayout());

            GridBagConstraints gridBagConstraints00 = new GridBagConstraints();
            gridBagConstraints00.gridx = 0;
            gridBagConstraints00.gridy = 0;
            gridBagConstraints00.fill = GridBagConstraints.BOTH;
            gridBagConstraints00.weightx = 1;
            gridBagConstraints00.weighty = 1;
            editPanel.add(getDimensionsPanel(), gridBagConstraints00);

            GridBagConstraints gridBagConstraints01 = new GridBagConstraints();
            gridBagConstraints01.gridx = 0;
            gridBagConstraints01.gridy = 1;
            gridBagConstraints01.fill = GridBagConstraints.BOTH;
            gridBagConstraints01.weightx = 1;
            gridBagConstraints01.weighty = 1;
            editPanel.add(getPeriodPanel(), gridBagConstraints01);
            
            GridBagConstraints gridBagConstraints02 = new GridBagConstraints();
            gridBagConstraints02.gridx = 0;
            gridBagConstraints02.gridy = 2;
            gridBagConstraints02.fill = GridBagConstraints.BOTH;
            gridBagConstraints02.weightx = 1;
            gridBagConstraints02.weighty = 1;
            editPanel.add(getOuterSpacePanel(), gridBagConstraints02);
        
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 1;
            gridBagConstraints10.gridy = 0;
            gridBagConstraints10.fill = GridBagConstraints.BOTH;
            gridBagConstraints10.gridheight = 3;
            gridBagConstraints10.weightx = 1;
            gridBagConstraints10.weighty = 1;
            editPanel.add(getSymmetryPanel(), gridBagConstraints10);
        }
        return editPanel;
    }

    /**
     * This method initializes columns
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getColumns()
    {
        if (columns == null)
        {
            columns = new JSpinner();
            columns.setPreferredSize(new Dimension(50, 20));
            columns.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
        }
        return columns;
    }

    /**
     * This method initializes rows
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getRows()
    {
        if (rows == null)
        {
            rows = new JSpinner();
            rows.setPreferredSize(new Dimension(50, 20));
            rows.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
        }
        return rows;
    }

    /**
     * This method initializes generations
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getGenerations()
    {
        if (generations == null)
        {
            generations = new JSpinner();
            generations.setPreferredSize(new Dimension(50, 20));
            generations.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
        }
        return generations;
    }

    /**
     * This method initializes period1
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getPeriod1()
    {
        if (period1 == null)
        {
            period1 = new JSpinner();
            period1.setPreferredSize(new Dimension(50, 20));
            period1.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
            ((JSpinner.DefaultEditor)period1.getEditor()).getTextField().setBackground(ImageCache.normalColors[1]);
        }
        return period1;
    }

    /**
     * This method initializes period2
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getPeriod2()
    {
        if (period2 == null)
        {
            period2 = new JSpinner();
            period2.setPreferredSize(new Dimension(50, 20));
            period2.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
            ((JSpinner.DefaultEditor)period2.getEditor()).getTextField().setBackground(ImageCache.normalColors[2]);
        }
        return period2;
    }

    /**
     * This method initializes period3
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getPeriod3()
    {
        if (period3 == null)
        {
            period3 = new JSpinner();
            period3.setPreferredSize(new Dimension(50, 20));
            period3.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
            ((JSpinner.DefaultEditor)period3.getEditor()).getTextField().setBackground(ImageCache.normalColors[3]);
        }
        return period3;
    }

    /**
     * This method initializes jSpinner33
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getPeriod4()
    {
        if (period4 == null)
        {
            period4 = new JSpinner();
            period4.setPreferredSize(new Dimension(50, 20));
            period4.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
            ((JSpinner.DefaultEditor)period4.getEditor()).getTextField().setBackground(ImageCache.normalColors[4]);
        }
        return period4;
    }

    /**
     * This method initializes period5
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getPeriod5()
    {
        if (period5 == null)
        {
            period5 = new JSpinner();
            period5.setPreferredSize(new Dimension(50, 20));
            period5.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
            ((JSpinner.DefaultEditor)period5.getEditor()).getTextField().setBackground(ImageCache.normalColors[5]);
        }
        return period5;
    }

    /**
     * This method initializes period6
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getPeriod6()
    {
        if (period6 == null)
        {
            period6 = new JSpinner();
            period6.setPreferredSize(new Dimension(50, 20));
            period6.setModel(new SpinnerNumberModel(1, 1, 1000, 1));
            ((JSpinner.DefaultEditor)period6.getEditor()).getTextField().setBackground(ImageCache.normalColors[6]);
        }
        return period6;
    }

    /**
     * This method initializes editFieldPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getDimensionsPanel()
    {
        if (dimensionsPanel == null)
        {
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.anchor = GridBagConstraints.EAST;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new Insets(3, 3, 3, 3);
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.anchor = GridBagConstraints.EAST;
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 1;
            gridBagConstraints4.insets = new Insets(3, 3, 3, 3);
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.gridx = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.anchor = GridBagConstraints.EAST;
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 2;
            gridBagConstraints5.insets = new Insets(3, 3, 3, 3);
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints3.gridy = 2;
            gridBagConstraints3.gridx = 1;
            dimensionsPanel = new JPanel();
            dimensionsPanel.setLayout(new GridBagLayout());
            dimensionsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Dimensions", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            dimensionsPanel.add(getGenerations(), gridBagConstraints3);
            dimensionsPanel.add(new JLabel("Generations (depth)"), gridBagConstraints5);
            dimensionsPanel.add(getRows(), gridBagConstraints2);
            dimensionsPanel.add(new JLabel("Rows (height)"), gridBagConstraints4);
            dimensionsPanel.add(getColumns(), gridBagConstraints1);
            dimensionsPanel.add(new JLabel("Columns (width)"), gridBagConstraints);
        }
        return dimensionsPanel;
    }

    private JPanel getOuterSpacePanel()
    {
        if (outerSpacePanel == null)
        {
            outerSpacePanel = new JPanel();
            outerSpacePanel.setLayout(new GridBagLayout());
            outerSpacePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Outer space cells", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            GridBagConstraints gridBagConstraints00 = new GridBagConstraints();
            gridBagConstraints00.gridx = 0;
            gridBagConstraints00.gridy = 0;
            gridBagConstraints00.gridheight = 2;
            gridBagConstraints00.weightx = 1;
            outerSpacePanel.add(new JLabel(""), gridBagConstraints00);

            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 1;
            gridBagConstraints10.gridy = 0;
            gridBagConstraints10.weightx = 0;
            gridBagConstraints10.anchor = GridBagConstraints.WEST;
            outerSpacePanel.add(getOuterSpaceSet(), gridBagConstraints10);

            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.gridy = 1;
            gridBagConstraints11.weightx = 0;
            gridBagConstraints11.anchor = GridBagConstraints.WEST;
            outerSpacePanel.add(getOuterSpaceUnset(), gridBagConstraints11);

            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.gridx = 2;
            gridBagConstraints20.gridy = 0;
            gridBagConstraints20.gridheight = 2;
            gridBagConstraints20.weightx = 1;
            outerSpacePanel.add(new JLabel(""), gridBagConstraints20);
        }
        return outerSpacePanel;
    }
    /**
     * This method initializes periodPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getPeriodPanel()
    {
        if (periodPanel == null)
        {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints6.gridy = 1;
            gridBagConstraints6.gridx = 0;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.insets = new Insets(3, 3, 3, 9);
            gridBagConstraints7.gridy = 1;
            gridBagConstraints7.gridx = 1;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints13.gridy = 1;
            gridBagConstraints13.gridx = 3;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints8.gridy = 1;
            gridBagConstraints8.gridx = 4;
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints14.gridy = 2;
            gridBagConstraints14.gridx = 0;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.insets = new Insets(3, 3, 3, 9);
            gridBagConstraints9.gridy = 2;
            gridBagConstraints9.gridx = 1;
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints15.gridy = 2;
            gridBagConstraints15.gridx = 3;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints10.gridy = 2;
            gridBagConstraints10.gridx = 4;
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints16.gridy = 3;
            gridBagConstraints16.gridx = 0;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.insets = new Insets(3, 3, 3, 9);
            gridBagConstraints11.gridy = 3;
            gridBagConstraints11.gridx = 1;
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints17.gridy = 3;
            gridBagConstraints17.gridx = 3;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints12.gridy = 3;
            gridBagConstraints12.gridx = 4;
            periodPanel = new JPanel();
            periodPanel.setLayout(new GridBagLayout());
            periodPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Subperiods", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            periodPanel.add(getPeriod6(), gridBagConstraints12);
            periodPanel.add(new JLabel("6."), gridBagConstraints17);
            periodPanel.add(getPeriod5(), gridBagConstraints11);
            periodPanel.add(new JLabel("5."), gridBagConstraints16);
            periodPanel.add(getPeriod4(), gridBagConstraints10);
            periodPanel.add(new JLabel("4."), gridBagConstraints15);
            periodPanel.add(getPeriod3(), gridBagConstraints9);
            periodPanel.add(new JLabel("3."), gridBagConstraints14);
            periodPanel.add(getPeriod2(), gridBagConstraints8);
            periodPanel.add(new JLabel("2."), gridBagConstraints13);
            periodPanel.add(getPeriod1(), gridBagConstraints7);
            periodPanel.add(new JLabel("1."), gridBagConstraints6);
        }
        return periodPanel;
    }

    /**
     * This method initializes tilingPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getTilingPanel()
    {
        if (tilingPanel == null)
        {
            tilingPanel = new JPanel();
            tilingPanel.setLayout(new GridBagLayout());

            GridBagConstraints gridBagConstraints52 = new GridBagConstraints();
            gridBagConstraints52.gridheight = 3;
            gridBagConstraints52.gridx = 1;
            gridBagConstraints52.gridy = 0;
            gridBagConstraints52.gridheight = 3;
            gridBagConstraints52.weightx = 0;
            gridBagConstraints52.weighty = 1;
            gridBagConstraints52.fill = GridBagConstraints.BOTH;
            tilingPanel.add(getTranslationPanel(), gridBagConstraints52);

            GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
            gridBagConstraints41.fill = GridBagConstraints.BOTH;
            gridBagConstraints41.gridx = 0;
            gridBagConstraints41.gridy = 0;
            gridBagConstraints41.weightx = 1;
            gridBagConstraints41.weighty = 1;
            tilingPanel.add(getGenerationTilePanel(), gridBagConstraints41);

            GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
            gridBagConstraints33.fill = GridBagConstraints.BOTH;
            gridBagConstraints33.gridx = 0;
            gridBagConstraints33.gridy = 1;
            gridBagConstraints33.weightx = 1;
            gridBagConstraints33.weighty = 1;
            tilingPanel.add(getHorizontalTilePanel(), gridBagConstraints33);

            GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
            gridBagConstraints40.fill = GridBagConstraints.BOTH;
            gridBagConstraints40.gridx = 0;
            gridBagConstraints40.gridy = 2;
            gridBagConstraints40.weightx = 1;
            gridBagConstraints40.weighty = 1;
            tilingPanel.add(getVerticalTilePanel(), gridBagConstraints40);
        }
        return tilingPanel;
    }

    /**
     * This method initializes horizontalTilePanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getHorizontalTilePanel()
    {
        if (horizontalTilePanel == null)
        {
            horizontalTilePanel = new JPanel();
            horizontalTilePanel.setLayout(new GridBagLayout());
            horizontalTilePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Behind the right edge is...", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

            GridBagConstraints gridBagConstraints00 = new GridBagConstraints();
            gridBagConstraints00.gridx = 0;
            gridBagConstraints00.gridy = 0;
            gridBagConstraints00.gridwidth = 3;
            gridBagConstraints00.weightx = 1;
            gridBagConstraints00.anchor = GridBagConstraints.WEST;
            gridBagConstraints00.fill = GridBagConstraints.HORIZONTAL;
            horizontalTilePanel.add(getTileRightNo(), gridBagConstraints00);

            GridBagConstraints gridBagConstraints01 = new GridBagConstraints();
            gridBagConstraints01.gridx = 0;
            gridBagConstraints01.gridy = 1;
            gridBagConstraints01.anchor = GridBagConstraints.WEST;
            horizontalTilePanel.add(getTileRightYes(), gridBagConstraints01);

            GridBagConstraints gridBagConstraints02 = new GridBagConstraints();
            gridBagConstraints02.gridx = 0;
            gridBagConstraints02.gridy = 2;
            gridBagConstraints02.anchor = GridBagConstraints.WEST;
            gridBagConstraints02.fill = GridBagConstraints.VERTICAL;
            horizontalTilePanel.add(new JLabel(""), gridBagConstraints02);

            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.gridy = 1;
            gridBagConstraints11.insets = new Insets(3, 3, 3, 3);
            horizontalTilePanel.add(getTileRightShiftDown(), gridBagConstraints11);

            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 2;
            gridBagConstraints21.gridy = 1;
            gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints21.anchor = GridBagConstraints.WEST;
            gridBagConstraints21.weightx = 1;
            horizontalTilePanel.add(new JLabel("cells down, and"), gridBagConstraints21);

            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 1;
            gridBagConstraints12.gridy = 2;
            gridBagConstraints12.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints12.anchor = GridBagConstraints.WEST;
            gridBagConstraints12.fill = GridBagConstraints.VERTICAL;
            horizontalTilePanel.add(getTileRightShiftFuture(), gridBagConstraints12);

            GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
            gridBagConstraints22.gridx = 2;
            gridBagConstraints22.gridy = 2;
            gridBagConstraints22.anchor = GridBagConstraints.WEST;
            gridBagConstraints22.fill = GridBagConstraints.BOTH;
            gridBagConstraints22.weightx = 1;
            horizontalTilePanel.add(new JLabel("generations to future"), gridBagConstraints22);
        }
        return horizontalTilePanel;
    }

    /**
     * This method initializes tileRightNo
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getTileRightNo()
    {
        if (tileRightNo == null)
        {
            tileRightNo = new JRadioButton();
            tileRightNo.setText("Outer space (as well as behind left edge)");
        }
        return tileRightNo;
    }

    /**
     * This method initializes tileRightYes
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getTileRightYes()
    {
        if (tileRightYes == null)
        {
            tileRightYes = new JRadioButton();
            tileRightYes.setText("Left edge, shifted");
        }
        return tileRightYes;
    }

    /**
     * This method initializes tileRightShiftDown
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getTileRightShiftDown()
    {
        if (tileRightShiftDown == null)
        {
            tileRightShiftDown = new JSpinner();
            tileRightShiftDown.setPreferredSize(new Dimension(50, 20));
            tileRightShiftDown.setModel(new SpinnerNumberModel(0, -1000, 1000, 1));
        }
        return tileRightShiftDown;
    }

    /**
     * This method initializes tileRightShiftFuture
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getTileRightShiftFuture()
    {
        if (tileRightShiftFuture == null)
        {
            tileRightShiftFuture = new JSpinner();
            tileRightShiftFuture.setPreferredSize(new Dimension(50, 20));
            tileRightShiftFuture.setModel(new SpinnerNumberModel(0, -1000, 1000, 1));
        }
        return tileRightShiftFuture;
    }

    /**
     * This method initializes verticalTilePanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getVerticalTilePanel()
    {
        if (verticalTilePanel == null)
        {
            verticalTilePanel = new JPanel();
            verticalTilePanel.setLayout(new GridBagLayout());
            verticalTilePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Below the bottom edge is...", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

            GridBagConstraints gridBagConstraints00 = new GridBagConstraints();
            gridBagConstraints00.anchor = GridBagConstraints.WEST;
            gridBagConstraints00.gridx = 0;
            gridBagConstraints00.gridy = 0;
            gridBagConstraints00.gridwidth = 3;
            gridBagConstraints00.weightx = 1;
            verticalTilePanel.add(getTileBelowNo(), gridBagConstraints00);

            GridBagConstraints gridBagConstraints01 = new GridBagConstraints();
            gridBagConstraints01.anchor = GridBagConstraints.WEST;
            gridBagConstraints01.gridx = 0;
            gridBagConstraints01.gridy = 1;
            verticalTilePanel.add(getTileBelowYes(), gridBagConstraints01);
            
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.gridy = 1;
            verticalTilePanel.add(getTileBelowShiftRight(), gridBagConstraints11);
            
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.anchor = GridBagConstraints.WEST;
            gridBagConstraints21.gridx = 2;
            gridBagConstraints21.gridy = 1;
            gridBagConstraints21.weightx = 1;
            verticalTilePanel.add(new JLabel("cells to the right, and"), gridBagConstraints21);
            
            GridBagConstraints gridBagConstrinnts12 = new GridBagConstraints();
            gridBagConstrinnts12.insets = new Insets(3, 3, 3, 3);
            gridBagConstrinnts12.gridx = 1;
            gridBagConstrinnts12.gridy = 2;
            verticalTilePanel.add(getTileBelowShiftFuture(), gridBagConstrinnts12);
            
            GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
            gridBagConstraints22.anchor = GridBagConstraints.WEST;
            gridBagConstraints22.gridx = 2;
            gridBagConstraints22.gridy = 2;
            gridBagConstraints22.weightx = 1;
            verticalTilePanel.add(new JLabel("generations to future"), gridBagConstraints22);
        }
        return verticalTilePanel;
    }

    /**
     * This method initializes tileBelowNo
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getTileBelowNo()
    {
        if (tileBelowNo == null)
        {
            tileBelowNo = new JRadioButton();
            tileBelowNo.setText("Outer space (as well as above top edge)");
        }
        return tileBelowNo;
    }

    /**
     * This method initializes tileBelowYes
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getTileBelowYes()
    {
        if (tileBelowYes == null)
        {
            tileBelowYes = new JRadioButton();
            tileBelowYes.setText("Top edge, shifted");
        }
        return tileBelowYes;
    }

    /**
     * This method initializes tileBelowShiftRight
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getTileBelowShiftRight()
    {
        if (tileBelowShiftRight == null)
        {
            tileBelowShiftRight = new JSpinner();
            tileBelowShiftRight.setPreferredSize(new Dimension(50, 20));
            tileBelowShiftRight.setModel(new SpinnerNumberModel(0, -1000, 1000, 1));
        }
        return tileBelowShiftRight;
    }

    /**
     * This method initializes tileBelowShiftFuture
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getTileBelowShiftFuture()
    {
        if (tileBelowShiftFuture == null)
        {
            tileBelowShiftFuture = new JSpinner();
            tileBelowShiftFuture.setPreferredSize(new Dimension(50, 20));
            tileBelowShiftFuture.setModel(new SpinnerNumberModel(0, -1000, 1000, 1));
        }
        return tileBelowShiftFuture;
    }

    /**
     * This method initializes generationTilePanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getGenerationTilePanel()
    {
        if (generationTilePanel == null)
        {
            generationTilePanel = new JPanel();
            generationTilePanel.setLayout(new GridBagLayout());
            generationTilePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "After the last generation is...", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

            GridBagConstraints gridBagConstraints00 = new GridBagConstraints();
            gridBagConstraints00.anchor = GridBagConstraints.WEST;
            gridBagConstraints00.gridx = 0;
            gridBagConstraints00.gridy = 0;
            gridBagConstraints00.gridwidth = 3;
            gridBagConstraints00.weightx = 1;
            generationTilePanel.add(getTileAfterNo(), gridBagConstraints00);

            GridBagConstraints gridBagConstraints01 = new GridBagConstraints();
            gridBagConstraints01.anchor = GridBagConstraints.WEST;
            gridBagConstraints01.gridx = 0;
            gridBagConstraints01.gridy = 1;
            generationTilePanel.add(getTileAfterYes(), gridBagConstraints01);

            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints11.gridx = 1;
            gridBagConstraints11.gridy = 1;
            generationTilePanel.add(getTileAfterShiftRight(), gridBagConstraints11);

            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.anchor = GridBagConstraints.WEST;
            gridBagConstraints21.gridx = 2;
            gridBagConstraints21.gridy = 1;
            gridBagConstraints21.weightx = 1;
            generationTilePanel.add(new JLabel("cells to the right, and"), gridBagConstraints21);

            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.insets = new Insets(3, 3, 3, 3);
            gridBagConstraints12.gridx = 1;
            gridBagConstraints12.gridy = 2;
            generationTilePanel.add(getTileAfterShiftDown(), gridBagConstraints12);

            GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
            gridBagConstraints22.anchor = GridBagConstraints.WEST;
            gridBagConstraints22.gridx = 2;
            gridBagConstraints22.gridy = 2;
            gridBagConstraints22.weightx = 1;
            generationTilePanel.add(new JLabel("cells down"), gridBagConstraints22);
        }
        return generationTilePanel;
    }

    /**
     * This method initializes tileAfterNo
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getTileAfterNo()
    {
        if (tileAfterNo == null)
        {
            tileAfterNo = new JRadioButton();
            tileAfterNo.setText("Unknown");
        }
        return tileAfterNo;
    }

    private JRadioButton getOuterSpaceUnset()
    {
        if (outerSpaceUnset == null)
        {
            outerSpaceUnset = new JRadioButton();
            outerSpaceUnset.setText("Unset");
        }
        return outerSpaceUnset;
    }
    
    private void setOuterSpaceText()
    {
        if (getBirth0().isSelected())
        {
            if (getSurvival8().isSelected())
            {
                outerSpaceSet.setText("Rule-based (ON)");
            }
            else
            {
                outerSpaceSet.setText("Rule-based (OFF/ON)");
            }
        }
        else
        {
            outerSpaceSet.setText("Rule-based (OFF)");
        }
    }

    private JRadioButton getOuterSpaceSet()
    {
        if (outerSpaceSet == null)
        {
            outerSpaceSet = new JRadioButton();
            setOuterSpaceText();
        }
        return outerSpaceSet;
    }

    /**
     * This method initializes tileAfterYes
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getTileAfterYes()
    {
        if (tileAfterYes == null)
        {
            tileAfterYes = new JRadioButton();
            tileAfterYes.setText("Generation 0, shifted");
        }
        return tileAfterYes;
    }

    /**
     * This method initializes tileAfterShiftRight
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getTileAfterShiftRight()
    {
        if (tileAfterShiftRight == null)
        {
            tileAfterShiftRight = new JSpinner();
            tileAfterShiftRight.setPreferredSize(new Dimension(50, 20));
            tileAfterShiftRight.setModel(new SpinnerNumberModel(0, -1000, 1000, 1));
        }
        return tileAfterShiftRight;
    }

    /**
     * This method initializes tileAfterShiftDown
     *
     * @return javax.swing.JSpinner
     */
    private JSpinner getTileAfterShiftDown()
    {
        if (tileAfterShiftDown == null)
        {
            tileAfterShiftDown = new JSpinner();
            tileAfterShiftDown.setPreferredSize(new Dimension(50, 20));
            tileAfterShiftDown.setModel(new SpinnerNumberModel(0, -1000, 1000, 1));
        }
        return tileAfterShiftDown;
    }

    /**
     * This method initializes xlatNone
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getXlatNone()
    {
        if (xlatNone == null)
        {
            xlatNone = new JRadioButton();
            xlatNone.setText("None");
            xlatNone.setIcon(new ImageIcon(getClass().getResource("/images/xlat_1.gif")));
            xlatNone.setMnemonic(KeyEvent.VK_N);
            xlatNone.setSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_1_sel.gif")));
            xlatNone.setDisabledIcon(new ImageIcon(getClass().getResource("/images/xlat_1_dis.gif")));
            xlatNone.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_1_sel_dis.gif")));
        }
        return xlatNone;
    }

    /**
     * This method initializes xlatHorz
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getXlatHorz()
    {
        if (xlatHorz == null)
        {
            xlatHorz = new JRadioButton();
            xlatHorz.setText("Flip horizontal");
            xlatHorz.setIcon(new ImageIcon(getClass().getResource("/images/xlat_5.gif")));
            xlatHorz.setMnemonic(KeyEvent.VK_H);
            xlatHorz.setSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_5_sel.gif")));
            xlatHorz.setDisabledIcon(new ImageIcon(getClass().getResource("/images/xlat_5_dis.gif")));
            xlatHorz.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_5_sel_dis.gif")));
        }
        return xlatHorz;
    }

    /**
     * This method initializes xlatVert
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getXlatVert()
    {
        if (xlatVert == null)
        {
            xlatVert = new JRadioButton();
            xlatVert.setText("Flip vertical");
            xlatVert.setIcon(new ImageIcon(getClass().getResource("/images/xlat_6.gif")));
            xlatVert.setMnemonic(KeyEvent.VK_V);
            xlatVert.setSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_6_sel.gif")));
            xlatVert.setDisabledIcon(new ImageIcon(getClass().getResource("/images/xlat_6_dis.gif")));
            xlatVert.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_6_sel_dis.gif")));
        }
        return xlatVert;
    }

    /**
     * This method initializes xlatDiagFwd
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getXlatDiagFwd()
    {
        if (xlatDiagFwd == null)
        {
            xlatDiagFwd = new JRadioButton();
            xlatDiagFwd.setText("Flip diagonal forward");
            xlatDiagFwd.setIcon(new ImageIcon(getClass().getResource("/images/xlat_8.gif")));
            xlatDiagFwd.setMnemonic(KeyEvent.VK_D);
            xlatDiagFwd.setSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_8_sel.gif")));
            xlatDiagFwd.setDisabledIcon(new ImageIcon(getClass().getResource("/images/xlat_8_dis.gif")));
            xlatDiagFwd.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_8_sel_dis.gif")));
        }
        return xlatDiagFwd;
    }

    /**
     * This method initializes xlatRot90
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getXlatRot90()
    {
        if (xlatRot90 == null)
        {
            xlatRot90 = new JRadioButton();
            xlatRot90.setText("Rotate 90");
            xlatRot90.setIcon(new ImageIcon(getClass().getResource("/images/xlat_2.gif")));
            xlatRot90.setMnemonic(KeyEvent.VK_9);
            xlatRot90.setSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_2_sel.gif")));
            xlatRot90.setDisabledIcon(new ImageIcon(getClass().getResource("/images/xlat_2_dis.gif")));
            xlatRot90.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_2_sel_dis.gif")));
        }
        return xlatRot90;
    }

    /**
     * This method initializes xlatRot180
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getXlatRot180()
    {
        if (xlatRot180 == null)
        {
            xlatRot180 = new JRadioButton();
            xlatRot180.setText("Rotate 180");
            xlatRot180.setIcon(new ImageIcon(getClass().getResource("/images/xlat_3.gif")));
            xlatRot180.setMnemonic(KeyEvent.VK_1);
            xlatRot180.setSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_3_sel.gif")));
            xlatRot180.setDisabledIcon(new ImageIcon(getClass().getResource("/images/xlat_3_dis.gif")));
            xlatRot180.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_3_sel_dis.gif")));
        }
        return xlatRot180;
    }

    /**
     * This method initializes xlatRot270
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getXlatRot270()
    {
        if (xlatRot270 == null)
        {
            xlatRot270 = new JRadioButton();
            xlatRot270.setText("Rotate 270");
            xlatRot270.setIcon(new ImageIcon(getClass().getResource("/images/xlat_4.gif")));
            xlatRot270.setMnemonic(KeyEvent.VK_2);
            xlatRot270.setSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_4_sel.gif")));
            xlatRot270.setDisabledIcon(new ImageIcon(getClass().getResource("/images/xlat_4_dis.gif")));
            xlatRot270.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_4_sel_dis.gif")));
        }
        return xlatRot270;
    }

    /**
     * This method initializes xlatDiagBwd
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getXlatDiagBwd()
    {
        if (xlatDiagBwd == null)
        {
            xlatDiagBwd = new JRadioButton();
            xlatDiagBwd.setText("Flip diagonal backward");
            xlatDiagBwd.setIcon(new ImageIcon(getClass().getResource("/images/xlat_7.gif")));
            xlatDiagBwd.setMnemonic(KeyEvent.VK_B);
            xlatDiagBwd.setSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_7_sel.gif")));
            xlatDiagBwd.setDisabledIcon(new ImageIcon(getClass().getResource("/images/xlat_7_dis.gif")));
            xlatDiagBwd.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_7_sel_dis.gif")));
        }
        return xlatDiagBwd;
    }

    /**
     * This method initializes xlatGlideDiagFwd
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getXlatGlideDiagFwd()
    {
        if (xlatGlideDiagFwd == null)
        {
            xlatGlideDiagFwd = new JRadioButton();
            xlatGlideDiagFwd.setText("Glide diagonal forward");
            xlatGlideDiagFwd.setIcon(new ImageIcon(getClass().getResource("/images/xlat_8.gif")));
            xlatGlideDiagFwd.setMnemonic(KeyEvent.VK_Q);
            xlatGlideDiagFwd.setSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_8_sel.gif")));
            xlatGlideDiagFwd.setDisabledIcon(new ImageIcon(getClass().getResource("/images/xlat_8_dis.gif")));
            xlatGlideDiagFwd.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_8_sel_dis.gif")));
        }
        return xlatGlideDiagFwd;
    }

    /**
     * This method initializes xlatGlideDiagBwd
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getXlatGlideDiagBwd()
    {
        if (xlatGlideDiagBwd == null)
        {
            xlatGlideDiagBwd = new JRadioButton();
            xlatGlideDiagBwd.setText("Glide diagonal backward");
            xlatGlideDiagBwd.setIcon(new ImageIcon(getClass().getResource("/images/xlat_7.gif")));
            xlatGlideDiagBwd.setMnemonic(KeyEvent.VK_X);
            xlatGlideDiagBwd.setSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_7_sel.gif")));
            xlatGlideDiagBwd.setDisabledIcon(new ImageIcon(getClass().getResource("/images/xlat_7_dis.gif")));
            xlatGlideDiagBwd.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/xlat_7_sel_dis.gif")));
        }
        return xlatGlideDiagBwd;
    }

    /**
     * This method initializes translationPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getTranslationPanel() {
        if (translationPanel == null) {
            GridBagConstraints gridBagConstraints49 = new GridBagConstraints();
            gridBagConstraints49.anchor = GridBagConstraints.WEST;
            gridBagConstraints49.gridy = 6;
            gridBagConstraints49.gridx = 0;
            GridBagConstraints gridBagConstraints48 = new GridBagConstraints();
            gridBagConstraints48.anchor = GridBagConstraints.WEST;
            gridBagConstraints48.gridy = 5;
            gridBagConstraints48.gridx = 0;
            GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
            gridBagConstraints51.anchor = GridBagConstraints.WEST;
            gridBagConstraints51.gridy = 4;
            gridBagConstraints51.gridx = 0;
            GridBagConstraints gridBagConstraints47 = new GridBagConstraints();
            gridBagConstraints47.anchor = GridBagConstraints.WEST;
            gridBagConstraints47.gridy = 3;
            gridBagConstraints47.gridx = 0;
            GridBagConstraints gridBagConstraints46 = new GridBagConstraints();
            gridBagConstraints46.anchor = GridBagConstraints.WEST;
            gridBagConstraints46.gridy = 2;
            gridBagConstraints46.gridx = 0;
            GridBagConstraints gridBagConstraints45 = new GridBagConstraints();
            gridBagConstraints45.anchor = GridBagConstraints.WEST;
            gridBagConstraints45.gridy = 1;
            gridBagConstraints45.gridx = 0;
            translationPanel = new JPanel();
            translationPanel.setLayout(new BoxLayout(getTranslationPanel(), BoxLayout.Y_AXIS));
            translationPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Translation after last gen.", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
            translationPanel.add(getXlatNone(), null);
            translationPanel.add(getXlatHorz(), null);
            translationPanel.add(getXlatVert(), null);
            translationPanel.add(getXlatDiagFwd(), null);
            translationPanel.add(getXlatDiagBwd(), null);
            translationPanel.add(getXlatRot90(), null);
            translationPanel.add(getXlatRot180(), null);
            translationPanel.add(getXlatRot270(), null);
            translationPanel.add(getXlatGlideDiagFwd(), null);
            translationPanel.add(getXlatGlideDiagBwd(), null);
        }
        return translationPanel;
    }

    /**
     * This method initializes symmetryPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getSymmetryPanel() {
        if (symmetryPanel == null) {
            GridLayout gridLayout2 = new GridLayout();
            gridLayout2.setRows(1);
            gridLayout2.setColumns(2);
            GridLayout gridLayout1 = new GridLayout();
            gridLayout1.setRows(1);
            symmetryPanel = new JPanel();
            symmetryPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(null, "Symmetry", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
            symmetryPanel.setLayout(gridLayout2);
            symmetryPanel.add(getJPanel2(), null);
            symmetryPanel.add(getJPanel3(), null);
        }
        return symmetryPanel;
    }

    /**
     * This method initializes jPanel2
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
            gridBagConstraints27.anchor = GridBagConstraints.WEST;
            gridBagConstraints27.gridy = 5;
            gridBagConstraints27.gridx = 0;
            GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
            gridBagConstraints26.anchor = GridBagConstraints.WEST;
            gridBagConstraints26.gridy = 4;
            gridBagConstraints26.gridx = 0;
            GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
            gridBagConstraints25.anchor = GridBagConstraints.WEST;
            gridBagConstraints25.gridy = 3;
            gridBagConstraints25.gridx = 0;
            GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
            gridBagConstraints24.anchor = GridBagConstraints.WEST;
            gridBagConstraints24.gridy = 2;
            gridBagConstraints24.gridx = 0;
            GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
            gridBagConstraints23.anchor = GridBagConstraints.WEST;
            gridBagConstraints23.gridy = 0;
            gridBagConstraints23.gridx = 0;
            jPanel2 = new JPanel();
            jPanel2.setLayout(new GridBagLayout());
            jPanel2.add(getSymmNone(), gridBagConstraints23);
            jPanel2.add(getSymmMirrorHorz(), gridBagConstraints24);
            jPanel2.add(getSymmMirrorVert(), gridBagConstraints25);
            jPanel2.add(getSymmMirrorDiagFwd(), gridBagConstraints26);
            jPanel2.add(getSymmMirrorDiagBwd(), gridBagConstraints27);
        }
        return jPanel2;
    }

    /**
     * This method initializes symmNone
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getSymmNone() {
        if (symmNone == null) {
            symmNone = new JRadioButton();
            symmNone.setMnemonic(KeyEvent.VK_N);
            symmNone.setSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_none_sel.gif")));
            symmNone.setDisabledIcon(new ImageIcon(getClass().getResource("/images/symm_none_dis.gif")));
            symmNone.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_none_sel_dis.gif")));
            symmNone.setText("None");
            symmNone.setIcon(new ImageIcon(getClass().getResource("/images/symm_none.gif")));
        }
        return symmNone;
    }

    /**
     * This method initializes symmMirrorHorz
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getSymmMirrorHorz() {
        if (symmMirrorHorz == null) {
            symmMirrorHorz = new JRadioButton();
            symmMirrorHorz.setMnemonic(KeyEvent.VK_H);
            symmMirrorHorz.setText("Horizontal mirror");
            symmMirrorHorz.setIcon(new ImageIcon(getClass().getResource("/images/symm_horz.gif")));
            symmMirrorHorz.setSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_horz_sel.gif")));
            symmMirrorHorz.setDisabledIcon(new ImageIcon(getClass().getResource("/images/symm_horz_dis.gif")));
            symmMirrorHorz.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_horz_sel_dis.gif")));
        }
        return symmMirrorHorz;
    }

    /**
     * This method initializes symmMirrorVert
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getSymmMirrorVert() {
        if (symmMirrorVert == null) {
            symmMirrorVert = new JRadioButton();
            symmMirrorVert.setMnemonic(KeyEvent.VK_V);
            symmMirrorVert.setText("Vertical mirror");
            symmMirrorVert.setIcon(new ImageIcon(getClass().getResource("/images/symm_vert.gif")));
            symmMirrorVert.setSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_vert_sel.gif")));
            symmMirrorVert.setDisabledIcon(new ImageIcon(getClass().getResource("/images/symm_vert_dis.gif")));
            symmMirrorVert.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_vert_sel_dis.gif")));
        }
        return symmMirrorVert;
    }

    /**
     * This method initializes symmMirrorDiagFwd
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getSymmMirrorDiagFwd() {
        if (symmMirrorDiagFwd == null) {
            symmMirrorDiagFwd = new JRadioButton();
            symmMirrorDiagFwd.setMnemonic(KeyEvent.VK_F);
            symmMirrorDiagFwd.setText("Diagonal forward");
            symmMirrorDiagFwd.setIcon(new ImageIcon(getClass().getResource("/images/symm_diag1.gif")));
            symmMirrorDiagFwd.setSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_diag1_sel.gif")));
            symmMirrorDiagFwd.setDisabledIcon(new ImageIcon(getClass().getResource("/images/symm_diag1_dis.gif")));
            symmMirrorDiagFwd.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_diag1_sel_dis.gif")));
        }
        return symmMirrorDiagFwd;
    }

    /**
     * This method initializes symmMirrorDiagBwd
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getSymmMirrorDiagBwd() {
        if (symmMirrorDiagBwd == null) {
            symmMirrorDiagBwd = new JRadioButton();
            symmMirrorDiagBwd.setMnemonic(KeyEvent.VK_B);
            symmMirrorDiagBwd.setText("Diagonal backward");
            symmMirrorDiagBwd.setIcon(new ImageIcon(getClass().getResource("/images/symm_diag2.gif")));
            symmMirrorDiagBwd.setSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_diag2_sel.gif")));
            symmMirrorDiagBwd.setDisabledIcon(new ImageIcon(getClass().getResource("/images/symm_diag2_dis.gif")));
            symmMirrorDiagBwd.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_diag2_sel_dis.gif")));
        }
        return symmMirrorDiagBwd;
    }

    /**
     * This method initializes jPanel3
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
            gridBagConstraints32.anchor = GridBagConstraints.WEST;
            gridBagConstraints32.gridy = 4;
            gridBagConstraints32.gridx = 0;
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.anchor = GridBagConstraints.WEST;
            gridBagConstraints31.gridy = 3;
            gridBagConstraints31.gridx = 0;
            GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
            gridBagConstraints30.anchor = GridBagConstraints.WEST;
            gridBagConstraints30.gridy = 2;
            gridBagConstraints30.gridx = 0;
            GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
            gridBagConstraints29.anchor = GridBagConstraints.WEST;
            gridBagConstraints29.gridy = 1;
            gridBagConstraints29.gridx = 0;
            GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
            gridBagConstraints28.anchor = GridBagConstraints.WEST;
            gridBagConstraints28.gridy = -1;
            gridBagConstraints28.gridx = -1;
            jPanel3 = new JPanel();
            jPanel3.setLayout(new GridBagLayout());
            jPanel3.add(getSymmRot180(), gridBagConstraints28);
            jPanel3.add(getSymm4fold(), gridBagConstraints29);
            jPanel3.add(getSymm4foldDiag(), gridBagConstraints30);
            jPanel3.add(getSymmRot90(), gridBagConstraints31);
            jPanel3.add(getSymm8fold(), gridBagConstraints32);
        }
        return jPanel3;
    }

    /**
     * This method initializes symmRot180
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getSymmRot180() {
        if (symmRot180 == null) {
            symmRot180 = new JRadioButton();
            symmRot180.setMnemonic(KeyEvent.VK_1);
            symmRot180.setText("Rotation 180");
            symmRot180.setIcon(new ImageIcon(getClass().getResource("/images/symm_180.gif")));
            symmRot180.setSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_180_sel.gif")));
            symmRot180.setDisabledIcon(new ImageIcon(getClass().getResource("/images/symm_180_dis.gif")));
            symmRot180.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_180_sel_dis.gif")));
        }
        return symmRot180;
    }

    /**
     * This method initializes symm4fold
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getSymm4fold() {
        if (symm4fold == null) {
            symm4fold = new JRadioButton();
            symm4fold.setMnemonic(KeyEvent.VK_4);
            symm4fold.setText("4-fold");
            symm4fold.setIcon(new ImageIcon(getClass().getResource("/images/symm_4.gif")));
            symm4fold.setSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_4_sel.gif")));
            symm4fold.setDisabledIcon(new ImageIcon(getClass().getResource("/images/symm_4_dis.gif")));
            symm4fold.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_4_sel_dis.gif")));
        }
        return symm4fold;
    }

    /**
     * This method initializes symm4foldDiag
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getSymm4foldDiag() {
        if (symm4foldDiag == null) {
            symm4foldDiag = new JRadioButton();
            symm4foldDiag.setMnemonic(KeyEvent.VK_D);
            symm4foldDiag.setText("Diagonal 4-fold");
            symm4foldDiag.setIcon(new ImageIcon(getClass().getResource("/images/symm_4diag.gif")));
            symm4foldDiag.setSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_4diag_sel.gif")));
            symm4foldDiag.setDisabledIcon(new ImageIcon(getClass().getResource("/images/symm_4diag_dis.gif")));
            symm4foldDiag.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_4diag_sel_dis.gif")));
        }
        return symm4foldDiag;
    }

    /**
     * This method initializes symmRot90
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getSymmRot90() {
        if (symmRot90 == null) {
            symmRot90 = new JRadioButton();
            symmRot90.setMnemonic(KeyEvent.VK_9);
            symmRot90.setText("Rotation 90");
            symmRot90.setIcon(new ImageIcon(getClass().getResource("/images/symm_90.gif")));
            symmRot90.setSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_90_sel.gif")));
            symmRot90.setDisabledIcon(new ImageIcon(getClass().getResource("/images/symm_90_dis.gif")));
            symmRot90.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_90_sel_dis.gif")));
        }
        return symmRot90;
    }

    /**
     * This method initializes symm8fold
     *
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getSymm8fold() {
        if (symm8fold == null) {
            symm8fold = new JRadioButton();
            symm8fold.setMnemonic(KeyEvent.VK_8);
            symm8fold.setText("8-fold");
            symm8fold.setIcon(new ImageIcon(getClass().getResource("/images/symm_8.gif")));
            symm8fold.setSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_8_sel.gif")));
            symm8fold.setDisabledIcon(new ImageIcon(getClass().getResource("/images/symm_8_dis.gif")));
            symm8fold.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/images/symm_8_sel_dis.gif")));
        }
        return symm8fold;
    }

    /**
     * This method initializes rulePanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getRulePanel() {
        if (rulePanel == null) {
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.gridx = 0;
            gridBagConstraints20.anchor = GridBagConstraints.CENTER;
            gridBagConstraints20.insets = new Insets(0, 0, 0, 50);
            gridBagConstraints20.gridy = 0;
            GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
            gridBagConstraints18.gridx = 1;
            gridBagConstraints18.insets = new Insets(5, 0, 0, 0);
            gridBagConstraints18.gridy = 0;
            jLabel79 = new JLabel();
            jLabel79.setText("8");
            jLabel78 = new JLabel();
            jLabel78.setText("7");
            jLabel77 = new JLabel();
            jLabel77.setText("6");
            jLabel76 = new JLabel();
            jLabel76.setText("5");
            jLabel75 = new JLabel();
            jLabel75.setText("4");
            jLabel74 = new JLabel();
            jLabel74.setText("3");
            jLabel73 = new JLabel();
            jLabel73.setText("2");
            jLabel72 = new JLabel();
            jLabel72.setText("1");
            jLabel71 = new JLabel();
            jLabel71.setText("0");
            jLabel8 = new JLabel();
            jLabel8.setText("Survival");
            jLabel7 = new JLabel();
            jLabel7.setText("Birth");
            rulePanel = new JPanel();
            rulePanel.setLayout(new GridBagLayout());
            rulePanel.add(getJPanel5(), gridBagConstraints18);
            rulePanel.add(getJPanel6(), gridBagConstraints20);
        }
        return rulePanel;
    }

    /**
     * This method initializes birth0
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBirth0() {
        if (birth0 == null) {
            birth0 = new JCheckBox();
            birth0.addChangeListener(this);
        }
        return birth0;
    }

    /**
     * This method initializes birth1
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBirth1() {
        if (birth1 == null) {
            birth1 = new JCheckBox();
        }
        return birth1;
    }

    /**
     * This method initializes birth2
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBirth2() {
        if (birth2 == null) {
            birth2 = new JCheckBox();
        }
        return birth2;
    }

    /**
     * This method initializes birth3
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBirth3() {
        if (birth3 == null) {
            birth3 = new JCheckBox();
        }
        return birth3;
    }

    /**
     * This method initializes birth4
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBirth4() {
        if (birth4 == null) {
            birth4 = new JCheckBox();
        }
        return birth4;
    }

    /**
     * This method initializes birth5
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBirth5() {
        if (birth5 == null) {
            birth5 = new JCheckBox();
        }
        return birth5;
    }

    /**
     * This method initializes birth6
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBirth6() {
        if (birth6 == null) {
            birth6 = new JCheckBox();
        }
        return birth6;
    }

    /**
     * This method initializes birth7
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBirth7() {
        if (birth7 == null) {
            birth7 = new JCheckBox();
        }
        return birth7;
    }

    /**
     * This method initializes birth8
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getBirth8() {
        if (birth8 == null) {
            birth8 = new JCheckBox();
        }
        return birth8;
    }

    /**
     * This method initializes survival0
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getSurvival0() {
        if (survival0 == null) {
            survival0 = new JCheckBox();
        }
        return survival0;
    }

    /**
     * This method initializes survival1
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getSurvival1() {
        if (survival1 == null) {
            survival1 = new JCheckBox();
        }
        return survival1;
    }

    /**
     * This method initializes survival2
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getSurvival2() {
        if (survival2 == null) {
            survival2 = new JCheckBox();
        }
        return survival2;
    }

    /**
     * This method initializes survival3
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getSurvival3() {
        if (survival3 == null) {
            survival3 = new JCheckBox();
        }
        return survival3;
    }

    /**
     * This method initializes survival4
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getSurvival4() {
        if (survival4 == null) {
            survival4 = new JCheckBox();
        }
        return survival4;
    }

    /**
     * This method initializes survival5
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getSurvival5() {
        if (survival5 == null) {
            survival5 = new JCheckBox();
        }
        return survival5;
    }

    /**
     * This method initializes survival6
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getSurvival6() {
        if (survival6 == null) {
            survival6 = new JCheckBox();
        }
        return survival6;
    }

    /**
     * This method initializes survival7
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getSurvival7() {
        if (survival7 == null) {
            survival7 = new JCheckBox();
        }
        return survival7;
    }

    /**
     * This method initializes survival8
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getSurvival8() {
        if (survival8 == null) {
            survival8 = new JCheckBox();
            survival8.addChangeListener(this);
        }
        return survival8;
    }

    /**
     * This method initializes jPanel1	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel1()
    {
        if (jPanel1 == null)
        {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            gridLayout.setHgap(5);
            jPanel1 = new JPanel();
            jPanel1.setLayout(gridLayout);
            jPanel1.add(getJButton(), null);
            jPanel1.add(getJButton1(), null);
        }
        return jPanel1;
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
            GridLayout gridLayout3 = new GridLayout();
            gridLayout3.setRows(6);
            gridLayout3.setVgap(5);
            gridLayout3.setHgap(5);
            jPanel5 = new JPanel();
            jPanel5.setLayout(gridLayout3);
            jPanel5.add(getJButton2(), null);
            jPanel5.add(getHighLife(), null);
            jPanel5.add(getDayNight(), null);
            jPanel5.add(getDiamoeba(), null);
            jPanel5.add(getSeeds(), null);
            jPanel5.add(getLife34(), null);
        }
        return jPanel5;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButton2()
    {
        if (conwayLife == null)
        {
            conwayLife = new JButton();
            conwayLife.setText("Conway's Life (B3/S23)");
            conwayLife.addActionListener(this);
        }
        return conwayLife;
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
            GridBagConstraints gridBagConstraints77 = new GridBagConstraints();
            gridBagConstraints77.gridx = 10;
            gridBagConstraints77.gridy = 1;
            GridBagConstraints gridBagConstraints76 = new GridBagConstraints();
            gridBagConstraints76.gridx = 8;
            gridBagConstraints76.gridy = 1;
            GridBagConstraints gridBagConstraints75 = new GridBagConstraints();
            gridBagConstraints75.gridx = 7;
            gridBagConstraints75.gridy = 1;
            GridBagConstraints gridBagConstraints74 = new GridBagConstraints();
            gridBagConstraints74.gridx = 6;
            gridBagConstraints74.gridy = 1;
            GridBagConstraints gridBagConstraints73 = new GridBagConstraints();
            gridBagConstraints73.gridx = 5;
            gridBagConstraints73.gridy = 1;
            GridBagConstraints gridBagConstraints72 = new GridBagConstraints();
            gridBagConstraints72.gridx = 4;
            gridBagConstraints72.gridy = 1;
            GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
            gridBagConstraints71.gridx = 3;
            gridBagConstraints71.gridy = 1;
            GridBagConstraints gridBagConstraints63 = new GridBagConstraints();
            gridBagConstraints63.gridx = 2;
            gridBagConstraints63.gridy = 1;
            GridBagConstraints gridBagConstraints78 = new GridBagConstraints();
            gridBagConstraints78.gridx = 11;
            gridBagConstraints78.gridy = 1;
            GridBagConstraints gridBagConstraints79 = new GridBagConstraints();
            gridBagConstraints79.gridx = 2;
            gridBagConstraints79.gridy = 2;
            GridBagConstraints gridBagConstraints80 = new GridBagConstraints();
            gridBagConstraints80.gridx = 3;
            gridBagConstraints80.gridy = 2;
            GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
            gridBagConstraints81.gridx = 4;
            gridBagConstraints81.gridy = 2;
            GridBagConstraints gridBagConstraints82 = new GridBagConstraints();
            gridBagConstraints82.gridx = 5;
            gridBagConstraints82.gridy = 2;
            GridBagConstraints gridBagConstraints83 = new GridBagConstraints();
            gridBagConstraints83.gridx = 6;
            gridBagConstraints83.gridy = 2;
            GridBagConstraints gridBagConstraints84 = new GridBagConstraints();
            gridBagConstraints84.gridx = 7;
            gridBagConstraints84.gridy = 2;
            GridBagConstraints gridBagConstraints85 = new GridBagConstraints();
            gridBagConstraints85.gridx = 8;
            gridBagConstraints85.gridy = 2;
            GridBagConstraints gridBagConstraints70 = new GridBagConstraints();
            gridBagConstraints70.gridx = 10;
            gridBagConstraints70.gridy = 2;
            GridBagConstraints gridBagConstraints69 = new GridBagConstraints();
            gridBagConstraints69.gridx = 11;
            gridBagConstraints69.gridy = 2;
            GridBagConstraints gridBagConstraints68 = new GridBagConstraints();
            gridBagConstraints68.gridx = 3;
            gridBagConstraints68.gridy = 0;
            GridBagConstraints gridBagConstraints67 = new GridBagConstraints();
            gridBagConstraints67.gridx = 4;
            gridBagConstraints67.gridy = 0;
            GridBagConstraints gridBagConstraints66 = new GridBagConstraints();
            gridBagConstraints66.gridx = 5;
            gridBagConstraints66.gridy = 0;
            GridBagConstraints gridBagConstraints65 = new GridBagConstraints();
            gridBagConstraints65.gridx = 6;
            gridBagConstraints65.gridy = 0;
            GridBagConstraints gridBagConstraints64 = new GridBagConstraints();
            gridBagConstraints64.gridx = 7;
            gridBagConstraints64.gridy = 0;
            GridBagConstraints gridBagConstraints62 = new GridBagConstraints();
            gridBagConstraints62.gridx = 8;
            gridBagConstraints62.gridy = 0;
            GridBagConstraints gridBagConstraints56 = new GridBagConstraints();
            gridBagConstraints56.gridx = 10;
            gridBagConstraints56.gridy = 0;
            GridBagConstraints gridBagConstraints55 = new GridBagConstraints();
            gridBagConstraints55.gridx = 11;
            gridBagConstraints55.gridy = 0;
            GridBagConstraints gridBagConstraints54 = new GridBagConstraints();
            gridBagConstraints54.gridx = 0;
            gridBagConstraints54.gridy = 2;
            GridBagConstraints gridBagConstraints53 = new GridBagConstraints();
            gridBagConstraints53.gridx = 2;
            gridBagConstraints53.gridy = 0;
            GridBagConstraints gridBagConstraints43 = new GridBagConstraints();
            gridBagConstraints43.anchor = GridBagConstraints.EAST;
            gridBagConstraints43.gridy = 1;
            gridBagConstraints43.gridx = 0;
            jPanel6 = new JPanel();
            jPanel6.setLayout(new GridBagLayout());
            jPanel6.add(jLabel7, gridBagConstraints43);
            jPanel6.add(jLabel71, gridBagConstraints53);
            jPanel6.add(jLabel8, gridBagConstraints54);
            jPanel6.add(jLabel79, gridBagConstraints55);
            jPanel6.add(jLabel78, gridBagConstraints56);
            jPanel6.add(jLabel77, gridBagConstraints62);
            jPanel6.add(jLabel76, gridBagConstraints64);
            jPanel6.add(jLabel75, gridBagConstraints65);
            jPanel6.add(jLabel74, gridBagConstraints66);
            jPanel6.add(jLabel73, gridBagConstraints67);
            jPanel6.add(jLabel72, gridBagConstraints68);
            jPanel6.add(getSurvival8(), gridBagConstraints69);
            jPanel6.add(getSurvival7(), gridBagConstraints70);
            jPanel6.add(getSurvival6(), gridBagConstraints85);
            jPanel6.add(getSurvival5(), gridBagConstraints84);
            jPanel6.add(getSurvival4(), gridBagConstraints83);
            jPanel6.add(getSurvival3(), gridBagConstraints82);
            jPanel6.add(getSurvival2(), gridBagConstraints81);
            jPanel6.add(getSurvival1(), gridBagConstraints80);
            jPanel6.add(getSurvival0(), gridBagConstraints79);
            jPanel6.add(getBirth8(), gridBagConstraints78);
            jPanel6.add(getBirth0(), gridBagConstraints63);
            jPanel6.add(getBirth1(), gridBagConstraints71);
            jPanel6.add(getBirth2(), gridBagConstraints72);
            jPanel6.add(getBirth3(), gridBagConstraints73);
            jPanel6.add(getBirth4(), gridBagConstraints74);
            jPanel6.add(getBirth5(), gridBagConstraints75);
            jPanel6.add(getBirth6(), gridBagConstraints76);
            jPanel6.add(getBirth7(), gridBagConstraints77);
        }
        return jPanel6;
    }

    /**
     * This method initializes highLife	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getHighLife()
    {
        if (highLife == null)
        {
            highLife = new JButton();
            highLife.setText("HighLife (B36/S23)");
            highLife.addActionListener(this);
        }
        return highLife;
    }

    /**
     * This method initializes dayNight	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getDayNight()
    {
        if (dayNight == null)
        {
            dayNight = new JButton();
            dayNight.setText("Day & Night (B3678/S34678)");
            dayNight.addActionListener(this);
        }
        return dayNight;
    }

    /**
     * This method initializes diamoeba	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getDiamoeba()
    {
        if (diamoeba == null)
        {
            diamoeba = new JButton();
            diamoeba.setText("Diamoeba (B35678/S5678)");
            diamoeba.addActionListener(this);
        }
        return diamoeba;
    }

    /**
     * This method initializes seeds	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getSeeds()
    {
        if (seeds == null)
        {
            seeds = new JButton();
            seeds.setText("Seeds (B2/S)");
            seeds.addActionListener(this);
        }
        return seeds;
    }

    /**
     * This method initializes life34	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getLife34()
    {
        if (life34 == null)
        {
            life34 = new JButton();
            life34.setText("34 Life (B34/S34)");
            life34.addActionListener(this);
        }
        return life34;
    }

    public void stateChanged(ChangeEvent e)
    {
        if ((e.getSource() == birth0)
                || (e.getSource() == survival8))
        {
            setOuterSpaceText();
        }
    }
}
