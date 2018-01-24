package com.phduo.hiit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Created by phduo on 11/25/2017.
 */

public class ExerciseEditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = ExerciseEditorActivity.class.getName();

    private static final MyAppApplication GLOBALVARS = new MyAppApplication();

    private Resources res;

    private Exercise exercise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_editor);

        res = ExerciseEditorActivity.this.getResources();
        
        Intent intent = getIntent();
        int header = intent.getIntExtra(GLOBALVARS.HEADER_KEY, -1);
        if (header >= 0) {
            ExerciseEditorActivity.this.setTitle(res.getString(header));
        }

        exercise = (intent.hasExtra(GLOBALVARS.EXERCISE_KEY)) ? intent.getParcelableExtra(GLOBALVARS.EXERCISE_KEY) : null;

        //If Exercise is populated, we initialize the editor
        if (exercise != null) {
            initEditor();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_save:
//                finishResult(Activity.RESULT_OK);

                buildExercise();

                Intent saveIntent = new Intent();
//                saveIntent.putExtra(GLOBALVARS.EXERCISE_KEY, exercise);
                saveIntent.putExtra("name", exercise.getName());
                saveIntent.putExtra("description", exercise.getDescription());
                saveIntent.putExtra("time", exercise.getTime());
                ExerciseEditorActivity.this.setResult(Activity.RESULT_OK, saveIntent);
                finish();

                return true;
            case android.R.id.home:
//                finishResult(Activity.RESULT_CANCELED);

                setResult(Activity.RESULT_CANCELED);
                finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void buildExercise() {
        EditText nameField = (EditText) findViewById(R.id.edit_exercise_name_field);
        EditText descField = (EditText) findViewById(R.id.edit_exercise_desc_field);
        MyNumberPicker minField = (MyNumberPicker) findViewById(R.id.edit_exercise_time_min);
        MyNumberPicker secField = (MyNumberPicker) findViewById(R.id.edit_exercise_time_sec);

        String name = String.valueOf(nameField.getText());
        String description = String.valueOf(descField.getText());
        int minTime = minField.getValue() * GLOBALVARS.MINUTE;
        int secTime = secField.getValue() * GLOBALVARS.SECOND;
        int time = minTime + secTime;

        if (exercise == null) {
            exercise = new Exercise(name, description, time);
        } else {
            exercise.editExercise(name, description, time);
        }
    }

    private void initEditor() {
        EditText nameField = (EditText) findViewById(R.id.edit_exercise_name_field);
        EditText descField = (EditText) findViewById(R.id.edit_exercise_desc_field);
        EditText minField = (EditText) findViewById(R.id.edit_exercise_time_min);
        EditText secField = (EditText) findViewById(R.id.edit_exercise_time_sec);

        int[] time = GLOBALVARS.extractTime(exercise.getTime());

        nameField.setText(exercise.getName());
        descField.setText(exercise.getDescription());
        minField.setText(time[0]);
        secField.setText(time[1]);
    }

    private void finishResult(int result) {
        switch (result) {
            case Activity.RESULT_OK:
                EditText nameField = (EditText) findViewById(R.id.edit_exercise_name_field);
                EditText descField = (EditText) findViewById(R.id.edit_exercise_desc_field);
                EditText minField = (EditText) findViewById(R.id.edit_exercise_time_min);
                EditText secField = (EditText) findViewById(R.id.edit_exercise_time_sec);

                String name = String.valueOf(nameField.getText());
                String description = String.valueOf(descField.getText());
                int minTime = (TextUtils.isEmpty(String.valueOf(minField.getText()))) ? 0 : Integer.parseInt(String.valueOf(minField.getText())) * GLOBALVARS.MINUTE;
                int secTime = (TextUtils.isEmpty(String.valueOf(secField.getText()))) ? 0 : Integer.parseInt(String.valueOf(secField.getText())) * GLOBALVARS.SECOND;
                int time = minTime + secTime;

                Intent saveIntent = new Intent();
                saveIntent.putExtra("name", name);
                saveIntent.putExtra("description", description);
                saveIntent.putExtra("time", time);
                setResult(Activity.RESULT_OK, saveIntent);

                break;
            case Activity.RESULT_CANCELED:
                setResult(Activity.RESULT_CANCELED);

                break;
        }

        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
