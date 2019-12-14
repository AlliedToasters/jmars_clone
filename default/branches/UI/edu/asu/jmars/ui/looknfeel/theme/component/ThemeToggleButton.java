package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.TOGGLE_OFF_IMG;
import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.TOGGLE_ON_IMG;
import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import edu.asu.jmars.ui.image.factory.ImageFactory;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;

public class ThemeToggleButton implements ThemeComponent {

    private static String catalogKey = "togglebutton";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeToggleButton());
    }

    public ThemeToggleButton() {
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getDisabledtext() {
        return ThemeProvider.getInstance().getText().getDisabled();
    }

    public String getSelect() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public String getOffColor() {
        return ThemeProvider.getInstance().getAction().getToggleOff();
    }

    public String getOnColor() {
        return ThemeProvider.getInstance().getAction().getToggleOn();
    }

    @Override
    public void configureUI() {        
        UIManager.put("ToggleButton.font", ThemeFont.getRegular());
        UIManager.put("ToggleButton.background", parse(this.getBackground()));
        UIManager.put("ToggleButton.foreground", parse(this.getForeground()));
        UIManager.put("ToggleButton.disabledText", parse(this.getDisabledtext()));
        UIManager.put("ToggleButton.select", parse(this.getSelect()));
        UIManager.put("ToggleButton.icon",
                new ImageIcon(ImageFactory.createImage(TOGGLE_OFF_IMG.withDisplayColor(parse(this.getOffColor())))
                        .getScaledInstance(27, 16, Image.SCALE_SMOOTH)));
        UIManager.put("ToggleButton.selectedIcon",
                new ImageIcon(ImageFactory.createImage(TOGGLE_ON_IMG.withDisplayColor(parse(this.getOnColor())))
                        .getScaledInstance(27, 16, Image.SCALE_SMOOTH)));

    }

}
