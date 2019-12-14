package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;

public class ThemeTree implements ThemeComponent {

    private static String catalogKey = "tree";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeTree());
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getContrast();
    }

    public String getSelectionforeground() {
        return ThemeProvider.getInstance().getBackground().getHighlight();
    }

    public String getTextforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getTextbackground() {
        return ThemeProvider.getInstance().getBackground().getContrast();
    }

    public boolean isPaintlines() {
        return ThemeProvider.getInstance().getSettings().isTreePaintLines();
    }

    public ThemeTree() {
    }

    @Override
    public void configureUI() {        
        UIManager.put("Tree.font", ThemeFont.getRegular());
        UIManager.put("Tree.background", parse(this.getBackground()));
        UIManager.put("Tree.selectionForeground", parse(this.getSelectionforeground()));
        UIManager.put("Tree.textForeground", parse(this.getTextforeground()));
        UIManager.put("Tree.textBackground", parse(this.getTextbackground()));
        UIManager.put("Tree.paintLines", this.isPaintlines());
    }

}
