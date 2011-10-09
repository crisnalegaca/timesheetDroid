package br.com.xonesoftware.timesheet;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import br.com.xonesoftware.timesheet.dao.TimesheetDAO;
import br.com.xonesoftware.timesheet.model.Timesheet;

/**
 * @author cris
 * Tela - Hist—rico do per’odo selecionado.
 * 
 */
public class HistoryActivity extends Activity {

	Timesheet recordSelected;

	 public void loadList() {
	 TimesheetDAO dao = new TimesheetDAO(this);
		List<Timesheet> list = dao.getAll();
		TableLayout table = (TableLayout) findViewById(R.id.tableHistory);
		table.removeAllViews();
		 
		 for (final Timesheet timesheet : list) {
				boolean isArrival = (timesheet.getInput() != null);
				boolean isOutputLunch = (timesheet.getOutputLunch() != null);
				boolean isBackLunch = (timesheet.getInputLunch() != null);
				boolean isOutput = (timesheet.getOutput() != null);

				String date = (isArrival) ? timesheet.getInput().split(" ")[0] : "";
				String arrival = (isArrival) ? timesheet.getInput().split(" ")[1] : "";
				String outputLunch = (isOutputLunch) ? timesheet.getOutputLunch().split(" ")[1] : "";
				String backLunch = (isBackLunch) ? timesheet.getInputLunch().split(" ")[1] : "";
				String output = (isOutput) ? timesheet.getOutput().split(" ")[1] : "";
				
				TextView dateView = new TextView(this);
				dateView.setText(date);
				dateView.setTypeface(null, Typeface.BOLD_ITALIC);
				

				final TableRow row = new TableRow(this);

				TextView arrivalView = new TextView(this);
				arrivalView.setText(arrival);
				row.addView(arrivalView);

				TextView outputLunchView = new TextView(this);
				outputLunchView.setText(outputLunch);
				row.addView(outputLunchView);

				TextView backLunchView = new TextView(this);
				backLunchView.setText(backLunch);
				row.addView(backLunchView);

				TextView outputView = new TextView(this);
				outputView.setText(output);
				row.addView(outputView);
				
				TextView sum = new TextView(this);
				sum.setText("XX:XX");
				sum.setTypeface(null, Typeface.BOLD);
				row.addView(sum);
				
				row.setOnLongClickListener(new OnLongClickListener() {
					
					public boolean onLongClick(View v) {
						registerForContextMenu(row);
						//guarda registro selecionado
						recordSelected = timesheet;
						return false;
					}
				});
				
				View line = new View(this);
				line.setBackgroundColor(Color.GRAY);
				line.setMinimumHeight(2);

				table.addView(dateView, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				table.addView(row, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				table.addView(line, new TableLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			}
			dao.close();
	 }

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// Habilita novos recursos na janela
		requestWindowFeature(Window.FEATURE_LEFT_ICON);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		// Adiciona um ’cone a esquerda
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.timesheet_menu);
	}

	@Override
	protected void onResume() {
		super.onResume();
		 loadList();
	}

	// menu
	private static final int ADD_MENU = 0;
	private static final int MAIL_MENU = 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem add = menu.add(0, ADD_MENU, 0, "Adicionar registro");
		MenuItem mail = menu.add(0, MAIL_MENU, 0, "Enviar");
		mail.setIcon(R.drawable.email_go);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean add_menu_selected = (item.getItemId() == ADD_MENU);
		boolean mail_menu_selected = (item.getItemId() == MAIL_MENU);

		if (add_menu_selected) {
			Toast.makeText(HistoryActivity.this, "adicionar registro....",
					Toast.LENGTH_SHORT).show();
		} else if (mail_menu_selected) {
			Toast.makeText(HistoryActivity.this, "enviar email....",
					Toast.LENGTH_SHORT).show();
		}

		return true;
	}

	private static final int SEND_MAIL = 0;
	private static final int EDIT = 1;
	private static final int DELETE = 2;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		menu.add(0, SEND_MAIL, 0, "Enviar E-mail");
		menu.add(0, EDIT, 0, "Editar");
		menu.add(0, DELETE, 0, "Excluir");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		boolean send_mail_selected = (item.getItemId() == SEND_MAIL);
		boolean edit_selected = (item.getItemId() == EDIT);
		boolean delete_selected = (item.getItemId() == DELETE);

		if (send_mail_selected) {
			Toast.makeText(this, "implementar...."+recordSelected.getInput(), Toast.LENGTH_SHORT).show();

		} else if (edit_selected) {
			Intent intent = new Intent(this, EditRecordActivity.class);
			intent.putExtra("recordSelected", recordSelected);
			startActivity(intent);

		} else if (delete_selected) {
			TimesheetDAO dao = new TimesheetDAO(this);
			dao.delete(recordSelected);
			dao.close();

			 loadList();
		}

		return super.onContextItemSelected(item);
	}
}
