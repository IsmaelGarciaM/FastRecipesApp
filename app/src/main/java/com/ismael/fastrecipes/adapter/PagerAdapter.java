package com.ismael.fastrecipes.adapter;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ismael.fastrecipes.MyCommentsFragment;
import com.ismael.fastrecipes.MyRecipesFragment;
import com.ismael.fastrecipes.SearchByCategoriesFragment;
import com.ismael.fastrecipes.SearchByIngredientFragment;
import com.ismael.fastrecipes.SearchRecipeFragment;

/**
 * Created by Ismael on 21/01/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    MyRecipesFragment tabFragment1;
    MyCommentsFragment tabFragment2;


    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tabFragment1 = MyRecipesFragment.getInstance(null);
                return tabFragment1;
            case 1:
                tabFragment2 = MyCommentsFragment.getInstance(null);
                return tabFragment2;
            default:
                tabFragment1 = MyRecipesFragment.getInstance(null);
                return tabFragment1;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
