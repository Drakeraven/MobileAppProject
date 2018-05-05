package edu.uw.tacoma.group2.mobileappproject.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class GroupContent {

    /**
     * An array of sample (group) items.
     */
    public static final List<GroupItem> ITEMS = new ArrayList<GroupItem>();

    /**
     * A map of sample (group) items, by ID.
     */
    public static final Map<String, GroupItem> ITEM_MAP = new HashMap<String, GroupItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(GroupItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static GroupItem createDummyItem(int position) {
        return new GroupItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    //TODO: Define what we want in the group object
    /**
     * A group item representing a piece of name.
     */
    public static class GroupItem {
        public final String id;
        public final String content;
        public final String details;

        public GroupItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
