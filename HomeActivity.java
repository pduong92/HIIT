package com.phduo.hiit;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.phduo.hiit.data.HiitDbHelper;

import com.phduo.hiit.data.HiitContract.RegimeEntry;
import com.phduo.hiit.data.HiitContract.ExerciseEntry;
import com.phduo.hiit.data.HiitContract.HistoryEntry;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = HomeActivity.class.getName();
    private static final MyAppApplication GLOBALVARS = new MyAppApplication();

    private static final int REGIMES_LOADER = 0;

    private HiitDbHelper mDbHelper;

    private ListView regimeList;
    private RegimeCursorAdapter regimeAdapter;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Instantiate Database Helper
        mDbHelper = new HiitDbHelper(HomeActivity.this);

        regimeList = (ListView) findViewById(R.id.regime_list);

        //Instantiate regimeAdapter and attach to ListView
        regimeAdapter = new RegimeCursorAdapter(HomeActivity.this, null);
        regimeList.setAdapter(regimeAdapter);

        //Set list's empty view if empty
        RelativeLayout emptyView = (RelativeLayout) findViewById(R.id.empty_view);
        regimeList.setEmptyView(emptyView);

        HomeActivity.this.getLoaderManager().initLoader(REGIMES_LOADER, null, this);

        regimeList.setOnItemClickListener((parent, view, position, id) -> {

            Uri regimeUri = ContentUris.withAppendedId(RegimeEntry.CONTENT_URI, id);
            Intent intent = new Intent(Intent.ACTION_VIEW, regimeUri, HomeActivity.this, OverviewActivity.class);
            intent.putExtra(GLOBALVARS.INDEX_KEY, position);
            startActivity(intent);

        });

       /* new CountDownTimer(2*MINUTE, SECOND) {
            TextView timer = (TextView) findViewById(R.id.test);

            public void onTick(long millisLeft) {
                int mins = (int)millisLeft / MINUTE;
                int secs = (int)millisLeft % MINUTE / SECOND;

                timer.setText(mins + ":" + secs);
            }

            public void onFinish() {
                timer.setText("Done!");
            }
        }.start();*/

//        TextView test = (TextView) findViewById(R.id.test);

//        //Display Regime Table data
//        displayData(RegimeEntry.TABLE_NAME, (TextView) findViewById(R.id.regime_data));
//        //Display Exercise Table data
//        displayData(ActivityEntry.TABLE_NAME, (TextView) findViewById(R.id.activity_data));
//        //Display History Table data
//        displayData(HistoryEntry.TABLE_NAME, (TextView) findViewById(R.id.history_data));

//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(HomeActivity.this, OverviewActivity.class);
//                startActivity(intent);
//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem editItem = menu.findItem(R.id.action_edit_regime);
        MenuItem deleteItem = menu.findItem(R.id.action_delete_regime);

        editItem.setVisible(false);
        deleteItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertTestDummy();
                //Refresh table display after adding test dummy
