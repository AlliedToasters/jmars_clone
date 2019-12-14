package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeMenu implements ThemeComponent {

    private static String catalogKey = "menu";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeMenu());
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getRow().getAlternate();
    }

    public String getSelectionbackground() {
        return ThemeProvider.getInstance().getBackground().getHighlight();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public ThemeMenu() {
    }

    @Override
    public void configureUI() {       
        UIManager.put("Menu.font", ThemeFont.getBold());
        UIManager.put("Menu.selectionBackground", parse(this.getSelectionbackground()));
        UIManager.put("Menu.border", BorderFactory.createEmptyBorder(5, 0, 5, 0));
        UIManager.put("Menu.background", parse(this.getBackground()));
        UIManager.put("Menu.foreground", parse(this.getForeground()));
    }

}
