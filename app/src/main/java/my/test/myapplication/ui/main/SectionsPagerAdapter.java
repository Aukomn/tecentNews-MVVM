package my.test.myapplication.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import my.test.myapplication.R;
import my.test.myapplication.constant.BaseConstant;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1,R.string.tab_text_8, R.string.tab_text_2,
            R.string.tab_text_3,R.string.tab_text_4,R.string.tab_text_5,R.string.tab_text_6,R.string.tab_text_7};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        FragmentTransaction ft=fm.beginTransaction();
}

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        if(position==0)
            return NewFragment.newInstance(1, BaseConstant.getRecommend());
        else if(position==1)
            return VideoFragment.newInstance(1);
        else    if(position==2)
            return NewFragment1.newInstance(1, BaseConstant.getWorldParams());
        else    if(position==3)
            return NewFragment2.newInstance(1, BaseConstant.getFinanceParams());
        else    if(position==4)
            return NewFragment3.newInstance(1, BaseConstant.getEntParams());
        else    if(position==5)
            return NewFragment4.newInstance(1, BaseConstant.getMiliteParams());
        else    if(position==6)
            return NewFragment5.newInstance(1, BaseConstant.getHistory());
        else
            return NewFragment6.newInstance(1, BaseConstant.getCul());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}