package ru.kvisaz.yandextranslate.screens.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.kvisaz.yandextranslate.R;
import ru.kvisaz.yandextranslate.data.models.Translate;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private static int POSITION_WITH_DELIMITER_VIEW = 3;
    private static String DIRECTION_DELIMITER = " - ";

    private List<Translate> mData;

    public void setInteractionListener(InteractionListener interactionListener) {
        this.mInteractionListener = interactionListener;
    }

    private InteractionListener mInteractionListener;

    public interface InteractionListener {
        void onFavoriteCheck(Translate translate);
    }

    public void setData(List<Translate> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public HistoryAdapter() {
        mData = new ArrayList<>();
    }


    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_history_item, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        Translate translate = mData.get(position);

        //  delimiter
        holder.delimiterView.setVisibility(mustHaveDelimiter(position) ? View.VISIBLE : View.INVISIBLE);

        // check bookmark
        holder.bookmarkCheckBox.setChecked(translate.isFavorite());
        holder.bookmarkCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) { // проверяем, что это пользователь
                translate.setFavorite(isChecked);
                mInteractionListener.onFavoriteCheck(translate);
            }
        });

        // source text
        holder.sourceTextView.setText(translate.getSource());

        // translated text
        holder.translatedTextView.setText(translate.getText());

        // direction
        holder.directionTextView.setText(translate.getFrom() + DIRECTION_DELIMITER + translate.getTo());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void remove(Translate translate) {
        int position = mData.indexOf(translate);
        if (position >= 0) {
            removeAt(position);
        }
    }

    public void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }

    private boolean mustHaveDelimiter(int position) {
        return position != 0 && (position - 1) % POSITION_WITH_DELIMITER_VIEW == 0;
    }
}
