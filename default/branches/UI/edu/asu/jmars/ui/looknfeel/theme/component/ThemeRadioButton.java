package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.RADIO_OFF_IMG;
import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.RADIO_ON_IMG;
import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import edu.asu.jmars.ui.image.factory.ImageFactory;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeRadioButton implements ThemeComponent {

    private static String catalogKey = "radiobutton";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeRadioButton());
    }

    public ThemeRadioButton() {
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getOffColor() {
        return ThemeProvider.getInstance().getAction().getImageOff();
    }

    public String getOnColor() {
        return ThemeProvider.getInstance().getAction().getImageOn();
    }

    @Override
    public void configureUI() {        
        UIManager.put("RadioButton.font", ThemeFont.getRegular());
        UIManager.put("RadioButton.background", parse(this.getBackground()));
        UIManager.put("RadioButton.foreground", parse(this.getForeground()));
        UIManager.put("RadioButton.icon",
                new ImageIcon(ImageFactory.createImage(RADIO_OFF_IMG.withDisplayColor(parse(this.getOffColor())))));
        UIManager.put("RadioButton.selectedIcon",
                new ImageIcon(ImageFactory.createImage(RADIO_ON_IMG.withDisplayColor(parse(this.getOnColor())))));

    }

}
