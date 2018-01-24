package com.phduo.hiit;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.phduo.hiit.data.HiitContract.RegimeEntry;

/**
 * Created by phduo on 11/4/2017.
 */

public class RegimeCursorAdapter extends CursorAdapter {

    public RegimeCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.regime_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameView = (TextView) view.findViewById(R.id.rItem_name);
        TextView descriptionView = (TextView) view.findViewById(R.id.rItem_description);
        TextView timeView = (TextView) view.findViewById(R.id.rItem_time);

        int nameColumnIndex = cursor.getColumnIndex(RegimeEntry.COLUMN_NAME);
        int descriptionColumnIndex = cursor.getColumnIndex(RegimeEntry.COLUMN_DESCRIPTION);
        int timeColumnIndex = cursor.getColumnIndex(RegimeEntry.COLUMN_TIME);

        String name = cursor.getString(nameColumnIndex);
        String description = cursor.getString(descriptionColumnIndex);
        String time = String.valueOf(cursor.getInt(timeColumnIndex));

        nameView.setText(name);
        descriptionView.setText(description);
        timeView.setText(time);
    }
}
