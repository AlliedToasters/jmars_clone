package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeMenuItem implements ThemeComponent {

    private static String catalogKey = "menuitem";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeMenuItem());
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
        return ThemeProvider.getInstance().getSelection().getForeground();
    }

    public String getDisabledforeground() {
        return ThemeProvider.getInstance().getText().getDisabled();
    }

    public String getAcceleratorforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getAcceleratorselectionforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public ThemeMenuItem() {
    }

    @Override
    public void configureUI() {       
        UIManager.put("MenuItem.font", ThemeFont.getBold());
        UIManager.put("MenuItem.border", BorderFactory.createEmptyBorder());
        UIManager.put("MenuItem.acceleratorFont", ThemeFont.getRegular());
        UIManager.put("MenuItem.selectionBackground", parse(this.getSelectionbackground()));
        UIManager.put("MenuItem.border", BorderFactory.createEmptyBorder(5, 0, 5, 0));
        UIManager.put("MenuItem.background", parse(this.getBackground()));
        UIManager.put("MenuItem.foreground", parse(this.getForeground()));
        UIManager.put("MenuItem.disabledForeground", parse(this.getDisabledforeground()));
        UIManager.put("MenuItem.acceleratorForeground", parse(this.getAcceleratorforeground()));
        UIManager.put("MenuItem.acceleratorSelectionForeground", parse(this.getAcceleratorselectionforeground()));
    }

}
