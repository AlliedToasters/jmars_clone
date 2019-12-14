package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;

public class ThemeTextPane implements ThemeComponent {

	private static String catalogKey = "textpane";

	static {
		GUITheme.getCatalog().put(catalogKey, new ThemeTextPane());
	}

	public static String getCatalogKey() {
		return catalogKey;
	}

	public String getSelectionbackground() {
		return ThemeProvider.getInstance().getBackground().getContrast();
	}

	public String getSelectionforeground() {
		return ThemeProvider.getInstance().getText().getMain();
	}

	public String getForeground() {
		return ThemeProvider.getInstance().getText().getMain();
	}

	public String getBackground() {
		return ThemeProvider.getInstance().getBackground().getContrast();
	}

	public String getCaretforeground() {
		return ThemeProvider.getInstance().getText().getMain();
	}

	public String getInactiveforeground() {
		return ThemeProvider.getInstance().getText().getMain();
	}

	public ThemeTextPane() {
	}

	@Override
	public void configureUI() {
		GUITheme.getCatalog().put(catalogKey, this);
		UIManager.put("TextPane.font", ThemeFont.getBold());
		UIManager.put("TextPane.foreground", parse(this.getForeground()));
		UIManager.put("TextPane.border", BorderFactory.createEmptyBorder());
		UIManager.put("TextPane.background", parse(this.getBackground()));
		UIManager.put("TextPane.caretForeground", parse(this.getCaretforeground()));
		UIManager.put("TextPane.selectionForeground", parse(this.getSelectionforeground()));
		UIManager.put("TextPane.inactiveForeground", parse(this.getInactiveforeground()));
		UIManager.put("TextPane.selectionBackground", parse(this.getSelectionbackground()));
	}

}
