package edu.asu.jmars.layer.planning;

import java.util.List;

public class SpacecraftCommand {

	private String commandName;
	private int    commandCode;
	private String commandMnemonic;
	private int    hardwareID;
	List<CommandParameter> parameters;

	public SpacecraftCommand(String name, int code, String mnemonic, int hwId, List<CommandParameter> parms) {
		
		commandName = name;
		commandCode = code;
		commandMnemonic = mnemonic;
		hardwareID = hwId;
		parameters = parms;
	}

	/**
	 * @return the commandName
	 */
	public String getCommandName() {
		return commandName;
	}
	/**
	 * @return the commandCode
	 */
	public int getCommandCode() {
		return commandCode;
	}
	/**
	 * @return the commandMnemonic
	 */
	public String getCommandMnemonic() {
		return commandMnemonic;
	}
	/**
	 * @return the hardwareID
	 */
	public int getHardwareID() {
		return hardwareID;
	}
	/**
	 * @return the parameters
	 */
	public List<CommandParameter> getParameters() {
		return parameters;
	}

}
