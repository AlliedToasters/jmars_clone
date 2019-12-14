package edu.asu.jmars.lmanager;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.plaf.metal.MetalComboBoxIcon;

import edu.asu.jmars.Main;
import edu.asu.jmars.layer.LManager;
import edu.asu.jmars.layer.LViewFactory;
import edu.asu.jmars.layer.Layer;
import edu.asu.jmars.layer.Layer.LView3D;
import edu.asu.jmars.layer.MultiFactory;
import edu.asu.jmars.ui.image.factory.ImageCatalogItem;
import edu.asu.jmars.ui.image.factory.ImageFactory;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.util.Config;
import edu.asu.jmars.util.Util;
import edu.asu.jmars.viz3d.ThreeDManager;
import static  edu.asu.jmars.ui.looknfeel.Utilities.parse;
import edu.asu.jmars.ui.looknfeel.theme.component.ThemePanel;
import edu.asu.jmars.ui.looknfeel.theme.component.ThemeButton;
import edu.asu.jmars.ui.looknfeel.theme.component.ThemeImages;

public class MainPanel extends JLayeredPane {

	private final Color backgroundMainColor = parse(((ThemePanel)GUITheme.get("panel")).getBackground());
	
	Row dragRow;
	private Row curRow;
	int dragPos;
	int dragSrc;
	int dragDst;
	public List<Row> rows = new ArrayList<Row>();
	JMenuBar addBtn;
	JMenuBar editBar;
	MenuButton editBtn;
	JButton addBtn1;
	JCheckBoxMenuItem tooltip, tooltipsItem;
	
	public JScrollPane rowScrollPane;
	public JLayeredPane rowsPanel = new JLayeredPane() {
		public void layout() {
			if (rows.isEmpty())
				return;

			Insets insets = rowsPanel.getInsets();
			int h = ((Row) rows.get(0)).getPreferredSize().height;
			int w = rowsPanel.getWidth() - insets.left - insets.right;

			for (int i = 0; i < rows.size(); i++) {
				Row r = (Row) rows.get(i);
				//Can be used to alternate row colors
//				if(i%2 == 0){
//					Color lightGray = new Color(186,186, 186);
//					r.setColor(lightGray);
//				} else{
//					Color standard = UIManager.getColor("Panel.background");
//					r.setColor(standard);
//				}
					
				int y;
				if (i == dragSrc)
					y = dragPos;
				else if (i > dragSrc && i <= dragDst)
					y = h * (i - 1);
				else if (i < dragSrc && i >= dragDst)
					y = h * (i + 1);
				else
					y = h * i;
				r.setSize(w, h);
				r.setLocation(insets.left, insets.top + y);
			}
		}

		public Dimension getPreferredSize() {
			int width = 0;
			int height = 0;
			Dimension size = new Dimension(width, height);

			if (rows.size() == 0)
				return size;
			int h = ((Row) rows.get(0)).getPreferredSize().height;
			height = rows.size() * h;
			width = 1;  // make it expand to take whatever space is available
			size.setSize(width, height);
			return size;
		}

	};

	public void delete(int selectedIdx) {
		// Remove the row... the row indices are reversed relative to everything else.
		Row r = (Row) rows.remove(rows.size() - 1 - selectedIdx);
		rowsPanel.remove(r);
		rowsPanel.repaint();
		if (rowScrollPane != null) {
			rowScrollPane.revalidate();
			rowScrollPane.repaint();
		}	
	}
	
