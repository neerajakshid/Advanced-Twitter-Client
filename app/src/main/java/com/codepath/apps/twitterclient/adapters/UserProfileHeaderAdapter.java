package com.codepath.apps.twitterclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.twitterclient.fragments.UserHeaderFragmentOne;
import com.codepath.apps.twitterclient.fragments.UserHeaderFragmentTwo;
import com.codepath.apps.twitterclient.models.User;

public class UserProfileHeaderAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Page1", "Page2" };
    private User user;

    public UserProfileHeaderAdapter(FragmentManager fm, User user) {
        super(fm);
        this.user = user;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return UserHeaderFragmentOne.newInstance(user);
            case 1:
                return UserHeaderFragmentTwo.newInstance(user);
            default:
                return UserHeaderFragmentOne.newInstance(user);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }


}

