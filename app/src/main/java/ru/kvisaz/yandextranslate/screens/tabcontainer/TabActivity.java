package ru.kvisaz.yandextranslate.screens.tabcontainer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.arellomobile.mvp.MvpAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.R;

public class TabActivity extends MvpAppCompatActivity {

    private final static int DEFAULT_SELECTED_PAGE = 0;
    private final static String CURRENT_PAGE_BUNDLE_TAG = "CurrentPage";


    @BindView(R.id.mainViewPager)
    protected ViewPager viewPager;

    @BindView(R.id.mainTabLayout)
    protected TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        ButterKnife.bind(this);

        initTabs();
        setupTabsListener();
        setupTabIcons();
        selectTab(DEFAULT_SELECTED_PAGE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_PAGE_BUNDLE_TAG, viewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int currentPage = savedInstanceState.getInt(CURRENT_PAGE_BUNDLE_TAG);
        selectTab(currentPage);
    }

    private void initTabs() {
        FragmentPagerAdapter fragmentPagerAdapter = new TabFragmentAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void selectTab(int position) {
        viewPager.setCurrentItem(position);
        makeIconAccented(tabLayout.getTabAt(position));
    }

    private void setupTabIcons() {
        for (TabEnum tabDefinition : TabEnum.values()) {
            int position = tabDefinition.ordinal();
            TabLayout.Tab currentTab = tabLayout.getTabAt(position);
            currentTab.setIcon(tabDefinition.iconResId);
            makeIconNonAccented(currentTab);
        }
    }

    private void setupTabsListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                makeIconNonAccented(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void makeIconAccented(TabLayout.Tab tab) {
        tab.getIcon().setAlpha(Constants.ALPHA_NON_TRANSPARENT);
    }

    private void makeIconNonAccented(TabLayout.Tab tab) {
        tab.getIcon().setAlpha(Constants.ALPHA_HALF_TRANSPARENT);
    }
}