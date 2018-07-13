package com.example.kausthubram.login;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class Project implements Parcelable {


    private String name;
    private String type;
    private int positions;
    private String keySkills;
    private String allSkills;
    private String briefDescription;
    private String wholeDescription;
    private int id;
    private int currentPeople;
    private String length;

    private String author;

    private Map<Integer,String> applicants;
    private Map<Integer,String> accepted;


    public Project() {}

    public Project(String name, String type, int positions, String keySkills,
                   String allSkills, String briefDescription,
                   String wholeDescription,
                   int id,String author,String length,int currentPeople) {
        this.name = name;
        this.type = type;
        this.positions = positions;
        this.keySkills = keySkills;
        this.allSkills = allSkills;
        this.briefDescription = briefDescription;
        this.wholeDescription = wholeDescription;
        this.id = id;
        this.author = author;
        this.length = length;
        this.currentPeople = currentPeople;
        this.applicants = applicants;
        this.accepted = accepted;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPositions() {
        return positions;
    }

    public String getKeySkills() {
        return keySkills;
    }

    public String getAllSkills() {
        return allSkills;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public String getWholeDescription() {
        return wholeDescription;
    }


    public String getAuthor(){
        return author;
    }
    public int  getid(){return id;}

    public String getLength(){
        return length;
    }

    public int getCurrentPeople() {
        return currentPeople;
    }

    protected Project(Parcel in) {
        name = in.readString();
        type = in.readString();
        positions = in.readInt();
        keySkills = in.readString();
        allSkills = in.readString();
        briefDescription = in.readString();
        wholeDescription = in.readString();
        id = in.readInt();
        author = in.readString();
        length = in.readString();
        currentPeople = in.readInt();
        applicants = in.readHashMap(String.class.getClassLoader());
        accepted = in.readHashMap(String.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeInt(positions);
        dest.writeString(keySkills);
        dest.writeString(allSkills);
        dest.writeString(briefDescription);
        dest.writeString(wholeDescription);
        dest.writeInt(id);
        dest.writeString(author);
        dest.writeString(length);
        dest.writeInt(currentPeople);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
}