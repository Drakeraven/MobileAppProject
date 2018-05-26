package edu.uw.tacoma.group2.mobileappproject.hangout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Hangout {
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

    private Hangout(String hid, String restName, String numMems, String closedOpen){
        this.mHid = hid;
        this.mRestName = restName;
        this.mNumMembers = numMems;
        this.mClosedOpen = closedOpen;
    }

    private Hangout(String hid, String fid, String ordered, String fName, String price){
        this.mHid = hid;
        this.mFid = fid;
        this.mOrdered = ordered;
        this.mFriendName = fName;
        this.mPrice = price;
    }

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
