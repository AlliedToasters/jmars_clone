package edu.asu.jmars.layer.map2.custom;

import java.awt.Adjustable;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;

import edu.asu.jmars.Main;
import edu.asu.jmars.layer.LManager;
import edu.asu.jmars.layer.LayerParameters;
import edu.asu.jmars.layer.map2.MapLViewFactory;
import edu.asu.jmars.layer.map2.MapSource;
import edu.asu.jmars.swing.STable;
import edu.asu.jmars.util.Util;
import edu.asu.jmars.util.stable.FilteringColumnModel;
import edu.asu.jmars.util.stable.Sorter;

public class CM_Manager extends JDialog {
    private static CM_Manager cmManager = null;
    private static final int CARD_0 = 0;
    private static final int CARD_1 = 1;
    private static final int CARD_2 = 2;
    
    private static final int CARD_STATE_INITIAL = 0;
    private static final int CARD_STATE_EDIT_UPLOAD = 1;
    private static final int CARD_STATE_VIEW_UPLOAD = 2;
    private static final int CARD_STATE_EDIT_EXISTING = 3;
    
    private static final int TABLE_SELECTION_IN_PROGRESS = 0;
    private static final int TABLE_SELECTION_COMPLETED = 1;
    private static final int TABLE_SELECTION_EXISTING = 2;
    private static final int TABLE_SELECTION_FILE_CHOOSER = 3;
    
    private int card_state = CARD_STATE_INITIAL;
    private int visibleCard = CARD_0;
    
    public static final int TAB_UPLOAD = 0;
    public static final int TAB_EXISTING = 1;
    
    private static final String CARD1_NOTE_DEFAULT_MSG = "This information will apply to all files selected. Some fields may not be editable. "
            + "The information can be updated later by editing the existing map.";
   
    
    private int tableSelectionSource = TABLE_SELECTION_IN_PROGRESS;
        
    //upload tab
    private JButton cancelUploadButton;
    private JButton refreshUploadStatusButton;
    private JButton selectFilesButton;
//    private JButton uploadRemoveButton;
    private JButton viewEditUploadButton;
    private JTabbedPane mainTp;
    private JPanel uploadTabPanel;
    private STable uploadInProgressTable;
    private STable uploadCompletedTable;
    private JPanel uploadInProgressTablePanel;
    private JPanel uploadCompletedTablePanel;
    private JScrollPane uploadTableSp;
    private JFileChooser fileChooser;
    private JPopupMenu uploadRCMenu;
    private JPopupMenu completedRCMenu;
    private JMenuItem cancelUploadMenuItem;
    private JMenuItem viewUploadMenuItem;
//    private JMenuItem removeUploadMenuItem;
    private JMenuItem viewCompletedMenuItem;
    private JMenuItem clearCompletedMenuItem;
    private JSeparator uploadSeparator;
    private JLabel inProgressUploadsLbl;
    private JPanel uploadStartPanel;
    private JScrollPane uploadCompletedTableSp;
    private JButton viewCompletedButton;
    private JButton clearSelectedButton;
    private JButton clearAllCompletedButton;
    private JLabel completedUploadsLbl;
    
    private FileTableModel inProgressUploadFileTableModel;
    private FileTableModel completedUploadFileTableModel;
    private CustomMapTableModel customMapTableModel;
    private SharingTableModel sharingTableModel;
    
    //manage maps tab
//    private JPanel manageMapsTabPanel;
    
    
    //data structures
    //uploadFiles is used for the uploadFilesTable
    private List<CustomMap> uploadInProgressFiles = Collections.synchronizedList(new ArrayList<CustomMap>());
    private List<CustomMap> uploadCompletedFiles = Collections.synchronizedList(new ArrayList<CustomMap>());
    
    //existingMapFiles is used for the existingMapsTable
    private List<CustomMap> existingMapsFiles = Collections.synchronizedList(new ArrayList<CustomMap>());
    private ArrayList<CustomMap> allExistingMapsFiles = new ArrayList<CustomMap>();
    private ArrayList<CustomMap> ownedByMeExistingMapsFiles = null;//to be populated if necessary to shorten search time
    private ArrayList<CustomMap> sharedWithMeExistingMapsFiles = null;//to be populated if necessary to shorten search time
    private ArrayList<CustomMap> sharedByMeExistingMapsFiles = null;//to be populated if necessary to shorten search time
    private ArrayList<String> mapNames = new ArrayList<String>();//used for validation and prevention of duplicates
    private ArrayList<String> inProgressNames = new ArrayList<String>();//used for validation and prevention of duplicates
    
    //shareMapFiles is used for the shareMapsTable
    private ArrayList<CustomMap> sharedMapFiles = new ArrayList<CustomMap>();
    
    
    //dialogUplaodFileList is the list of UploadFile objects while in the main dialog
    private List<CustomMap> dialogUploadFileList = Collections.synchronizedList(new ArrayList<CustomMap>());
    
    //main dialog
    private JDialog mainDialog;
    
    //MainDialog
    private JLabel citationLbl;
    private JScrollPane citationSp;
    private JTextArea citationTextAreaInput;
    
    private JLabel descriptionLbl;
    private JScrollPane descriptionSp;
    private JTextArea descriptionTextAreaInput;
   
    private JLabel enterMapInfoLbl;
    private JLabel enterMapInfoLbl1;
    private JScrollPane enterMapInfoNoteSp;
    private JTextPane enterMapInfoNoteTextPane;
    private JLabel extentLbl;
    private JList<String> selectedFilesList;
    private JLabel keywordsLbl;
    private JScrollPane keywordsSp;
    private JTextArea keywordsTextAreaInput;
    private JLabel linksLbl;
    private JScrollPane linksSp;
    private JTextArea linksTextAreaInput;
    private JPanel mdCardLayoutPanel;
    private JPanel mdOptionsPanel;
    
    private JScrollPane option1ScrollPane;
    private JScrollPane option2ScrollPane;
    private JScrollPane option3ScrollPane;
    private JTextPane option1TextPane;
    private JTextPane option2TextPane;
    private JTextPane option3TextPane;
    private JPanel optionsCard;
    private JPanel optionsCard1;
    private JPanel optionsCard2;
    
    //buttons for different cards
    //card0
    private JButton card0ContinueButton;
    private JButton card0CancelButton;
//    private JButton optionsUploadPromptButton;
    private JRadioButton manauallyEnterRadio;
    private JRadioButton uploadAndProcessRadio;
    private JRadioButton verifyInformationRadio;
    private JPanel uploadOptionPanel;
    //card1
    private JButton card1BackButton;
    private JButton card1CancelButton;
    private JButton card1ClearButton;
    private JButton card1NextButton;
    private JButton card1UploadButton;
    //card2
//    private JButton card2BackButton;
//    private JButton card2UploadButton;
//    private JButton card2ClearButton;
//    private JButton card2CancelButton;
    //end buttons for cards
    
    //inputs and labels for card1
    private JRadioButton regionalRadio;
    private JTextField southernLatInput;
    private JLabel southernmostLatLbl;
    private JTextField unitsInput;
    private JLabel unitsLbl;
    private JLabel westernLonLbl;
    private JRadioButton westRadio;
    private JTextField westernLonInput;
    private JRadioButton globalRadio;
    private JTextField ignoreValueInput;
    private JLabel ignoreValueLbl;
    private JLabel degreesLbl;
    private JRadioButton eastRadio;
    private JTextField easternLonInput;
    private JLabel easternLonLbl;
    private JTextField nameInput;
    private JLabel nameLbl;
    private JTextField northernLatInput;
    private JLabel northernmostLatLbl;
    private ButtonGroup extentButtonGroup;
    private ButtonGroup degreeButtonGroup;
    private ButtonGroup card0ButtonGroup;
    private JPanel card1ButtonPanel;
    //end inputs for card1
    
    
    
    private JLabel selectedFilesLbl;
    private JPanel selectedFilesPanel;
    private JScrollPane selectedFilesScrollPane;
    private JLabel uploadOptionsHeaderLbl;
    
    private String degEStr = "\u00b0E";
    private String degWStr = "\u00b0W";
    private String degNStr = "\u00b0N";
    
//    private Thread processThread = null;
    private Thread uploadThread = null;
    private Thread monitorThread = null;
    private CustomMapMonitor customMapMonitor = null;
    private UploadFileRunner uploadRunner = null;
    
    private DefaultListModel<String> selectedFilesModel;
    private DefaultListModel<String> availableUsersModel;
    private DefaultListModel<String> sharedWithUsersModel;
    private DefaultListModel<String> sharedWithGroupsModel;
    private DefaultListModel<String> availableGroupsModel;
    private DefaultListModel<String> manageUsersListModel;
    private DefaultListModel<String> manageGroupsUsersModel;
    private DefaultListModel<String> manageGroupsAvailableModel;
    private boolean manageUsersDirtyFlag = false;
    private DefaultComboBoxModel<String> groupComboBoxModel;
    
    
    //End MainDialog
    
    //Existing Maps
    private JButton manageSharingButton;
    private JRadioButton sharedWithOthersRadio;
    private STable existingMapsTable;
    private JButton addLayerButton;
    private JButton deleteExistingMapButton;
    private JButton editExistingMapButton;
    private JButton shareExistingMapButton;
    private JButton refreshMapsButton;
    private JCheckBox descriptionCheckbox;
    private JPanel existingMainPanel;
    private JPanel existingMapsButtonPanel;
    private JPanel mapFilterPanel;
    private JScrollPane existingMapsTableSp;
    private JCheckBox fileNameCheckbox;
    private JButton filterButton;
    private JButton clearFilterButton;
    private JPanel filterCheckBoxPanel;
    private JLabel filterMapsLabel;
    private JPanel filterPanel;
    private JRadioButton allMapsRadio;
    private JRadioButton myMapsRadio;
    private JRadioButton sharedWithMeRadio;
    private JTextField filterTextbox;
    private JCheckBox keywordsCheckbox;
    private JCheckBox mapNameCheckbox;
    private JMenuItem editCMMenuItem;
    private JMenuItem deleteCMMenuItem;
    private JMenuItem addLayerMenuItem;
    private JMenuItem shareCMMenuItem;
    private JPopupMenu existingMapRCMenu;
    private JCheckBox ownerCheckbox;
    private JPanel existingMapsTabPanel;
    private boolean activeFilter = false;//flag to know if a filter needs to be re-applied
    
    //sharing
    private JButton removeGroupShareButton;
    private JButton saveSharingChangesButton;
    private JLabel shareUsernameLbl;
    private JPanel shareWithGroupsPanel;
    private JScrollPane sharedWithGroupsSp;
    private JButton shareWithUserButton;
    private JTextField shareWithUserInput;
    private JPanel shareWithUserSp;
    private JLabel sharedMapsLbl;
    private JScrollPane sharedMapsSp;
    private STable sharedMapsTable;
    private JPanel sharedUsersPanel;
    private JLabel sharedWithGroupsLbl;
    private JLabel sharedWithUsersLbl;
    private JList<String> sharedWithUsersList;
    private JScrollPane sharedWithUsersSp;
    private JPanel sharingManagmentButtonPanel;
    private JButton manageUserListButton;
    private JList<String> availableGroupsList;
    private JList<String> sharedWithGroupsList;
    private JButton manageGroupsButton;
    private JDialog sharingManagementDialog;
    private JButton addGroupShareButton;
    private JLabel availableGroupsLbl;
    private JScrollPane availableGroupsSp;
    private JButton cancelSharingButton;
    private JLabel availableUsersLbl;
    private JList<String> availableUsersList;
    private JScrollPane availableUsersSp;
    private JPanel groupsSelectDeselectButtonPanel;
    private JButton unshareWithUserButton;
    private JPanel usersSelectDeselectButtonPanel;
    private JList<String> manageUsersList;
    private JButton manageUsersAddButton;
    private JPanel manageUsersAddPanel;
    private JPanel manageUsersButtonPanel;
    private JDialog manageUsersDialog;
    private JButton manageUsersDoneButton;
    private JLabel manageUsersLbl;
    private JButton manageUsersRemoveButton;
    private JTextField searchUserInput;
    private JScrollPane userListSp;
    private JLabel manageUsersMsgLbl;
    private JButton manageUsersSaveButton;
    
    private JDialog manageGroupsDialog;
    private JPanel groupAddRemoveUserButtonPanel;
    private JButton groupAddUserButton;
//    private JScrollPane groupAvailableListSp;
    private JList<String> groupAvailableUserList;
    private JLabel groupAvailableUsersLbl;
    private JPanel groupAvailableUsersPanel;
    private JPanel groupButtonPanel;
    private JComboBox<String> groupComboBox;
    private JButton groupCreateGroupButton;
    private JButton groupDeleteButton;
    private JButton groupDoneButton;
    private JPanel groupListPanel;
    private JButton groupManageUserListButton;
    private JLabel groupMgmtLbl;
//    private JTextField groupNewGroupInput;
//    private JLabel groupNewGroupLbl;
    private JPanel groupNewGroupPanel;
    private JButton groupRemoveUserButton;
    private JButton groupSaveButton;
    private JList<String> groupUserList;
    private JPanel groupUserListPanel;
    private JScrollPane groupUserListSp;
    private JLabel groupUsersLbl;
    
    
    private JScrollPane groupAvailableUserListSp;
    
    
    private JPanel groupComboBoxPanel;
    private JPanel groupMainListPanel;
    private JButton groupRenameButton;
    private JButton groupSaveAndCloseButton;
    private JPanel groupTopButtonPanel;
    private JLabel sharingGroupsLbl;
    private JButton sharingSaveAndCloseButton;
    private JLabel sharingUsersLbl;
    
    ArrayList<SharingGroup> allSharingGroups = null;
    ArrayList<String> allSharingUsers = null;
    //end sharing
    
    public CM_Manager(JFrame ownerFrame) {
        super(ownerFrame);
        initComponents();
    }
    public CM_Manager(JDialog ownerDialog) {
        super(ownerDialog);
        initComponents();
    }
    public static CM_Manager getInstance() {
        return getInstance(null);
    }
    public static CM_Manager getInstance(Component owner) {
        if (cmManager == null) {
            if (owner == null) {
                cmManager = new CM_Manager(Main.mainFrame);
            } else if (owner instanceof JDialog) {
                JDialog ownerDialog = (JDialog) owner;
                cmManager = new CM_Manager(ownerDialog);
            } else if (owner instanceof JFrame) {
                JFrame ownerFrame = (JFrame) owner;
                cmManager = new CM_Manager(ownerFrame);
            } else {
                cmManager = new CM_Manager(Main.mainFrame);
            }
        } else {
            if (owner != null && owner != cmManager.getParent()) {
                //because we can be called from the MapSettingsDialog or from the File menu on the mainFrame, we need to 
                //properly parent the Dialog if it already existed and is called from the other source.
                cmManager.setVisible(false);
                cmManager.dispose();
                cmManager = null;
                cmManager = getInstance(owner);
            }
        }
        return cmManager;
    }
    public void setSelectedTab(int selectedTab) {
        if (selectedTab == TAB_UPLOAD) {
            mainTp.setSelectedIndex(TAB_UPLOAD);
        } else if (selectedTab == TAB_EXISTING) {
            mainTp.setSelectedIndex(TAB_EXISTING);
        } else {
            mainTp.setSelectedIndex(TAB_UPLOAD);
        }
    }
    private void initComponents() {
        setIconImage(Util.getJMarsIcon());
        uploadInProgressFiles = CustomMapBackendInterface.getInProgressCustomMapList();
        ArrayList<CustomMap> toRemove = new ArrayList<CustomMap>();
        for(CustomMap map : uploadInProgressFiles) {
            if (CustomMap.STATUS_CANCELED.equalsIgnoreCase(map.getStatus())
                || CustomMap.STATUS_COMPLETE.equalsIgnoreCase(map.getStatus()) 
                || CustomMap.STATUS_ERROR.equalsIgnoreCase(map.getStatus())) {
                uploadCompletedFiles.add(map);
                toRemove.add(map);
            } else {
                inProgressNames.add(map.getName());
            }
        }
        uploadInProgressFiles.removeAll(toRemove);
        existingMapsFiles = CustomMapBackendInterface.getExistingMapList();
        allExistingMapsFiles.addAll(existingMapsFiles);
        
        //populate mapNames for validation and prevention of dupes later
        for (CustomMap map : allExistingMapsFiles) {
            mapNames.add(map.getName());
        }
        
        
        //JFrame attributes
        setTitle("Custom Map Manager");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setMaximumSize(new Dimension(5000, 4000));
        setMinimumSize(new Dimension(400, 300));
        setPreferredSize(new Dimension(700, 570));
        setSize(new Dimension(700, 570));
        setResizable(false);

        mainTp = new JTabbedPane();
        mainTp.setTabPlacement(JTabbedPane.TOP);
        mainTp.setMaximumSize(new Dimension(5000, 4000));
        mainTp.setMinimumSize(new Dimension(400, 300));
        mainTp.setPreferredSize(new Dimension(700, 535));
        
        //build Panels for each tab
        buildUploadPanel();
        buildExistingMapsPanel();
        buildSharingManagementDialog();
        
        //build main dialog
        buildMainDialog();
        
        mainTp.addTab("Upload".toUpperCase(), uploadTabPanel);
        mainTp.addTab("Existing Maps".toUpperCase(), existingMapsTabPanel);
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainTp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainTp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );   
        
        //existingMaps
        
        pack();
        
