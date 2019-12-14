package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.UIManager;
import org.jdesktop.swingx.painter.MattePainter;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;

public class ThemeTaskPaneContainer implements ThemeComponent {

    private static String catalogKey = "taskpanecontainer";

    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeTaskPaneContainer());
    }

    public static String getCatalogKey() {
        return catalogKey;
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public String getBackgroundpainter() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public ThemeTaskPaneContainer() {
    }

    @Override
    public void configureUI() {        
        UIManager.put("TaskPaneContainer.background", parse(this.getBackground()));
        UIManager.put("TaskPaneContainer.backgroundPainter", new MattePainter(parse(this.getBackgroundpainter())));
    }

}