	void buildEditMenu() {
		JMenuItem options = new JMenuItem(actOptions);
		options.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
		JMenuItem docked = new JMenuItem(actDocked);
		docked.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.SHIFT_DOWN_MASK));
		JMenuItem rename = new JMenuItem(actRename);
		JMenuItem delete = new JMenuItem(actDelete);
		delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.SHIFT_DOWN_MASK));
		tooltip = new JCheckBoxMenuItem(actTooltip);
		editBtn.add(options);
		editBtn.add(docked);
		editBtn.add(rename);
		editBtn.add(delete);
		editBtn.add(tooltip);

		editBar.removeAll();
		editBar.add(editBtn);
	}
	
	
	private class MenuButton extends JMenu{
		
		public MenuButton(String name){
			super(name);		
		    Color imgColor = ((ThemeImages) GUITheme.get("images")).getFill();
			Image caretdown =  ImageFactory.createImage(ImageCatalogItem.CARET_DOWN_IMG
	 		   				    .withDisplayColor(imgColor))
					            .getScaledInstance(10, 8, Image.SCALE_SMOOTH);
			
			setIcon(new ImageIcon(caretdown));
					
			setHorizontalTextPosition(SwingConstants.LEFT);
			setIconTextGap(8);
			Color back =  parse(((ThemeButton)GUITheme.get("button")).getBackground());
			Color front =  parse(((ThemeButton)GUITheme.get("button")).getForeground());
			setBackground(back);
			setForeground(front);
			setBorder(new EmptyBorder(0, 10, 0, 5));
			
			addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					tooltip.setSelected(!LManager.getLManager().getTooltipDisabledStatus());
				}
			});
			
			setUI(new BasicMenuUI() {
				protected void installDefaults() {
					super.installDefaults();
					selectionBackground = parse(((ThemeButton)GUITheme.get("button")).getHighlight());
					selectionForeground = front;
				}
			});
		}
		
		public void paintBorder(Graphics g){
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2f));
			Color buttonSelected = parse(((ThemeButton)GUITheme.get("button")).getHighlight());
			
			Color buttonRegular = parse(((ThemeButton)GUITheme.get("button")).getHighlight());
			if(isSelected()){
				g2.setColor(buttonSelected);
			}else{
				g2.setColor(buttonRegular);
			}
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 7, 7);
		}		
	}
	

	//
	//  OLD ADD LAYER METHOD
	//
	public void rebuildAddMenu() {
		JMenu menu = new JMenu("Add new layer");
		MultiFactory.addAllToMenu(menu, LViewFactory.factoryList2);

		menu.setUI(new BasicMenuUI() {
			protected void installDefaults() {
				super.installDefaults();
				selectionBackground = UIManager.getColor("ThemeButton.select");
				if(selectionBackground==null){
					selectionBackground = UIManager.getColor("TabbedPane.highlight");
					selectionForeground = Color.BLACK;
				}
			}
		});
		menu.setBorder(UIManager.getBorder("ThemeButton.border"));
		menu.setIcon(new MetalComboBoxIcon());

		if (Config.get("lmanager.flatten", false)) {
			menu.removeAll();
			Iterator i = LViewFactory.factoryList.iterator();
			while (i.hasNext())
				menu.add(new JMenuItem(i.next().toString()));
		}

		addBtn.removeAll();
		addBtn.add(menu);
	}

//Depending on whether user wants old or new style of adding layers
// a different component is returned and added.	
	private JButton getAddBtn(){
	    addBtn1 = new JButton(actAdd);
		return addBtn1;
	}
	
	public MainPanel() {
		JButton addButton = getAddBtn();
		Main.mainFrame.getRootPane().setDefaultButton(addButton);
		editBar = new JMenuBar();
		editBar.setBorder(null);
		editBtn = new MenuButton("EDIT SELECTED"); //caps is per the UX Design
		editBar.add(editBtn);
		buildEditMenu();
		
		Dimension d1 = addButton.getPreferredSize();
		Dimension d2 = editBar.getPreferredSize();
		Dimension d = new Dimension(Math.max(d1.width, d2.width), Math.max(
				d1.height, d2.height));
		setAll(d, addButton);
		setAll(d, editBar.getMenu(0));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(10, 5, 10, 5);
		c.weightx = 1;
		c.anchor = GridBagConstraints.CENTER;
		add(addButton, c);

		c.anchor = GridBagConstraints.WEST;
		add(editBar, c);

		c.gridy = 1;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 8, 0);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
