package com.dev.pipi.commui.multiSelectDialog;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MultiSelectData implements Parcelable {
    private String title;
    private List<LabelData> labelDatas;
    private String key;

    public MultiSelectData() {
    }

    public MultiSelectData(String title, List<LabelData> labelDatas, String key) {
        this.title = title;
        this.labelDatas = labelDatas;
        this.key = key;
    }

    public MultiSelectData(String title, List<LabelData> labelDatas) {
        this.title = title;
        this.labelDatas = labelDatas;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LabelData> getLabelDatas() {
        return labelDatas;
    }

    public void setLabelDatas(List<LabelData> labelDatas) {
        this.labelDatas = labelDatas;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeList(this.labelDatas);
        dest.writeString(this.key);
    }

    protected MultiSelectData(Parcel in) {
        this.title = in.readString();
        this.labelDatas = new ArrayList<LabelData>();
        in.readList(this.labelDatas, LabelData.class.getClassLoader());
        this.key = in.readString();
    }

    public static final Parcelable.Creator<MultiSelectData> CREATOR = new Parcelable.Creator<MultiSelectData>() {
        @Override
        public MultiSelectData createFromParcel(Parcel source) {
            return new MultiSelectData(source);
        }

        @Override
        public MultiSelectData[] newArray(int size) {
            return new MultiSelectData[size];
        }
    };
}
