package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeLabel implements ThemeComponent {

    private static String catalogKey = "label";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeLabel());
    }

    public ThemeLabel() {
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getDisabledforeground() {
        return ThemeProvider.getInstance().getText().getDisabled();
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getBackgroundhilight() {
        return ThemeProvider.getInstance().getBackground().getContrast();
    }

    @Override
    public void configureUI() {        
        UIManager.put("Label.font", ThemeFont.getBold());
        UIManager.put("Label.background", parse(this.getBackground()));
        UIManager.put("Label.foreground", parse(this.getForeground()));
        UIManager.put("Label.disabledForeground", parse(this.getDisabledforeground()));
    }

}
