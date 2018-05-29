package edu.uw.tacoma.group2.mobileappproject.hangout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Hangout object and its fields. A hangout is created by the user choosing
 * one of their groups and a restaurant for the hangout. The members of this class also correlate with
 * the column names of the database table which they will be used for retrieving and sending data.
 * @author Harlan Stewart
 * @version 1.0
 */
public class Hangout implements Serializable {
    public static final String HID = "hid";
    public static final String REST_NAME = "rest_name";
    public static final String NUM_MEMBERS = "num_members";
    public static final String CLOSED_OPEN = "closed_open";
    private String mHid;
    private String mRestName;
    private String mNumMembers;
    private String mClosedOpen;

    public static final String FID = "fid";
    public static final String ORDERED = "ordered";
    public static final String FRIEND_NAME = "friend_name";
    public static final String PRICE = "price";
    private String mFid;
    private String mOrdered;
    private String mFriendName;
    private String mPrice;

    public static final List<Hangout> hangList = new ArrayList<>();

    /**
     * Private constructor used to create a hangout object that is related to the database
     * hangout table.
     * @param hid The time and date used for the unique primary key.
     * @param restName The name of the restaurant the user chose for the hangout.
     * @param numMems The total number of people in this hangout.
     * @param closedOpen Value for knowing if a hangout has already ended or not.
     */
    private Hangout(String hid, String restName, String numMems, String closedOpen){
        this.mHid = hid;
        this.mRestName = restName;
        this.mNumMembers = numMems;
        this.mClosedOpen = closedOpen;
    }

    /**
     * Private constructor used to create a hangout object that is related to the database
     * hangout_mems table.
     * @param hid The time and date used for the unique primary key.
     * @param fid The id of the person who is part of this hangout.
     * @param ordered Value to indicate whether this person has ordered or not.
     * @param fName The name of the person who is part of this hangout.
     * @param price The price of the item the person has ordered.
     */
    private Hangout(String hid, String fid, String ordered, String fName, String price){
        this.mHid = hid;
        this.mFid = fid;
        this.mOrdered = ordered;
        this.mFriendName = fName;
        this.mPrice = price;
    }

    /**
     * Method using for parsing a JSON string into a list of newly created Hangout objects.
     * @param hangJSON The json string that will be parsed.
     * @return a list of hangout objects.
     * @throws JSONException thrown when the JSON string is invalid.
     */
    public static List<Hangout> parseHangout(String hangJSON) throws JSONException {
        List<Hangout> hList = new ArrayList<>();
        if(hangJSON != null) {
            JSONArray arr = new JSONArray(hangJSON);
            for(int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                Hangout hangout = new Hangout(obj.getString(Hangout.HID),
                        obj.getString(Hangout.REST_NAME),
                        obj.getString(Hangout.NUM_MEMBERS),
                        obj.getString(Hangout.CLOSED_OPEN));
                hList.add(hangout);
            }
        }
        return hList;
    }

    /**
     * Method used for parsing a list of hangout objects based on the hangout_mems table.
     * @param hangJSON The json string that will be parsed.
     * @return a list of hangout objects.
     * @throws JSONException thrown when the JSON string is invalid.
     */
    public static List<Hangout> parseHangoutMems(String hangJSON) throws JSONException {
        List<Hangout> hList = new ArrayList<>();
        if(hangJSON != null) {
            JSONArray arr = new JSONArray(hangList);
            for(int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                Hangout hangout = new Hangout(obj.getString(Hangout.HID),
                        obj.getString(Hangout.FID),
                        obj.getString(Hangout.ORDERED),
                        obj.getString(Hangout.FRIEND_NAME),
                        obj.getString(Hangout.PRICE));
                hangList.add(hangout);
            }
        }
        return hangList;
    }

    public String getHid() {
        return mHid;
    }

    public void setHid(String mHid) {
        this.mHid = mHid;
    }

    public String getRestName() {
        return mRestName;
    }

    public void setRestName(String mRestName) {
        this.mRestName = mRestName;
    }

    public String getNumMembers() {
        return mNumMembers;
    }

    public void setNumMembers(String mNumMembers) {
        this.mNumMembers = mNumMembers;
    }

    public String getClosedOpen() {
        return mClosedOpen;
    }

    public void setClosedOpen(String mClosedOpen) {
        this.mClosedOpen = mClosedOpen;
    }

    public String getFid() {
        return mFid;
    }

    public void setFid(String mFid) {
        this.mFid = mFid;
    }

    public String getOrdered() {
        return mOrdered;
    }

    public void setOrdered(String mOrdered) {
        this.mOrdered = mOrdered;
    }

    public String getFriendName() {
        return mFriendName;
    }

    public void setFriendName(String mFriendName) {
        this.mFriendName = mFriendName;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String mPrice) {
        this.mPrice = mPrice;
    }
}