package edu.asu.jmars.layer.planning;

import edu.asu.jmars.layer.DataReceiver;
import edu.asu.jmars.layer.Layer;

public class PlanningLayer extends Layer {

	private double startEt;
	private double endEt;
	private String name = "";
	private final PlanningConfigData missionConfigData;

	public void receiveRequest(Object layerRequest, DataReceiver requester) {
		broadcast(layerRequest);
	}
	
	public PlanningLayer(double startingEt, double endingEt) {
		this.startEt = startingEt;
		this.endEt   = endingEt;
		this.missionConfigData = PlanningConfigDataPHX.getInstance();
	}
	
	public double getStartET() {
		return startEt;
	}
	
	public double getEndET() {
		return endEt;
	}

	public void setName(String planName) {
		name = planName;
	}
	
	public String getName() {
		return name;
	}
	
	public PlanningConfigData getMissionData() {
		return this.missionConfigData;
	}

}
