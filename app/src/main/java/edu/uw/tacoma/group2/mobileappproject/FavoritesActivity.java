package edu.uw.tacoma.group2.mobileappproject;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import edu.uw.tacoma.group2.mobileappproject.friend.FriendFragment;
import edu.uw.tacoma.group2.mobileappproject.order.OrdersContent;
import edu.uw.tacoma.group2.mobileappproject.order.OrdersFragment;

/**
 * This class is used to setup and display the Activity which will display the users favorite orders
 * and restaurants.
 * @author Harlan Stewart
 * @version 1.0
 */
public class FavoritesActivity extends AppCompatActivity implements  OrdersFragment.OrdersTabListener {
    private ViewPager mViewPager;
    private TabPagerAdapter mAdapter;

    /**
     * Creates the toolbar, tab layout, and establishes the tabPageradapter to
     * handle the event based on the users tab choice.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_fav);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new TabPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.containter_fav);
        mViewPager.setAdapter(mAdapter);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_fav);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_fav);
    }

    /**
     * Not yet implemented: Will handle the functionality for when a user chooses the order
     * tab.
     * @param item
     */
    @Override
    public void orderTabListener(OrdersContent item) {

    }



    /**
     * public adapter class used to handle which tab of the activity the user has currently selected
     * or changes to.
     */
    public class TabPagerAdapter extends FragmentPagerAdapter {

        /**
         * public constructor for tab adapter.
         * @param fm
         */
        public TabPagerAdapter(FragmentManager fm){super(fm);}

        /**
         * returns the fragment for the tab item the user has selected.
         *
         * @param position which tab is selected
         * @return the fragment of corresponding position
         */
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    Fragment friends = FriendFragment.newInstance(1);
                    return friends;
                case 1:
                    //Fragment restaurants = RestaurantsFragment.newInstance(1);
                    return null;
                case 2:
                    Fragment orders = OrdersFragment.newInstance(1);
                    return orders;
                default:
                    return null;
            }
        }

        /**
         * Returns the number of tabs for the activity.
         * @return
         */
        @Override
        public int getCount() {
            return 2;
        }
    }
}
