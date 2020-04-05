package com.mobdev.hellodb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Marco Picone (picone.m@gmail.com) 20/03/2020
 * DB SQLiteOpenHelper Class
 */
public class LogDescriptorOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    
    private static final String DATABASE_NAME = "log.db";
    
    public static final String TABLE_NAME = "log";
    
    public static final String TIMESTAMP_COL = "timestamp";
    public static final String LATITUDE_COL = "latitude";
    public static final String LONGITUDE_COL = "longitude";
    public static final String TYPE_COL = "type";
    public static final String DATA_COL = "data";
    public static final String ID_COL = "id";
    
    private static final String DATABASE_TABLE_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TIMESTAMP_COL + " TIMESTAMP, " +
                LATITUDE_COL + " DOUBLE,"+
                LONGITUDE_COL + " DOUBLE,"+
                TYPE_COL + " VARCHAR(50),"+
                DATA_COL + " TEXT"+
                ");";

    public LogDescriptorOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(MainActivity.TAG, "LogOpenHelper Constructor !");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.d(MainActivity.TAG,"LogOpenHelper onCreate !");
        db.execSQL(DATABASE_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(MainActivity.TAG,"LogOpenHelper onUpgrade !");	
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL(DATABASE_TABLE_CREATE);
	}
}