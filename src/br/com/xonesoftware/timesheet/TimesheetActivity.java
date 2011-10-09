package br.com.xonesoftware.timesheet;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import br.com.xonesoftware.timesheet.dao.TimesheetDAO;
import br.com.xonesoftware.timesheet.model.Timesheet;
import br.com.xonesoftware.util.DateUtils;

/**
 * @author cris
 * Tela Principal - Registra hor‡rio
 *
 */
public class TimesheetActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Habilita novos recursos na janela
		requestWindowFeature(Window.FEATURE_LEFT_ICON);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.save);

		// Adiciona um ’cone a esquerda
		setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.timesheet_menu);

		ImageView save = (ImageView) findViewById(R.id.save);
		save.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				TimesheetDAO dao = new TimesheetDAO(TimesheetActivity.this);
				dao.save();
				// dao.deleteAll();
				List<Timesheet> records = dao.getAll();
				dao.close();

				for (Timesheet timesheet : records) {
					Log.i("teste", "for - input " + timesheet.getInput());
					Log.i("teste",
							"for - outputLunch " + timesheet.getOutputLunch());
					Log.i("teste",
							"for - inputLunch " + timesheet.getInputLunch());
					Log.i("teste", "for - output " + timesheet.getOutput());
				}

				Toast.makeText(TimesheetActivity.this, "Ponto registrado - "+DateUtils.today(),
						Toast.LENGTH_LONG).show();
			}
		});

	}

	// menu
	private static final int HISTORY_MENU = 0;
	private static final int MAIL_MENU = 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem history = menu.add(0, HISTORY_MENU, 0, "Hist—rico");
		history.setIcon(R.drawable.history);
		MenuItem mail = menu.add(0, MAIL_MENU, 0, "Configurar");
		mail.setIcon(R.drawable.email_edit);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean history_menu_selected = (item.getItemId() == HISTORY_MENU);
		boolean mail_menu_selected = (item.getItemId() == MAIL_MENU);
		
		if (history_menu_selected) {
			startActivity(new Intent(this, HistoryActivity.class));

		} else if (mail_menu_selected) {
			Toast.makeText(TimesheetActivity.this, "configurar email....",
					Toast.LENGTH_SHORT).show();
		}

		return true;
	}
}