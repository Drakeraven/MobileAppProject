package edu.uw.tacoma.group2.mobileappproject.friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class FriendContent implements Serializable {
    //defined JSON tags for parsing and storage:
    private static final String ID = "fid";
    private static final String NAME = "fname";
    private static final String PIC = "fimage";
    private static final String EMAIL = "femail";

    private String frenID;
    private String frenName;
    private String frenImage;
    private String frenEmail;

    private FriendContent(String frenID, String frenName, String frenImage, String frenEmail) {
        this.frenID = frenID;
        this.frenName = frenName;
        this.frenImage = frenImage;
        this.frenEmail = frenEmail;
    }

    /**
     * A method that parses a JSON variable to generate individual friend items
     * @param friendsJSON
     * @return
     * @throws JSONException
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
        return frenID;
    }

    public void setFrenID(String frenID) {
        this.frenID = frenID;
    }

    public String getFrenName() {
        return frenName;
    }

    public void setFrenName(String frenName) {
        this.frenName = frenName;
    }

    public String getFrenImage() {
        return frenImage;
    }

    public void setFrenImage(String frenImage) {
        this.frenImage = frenImage;
    }

    public String getFrenEmail() {
        return frenEmail;
    }

    public void setFrenEmail(String frenEmail) {
        this.frenEmail = frenEmail;
    }
}
