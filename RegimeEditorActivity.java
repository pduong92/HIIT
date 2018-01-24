package com.phduo.hiit;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RegimeEditorActivity extends AppCompatActivity {

    private final String LOG_TAG = RegimeEditorActivity.class.getName();
    private static final MyAppApplication GLOBALVARS = new MyAppApplication();

    private Resources res;

    private Regime regime;
    private ListView exerciseListView;
    private ExerciseAdapter exerciseAdapter;

    private final int ADD_EXERCISE_CODE = 1;
    private final int EDIT_EXERCISE_CODE = 2;


//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if (savedInstanceState != null) {
//            regime = savedInstanceState.getParcelable(REGIME_KEY);
//
//            initEditor();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regime_editor);

        res = RegimeEditorActivity.this.getResources();

        Intent intent = getIntent();
        int header = intent.getIntExtra(GLOBALVARS.HEADER_KEY, -1);
        if (header >= 0) {
            RegimeEditorActivity.this.setTitle(res.getString(header));
        }

        //If not editing a Regime, we are creating a new one
        regime = (intent.hasExtra(GLOBALVARS.REGIME_KEY)) ? intent.getParcelableExtra(GLOBALVARS.REGIME_KEY) : new Regime();

        exerciseListView = (ListView) findViewById(R.id.edit_regime_exercise_list);
        exerciseAdapter = new ExerciseAdapter(RegimeEditorActivity.this, regime.getExercises());
        exerciseListView.setAdapter(exerciseAdapter);
        TextView emptyView = (TextView) findViewById(R.id.edit_regime_exercise_list_empty);
        exerciseListView.setEmptyView(emptyView);

        //Initialize button
        Button addExerciseButton = (Button) findViewById(R.id.edit_regime_add_exercise_button);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent addExerciseIntent = new Intent(RegimeEditorActivity.this, ExerciseEditorActivity.class);
                startActivityForResult(addExerciseIntent, ADD_EXERCISE_CODE);
            }
        });
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
                Intent saveIntent = new Intent(RegimeEditorActivity.this, OverviewActivity.class);
                startActivity(saveIntent);

                return true;
            case android.R.id.home:
                Intent backIntent = new Intent(RegimeEditorActivity.this, OverviewActivity.class);
                startActivity(backIntent);

                return true;
        }

        return false;
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        EditText nameField = (EditText) findViewById(R.id.edit_regime_name_field);
//        EditText descField = (EditText) findViewById(R.id.edit_regime_desc_field);
//        EditText setCountField = (EditText) findViewById(R.id.edit_regime_set_count_field);
//
//        String name = String.valueOf(nameField.getText());
//        String description = String.valueOf(descField.getText());
//        int sets = (TextUtils.isEmpty(String.valueOf(setCountField.getText()))) ? 0 : Integer.parseInt(String.valueOf(setCountField.getText()));
//
//        if (regime == null) {
//            regime = new Regime(name, description, 0, sets, 0, new ArrayList<Exercise>());
//        } else {
//            regime.editName(name);
//            regime.editDescription(name);
//            regime.editSetCount(sets);
//        }
//
//        outState.putParcelable(REGIME_KEY, regime);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_EXERCISE_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle results = data.getExtras();
                    regime.addExercise(new Exercise(results.getString("name"), results.getString("description"), results.getInt("time")));
                    exerciseAdapter.notifyDataSetChanged();

                    Log.v(LOG_TAG, "Added New Exercise");
                }
                break;
            case EDIT_EXERCISE_CODE:
                if (resultCode == RESULT_OK) {
                    int exerciseIndex = data.getIntExtra(GLOBALVARS.EXERCISE_INDEX_KEY, -1);
                    ArrayList<Exercise> exerciseList = regime.getExercises();
                    Exercise updatedExercise = data.getParcelableExtra(GLOBALVARS.EXERCISE_KEY);
                    Exercise currentExercise = exerciseList.get(exerciseIndex);
                    if (currentExercise.getId() == updatedExercise.getId()) {
                        currentExercise.editExercise(updatedExercise);
                    }
                    exerciseAdapter.notifyDataSetChanged();

                    Log.v(LOG_TAG, "Edited Exercise");
                }
                break;
        }
    }

    private void initEditor() {
        EditText nameField = (EditText) findViewById(R.id.edit_regime_name_field);
        EditText descriptionField = (EditText) findViewById(R.id.edit_regime_desc_field);
        EditText setCountField = (EditText) findViewById(R.id.edit_regime_set_count_field);

        nameField.setText(regime.getName());
        nameField.setText(regime.getDescription());
        nameField.setText(regime.getSetCount() + "");
    }

    private boolean saveRegime() {
        return false;
    }
}
