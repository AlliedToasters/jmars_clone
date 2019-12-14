package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeTitleBorder implements ThemeComponent {

    private static String catalogKey = "titleborder";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeTitleBorder());
    }

    public ThemeTitleBorder() {
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getTitlecolor() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getBorder() {
        return ThemeProvider.getInstance().getBackground().getContrast();
    }

    @Override
    public void configureUI() {        
        UIManager.put("TitledBorder.font", ThemeFont.getBold());
        UIManager.put("TitledBorder.titleColor", parse(this.getTitlecolor()));
        UIManager.put("TitledBorder.border", BorderFactory.createLineBorder(parse(this.getBorder())));
    }

}
