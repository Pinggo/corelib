package com.dev.pipi.commui.multiSelectDialog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class LabelData implements Parcelable {
    private boolean isSelect;
    private String name;
    private long id = -1;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static Creator<LabelData> getCREATOR() {
        return CREATOR;
    }

    public LabelData() {
    }

    public LabelData(String name) {
        this.name = name;
    }

    public LabelData(boolean isSelect, String name) {
        this.isSelect = isSelect;
        this.name = name;
    }

    public LabelData(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
    }

    protected LabelData(Parcel in) {
        this.isSelect = in.readByte() != 0;
        this.name = in.readString();
    }

    public static final Parcelable.Creator<LabelData> CREATOR = new Parcelable.Creator<LabelData>() {
        @Override
        public LabelData createFromParcel(Parcel source) {
            return new LabelData(source);
        }

        @Override
        public LabelData[] newArray(int size) {
            return new LabelData[size];
        }
    };
}