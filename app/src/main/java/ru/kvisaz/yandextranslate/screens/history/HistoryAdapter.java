package ru.kvisaz.yandextranslate.screens.history;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.kvisaz.yandextranslate.R;
import ru.kvisaz.yandextranslate.data.models.Translate;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private static int POSITION_WITH_DELIMITER_VIEW = 3;
    private static String DIRECTION_DELIMITER = " - ";

    private List<Translate> mData;
    private List<Translate> selectedItems;

    public void setInteractionListener(InteractionListener interactionListener) {
        this.mInteractionListener = interactionListener;
    }

    private InteractionListener mInteractionListener;

    public interface InteractionListener {
        void onFavoriteCheck(Translate translate, BookmarkCheckedCallback checkedCallback);
    }

    public interface BookmarkCheckedCallback {
        void setBookmarkChecked(boolean checked);
    }

    public void setData(List<Translate> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public HistoryAdapter() {
        mData = new ArrayList<>();
        selectedItems = new ArrayList<>();
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
                mInteractionListener.onFavoriteCheck(translate, holder);
            }
        });

        // source text
        holder.sourceTextView.setText(translate.getSource());

        // translated text
        holder.translatedTextView.setText(translate.getText());

        // direction
        holder.directionTextView.setText(translate.getFrom() + DIRECTION_DELIMITER + translate.getTo());

        // by default itemView is non-marked
        holder.setMarked(false);

        // selection by long click
        View itemView = holder.itemView;
        itemView.setOnLongClickListener(v -> {
            if (selectedItems.contains(translate)) {
                selectedItems.remove(translate);
                holder.setMarked(false);
            } else {
                selectedItems.add(translate);
                holder.setMarked(true);
            }
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void remove(List<Translate> dataForRemoving) {
        Iterator<Translate> itr = dataForRemoving.iterator();
        while (itr.hasNext()) {
            Translate currentTranslate = itr.next();
            if (dataForRemoving.contains(currentTranslate)) {
                mData.remove(currentTranslate);
            }
        }
        notifyDataSetChanged();
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

    public List<Translate> getSelected() {
        return selectedItems;
    }


    private boolean mustHaveDelimiter(int position) {
        return position != 0 && (position - 1) % POSITION_WITH_DELIMITER_VIEW == 0;
    }
}
