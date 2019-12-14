package edu.asu.jmars.layer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import edu.asu.jmars.Main;
import edu.asu.jmars.layer.crater.CraterFactory;
import edu.asu.jmars.layer.grid.GridFactory;
import edu.asu.jmars.layer.groundtrack.GroundTrackFactory;
import edu.asu.jmars.layer.krc.KRCFactory;
import edu.asu.jmars.layer.landing.LandingFactory;
import edu.asu.jmars.layer.map2.FileUploadDialog;
import edu.asu.jmars.layer.map2.MapLViewFactory;
import edu.asu.jmars.layer.map2.MapServer;
import edu.asu.jmars.layer.map2.MapServerFactory;
import edu.asu.jmars.layer.map2.MapSource;
import edu.asu.jmars.layer.map2.custom.CM_Manager;
import edu.asu.jmars.layer.mcd.MCDFactory;
import edu.asu.jmars.layer.mosaics.MosaicsLViewFactory;
import edu.asu.jmars.layer.nomenclature.NomenclatureFactory;
import edu.asu.jmars.layer.north.NorthFactory;
import edu.asu.jmars.layer.scale.ScaleFactory;
import edu.asu.jmars.layer.shape2.ShapeFactory;
import edu.asu.jmars.layer.shape2.ShapeLayer;
import edu.asu.jmars.layer.shape2.ShapeLView;
import edu.asu.jmars.layer.slider.SliderFactory;
import edu.asu.jmars.layer.stamp.StampFactory;
import edu.asu.jmars.layer.streets.StreetLViewFactory;
import edu.asu.jmars.layer.threed.ThreeDFactory;
import edu.asu.jmars.swing.ImportantMessagePanel;
import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.*;
import edu.asu.jmars.ui.image.factory.ImageFactory;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.component.ThemeImages;
import edu.asu.jmars.util.Config;
import edu.asu.jmars.util.JmarsHttpRequest;
import edu.asu.jmars.util.Util;
import edu.asu.jmars.util.HttpRequestType;

public class AddLayer extends JFrame {
	
	private JPanel topPanel;
	private static JPanel centerPanel;
	private JPanel westPanel;
	private JPanel eastPanel;
	private JPanel bottomPanel;
	public static Container cp;
	private JLabel categoryLbl;
	protected static JCheckBox advBox;
	private static JComboBox categories;
	private static JComboBox subcategories;
	
	private static JTextField searchBox;
	private static JCheckBox nameBx;
	private static JCheckBox categoryBx;
	private static JCheckBox citationBx;
	private static JCheckBox descripBx;
	private static JCheckBox topicBx;
	private static JCheckBox advancedSearchBx;
	private static JCheckBox customBx;
	private static JXTaskPane resultsPane;
	private static ArrayList<LayerParameters> searchResults = new ArrayList<LayerParameters>();
	private final static String searchPrompt = "Enter words to seach for...";
	
	public JButton close;
	public JButton dockMe;
	public static boolean isDocked=false;
		
	private Font catFont = ThemeFont.getBold().deriveFont(15f);
	
	private static Dimension gap = new Dimension(0,8);
	private Dimension gap2 = new Dimension(0,3);
	private Dimension gap3 = new Dimension(5,0);
	
	final public static int addLayerHeight = 550;
	public final static int addLayerWidth = 390;
	
	private static AddLayer addLayer = null;
	private final Color backgroundColor = UIManager.getColor("TabbedPane.selected");
	private final static Color panelGrey = UIManager.getColor("Panel.background");
	
