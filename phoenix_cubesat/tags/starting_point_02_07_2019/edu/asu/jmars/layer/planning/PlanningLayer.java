package edu.asu.jmars.layer.planning;

import edu.asu.jmars.layer.DataReceiver;
import edu.asu.jmars.layer.Layer;

public class PlanningLayer extends Layer {

	public void receiveRequest(Object layerRequest, DataReceiver requester) {
		broadcast(layerRequest);
	}

}
