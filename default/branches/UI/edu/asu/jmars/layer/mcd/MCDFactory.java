package edu.asu.jmars.layer.mcd;


import edu.asu.jmars.layer.LManager;
import edu.asu.jmars.layer.LViewFactory;
import edu.asu.jmars.layer.Layer.LView;
import edu.asu.jmars.layer.LayerParameters;
import edu.asu.jmars.layer.SerializedParameters;

public class MCDFactory extends LViewFactory{

	public MCDFactory() {
		type = "mcd_point";
	}
	
	/**
	 * Is called from the default block in the AddLayer dialog
	 * in the createButton method
	 */
	public void createLView(boolean async, LayerParameters lp){
		MCDLView lview = buildLView();
		lview.setLayerParameters(lp);
		LManager.receiveNewLView(lview);
	}
	
	private MCDLView buildLView(){
		MCDLayer layer = new MCDLayer();
		MCDLView3D lview3d = new MCDLView3D(layer);
		MCDLView lview = new MCDLView(layer, true, lview3d);
		lview.originatingFactory = this;
		
		return lview;
	}
	
	
	/**
	 * Called when restoring sessions
	 */
	public LView recreateLView(SerializedParameters parmBlock) {
		return null;
	}

}
