package edu.asu.jmars.layer.planning;

import java.util.List;

/**
 * 
 * This interface is intended to contain configurable data for a particular Earth
 * Mission by a small sat or Cubesat.
 * 
 * Only one class can implement this per version of JMARS.
 * 
 * Among the data to be available are:
 * 
 *   * Targets on the Earth's surface to be imaged
 *   * Ground stations with which to uplink/downlink data
 *   * Spacecraft Commands available for plan creation 
 *   
 */
public interface PlanningConfigData {
	
	public void initializeConfigData();
		
	public String getMissionName();
	
	public List<SurfaceFeature> getTargets();

	public List<SurfaceFeature> getGroundStations();
	
	public List<SpacecraftCommand> getCommands();
	
	
}
