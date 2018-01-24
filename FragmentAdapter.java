package com.phduo.hiit;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.phduo.hiit.data.HiitContract;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by phduo on 11/4/2017.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    public static final String LOG_TAG = FragmentAdapter.class.getName();

    private ArrayList<Regime> dataList;
    private TreeMap<Integer, Regime> regimeMap;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentAdapter(FragmentManager fm, Cursor regimeData, Cursor exerciseData, Cursor historyData) {
        super(fm);
        dataList = generateDataList(regimeData, exerciseData, historyData);
    }

    @Override
    public Fragment getItem(int position) {
        return OverviewFragment.newInstance(dataList.get(position));
    }

    public int getItemId(int position) {
        return dataList.get(position).getId();
    }

    public Regime getRegime(int position) {
        return dataList.get(position);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    private ArrayList<Regime> generateDataList(Cursor regimeData, Cursor exerciseData, Cursor historyData) {
        regimeMap = new TreeMap<Integer, Regime>();

        if (regimeData != null) {
            int idColumnIndex = regimeData.getColumnIndex(HiitContract.RegimeEntry._ID);
            int nameColumnIndex = regimeData.getColumnIndex(HiitContract.RegimeEntry.COLUMN_NAME);
            int descriptionColumnIndex = regimeData.getColumnIndex(HiitContract.RegimeEntry.COLUMN_DESCRIPTION);
            int set_timeColumnIndex = regimeData.getColumnIndex(HiitContract.RegimeEntry.COLUMN_SETTIME);
            int set_countColumnIndex = regimeData.getColumnIndex(HiitContract.RegimeEntry.COLUMN_SETCOUNT);
            int timeColumnIndex = regimeData.getColumnIndex(HiitContract.RegimeEntry.COLUMN_TIME);

            while (regimeData.moveToNext()) {
                int id = regimeData.getInt(idColumnIndex);
                String name = regimeData.getString(nameColumnIndex);
                String description = regimeData.getString(descriptionColumnIndex);
                int setTime = regimeData.getInt(set_timeColumnIndex);
                int setCount = regimeData.getInt(set_countColumnIndex);
                int time = regimeData.getInt(timeColumnIndex);

                regimeMap.put(id, new Regime(name, description, setTime, setCount, time, null, null, id)); //Let the constructor initialize the Activites and History arrays
            }

            Log.v(LOG_TAG, "Parse Regime data complete.");

            if (exerciseData != null) {
                idColumnIndex = exerciseData.getColumnIndex(HiitContract.ExerciseEntry._ID);
                nameColumnIndex = exerciseData.getColumnIndex(HiitContract.ExerciseEntry.COLUMN_NAME);
                descriptionColumnIndex = exerciseData.getColumnIndex(HiitContract.ExerciseEntry.COLUMN_DESCRIPTION);
                timeColumnIndex = exerciseData.getColumnIndex(HiitContract.ExerciseEntry.COLUMN_TIME);
                int regime_idColumnIndex = exerciseData.getColumnIndex(HiitContract.ExerciseEntry.COLUMN_REGIMEID);

                while (exerciseData.moveToNext()) {
                    int id = exerciseData.getInt(idColumnIndex);
                    String name = exerciseData.getString(nameColumnIndex);
                    String description = exerciseData.getString(descriptionColumnIndex);
                    int time = exerciseData.getInt(timeColumnIndex);
                    int regimeID = exerciseData.getInt(regime_idColumnIndex);

                    if (regimeMap.containsKey(regimeID)) {
                        Regime r = regimeMap.get(regimeID);
                        r.addExercise(new Exercise(name, description, time, id));
                        regimeMap.put(regimeID, r);
                    }
                }

//                Log.v(LOG_TAG, "Parse Exercise data complete.");
            }

//                if (historyData != null) {
//                    timeColumnIndex = historyData.getColumnIndex(HistoryEntry.COLUMN_TIME);
//                    int dateColumnIndex = historyData.getColumnIndex(HistoryEntry.COLUMN_DATE);
//                    int set_targetColumnIndex = historyData.getColumnIndex(HistoryEntry.COLUMN_SETTARGET);
//                    int completionColumnIndex = historyData.getColumnIndex(HistoryEntry.COLUMN_COMPLETION);
//                    int regime_idColumnIndex = historyData.getColumnIndex(HistoryEntry.COLUMN_REGIMEID);
//
//                    while(historyData.moveToNext()) {
//                        int id
//                    }
//                }
        }

        return new ArrayList<Regime>(regimeMap.values());
    }

    private void parseCursor(Cursor cursor) {

    }
}
