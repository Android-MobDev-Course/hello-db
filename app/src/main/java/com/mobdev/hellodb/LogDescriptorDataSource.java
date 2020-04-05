package com.mobdev.hellodb;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Marco Picone (picone.m@gmail.com) 20/03/2020
 * DataSource class to provide all the methods to interact with the DB
 */
public class LogDescriptorDataSource {

	// Database fields
	private SQLiteDatabase database;
	private LogDescriptorOpenHelper dbHelper;
	private String[] allColumns = { LogDescriptorOpenHelper.ID_COL,LogDescriptorOpenHelper.TIMESTAMP_COL, LogDescriptorOpenHelper.LATITUDE_COL, LogDescriptorOpenHelper.LONGITUDE_COL, LogDescriptorOpenHelper.TYPE_COL, LogDescriptorOpenHelper.DATA_COL };
	
	public LogDescriptorDataSource(Context context) {
		dbHelper = new LogDescriptorOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Main method to add a new record in the DB starting from a LogDecriptor obj
	 * @param log
	 * @return
	 */
	public LogDescriptor createLogDescriptor(LogDescriptor log) {
		
		Log.d(MainActivity.TAG, "Creating Log Descritor ...");
		
		
		//object used to define the columns and the value used to create a new record in the DB
		ContentValues values = new ContentValues();
		values.put(LogDescriptorOpenHelper.TIMESTAMP_COL, log.getTimestamp());
		values.put(LogDescriptorOpenHelper.LATITUDE_COL, log.getLatitude());
		values.put(LogDescriptorOpenHelper.LONGITUDE_COL, log.getLongitude());
		values.put(LogDescriptorOpenHelper.TYPE_COL, log.getType());
		values.put(LogDescriptorOpenHelper.DATA_COL, log.getData());
		
		//Insert the new record in the DB and obtain the new ID as result
		long insertId = database.insert(LogDescriptorOpenHelper.TABLE_NAME, null,values);
		
		if(insertId == -1)
			return null;
		else
		{
			Log.d(MainActivity.TAG, "New Log record created: " + insertId);
			
			// To show how to query with for a specific record according to the id 
			Cursor cursor = database.query(LogDescriptorOpenHelper.TABLE_NAME, allColumns, LogDescriptorOpenHelper.ID_COL + " = " + insertId, null, null, null, null);
			cursor.moveToFirst();
			return cursorToLogDescriptor(cursor);
		}
	}


	public void deleteAllLogs() {
		database.delete(LogDescriptorOpenHelper.TABLE_NAME,null, null);
	}

	
	public void deleteLog(LogDescriptor logDescr) {
		long id = logDescr.getId();
		database.delete(LogDescriptorOpenHelper.TABLE_NAME,LogDescriptorOpenHelper.ID_COL + " = " + id, null);
	}

	/**
	 * Method to retrieve the entire List of LogDescriptor stored in the DB
	 * 
	 * @return
	 */
	public List<LogDescriptor> getAllLogsDescriptor() {
		
		Log.d(MainActivity.TAG, "getAllLogsDescriptor ...");
		
		List<LogDescriptor> logList = new ArrayList<LogDescriptor>();
		
		Cursor cursor = database.query(LogDescriptorOpenHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			LogDescriptor logDescr = cursorToLogDescriptor(cursor);
			logList.add(logDescr);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return logList;
	}
	
	/**
	 * Method to retrieve the entire List of LogDescriptor stored in the DB
	 * 
	 * @return
	 */
	public List<LogDescriptor> getAllLogsDescriptorOrderByIdDesc() {
		
		Log.d(MainActivity.TAG, "getAllLogsDescriptor ...");
		
		List<LogDescriptor> logList = new ArrayList<LogDescriptor>();
		
		Cursor cursor = database.query(LogDescriptorOpenHelper.TABLE_NAME,
				allColumns, null, null, null, null, LogDescriptorOpenHelper.ID_COL+" DESC");
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			LogDescriptor logDescr = cursorToLogDescriptor(cursor);
			logList.add(logDescr);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return logList;
	}
	
	
	public int getLogCount() {
		
		int logCount = 0;
		
		String query = "SELECT COUNT(*) FROM "+LogDescriptorOpenHelper.TABLE_NAME;

		Cursor cursor = database.rawQuery(query,null);

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			logCount = cursor.getInt(0);
			cursor.moveToNext();
		}

		cursor.close();

		
		return logCount;
	}

	/**
	 * Utility method to start from a Cursor Object and obtain back a LogDescriptor
	 * 
	 * @param cursor
	 * @return
	 */
	private LogDescriptor cursorToLogDescriptor(Cursor cursor) {

		LogDescriptor logDescriptor = new LogDescriptor();

		logDescriptor.setId(cursor.getInt(0));
		logDescriptor.setTimestamp(cursor.getLong(1));
		logDescriptor.setLatitude(cursor.getLong(2));
		logDescriptor.setLongitude(cursor.getLong(3));
		logDescriptor.setType(cursor.getString(4));
		logDescriptor.setData(cursor.getString(5));

		return logDescriptor;
	}
}
