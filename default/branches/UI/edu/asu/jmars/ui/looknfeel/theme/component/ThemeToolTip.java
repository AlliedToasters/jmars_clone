package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;

public class ThemeToolTip implements ThemeComponent {

    private static String catalogKey = "tooltip";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeToolTip());
    }

    public ThemeToolTip() {
    }

    public Color getBackground() {
        Color backColor = parse(ThemeProvider.getInstance().getBackground().getContrast());
        int red = backColor.getRed();
        int green = backColor.getGreen();
        int blue = backColor.getBlue();
        int alpha = (int) (255 * 0.8);
        return new Color(red, green, blue, alpha);
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    @Override
    public void configureUI() {        
        UIManager.put("ToolTip.font", ThemeFont.getRegular());
        UIManager.put("ToolTip.background", this.getBackground());
        UIManager.put("ToolTip.foreground", parse(this.getForeground()));
        UIManager.put("ToolTip.border", BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

}
