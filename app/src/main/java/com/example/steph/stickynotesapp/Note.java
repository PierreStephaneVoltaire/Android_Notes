package com.example.steph.stickynotesapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by steph on 1/1/2017.
 */

public class Note implements Parcelable {
    private String title;
    private String Description;
    private String content;
    private Date CreateDate;
    private Date LastModified;

    public Note() {
    }


    public Note(String title, String description, String content, Date createDate, Date lastModified) {
        this.title = title;
        Description = description;
        this.content = content;
        CreateDate = createDate;
        LastModified = lastModified;
    }

    protected Note(Parcel in) {
        title = in.readString();
        Description = in.readString();
        content = in.readString();
        CreateDate = new Date(in.readLong());
        LastModified = new Date(in.readLong());
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    public Date getLastModified() {
        return LastModified;
    }

    public void setLastModified(Date lastModified) {
        LastModified = lastModified;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(Description);
        dest.writeString(content);
        dest.writeLong(CreateDate.getTime());
        dest.writeLong(LastModified.getTime());

    }
}


