package com.cvirn.mobileapp2.survey.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cvirn.mobileapp2.survey.messages.MessageFragment;


/**
 * Created by janko on 1/11/14.
 */
public class TabsAccountPageAdapter extends FragmentPagerAdapter {



    public TabsAccountPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {

            case 0:

               return new MessageFragment();


        }

        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
