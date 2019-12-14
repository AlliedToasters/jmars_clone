package edu.asu.jmars.layer.planning;

public class CommandParameter {

	private String parameterName;
	private int dataType;
	private String parameterValue = "";
	
	public CommandParameter(String name, int type) {
		parameterName = name;
		dataType = type;
	}

	public CommandParameter(String name, int type, String value) {
		parameterName = name;
		dataType = type;
		parameterValue = value; 
	}
	

	/**
	 * @return the parameterValue
	 */
	public String getParameterValue() {
		return parameterValue;
	}

	/**
	 * @param parameterValue the parameterValue to set
	 */
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * @return the dataType
	 */
	public int getDataType() {
		return dataType;
	}

	

}
