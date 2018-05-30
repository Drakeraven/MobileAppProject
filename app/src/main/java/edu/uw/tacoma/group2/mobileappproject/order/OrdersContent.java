package edu.uw.tacoma.group2.mobileappproject.order;

//TODO: modify this class to parse sqlLite to display previous orders
/**
 * This class represents an Order object and its associated values.
 * @author Harlan Stewart
 * @version 1.0
 */
public class OrdersContent {
    public String mHangoutID;
    public String mFoods;
    public String mPrice;

    public OrdersContent(String mHangoutID, String mFoods, String mPrice) {
        this.mHangoutID = mHangoutID;
        this.mFoods = mFoods;
        this.mPrice = mPrice;
    }
}
