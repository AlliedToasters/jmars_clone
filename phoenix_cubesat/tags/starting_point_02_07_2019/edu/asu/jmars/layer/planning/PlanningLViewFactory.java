package edu.asu.jmars.layer.planning;

import edu.asu.jmars.Main;
import edu.asu.jmars.layer.LManager;
import edu.asu.jmars.layer.LViewFactory;
import edu.asu.jmars.layer.SerializedParameters;
import edu.asu.jmars.layer.Layer.LView;
import edu.asu.jmars.layer.LayerParameters;

public class PlanningLViewFactory extends LViewFactory {

	public PlanningLViewFactory() {
        type = "PlanningLayer";
	}
	public LView createLView() {
		return null;
	}

	public void createLView(boolean async, LayerParameters l) {
		
		boolean kernelsLoaded = PlanningKernels.getInstance().chooseAndLoadMetaKernel(Main.mainFrame);
		if (kernelsLoaded) {
			// Create LView with defaults
			PlanningLView lview = new PlanningLView(new PlanningLayer());
			lview.originatingFactory = this;
			LManager.receiveNewLView(lview);			
		} else {
			// show error dialog TODO
		}
		
	}

	public LView recreateLView(SerializedParameters parmBlock) {
		PlanningLView lview = new PlanningLView(new PlanningLayer());
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
