package com.phduo.hiit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by phduo on 11/4/2017.
 */

public class History implements Parcelable{

    private int date;
    private int setTarget;
    private long completion;
    private int time;
    private int regimeID;

    public History(int date, int setTarget, long completion, int time, int regimeID) {
        this.date = date;
        this.setTarget = setTarget;
        this.completion = completion;
        this.time = time;
        this.regimeID = regimeID;
    }

    public static final Parcelable.Creator<History> CREATOR
            = new Parcelable.Creator<History>() {
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        public History[] newArray(int size) {
            return new History[size];
        }
    };

    private History(Parcel in) {
        this.date = in.readInt();
        this.setTarget = in.readInt();
        this.completion = in.readLong();
        this.time = in.readInt();
        this.regimeID = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.date);
        parcel.writeInt(this.setTarget);
        parcel.writeLong(this.completion);
        parcel.writeInt(this.time);
        parcel.writeInt(this.regimeID);

        parcel.setDataPosition(0);
    }
}
