package ru.kvisaz.yandextranslate.screens.history;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.kvisaz.yandextranslate.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.historyItemBookmarkCheckBox)
    CheckBox bookmarkCheckBox;

    @BindView(R.id.historyItemTranslateTextView)
    TextView translatedTextView;

    @BindView(R.id.historyItemSourceTextView)
    TextView sourceTextView;

    @BindView(R.id.historyItemDelimiterView)
    View delimiterView;

    @BindView(R.id.historyItemDirectionTextView)
    TextView directionTextView;


    public HistoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
