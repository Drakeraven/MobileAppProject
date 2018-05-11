package edu.uw.tacoma.group2.mobileappproject.group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of the User's groups for displaying purposes.
 * @author Stephanie Day
 * @version 1.0
 */
public class GroupContent {
    /**
     * Variables used to parse JSON queries
     */
    private static final String GROUP_NAME = "GroupName";
    private static final String MEMBER_COUNT = "memcount";
    private static final String FRIEND_ID = "fid";
    private static final String GROUP_ID = "groupid";

    private String mGroupName;
    private String mGroupCount;
    private List<String> mFriendIDs;
    private String mGroupID;

    /**
     * Creates new Group Object
     * @param mGroupName Group's name
     * @param mGroupCount Group's email
     * @param mGroupID Group's ID
     */
    private GroupContent(String mGroupName, String mGroupCount, String mGroupID) {
        this.mGroupName = mGroupName;
        this.mGroupCount = mGroupCount;
        this.mGroupID = mGroupID;
    }

    /**
     * A method that parses a JSON variable to generate individual Group items
     * @param groupJSON Result from the web service request for user's groups
     * @return List of group objects for display
     * @throws JSONException In case malformed JSON
     */
    public static List<GroupContent> parseGroupList(String groupJSON) throws JSONException {
        List<GroupContent> groupList = new ArrayList<>();
        if (groupJSON != null) {
            JSONArray arr = new JSONArray(groupJSON);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                GroupContent course = new GroupContent(obj.getString(GroupContent.GROUP_NAME),
                        obj.getString(GroupContent.MEMBER_COUNT), obj.getString(GroupContent.GROUP_ID));
                groupList.add(course);
            }

        }
        return groupList;
    }

    /**
     * Parses a  JSON to generate a group's member's
     * @param membersJSON Result from a web service request for a group's members
     * @return List of the names of group members
     * @throws JSONException In case malformed JSON
     */
    public static List<String> parseGroupMembers(String membersJSON) throws JSONException {
            List<String> members = new ArrayList<>();
        if (membersJSON != null) {
            JSONArray array = new JSONArray(membersJSON);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                members.add(object.getString(GroupContent.FRIEND_ID));
            }
        }
        return members;
    }

    public String getGroupID() {
        return mGroupID;
    }

    public void setGroupID(String groupID) {
        this.mGroupID = groupID;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        this.mGroupName = groupName;
    }

    public String getGroupCount() {
        return mGroupCount;
    }

    public void setGroupCount(String groupCount) {
        this.mGroupCount = groupCount;
    }

    public List<String> getFriendIDs() {
        return mFriendIDs;
    }

    public void setFriendIDs(List<String> friendIDs) {
        this.mFriendIDs = friendIDs;
    }
}
