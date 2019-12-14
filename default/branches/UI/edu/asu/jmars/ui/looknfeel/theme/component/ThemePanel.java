package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemePanel implements ThemeComponent {

    private static String catalogKey = "panel";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemePanel());
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getBackgroundhi() {
        return ThemeProvider.getInstance().getBackground().getContrast();
    }

    public String getLinecolor() {
        return ThemeProvider.getInstance().getBackground().getAlternateContrast();
    }

    public String getTextcolor() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getSelectionhi() {
        return ThemeProvider.getInstance().getBackground().getHighlight();
    }

    public ThemePanel() {
    }

    @Override
    public void configureUI() {        
        UIManager.put("Panel.font", ThemeFont.getRegular());
        UIManager.put("Panel.background", parse(this.getBackground()));
        UIManager.put("Panel.border", BorderFactory.createEmptyBorder());
    }

}
