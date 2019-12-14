package edu.asu.jmars.layer.map2.stages.composite;

import edu.asu.jmars.layer.map2.AbstractStageSettings;
import edu.asu.jmars.layer.map2.Stage;
import edu.asu.jmars.layer.map2.StageView;
import edu.asu.jmars.layer.map2.stages.DummyStageView;

public class RGBCompositeSettings extends AbstractStageSettings {

	public static final String stageName = "RGB";

	public Stage createStage() {
		return new RGBComposite(this);
	}

	public StageView createStageView() {
		return new DummyStageView(this);
	}

	public String getStageName() {
		return stageName;
	}

}
