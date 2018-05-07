package edu.uw.tacoma.group2.mobileappproject.group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.tacoma.group2.mobileappproject.friend.FriendContent;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class GroupContent {
    /**
     * Variables used to parse JSON queries
     */
    private static final String GROUP_NAME = "GroupName";
    private static final String MEMBER_COUNT = "memcount";
    private static final String FRIEND_ID = "fid";
    private static final String GROUP_ID = "groupid";

    private String groupName;
    private String groupCount;
    private List<String> friendIDs;
    private String groupID;

    private GroupContent(String groupName, String groupCount, String groupID) {
        this.groupName = groupName;
        this.groupCount = groupCount;
        this.groupID = groupID;
    }

    /**
     * A method that parses a JSON variable to generate individual Group items
     * @param groupJSON
     * @return
     * @throws JSONException
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
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(String groupCount) {
        this.groupCount = groupCount;
    }

    public List<String> getFriendIDs() {
        return friendIDs;
    }

    public void setFriendIDs(List<String> friendIDs) {
        this.friendIDs = friendIDs;
    }
}
