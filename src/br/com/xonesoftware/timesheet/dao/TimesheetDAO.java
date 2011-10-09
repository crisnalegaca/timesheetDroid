package br.com.xonesoftware.timesheet.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.xonesoftware.timesheet.model.Timesheet;
import br.com.xonesoftware.util.DateUtils;

public class TimesheetDAO extends SQLiteOpenHelper {

	// versão da tabela para marcar que alteramos algum detalhe no modelo
	private static final int VERSION = 1;
	private static final String TABLE = "Timesheet";
	private static final String[] COLS = { "input", "outputLunch",
			"inputLunch", "output" };
	private static final int INPUT = 0;
	private static final int OUTPUT_LUNCH = 1;
	private static final int INPUT_LUNCH = 2;
	private static final int OUTPUT = 3;

	public TimesheetDAO(Context context) {
		super(context, TABLE, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE " + TABLE + " ");
		builder.append("(input TEXT NOT NULL, ");
		builder.append("outputLunch TEXT, ");
		builder.append("inputLunch TEXT, ");
		builder.append("output TEXT);");
		db.execSQL(builder.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// se alguma alteração no modelo for feita, é interessante que a tabela
		// do banco se altere de acordo. Criar uma heuristica de atualização
		// para não perder os dados.

		// para teste
		StringBuilder builder = new StringBuilder();
		builder.append("DROP TABLE IF EXISTS " + TABLE);
		db.execSQL(builder.toString());
		onCreate(db);
	}

	private ContentValues timesheetToContentValues(Timesheet timesheet) {
		ContentValues values = new ContentValues();
		values.put("input", timesheet.getInput());
		values.put("outputLunch", timesheet.getOutputLunch());
		values.put("inputLunch", timesheet.getInputLunch());
		values.put("output", timesheet.getOutput());

		return values;
	}

	// para teste
	public void deleteAll() {
		getWritableDatabase().delete(TABLE, null, null);
	}

	public void save() {
		String today = DateUtils.today();
		Timesheet record = getByDate(today.split(" ")[0]);

		boolean isArrival = (record.getInput() == null);
		boolean isOutputLunch = (record.getOutputLunch() == null);
		boolean isBackLunch = (record.getInputLunch() == null);
		boolean isOutput = (record.getOutput() == null);

		if (isArrival) {
			record.setInput(today);
			insert(record);
		} else if (isOutputLunch) {
			record.setOutputLunch(today);
		} else if (isBackLunch) {
			record.setInputLunch(today);
		} else if (isOutput) {
			record.setOutput(today);
		}

		update(record);

	}

	public void insert(Timesheet record) {
		ContentValues values = timesheetToContentValues(record);
		getWritableDatabase().insert(TABLE, null, values);
	}

	public void update(Timesheet record) {
		// encontra o data
		String date = record.getInput().split(" ")[0];
		
		boolean existOutputLunch = (record.getOutputLunch() != null);
		boolean existBackLunch = (record.getInputLunch() != null);
		boolean existOutput = (record.getOutput() != null); 

		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE " + TABLE + " SET ");
		builder.append("input='" + record.getInput() + "'");
		if (existOutputLunch) {
			builder.append(", outputLunch='" + record.getOutputLunch() + "'");
		}
		if (existBackLunch) {
			builder.append(", inputLunch='" + record.getInputLunch() + "'");
		}
		if (existOutput) {
			builder.append(", output='" + record.getOutput() + "'");
		}
		builder.append(" WHERE input LIKE '" + date + "%'");
		getWritableDatabase().execSQL(builder.toString());

		// ContentValues values = timesheetToContentValues(record);
		// getWritableDatabase().update(TABLE, values, "input LIKE ?",
		// new String[] { date });
	}

	public void delete(Timesheet record) {
		getWritableDatabase().delete(TABLE, "input=?",
				new String[] { record.getInput() });
	}

	public List<Timesheet> getAll() {
		List<Timesheet> records = new ArrayList<Timesheet>();

		Cursor cursor = getWritableDatabase().query(TABLE, COLS, null, null,
				null, null, null);

		while (cursor.moveToNext()) {
			Timesheet record = new Timesheet();
			record.setInput(cursor.getString(INPUT));
			record.setOutputLunch(cursor.getString(OUTPUT_LUNCH));
			record.setInputLunch(cursor.getString(INPUT_LUNCH));
			record.setOutput(cursor.getString(OUTPUT));

			records.add(record);
		}
		cursor.close();

		return records;
	}

	// sem teste...
	public List<Timesheet> getByPeriod(String dateInit, String dateEnd) {
		List<Timesheet> records = new ArrayList<Timesheet>();

		String find_records_by_period = "SELECT * FROM " + TABLE + " WHERE input between '"
				+ dateInit + "' and '" + dateEnd + "'";
		Cursor cursor = getWritableDatabase().rawQuery(find_records_by_period, null);

		while (cursor.moveToNext()) {
			Timesheet record = new Timesheet();
			record.setInput(cursor.getString(INPUT));
			record.setOutputLunch(cursor.getString(OUTPUT_LUNCH));
			record.setInputLunch(cursor.getString(INPUT_LUNCH));
			record.setOutput(cursor.getString(OUTPUT));

			records.add(record);
		}
		cursor.close();

		return records;
	}

	public Timesheet getByDate(String date) {
		String find_record_day = "SELECT * FROM " + TABLE + " WHERE input LIKE '" + date
				+ "%'";
		Cursor cursor = getWritableDatabase().rawQuery(find_record_day, null);
		boolean isRecordFound = (cursor.getCount() > 0);
		if(isRecordFound){
			cursor.moveToFirst();
			Timesheet timesheetFound = new Timesheet();
			timesheetFound.setInput(cursor.getString(INPUT));
			timesheetFound.setOutputLunch(cursor.getString(OUTPUT_LUNCH));
			timesheetFound.setInputLunch(cursor.getString(INPUT_LUNCH));
			timesheetFound.setOutput(cursor.getString(OUTPUT));
			
			cursor.close();
			return timesheetFound;
		}else{
			return new Timesheet();
		}
		
	}
	
}
