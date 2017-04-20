package ru.kvisaz.yandextranslate.screens.translator.dict;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.kvisaz.yandextranslate.R;

class DictArticleViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.orderNumberTextView)
    TextView orderNumberTextView;

    @BindView(R.id.synTextView)
    TextView synTextView;

    @BindView(R.id.meanTextView)
    TextView meanTextView;


    public DictArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}