        if (uploadInProgressFiles.size() > 0) {
            kickOffMonitorThread();
        }
    }
    private void buildMainDialog() {
        mainDialog = new JDialog(this, "Custom Map Upload", false);
        mainDialog.setLocationRelativeTo(this);
        Dimension d1 = new Dimension(899, 395);
        mainDialog.setPreferredSize(d1);
        mainDialog.setMinimumSize(d1);
        mainDialog.setResizable(false);

        buildMainDialogOptionsPanel();
        
        mainDialog.add(mdOptionsPanel);
        pack();
    }
    
    private void buildMainDialogOptionsPanel() {
        initializeDialogWidgets();
        
        mdOptionsPanel.setMinimumSize(new Dimension(745, 498));
        mdOptionsPanel.setSize(new Dimension(745, 498));

        //remove borders
        option1ScrollPane.setBorder(new EmptyBorder(0,0,0,0));
        option2ScrollPane.setBorder(new EmptyBorder(0,0,0,0));
        option3ScrollPane.setBorder(new EmptyBorder(0,0,0,0));
        
        selectedFilesModel = new DefaultListModel<String>();
        selectedFilesList.setModel(selectedFilesModel);
        selectedFilesScrollPane.setViewportView(selectedFilesList);
        selectedFilesLbl.setText("Selected File(s)");

        layoutSelectedFilesPanel();

        //main dialog
        mdCardLayoutPanel.setLayout(new CardLayout());

        //card0 stuff
        option3TextPane.setText("Upload the file(s), then allow me to verify the image header information before processing.");
        option3ScrollPane.setViewportView(option3TextPane);
        option2TextPane.setText("Manually enter geospatial and other information about the image(s).");
        option2ScrollPane.setViewportView(option2TextPane);
        option1TextPane.setText("Map(s) will be uploaded and processed using geospatial information found in the image header(s).");
        option1ScrollPane.setViewportView(option1TextPane);
        uploadOptionsHeaderLbl.setText("Upload Options:");
        
        option1TextPane.setEditable(false);
        option2TextPane.setEditable(false);
        option3TextPane.setEditable(false);
        manauallyEnterRadio.setText("Manually Enter:");
        verifyInformationRadio.setText("Verify Image Information:");
        uploadAndProcessRadio.setText("Upload and Process Now:");
        

        layoutOptionsCard0();

        mdCardLayoutPanel.add(optionsCard);
        
        //card1 stuff
        enterMapInfoLbl.setText("Enter Map Information:");
        enterMapInfoNoteTextPane.setText("Note: This information will apply to all files selected. Some fields may not be editable. The information can be updated later by editing the existing map. ");
        enterMapInfoNoteTextPane.setEditable(false);
        enterMapInfoNoteSp.setViewportView(enterMapInfoNoteTextPane);
        nameLbl.setText("Name:");
        ignoreValueLbl.setText("Ignore Value:");
        unitsLbl.setText("Units:");
        extentLbl.setText("Extent:");
        globalRadio.setText("Global");
        regionalRadio.setText("Regional");
        degreesLbl.setText("Degrees:");
        eastRadio.setText("East("+degEStr+")");
        westRadio.setText("West("+degWStr+")");
        easternLonLbl.setText("Easternmost Lon: ");
        westernLonLbl.setText("Westernmost Lon: ");
        northernmostLatLbl.setText("Northernmost Lat: ");
        southernmostLatLbl.setText("Southernmost Lat:");
        
        layoutOptionsCard1();

        mdCardLayoutPanel.add(optionsCard1);

        //card2 
        enterMapInfoLbl1.setText("Enter Map Information (optional):");
        descriptionLbl.setText("Description:");
        descriptionSp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        descriptionSp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        descriptionTextAreaInput.setColumns(20);
        descriptionTextAreaInput.setRows(5);
        descriptionSp.setViewportView(descriptionTextAreaInput);
        linksLbl.setText("Links:");
        linksLbl.setToolTipText("Enter links as a comma separated list (with or without spaces)");
        linksSp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        linksSp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        linksTextAreaInput.setColumns(20);
        linksTextAreaInput.setRows(5);
        linksSp.setViewportView(linksTextAreaInput);
        citationLbl.setText("Citation:");
        citationSp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        citationSp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        citationTextAreaInput.setColumns(20);
        citationTextAreaInput.setRows(5);
        citationSp.setViewportView(citationTextAreaInput);
        keywordsLbl.setText("Keywords:");
        keywordsLbl.setToolTipText("");
        keywordsSp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        keywordsSp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        keywordsTextAreaInput.setColumns(20);
        keywordsTextAreaInput.setRows(5);
        keywordsSp.setViewportView(keywordsTextAreaInput);

        layoutOptionsCard2();

        mdCardLayoutPanel.add(optionsCard2);

        layoutMainDialogPanel();
    }
    
    private void buildUploadPanel() {
        initializeFileChooser();
       
        uploadTabPanel = new JPanel();
        uploadInProgressTable = new STable();
        uploadCompletedTable = new STable();
        
        selectFilesButton = new JButton(selectFilesAction);
        
        getRootPane().setDefaultButton(selectFilesButton);
        
        viewEditUploadButton = new JButton(viewEditUploadAction);
        cancelUploadButton = new JButton(cancelUploadAction);
        refreshUploadStatusButton = new JButton(refreshStatusAction);
        
        inProgressUploadsLbl = new JLabel();
        uploadSeparator = new JSeparator();
        uploadStartPanel = new JPanel();
        uploadCompletedTablePanel = new JPanel();
        uploadCompletedTableSp = new JScrollPane();
        completedUploadsLbl = new JLabel();
        uploadInProgressTablePanel = new JPanel();
        uploadTableSp = new JScrollPane();
        
        uploadRCMenu = new JPopupMenu();
        completedRCMenu = new JPopupMenu();
        cancelUploadMenuItem = new JMenuItem(cancelUploadAction);
        viewUploadMenuItem = new JMenuItem(viewEditUploadAction);
        viewCompletedMenuItem = new JMenuItem(viewCompletedUploadAction);
        clearCompletedMenuItem = new JMenuItem(clearCompletedUploadAction);
        uploadRCMenu.add(viewUploadMenuItem);
        uploadRCMenu.add(cancelUploadMenuItem);
        completedRCMenu.add(viewCompletedMenuItem);
        completedRCMenu.add(clearCompletedMenuItem);
        
        clearAllCompletedButton = new JButton(clearAllCompletedUploadAction);
        clearSelectedButton = new JButton(clearCompletedUploadAction);
        viewCompletedButton = new JButton(viewCompletedUploadAction);
                
        inProgressUploadsLbl.setText("In progress uploads:");
        completedUploadsLbl.setText("Completed/Canceled/Error uploads:");
        

        inProgressUploadFileTableModel = new FileTableModel(uploadInProgressFiles);
        uploadInProgressTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        uploadInProgressTable.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        uploadInProgressTable.setUnsortedTableModel(inProgressUploadFileTableModel);
        uploadTableSp.setViewportView(uploadInProgressTable);
        uploadInProgressTable.getSelectionModel().addListSelectionListener(uploadTableSelectionListener);
        uploadInProgressTable.addMouseListener(uploadTableMouseListener);
        uploadInProgressTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        uploadInProgressTable.setAutoCreateColumnsFromModel(false);
       
        FilteringColumnModel fcm = new FilteringColumnModel();
        for (int i=0; i<inProgressUploadFileTableModel.getColumnCount(); i++) {
            String header = inProgressUploadFileTableModel.getColumnName(i);
            TableColumn tc = new TableColumn(i);
            tc.setHeaderValue(header);
            tc.setPreferredWidth(inProgressUploadFileTableModel.getWidth(header));
            tc.setMinWidth(inProgressUploadFileTableModel.getWidth(header));
            fcm.addColumn(tc);
            fcm.setVisible(tc, inProgressUploadFileTableModel.getDefaultVisibleColumns().contains(header));
        }
        uploadInProgressTable.setColumnModel(fcm);
        
        completedUploadFileTableModel = new FileTableModel(uploadCompletedFiles);
        uploadCompletedTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        uploadCompletedTable.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        uploadCompletedTable.setUnsortedTableModel(completedUploadFileTableModel);
        uploadCompletedTableSp.setViewportView(uploadCompletedTable);
        uploadCompletedTable.getSelectionModel().addListSelectionListener(uploadCompletedTableSelectionListener);
        uploadCompletedTable.addMouseListener(completedTableMouseListener);
        uploadCompletedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        uploadCompletedTable.setAutoCreateColumnsFromModel(false);
       
        FilteringColumnModel cfcm = new FilteringColumnModel();
        for (int i=0; i<completedUploadFileTableModel.getColumnCount(); i++) {
            String header = completedUploadFileTableModel.getColumnName(i);
            TableColumn tc = new TableColumn(i);
            tc.setHeaderValue(header);
            tc.setPreferredWidth(completedUploadFileTableModel.getWidth(header));
            tc.setMinWidth(completedUploadFileTableModel.getWidth(header));
            cfcm.addColumn(tc);
            cfcm.setVisible(tc, completedUploadFileTableModel.getDefaultVisibleColumns().contains(header));
        }
        uploadCompletedTable.setColumnModel(cfcm);
        

        layoutUploadPanel();
        
        //init the main button state to disabled
        toggleMainButtons(false);
        toggleCompletedButtons(false);
    }
    
    private void buildExistingMapsPanel() {
//        manageMapsTabPanel = new JPanel();
        descriptionCheckbox = new JCheckBox();
        existingMainPanel = new JPanel();
        existingMapsButtonPanel = new JPanel();
        mapFilterPanel = new JPanel();
        existingMapsTableSp = new JScrollPane();
        existingMapsTabPanel = new JPanel();
        
        fileNameCheckbox = new JCheckBox();
        filterCheckBoxPanel = new JPanel();
        filterMapsLabel = new JLabel();
        filterPanel = new JPanel();
        filterTextbox = new JTextField();
        keywordsCheckbox = new JCheckBox();
        ownerCheckbox = new JCheckBox();
        mapNameCheckbox = new JCheckBox();
        allMapsRadio = new JRadioButton();
        myMapsRadio = new JRadioButton();
        sharedWithMeRadio = new JRadioButton();
        sharedWithOthersRadio = new JRadioButton();
        
        
        manageSharingButton = new JButton(manageSharingAction);
        editExistingMapButton = new JButton(existingMapEditAction);
        deleteExistingMapButton = new JButton(existingMapDeleteAction);
        addLayerButton = new JButton(existingMapAddLayerAction);
        shareExistingMapButton = new JButton(existingMapShareAction);
        filterButton = new JButton(filterMapsAction);
        clearFilterButton = new JButton(clearFilterMapsAction);
        refreshMapsButton = new JButton(refreshMapsAction);
        
        
        existingMapRCMenu = new JPopupMenu();
        editCMMenuItem = new JMenuItem(existingMapEditAction);
        deleteCMMenuItem = new JMenuItem(existingMapDeleteAction);
        addLayerMenuItem = new JMenuItem(existingMapAddLayerAction);
        shareCMMenuItem = new JMenuItem(existingMapShareAction);
        existingMapRCMenu.add(addLayerMenuItem);
        existingMapRCMenu.add(editCMMenuItem);
        existingMapRCMenu.add(shareCMMenuItem);
        existingMapRCMenu.add(deleteCMMenuItem);
        
        filterMapsLabel.setText("Filter Value:");
        ownerCheckbox.setText("Owner");
        fileNameCheckbox.setText("File Name");
        descriptionCheckbox.setText("Description");
        keywordsCheckbox.setText("Keywords");
        mapNameCheckbox.setText("Map Name");
        allMapsRadio.setText("All maps");
        myMapsRadio.setText("My maps");
        sharedWithMeRadio.setText("Shared with me");
        sharedWithOthersRadio.setText("Shared with others");
        
        keywordsCheckbox.setSelected(true);
        fileNameCheckbox.setSelected(true);
        ownerCheckbox.setSelected(true);
        descriptionCheckbox.setSelected(true);
        mapNameCheckbox.setSelected(true);
        
        
        ButtonGroup mapFilterRadioButtonGroup = new ButtonGroup();
        mapFilterRadioButtonGroup.add(allMapsRadio);
        mapFilterRadioButtonGroup.add(myMapsRadio);
        mapFilterRadioButtonGroup.add(sharedWithMeRadio);
        mapFilterRadioButtonGroup.add(sharedWithOthersRadio);
        allMapsRadio.addActionListener(mapFilterRadioAction);
        myMapsRadio.addActionListener(mapFilterRadioAction);
        sharedWithMeRadio.addActionListener(mapFilterRadioAction);
        sharedWithOthersRadio.addActionListener(mapFilterRadioAction);
        allMapsRadio.setSelected(true);
        
        existingMapsTable = new STable();
        
        
        customMapTableModel = new CustomMapTableModel(existingMapsFiles);
        existingMapsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        existingMapsTable.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        existingMapsTable.setUnsortedTableModel(customMapTableModel);
        existingMapsTableSp.setViewportView(existingMapsTable);
        existingMapsTableSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        existingMapsTableSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        existingMapsTable.getSelectionModel().addListSelectionListener(existingMapsTableSelectionListener);
        existingMapsTable.addMouseListener(existingMapsTableMouseListener);
        existingMapsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        existingMapsTable.setAutoCreateColumnsFromModel(false);
       
        FilteringColumnModel fcm = new FilteringColumnModel();
        for (int i=0; i<customMapTableModel.getColumnCount(); i++) {
            String header = customMapTableModel.getColumnName(i);
            TableColumn tc = new TableColumn(i);
            tc.setHeaderValue(header);
            tc.setPreferredWidth(customMapTableModel.getWidth(header));
            tc.setMinWidth(customMapTableModel.getWidth(header));
            fcm.addColumn(tc);
            fcm.setVisible(tc, customMapTableModel.getDefaultVisibleColumns().contains(header));
        }
        existingMapsTable.setColumnModel(fcm);
        
        layoutExistingMapsTab();
        
        toggleExistingMapButtons();
    }
    private void buildSharingManagementDialog() {
        allSharingGroups = CustomMapBackendInterface.getSharingGroupList();
        sharedMapFiles.addAll(CustomMapBackendInterface.getSharedMapsForUser(allExistingMapsFiles, allSharingGroups));
        
        sharingManagementDialog = new JDialog(this, "Sharing Management", false);
        sharingManagementDialog.setLocationRelativeTo(existingMapsTable);
        sharingManagementDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        sharedMapsSp = new JScrollPane();
        sharedMapsLbl = new JLabel();
        sharedUsersPanel = new JPanel();
        sharedWithUsersLbl = new JLabel();
        sharedWithUsersSp = new JScrollPane();
        sharedWithUsersList = new JList<>();
        shareWithUserSp = new JPanel();
        shareUsernameLbl = new JLabel();
        shareWithUserInput = new JTextField();
        shareWithGroupsPanel = new JPanel();
        availableGroupsSp = new JScrollPane();
        sharedWithGroupsList = new JList<>();
        sharedWithGroupsSp = new JScrollPane();
        availableGroupsList = new JList<>();
        sharedWithGroupsLbl = new JLabel();
        availableGroupsLbl = new JLabel();
        sharingManagmentButtonPanel = new JPanel();
        availableUsersSp = new JScrollPane();
        availableUsersList = new JList<>();
        usersSelectDeselectButtonPanel = new JPanel();
        availableUsersLbl = new JLabel();
        groupsSelectDeselectButtonPanel = new JPanel();
        userListSp = new JScrollPane();
        manageUsersList = new JList<>();
        manageUsersAddPanel = new JPanel();
        manageUsersLbl = new JLabel();
        searchUserInput = new JTextField();
        manageUsersButtonPanel = new JPanel();
        manageUsersMsgLbl = new JLabel();
        sharingUsersLbl = new JLabel();
        sharingGroupsLbl = new JLabel();
        
        manageGroupsDialog = new JDialog(sharingManagementDialog, "Manage Groups", false);
        groupAvailableUsersPanel = new JPanel();
        groupAvailableUsersLbl = new JLabel();
        groupAvailableUserList = new JList<>();
        groupAvailableUserListSp = new JScrollPane();
        groupUserListPanel = new JPanel();
        groupUserListSp = new JScrollPane();
        groupUserList = new JList<>();
        groupUsersLbl = new JLabel();
        groupAddRemoveUserButtonPanel = new JPanel();
        groupListPanel = new JPanel();
        groupComboBox = new JComboBox<>();
        groupMgmtLbl = new JLabel();
        groupButtonPanel = new JPanel();
        groupNewGroupPanel = new JPanel();
        groupComboBoxPanel = new JPanel();
        groupMainListPanel = new JPanel();
        groupTopButtonPanel = new JPanel();
        
        sharedMapsTable = new STable();
        
        cancelSharingButton = new JButton(doneSharingAction);
        saveSharingChangesButton = new JButton(saveSharingAction);
        sharingSaveAndCloseButton = new JButton(sharingSaveAndCloseAction);
        manageUserListButton = new JButton(manageUserListAction);
        addGroupShareButton = new JButton(addGroupShareAction);
        removeGroupShareButton = new JButton(removeGroupShareAction);
        manageGroupsButton = new JButton(manageGroupsAction);
        shareWithUserButton = new JButton(shareWithUserAction);
        unshareWithUserButton = new JButton(unshareWithUserAction);
        manageUsersRemoveButton = new JButton(manageUsersRemoveAction);
        manageUsersRemoveButton.setEnabled(false);
        manageUsersDoneButton = new JButton(manageUsersDoneAction);
        manageUsersAddButton = new JButton(manageUsersAddAction);
        manageUsersSaveButton = new JButton(manageUsersSaveAction);
        groupRemoveUserButton = new JButton(groupRemoveUserAction);
        groupDeleteButton = new JButton(groupDeleteAction);
        groupCreateGroupButton = new JButton(groupCreateAction);
        groupManageUserListButton = new JButton(groupUserListAction);
        groupAddUserButton = new JButton(groupAddUserAction);
        groupDoneButton = new JButton(groupCloseAction);
        groupSaveButton = new JButton(groupSaveAction);
        groupSaveAndCloseButton = new JButton(groupSaveAndCloseAction);
        groupRenameButton = new JButton(groupRenameAction);
        
        saveSharingChangesButton.setEnabled(false);
        sharingSaveAndCloseButton.setEnabled(false);
        groupSaveButton.setEnabled(false);
        groupSaveAndCloseButton.setEnabled(false);
        groupRenameButton.setEnabled(false);
        groupDeleteButton.setEnabled(false);
        
        groupComboBoxModel = new DefaultComboBoxModel<>(new String[]{});
        groupComboBox.setModel(groupComboBoxModel);
        groupComboBox.addItemListener(groupItemListener);
        
        manageGroupsDialog.setResizable(false);
        manageGroupsDialog.setSize(new Dimension(430, 450));
        manageGroupsDialog.setLocationRelativeTo(sharingManagementDialog);
        manageGroupsDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        
        groupListPanel.setBorder(BorderFactory.createEtchedBorder());
        groupMainListPanel.setBorder(BorderFactory.createEtchedBorder());
        
        
        groupListPanel.setBorder(BorderFactory.createEtchedBorder());
        groupNewGroupPanel.setBorder(BorderFactory.createEtchedBorder());

        
        sharingManagementDialog.setPreferredSize(new Dimension(810, 500));
        sharingManagementDialog.setSize(new Dimension(810, 500));
        sharingManagementDialog.setResizable(false);
        sharedMapsSp.setPreferredSize(new Dimension(453, 133));
        shareWithGroupsPanel.setPreferredSize(new Dimension(384, 231));
        sharedUsersPanel.setPreferredSize(new Dimension(384, 231));
        
        sharingTableModel = new SharingTableModel(sharedMapFiles);
        sharedMapsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sharedMapsTable.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        sharedMapsTable.setUnsortedTableModel(sharingTableModel);
        sharedMapsSp.setViewportView(sharedMapsTable);
        sharedMapsTable.getSelectionModel().addListSelectionListener(shareTableSelectionListener);
        sharedMapsTable.addMouseListener(shareTableMouseListener);
        sharedMapsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        sharedMapsTable.setAutoCreateColumnsFromModel(false);
       
        FilteringColumnModel fcm = new FilteringColumnModel();
        for (int i=0; i<sharingTableModel.getColumnCount(); i++) {
            String header = sharingTableModel.getColumnName(i);
            TableColumn tc = new TableColumn(i);
            tc.setHeaderValue(header);
            tc.setPreferredWidth(sharingTableModel.getWidth(header));
            tc.setMinWidth(sharingTableModel.getWidth(header));
            fcm.addColumn(tc);
            fcm.setVisible(tc, sharingTableModel.getDefaultVisibleColumns().contains(header));
        }
        sharedMapsTable.setColumnModel(fcm);
        
        availableUsersModel = new DefaultListModel<String>();
        availableUsersList.setModel(availableUsersModel);
        availableUsersSp.setViewportView(availableUsersList);
        
        sharedWithUsersModel = new DefaultListModel<String>();
        sharedWithUsersList.setModel(sharedWithUsersModel);
        sharedWithUsersSp.setViewportView(sharedWithUsersList);
        
        availableGroupsModel = new DefaultListModel<String>();
        availableGroupsList.setModel(availableGroupsModel);
        availableGroupsSp.setViewportView(availableGroupsList);
        
        sharedWithGroupsModel = new DefaultListModel<String>();
        sharedWithGroupsList.setModel(sharedWithGroupsModel);
        sharedWithGroupsSp.setViewportView(sharedWithGroupsList);
        
        manageUsersListModel = new DefaultListModel<String>();
        manageUsersList.setModel(manageUsersListModel);
        manageUsersList.addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (manageUsersList.getSelectedValuesList().size() > 0) {
                    manageUsersRemoveButton.setEnabled(true);
                } else {
                    manageUsersRemoveButton.setEnabled(false);
                }
            }
        });
        userListSp.setViewportView(manageUsersList);
        
        manageGroupsUsersModel = new DefaultListModel<String>();
        groupUserList.setModel(manageGroupsUsersModel);
        groupUserListSp.setViewportView(groupUserList);
        
        manageGroupsAvailableModel = new DefaultListModel<String>();
        groupAvailableUserList.setModel(manageGroupsAvailableModel);
        groupAvailableUserListSp.setViewportView(groupAvailableUserList);
        
        
        manageUsersAddPanel.setBorder(BorderFactory.createEtchedBorder());
        
        sharedMapsLbl.setText("Shared Maps:");
        shareUsernameLbl.setText("Username:");
        availableUsersLbl.setText("Not Shared:");
        sharedWithGroupsLbl.setText("Shared:");
        availableGroupsLbl.setText("Not Shared:");
        sharedWithUsersLbl.setText("Shared:");
        manageUsersLbl.setText("JMARS Username:");
        manageUsersMsgLbl.setForeground(new Color(255, 51, 51));
        manageUsersMsgLbl.setText("Invalid user entered");
        manageUsersMsgLbl.setVisible(false);
        sharingUsersLbl.setText("Users");
        sharingGroupsLbl.setText("Groups");
        groupAvailableUsersLbl.setText("Not in group:");
        groupUsersLbl.setText("In group:");
        groupMgmtLbl.setText("Group:");
