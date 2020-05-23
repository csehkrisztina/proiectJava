package com.example.mycostcalendar.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "cost_table")
public class Cost implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private double price;
    private String description;
    private Date date;

    public Cost(String title, double price, String description, Date date) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.date = date;
    }

    protected Cost(Parcel in) {
        id = in.readInt();
        title = in.readString();
        price = in.readDouble();
        description = in.readString();
        long tmpDate = in.readLong();
        date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<Cost> CREATOR = new Creator<Cost>() {
        @Override
        public Cost createFromParcel(Parcel in) {
            return new Cost(in);
        }

        @Override
        public Cost[] newArray(int size) {
            return new Cost[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeDouble(price);
        dest.writeString(description);
        dest.writeLong(date != null ? date.getTime() : -1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
