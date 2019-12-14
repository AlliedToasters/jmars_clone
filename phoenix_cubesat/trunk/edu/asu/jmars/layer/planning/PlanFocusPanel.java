package edu.asu.jmars.layer.planning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import edu.asu.jmars.Main;
import edu.asu.jmars.layer.FocusPanel;
import edu.asu.jmars.layer.planning.TimeUtil;
import edu.asu.jmars.swing.ColorButton;
import edu.asu.jmars.swing.ColorCombo;
import edu.asu.jmars.swing.TableColumnAdjuster;
import edu.asu.jmars.util.Config;
import edu.asu.jmars.util.DebugLog;
import edu.asu.jmars.util.Util;

/**
 * This focus panel is used to display all the necessary information for
 * the plan associated with this layer.  Lets the user create, view and 
 * edit objectives, targets, sequences, commands and blocks.
 * If this plan layer has been loaded from a saved plan from the database,
 * the content will only be editable if the user is the same one who
 * created and saved the plan to the database originally.  If not, the plan
 * and its plan focus panel will be "read-only".
 */
@SuppressWarnings("serial")
public class PlanFocusPanel extends FocusPanel  {
	
	private JLabel nameLbl;
	private JTable objTbl;
	private JScrollPane objSp;
	private JPanel objPnl;
	private JButton addNewObjBtn;
	private JButton addSiteSearchBtn;
	private JButton olaPowerBtn;
	private JTextField olaPowerTF;
	private String olaUtcUnspecifiedStr = "UTC not specified";
	private JLabel tseLbl;
	private String tsePrompt = "TSE Filename: ";
	private JComboBox<String> mapPowerBx;
	private JComboBox<String> polyPowerBx;
	private JComboBox<String> samPowerBx;
	private final String onStr = "On";
	private final String offStr = "Off";
	private JComboBox<String> olaPowerBx;
	private JPanel oszPnl;
	
	private JXTaskPane conPnl;
	
	private JPanel tPnl; //title panel for mosaic results
	private JLabel titleLbl;
	private JButton mosaicLBtn;
	private JButton mosaicRBtn;
	private JButton mosaicBeginBtn;
	private JButton mosaicEndBtn;
	private JLabel tarTotLbl;
	private String tarTotPrompt = "Total Targets: ";
	private JPanel targetPnl;

	private JLabel tarConfLbl;
	private String tarInConflictPrompt = "Targets in conflict: ";

	private JScrollPane targetSp;
//	private TargetTable targetTbl;
	private JButton addTarBtn;
	private JLabel comTotLbl;
	private String comTotPrompt = "Total Commands: ";
	private JPanel comPnl;
	
	private JPanel seqPnl;
	private JTable seqTbl;
	private JScrollPane seqSP;
	private JButton addSeqBtn;
	private JLabel seqTotLbl;
	private String seqTotPrompt ="Total Sequences: ";
	private JLabel seqConfLbl;
	private String seqInConflictPrompt = "Sequences in conflict: ";
	
	private JLabel comConfLbl;
	private String comInConflictPrompt = "Commands in conflict: ";

	private JScrollPane comSp;
	private JTable comTbl;
	private JButton addComBtn;
	private JButton addBlkBtn;
	private JButton sandboxBlockBtn;
	
	private JSplitPane topSplit;
	private JSplitPane botSplit;
	
	private JCheckBox proposedChk;
	private JCheckBox approveChk;
	private JTextArea descTA;
	private JScrollPane descSP;
	private JButton saveAsBtn;
	private JButton saveBtn;
	private JCheckBox reportChk;
	
	private ColorCombo radCC;
	private ColorCombo slewCC;
	private ColorCombo mapCamCC;
	private ColorCombo polyCamCC;
	private ColorCombo samCamCC;
	private ColorCombo olaCC;
	private ColorCombo otesCC;
	private ColorCombo ovirsCC;
	private ColorCombo rexisCC;
	private ColorCombo navCamCC;
	private ColorCombo oldFtCC;
	private ColorCombo trackCC;
	private ColorCombo starsCC;
	
	private JCheckBox showRexisChk;
	private JCheckBox showOLAChk;
	private JCheckBox showOTESChk;
	private JCheckBox showOVIRSChk;
	private JCheckBox showMapCamChk;
	private JCheckBox showPolyCamChk;
	private JCheckBox showSamCamChk;
	private JCheckBox showNavCamChk;
	private JCheckBox showOldFootprintsChk;
	private JCheckBox fillCamFpChk;
	private JCheckBox fitCamFpChk;
	private JCheckBox radialUncChk;
	private JCheckBox trackUncChk;
	private JCheckBox showTrackChk;
	private JCheckBox showStarsChk;
	
	// various checkboxes to aid with debugging
	private JCheckBox showVectorsAtTargetsChk;
	private JCheckBox showVectorsAtImagesChk;
	private JCheckBox showVectorsBetweenTargetsChk;
	private JSpinner  numVectorsBetweenTargetsSpn;
	private JCheckBox showOrbitTrackChk;
	private JTextField orbitTrackStepSzTf;
	
	private ColorButton xButton, yButton, zButton, sunButton, velButton, velBButton, motionButton, momentButton;
	private ColorButton sc2EarthButton, sc2MoonButton, sc2BodyButton, sigmaUncEllipsoidButton, rtnEllipsoidButton;
	private ColorButton bodyVelWrtSunButton;
	
	private JCheckBox showScAxesChk, showJ2kAxesChk, showRtnAxesChk, showSamAxesChk, showSunChk, showEarthChk, showMoonChk, showSc2BodyChk, showSc2TgtChk;
	private JTextField scAxesScaleTf, j2kAxesScaleTf, rtnAxesScaleTf, samAxesScaleTf, sunScaleTf, earthScaleTf, moonScaleTf, sc2BodyScaleTf, baseLineWidthTf, bodyVelWrtSunScaleTf;
	private JCheckBox showRtnEllipsoidChk, showSigmaEllipsoidChk;
	private JCheckBox showVelJ2kChk, showVelBChk, showAngmChk, showMotionChk, showBodyVelWrtSunChk;
	private JTextField velJ2kScaleTf, velBScaleTf, angmScaleTf, motionScaleTf, olaScaleTf;

	private JCheckBox showOrgLocChk;
	private JCheckBox showOrgOffLocChk;
	private JCheckBox showSigmaLocChk;
	private JTextField showSigmaLocSigmaTf;
	private ColorCombo showOrgLocCC;
	private ColorCombo showOrgOffLocCC;
	private ColorCombo showSigmaLocCC;
	private ColorCombo showSigmaLocSigmaCC;
	
	
	private JCheckBox vioChk;
	private JTable vioTbl;
	private JPanel vioPnl;
	private JScrollPane vioSp;
	private JTextArea vioTitleTA;
	private JTextArea vioDescTA;
	private JTextArea vioWaiverTA;
	private JButton vioSetWaiverBtn;
	private JButton vioRemoveWaiverBtn;
	private int vioTblIndex;
	
	private JButton setSelfDelFlagBtn, unsetSelfDelFlagBtn;

	private boolean savedProposedChkEnableState;
	private boolean savedApproveChkEnableState;
	private boolean savedSaveBtnEnableState;
	private boolean savedSaveAsBtnEnabledState;

    private int curMosaic;
    private int[] selectedObjs;
    
	private int pad = 1;
	private Insets in = new Insets(pad,pad,pad,pad);

	private PlanningLayer myLayer;
	
//	private CommandTableMouseListener ctml;
//	private SequenceTableMouseListener stml;
//	private TargetTableMouseListener ttml;
//	private FittingListenerImpl fitl;
//	
//	private PlannedSequence selectedSequence = null;
//	private Target selectedTarget = null;
//	
//	private SiteSearchWindow ssw;
//	private ObjCreationWindow ocw;
	
	private JPanel overviewTab;
	private JPanel resultsTab;
//	private JPanel violationTab;
	private JPanel kernelTab;
//	private JPanel settingsTab;
    private JPanel sunTab;
    private JPanel groundTab;
	private JPanel finalizeTab;
	
	private JLabel vmlVerLbl;
	private final String vmlPrompt = "J-Asteroid payload library filename:";
	private JLabel scLibLbl;
	private final String scLibPrompt = "Spacecraft payload library filename:";
	private String defaultVMLFile;
	
	private boolean canApprove;
	
	private static final DebugLog log = DebugLog.instance();
	
	
	
	/**
	 * Creates a plan focus panel with an overview tab, results tab,
	 * violation tab, kernel tab, settings tab and finalize tab.
	 * 
	 * @param parent The PlanLView for this focus panel.
	 */
	public PlanFocusPanel(PlanningLView parent) {
		super(parent, true);
		myLayer = (PlanningLayer)parent.getLayer();
		
//		//set the approval boolean
//		if(Main.groups.contains(PlanningLayer.APPROVER_ROLE)){
//			canApprove = true;
//		}else{
//			canApprove = false;
//		}
		
		
		overviewTab = createOverviewPanel();
//		resultsTab = createResultsPanel();
//		violationTab = createViolationPanel();
//		kernelTab = createKernelPanel();
//		groundTab = createGroundStationTab();
//		sunTab = createSunTab();
//		settingsTab = createSettingsPanel();
//		finalizeTab = createFinalizePanel();
		
		//Add Tabs
		addTab("Overview", overviewTab);
//		addTab("Targets", targetsTab);
//		addTab("Results", resultsTab);
//		addTab("Violations", violationTab);
//		addTab("Kernel Details", kernelTab);
//		addTab("Targets", targetsTab);
//		addTab("Ground Station", groundTab);
//		addTab("Sun", sunTab);
//		addTab("View Settings", settingsTab);
//		addTab("Finalize", finalizeTab);
		
		setPreferredSize(new Dimension(650,600));
		
//		//refresh ui if loading from db (if objectives already exist)
//		if(myLayer.getObjectives().size()>0){
//			refreshObjTable();
//			refreshResultsPanel();
//		}
		
	}
	

	/**
	 * The Rulers need some ability to listen for table updates.  TODO: Implement this correctly
	 * @param tml
	 */
//	public void addTableModelListener(TableModelListener tml) {
//		comTbl.getModel().addTableModelListener(tml);
//		targetTbl.getModel().addTableModelListener(tml);
//	}

	/**
	 * Remove a table model listener from both the command and target
	 * table.
	 * @param tml
	 */
//	public void removeTableModelListener(TableModelListener tml) {
//		comTbl.getModel().removeTableModelListener(tml);
//		targetTbl.getModel().removeTableModelListener(tml);
//	}
	
	
	
	private JPanel createOverviewPanel (){
	//Planning Window Panel
		JPanel planInfoPnl = new JPanel();
		planInfoPnl.setLayout(new BoxLayout(planInfoPnl, BoxLayout.PAGE_AXIS));
		planInfoPnl.setBorder(new TitledBorder(myLayer.getMissionData().getMissionName()+" Planning Window"));
		nameLbl = new JLabel("Unsaved Plan");
		if(myLayer.getName().length()>0){
			nameLbl.setText(myLayer.getName());
		}
		nameLbl.setFont(new Font("Dialog", Font.BOLD, 18));
		JPanel namePnl = new JPanel();
		namePnl.add(nameLbl);
		JLabel windowLbl = new JLabel(TimeUtil.etToUtcStr(myLayer.getStartET())+" to "+TimeUtil.etToUtcStr(myLayer.getEndET()));
		Font windowFont = new Font("Dialog", Font.ITALIC, 16);
		windowLbl.setFont(windowFont);
		windowLbl.setForeground(Color.DARK_GRAY);
		JLabel spiceLbl = new JLabel("Selected SPICE Kernel: "+PlanningKernels.getInstance().getMetaKernelName());
        spiceLbl.setFont(new Font("Dialog", Font.BOLD, 17));
		JPanel windowPnl = new JPanel();
		windowPnl.add(windowLbl);
		windowPnl.add(spiceLbl);
//		JPanel descPnl = new JPanel(new GridLayout(1,1));
//		descPnl.setBorder(new CompoundBorder(new TitledBorder("Window Description"), new EmptyBorder(0, 5, 0, 5)));
//		JTextArea winDescTA = new JTextArea();
//		winDescTA.setBackground(Util.panelGrey);
//		winDescTA.setLineWrap(true);
//		winDescTA.setWrapStyleWord(true);
//		winDescTA.setEditable(false);
//		winDescTA.setText(myLayer.getPlanTimeWindow().getDescription());
//		JScrollPane descSP = new JScrollPane(winDescTA);
//		descSP.setBorder(BorderFactory.createEmptyBorder());
//		descPnl.add(descSP);
		//Name and window have their own panel
		JPanel planWindowPnl = new JPanel();
		planWindowPnl.setLayout(new BoxLayout(planWindowPnl, BoxLayout.PAGE_AXIS));
		planWindowPnl.add(namePnl);
		planWindowPnl.add(windowPnl);
//		planWindowPnl.add(descPnl);
		
		//spice
//		JPanel spicePnl = new JPanel();
//		spicePnl.add(spiceLbl);

/*		//spk
		String spkStr = myLayer.getSpacecraftSPK();
		//Empty string and null are to be treated as the same thing,
		// so show an empty string to the user always, for consistency.
		if(spkStr == null){
			spkStr = "";
		}
		JLabel spkLbl = new JLabel("Additional Spacecraft SPK: "+spkStr);
		JPanel spkPnl = new JPanel();
		spkPnl.add(spkLbl);
		//cdl
		JLabel cdlVerLbl = new JLabel("CDL Version: "+myLayer.getCDLVersionName()); 
		if (myLayer.getCreatedFromOldCDLFlag()) {
		    cdlVerLbl.setForeground(Color.RED);
		}
		JPanel cdlPnl = new JPanel();
		cdlPnl.add(cdlVerLbl);
		//vml
		String vmlName = myLayer.getVmlLibraryName();
		vmlVerLbl = new JLabel();
		if (vmlName == null) {
		    vmlName = "No payload library available.";
		    vmlVerLbl.setForeground(Color.RED);
		}
		vmlVerLbl.setText(vmlPrompt+" "+vmlName);
		JPanel vmlPnl = new JPanel();
        vmlPnl.add(vmlVerLbl);
        String scLibName = PlanningLayer.getBlockFactory().getSpacecraftLibraryName();
        scLibLbl = new JLabel();
		if (scLibName == null) {
			scLibName = "No spacecraft payload library name available";
		    scLibLbl.setForeground(Color.RED);
		}
		scLibLbl.setText(scLibPrompt+" "+scLibName);
		JPanel scLibPnl = new JPanel();
        scLibPnl.add(scLibLbl);
        
        sandboxBlockBtn = new JButton(addSandboxBlockAct);
        sandboxBlockBtn.setEnabled(true);
        
        JPanel vmlSandboxPnl = new JPanel();
        vmlSandboxPnl.add(sandboxBlockBtn);
        
		
		//uncertainties
		JPanel tsePnl = new JPanel();
		tseLbl = new JLabel();
		refreshTSELabel();
		tsePnl.add(tseLbl);
        
		//slew rates
		JTable slewCfgSummaryTbl = new JTable(new SlewSummaryTableModel());
		slewCfgSummaryTbl.setDefaultRenderer(double.class, new SlewSummaryTableDoubleValueCellRenderer());
		slewCfgSummaryTbl.setPreferredScrollableViewportSize(slewCfgSummaryTbl.getPreferredSize());
		slewCfgSummaryTbl.setBorder(new LineBorder(Color.GRAY));
		JPanel slewPnl = new JPanel();
		slewPnl.setLayout(new BorderLayout());
		slewPnl.setBorder(new TitledBorder("Slew Config Values"));
		slewPnl.add(slewCfgSummaryTbl, BorderLayout.NORTH);
		//window type
		JLabel winTypeLbl = new JLabel("This plan is an "+myLayer.getWindowType()+" window.");
		winTypeLbl.setForeground(Color.BLUE);
		JPanel winTypePnl = new JPanel();
		winTypePnl.add(winTypeLbl);
		
		//schema
		JLabel schemaLbl = new JLabel("Database schema: "+DatabaseConnect.getSchema());
		schemaLbl.setForeground(Color.DARK_GRAY);
		JPanel schemaPnl = new JPanel();
		schemaPnl.add(schemaLbl);
		
		
		//polycam motor position and ocams states
		//lables
		JLabel polyMotorLbl = new JLabel("Starting POLYCAM motor position: "+myLayer.getPolycamStartingMotorPosition());
		JLabel ocamsPowerLbl = new JLabel("OCAMS Starting Power States:  ");
		JLabel mapcamOnLbl = new JLabel("MAPCAM: ");
		JLabel polycamOnLbl = new JLabel("POLYCAM: ");
		JLabel samcamOnLbl = new JLabel("SAMCAM: ");
		//comboboxes
		Vector<String> onOffVec = getOnOffVector();
		mapPowerBx = new JComboBox<String>(onOffVec);
		mapPowerBx.setEnabled(myLayer.isEditable()); //read only sensitive
		polyPowerBx = new JComboBox<String>(onOffVec);
		polyPowerBx.setEnabled(myLayer.isEditable()); //read only sensitive
		samPowerBx = new JComboBox<String>(onOffVec);
		samPowerBx.setEnabled(myLayer.isEditable()); //read only sensitive
		//set selected on boxes
		mapPowerBx.setSelectedItem(getOnOffFromBoolean(myLayer.isInitialMapCamPoweredOn()));
		polyPowerBx.setSelectedItem(getOnOffFromBoolean(myLayer.isInitialPolyCamPoweredOn()));
		samPowerBx.setSelectedItem(getOnOffFromBoolean(myLayer.isInitialSamCamPoweredOn()));
		//add listener to boxes
		mapPowerBx.addActionListener(instrumentPowerListener);
		polyPowerBx.addActionListener(instrumentPowerListener);
		samPowerBx.addActionListener(instrumentPowerListener);
		JPanel ocamsPnl1 = new JPanel();
		ocamsPnl1.add(polyMotorLbl);
		JPanel ocamsPnl2 = new JPanel();
		ocamsPnl2.add(ocamsPowerLbl);
		ocamsPnl2.add(mapcamOnLbl);
		ocamsPnl2.add(mapPowerBx);
		ocamsPnl2.add(Box.createHorizontalStrut(3));
		ocamsPnl2.add(polycamOnLbl);
		ocamsPnl2.add(polyPowerBx);
		ocamsPnl2.add(Box.createHorizontalStrut(3));
		ocamsPnl2.add(samcamOnLbl);
		ocamsPnl2.add(samPowerBx);
		
		//ola power on et
		olaPowerBtn = new JButton(setOlaPowerAct);
		olaPowerBtn.setEnabled(myLayer.isEditable()); //read only sensitive
		olaPowerTF = new JTextField(20);
		olaPowerTF.setEditable(false);
		double olaET = myLayer.getOlaPowerOnET();
		if(olaET == -1){
			olaPowerTF.setText(olaUtcUnspecifiedStr);
		}else{
			olaPowerTF.setText(TimeUtil.etToUtcStr(myLayer.getOlaPowerOnET()));
		}
		JPanel olaPowerPnl1 = new JPanel();
		olaPowerPnl1.add(olaPowerBtn);
		olaPowerPnl1.add(olaPowerTF);
		//ola power state
		olaPowerBx = new JComboBox<String>(onOffVec);
		olaPowerBx.setEnabled(myLayer.isEditable()); //read only sensitive
		olaPowerBx.setSelectedItem(getOnOffFromBoolean(myLayer.isOlaPoweredOn()));
		olaPowerBx.addActionListener(instrumentPowerListener);
		JLabel olaPowerLbl = new JLabel("OLA power state: ");
		JPanel olaPowerPnl2 = new JPanel();
		olaPowerPnl2.add(olaPowerLbl);
		olaPowerPnl2.add(olaPowerBx);
		
		//osz threshold
		JLabel oszThresholdLbl = new JLabel("OSZ Thermal Threshold: "+myLayer.getOszThermalThreshold());
		JPanel oszThresholdPnl = new JPanel();
		oszThresholdPnl.add(oszThresholdLbl);
		
		//osz scores
		oszPnl = new JPanel(new GridBagLayout());
		oszPnl.setBorder(new TitledBorder("OSZ Scores"));
		refreshOSZPanel();
*/		
		//add to window panel
//		JPanel scrollPnl = new JPanel();
//		scrollPnl.setLayout(new BoxLayout(scrollPnl, BoxLayout.PAGE_AXIS));
//		scrollPnl.add(attitudePnl);
//		scrollPnl.add(spicePnl);
//		scrollPnl.add(spkPnl);
//		scrollPnl.add(cdlPnl);
//		scrollPnl.add(vmlPnl);
//		scrollPnl.add(scLibPnl);
//		scrollPnl.add(vmlSandboxPnl);
//		scrollPnl.add(tsePnl);
//		scrollPnl.add(schemaPnl);
//		scrollPnl.add(slewPnl);
//		scrollPnl.add(winTypePnl);
//		scrollPnl.add(ocamsPnl1);
//		scrollPnl.add(ocamsPnl2);
//		scrollPnl.add(olaPowerPnl1);
//		scrollPnl.add(olaPowerPnl2);
//		scrollPnl.add(oszThresholdPnl);
//		scrollPnl.add(oszPnl);
//		JScrollPane infoSP = new JScrollPane(scrollPnl);
//		infoSP.setBorder(new LineBorder(Color.LIGHT_GRAY));
//		infoSP.getVerticalScrollBar().setUnitIncrement(15);
		
		planInfoPnl.add(planWindowPnl);
//		planInfoPnl.add(infoSP);
		
	//Objectives Panel
/*		objPnl = new JPanel();
		objPnl.setBorder(new TitledBorder("Objectives"));
		objPnl.setLayout(new BorderLayout());
		objTbl = new JTable();
		objTbl.addMouseListener(objMouseListener);
		objTbl.getSelectionModel().addListSelectionListener(objRowListener);
		objSp = new JScrollPane(objTbl, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JButton ocamsReorderIdBtn = new JButton(reorderOcamsAct);
		ocamsReorderIdBtn.setEnabled(myLayer.isEditable()); //read only sensitive
		
		JButton renameSeqBtn = new JButton(renameSequencesAct);
		renameSeqBtn.setEnabled(myLayer.isEditable()); // read only sensitive
		
		JButton makeUniqueSeqBtn = new JButton(makeUniqueSequencesAct);
		makeUniqueSeqBtn.setEnabled(myLayer.isEditable()); // read only sensitive

		JButton updateSeqDOYBtn = new JButton(updateSequenceDOYAct);
		updateSeqDOYBtn.setEnabled(myLayer.isEditable()); // read only sensitive
		
        setSelfDelFlagBtn = new JButton("All Seq Self-Deleting");
		setSelfDelFlagBtn.addActionListener(setSelfDeletingFlagAct);
		setSelfDelFlagBtn.setEnabled(myLayer.isEditable()); //read only sensitive
		unsetSelfDelFlagBtn = new JButton("No Seq Self-Deleting");
		unsetSelfDelFlagBtn.addActionListener(setSelfDeletingFlagAct);
		unsetSelfDelFlagBtn.setEnabled(myLayer.isEditable()); //read only sensitive
		JButton timeShiftBtn = new JButton(timeShiftTargetsAct);
		timeShiftBtn.setEnabled(myLayer.isEditable()); //read only sensitive
		JButton recenterBtn = new JButton(recenterTargetsAct);
		recenterBtn.setEnabled(myLayer.isEditable()); //read only sensitive
		JButton recomputeBtn = new JButton(recomputeTargetsAct);
		recomputeBtn.setEnabled(myLayer.isEditable()); //read only sensitive
		JButton slewDurBtn = new JButton(recomputeTargetSlewAct);
		slewDurBtn.setEnabled(myLayer.isEditable()); //read only sensitive
		
		
		JPanel btnPnl = new JPanel(new GridBagLayout());
		btnPnl.setBorder(new EmptyBorder(5, 0, 5, 0));
		int row = 0; int col = 0;
		btnPnl.add(ocamsReorderIdBtn, new GridBagConstraints(col, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
		btnPnl.add(setSelfDelFlagBtn, new GridBagConstraints(++col, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));		
		btnPnl.add(unsetSelfDelFlagBtn, new GridBagConstraints(++col, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
		row++; col = 0;
		btnPnl.add(timeShiftBtn, new GridBagConstraints(col, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
		btnPnl.add(recenterBtn, new GridBagConstraints(++col, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
		btnPnl.add(recomputeBtn, new GridBagConstraints(++col, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
		row++; col = 0;
		btnPnl.add(renameSeqBtn, new GridBagConstraints(col, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
		btnPnl.add(makeUniqueSeqBtn, new GridBagConstraints(++col, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
		btnPnl.add(updateSeqDOYBtn, new GridBagConstraints(++col, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
//		btnPnl.add(slewDurBtn, new GridBagConstraints(++col, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));  //can uncom
		
		objPnl.add(objSp, BorderLayout.CENTER);
		objPnl.add(btnPnl, BorderLayout.SOUTH);	
	
	//Add New Objective Panel
		JPanel addNewPnl = new JPanel();
		addNewPnl.setBorder(new TitledBorder("Add New Objective"));
		addNewObjBtn = new JButton(createObjAct);
		addNewObjBtn.setEnabled(myLayer.isEditable());
		addSiteSearchBtn = new JButton(siteSearchAct); 
		addSiteSearchBtn.setEnabled(myLayer.isEditable());
		//add to addNewPnl
		addNewPnl.add(addNewObjBtn);
		addNewPnl.add(Box.createHorizontalStrut(15));
		addNewPnl.add(addSiteSearchBtn);		
*/	
	//Put everything together on main panel and return that panel	
		JPanel overviewPnl = new JPanel();
		overviewPnl.setBackground(Util.lightBlue);
		overviewPnl.setLayout(new BoxLayout(overviewPnl, BoxLayout.PAGE_AXIS));
		JPanel one = new JPanel(new GridLayout(1,1));
		one.setBackground(Util.lightBlue);
		one.setBorder(new EmptyBorder(5,5,5,5));
		one.add(planInfoPnl);
		JPanel two = new JPanel(new GridLayout(1,1));
		two.setBackground(Util.lightBlue);
		two.setBorder(new EmptyBorder(5,5,5,5));
//		two.add(objPnl);
		JPanel three = new JPanel(new GridLayout(1,1));
		three.setBackground(Util.lightBlue);
		three.setBorder(new EmptyBorder(5,5,5,5));
//		three.add(addNewPnl);
		overviewPnl.add(one);
//		overviewPnl.add(two);
//		overviewPnl.add(three);

		return overviewPnl;
	}
	
