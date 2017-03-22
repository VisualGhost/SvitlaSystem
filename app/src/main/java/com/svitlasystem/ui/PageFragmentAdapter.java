package com.svitlasystem.ui;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.svitlasystem.R;

public class PageFragmentAdapter extends FragmentPagerAdapter {

    private static final int COUNT_PAGE = 2;
    private Context mContext;

    public PageFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BeerFragment();
            case 1:
                return new LocationFragment();
            default:
                throw new IllegalArgumentException("Wrong position: " + position);

        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.beers);
            case 1:
                return mContext.getString(R.string.locations);
            default:
                throw new IllegalArgumentException("Wrong position: " + position);
        }
    }

    @Override
    public int getCount() {
        return COUNT_PAGE;
    }
}
