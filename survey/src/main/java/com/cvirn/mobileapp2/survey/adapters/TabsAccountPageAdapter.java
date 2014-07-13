package com.cvirn.mobileapp2.survey.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cvirn.mobileapp2.survey.messages.MessageFragment;
import com.cvirn.mobileapp2.survey.poi.PoiFragment;
import com.cvirn.mobileapp2.survey.search.SearchFragment;
import com.cvirn.mobileapp2.survey.tasks.TasksFragment;


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

                return new PoiFragment();

            case 1:

               return new MessageFragment();


            case 2:

                return new TasksFragment();
            case 3:
                return new SearchFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
