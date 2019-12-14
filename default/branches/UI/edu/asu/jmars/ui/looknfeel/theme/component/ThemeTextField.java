package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeTextField implements ThemeComponent {

    private static String catalogKey = "textfield";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeTextField());
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getSelectionbackground() {
        return ThemeProvider.getInstance().getBackground().getHighlight();
    }

    public String getSelectionforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getContrast();
    }

    public String getCaretforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getInactiveforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getInactivebackground() {
        return ThemeProvider.getInstance().getBackground().getContrast();        	
    }

    public String getLineactive() {
        return ThemeProvider.getInstance().getAction().getLineActive();
    }

    public String getLineinactive() {
        return ThemeProvider.getInstance().getAction().getLineInactive();
    }

    public ThemeTextField() {
    }

    @Override
    public void configureUI() {        
        UIManager.put("TextField.font", ThemeFont.getRegular());
        UIManager.put("TextField.background", parse(this.getBackground()));
        UIManager.put("TextField.foreground", parse(this.getForeground()));
        UIManager.put("TextField.caretForeground", parse(this.getCaretforeground()));
        UIManager.put("TextField.selectionForeground", parse(this.getSelectionforeground()));
        UIManager.put("TextField.inactiveForeground", parse(this.getInactiveforeground()));
        UIManager.put("TextField.inactiveBackground", parse(this.getInactivebackground()));
        UIManager.put("TextField.selectionBackground", parse(this.getSelectionbackground()));
        UIManager.put("TextField[Line].inactiveColor", parse(this.getLineinactive()));
        UIManager.put("TextField[Line].activeColor", parse(this.getLineactive()));
    }

}
