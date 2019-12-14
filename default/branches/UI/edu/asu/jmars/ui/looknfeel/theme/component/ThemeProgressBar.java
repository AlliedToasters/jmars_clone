package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeProgressBar implements ThemeComponent {

    private static String catalogKey = "progressbar";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeProgressBar());
    }

    public ThemeProgressBar() {
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getBordercolor() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public int getBorderthickness() {
        return ThemeProvider.getInstance().getSettings().getProgressbarBorderThickness();
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getContrast();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public Color getSplash() {
        return parse(ThemeProvider.getInstance().getBackground().getProgressbar());
    }

    @Override
    public void configureUI() {        
        UIManager.put("ProgressBar.border",
                BorderFactory.createLineBorder(parse(this.getBordercolor()), this.getBorderthickness()));
        UIManager.put("ProgressBar.background", parse(this.getBackground()));
        UIManager.put("ProgressBar.foreground", parse(this.getForeground()));

    }

}
