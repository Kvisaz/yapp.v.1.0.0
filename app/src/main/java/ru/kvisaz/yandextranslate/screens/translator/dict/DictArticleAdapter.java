package ru.kvisaz.yandextranslate.screens.translator.dict;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.kvisaz.yandextranslate.R;
import ru.kvisaz.yandextranslate.data.models.DictArticle;

public class DictArticleAdapter extends RecyclerView.Adapter<DictArticleViewHolder> {

    private DictArticle dictArticle;

    public DictArticleAdapter(DictArticle dictArticle) {
        this.dictArticle = dictArticle;
    }

    public void setArticle(DictArticle dictArticle) {
        this.dictArticle = dictArticle;
        notifyDataSetChanged();
    }

    @Override
    public DictArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.recycler_dictdef_item, parent, false);
        return new DictArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DictArticleViewHolder holder, int position) {
        holder.orderNumberTextView.setText(""+(position + 1));
        holder.synTextView.setText(dictArticle.synonimStrings.get(position));
        String mean = dictArticle.meanStrings.get(position);
        if(mean.length()==0){
            holder.meanTextView.setVisibility(View.GONE);
        }
        else{
            holder.meanTextView.setVisibility(View.VISIBLE);
            holder.meanTextView.setText("( "+mean + " )");
        }

    }

    @Override
    public int getItemCount() {
        return dictArticle.synonimStrings.size();
    }
}