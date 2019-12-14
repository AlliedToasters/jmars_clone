package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.FILE_DETAILS_IMG;
import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.FILE_LIST_IMG;
import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import edu.asu.jmars.ui.image.factory.ImageFactory;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeFileChooser implements ThemeComponent {

    private static String catalogKey = "filechooser";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeFileChooser());
    }

    public ThemeFileChooser() {
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getListimgcolor() {
        return ThemeProvider.getInstance().getImage().getFill();
    }

    public String getDetailsimgcolor() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public int getImgscaledwidth() {
        return ThemeProvider.getInstance().getSettings().getFilechooserImgScaledWidth();
    }

    public int getImgscaledheight() {
        return ThemeProvider.getInstance().getSettings().getFilechooserImgScaledHeight();
    }

    @Override
    public void configureUI() {       
        UIManager.put("FileChooser.font", ThemeFont.getBold());
        UIManager.put("FileChooser[icons].list",
                new ImageIcon(ImageFactory.createImage(FILE_LIST_IMG.withDisplayColor(parse(this.getListimgcolor())))
                        .getScaledInstance(this.getImgscaledwidth(), this.getImgscaledheight(), Image.SCALE_SMOOTH)));
        UIManager.put("FileChooser[icons].details",
                new ImageIcon(ImageFactory.createImage(FILE_DETAILS_IMG.withDisplayColor(parse(this.getListimgcolor())))
                        .getScaledInstance(this.getImgscaledwidth(), this.getImgscaledheight(), Image.SCALE_SMOOTH)));
    }
}
