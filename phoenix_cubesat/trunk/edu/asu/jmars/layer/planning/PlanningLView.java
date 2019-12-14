package edu.asu.jmars.layer.planning;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Set;

import edu.asu.jmars.Main;
import edu.asu.jmars.layer.FocusPanel;
import edu.asu.jmars.layer.Layer.LView;
import edu.asu.jmars.util.HVector;
import edu.asu.jspice.JS;
import edu.asu.jspice.JSNadirProviderNormal;
import edu.asu.jspice.JSScanNormPerpGroundTrack;

public class PlanningLView extends LView {
	
	public final int NAIF_ISS   = -125544;
	public final int NAIF_EARTH = 399;
	public final double EARTH_SIDEREAL_DAY_HOURS = 23.93;
	public static Color trackColor = Color.YELLOW;
	
	public PlanningLView(PlanningLayer layer){
		super(layer);
	}
	
	protected LView _new() {
		// Create a copy of ourself for use in the panner-view.
		return new PlanningLView((PlanningLayer)getLayer());
	}

	protected Object createRequest(Rectangle2D where) {
		// Build a request object for the layer.
		// The layer will respond back with the data.
		return where;
	}

	public void receiveData(Object layerData) {
		// Process the data returned by the layer.
		// Including displaying the data to the screen.
		Rectangle2D r = (Rectangle2D)layerData;
		System.out.println("Layer returned: [x="+r.getMinX()+",y="+r.getMinY()+",w="+r.getWidth()+",h="+r.getHeight()+"]");
	}

	/**
	 * Perform the work that needs to be done before this layer is deleted
	 */
	public void viewCleanup() {
		if (getChild()==null) return;  // Only do this work for the MainLView, not for the panner
		
		// Unload SPICE kernels
		PlanningKernels.getInstance().unloadAllKernels();
	}
	
	public String getName() {
		return "Planning Layer";
	}

	/**
	 * @return the focus panel for this lview which will be created if necessary
	 */
	public FocusPanel getFocusPanel(){
		if(focusPanel == null){
			//for panner
			if(getChild()==null){
				focusPanel = getParentLView().getFocusPanel();
			}else{
				focusPanel = new PlanFocusPanel(this);
			}
		}
		return focusPanel;
	}
	
	
	
    ArrayList<Point2D> groundTrackPoints = new ArrayList<Point2D>();
    
	private void drawGroundTracks(Graphics g2) {
		
		JS myJS = new JS(NAIF_ISS, NAIF_EARTH, EARTH_SIDEREAL_DAY_HOURS, new JSNadirProviderNormal(), new JSScanNormPerpGroundTrack());
		if (groundTrackPoints.size()==0) {
		    double startEt = ((PlanningLayer)getLayer()).getStartET();
		    double endEt = ((PlanningLayer)getLayer()).getEndET();
		    
			int stepSizeSeconds = 10;
			for (double et=startEt; et<endEt; et+=stepSizeSeconds) {
				HVector positionV = new HVector();
				HVector velocityV = new HVector(); // not used
				myJS.spkez(et, NAIF_ISS, positionV, velocityV);
                HVector nadirVector = positionV.neg();
 
				
	            HVector vSurface = myJS.surfpt(positionV, nadirVector, JS.getRadii(NAIF_EARTH));

				Point2D spatialPt = new Point2D.Double(vSurface.lon(), vSurface.lat());
				
				groundTrackPoints.add(spatialPt);				
			}
		}

		double lastx=Double.MAX_VALUE, lasty=Double.MAX_VALUE;
		
		g2.setColor(trackColor);
		
		for (Point2D spatialPt : groundTrackPoints) {
			
			Point2D worldPt = Main.PO.convSpatialToWorld(spatialPt);
			double x = worldPt.getX();
			double y = worldPt.getY();
			if (lastx != Double.MAX_VALUE) {
				Line2D line = new Line2D.Double(lastx, lasty, x, y);
				((Graphics2D)g2).draw(line);
//				g2.drawLine((int)lastx, (int)lasty, (int)(x), (int)(y));
			} 				
		
			lastx=x;
			lasty=y;
		}

	}

	/**
	 * Draws this components data into the provided Graphics g object
	 * 
	 * @param Graphics
	 */
	public void paintComponent(Graphics g) {
		// Don't do this for the panner
		if (getChild()==null) return;
		
		// TODO: We should probably be smarter about caching versus clearing
		clearOffScreen();
		Graphics2D g2=getOffScreenG2();
		
		if (g2==null) return;
		
		PlanningLayer myLayer = (PlanningLayer)getLayer();
		
//		Set<Camera> showingCameras = PlanningSettings.whichCamerasAreShowing();
//		
//		for (PlannedCommand command : ((PlanLayer)getLayer()).getCommands()) {
//			if (command.isImage()) {
//				Camera camera = command.getCamera();
//				
//				if (showingCameras.contains(camera)){
//					
//					g2.setColor(PlanningSettings.getCameraColor(camera));
//					
//					// TODO: Draw selected observations last, all in one pass, so that they're always on top of non-selected observations
//					if (myLayer.getSelectedCommands().contains(command)) {
//						g2.setColor(Color.WHITE);
//					}
//					
//					//This is thicker so that the it can still be seen if the original
//					// is drawn over top at the same place. (the kernel hasn't changed)
//					g2.setStroke(new BasicStroke(0));
//					
//					Shape s = command.getPath();
//					g2.draw(s);
//				}
//			}
//		}
//		
//		for(Objective o : ((PlanningLayer)getLayer()).getObjectives()){
//			//draw the original paths, if the objective has any (was loaded from the db)
//			// and if the view setting is to yes, to show the old footprint
//			for(Shape orgPath : o.getOrgPaths()){
//				if(orgPath != null && PlanningSettings.showOldFootprints){
//					g2.setStroke(new BasicStroke(0));
//					g2.setColor(PlanningSettings.getOldFootprintColor());
//					g2.draw(orgPath);
//				}
//			}
//		}
		
		g2.setStroke(new BasicStroke(0));

		drawGroundTracks(g2);

		super.paintComponent(g);

	}
	
	@Override
	public String getToolTipText(MouseEvent event) {
		Point2D spatialPoint = getProj().screen.toSpatial(event.getPoint());
		return "Lon: " + String.format("%.3f", 360-spatialPoint.getX()) + " | Lat: " + String.format("%.3f",spatialPoint.getY());
	}
	
}
