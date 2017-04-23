package ru.kvisaz.yandextranslate.screens.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ru.kvisaz.yandextranslate.R;
import ru.kvisaz.yandextranslate.common.CommonTabFragment;
import ru.kvisaz.yandextranslate.data.models.Translate;

public class HistoryFraqment extends CommonTabFragment implements IHistoryView, HistoryAdapter.InteractionListener {

    @InjectPresenter
    HistoryPresenter presenter;

    @BindView(R.id.historyRecyclerView)
    RecyclerView historyRecyclerView;

    @BindView(R.id.historyModeTabLayout)
    TabLayout historyModeTabLayout;

    @BindView(R.id.historyDeleteButton)
    ImageButton historyDeleteButton;

    @BindView(R.id.historySearchEditText)
    EditText historySearchEditText;

    @BindView(R.id.historySearchButton)
    ImageButton historySearchButton;

    private HistoryAdapter historyAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_history;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        setupTabsListener();
    }

    @Override
    protected void onVisible() {
        presenter.onPageVisible();
    }

    @Override
    public void showHistory(List<Translate> translates) {
        historyAdapter.setData(translates);
    }

    @Override
    public void setSearchFieldHint(String hint) {

    }

    @Override
    public void onFavoriteCheck(Translate translate) {
        presenter.onFavoriteCheck(translate);
    }

    @Override
    public void hideTranslate(Translate translate) {
        historyAdapter.remove(translate);
    }

    @Override
    public void hideTranslate(List<Translate> translates) {
        historyAdapter.remove(translates);
    }

    @OnClick(R.id.historyDeleteButton)
    public void onDeleteClick() {
        presenter.onDeleteButtonClick(historyAdapter.getSelected());
    }

    private void initRecyclerView() {
        historyAdapter = new HistoryAdapter();
        historyAdapter.setInteractionListener(this);
        historyRecyclerView.setAdapter(historyAdapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupTabsListener() {
        historyModeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                HistoryTabMode mode = HistoryTabMode.values()[tab.getPosition()];
                presenter.onHistoryModeSelect(mode);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}