package edu.uw.tacoma.group2.mobileappproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.uw.tacoma.group2.mobileappproject.hangout.HangoutActivity;
import edu.uw.tacoma.group2.mobileappproject.order.OrderMenu.MyFoodItemRecyclerViewAdapter;

/**
 * This class is used to display a confirmation to the user after they have ordered an item and
 * the information about their order has been inserted into the correct database tables. When the
 * user has placed their order they will be shown this activity which will display a confirmation
 * explaining the hangout creator will pick up their order and also display the cost of their individual
 * order. The user will then click on the order/pay button which currently will take them back to the list
 * of hangouts they are part of. Further implementation will allow the user to pay for their item.
 * @author Harlan Stewart
 * @version 1.2
 */
public class OrderCompleteActivity extends AppCompatActivity {
    private TextView mCompleteTotal;
    private Button mOrderPayBtn;

    /**
     * Method for setting up the views of the order complete activity and setting up the
     * functionality for when the user clicks on the order/pay button.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_complete);
        mCompleteTotal = findViewById(R.id.order_complete_total);
        mCompleteTotal.setText(MyFoodItemRecyclerViewAdapter.USER_TOTAL);
        mOrderPayBtn = (Button) findViewById(R.id.order_pay_button);
        mOrderPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(v.getContext(), HangoutActivity.class);
                v.getContext().startActivity(returnIntent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}