/**
 * 
 */
package edu.asu.jmars.layer.planning;

import java.util.ArrayList;
import java.util.List;

/**
 * <description>
 *
 * <intended usage>
 *
 * <external dependencies>
 *
 * <multi-thread warning>
 */
public class PlanningConfigDataPHX implements PlanningConfigData {
	
	public final int FLIR = 1;
	public final int ADCS = 2;
	public final int EPS  = 6;
	
	public final int PARAM_INT   = 0;
	public final int PARAM_FLOAT = 1;
	public final int PARAM_HEX   = 2;
	
	public final int MODE_CSS = 0;
	public final int MODE_EHS = 2;
	
	public final int EPS_FLIR    = 6;
	public final int EPS_GPS     = 8;
	public final int EPS_GPS_LNA = 2;
	
	
	private String missionName = "PHX SAT";
	
	private ArrayList<SurfaceFeature> imagingTargets;
	
	private ArrayList<SurfaceFeature> groundStations;
	
	private ArrayList<SpacecraftCommand> spacecraftCommands;

	private static PlanningConfigData instance = null;
	
	protected PlanningConfigDataPHX() {
		initializeConfigData();
	}
	
	public static PlanningConfigData getInstance() {
		if (instance == null) {
			instance = new PlanningConfigDataPHX();
		}
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.jmars.layer.planning.PlanningConfigData#initializeConfigData()
	 */
	@Override
	public void initializeConfigData() {
		
		groundStations = new ArrayList<SurfaceFeature>();		
		groundStations.add(new SurfaceFeature("SESE 1",112.0339, 33.4308));   // 111 55'42.2"W,  33 25'5.1"N
		
		imagingTargets = new ArrayList<SurfaceFeature>();
		imagingTargets.add(new SurfaceFeature("Atlanta", 85, 32));
		imagingTargets.add(new SurfaceFeature("Baltimore", 85, 32));
		imagingTargets.add(new SurfaceFeature("Chicago", 85, 32));
		imagingTargets.add(new SurfaceFeature("Houston", 85, 32));
		imagingTargets.add(new SurfaceFeature("Los Angeles", 85, 32));
		imagingTargets.add(new SurfaceFeature("Minneapolis", 85, 32));
		imagingTargets.add(new SurfaceFeature("Phoenix", 112, 33.4));
		
		// SPACECRAFT COMMANDS
		spacecraftCommands = new ArrayList<SpacecraftCommand>();
		spacecraftCommands.add(new SpacecraftCommand("Start Image Batch", FLIR, "Start image batch", 127, new ArrayList<CommandParameter>()));
		spacecraftCommands.add(new SpacecraftCommand("End Image Batch",   FLIR, "End image batch",   128, new ArrayList<CommandParameter>()));
		spacecraftCommands.add(new SpacecraftCommand("FFC",               FLIR, "FFC",               129, new ArrayList<CommandParameter>()));
		spacecraftCommands.add(new SpacecraftCommand("Take Image",        FLIR, "take image",        130, new ArrayList<CommandParameter>()));

		spacecraftCommands.add(new SpacecraftCommand("Normal-Sun Mode",   ADCS, "Normal-Sun Mode",   200, new ArrayList<CommandParameter>()));
		spacecraftCommands.add(new SpacecraftCommand("Lat/Long Sun Mode", ADCS, "Lat/Long Sun Mode", 200, new ArrayList<CommandParameter>()));
		spacecraftCommands.add(new SpacecraftCommand("Lat/Long Data",     ADCS, "Lat/Long Data",     201, new ArrayList<CommandParameter>()));
		spacecraftCommands.add(new SpacecraftCommand("Set QBO",           ADCS, "Set QBO",           202, new ArrayList<CommandParameter>()));
		
		ArrayList<CommandParameter> c208Params = new ArrayList<CommandParameter>();
		c208Params.add(new CommandParameter("Sync Byte", PARAM_HEX));
		c208Params.add(new CommandParameter("Command ID", PARAM_HEX));
		c208Params.add(new CommandParameter("Mode", PARAM_INT, Integer.toString(MODE_EHS)));
		c208Params.add(new CommandParameter("Filler", PARAM_HEX, "0x00"));
		c208Params.add(new CommandParameter("checksum", PARAM_HEX));
		spacecraftCommands.add(new SpacecraftCommand("Set Attitude Mode - EHS & MAG", ADCS, "Set Attitude Mode - EHS&MAG", 208, c208Params));
 
		c208Params.get(2).setParameterValue(Integer.toString(MODE_CSS));
		spacecraftCommands.add(new SpacecraftCommand("Set Attitude Mode - CSS & MAG", ADCS, "Set Attitude Mode - CSS&MAG", 208, c208Params));
		spacecraftCommands.add(new SpacecraftCommand("SetRV", ADCS, "SetRV", 202, new ArrayList<CommandParameter>()));

		ArrayList<CommandParameter> c600Params = new ArrayList<CommandParameter>();
		c600Params.add(new CommandParameter("Switch", PARAM_INT, Integer.toString(EPS_GPS)));
		spacecraftCommands.add(new SpacecraftCommand("GPS On",        EPS, "GPS On",        600, c600Params));
		spacecraftCommands.add(new SpacecraftCommand("GPS Off",        EPS, "GPS Off",       601, c600Params));
		c600Params.get(0).setParameterValue(Integer.toString(EPS_GPS_LNA));
		spacecraftCommands.add(new SpacecraftCommand("GPS LNA On",        EPS, "GPS LNA On",        600, c600Params));
		spacecraftCommands.add(new SpacecraftCommand("GPS LNA Off",        EPS, "GPS LNA Off",        601, c600Params));
		c600Params.get(0).setParameterValue(Integer.toString(EPS_FLIR));
		spacecraftCommands.add(new SpacecraftCommand("FLIR On",        EPS, "FLIR On",        600, c600Params));
		spacecraftCommands.add(new SpacecraftCommand("FLIR Off",        EPS, "FLIR Off",        601, c600Params));
		
	}

	/* (non-Javadoc)
	 * @see edu.asu.jmars.layer.planning.PlanningConfigData#getMissionName()
	 */
	@Override
	public String getMissionName() {
		return missionName;
	}

	/* (non-Javadoc)
	 * @see edu.asu.jmars.layer.planning.PlanningConfigData#getTargets()
	 */
	@Override
	public List<SurfaceFeature> getTargets() {
		return imagingTargets;
	}

	/* (non-Javadoc)
	 * @see edu.asu.jmars.layer.planning.PlanningConfigData#getGroundStations()
	 */
	@Override
	public List<SurfaceFeature> getGroundStations() {
		return groundStations;
	}

	/* (non-Javadoc)
	 * @see edu.asu.jmars.layer.planning.PlanningConfigData#getCommands()
	 */
	@Override
	public List<SpacecraftCommand> getCommands() {
		return spacecraftCommands;
	}

}
