package edu.uw.tacoma.group2.mobileappproject;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import edu.uw.tacoma.group2.mobileappproject.friend.FriendContent;
import edu.uw.tacoma.group2.mobileappproject.order.OrdersContent;
import edu.uw.tacoma.group2.mobileappproject.restaurant.RestaurantsContent;

public class FavoritesActivity extends AppCompatActivity implements FriendFragment.FriendTabListener,
    RestaurantsFragment.RestaurantsTabListener, OrdersFragment.OrdersTabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    //private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private TabPagerAdapter mAdapter;


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
        // may need to add onclick listener for fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_fav);
    }

    @Override
    public void friendTabListener(FriendContent.FriendItem item) {

    }

    @Override
    public void orderTabListener(OrdersContent.OrderItem item) {

    }

    @Override
    public void restaurantsTabListener(RestaurantsContent.RestaurantItem item) {

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
                    Fragment restaurants = RestaurantsFragment.newInstance(1);
                    return restaurants;
                case 2:
                    Fragment orders = OrdersFragment.newInstance(1);
                    return orders;
                default:
                    return null;



            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
