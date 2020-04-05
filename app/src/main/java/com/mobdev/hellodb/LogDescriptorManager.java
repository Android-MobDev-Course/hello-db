package com.mobdev.hellodb;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Marco Picone (picone.m@gmail.com) 20/03/2020
 * Singleton to manage Logs storage
 */
public class LogDescriptorManager {

	private Context context = null;

	/*
	 * The instance is static so it is shared among all instances of the class. It is also private
	 * so it is accessible only within the class.
	 */
	private static LogDescriptorManager instance = null;

	private ArrayList<LogDescriptor> logList = null;

	/*
	 * The constructor is private so it is accessible only within the class.
	 */
	private LogDescriptorManager(Context context){
		Log.d(MainActivity.TAG,"Number Manager Created !");
		this.context = context;		
		/*
		 * Try to read an existing log list and load into the ArrayList
		 */
		try {

			LogDescriptorDataSource datasource = new LogDescriptorDataSource(context);
			datasource.open();
			
			this.logList = (ArrayList<LogDescriptor>) datasource.getAllLogsDescriptorOrderByIdDesc();
			
			datasource.close();
			
			Log.d(MainActivity.TAG,"Log File available ! List size: " + this.logList.size());
		} catch(Exception e) {
			//If there is not an existing file create an empty ArrayList
			this.logList = new ArrayList<LogDescriptor>();
			Log.e(MainActivity.TAG,"Error Reading Log List on File: " + e.getLocalizedMessage());
		} 
	}

	public static LogDescriptorManager getInstance(Context context){
		/*
		 * The constructor is called only if the static instance is null, so only the first time 
		 * that the getInstance() method is invoked.
		 * All the other times the same instance object is returned.
		 */
		if(instance == null)
			instance = new LogDescriptorManager(context);
		return instance;
	}

	public void addLog(LogDescriptor log){
		this.logList.add(log);

		try {

			LogDescriptorDataSource datasource = new LogDescriptorDataSource(context);
			datasource.open();

			if(datasource.createLogDescriptor(log) != null)
				Toast.makeText(context, "New Log Correctly Added !", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(context, "ERROR Adding new Log !", Toast.LENGTH_LONG).show();

			datasource.close();

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Error Saving Log List on File ...", Toast.LENGTH_LONG).show();
		}

	}

	public void addLogToHead(LogDescriptor log){
		this.logList.add(0,log);

		try {

			LogDescriptorDataSource datasource = new LogDescriptorDataSource(context);
			datasource.open();

			if(datasource.createLogDescriptor(log) != null)
				Toast.makeText(context, "New Log Correctly Added !", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(context, "ERROR Adding new Log !", Toast.LENGTH_LONG).show();

			datasource.close();

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Error Saving Log List on File ...", Toast.LENGTH_LONG).show();
		}
	}

	public void removeLog(LogDescriptor log){
		this.logList.remove(log);

		try {

			LogDescriptorDataSource datasource = new LogDescriptorDataSource(context);
			datasource.open();

			datasource.deleteLog(log);
			
			datasource.close();

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "Error Saving Log List on File ...", Toast.LENGTH_LONG).show();
		}
	}
	
	public void removeLog(int position){
		this.removeLog(this.logList.get(position));
	}

	public ArrayList<LogDescriptor> getLogList(){
		return logList;
	}

}

