package com.phduo.hiit.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by phduo on 9/30/2017.
 */

public final class HiitContract {

    private HiitContract() {};

    public static final String CONTENT_AUTHORITY = "com.phduo.hiit";
    public static final String BASE_CONTENT = "content://" + CONTENT_AUTHORITY;

    public static final Uri BASE_CONTENT_URI = Uri.parse(BASE_CONTENT);

    public static final String PATH_REGIMES = "Regimes";
    public static final String BY_REGIME_ID = "regID";

    public static class RegimeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REGIMES);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REGIMES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REGIMES;

        public static final String TABLE_NAME = "Regimes";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SETTIME = "setTime";
        public static final String COLUMN_SETCOUNT = "setCount";
        public static final String COLUMN_TIME = "time";

        public static final String DEFAULT_NAME = "Regime";
        public static int DEFAULT_COUNT = 0;
    }

    public static final String PATH_EXERCISES = "Exercises";

    public static class ExerciseEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EXERCISES);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXERCISES;

        public static final String TABLE_NAME = "Exercises";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_ORDERID = "orderId";
        public static final String COLUMN_REGIMEID = "regimeId";
        public static final String COLUMN_PICTUREID = "pictureId";

        public static final String DEFAULT_NAME = "Exercise";
        public static int DEFAULT_COUNT = 0;
    }

    public static final String PATH_HISTORY = "History";

    public static class HistoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HISTORY);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORY;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORY;

        public static final String TABLE_NAME = "History";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_REGIMEID = "regimeId";
        public static final String COLUMN_SETTARGET = "setTarget";
        public static final String COLUMN_COMPLETION = "completionPercent";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DATE = "date";
    }
}
