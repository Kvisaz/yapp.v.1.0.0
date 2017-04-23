package ru.kvisaz.yandextranslate.screens.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.kvisaz.yandextranslate.R;
import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private static int POSITION_WITH_DELIMITER_VIEW = 3;
    private static String DIRECTION_DELIMITER = " - ";

    private List<HistoryEntity> entities;

    public void setInteractionListener(InteractionListener interactionListener) {
        this.mInteractionListener = interactionListener;
    }

    private InteractionListener mInteractionListener;

    public interface InteractionListener {
        void onFavoriteCheck(HistoryEntity entity);
    }

    public HistoryAdapter() {
        entities = new ArrayList<>();
    }

    public HistoryAdapter(@NonNull List<HistoryEntity> entities) {
        this.entities = entities;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_history_item, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        HistoryEntity entity = entities.get(position);

        //  delimiter
        holder.delimiterView.setVisibility(mustHaveDelimiter(position) ? View.VISIBLE : View.INVISIBLE);

        // check bookmark
        holder.bookmarkCheckBox.setChecked(entity.isFavorite);
        holder.bookmarkCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isPressed()){ // проверяем, что это пользователь
                entity.isFavorite = isChecked;
                mInteractionListener.onFavoriteCheck(entity);
            }
        });

        // source text
        holder.sourceTextView.setText(entity.source);

        // translated text
        holder.translatedTextView.setText(entity.translated);

        // direction
        holder.directionTextView.setText(entity.fromLang + DIRECTION_DELIMITER + entity.toLang);
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    private boolean mustHaveDelimiter(int position) {
        return position != 0 && (position - 1) % POSITION_WITH_DELIMITER_VIEW == 0;
    }
}