package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.RADIO_OFF_IMG;
import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.RADIO_ON_IMG;
import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import edu.asu.jmars.ui.image.factory.ImageFactory;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeRadioButtonMenuItem implements ThemeComponent {

    private static String catalogKey = "radiobuttonmenuitem";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeRadioButtonMenuItem());
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getSelectionbackground() {
        return ThemeProvider.getInstance().getBackground().getHighlight();
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getRow().getAlternate();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getRadioiconimgcolor() {
        return ThemeProvider.getInstance().getImage().getFill();
    }

    public String getSelectedradioiconimgcolor() {
        return ThemeProvider.getInstance().getImage().getFill();
    }

    public String getAcceleratorforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getAcceleratorselectionforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public ThemeRadioButtonMenuItem() {
    }

    @Override
    public void configureUI() {        
        UIManager.put("RadioButtonMenuItem.font", ThemeFont.getBold());
        UIManager.put("RadioButtonMenuItem.selectionBackground", parse(this.getSelectionbackground()));
        UIManager.put("RadioButtonMenuItem.border", BorderFactory.createEmptyBorder(5, 0, 5, 0));
        UIManager.put("RadioButtonMenuItem.background", parse(this.getBackground()));
        UIManager.put("RadioButtonMenuItem.foreground", parse(this.getForeground()));
        UIManager.put("RadioButtonMenuItem.checkIcon", new ImageIcon(
                ImageFactory.createImage(RADIO_OFF_IMG.withDisplayColor(parse(this.getRadioiconimgcolor())))));
        UIManager.put("RadioButtonMenuItem.selectedCheckIcon", new ImageIcon(
                ImageFactory.createImage(RADIO_ON_IMG.withDisplayColor(parse(this.getSelectedradioiconimgcolor())))));
        UIManager.put("RadioButtonMenuItem.acceleratorForeground", parse(this.getAcceleratorforeground()));
        UIManager.put("RadioButtonMenuItem.acceleratorSelectionForeground",
                parse(this.getAcceleratorselectionforeground()));
    }

}
