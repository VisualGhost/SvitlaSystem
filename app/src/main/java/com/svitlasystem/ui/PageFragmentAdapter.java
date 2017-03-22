package com.svitlasystem.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.svitlasystem.R;
import com.svitlasystem.ui.beers.BeerFragment;

public class PageFragmentAdapter extends FragmentStatePagerAdapter {

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
                return new LocationFragment();
            case 1:
                return new BeerFragment();
            default:
                throw new IllegalArgumentException("Wrong position: " + position);

        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.locations);
            case 1:
                return mContext.getString(R.string.beers);
            default:
                throw new IllegalArgumentException("Wrong position: " + position);
        }
    }

    @Override
    public int getCount() {
        return COUNT_PAGE;
    }
}
