package com.ismael.fastrecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Filter -> Clase POJO para filtros
 * @author Ismael Garcia
 */

public class Filter implements Parcelable{
    private String type;
    private String content;

    public Filter(String type, String content) {
        this.type = type;
        this.content = content;
    }

    protected Filter(Parcel in) {
        type = in.readString();
        content = in.readString();
    }

    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(content);
    }

    public boolean compare(Filter f){
        if(this.getType().equals(f.getType()))
            return true;
        else return false;
    }
}
