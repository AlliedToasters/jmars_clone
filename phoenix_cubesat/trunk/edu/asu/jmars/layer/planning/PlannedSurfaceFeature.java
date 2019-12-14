/**
 * 
 */
package edu.asu.jmars.layer.planning;

/**
 * <description>
 *
 * <intended usage>
 *
 * <external dependencies>
 *
 * <multi-thread warning>
 */
public class PlannedSurfaceFeature extends SurfaceFeature {

	
	private double plannedTime;
	
	public PlannedSurfaceFeature(String name, double lon, double lat, double et) {
        super(name, lon, lat);
        plannedTime = et;
	}
	
	public double getPlannedEt() {
		return this.plannedTime;
	}

	
	
}
