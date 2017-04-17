package ru.kvisaz.yandextranslate.screens.tabcontainer;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabFragmentAdapter extends FragmentPagerAdapter {
    private final Context context;

    public TabFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return TabFactory.createTabFragment(position);
    }

    @Override
    public int getCount() {
        return TabEnum.values().length;
    }
}