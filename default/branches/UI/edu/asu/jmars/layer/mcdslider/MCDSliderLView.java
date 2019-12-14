package edu.asu.jmars.layer.mcdslider;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import edu.asu.jmars.Main;
import edu.asu.jmars.layer.InvestigateData;
import edu.asu.jmars.layer.Layer;
import edu.asu.jmars.layer.MultiProjection;
import edu.asu.jmars.layer.Layer.LView;
import edu.asu.jmars.util.Util;

/**
 * 
 * @author srcheese
 *
 */
public class MCDSliderLView extends LView {

	private MCDSliderFocusPanel myFocusPanel;
	private MCDSliderLayer myLayer;
	
	public MCDSliderLView(Layer layerParent) {
		super(layerParent);
		
		myLayer = (MCDSliderLayer) layerParent;
	}
	
	public String getName(){
		return "MCD Map Slider Layer";
	}

	@Override
	protected Object createRequest(Rectangle2D where) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receiveData(Object layerData) {
		// TODO Auto-generated method stub

	}

	@Override
	protected LView _new() {
		return new MCDSliderLView(getLayer());
	}
	
	public MCDSliderFocusPanel getFocusPanel(){
		//Do not create fp for the panner
		if(getChild() == null){
			return null;
		}
		if(focusPanel == null || myFocusPanel ==  null){
			focusPanel = myFocusPanel = new MCDSliderFocusPanel(MCDSliderLView.this);
		}
		return myFocusPanel;
	}
	
	public synchronized void paintComponent(Graphics g){
		
		Graphics2D g2 = getOffScreenG2();
		if(g2 == null){
			return;
		}
		
		//if projection has changed, re-generate image first
		if(Main.PO != myLayer.getCurrentProjection()){
			myLayer.loadImage();
		}
		
		BufferedImage img = myLayer.getMapImage();
		
		//transform the image into it's known world bounds (only works in default projection)
		Rectangle2D worldClip = new Rectangle2D.Double();
		worldClip.setFrame(-180,-90, 360,180);
		
		g2.drawImage(img, Util.image2world(img.getWidth(), img.getHeight(), worldClip), null);

		//draw the image one more time shifted by 360 because the graphics wrapped doesn't
		// seem to draw enough times (sometimes it will end at the prime meridian)
		// So, one more instance of the drawing ensures it always covers the screen
		worldClip.setFrame(180, -90, 360, 180);
		g2.drawImage(img, Util.image2world(img.getWidth(), img.getHeight(), worldClip), null);

		// super.paintComponent draws the back buffers onto the layer panel
		super.paintComponent(g);	
	}

    /**
     * Overridden by individual views, returns information for formatting for 
     * Investigate mode while hovering with the cursor.
     */
    public InvestigateData getInvestigateData(MouseEvent event) {
		// Don't do this for the panner
 		if (getChild()==null) return null;

		MultiProjection proj = getProj();	
		if (proj == null) return null;

		Point2D screenPt = event.getPoint();
		Point2D worldPoint = proj.screen.toWorld(screenPt);
		
		int worldX = (int)Math.floor(worldPoint.getX());
		int worldY = (int)Math.floor(worldPoint.getY());

		worldX += 180;
		
		while (worldX<0) {
			worldX+=360;
		}
		worldX = worldX%360;
		
		BufferedImage img = myLayer.getNumericImage();
		
		int ppd = img.getWidth()/360;
		
		int pixelX = (worldX) * ppd;
		int pixelY = (180-(90+worldY)) * ppd;  // 
		
		if (pixelX<0 || pixelY<0 || pixelX >= img.getWidth() || pixelY >= img.getHeight()) return null;
		
		double val = img.getRaster().getSampleDouble(pixelX, pixelY, 0);
		
		InvestigateData id = new InvestigateData(getName());
		
		String category = myFocusPanel.getTypeString();
		
		id.add(category, ""+val);
		return id;
    }
}
