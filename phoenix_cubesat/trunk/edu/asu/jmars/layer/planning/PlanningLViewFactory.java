package edu.asu.jmars.layer.planning;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import edu.asu.jmars.Main;
import edu.asu.jmars.layer.LManager;
import edu.asu.jmars.layer.LViewFactory;
import edu.asu.jmars.layer.SerializedParameters;
import edu.asu.jmars.layer.Layer.LView;
import edu.asu.jmars.layer.LayerParameters;
import edu.asu.jspice.SpiceException;

public class PlanningLViewFactory extends LViewFactory {

	double startEt;
	double endEt;
	
	public PlanningLViewFactory() {
        type = "PlanningLayer";
	}
	public LView createLView() {
		return null;
	}

	public void createLView(boolean async, LayerParameters l) {
		
		// TODO add code here to ensure only one planning payer at a time
		
		Boolean newPlan = displayNewOrLoadDialog();
		
		if (newPlan == null) {
			return;
		} else if (newPlan) {
    		boolean kernelsLoaded = PlanningKernels.getInstance().chooseAndLoadMetaKernel(Main.mainFrame);
    		boolean rangeNotEntered = true;
    		while (rangeNotEntered) {
    			try {
    				displayPlanningLayerDialog();
    				rangeNotEntered = false;
    			} catch (Exception e) {
    
    			}
    		}
    		if (kernelsLoaded) {
    			// Create LView with defaults
    			PlanningLView lview = new PlanningLView(new PlanningLayer(startEt, endEt));
    			lview.originatingFactory = this;
    			LManager.receiveNewLView(lview);			
    		} else {
    			System.out.println("Unable to load kernels. Layer aborted");
    		}
		} else {
			// Code to load existing plan...
		}
	}

	/**
	 * Displays dialog box with choice of Create New Plan, Load Existing Plan, or CANCEL.
	 * 
	 * Returns TRUE if use asks for New Plan, FALSE for Load Existing, and returns NULL if
	 * Cancel was selected.
	 */
	private Boolean displayNewOrLoadDialog() {
		
		Boolean newPlan = true;
		final JPanel panel = new JPanel();
		String[] choices = {"Create New Plan", "Load Existing Plan", "Cancel"};
		
		Object selected = JOptionPane.showOptionDialog(null, "New or Existing Plan?", null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[2]);
		if (selected.toString().equals("2")) {  // Cancel
			return null;
		} else if (selected.toString().equals("1")) {   // Load Existing Plan
			newPlan = false;
		} else {
			newPlan = true;
		}
		return newPlan;
	}
	
	private void displayPlanningLayerDialog() throws SpiceException {
	      JTextField startUtcField = new JTextField(20);
	      JTextField endUtcField = new JTextField(20);

	      JPanel myPanel = new JPanel();
	      myPanel.add(new JLabel("Plan Start UTC:"));
	      myPanel.add(startUtcField);
	      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
	      myPanel.add(new JLabel("Plan End UTC:"));
	      myPanel.add(endUtcField);

	      int result = JOptionPane.showConfirmDialog(null, myPanel, 
	               "Add Planning Layer: Enter Time Range", JOptionPane.OK_CANCEL_OPTION);
	      if (result == JOptionPane.OK_OPTION) {
	    	  this.startEt = TimeUtil.utcToET(startUtcField.getText());
	    	  this.endEt = TimeUtil.utcToET(endUtcField.getText());
	      }
		
	}
	public LView recreateLView(SerializedParameters parmBlock) {
		PlanningLView lview = new PlanningLView(new PlanningLayer(startEt, endEt));
		lview.originatingFactory = this;
		return lview;
	}

	public String getDesc() {
		return "Planning LView";
	}

	public String getName() {
		return "Planning LView";
	}

}
