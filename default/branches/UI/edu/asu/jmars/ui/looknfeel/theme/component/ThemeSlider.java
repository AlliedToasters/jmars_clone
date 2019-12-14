package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeSlider implements ThemeComponent {

    private static String catalogKey = "slider";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeSlider());
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

    public String getTrackcolor() {
        return ThemeProvider.getInstance().getBackground().getAlternateContrast();
    }

    public String getHalocolor() {
        return ThemeProvider.getInstance().getBackground().getHighlight();
    }

    public ThemeSlider() {
    }

    @Override
    public void configureUI() {       
        UIManager.put("Slider.background", parse(this.getBackground()));
        UIManager.put("Slider.foreground", parse(this.getForeground()));
        UIManager.put("Slider.trackColor", parse(this.getTrackcolor()));
        UIManager.put("Slider.border", BorderFactory.createEmptyBorder());
        UIManager.put("Slider[halo].color", parse(this.getHalocolor()));
    }

}
