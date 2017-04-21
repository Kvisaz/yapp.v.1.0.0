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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ru.kvisaz.yandextranslate.R;
import ru.kvisaz.yandextranslate.common.CommonTabFragment;
import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;
import ru.kvisaz.yandextranslate.data.models.SentencePair;

public class HistoryFraqment extends CommonTabFragment implements IHistoryView {

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
    private List<HistoryEntity> historyEntities;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_history;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    @Override
    protected void onVisible() {
        presenter.onPageVisible();
    }

    @Override
    public void showHistory(List<HistoryEntity> entities) {
        historyEntities.clear();
        historyEntities.addAll(entities);
        historyAdapter.notifyDataSetChanged();
    }

    @Override
    public void addPage(List<SentencePair> sentencePairs) {

    }

    @Override
    public void setSearchFieldHint(String hint) {

    }

    private void initRecyclerView() {
        historyEntities = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyEntities);
        historyRecyclerView.setAdapter(historyAdapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}