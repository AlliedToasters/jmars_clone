package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;

public class ThemeTextArea implements ThemeComponent {

    private static String catalogKey = "textarea";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeTextArea());
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getSelectionbackground() {
        return ThemeProvider.getInstance().getBackground().getContrast();
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

    public ThemeTextArea() {
    }

    @Override
    public void configureUI() {        
        UIManager.put("TextArea.font", ThemeFont.getRegular());
        UIManager.put("TextArea.foreground", parse(this.getForeground()));
        UIManager.put("TextArea.border", BorderFactory.createEmptyBorder());
        UIManager.put("TextArea.background", parse(this.getBackground()));
        UIManager.put("TextArea.caretForeground", parse(this.getCaretforeground()));
        UIManager.put("TextArea.selectionForeground", parse(this.getSelectionforeground()));
        UIManager.put("TextArea.inactiveForeground", parse(this.getInactiveforeground()));
        UIManager.put("TextArea.selectionBackground", parse(this.getSelectionbackground()));
    }

}