//        groupNewNameLbl.setText("New name:");
//        groupNewGroupLbl.setText("New Group:");
        
        
        sharedUsersPanel.setBorder(BorderFactory.createEtchedBorder());
        shareWithGroupsPanel.setBorder(BorderFactory.createEtchedBorder());
        
        layoutSharingDialog();
        
    }
    
    public void refreshForBodySwitch() {
        uploadInProgressFiles.clear();
        allExistingMapsFiles.clear();
        existingMapsFiles.clear();
        sharedMapFiles.clear();
        uploadCompletedFiles.clear();
        ownedByMeExistingMapsFiles = null;
        sharedByMeExistingMapsFiles = null;
        sharedWithMeExistingMapsFiles = null;
        mapNames.clear();
        inProgressNames.clear();
        CustomMapBackendInterface.setCustomMapServerURL();
        
        ArrayList<CustomMap> tempIPFiles = CustomMapBackendInterface.getInProgressCustomMapList();
        uploadInProgressFiles.addAll(tempIPFiles);
        ArrayList<CustomMap> toRemove = new ArrayList<CustomMap>();
        for(CustomMap map : uploadInProgressFiles) {
            if (CustomMap.STATUS_CANCELED.equalsIgnoreCase(map.getStatus())
                || CustomMap.STATUS_COMPLETE.equalsIgnoreCase(map.getStatus()) 
                || CustomMap.STATUS_ERROR.equalsIgnoreCase(map.getStatus())) {
                uploadCompletedFiles.add(map);
                toRemove.add(map);
            } else {
                inProgressNames.add(map.getName());
            }
        }
        uploadInProgressFiles.removeAll(toRemove);
        ArrayList<CustomMap> tempCMFiles = CustomMapBackendInterface.getExistingMapList();
        existingMapsFiles.addAll(tempCMFiles);
        allExistingMapsFiles.addAll(existingMapsFiles);
        for (CustomMap map : allExistingMapsFiles) {
            mapNames.add(map.getName());
        }
        ArrayList<CustomMap> tempSMFiles = CustomMapBackendInterface.getSharedMapsForUser(allExistingMapsFiles, allSharingGroups);
        sharedMapFiles.addAll(tempSMFiles);
        
        allMapsRadio.setSelected(true);
        filterTextbox.setText("");
        keywordsCheckbox.setSelected(true);
        fileNameCheckbox.setSelected(true);
        ownerCheckbox.setSelected(true);
        descriptionCheckbox.setSelected(true);
        mapNameCheckbox.setSelected(true);
        
        refreshCompletedTable();
        refreshUploadTable();
        refreshExistingMapsTable();
        refreshSharingTable();
    }
    //setting up dialogs and cards for input/edit
    private void setupManualInputDialog() {
        //disable name input if more than one file was selected
        if (selectedFilesModel.size() > 1) {
            nameLbl.setEnabled(false);
            nameInput.setEnabled(false);
        } else {
            nameLbl.setEnabled(true);
            nameInput.setEnabled(true);
        }
    }
//    private void setupNewUpload() {
//        //we need one UploadFile object per upload file
//        dialogUploadFileList.clear();
//        for (int i=0; i<selectedFilesModel.size(); i++) {
//            UploadFile file = new UploadFile(selectedFilesModel.get(i));
//            dialogUploadFileList.add(file);
//        }
//    }
    private void clearAllInputs() {
        clearCard1Inputs();
        clearCard2Inputs();
        card_state = CARD_STATE_INITIAL;//reset
    }
    private void clearCard1Inputs() {
        nameInput.setText("");
        ignoreValueInput.setText("");
        unitsInput.setText("");
        northernLatInput.setText("");
        southernLatInput.setText("");
        westernLonInput.setText("");
        easternLonInput.setText("");
        regionalRadio.setSelected(true);
        eastRadio.setSelected(true);
        enterMapInfoNoteTextPane.setText(CARD1_NOTE_DEFAULT_MSG);//just a reset to be sure
    }
    private void clearCard2Inputs() {
        citationTextAreaInput.setText("");
        linksTextAreaInput.setText("");
        keywordsTextAreaInput.setText("");
        descriptionTextAreaInput.setText("");
    }
    private void storeCard1InputValues(){
        //set the state of the radio buttons
        int extRadio = -1;
        if (globalRadio.isSelected()) {
            extRadio = CustomMap.EXTENT_INPUT_GLOBAL;
        } else if (regionalRadio.isSelected()) {
            extRadio = CustomMap.EXTENT_INPUT_REGIONAL;
        }
        int degRadio = -1;
        if (eastRadio.isSelected()) {
            degRadio = CustomMap.DEGREES_INPUT_EAST;
        } else if (westRadio.isSelected()) {
            degRadio = CustomMap.DEGREES_INPUT_WEST;
        }
        //loop through all of the UploadFile objects and set the values
        for (CustomMap file : dialogUploadFileList) {
            //if this is an edit of an existing map or in progress map, remove, then add this name
            if (tableSelectionSource == TABLE_SELECTION_EXISTING) {
                mapNames.remove(file.getName());
            }
            if (tableSelectionSource == TABLE_SELECTION_IN_PROGRESS) {
                inProgressNames.remove(file.getName());
            }
            
            //nameInput is disabled if multiple files are selected
            if (nameInput.isEnabled()) {
                String name = nameInput.getText();
                if (name.trim().length() == 0) {
                    name = file.getBasename();
                }
                file.setName(name);
            } else {
                file.setName(file.getBasename());
            }
            file.addIgnoreValue(ignoreValueInput.getText());
            file.setUnits(unitsInput.getText());
            file.setExtent(extRadio);
            file.setDegrees(degRadio);
            file.setNorthLat(northernLatInput.getText());
            file.setSouthLat(southernLatInput.getText());
            file.setWestLon(westernLonInput.getText());
            file.setEastLon(easternLonInput.getText());
            
            if (tableSelectionSource == TABLE_SELECTION_EXISTING) {
                mapNames.add(file.getName());
            }
            if (tableSelectionSource == TABLE_SELECTION_IN_PROGRESS) {
                inProgressNames.add(file.getName());
            }
        }
    }
    private void storeCard2InputValues() {
        for (CustomMap file : dialogUploadFileList) {
            file.setCitation(citationTextAreaInput.getText());
            file.setLinks(linksTextAreaInput.getText());
            file.setKeywords(keywordsTextAreaInput.getText());
            file.setDescription(descriptionTextAreaInput.getText());
        }
    }
    private void setUploadInputs() {
        dialogUploadFileList.clear();//clear out the list that the dialog uses
        selectedFilesModel.clear();//clear out the selected file list (display only)
        CustomMap file = null;
        if (tableSelectionSource == TABLE_SELECTION_COMPLETED) {
            file = getSelectedUploadCompletedFile();
        } else if (tableSelectionSource == TABLE_SELECTION_IN_PROGRESS){
            file = getSelectedUploadInProgressFile();
        }
        dialogUploadFileList.add(file);//add to the list the dialog uses
        selectedFilesModel.addElement(file.getName());//add to the selected files display
        fillCard1InputsWithUploadFileValues(file);
        fillCard2InputsWithUploadFileValues(file);
    }
    private void setExistingMapInputs() {
        dialogUploadFileList.clear();//clear out the list that the dialog uses
        selectedFilesModel.clear();//clear out the selected file list (display only)
        CustomMap map = getSelectedExistingMap();
        dialogUploadFileList.add(map);//add to the list the dialog uses
        selectedFilesModel.addElement(map.getName());//add to the selected files display
        fillCard1InputsWithUploadFileValues(map);
        fillCard2InputsWithUploadFileValues(map);
    }
    private void editModeResetUploadCard1Inputs() {
        CustomMap file = getSelectedUploadInProgressFile();
        fillCard1InputsWithUploadFileValues(file);
    }
    private void editModeResetUploadCard2Inputs() {
        CustomMap file = getSelectedUploadInProgressFile();
        fillCard2InputsWithUploadFileValues(file);
    }
    private void editModeResetExistingCard1Inputs() {
        CustomMap file = getSelectedExistingMap();
        fillCard1InputsWithUploadFileValues(file);
    }
    private void editModeResetExistingCard2Inputs() {
        CustomMap file = getSelectedExistingMap();
        fillCard2InputsWithUploadFileValues(file);
    }
    private void fillCard2InputsWithUploadFileValues(CustomMap file) {
        String kws = "";
        boolean first = true;
        for (String kw : file.getKeywords()) {
            if (!first) {
                kws += ",";
            }
            kws += kw;
            first = false;
        }
        keywordsTextAreaInput.setText(kws);
        citationTextAreaInput.setText(file.getCitation());
        descriptionTextAreaInput.setText(file.getDescription());
        linksTextAreaInput.setText(file.getLinks());
    }
    private void fillCard1InputsWithUploadFileValues(CustomMap file) {
        nameInput.setText(file.getName());
        ignoreValueInput.setText(file.getIgnoreValue());
        unitsInput.setText(file.getUnits());
        
        String noteMsg = file.getErrorMessage();
        if (noteMsg == null || noteMsg.trim().equals("")) {
            noteMsg = "No processing notes to report.";
        }
        enterMapInfoNoteTextPane.setText("Note: "+noteMsg);
        
        file.formatCornerPoints();
        file.setupGCPsForCompare();
        
        northernLatInput.setText(file.getNorthLat());
        southernLatInput.setText(file.getSouthLat());
        westernLonInput.setText(file.getWestLon());
        easternLonInput.setText(file.getEastLon());
        
        extentButtonGroup.clearSelection();
        if (file instanceof ExistingMap) {
            //editing of an existing map, the global/regional radio buttons are irrelevant until we are able
            //to re-process a map from this dialog. Set it to regional, it is uneditable anyway. 
            regionalRadio.setSelected(true);
            eastRadio.setSelected(true);
        } else {
            int ext = file.getExtent();
            if (ext == CustomMap.EXTENT_INPUT_GLOBAL) {
                globalRadio.doClick();
            } else if (ext == CustomMap.EXTENT_INPUT_REGIONAL) {
                regionalRadio.doClick();
            } else {
                if (globalRadio.isSelected()) {
                    globalRadio.doClick();
                } else {
                    regionalRadio.doClick();
                }
            }
            int deg = file.getDegrees();
            if (deg == CustomMap.DEGREES_INPUT_WEST) {
                westRadio.doClick();
            } else {
                eastRadio.doClick();
            }
        }
    }
    private void addFilesToUploadTable() {
        int start = uploadInProgressFiles.size();
        int end = start;
        for (CustomMap cardInput : dialogUploadFileList) {
            CustomMap file = (CustomMap) cardInput;
            if (!uploadInProgressFiles.contains(file)) {
                uploadInProgressFiles.add(file);
                inProgressNames.add(file.getName());
                end++;
            }
        }
        inProgressUploadFileTableModel.fireTableRowsInserted(start, end);
        scrollToBottom(uploadTableSp);
    }
    private void prepareCardsForEdit() {
        CustomMap file = null;
        if (tableSelectionSource == TABLE_SELECTION_COMPLETED) {
            file = getSelectedUploadCompletedFile();
        } else if (tableSelectionSource == TABLE_SELECTION_IN_PROGRESS){
            file = getSelectedUploadInProgressFile();
        } 
        if (CustomMap.STATUS_AWAITING_USER_INPUT.equals(file.getStatus())) {
            //edit upload mode
            toggleAllCardInputs(true);
            prepareCardButtonRowsForUploadEdit();
        } else {
            //view mode
            toggleAllCardInputs(false);
            prepareCardButtonRowsForUploadView();
        }
    }
    private void prepareCardButtonRowsForUploadView() {
        card_state = CARD_STATE_VIEW_UPLOAD;
        backAction.setEnabled(false);
        card1UploadButton.setEnabled(false);
        card1ClearButton.setEnabled(false);
        card1CancelButton.setText("Done".toUpperCase());
    }
    private void prepareCardButtonRowsForUploadEdit() {
        card_state = CARD_STATE_EDIT_UPLOAD;
        backAction.setEnabled(false);
        card1UploadButton.setEnabled(true);
        card1ClearButton.setEnabled(true);
        card1UploadButton.setText("Process".toUpperCase());
        card1ClearButton.setText("Reset".toUpperCase());
        card1CancelButton.setText("Done".toUpperCase());
    }
    private void prepareCardButtonRowsForExistingMapEdit() {
        card_state = CARD_STATE_EDIT_EXISTING;
        backAction.setEnabled(false);
        card1UploadButton.setEnabled(true);
        card1ClearButton.setEnabled(true);
        card1UploadButton.setText("Save".toUpperCase());
        card1ClearButton.setText("Reset".toUpperCase());
        card1CancelButton.setText("Cancel".toUpperCase());
    }
    private void toggleAllCardInputsForExistingMapEdit() {
        nameInput.setEnabled(true);
        ignoreValueInput.setEnabled(true);
        unitsInput.setEnabled(true);
        northernLatInput.setEnabled(false);
        southernLatInput.setEnabled(false);
        westernLonInput.setEnabled(false);
        easternLonInput.setEnabled(false);
        globalRadio.setEnabled(false);
        regionalRadio.setEnabled(false);
        eastRadio.setEnabled(false);
        westRadio.setEnabled(false);
        descriptionTextAreaInput.setEnabled(true);
        linksTextAreaInput.setEnabled(true);
        citationTextAreaInput.setEnabled(true);
        keywordsTextAreaInput.setEnabled(true);
    }
    private void toggleAllCardInputs(boolean enabled) {
        nameInput.setEnabled(enabled);
        ignoreValueInput.setEnabled(enabled);
        unitsInput.setEnabled(enabled);
        northernLatInput.setEnabled(enabled);
        southernLatInput.setEnabled(enabled);
        westernLonInput.setEnabled(enabled);
        easternLonInput.setEnabled(enabled);
        globalRadio.setEnabled(enabled);
        regionalRadio.setEnabled(enabled);
        eastRadio.setEnabled(enabled);
        westRadio.setEnabled(enabled);
        descriptionTextAreaInput.setEnabled(enabled);
        linksTextAreaInput.setEnabled(enabled);
        citationTextAreaInput.setEnabled(enabled);
        keywordsTextAreaInput.setEnabled(enabled);
    }
    private void resetCardInputs() {
        nameInput.setEnabled(true);
        ignoreValueInput.setEnabled(true);
        unitsInput.setEnabled(true);
        northernLatInput.setEnabled(true);
        southernLatInput.setEnabled(true);
        westernLonInput.setEnabled(true);
        easternLonInput.setEnabled(true);
        globalRadio.setEnabled(true);
        regionalRadio.setEnabled(true);
        eastRadio.setEnabled(true);
        westRadio.setEnabled(true);
        descriptionTextAreaInput.setEnabled(true);
        linksTextAreaInput.setEnabled(true);
        citationTextAreaInput.setEnabled(true);
        keywordsTextAreaInput.setEnabled(true);
        
        backAction.setEnabled(true);
        card1UploadButton.setEnabled(true);
        card1ClearButton.setEnabled(true);
        
//        card2UploadButton.setEnabled(true);
//        card2ClearButton.setEnabled(true);
        
        card1CancelButton.setText("Cancel".toUpperCase());
        card1ClearButton.setText("Clear".toUpperCase());
        card1UploadButton.setText("Upload".toUpperCase());
        
//        card2CancelButton.setText("Cancel");
//        card2ClearButton.setText("Clear");
//        card2UploadButton.setText("Upload");
    }
    //Actions
    private AbstractAction selectFilesAction = new AbstractAction("Start New Upload".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            uploadInProgressTable.clearSelection();//necessary so that the selected file is the coming from the file chooser
            tableSelectionSource = TABLE_SELECTION_FILE_CHOOSER;
            //If the user selects approve on the file chooser
            if(fileChooser.showOpenDialog(CM_Manager.this) == JFileChooser.APPROVE_OPTION){
                //pop the dialog
                resetCardInputs();
                showCard(CARD_0);//tell the dialog where to start
                selectedFilesModel.clear();
                dialogUploadFileList.clear();
                
                clearAllInputs();
                File[] selFiles = fileChooser.getSelectedFiles();
                for (File f : selFiles) {
                    String name = f.getName();
                    if (name != null && name.trim().length() > 0) {
                        selectedFilesModel.addElement(name);
                    }
                    CustomMap file = new UploadFile(f.getAbsolutePath());
                    dialogUploadFileList.add(file);
                }
                if (selFiles.length == 1) {
                    nameInput.setText(selectedFilesModel.getElementAt(0));
                }
//                setupNewUpload();
                showMainDialog();
            }
        }
    };
    private void showMainDialog() {
        mainDialog.setVisible(true);
        mainDialog.setLocationRelativeTo(fileChooser);
        mainDialog.requestFocus();
    }
    //card0 actions
    private AbstractAction card0ContinueAction = new AbstractAction("Continue".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (uploadAndProcessRadio.isSelected()) {
                kickOffUploadAllFiles(CustomMap.PROCESSING_OPTION_UPLOAD);
                addFilesToUploadTable();
                clearAllInputs();
                mainDialog.setVisible(false);
                kickOffMonitorThread();
            } else if (manauallyEnterRadio.isSelected()) {
                setupManualInputDialog();
                showCard(CARD_1);
            } else if (verifyInformationRadio.isSelected()) {
                kickOffUploadAllFiles(CustomMap.PROCESSING_OPTION_PROMPT);
                addFilesToUploadTable();
                clearAllInputs();
                mainDialog.setVisible(false);
                kickOffMonitorThread();
            } else {
                JOptionPane.showMessageDialog(mainDialog, "Please select an option.","Select an Option",JOptionPane.PLAIN_MESSAGE);
            }
        }
    };
    private AbstractAction card0CancelAction = new AbstractAction("Cancel".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            clearAllInputs();
            mainDialog.setVisible(false);
        }
    };
