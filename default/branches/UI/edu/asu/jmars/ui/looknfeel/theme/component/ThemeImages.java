package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import java.awt.Color;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeImages implements ThemeComponent {

	private static String catalogKey = "images";

	static {
		GUITheme.getCatalog().put(catalogKey, new ThemeImages());
	}

	public ThemeImages() {
	}

	public static String getCatalogKey() {
		return catalogKey;
	}

	public Color getFill() {
		return parse(ThemeProvider.getInstance().getImage().getFill());
	}

	public Color getSelectedfill() {
		return parse(ThemeProvider.getInstance().getImage().getSelectionFill());
	}

	public Color getLayerfill() {
		return parse(ThemeProvider.getInstance().getImage().getLayer());
	}

	public Color getLinkfill() {
		return parse(ThemeProvider.getInstance().getImage().getLink());
	}

	@Override
	public void configureUI() {	   
	}
}
