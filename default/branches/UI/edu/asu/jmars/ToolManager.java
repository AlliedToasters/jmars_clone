package edu.asu.jmars;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import edu.asu.jmars.layer.InvestigateDisplay;
import edu.asu.jmars.layer.LManager;
import edu.asu.jmars.layer.Layer.LView;
import edu.asu.jmars.layer.investigate.InvestigateFactory;
import edu.asu.jmars.swing.UrlLabel;
import edu.asu.jmars.ui.image.factory.ImageFactory;
import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.*;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.util.Config;
import edu.asu.jmars.viz3d.ThreeDManager;
import edu.asu.jmars.ui.looknfeel.theme.component.ThemeButton;
import edu.asu.jmars.ui.looknfeel.theme.component.ThemeImages;
import edu.asu.jmars.ui.looknfeel.theme.component.ThemeTextArea;
import static edu.asu.jmars.ui.looknfeel.Utilities.parse;



public class ToolManager extends JPanel {
	
	private static List<ToolListener> listeners = new ArrayList<ToolListener>();	
	private static int Mode;
	private static int Prev;
	  
    final public static int ZOOM_IN 	= 0;
    final public static int ZOOM_OUT 	= 1;
    final public static int MEASURE 	= 2;
    final public static int PAN_HAND 	= 3;
    final public static int SEL_HAND 	= 4;
    final public static int SUPER_SHAPE = 5;
    final public static int INVESTIGATE = 6;
    final public static int EXPORT = 7;
    final public static int RESIZE = 8;  
   
    private ToolButton selHand;
    private ToolButton panHand;
    private ToolButton zoomIn;
    private ToolButton zoomOut;
    private ToolButton measure;
    private static ToolButton investigate;
    private ToolButton exportButton;
    private ToolButton resizeButton;
    
    private static Color imgColor = ((ThemeImages) GUITheme.get("images")).getFill();
    private static Color imgSelectedColor = ((ThemeImages) GUITheme.get("images")).getSelectedfill();
    
