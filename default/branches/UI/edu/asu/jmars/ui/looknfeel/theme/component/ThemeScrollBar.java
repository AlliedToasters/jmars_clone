package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeScrollBar implements ThemeComponent {

    private static String catalogKey = "scrollbar";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeScrollBar());
    }

    public ThemeScrollBar() {
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public int getWidth() {
        return ThemeProvider.getInstance().getSettings().getScrollbarWidth();
    }

    public String getTrackcolor() {
        return ThemeProvider.getInstance().getBackground().getContrast();
    }

    public String getThumbcolor() {
        return ThemeProvider.getInstance().getBackground().getScrollbar();
    }

    @Override
    public void configureUI() {       
        UIManager.put("ScrollBar.width", this.getWidth());
        UIManager.put("ScrollBar.track", parse(this.getTrackcolor()));
        UIManager.put("ScrollBar.thumb", parse(this.getThumbcolor()));
        UIManager.put("ScrollBar.enableArrow", false);
    }

}
