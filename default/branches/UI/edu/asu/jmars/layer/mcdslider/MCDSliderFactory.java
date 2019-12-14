package edu.asu.jmars.layer.mcdslider;

import edu.asu.jmars.layer.LManager;
import edu.asu.jmars.layer.LViewFactory;
import edu.asu.jmars.layer.LayerParameters;
import edu.asu.jmars.layer.Layer.LView;
import edu.asu.jmars.layer.SerializedParameters;

/**
 * 
 * @author srcheese
 *
 */
public class MCDSliderFactory extends LViewFactory{
	
	public MCDSliderFactory(){
		type = "mcd_slider";
	}
	
	/**
	 * Is called from the default block in the AddLayer dialog
	 * in the createButton method
	 */
	public void createLView(boolean async, LayerParameters lp){
		//get the version string to pass to the layer
		String vStr = lp.options.get(0);
		MCDSliderLayer layer = new MCDSliderLayer(vStr);
		MCDSliderLView lview = new MCDSliderLView(layer);
		lview.originatingFactory = this;
		lview.setLayerParameters(lp);
		LManager.receiveNewLView(lview);
	}

	@Override
	public LView recreateLView(SerializedParameters parmBlock) {
		// TODO Auto-generated method stub
		return null;
	}

}
