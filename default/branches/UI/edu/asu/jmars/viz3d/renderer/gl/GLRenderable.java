package edu.asu.jmars.viz3d.renderer.gl;

import com.jogamp.opengl.GL2;

/**
 *	Interface for JOGL renderable object actions that need to be executed on the current JOGL context. 
 *
 */
public interface GLRenderable {
	
	/**
	 * Method used by the renderer to render the object
	 *
	 * @param gl current JOGL context
	 */
	public void execute(GL2 gl);
	
	/**
	 * Method to define any JOGL/OpenGL specific preparation that will be required for rendering
	 *
	 * @param gl current JOGL context
	 */
	public void preRender(GL2 gl);

	/**
	 * Method to define any JOGL/OpenGL specific post-rendering operations
	 *
	 * @param gl current JOGL context
	 */
	public void postRender(GL2 gl);

	/**
	 * Method to release and JOGL/OpenGL specific resources held by the object
	 *
	 * @param gl current JOGL context
	 */
	public void delete(GL2 gl);

	/**
	 * Method to return the unique ID of the object
	 * Recommended to return the System defined ID
	 *
	 * @return int id
	 * thread-safe
	 */
	public int getId();
	
	/**
	 * Method to return the object's opacity value
	 *
	 * @return float opacity value between 0 and 1.0
	 * thread-safe
	 */
	public float getAlpha();
	
	/**
	 * Method to return the combined opacity value (object and layer)
	 *
	 * @return float combined opacity value
	 *
	 * thread-safe
	 */
	public float getDisplayAlpha();
	
	/**
	 * Method to set the combined opacity value (product of object and layer normalized)
	 *
	 * @param alpha combined opacity value
	 *
	 * not thread-safe
	 */
	public void setDisplayAlpha(float alpha);
	
	/**
	 * Describes whether a GLRenderable can be meaningfully rescaled.
	 *
	 * @return true if scaling will affect the size of the object when rendered.
	 */
	public boolean isScalable();
	
	/**
	 * This method allows down-scaling by a scalar value.
	 * This method should only be used if isScalable returns true.
	 *
	 * @param scalar value to scale down all the objects vertices
	 */
	public void scaleByDivision(float scalar);
}
