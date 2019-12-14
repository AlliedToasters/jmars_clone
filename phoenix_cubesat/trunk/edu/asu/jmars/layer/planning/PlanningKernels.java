
package edu.asu.jmars.layer.planning;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.asu.jspice.JSpice;

/**
 * This class encapsulates the NAIF SPICE kernels that are loaded to support planning.
 * 
 * It provides:
 * 1. a method for a modal dialog box to choose a metekernel file for loading
 * 2. a method to unload all currently-loaded SPICE kernels
 * 3. a method to retrieve a list of currently-loaded kernels 
 *
 */
public class PlanningKernels {

    private static PlanningKernels instance = null;
    private String loadedMetaKernel = null;
	private ArrayList<String> loadedKernels = new ArrayList<String>();
	private boolean kernelsLoaded = false;
	private File metaKernelFile;

	JFileChooser fc = null;

	
    /**
     *  Constructor.
     *  
     *  Kept private to ensure no one can create instances of this singleton.
     */
    private PlanningKernels() {    	
    	    this.kernelsLoaded = false;
    	    this.fc = new JFileChooser();
    		FileNameExtensionFilter filter = new FileNameExtensionFilter("Meta-kernel files (.tm)", "tm");
    		this.fc.setFileFilter(filter);
    		this.fc.setDialogTitle("Select a SPICE Meta-kernel for Planning");
    		this.fc.setApproveButtonText("Load");

    }
    
    /**
     * All consumers of this class must call getInstance() to gain access.
     *
     * @return     reference to the singleton instance.
     *
     */
    public static synchronized PlanningKernels getInstance() {

        if (instance == null) {
            instance = new PlanningKernels();
        }
        return instance;
    }
	
	public boolean chooseAndLoadMetaKernel(Component parent) {
		
		boolean allGood = false;
		
		if (this.kernelsLoaded) {
			allGood = false;
		} else {
    		
    		int returnVal = fc.showOpenDialog(parent);
    		if (returnVal == JFileChooser.APPROVE_OPTION) {
    			this.metaKernelFile = fc.getSelectedFile();
    			try {
    				JSpice.furnshc(new StringBuffer(this.metaKernelFile.getAbsolutePath()));
    				this.kernelsLoaded = true;
    				this.loadedMetaKernel = this.metaKernelFile.getName();
    				allGood = true;
    			} catch (Exception e) {
    				e.printStackTrace();
    				this.metaKernelFile = null;
    				allGood = false;
    			}
    		}
		}
		return allGood;
	}
	
    public String getMetaKernelName() {
    	
    	return this.loadedMetaKernel;
    	    
    }
	
	public ArrayList<String> getLoadedKernels() {
		
		// Need to call some Spice methods to get this
		return this.loadedKernels;
		
	}
	
	public void unloadAllKernels() {
		
		if (this.kernelsLoaded) {
			JSpice.unloadc(new StringBuffer(this.metaKernelFile.getAbsolutePath()));
			this.kernelsLoaded = false;
			this.loadedMetaKernel = null;
			this.metaKernelFile = null;			
		}
		
	}


	/**
	 * Thrown by PlanningKernels when an error is encountered.
	 *
	 * A specific reason for why the exception is being thrown should always be included.
	 *
	 * Thread-safe.
	 */
	public class KernelException extends Exception {

	    
	    public KernelException() {
	        super();
	    }
	    
	    public KernelException(String message) {
	        super(message);
	    }
	    
	    public KernelException(Throwable cause) {
	        super(cause);
	    }
	    
	    public KernelException(String message, Throwable cause) {
	        super(message, cause);
	    }
	        
	}
	
//	public static void main(String[] args) {
//        //Schedule a job for the event dispatch thread:
//        //creating and showing this application's GUI.
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                //Turn off metal's use of bold fonts
//                UIManager.put("swing.boldMetal", Boolean.FALSE); 
//                //Create and set up the window.
//                JFrame frame = new JFrame("PlanningKernels Demo");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//                //Add content to the window.
//                frame.add(new PlanningKernels());
//
//                //Display the window.
//                frame.pack();
//                frame.setVisible(true);
//            }
//        });
//	}

}
