package com.phduo.hiit;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by phduo on 10/7/2017.
 */

public class Regime implements Parcelable {

    private static final MyAppApplication GLOBALVARS = new MyAppApplication();

    private String name;
    private String description;
    private int setTime;
    private int setCount;
    private int totalTime;
    private ArrayList<Exercise> exercises;
    private List<History> history;
    private int id;

    //Blank Regime Constructor
    public Regime() {
        this.name = "";
        this.description = "";
        this.setTime = 0;
        this.setCount = 0;
        this.totalTime = 0;
        this.exercises = new ArrayList<Exercise>();
        this.history = null;
        this.id = 0;
    }

    //General constructor when creating new Regime
    public Regime(String name, String description, int setTime, int setCount, int totalTime, ArrayList<Exercise> exerciseList) {
        this.name = name;
        this.description = description;
        this.setTime = setTime;
        this.setCount = setCount;
        this.totalTime = totalTime;
        this.history = new LinkedList<History>();

        if (exerciseList != null) {
            this.exercises = exerciseList;
        } else {
//            Resources res = context.getResources();

            exercises = new ArrayList<Exercise>();
            //Adding default exercises for newly created Regimes
            exercises.add(new Exercise("Exercise", "", GLOBALVARS.MINUTE));
            exercises.add(new Exercise("Rest", "", GLOBALVARS.SECOND * 20));
//            exercises.add(new Exercise("Exercise", "", res.getInteger(R.integer.Minute)));
//            exercises.add(new Exercise("Rest", "", res.getInteger(R.integer.Second) * 20));

        }
    }

    //Constructor for Regime from database
    public Regime(String name, String description, int setTime, int setCount, int totalTime, ArrayList<Exercise> exerciseList, List<History> historyList, int id) {
        this.name = name;
        this.description = description;
        this.setTime = setTime;
        this.setCount = setCount;
        this.totalTime = totalTime;

        if (exerciseList != null) {
            this.exercises = exerciseList;
        } else {
            this.exercises = new ArrayList<Exercise>();
        }

        if (historyList != null) {
            this.history = historyList;
        } else {
            this.history = new LinkedList<History>();
        }

        this.id = id;
    }

    public int getId() { return this.id; }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getSetTime() {
        return this.setTime;
    }

    public int getSetCount() {
        return this.setCount;
    }

    public int getTotalTime() {
        return this.totalTime;
    }

    public ArrayList<Exercise> getExercises() {
        return this.exercises;
    }

    public void editRegime(String newName, String newDescription, int newSetTime, int newSetCount, int newTotalTime) {

    }

    public void editName(String newName) {
        this.name = newName;
    }

    public void editDescription(String newDescription) {
        this.description = newDescription;
    }

    public void editSetTime(int newSetTime) {
        this.setTime = newSetTime;
    }

    public void editSetCount(int newSetCount) {
        this.setCount = newSetCount;
    }

    public void editTotalTime(int newTotalTime) {
        this.totalTime = newTotalTime;
    }

    public ArrayList<Exercise> addExercise(Exercise exercise) {
        exercises.add(exercise);

        return exercises;
    }

    public int deleteExercise(int exerciseIndex) {
        Exercise rmExercise = exercises.get(exerciseIndex);
        exercises.remove(exerciseIndex);

        return rmExercise.getId();
    }

    public List<History> addHistory(History h) {
        history.add(h);

        return history;
    }

    // ############################################
    // PARCELABLE METHODS

    public static final Parcelable.Creator<Regime> CREATOR
            = new Parcelable.Creator<Regime>() {
        public Regime createFromParcel(Parcel in) {
            return new Regime(in);
        }

        public Regime[] newArray(int size) {
            return new Regime[size];
        }
    };

    private Regime(Parcel in) {
        try {
            this.name = in.readString();
            this.description = in.readString();
            this.setTime = in.readInt();
            this.setCount = in.readInt();
            this.totalTime = in.readInt();
            this.exercises = new ArrayList<Exercise>();
            in.readTypedList(this.exercises, Exercise.CREATOR);
            this.history = new ArrayList<History>();
            in.readTypedList(this.history, History.CREATOR);
            this.id = in.readInt();

            in.setDataPosition(0);
        } finally {
            in.recycle();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.name);
        parcel.writeString(this.description);
        parcel.writeInt(this.setTime);
        parcel.writeInt(this.setCount);
        parcel.writeInt(this.totalTime);
        parcel.writeTypedList(this.exercises);
        parcel.writeTypedList(this.history);
        parcel.writeInt(this.id);

        parcel.setDataPosition(0);
    }
}