    private static Color imgLayerColor = ((ThemeImages) GUITheme.get("images")).getLayerfill();
    
		
	private static ImageIcon mapsIcon = new ImageIcon(ImageFactory.createImage(MAP_LAYER_IMG
            							    .withDisplayColor(imgLayerColor))			
			                                .getScaledInstance(25, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon stampsIcon = new ImageIcon(ImageFactory.createImage(STAMPS
			                                  .withDisplayColor(imgLayerColor))
											  .getScaledInstance(20, 15, Image.SCALE_SMOOTH));
	
	private static ImageIcon threeDIcon = new ImageIcon(ImageFactory.createImage(THREE_D_LAYER_IMG
				 						      .withDisplayColor(imgLayerColor))			
			                                  .getScaledInstance(18, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon craterIcon = new ImageIcon(ImageFactory.createImage(CRATER_COUNT_IMG
            								  .withStrokeColor(imgLayerColor))
			                                  .getScaledInstance(20, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon gridIcon = new ImageIcon(ImageFactory.createImage(GRID_LAYER_IMG
				 						    .withDisplayColor(imgLayerColor))
			                                .getScaledInstance(22, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon groundtrackIcon = new ImageIcon(ImageFactory.createImage(GROUNDTRACK_LAYER_IMG
											  .withDisplayColor(imgLayerColor))			         
			                                  .getScaledInstance(28, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon krcIcon = new ImageIcon(ImageFactory.createImage(KRC_LAYER_IMG
			  						       .withDisplayColor(imgLayerColor))			
			                               .getScaledInstance(23, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon landingIcon = new ImageIcon(ImageFactory.createImage(LANDING_SITE_IMG
            							   .withDisplayColor(imgLayerColor))
			                               .getScaledInstance(27, 15, Image.SCALE_SMOOTH));
	
	private static ImageIcon mcdIcon = new ImageIcon(ImageFactory.createImage(MCD_LAYER_IMG
			   						   .withDisplayColor(imgLayerColor))			
			                           .getScaledInstance(23, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon mosaicsIcon = new ImageIcon(ImageFactory.createImage(MOSAICS_LAYER_IMG
			   							   .withDisplayColor(imgLayerColor))			
			                               .getScaledInstance(22, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon nomenclatureIcon = new ImageIcon(ImageFactory.createImage(NOMENCLATURE_LAYER_IMG
			   									.withDisplayColor(imgLayerColor))			
			                                    .getScaledInstance(20, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon northIcon = new ImageIcon(ImageFactory.createImage(NORTH_LAYER_IMG
										.withDisplayColor(imgLayerColor))			
			                            .getScaledInstance(18, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon roiIcon = new ImageIcon(ImageFactory.createImage(ROI_LAYER_IMG
									   .withDisplayColor(imgLayerColor))			
			                           .getScaledInstance(18, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon scaleIcon = new ImageIcon(ImageFactory.createImage(SCALE_BAR_IMG
			  							 .withDisplayColor(imgLayerColor))
			                       		 .getScaledInstance(22, 8, Image.SCALE_SMOOTH));
	
	private static ImageIcon shapeIcon = new ImageIcon(ImageFactory.createImage(SHAPE_LAYER_IMG
										.withDisplayColor(imgLayerColor))			
			                            .getScaledInstance(19, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon streetsIcon = new ImageIcon(ImageFactory.createImage(STREETS_LAYER_IMG
										  .withDisplayColor(imgLayerColor))			
			                              .getScaledInstance(31, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon tesIcon = new ImageIcon(ImageFactory.createImage(TES_LAYER_IMG
									  .withDisplayColor(imgLayerColor))			
			                          .getScaledInstance(27, 20, Image.SCALE_SMOOTH));
	
	private static ImageIcon sliderIcon = new ImageIcon(ImageFactory.createImage(SLIDER_LAYER_IMG
										 .withDisplayColor(imgLayerColor))			
			                             .getScaledInstance(18, 20, Image.SCALE_SMOOTH));
	
	private static String selectedCategory=null;
	private static String selectedSubcategory=null;
	private static boolean activateComboBoxListener=true;
	
	
	private static String initCat = null;
	private static String subcatPrompt = "Subcategories";
	
	private AddLayer() {
		
		setTitle("Add a New Layer");
		setIconImage(Util.getJMarsIcon());
		cp = getContentPane();
		cp.setBackground(backgroundColor);
		setLayout(new BorderLayout());
		
	// Shown at the top...contains label, comboboxes, and adv checkbox
		JPanel topPanel = new JPanel(new GridBagLayout());
		topPanel.setBorder(new EmptyBorder(5, 8, 0, 10));

		categoryLbl = new JLabel("Select Category:");
		categoryLbl.setFont(catFont);
		categoryLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		categories = new JComboBox();
		categories.setMaximumRowCount(12);
		categories.setAlignmentX(Component.CENTER_ALIGNMENT);
		categories.addActionListener(comboBoxListener);
		
		subcategories = new JComboBox();
		subcategories.setMaximumRowCount(12);
		subcategories.setAlignmentX(Component.CENTER_ALIGNMENT);
		subcategories.addActionListener(comboBoxListener);
		
		advBox = new JCheckBox(extendChoices);
		advBox.addActionListener(advancedBoxListener);
		advBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		advBox.setBackground(backgroundColor);
		if (Config.get("advancedModeSelected", false))
			advBox.setSelected(true);
		
		int pad = 2;
		Insets in = new Insets(pad,pad,pad,pad);
		
		topPanel.add(categoryLbl, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, in, pad, pad));
		topPanel.add(categories, new GridBagConstraints(0, 1, 1, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, in, pad, pad));
		topPanel.add(subcategories, new GridBagConstraints(0, 2, 1, 1, 0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, in, pad, pad));
		topPanel.add(advBox, new GridBagConstraints(1, 0, 1, 3, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
	//	end of topPanel gui ----------------------------------	
	
		
	// shown in middle of frame...scrollpane's go on top	
		centerPanel = new JPanel();
	// end of centerPanel -----------------------------------
		
	// shown at the bottom of the frame with close, search and dockme buttons
		bottomPanel = new JPanel();
		bottomPanel.setBorder(new EmptyBorder(8, 0, 8, 0));
		bottomPanel.setBackground(backgroundColor);
		close = new JButton(closeWindow);
		dockMe = new JButton(dockAddLayer);
		bottomPanel.add(close);
		bottomPanel.add(dockMe);
	// end of bottomPanel ----------------------------------- 
	
	// set the selected startup category, if one is defined
		setInitialCategory();
	// populates the combobox
		rebuildCategoryBox();
		
		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		
	// end of addLayer gui
		
	} // end constructor
	/**
	 * Factory method to return the static instance of AddLayer or create a new instance if it does not exist
	 * @return AddLayer
	 */
	public static AddLayer getInstance() {
		//added this method instead of always having a new instance get created statically so that it can be determined if the 
		//AddLayer instance has been created. If it has not been created, calling any public method on it, creates an instance
		//of AddLayer, which is a JFrame and creates a small JFrame inadvertently with no attributes set. The instanceExists() method can now be called
		//to determine if there is an AddLayer to refresh without creating one by accident. 
		if (addLayer == null) {
			addLayer = new AddLayer();
		} 
		return addLayer;
	}
	/**
	 * Determine if an instance of AddLayer exists
	 * @return boolean 
	 */
	public static boolean instanceExists() {
		if (addLayer == null) {
			return false;
		} else {
			return true;
		}
	}
	// listener is added to 'choices'  (combobox on add layer screen)
	private ActionListener comboBoxListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(activateComboBoxListener){
				
				if (e.getSource()==categories){
					activateComboBoxListener=false;
					rebuildSubcategoryBox();	
					activateComboBoxListener=true;
				}

				String cat = (String) categories.getSelectedItem();
				String subcat = (String) subcategories.getSelectedItem();

				
				if (cat!=null){
					displayScrollMenu(cat, subcat);
				
					// if your not logged in there will be no customServer
					MapServer customServer = MapServerFactory.getCustomMapServer();
					if(customServer==null)
						return;
				}
			}
		}
	};
	
	
// Method rebuilds the scrollmenu that displays all the topic panes and 
// layer buttons.	
	public static void displayScrollMenu(String category, String subcategory) {
	// Creates the panel which will go inside the scrollpane
		JXTaskPaneContainer displayPane = new JXTaskPaneContainer();
		displayPane.setBackground(panelGrey);
	// Hashmap is used to keep a running list of the topics for this category
		HashMap<String, JXTaskPane> topicToPane = new HashMap<String, JXTaskPane>();
		HashMap<String, Integer> topicToCount = new HashMap<String, Integer>();
		boolean first = true;
		
	// Displays the a page with all the user's custom maps	
		if(category!=null && category.equalsIgnoreCase("Custom")){
			
			int count = 0;
			JXTaskPane topicPane = new JXTaskPane();
			topicPane.add(Box.createRigidArea(new Dimension(300,5)));
			
			for(final LayerParameters cl: LayerParameters.customlParameters){
				if(!advBox.isSelected())
					continue;
				JButton newButton = new JButton(cl.name.toUpperCase()+" ");
				newButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						MapServer cServer = MapServerFactory.getCustomMapServer();
						MapSource display = cServer.getSourceByName(cl.options.get(0));
						MapSource plot = cServer.getSourceByName(cl.options.get(1));
						if(display == null && plot == null){
							System.out.println("map not found");
							return;
						}

						new MapLViewFactory().createLayer(display, plot);
						LManager.getLManager().repaint();
//						System.out.println(display.getName());
					}
				});
				setButton(newButton, mapsIcon);
				topicPane.add(newButton);
				count++;
			}
			topicPane.setTitle("Custom Maps  ("+count+")");
			displayPane.add(topicPane);
		}
		
	// Displays the search page
		if(category!=null && category.equalsIgnoreCase("Search")){
			JXTaskPane searchOptions = new JXTaskPane();
			searchOptions.add(Box.createRigidArea(new Dimension(300,5)));
			
			searchOptions.setTitle("Search Options");
			JButton searchBtn = new JButton(searchAct);
			searchBox = new JTextField(searchPrompt);
			searchBox.addActionListener(searchAct);
			searchBox.addFocusListener(searchBoxListener);
			
			nameBx = new JCheckBox("Name");
			categoryBx = new JCheckBox("Category");
			citationBx = new JCheckBox("Citation");
			descripBx = new JCheckBox("Description");
			topicBx = new JCheckBox("Topic");
			advancedSearchBx = new JCheckBox("Adv. Layers");
			customBx = new JCheckBox("Custom");
			
		// Sets defaults on search options	
			nameBx.setSelected(true);
			descripBx.setSelected(true);
		// if Advanced is selected, also search those layers	
			if (advBox.isSelected())
				advancedSearchBx.setSelected(true);
		// if not logged in, disable searching custom layers	
			if (Main.USER.equals(""))
				customBx.setEnabled(false);
			
			JPanel top = new JPanel();
			top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
			top.setAlignmentX(Component.CENTER_ALIGNMENT);
			top.add(searchBox);
			top.add(searchBtn);
			
			JPanel bottom = new JPanel();
			bottom.setLayout(new GridLayout(3,3));
			bottom.setAlignmentX(Component.CENTER_ALIGNMENT);
			bottom.add(nameBx);
			bottom.add(categoryBx);
			bottom.add(citationBx);
			bottom.add(descripBx);
			bottom.add(topicBx);
			bottom.add(advancedSearchBx);
			bottom.add(customBx);
			
			searchOptions.add(top);
			searchOptions.add(bottom);
			
			resultsPane = new JXTaskPane();
			resultsPane.add(Box.createRigidArea(new Dimension(300,5)));
			resultsPane.setTitle("Results");
			
			displayPane.add(searchOptions);
			displayPane.add(resultsPane);
		}
			
	// If there are subcategories, the default screen shows a message requesting user 
	// to select a subcategory in order to see any buttons	
		if (subcategories.getSelectedItem().equals(subcatPrompt)&&subcategories.isEnabled()){
			// Add 'select subcategory' message
			ImportantMessagePanel selectMsg = new ImportantMessagePanel("Please select a subcategory from the dropdown");
			displayPane.add(selectMsg);
			
			// Begin available subcategories list
			displayPane.add(Box.createVerticalStrut(20));
			Font ssFont = ThemeFont.getBold().deriveFont(15f);
			JLabel availableLbl = new JLabel("Subcategories available:");
			availableLbl.setFont(ssFont);
			availableLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
			JPanel p = new JPanel();
			p.add(availableLbl);
			displayPane.add(p);
			// Loop through and create a list of corresponding subcategories
			ArrayList<String> subChs =new ArrayList<String>();
			for (LayerParameters l : LayerParameters.lParameters){
				// Only look at lParams that match current category	
					if (l.category.equalsIgnoreCase(selectedCategory)){
						if(!advBox.isSelected() && l.advancedOnly)
							continue;
						if(!subChs.contains(l.subcategory)&&l.subcategory!=null&&l.subcategory.length()>0)
							subChs.add(l.subcategory);
					}
				}
			// Add subcategories list to display pane
			JPanel p2 = new JPanel();
			p2.setLayout(new BoxLayout(p2, BoxLayout.PAGE_AXIS));
			for (String s : subChs){
				JLabel aSubCat = new JLabel(s);
				p2.add(aSubCat, BorderLayout.CENTER);
			}
			//Funky way needed to have labels centered in panel
			JPanel p3 = new JPanel();
			p3.add(p2);
			displayPane.add(p3);
		}
		
	// Subcategory check needed for the lparams for loop below...	
		if (subcategory.equalsIgnoreCase(subcatPrompt)&&!subcategories.isEnabled()){
			subcategory = "";
		}
		
		
	// Cycles through the arraylist of LayerParameters and populates
	// display panel with the appropriate buttonshomePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
		for (final LayerParameters l : LayerParameters.lParameters){
			if (!advBox.isSelected() && l.advancedOnly)
				continue;
			if (l.category.equalsIgnoreCase(category)&&l.subcategory.equalsIgnoreCase(subcategory)){
				if(!l.name.equalsIgnoreCase("break")){
				// Creates the new button to be added to displayPanel	
					JButton newButton = createButton(l);
					if (newButton == null){
						continue;
					}

					// Keeps track of the number of results to display on each JXTaskPane
					// and collapses all taskPanes except the first one.
					if(l.topic!=null){
						if(topicToPane.containsKey(l.topic)){;
							topicToPane.get(l.topic).add(newButton);
							
							if (topicToCount.get(l.topic)==null){
								topicToCount.put(l.topic, 1);
							}
							else{
								int count = topicToCount.get(l.topic);
								topicToCount.put(l.topic, count+1);
								topicToPane.get(l.topic).setTitle(l.topic+"  ( "+topicToCount.get(l.topic)+" )");	
							}
							first=false;
						}
						else{
							JXTaskPane topicPane = new JXTaskPane();									
							topicPane.add(Box.createRigidArea(new Dimension(300,5)));
							topicPane.add(newButton);
						
							topicToPane.put(l.topic, topicPane);
							topicToCount.put(l.topic, 1);
							topicPane.setTitle(l.topic+"  ( "+topicToCount.get(l.topic)+" )");
							
							if(!first && !l.category.equalsIgnoreCase("home")){
								topicPane.setCollapsed(true);
//								int count = topicPane.getComponentCount();
//								for (int i=0; i<count; i++){
//									Object o = topicPane.getComponent(i);
//									if (o instanceof JXCollapsiblePane){
//										JXCollapsiblePane jxo = (JXCollapsiblePane)o;
//										jxo.setCollapsed(true);
//									}	
//								}
//								topicPane.setCollapsed(true);
							}
							else{
								if(!l.category.equalsIgnoreCase("home")){
									first=false;
								}
							}
							displayPane.add(topicPane);
						}
					}
					else{
						displayPane.add(Box.createRigidArea(gap));
						displayPane.add(newButton);
					}
				} // end of "!break" if statement
				
				if(l.name.equalsIgnoreCase("break")){
					if(topicToPane.containsKey(l.topic)){
						topicToPane.get(l.topic).add(Box.createRigidArea(new Dimension(0,5)));
						topicToPane.get(l.topic).add(new JSeparator());
						topicToPane.get(l.topic).add(Box.createRigidArea(new Dimension(0,5)));
					}
				}
				
				
			}	// end if statement
		}	//end for loop
		
		displayPane.add(Box.createRigidArea(gap));
	// Rebuilds what the user sees
	// Creates the scrollpane which will be displayed in AddLayer.centerPanel
		JScrollPane sp = new JScrollPane(displayPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
														ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		centerPanel.removeAll();
		centerPanel.setLayout(new BorderLayout());
	// adds the scrollpane to the centerPanel and increases the scroll sensitivity
		centerPanel.add(sp, BorderLayout.CENTER);		
		sp.getVerticalScrollBar().setUnitIncrement(25);
		
		refresh();
	}
	
	
// Used in the checkbox	
	Action extendChoices = new AbstractAction("Advanced") {
		public void actionPerformed(ActionEvent e) {
		// Sets the selectedCategory variable (will be referred to w/the advBtn)
			selectedCategory = (String)categories.getSelectedItem();
			selectedSubcategory = (String)subcategories.getSelectedItem();
			activateComboBoxListener=false;
			rebuildCategoryBox();	
			activateComboBoxListener=true;
		}
	};  // end action

	
	
// This method rebuilds the dropdown menu of choices of categories to select from
	public static void rebuildCategoryBox() {
		if(LayerParameters.initializationComplete==false){
			System.out.println("LayerParameters has not initialized");
			return;
		}
		ArrayList<String> chs = new ArrayList<String>();
		//remove everything from the dropdown
		categories.removeAllItems();
		//make sure to refresh the size (during body change, etc)
		categories.setPreferredSize(null);

		for (LayerParameters l : LayerParameters.lParameters){
			if (!advBox.isSelected() && l.advancedOnly)
				continue;
			if (!chs.contains(l.category)&&l.category!=null&&l.category.length()>0)
				chs.add(l.category);
		}
		if(Main.USER!=null&&Main.USER.length()>0){
			LayerParameters.refreshCustomList();
			for (LayerParameters cl : LayerParameters.customlParameters){
				if(!advBox.isSelected()) {
					break;//this conditional will not change so just break out
				}
				if (!chs.contains(cl.category)&&cl.category!=null&&cl.category.length()>0)
					chs.add(cl.category);
			}
		}
// This page is for word searching for maps/stamps/etc	
		if (chs.size() > 0) {//if there is nothing in chs, don't add anything
			chs.add(1,"Search");
		}
		
// Add all entries back to combobox
		for (String c : chs){
			activateComboBoxListener=false;
			categories.addItem(c);
			activateComboBoxListener=true;
		}
		if (categories.getItemCount() > 0) {//when choices is empty, setting the selected index throws an Exception
			if(chs.contains(selectedCategory))	{
				activateComboBoxListener=false;
				categories.setSelectedItem(selectedCategory);
				activateComboBoxListener=true;
			} else {
				activateComboBoxListener=false;
				categories.setSelectedIndex(0);
				activateComboBoxListener=true;
			}
			activateComboBoxListener=true;
		}
		Dimension size = categories.getPreferredSize();
		categories.setMinimumSize(size);
		categories.setPreferredSize(size);
		categories.setMaximumSize(size);
		categories.setSize(size);
		
		activateComboBoxListener=false;
		rebuildSubcategoryBox();
		activateComboBoxListener=true;
		
	} // end method
	
// This method rebuilds the dropdown list of subcategories to choose from based
// off of the selected category	
	public static void rebuildSubcategoryBox(){
		//ArrayList is added to combobox
		ArrayList<String> subChs = new ArrayList<String>();
		subcategories.removeAllItems();
		subChs.add(subcatPrompt);
		//Used to match in the for loop
		if (categories.getSelectedItem()!=null){
			selectedCategory=(String)categories.getSelectedItem();
		}
		
		for (LayerParameters l : LayerParameters.lParameters){
		// Only look at lParams that match current category	
			if (l.category.equalsIgnoreCase(selectedCategory)){
				if(!advBox.isSelected() && l.advancedOnly)
					continue;
				if(!subChs.contains(l.subcategory)&&l.subcategory!=null&&l.subcategory.length()>0)
					subChs.add(l.subcategory);
			}
		}
		
		//Add all entries back to combobox
		for (String sc : subChs){
			subcategories.addItem(sc);
		}
		
		if (subcategories.getItemCount() > 0) {

			if(subChs.contains(selectedSubcategory))	{
				subcategories.setSelectedItem(selectedSubcategory);
			} else {
				subcategories.setSelectedIndex(0);
			}
		}
	//If there are actually subcategories, enable combobox otherwise disable.
		if (subChs.size()<2)
			subcategories.setEnabled(false);
		else
			subcategories.setEnabled(true);
		
		displayScrollMenu((String)categories.getSelectedItem(), (String)subcategories.getSelectedItem());
		
	}// end subcategory rebuild method
	
	
	
	public static void displayAddLayer(Component c) {	
		getInstance(); //make sure an instance exists
		if(!isDocked){
			close_window();
			Dimension size = new Dimension(addLayerWidth, addLayerHeight);
			addLayer.setContentPane(cp);
			addLayer.setVisible(false);
			addLayer.setSize(size);
			addLayer.setLocationRelativeTo(c);
			Point p = addLayer.getLocation();
			addLayer.setLocation(p.x+80, p.y-50);
			addLayer.setVisible(false);
			
			// Explicitly check whether the size is still correct, 
			// to try and fix a stupid, stupid linux bug
			if (addLayer.getSize().width!=size.width || 
					addLayer.getSize().height!=size.height) {
				addLayer.setSize(size);
			}
	
			// pulls frame back up if minimized.
			if(addLayer.getState()!=addLayer.NORMAL) { addLayer.setState(addLayer.NORMAL); }	
			
			addLayer.setVisible(true);
		}

		if(isDocked) {
			LManager.getLManager().activateTab(1);
		}
			
	} // end method
	
	
	private static void refresh()
	{
		if(addLayer != null){
			addLayer.centerPanel.setVisible(false);
			addLayer.centerPanel.setVisible(true);
		}
	}
	
	public static void setButton(JButton b, ImageIcon i) {
		
		b.setMinimumSize(b.getPreferredSize());
		b.setPreferredSize(b.getPreferredSize());
		b.setMaximumSize(b.getPreferredSize());
		
		b.setText(b.getText());
		
		//if the icon is null, create a transparent icon,
		// this way all the text will still be properly aligned.
		if(i == null){
			i = new ImageIcon(Util.newBufferedImage(5, 5));
		}
		b.setIcon(i);
		
		//set the text to start at a consistent location,
		//  regardless of the width of the icon
		int gap = 40 - i.getIconWidth();
		
		b.setHorizontalAlignment(SwingConstants.LEFT);
		b.setHorizontalTextPosition(SwingConstants.RIGHT);
		b.setIconTextGap(gap);
	}
	
	public static void setButton(JButton b){
		setButton(b, null);
	}
	
	Action closeWindow = new AbstractAction("Close".toUpperCase()) {
		public void actionPerformed(ActionEvent e) {
			close_window();
		}
	};  // end action
	
	public static void close_window() {
		addLayer.setVisible(false);
	}
	
	Action dockAddLayer = new AbstractAction("Dock Me".toUpperCase()){
		public void actionPerformed(ActionEvent e) {
			dock_add_layer();
		}
	}; // end action
	
	public void dock_add_layer() {
		isDocked=true;
		String name = "Add Layer";
		LManager.getLManager().dockAddLayer(name, AddLayer.cp);
		setVisible(false);
		addLayer.dockMe.setEnabled(false);
		addLayer.close.setEnabled(false);
	}
	
	private ActionListener advancedBoxListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			new PropWriter().queueProcessing();
		}
	};
	
	
//Clears the textfield (for searching) once cursor enters it if it has 
//	the original prompt in it.	
	private static FocusListener searchBoxListener = new FocusListener() {	
		public void focusLost(FocusEvent e) {
		}
		public void focusGained(FocusEvent e) {
			String searchTerm = searchBox.getText();
			if (searchTerm.equals(searchPrompt))
				searchBox.setText("");
		}
	};
	
	
	
//Is used when the "Search" button is selected, or when enter is pressed while 
// typing inside the textfield.	
	static Action searchAct = new AbstractAction("Search".toUpperCase()){
		public void actionPerformed(ActionEvent e){
	// pulls search words	
			String matchMe = searchBox.getText(); 
			String[] matchWords = matchMe.split(" ");
	// makes sure new text was actually entered		
			if (matchMe.equals(searchPrompt))
				return;
	// resets resultsPane		
			resultsPane.removeAll();
			searchResults.clear();
			resultsPane.add(Box.createRigidArea(new Dimension(300,5)));
			resultsPane.setTitle("Results");
	// checks what areas to search in		
			boolean nameIsChecked = nameBx.isSelected();
			boolean descripIsChecked = descripBx.isSelected();
			boolean catIsChecked = categoryBx.isSelected();
			boolean topicIsChecked = topicBx.isSelected();
			boolean citIsChecked = citationBx.isSelected();
	// performs search		
			searchFor(matchWords, nameIsChecked, descripIsChecked, catIsChecked, topicIsChecked, citIsChecked);
		}
	};
	

	
	public static void searchFor(String[] match, boolean nameIsChecked,boolean descripIsChecked,
							boolean catIsChecked,boolean topicIsChecked, boolean citIsChecked){
		
	// checks to make sure the search string is reasonable length		
		if (match[0].length()<3){
			JLabel tooShortMsg = new JLabel("Search entry must be at least three characters.");
			resultsPane.add(tooShortMsg);	
			return;
		}
		

	// pulls the list of lparams to search through	
		ArrayList<LayerParameters> searchLParams = new ArrayList<LayerParameters>();
		searchLParams.addAll(LayerParameters.lParameters);
		// If logged in, cycle through custom lparameters as well	
		if (MapServerFactory.getCustomMapServer()!=null){
			searchLParams.addAll(LayerParameters.customlParameters);
		}
		
	// starts looping through appropriate lparameters sections (topic, name, etc)			
		for (LayerParameters l : searchLParams){
			if (l.advancedOnly && !advancedSearchBx.isSelected())
				continue;
			if(l.category.equalsIgnoreCase("Custom") && !customBx.isSelected()){
				continue;
			}
			
			if (l.category.length()>1 && !l.name.equals("break")){	
				if (nameIsChecked){		
					if (foundIn(l.name, match)){
						searchResults.add(l);
						continue;
					}
				}
				if (descripIsChecked){
					if (foundIn(l.description,match)){
						searchResults.add(l);
						continue;
					}
				}
				if (catIsChecked){
					if (foundIn(l.category, match)){
						searchResults.add(l);
						continue;
					}
				}
				if (topicIsChecked){
					if (foundIn(l.topic, match)){
						searchResults.add(l);
						continue;
					}
				}
				if (citIsChecked){
					if (foundIn(l.citation, match)){
						searchResults.add(l);
						continue;
					}
				}
			}
		}
		

		if (searchResults.size()==0){
			JLabel noResults = new JLabel("Your search returned no results.");
			resultsPane.add(noResults);
			return;
		}
		

	// Hashmap is used to keep a running list of the topics for this category
		HashMap<String, JXTaskPane> topicToPane = new HashMap<String, JXTaskPane>();
		HashMap<String, Integer> topicToCount = new HashMap<String, Integer>();	
	// adds the buttons to the display area	and creates collapsible panes for unique
	// category-topic paths.	
		for (LayerParameters r : searchResults){
			String paneTitle = r.toString();
			JButton resultBtn = createButton(r);
			
			if(topicToPane.containsKey(paneTitle)){
				topicToPane.get(paneTitle).add(resultBtn);
				
				if (topicToCount.get(paneTitle)==null)
					topicToCount.put(paneTitle, 1);
				else{
					
					int count = topicToCount.get(paneTitle);
					topicToCount.put(paneTitle, count+1);
					topicToPane.get(paneTitle).setTitle(paneTitle+"  ( "+topicToCount.get(paneTitle)+" )");	
				}
			}
			else{
				JXTaskPane topicPane = new JXTaskPane();
				topicPane.add(Box.createRigidArea(new Dimension(300,5)));
				topicPane.add(resultBtn);
				topicToPane.put(paneTitle, topicPane);
				topicToCount.put(paneTitle, 1);
				topicPane.setTitle(paneTitle +"  ( "+topicToCount.get(paneTitle)+" )");
				resultsPane.add(topicPane);
			}
		
			
			
		}
		
		resultsPane.setTitle("Results  ("+searchResults.size()+")");
		
	}
	

//Used in the searchFor method to help with the search ability of addlayer.
	public static boolean foundIn(String textToBeSearched, String[] searchString){
		if (textToBeSearched==null || textToBeSearched.length()<=0)
			return false;
		textToBeSearched = textToBeSearched.toUpperCase();
		for (String searchWord : searchString){
			searchWord = searchWord.toUpperCase();
			if (textToBeSearched.contains(searchWord)){
				continue;
			}
			return false;
		}
		return true;
	}
	
// Creates the buttons that are used to load the layers.  Defines how each
//	type of button is supposed to act and what it's supposed to look like.
	public static JButton createButton(final LayerParameters l){
		JButton newButton = new JButton(l.name.toUpperCase()+" ");
		// Creates the listener assigned to the new button	
			newButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
			
			// Tests to make sure the servers have responded before
			//	a new layer is added.	
					int waitcount = 0;
					while(MapServerFactory.getMapServers()==null){
						try {
							Thread.sleep(500);
							waitcount++;
							if (waitcount >= 30){
								JOptionPane.showMessageDialog(
										Main.mainFrame,
										"Unable to add layer, try again.",
										"Error!",
										JOptionPane.ERROR_MESSAGE
								);
								return;
							}
								
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					
				// MAPS	
					if(l.type.equalsIgnoreCase("map")){
						//check to see if servers is null, if so set to a new list
						// hopefully shouldn't get here...but just in case
						if(l.servers == null){
							l.servers = new ArrayList<String>();
						}
						
						ArrayList<MapSource> plotSources = new ArrayList<MapSource>();

						List<MapServer> servers = MapServerFactory.getMapServers();
						MapSource display = null;
						
						MapServer server = null;
						//if server is specified try that first, if not try default server first.
						if(l.servers.size()>0 && l.servers.get(0)!=null){
							server = MapServerFactory.getServerByName(l.servers.get(0));
						}else{
							server = MapServerFactory.getServerByName("default");
						}
						
						//try to find map on specified server
						if(server != null){
							display = server.getSourceByName(l.options.get(0));
						}
						
						//if it wasn't found cycle through all map servers to find source
						if(display == null){
							for (MapServer s: servers) {
								display = s.getSourceByName(l.options.get(0));
								if (display!=null){
									break;
								}
							}
						}

						int optionsLength = l.options.size();
						// For each map source we want to plot, loop through our list of servers and look for matches
						// If a map source doesn't exist, it will silently disappear from the list of plotted maps....
						for (int i=1; i<optionsLength; i++) {
							MapSource plot = null;
							MapServer pserver = null;
							//first try and use specified server (none specified means default server)
							if(l.servers.size()>i && l.servers.get(i)!=null){
								pserver = MapServerFactory.getServerByName(l.options.get(i));
							}else{
								pserver = MapServerFactory.getServerByName("default");
							}
							//try to find map on that server
							if(pserver != null){
								plot = pserver.getSourceByName(l.options.get(i));
							}
							//if it finds something add that to all plots
							if(plot!=null){
								plotSources.add(plot);
							}
							//else cycle through servers looking for map
							else{
								for (MapServer s: servers) {
									plot = s.getSourceByName(l.options.get(i));
									if (plot!=null) {
										plotSources.add(plot);
										break;
									}
								}
							}
						}
						
						if (display == null && plotSources.size()==0){
							System.out.println("map not found");
							return;
						}
											
						new MapLViewFactory().createLayer(display, plotSources, l, null);
						LManager.getLManager().repaint();
					}
				// UPLOAD_MAP	
					else if(l.type.equalsIgnoreCase("upload_map")){
					    CM_Manager mgr = CM_Manager.getInstance();
                        mgr.setLocationRelativeTo(Main.mainFrame);
                        mgr.setVisible(true);
                        mgr.setSelectedTab(CM_Manager.TAB_UPLOAD);
					} 
				// ADVANCED_MAP	
					else if(l.type.equalsIgnoreCase("advanced_map")){
						new MapLViewFactory().createLView(true, AddLayer.getInstance());
						LManager.getLManager().repaint();
					}
				// SHAPES
					else if(l.type.equalsIgnoreCase("shape")){
						if (l.name.equalsIgnoreCase("Custom Shape Layer")){
							ShapeLView shpLView = (ShapeLView)new ShapeFactory().newInstance(false, l);
							LManager.getLManager().receiveNewLView(shpLView);
							LManager.getLManager().repaint();	
						}
						else{
							String dirName = l.options.get(0);
							String fileName = l.options.get(1);
							String url = l.options.get(2);
							boolean readOnly = true;
							if(l.options.size()>3){
								if(l.options.get(3).equals("false")){
									readOnly = false;
								}
							}
							ShapeLView shpLView = (ShapeLView)new ShapeFactory().newInstance(readOnly, l);
							ShapeLayer shpLayer = (ShapeLayer)shpLView.getLayer();
							shpLayer.loadReadOnlyFile(dirName, fileName, url);
							
							LManager.getLManager().receiveNewLView(shpLView);
							LManager.getLManager().repaint();
						}
					}			
				// OPENSTREETMAP	
					else if(l.type.equalsIgnoreCase("open_street_map")){
						if (l.options.get(0).equals("0")){ 
							int osmType = 0;											
							new StreetLViewFactory().createLView(osmType, l);
							LManager.getLManager().repaint();
							
						}else if (l.options.get(0).equals("1")){ 
							int osmType = 1;
							new StreetLViewFactory().createLView(osmType, l);
							LManager.getLManager().repaint();						
						} 	
					}
				// STAMPS
					else if(l.type.equalsIgnoreCase("stamp")){
						ArrayList<String> temp = new ArrayList<String>(l.options);
						String instrument = temp.get(0);
						temp.remove(0);
						String[] initialColumns = new String[temp.size()];
						initialColumns = temp.toArray(initialColumns);
						new StampFactory().addLView(l, instrument, initialColumns, l.layergroup);
						LManager.getLManager().repaint();
					}
				// SAVED_LAYER
					else if(l.type.equalsIgnoreCase("saved_layer")){
						String url = l.options.get(0);
						try{
                            if(url == null){
                                System.err.println("Could not read URl for session: "+l.name);
                                return;
                            }
						    JmarsHttpRequest request = new JmarsHttpRequest(url, HttpRequestType.GET);
						    request.setBrowserCookies();
						    request.setConnectionTimeout(10*1024);
						    boolean fetchSuccessful = request.send();
							if(!fetchSuccessful) {
							    int code = request.getStatus();
								System.err.println("HTTP code "+code+" recieved when downloading session from "+url);
								return;
							}
                            List<SavedLayer> layers = SavedLayer.load(request.getResponseAsStream());
							layers.get(0).materialize(l);
							LManager.getLManager().repaint();
							request.close();
						} catch(Exception e1){
							synchronized(this){
								System.err.println("Error processing session named "+l.name);
								e1.printStackTrace();
							}
						}
					}	
				// TIMESLIDER
					else if(l.type.equalsIgnoreCase("timeslider")){
						new SliderFactory().createLView(l);
						LManager.getLManager().repaint();
					}
				//LandingSite
					else if(l.type.equalsIgnoreCase("landing_site")){
						String layerName = null;
						String config = null;
						if(l.options.size()>0){
							layerName = l.options.get(0);
							config = l.options.get(1);
						}
						new LandingFactory().createLView(false, l, layerName, config);
						LManager.getLManager().repaint();
					}
					
				// Queries the LViewFactory to get the matching factory and then create and display lview.
					else {
						LViewFactory factory = LViewFactory.findFactoryType(l.type);
						if (factory!=null) {
							factory.createLView(false, l);
							LManager.getLManager().repaint();
						} else {
							JOptionPane.showMessageDialog(AddLayer.addLayer, "Unable to add the selected layer.  Please update to the latest version of JMARS or contact the support team.");
						}
					}
				}
			});		// end action listener
			
		// Determines what icon is to be used with new button	
			if(l.type.contains(new MapLViewFactory().type)){
				setButton(newButton, mapsIcon);
			}
			else if(l.type.equalsIgnoreCase(new ShapeFactory().type)){
				setButton(newButton, shapeIcon);
			}
			else if(l.type.equalsIgnoreCase(new StampFactory().type)){
				setButton(newButton, stampsIcon);
			}		
			else if(l.type.equalsIgnoreCase(new SliderFactory().type)){
				setButton(newButton, sliderIcon);
			}
			else if(l.type.equalsIgnoreCase(new ThreeDFactory().type)){
				setButton(newButton, threeDIcon);
			}
			else if(l.type.equalsIgnoreCase(new GroundTrackFactory().type)){
				setButton(newButton, groundtrackIcon);
			}
			else if(l.type.equalsIgnoreCase(new GridFactory().type)){
				setButton(newButton, gridIcon);
			}
			else if(l.type.equalsIgnoreCase(new NomenclatureFactory().type)){
				setButton(newButton, nomenclatureIcon);
			}
			else if(l.type.equalsIgnoreCase(new CraterFactory().type)){
				setButton(newButton, craterIcon);
			}
			else if(l.type.equalsIgnoreCase(new KRCFactory().type)){
				setButton(newButton, krcIcon);
			}
			else if(l.type.equalsIgnoreCase(new LandingFactory().type)){
				setButton(newButton, landingIcon);
			}
			else if(l.type.equalsIgnoreCase(new MCDFactory().type)){
				setButton(newButton, mcdIcon);
			}
			else if(l.type.equalsIgnoreCase(new MosaicsLViewFactory().type)){
				setButton(newButton, mosaicsIcon);
			}
			else if(l.type.equalsIgnoreCase(new NorthFactory().type)){
				setButton(newButton, northIcon);
			}
			else if(l.type.contains("roi")){
				setButton(newButton, roiIcon);
			}
			else if(l.type.equalsIgnoreCase(new ScaleFactory().type)){
				setButton(newButton, scaleIcon);
			}
			else if(l.type.equalsIgnoreCase(new StreetLViewFactory().type)){
				setButton(newButton, streetsIcon);
			}
			else{
				setButton(newButton);
			}
		// disable special buttons first becasue it makes the next logic simpler
			if(l.type.equalsIgnoreCase("upload_map") || l.type.contains("roi")
					|| l.type.contains("plan") || l.type.equalsIgnoreCase("tes") 
					|| l.name.equalsIgnoreCase("custom stamps" )
					|| (l.type.equalsIgnoreCase("stamp") && l.layergroup.equalsIgnoreCase("dawn_team"))){
				
				//disable button
				newButton.setEnabled(false);
				//disable the name and icon on the button
				if (newButton.getComponentCount()>0){
					newButton.getComponent(0).setEnabled(false);
					newButton.getComponent(1).setEnabled(false);
				}
			}
			

		// enable themis/mars buttons if logged in on mars
			if(!Main.USER.equals("") && Main.AUTH_DOMAIN.equalsIgnoreCase("msff") 
					&&
					(l.type.equalsIgnoreCase("upload_map") || l.type.contains("roi")
					|| l.type.contains("plan") || l.type.equalsIgnoreCase("tes") 
					|| l.name.equalsIgnoreCase("custom stamps" ))){
				
				//enable button and icon if it has one
				newButton.setEnabled(true);
				if (newButton.getComponentCount()>0){
					newButton.getComponent(0).setEnabled(true);
					newButton.getComponent(1).setEnabled(true);
				}
			}
			
		// enable dawn team stamps
			if(!Main.USER.equals("") && Main.AUTH_DOMAIN.equalsIgnoreCase("dawn")
					&&
					(l.type.equalsIgnoreCase("stamp") && l.layergroup.equalsIgnoreCase("dawn_team"))){
				
				//enable button and icon if it has one
				newButton.setEnabled(true);
				if (newButton.getComponentCount()>0){
					newButton.getComponent(0).setEnabled(true);
					newButton.getComponent(1).setEnabled(true);
				}
			}
			
			
			return newButton;
	}
		
	
	public static void setInitialCategory(){
		initCat = Config.get(Util.getProductBodyPrefix()+"initialCat", "");
		if (initCat != null && !initCat.equals("")){
			selectedCategory = initCat;
		}
	}
	
	
}  //end class



// These two classes are to needed to create a thread that will 
// not interfere with the awt thread so the UI does not have to wait
// to finish loading while the config property for the advanced box
// is saved.
class PropWriter implements Runnable {
	static ExecutorService pool;
	
	public void queueProcessing() {
		synchronized (this) {
			if (pool == null) {

				pool = Executors.newFixedThreadPool(1, new AdvConfigPropFactory("Advance Config Setting"));
			}
			
			pool.execute(this);
		}
	}
		
	public void run() {
		if (AddLayer.advBox.isSelected())
			Config.set("advancedModeSelected", "true");
		else
			Config.set("advancedModeSelected", "false");
	}		
}
class AdvConfigPropFactory implements ThreadFactory {
	static final AtomicInteger poolNumber = new AtomicInteger(1);
	final ThreadGroup group;
	final AtomicInteger threadNumber = new AtomicInteger(1);
	final String namePrefix;

	public AdvConfigPropFactory(String name) {
		SecurityManager s = System.getSecurityManager();
		group = (s != null)? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = name + "-" + poolNumber.getAndIncrement() + "-thread-";
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		// do NOT want a hung Stage to keep the JVM open
		t.setDaemon(true);
		// do NOT want threads killing the AWT thread
		t.setPriority(Thread.MIN_PRIORITY);
		return t;
	}
}// end config property writing classes