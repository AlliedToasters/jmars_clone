package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeSplitPaneDivider implements ThemeComponent {

    private static String catalogKey = "splitpanedivider";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeSplitPaneDivider());
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getContrast();
    }

    public String getDraggingcolor() {
        return ThemeProvider.getInstance().getBackground().getHighlight();
    }

    public ThemeSplitPaneDivider() {
    }

    @Override
    public void configureUI() {       
        UIManager.put("SplitPaneDivider.background", parse(this.getBackground()));
        UIManager.put("SplitPaneDivider.draggingColor", parse(this.getDraggingcolor()));
    }

}