//                displayData(RegimeEntry.TABLE_NAME, (TextView) findViewById(R.id.regime_data));
//                displayData(ActivityEntry.TABLE_NAME, (TextView) findViewById(R.id.activity_data));
                return true;
            case R.id.action_new_regime:
                Intent editorIntent = new Intent(HomeActivity.this, RegimeEditorActivity.class);
                editorIntent.putExtra(GLOBALVARS.HEADER_KEY, R.string.editor_add_regime);
                startActivity(editorIntent);
                return true;
            case R.id.action_settings:

                return true;
            case R.id.action_view_db:
                Intent dbIntent = new Intent(HomeActivity.this, AndroidDatabaseManager.class);
                startActivity(dbIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void insertTestDummy() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Toast toast;
        boolean dummyInserted = false;

        String dummyName = "TestRegime";
        String dummyDescription = "This is a test description for TestRegime.";
        int dummySetTime = 3 * GLOBALVARS.MINUTE;
        int dummySetCount = 3;
        int dummyTime = dummySetTime * dummySetCount;

        ContentValues values = new ContentValues();
        values.put(RegimeEntry.COLUMN_NAME, dummyName);
        values.put(RegimeEntry.COLUMN_DESCRIPTION, dummyDescription);
        values.put(RegimeEntry.COLUMN_SETTIME, dummySetTime);
        values.put(RegimeEntry.COLUMN_SETCOUNT, dummySetCount);
        values.put(RegimeEntry.COLUMN_TIME, dummyTime);

        Uri regimeResult = HomeActivity.this.getContentResolver().insert(RegimeEntry.CONTENT_URI, values);

//        long newRegimeId = db.insert(RegimeEntry.TABLE_NAME, null, values);

        if (regimeResult == null) {
            Log.v(LOG_TAG, "Failed to insert into " + RegimeEntry.TABLE_NAME);
            dummyInserted = false;
        } else {
            long newRegimeId = ContentUris.parseId(regimeResult);
            Log.v(LOG_TAG, "Successfully inserted row " + newRegimeId + " into " + RegimeEntry.TABLE_NAME);
            dummyInserted = true;

            String[] dummyANames = {"Exercise1", "Exercise2", "Exercise3"};
            String dummyADescription = "This is a test description for ";
            int dummyATime = GLOBALVARS.MINUTE;
            int dummyARegimeId = (int)newRegimeId;

//            List<String> dummyActivities = Arrays.asList(dummyANames);
            ContentValues aValues;
            long newExerciseId;

            Uri exerciseResult;

            int orderIndex = 1;

            for (String name : dummyANames) {
                aValues = new ContentValues();
                aValues.put(ExerciseEntry.COLUMN_NAME, name);
                aValues.put(ExerciseEntry.COLUMN_DESCRIPTION, dummyADescription + name);
                aValues.put(ExerciseEntry.COLUMN_TIME, dummyATime);
                aValues.put(ExerciseEntry.COLUMN_ORDERID, orderIndex++);
                aValues.put(ExerciseEntry.COLUMN_REGIMEID, dummyARegimeId);

                exerciseResult = HomeActivity.this.getContentResolver().insert(ExerciseEntry.CONTENT_URI, aValues);

                if (exerciseResult == null) {
                    Log.v(LOG_TAG, "Failed to insert into " + ExerciseEntry.TABLE_NAME);
                    dummyInserted = false;
                    break;
                } else {
                    newExerciseId = ContentUris.parseId(exerciseResult);
                    Log.v(LOG_TAG, "Successfully inserted row " + newExerciseId + " into " + ExerciseEntry.TABLE_NAME);
                    dummyInserted = true;
                }
            }
        }

        if (dummyInserted) {
            toast = Toast.makeText(HomeActivity.this, "Successfully inserted dummy", Toast.LENGTH_SHORT);
            regimeAdapter.notifyDataSetChanged();
        } else {
            toast = Toast.makeText(HomeActivity.this, "Failed to insert dummy", Toast.LENGTH_SHORT);
        }

        toast.show();
    }

    public void displayData(String tableName, TextView outputView) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection;

        View displayView = outputView;

        switch (tableName) {
            case RegimeEntry.TABLE_NAME:
                projection = new String[] {
                        RegimeEntry._ID,
                        RegimeEntry.COLUMN_NAME,
                        RegimeEntry.COLUMN_DESCRIPTION,
                        RegimeEntry.COLUMN_SETTIME,
                        RegimeEntry.COLUMN_SETCOUNT,
                        RegimeEntry.COLUMN_TIME
                };
//                displayView = (TextView) findViewById(R.id.regime_data);

                break;
            case ExerciseEntry.TABLE_NAME:
                projection = new String[] {
                        ExerciseEntry._ID,
                        ExerciseEntry.COLUMN_NAME,
                        ExerciseEntry.COLUMN_DESCRIPTION,
                        ExerciseEntry.COLUMN_TIME,
                        ExerciseEntry.COLUMN_REGIMEID
                };
//                displayView = (TextView) findViewById(R.id.exercise_data);

                break;
            case HistoryEntry.TABLE_NAME:
                projection = new String[] {
                        HistoryEntry._ID,
                        HistoryEntry.COLUMN_REGIMEID,
                        HistoryEntry.COLUMN_SETTARGET,
                        HistoryEntry.COLUMN_COMPLETION,
                        HistoryEntry.COLUMN_TIME,
                        HistoryEntry.COLUMN_DATE
                };
//                displayView = (TextView) findViewById(R.id.history_data);

                break;
            default:
                Log.v(LOG_TAG, "Table " + tableName + " does not exist.");
                return;
        }

        Cursor cursor = db.query(
                tableName,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        try {
            StringBuilder data = new StringBuilder();

            int rowCount = cursor.getCount();
            int colCount = cursor.getColumnCount();

            data.append("Number of rows in " + tableName + ": " + rowCount + "\n\n");

            String[] colnames = cursor.getColumnNames();
            List<String> colNames = Arrays.asList(colnames);

            for(String name : colNames) {
                data.append(name + " - ");
            }
            data.append("\n\n");

            switch(tableName) {
                case RegimeEntry.TABLE_NAME:

                    int idColIndex =  cursor.getColumnIndex(RegimeEntry._ID);
                    int nameColIndex = cursor.getColumnIndex(RegimeEntry.COLUMN_NAME);
                    int descColIndex = cursor.getColumnIndex(RegimeEntry.COLUMN_DESCRIPTION);
                    int setTimeColIndex = cursor.getColumnIndex(RegimeEntry.COLUMN_SETTIME);
                    int setCountColIndex = cursor.getColumnIndex(RegimeEntry.COLUMN_SETCOUNT);
                    int timeColIndex = cursor.getColumnIndex(RegimeEntry.COLUMN_TIME);

                    while(cursor.moveToNext()) {
                        int rowId = cursor.getInt(idColIndex);
                        String rowName = cursor.getString(nameColIndex);
                        String rowDesc = cursor.getString(descColIndex);
                        int rowSetTime = cursor.getInt(setTimeColIndex);
                        int rowSetCount = cursor.getInt(setCountColIndex);
                        int rowTime = cursor.getInt(timeColIndex);

                        data.append(rowId + " - " +
                                    rowName + " - " +
                                    rowDesc + " - " +
                                    rowSetTime + " - " +
                                    rowSetCount + " - " +
                                    rowTime + "\n");
                    }

                    break;
                case ExerciseEntry.TABLE_NAME:

                    int idAColIndex =  cursor.getColumnIndex(ExerciseEntry._ID);
                    int nameAColIndex = cursor.getColumnIndex(ExerciseEntry.COLUMN_NAME);
                    int descAColIndex = cursor.getColumnIndex(ExerciseEntry.COLUMN_DESCRIPTION);
                    int timeAColIndex = cursor.getColumnIndex(ExerciseEntry.COLUMN_TIME);
                    int regimeAColIndex = cursor.getColumnIndex(ExerciseEntry.COLUMN_REGIMEID);

                    while(cursor.moveToNext()) {
                        int rowAId = cursor.getInt(idAColIndex);
                        String rowAName = cursor.getString(nameAColIndex);
                        String rowADesc = cursor.getString(descAColIndex);
                        int rowATime = cursor.getInt(timeAColIndex);
                        int rowARegime = cursor.getInt(regimeAColIndex);

                        data.append(rowAId + " - " +
                                rowAName + " - " +
                                rowADesc + " - " +
                                rowATime + " - " +
                                rowARegime + "\n");
                    }

                    break;
                case HistoryEntry.TABLE_NAME:

                    int idHColIndex =  cursor.getColumnIndex(HistoryEntry._ID);
                    int regimeHColIndex = cursor.getColumnIndex(HistoryEntry.COLUMN_REGIMEID);
                    int setTargetHColIndex = cursor.getColumnIndex(HistoryEntry.COLUMN_SETTARGET);
                    int completionHColIndex = cursor.getColumnIndex(HistoryEntry.COLUMN_COMPLETION);
                    int timeHColIndex = cursor.getColumnIndex(HistoryEntry.COLUMN_TIME);
                    int dateHColIndex = cursor.getColumnIndex(HistoryEntry.COLUMN_DATE);

                    while(cursor.moveToNext()) {
                        int rowHId = cursor.getInt(idHColIndex);
                        String rowHRegime = cursor.getString(regimeHColIndex);
                        String rowHSetTarget = cursor.getString(setTargetHColIndex);
                        int rowHCompletion = cursor.getInt(completionHColIndex);
                        int rowHTime = cursor.getInt(timeHColIndex);
                        int rowHDate = cursor.getInt(dateHColIndex);

                        data.append(rowHId + " - " +
                                rowHRegime + " - " +
                                rowHSetTarget + " - " +
                                rowHCompletion + " - " +
                                rowHTime + " - " +
                                rowHDate + "\n");
                    }

                    break;
            }

            ((TextView)displayView).setText(data);
        } finally {
            cursor.close();
        }
    }

    // ###############################################
    // METHODS FOR LOADERMANAGER

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        String[] projection = {RegimeEntry._ID,
                RegimeEntry.COLUMN_NAME,
                RegimeEntry.COLUMN_DESCRIPTION,
                RegimeEntry.COLUMN_TIME};

        switch(loaderID) {
            case REGIMES_LOADER:
                return new CursorLoader(
                        HomeActivity.this,
                        RegimeEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (regimeList.getAdapter() == null) {
            regimeAdapter = new RegimeCursorAdapter(HomeActivity.this, data);
            regimeList.setAdapter(regimeAdapter);

            Log.v(LOG_TAG, "onLoadFinished: Initialized adapter with data from CursorLoader");
        }
        else {
            regimeAdapter.changeCursor(data);

            Log.v(LOG_TAG, "onLoadFinished: Updated adapter cursor with data from CursorLoader");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        regimeAdapter.changeCursor(null);
    }
}
