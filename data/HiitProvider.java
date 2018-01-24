package com.phduo.hiit.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;


import com.phduo.hiit.data.HiitContract;
import com.phduo.hiit.data.HiitContract.RegimeEntry;
import com.phduo.hiit.data.HiitContract.ExerciseEntry;
import com.phduo.hiit.data.HiitContract.HistoryEntry;
/**
 * Created by phduo on 10/7/2017.
 */

public class HiitProvider extends ContentProvider {

    public static final String LOG_TAG = HiitProvider.class.getName();

    private static final int REGIMES = 100;
    private static final int REGIME_ID = 101;
    private static final int REGIME_EXERCISES = 202;
    private static final int REGIME_HISTORY = 302;

    private static final int EXERCISES = 200;
    private static final int EXERCISE_ID = 201;

    private static final int HISTORY = 300;
    private static final int HISTORY_ID = 301;

    private static final String QUERY_REGIME_ID = HiitContract.BY_REGIME_ID + "=#";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(HiitContract.CONTENT_AUTHORITY, HiitContract.PATH_REGIMES, REGIMES);
        sUriMatcher.addURI(HiitContract.CONTENT_AUTHORITY, HiitContract.PATH_REGIMES + "/#", REGIME_ID);
        sUriMatcher.addURI(HiitContract.CONTENT_AUTHORITY, HiitContract.PATH_EXERCISES, EXERCISES);
        sUriMatcher.addURI(HiitContract.CONTENT_AUTHORITY, HiitContract.PATH_EXERCISES + "/#", EXERCISE_ID);
        sUriMatcher.addURI(HiitContract.CONTENT_AUTHORITY, HiitContract.PATH_EXERCISES + "/?" + QUERY_REGIME_ID, REGIME_EXERCISES);
        sUriMatcher.addURI(HiitContract.CONTENT_AUTHORITY, HiitContract.PATH_HISTORY, HISTORY);
        sUriMatcher.addURI(HiitContract.CONTENT_AUTHORITY, HiitContract.PATH_HISTORY + "/#", HISTORY_ID);
        sUriMatcher.addURI(HiitContract.CONTENT_AUTHORITY, HiitContract.PATH_HISTORY + "/?" + QUERY_REGIME_ID, REGIME_HISTORY);
    }

    private HiitDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new HiitDbHelper(HiitProvider.this.getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projector, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = null;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REGIMES:
                cursor = db.query(RegimeEntry.TABLE_NAME, projector, selection, selectionArgs, null, null, sortOrder);

                break;
            case REGIME_ID:
                selection = RegimeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(RegimeEntry.TABLE_NAME, projector, selection, selectionArgs, null, null, sortOrder);

                break;
            case REGIME_EXERCISES:
                selection = ExerciseEntry.COLUMN_REGIMEID + "=?";
                selectionArgs = new String[] { uri.getQueryParameter(HiitContract.BY_REGIME_ID)};
                cursor = db.query(ExerciseEntry.TABLE_NAME, projector, selection, selectionArgs, null, null, sortOrder);

                break;
            case REGIME_HISTORY:
                selection = HistoryEntry.COLUMN_REGIMEID + "=?";
                selectionArgs = new String[] { uri.getQueryParameter(HiitContract.BY_REGIME_ID)};
                cursor = db.query(HistoryEntry.TABLE_NAME, projector, selection, selectionArgs, null, null, sortOrder);

                break;
            case EXERCISES:
                cursor = db.query(ExerciseEntry.TABLE_NAME, projector, selection, selectionArgs, null, null, sortOrder);

                break;
            case EXERCISE_ID:
                selection = ExerciseEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ExerciseEntry.TABLE_NAME, projector, selection, selectionArgs, null, null, sortOrder);

                break;
            case HISTORY:
                cursor = db.query(HistoryEntry.TABLE_NAME, projector, selection, selectionArgs, null, null, sortOrder);

                break;
            case HISTORY_ID:
                selection = HistoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(HistoryEntry.TABLE_NAME, projector, selection, selectionArgs, null, null, sortOrder);

                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch(match) {
            case REGIMES:
                return RegimeEntry.CONTENT_LIST_TYPE;
            case REGIME_ID:
                return RegimeEntry.CONTENT_ITEM_TYPE;
            case EXERCISES:
                return ExerciseEntry.CONTENT_LIST_TYPE;
            case EXERCISE_ID:
                return ExerciseEntry.CONTENT_ITEM_TYPE;
            case REGIME_EXERCISES:
                return ExerciseEntry.CONTENT_LIST_TYPE;
            case HISTORY:
                return HistoryEntry.CONTENT_LIST_TYPE;
            case HISTORY_ID:
                return HistoryEntry.CONTENT_ITEM_TYPE;
            case REGIME_HISTORY:
                return HistoryEntry.CONTENT_LIST_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case REGIMES:
                return insertRegime(uri, contentValues);
            case EXERCISES:
                return insertExercise(uri, contentValues);
            case HISTORY:
                return insertHistory(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertRegime(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(RegimeEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.v(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        Log.v(LOG_TAG, "Successfully inserted id " + id + " into table " + RegimeEntry.TABLE_NAME);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertExercise(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(ExerciseEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.v(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        Log.v(LOG_TAG, "Successfully inserted id " + id + " into table " + ExerciseEntry.TABLE_NAME);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertHistory(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(HistoryEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.v(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        Log.v(LOG_TAG, "Successfully inserted id " + id + " into table " + HistoryEntry.TABLE_NAME);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
