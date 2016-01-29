package com.atlantbh.boristomic.movieapplication.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boristomic on 29/01/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    /**
     * Constructor of ViewPagerAdapter class, passes FragmentManager to super constructor
     *
     * @param manager <code>FragmentManager</code> type object
     */
    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    /**
     * Adds fragment to fragment list, and fragment title to fragmentTitleList.
     *
     * @param fragment <code>Fragment</code> type object
     * @param title    <code>String</code> type object with fragment title
     */
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    /**
     * Return single fragment from list of fragments
     *
     * @param position <code>int</code> type value of position in list
     * @return <code>Fragment</code> type object
     */
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /**
     * Total count of fragmentList
     *
     * @return <code>int</code> type value of fragmentList size
     */
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * Title of fragment in given position
     *
     * @param position <code>int</code> type value of fragment title
     * @return <code>String</code> type value of fragment title
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
