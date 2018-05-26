package edu.uw.tacoma.group2.mobileappproject.order.OrderMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<FoodItem> ITEMS = new ArrayList<FoodItem>();

    private static final int COUNT = 10;

    static {
        createMenu();
    }

    private static void createMenu() {
        ITEMS.add(new FoodItem("1","Hamburger", "5.99"));
        ITEMS.add(new FoodItem("2","Caesar Salad", "3.99"));
        ITEMS.add(new FoodItem("3", "Tacos", "4.99"));
        ITEMS.add(new FoodItem("4", "Stroganoff", "7.99"));
        ITEMS.add(new FoodItem("5", "Soup/Salad/Breadsticks", "11.99"));
        ITEMS.add(new FoodItem("6", "Butter Chicken Curry", "6.50"));
        ITEMS.add(new FoodItem("7", "Grande Burrito", "8.50"));
        ITEMS.add(new FoodItem("8", "Bulgogi", "9.99"));
        ITEMS.add(new FoodItem("9", "Sushi Platter", "12.00"));
        ITEMS.add(new FoodItem("10", "Lava Cake", "4.99"));
    }




    /**
     * A dummy item representing a piece of foodName.
     */
    public static class FoodItem {
        public final String id;
        public final String foodName;
        public final String foodPrice;
        private boolean isSelected = false;

        public FoodItem(String id, String foodName, String foodPrice) {
            this.id = id;
            this.foodName = foodName;
            this.foodPrice = foodPrice;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public boolean isSelected() {
            return isSelected;
        }

        @Override
        public String toString() {
            return foodName;
        }
    }
}
