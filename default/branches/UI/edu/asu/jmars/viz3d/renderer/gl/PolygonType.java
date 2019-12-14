package edu.asu.jmars.viz3d.renderer.gl;

/**
 * Enumeration of PolygonType
 *
 * thread-safe
 */
public enum PolygonType {
    OnBody,
    OffBody,
    OnOffBody;

	/**
	 * Returns the ordinal value of the Enumerated type
	 *
	 * @return the value of the type
	 *
	 * thread-safe
	 */
    public int getValue() {
        return this.ordinal();
    }

	/**
	 * Returns the Enumerated type that maps to the input ordinal value
	 *
	 * @param value
	 * @return Enumerated Type
	 *
	 * thread-safe
	 */
    public static PolygonType forValue(int value) {
        return values()[value];
    }
}