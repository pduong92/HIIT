package com.phduo.hiit;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.phduo.hiit.data.HiitContract.RegimeEntry;
import com.phduo.hiit.data.HiitContract.ExerciseEntry;
import com.phduo.hiit.data.HiitContract.HistoryEntry;

public class OverviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    static final String LOG_TAG = OverviewActivity.class.getName();
    private static final MyAppApplication GLOBALVARS = new MyAppApplication();

    private static final int REGIMES_LOADER = 0;
    private static final int EXERCISES_LOADER = 1;
    private static final int HISTORY_LOADER = 2;

    private Cursor rCursor;
    private Cursor aCursor;
    private Cursor hCursor;

    private FragmentAdapter fragAdapter;

    private ViewPager fragPager;

    private int initIndex;
    private Uri regimeUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        regimeUri = getIntent().getData();
        initIndex = getIntent().getIntExtra(GLOBALVARS.INDEX_KEY, -1);

        LoaderManager loaderManager = OverviewActivity.this.getLoaderManager();
        loaderManager.initLoader(REGIMES_LOADER, null, this);
        loaderManager.initLoader(EXERCISES_LOADER, null, this);

        fragPager = (ViewPager) findViewById(R.id.pager);

        FloatingActionButton fabutton = (FloatingActionButton) findViewById(R.id.fab);

        fabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:

                return true;
            case R.id.action_new_regime:
                Intent addIntent = new Intent(OverviewActivity.this, RegimeEditorActivity.class);
                addIntent.putExtra(GLOBALVARS.HEADER_KEY, R.string.editor_add_regime);
                startActivity(addIntent);

                return true;
            case R.id.action_edit_regime:
                int currentIndex = fragPager.getCurrentItem();
                int currentRegmieId = fragAdapter.getItemId(currentIndex);
                Regime currentRegime = fragAdapter.getRegime(currentIndex);
                regimeUri = ContentUris.withAppendedId(RegimeEntry.CONTENT_URI, currentRegmieId);
                Intent editIntent = new Intent(Intent.ACTION_EDIT, regimeUri, OverviewActivity.this, RegimeEditorActivity.class);
                editIntent.putExtra(GLOBALVARS.HEADER_KEY, R.string.editor_edit_regime);
                editIntent.putExtra(GLOBALVARS.REGIME_ID_KEY, currentRegmieId);
                editIntent.putExtra(GLOBALVARS.REGIME_KEY, currentRegime);
                startActivity(editIntent);

                return true;
            case R.id.action_delete_regime:

                return true;
            case R.id.action_settings:

                return true;
        }

        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        String[] projection;

        switch (loaderID) {
            case REGIMES_LOADER:
                projection = new String[] {RegimeEntry._ID,
                        RegimeEntry.COLUMN_NAME,
                        RegimeEntry.COLUMN_DESCRIPTION,
                        RegimeEntry.COLUMN_SETCOUNT,
                        RegimeEntry.COLUMN_SETTIME,
                        RegimeEntry.COLUMN_TIME};

                return new CursorLoader(this,
                        RegimeEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);

            case EXERCISES_LOADER:
                projection = new String[] {ExerciseEntry._ID,
                        ExerciseEntry.COLUMN_NAME,
                        ExerciseEntry.COLUMN_DESCRIPTION,
                        ExerciseEntry.COLUMN_TIME,
                        ExerciseEntry.COLUMN_REGIMEID};

                return new CursorLoader(this,
                        ExerciseEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);

            case HISTORY_LOADER:
                projection = new String[] {HistoryEntry._ID,
                        HistoryEntry.COLUMN_DATE,
                        HistoryEntry.COLUMN_SETTARGET,
                        HistoryEntry.COLUMN_COMPLETION,
                        HistoryEntry.COLUMN_TIME,
                        HistoryEntry.COLUMN_REGIMEID};

                return new CursorLoader(this,
                        HistoryEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        final int loaderId = loader.getId();

        switch (loaderId) {
            case REGIMES_LOADER:
                rCursor = cursor;
                Log.v(LOG_TAG, "Loader ID: " + loaderId + " has completed. Regime data received." );
                break;
            case EXERCISES_LOADER:
                aCursor = cursor;
                Log.v(LOG_TAG, "Loader ID: " + loaderId + " has completed. Exercise data received." );
                break;
            case HISTORY_LOADER:
                hCursor = cursor;
                Log.v(LOG_TAG, "Loader ID: " + loaderId + " has completed. History data received." );
                break;
        }

        if (rCursor != null && aCursor != null) {
            fragAdapter = new FragmentAdapter(OverviewActivity.this.getSupportFragmentManager(), rCursor, aCursor, hCursor);
            fragPager.setAdapter(fragAdapter);
            if (initIndex >= 0) {
//                fragAdapter.setPrimaryItem(fragPager, initIndex, fragAdapter.instantiateItem(fragPager, initIndex));
                fragPager.setCurrentItem(initIndex);
            }

            Log.v(LOG_TAG, "Fragment Adapter set.");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
