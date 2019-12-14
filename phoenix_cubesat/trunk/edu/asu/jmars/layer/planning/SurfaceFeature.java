package edu.asu.jmars.layer.planning;

public class SurfaceFeature {
	
	private String featureName;
	private double latitude;
	private double longitude;
	
	public SurfaceFeature(String name, double lon, double lat) {
		this.featureName = name;
		this.latitude = lat;
		this.longitude = lon;
	}
	
	public String name() {
		return this.featureName;
	}
	
	public double lat() {
		return this.latitude;
	}
	
	public double lon() {
		return this.longitude;
	}

}
