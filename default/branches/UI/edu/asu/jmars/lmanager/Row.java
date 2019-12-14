package edu.asu.jmars.lmanager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;

import edu.asu.jmars.layer.LManager;
import edu.asu.jmars.layer.Layer;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import edu.asu.jmars.ui.looknfeel.theme.component.ThemePanel;

public class Row extends JPanel {

	static final int PAD = 2;

	final Layer.LView view;
	final Layer.LView3D view3D;
	JComponent light;
	JToggleButton btnM;
	JToggleButton btnP;
	JToggleButton btn3;
	JLabel label;
	JSlider slider;
	JPanel top;
	Border topBottomBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY);
	Border leftBorder = BorderFactory.createEmptyBorder(0, 5, 0, 0);
	Color selectionhi = parse(((ThemePanel)GUITheme.get("panel")).getSelectionhi());
	Border selectedLeftBorder = BorderFactory.createMatteBorder(0, 5, 0, 0, selectionhi);
	CompoundBorder normalBorder = new CompoundBorder(topBottomBorder, leftBorder);
	CompoundBorder selectedBorder = new CompoundBorder(selectedLeftBorder, topBottomBorder);
	
	private Color normalBackground = parse(((ThemePanel)GUITheme.get("panel")).getBackground());
	private Color hoverBackground = parse(((ThemePanel)GUITheme.get("panel")).getBackgroundhi());

	public Row(final Layer.LView view, MouseInputListener rowMouseHandler) {
		this.view = view;
		this.view3D = view.getLView3D();
		this.light = view.getLight();
		this.btnM = new JToggleButton("M", view.isVisible());
		this.btnP = new JToggleButton("P", view.getChild().isVisible());
		this.btn3 = new JToggleButton("3D", view3D.isVisible());
		//if the LView3D has not been written for the particular layer,
		// disable the 3D ThemeButton
		if(!view3D.isEnabled()){
			btn3.setEnabled(false);
			btn3.setSelected(false);
		}
		this.label = new JLabel(" " + LManager.getLManager().getUniqueName(view));
		this.slider = new JSlider(0, 1000);

		slider.setFocusable(false);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				float value = (float)slider.getValue()/slider.getMaximum();
				//2D views
				view.setAlpha(value);
				//3D view (check to make sure the 3d view exists at all)
				if(!slider.getValueIsAdjusting() && view3D.exists()){
				//only update when it's finished moving, because the redraw is not seemless
					view3D.setAlpha(value);
				}
				//tooltip
				slider.setToolTipText("Opacity: "+Math.round(value * 100d)+"%");
			}
		});
		slider.setValue((int) (view.getAlpha() * 1000));

		label.addMouseListener(rowMouseHandler);
		label.addMouseMotionListener(rowMouseHandler);

		addMouseListener(rowMouseHandler);
		addMouseMotionListener(rowMouseHandler);

		prepare(btnM);
		prepare(btnP);
		prepare(btn3);

		int btnHeight = 12;
		Dimension d = new Dimension(btnHeight, btnHeight);

		setAll(d, light);
		
		d.height = 28;
		d.width = 100;
		setAll(d, slider);


		top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
		top.setBorder(new EmptyBorder(13, 15, 5, 15));
		top.add(light);
		top.add(label);
		
		int row = 0; 
		int col = 0;
		int pad = 0;
		Insets in = new Insets(pad, pad, pad, pad);
		setLayout(new GridBagLayout());

		add(top, new GridBagConstraints(col, row, 4, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, in, pad, pad));
		row++; col = 0;
		pad = 2;
		in = new Insets(pad,pad,13,pad);
		add(btnM, new GridBagConstraints(col, row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(pad, 15, 13, pad), pad, pad));
		add(btnP, new GridBagConstraints(++col, row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, in, pad, pad));
		add(btn3, new GridBagConstraints(++col, row, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, in, pad, pad));
		add(slider, new GridBagConstraints(++col, row, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(pad, pad, 8, 10), pad, pad));
		
		updateRow();
	}

	public void setText(String newText) {
		label.setText(newText);
		label.setToolTipText(newText);
	}
	
	public void updateVis() {
		btnM.setSelected(view.isVisible());
		btnP.setSelected(view.getChild().isVisible());	
		btn3.setSelected(view3D.isVisible());
		btn3.setEnabled(view3D.isEnabled());
		
		//@since 3.0.3 - added to update the slider on updateVis since after loading a session, the view opacity was property adjusted,
		//but the slider was not being updated 
		slider.setValue((int) (view.getAlpha() * 1000));
	}
	
	public Layer.LView getView() {
		return view;
	}
	
	public void setBackground(Color newColor) {
		super.setBackground(newColor);
		if (top != null){
			top.setBackground(newColor);
		}
		if (slider!=null) {
			slider.setBackground(newColor);
		}		
		if (btnM != null){
			btnM.setBackground(newColor);
		}
		if (btnP != null){
			btnP.setBackground(newColor);
		}
		if (btn3 != null){
			btn3.setBackground(newColor);
		}
		if (label != null){
			label.setBackground(newColor);
		}
	}
	
	public void updateRow() {		
		if (view == LManager.getLManager().getActiveLView())
		{		
			setBorder(selectedBorder);
		}
		else
		{			
			setBorder(normalBorder);
		}	
		
		setBackground(normalBackground);
	}
	
	public void setToHoverAppearance(){
		setBackground(hoverBackground);
	}

	void prepare(JToggleButton btn) {
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setFocusable(false);
		btn.addActionListener(visibilityHandler);
	}
	
	private void setAll(Dimension d, JComponent c) {
		d = new Dimension(d);
		c.setMinimumSize(d);
		c.setMaximumSize(d);
		c.setPreferredSize(d);
	}

	private ActionListener visibilityHandler = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JToggleButton btn = (JToggleButton) e.getSource();
			Row row = (Row) btn.getParent();
			Layer.LView view = row.view;
			
			//Check to see if it's from the 3d button first
			if(btn.getText().equals("3D")){
				view3D.setVisible(btn.isSelected());
				return;
			}
			
			if (btn.getText().equals("P"))
				view = view.getChild();
			
			//KJR - 6/6/12
			if (btn.isSelected()) {
				view.setDirty(true);//we need it to believe it is dirty in order to trigger a redraw
			}
			
			view.setVisible(btn.isSelected());
		}
	};
//Not used currently, but could be called in MainPanel to be used to alternate row colors
	public void setColor(Color c){
		this.setBackground(c);
		top.setBackground(c);
	}
}

