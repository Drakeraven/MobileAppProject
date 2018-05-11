package edu.uw.tacoma.group2.mobileappproject.friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model of a user's friends, for populating and interaction.
 * @author Stephanie Day
 * @version 1.0
 */
public class FriendContent implements Serializable {
    /**Defined JSON tags for parsing and storage*/
    private static final String ID = "fid";
    private static final String NAME = "fname";
    private static final String PIC = "fimage";
    private static final String EMAIL = "femail";

    private String mFrenID;
    private String mFrenName;
    private String mFrenImage;
    private String mFrenEmail;

    /**
     * Constructor to build a friend object.
     * @param mFrenID Friend's Facebook ID.
     * @param mFrenName Friend's Name
     * @param mFrenImage Link to picture of the friend
     * @param mFrenEmail Friend's email
     */
    private FriendContent(String mFrenID, String mFrenName, String mFrenImage, String mFrenEmail) {
        this.mFrenID = mFrenID;
        this.mFrenName = mFrenName;
        this.mFrenImage = mFrenImage;
        this.mFrenEmail = mFrenEmail;
    }

    /**
     * A method that parses a JSON variable to generate individual friend items
     * @param friendsJSON Returned query from web service database
     * @return List of constructed friend objects
     * @throws JSONException In case malformed JSONS
     */
    public static List<FriendContent> giveMeFriends(String friendsJSON) throws JSONException {
        List<FriendContent> FriendList = new ArrayList<FriendContent>();
        if (friendsJSON != null) {

            JSONArray arr = new JSONArray(friendsJSON);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                FriendContent course = new FriendContent(obj.getString(FriendContent.ID), obj.getString(FriendContent.NAME)
                        , obj.getString(FriendContent.PIC), obj.getString(FriendContent.EMAIL));
                FriendList.add(course);
            }

        }

        return FriendList;
    }


    public String getFrenID() {
        return mFrenID;
    }

    public void setFrenID(String frenID) {
        this.mFrenID = frenID;
    }

    public String getFrenName() {
        return mFrenName;
    }

    public void setmFrenName(String mFrenName) {
        this.mFrenName = mFrenName;
    }

    public String getFrenImage() {
        return mFrenImage;
    }

    public void setFrenImage(String frenImage) {
        this.mFrenImage = frenImage;
    }

    public String getFrenEmail() {
        return mFrenEmail;
    }

    public void setFrenEmail(String frenEmail) {
        this.mFrenEmail = frenEmail;
    }
}
