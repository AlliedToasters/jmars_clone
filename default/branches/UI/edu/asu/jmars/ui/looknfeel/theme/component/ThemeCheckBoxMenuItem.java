package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.CHECK_OFF_IMG;
import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.CHECK_ON_IMG;
import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import edu.asu.jmars.ui.image.factory.ImageFactory;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeCheckBoxMenuItem implements ThemeComponent {

	private static String catalogKey = "checkboxmenuitem";

	static {
		GUITheme.getCatalog().put(catalogKey, new ThemeCheckBoxMenuItem());
	}

	public static String getCatalogKey() {
		return catalogKey;
	}

	public String getSelectionbackground() {
		return ThemeProvider.getInstance().getBackground().getHighlight();
	}

	public String getBackground() {
		return ThemeProvider.getInstance().getRow().getAlternate();
	}

	public String getForeground() {
		return ThemeProvider.getInstance().getText().getMain();
	}

	public String getCheckiconimgcolor() {
		return ThemeProvider.getInstance().getAction().getImageOn();
	}

	public String getSelectedcheckiconimgcolor() {
		return ThemeProvider.getInstance().getAction().getImageOff();
	}

	public String getAcceleratorforeground() {
		return ThemeProvider.getInstance().getText().getMain();
	}

	public String getAcceleratorselectionforeground() {
		return ThemeProvider.getInstance().getText().getMain();
	}

	public ThemeCheckBoxMenuItem() {
	}

	@Override
	public void configureUI() {	    
		UIManager.put("CheckBoxMenuItem.font", ThemeFont.getBold());
		UIManager.put("CheckBoxMenuItem.selectionBackground", parse(this.getSelectionbackground()));
		UIManager.put("CheckBoxMenuItem.border", BorderFactory.createEmptyBorder(5, 0, 5, 0));
		UIManager.put("CheckBoxMenuItem.background", parse(this.getBackground()));
		UIManager.put("CheckBoxMenuItem.foreground", parse(this.getForeground()));
		UIManager.put("CheckBoxMenuItem.checkIcon", new ImageIcon(ImageFactory
				.createImage(CHECK_OFF_IMG.withDisplayColor(parse(this.getCheckiconimgcolor())))));
		UIManager.put("CheckBoxMenuItem.selectedCheckIcon", new ImageIcon(ImageFactory.createImage(
				CHECK_ON_IMG.withDisplayColor(parse(this.getSelectedcheckiconimgcolor())))));
		UIManager.put("CheckBoxMenuItem.acceleratorForeground", parse(this.getAcceleratorforeground()));
		UIManager.put("CheckBoxMenuItem.acceleratorSelectionForeground",
				parse(this.getAcceleratorselectionforeground()));
	}
}
