package br.com.xonesoftware.timesheet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import br.com.xonesoftware.timesheet.dao.TimesheetDAO;
import br.com.xonesoftware.timesheet.model.Timesheet;

/**
 * @author cris
 * Tela - Edita um registro
 *
 */
public class EditRecordActivity extends Activity {

	Timesheet record;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Habilita novos recursos na janela
		requestWindowFeature(Window.FEATURE_LEFT_ICON);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_record);

		// Adiciona um ’cone a esquerda
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.timesheet_menu);

		final TimePicker inputView = (TimePicker) findViewById(R.id.input);
		final TimePicker outputLunchView = (TimePicker) findViewById(R.id.outputLunch);
		final CheckBox enableOutputLunchView = (CheckBox) findViewById(R.id.enableOutputLunch);
		final TimePicker inputLunchView = (TimePicker) findViewById(R.id.inputLunch);
		final TimePicker outputView = (TimePicker) findViewById(R.id.output);

		record = (Timesheet) getIntent().getSerializableExtra("recordSelected");

		String[] tokens_input = record.getInput().split(" ");
		final String day = tokens_input[0];
		String inputHour = tokens_input[1];
		inputView.setCurrentHour(Integer.parseInt(inputHour.split(":")[0]));
		inputView.setCurrentMinute(Integer.parseInt(inputHour.split(":")[1]));

		if (record.getOutputLunch() != null) {
			String[] tokens_outputLunch = record.getOutputLunch().split(" ");
			String outputLunchHour = tokens_outputLunch[1];
			outputLunchView.setCurrentHour(Integer.parseInt(outputLunchHour
					.split(":")[0]));
			outputLunchView.setCurrentMinute(Integer.parseInt(outputLunchHour
					.split(":")[1]));
			enableOutputLunchView.setChecked(true);
		} else {
			outputLunchView.setEnabled(false);
			enableOutputLunchView.setChecked(false);
		}

		if (record.getInputLunch() != null) {
			String[] tokens_inputLunch = record.getInputLunch().split(" ");
			String inputLunchHour = tokens_inputLunch[1];
			inputLunchView.setCurrentHour(Integer.parseInt(inputLunchHour
					.split(":")[0]));
			inputLunchView.setCurrentMinute(Integer.parseInt(inputLunchHour
					.split(":")[1]));
		} else {
			inputLunchView.setEnabled(false);
		}

		if (record.getOutput() != null) {
			String[] tokens_output = record.getOutput().split(" ");
			String outputHour = tokens_output[1];
			outputView
					.setCurrentHour(Integer.parseInt(outputHour.split(":")[0]));
			outputView
					.setCurrentMinute(Integer.parseInt(outputHour.split(":")[1]));
		} else {
			outputView.setEnabled(false);
		}

		TextView dayView = (TextView) findViewById(R.id.day);
		dayView.setText("Registro do dia: " + day);

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				record.setInput(day + " "
						+ inputView.getCurrentHour().toString() + ":"
						+ inputView.getCurrentMinute().toString());
				Log.i("teste", record.getInput());
				if (outputLunchView.isEnabled()) {
					record.setOutputLunch(day + " "
							+ outputLunchView.getCurrentHour().toString() + ":"
							+ outputLunchView.getCurrentMinute().toString());
					Log.i("teste", record.getOutputLunch());
				}
				if (inputLunchView.isEnabled()) {
					record.setInputLunch(day + " "
							+ inputLunchView.getCurrentHour().toString() + ":"
							+ inputLunchView.getCurrentMinute().toString());
					Log.i("teste", record.getInputLunch());
				}
				if (outputView.isEnabled()) {
					record.setOutput(day + " "
							+ outputView.getCurrentHour().toString() + ":"
							+ outputView.getCurrentMinute().toString());
					Log.i("teste", record.getOutput());
				}

				TimesheetDAO dao = new TimesheetDAO(EditRecordActivity.this);
				dao.update(record);
				dao.close();

				// TimePicker time = (TimePicker) findViewById(R.id.time);
				// Log.i("teste", time.getCurrentHour().toString());
				// Log.i("teste", time.getCurrentMinute().toString());

				finish(); // volta para activity anterior

			}
		});

		enableOutputLunchView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				outputLunchView.setEnabled(enableOutputLunchView.isChecked());
			}
		});
	}

}
