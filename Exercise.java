package com.phduo.hiit;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by phduo on 10/7/2017.
 */

public class Exercise implements Parcelable{

    private String name;
    private String description;
    private int time;
    private int id;

    //General constructor for newly created exercise
    public Exercise(String name, String description, int time) {
        this.name = name;
        this.description = description;
        this.time = time;
    }

    //Constructor for when we retrieve existing exercise from data
    public Exercise(String name, String description, int time, int id) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getTime() {
        return this.time;
    }

    public int getId() {
        return this.id;
    }

    public void editExercise(String newName, String newDescription, int newTime) {
        this.name = newName;
        this.description = newDescription;
        this.time = newTime;
    }

    public void editExercise(Exercise updatedExercise) {
        this.name = updatedExercise.name;
        this.description = updatedExercise.description;
        this.time = updatedExercise.time;
    }

    public void editName(String newName) {
        this.name = newName;
    }

    public void editDescription(String newDescription) {
        this.description = newDescription;
    }

    public void editTime(int newTime) {
        this.time = newTime;
    }

    public static final Parcelable.Creator<Exercise> CREATOR
            = new Parcelable.Creator<Exercise>() {
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    private Exercise(Parcel in) {
        try {
            this.description = in.readString();
            this.name = in.readString();
            this.id = in.readInt();
            this.time = in.readInt();

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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.description);
        parcel.writeString(this.name);
        parcel.writeInt(this.id);
        parcel.writeInt(this.time);

        parcel.setDataPosition(0);
    }

    @Override
    public String toString() {
        return this.name + " " + this.description + " " + this.time;
    }
}
