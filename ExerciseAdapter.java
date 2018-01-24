package com.phduo.hiit;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by phduo on 11/18/2017.
 */

public class ExerciseAdapter extends ArrayAdapter<Exercise>{

    public ExerciseAdapter(Activity context, ArrayList<Exercise> exerciseList) {
        super(context, 0, exerciseList);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.exercise_item, parent, false);
        }

        Exercise exercise = ExerciseAdapter.this.getItem(position);

        TextView nameView = (TextView) listItemView.findViewById(R.id.aItem_name);
        TextView descriptionView = (TextView) listItemView.findViewById(R.id.aItem_description);

        nameView.setText(exercise.getName());
        descriptionView.setText(exercise.getDescription());

        return listItemView;
    }
}
