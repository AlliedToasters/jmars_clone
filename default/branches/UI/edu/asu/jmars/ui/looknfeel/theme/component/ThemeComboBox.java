package edu.asu.jmars.ui.looknfeel.theme.component;

import static edu.asu.jmars.ui.looknfeel.Utilities.parse;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import edu.asu.jmars.ui.looknfeel.ThemeProvider;
import edu.asu.jmars.ui.looknfeel.GUITheme;
import edu.asu.jmars.ui.looknfeel.ThemeFont;
import edu.asu.jmars.ui.looknfeel.theme.ThemeComponent;
import mdlaf.utils.MaterialBorders;


public class ThemeComboBox implements ThemeComponent {

	private static String catalogKey = "combobox";

	static {
		GUITheme.getCatalog().put(catalogKey, new ThemeComboBox());
	}

	public ThemeComboBox() {
	}

	public static String getCatalogKey() {
		return catalogKey;
	}

	public String getSelectedindropdownbackground() {
		return ThemeProvider.getInstance().getBackground().getHighlight();
	}

	public String getLineborder() {
		return ThemeProvider.getInstance().getAction().getMain();
	}

	public String getEmptyborder() {
		return ThemeProvider.getInstance().getBlack();
	}

	public String getButtonborder() {
		return ThemeProvider.getInstance().getBackground().getMain();
	}

	public String getBackground() {
		return ThemeProvider.getInstance().getBackground().getMain();
	}

	public String getForeground() {
		return ThemeProvider.getInstance().getText().getMain();
	}

	public String getSelectionbackground() {
		return ThemeProvider.getInstance().getBackground().getMain();
	}

	public String getSelectionforeground() {
		return ThemeProvider.getInstance().getText().getMain();
	}

	public String getDisabledback() {
		return ThemeProvider.getInstance().getBackground().getContrast();
	}

	public String getDisabledforeground() {
		return ThemeProvider.getInstance().getText().getDisabled();
	}

	public String getMousehover() {
		return ThemeProvider.getInstance().getBackground().getMain();
	}

	public boolean isEnablehover() {
		return ThemeProvider.getInstance().getSettings().isComboboxEnableHover() ;
	}

	public String getUnfocus() {
		return ThemeProvider.getInstance().getAction().getImageOn();
	}

	public String getFocus() {
		return ThemeProvider.getInstance().getAction().getMain();
	}

	public String getButtonbackground() {
		return ThemeProvider.getInstance().getBackground().getMain();
	}

	public String getBorder() {
		return ThemeProvider.getInstance().getBackground().getSecondaryBorder();
	}
	
	public String getItemSelectionforeground() {
		return ThemeProvider.getInstance().getSelection().getForegroundContrast();
	}

	@Override
	public void configureUI() {	    
		UIManager.put("ComboBox.font", ThemeFont.getRegular());
		UIManager.put("ComboBox.selectedInDropDownBackground", parse(this.getSelectedindropdownbackground()));
		UIManager.put("ComboBox.background", parse(this.getBackground()));
		UIManager.put("ComboBox.foreground", parse(this.getForeground()));
		UIManager.put("ComboBox.selectionBackground", parse(this.getSelectionbackground()));
		UIManager.put("ComboBox.selectionForeground", parse(this.getSelectionforeground()));
		UIManager.put("ComboBox.disabledBackground", parse(this.getDisabledback()));
		UIManager.put("ComboBox.disabledForeground", parse(this.getDisabledforeground()));
		UIManager.put("ComboBox.mouseHoverColor", parse(this.getMousehover()));
		UIManager.put("ComboBox.mouseHoverEnabled", this.isEnablehover());
		UIManager.put("ComboBox.unfocusColor", parse(this.getUnfocus()));
		UIManager.put("ComboBox.focusColor", parse(this.getFocus()));
		UIManager.put("ComboBox[button].border", BorderFactory.createLineBorder(parse(this.getButtonborder())));
		UIManager.put("ComboBox.buttonBackground", parse(this.getButtonbackground()));		
		UIManager.put("ComboBox.borderItems", BorderFactory.createEmptyBorder(2, 2, 2 , 2));
		UIManager.put("ComboBox[item].selectionForeground", parse(this.getItemSelectionforeground()));
		UIManager.put("ComboBox.border", MaterialBorders.roundedLineColorBorder(parse(this.getBorder())));
		//UIManager.put("ComboBox.border", BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(parse(this.getLineborder())),BorderFactory.createEmptyBorder()));
	}

}