	private Vector<String> getOnOffVector(){
		Vector<String> vec = new Vector<String>();
		vec.add(onStr);
		vec.add(offStr);
		return vec;
	}
	
	private String getOnOffFromBoolean(boolean on){
		if(on){
			return onStr;
		}else{
			return offStr;
		}
	}
	
	private boolean getBooleanFromOnOff(String str){
		if(str.equals(onStr)){
			return true;
		}else{
			return false;
		}
	}
	
//	private ActionListener instrumentPowerListener = new ActionListener() {
//		@SuppressWarnings("unchecked")
//		public void actionPerformed(ActionEvent e) {
//			JComboBox<String> source = (JComboBox<String>)e.getSource();
//			if(source == mapPowerBx){
//				myLayer.setInitialMapCamPoweredOn(getBooleanFromOnOff((String)mapPowerBx.getSelectedItem()));
//			}
//			else if(source == polyPowerBx){
//				myLayer.setInitialPolyCamPoweredOn(getBooleanFromOnOff((String)polyPowerBx.getSelectedItem()));
//			}
//			else if(source == samPowerBx){
//				myLayer.setInitialSamCamPoweredOn(getBooleanFromOnOff((String)samPowerBx.getSelectedItem()));
//			}
//			else if(source == olaPowerBx){
//				myLayer.setOlaPoweredOn(getBooleanFromOnOff((String)olaPowerBx.getSelectedItem()));
//				refreshOSZPanel();
//			}
//			/* 
//			 * No matter which instrument's power was updated, at least one
//			 * was... so let's refresh the Violation table.
//			 */
//			refreshViolationTable();
//		}
//	};
	
	
	
