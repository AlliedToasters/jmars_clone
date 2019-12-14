package edu.asu.jmars.ui.looknfeel.theme.component;

import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;

public class ThemeText implements ThemeComponent {

    private static String catalogKey = "text";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeText());
    }

    public ThemeText() {
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getTextcolor() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getHyperlink() {
        return ThemeProvider.getInstance().getText().getLink();
    }

    public String getTextDisabled() {
        return ThemeProvider.getInstance().getText().getDisabled();
    }

    @Override
    public void configureUI() {        
    }

}