//    private AbstractAction card0PromptAction = new AbstractAction("3) Upload and Prompt") {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            kickOffUploadAllFiles(CustomMap.PROCESSING_OPTION_PROMPT);
//            addFilesToUploadTable();
//            clearAllInputs();
//            mainDialog.setVisible(false);
//            kickOffMonitorThread();
//        }
//    };
    //end card0 actions
    //card1 actions
    private AbstractAction card1MainAction = new AbstractAction("Upload".toUpperCase()) {//Upload, process, save are possible labels
        @Override
        public void actionPerformed(ActionEvent e) {
            if (card_state == CARD_STATE_EDIT_EXISTING) {
                //editing an existing map here
                if (validateCard1Inputs()) {
                    storeCard1InputValues();
                    storeCard2InputValues();
                    CustomMap map = getSelectedExistingMap();
                    CustomMapBackendInterface.saveMetadata(map);
                    clearAllInputs();
                    mainDialog.setVisible(false);
                    refreshExistingMapsRow(existingMapsTable.getSelectedRow());
                    CustomMapBackendInterface.finishMapEdit(map);
                }
            } else {
                if (validateCard1Inputs()) {
                    storeCard1InputValues();
                    storeCard2InputValues();
                    if (card_state == CARD_STATE_EDIT_UPLOAD) {
                        //we are in edit/process mode here
                        processInProgressFile(getSelectedUploadInProgressFile());
                    } else {
                        kickOffUploadAllFiles(CustomMap.PROCESSING_OPTION_MANUAL);
                    }
                    addFilesToUploadTable();
                    clearAllInputs();
                    mainDialog.setVisible(false);
                    kickOffMonitorThread();
                } else {
                    mainDialog.requestFocus();
                }
            }
        }
    };
    private AbstractAction nextAction = new AbstractAction("Next".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            showCard(CARD_2);
        }
    };
    private AbstractAction backAction = new AbstractAction("Back".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (visibleCard == CARD_1) {
                showCard(CARD_0);
            } else if (visibleCard == CARD_2) {
                showCard(CARD_1);
            }
        }
    };
    private AbstractAction clearAction = new AbstractAction("Clear".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            //if we are editing and resetting values   
            if (card_state == CARD_STATE_EDIT_UPLOAD) {
                if (visibleCard == CARD_1) {
                    editModeResetUploadCard1Inputs();
                } else if (visibleCard == CARD_2) {
                    editModeResetUploadCard2Inputs();
                }
            } else if (card_state == CARD_STATE_EDIT_EXISTING) {
                if (visibleCard == CARD_1) {
                    editModeResetExistingCard1Inputs();
                } else if (visibleCard == CARD_2) {
                    editModeResetExistingCard2Inputs();
                }
            } else {
                //clear
                if (visibleCard == CARD_1) {
                    clearCard1Inputs();
                } else if (visibleCard == CARD_2){
                    clearCard2Inputs();
                }
            }
        }
    };
    private AbstractAction cardCancelAction = new AbstractAction("Cancel".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            clearAllInputs();
            mainDialog.setVisible(false);
        }
    };
    //end card1 actions
    //existing map actions
    private AbstractAction existingMapEditAction = new AbstractAction("Edit".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            doEditExistingMap();
        }
    };
    private void doEditExistingMap() {
        clearAllInputs();
        tableSelectionSource = TABLE_SELECTION_EXISTING;
        setExistingMapInputs();
        toggleAllCardInputsForExistingMapEdit();
        prepareCardButtonRowsForExistingMapEdit();
        showCard(CARD_1);
        showMainDialog();
    }
    public void showSharingDialog() {
        //only make these requests to the backend if they actually open up the dialog
        if (allSharingUsers == null) {
            allSharingUsers = CustomMapBackendInterface.getUserSharingList();
        }
        if (sharedMapFiles == null || sharedMapFiles.size() == 0) {
            sharedMapFiles.addAll(CustomMapBackendInterface.getSharedMapsForUser(allExistingMapsFiles,allSharingGroups));
            refreshSharingTable();
        }
        sharedWithGroupsModel.clear();
        sharedWithUsersModel.clear();
        availableGroupsModel.clear();
        availableUsersModel.clear();
        sharedMapsTable.clearSelection();
        sharingManagementDialog.setVisible(true);
    }
    private void saveSharingChanges() {
        int selectedRow = sharedMapsTable.getSelectedRow();
        boolean somethingChanged = false;
        for (CustomMap map : sharedMapFiles) {
            if (map.hasSharingUserChanges()) {
                if (CustomMapBackendInterface.shareMapWithUsers(map)) {
                    map.updateSharedUsers();
                }
                somethingChanged = true;
            }
            if (map.hasSharingGroupChanges()) {
                if (CustomMapBackendInterface.shareMapWithGroups(map)) {
                    map.updateSharedGroups();
                }
                somethingChanged = true;
            }
            if (somethingChanged) {
                if (sharedByMeExistingMapsFiles != null) {//could be null if they never clicked the radio button
                    if (!sharedByMeExistingMapsFiles.contains(map)) {
                        sharedByMeExistingMapsFiles.add(map);
                    }
                }
            }
            somethingChanged = false;
        }
        refreshSharingTable();
        if (selectedRow > -1) {
            sharedMapsTable.setRowSelectionInterval(selectedRow, selectedRow);
        }
        refreshExistingMapsTable();
    }
    private AbstractAction manageSharingAction = new AbstractAction("Manage Sharing".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            showSharingDialog();
        }
    };
    private AbstractAction existingMapAddLayerAction = new AbstractAction("Add Layer".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            addMapLayer();
        }
    };
    private AbstractAction existingMapDeleteAction = new AbstractAction("Delete".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            deleteExistingMaps();
        }
    };
    private AbstractAction existingMapShareAction = new AbstractAction("Share".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            shareExistingMaps();
        }
    };
    private AbstractAction filterMapsAction = new AbstractAction("Apply Filter".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            filterExistingMaps();
        }
    };
    private AbstractAction clearFilterMapsAction = new AbstractAction("Clear Filter".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            clearExistingMapsFilter();
        }
    };
    private AbstractAction refreshMapsAction = new AbstractAction("Refresh Maps".toUpperCase()) {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshExistingMaps();
        }
    };
    private AbstractAction mapFilterRadioAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            filterExistingMapsByRadioButton();
        }
    };
    private void shareExistingMaps() {
        ArrayList<CustomMap> mapsToShare = getSelectedExistingMaps();
        for (CustomMap map : mapsToShare) {
            if (!sharedMapFiles.contains(map)) {
                sharedMapFiles.add(map);
            }
        }
        
        refreshSharingTable();
        showSharingDialog();
        //select map in sharing table if only one map was shared
        if (mapsToShare.size() == 1) {
            Sorter sorter = sharedMapsTable.getSorter();
            for (int i=0; i<sharedMapFiles.size(); i++) {
                int unsortRow = sorter.unsortRow(i);
                CustomMap map = sharingTableModel.getMap(unsortRow);
                if (map == mapsToShare.get(0)) {
                    sharedMapsTable.setRowSelectionInterval(unsortRow, unsortRow);
                    break;
                }
            }
        }
        scrollToBottom(sharedMapsSp);
    }
    //Upload tab button actions
    private AbstractAction viewEditUploadAction = new AbstractAction("View/Edit".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            clearAllInputs();
            tableSelectionSource = TABLE_SELECTION_IN_PROGRESS;
            setUploadInputs();
            prepareCardsForEdit();
            showCard(CARD_1);
            showMainDialog();
        }
    };
    private AbstractAction cancelUploadAction = new AbstractAction("Cancel".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: confirm dialog before canceling
            CustomMap file = getSelectedUploadInProgressFile();
            //do not change the status of the UploadFile here. It will be managed in the cancelUpload call
            if (file.getCustomMapId() != null ) {//should never be null
                //CustomMapBackendInterface.setCancelFlag(file);
                CustomMapBackendInterface.cancelUpload(file);
                file.setStatus(CustomMap.STATUS_CANCELED);
                file.setCancelFlag(true);
                kickOffMonitorThread();
            }
        }
    };
    private AbstractAction clearAllCompletedUploadAction = new AbstractAction("Clear All".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            clearAllCompletedButton.setEnabled(false);
            int response = JOptionPane.showConfirmDialog(existingMainPanel, 
                    "Are you sure you want to clear all completed/canceled/error maps?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                ArrayList<CustomMap> toRemove = new ArrayList<CustomMap>();
                for (CustomMap file : uploadCompletedFiles) {
                    if (CustomMap.STATUS_CANCELED.equalsIgnoreCase(file.getStatus())
                        || CustomMap.STATUS_ERROR.equalsIgnoreCase(file.getStatus())) {
                        CustomMapBackendInterface.deleteUploadFile(file);
                    }
                }
                uploadCompletedFiles.clear();
                refreshCompletedTable();
            }
            clearAllCompletedButton.setEnabled(true);
        }
    };
    private AbstractAction clearCompletedUploadAction = new AbstractAction("Clear Selected".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            clearSelectedButton.setEnabled(false);
            int response = JOptionPane.showConfirmDialog(existingMainPanel, 
                    "Are you sure you want to clear selected completed/canceled/error maps?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                Sorter sorter = uploadCompletedTable.getSorter();
                ArrayList<CustomMap> toRemove = new ArrayList<CustomMap>();
                int[] selRows = uploadCompletedTable.getSelectedRows();
                for (int idx : selRows) {
                    int row = sorter.unsortRow(idx);
                    CustomMap file = completedUploadFileTableModel.getFile(row);
                    if (CustomMap.STATUS_COMPLETE.equalsIgnoreCase(file.getStatus())) {
                        toRemove.add(file);
                    } else {
                        if (CustomMapBackendInterface.deleteUploadFile(file)) {
                            toRemove.add(file);
                        } else {
                            String msg = "There was a problem clearing the selected uploads. \nIf this problem persists, please select Help->Report a Problem to contact support.";
                            JOptionPane.showMessageDialog(clearSelectedButton, msg);
                        }
                    }
                }
                uploadCompletedFiles.removeAll(toRemove);
                refreshCompletedTable();
            }
            clearSelectedButton.setEnabled(true);
        }
    };
    private AbstractAction viewCompletedUploadAction = new AbstractAction("View".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            clearAllInputs();
            tableSelectionSource = TABLE_SELECTION_COMPLETED;
            setUploadInputs();
            prepareCardsForEdit();
            showCard(CARD_1);
            showMainDialog();
        }
    };

    private AbstractAction refreshStatusAction = new AbstractAction("Refresh Status of Uploads".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            kickOffMonitorThread();
        }
    };


    private AbstractAction doneSharingAction = new AbstractAction("Close".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isSharingDialogDirty) {
                int confirm = JOptionPane.showConfirmDialog(sharingManagementDialog, 
                    "You may have unsaved sharing changes. Are you sure you want to close without saving? \n"
                    + "Note: the changes will exist, and can be saved until JMARS is restarted.", "Confirm Done", JOptionPane.YES_NO_OPTION);
                if (JOptionPane.YES_OPTION == confirm) {
                    sharingManagementDialog.setVisible(false);
                }
            } else {
                sharingManagementDialog.setVisible(false);
            }
            
        }
    };
    private AbstractAction saveSharingAction = new AbstractAction("Save".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            saveSharingChanges(); 
            saveSharingChangesButton.setEnabled(false);
            sharingSaveAndCloseButton.setEnabled(false);
            isSharingDialogDirty = false;
        }
    };
    private AbstractAction sharingSaveAndCloseAction = new AbstractAction("Save and Close".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            saveSharingChanges();
            saveSharingChangesButton.setEnabled(false);
            sharingSaveAndCloseButton.setEnabled(false);
            isSharingDialogDirty = false;
            sharingManagementDialog.setVisible(false);
        }
    };
    private AbstractAction manageUsersRemoveAction = new AbstractAction("Remove Selected".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            manageUsersDirtyFlag = true;
            List<String> selectedValuesList = manageUsersList.getSelectedValuesList();
            for (String val : selectedValuesList) {
                manageUsersListModel.removeElement(val);
            }
            manageUsersRemoveButton.setEnabled(false);
        }
    };
    private AbstractAction manageUsersAddAction = new AbstractAction("Add User".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            manageUsersMsgLbl.setVisible(false);
            String user = searchUserInput.getText();
            if (user.trim().length() > 0) {
                if (CustomMapBackendInterface.checkValidUser(user)) {
                    manageUsersDirtyFlag = true;
                    if (!manageUsersListModel.contains(user)) {
                        manageUsersListModel.addElement(user);
                        searchUserInput.setText("");
                    }
                } else {
                    manageUsersMsgLbl.setText("Invalid user entered");
                    manageUsersMsgLbl.setVisible(true);
                }
            }
        }
    };
    private AbstractAction manageUsersSaveAction = new AbstractAction("Save and Close".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            manageUsersDirtyFlag = false;
            ArrayList<String> toRemove = new ArrayList<String>();
            
            for(String user : allSharingUsers) {//loop through the complete list of favorite users
                if (!manageUsersListModel.contains(user)) {//if the user is not currently in the selected favorite list, flag for removal
                    toRemove.add(user);
                    if (availableUsersModel.contains(user)) {//if it is in the sharing dialog available user list remove it
                        availableUsersModel.removeElement(user);
                    }
                    if (manageGroupsAvailableModel.contains(user)) {
                        manageGroupsAvailableModel.removeElement(user);
                    }
                }
                
            }
            allSharingUsers.removeAll(toRemove);//remove all the favorite users that were not still in the list
            
            for (int x=0; x<manageUsersListModel.size(); x++) {//now let's add in the new users in the list. (Don't remove from "not shared" lists).
                if (!allSharingUsers.contains(manageUsersListModel.get(x))) {
                    String user = manageUsersListModel.get(x);
                    allSharingUsers.add(user);
                    if (sharedMapsTable.getSelectedRowCount() == 1) {
                        if (!availableUsersModel.contains(user) && !sharedWithUsersModel.contains(user)) {
                            //if it is not already in the favorite list and it is not already selected for this map, add the username to the favorite
                            //list on the sharing dialog
                            availableUsersModel.addElement(user);
                        }
                    }
                    if (manageGroupsDialog.isVisible()) {
                        //let's update manage groups dialog
                        if (groupComboBox.getSelectedIndex() > 0) {
                            if (!manageGroupsAvailableModel.contains(user) && !manageGroupsUsersModel.contains(user)) {
                                manageGroupsAvailableModel.addElement(user);
                            }
                        }
                    }
                }
            }
            
            searchUserInput.setText("");//clear out the add user input on manage users
            manageUsersMsgLbl.setText("");
            manageUsersMsgLbl.setVisible(false);
            
            //allSharingUsers should now be up to date and ready for sharing table selection action.
            //update the list of users in the database
            if (CustomMapBackendInterface.updateSharingUserList(allSharingUsers)) {
                manageUsersDialog.setVisible(false);
                manageUsersDialog.dispose();
            }
        }
    };
    private AbstractAction manageUsersDoneAction = new AbstractAction("Close".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean continueFlag = false;
            if (manageUsersDirtyFlag) {
                int confirm = JOptionPane.showConfirmDialog(manageUsersDialog, 
                    "You have unsaved changes. Are you sure you want to close without saving?", "Confirm Close", JOptionPane.YES_NO_OPTION);
                if (JOptionPane.YES_OPTION == confirm) {
                    continueFlag = true;
                }
            }
            if (continueFlag || !manageUsersDirtyFlag) {
                manageUsersMsgLbl.setVisible(false);
                searchUserInput.setText("");
                manageUsersMsgLbl.setText("");
                manageUsersMsgLbl.setVisible(false);
                manageUsersDialog.setVisible(false);
                manageUsersDialog.dispose();
            }
        }
    };
    private AbstractAction manageUserListAction = new AbstractAction("Favorite Users".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            showManageUsersDialog(sharingManagementDialog);
        }
    };
    private AbstractAction groupUserListAction = new AbstractAction("Favorite Users".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            showManageUsersDialog(manageGroupsDialog);
        }
    };
    
    private boolean isSharingDialogDirty = false;
    private void setSharingDialogDirty() {
        isSharingDialogDirty = true;
        saveSharingChangesButton.setEnabled(true);
        sharingSaveAndCloseButton.setEnabled(true);
    }
    private AbstractAction addGroupShareAction = new AbstractAction(">>") {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> selectedGroups = availableGroupsList.getSelectedValuesList();
            DefaultListModel<String> model = (DefaultListModel<String>) availableGroupsList.getModel();
            for (int i=model.getSize()-1; i>=0; i--) {
                String group = model.getElementAt(i);
                if (selectedGroups.contains(group)) {
                    if (!sharedWithGroupsModel.contains(group)) {
                        sharedWithGroupsModel.addElement(group);
                        model.remove(i);
                        for (SharingGroup aGroup : allSharingGroups) {
                            if (aGroup.getName().trim().equalsIgnoreCase(group)) {
                                getSelectedSharingMap().addTempSharedGroup(aGroup);
                                setSharingDialogDirty();
                                break;
                            }
                        }
                    }
                }
            }
        }
    };
    
    private AbstractAction removeGroupShareAction = new AbstractAction("<<") {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> selectedGroups = sharedWithGroupsList.getSelectedValuesList();
            DefaultListModel<String> model = (DefaultListModel<String>) sharedWithGroupsList.getModel();
            for (int i=model.getSize()-1; i>=0; i--) {
                String group = model.getElementAt(i);
                if (selectedGroups.contains(group)) {
                    if (!availableGroupsModel.contains(group)) {
                        availableGroupsModel.addElement(group);
                        model.remove(i);
                        getSelectedSharingMap().removeTempSelectedGroup(group);
                        setSharingDialogDirty();
                    }
                }
            }
            
        }
    };
    private AbstractAction shareWithUserAction = new AbstractAction(">>") {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> selectedUsers = availableUsersList.getSelectedValuesList();
            DefaultListModel<String> model = (DefaultListModel<String>) availableUsersList.getModel();
            for (int i=model.getSize()-1; i>=0; i--) {
                String user = model.getElementAt(i);
                if (selectedUsers.contains(user)) {
                    if (!sharedWithUsersModel.contains(user)) {
                        sharedWithUsersModel.addElement(user);
                        model.remove(i);
                        getSelectedSharingMap().addTempSharedUser(user);
                        setSharingDialogDirty();
                    }
                }
            }
            
        }
    };
    private AbstractAction unshareWithUserAction = new AbstractAction("<<") {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> selectedUsers = sharedWithUsersList.getSelectedValuesList();
            DefaultListModel<String> model = (DefaultListModel<String>) sharedWithUsersList.getModel();
            for (int i=model.getSize()-1; i>=0; i--) {
                String user = model.getElementAt(i);
                if (selectedUsers.contains(user)) {
                    if (!availableUsersModel.contains(user)) {
                        availableUsersModel.addElement(user);
                        model.remove(i);
                        getSelectedSharingMap().removeTempSelectedUser(user);
                        setSharingDialogDirty();
                    }
                }
            }
        }
    };
    private AbstractAction manageGroupsAction = new AbstractAction("Manage Groups".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            populateGroupComboBox();
            manageGroupsDialog.setVisible(true);
        }
    };
    private void populateGroupComboBox() {
        groupComboBoxModel.removeAllElements();
        groupComboBoxModel.addElement("");
        for(SharingGroup group : allSharingGroups) {
            groupComboBoxModel.addElement(group.getName());
        }
    }
    private void populateGroupLists() {
        String selectedGroup = (String)groupComboBox.getSelectedItem();
        ArrayList<String> groupUsers = null;
        manageGroupsUsersModel.clear();
        manageGroupsAvailableModel.clear();
        if (selectedGroup.trim().length() > 0) {
            for(SharingGroup group : allSharingGroups) {
                if (group.getName().equals(selectedGroup)) {
                    groupUsers = group.getUsers();
                    for (String user : groupUsers) {
                        manageGroupsUsersModel.addElement(user);
                    }
                    break;
                }
            }
            ArrayList<String> groupAvailUsers = new ArrayList<String>();
            if (allSharingUsers != null) {
                groupAvailUsers.addAll(allSharingUsers);
            }
            if (groupUsers != null) {
                groupAvailUsers.removeAll(groupUsers);
                for(String user : groupAvailUsers) {
                    manageGroupsAvailableModel.addElement(user);
                }
            }
            groupRenameButton.setEnabled(true);
            groupDeleteButton.setEnabled(true);
        } else {
            groupRenameButton.setEnabled(false);
            groupDeleteButton.setEnabled(false);
        }
    }
    private void storeGroupInfo() {
        if (groupListsChangedFlag) {
            if (previouslySelectedGroup != null && previouslySelectedGroup.trim().length() > 0) {
                for(SharingGroup group : allSharingGroups) {
                    if (group.getName().equals(previouslySelectedGroup)) {
                        group.clearUsers();
                        for (int i=0; i<manageGroupsUsersModel.size(); i++) {
                            group.addUser(manageGroupsUsersModel.get(i));
                        }
                        group.setDirtyFlag(true);
                        makeGroupManagementDirty();
                        break;
                    }
                }
            }
            groupListsChangedFlag = false;
        } 
    }
    
    private void changeGroupNameInComboBox(String oldName, String newName) {
        groupComboBoxModel.removeElement(oldName);
        groupComboBoxModel.addElement(newName);
        groupComboBox.setSelectedIndex(0);
    }
    
    
    private boolean groupManagementDirtyFlag = false;
    private boolean groupListsChangedFlag = false;
    private void makeGroupManagementDirty() {
        groupManagementDirtyFlag = true;
        groupSaveButton.setEnabled(true);
        groupSaveAndCloseButton.setEnabled(true);
    }
    private AbstractAction groupRemoveUserAction = new AbstractAction("<<") {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            groupListsChangedFlag = true;
            
            List<String> selectedList = groupUserList.getSelectedValuesList();
            if (selectedList.size() > 0) {
                makeGroupManagementDirty();
                for (String user : selectedList) {
                    if (!manageGroupsAvailableModel.contains(user)) {
                        manageGroupsAvailableModel.addElement(user);
                    }
                    manageGroupsUsersModel.removeElement(user);
                }
                storeGroupInfo();
            }
        }
    };
    private AbstractAction groupAddUserAction = new AbstractAction(">>") {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            groupListsChangedFlag = true;
            List<String> selectedList = groupAvailableUserList.getSelectedValuesList();
            if (selectedList.size() > 0) {
                makeGroupManagementDirty();
                for(String group : selectedList) {
                    if (!manageGroupsUsersModel.contains(group)) {
                        manageGroupsUsersModel.addElement(group);
                    }
                    manageGroupsAvailableModel.removeElement(group);
                }
                storeGroupInfo();
            }
        }
    };
    private AbstractAction groupDeleteAction = new AbstractAction("Delete".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            deleteGroup();
        }
    };
    private AbstractAction groupCreateAction = new AbstractAction("Create".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            createGroup();
        }
    };
    private AbstractAction groupCloseAction = new AbstractAction("Close".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (groupManagementDirtyFlag) {
                int response = JOptionPane.showConfirmDialog(manageGroupsDialog, 
                    "There may be unsaved changes. Are you sure you want to close this window? \n"
                    + "Note: the changes will exist, and can be saved until JMARS is restarted.", "Confirm Done", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    manageGroupsDialog.setVisible(false);
                }
            } else {
                manageGroupsDialog.setVisible(false);
                int selectedRow = sharedMapsTable.getSelectedRow();
                refreshSharingTable();
                if (selectedRow > -1) {
                    sharedMapsTable.setRowSelectionInterval(selectedRow, selectedRow);
                }
            }
        }
    };
    private AbstractAction groupSaveAction = new AbstractAction("Save".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            saveGroupChanges();
            groupManagementDirtyFlag = false;
            groupListsChangedFlag = false;
            groupSaveButton.setEnabled(false);
            groupSaveAndCloseButton.setEnabled(false);
        }
    };
    private AbstractAction groupSaveAndCloseAction = new AbstractAction("Save and Close".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            saveGroupChanges();
            groupManagementDirtyFlag = false;
            groupListsChangedFlag = false;
            groupSaveButton.setEnabled(false);
            groupSaveAndCloseButton.setEnabled(false);
            manageGroupsDialog.setVisible(false);
        }
    };
    
    private AbstractAction groupRenameAction = new AbstractAction("Rename".toUpperCase()) {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String newName = JOptionPane.showInputDialog(manageGroupsDialog, "Enter a new name for this group: ", "Rename Group", JOptionPane.QUESTION_MESSAGE);
            if (newName != null && newName.length() > 0) {
                if (groupComboBoxModel.getIndexOf(newName) == -1) {
                    String selectedGroup = (String) groupComboBox.getSelectedItem();
                    for (SharingGroup group : allSharingGroups) {
                        if (group.getName().equalsIgnoreCase(selectedGroup)) {
                            group.setName(newName);
                            CustomMapBackendInterface.editSharingGroup(group);
                            changeGroupNameInComboBox(selectedGroup, group.getName());
                            groupComboBox.setSelectedItem(group.getName());
                            break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(manageGroupsDialog, 
                    "Duplicate group names are not allowed.", "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    };

    private void saveGroupChanges() {
        if (groupManagementDirtyFlag) {
            for (SharingGroup group : allSharingGroups) {
                if (group.isDirtyFlag()) {
                    CustomMapBackendInterface.editSharingGroup(group);
                    group.setDirtyFlag(false);
                }
                
            }
        }
    }
    private void createGroup() {
        String newGroup = JOptionPane.showInputDialog(manageGroupsDialog, "Enter the name of the new group:", "Create New Group", JOptionPane.QUESTION_MESSAGE);
        newGroup = newGroup.trim();
        if (newGroup != null && newGroup.length() > 0) {
            if (groupComboBoxModel.getIndexOf(newGroup) != -1) {
                groupComboBox.setSelectedItem(newGroup);
            } else {
                SharingGroup group = new SharingGroup(newGroup);
                CustomMapBackendInterface.createGroup(group);
                allSharingGroups.add(group);
                groupComboBox.addItem(newGroup);
                groupComboBox.setSelectedItem(newGroup);
            }
        }
    }
    private void deleteGroup() {
        int response = JOptionPane.showConfirmDialog(groupListPanel, 
                    "Are you sure you want to delete the selected group?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            SharingGroup toRemove = null;
            String groupName = (String) groupComboBox.getSelectedItem();
            for (SharingGroup group : allSharingGroups) {
                if (group.getName().equals(groupName)) {
                    CustomMapBackendInterface.deleteSharingGroup(group);
                    groupComboBox.setSelectedIndex(0);
                    groupComboBox.removeItem(groupName);
                    toRemove = group;
                    break;
                }
            }
            if (toRemove != null) {
                allSharingGroups.remove(toRemove);
                for(CustomMap map : sharedMapFiles) {
                    ArrayList<SharingGroup> sharedWith = map.getSharedWithGroups();
                    ArrayList<SharingGroup> tempSharedWith = map.getTempSharedWithGroups();
                    if (sharedWith.contains(toRemove)) {
                        sharedWith.remove(toRemove);
                    }
                    if (tempSharedWith.contains(toRemove)) {
                        tempSharedWith.remove(toRemove);
                    }
                }
            }
        }
    }
    
    private void showManageUsersDialog(JDialog parent) {
        createManageUsersDialog(parent);
        this.manageUsersListModel.clear();
        for (String user : allSharingUsers) {
            this.manageUsersListModel.addElement(user);
        }
        this.manageUsersDialog.setVisible(true);
        searchUserInput.setText("");
        manageUsersMsgLbl.setText("");
        manageUsersMsgLbl.setVisible(false);
    }
    private void showCard(int card) {
        boolean showCard0 = false;
        boolean showCard1 = false;
        boolean showCard2 = false;
        switch(card) {
            case CARD_0:
                card1ButtonPanel.setVisible(false);
                showCard0 = true;
                visibleCard = CARD_0;
                break;
            case CARD_1:
                card1ButtonPanel.setVisible(true);
                card1NextButton.setEnabled(true);
                if (card_state == CARD_STATE_EDIT_UPLOAD || card_state == CARD_STATE_VIEW_UPLOAD || card_state == CARD_STATE_EDIT_EXISTING) {
                    card1BackButton.setEnabled(false);
                }
                showCard1 = true;
                visibleCard = CARD_1;
                break;
            case CARD_2:
                card1ButtonPanel.setVisible(true);
                card1BackButton.setEnabled(true);
                card1NextButton.setEnabled(false);
                showCard2 = true;
                visibleCard = CARD_2;
                break;
            default:
                card1ButtonPanel.setVisible(false);
                showCard0 = true;
                visibleCard = CARD_0;
                break;
        }
        
        optionsCard.setVisible(showCard0);
        optionsCard1.setVisible(showCard1);
        optionsCard2.setVisible(showCard2);    
         
    }
    
    private boolean validateCard1Inputs() {
        boolean allInputValid = true;
        
        String maxLat = northernLatInput.getText();
        String minLat = southernLatInput.getText();
        String minLon = westernLonInput.getText();
        String maxLon = easternLonInput.getText();
 
        if (regionalRadio.isSelected()) {
            if ("".equals(maxLat.trim()) || "".equals(minLat.trim()) || "".equals(minLon.trim()) || "".equals(maxLon.trim())) {
                JOptionPane.showMessageDialog(CM_Manager.this, "Some corner point information is missing. Please fill in and try again.",
                            "Custom Map Validation", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            try {
                double mxLat = Double.parseDouble(maxLat);
                double miLat = Double.parseDouble(minLat);
                double mxLon = Double.parseDouble(maxLon);
                double miLon = Double.parseDouble(minLon);
                
                if (Double.compare(mxLat,90) > 0 || Double.compare(mxLat,-90) < 0
                 || Double.compare(miLat,90) > 0 || Double.compare(miLat,-90) < 0
                 || Double.compare(miLon,360) > 0 || Double.compare(miLon,-360) < 0
                 || Double.compare(mxLon,360) > 0 || Double.compare(mxLon,-360) < 0
                        ) {
                    JOptionPane.showMessageDialog(CM_Manager.this, "Some corner point information is invalid. Please try again.",
                                "Custom Map Validation", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(CM_Manager.this, "Some corner point information is invalid. Please try again.",
                                "Custom Map Validation", JOptionPane.ERROR_MESSAGE);
                    return false;
            }
        }
        String name = nameInput.getText();
        name = name.trim();
        if (name.length() > 80) {
            JOptionPane.showMessageDialog(CM_Manager.this, "Name is too long (80 characters max).",
                                "Custom Map Name", JOptionPane.ERROR_MESSAGE);
                    return false;
        }
        boolean checkNameFlag = true;
        if (tableSelectionSource == TABLE_SELECTION_EXISTING) {//edit existing
            CustomMap selMap = getSelectedExistingMap();
            if (selMap != null) {//if this is an edit of an existing map
                String oldName = selMap.getName().trim();
                if (oldName.equals(name)) {
                    checkNameFlag = false;//this name will exist in the list, do not flag it
                }
            }
        }
        if (checkNameFlag && mapNames.contains(name)) {//if this is not the same name as the selected existing map and it is in the list
            JOptionPane.showMessageDialog(CM_Manager.this, "You already have a map by this name. Please choose a different name.",
                            "Custom Map Name", JOptionPane.ERROR_MESSAGE);
                return false;
        }
        checkNameFlag = true;
        if (tableSelectionSource == TABLE_SELECTION_IN_PROGRESS) {//edit existing
            CustomMap selMap = getSelectedUploadInProgressFile();
            if (selMap != null) {//if this is an edit of an existing map
                String oldName = selMap.getName().trim();
                if (oldName.equals(name)) {
                    checkNameFlag = false;//this name will exist in the list, do not flag it
                }
            }
        }
        if (checkNameFlag && inProgressNames.contains(name)) {//if this is not the same name as the selected existing map and it is in the list
            JOptionPane.showMessageDialog(CM_Manager.this, "You already have a map in progress by this name. Please choose a different name.",
                            "Custom Map Name", JOptionPane.ERROR_MESSAGE);
                return false;
        }
        
        return allInputValid;
    }
    private void clearExistingMapsFilter() {
        activeFilter = false;
        mapNameCheckbox.setSelected(true);
        ownerCheckbox.setSelected(true);
        keywordsCheckbox.setSelected(true);
        descriptionCheckbox.setSelected(true);
        fileNameCheckbox.setSelected(true);
        
        filterTextbox.setText("");
        
        existingMapsFiles.clear();
        if (sharedWithMeRadio.isSelected()) {
            existingMapsFiles.addAll(getSharedWithMeMaps());
        } else if (myMapsRadio.isSelected()) {
            existingMapsFiles.addAll(getOwnedByMeMaps());
        } else if (sharedWithOthersRadio.isSelected()){
            existingMapsFiles.addAll(getSharedWithOthersMaps());
        } else {
            existingMapsFiles.addAll(allExistingMapsFiles);
        }
        
        refreshExistingMapsTable();
    }
    private void refreshExistingMaps() {
        CustomMapBackendInterface.refreshCapabilities();
        
        allExistingMapsFiles.clear();
        existingMapsFiles.clear();
        sharedMapFiles.clear();
        ownedByMeExistingMapsFiles = null;
        sharedByMeExistingMapsFiles = null;
        sharedWithMeExistingMapsFiles = null;
        mapNames.clear();
        
        ArrayList<CustomMap> tempCMFiles = CustomMapBackendInterface.getExistingMapList();
        existingMapsFiles.addAll(tempCMFiles);
        allExistingMapsFiles.addAll(existingMapsFiles);
        
        for (CustomMap map : allExistingMapsFiles) {
            mapNames.add(map.getName());
        }
        
        ArrayList<CustomMap> tempSMFiles = CustomMapBackendInterface.getSharedMapsForUser(allExistingMapsFiles, allSharingGroups);
        sharedMapFiles.addAll(tempSMFiles);
        
        refreshExistingMapsTable();
        refreshSharingTable();
        filterExistingMaps();
    }
    private void filterExistingMapsByRadioButton() {
        //this action will check for an active filter and if it exists, call filter. 
        //otherwise, it will reset the data to the correct set based on the selected option.
        if (activeFilter) {
            filterExistingMaps();
        } else {
            existingMapsFiles.clear();
            if (sharedWithMeRadio.isSelected()) {
                existingMapsFiles.addAll(getSharedWithMeMaps());
            } else if (myMapsRadio.isSelected()) {
                existingMapsFiles.addAll(getOwnedByMeMaps());
            } else if (sharedWithOthersRadio.isSelected()){ 
                existingMapsFiles.addAll(getSharedWithOthersMaps());
            } else {
                existingMapsFiles.addAll(allExistingMapsFiles);
            }
            refreshExistingMapsTable();
        }
    }
    private ArrayList<CustomMap> getSharedWithMeMaps() {
        if (sharedWithMeExistingMapsFiles == null) {
            populateFilterLists();
        }
        return sharedWithMeExistingMapsFiles;
    }
    private ArrayList<CustomMap> getSharedWithOthersMaps() {
        if (sharedByMeExistingMapsFiles == null) {
            populateFilterLists();
        }
        return sharedByMeExistingMapsFiles;
    }
    private void populateFilterLists() {
        sharedWithMeExistingMapsFiles = new ArrayList<CustomMap>();
        ownedByMeExistingMapsFiles = new ArrayList<CustomMap>();
        sharedByMeExistingMapsFiles = new ArrayList<CustomMap>();
        String owner = Main.USER;
        for (CustomMap map : allExistingMapsFiles) {
            if (owner.equalsIgnoreCase(map.getOwner())) {
                ownedByMeExistingMapsFiles.add(map);
            } else {
                sharedWithMeExistingMapsFiles.add(map);
            }
            
            if (map.isSharedWithOthers()) {
                sharedByMeExistingMapsFiles.add(map);
            }
        }
    }
    private ArrayList<CustomMap> getOwnedByMeMaps() {
        if (ownedByMeExistingMapsFiles == null) {
            populateFilterLists();
        }
        return ownedByMeExistingMapsFiles;
    }
    private void filterExistingMaps() {
        activeFilter = true;
        boolean useName = mapNameCheckbox.isSelected();
        boolean useOwner = ownerCheckbox.isSelected();
        boolean useKW = keywordsCheckbox.isSelected();
        boolean useDesc = descriptionCheckbox.isSelected();
        boolean useFN = fileNameCheckbox.isSelected();

        
        String filterValue = filterTextbox.getText();
        
        ArrayList<CustomMap> toSearch = null;
        if (sharedWithMeRadio.isSelected()) {
            toSearch = getSharedWithMeMaps();
        } else if (myMapsRadio.isSelected()) {
            toSearch = getOwnedByMeMaps();
        } else if (sharedWithOthersRadio.isSelected()){
            toSearch = getSharedWithOthersMaps();
        } else {
            toSearch = allExistingMapsFiles;
        }
        
        ArrayList<CustomMap> toAdd = new ArrayList<CustomMap>();
        if (filterValue.trim().length() == 0) {
            //reset, they did not filter by any value
            toAdd.addAll(toSearch);
        } else if (!useName && !useOwner && !useKW && !useDesc && ! useFN) {
            //reset, they did not filter by any type
            toAdd.addAll(toSearch);
        } else {
            //we have to pare the list down
            String[] filterVals = filterValue.split(",", 0);
            for (CustomMap map : toSearch) {
                for(String val : filterVals) {
                    val = val.toLowerCase().trim();
                    if (useName && map.getName().toLowerCase().indexOf(val) > -1) {
                        toAdd.add(map);
                        break;
                    }
                    if (useOwner && map.getOwner().toLowerCase().indexOf(val) > -1) {
                        toAdd.add(map);
                        break;
                    }
                    if (useFN && map.getBasename().toLowerCase().indexOf(val) > -1) {
                        toAdd.add(map);
                        break;
                    }
                    if (useDesc && map.getDescription().toLowerCase().indexOf(val) > -1) {
                        toAdd.add(map);
                        break;
                    }
                    if (useKW) { 
                        String[] kws = map.getKeywords();
                        for (String kw : kws) {
                            kw = kw.toLowerCase().trim();
                            if (kw.indexOf(val) > -1) {
                                toAdd.add(map);
                                break;
                            }
                        }
                    }
                }     
            }
        }
        
        existingMapsFiles.clear();
        existingMapsFiles.addAll(toAdd);
        refreshExistingMapsTable();
    }

    //listeners
    private ListSelectionListener uploadTableSelectionListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                if (uploadInProgressTable.getSelectedRow() != -1) {
                    toggleMainButtons(true);
                } else {
                    toggleMainButtons(false);
                }
            }
        }
    };
    private ListSelectionListener uploadCompletedTableSelectionListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                if (uploadCompletedTable.getSelectedRow() != -1) {
                    toggleCompletedButtons(true);
                } else {
                    toggleCompletedButtons(false);
                }
            }
        }
    };
    private ListSelectionListener existingMapsTableSelectionListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                toggleExistingMapButtons();
            }
        }
    };
    private String previouslySelectedGroup = "";
    private ItemListener groupItemListener = new ItemListener() {
        
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                storeGroupInfo();
                populateGroupLists();
                previouslySelectedGroup = (String) groupComboBox.getSelectedItem();
                
            }
        }
    };
    private ListSelectionListener shareTableSelectionListener = new ListSelectionListener() {
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                populateSharingLists();
            }
        }
    };
    private void populateSharingLists() {
        sharedWithGroupsModel.clear();
        sharedWithUsersModel.clear();
        availableGroupsModel.clear();
        availableUsersModel.clear();
        
        if (sharedMapsTable.getSelectedRow() != -1) {
            CustomMap map = getSelectedSharingMap();
            ArrayList<SharingGroup> sharedWithGroups = map.getTempSharedWithGroups();
            ArrayList<String> sharedWithUsers = map.getTempSharedWithUsers();
            ArrayList<String> groupsToRemove = new ArrayList<String>();
            ArrayList<String> usersToRemove = new ArrayList<String>();
            for (SharingGroup group : sharedWithGroups) {
                sharedWithGroupsModel.addElement(group.getName());
                groupsToRemove.add(group.getName());
            }
            for (String user : sharedWithUsers) {
                sharedWithUsersModel.addElement(user);
                usersToRemove.add(user);
            }
            for (SharingGroup group : allSharingGroups) {
                if (!groupsToRemove.contains(group.getName())) {
                    availableGroupsModel.addElement(group.getName());
                }
            }
            for (String user : allSharingUsers) {
                if (!usersToRemove.contains(user)) {
                    availableUsersModel.addElement(user);
                }
            }
            
        }
    }
    private ActionListener extentListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            CustomMap file = null;
            if (tableSelectionSource == TABLE_SELECTION_COMPLETED) {
                file = getSelectedUploadCompletedFile();
            } else if (tableSelectionSource == TABLE_SELECTION_IN_PROGRESS){
                file = getSelectedUploadInProgressFile();
            } else if (tableSelectionSource == TABLE_SELECTION_EXISTING) {
                file = getSelectedExistingMap();
            } else if (tableSelectionSource == TABLE_SELECTION_FILE_CHOOSER) {
                if (dialogUploadFileList != null && dialogUploadFileList.size() > 0) {
                    file = dialogUploadFileList.get(0);
                }
            }
            if (file == null) {
                throw new IllegalStateException("The correct file could not be found in the extent listener! Table selection source = "+tableSelectionSource);
            }
            if(globalRadio.isSelected()){
                //only set the values if they changed to global
                if (file.getExtent() != CustomMap.EXTENT_INPUT_GLOBAL) {
                    //set these values in case they click back on regional
                    file.setNorthLat(northernLatInput.getText());
                    file.setSouthLat(southernLatInput.getText());
                    file.setWestLon(westernLonInput.getText());
                    file.setEastLon(easternLonInput.getText());
                }
                northernLatInput.setText("90");
                southernLatInput.setText("-90");
                westernLonInput.setText("-180");
                easternLonInput.setText("180");
//                eastRadio.setSelected(true);
//                westRadio.setEnabled(false);
//                northernLatInput.setEnabled(false);
//                southernLatInput.setEnabled(false);
//                westernLonInput.setEnabled(false);
//                easternLonInput.setEnabled(false);
                file.setExtent(CustomMap.EXTENT_INPUT_GLOBAL);
                
            } else if(regionalRadio.isSelected()){
//                westRadio.setEnabled(true);
//                northernLatInput.setEnabled(true);
//                southernLatInput.setEnabled(true);
//                westernLonInput.setEnabled(true);
//                easternLonInput.setEnabled(true);
                
                northernLatInput.setText(file.getNorthLat());
                southernLatInput.setText(file.getSouthLat());
                westernLonInput.setText(file.getWestLon());
                easternLonInput.setText(file.getEastLon());
                file.setExtent(CustomMap.EXTENT_INPUT_REGIONAL);
            }
        }
    };
    private CustomMap getSelectedUploadInProgressFile() {
        CustomMap file = null;
        int row = uploadInProgressTable.getSelectedRow();
        if (row != -1) {
            int select = uploadInProgressTable.getSorter().unsortRow(row);
            file = inProgressUploadFileTableModel.getFile(select);
        } else {
            //we came in from the file chooser
            file = dialogUploadFileList.get(0);
        }
        return file;
    }
    private CustomMap getSelectedUploadCompletedFile() {
        CustomMap file = null;
        int row = uploadCompletedTable.getSelectedRow();
        if (row != -1) {
            int select = uploadCompletedTable.getSorter().unsortRow(row);
            file = completedUploadFileTableModel.getFile(select);
        }
        return file;
    }
    private CustomMap getSelectedExistingMap() {
        CustomMap map = null;
        int row = existingMapsTable.getSelectedRow();
        if (row != -1) {
            int select = existingMapsTable.getSorter().unsortRow(row);
            map = customMapTableModel.getMap(select);
        }
        return map;
    }
    private ArrayList<CustomMap> getSelectedExistingMaps() {
        Sorter sort = existingMapsTable.getSorter();
        ArrayList<CustomMap> selected = new ArrayList<CustomMap>();
        int[] idxs = existingMapsTable.getSelectedRows();
        for (int x : idxs) {
            int realIdx = sort.unsortRow(x);
            selected.add(customMapTableModel.getMap(realIdx));
        }
        return selected;
    }
    private CustomMap getSelectedSharingMap() {
        CustomMap map = null;
        int row = sharedMapsTable.getSelectedRow();
        if (row != -1) {
            int select = sharedMapsTable.getSorter().unsortRow(row);
            map = sharingTableModel.getMap(select);
        }
        return map;
    }
    private MouseListener uploadTableMouseListener = new MouseListener() {
        public void mouseReleased(MouseEvent e) {
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseClicked(MouseEvent e) {
            if(SwingUtilities.isRightMouseButton(e)){
                //if no file is selected, disable
                if (uploadInProgressTable.getSelectedRow() == -1) {
                    cancelUploadMenuItem.setEnabled(false);
                    viewUploadMenuItem.setEnabled(false);
                } else {
                    cancelUploadMenuItem.setEnabled(true);
                    viewUploadMenuItem.setEnabled(true);
                }
                uploadRCMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    };
    private MouseListener completedTableMouseListener = new MouseListener() {
        public void mouseReleased(MouseEvent e) {
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseClicked(MouseEvent e) {
            if(SwingUtilities.isRightMouseButton(e)){
                //if no file is selected, disable
                if (uploadCompletedTable.getSelectedRow() == -1) {
                    viewCompletedMenuItem.setEnabled(false);
                    clearCompletedMenuItem.setEnabled(false);
                } else if (uploadCompletedTable.getSelectedRowCount() > 1){
                    viewCompletedMenuItem.setEnabled(false);
                    clearCompletedMenuItem.setEnabled(true);
                } else {
                    viewCompletedMenuItem.setEnabled(true);
                    clearCompletedMenuItem.setEnabled(true);
                }
                completedRCMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    };
    
    private boolean areAnySelectedExistingMapsSharedWithMe() {
    	Sorter sorter = existingMapsTable.getSorter();
		int[] rows = existingMapsTable.getSelectedRows();
		for (int i=0; i<rows.length; i++) {
			int row = rows[i];
			int realRow = sorter.unsortRow(row);
			CustomMap map = existingMapsFiles.get(realRow);
			if (!map.getOwner().equalsIgnoreCase(Main.USER)) {
				return true;
			}
		}
		return false;
    }
    
    private MouseListener existingMapsTableMouseListener = new MouseListener() {
        public void mouseReleased(MouseEvent e) {
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseClicked(MouseEvent e) {
            if(SwingUtilities.isRightMouseButton(e)){
                //if no file is selected, disable
                if (existingMapsTable.getSelectedRow() == -1) {
                    editCMMenuItem.setEnabled(false);
                    deleteCMMenuItem.setEnabled(false);
                    addLayerMenuItem.setEnabled(false);
                    shareCMMenuItem.setEnabled(false);
                } else {
                	boolean sharedSelected = areAnySelectedExistingMapsSharedWithMe();
                	if (sharedSelected) {
                		editCMMenuItem.setEnabled(false);
                		deleteCMMenuItem.setEnabled(false);
                        shareCMMenuItem.setEnabled(false);
                        addLayerMenuItem.setEnabled(true);
                	} else {
		                if(existingMapsTable.getSelectedRowCount() > 1) {
		                    editCMMenuItem.setEnabled(false);
		                } else {
		                    editCMMenuItem.setEnabled(true);
		                }
		                deleteCMMenuItem.setEnabled(true);
		                addLayerMenuItem.setEnabled(true);
		                shareCMMenuItem.setEnabled(true);
                	}
                }
                existingMapRCMenu.show(e.getComponent(), e.getX(), e.getY());
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                if (e.getClickCount() == 2) {
                    doEditExistingMap();
                }
            }
        }
    };
    private MouseListener shareTableMouseListener = new MouseListener() {
        public void mouseReleased(MouseEvent e) {
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseClicked(MouseEvent e) {
            if(SwingUtilities.isRightMouseButton(e)){
                //if no file is selected, disable
//                if (uploadTable.getSelectedRow() == -1) {
//                    cancelUploadMenuItem.setEnabled(false);
//                    viewUploadMenuItem.setEnabled(false);
//                    removeUploadMenuItem.setEnabled(false);
//                } else {
//                    cancelUploadMenuItem.setEnabled(true);
//                    viewUploadMenuItem.setEnabled(true);
//                    removeUploadMenuItem.setEnabled(true);
//                }
//                uploadRCMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    };

    private void kickOffUploadAllFiles(int processingOption) {
        for(CustomMap cardInput : dialogUploadFileList) {
            cardInput.setSelectedUploadProcess(processingOption);
            //prevent duplicate map names before sending to upload
            int suffix = 1;
            String cardInputName = cardInput.getName();
            while (mapNames.contains(cardInput.getName()) || inProgressNames.contains(cardInput.getName())) {
                cardInput.setName(cardInputName+"("+suffix+")");
                suffix++;
            }
        }
        kickOffUploadThread(dialogUploadFileList);
    }
    private void processInProgressFile(CustomMap file) {
        file.setStatus(CustomMap.STATUS_PROCESSING);//need to change the status so that monitor cares about it
        CustomMapBackendInterface.updateHeaderValuesAndStartProcessing(file);
    }
    private void kickOffMonitorThread() {
        if (customMapMonitor == null) {
            customMapMonitor = new CustomMapMonitor();
        }
        customMapMonitor.setUploadFiles(uploadInProgressFiles);
        try {
            if (monitorThread == null || !monitorThread.isAlive()) {
                monitorThread = new Thread(customMapMonitor);
                monitorThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Thread problem...starting new thread");
            monitorThread = new Thread(customMapMonitor);
            monitorThread.start();
        }
    }
    private void kickOffUploadThread(List<CustomMap> files) {
        if (uploadRunner == null) {
            uploadRunner = new UploadFileRunner(files);
        } else {
            uploadRunner.addUploadFiles(files);
        }
        if (uploadThread == null || !uploadThread.isAlive()) {
            uploadThread = new Thread(uploadRunner);
            uploadThread.start();
        }
    }
    
    private void addMapLayer() {
        for(CustomMap map : getSelectedExistingMaps()){
//        CustomMap map = getSelectedExistingMap();
            if(map != null){
                try{
                    //combine keywords in the description
                    String desc = map.getDescription();
                    String[] keywords = map.getKeywords();
                    if(keywords.length>0){
                        desc += "\n\nKeywords: ";
                        for(String word : keywords){
                            desc+=word+", ";
                        }
                        desc = desc.substring(0, desc.length()-2);
                    }
                    
                    LayerParameters lp = new LayerParameters(map.getFilename(), map.getCitation(), desc, map.getUnits(), map.getLinks());
                    //create the map layer
                    //TODO: when/if there is numeric data, need to pass it in with the plot array list
                    new MapLViewFactory().createLayer(map.getGraphicSource(), new ArrayList<MapSource>(), lp, null);
                    //refresh the lmanager
                    LManager.getLManager().repaint();
                    //update the CusomMap last used date
                    //getting current date and time using Date class
                    Date dateobj = new Date();
                    map.setLastUsedDate(ExistingMap.dateFormat.format(dateobj));
                    //refresh the selected row of the table
                    refreshExistingMapsTable();
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(CM_Manager.this, "Unable to load custom map, "+map.getName()+". Please see log for more details.",
                            "Custom Map Failure", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
    }
    private void deleteExistingMaps() {
        ArrayList<CustomMap> maps = getSelectedExistingMaps();
        int response = JOptionPane.showConfirmDialog(existingMainPanel, 
                    "Are you sure you want to delete the selected maps?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            boolean error = false;
            for(CustomMap map : maps) {
                boolean success = CustomMapBackendInterface.deleteCustomMap(map);
                if (success) {
                    CustomMapBackendInterface.finishDeleteCustomMap(map);
                    existingMapsFiles.remove(map);//model for existing map table 
                    allExistingMapsFiles.remove(map);//data structure for all maps
                    if (sharedWithMeExistingMapsFiles != null) {//for radio button
                        sharedWithMeExistingMapsFiles.remove(map);
                    }
                    if (ownedByMeExistingMapsFiles != null) {//for radio button
                        ownedByMeExistingMapsFiles.remove(map);
                    }
                    if (sharedByMeExistingMapsFiles != null) {//for radio button
                        sharedByMeExistingMapsFiles.remove(map);
                    }
                    if (sharedMapFiles != null) {//for sharing management dialog
                        sharedMapFiles.remove(map);
                    }
                    refreshExistingMapsTable();
                    refreshSharingTable();
                } else {
                    error = true;
                }
            }
            
            CustomMapBackendInterface.refreshCapabilities();
            
             if (error) {
                String msg = "There was a problem deleting the selected custom maps. \nSome maps may not have deleted. If this problem persists, please select Help->Report a Problem to contact support.";
                JOptionPane.showMessageDialog(deleteExistingMapButton, msg);
            }
        }
    }
//    private void refreshStatusOfUploads() {
//        for (int i=0; i<uploadInProgressFiles.size(); i++) {
//                    CustomMap file = uploadInProgressFiles.get(i);
//                    file.setStatus(CustomMap.STATUS_UPDATING_STATUS);
//                    updateProgressStatus(i);
//        }
//        Runnable r = new Runnable() {
// 
//            @Override
//            public void run() {
//                CustomMapBackendInterface.checkMapProcessingStatus(uploadInProgressFiles);
//                final ArrayList<CustomMap> toRemove = new ArrayList<CustomMap>();
//                for (int i=0; i<uploadInProgressFiles.size(); i++) {
//                    CustomMap file = uploadInProgressFiles.get(i);
//                    if (CustomMap.STATUS_IN_PROGRESS.equals(file.getStatus()) && CustomMap.STAGE_HEADER_ANALYZED.equals(file.getStage())) {
//                        file.setStatus(CustomMap.STATUS_AWAITING_USER_INPUT);
//                    } else if (CustomMap.STATUS_CANCELED.equalsIgnoreCase(file.getStatus()) 
//                       || CustomMap.STATUS_ERROR.equalsIgnoreCase(file.getStatus())
//                       || CustomMap.STATUS_COMPLETE.equalsIgnoreCase(file.getStatus())) {
//                        toRemove.add(file);
//                    }
//                }
//                try {
//                    SwingUtilities.invokeAndWait(new Runnable() {
//                        @Override
//                        public void run() {
//                            addToCompleted(toRemove);
//                            refreshUploadTable();
//                        }
//                    });
//                } catch (InvocationTargetException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                
//            }
//        };
//        Thread t = new Thread(r);
//        t.start();
//    }
//    public void resetUploadFileList() {
//        uploadInProgressFiles = CustomMapBackendInterface.getInProgressCustomMapList();
//        if (uploadInProgressFiles.size() > 0) {
//            kickOffMonitorThread();
//        }
//    }
    public static void updateStatusOnEDThread(final int row) {
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                CM_Manager.getInstance().updateProgressStatus(row);
            }
        });
    }
    public void updateProgressStatus() {
        for(int i=1; i<inProgressUploadFileTableModel.getRowCount(); i++) {
            updateProgressStatus(i);
        }
    }
    public void addToCompleted(ArrayList<CustomMap> files) {
        this.uploadInProgressFiles.removeAll(files);
        refreshUploadTable();
        this.uploadCompletedFiles.addAll(files);
        refreshCompletedTable();
        this.scrollToBottom(uploadCompletedTableSp);
        addNewlyCompletedFilesToExistingMaps(files);
    }
    private void addNewlyCompletedFilesToExistingMaps(ArrayList<CustomMap> files) {
        ArrayList<CustomMap> toAdd = new ArrayList<CustomMap>();
        for (CustomMap map : files) {
            if (CustomMap.STATUS_COMPLETE.equalsIgnoreCase(map.getStatus())) {
                CustomMap customMap = CustomMapBackendInterface.getExistingMap(map);//build the ExistingMap object from the database to be safe.
                toAdd.add(customMap);
                mapNames.add(customMap.getName());
            }
        }
        
        allExistingMapsFiles.addAll(toAdd);
        populateFilterLists();
        filterExistingMapsByRadioButton();
        this.scrollToBottom(existingMapsTableSp);
        
    }
    public void refreshUploadTable() {
        inProgressUploadFileTableModel.fireTableDataChanged();
    }
    public void updateProgressStatus(int row) { 
        inProgressUploadFileTableModel.fireTableRowsUpdated(row, row);
    }
    public void refreshCompletedTable() {
        completedUploadFileTableModel.fireTableDataChanged();
    }
    public void refreshExistingMapsTable() {
        customMapTableModel.fireTableDataChanged();
    }
    public void refreshExistingMapsRow(int row) {
        customMapTableModel.fireTableRowsUpdated(row, row);
    }
    public void refreshSharingTable() {
        sharingTableModel.fireTableDataChanged();
    }
    
    //TODO: tricky way of scrolling to the bottom. Now that we have an STable, I think there is an easier way
    private void scrollToBottom(JScrollPane scrollPane) {
        final JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
    }
    private void initializeFileChooser() {
        //set up the file chooser
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Select file(s) to upload as maps"); 
        fileChooser.setMultiSelectionEnabled(true);
        FileFilter filter = new FileNameExtensionFilter("Map File Types", "jpeg", "tiff", "tif", "geotiff", "png", 
                "jpg", "img", "gif", "cub", "jp2","pbm","pgm","ppm","vic","vicar","fit");
        fileChooser.setFileFilter(filter);
    }
    private void initializeDialogWidgets() {
        mdOptionsPanel = new JPanel();
        selectedFilesPanel = new JPanel();
        selectedFilesScrollPane = new JScrollPane();
        selectedFilesList = new JList<>();
        selectedFilesLbl = new JLabel();
        mdCardLayoutPanel = new JPanel();
        optionsCard = new JPanel();
        option3ScrollPane = new JScrollPane();
        option3TextPane = new JTextPane();
        option2ScrollPane = new JScrollPane();
        option2TextPane = new JTextPane();
        option1ScrollPane = new JScrollPane();
        option1TextPane = new JTextPane();
        uploadOptionsHeaderLbl = new JLabel();
        optionsCard1 = new JPanel();
        enterMapInfoLbl = new JLabel();
        enterMapInfoNoteSp = new JScrollPane();
        enterMapInfoNoteTextPane = new JTextPane();
        nameLbl = new JLabel();
        nameInput = new JTextField();
        ignoreValueLbl = new JLabel();
        ignoreValueInput = new JTextField();
        unitsLbl = new JLabel();
        unitsInput = new JTextField();
        extentLbl = new JLabel();
        globalRadio = new JRadioButton();
        regionalRadio = new JRadioButton();
        degreesLbl = new JLabel();
        eastRadio = new JRadioButton();
        westRadio = new JRadioButton();
        easternLonLbl = new JLabel();
        westernLonLbl = new JLabel();
        easternLonInput = new JTextField();
        westernLonInput = new JTextField();
        northernmostLatLbl = new JLabel();
        northernLatInput = new JTextField();
        southernmostLatLbl = new JLabel();
        southernLatInput = new JTextField();
        uploadOptionPanel = new JPanel();
        uploadAndProcessRadio = new JRadioButton();
        verifyInformationRadio = new JRadioButton();
        manauallyEnterRadio = new JRadioButton();
        
        card1UploadButton = new JButton(card1MainAction);
        card1ClearButton = new JButton(clearAction);
        card1CancelButton = new JButton(cardCancelAction);
        card1BackButton = new JButton(backAction);
        card1NextButton = new JButton(nextAction);
        card0CancelButton = new JButton(card0CancelAction);
        card0ContinueButton = new JButton(card0ContinueAction);
//        optionsUploadPromptButton = new JButton(card0PromptAction);
//        card2BackButton = new JButton(card2BackAction);
//        card2UploadButton = new JButton(card2UploadAction);
//        card2ClearButton = new JButton(card2ClearAction);
//        card2CancelButton = new JButton(card2CancelAction);
        card1ButtonPanel = new JPanel();
        
        optionsCard2 = new JPanel();
        enterMapInfoLbl1 = new JLabel();
        descriptionLbl = new JLabel();
        descriptionSp = new JScrollPane();
        descriptionTextAreaInput = new JTextArea();
        linksLbl = new JLabel();
        linksSp = new JScrollPane();
        linksTextAreaInput = new JTextArea();
        citationLbl = new JLabel();
        citationSp = new JScrollPane();
        citationTextAreaInput = new JTextArea();
        keywordsLbl = new JLabel();
        keywordsSp = new JScrollPane();
        keywordsTextAreaInput = new JTextArea();
        
        extentButtonGroup = new ButtonGroup();
        extentButtonGroup.add(globalRadio);
        extentButtonGroup.add(regionalRadio);
        globalRadio.addActionListener(extentListener);
        regionalRadio.addActionListener(extentListener);
        
        degreeButtonGroup = new ButtonGroup();
        degreeButtonGroup.add(eastRadio);
        degreeButtonGroup.add(westRadio);
        
        card0ButtonGroup = new ButtonGroup();
        card0ButtonGroup.add(manauallyEnterRadio);
        card0ButtonGroup.add(verifyInformationRadio);
        card0ButtonGroup.add(uploadAndProcessRadio);
        
        
//        eastRadio.addActionListener(degreeListener);
//        westRadio.addActionListener(degreeListener);
        
    }
    private void toggleMainButtons(boolean enabled) {
        viewEditUploadButton.setEnabled(enabled);
        cancelUploadButton.setEnabled(enabled);
    }
    private void toggleCompletedButtons(boolean enabled) {
        if (enabled && uploadCompletedTable.getSelectedRowCount() > 1) {
            viewCompletedButton.setEnabled(false);
            clearSelectedButton.setEnabled(enabled);
        } else {
            viewCompletedButton.setEnabled(enabled);
            clearSelectedButton.setEnabled(enabled);
        }
    }
    private void toggleExistingMapButtons() {
    	if (existingMapsTable.getSelectedRow() == -1) {
    		editExistingMapButton.setEnabled(false);
    		addLayerButton.setEnabled(false);
	        deleteExistingMapButton.setEnabled(false);
	        shareExistingMapButton.setEnabled(false);
    	} else {
    		boolean sharedSelected = areAnySelectedExistingMapsSharedWithMe();
    		if (sharedSelected) {
            	editExistingMapButton.setEnabled(false);
    			shareExistingMapButton.setEnabled(false);
    			deleteExistingMapButton.setEnabled(false);
    			addLayerButton.setEnabled(true);
            } else {
		    	if (existingMapsTable.getSelectedRowCount() > 1) {
                    //if we are turning buttons on, and more than one row is selected
                    editExistingMapButton.setEnabled(false);
                } else {
		        	//here only one is selected
		        	editExistingMapButton.setEnabled(true);
                }
	            addLayerButton.setEnabled(true);
		        deleteExistingMapButton.setEnabled(true);
		        shareExistingMapButton.setEnabled(true);
            }
        }
    }
   
    //Warning, if you change the layout code in any method below, you own it. Have a nice day!
    
    private void createManageUsersDialog(JDialog parent) {
        manageUsersDialog = new JDialog(parent, "Manage User List", false);
        manageUsersDialog.setSize(462, 250);
        manageUsersDialog.setResizable(false);
        manageUsersDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        manageUsersDialog.setLocationRelativeTo(parent);
        
        GroupLayout manageUsersDialogLayout = new GroupLayout(manageUsersDialog.getContentPane());
        manageUsersDialog.getContentPane().setLayout(manageUsersDialogLayout);
        manageUsersDialogLayout.setHorizontalGroup(
            manageUsersDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(manageUsersDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userListSp)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manageUsersAddPanel, GroupLayout.PREFERRED_SIZE, 271, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(manageUsersDialogLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(manageUsersButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        manageUsersDialogLayout.setVerticalGroup(
            manageUsersDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(manageUsersDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(manageUsersDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(userListSp, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(manageUsersAddPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(manageUsersButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
    private void layoutExistingMapsTab() {
        GroupLayout filterCheckBoxPanelLayout = new GroupLayout(filterCheckBoxPanel);
        filterCheckBoxPanel.setLayout(filterCheckBoxPanelLayout);
        filterCheckBoxPanelLayout.setHorizontalGroup(
            filterCheckBoxPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(filterCheckBoxPanelLayout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(mapNameCheckbox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(ownerCheckbox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(fileNameCheckbox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(keywordsCheckbox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(descriptionCheckbox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(87, 87, 87))
        );
        filterCheckBoxPanelLayout.setVerticalGroup(
            filterCheckBoxPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(filterCheckBoxPanelLayout.createSequentialGroup()
                .addGroup(filterCheckBoxPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionCheckbox)
                    .addComponent(fileNameCheckbox)
                    .addComponent(keywordsCheckbox)
                    .addComponent(ownerCheckbox)
                    .addComponent(mapNameCheckbox))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        GroupLayout filterPanelLayout = new GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, filterPanelLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(filterMapsLabel, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterTextbox, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearFilterButton)
                .addGap(74, 74, 74))
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(filterPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(filterTextbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterMapsLabel)
                    .addComponent(filterButton)
                    .addComponent(clearFilterButton))
                .addGap(69, 69, 69))
        );

        GroupLayout existingMapsButtonPanelLayout = new GroupLayout(existingMapsButtonPanel);
        existingMapsButtonPanel.setLayout(existingMapsButtonPanelLayout);
        existingMapsButtonPanelLayout.setHorizontalGroup(
            existingMapsButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(existingMapsButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addLayerButton)
                .addGap(18, 18, 18)
                .addComponent(editExistingMapButton)
                .addGap(18, 18, 18)
                .addComponent(shareExistingMapButton)
                .addGap(18, 18, 18)
                .addComponent(deleteExistingMapButton)
                .addGap(18, 18, 18)
                .addComponent(manageSharingButton)
                .addGap(18, 18, 18)
                .addComponent(refreshMapsButton)               
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        existingMapsButtonPanelLayout.setVerticalGroup(
            existingMapsButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(existingMapsButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(shareExistingMapButton)
                .addComponent(editExistingMapButton)
                .addComponent(deleteExistingMapButton)
                .addComponent(addLayerButton)
                .addComponent(manageSharingButton)
                .addComponent(refreshMapsButton))
        );
        
        GroupLayout mapFilterPanelLayout = new GroupLayout(mapFilterPanel);
        mapFilterPanel.setLayout(mapFilterPanelLayout);
        mapFilterPanelLayout.setHorizontalGroup(
            mapFilterPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mapFilterPanelLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(allMapsRadio)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myMapsRadio)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sharedWithMeRadio)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sharedWithOthersRadio)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mapFilterPanelLayout.setVerticalGroup(
            mapFilterPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mapFilterPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(allMapsRadio)
                .addComponent(myMapsRadio)
                .addComponent(sharedWithMeRadio)
                .addComponent(sharedWithOthersRadio))
        );

        GroupLayout existingMainPanelLayout = new GroupLayout(existingMainPanel);
        existingMainPanel.setLayout(existingMainPanelLayout);
        existingMainPanelLayout.setHorizontalGroup(
            existingMainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(existingMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(existingMainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(mapFilterPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(filterPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(existingMapsTableSp)
                    .addComponent(filterCheckBoxPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(existingMapsButtonPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        existingMainPanelLayout.setVerticalGroup(
            existingMainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(existingMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mapFilterPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(existingMapsTableSp, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(filterCheckBoxPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterPanel, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                .addGap(41, 41, 41)
                .addComponent(existingMapsButtonPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(63, 63, 63))
        );

        GroupLayout existingMapsTabPanelLayout = new GroupLayout(existingMapsTabPanel);
        existingMapsTabPanel.setLayout(existingMapsTabPanelLayout);
        existingMapsTabPanelLayout.setHorizontalGroup(
            existingMapsTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(existingMapsTabPanelLayout.createSequentialGroup()
                .addComponent(existingMainPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        existingMapsTabPanelLayout.setVerticalGroup(
            existingMapsTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(existingMainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }
    private void layoutSharingDialog() {
        GroupLayout usersSelectDeselectButtonPanelLayout = new GroupLayout(usersSelectDeselectButtonPanel);
        usersSelectDeselectButtonPanel.setLayout(usersSelectDeselectButtonPanelLayout);
        usersSelectDeselectButtonPanelLayout.setHorizontalGroup(
            usersSelectDeselectButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(shareWithUserButton)
            .addComponent(unshareWithUserButton)
        );
        usersSelectDeselectButtonPanelLayout.setVerticalGroup(
            usersSelectDeselectButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(usersSelectDeselectButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(shareWithUserButton)
                .addGap(18, 18, 18)
                .addComponent(unshareWithUserButton))
        );
        GroupLayout groupsSelectDeselectButtonPanelLayout = new GroupLayout(groupsSelectDeselectButtonPanel);
        groupsSelectDeselectButtonPanel.setLayout(groupsSelectDeselectButtonPanelLayout);
        groupsSelectDeselectButtonPanelLayout.setHorizontalGroup(
            groupsSelectDeselectButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(addGroupShareButton)
            .addComponent(removeGroupShareButton)
        );
        groupsSelectDeselectButtonPanelLayout.setVerticalGroup(
            groupsSelectDeselectButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupsSelectDeselectButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addGroupShareButton)
                .addGap(18, 18, 18)
                .addComponent(removeGroupShareButton))
        );
        GroupLayout shareWithUserSpLayout = new GroupLayout(shareWithUserSp);
        shareWithUserSp.setLayout(shareWithUserSpLayout);
        shareWithUserSpLayout.setHorizontalGroup(
            shareWithUserSpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(shareWithUserSpLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(shareWithUserSpLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(shareUsernameLbl)
                    .addComponent(shareWithUserButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(shareWithUserInput))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        shareWithUserSpLayout.setVerticalGroup(
            shareWithUserSpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(shareWithUserSpLayout.createSequentialGroup()
                .addComponent(shareUsernameLbl)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(shareWithUserInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(shareWithUserButton)
                .addContainerGap())
        );

        GroupLayout sharedUsersPanelLayout = new GroupLayout(sharedUsersPanel);
        sharedUsersPanel.setLayout(sharedUsersPanelLayout);
        sharedUsersPanelLayout.setHorizontalGroup(
            sharedUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(sharedUsersPanelLayout.createSequentialGroup()
                .addGroup(sharedUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(sharedUsersPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(sharedUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(availableUsersSp, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
                            .addComponent(availableUsersLbl))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(sharedUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(usersSelectDeselectButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGroup(GroupLayout.Alignment.TRAILING, sharedUsersPanelLayout.createSequentialGroup()
                                .addComponent(sharingUsersLbl)
                                .addGap(11, 11, 11)))
                        .addGap(18, 18, 18)
                        .addGroup(sharedUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(sharedWithUsersLbl)
                            .addComponent(sharedWithUsersSp, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)))
                    .addGroup(sharedUsersPanelLayout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(manageUserListButton)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sharedUsersPanelLayout.setVerticalGroup(
            sharedUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(sharedUsersPanelLayout.createSequentialGroup()
                .addGroup(sharedUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(sharedUsersPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(sharedUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(availableUsersLbl)
                            .addComponent(sharedWithUsersLbl)))
                    .addComponent(sharingUsersLbl))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sharedUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(sharedUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(sharedWithUsersSp)
                        .addComponent(availableUsersSp))
                    .addGroup(sharedUsersPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(usersSelectDeselectButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(manageUserListButton)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        GroupLayout shareWithGroupsPanelLayout = new GroupLayout(shareWithGroupsPanel);
        shareWithGroupsPanel.setLayout(shareWithGroupsPanelLayout);
        shareWithGroupsPanelLayout.setHorizontalGroup(
            shareWithGroupsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(shareWithGroupsPanelLayout.createSequentialGroup()
                .addGroup(shareWithGroupsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(shareWithGroupsPanelLayout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addComponent(manageGroupsButton))
                    .addGroup(shareWithGroupsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(shareWithGroupsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(availableGroupsSp, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
                            .addComponent(availableGroupsLbl))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(shareWithGroupsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(shareWithGroupsPanelLayout.createSequentialGroup()
                                .addComponent(sharingGroupsLbl)
                                .addGap(27, 27, 27)
                                .addComponent(sharedWithGroupsLbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(shareWithGroupsPanelLayout.createSequentialGroup()
                                .addComponent(groupsSelectDeselectButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(sharedWithGroupsSp, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        shareWithGroupsPanelLayout.setVerticalGroup(
            shareWithGroupsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(shareWithGroupsPanelLayout.createSequentialGroup()
                .addGroup(shareWithGroupsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(shareWithGroupsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(shareWithGroupsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(availableGroupsLbl)
                            .addComponent(sharedWithGroupsLbl)))
                    .addComponent(sharingGroupsLbl))
                .addGroup(shareWithGroupsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(shareWithGroupsPanelLayout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(shareWithGroupsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(availableGroupsSp)
                            .addComponent(sharedWithGroupsSp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                    .addGroup(shareWithGroupsPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(groupsSelectDeselectButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(manageGroupsButton)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        GroupLayout sharingManagmentButtonPanelLayout = new GroupLayout(sharingManagmentButtonPanel);
        sharingManagmentButtonPanel.setLayout(sharingManagmentButtonPanelLayout);
        sharingManagmentButtonPanelLayout.setHorizontalGroup(
            sharingManagmentButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(sharingManagmentButtonPanelLayout.createSequentialGroup()
                .addComponent(sharingSaveAndCloseButton)
                .addGap(18, 18, 18)
                .addComponent(saveSharingChangesButton)
                .addGap(18, 18, 18)
                .addComponent(cancelSharingButton))
        );
        sharingManagmentButtonPanelLayout.setVerticalGroup(
            sharingManagmentButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(sharingManagmentButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(saveSharingChangesButton)
                .addComponent(cancelSharingButton)
                .addComponent(sharingSaveAndCloseButton))
        );

        GroupLayout sharingManagementDialogLayout = new GroupLayout(sharingManagementDialog.getContentPane());
        sharingManagementDialog.getContentPane().setLayout(sharingManagementDialogLayout);
        sharingManagementDialogLayout.setHorizontalGroup(
            sharingManagementDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(sharingManagementDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sharingManagementDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(sharingManagementDialogLayout.createSequentialGroup()
                        .addComponent(sharedMapsLbl)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(sharingManagementDialogLayout.createSequentialGroup()
                        .addComponent(sharedMapsSp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(sharingManagementDialogLayout.createSequentialGroup()
                        .addComponent(sharedUsersPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(shareWithGroupsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(sharingManagementDialogLayout.createSequentialGroup()
                .addGap(228, 228, 228)
                .addComponent(sharingManagmentButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        sharingManagementDialogLayout.setVerticalGroup(
            sharingManagementDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(sharingManagementDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sharedMapsLbl)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sharedMapsSp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sharingManagementDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(shareWithGroupsPanel, GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(sharedUsersPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(sharingManagmentButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
        GroupLayout manageUsersAddPanelLayout = new GroupLayout(manageUsersAddPanel);
        manageUsersAddPanel.setLayout(manageUsersAddPanelLayout);
        manageUsersAddPanelLayout.setHorizontalGroup(
            manageUsersAddPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(manageUsersAddPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(manageUsersAddPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(manageUsersLbl)
                    .addComponent(searchUserInput, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
                    .addComponent(manageUsersAddButton)
                    .addComponent(manageUsersMsgLbl))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        manageUsersAddPanelLayout.setVerticalGroup(
            manageUsersAddPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(manageUsersAddPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(manageUsersLbl)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchUserInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(manageUsersAddButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(manageUsersMsgLbl)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        GroupLayout manageUsersButtonPanelLayout = new GroupLayout(manageUsersButtonPanel);
        manageUsersButtonPanel.setLayout(manageUsersButtonPanelLayout);
        manageUsersButtonPanelLayout.setHorizontalGroup(
            manageUsersButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(manageUsersButtonPanelLayout.createSequentialGroup()
                .addComponent(manageUsersRemoveButton)
                .addGap(18, 18, 18)
                .addComponent(manageUsersSaveButton)
                .addGap(18, 18, 18)
                .addComponent(manageUsersDoneButton)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        manageUsersButtonPanelLayout.setVerticalGroup(
            manageUsersButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(manageUsersButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(manageUsersButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(manageUsersRemoveButton)
                    .addComponent(manageUsersSaveButton)
                    .addComponent(manageUsersDoneButton))
                .addContainerGap())
        );
        
        GroupLayout groupAvailableUsersPanelLayout = new GroupLayout(groupAvailableUsersPanel);
        groupAvailableUsersPanel.setLayout(groupAvailableUsersPanelLayout);
        groupAvailableUsersPanelLayout.setHorizontalGroup(
            groupAvailableUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupAvailableUsersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupAvailableUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(groupUserListSp, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                    .addComponent(groupUsersLbl))
                .addContainerGap())
        );
        groupAvailableUsersPanelLayout.setVerticalGroup(
            groupAvailableUsersPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupAvailableUsersPanelLayout.createSequentialGroup()
                .addComponent(groupUsersLbl)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupUserListSp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        GroupLayout groupUserListPanelLayout = new GroupLayout(groupUserListPanel);
        groupUserListPanel.setLayout(groupUserListPanelLayout);
        groupUserListPanelLayout.setHorizontalGroup(
            groupUserListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupUserListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupUserListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(groupAvailableUserListSp, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                    .addComponent(groupAvailableUsersLbl))
                .addContainerGap())
        );
        groupUserListPanelLayout.setVerticalGroup(
            groupUserListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupUserListPanelLayout.createSequentialGroup()
                .addComponent(groupAvailableUsersLbl)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupAvailableUserListSp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        GroupLayout groupAddRemoveUserButtonPanelLayout = new GroupLayout(groupAddRemoveUserButtonPanel);
        groupAddRemoveUserButtonPanel.setLayout(groupAddRemoveUserButtonPanelLayout);
        groupAddRemoveUserButtonPanelLayout.setHorizontalGroup(
            groupAddRemoveUserButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupAddRemoveUserButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupAddRemoveUserButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(groupAddUserButton)
                    .addComponent(groupRemoveUserButton))
                .addContainerGap())
        );
        groupAddRemoveUserButtonPanelLayout.setVerticalGroup(
            groupAddRemoveUserButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupAddRemoveUserButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(groupAddUserButton)
                .addGap(18, 18, 18)
                .addComponent(groupRemoveUserButton)
                .addContainerGap())
        );
        GroupLayout groupListPanelLayout = new GroupLayout(groupListPanel);
        groupListPanel.setLayout(groupListPanelLayout);
        groupListPanelLayout.setHorizontalGroup(
            groupListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupListPanelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(groupListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(groupTopButtonPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(groupComboBoxPanel, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 291, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        groupListPanelLayout.setVerticalGroup(
            groupListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupListPanelLayout.createSequentialGroup()
                .addComponent(groupComboBoxPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupTopButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        GroupLayout groupButtonPanelLayout = new GroupLayout(groupButtonPanel);
        groupButtonPanel.setLayout(groupButtonPanelLayout);
        groupButtonPanelLayout.setHorizontalGroup(
            groupButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupButtonPanelLayout.createSequentialGroup()
                .addComponent(groupSaveAndCloseButton, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(groupSaveButton)
                .addGap(18, 18, 18)
                .addComponent(groupDoneButton))
        );
        groupButtonPanelLayout.setVerticalGroup(
            groupButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupButtonPanelLayout.createSequentialGroup()
                .addGroup(groupButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(groupSaveAndCloseButton)
                    .addComponent(groupSaveButton)
                    .addComponent(groupDoneButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        
        GroupLayout groupTopButtonPanelLayout = new GroupLayout(groupTopButtonPanel);
        groupTopButtonPanel.setLayout(groupTopButtonPanelLayout);
        groupTopButtonPanelLayout.setHorizontalGroup(
            groupTopButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupTopButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(groupCreateGroupButton)
                .addGap(18, 18, 18)
                .addComponent(groupDeleteButton)
                .addGap(18, 18, 18)
                .addComponent(groupRenameButton)
                .addContainerGap())
        );
        groupTopButtonPanelLayout.setVerticalGroup(
            groupTopButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupTopButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupTopButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(groupCreateGroupButton)
                    .addComponent(groupDeleteButton)
                    .addComponent(groupRenameButton))
                .addContainerGap())
        );
        GroupLayout groupComboBoxPanelLayout = new GroupLayout(groupComboBoxPanel);
        groupComboBoxPanel.setLayout(groupComboBoxPanelLayout);
        groupComboBoxPanelLayout.setHorizontalGroup(
            groupComboBoxPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupComboBoxPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(groupMgmtLbl, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(groupComboBox, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        groupComboBoxPanelLayout.setVerticalGroup(
            groupComboBoxPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupComboBoxPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupComboBoxPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(groupComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(groupMgmtLbl))
                .addContainerGap())
        );
        
        GroupLayout manageGroupsDialogLayout = new GroupLayout(manageGroupsDialog.getContentPane());
        manageGroupsDialog.getContentPane().setLayout(manageGroupsDialogLayout);
        manageGroupsDialogLayout.setHorizontalGroup(
            manageGroupsDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(manageGroupsDialogLayout.createSequentialGroup()
                .addGroup(manageGroupsDialogLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                    .addGroup(GroupLayout.Alignment.LEADING, manageGroupsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(groupListPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(GroupLayout.Alignment.LEADING, manageGroupsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(groupMainListPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 12, Short.MAX_VALUE))
            .addGroup(manageGroupsDialogLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(groupButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        manageGroupsDialogLayout.setVerticalGroup(
            manageGroupsDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(manageGroupsDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(groupListPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupMainListPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(30, 30, 30)
                .addComponent(groupButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        GroupLayout groupMainListPanelLayout = new GroupLayout(groupMainListPanel);
        groupMainListPanel.setLayout(groupMainListPanelLayout);
        groupMainListPanelLayout.setHorizontalGroup(
            groupMainListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupMainListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupMainListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(GroupLayout.Alignment.TRAILING, groupMainListPanelLayout.createSequentialGroup()
                        .addComponent(groupUserListPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(groupAddRemoveUserButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(groupAvailableUsersPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(groupMainListPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(groupManageUserListButton)))
                .addContainerGap())
        );
        groupMainListPanelLayout.setVerticalGroup(
            groupMainListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(groupMainListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(groupMainListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(groupMainListPanelLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(groupAddRemoveUserButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addComponent(groupUserListPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(groupAvailableUsersPanel, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupManageUserListButton)
                .addContainerGap())
        );
    }
    private void layoutSelectedFilesPanel() {
        GroupLayout selectedFilesPanelLayout = new GroupLayout(selectedFilesPanel);
        selectedFilesPanel.setLayout(selectedFilesPanelLayout);
        selectedFilesPanelLayout.setHorizontalGroup(
            selectedFilesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(selectedFilesPanelLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(selectedFilesLbl)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, selectedFilesPanelLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(selectedFilesScrollPane, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        selectedFilesPanelLayout.setVerticalGroup(
            selectedFilesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, selectedFilesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectedFilesLbl)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(selectedFilesScrollPane, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );
    }
    private void layoutOptionsCard0() {
        GroupLayout uploadOptionPanelLayout = new GroupLayout(uploadOptionPanel);
        uploadOptionPanel.setLayout(uploadOptionPanelLayout);
        uploadOptionPanelLayout.setHorizontalGroup(
            uploadOptionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(uploadOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(uploadOptionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(manauallyEnterRadio)
                    .addGroup(uploadOptionPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(uploadOptionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(uploadAndProcessRadio)
                            .addGroup(uploadOptionPanelLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(option1ScrollPane, GroupLayout.PREFERRED_SIZE, 553, GroupLayout.PREFERRED_SIZE))
                            .addComponent(option3ScrollPane, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 553, GroupLayout.PREFERRED_SIZE))
                        .addComponent(verifyInformationRadio, GroupLayout.Alignment.LEADING)
                        .addComponent(option2ScrollPane, GroupLayout.PREFERRED_SIZE, 553, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        uploadOptionPanelLayout.setVerticalGroup(
            uploadOptionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(uploadOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(manauallyEnterRadio)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(option2ScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(uploadAndProcessRadio)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(option1ScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(verifyInformationRadio)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(option3ScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        GroupLayout optionsCardLayout = new GroupLayout(optionsCard);
        optionsCard.setLayout(optionsCardLayout);
        optionsCardLayout.setHorizontalGroup(
            optionsCardLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(optionsCardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(uploadOptionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addGroup(optionsCardLayout.createSequentialGroup()
                .addGap(206, 206, 206)
                .addComponent(uploadOptionsHeaderLbl, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE))
            .addGroup(optionsCardLayout.createSequentialGroup()
                .addGap(187, 187, 187)
                .addComponent(card0ContinueButton)
                .addGap(18, 18, 18)
                .addComponent(card0CancelButton))
        );
        optionsCardLayout.setVerticalGroup(
            optionsCardLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, optionsCardLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(uploadOptionsHeaderLbl)
                .addGap(18, 18, 18)
                .addComponent(uploadOptionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(optionsCardLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(card0ContinueButton)
                    .addComponent(card0CancelButton))
                .addGap(24, 24, 24))
        );

    }
    private void layoutOptionsCard1() {
        GroupLayout card1ButtonPanelLayout = new GroupLayout(card1ButtonPanel);
        card1ButtonPanel.setLayout(card1ButtonPanelLayout);
        card1ButtonPanelLayout.setHorizontalGroup(
            card1ButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(card1ButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(card1BackButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(card1NextButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(card1UploadButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(card1ClearButton)
                .addGap(12, 12, 12)
                .addComponent(card1CancelButton)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card1ButtonPanelLayout.setVerticalGroup(
            card1ButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(card1ButtonPanelLayout.createSequentialGroup()
                .addGroup(card1ButtonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(card1BackButton)
                    .addComponent(card1NextButton)
                    .addComponent(card1UploadButton)
                    .addComponent(card1ClearButton)
                    .addComponent(card1CancelButton))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        GroupLayout optionsCard1Layout = new GroupLayout(optionsCard1);
        optionsCard1.setLayout(optionsCard1Layout);
        optionsCard1Layout.setHorizontalGroup(
            optionsCard1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(optionsCard1Layout.createSequentialGroup()
                .addGroup(optionsCard1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(optionsCard1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(optionsCard1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(optionsCard1Layout.createSequentialGroup()
                                .addGroup(optionsCard1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(optionsCard1Layout.createSequentialGroup()
                                        .addComponent(extentLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(globalRadio)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(regionalRadio))
                                    .addGroup(optionsCard1Layout.createSequentialGroup()
                                        .addComponent(northernmostLatLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(northernLatInput, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(optionsCard1Layout.createSequentialGroup()
                                        .addComponent(easternLonLbl)
                                        .addGap(18, 18, 18)
                                        .addComponent(easternLonInput, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)))
                                .addGap(42, 42, 42)
                                .addGroup(optionsCard1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addGroup(optionsCard1Layout.createSequentialGroup()
                                        .addComponent(degreesLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(eastRadio)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(westRadio))
                                    .addGroup(optionsCard1Layout.createSequentialGroup()
                                        .addComponent(westernLonLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(westernLonInput, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
                                    .addGroup(optionsCard1Layout.createSequentialGroup()
                                        .addComponent(southernmostLatLbl)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(southernLatInput, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))))
                            .addGroup(optionsCard1Layout.createSequentialGroup()
                                .addComponent(nameLbl)
                                .addGap(4, 4, 4)
                                .addComponent(nameInput, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(ignoreValueLbl)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ignoreValueInput, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(unitsLbl)
                                .addGap(18, 18, 18)
                                .addComponent(unitsInput, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE))))
                    .addGroup(optionsCard1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(enterMapInfoNoteSp, GroupLayout.PREFERRED_SIZE, 518, GroupLayout.PREFERRED_SIZE))
                    .addGroup(optionsCard1Layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(enterMapInfoLbl)))
                .addGap(31, 86, Short.MAX_VALUE))
        );
        optionsCard1Layout.setVerticalGroup(
            optionsCard1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(optionsCard1Layout.createSequentialGroup()
                .addComponent(enterMapInfoLbl, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(enterMapInfoNoteSp, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addGroup(optionsCard1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLbl)
                    .addComponent(nameInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(ignoreValueLbl)
                    .addComponent(ignoreValueInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitsLbl)
                    .addComponent(unitsInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(optionsCard1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(extentLbl)
                    .addComponent(globalRadio)
                    .addComponent(regionalRadio)
                    .addComponent(degreesLbl)
                    .addComponent(eastRadio)
                    .addComponent(westRadio))
                .addGap(18, 18, 18)
                .addGroup(optionsCard1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(easternLonInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(westernLonLbl)
                    .addComponent(westernLonInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(easternLonLbl))
                .addGap(18, 18, 18)
                .addGroup(optionsCard1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(northernmostLatLbl)
                    .addComponent(southernmostLatLbl)
                    .addComponent(southernLatInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(northernLatInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        
        
    }
    private void layoutOptionsCard2() {
        GroupLayout optionsCard2Layout = new GroupLayout(optionsCard2);
        optionsCard2.setLayout(optionsCard2Layout);
        optionsCard2Layout.setHorizontalGroup(
            optionsCard2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(optionsCard2Layout.createSequentialGroup()
                .addGroup(optionsCard2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionLbl)
                    .addComponent(linksLbl)
                    .addComponent(citationLbl)
                    .addComponent(keywordsLbl))
                .addGap(18, 18, 18)
                .addGroup(optionsCard2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(enterMapInfoLbl1)
                    .addComponent(linksSp, GroupLayout.PREFERRED_SIZE, 352, GroupLayout.PREFERRED_SIZE)
                    .addComponent(descriptionSp, GroupLayout.PREFERRED_SIZE, 352, GroupLayout.PREFERRED_SIZE)
                    .addComponent(citationSp, GroupLayout.PREFERRED_SIZE, 352, GroupLayout.PREFERRED_SIZE)
                    .addComponent(keywordsSp, GroupLayout.PREFERRED_SIZE, 352, GroupLayout.PREFERRED_SIZE))
                .addGap(0, 176, Short.MAX_VALUE))
        );
        optionsCard2Layout.setVerticalGroup(
            optionsCard2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(optionsCard2Layout.createSequentialGroup()
                .addComponent(enterMapInfoLbl1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(optionsCard2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionLbl)
                    .addComponent(descriptionSp, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(optionsCard2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(linksSp, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                    .addComponent(linksLbl))
                .addGap(18, 18, 18)
                .addGroup(optionsCard2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(citationSp, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                    .addComponent(citationLbl))
                .addGap(18, 18, 18)
                .addGroup(optionsCard2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(keywordsSp, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                    .addComponent(keywordsLbl))
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }
    private void layoutMainDialogPanel() {
        GroupLayout mdOptionsPanelLayout = new GroupLayout(mdOptionsPanel);
        mdOptionsPanel.setLayout(mdOptionsPanelLayout);
        mdOptionsPanelLayout.setHorizontalGroup(
            mdOptionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mdOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectedFilesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mdCardLayoutPanel, GroupLayout.PREFERRED_SIZE, 622, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, mdOptionsPanelLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(card1ButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(224, 224, 224))
        );
        mdOptionsPanelLayout.setVerticalGroup(
            mdOptionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mdOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mdOptionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(selectedFilesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(mdCardLayoutPanel, GroupLayout.PREFERRED_SIZE, 313, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(card1ButtonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout mainDialogLayout = new GroupLayout(mainDialog.getContentPane());
        mainDialog.getContentPane().setLayout(mainDialogLayout);
        mainDialogLayout.setHorizontalGroup(
            mainDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mdOptionsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        mainDialogLayout.setVerticalGroup(
            mainDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mainDialogLayout.createSequentialGroup()
                .addComponent(mdOptionsPanel, GroupLayout.PREFERRED_SIZE, 419, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );
    }
    private void layoutUploadPanel() {
        GroupLayout uploadInProgressTablePanelLayout = new GroupLayout(uploadInProgressTablePanel);
        uploadInProgressTablePanel.setLayout(uploadInProgressTablePanelLayout);
        uploadInProgressTablePanelLayout.setHorizontalGroup(
            uploadInProgressTablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, uploadInProgressTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(uploadInProgressTablePanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(uploadTableSp, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                    .addGroup(GroupLayout.Alignment.LEADING, uploadInProgressTablePanelLayout.createSequentialGroup()
                        .addGroup(uploadInProgressTablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(inProgressUploadsLbl, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
                            .addGroup(uploadInProgressTablePanelLayout.createSequentialGroup()
                                .addComponent(viewEditUploadButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cancelUploadButton, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(refreshUploadStatusButton)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        uploadInProgressTablePanelLayout.setVerticalGroup(
            uploadInProgressTablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, uploadInProgressTablePanelLayout.createSequentialGroup()
                .addComponent(inProgressUploadsLbl)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uploadTableSp, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(uploadInProgressTablePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(viewEditUploadButton)
                    .addComponent(cancelUploadButton)
                    .addComponent(refreshUploadStatusButton))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        GroupLayout uploadStartPanelLayout = new GroupLayout(uploadStartPanel);
        uploadStartPanel.setLayout(uploadStartPanelLayout);
        uploadStartPanelLayout.setHorizontalGroup(
            uploadStartPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(uploadStartPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectFilesButton)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        uploadStartPanelLayout.setVerticalGroup(
            uploadStartPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(uploadStartPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectFilesButton)
                .addContainerGap())
        );
        
        GroupLayout uploadCompletedTablePanelLayout = new GroupLayout(uploadCompletedTablePanel);
        uploadCompletedTablePanel.setLayout(uploadCompletedTablePanelLayout);
        uploadCompletedTablePanelLayout.setHorizontalGroup(
            uploadCompletedTablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(uploadCompletedTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(uploadCompletedTablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(uploadCompletedTableSp, GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                    .addGroup(uploadCompletedTablePanelLayout.createSequentialGroup()
                        .addGroup(uploadCompletedTablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(uploadCompletedTablePanelLayout.createSequentialGroup()
                                .addComponent(viewCompletedButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(clearSelectedButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(clearAllCompletedButton))
                            .addComponent(completedUploadsLbl))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        uploadCompletedTablePanelLayout.setVerticalGroup(
            uploadCompletedTablePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, uploadCompletedTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(completedUploadsLbl)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uploadCompletedTableSp, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(uploadCompletedTablePanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(clearAllCompletedButton)
                    .addComponent(clearSelectedButton)
                    .addComponent(viewCompletedButton))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        GroupLayout uploadTabPanelLayout = new GroupLayout(uploadTabPanel);
        uploadTabPanel.setLayout(uploadTabPanelLayout);
        uploadTabPanelLayout.setHorizontalGroup(
            uploadTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(uploadTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(uploadTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(uploadInProgressTablePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(uploadCompletedTablePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(uploadSeparator)
                    .addGroup(uploadTabPanelLayout.createSequentialGroup()
                        .addComponent(uploadStartPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        uploadTabPanelLayout.setVerticalGroup(
            uploadTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(uploadTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(uploadStartPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uploadInProgressTablePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uploadSeparator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uploadCompletedTablePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );
    }
    public void addPreviousUser(String text) {
        // TODO Auto-generated method stub
        
    }
    public String[] getPreviouslyUsedUsers() {
        // TODO Auto-generated method stub
        return null;
    }
    public ArrayList<SharingGroup> getGroups() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void addGroup(SharingGroup newGroup) {
        // TODO Auto-generated method stub
        
    }
    public void refreshGroupsTable() {
        // TODO Auto-generated method stub
        
    }
}
