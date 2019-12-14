package edu.asu.jmars.layer.planning;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Set;

import edu.asu.jmars.Main;
import edu.asu.jmars.layer.FocusPanel;
import edu.asu.jmars.layer.Layer.LView;
import edu.asu.jmars.layer.obs.osirisrex.Camera;
import edu.asu.jmars.layer.obs.osirisrex.Objective;
import edu.asu.jmars.layer.obs.osirisrex.PlanFocusPanel;
import edu.asu.jmars.layer.obs.osirisrex.PlanLayer;
import edu.asu.jmars.layer.obs.osirisrex.PlannedCommand;
import edu.asu.jmars.layer.obs.osirisrex.PlanningLayer;
import edu.asu.jmars.layer.obs.osirisrex.PlanningSettings;
import edu.asu.jmars.layer.obs.util.InstrumentFov;
import edu.asu.jmars.layer.obs.util.SpiceKernelManager;
import edu.asu.jmars.util.HVector;

public class PlanningLView extends LView {
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
		if (groundTrackPoints.size()==0) {
		    double startEt = ((PlanLayer)getLayer()).getStartET();
		    double endEt = ((PlanLayer)getLayer()).getEndET();
		    
			int stepSize = 1000;
			for (double et=startEt; et<endEt; et+=stepSize) {
			    int dskHandle = SpiceKernelManager.getInstance().getDskHandle();			    
				InstrumentFov fov = new InstrumentFov(Camera.POLYCAM.getNaifId(), PlanningLayer.getTargetBody(), et, dskHandle, "SUN", 0.0, 1);
		
				HVector boresight = fov.getBoresight();
				Point2D spatialPt = new Point2D.Double(boresight.lon(), boresight.lat());
				
				groundTrackPoints.add(spatialPt);				
			}
		}

		double lastx=Double.MAX_VALUE, lasty=Double.MAX_VALUE;
		
		g2.setColor(PlanningSettings.trackColor);
		
		for (Point2D spatialPt : groundTrackPoints) {
			
			Point2D worldPt = Main.PO.convSpatialToWorld(spatialPt);
			double x = worldPt.getX();
			double y = worldPt.getY();
			if (lastx != Double.MAX_VALUE) {
				g2.drawLine((int)lastx, (int)lasty, (int)(x), (int)(y));
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
		
		PlanLayer myLayer = (PlanLayer)getLayer();
		
		Set<Camera> showingCameras = PlanningSettings.whichCamerasAreShowing();
		
		for (PlannedCommand command : ((PlanLayer)getLayer()).getCommands()) {
			if (command.isImage()) {
				Camera camera = command.getCamera();
				
				if (showingCameras.contains(camera)){
					
					g2.setColor(PlanningSettings.getCameraColor(camera));
					
					// TODO: Draw selected observations last, all in one pass, so that they're always on top of non-selected observations
					if (myLayer.getSelectedCommands().contains(command)) {
						g2.setColor(Color.WHITE);
					}
					
					//This is thicker so that the it can still be seen if the original
					// is drawn over top at the same place. (the kernel hasn't changed)
					g2.setStroke(new BasicStroke(0));
					
					Shape s = command.getPath();
					g2.draw(s);
				}
			}
		}
		
		for(Objective o : ((PlanningLayer)getLayer()).getObjectives()){
			//draw the original paths, if the objective has any (was loaded from the db)
			// and if the view setting is to yes, to show the old footprint
			for(Shape orgPath : o.getOrgPaths()){
				if(orgPath != null && PlanningSettings.showOldFootprints){
					g2.setStroke(new BasicStroke(0));
					g2.setColor(PlanningSettings.getOldFootprintColor());
					g2.draw(orgPath);
				}
			}
		}
		
		g2.setStroke(new BasicStroke(0));

		drawGroundTracks(g2);

		super.paintComponent(g);

	}

}
