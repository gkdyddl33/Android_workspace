package com.koreait.pageapp.intent;

import android.os.Parcel;
import android.os.Parcelable;

public class Member implements Parcelable {
    private String id;
    private String pass;
    private String name;


    protected Member(Parcel in) {
        id = in.readString();
        pass = in.readString();
        name = in.readString();
    }

    public Member() {

    }

    // 보내기 위해 직렬화시켜 저장..
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pass);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Member> CREATOR = new Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
