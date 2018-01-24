package com.phduo.hiit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//import classes for Android Database Manager
import android.database.MatrixCursor;
import android.database.Cursor;
import android.database.SQLException;

import com.phduo.hiit.data.HiitContract.RegimeEntry;
import com.phduo.hiit.data.HiitContract.ExerciseEntry;
import com.phduo.hiit.data.HiitContract.HistoryEntry;

import java.util.ArrayList;

/**
 * Created by phduo on 9/30/2017.
 */

public class HiitDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = HiitDbHelper.class.getName();

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "hiit.db";

    public HiitDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        ArrayList<String> tableQueries = new ArrayList<String>();

        String SQL_CREATE_REGIMES_TABLE =
                "CREATE TABLE IF NOT EXISTS " + RegimeEntry.TABLE_NAME + " (" +
                        RegimeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        RegimeEntry.COLUMN_NAME + " VARCHAR(30) NOT NULL," +
                        RegimeEntry.COLUMN_DESCRIPTION + " VARCHAR(120) DEFAULT ''," +
                        RegimeEntry.COLUMN_SETTIME + " NUMERIC NOT NULL DEFAULT 0," +
                        RegimeEntry.COLUMN_SETCOUNT + " SMALLINT NOT NULL DEFAULT 3," +
                        RegimeEntry.COLUMN_TIME + " NUMERIC NOT NULL DEFAULT 0" +
                        ")";
        tableQueries.add(SQL_CREATE_REGIMES_TABLE);

        String SQL_CREATE_EXERCISES_TABLE =
                "CREATE TABLE IF NOT EXISTS " + ExerciseEntry.TABLE_NAME + " (" +
                        ExerciseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        ExerciseEntry.COLUMN_NAME + " VARCHAR(30) NOT NULL," +
                        ExerciseEntry.COLUMN_DESCRIPTION + " VARCHAR(120) DEFAULT ''," +
                        ExerciseEntry.COLUMN_TIME + " NUMERIC NOT NULL DEFAULT 0," +
                        ExerciseEntry.COLUMN_ORDERID + " SMALLINT NOT NULL," +
                        ExerciseEntry.COLUMN_REGIMEID + " INTEGER," +
                            "FOREIGN KEY (" + ExerciseEntry.COLUMN_REGIMEID + ") REFERENCES " + RegimeEntry.TABLE_NAME + "(" + RegimeEntry._ID + ")" +
                        ")";
        tableQueries.add(SQL_CREATE_EXERCISES_TABLE);

        String SQL_CREATE_HISTORY_TABLE =
                "CREATE TABLE IF NOT EXISTS " + HistoryEntry.TABLE_NAME + " (" +
                        HistoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        HistoryEntry.COLUMN_REGIMEID + " INTEGER," +
                        HistoryEntry.COLUMN_SETTARGET + " SMALLINT NOT NULL DEFAULT 3," +
                        HistoryEntry.COLUMN_COMPLETION + " NUMERIC NOT NULL DEFAULT 0," +
                        HistoryEntry.COLUMN_TIME + " NUMERIC NOT NULL DEFAULT 0," +
                        HistoryEntry.COLUMN_DATE + " DATE DEFAULT CURRENT_TIMESTAMP," +
                            "FOREIGN KEY (" + HistoryEntry.COLUMN_REGIMEID + ") REFERENCES " + RegimeEntry.TABLE_NAME + "(" + RegimeEntry._ID + ")" +
                        ")";
        tableQueries.add(SQL_CREATE_HISTORY_TABLE);

        for(String query : tableQueries) {
            db.execSQL(query);
            Log.v(LOG_TAG, "Successfully processed: " + DATABASE_NAME + "( " + query + " )");
        }
        Log.v(LOG_TAG, "Database created: " + DATABASE_NAME + " Ver: " + DATABASE_VERSION);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

}
