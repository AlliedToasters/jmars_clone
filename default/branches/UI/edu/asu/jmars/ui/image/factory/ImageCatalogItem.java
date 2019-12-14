package edu.asu.jmars.ui.image.factory;

import java.awt.Color;
import java.util.Optional;
import static edu.asu.jmars.ui.image.factory.SvgConverter.*;

public enum ImageCatalogItem implements ImageDescriptor {	

MOUSE_POINTER_IMG("cursor.svg"),
MOUSE_POINTER_IMG_SEL("cursor_sel.svg"),	
PAN_HAND_IMG("hand.svg"),
PAN_HAND_IMG_SEL("hand_sel.svg"),
ZOOM_IN_IMG("zoomin.svg"),
ZOOM_IN_IMG_SEL("zoomin_sel.svg"),
ZOOM_OUT_IMG("zoomout.svg"),
ZOOM_OUT_IMG_SEL("zoomout_sel.svg"),
RULER_IMG("ruler.svg"),
RULER_IMG_SEL("ruler_sel.svg"),
INVESTIGATE_IMG("investigate.svg"),
INVESTIGATE_IMG_SEL("investigate_sel.svg"),
EXPORT_IMG("export.svg"),
EXPORT_IMG_SEL("export_sel.svg"),
RESIZE_IMG("resize.svg"),
RESIZE_IMG_SEL("resize_sel.svg"),
FACEBOOK_IMG("facebook.svg"),
TWITTER_IMG("twitter.svg"),
LAYER_STATUS("layer-status.svg"),
STAR("star.svg"),
STAMPS("stamps.svg"),
LANDING_SITE_IMG("landing-site.svg"),
CRATER_COUNT_IMG("crater-counting.svg"),
SCALE_BAR_IMG("scalebar.svg"),
THREE_D_LAYER_IMG("cube2.svg"),
GRID_LAYER_IMG("grid.svg"),
GROUNDTRACK_LAYER_IMG("groundtrack.svg"),
KRC_LAYER_IMG("KRC.svg"),
MAP_LAYER_IMG("Map.svg"),
MCD_LAYER_IMG("MCD.svg"),
MOSAICS_LAYER_IMG("mosaics.svg"),
NOMENCLATURE_LAYER_IMG("nomenclature.svg"),
NORTH_LAYER_IMG("north-arrow.svg"),
ROI_LAYER_IMG("region-of-interest.svg"),
SHAPE_LAYER_IMG("shape.svg"),
STREETS_LAYER_IMG("street.svg"),
TES_LAYER_IMG("tes.svg"),
SLIDER_LAYER_IMG("timeslider.svg"),
TOGGLE_ON_IMG("toggle-on.svg"),
TOGGLE_OFF_IMG("toggle-off.svg"),
RIGHT_ARROW_IMG("arrow-right.svg"),
LEFT_ARROW_IMG("arrow-left.svg"),
INFO_IMG("info-circle.svg"),
CARET_DOWN_IMG("caret-down.svg"),
CURSOR_EXPORT("export-cursor.svg"),
CURSOR_INVESTIGATE("investigate-cursor.svg"),
CURSOR_PAN_GRAB("pangrab-cursor.svg"),
CURSOR_PAN("pan-cursor.svg"),
CURSOR_ZOOM_IN("zoomin-cursor.svg"),
CURSOR_ZOOM_OUT("zoomout-cursor.svg"),
CURSOR_RULER("ruler-cursor.svg"),
M_LOGO_IMG("label-map.svg"),
P_LOGO_IMG("label-pan.svg"),
LOCATION_IMG("location-arrow.svg"),
RIGHT_LINK_IMG("chevron-right.svg"),
ACTIVE_FILE_IMG("active-file.svg"),
EXTERNAL_LINK_IMG("external-link.svg"),
CHECK_ON_IMG("checked.svg"),
CHECK_OFF_IMG("unchecked.svg"),
FILE_DETAILS_IMG("file-details.svg"),
FILE_LIST_IMG("file-list.svg"),
COLLAPSED_IMG("collapsed.svg"), //">"
EXPANDED_IMG("expanded.svg"),  //"down"
RADIO_ON_IMG("radio-on.svg"),
RADIO_OFF_IMG("radio-off.svg"),
SYNC_IMG("sync-light.svg"),
JMARS("Jicon.svg"),
PAN_1("pan_1.svg"),
PAN_2("pan_2.svg"),
PAN_5("pan_5.svg"),
PAN_10("pan_10.svg"),
PAN_E("pan_e.svg"),
PAN_N("pan_n.svg"),
PAN_NE("pan_ne.svg"),
PAN_NW("pan_nw.svg"),
PAN_S("pan_s.svg"),
PAN_SE("pan_se.svg"),
PAN_SW("pan_sw.svg"),
PAN_W("pan_w.svg"),
DOT("dot.svg");


	
private String imgSourceFile;	
private String PATH_TO_MATERIAL_SVG = "/resources/material/images/svg/";
private Optional<Color> displayColor = Optional.empty();
private Optional<Color> strokeColor = Optional.empty();
private String conversionFormat = PNG.name();

private ImageCatalogItem(String filepath) {
	this.imgSourceFile = PATH_TO_MATERIAL_SVG + filepath;
	this.conversionFormat = PNG.name();
}

@Override
public String getImageFilePath() {
	return this.imgSourceFile;
}

@Override
public ImageDescriptor withDisplayColor(Color fill) {
	displayColor = Optional.ofNullable(fill);
	return this;
}

@Override
public ImageDescriptor withStrokeColor(Color stroke) {
	strokeColor = Optional.ofNullable(stroke);
	return this;
}

@Override
public ImageDescriptor as(SvgConverter format) {
	conversionFormat = format.name();
	return this;
}		

@Override
public Optional<Color> getDisplayColor() {			
	return displayColor;
} 

@Override
public Optional<Color> getStrokeColor() {			
	return strokeColor;
}

@Override
public String getConversionFormat() {			
	return conversionFormat;
}

}