//		add(new JSeparator(), c);

		c.insets = new Insets(0, 5, 5, 5);
		c.gridy = 2;
		c.weighty = 2;

		rowsPanel.setBackground(backgroundMainColor);
		rowsPanel.setOpaque(true);

		rowScrollPane = new JScrollPane(rowsPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		rowScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		
		c.insets = new Insets(0, 0, 0, 0);
		add(rowScrollPane, c);
		//Material
		setOpaque(true);
		setBackground(backgroundMainColor);
   }

	private void setAll(Dimension d, JComponent c) {
		c.setMinimumSize(d);
		c.setMaximumSize(d);
		c.setPreferredSize(d);
	}
	// These actions are used in the drop down menu, right click menu, and
	// also double clicking of the rows.

	public Action actDelete = new AbstractAction("Delete") {
		public void actionPerformed(ActionEvent e) {
			LManager.getLManager().deleteSelectedLayer();
		}
	};

	Action actRename = new AbstractAction("Rename") {
		public void actionPerformed(ActionEvent e) {
			LManager.getLManager().renameSelectedLayer();
		}
	};

	Action actOptions = new AbstractAction("Open") {
		public void actionPerformed(ActionEvent e) {
			LManager.getLManager().accessSelectedOptions(false);
		}
	};
	
	Action actDocked = new AbstractAction("Open Docked"){
		public void actionPerformed(ActionEvent e){
			LManager.getLManager().accessSelectedOptions(true);
		}
	};
	
	Action actTooltip = new AbstractAction("Show Tooltip"){
		public void actionPerformed(ActionEvent e) {
			boolean show = true;;
			if(e.getSource() == tooltip){
				show = tooltip.isSelected();
			}
			if(e.getSource() == tooltipsItem){
				show = tooltipsItem.isSelected();
			}
			LManager.getLManager().showTooltipForSelectedLayer(show);
			
		};
	};

	// This action is used to pull up the 'add layer' jframe with the
	// button
	Action actAdd = new AbstractAction("Add New Layer".toUpperCase()) {
		public void actionPerformed(ActionEvent e) {
			LManager.getLManager().displayAddNewLayer();
		}
	};

	// ------------------------------------------------------------------------//

	public void addView(Layer.LView view) {
		Row r = new Row(view, rowMouseHandler);
		rowsPanel.add(r);
		rows.add(0, r);
	}

	public void updateRows() {
		for (Iterator i = rows.iterator(); i.hasNext();)
			((Row) i.next()).updateRow();
	}

	void setDragOffset(int dragOffset) {
		if (dragOffset == 0 || rows.isEmpty()) {
			dragSrc = -1;
			dragDst = -1;
			dragPos = -1;
		} else {
			int h = ((Row) rows.get(0)).getPreferredSize().height;
			dragSrc = rows.indexOf(dragRow);
			dragPos = Util.bound(0, h * dragSrc + dragOffset,
					h * (rows.size() - 1));
			dragDst = (dragPos + h / 2 - 1) / h;
		}
	}

	public MouseInputListener rowMouseHandler = new MouseInputAdapter() {
		int pressed;

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() != 2)
				return;
			if(Config.get("openDocked").equalsIgnoreCase("false"))
				LManager.getLManager().accessSelectedOptions(false);
			else
				LManager.getLManager().accessSelectedOptions(true);
		}

		public void mousePressed(MouseEvent e) {
			Component c = e.getComponent();
			if(c instanceof Row){
				dragRow = (Row) c;
			}else if(c instanceof JLabel){
				//The label on the row is inside a JPanel ('top')
				// get top, and then get the row
				c = c.getParent().getParent();
				dragRow = (Row) c;
			}
			
			setDragOffset(0);
			pressed = screenY(e);
			rowsPanel.moveToFront(dragRow);
			LManager.getLManager().setActiveLView(dragRow.getView());
			dragRow.setToHoverAppearance();
			

			/*************** Right Click popup menu *****************************/
			if (SwingUtilities.isRightMouseButton(e)) {
				// Create popup menus and menu items
				JPopupMenu editMenu = new JPopupMenu();
				JMenuItem deleteItem = new JMenuItem(actDelete);
				JMenuItem renameItem = new JMenuItem(actRename);
				JMenuItem optionsItem = new JMenuItem(actOptions);
				JMenuItem dockedItem = new JMenuItem(actDocked);
				tooltipsItem = new JCheckBoxMenuItem(actTooltip);
				tooltipsItem.setSelected(!LManager.getLManager().getTooltipDisabledStatus());

				// Add items to menu
				editMenu.add(optionsItem);
				editMenu.add(dockedItem);
				editMenu.add(renameItem);
				editMenu.add(deleteItem);
				editMenu.add(tooltipsItem);

				// Display popup menu
				editMenu.show(e.getComponent(), e.getX(), e.getY());
			}

			/********************************************************************/
		}

		public void mouseDragged(MouseEvent e) {
			setDragOffset(screenY(e) - pressed);
			rowsPanel.revalidate();
			rowsPanel.repaint();
			if (rowScrollPane != null) {
				rowScrollPane.revalidate();
				rowScrollPane.repaint();
			}

		}

		public void mouseReleased(MouseEvent e) {
			if (dragSrc != dragDst) {
				int dragSrcRev = rows.size() - 1 - dragSrc;
				int dragDstRev = rows.size() - 1 - dragDst;

				// Move the user-visible row, the actual view list
				// order, and the focus tabs. The latter two are
				// in reverse order from the first one.
				Row rowToMove = rows.remove(dragSrc);
				rows.add(dragDst, rowToMove);
				LManager.getLManager().viewList.move(dragSrcRev, dragDstRev);
				LManager.getLManager().setActiveLView(dragDstRev);
				
				//update the 3d view if the row has 3d data
				LView3D view3d = rowToMove.getView().getLView3D();
				ThreeDManager mgr = ThreeDManager.getInstance();
				if(mgr.isReady() && view3d.isEnabled() && view3d.isVisible()){
					if (mgr.hasLayerDecalSet(view3d)) {
						mgr.update();
					}
				}
			}

			dragRow.setToHoverAppearance();
			dragRow = null;
			setDragOffset(0);
			rowsPanel.revalidate();
			rowsPanel.repaint();
			if (rowScrollPane != null) {
				rowScrollPane.revalidate();
				rowScrollPane.repaint();
			}

		}
		
		public void mouseMoved(MouseEvent e){
			Component c = e.getComponent();
			Row hoverRow = null;
			if(c instanceof Row){
				hoverRow = (Row) c;
			}else if(c instanceof JLabel){
				//The label on the row is inside a JPanel ('top')
				// get top, and then get the row
				hoverRow = (Row) c.getParent().getParent();
			}
			
			updateRows();
			
			if(hoverRow != null){
				hoverRow.setToHoverAppearance();
			}
		}
		
		public void mouseExited(MouseEvent e){
			updateRows();
			
			// This code will be reached if the mouse goes over any 
			// of the toggle buttons or the slider, so if the mouse
			// is still really inside a row, set the hover border
			Point2D screenPt = e.getLocationOnScreen();
			
			Point uL = curRow.getLocationOnScreen();
			Rectangle2D rect = new Rectangle2D.Double(uL.x, uL.y, curRow.getWidth(), curRow.getHeight());
			
			if(rect.contains(screenPt)){
				curRow.setToHoverAppearance();
			}
		}
		
		public void mouseEntered(MouseEvent e){
			Component c = e.getComponent();
			if(c instanceof Row){
				curRow = (Row) c;
			}else if(c instanceof JLabel){
				//The label on the row is inside a JPanel ('top')
				// get top, and then get the row
				curRow = (Row) c.getParent().getParent();
			}
			
			updateRows();
			
			if(curRow != null){
				curRow.setToHoverAppearance();
			}
		}
	};
	
	private static int screenY(MouseEvent e) {
		Point pt = e.getPoint();
		SwingUtilities.convertPointToScreen(pt, e.getComponent());
		return pt.y;
	}

}