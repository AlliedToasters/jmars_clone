package edu.asu.jmars.layer.mcdslider;

import java.awt.image.BufferedImage;

import edu.asu.jmars.Main;
import edu.asu.jmars.ProjObj;
import edu.asu.jmars.layer.DataReceiver;
import edu.asu.jmars.layer.Layer;
import edu.asu.jmars.layer.stamp.StampCache;
import edu.asu.jmars.layer.stamp.StampImageFactory;
import edu.asu.jmars.util.FloatingPointImageOp;

/**
 * 
 * @author srcheese
 * @author sdickens
 *
 */
public class MCDSliderLayer extends Layer {

	private String mapPath;
	private BufferedImage mapImg;
	private BufferedImage numericImg;
	private String versionStr;
	private ProjObj currentProj;
	
	public MCDSliderLayer(String vStr){
		versionStr = vStr;
	}
	
	@Override
	public void receiveRequest(Object layerRequest, DataReceiver requester) {
		// TODO Auto-generated method stub
	}
	
	public void setPath(String path){
		mapPath = path;
		//load the new image
		loadImage();
	}
	
	void loadImage(){
		try {
			currentProj = Main.PO;
			
			String urlStr = "ImageServer?instrument=mcd&id="+mapPath+"&imageType=mcd&zoom=100";
			
			String projStr = Main.PO.getCenterLon()+":"+Main.PO.getCenterLat();
			
			String cacheProjStr = "mcd_"+mapPath+":"+projStr;
			
			BufferedImage img = StampCache.readProj(cacheProjStr, true);
			
			if (img==null) {
				//Get image from the server or cache if exists
				img = StampImageFactory.getImage(urlStr, true);
				
	//			BufferedImage img = ImageIO.read(new File(mapPath));
				
				GlobalDataReprojecter gdr = new GlobalDataReprojecter(img);
				img = gdr.getProjectedImage();
				
				StampCache.writeProj(img, cacheProjStr);
			}
			
			numericImg = img;
			
			if(img!=null){
				//convert the image and store as something else
				BufferedImage convertedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
                FloatingPointImageOp op = new FloatingPointImageOp();														
				convertedImg.createGraphics().drawImage(img, op, 0, 0);
				mapImg = convertedImg;
				
//				System.out.println("image loaded "+mapImg);
//				System.out.println(ImageIO.write(mapImg, "tiff", new File("/tmp/mapImg.tiff")));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedImage getMapImage(){
		return mapImg;
	}
	
	public BufferedImage getNumericImage(){
		return numericImg;
	}
	
	/**
	 * @return The string associated with the version of MCD 
	 * with which these products were produced with, and now
	 * related to where they live on disk.
	 */
	public String getVersionString(){
		return versionStr;
	}
	
	
	/**
	 * @return The current projection that the map image is in
	 */
	public ProjObj getCurrentProjection(){
		return currentProj;
	}
}
