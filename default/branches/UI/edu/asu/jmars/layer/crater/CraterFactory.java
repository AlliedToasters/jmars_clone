package edu.asu.jmars.layer.crater;

import edu.asu.jmars.layer.LManager;
import edu.asu.jmars.layer.LViewFactory;
import edu.asu.jmars.layer.LayerParameters;
import edu.asu.jmars.layer.SerializedParameters;
import edu.asu.jmars.layer.Layer.LView;

public class CraterFactory extends LViewFactory {
	public CraterFactory(){
		type = "crater_counting";
	}
	
	public LView createLView() {
		return null;
	}

	public void createLView(boolean async, LayerParameters l) {
		// Create LView with defaults
		CraterLayer layer = new CraterLayer();
		CraterLView3D lview3d = new CraterLView3D(layer);
		CraterLView lview = new CraterLView(layer, lview3d);
		lview.setLayerParameters(l);
		lview.originatingFactory = this;
		LManager.receiveNewLView(lview);
	}

	public LView recreateLView(SerializedParameters parmBlock) {
		CraterLayer craterLayer;
		
        if (parmBlock != null &&
                parmBlock instanceof CraterSettings) {
                CraterSettings settings = (CraterSettings) parmBlock;
                craterLayer = new CraterLayer(settings);
        } else {
        	craterLayer = new CraterLayer();
        }
	
        CraterLView3D lview3d = new CraterLView3D(craterLayer);
		CraterLView lview = new CraterLView(craterLayer, lview3d);
		lview.setLayerParameters(((CraterSettings)parmBlock).myLP);
		lview.originatingFactory = this;
		return lview;
	}

	public String getDesc() {
		return "Crater Counting";
	}

	public String getName() {
		return "Crater Counting";
	}

}
