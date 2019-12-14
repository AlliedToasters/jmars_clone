package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.CHECK_OFF_IMG;
import static edu.asu.jmars.ui.image.factory.ImageCatalogItem.CHECK_ON_IMG;
import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import edu.asu.jmars.ui.image.factory.ImageFactory;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.ThemeFont.FONTS;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeTable implements ThemeComponent {

    private static String catalogKey = "table";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeTable());
    }

    public ThemeTable() {
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getSelectionbackground() {
        return ThemeProvider.getInstance().getSelection().getMain();
    }

    public String getSelectionforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public boolean isFocusable() {
        return ThemeProvider.getInstance().getSettings().isTableFocusable();
    }

    public String getGridcolor() {
        return ThemeProvider.getInstance().getRow().getBorder();
    }

    public int getRowheight() {
        return ThemeProvider.getInstance().getSettings().getTableRowHeight();
    }

    public boolean isAlternaterowcolor() {
        return ThemeProvider.getInstance().getSettings().isTableAlternateRowColor();
    }

    public String getAlternaterowbackground() {
        return ThemeProvider.getInstance().getRow().getAlternate();
    }

    public String getCheckon() {
        return ThemeProvider.getInstance().getAction().getImageOn();
    }

    public String getCheckoff() {
        return ThemeProvider.getInstance().getAction().getImageOff();
    }

    public Color getSpecialDataHighlight() {
        return parse(ThemeProvider.getInstance().getText().getHighlight());
    }

    @Override
    public void configureUI() {        
        UIManager.put("Table.font", ThemeFont.getRegular().deriveFont(FONTS.ROBOTO_TABLE.fontSize()));
        UIManager.put("Table.selectionBackground", parse(this.getSelectionbackground()));
        UIManager.put("Table.selectionForeground", parse(this.getSelectionforeground()));
        UIManager.put("Table.foreground", parse(this.getForeground()));
        UIManager.put("Table.background", parse(this.getBackground()));
        UIManager.put("Table.focusable", this.isFocusable());
        UIManager.put("Table.gridColor", parse(this.getGridcolor()));
        UIManager.put("Table[CheckBox].checked",
                new ImageIcon(ImageFactory.createImage(CHECK_ON_IMG.withDisplayColor(parse(this.getCheckon())))));
        UIManager.put("Table[CheckBox].unchecked",
                new ImageIcon(ImageFactory.createImage(CHECK_OFF_IMG.withDisplayColor(parse(this.getCheckoff())))));
        UIManager.put("Table.rowHeight", this.getRowheight());
        UIManager.put("Table.alternateRowColor", this.isAlternaterowcolor());
        UIManager.put("Table.alternateRowBackground", parse(this.getAlternaterowbackground()));
    }

}
