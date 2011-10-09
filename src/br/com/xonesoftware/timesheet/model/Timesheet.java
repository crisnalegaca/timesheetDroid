package br.com.xonesoftware.timesheet.model;

import java.io.Serializable;

/**
 * @author cris
 *
 */
public class Timesheet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String input;
	private String outputLunch;
	private String inputLunch;
	private String output;

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutputLunch() {
		return outputLunch;
	}

	public void setOutputLunch(String outputLunch) {
		this.outputLunch = outputLunch;
	}

	public String getInputLunch() {
		return inputLunch;
	}

	public void setInputLunch(String inputLunch) {
		this.inputLunch = inputLunch;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String toString() {
		String[] tokens_input = input.split(" ");
		String day = tokens_input[0];
		String inputHour = tokens_input[1];
		String outputLunchHour = "";
		if(outputLunch != null){
			String[] tokens_outputLunch = outputLunch.split(" ");
			outputLunchHour = tokens_outputLunch[1];
		}
		String inputLunchHour = "";
		if(inputLunch != null){
			String[] tokens_inputLunch = inputLunch.split(" ");
			inputLunchHour = tokens_inputLunch[1];
		}
		String outputHour = "";
		if(output != null){
			String[] tokens_output = output.split(" ");
			outputHour = tokens_output[1];
		}

		return day + ": " + inputHour + " - " + outputLunchHour + " - "
				+ inputLunchHour + " - " + outputHour;
	}
}