	/**
	 * Update the plan name label which is displayed in the
	 * overview panel.  This would be necessary in a "Save as..."
	 * situation, since after the save the user is now working
	 * with a different plan with a different name.
	 */
	public void refreshPlanName(){
		if(myLayer.getName().length()>0){
			nameLbl.setText(myLayer.getName());
		}
	}


	
//	private JPanel createResultsPanel(){
//		JPanel resPnl = new JPanel();
//		resPnl.setBackground(Util.lightBlue);
//		resPnl.setBorder(new EmptyBorder(8,8,8,8));
//		resPnl.setLayout(new BorderLayout());
//		
//	//top panel with title and navigation
//		JPanel titlePnl = new JPanel();
//		titlePnl.setLayout(new BorderLayout());
//		titlePnl.setBackground(Util.lightBlue);
//		Dimension size = new Dimension(PlanFocusPanel.this.getPreferredSize().width, 40);
//		titlePnl.setPreferredSize(size);
//		titlePnl.setMaximumSize(size);
//		titlePnl.setMinimumSize(size);
//		titleLbl = new JLabel("Objective 0 of "+myLayer.getObjectives().size());
//		Font titleFont = new Font("Dialog", Font.BOLD, 18);
//		titleLbl.setFont(titleFont);
//		tPnl = new JPanel();
//		tPnl.setBackground(Util.lightBlue);
//		tPnl.add(titleLbl);
//		mosaicLBtn = new JButton(objLAct);
//		mosaicBeginBtn = new JButton(objBeginAct);
//		mosaicRBtn = new JButton(objRAct);
//		mosaicEndBtn = new JButton(objEndAct);
//		JPanel leftPnl = new JPanel();
//		leftPnl.setBackground(Util.lightBlue);
//		leftPnl.add(mosaicBeginBtn);
//		leftPnl.add(mosaicLBtn);
//		JPanel rightPnl = new JPanel();
//		rightPnl.setBackground(Util.lightBlue);
//		rightPnl.add(mosaicRBtn);
//		rightPnl.add(mosaicEndBtn);
//		titlePnl.add(tPnl, BorderLayout.CENTER);
//		titlePnl.add(leftPnl, BorderLayout.WEST);
//		titlePnl.add(rightPnl, BorderLayout.EAST);
//		
//	//Constraints readout	
//		conPnl = new JXTaskPane();
//		conPnl.setTitle("Objective Constraint Results");
//		conPnl.setCollapsed(true);
//		
//		JXTaskPaneContainer conContainer = new JXTaskPaneContainer();
//		conContainer.setLayout(new GridLayout(1,1));
//		conContainer.setBackground(Util.lightBlue);
//		conContainer.add(conPnl);
//		
//		JPanel constraintPnl = new JPanel();
//		constraintPnl.setLayout(new GridLayout(1,1));
//		constraintPnl.setBorder(new EmptyBorder(0,0,8,0));
//		constraintPnl.setBackground(Util.lightBlue);
//		constraintPnl.add(conContainer);
//
//	//Target panel with table
//		targetPnl = new JPanel();
//		targetPnl.setLayout(new GridLayout(1,1));
//		targetPnl.setPreferredSize(new Dimension(targetPnl.getPreferredSize().width,150));
//		targetTbl = loadTargetData();
//		targetSp = new JScrollPane(targetTbl, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		targetPnl.add(targetSp);
//		JPanel tarSumPnl = new JPanel();
//		tarSumPnl.setLayout(new BoxLayout(tarSumPnl, BoxLayout.LINE_AXIS));
//		tarTotLbl = new JLabel(tarTotPrompt);
//		tarConfLbl = new JLabel(tarInConflictPrompt);
//		tarSumPnl.add(Box.createHorizontalGlue());
//		tarSumPnl.add(tarTotLbl);
//		tarSumPnl.add(Box.createHorizontalStrut(20));
//		tarSumPnl.add(tarConfLbl);
//		tarSumPnl.add(Box.createHorizontalGlue());
//		JPanel tarBtnPnl = new JPanel();
//		addTarBtn = new JButton(addTargetAct);
//		addTarBtn.setEnabled(false);
//		tarBtnPnl.add(addTarBtn);
//		JPanel south = new JPanel();
//		south.setLayout(new BoxLayout(south, BoxLayout.PAGE_AXIS));
//		south.add(tarSumPnl);
//		south.add(tarBtnPnl);
//		JPanel tarPnl = new JPanel(new BorderLayout());
//		tarPnl.setBorder(new TitledBorder("Targets"));
//		tarPnl.add(targetPnl, BorderLayout.CENTER);
//		tarPnl.add(south, BorderLayout.SOUTH);
//		
//		//Sequence panel with table
//		seqPnl = new JPanel();
//		seqPnl.setLayout(new GridLayout(1,1));
//		seqPnl.setPreferredSize(new Dimension(seqPnl.getPreferredSize().width, 100));
//		seqSP = new JScrollPane(seqTbl, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		seqPnl.add(seqSP);
//		JPanel seqSumPnl = new JPanel();
//		seqSumPnl.setLayout(new BoxLayout(seqSumPnl, BoxLayout.LINE_AXIS));
//		seqTotLbl = new JLabel(seqTotPrompt);
//		seqConfLbl = new JLabel(seqInConflictPrompt);
//		seqSumPnl.add(Box.createHorizontalGlue());
//		seqSumPnl.add(seqTotLbl);
//		seqSumPnl.add(Box.createHorizontalStrut(20));
//		seqSumPnl.add(seqConfLbl);
//		seqSumPnl.add(Box.createHorizontalGlue());
//		JPanel seqBtnPnl = new JPanel();
//		addSeqBtn = new JButton(addSeqAct);
//		addSeqBtn.setEnabled(false);
//		seqBtnPnl.add(addSeqBtn);
//		JPanel south1 = new JPanel();
//		south1.setLayout(new BoxLayout(south1, BoxLayout.PAGE_AXIS));
//		south1.add(seqSumPnl);
//		south1.add(seqBtnPnl);
//		JPanel sequencePnl = new JPanel(new BorderLayout());
//		sequencePnl.setBorder(new TitledBorder("Sequences"));
//		sequencePnl.add(seqPnl, BorderLayout.CENTER);
//		sequencePnl.add(south1, BorderLayout.SOUTH);
//		
//	//Command panel with table
//		comPnl = new JPanel();
//		comPnl.setLayout(new GridLayout(1,1));
//		comPnl.setPreferredSize(new Dimension(comPnl.getPreferredSize().width, 100));
//		comTbl = new JTable();
//		comSp = new JScrollPane(comTbl, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		comPnl.add(comSp);
//		JPanel comSumPnl = new JPanel();
//		comSumPnl.setLayout(new BoxLayout(comSumPnl, BoxLayout.LINE_AXIS));
//		comTotLbl = new JLabel(comTotPrompt);
//		comConfLbl = new JLabel(comInConflictPrompt);
//		comSumPnl.add(Box.createHorizontalGlue());
//		comSumPnl.add(comTotLbl);
//		comSumPnl.add(Box.createHorizontalStrut(20));
//		comSumPnl.add(comConfLbl);
//		comSumPnl.add(Box.createHorizontalGlue());
//		addComBtn = new JButton(addCommandAct);
//		addComBtn.setEnabled(false); //set true once myLayer has objectives
//		addBlkBtn = new JButton(addBlockAct);
//		addBlkBtn.setEnabled(false);
//		JPanel comBtnPnl = new JPanel();
//		comBtnPnl.add(addComBtn);
//		comBtnPnl.add(Box.createHorizontalStrut(10));
//		comBtnPnl.add(addBlkBtn);
//		JPanel comBotPnl = new JPanel();
//		comBotPnl.setLayout(new BoxLayout(comBotPnl, BoxLayout.PAGE_AXIS));
//		comBotPnl.add(comSumPnl);
//		comBotPnl.add(comBtnPnl);
//		JPanel commandPnl = new JPanel(new BorderLayout());
//		commandPnl.setBorder(new TitledBorder("Commands"));
//		commandPnl.add(comPnl, BorderLayout.CENTER);
//		commandPnl.add(comBotPnl, BorderLayout.SOUTH);
//		
//	//create split panes for the target, sequence and command panel
//		topSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, tarPnl, sequencePnl);
//		topSplit.setBorder(BorderFactory.createEmptyBorder());
//		botSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, topSplit, commandPnl);
//		botSplit.setBorder(BorderFactory.createEmptyBorder());
//		JPanel cenPnl = new JPanel(new BorderLayout());
//		cenPnl.add(constraintPnl, BorderLayout.NORTH);
//		cenPnl.add(botSplit, BorderLayout.CENTER);
//
//	//Add all panels to results panel
//		resPnl.add(titlePnl, BorderLayout.NORTH);
//		resPnl.add(cenPnl, BorderLayout.CENTER);
//
//		return resPnl;
//	}
//	
//	
//	private JPanel createViolationPanel(){
//		JPanel violationPnl = new JPanel();
//		violationPnl.setLayout(new BorderLayout());
//		violationPnl.setBackground(Util.lightBlue);
//		violationPnl.setBorder(new EmptyBorder(10, 10, 10, 10));
//		
//		vioPnl = new JPanel();
//		vioPnl.setLayout(new GridLayout(1,1));
//		vioTbl = loadViolationTable();
//		vioSp = new JScrollPane(vioTbl, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		vioPnl.add(vioSp);
//		
//		vioChk = new JCheckBox(violationCheckingAct);
//		JPanel chkPnl = new JPanel();
//		chkPnl.add(vioChk);
//		
//		JPanel centerPnl = new JPanel();
//		centerPnl.setBorder(new TitledBorder("Plan Violations"));
//		centerPnl.setLayout(new BorderLayout());
//		centerPnl.add(chkPnl, BorderLayout.NORTH);
//		centerPnl.add(vioPnl, BorderLayout.CENTER);
//		
//		JPanel infoPnl = new JPanel(new GridBagLayout());
//		infoPnl.setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Violation Information"), new EmptyBorder(5, 0, 0, 0)));
//		
//		//title
//		JPanel titlePnl = new JPanel(new GridLayout(1,1));
//		titlePnl.setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Title"), new EmptyBorder(0, 5, 0, 5)));
//		vioTitleTA = new JTextArea();
//		vioTitleTA.setLineWrap(true);
//		vioTitleTA.setWrapStyleWord(true);
//		vioTitleTA.setEditable(false);
//		vioTitleTA.setBorder(BorderFactory.createEmptyBorder());
//		vioTitleTA.setBackground(Util.panelGrey);
//		vioTitleTA.setPreferredSize(new Dimension(0,15));
//		titlePnl.add(vioTitleTA);
//		//description
//		JPanel descPnl = new JPanel(new GridLayout(1,1));
//		descPnl.setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Description"), new EmptyBorder(0,5,0,5)));
//		descPnl.setPreferredSize(new Dimension(0,100));
//		descPnl.setMinimumSize(new Dimension(0,100));
//		vioDescTA = new JTextArea();
//		vioDescTA.setWrapStyleWord(true);
//		vioDescTA.setLineWrap(true);
//		vioDescTA.setEditable(false);
//		vioDescTA.setBorder(BorderFactory.createEmptyBorder());
//		vioDescTA.setBackground(Util.panelGrey);
//		JScrollPane descSP = new JScrollPane(vioDescTA);
//		descSP.setBorder(BorderFactory.createEmptyBorder());
//		descPnl.add(descSP);
//		//waivers
//		JPanel waiverPnl = new JPanel(new GridLayout(1,1));
//		waiverPnl.setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Waiver Text"), new EmptyBorder(0,5,0,5)));
//		waiverPnl.setMinimumSize(new Dimension(0,60));
//		waiverPnl.setPreferredSize(new Dimension(0,60));
//		vioWaiverTA = new JTextArea();
//		vioWaiverTA.setWrapStyleWord(true);
//		vioWaiverTA.setLineWrap(true);
//		vioWaiverTA.setEditable(false);
//		vioWaiverTA.setBorder(BorderFactory.createEmptyBorder());
//		vioWaiverTA.setBackground(Util.panelGrey);
//		JScrollPane waiverSP = new JScrollPane(vioWaiverTA);
//		waiverSP.setBorder(BorderFactory.createEmptyBorder());
//		waiverPnl.add(waiverSP);
//		//buttons
//		vioSetWaiverBtn = new JButton(waiverAct);
//		vioSetWaiverBtn.setEnabled(false);
//		vioRemoveWaiverBtn = new JButton(removeWaiverAct);
//		vioRemoveWaiverBtn.setEnabled(false);
//		JPanel buttonPnl = new JPanel();
//		buttonPnl.add(vioSetWaiverBtn);
//		buttonPnl.add(Box.createHorizontalStrut(5));
//		buttonPnl.add(vioRemoveWaiverBtn);
//		
//		int row = 0;
//		infoPnl.add(titlePnl, new GridBagConstraints(0, row, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, in, pad, pad));
//		infoPnl.add(descPnl, new GridBagConstraints(0, ++row, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, in, pad, pad));
//		infoPnl.add(waiverPnl, new GridBagConstraints(0, ++row, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, in, pad, pad));
//		infoPnl.add(buttonPnl, new GridBagConstraints(0, ++row, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, in, pad, pad));
//
//		JPanel outerInfoPnl = new JPanel(new GridLayout(1,1));
//		outerInfoPnl.setBackground(Util.lightBlue);
//		outerInfoPnl.setBorder(new EmptyBorder(20, 0, 0, 0));
//		outerInfoPnl.setPreferredSize(new Dimension((int)centerPnl.getPreferredSize().getWidth(), 300));
//		outerInfoPnl.add(infoPnl);
//
//		violationPnl.add(centerPnl, BorderLayout.CENTER);
//		violationPnl.add(outerInfoPnl, BorderLayout.SOUTH);
//		
//		return violationPnl;
//	}
//	
//	private AbstractAction violationCheckingAct = new AbstractAction("Disable All Flight Rule Checking") {
//		public void actionPerformed(ActionEvent e) {
//			//it is possible to have the planning layer in a state where the violations
//			// are not being run. If this is the case, disable the following buttons 
//			// and checkboxes: Proposed Plan, Approved Plan, Save..., Save As...
//			FlightRuleManager mgr = FlightRuleManager.getInstance();
//			if(vioChk.isSelected()){
//				mgr.disableRuleChecks();
//				savedProposedChkEnableState = proposedChk.isEnabled();
//				savedApproveChkEnableState = approveChk.isEnabled();
//				savedSaveBtnEnableState = saveBtn.isEnabled();
//				savedSaveAsBtnEnabledState = saveAsBtn.isEnabled();
//				proposedChk.setEnabled(false);
//				approveChk.setEnabled(false);
//				saveBtn.setEnabled(false);
//				saveAsBtn.setEnabled(false);
//			}
//			else{
//				mgr.enableRuleChecks();
//				proposedChk.setEnabled(savedProposedChkEnableState);
//				approveChk.setEnabled(savedApproveChkEnableState);
//				saveBtn.setEnabled(savedSaveBtnEnableState);
//				saveAsBtn.setEnabled(savedSaveAsBtnEnabledState);
//				myLayer.retestAllRules();
//				refreshViolationTable();
//			}
//		}
//	};
//	
//	
//	private AbstractAction waiverAct = new AbstractAction("Set Waiver") {
//		public void actionPerformed(ActionEvent e) {
//			int index = vioTbl.getSelectedRow();
//			
//			ViolationTableModel vioMdl = (ViolationTableModel)vioTbl.getModel();
//			TestableComponent tc = vioMdl.getComponent(vioTblIndex);
//			
//			PlanCheckViolations frv = tc.getViolations();
//			PlanCheckViolation rule = frv.getViolation(vioMdl.getSelectedRuleName(vioTblIndex));
//			PlanCheckDetails frd = PlanCheckInfoTable.getInstance().getDetails(vioMdl.getSelectedRuleName(vioTblIndex));
//			
//			new WaiverDialog(PlanFocusPanel.this.getFrame(), PlanFocusPanel.this, frv, rule);
//			
//			// If a waiver was added, we need to save this testable component (including its waivers) to the
//			// Waiver Backup.
//			if (myLayer != null && frv.getWaivers().size() > 0) {
//			    myLayer.getWaiverBackup().addWaivedComponent(tc);
//			}
//			
//			//set the selection in the table back to what it was to begin with
//			vioTbl.setRowSelectionInterval(index, index);
//		}
//	};
//	
//	private AbstractAction removeWaiverAct = new AbstractAction("Remove Waiver") {
//		public void actionPerformed(ActionEvent e) {
//			int index = vioTbl.getSelectedRow();
//			
//			ViolationTableModel vioMdl = (ViolationTableModel)vioTbl.getModel();
//			TestableComponent tc = vioMdl.getComponent(vioTblIndex);
//			
//			PlanCheckViolations frv = tc.getViolations();
//			
//			String name = vioMdl.getSelectedRuleName(vioTblIndex);
//			
//			FlightRuleWaiver waiver = frv.getWaiver(name);
//			
//			frv.removeWaiver(waiver);
//			myLayer.getWaiverBackup().removeWaiver(tc, waiver);
//			
//			refreshViolationInfoPanel();
//			refreshViolationTable();
//			
//			//set the selection in the table back to what it was to begin with
//			vioTbl.setRowSelectionInterval(index, index);
//		}
//	};
//	
//	private JTable loadViolationTable(){
//		JTable table = new ViolationTable(new ViolationTableModel(myLayer.getAllTestableComponentsWithViolations()));
//		table.getTableHeader().setReorderingAllowed(false);
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		table.getSelectionModel().addListSelectionListener(rowListener);
//		
//		TableColumnAdjuster tca = new TableColumnAdjuster(table);
//		tca.adjustColumns();
//		
//		return table;
//	}
//	
//	private ListSelectionListener rowListener = new ListSelectionListener() {
//		public void valueChanged(ListSelectionEvent e) {
//			vioTblIndex = vioTbl.getSelectedRow();
//			refreshViolationInfoPanel();
//		}
//	};
//	
//	/**
//	 * Implementation of:
//	 * @see edu.asu.jmars.layer.obs.osirisrex.ui.ViolationTableOwnerInterface#refreshViolationTable()
//	 * 
//	 * Is used to update the violation table which is on the violation
//	 * panel.  This could be necessary whenever any of the {@link TestableComponent}
//	 * with violations have been modified (ie. had a waiver added, etc.).
//	 */
//	public void refreshViolationTable(){
//		vioPnl.remove(vioSp);
//		vioTbl = loadViolationTable();
//		vioSp = new JScrollPane(vioTbl);
//		vioPnl.add(vioSp);
//		vioPnl.validate();
//	}
//	
//	/** 
//	 * Implementation of:
//	 * @see edu.asu.jmars.layer.obs.osirisrex.ui.ViolationTableOwnerInterface#refreshViolationInfoPanel()
//	 * 
//	 * Used to update the violation readout panel.  This panel has title,
//	 * description and waiver information about a selected violation from
//	 * the violation table.
//	 */
//	public void refreshViolationInfoPanel(){
//		//if no component is selected, clear the readout.
//		if(vioTblIndex<0){
//			vioTitleTA.setText("");
//			vioDescTA.setText("");
//			vioWaiverTA.setText("");
//			return;
//		}
//		
//		ViolationTableModel vioMdl = (ViolationTableModel)vioTbl.getModel();
//		TestableComponent tc = vioMdl.getComponent(vioTblIndex);
//		
//		PlanCheckViolations frv = tc.getViolations();
//		PlanCheckViolation violation = frv.getViolation(vioMdl.getSelectedRuleName(vioTblIndex));
//		PlanCheckDetails frd = PlanCheckInfoTable.getInstance().getDetails(vioMdl.getSelectedRuleName(vioTblIndex));
//		
//		if(frd!=null){
//			vioTitleTA.setText(frd.getTitle());
//			
//			vioDescTA.setText(violation.getAdditionalInfo()+frd.getDescription());
//			//set the scroll pane back to the top
//			vioDescTA.setCaretPosition(0);
//			
//			FlightRuleWaiver waiver = frv.getWaiver(frd.getName());
//			
//			if(waiver!=null){
//				vioWaiverTA.setText(waiver.getWaiverText());
//				vioRemoveWaiverBtn.setEnabled(true);
//				//scroll back to the top
//				vioWaiverTA.setCaretPosition(0);
//			}else{
//				vioWaiverTA.setText("");
//				vioRemoveWaiverBtn.setEnabled(false);
//			}
//			vioSetWaiverBtn.setEnabled(frd.isWaiverPermitted());
//		}else{
//			vioTitleTA.setText("");
//			descTA.setText("");
//			vioWaiverTA.setText("");
//			vioRemoveWaiverBtn.setEnabled(false);
//			vioSetWaiverBtn.setEnabled(false);
//			
//		}
//		
//		//if the layer is read only, disable the set and remove waiver buttons
//		if(!myLayer.isEditable()){
//			vioSetWaiverBtn.setEnabled(false);
//			vioRemoveWaiverBtn.setEnabled(false);
//		}
//	}
//	
//
//	private JPanel createKernelPanel(){
//		JPanel kernelPnl = new JPanel();
//		kernelPnl.setBackground(Util.lightBlue);
//		kernelPnl.setLayout(new BorderLayout());
//		kernelPnl.setBorder(new EmptyBorder(5,5,5,5));
//		
//		JPanel mainPnl = new JPanel();
//		mainPnl.setBorder(new TitledBorder("Kernel Information"));
//		mainPnl.setLayout(new GridBagLayout());
//		
//		JLabel configLbl = new JLabel("Config: "+myLayer.getKernel());
//		Font configFont = new Font("Dialog", Font.ITALIC, 17);
//		configLbl.setFont(configFont);
//		mainPnl.add(configLbl, new GridBagConstraints(0,0,2,1,0,0,GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, 3*pad));
//		
//		JLabel filesLbl = new JLabel("File names from spice kernel:");
//		JLabel typesLbl = new JLabel("File types:");
//		mainPnl.add(filesLbl, new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.LINE_START,GridBagConstraints.NONE,in,pad,pad));
//		mainPnl.add(typesLbl, new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.LINE_START,GridBagConstraints.NONE,in,pad,pad));
//		
//		Font font = new Font("Dialog", Font.PLAIN, 14);
//		int count = 2;
//		for(SpiceKernel s : myLayer.getKernelFiles()){
//			JLabel nameLbl = new JLabel(s.getKernelFileName());
//			nameLbl.setFont(font);
//			JLabel typeLbl = new JLabel(s.getKernelType());
//			typeLbl.setFont(font);
//			mainPnl.add(nameLbl, new GridBagConstraints(0,count,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//			mainPnl.add(typeLbl, new GridBagConstraints(1,count,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,2*pad,pad));
//			count++;
//		}
//		
//		//add the spacecraft SPK override file to the list
//		mainPnl.add(Box.createVerticalStrut(3), new GridBagConstraints(0, count++, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
//		JLabel spkTitleLbl = new JLabel("Additional S/C SPK:");
//		if(myLayer.getSpacecraftSPK()!=null && myLayer.getSpacecraftSPK().length()>0){
//			JLabel spkLbl = new JLabel(myLayer.getSpacecraftSPK());
//			JLabel spkTypeLbl = new JLabel("SPK");
//			spkLbl.setFont(font);
//			spkTypeLbl.setFont(font);
//			mainPnl.add(spkTitleLbl, new GridBagConstraints(0, count++, 1, 1, 0, 0, GridBagConstraints.PAGE_END, GridBagConstraints.NONE, in, pad, pad));
//			mainPnl.add(spkLbl, new GridBagConstraints(0, count, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
//			mainPnl.add(spkTypeLbl, new GridBagConstraints(1, count, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
//		}
//		
//		kernelPnl.add(mainPnl, BorderLayout.NORTH);
//		return kernelPnl;
//	}
//	
//	
//	private JPanel createSettingsPanel(){
//		JPanel settingsPnl = new JPanel();
//		settingsPnl.setBackground(Util.lightBlue);
//
//		
//		JPanel mSettingsPnl = new JPanel(new GridBagLayout());
//		mSettingsPnl.setBorder(new TitledBorder("Mosaic View Settings"));
//		
//		JLabel radLbl = new JLabel("Radius Color:");
//		radCC = new ColorCombo();
//		radCC.setColor(PlanningSettings.radiusColor);
//		radCC.setPreferredSize(new Dimension(80, radCC.getPreferredSize().height));
//		radCC.addActionListener(colorBoxListener);
//		
//		JLabel slewLbl = new JLabel("Slew Color:");
//		slewCC = new ColorCombo();
//		slewCC.setColor(PlanningSettings.slewColor);
//		slewCC.setPreferredSize(new Dimension(80, slewCC.getPreferredSize().height));
//		slewCC.addActionListener(colorBoxListener);
//		
//		showRexisChk = new JCheckBox(showRexisAct);
//		showRexisChk.setSelected(PlanningSettings.showREXIS);
//		rexisCC = createColorCombo(PlanningSettings.REXISColor);
//
//		showOLAChk = new JCheckBox(showOLAAct);
//		showOLAChk.setSelected(PlanningSettings.showOLA);
//		olaCC = createColorCombo(PlanningSettings.OLAColor);
//
//		showOTESChk = new JCheckBox(showOTESAct);
//		showOTESChk.setSelected(PlanningSettings.showOTES);
//		otesCC = createColorCombo(PlanningSettings.OTESColor);
//
//		showOVIRSChk = new JCheckBox(showOVIRSAct);
//		showOVIRSChk.setSelected(PlanningSettings.showOVIRS);
//		ovirsCC = createColorCombo(PlanningSettings.OVIRSColor);
//
//		showMapCamChk = new JCheckBox(showMapCamAct);
//		showMapCamChk.setSelected(PlanningSettings.showMapCam);
//		mapCamCC = createColorCombo(PlanningSettings.MapCamColor);
//
//		showPolyCamChk = new JCheckBox(showPolyCamAct);
//		showPolyCamChk.setSelected(PlanningSettings.showPolyCam);
//		polyCamCC = createColorCombo(PlanningSettings.PolyCamColor);
//
//		showSamCamChk = new JCheckBox(showSamCamAct);
//		showSamCamChk.setSelected(PlanningSettings.showSamCam);
//		samCamCC = createColorCombo(PlanningSettings.SamCamColor);
//		
//		showOldFootprintsChk = new JCheckBox(showOldFtAct);
//		showOldFootprintsChk.setSelected(PlanningSettings.showOldFootprints);
//		oldFtCC = createColorCombo(PlanningSettings.oldFootprintColor);
//		
//		fitCamFpChk = new JCheckBox(fitCamFpAct);
//		fitCamFpChk.setSelected(PlanningSettings.fitCamFp);
//
//		fillCamFpChk = new JCheckBox(fillCamFpAct);
//		fillCamFpChk.setSelected(PlanningSettings.fillCamFp);
//
//		showNavCamChk = new JCheckBox(showNavCamAct);
//		showNavCamChk.setSelected(PlanningSettings.showNavCam);
//		navCamCC = createColorCombo(PlanningSettings.NavCamColor);
//		
//		showTrackChk = new JCheckBox(showGroundTrackAct);
//		showTrackChk.setSelected(PlanningSettings.showTrack);
//		trackCC = createColorCombo(PlanningSettings.trackColor);
//		
//		showStarsChk = new JCheckBox(showStarsAct);
//		showStarsChk.setSelected(PlanningSettings.showStars);
//		starsCC = createColorCombo(PlanningSettings.starColor);
//
//		JLabel olaLbl = new JLabel("OLA Sample Interval (s):");
//		olaScaleTf = new JTextField(Double.toString(PlanningSettings.olaInterval), 5);
//		olaScaleTf.addActionListener(tfAct);
//		
//		
//		JPanel uncPnl = new JPanel();
//		uncPnl.setBorder(new TitledBorder("Uncertainty Display"));
//		uncPnl.setLayout(new GridLayout(2,1));
//		radialUncChk = new JCheckBox(showRadialUncAct);
//		radialUncChk.setSelected(PlanningSettings.showRadialUncertainty);
//		trackUncChk = new JCheckBox(showTrackUncAct);
//		trackUncChk.setSelected(PlanningSettings.showTrackUncertainty);
//		uncPnl.add(radialUncChk);
//		uncPnl.add(trackUncChk);
//		
//		JPanel debugPnl = createDebugPanel();
//		
//		int row = -1;
//		mSettingsPnl.add(radLbl, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(radCC, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(new JLabel("      "), new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(pad,pad*2,pad,pad*2),pad,pad));
//		mSettingsPnl.add(slewLbl, new GridBagConstraints(3,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(slewCC, new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(new JLabel(), new GridBagConstraints(5,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//
//		mSettingsPnl.add(showPolyCamChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(polyCamCC, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(showMapCamChk, new GridBagConstraints(3,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(mapCamCC, new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(showSamCamChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(samCamCC, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(showOVIRSChk, new GridBagConstraints(3,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(ovirsCC, new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(showOTESChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(otesCC, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(showOLAChk, new GridBagConstraints(3,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(olaCC, new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(showRexisChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(rexisCC, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(showNavCamChk, new GridBagConstraints(3,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(navCamCC, new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(showOldFootprintsChk, new GridBagConstraints(0, ++row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
//		mSettingsPnl.add(oldFtCC, new GridBagConstraints(1, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
//		mSettingsPnl.add(showTrackChk, new GridBagConstraints(3, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
//		mSettingsPnl.add(trackCC, new GridBagConstraints(4, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
//		mSettingsPnl.add(showStarsChk, new GridBagConstraints(0, ++row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
//		mSettingsPnl.add(starsCC, new GridBagConstraints(1, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
//
//		mSettingsPnl.add(olaLbl, new GridBagConstraints(3,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(olaScaleTf, new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//
//		mSettingsPnl.add(fitCamFpChk, new GridBagConstraints(0,++row,2,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(fillCamFpChk, new GridBagConstraints(3,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		mSettingsPnl.add(uncPnl, new GridBagConstraints(0,++row,6,1,0,0,GridBagConstraints.PAGE_START,GridBagConstraints.HORIZONTAL,in,pad,pad));
//		mSettingsPnl.add(debugPnl, new GridBagConstraints(0,++row,6,1,0,0,GridBagConstraints.PAGE_START,GridBagConstraints.HORIZONTAL,in,pad,pad));
//		
//		settingsPnl.add(mSettingsPnl);
//		return settingsPnl;
//	}
//	
//	private JPanel createDebugPanel(){
//		JPanel debugPnl = new JPanel();
//		debugPnl.setBorder(new TitledBorder("Debugging Display"));
//		debugPnl.setLayout(new GridBagLayout());		
//
//		showVectorsAtTargetsChk = new JCheckBox(checkBoxAct);
//		showVectorsAtTargetsChk.setText("Vecs at Targets? ");
//		showVectorsAtTargetsChk.setSelected(PlanningSettings.showVectorsAtTargets);
//		showVectorsAtImagesChk = new JCheckBox(checkBoxAct);
//		showVectorsAtImagesChk.setText("Vecs at Images? ");
//		showVectorsAtImagesChk.setSelected(PlanningSettings.showVectorsAtImages);
//		showVectorsBetweenTargetsChk = new JCheckBox(checkBoxAct);
//		showVectorsBetweenTargetsChk.setText("Vecs btwn Tgts? ");
//		showVectorsBetweenTargetsChk.setSelected(PlanningSettings.showVectorsBetweenTargets);
//		numVectorsBetweenTargetsSpn = new JSpinner(new SpinnerNumberModel(PlanningSettings.numVectorsBetweenTargets, 1, 100, 1));
//		numVectorsBetweenTargetsSpn.getModel().addChangeListener(numVectorsBetweenTargetsSpnListener);
//		showOrbitTrackChk = new JCheckBox(checkBoxAct);
//		showOrbitTrackChk.setText("Orbit Track? ");
//		showOrbitTrackChk.setSelected(PlanningSettings.showOrbitTrack);
//		orbitTrackStepSzTf = new JTextField(Double.toString(PlanningSettings.orbitTrackStepSz), 5);
//		orbitTrackStepSzTf.addActionListener(tfAct);		
//		showOrgLocChk = new JCheckBox(checkBoxAct);
//		showOrgLocChk.setText("Org Loc? ");
//		showOrgOffLocChk = new JCheckBox(checkBoxAct);
//		showOrgOffLocChk.setText("Org+Off Loc? ");
//		showSigmaLocChk = new JCheckBox(checkBoxAct);
//		showSigmaLocChk.setText("Sigma Loc? ");
//		showSigmaLocSigmaTf = new JTextField(Double.toString(PlanningSettings.showSigmaLocSigmaValue), 5);
//		showSigmaLocSigmaTf.addActionListener(tfAct);
//		showOrgLocCC = createColorCombo(PlanningSettings.orgLocColor);
//		showOrgOffLocCC = createColorCombo(PlanningSettings.orgOffLocColor);
//		showSigmaLocCC = createColorCombo(PlanningSettings.sigmaLocColor);
//		showSigmaLocSigmaCC = createColorCombo(PlanningSettings.sigmaLocSigmaColor);
//		showScAxesChk = new JCheckBox(checkBoxAct);
//		showScAxesChk.setText("S/c Axes?");
//		showScAxesChk.setSelected(PlanningSettings.showScAxes);
//		scAxesScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleScAxes), 5);
//		scAxesScaleTf.addActionListener(tfAct);
//		showJ2kAxesChk = new JCheckBox(checkBoxAct);
//		showJ2kAxesChk.setText("J2000 Axes?");
//		showJ2kAxesChk.setSelected(PlanningSettings.showJ2kAxes);
//		j2kAxesScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleJ2kAxes), 5);
//		j2kAxesScaleTf.addActionListener(tfAct);
//		showRtnAxesChk = new JCheckBox(checkBoxAct);
//		showRtnAxesChk.setText("RTN Axes?");
//		showRtnAxesChk.setSelected(PlanningSettings.showRTNAxes);
//		rtnAxesScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleRtnAxes), 5);
//		rtnAxesScaleTf.addActionListener(tfAct);
//		showSamAxesChk = new JCheckBox(checkBoxAct);
//		showSamAxesChk.setText("SAM Axes?");
//		showSamAxesChk.setSelected(PlanningSettings.showSAMAxes);
//		samAxesScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleSamAxes), 5);
//		samAxesScaleTf.addActionListener(tfAct);
//		showSunChk = new JCheckBox(checkBoxAct);
//		showSunChk.setText("\u2192Sun(\u2600)?");
//		showSunChk.setSelected(PlanningSettings.showSc2Sun);
//		sunScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleSc2Sun), 5);
//		sunScaleTf.addActionListener(tfAct);
//		showEarthChk = new JCheckBox(checkBoxAct);
//		showEarthChk.setText("\u2192Earth(\u2641)?");
//		showEarthChk.setSelected(PlanningSettings.showSc2Earth);
//		earthScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleSc2Earth), 5);
//		earthScaleTf.addActionListener(tfAct);
//		showMoonChk = new JCheckBox(checkBoxAct);
//		showMoonChk.setText("\u2192Moon(\u263E)?");
//		showMoonChk.setSelected(PlanningSettings.showSc2Moon);
//		moonScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleSc2Moon), 5);
//		moonScaleTf.addActionListener(tfAct);
//		showSc2BodyChk = new JCheckBox(checkBoxAct);
//		showSc2BodyChk.setText("\u2192body?");
//		showSc2BodyChk.setSelected(PlanningSettings.showSc2Body);
//		sc2BodyScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleSc2Body), 5);
//		sc2BodyScaleTf.addActionListener(tfAct);
//		showSc2TgtChk = new JCheckBox(checkBoxAct);
//		showSc2TgtChk.setText("\u2192tgt?");
//		showSc2TgtChk.setSelected(PlanningSettings.showSc2Tgt);
//		showRtnEllipsoidChk = new JCheckBox(checkBoxAct);
//		showRtnEllipsoidChk.setText("RTN ellipsoid?");
//		showRtnEllipsoidChk.setSelected(PlanningSettings.showRTNEllipsoid);
//		showSigmaEllipsoidChk = new JCheckBox(checkBoxAct);
//		showSigmaEllipsoidChk.setText("\u03C3 ellipsoid?");
//		showSigmaEllipsoidChk.setSelected(PlanningSettings.showSigmaUncEllipsoid);
//		baseLineWidthTf = new JTextField(Integer.toString(PlanningSettings.baseLineWidth), 5);
//		baseLineWidthTf.addActionListener(tfAct);
//		showVelJ2kChk = new JCheckBox(checkBoxAct);
//		showVelJ2kChk.setText("Vel j2k?");
//		showVelJ2kChk.setSelected(PlanningSettings.showVel);
//		velJ2kScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleVel), 5);
//		velJ2kScaleTf.addActionListener(tfAct);
//		showVelBChk = new JCheckBox(checkBoxAct);
//		showVelBChk.setText("VelB?");
//		showVelBChk.setSelected(PlanningSettings.showVelB);
//		velBScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleVelB), 5);
//		velBScaleTf.addActionListener(tfAct);
//		showAngmChk = new JCheckBox(checkBoxAct);
//		showAngmChk.setText("AngM?");
//		showAngmChk.setSelected(PlanningSettings.showMoment);
//		angmScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleMoment), 5);
//		angmScaleTf.addActionListener(tfAct);
//		showMotionChk = new JCheckBox(checkBoxAct);
//		showMotionChk.setText("Motion?");
//		showMotionChk.setSelected(PlanningSettings.showMotion);
//		motionScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleMotion), 5);
//		motionScaleTf.addActionListener(tfAct);
//		showBodyVelWrtSunChk = new JCheckBox(checkBoxAct);
//		showBodyVelWrtSunChk.setText("vbs?");
//		showBodyVelWrtSunChk.setToolTipText("Velocity of Body w.r.t. Sun (J2000)");
//		showBodyVelWrtSunChk.setSelected(PlanningSettings.showBodyVelWrtSun);
//		bodyVelWrtSunScaleTf = new JTextField(Integer.toString(PlanningSettings.scaleBodyVelWrtSun), 5);
//		bodyVelWrtSunScaleTf.addActionListener(tfAct);
//		
//		int row = -1;
//		debugPnl.add(showScAxesChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(scAxesScaleTf, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showJ2kAxesChk, new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(j2kAxesScaleTf, new GridBagConstraints(3,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showRtnAxesChk, new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(rtnAxesScaleTf, new GridBagConstraints(5,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showSamAxesChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(samAxesScaleTf, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showBodyVelWrtSunChk, new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(bodyVelWrtSunScaleTf, new GridBagConstraints(3,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showSunChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(sunScaleTf, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showEarthChk, new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(earthScaleTf, new GridBagConstraints(3,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showMoonChk, new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(moonScaleTf, new GridBagConstraints(5,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showSc2BodyChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(sc2BodyScaleTf, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showSc2TgtChk, new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showMotionChk, new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(motionScaleTf, new GridBagConstraints(5,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showVelJ2kChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(velJ2kScaleTf, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showVelBChk, new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(velBScaleTf, new GridBagConstraints(3,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showAngmChk, new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(angmScaleTf, new GridBagConstraints(5,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showVectorsAtTargetsChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(createVecColorPanel(), new GridBagConstraints(2,row,4,3,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showVectorsAtImagesChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showVectorsBetweenTargetsChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(numVectorsBetweenTargetsSpn, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showOrbitTrackChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(orbitTrackStepSzTf, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad)); debugPnl.add(new JLabel());
//		debugPnl.add(showRtnEllipsoidChk, new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showSigmaEllipsoidChk, new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showOrgLocChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showOrgLocCC, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showOrgOffLocChk, new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showOrgOffLocCC, new GridBagConstraints(5,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad)); 
//		debugPnl.add(showSigmaLocChk, new GridBagConstraints(0,++row,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showSigmaLocCC, new GridBagConstraints(1,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showSigmaLocSigmaCC, new GridBagConstraints(2,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(showSigmaLocSigmaTf, new GridBagConstraints(3,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(new JLabel("Line width"), new GridBagConstraints(4,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		debugPnl.add(baseLineWidthTf, new GridBagConstraints(5,row,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,in,pad,pad));
//		
//		return debugPnl;
//	}
//	
//	private JPanel createVecColorPanel(){
//		JPanel vecColorPanel = new JPanel();
//		vecColorPanel.setLayout(new GridLayout(3,5));
//		
//		PropertyChangeListener colorChangeListener = new PropertyChangeListener() {
//			@Override
//			public void propertyChange(PropertyChangeEvent evt) {
//				if ("foreground".equals(evt.getPropertyName()) || "background".equals(evt.getPropertyName())){
//					ActionEvent e = new ActionEvent(evt.getSource(), ActionEvent.ACTION_PERFORMED,
//							((ColorButton)evt.getSource()).getActionCommand());
//					colorBoxListener.actionPerformed(e);
//				}
//			}
//		};
//		
//		xButton = new ColorButton("X", PlanningSettings.scXColor);
//		yButton = new ColorButton("Y", PlanningSettings.scYColor);
//		zButton = new ColorButton("Z", PlanningSettings.scZColor);
//		sunButton = new ColorButton("\u2600", PlanningSettings.sc2SunColor);
//		velButton = new ColorButton("V", PlanningSettings.velColor);
//		velBButton = new ColorButton("v", PlanningSettings.velBColor);
//		motionButton = new ColorButton("d", PlanningSettings.motionColor);
//		momentButton = new ColorButton("M", PlanningSettings.momentColor);
//		sc2EarthButton = new ColorButton("\u2641", PlanningSettings.sc2EarthColor);
//		sc2MoonButton = new ColorButton("\u263E", PlanningSettings.sc2MoonColor);
//		sc2BodyButton = new ColorButton("b", PlanningSettings.sc2BodyColor);
//		bodyVelWrtSunButton = new ColorButton("vbs", PlanningSettings.bodyVelWrtSunColor);
//		sigmaUncEllipsoidButton = new ColorButton("\u03C3U", PlanningSettings.sigmaUncEllipsoidColor);
//		rtnEllipsoidButton = new ColorButton("rtn", PlanningSettings.rtnEllipsoidColor);
//
//		xButton.addPropertyChangeListener(colorChangeListener);
//		yButton.addPropertyChangeListener(colorChangeListener);
//		zButton.addPropertyChangeListener(colorChangeListener);
//		sunButton.addPropertyChangeListener(colorChangeListener);
//		velButton.addPropertyChangeListener(colorChangeListener);
//		velBButton.addPropertyChangeListener(colorChangeListener);
//		motionButton.addPropertyChangeListener(colorChangeListener);
//		momentButton.addPropertyChangeListener(colorChangeListener);
//		sc2EarthButton.addPropertyChangeListener(colorChangeListener);
//		sc2MoonButton.addPropertyChangeListener(colorChangeListener);
//		sc2BodyButton.addPropertyChangeListener(colorChangeListener);
//		bodyVelWrtSunButton.addPropertyChangeListener(colorChangeListener);
//		sigmaUncEllipsoidButton.addPropertyChangeListener(colorChangeListener);
//		rtnEllipsoidButton.addPropertyChangeListener(colorChangeListener);
//		
//        xButton.setToolTipText("Spacecraft X axis");
//        yButton.setToolTipText("Spacecraft Y axis");
//        zButton.setToolTipText("Spacecraft Z axis");
//        sunButton.setToolTipText("Sun vector");
//        velButton.setToolTipText("Spacecraft velocity vector (J2000)");
//        velBButton.setToolTipText("Spacecraft velocity vector (Bennu)");
//        motionButton.setToolTipText("Slew direction vector");
//        momentButton.setToolTipText("Angular momentum vector");
//        sc2EarthButton.setToolTipText("S/c to Earth vector");
//        sc2MoonButton.setToolTipText("S/c to Moon vector");
//        sc2BodyButton.setToolTipText("S/c to Body vector");
//        bodyVelWrtSunButton.setToolTipText("Vel body w.r.t. sun (J2000)");
//        sigmaUncEllipsoidButton.setToolTipText("Sigma uncertainty ellipsoid");
//        rtnEllipsoidButton.setToolTipText("1-sigma RTN uncertainty ellipsoid");
//		
//		vecColorPanel.add(xButton);
//		vecColorPanel.add(yButton);
//		vecColorPanel.add(zButton);
//		vecColorPanel.add(sunButton);
//		vecColorPanel.add(velButton);
//		vecColorPanel.add(velBButton);
//		vecColorPanel.add(motionButton);
//		vecColorPanel.add(momentButton);
//		vecColorPanel.add(sc2EarthButton);
//		vecColorPanel.add(sc2MoonButton);
//		vecColorPanel.add(sc2BodyButton);
//		vecColorPanel.add(bodyVelWrtSunButton);
//		vecColorPanel.add(sigmaUncEllipsoidButton);
//		vecColorPanel.add(rtnEllipsoidButton);
//		
//		return vecColorPanel;
//	}
//	
//	private JPanel createFinalizePanel(){
//		JPanel finalizePnl = new JPanel();
//		finalizePnl.setBackground(Util.lightBlue);
//		finalizePnl.setLayout(new GridBagLayout());
//		finalizePnl.setBorder(new EmptyBorder(5,5,5,5));
//		
//		//Save Plan Panel	
//		JPanel savePnl = new JPanel();
//		savePnl.setBorder(new TitledBorder("Save Plan to Database"));
//		savePnl.setLayout(new BoxLayout(savePnl, BoxLayout.PAGE_AXIS));
//		JPanel proposedPnl = new JPanel();
//		proposedChk = new JCheckBox("Proposed Plan");
//		proposedChk.setEnabled(myLayer.isEditable()); //read only
//		proposedChk.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			    
//			    //if this plan has custom slew rates, it cannot be proposed
//                if(myLayer.hasCustomSlewRates()){
//                    JOptionPane.showMessageDialog(proposedChk, 
//                            "Cannot propose this plan because the official ATL slew rates have\n"
//                            + "been overridden with custom values. This plan will need to be\n"
//                            + "recreated with the official slew rates in order to be proposed.",
//                            "Unofficial Slew Rates", JOptionPane.ERROR_MESSAGE);
//                    proposedChk.setSelected(false);
//                    return;
//                }
//                
//			    //if this plan has custom slew accelerations, it cannot be proposed
//                if(myLayer.hasCustomSlewAccelerations()){
//                    JOptionPane.showMessageDialog(proposedChk, 
//                            "Cannot propose this plan because the official ATL slew accelerations have\n"
//                            + "been overridden with custom values. This plan will need to be\n"
//                            + "recreated with the official slew accelerations in order to be proposed.",
//                            "Unofficial Slew Accelerations", JOptionPane.ERROR_MESSAGE);
//                    proposedChk.setSelected(false);
//                    return;
//                }
//                
//                //if this plan was created with an older CDL, it cannot be proposed
//                if (myLayer.getCreatedFromOldCDLFlag()) {
//                    JOptionPane.showMessageDialog(proposedChk, 
//                            "Cannot propose this plan because it was not created with\n"
//                            + "the latest CDL. This plan will need to be recreated with\n"
//                            + "the latest CDL in order to be proposed.",
//                            "Latest CDL", JOptionPane.ERROR_MESSAGE);
//                    proposedChk.setSelected(false);
//                    return;
//                }
//                
//                //if this plan was created with an older PL, it cannot be proposed
//                String[] payloadLibVals = DataAccess.getPayloadLibrary(myLayer.getStartET());
//                if (!(String.valueOf(PlanningLayer.getBlockFactory().getPayloadLibraryId()).equals(payloadLibVals[0]))) {
//                    JOptionPane.showMessageDialog(proposedChk, 
//                            "Cannot propose this plan because it was not created with\n"
//                            + "the latest Payload Library. This plan will need to be recreated with\n"
//                            + "the latest Payload Library in order to be proposed.",
//                            "Latest Payload Library", JOptionPane.ERROR_MESSAGE);
//                    proposedChk.setSelected(false);
//                    return;
//                }
//                
//				//if there is already a proposed plan for this time range,
//				//tell the user and don't let this check box be selected
//				if(!UIDataAccess.isUniqueProposedForTimeRange(myLayer.getPlanID(), myLayer.getTimeRangeID())){
//					proposedChk.setSelected(false);
//					JOptionPane.showMessageDialog(PlanFocusPanel.this,
//												"There is already a proposed plan for this time range.", 
//												"Proposed Plan Already Exists", 
//												JOptionPane.ERROR_MESSAGE);
//					return;
//				}
//				
//				//set the status
//				if(proposedChk.isSelected()){
//					myLayer.setPlanStatus(PlanStatus.PROPOSED);
//				}else{
//					myLayer.setPlanStatus(PlanStatus.DRAFT);
//				}
//
//				//refresh the checkbox ui
//				updatePropAprUI();
//			}
//		});
//		
//		proposedPnl.add(proposedChk);
//		
//		JPanel approvePnl = new JPanel();
//		approveChk = new JCheckBox("Approved Plan");
//		approveChk.setEnabled(myLayer.isEditable() && canApprove);
//		approveChk.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {	
//				
//				//cannot approve a plan if it has violations
//				// that don't have waivers.
//				if(myLayer.hasViolationsWithNoWaiver()){
//					JOptionPane.showMessageDialog(approveChk, 
//									"Cannot approve this plan because violations without waivers exist.\n"
//									+ "See violations tab for more information.",
//									"Unwaived Violations", JOptionPane.ERROR_MESSAGE);
//					approveChk.setSelected(false);
//					return;
//				}
//				
//				//if this plan has custom slew rates, it cannot be approved
//				if(myLayer.hasCustomSlewRates()){
//					JOptionPane.showMessageDialog(approveChk, 
//							"Cannot approve this plan because the official ATL slew rates have\n"
//							+ "been overridden with custom values. This plan will need to be\n"
//							+ "recreated with the official slew rates in order to be approved.",
//							"Unofficial Slew Rates", JOptionPane.ERROR_MESSAGE);
//					approveChk.setSelected(false);
//					return;
//				}
//				
//				//if this plan has custom slew accelerations, it cannot be approved
//				if(myLayer.hasCustomSlewAccelerations()){
//					JOptionPane.showMessageDialog(approveChk, 
//							"Cannot approve this plan because the official ATL slew accelerations have\n"
//							+ "been overridden with custom values. This plan will need to be\n"
//							+ "recreated with the official slew accelerations in order to be approved.",
//							"Unofficial Slew Accelerations", JOptionPane.ERROR_MESSAGE);
//					approveChk.setSelected(false);
//					return;
//				}
//				
//				//if this plan was created with an older CDL, it cannot be approved
//				if (myLayer.getCreatedFromOldCDLFlag()) {
//				    JOptionPane.showMessageDialog(approveChk, 
//                            "Cannot approve this plan because it was not created with\n"
//                            + "the latest CDL. This plan will need to be recreated with\n"
//                            + "the latest CDL in order to be approved.",
//                            "Latest CDL", JOptionPane.ERROR_MESSAGE);
//                    approveChk.setSelected(false);
//                    return;
//				}
//				
//				//if this plan was created with an older PL, it cannot be approved
//				String[] payloadLibVals = DataAccess.getPayloadLibrary(myLayer.getStartET());
//                if (!(String.valueOf(PlanningLayer.getBlockFactory().getPayloadLibraryId()).equals(payloadLibVals[0]))) {
//                    JOptionPane.showMessageDialog(approveChk, 
//                            "Cannot approve this plan because it was not created with\n"
//                            + "the latest Payload Library. This plan will need to be recreated with\n"
//                            + "the latest Payload Library in order to be approved.",
//                            "Latest Payload Library", JOptionPane.ERROR_MESSAGE);
//                    approveChk.setSelected(false);
//                    return;
//                }
//				
//				
//				//if there is already an approved plan for this time range,
//				//tell the user
//				if(approveChk.isSelected() && !UIDataAccess.isUniqueApprovedForTimeRange(myLayer.getPlanID(), myLayer.getTimeRangeID())){
//					int result = JOptionPane.showConfirmDialog(approveChk,
//									"An approved plan for this time range already exists, are you sure you want to approve this plan?",
//									"Approved Plan Exists",
//									JOptionPane.YES_NO_OPTION);
//					if(result == JOptionPane.NO_OPTION){
//						approveChk.setSelected(false);
//						return;
//					}
//				}
//				
//				//change the staus
//				if(approveChk.isSelected()){
//					myLayer.setPlanStatus(PlanStatus.APPROVED);
//				}else{
//					myLayer.setPlanStatus(PlanStatus.DRAFT);
//				}
//
//				//update the ui
//				updatePropAprUI();
//			}
//		});
//		approvePnl.add(approveChk);
//		updatePropAprUI();
//		
//		
//		JLabel descLbl = new JLabel("Description:");
//		descTA = new JTextArea(5, 25);
//		descTA.setWrapStyleWord(true);
//		descTA.setLineWrap(true);
//		descTA.setEditable(myLayer.isEditable());
//		descSP = new JScrollPane(descTA, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		if(myLayer.getDescription()!=null){
//			descTA.setText(myLayer.getDescription());
//		}
//		JPanel descPnl = new JPanel();
//		descPnl.add(descLbl);
//		descPnl.add(descSP);
//		saveBtn = new JButton(saveAct);
//		saveBtn.setEnabled(myLayer.isEditable());
//		if(myLayer.getPlanID()==-1){
//			saveBtn.setEnabled(false);
//		}
//		saveAsBtn = new JButton(saveAsAct);
//		
//		JButton updateBtn = new JButton(updateAct);
//		updateBtn.setEnabled(myLayer.isEditable());
//		updateBtn.setEnabled(myLayer.hasOutOfDateEntries());
//		
//		
//		JPanel buttonPnl = new JPanel();
//		buttonPnl.add(saveBtn);
//		buttonPnl.add(saveAsBtn);
//		buttonPnl.add(updateBtn);
//		
//		savePnl.add(proposedPnl);
//		savePnl.add(approvePnl);
//		savePnl.add(descPnl);
//		savePnl.add(buttonPnl);
//		
//		//Report panel
//		JPanel reportPnl = new JPanel();
//		reportPnl.setBorder(new TitledBorder("Manage Planning Reports"));
//		reportPnl.setLayout(new GridBagLayout());
//		JButton planningReportBtn = new JButton(planningRepAct);
//		JButton atlReportBtn = new JButton(atlRepAct);
//		JButton opNavReportBtn = new JButton(opNavRepAct);
//		JButton diffReportBtn = new JButton(diffRepAct);
//		JButton jsonReportBtn = new JButton(jsonRepAct);
//		JButton oszReportBtn = new JButton(oszRepAct);
//		reportChk = new JCheckBox("Generate All Reports On Save or Save As...");
//		JLabel reportLbl = new JLabel("**All reports can be found in the home directory under .../jmars/reports**");
//		reportLbl.setFont(new Font("Dialog", Font.ITALIC, 12));
//		reportLbl.setForeground(Color.BLUE);
//		
//		reportPnl.add(planningReportBtn, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), pad, pad));
//		reportPnl.add(atlReportBtn, new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), pad, pad));
//		reportPnl.add(opNavReportBtn, new GridBagConstraints(0, 4, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), pad, pad));
//		reportPnl.add(diffReportBtn, new GridBagConstraints(0, 5, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), pad, pad));
//		reportPnl.add(jsonReportBtn, new GridBagConstraints(0, 6, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), pad, pad));
//		reportPnl.add(oszReportBtn, new GridBagConstraints(0, 7, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), pad, pad));
//		reportPnl.add(reportChk, new GridBagConstraints(0, 8, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, 5*pad));
//		reportPnl.add(reportLbl, new GridBagConstraints(0, 9, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, 5*pad));
//		
//		
//		finalizePnl.add(savePnl, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), pad, pad));
//		finalizePnl.add(reportPnl, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), pad, pad));
//		finalizePnl.add(Box.createVerticalGlue(), new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, in, pad, pad));
//		
//		return finalizePnl;
//	}
//	
//	/**
//	 * Update the UI which is dependent on the status of the plan.
//	 * Update the checkboxes and such based on whether the plan is
//	 * DRAFT, PROPOSED, or APPROVED.
//	 */
//	public void updatePropAprUI(){
//		PlanStatus ps = myLayer.getPlanStatus();
//		//DRAFT
//		if(ps == PlanStatus.DRAFT){
//			proposedChk.setEnabled(myLayer.isEditable());
//			proposedChk.setSelected(false);
//			approveChk.setEnabled(myLayer.isEditable() && canApprove);
//			approveChk.setSelected(false);
//		}
//		//PROPOSED
//		else if(ps == PlanStatus.PROPOSED){
//			proposedChk.setEnabled(myLayer.isEditable());
//			proposedChk.setSelected(true);
//			approveChk.setEnabled(myLayer.isEditable() && canApprove);
//			approveChk.setSelected(false);
//		}
//		//APPROVED
//		else if(ps == PlanStatus.APPROVED){
//			proposedChk.setEnabled(false);
//			proposedChk.setSelected(false);
//			approveChk.setEnabled(myLayer.isEditable() && canApprove);
//			approveChk.setSelected(true);
//		}
//		
//		//set read only status
//		if(myLayer.isReadOnly){
//			proposedChk.setEnabled(false);
//			approveChk.setEnabled(false);
//		}		
//
//	}
//	
//	
//	private ListSelectionListener objRowListener = new ListSelectionListener() {
//		public void valueChanged(ListSelectionEvent e) {
//			if(e.getSource() == objTbl.getSelectionModel()){
//				if(objTbl.getSelectedRow() != curMosaic || !Arrays.equals(selectedObjs, objTbl.getSelectedRows())){
//					curMosaic = objTbl.getSelectedRow();
//					selectedObjs = objTbl.getSelectedRows();
//					ArrayList<Objective> selectedObjectives = new ArrayList<Objective>();
//					for(int i : selectedObjs){
//						selectedObjectives.add(myLayer.getObjectives().get(i));
//					}
//					//refresh results panel
//					refreshResultsPanel();
//					//refresh 3D view	
//					if (curMosaic!=-1) {
//						myLView3D.setObjectives(selectedObjectives);
//					} else {
//						myLView3D.setObjectives(null);
//					}
//					myLView3D.repaint();
//				}
//			}
//		}
//	};
//	
//	
//	private MouseAdapter objMouseListener = new MouseAdapter() {
//		public void mouseClicked(MouseEvent e){
//			if(SwingUtilities.isRightMouseButton(e)){
//			    //determine if row under the right-click was selected
//			    boolean rightClickSelected = false;
//                int[] selectedIdxs = objTbl.getSelectedRows();
//			    int row = objTbl.rowAtPoint(e.getPoint());
//			    for(int tmpIdx : selectedIdxs) {
//			        if (row == tmpIdx) {
//			            rightClickSelected = true;
//			            break;
//			        }
//			    }
//			    if (!rightClickSelected) {//user right-clicked on a row that was not selected, select that row
//                    if(row> -1 && row < objTbl.getRowCount()){
//                        objTbl.setRowSelectionInterval(row, row);
//                    } else {
//                        return;
//                    }
//			    }
//			    
//			    //we should now have one or more rows selected for this right-click
//			    boolean oneRowSelectedFlag = true;
//			    int selectedRowCount = objTbl.getSelectedRowCount();
//			    if (selectedRowCount > 1) {
//			        oneRowSelectedFlag = false;//flag for later to not show edit and violations if more than one row is selected
//			    }
//			    
//			    //make sure a valid row is selected
//                final int[] selectedRows = objTbl.getSelectedRows();
//                final int index;
//                if (selectedRows.length == 0) {
//                    return;
//                } else {
//                    index = selectedRows[0];//will only be used if there is only one row selected
//                }
//			    
//				//display popup menu with rename option
//				final JPopupMenu objMenu = new JPopupMenu();
//				JMenuItem editItem = new JMenuItem(new AbstractAction("Edit...") {
//					public void actionPerformed(ActionEvent e) {
//
//						Objective selObj = myLayer.getObjectives().get(index);
//						//if it's a normally made objective edit with the ocw.
//						if(selObj.getType()==ObjectiveType.NORMAL){
//							
//							// Warn the user that the objective is being removed 
//							//from the existing plan, and it must be re-added from
//							//the ocw to appear back in the plan, even if no changes
//							//are made.
//							JOptionPane.showMessageDialog(PlanFocusPanel.this,
//									"Editing an objective removes it from the plan immediately.\n"
//									+ "The objective must be added back from the results tab on\n"
//									+ "the Objective Creation window, even if no changes are made.", 
//									"Edit Message",
//									JOptionPane.INFORMATION_MESSAGE);
//							
//							// Remove before editing
//							myLayer.getObjectives().remove(selObj);
//							curMosaic = index -1;
//							
//							getPlanLayer().performPostUpdateProcessing(true);
//							
//							ObjCreationWindow ocw = new ObjCreationWindow(parentFrame, (PlanLView)parent, selObj);
//							ocw.setVisible(true);
//
//							refreshObjTable();
//							refreshResultsPanel();
//							
//							objTbl.setRowSelectionInterval(curMosaic, curMosaic);
//						}
//
//					}
//				});
//				JMenuItem delItem = new JMenuItem(new AbstractAction("Delete") {
//					public void actionPerformed(ActionEvent e) {
//
//						//Make sure the user really wants to delete the objective
//						int result = JOptionPane.showConfirmDialog(objTbl, 
//								"Are you sure you want to delete the selected objective(s)?", 
//								"Confirm Delete", 
//								JOptionPane.OK_CANCEL_OPTION, 
//								JOptionPane.WARNING_MESSAGE);
//						
//						//If yes, proceed with delete logic
//						if(result == JOptionPane.OK_OPTION){
//						    //change the curMosaic before any listeners are notified.
//						    curMosaic = 0;
//						    
//						    //do in 2 steps since deleting an objective from the list changes the indices
//						    ArrayList<Objective> toDelete = new ArrayList<Objective>();
//						    for (int idx : selectedRows) {
//    							Objective oldObj = myLayer.getObjectives().get(idx);
//    							toDelete.add(oldObj);
//						    }
//						    for (Objective oldObj : toDelete) {
//						        myLayer.removeObjective(oldObj);
//						    }
//						    toDelete = null;//just to free up resources
//						    
//							getPlanLayer().performPostUpdateProcessing(true);
//							
//							myLView3D.setObjectives(null);
//							myLView3D.repaint();
//
//							refreshObjTable();
//							objTbl.getSelectionModel().setSelectionInterval(curMosaic, curMosaic);
//
//							refreshResultsPanel();
//							if(myLayer.getObjectives().size()<1){
//								addComBtn.setEnabled(false);
//								addSeqBtn.setEnabled(false);
//								addBlkBtn.setEnabled(false);
//								addTarBtn.setEnabled(false);
//							}
//						}
//					}
//				});
//				JMenuItem violationItem = new JMenuItem(new AbstractAction("View violations...") {
//					public void actionPerformed(ActionEvent e) {
//						Objective obj = myLayer.getObjectives().get(curMosaic);
//						
//						new ViolationDialog(PlanFocusPanel.this, parentFrame, obj);
//					}
//				});
//				
//				objMenu.add(editItem);
//				objMenu.add(delItem);
//				objMenu.add(violationItem);
//				
//				if (oneRowSelectedFlag) {//only one row selected, do other logic to determine enabled options
//    				//disable edit and delete items if isReadOnly
//    				editItem.setEnabled(myLayer.isEditable());
//    				delItem.setEnabled(myLayer.isEditable());
//    				
//    				//disable edit and delete if this is initial slew obj
//    				// and add the optimize item
//    				if(myLayer.getObjectives().get(index).getType().equals(ObjectiveType.INITIAL_ATTITUDE)){
//    					delItem.setEnabled(false);
//    					editItem.setEnabled(false);
//    				}
//    				
//    				//disable violation item if not in violation
//    				violationItem.setEnabled(myLayer.getObjectives().get(curMosaic).getViolations().getViolationCount()>0);
//    				
//				} else {
//				    editItem.setEnabled(false);
//				    violationItem.setEnabled(false);
//				    delItem.setEnabled(myLayer.isEditable());
//				    for (int idx : selectedRows) {
//    				    if(myLayer.getObjectives().get(idx).getType().equals(ObjectiveType.INITIAL_ATTITUDE)){
//                            delItem.setEnabled(false);//might be able to just check if idx is 0?
//                        }
//				    }
//				}
//				
//				//Lastly, show the menu
//				objMenu.show(e.getComponent(), e.getX(), e.getY());
//			}
//		}
//	};
//	
//	
//	private ColorCombo createColorCombo(Color defaultColor) {
//		ColorCombo cc = new ColorCombo();
//		cc.setColor(defaultColor);
//		cc.setPreferredSize(new Dimension(80, cc.getPreferredSize().height));
//		cc.addActionListener(colorBoxListener);
//		return cc;
//	}
//	
//	
//	private ActionListener colorBoxListener = new ActionListener(){
//		public void actionPerformed(ActionEvent e) {
//			//From radius color combo
//			if(e.getSource() == radCC){
//				PlanningSettings.radiusColor = radCC.getColor();
//				Config.set("PlanningSettings.radiusColor.r", PlanningSettings.radiusColor.getRed()+"");
//				Config.set("PlanningSettings.radiusColor.b", PlanningSettings.radiusColor.getBlue()+"");
//				Config.set("PlanningSettings.radiusColor.g", PlanningSettings.radiusColor.getGreen()+"");
//			}
//			
//			//from slew color combo 
//			if(e.getSource() == slewCC){
//				PlanningSettings.slewColor = slewCC.getColor();
//				Config.set("PlanningSettings.slewColor.r", PlanningSettings.slewColor.getRed()+"");
//				Config.set("PlanningSettings.slewColor.b", PlanningSettings.slewColor.getBlue()+"");
//				Config.set("PlanningSettings.slewColor.g", PlanningSettings.slewColor.getGreen()+"");
//			}
//			
//			if(e.getSource() == mapCamCC) {
//				PlanningSettings.MapCamColor = mapCamCC.getColor();
//				Config.set("PlanningSettings.MapCamColor.r", PlanningSettings.MapCamColor.getRed()+"");
//				Config.set("PlanningSettings.MapCamColor.b", PlanningSettings.MapCamColor.getBlue()+"");
//				Config.set("PlanningSettings.MapCamColor.g", PlanningSettings.MapCamColor.getGreen()+"");
//			}
//			
//			if(e.getSource() == polyCamCC) {
//				PlanningSettings.PolyCamColor = polyCamCC.getColor();
//				Config.set("PlanningSettings.PolyCamColor.r", PlanningSettings.PolyCamColor.getRed()+"");
//				Config.set("PlanningSettings.PolyCamColor.b", PlanningSettings.PolyCamColor.getBlue()+"");
//				Config.set("PlanningSettings.PolyCamColor.g", PlanningSettings.PolyCamColor.getGreen()+"");
//			}
//
//			if(e.getSource() == samCamCC) {
//				PlanningSettings.SamCamColor = samCamCC.getColor();
//				Config.set("PlanningSettings.SamCamColor.r", PlanningSettings.SamCamColor.getRed()+"");
//				Config.set("PlanningSettings.SamCamColor.b", PlanningSettings.SamCamColor.getBlue()+"");
//				Config.set("PlanningSettings.SamCamColor.g", PlanningSettings.SamCamColor.getGreen()+"");
//			}
//
//			if(e.getSource() == olaCC) {
//				PlanningSettings.OLAColor = olaCC.getColor();
//				Config.set("PlanningSettings.OLAColor.r", PlanningSettings.OLAColor.getRed()+"");
//				Config.set("PlanningSettings.OLAColor.b", PlanningSettings.OLAColor.getBlue()+"");
//				Config.set("PlanningSettings.OLAColor.g", PlanningSettings.OLAColor.getGreen()+"");
//			}
//
//			if(e.getSource() == otesCC) {
//				PlanningSettings.OTESColor = otesCC.getColor();
//				Config.set("PlanningSettings.OTESColor.r", PlanningSettings.OTESColor.getRed()+"");
//				Config.set("PlanningSettings.OTESColor.b", PlanningSettings.OTESColor.getBlue()+"");
//				Config.set("PlanningSettings.OTESColor.g", PlanningSettings.OTESColor.getGreen()+"");
//			}
//
//			if(e.getSource() == ovirsCC) {
//				PlanningSettings.OVIRSColor = ovirsCC.getColor();
//				Config.set("PlanningSettings.OVIRSColor.r", PlanningSettings.OVIRSColor.getRed()+"");
//				Config.set("PlanningSettings.OVIRSColor.b", PlanningSettings.OVIRSColor.getBlue()+"");
//				Config.set("PlanningSettings.OVIRSColor.g", PlanningSettings.OVIRSColor.getGreen()+"");
//			}
//
//			if(e.getSource() == rexisCC) {
//				PlanningSettings.REXISColor = rexisCC.getColor();
//				Config.set("PlanningSettings.REXISColor.r", PlanningSettings.REXISColor.getRed()+"");
//				Config.set("PlanningSettings.REXISColor.b", PlanningSettings.REXISColor.getBlue()+"");
//				Config.set("PlanningSettings.REXISColor.g", PlanningSettings.REXISColor.getGreen()+"");
//			}
//			
//			if(e.getSource() == navCamCC){
//				PlanningSettings.NavCamColor = navCamCC.getColor();
//				Config.set("PlanningSettings.NavCamColor.r", PlanningSettings.NavCamColor.getRed()+"");
//				Config.set("PlanningSettings.NavCamColor.b", PlanningSettings.NavCamColor.getBlue()+"");
//				Config.set("PlanningSettings.NavCamColor.g", PlanningSettings.NavCamColor.getGreen()+"");
//			}
//			
//			if(e.getSource() == oldFtCC){
//				PlanningSettings.oldFootprintColor = oldFtCC.getColor();
//				Config.set("PlanningSettings.oldFootprintColor.r", PlanningSettings.oldFootprintColor.getRed()+"");
//				Config.set("PlanningSettings.oldFootprintColor.b", PlanningSettings.oldFootprintColor.getBlue()+"");
//				Config.set("PlanningSettings.oldFootprintColor.g", PlanningSettings.oldFootprintColor.getGreen()+"");
//			}
//			
//			if(e.getSource() == trackCC){
//				PlanningSettings.trackColor = trackCC.getColor();
//				Config.set("PlanningSettings.trackColor.r", PlanningSettings.trackColor.getRed()+"");
//				Config.set("PlanningSettings.trackColor.b", PlanningSettings.trackColor.getBlue()+"");
//				Config.set("PlanningSettings.trackColor.g", PlanningSettings.trackColor.getGreen()+"");
//			}
//			
//			if(e.getSource() == starsCC){
//				PlanningSettings.starColor = starsCC.getColor();
//				Config.set("PlanningSettings.starColor.r", PlanningSettings.starColor.getRed()+"");
//				Config.set("PlanningSettings.starColor.b", PlanningSettings.starColor.getBlue()+"");
//				Config.set("PlanningSettings.starColor.g", PlanningSettings.starColor.getGreen()+"");
//			}
//			
//			if (e.getSource() == xButton){
//				PlanningSettings.scXColor = xButton.getColor();
//			}
//			
//			if (e.getSource() == yButton){
//				PlanningSettings.scYColor = yButton.getColor();
//			}
//			
//			if (e.getSource() == xButton){
//				PlanningSettings.scZColor = zButton.getColor();
//			}
//			
//			if (e.getSource() == sunButton){
//				PlanningSettings.sc2SunColor = sunButton.getColor();
//			}
//			
//			if (e.getSource() == velButton){
//				PlanningSettings.velColor = velButton.getColor();
//			}
//			
//			if (e.getSource() == velBButton){
//				PlanningSettings.velBColor = velBButton.getColor();
//			}
//			
//			if (e.getSource() == motionButton){
//				PlanningSettings.motionColor = motionButton.getColor();
//			}
//			
//			if (e.getSource() == momentButton){
//				PlanningSettings.momentColor = momentButton.getColor();
//			}
//			
//			if (e.getSource() == sc2EarthButton){
//				PlanningSettings.sc2EarthColor = sc2EarthButton.getColor();
//			}
//			
//			if (e.getSource() == sc2MoonButton){
//				PlanningSettings.sc2MoonColor = sc2MoonButton.getColor();
//			}
//			
//			if (e.getSource() == sc2BodyButton){
//				PlanningSettings.sc2BodyColor = sc2BodyButton.getColor();
//			}
//			
//			if (e.getSource() == bodyVelWrtSunButton){
//				PlanningSettings.bodyVelWrtSunColor = bodyVelWrtSunButton.getColor();
//			}
//			
//			if (e.getSource() == sigmaUncEllipsoidButton){
//				PlanningSettings.sigmaUncEllipsoidColor = sigmaUncEllipsoidButton.getColor();
//			}
//			
//			if (e.getSource() == rtnEllipsoidButton){
//				PlanningSettings.rtnEllipsoidColor = rtnEllipsoidButton.getColor();
//			}
//			
//			if (e.getSource() == showSigmaLocCC){
//				PlanningSettings.sigmaLocColor = showSigmaLocCC.getColor();
//			}
//			
//			if (e.getSource() == showSigmaLocSigmaCC){
//				PlanningSettings.sigmaLocSigmaColor = showSigmaLocSigmaCC.getColor();
//			}
//			
//			if (e.getSource() == showOrgLocCC){
//				PlanningSettings.orgLocColor = showOrgLocCC.getColor();
//			}
//			
//			if (e.getSource() == showOrgOffLocCC){
//				PlanningSettings.orgOffLocColor = showOrgOffLocCC.getColor();
//			}
//			
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//		}
//		
//	};
//	
//	private AbstractAction addTargetAct = new AbstractAction("Add Target"){
//		public void actionPerformed(ActionEvent e) {
//			Objective obj = myLayer.getObjectives().get(curMosaic);
//			List<Target> targets = obj.getTargets();
//			double startEt = targets.isEmpty()? obj.getStartET(): targets.get(targets.size()-1).getTimeET();
//			new AddTargetDialog(parentFrame, obj, startEt, PlanFocusPanel.this, null);
//		}
//	};
//	
//	private AbstractAction addCommandAct = new AbstractAction("Add Command") {
//		public void actionPerformed(ActionEvent e) {
//			
//			double startTime;
//			//Get the last command on the sequence and use its end time as the new
//			// default start time.
//			PlannedCommand pc = selectedSequence.getLastPlannedCommand();
//			if(pc != null){
//				startTime = pc.calculateStartETForNextAction();
//			}else{
//				//If there are no commands on the sequence, use the sequence start time
//				startTime = selectedSequence.getStartET();
//			}
//			new AddCommandDialog(parentFrame, selectedSequence, startTime, PlanFocusPanel.this, null);
//		}
//	};
//	
//	private AbstractAction addSeqAct = new AbstractAction("Add a Sequence"){
//		public void actionPerformed(ActionEvent e) {
//			new AddSequenceDialog(parentFrame, selectedTarget, PlanFocusPanel.this, null, (PlanLView)parent);
//		}
//	};
//	
//	private AbstractAction addBlockAct = new AbstractAction("Add Block"){
//		public void actionPerformed(ActionEvent e){
//			
//			double startTime;
//			//Get the last command on the sequence and use its end time as the new
//			// default start time.
//			PlannedCommand pc = selectedSequence.getLastPlannedCommand();
//			if(pc != null){
//				startTime = pc.calculateStartETForNextAction();
//			}else{
//				//If there are no commands on the sequence, use the sequence start time
//				startTime = selectedSequence.getStartET();
//			}
//	
//			new AddBlockDialog(parentFrame, selectedSequence, startTime, PlanFocusPanel.this, PlanningLayer.getBlockFactory(), null);
//		}
//	};
//	
//	private AbstractAction addSandboxBlockAct = new AbstractAction("Enter Block Sandbox Mode"){
//        public void actionPerformed(ActionEvent e){
//           if (bsd == null) {
//               bsd = new BlockSandboxDialog(ocw, sandboxBlockBtn);
//           } else {
//               bsd.setVisible(true);
//           }
//           String parsedVMLFile = bsd.getLastParsedFile();
//           String vmlFilenameString = "";
//           if (parsedVMLFile == null) {
//               if (PlanFocusPanel.this.defaultVMLFile != null) {
//                   vmlFilenameString = PlanFocusPanel.this.defaultVMLFile;
//                   PlanFocusPanel.this.vmlVerLbl.setText(vmlPrompt+" "+vmlFilenameString);
//                   PlanFocusPanel.this.defaultVMLFile = null;
//               }
//           } else {
//               if (PlanFocusPanel.this.defaultVMLFile == null) {
//                   PlanFocusPanel.this.defaultVMLFile = PlanFocusPanel.this.vmlVerLbl.getText();
//               }
//               vmlFilenameString = parsedVMLFile;
//               PlanFocusPanel.this.vmlVerLbl.setToolTipText(vmlFilenameString);
//               vmlFilenameString = vmlFilenameString.substring(vmlFilenameString.lastIndexOf("/")+1);
//               PlanFocusPanel.this.vmlVerLbl.setText(vmlPrompt+" "+vmlFilenameString);
//           }
//           
//        }
//    };
//	
//	private AbstractAction setOlaPowerAct = new AbstractAction("OLA Power on UTC...") {
//		public void actionPerformed(ActionEvent e) {
//			new SetOlaPowerUTCDialog(parentFrame, olaPowerTF, PlanFocusPanel.this);
//		}
//	};
//	
//	private AbstractAction reorderOcamsAct = new AbstractAction("Reorder OCAMS IDs...") {
//		public void actionPerformed(ActionEvent e) {
//			new SetOcamsIdGapDialog(parentFrame, myLayer);
//		}
//	};
//
//	private AbstractAction renameSequencesAct = new AbstractAction("Rename Matching Sequences...") {
//		public void actionPerformed(ActionEvent e) {
//			new RenameIdenticalSequencesDialog(parentFrame, PlanFocusPanel.this, myLayer);
//		}
//	};
//
//	private AbstractAction makeUniqueSequencesAct = new AbstractAction("Make Unique Sequences...") {
//		public void actionPerformed(ActionEvent e) {
//			new MakeUniqueSequencesDialog(parentFrame, myLayer);
//		}
//	};
//
//	private AbstractAction updateSequenceDOYAct = new AbstractAction("Update Sequence DOY...") {
//		public void actionPerformed(ActionEvent e) {
//			new UpdateSequencesDOYDialog(parentFrame, myLayer);
//		}
//	};
//
//	public TargetRecomputeAction recenterTargetsAct = new TargetRecomputeAction("Target-shift", new TargetUtil.ReplTargetBuilderUsingDeltaQuat()) {
//		@Override
//		public boolean checkPreConditions() {
//			int ynResult = JOptionPane.showConfirmDialog(PlanFocusPanel.this, "Planar-realign?", getActionName(), 
//					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//			boolean doPlanarRealign = ynResult == JOptionPane.YES_OPTION;
//			log.println("doPlanarRealign="+doPlanarRealign);
//			((TargetUtil.ReplTargetBuilderUsingDeltaQuat)this.replTargetBuilder).setPlanarRealign(doPlanarRealign);
//			return true;
//		}
//	};
//	
//	public TargetRecomputeAction timeShiftTargetsAct = new TargetRecomputeAction("Time-shift Targets", new TargetUtil.ReplTargetBuilderUsingTimeShift(0)){
//		private List<String> getRelevantTargetInfo(List<Target> targets){
//			List<String> relevantInfoList = new ArrayList<>();
//			for(Target tgt: targets){
//				// TODO add a standard short formatting of a target as well as a long formatting of a target to Target
//				relevantInfoList.add(String.format("Target %s@%s (%s)", tgt.getTargetStr(), tgt.getTimeStr(), tgt.getSlewMode()));
//			}
//			return relevantInfoList;
//		}
//		
//		@Override
//		public boolean checkPreConditions(){
//			String errorMsg = null;
//			
//			List<Target> planTargets = myLayer.getTargets();
//			List<Target> durChangingTargets = TargetUtil.findDurationChangingTimeShiftTargets(planTargets);
//			
//			boolean goodToGo;
//			if (durChangingTargets.isEmpty()){
//				goodToGo = true;
//			}
//			else {
//				goodToGo = false;
//				
//				errorMsg = "Some targets could see their durations change:\n"+Util.join("\n", getRelevantTargetInfo(durChangingTargets));
//				int ynResult = JOptionPane.showConfirmDialog(PlanFocusPanel.this, errorMsg, getActionName(), 
//						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//				
//				if (ynResult == JOptionPane.YES_OPTION){
//					goodToGo = true;
//				}
//			}
//			
//			if (goodToGo){
//				errorMsg = null;
//				
//				String result = JOptionPane.showInputDialog(PlanFocusPanel.this, "Specify shift value in seconds", new Double(0));
//				if (result != null){
//					try {
//						double shiftBySeconds = Double.parseDouble(result);
//
//						if (myLayer.getObjectives().size() > 1 && myLayer.getObjectives().get(1).getTargets().size() > 0){
//							Target initialSlewTarget = myLayer.getObjectives().get(1).getTargets().get(0);
//							double deltaStart = (initialSlewTarget.getSlewStart()+shiftBySeconds) - myLayer.getStartET();
//							
//							List<Target> allTargets = myLayer.getTargets();
//							Target lastTarget = allTargets.get(allTargets.size()-1);
//							double deltaEnd = myLayer.getEndET() - (lastTarget.getTimeET()+shiftBySeconds);
//							
//							if (Double.isNaN(shiftBySeconds) || Double.isInfinite(shiftBySeconds)){
//								errorMsg = String.format("Shift by %f is invalid.", shiftBySeconds);
//							}
//							if (shiftBySeconds < 0 && deltaStart < 0){
//								errorMsg = String.format("Shift by %.3f will result in initial slew target move %.3f seconds before layer start time.", shiftBySeconds, -deltaStart);
//							}
//							else if (shiftBySeconds > 0 && deltaEnd < 0){
//								errorMsg = String.format("Shift by %.3f will result in last target move %.3f seconds after layer end time.", shiftBySeconds, -deltaEnd);
//							}
//
//							if (errorMsg == null){
//								((TargetUtil.ReplTargetBuilderUsingTimeShift)this.replTargetBuilder).setEtShift(shiftBySeconds);
//							}
//						}
//						else {
//							errorMsg = "Nothing to do.";
//						}
//					}
//					catch(NumberFormatException ex){
//						errorMsg = ex.getMessage();
//					}
//				}
//				else {
//					errorMsg = "Canceled by user.";
//				}
//			}
//			setPreConditionFailureReason(errorMsg);
//			return (errorMsg == null);
//		}
//	};
//	
//	/**
//	 * Common replacement target builder action logic.
//	 * Asks for confirmation before proceeding and shows success / failure status afterwards.
//	 */
//	public class TargetRecomputeAction extends AbstractAction implements Runnable {
//		TargetUtil.ReplTargetBuilder replTargetBuilder;
//		String actionName;
//		String preConditionFailureReason;
//		ProgressDialog progress;
//		
//		public TargetRecomputeAction(String name, TargetUtil.ReplTargetBuilder replTargetBuilder){
//			super(name);
//			this.actionName = name;
//			this.replTargetBuilder = replTargetBuilder;
//			this.preConditionFailureReason = null;
//			this.progress = null;
//		}
//		
//		public boolean checkPreConditions(){
//			return true;
//		}
//		
//		protected void setPreConditionFailureReason(String reason){
//			this.preConditionFailureReason = reason;
//		}
//		
//		public String getPreConditionFailureReason(){
//			return preConditionFailureReason;
//		}
//		
//		public String getActionName(){
//			return this.actionName;
//		}
//		
//		protected void setProgress(ProgressDialog progress){
//			this.progress = progress;
//		}
//		
//		protected void postProgressStart(){
//			if (progress == null){
//				return;
//			}
//			
//			progress.setNote(0, "Objective");
//			progress.setNote(1, "Target");
//		}
//		
//		protected void postProgressEnd(){
//			if (progress != null){
//				progress.stop();
//			}
//			setProgress(null);
//		}
//		
//		protected void postProgress(final int obj, final int nObj, final int tgt, final int nTgt){
//			SwingUtilities.invokeLater(new Runnable(){
//				@Override
//				public void run() {
//					if (progress == null){
//						return;
//					}
//					
//					if (obj < 0){
//						progress.setMax(0, nObj);
//					}
//					else {
//						progress.setProgress(0, obj+1);
//						if (tgt < 0){
//							progress.setMax(1, nTgt);
//						}
//						else {
//							progress.setProgress(1, tgt+1);
//						}
//					}
//				}
//			});
//		}
//		
//		@Override
//		public void run() {
//			
//			Exception recenterMosaicException = null;
//			Exception postRecenterMosaicException = null;
//			try {
//				try {
//					postProgressStart();
//					TargetUtil.recenterMosaicUsingReplTargetBuilder(TargetRecomputeAction.this.replTargetBuilder,
//							myLayer.getObjectives(),
//							new TargetUtil.ProgressListener() {
//						public void progressUpdate(int obj, int nObj, int tgt, int nTgt) {
//							postProgress(obj, nObj, tgt, nTgt);
//						}
//					});
//				}
//				catch(Exception ex){
//					log.aprintln(ex);
//					recenterMosaicException = ex;
//				}
//				log.aprintln("Re-running Flight Rules on Plan");
//				getPlanLayer().resetGlobalVariables();
//				getPlanLayer().retestAllRules();
//				refreshViolationTable();
//				refreshObjTable();
//				refreshResultsPanel();
//				myLView3D.repaint();
//			}
//			catch(Exception ex){
//				log.aprintln(ex);
//				postRecenterMosaicException = ex;
//			}
//			finally {
//				postProgressEnd();
//
//				final int messageType;
//				final String title, message;
//				if (recenterMosaicException == null && postRecenterMosaicException == null){
//					messageType = JOptionPane.INFORMATION_MESSAGE;
//					title = "Success!";
//					message = getActionName()+" completed successfully!";
//				}
//				else {
//					messageType = JOptionPane.ERROR_MESSAGE;
//					title = "Error!";
//					if (recenterMosaicException != null){
//						message = getActionName()+" failed with exception: "+recenterMosaicException.toString()+".\n See log for more information.";
//					}
//					else {
//						message = getActionName()+" succeeded, but following refresh step failed with exception: "+postRecenterMosaicException.toString()+".\n See log for more information.";
//					}
//				}
//				SwingUtilities.invokeLater(new Runnable(){
//					@Override
//					public void run(){
//						JOptionPane.showMessageDialog(PlanFocusPanel.this, message, title, messageType);
//					}
//				});
//			}
//		}
//		
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			if (!checkPreConditions()){
//				JOptionPane.showMessageDialog(PlanFocusPanel.this,
//						"Preconditions failed for "+getActionName()+": "+getPreConditionFailureReason(),
//						"Error!", JOptionPane.ERROR_MESSAGE);
//				return;
//			}
//			
//			int confirmResult = JOptionPane.showConfirmDialog(PlanFocusPanel.this, 
//					"This operation will "+getActionName()+" all targets and could take some time to finish. Do you want to continue?", 
//					"Confirm operation?", JOptionPane.YES_NO_OPTION);
//			
//			if (confirmResult == JOptionPane.OK_OPTION){
//				Thread workerThread = new Thread(this, getActionName());
//				ProgressDialog progress = new ProgressDialog(PlanFocusPanel.this, workerThread, 2, false, true);
//				setProgress(progress);
//				try {
//					workerThread.start();
//				}
//				catch(Exception ignore){
//					progress.stop();
//					setProgress(null);
//					log.aprintln(ignore);
//				}
//			}
//			
//		}
//	}
//	
////	private AbstractAction recenterTargetsAct = new TargetRecomputeAction("Target-shift", new TargetUtil.ReplTargetBuilderUsingDeltaQuat());
//	private AbstractAction recomputeTargetsAct = new TargetRecomputeAction("Target-recompute", new TargetUtil.ReplTargetBuilderUsingOrgCoords());
//    private AbstractAction recomputeTargetSlewAct = new TargetRecomputeAction("Slew-recompute", new TargetUtil.ReplTargetBuilderUsingRecalcSlewDur());
//	
//	private AbstractAction setSelfDeletingFlagAct = new AbstractAction("All Seq Self-Deleting") {
//		public void actionPerformed(ActionEvent e) {
//			boolean selfDelFlag;
//			String selfDelStr;
//			if (e.getSource() == setSelfDelFlagBtn){
//				selfDelFlag = true;
//				selfDelStr = "self-deleting";
//			}
//			else if (e.getSource() == unsetSelfDelFlagBtn){
//				selfDelFlag = false;
//				selfDelStr = "non-self-deleting";
//			}
//			else {
//				// nothing to do
//				return;
//			}
//			
//			int result = JOptionPane.showConfirmDialog(PlanFocusPanel.this,
//					"Do you want to update all sequences to "+selfDelStr+"?",
//					"Update self-deleting flag", JOptionPane.YES_NO_OPTION);
//			if (result == JOptionPane.YES_OPTION){
//				// set flag value on all sequences
//				int seqCount = 0;
//				for(Objective obj: myLayer.getObjectives()){
//					for(Target tgt: obj.getTargets()){
//						for(PlannedSequence ps: tgt.getSequences()){
//							ps.setSelfDeleting(selfDelFlag);
//							seqCount++;
//						}
//					}
//				}
//
//				log.println("Made "+seqCount+" sequences "+selfDelStr);
//				seqTbl.repaint();
//				JOptionPane.showMessageDialog(PlanFocusPanel.this,
//						"Made "+seqCount+" sequences "+selfDelStr,
//						"Updated self-deleting flag", JOptionPane.INFORMATION_MESSAGE);
//			}
//		}
//	};
//	
//	private AbstractAction createObjAct = new AbstractAction("Start from scratch...") {
//		public void actionPerformed(ActionEvent e) {
//			//only create a new one if there isn't one visible right now
//			if(ocw == null || !ocw.isVisible()){
//				//create objective creation window to show user
//				ocw = new ObjCreationWindow(parentFrame, (PlanLView)parent);
//			}else{
//				ocw.toFront();
//			}
//
//		}
//	};
//	
//	
//	private AbstractAction siteSearchAct = new AbstractAction("Site Specific Search..."){
//		public void actionPerformed(ActionEvent e) {
//			//if there are no sites, show a message and don't open the ssw
//			if(myLayer.getAOIs()==null || myLayer.getAOIs().size()<1){
//				JOptionPane.showMessageDialog(addSiteSearchBtn,
//								"There are no sites loaded in the database.",
//								"No AOIs", JOptionPane.ERROR_MESSAGE);
//				return;
//			}
//			//only create a new one if there isn't one visible right now
//			if(ssw == null || !ssw.isVisible()){
//				//show the site specific window
//				ssw = new SiteSearchWindow(parentFrame, (PlanLView)parent);
//			}else{
//				ssw.toFront();
//			}
//		}
//	};
//	
//
//	private AbstractAction saveAct = new AbstractAction("Save..."){
//		public void actionPerformed(ActionEvent e) {
//			
//			boolean report = reportChk.isSelected();
//		    if (PlanningLayer.isInSandBoxMode()) {
//		        JOptionPane.showMessageDialog(saveAsBtn, "Plans may not be saved in Block Sandbox Mode","Block Sandbox Mode Error", 
//		                JOptionPane.ERROR_MESSAGE);
//
//                return;
//		    }
//		    
//			//Check the first objective, must be of type initial slew
//			if(!myLayer.getObjectives().get(0).getType().equals(ObjectiveType.INITIAL_ATTITUDE)){
//				JOptionPane.showMessageDialog(saveAsBtn, 
//						"The first objective must be the initial slew objective. Please optimize start time on the initial slew objective.",
//						"Initial Slew Objective Error", JOptionPane.ERROR_MESSAGE);
//
//				return;
//			}
//			
//			//Display the "APPROVED" message if this is an approved plan.
//			if(myLayer.getPlanStatus() == PlanStatus.APPROVED){
//				int result = JOptionPane.showConfirmDialog(saveAsBtn, 
//						"Saving this plan as an approved plan will immediately make it a read-only plan layer. Do you want to save this plan?",
//						"Approved Plan Saving", JOptionPane.YES_NO_OPTION);
//				
//				if(result == JOptionPane.NO_OPTION){
//					return;
//				}
//			}
//			
//			String desc = descTA.getText();
//			if(desc.length()<129){
//				myLayer.setDesription(desc);
//				new SavePlanDialog(parentFrame, saveBtn, myLayer, PlanFocusPanel.this);
//				if(report){
//					planningRepAct.actionPerformed(e);
//					atlRepAct.actionPerformed(e);
//					opNavRepAct.actionPerformed(e);
//				}
//			}else{
//				JOptionPane.showMessageDialog(PlanFocusPanel.this, 
//						"Description is "+desc.length()+" characters, must be 128 characters or less.", 
//						"Description Too Long", JOptionPane.ERROR_MESSAGE);
//			}
//		}
//	};
//
//	private AbstractAction saveAsAct = new AbstractAction("Save As..."){
//		public void actionPerformed(ActionEvent e) {
//			boolean report = reportChk.isSelected();
//			boolean proceed = true;
//			
//			if (PlanningLayer.isInSandBoxMode()) {
//                JOptionPane.showMessageDialog(saveAsBtn,"Plans may not be saved in Block Sandbox Mode.","Block Sandbox Mode Error", 
//                        JOptionPane.ERROR_MESSAGE);
//
//                proceed = false;
//            }
//			
//			//Check the first objective, must be of type initial slew
//			if(!myLayer.getObjectives().get(0).getType().equals(ObjectiveType.INITIAL_ATTITUDE)){
//				JOptionPane.showMessageDialog(saveAsBtn, 
//						"The first objective must be the initial attitude objective. This should not ordinarily occur.",
//						"Initial Attitude Objective Error", JOptionPane.ERROR_MESSAGE);
//
//				proceed = false;
//			}
//			
//			//Display the "APPROVED" message if this is an approved plan.
//			if(myLayer.getPlanStatus() == PlanStatus.APPROVED){
//				int result = JOptionPane.showConfirmDialog(saveAsBtn, 
//						"Re-saving an approved plan will create a copy with a plan status of DRAFT. Do you want to re-save this plan?",
//						"Approved Plan Re-Saving", JOptionPane.YES_NO_OPTION);
//				
//				if(result == JOptionPane.YES_OPTION){
//					proceed = true;
//				}else{
//					proceed = false;
//				}
//			}
//			
//			//Tell them a proposed plan cannot be saved again
//			if(myLayer.getPlanStatus() == PlanStatus.PROPOSED){
//				int result = JOptionPane.showConfirmDialog(saveAsBtn, 
//						"Re-saving a proposed plan will create a copy with a plan status of DRAFT. Do you want to re-save this plan?",
//						"Proposed Plan Re-Saving", JOptionPane.YES_NO_OPTION);
//				
//				if(result == JOptionPane.YES_OPTION){
//					proceed = true;
//				}else{
//					proceed = false;
//				}
//			}
//			
//			if(proceed){
//			
//				String desc = descTA.getText();
//				if(desc.length()<129){
//					myLayer.setDesription(descTA.getText());
//					new SaveAsPlanDialog(parentFrame, saveBtn, myLayer, PlanFocusPanel.this, false);
//				}else{
//					JOptionPane.showMessageDialog(PlanFocusPanel.this, 
//							"Description is "+desc.length()+" characters, must be 128 characters or less.", 
//							"Description Too Long", JOptionPane.ERROR_MESSAGE);
//				}
//				if(report){
//					planningRepAct.actionPerformed(e);
//					atlRepAct.actionPerformed(e);
//					opNavRepAct.actionPerformed(e);
//				}
//				
//			}
//		}
//	};
//	
//	
//	private AbstractAction updateAct = new AbstractAction("Update Plan...") {
//		public void actionPerformed(ActionEvent e) {
//			String desc = descTA.getText();
//			if(desc.length()<129){
//				myLayer.setDesription(descTA.getText());
//				new SaveAsPlanDialog(parentFrame, saveBtn, myLayer, PlanFocusPanel.this, true);
//			}else{
//				JOptionPane.showMessageDialog(PlanFocusPanel.this, 
//						"Description is "+desc.length()+" characters, must be 128 characters or less.", 
//						"Description Too Long", JOptionPane.ERROR_MESSAGE);
//			}
//		}
//	};
//	
//	private class ReportAction extends AbstractAction {
//		ReportType reportType;
//		
//		public ReportAction(ReportType reportType){
//			super("Generate "+reportType+" report");
//			this.reportType = reportType;
//		}
//		
//		public void actionPerformed(ActionEvent e) {
//			try {
//				Reports report = new Reports(myLayer);
//				report.generateReport(reportType);
//			} catch (Exception ex) {
//				log.aprintln("Exception wile generating "+reportType+" report.");
//				log.aprintln(ex);
//				JOptionPane.showMessageDialog(
//						PlanFocusPanel.this,
//						"Exeception occurred while generating report.\n"
//						+"Check permissions / memory and exception text below and retry.\n"
//						+"Exception text: "+ex.toString()+"\n",
//						"Failed to generate report.",
//						JOptionPane.WARNING_MESSAGE);
//			}
//		}
//	};
//	
//	private ReportAction planningRepAct = new ReportAction(ReportType.PLAN);
//	private ReportAction atlRepAct = new ReportAction(ReportType.ATL);
//	private ReportAction opNavRepAct = new ReportAction(ReportType.OPNAV);
//	private ReportAction diffRepAct = new ReportAction(ReportType.DIFFS);
//	private ReportAction jsonRepAct = new ReportAction(ReportType.PLAN_OVERVIEW);
//	private ReportAction oszRepAct = new ReportAction(ReportType.OSZ_THERMAL);
//	
//	private AbstractAction objLAct = new AbstractAction("<"){
//		public void actionPerformed(ActionEvent e) {
//		//increment current mosaic index correctly
//			if(curMosaic == 0 || curMosaic == -1){
//				curMosaic = myLayer.getObjectives().size()-1;
//			}else{
//				curMosaic = curMosaic-1;
//			}
//		//refresh focus panel components
//
//		// Commented out, as it seems to get refreshed automatically when data changes
//		// Previously it was being called twice, which was slow with % coverage calculations
////			refreshResultsPanel();
//
//		//refresh 3D panel
//			myLView3D.setObjective(myLayer.getObjectives().get(curMosaic));
//			myLView3D.repaint();
//		//update objTable selection
//			objTbl.getSelectionModel().setSelectionInterval(curMosaic, curMosaic);
//		}
//	};
//	
//	private AbstractAction objBeginAct = new AbstractAction("|<<") {
//		public void actionPerformed(ActionEvent e) {
//			curMosaic = 0;
//		//refresh focus panel components
//			refreshResultsPanel();
//		//refresh 3D panel
//			myLView3D.setObjective(myLayer.getObjectives().get(curMosaic));
//			myLView3D.repaint();
//		//update objTable selection
//			objTbl.getSelectionModel().setSelectionInterval(curMosaic, curMosaic);
//
//		}
//	};
//	
//	private AbstractAction objRAct = new AbstractAction(">"){
//		public void actionPerformed(ActionEvent e) {
//		//increment current mosaic index correctly
//			if(curMosaic == myLayer.getObjectives().size()-1){
//				curMosaic = 0;
//			}else{
//				curMosaic = curMosaic+1;
//			}
//		//refresh focus panel components
//			refreshResultsPanel();
//		//refresh 3D panel
//			myLView3D.setObjective(myLayer.getObjectives().get(curMosaic));
//			myLView3D.repaint();
//		//update objTable selection
//			objTbl.getSelectionModel().setSelectionInterval(curMosaic, curMosaic);
//		}
//	};
//	
//	private AbstractAction objEndAct = new AbstractAction(">>|") {
//		public void actionPerformed(ActionEvent e) {
//			curMosaic = myLayer.getObjectives().size()-1;
//		//refresh focus panel components
//			refreshResultsPanel();
//		//refresh 3D panel
//			myLView3D.setObjective(myLayer.getObjectives().get(curMosaic));
//			myLView3D.repaint();
//		//update objTable selection
//			objTbl.getSelectionModel().setSelectionInterval(curMosaic, curMosaic);
//
//		}
//	};
//	
//	private AbstractAction showRexisAct = new AbstractAction("Show REXIS?"){
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showREXIS = showRexisChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//			Config.set("PlanningSettings.showRexis", PlanningSettings.showREXIS);
//		}
//	};
//
//	private AbstractAction showOLAAct = new AbstractAction("Show OLA?"){
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showOLA = showOLAChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//			Config.set("PlanningSettings.showOLA", PlanningSettings.showOLA);
//		}
//	};
//
//	private AbstractAction showOTESAct = new AbstractAction("Show OTES?"){
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showOTES = showOTESChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//			Config.set("PlanningSettings.showOTES", PlanningSettings.showOTES);
//		}
//	};
//
//	private AbstractAction showOVIRSAct = new AbstractAction("Show OVIRS?"){
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showOVIRS = showOVIRSChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//			Config.set("PlanningSettings.showOVIRS", PlanningSettings.showOVIRS);
//		}
//	};
//
//	private AbstractAction showMapCamAct = new AbstractAction("Show MapCam?"){
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showMapCam = showMapCamChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//			Config.set("PlanningSettings.showMapCam", PlanningSettings.showMapCam);
//		}
//	};
//
//	private AbstractAction showPolyCamAct = new AbstractAction("Show PolyCam?"){
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showPolyCam = showPolyCamChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//			Config.set("PlanningSettings.showPolyCam", PlanningSettings.showPolyCam);
//			// TODO: Need to also repaint lview.  Make generic method to repaint everything that needs to be repainted when this changes?
//			//TODO: after august ^^
//		}
//	};
//
//	private AbstractAction showSamCamAct = new AbstractAction("Show SamCam?"){
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showSamCam = showSamCamChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//			Config.set("PlanningSettings.showSamCam", PlanningSettings.showSamCam);
//		}
//	};
//
//	private AbstractAction fillCamFpAct = new AbstractAction("Fill Camera Footprints?"){
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.fillCamFp = fillCamFpChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint(); //TODO: the lview doesn't actually care about this flag.  Needs to be updated to look at this flag and fill in FOVs accordingly.
//			//TODO: after august ^^
//			Config.set("PlanningSettings.fillCamFp", PlanningSettings.fillCamFp);
//		}
//	};
//
//	private AbstractAction fitCamFpAct = new AbstractAction("Fit Camera Footprints to Shape Model?"){
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.fitCamFp = fitCamFpChk.isSelected();
//			ThreeDManager.getInstance().enableFitting(PlanningSettings.fitCamFp);
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint(); //TODO: the lview doesn't actually care about this flag.  Needs to be updated to look at this flag and fill in FOVs accordingly.
//			//TODO: after august ^^
//		}
//	};
//
//	private AbstractAction showRadialUncAct = new AbstractAction("Radial") {
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showRadialUncertainty = radialUncChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//			Config.set("PlanningSettings.showRadialUncertainty", PlanningSettings.showRadialUncertainty);
//		}
//	};
//	private AbstractAction showTrackUncAct = new AbstractAction("Along/Cross Track") {
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showTrackUncertainty = trackUncChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//			Config.set("PlanningSettings.showTrackUncertainty", PlanningSettings.showTrackUncertainty);
//		}
//	};
//
//	private AbstractAction showNavCamAct = new AbstractAction("Show NavCam?"){
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showNavCam = showNavCamChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//			Config.set("PlanningSettings.showNavCam", PlanningSettings.showNavCam);
//			// TODO: Need to also repaint lview.  Make generic method to repaint everything that needs to be repainted when this changes?
//			//TODO: after august ^^
//		}
//	};
//	
//	private AbstractAction showOldFtAct = new AbstractAction("Show Old ") {
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showOldFootprints = showOldFootprintsChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//			Config.set("PlanningSettings.showOldFootprints", PlanningSettings.showOldFootprints);
//		}
//	};
//
//	private AbstractAction showGroundTrackAct = new AbstractAction("Ground Track? ") {
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showTrack = showTrackChk.isSelected();
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//		}
//	};
//	
//	private AbstractAction showStarsAct = new AbstractAction("Show Stars?") {
//		public void actionPerformed(ActionEvent e) {
//			PlanningSettings.showStars = showStarsChk.isSelected();
//			myLView3D.repaint();
//		}
//	};
//	
//
//	private ChangeListener numVectorsBetweenTargetsSpnListener = new ChangeListener() {
//		public void stateChanged(ChangeEvent e) {
//			int count;
//			try {
//				count = Integer.parseInt(numVectorsBetweenTargetsSpn.getModel().getValue().toString());
//			}
//			catch(NumberFormatException | NullPointerException ex){
//				count = PlanningSettings.numVectorsBetweenTargets;
//			}
//			PlanningSettings.numVectorsBetweenTargets = count;
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//		}
//	};
//	
//	
//	private AbstractAction checkBoxAct = new AbstractAction(){
//		public void actionPerformed(ActionEvent e){
//
//			if (e.getSource() == showScAxesChk){
//				PlanningSettings.showScAxes = showScAxesChk.isSelected();
//			}
//			else if (e.getSource() == showJ2kAxesChk){
//				PlanningSettings.showJ2kAxes = showJ2kAxesChk.isSelected();
//			}
//			else if (e.getSource() == showRtnAxesChk){
//				PlanningSettings.showRTNAxes = showRtnAxesChk.isSelected();
//			}
//			else if (e.getSource() == showSamAxesChk) {
//				PlanningSettings.showSAMAxes = showSamAxesChk.isSelected();
//			}
//			else if (e.getSource() == showSunChk){
//				PlanningSettings.showSc2Sun = showSunChk.isSelected();
//			}
//			else if (e.getSource() == showEarthChk){
//				PlanningSettings.showSc2Earth = showEarthChk.isSelected();
//			}
//			else if (e.getSource() == showMoonChk){
//				PlanningSettings.showSc2Moon = showMoonChk.isSelected();
//			}
//			else if (e.getSource() == showSc2BodyChk){
//				PlanningSettings.showSc2Body = showSc2BodyChk.isSelected();
//			}
//			else if (e.getSource() == showSc2TgtChk){
//				PlanningSettings.showSc2Tgt = showSc2TgtChk.isSelected();
//			}
//			else if (e.getSource() == showVelJ2kChk){
//				PlanningSettings.showVel = showVelJ2kChk.isSelected();
//			}
//			else if (e.getSource() == showVelBChk){
//				PlanningSettings.showVelB = showVelBChk.isSelected();
//			}
//			else if (e.getSource() == showAngmChk){
//				PlanningSettings.showMoment = showAngmChk.isSelected();
//			}
//			else if (e.getSource() == showMotionChk){
//				PlanningSettings.showMotion = showMotionChk.isSelected();
//			}
//			else if (e.getSource() == showBodyVelWrtSunChk){
//				PlanningSettings.showBodyVelWrtSun = showBodyVelWrtSunChk.isSelected();
//			}
//			else if (e.getSource() == showSigmaEllipsoidChk){
//				PlanningSettings.showSigmaUncEllipsoid = showSigmaEllipsoidChk.isSelected();
//			}
//			else if (e.getSource() == showRtnEllipsoidChk){
//				PlanningSettings.showRTNEllipsoid = showRtnEllipsoidChk.isSelected();
//			}
//			else if (e.getSource() == showVectorsAtTargetsChk){
//				PlanningSettings.showVectorsAtTargets = showVectorsAtTargetsChk.isSelected();
//			}
//			else if (e.getSource() == showVectorsAtImagesChk){
//				PlanningSettings.showVectorsAtImages = showVectorsAtImagesChk.isSelected();
//			}
//			else if (e.getSource() == showVectorsBetweenTargetsChk){
//				PlanningSettings.showVectorsBetweenTargets = showVectorsBetweenTargetsChk.isSelected();
//			}
//			else if (e.getSource() == showOrbitTrackChk){
//				PlanningSettings.showOrbitTrack = showOrbitTrackChk.isSelected();
//			}
//			else if (e.getSource() == showOrgLocChk){
//				PlanningSettings.showOrgLoc = showOrgLocChk.isSelected();
//			}
//			else if (e.getSource() == showOrgOffLocChk){
//				PlanningSettings.showOrgOffLoc = showOrgOffLocChk.isSelected();
//			}
//			else if (e.getSource() == showSigmaLocChk){
//				PlanningSettings.showSigmaLoc = showSigmaLocChk.isSelected();
//			}
//
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//		}
//	};
//
//	private AbstractAction tfAct = new AbstractAction(){
//		private int parse(JTextField tf, int defaultVal){
//			int val;
//			try {
//				val = Integer.parseInt(tf.getText());
//			}
//			catch(NumberFormatException | NullPointerException ex){
//				val = defaultVal;
//			}
//			if (val < 0){
//				val = defaultVal;
//			}
//			return val;
//		}
//		
//		private double parse(JTextField tf, double defaultVal){
//			double val;
//			try {
//				val = Double.parseDouble(tf.getText());
//			}
//			catch(NumberFormatException | NullPointerException ex){
//				val = defaultVal;
//			};
//			if (val < 0){
//				val = defaultVal;
//			}
//			return val;
//		}
//		
//		public void actionPerformed(ActionEvent e) {
//			if (e.getSource() == scAxesScaleTf){
//				PlanningSettings.scaleScAxes = parse(scAxesScaleTf, PlanningSettings.scaleScAxes);
//			}
//			else if (e.getSource() == j2kAxesScaleTf){
//				PlanningSettings.scaleJ2kAxes = parse(j2kAxesScaleTf, PlanningSettings.scaleJ2kAxes);
//			}
//			else if (e.getSource() == rtnAxesScaleTf){
//				PlanningSettings.scaleRtnAxes = parse(rtnAxesScaleTf, PlanningSettings.scaleRtnAxes);
//			}
//			else if (e.getSource() == samAxesScaleTf){
//				PlanningSettings.scaleSamAxes = parse(samAxesScaleTf, PlanningSettings.scaleSamAxes);
//			}
//			else if (e.getSource() == sunScaleTf){
//				PlanningSettings.scaleSc2Sun = parse(sunScaleTf, PlanningSettings.scaleSc2Sun);
//			}
//			else if (e.getSource() == earthScaleTf){
//				PlanningSettings.scaleSc2Earth = parse(earthScaleTf, PlanningSettings.scaleSc2Earth);
//			}
//			else if (e.getSource() == moonScaleTf){
//				PlanningSettings.scaleSc2Moon = parse(moonScaleTf, PlanningSettings.scaleSc2Moon);
//			}
//			else if (e.getSource() == sc2BodyScaleTf){
//				PlanningSettings.scaleSc2Body = parse(sc2BodyScaleTf, PlanningSettings.scaleSc2Body);
//			}
//			else if (e.getSource() == velJ2kScaleTf){
//				PlanningSettings.scaleVel = parse(velJ2kScaleTf, PlanningSettings.scaleVel);
//			}
//			else if (e.getSource() == velBScaleTf){
//				PlanningSettings.scaleVelB = parse(velBScaleTf, PlanningSettings.scaleVelB);
//			}
//			else if (e.getSource() == angmScaleTf){
//				PlanningSettings.scaleMoment = parse(angmScaleTf, PlanningSettings.scaleMoment);
//			}
//			else if (e.getSource() == motionScaleTf){
//				PlanningSettings.scaleMotion = parse(motionScaleTf, PlanningSettings.scaleMotion);
//			}
//			else if (e.getSource() == bodyVelWrtSunScaleTf){
//				PlanningSettings.scaleBodyVelWrtSun = parse(bodyVelWrtSunScaleTf, PlanningSettings.scaleBodyVelWrtSun);
//			}
//			else if (e.getSource() == baseLineWidthTf){
//				PlanningSettings.baseLineWidth = parse(baseLineWidthTf, PlanningSettings.baseLineWidth);
//			}
//			else if (e.getSource() == showSigmaLocSigmaTf){
//				PlanningSettings.showSigmaLocSigmaValue = parse(showSigmaLocSigmaTf, PlanningSettings.showSigmaLocSigmaValue);
//			}
//			else if (e.getSource() == orbitTrackStepSzTf){
//				PlanningSettings.orbitTrackStepSz = parse(orbitTrackStepSzTf, PlanningSettings.orbitTrackStepSz);
//			}
//			else if (e.getSource() == olaScaleTf){
//				PlanningSettings.olaInterval = parse(olaScaleTf, PlanningSettings.olaInterval);
//			}
//			myLView3D.repaint();
//			PlanFocusPanel.this.parent.repaint();
//		}
//	};
//
//	private TargetTable loadTargetData(){
//		if (targetTbl!=null) {
//			myLayer.removeTargetSelectionListener(targetTbl);
//		}
//		ArrayList<Target> targets;
//		
//		if (myLayer.getObjectives().size()>0 && curMosaic>=0) {
//			targets = myLayer.getObjectives().get(curMosaic).getTargets();
//		} else {
//			targets = new ArrayList<Target>();
//		}
//		TargetTableModel tableModel = new TargetTableModel(targets);
//		
//		TargetTable targetTable = new TargetTable(tableModel, myLayer, myLView3D);
//		
//		myLayer.addTargetSelectionListener(targetTable);
//		return targetTable;
//	}
//	
//
//	
//	private JTable loadSequenceData() {
//		SequenceTableModel tableModel = new SequenceTableModel(selectedTarget);
//		
//		JTable seqTable = new SequenceTable(tableModel);
//		
//		return seqTable;
//	}
//	
//	private JTable loadCommandData(){
//		JTable commandTable = null;
//		if (myLayer.getObjectives().size()>0) {
//			CommandTableModel tableModel = new CommandTableModel(selectedSequence);
//			
//			commandTable = new CommandTable(tableModel, myLayer, myLView3D);
//			
//			return commandTable;
//		} else {
//			return new JTable();
//		}
//	}
//	
//	private JTable loadObjData(){
//		DisplayTableModel tableModel = new ObjectiveTableModel(myLayer.getObjectives());
//		
//		JTable result = new ObjectiveTable(tableModel);
//		
//		return result;
//	}
//	
//	
//	/**
//	 * Refreshes the TSE label to reflect whatever tse filename
//	 * is currently stored in the PlanningLayer.
//	 */
//	public void refreshTSELabel(){
//		tseLbl.setText(tsePrompt+(myLayer.getTSEFileName() == null ? "" : myLayer.getTSEFileName()));
//		tseLbl.setToolTipText(myLayer.getTSETooltip());
//	}
//	
//	/**
//	 * Refresh the objective table.  Reloads the data for the table,
//	 * adds the mouse listener and list selection listener, and redraws
//	 * the table in its scrollpane and panel.
//	 */
//	public void refreshObjTable(){
//		objPnl.remove(objSp);
//		objTbl = loadObjData();
//		objTbl.addMouseListener(objMouseListener);
//		objTbl.getSelectionModel().addListSelectionListener(objRowListener);
//		TableColumnAdjuster tca = new TableColumnAdjuster(objTbl);
//		tca.adjustColumns();
//		objSp = new JScrollPane(objTbl);
//		objPnl.add(objSp);
//		objPnl.validate();
//		
//		
//		//if read only don't enable the buttons
//		if(myLayer.isEditable() && myLayer.getObjectives().size()>0){
//			addTarBtn.setEnabled(true);
//			if(selectedSequence!=null){
//				addComBtn.setEnabled(true);
//				addBlkBtn.setEnabled(true);
//			}else{
//				addComBtn.setEnabled(false);
//				addBlkBtn.setEnabled(false);
//			}
//		}else{
//			addTarBtn.setEnabled(false);
//			addComBtn.setEnabled(false);
//			addBlkBtn.setEnabled(false);
//		}
//		
//		//if any testable component could have changed, refresh the violation table
//		refreshViolationTable();
//	}
//	
//	/**
//	 * Implementation of:
//	 * @see edu.asu.jmars.layer.obs.osirisrex.ui.DetailsTablesOwnerInterface#refreshTargetInfo()
//	 * 
//	 * Reloads the target table, and adds the mouse listener and list selection listener if necessary.
//	 * Redraws the table in the scrollpane and panel.  Updates the readout displays: target number, and
//	 * conflict number.  Sets whether the Add Target button should be enabled or not.
//	 */
//	public void refreshTargetInfo(){
//		//refresh table
//    	targetPnl.remove(targetSp);
//    	targetTbl = loadTargetData();
//    	
//    	//only add a mouse listener if there are actually
//    	// objectives to display in the target table
//    	if(myLayer.getObjectives().size()>0 && curMosaic!=-1){
//    		//update the target mouse listener with proper table and objective
//    		if(ttml == null){
//    			ttml = new TargetTableMouseListener(targetTbl, parentFrame, myLayer.getObjectives().get(curMosaic), PlanFocusPanel.this, myLayer.isReadOnly);
//    		}else{
//    			ttml.setTargetTable(targetTbl);
//    			ttml.setObjective(myLayer.getObjectives().get(curMosaic));
//    		}
//    		targetTbl.addMouseListener(ttml);
//    	}
//    	
//    	if (myLayer.getObjectives().size()>0 && curMosaic!=-1) {
//    		targetTbl.getSelectionModel().addListSelectionListener(new TargetTableListener(targetTbl, myLayer.getObjectives().get(curMosaic), PlanFocusPanel.this));
//    	}
//    	
//    	TableColumnAdjuster tca = new TableColumnAdjuster(targetTbl);
//    	tca.adjustColumns();
//    	targetSp = new JScrollPane(targetTbl, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
//    					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//    	targetPnl.add(targetSp);
//    	targetPnl.validate();
//    	
//		//Update readout
//    	//reset readout if there are no more objectives
//    	if(myLayer.getObjectives().size()<1 || curMosaic==-1){
//    		tarTotLbl.setText(tarTotPrompt);
//    		tarConfLbl.setText(tarInConflictPrompt);
//    	}else{
//    		ArrayList<Target> targets = myLayer.getObjectives().get(curMosaic).getTargets();
//    		int numTargets = targets.size();
//    		int numTargetConflicts = 0;
//    		
//    		for (Target target : targets) {
//    			if (target.getViolations().getViolationCount()>0) {
//    				numTargetConflicts++;
//    			}
//    		}
//    		
//    		tarTotLbl.setText(tarTotPrompt+numTargets);
//    		tarConfLbl.setText(tarInConflictPrompt+numTargetConflicts);
//		}
//    	
//    	//if this is from the initial slew obj, don't enable the addTarBtn
//    	
//    	if(myLayer.getObjectives().size()>0 && curMosaic>=0 && myLayer.getObjectives().get(curMosaic).getType().equals(ObjectiveType.INITIAL_ATTITUDE)){
//    		addTarBtn.setEnabled(false);
//    	}else{
//    		addTarBtn.setEnabled(myLayer.isEditable());
//    	}
//    	
//		//if any testable component could have changed, refresh the violation table
//		refreshViolationTable();
//		
//		//no target is now selected
//		selectedTarget = null;
//		selectedSequence = null;
//	}
//	
//	/**
//	 * Implementation of:
//	 * @see edu.asu.jmars.layer.obs.osirisrex.ui.DetailsTablesOwnerInterface#refreshCommandInfo()
//	 * 
//	 * Reloads the command table, and adds the mouse listener and list selection listener if necessary.
//	 * Redraws the table in the scrollpane and panel.  Updates the readout displays: command number, and
//	 * conflict number.  Sets whether the Add Command and Add Block buttons should be enabled or not.
//	 */
//	public void refreshCommandInfo(){
//		//Refresh table
//    	comPnl.remove(comSp);
//		
//    	comTbl = loadCommandData();
//    	
//    	//only add a mouse listener if there are actually objectives added to the layer
//    	if(myLayer.getObjectives().size()>0){
//	    	//update the command mouse listener with proper table and objective
//	    	ctml = new CommandTableMouseListener(comTbl, parentFrame, selectedSequence, PlanFocusPanel.this, PlanningLayer.getBlockFactory(), !myLayer.isEditable());
//	    	comTbl.addMouseListener(ctml);
//    	}
//    	
//    	TableColumnAdjuster tca = new TableColumnAdjuster(comTbl);
//    	tca.adjustColumns();
//    	comSp = new JScrollPane(comTbl, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
//    					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//    	comPnl.add(comSp);
//    	comPnl.validate();
//    	
//		//Update readout
//		int totCommands = 0;
//		int commandsInConflict = 0;
//		
//		if(selectedSequence !=null){
//    		ArrayList<PlannedCommand> commands = selectedSequence.getPlannedCommands();
//    		totCommands = commands.size();
//    		for(PlannedCommand pc : commands){
//    			commandsInConflict += pc.getViolations().getViolationCount();
//    		}
//		}
//		
//		comTotLbl.setText(comTotPrompt+totCommands);    		
//		comConfLbl.setText(comInConflictPrompt+commandsInConflict);
//		
//		//don't enable buttons if is readonly
//		if(myLayer.isEditable()){
//			ArrayList<Objective> myObjectives = myLayer.getObjectives();
//			//enable addCommand button
//			if(myObjectives.size()>0 && selectedSequence!=null){
//				addComBtn.setEnabled(true);
//				addBlkBtn.setEnabled(true);
//			}else{
//				addComBtn.setEnabled(false);
//				addBlkBtn.setEnabled(false);
//			}
//		}
//		
//		//update the lview3d if commands change
//		myLView3D.repaint();
//		//TODO: maybe also update the lview?
//		
//		//if any testable component could have changed, refresh the violation table
//		refreshViolationTable();
//	}
//	
//	private void refreshNavPanel(){
//		int total = myLayer.getObjectives().size();
//		titleLbl.setText("Objective "+(curMosaic+1)+" of "+total);
//		if(total < 2){
//			mosaicLBtn.setEnabled(false);
//			mosaicRBtn.setEnabled(false);
//			mosaicBeginBtn.setEnabled(false);
//			mosaicEndBtn.setEnabled(false);
//		}else{
//			mosaicLBtn.setEnabled(true);
//			mosaicRBtn.setEnabled(true);
//			mosaicBeginBtn.setEnabled(true);
//			mosaicEndBtn.setEnabled(true);
//		}
//	}
//	
//	private void refreshConstraintResultPanel(){
//		conPnl.removeAll();
//		
//		if(myLayer.getObjectives().size()>0 && curMosaic!=-1){
//			if(conPnl.isCollapsed()){
//				conPnl.setCollapsed(false);
//			}
//			conPnl.add(new ConstraintPanel(myLayer.getObjectives().get(curMosaic)));
//		}else{
//			conPnl.setCollapsed(true);
//		}
//		conPnl.revalidate();
//	}
//	
//	private void refreshResultsPanel(){
//		selectedSequence = null;
//		selectedTarget = null;
//		
//		refreshConstraintResultPanel();
//		refreshNavPanel();
//		refreshTargetInfo();
//		refreshSequenceInfo();
//		refreshCommandInfo();
//	}
//	
//	/**
//     * This is a convenience method for making sure the selected sequence stays selected after the refresh.
//     * @see refreshSequenceInfo()
//     */
//	public void refreshSequenceInfoAndRestoreSelection() {
//	    if (seqTbl != null) {
//            int selectedRow = seqTbl.getSelectedRow();
//            refreshSequenceInfo();
//            if (selectedRow > -1) {
//                seqTbl.setRowSelectionInterval(selectedRow, selectedRow);
//            }
//	    }
//    }
//	
//	/**
//	 * Implementation of:
//	 * @see edu.asu.jmars.layer.obs.osirisrex.ui.DetailsTablesOwnerInterface#refreshSequenceInfo()
//	 * 
//	 * Reloads the sequence table, and adds the mouse listener and list selection listener if necessary.
//	 * Redraws the table in the scrollpane and panel.  Updates the readout displays: sequence number, and
//	 * conflict number.  Sets whether the Add Sequence button should be enabled or not.
//	 */
//	public void refreshSequenceInfo() {
//
//		//Refresh table
//		seqPnl.remove(seqSP);
//		seqTbl = loadSequenceData();
//		//add the ttml
//		if(myLayer.getObjectives().size()>0){
//			//update the ttml
//			if(stml==null){
//				stml = new SequenceTableMouseListener(seqTbl, parentFrame, selectedTarget, PlanFocusPanel.this, (PlanLView)parent);
//			}else{
//				stml.setSequenceTable(seqTbl);
//				stml.setTarget(selectedTarget);
//			}
//			seqTbl.addMouseListener(stml);
//		}
//		
//		seqTbl.getSelectionModel().addListSelectionListener(new SequenceTableListener(seqTbl, selectedTarget, PlanFocusPanel.this));
//		
//		TableColumnAdjuster tca = new TableColumnAdjuster(seqTbl);
//		tca.adjustColumns();
//		seqSP = new JScrollPane(seqTbl,
//				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
//				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		seqPnl.add(seqSP);
//		seqPnl.validate();
//		
//		//Update readout
//		int numSequences= 0;
//		int numSequenceConflicts = 0;
//		
//		if(selectedTarget != null){
//			ArrayList<PlannedSequence> sequences = selectedTarget.getSequences();
//			numSequences = sequences.size();
//			for (PlannedSequence sequence : sequences) {
//				int violationNum = sequence.getViolations().getViolationCount();
//				numSequenceConflicts += violationNum;
//			}
//		}
//		
//		
//		seqTotLbl.setText(seqTotPrompt+numSequences);
//		seqConfLbl.setText(seqInConflictPrompt+numSequenceConflicts);
//		
//		//only activate buttons if is editable
//		if(myLayer.isEditable()){
//			if(myLayer.getObjectives().size()>0 && selectedTarget!=null){
//				addSeqBtn.setEnabled(true);
//			}else{
//				addSeqBtn.setEnabled(false);
//			}
//		}
//		
//		//if any testable component could have changed, refresh the violation table
//		refreshViolationTable();
//		
//		//no sequence is now selected
//		selectedSequence = null;
//	}
//	
//	/**
//	 * Implementation of:
//	 * @see edu.asu.jmars.layer.obs.osirisrex.ui.DetailsTablesOwnerInterface#getSelectedSequence()
//	 * 
//	 * Gets the selected sequence (which the command table depends on).
//	 */
//	public PlannedSequence getSelectedSequence() {
//		return selectedSequence;
//	}
//	
//	/**
//	 * Implementation of:
//	 * @see edu.asu.jmars.layer.obs.osirisrex.ui.DetailsTablesOwnerInterface#getSelectedTarget()
//	 * 
//	 * Gets the selected target (which the sequence table depends on).
//	 */
//	public Target getSelectedTarget() {
//		return selectedTarget;
//	}
//	
//	/**
//	 * Implementation
//	 * @see edu.asu.jmars.layer.obs.osirisrex.ui.DetailsTablesOwnerInterface#selectSequence(edu.asu.jmars.layer.obs.osirisrex.PlannedSequence)
//	 * 
//	 * Sets the selected sequence (which the command table depends on).
//	 * 
//	 * @param The {@link PlannedSequence} which is selected.
//	 */
//	public void selectSequence(PlannedSequence ps) {
//		selectedSequence = ps;
//	}
//	
//	/**
//	 * Implementation
//	 * @see edu.asu.jmars.layer.obs.osirisrex.ui.DetailsTablesOwnerInterface#selectTarget(edu.asu.jmars.layer.obs.osirisrex.Target)
//	 * 
//	 * Sets the selected target (which the sequence table depends on).
//	 * 
//	 * @param The {@link Target} which is selected.
//	 */
//	public void selectTarget(Target t) {
//		selectedTarget = t;
//	}
//
//
//	/**
//	 * Implementation
//	 * @see edu.asu.jmars.layer.obs.osirisrex.ui.DetailsTablesOwnerInterface#getPlanLayer()
//	 * 
//	 * @return The plan layer associated with this focus panel
//	 */
//	public PlanningLayer getPlanLayer() {
//		return myLayer;
//	}
//	
//	
//	/**
//	 * Refresh all of the tabs of the plan focus panel, including
//	 * all of the display tables.
//	 * This is called when saving or saving as a plan. If the plan has
//	 * become approved, or is no longer approved, then the read-only
//	 * status of the plan changes, and the UI needs to change accordingly.
//	 */
//	public void refreshUIForReadOnly(){
//		remove(overviewTab);
//		remove(resultsTab);
//		remove(violationTab);
//		remove(kernelTab);
//		remove(settingsTab);
//		remove(finalizeTab);
//		
//		overviewTab = createOverviewPanel();
//		resultsTab = createResultsPanel();
//		violationTab = createViolationPanel();
//		kernelTab = createKernelPanel();
//		settingsTab = createSettingsPanel();
//		finalizeTab = createFinalizePanel();
//
//		addTab("Overview", overviewTab);
//		addTab("Results", resultsTab);
//		addTab("Violations", violationTab);
//		addTab("Kernel Details", kernelTab);
//		addTab("View Settings", settingsTab);
//		addTab("Finalize", finalizeTab);
//		
//		refreshObjTable();
//		refreshResultsPanel();
//		
//		setSelectedComponent(finalizeTab);
//	}
//
//	/**
//	 * @see edu.asu.jmars.layer.obs.osirisrex.ui.ObjectiveListener#objectiveChanged()
//	 */
//	@Override
//	public void objectiveChanged() {
//		refreshResultsPanel();
//	}
//
//	
//	/**
//	 * Get the {@ SiteSearchWindow} associated with this focus 
//	 * panel and the "Site Specific Search..." button.
//	 * @return SiteSearchWindow ssw
//	 */
//	public SiteSearchWindow getSiteSearchWindow(){
//		return ssw;
//	}
//	
//	/**
//	 * Get the {@link ObjCreationWindow} associated with this focus
//	 * panel and the "Start from scratch..." button.
//	 * @return ObjCreationWindow ocw
//	 */
//	public ObjCreationWindow getObjectiveCreationWindow(){
//		return ocw;
//	}
//	
//	/**
//	 * Get the save button from the finalize tab.
//	 * @return JButton saveBtn
//	 */
//	public JButton getSaveButton(){
//		return saveBtn;
//	}
//	
//	
//    /**
//     * @see edu.asu.jmars.layer.obs.osirisrex.ui.DetailsTablesOwnerInterface#showWaiverInformation()
//     * 
//     * The {@link TestableComponent}s found from this class are now
//     * real peices of the plan.  That means that waivers are now allowed
//     * to be added to violations.
//     * @return true
//     */
//    public boolean showWaiverInformation(){
//    	return true;
//    }
//
//	/**
//	 * Causes the fittingListener to be be removed from ThreeDManager. This is intended to
//	 * be called from the PlanLView class whenever a planning layer is being deleted.
//	 */
//	public void deregisterFittingListener() {
//
//		ThreeDManager.getInstance().removeListener(this.fitl);
//
//	}
//
//    /**
//     * Implementation of the FittingListener interface provided by ThreeDManager.
//     *
//     * Implemented to respond to notifications from ThreeDManager when the state of
//     * footprint fitting is enabled or disabled.
//     *
//     */
//    public class FittingListenerImpl implements FittingListener {
//
//		@Override
//		public void setResults(boolean isFittingEnabled) {			
//			PlanningSettings.fitCamFp = isFittingEnabled;
//			PlanFocusPanel.this.refreshFittingCheckbox();
//		}
//    	
//    }

}
