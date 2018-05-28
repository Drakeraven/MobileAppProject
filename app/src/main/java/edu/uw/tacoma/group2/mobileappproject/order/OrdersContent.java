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
    public String mHangoutID;
    public String mFoods;
    public String mPrice;

    public OrdersContent(String mHangoutID, String mFoods, String mPrice) {
        this.mHangoutID = mHangoutID;
        this.mFoods = mFoods;
        this.mPrice = mPrice;
    }

    public String getmHangoutID() {
        return mHangoutID;
    }

    public void setmHangoutID(String mHangoutID) {
        this.mHangoutID = mHangoutID;
    }

    public String getmFoods() {
        return mFoods;
    }

    public void setmFoods(String mFoods) {
        this.mFoods = mFoods;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }
}
