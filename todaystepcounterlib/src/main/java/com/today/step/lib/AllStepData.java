package com.today.step.lib;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class AllStepData implements Serializable, Parcelable {

    //时间，只显示到天 yyyy-MM-dd
    private String day;
    //对应时间的步数
    private String step;

    public AllStepData(String day, String step) {
        this.day = day;
        this.step = step;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.day);
        dest.writeString(this.step);
    }

    protected AllStepData(Parcel in) {
        this.day = in.readString();
        this.step = in.readString();
    }

    public static final Creator<AllStepData> CREATOR = new Creator<AllStepData>() {
        @Override
        public AllStepData createFromParcel(Parcel source) {
            return new AllStepData(source);
        }

        @Override
        public AllStepData[] newArray(int size) {
            return new AllStepData[size];
        }
    };

    @Override
    public String toString() {
        return "AllStepData{" +
                "day='" + day + '\'' +
                ", step='" + step + '\'' +
                '}';
    }
}
