package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeRootPane implements ThemeComponent {

    private static String catalogKey = "rootpane";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeRootPane());
    }

    public ThemeRootPane() {
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getPlaindialogborder() {
        return ThemeProvider.getInstance().getBackground().getSecondaryBorder();
    }

    public String getInformationdialogborder() {
        return ThemeProvider.getInstance().getBackground().getSecondaryBorder();
    }

    public String getErrordialogborder() {
        return ThemeProvider.getInstance().getBackground().getSecondaryBorder();
    }

    public String getFilechooserdialogborder() {
        return ThemeProvider.getInstance().getBackground().getSecondaryBorder();
    }

    public String getQuestiondialogborder() {
        return ThemeProvider.getInstance().getBackground().getSecondaryBorder();
    }

    public String getWarningdialogborder() {
        return ThemeProvider.getInstance().getBackground().getSecondaryBorder();
    }

    public String getColorchooserdialogborder() {
        return ThemeProvider.getInstance().getBackground().getSecondaryBorder();
    }

    @Override
    public void configureUI() {        
        UIManager.put("RootPane.background", parse(this.getBackground()));
        UIManager.put("RootPane.plainDialogBorder", BorderFactory.createLineBorder(parse(this.getPlaindialogborder())));
        UIManager.put("RootPane.informationDialogBorder",
                BorderFactory.createLineBorder(parse(this.getInformationdialogborder())));
        UIManager.put("RootPane.errorDialogBorder", BorderFactory.createLineBorder(parse(this.getErrordialogborder())));
        UIManager.put("RootPane.fileChooserDialogBorder",
                BorderFactory.createLineBorder(parse(this.getFilechooserdialogborder())));
        UIManager.put("RootPane.questionDialogBorder",
                BorderFactory.createLineBorder(parse(this.getQuestiondialogborder())));
        UIManager.put("RootPane.warningDialogBorder",
                BorderFactory.createLineBorder(parse(this.getWarningdialogborder())));
        UIManager.put("RootPane.colorChooserDialogBorder",
                BorderFactory.createLineBorder(parse(this.getColorchooserdialogborder())));

    }

}
