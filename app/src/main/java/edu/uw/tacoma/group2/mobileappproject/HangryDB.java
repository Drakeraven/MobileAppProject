package edu.uw.tacoma.group2.mobileappproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tacoma.group2.mobileappproject.order.OrdersContent;

public class HangryDB {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Order.db";
    public static final String ORDER_TABLE = "Order";

    private OrderDBHelper mOrderDBHelper;
    private SQLiteDatabase mSqlLiteDatabase;

    public HangryDB(Context context) {
        mOrderDBHelper = new OrderDBHelper(context, DB_NAME, null, DB_VERSION);
        mSqlLiteDatabase = mOrderDBHelper.getWritableDatabase();
    }

    public boolean insertOrder(String hid, String foods, String price) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("hid", hid);
        contentValues.put("foods", foods);
        contentValues.put("price", price);

        long rowId = mSqlLiteDatabase.insert("Order", null, contentValues);
        return rowId != -1;
    }

    public void deleteOrders() {
        mSqlLiteDatabase.delete(ORDER_TABLE, null, null);
    }

    public List<OrdersContent> getOrders() {
            String[] columns = {
                    "hid", "foods", "price"
            };

        Cursor c = mSqlLiteDatabase.query(
                ORDER_TABLE,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        c.moveToFirst();
        List<OrdersContent> list = new ArrayList<>();
        for (int i = 0; i < c.getCount(); i++) {
            String hid = c.getString(0);
            String foods = c.getString(1);
            String price = c.getString(2);
            //TODO: Create new orders content object here

            c.moveToNext();
        }
        return null;

    }


    class OrderDBHelper extends SQLiteOpenHelper {

        private final String CREATE_ORDER_SQL;
        private final String DROP_ORDER_SQL;


        public OrderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_ORDER_SQL = context.getString(R.string.CREATE_ORDER_SQL);
            DROP_ORDER_SQL = context.getString(R.string.DROP_ORDER_SQL);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mSqlLiteDatabase.execSQL(CREATE_ORDER_SQL);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            mSqlLiteDatabase.execSQL(DROP_ORDER_SQL);
            onCreate(mSqlLiteDatabase);
        }
    }
}
