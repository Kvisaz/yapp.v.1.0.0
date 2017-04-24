package ru.kvisaz.yandextranslate.screens.history;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.kvisaz.yandextranslate.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder implements HistoryAdapter.BookmarkCheckedCallback {

    View itemView;

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

    @BindView(R.id.historyItemMarkImageView)
    ImageView markImageView;


    public HistoryViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void setMarked(boolean marked) {
        markImageView.setVisibility(marked ? View.VISIBLE : View.GONE);
        directionTextView.setVisibility(marked ? View.GONE : View.VISIBLE);
        @ColorRes int bgColorResId = marked ? R.color.colorLightLightGray : R.color.colorTransparent;
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), bgColorResId));
    }

    @Override
    public void setBookmarkChecked(boolean checked) {
        bookmarkCheckBox.setChecked(checked);
    }
}
