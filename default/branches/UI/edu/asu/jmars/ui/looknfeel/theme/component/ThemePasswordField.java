package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemePasswordField implements ThemeComponent {

    private static String catalogKey = "passwordfield";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemePasswordField());
    }

    public ThemePasswordField() {
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getContrast();
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getCaretforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getSelectionforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getInactiveforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    public String getInactivebackground() {
        return ThemeProvider.getInstance().getBackground().getContrast();
    }

    public String getSelectionbackground() {
        return ThemeProvider.getInstance().getBackground().getHighlight();
    }

    public String getLineinactivecolor() {
        return ThemeProvider.getInstance().getAction().getLineInactive();
    }

    public String getLineactivecolor() {
        return ThemeProvider.getInstance().getAction().getLineActive();
    }

    @Override
    public void configureUI() {        
        UIManager.put("PasswordField.background", parse(this.getBackground()));
        UIManager.put("PasswordField.foreground", parse(this.getForeground()));
        UIManager.put("PasswordField.caretForeground", parse(this.getCaretforeground()));
        UIManager.put("PasswordField.selectionForeground", parse(this.getSelectionforeground()));
        UIManager.put("PasswordField.inactiveForeground", parse(this.getInactiveforeground()));
        UIManager.put("PasswordField.inactiveBackground", parse(this.getInactivebackground()));
        UIManager.put("PasswordField.selectionBackground", parse(this.getSelectionbackground()));
        UIManager.put("PasswordField[Line].inactiveColor", parse(this.getLineinactivecolor()));
        UIManager.put("PasswordField[Line].activeColor", parse(this.getLineactivecolor()));
    }

}
