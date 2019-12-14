package edu.asu.jmars.layer.planning;

import java.util.ArrayList;

public class SavedPlan {

	private String planName;
	private double startEt;
	private double endEt;
	private String metaKernelPath;
	private ArrayList<PlannedSurfaceFeature> plannedSurfaceTargets;
	private ArrayList<PlannedSurfaceFeature> plannedGroundStations;
	
	/**
	 * Use this constructor to create the plan from a saved file
	 * 
	 * @param savedPlanPath the full file path to a previously-saved plan
	 */
	public SavedPlan(String savedPlanPath) {
		// code goes here to load the plan from a text file and construct the object
	}
	
	
	/**
	 * Use this constructor to create a plan object to be saved.
	 * 
	 * @param name        the plan name
	 * @param start       the starting et
	 * @param end         the ending et
	 * @param metaKernel  the full path to the metakernel file
	 * @param targets     the list of planned targets
	 * @param stations    the list of planned groundstation flyovers
	 */
	public SavedPlan(String name, double start, double end, String metaKernel, ArrayList<PlannedSurfaceFeature> targets, ArrayList<PlannedSurfaceFeature> stations) {
		planName = name;
		startEt = start;
		endEt = end;
		metaKernelPath = metaKernel;
		plannedSurfaceTargets = targets;
		plannedGroundStations = stations;
	}
	
	public boolean save() {
		// code goes here to save plan to file
		return true;
	}


	/**
	 * @return the planName
	 */
	public String getPlanName() {
		return planName;
	}


	/**
	 * @return the startEt
	 */
	public double getStartEt() {
		return startEt;
	}


	/**
	 * @return the endEt
	 */
	public double getEndEt() {
		return endEt;
	}


	/**
	 * @return the metaKernelPath
	 */
	public String getMetaKernelPath() {
		return metaKernelPath;
	}


	/**
	 * @return the plannedSurfaceTargets
	 */
	public ArrayList<PlannedSurfaceFeature> getPlannedSurfaceTargets() {
		return plannedSurfaceTargets;
	}


	/**
	 * @return the plannedGroundStations
	 */
	public ArrayList<PlannedSurfaceFeature> getPlannedGroundStations() {
		return plannedGroundStations;
	}
}
