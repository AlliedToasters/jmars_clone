package edu.asu.jmars.layer.mcdslider;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.asu.jmars.layer.FocusPanel;
import edu.asu.jmars.swing.BoundsPopupMenuListener;
import edu.asu.jmars.util.Util;

/**
 * 
 * @author srcheese
 *
 */
public class MCDSliderFocusPanel extends FocusPanel {
	private MCDSliderLView myLView;
	private MCDSliderLayer myLayer;
	
	private JComboBox<String> typeBx;
	private JSlider hourSld;
	private JSlider lsSld;
	private JSlider elevSld;
	private JLabel hourLbl;
	private JLabel lsLbl;
	private JLabel elevLbl;
	private final String HOUR_PROMPT = "Hour: ";
	private final String LS_PROMPT = "Ls: ";
	private final String ELEV_PROMPT = "Elevation: ";
	private int lsStep = 10;
	private int lsMax = 360-lsStep;

	
	private final String TYPE_TEMP = "Temperature";
	private final String TYPE_PRESSURE = "Atmospheric Pressure";
	private final String TYPE_Z_WIND = "Zonal Wind";
	private final String TYPE_RHO = "Atmospheric Density (Rho)";
	private final String TYPE_WATER_VAP = "Water Vapor";
	private final String TYPE_WATER_ICE = "Water Ice";
	private final String TYPE_DUST_COL = "Dust Column";
	private final String TYPE_DUST_DEP = "Dust Deposition";
	private final String TYPE_DUST_ERAD = "Radius of Dust Particle";
	private final String TYPE_SFLUX_SPC = "Solar Flux Reflected to Space";
	private final String TYPE_SFLUX_SURF = "Solar Flux to Surface";
	private final String TYPE_TFLUX_SPC = "Thermal Flux from Surface to Space";
	private final String TYPE_TFLUX_SURF = "Thermal Flux from Space to Surface";
	private final String TYPE_V_WIND = "Meridional Wind";
	
	/* Used to map between the string sin the type box, and the string
	 * used on disc to store the map */
	private HashMap<String, String> typeToString;
	
	private HashMap<Integer, Integer> valueToElevation;
	private DecimalFormat df = new DecimalFormat("#,###,###");

	//parts to the map path
	private String typeStr;
	private String lsStr = "ls_";
	private String altStr = "alt_";
	private String ltStr = "lt_";
	private String lsVal;
	private String altVal;
	private String ltVal;
	
	//UI constants
	private int row = 0;
	private int col = 0;
	private int pad = 2;
	private Insets in = new Insets(pad,pad,pad,pad);
	
	
	public MCDSliderFocusPanel(MCDSliderLView parent) {
		super(parent, false);
		myLView = parent;
		myLayer = (MCDSliderLayer)myLView.getLayer();
		
		typeToString = new HashMap<String, String>();
		typeToString.put(TYPE_TEMP, "temp");
		typeToString.put(TYPE_PRESSURE, "pressure");
		typeToString.put(TYPE_Z_WIND, "zwind");
		typeToString.put(TYPE_RHO, "rho");
		typeToString.put(TYPE_WATER_VAP, "h20_vap");
		typeToString.put(TYPE_WATER_ICE, "h20_ice");
		typeToString.put(TYPE_DUST_COL, "dust_col");
		typeToString.put(TYPE_DUST_DEP, "dust_dep");
		typeToString.put(TYPE_DUST_ERAD, "dust_erad");
		typeToString.put(TYPE_SFLUX_SPC, "sflux_spc");
		typeToString.put(TYPE_SFLUX_SURF, "sflux_surf");
		typeToString.put(TYPE_TFLUX_SPC, "tflux_spc");
		typeToString.put(TYPE_TFLUX_SURF, "tflux_surf");
		typeToString.put(TYPE_V_WIND, "vwind");
		
		add("Data", createDataPanel());
	}
	
