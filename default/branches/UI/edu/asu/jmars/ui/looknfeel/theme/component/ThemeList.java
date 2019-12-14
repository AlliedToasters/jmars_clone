package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;

public class ThemeList implements ThemeComponent {

    private static String catalogKey = "list";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeList());
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getSelectionbackground() {
        return ThemeProvider.getInstance().getBackground().getHighlight();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }


    public ThemeList() {
    }

    @Override
    public void configureUI() {        
        UIManager.put("List.font", ThemeFont.getRegular());
        UIManager.put("List.foreground", parse(this.getForeground()));
        UIManager.put("List.background", parse(this.getBackground()));
        UIManager.put("List.selectionBackground", parse(this.getSelectionbackground()));
    }

}
