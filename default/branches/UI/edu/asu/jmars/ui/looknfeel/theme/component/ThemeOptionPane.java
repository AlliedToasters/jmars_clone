package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeOptionPane implements ThemeComponent {

    private static String catalogKey = "optionpane";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeOptionPane());
    }

    public ThemeOptionPane() {
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getMessageforeground() {
        return ThemeProvider.getInstance().getText().getMain();
    }

    @Override
    public void configureUI() {       
        UIManager.put("OptionPane.font", ThemeFont.getBold());
        UIManager.put("OptionPane.background", parse(this.getBackground()));
        UIManager.put("OptionPane.messageForeground", parse(this.getMessageforeground()));
        UIManager.put("OptionPane.font", ThemeFont.getRegular());
        UIManager.put("OptionPane.border", BorderFactory.createEmptyBorder(10, 10, 10, 10));
        UIManager.put("OptionPane.cancelButtonText", "Cancel".toUpperCase());
        UIManager.put("OptionPane.okButtonText", "OK".toUpperCase());
        UIManager.put("OptionPane.yesButtonText", "Yes".toUpperCase());
        UIManager.put("OptionPane.noButtonText", "No".toUpperCase());
    }

}
