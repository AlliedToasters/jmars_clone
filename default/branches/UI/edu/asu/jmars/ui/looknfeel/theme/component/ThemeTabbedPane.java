package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;

import java.awt.Insets;

import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.ThemeFont.FONTS;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeTabbedPane implements ThemeComponent {

    private static String catalogKey = "tabbedpane";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeTabbedPane());
    }

    public ThemeTabbedPane() {
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getSelected() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getHighlight() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public String getBorderhighlightcolor() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public String getContentareacolor() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public String getFocuscolorline() {
        return ThemeProvider.getInstance().getBackground().getHighlight();
    }
    
    public int getTabindent() {
        return ThemeProvider.getInstance().getSettings().getTabIndent();
    }
    
    public int getTabspace() {
        return ThemeProvider.getInstance().getSettings().getTabSpace();
    }

    @Override
    public void configureUI() {        
        UIManager.put("TabbedPane.font", ThemeFont.getBold().deriveFont(FONTS.ROBOTO_TAB.fontSize()));
        UIManager.put("TabbedPane.selected", parse(this.getSelected()));
        UIManager.put("TabbedPane.highlight", parse(this.getHighlight()));
        UIManager.put("TabbedPane.background", parse(this.getBackground()));
        UIManager.put("TabbedPane.foreground", parse(this.getForeground()));
        UIManager.put("TabbedPane.borderHighlightColor", parse(this.getHighlight()));
        UIManager.put("TabbedPane.contentAreaColor", parse(this.getContentareacolor()));
        UIManager.put("TabbedPane[focus].colorLine", parse(this.getFocuscolorline()));
        UIManager.put("TabbedPane.spacer", this.getTabspace());
        UIManager.put("TabbedPane.indent", this.getTabindent());
        UIManager.put("TabbedPane.selectionForeground", parse(this.getFocuscolorline()));
        UIManager.put("TabbedPane.spacer", this.getTabspace());
        UIManager.put("TabbedPane.indent", this.getTabindent());        
        UIManager.put("TabbedPane.linePositionY", 50);
    }
}
