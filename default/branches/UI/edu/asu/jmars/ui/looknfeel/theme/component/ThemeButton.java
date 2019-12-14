package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;


public class ThemeButton implements ThemeComponent {

    private static String catalogKey = "button";
    
    static {
        GUITheme.getCatalog().put(catalogKey, new ThemeButton());
    }

    public ThemeButton() {
    }

    public Font getFont() {
        return ThemeFont.getBold();
    }

    public String getBorder() {
        return ThemeProvider.getInstance().getAction().getBorder();
    }

    public String getBackground() {
        return ThemeProvider.getInstance().getBackground().getMain();
    }

    public String getForeground() {
        return ThemeProvider.getInstance().getAction().getForeground();
    }

    public String getDisabledtext() {
        return ThemeProvider.getInstance().getAction().getDisabledForeground();
    }

    public String getDisabledback() {
        return ThemeProvider.getInstance().getAction().getDisabled();
    }

    public String getHighlight() {
        return ThemeProvider.getInstance().getAction().getContrast();
    }

    public String getDarkshadow() {
        return ThemeProvider.getInstance().getAction().getContrast();
    }

    public String getSelect() {
        return ThemeProvider.getInstance().getAction().getContrast();
    }

    public String getOnhover() {
        return ThemeProvider.getInstance().getAction().getContrast();
    }

    public boolean isEnablehover() {
        return ThemeProvider.getInstance().getSettings().isButtonEnableHover();
    }

    public boolean isDefaultbuttonfollowsfocus() {
        return ThemeProvider.getInstance().getSettings().isButtonDefaultFollowsFocus();
    }

    public boolean isFocusable() {
        return ThemeProvider.getInstance().getSettings().isButtonFocusable();
    }

    public String getDefaultback() {
        return ThemeProvider.getInstance().getAction().getMain();
    }

    public String getDefaultforeground() {
        return ThemeProvider.getInstance().getAction().getForeground();
    }

    public Color getThemebackground() {
        return parse(ThemeProvider.getInstance().getBackground().getMain());
    }

    public Color getThemehilightbackground() {
        return parse(ThemeProvider.getInstance().getBackground().getHighlight());
    }
    
    public int getButtonArcSize() {
        return ThemeProvider.getInstance().getSettings().getButtonArcSize();
    }
    
    public String getFocusColor()
    {
    	return ThemeProvider.getInstance().getBackground().getAlternateContrast();
    }

    @Override
    public void configureUI() {        
        UIManager.put("Button.font", this.getFont());
        UIManager.put("Button.background", parse(this.getBackground()));
        UIManager.put("Button.foreground", parse(this.getForeground()));      
        UIManager.put("Button.disabledText", parse(this.getDisabledtext()));
        UIManager.put("Button[disabled].background", parse(this.getDisabledback()));
        UIManager.put("Button.highlight", parse(this.getHighlight()));
        UIManager.put("Button.darkShadow", parse(this.getDarkshadow()));
        UIManager.put("Button.select", parse(this.getSelect()));
        UIManager.put("Button.mouseHoverColor", parse(this.getOnhover()));
        UIManager.put("Button.mouseHoverEnable", this.isEnablehover());
        UIManager.put("Button.defaultButtonFollowsFocus", this.isDefaultbuttonfollowsfocus());
        UIManager.put("Button.focusable", this.isFocusable());
        UIManager.put("Button[Default].background", parse(this.getDefaultback()));
        UIManager.put("Button[Default].foreground", parse(this.getDefaultforeground()));
        UIManager.put("Button[Default][focus].color", ThemeProvider.getInstance().getBlack());
        UIManager.put("Button[Default].shadowPixel", 3);
        UIManager.put("Button[Default].shadowEnable", false);
        UIManager.put("Button[Default].mouseHoverColor", parse(this.getHighlight()));
        UIManager.put("Button[focus].color", parse(this.getFocusColor()));
        UIManager.put("Button[border].enable", true);
        UIManager.put("Button[border].color", parse(this.getDefaultback()));
        UIManager.put("Button[border].toAll", false);
        UIManager.put("Button.arc", this.getButtonArcSize());
    }
}
