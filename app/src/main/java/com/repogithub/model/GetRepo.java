package com.repogithub.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.repogithub.database.ItemsTable;

public class GetRepo implements Parcelable {

    private String name;
    private String html_url;
    private String description;
    private String size;
    private String watchers_count;
    private String open_issues_count;
    public Owner owner;

    protected GetRepo(Parcel in) {
        name = in.readString();
        html_url = in.readString();
        description = in.readString();
        size = in.readString();
        watchers_count = in.readString();
        open_issues_count = in.readString();
    }

    public GetRepo (){

    }

    public static final Creator<GetRepo> CREATOR = new Creator<GetRepo>() {
        @Override
        public GetRepo createFromParcel(Parcel in) {
            return new GetRepo(in);
        }

        @Override
        public GetRepo[] newArray(int size) {
            return new GetRepo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWatchers_count() {
        return watchers_count;
    }

    public void setWatchers_count(String watchers_count) {
        this.watchers_count = watchers_count;
    }

    public String getOpen_issues_count() {
        return open_issues_count;
    }

    public void setOpen_issues_count(String open_issues_count) {
        this.open_issues_count = open_issues_count;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(html_url);
        parcel.writeString(description);
        parcel.writeString(size);
        parcel.writeString(watchers_count);
        parcel.writeString(open_issues_count);
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues(7);

        values.put(ItemsTable.COLUMN_ID, name);
        values.put(ItemsTable.COLUMN_NAME, html_url);
        values.put(ItemsTable.COLUMN_DESCRIPTION, description);
        values.put(ItemsTable.COLUMN_SIZE, size);
        values.put(ItemsTable.COLUMN_WATCHER, watchers_count);
        values.put(ItemsTable.COLUMN_ISSUES, open_issues_count);
        values.put(ItemsTable.COLUMN_IMAGE, owner.avatar_url);
        return values;
    }


}
