package edu.asu.jmars.layer.map2;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import edu.asu.jmars.ProjObj;
import edu.asu.jmars.layer.Layer;
import edu.asu.jmars.layer.Layer.LView3D;
import edu.asu.jmars.util.Config;
import edu.asu.jmars.util.Util;
import edu.asu.jmars.viz3d.renderer.textures.Decal;
import edu.asu.jmars.viz3d.renderer.textures.DecalSet;

public class MapLView3D extends LView3D{
	
	private Pipeline[] pipeline;
	private DecalSet dSet = null;
	private int currentStateId;
	private MapLView myLView;

	private static final int NUM_RETRIEVER_THREADS = Config.get("map.retriever.threadCount", 5);
	private static ExecutorService pool = Executors.newFixedThreadPool(NUM_RETRIEVER_THREADS);	
	private static final long MAP_THREAD_TIMEOUT = Config.get("map.lview3d.threadTimeout", 100000);

	
	//TODO: this variable can be deleted when updating
	// shape models for decals is supported
	private String shapeModel = "";

	public MapLView3D(Layer layer) {
		super(layer);
		
		exists = true;
		usesDecals = true;
		//there is only one buffer (decal set) for maps
		currentStateId = myLayer.getStateId(0);
	}
	
	
	/**
	 * Set the LView on this LView3d
	 * @param view
	 */
	public void setLView(MapLView view){
		myLView = view;
	}
	
	
	/**
	 * This layer will only be enabled when the shape model
	 * that was active when it was added, is selected. This is
	 * because changing shape models with decals is not yet 
	 * supported.
	 * 
	 * When updating shape models is supported for decals the 
	 * comment should read:
	 * Is always enabled since it is not shape model dependant
	 * @see edu.asu.jmars.layer.Layer.LView3D#isEnabled()
	 */
	public boolean isEnabled(){
		if(mgr.getShapeModel() == null){
			return true;
		}else{
			//first time through, set the shape model
			if(shapeModel.equals("")){
				shapeModel = mgr.getShapeModelName();
			}
		}
		return mgr.getShapeModelName().equals(shapeModel);
		
		//TODO: when updating shape models is supported the
		// method should return true always.
//		return true;
	}
	
	
	public synchronized DecalSet getDecals(){
		
		int layerState = myLayer.getStateId(0);
//		System.out.println("layerState, currentState: "+layerState+", "+currentStateId);
		
		if(dSet == null || currentStateId != layerState){
			currentStateId = layerState;
			ArrayList<DecalSet> decalSets = mgr.getLayerDecalSet(this);
			dSet = decalSets.get(0);
			if (dSet != null) {
				final ArrayList<Decal> copyDecals = dSet.getDecals();
				if (!copyDecals.isEmpty()) {
					final CountDownLatch latch = new CountDownLatch(copyDecals.size());
				
					final Thread manager = new Thread(new Runnable(){
						public void run(){
				
							if(currentStateId == myLayer.getStateId(0)){
								for (Decal d : copyDecals){
									if(currentStateId != myLayer.getStateId(0)){
										//break out of creating new threads if at any
										// time the map layer has changed since this work
										// has started
										break;
									}
									d.setDisplayAlpha(alpha);
									pool.submit(new MapSampler(latch, d));
								}
							
								try {
									Long appropriateWait = MAP_THREAD_TIMEOUT;
									latch.await(appropriateWait, TimeUnit.MILLISECONDS);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								
								if(latch.getCount()>0){
	//								System.err.println("request not completed");
								}else{
//									System.err.println("request completed");
									if(currentStateId == myLayer.getStateId(0)){
										dSet.setRenderable(true);
									}
								}
								
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										mgr.refresh();
									}
								});
							}
						}
					}, "MapLView3DLatchWatcher");
					
					if(currentStateId == myLayer.getStateId(0)){
	//					System.err.println("starting threads");
						manager.start();
					}
				}
			}
		}
		
		return dSet;
	}
	
	
	private class MapSampler implements Runnable, MapChannelReceiver{
		private MapChannel ch = new MapChannel();
		private CountDownLatch latch;
		private Decal decal = null;
		private Rectangle2D worldRect;
		private ProjObj po;
		private int ppd;
		private BufferedImage finalImage;
		
		private MapSampler(CountDownLatch latch, Decal decal){
			this.latch = latch;
			this.decal = decal;
			
			//get all the pieces for the work
			Point2D minExtent = decal.getMinExtent();
			Point2D maxExtent = decal.getMaxExtent();
			
			double width = maxExtent.getX()-minExtent.getX();
			double height = maxExtent.getY()-minExtent.getY();
			
			height = Math.abs(height);
			width = Math.abs(width);
			
			worldRect = new Rectangle2D.Double(minExtent.getX(), maxExtent.getY(), width, height);
			ppd = decal.getPPD();
			po = decal.getProjection();
		}
		
		private MapSampler(CountDownLatch latch, Rectangle2D worldRect, ProjObj po, int ppd){
			this.latch = latch;
			this.worldRect = worldRect;
			this.po = po;
			this.ppd = ppd;
		}
		
		@Override
		public void mapChanged(MapData mapData) {
			if(mapData.isFinished()){
				if(currentStateId == myLayer.getStateId(0)){
//					System.out.println("setting image, is finished: "+mapData.isFinished());
					BufferedImage image = mapData.getImage();	
					if(decal != null){
						decal.setImage(image);
					}else{
						finalImage = image;
					}
				}
				latch.countDown();
			}
		}

		@Override
		public void run() {
			
			if(currentStateId == myLayer.getStateId(0)){
				if(myLView.getGraphicRequest() == null){
					//if there's nothing to draw, don't make any requests
					return;
				}else{
					pipeline = myLView.getGraphicRequest().getPipelines();
				}
				
				//do the work
				if(currentStateId == myLayer.getStateId(0)){
//					System.out.println("setting request");
					ch.setPipeline(pipeline);
					ch.setMapWindow(worldRect, ppd, po);
					ch.addReceiver(MapSampler.this);
				}
			}
		}
		
		public BufferedImage getImage(){
			return finalImage;
		}
		
	}
	
	public BufferedImage createDataImage(Rectangle2D worldRect, ProjObj po, int ppd, int labelScaleFactor){
		final BufferedImage bi = Util.newBufferedImage((int)(worldRect.getWidth()*ppd), (int)(worldRect.getHeight()*ppd));
		final Rectangle2D wRect = worldRect;
		final ProjObj projObj = po;
		final int Ppd = ppd;
		
		currentStateId = myLayer.getStateId(0);
		//draws one image at a time
		CountDownLatch latch = new CountDownLatch(1);
		
		MapSampler ms = new MapSampler(latch, wRect, projObj, Ppd);
		
		pool.submit(ms);
		
		try {
			Long appropriateWait = (long)100000;
			latch.await(appropriateWait, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(latch.getCount()>0){
			System.err.println("Request timed out while trying to pull map tiles for High Res export.");
		}else{
			 bi.getGraphics().drawImage(ms.getImage(), 0, 0, null);
		}
		
		return bi;
	}

}
