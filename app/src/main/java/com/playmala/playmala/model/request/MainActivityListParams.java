package com.playmala.playmala.model.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.playmala.playmala.AppCookie;
import com.playmala.playmala.util.TimeUtil;

/**
 * author：zhangguobing on 2017/7/19 11:35
 * email：bing901222@qq.com
 */

public class MainActivityListParams implements Parcelable {

    public int activity_type_id;
    public String city;
    public String sort_type;
    public String state;
    public String begin_at;
    public String end_at;
    public String area;

    public static MainActivityListParams getDefault(){
        MainActivityListParams  params = new MainActivityListParams();
        params.state = "1";
        params.city = AppCookie.getCity();
        params.begin_at = TimeUtil.getStartTime(0);
        params.end_at = TimeUtil.getEndTime(7);
        params.sort_type = "time";
        return params;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.activity_type_id);
        dest.writeString(this.city);
        dest.writeString(this.sort_type);
        dest.writeString(this.state);
        dest.writeString(this.begin_at);
        dest.writeString(this.end_at);
        dest.writeString(this.area);
    }

    public MainActivityListParams() {
    }

    protected MainActivityListParams(Parcel in) {
        this.activity_type_id = in.readInt();
        this.city = in.readString();
        this.sort_type = in.readString();
        this.state = in.readString();
        this.begin_at = in.readString();
        this.end_at = in.readString();
        this.area = in.readString();
    }

    public static final Parcelable.Creator<MainActivityListParams> CREATOR = new Parcelable.Creator<MainActivityListParams>() {
        @Override
        public MainActivityListParams createFromParcel(Parcel source) {
            return new MainActivityListParams(source);
        }

        @Override
        public MainActivityListParams[] newArray(int size) {
            return new MainActivityListParams[size];
        }
    };
}
