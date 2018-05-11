package edu.uw.tacoma.group2.mobileappproject.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents an Order object and its associated values.
 * @author Harlan Stewart
 * @version 1.0
 */
public class OrdersContent {
    public static final List<OrderItem> ITEMS = new ArrayList<OrderItem>();
    public static final Map<String, OrderItem> ITEM_MAP = new HashMap<String, OrderItem>();
    private static final int COUNT = 25;

    /**
     * Currently used to create a dummy list of fake items to test the display
     * of a list of a users favorite order items.
     */
    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    /**
     * Adds an item to the the ITEM_MAP currently using dummy items but can
     * be implemented to add correct values once this class is further developed.
     * @param item
     */
    private static void addItem(OrderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * Currently being used to create a dummy item to the functionality of the order list.
     * @param position
     * @return
     */
    private static OrderItem createDummyItem(int position) {
        return new OrderItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    /**
     * String builder for populating information about items in the OrderList
     * @param position
     * @return
     */
    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * Class used to represent an order item and its respective values.
     */
    public static class OrderItem {
        public final String id;
        public final String content;
        public final String details;

        /**
         * Public constructor for creating an Order Item.
         * @param id id to represent an Order object.
         * @param content string value for content of an order.
         * @param details string value for details about an order.
         */
        public OrderItem(String id, String content, String details){
            this.id = id;
            this.content = content;
            this.details = details;
        }
        @Override
        public String toString(){
            return content;
        }
    }
}
