package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;

public class ThemeTableHeader implements ThemeComponent {

    private static String catalogKey = "tableheader";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeTableHeader());
    }

    public ThemeTableHeader() {
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getAction().getMain();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getLineborder() {
        return ThemeProvider.getInstance().getRow().getBorder();
    }

    @Override
    public void configureUI() {        
        UIManager.put("TableHeader.font",
                ThemeFont.getBold().deriveFont(ThemeFont.FONTS.ROBOTO_TABLE_HEADER.fontSize()));
        UIManager.put("TableHeader.background", parse(this.getBackground()));
        UIManager.put("TableHeader.foreground", parse(this.getForeground()));
        UIManager.put("TableHeader.cellBorder", BorderFactory
                .createCompoundBorder(new LineBorder(parse(this.getLineborder())), new EmptyBorder(5, 5, 5, 5)));

    }

}
