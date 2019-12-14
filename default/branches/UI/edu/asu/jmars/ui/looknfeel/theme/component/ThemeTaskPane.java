package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.COLLAPSED_IMG;
import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.EXPANDED_IMG;
import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import edu.asu.jmars.ui.image.factory.ImageFactory;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeTaskPane implements ThemeComponent {

    private static String catalogKey = "taskpane";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeTaskPane());
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getAlternateContrast();
    }

    public String getContentbackground() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public String getTitlebackgroundgradientstart() {
        return ThemeProvider.getInstance().getBackground().getAlternateContrast();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getTitleover() {
        return ThemeProvider.getInstance().getBackground().getHighlightContrast();
    }

    public String getYescollapsedimgcolor() {
        return ThemeProvider.getInstance().getImage().getFill();
    }

    public String getNocollapsedimgcolor() {
        return ThemeProvider.getInstance().getImage().getFill();
    }

    public ThemeTaskPane() {
    }

    @Override
    public void configureUI() {       
        UIManager.put("TaskPane.font", ThemeFont.getRegular());
        UIManager.put("TaskPane.contentBackground", parse(this.getContentbackground()));
        UIManager.put("TaskPane.titleBackgroundGradientStart", parse(this.getTitlebackgroundgradientstart()));
        UIManager.put("TaskPane.foreground", parse(this.getForeground()));
        UIManager.put("TaskPane.titleOver", parse(this.getTitleover()));
        UIManager.put("TaskPane.yesCollapsed", new ImageIcon(
                ImageFactory.createImage(COLLAPSED_IMG.withDisplayColor(parse(this.getYescollapsedimgcolor())))));
        UIManager.put("TaskPane.noCollapsed", new ImageIcon(
                ImageFactory.createImage(EXPANDED_IMG.withDisplayColor(parse(this.getNocollapsedimgcolor())))));
        UIManager.put("TaskPane.background", parse(this.getBackground()));

    }

}