    public ToolManager ()
    {    
        Image sel_hand =  ImageFactory.createImage(MOUSE_POINTER_IMG
        		         .withDisplayColor(imgColor))       		               		          				 
        		         .getScaledInstance(13, 20, Image.SCALE_SMOOTH);      
        Image sel_hand_sel = ImageFactory.createImage(MOUSE_POINTER_IMG_SEL
		         			.withDisplayColor(imgSelectedColor))		         			
		         			.getScaledInstance(13, 20, Image.SCALE_SMOOTH);     
        ImageIcon select = new ImageIcon(sel_hand);
        ImageIcon selectSel = new ImageIcon(sel_hand_sel);
      
        Image zoom_in = ImageFactory.createImage(ZOOM_IN_IMG
        		        .withDisplayColor(imgColor))		                
        		        .getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Image zoom_in_sel = ImageFactory.createImage(ZOOM_IN_IMG_SEL
        					.withDisplayColor(imgSelectedColor))        					       		
        		            .getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Image zoom_in_c = ImageFactory.createImage(CURSOR_ZOOM_IN
   				 		  .withDisplayColor(imgColor));
        ImageIcon magIn = new ImageIcon(zoom_in);
        ImageIcon magInSel = new ImageIcon(zoom_in_sel);
        
        Image zoom_out = ImageFactory.createImage(ZOOM_OUT_IMG
        				.withDisplayColor(imgColor))    				
        		        .getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Image zoom_out_sel = ImageFactory.createImage(ZOOM_OUT_IMG_SEL
        					.withDisplayColor(imgSelectedColor))       					
        		            .getScaledInstance(20, 20, Image.SCALE_SMOOTH);        
        Image zoom_out_c = ImageFactory.createImage(CURSOR_ZOOM_OUT
   				           .withDisplayColor(imgColor));
        ImageIcon magOut = new ImageIcon(zoom_out);
        ImageIcon magOutSel = new ImageIcon(zoom_out_sel);
        
        Image yard_stick = ImageFactory.createImage(RULER_IMG
        					.withDisplayColor(imgColor))        					 
        		            .getScaledInstance(25, 20, Image.SCALE_SMOOTH);
        Image yard_stick_sel = ImageFactory.createImage(RULER_IMG_SEL
        					   .withDisplayColor(imgSelectedColor))        					 
        		               .getScaledInstance(25, 20, Image.SCALE_SMOOTH);
        Image yard_stick_c = ImageFactory.createImage(CURSOR_RULER
		                     .withDisplayColor(imgColor));
        ImageIcon ruler = new ImageIcon(yard_stick);
        ImageIcon rulerSel = new ImageIcon(yard_stick_sel);
        
        Image pan_hand = ImageFactory.createImage(PAN_HAND_IMG
        		 		.withDisplayColor(imgColor))        		 		 
        		        .getScaledInstance(18, 20, Image.SCALE_SMOOTH);
        Image pan_hand_sel = ImageFactory.createImage(PAN_HAND_IMG_SEL
        					 .withDisplayColor(imgSelectedColor))        				
        		             .getScaledInstance(18, 20, Image.SCALE_SMOOTH);
        Image pan_hand_c = ImageFactory.createImage(CURSOR_PAN
   				 		   .withDisplayColor(imgColor));
        ImageIcon hand = new ImageIcon(pan_hand);
        ImageIcon handSel = new ImageIcon(pan_hand_sel);
        
        Image look_at = ImageFactory.createImage(INVESTIGATE_IMG
        			    .withDisplayColor(imgColor))				       
	                    .getScaledInstance(23, 20, Image.SCALE_SMOOTH);        		
        Image look_at_sel = ImageFactory.createImage(INVESTIGATE_IMG_SEL
        		  			.withDisplayColor(imgSelectedColor))        		  			       		
        		            .getScaledInstance(23, 20, Image.SCALE_SMOOTH);
        Image look_at_c = ImageFactory.createImage(CURSOR_INVESTIGATE
 		   				 .withDisplayColor(imgColor));
        ImageIcon lookAt = new ImageIcon(look_at);
        ImageIcon lookAtSel = new ImageIcon(look_at_sel);
        
        Image export = ImageFactory.createImage(EXPORT_IMG
        			  .withDisplayColor(imgColor))	  			           		
        		      .getScaledInstance(23, 20, Image.SCALE_SMOOTH);
        Image export_sel = ImageFactory.createImage(EXPORT_IMG_SEL
        		 		   .withDisplayColor(imgSelectedColor)) 			           
        		           .getScaledInstance(23, 20, Image.SCALE_SMOOTH);
        Image export_c = ImageFactory.createImage(CURSOR_EXPORT
		 		   		.withDisplayColor(imgColor));
        ImageIcon exportIcon = new ImageIcon(export);
        ImageIcon exportIconSel = new ImageIcon(export_sel);
        
        Image resize = ImageFactory.createImage(RESIZE_IMG
        		 	   .withDisplayColor(imgColor))        		 	     		
        		       .getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Image resize_sel = ImageFactory.createImage(RESIZE_IMG_SEL
        				  .withDisplayColor(imgSelectedColor)) 		 	                 
        		          .getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon resizeIcon = new ImageIcon(resize);
        ImageIcon resizeIconSel = new ImageIcon(resize_sel);        
               
        // creates cursors to be used in each mode
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Point hotSpot = new Point(0,0);
        
        Cursor ZoomIn = toolkit.createCustomCursor(zoom_in_c, hotSpot, "zoom in");
        Cursor ZoomOut = toolkit.createCustomCursor(zoom_out_c, hotSpot, "zoom out");
        Cursor Measure = toolkit.createCustomCursor(yard_stick_c, hotSpot, "measure");
        Cursor PanHand = toolkit.createCustomCursor(pan_hand_c, hotSpot, "pan");
        Cursor Default = Cursor.getDefaultCursor();
        Cursor Investigate = toolkit.createCustomCursor(look_at_c, hotSpot, "investigate");    
        Cursor Export = toolkit.createCustomCursor(export_c, new Point(10,10), "export");    
        Cursor Resize = toolkit.createCustomCursor(export_c, new Point(10,10), "resize");    // same as export
   
        // creates buttons (and button dimensions) for the toolbar
        zoomIn = new ToolButton(ZOOM_IN, magIn, magInSel, ZoomIn);
        zoomOut = new ToolButton(ZOOM_OUT, magOut, magOutSel, ZoomOut);
        measure = new ToolButton(MEASURE, ruler, rulerSel, Measure);
        panHand = new ToolButton(PAN_HAND, hand, handSel, PanHand);
        selHand = new ToolButton(SEL_HAND, select, selectSel, Default);
        investigate = new ToolButton(INVESTIGATE, lookAt, lookAtSel, Investigate);
        exportButton = new ToolButton(EXPORT, exportIcon, exportIconSel, Export);
        resizeButton = new ToolButton(RESIZE, resizeIcon, resizeIconSel, Resize);
                   
        Dimension buttonDim = new Dimension(35,25);
        Dimension gap = new Dimension(5,0);       
     
        // sets button sizes so they don't change when window changes
        zoomIn.setPreferredSize(buttonDim);
        zoomIn.setMaximumSize(buttonDim);
        zoomIn.setMinimumSize(buttonDim);
        zoomOut.setPreferredSize(buttonDim);
        zoomOut.setMaximumSize(buttonDim);
        zoomOut.setMinimumSize(buttonDim);
        measure.setPreferredSize(buttonDim);
        measure.setMaximumSize(buttonDim);
        measure.setMinimumSize(buttonDim);
        panHand.setPreferredSize(buttonDim);
        panHand.setMaximumSize(buttonDim);
        panHand.setMinimumSize(buttonDim);
        selHand.setPreferredSize(buttonDim);
        selHand.setMaximumSize(buttonDim);
        selHand.setMinimumSize(buttonDim);
        investigate.setPreferredSize(buttonDim);
        investigate.setMaximumSize(buttonDim);
        investigate.setMinimumSize(buttonDim);
        exportButton.setPreferredSize(buttonDim);
        exportButton.setMaximumSize(buttonDim);
        exportButton.setMinimumSize(buttonDim);
        resizeButton.setPreferredSize(buttonDim);
        resizeButton.setMaximumSize(buttonDim);
        resizeButton.setMinimumSize(buttonDim);    

        // adds buttons to ToolManager JPanel    
        // adds buttons to ToolManager JPanel    
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(Box.createRigidArea(new Dimension(74,0)));
        add(selHand);
        add(Box.createRigidArea(gap));
        add(panHand);
        add(Box.createRigidArea(gap));
        add(zoomIn);   
        add(Box.createRigidArea(gap));
        add(zoomOut);
        add(Box.createRigidArea(gap));
        add(measure);
        add(Box.createRigidArea(gap));
        add(investigate);
        add(Box.createRigidArea(gap));
        add(exportButton);
        add(Box.createRigidArea(gap));
        add(resizeButton);
       
        add(Box.createRigidArea(gap));
        add(Box.createRigidArea(gap));
        
        // sets tooltip texts
        zoomIn.setToolTipText("Zoom In   Shift-I");
        zoomOut.setToolTipText("Zoom Out   Shift-O");
        measure.setToolTipText("Measure   Shift-M");
        panHand.setToolTipText("Pan   Shift-P");
        selHand.setToolTipText("Selection   Shift-D");
        investigate.setToolTipText("Investigate    Shift-C");
        exportButton.setToolTipText("Export    Shift-E");
        resizeButton.setToolTipText("Resize Main View    Shift-R");        
    }

    
// Allows the buttons to change the tool mode and respond to that.    
    private class ToolButton extends JButton implements ToolListener
    {
    	private int myMode;
    	private Cursor myCursor;
    	private ImageIcon img;
    	private ImageIcon selImg;
    	
    	
    	ToolButton (int tmode, ImageIcon normal, ImageIcon selected, Cursor csr)
    	{
    		super(normal);
    		img = normal;
    		selImg = selected;
    		myMode = tmode;
    		myCursor = csr;
    		
    		setFocusPainted(false);
    		
    		addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					ToolManager.setToolMode(myMode);
				}
			});
    		
    		ToolManager.addToolListener(this);
    	}
    	
    	public void toolChanged (int newMode)
    	{
    		if (newMode == myMode){
    			setIcon(selImg);
    			if (Main.testDriver != null){
    				Main.testDriver.mainWindow.setCursor(myCursor);
    			}
    		}
    		else{
    			setIcon(img);
    			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    		}
    	}
    	
    	public void paintComponent(Graphics g) {		
    		g.setColor(((ThemeButton)GUITheme.get("button")).getThemebackground());
    		g.fillRect(0, 0, getWidth(), getHeight());
    		setBackground(((ThemeButton)GUITheme.get("button")).getThemebackground());
    		super.paintComponent(g);
    	}
    }

    public static void addToolListener(ToolListener tl){
    	listeners.add(tl);
    }

    public static boolean removeToolListener(ToolListener tl){
    	return listeners.remove(tl);
    }
    
    public static void notifyToolListeners(){
    	for (ToolListener tl: listeners)
    		tl.toolChanged(Mode);
    }
    
    public static void setToolMode(int newMode){
    	Prev = Mode;
    	Mode = newMode;
    	
    	if(newMode == ToolManager.INVESTIGATE){
    		//Display instruction dialog unless indicated in config file
    		if(Config.get("showInvestigateInstructions").equalsIgnoreCase("true")){
    			JCheckBox chkBox = new JCheckBox("Do not show this message again.");
    			int n = displayInvestigatePopup(chkBox);

    			if(n == JOptionPane.OK_OPTION){	//if Okay, check the checkbox to set config variable
    				boolean dontShow = chkBox.isSelected();
    				if(dontShow){
    					Config.set("showInvestigateInstructions", "false");
    				}
    			}else{      			//if cancel, return to previous toolmode
    				Mode = Prev;
    				setToolMode(Mode);
    				return;
    			}
    		}
    		if(InvestigateFactory.getLviewExists() == false){
        		//Create investigate layer if doesn't exist
    			new InvestigateFactory().createLView(true, null);
    		}else{
    			//if it does exist, make sure it's selected when in this tool mode
    			for(LView lv :LManager.getLManager().viewList){
    				if(lv.getName().equalsIgnoreCase("investigate layer")){
    					LManager.getLManager().setActiveLView(lv);
    				}
    			}
    			
    		}
    		LManager.getLManager().repaint();
    		
    		//turn the 3d investigate on
    		ThreeDManager.getInstance().setInvestigateMode(true);
    	}else{
    		//turn 3d investigate off when not in investigate mode
    		ThreeDManager.getInstance().setInvestigateMode(false);
    		//Make sure the InvestigateDisplay is not visible anymore
    		InvestigateDisplay.getInstance().setVisible(false);
    	}
    	notifyToolListeners();
    }
    
    
    private static int displayInvestigatePopup(JCheckBox cb){ 
    	
    	JLabel welcomeMessage = new JLabel("Welcome to the Investigate Tool");
		welcomeMessage.setFont(ThemeFont.getBold().deriveFont(Font.ITALIC, 18));

		JTextArea message = new JTextArea("\n" +
						 "• While in this mode, a display box will follow the\n" +
						 "   cursor around. This display box shows all the\n" +
						 "   information under the cursor at that point on\n" +
						 "   the screen.\n\n" +
						 "• By default the display will show the list view.\n" +
						 "   When numeric data is available (with at least two\n"+
						 "   data points at one spot) switching to the chart\n"+
						 "   view will display a chart. To change views, use\n"+
						 "   the left and right arrow keys.\n\n" +
						 "• Left clicking when a chart is available will save\n"+
						 "   that chart temporarily to the Investigate Layer\n"+
						 "   (which is created and selected whenever the\n"+
						 "   Investigate Tool is selected). These charts can be\n" +
						 "   viewed and exported by opening up the focus\n"+
						 "   panel for the Investigate Layer.\n\n"+
						 "• For more information see the tutorial page:");
		message.setEditable(false);
		message.setBackground(parse(((ThemeTextArea)GUITheme.get("textarea")).getBackground()));
		message.setFont(ThemeFont.getRegular());
		message.setPreferredSize(new Dimension(320, 340));
		
		UrlLabel tutorial = new UrlLabel("  https://jmars.mars.asu.edu/investigate-layer");
		
    	JCheckBox chkBox = cb;
		
		Object[] params = {welcomeMessage, message, tutorial, chkBox};
		return JOptionPane.showConfirmDialog(investigate, params, "Investigate Tool Instructions", JOptionPane.OK_CANCEL_OPTION);
		
    }
   
    public static int getToolMode(){
    	return Mode;
    }
    
    public static int getPrevMode(){
    	return Prev;
    }
    
    public static void setGrabHand(){
    	Image grab_hand = ImageFactory.createImage(CURSOR_PAN_GRAB
   				         .withDisplayColor(imgColor));
		Cursor gh = Toolkit.getDefaultToolkit().createCustomCursor(grab_hand,new Point(0,0),"pandrag");	
		Main.testDriver.mainWindow.setCursor(gh);
	
    }
   
}