	private JPanel createDataPanel(){
		JPanel innerPnl = new JPanel();
		innerPnl.setBorder(new TitledBorder("Controls"));
		innerPnl.setLayout(new GridBagLayout());
		
		JLabel typeLbl = new JLabel("Display Type: ");
		Vector<String> typeVec = new Vector<String>();
		//TODO: what's the best way to populate these options?
		typeVec.add(TYPE_TEMP);
		typeVec.add(TYPE_PRESSURE);
		typeVec.add(TYPE_Z_WIND);
		typeVec.add(TYPE_RHO);
		typeVec.add(TYPE_WATER_VAP);
		typeVec.add(TYPE_WATER_ICE);
		typeVec.add(TYPE_DUST_COL); 
		typeVec.add(TYPE_DUST_DEP);
		typeVec.add(TYPE_DUST_ERAD);
		typeVec.add(TYPE_SFLUX_SPC);
		typeVec.add(TYPE_SFLUX_SURF);
		typeVec.add(TYPE_TFLUX_SPC);
		typeVec.add(TYPE_TFLUX_SURF);
		typeVec.add(TYPE_V_WIND);
		//TODO: how do we know units?  Should we display that somewhere in the UI? 
		typeBx = new JComboBox<String>(typeVec);
		typeBx.addActionListener(typeListener);
		
	    //set a preferred size, and popup listener so the width can be shorter than the contents
		BoundsPopupMenuListener cbListener = new BoundsPopupMenuListener(true, false);
	    typeBx.setPreferredSize(new Dimension(120, typeBx.getPreferredSize().height));
	    typeBx.setMinimumSize(new Dimension(120, typeBx.getPreferredSize().height));
	    typeBx.addPopupMenuListener(cbListener);
		
		hourLbl = new JLabel(HOUR_PROMPT);
		lsLbl = new JLabel(LS_PROMPT);
		elevLbl = new JLabel(ELEV_PROMPT);
		//start all sliders at 0?
		hourSld = new JSlider(0, 23);
		hourSld.addChangeListener(sliderListener);
		hourSld.setValue(0);
		lsSld = new JSlider(0, lsMax/lsStep);
		lsSld.addChangeListener(sliderListener);
		lsSld.setValue(0);
		//populate the map for the elevation slider
		valueToElevation = new HashMap<Integer, Integer>();
		int i = 0;
		valueToElevation.put(i++, 0);
		valueToElevation.put(i++, 1);
		valueToElevation.put(i++, 10);
		valueToElevation.put(i++, 100);
		valueToElevation.put(i++, 1000);
		valueToElevation.put(i++, 5000);
		valueToElevation.put(i++, 10000);
		valueToElevation.put(i++, 20000);
		valueToElevation.put(i++, 40000);
		valueToElevation.put(i++, 60000);
		valueToElevation.put(i++, 80000);
		valueToElevation.put(i, 100000);
		elevSld = new JSlider(0, i);
		elevSld.addChangeListener(sliderListener);
		elevSld.setValue(0);
		
		//set preferred slider size to be pretty narrow
		int h = hourSld.getPreferredSize().height;
		int w = hourSld.getMinimumSize().width;
		Dimension sliderDim = new Dimension(w, h);
		hourSld.setPreferredSize(sliderDim);
		lsSld.setPreferredSize(sliderDim);
		elevSld.setPreferredSize(sliderDim);
		
		row = 0; col = 0;
		innerPnl.add(typeLbl, new GridBagConstraints(col, row, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, in, pad, pad));
		innerPnl.add(typeBx, new GridBagConstraints(++col, row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, in, pad, pad));
		//this row is just to provide a little bit of vertical spacing, and 
		// ensure the first column is at least as wide as the widest elevation label
		col = 0; row++;
		JLabel lbl = new JLabel(ELEV_PROMPT+valueToElevation.get((i)));
		innerPnl.add(Box.createHorizontalStrut(lbl.getPreferredSize().width+5), new GridBagConstraints(col, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
		innerPnl.add(Box.createVerticalStrut(15), new GridBagConstraints(++col, row, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, in, pad, pad));
		col = 0; row++;
		innerPnl.add(hourLbl, new GridBagConstraints(col, row, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, in, pad, pad));
		innerPnl.add(hourSld, new GridBagConstraints(++col, row, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, in, pad, pad));
		col = 0; row++;
		innerPnl.add(lsLbl, new GridBagConstraints(col, row, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, in, pad, pad));
		innerPnl.add(lsSld, new GridBagConstraints(++col, row, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, in, pad, pad));
		col = 0; row++;
		innerPnl.add(elevLbl, new GridBagConstraints(col, row, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, in, pad, pad));
		innerPnl.add(elevSld, new GridBagConstraints(++col, row, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, in, pad, pad));
		
		JPanel panel = new JPanel();
		panel.setBackground(Util.lightBlue);
		panel.setLayout(new BorderLayout());
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(innerPnl, BorderLayout.NORTH);
		
		return panel;
	}
	
	private ChangeListener sliderListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			Object source = e.getSource();
			
			Integer value = null;
			JLabel label = null;
			String prompt = null;
			//update the proper label
			if(source.equals(hourSld)){
				value = getHourVal();
				label = hourLbl;
				prompt = HOUR_PROMPT;
			}
			else if(source.equals(lsSld)){
				value = getLsVal();
				label = lsLbl;
				prompt = LS_PROMPT;
			}
			else if(source.equals(elevSld)){
				value = getElevVal();
				label = elevLbl;
				prompt = ELEV_PROMPT;
			}
			
			//set the label, and update the map if needed
			if(value != null && label != null && prompt != null){
				label.setText(prompt+df.format(value));
				
				//if all the sliders are done moving, and are not null, pull new map
				if(lsSld!=null && hourSld!=null && elevSld!=null ){
//						&& !lsSld.getValueIsAdjusting() && !hourSld.getValueIsAdjusting() && !elevSld.getValueIsAdjusting()){
					setMapPath();
				}
			}
			
		}
	};
	
	private ActionListener typeListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			//if all the sliders are not null, pull new map
			if(lsSld!=null && hourSld!=null && elevSld!=null ){
				setMapPath();
			}
		}
	};
	
	private int getHourVal(){
		return hourSld.getValue();
	}
	
	private int getLsVal(){
		return lsSld.getValue()*lsStep;
	}
	
	private int getElevVal(){
		return valueToElevation.get(elevSld.getValue());
	}
	
	
	private void setMapPath(){
		//set the vals
		typeStr = typeToString.get((String)typeBx.getSelectedItem());
		lsVal = getLsVal()+"";
		altVal = getElevVal()+"";
		ltVal = getHourVal()+"";
		
		//path is /mars/storage2/mcd_maps/[typeStr]/[lsStr][lsVal]/[altStr][altVal]/[typeStr]_[lsStr][lsVal]_[altStr][altVal]_[ltStr][ltVal]_s1_v1.tiff
		//String startPath = "/mars/storage2/mcd_maps/";
		String fileName = typeStr+"_"+lsStr+lsVal+"_"+altStr+altVal+"_"+ltStr+ltVal+"_s1_v1.tiff";
		
		String fullPath = myLayer.getVersionString()+"/"+typeStr+"/"+lsStr+lsVal+"/"+altStr+altVal+"/"+fileName;
		
//		System.out.println("setting path: "+fullPath);
		
		myLayer.setPath(fullPath);
		//update the lview
		myLView.repaint();
	}
	
	public String getTypeString(){
		return typeStr;
	}

}
