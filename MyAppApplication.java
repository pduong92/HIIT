package com.phduo.hiit;

import android.app.Application;

/**
 * Created by phduo on 12/9/2017.
 */

public class MyAppApplication extends Application {

    public static final int SECOND = 1000;
    public static final int MINUTE = 60 * SECOND;

    public static final String HEADER_KEY = "header";
    public static final String INDEX_KEY = "index";
    public static final String REGIME_KEY = "regime";
    public static final String REGIME_ID_KEY = "regimeId";
    public static final String EXERCISE_KEY = "exercise";
    public static final String EXERCISE_ID_KEY = "exerciseId";
    public static final String EXERCISE_INDEX_KEY = "exerciseIndex";

    public MyAppApplication() {}

    public static int[] extractTime(int millisecs) {
        int secFrac = millisecs % MINUTE;
        int minFrac = millisecs - secFrac;

        return new int[] {minFrac / MINUTE, secFrac / SECOND};
    }
}
