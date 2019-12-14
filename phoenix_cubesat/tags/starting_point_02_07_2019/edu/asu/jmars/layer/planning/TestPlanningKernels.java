package edu.asu.jmars.layer.planning;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestPlanningKernels extends JPanel implements ActionListener {

    static private final String newline = "\n";
    JButton openButton, unloadButton, showButton;
    JTextArea log;

    public TestPlanningKernels() {
        super(new BorderLayout());

        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Load SPICE Kernels",
                                 createImageIcon("images/Open16.gif"));
        openButton.addActionListener(this);

        //Create the unload button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        unloadButton = new JButton("Unload SPICE Kernels",
                                 createImageIcon("images/Save16.gif"));
        unloadButton.addActionListener(this);

        //Create the show button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        showButton = new JButton("Show Loaded MetaKernel",
                                 createImageIcon("images/Save16.gif"));
        showButton.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(unloadButton);
        buttonPanel.add(showButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {
        	PlanningKernels pk = PlanningKernels.getInstance();
            boolean status = pk.chooseAndLoadMetaKernel(this);

            if (status) {
                //This is where a real application would open the file.
                log.append("Opening: " + pk.getMetaKernelName() + newline);
                log.append("Loaded metaKernel: "+ pk.getMetaKernelName() + newline);
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        } else if (e.getSource() == unloadButton) {
        	PlanningKernels pk = PlanningKernels.getInstance();
        	if (pk.getMetaKernelName() == null) {
                log.append("No SPICE kernels are loaded." + newline);
        	} else {
        		pk.unloadAllKernels();
                log.append("SPICE Kernels unloaded." + newline);
        	}
        } else if (e.getSource() == showButton) {
        	PlanningKernels pk = PlanningKernels.getInstance();
        	if (pk.getMetaKernelName() == null) {
                log.append("No SPICE kernels are loaded." + newline);
        	} else {
                log.append("SPICE Kernels loaded from metakernel: " + pk.getMetaKernelName() + newline);
        	}
        } else {
            log.append("Open command cancelled by user." + newline);
        }
        log.setCaretPosition(log.getDocument().getLength());
        	
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TestPlanningKernels.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Test Planning Kernels");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new TestPlanningKernels());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
    }


}