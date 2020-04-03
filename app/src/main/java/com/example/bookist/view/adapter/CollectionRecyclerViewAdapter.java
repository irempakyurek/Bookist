package com.example.bookist.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.bookist.R;
import com.example.bookist.databinding.GridItemCollectionBinding;
import com.example.bookist.model.pojo.Book;
import com.example.bookist.model.realm.po.RealmBook;
import com.example.bookist.model.realm.util.RealmUtil;
import com.example.bookist.view.listener.ClickListener;
import com.example.bookist.view.listener.LongClickListener;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class CollectionRecyclerViewAdapter extends RecyclerView.Adapter<CollectionRecyclerViewAdapter.ViewHolder> {

    private RealmResults<RealmBook> realmResults;
    private LongClickListener longClickListener;
    private ClickListener clickListener;

    public CollectionRecyclerViewAdapter(RealmResults<RealmBook> realmResults,
                                         LongClickListener longClickListener,
                                         ClickListener clickListener) {
        this.realmResults = realmResults;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int holderPosition) {
        GridItemCollectionBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.grid_item_collection,
                parent,
                false
        );

        ViewHolder holder = new ViewHolder(binding);

        holder.itemView.setOnLongClickListener(view -> {

            if (longClickListener != null) {
                int position = holder.getAdapterPosition();
                longClickListener.onBookLongClick(getRealmBook(position));
            }

            return true;
        });

        holder.itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                int position = holder.getAdapterPosition();
                Book book = RealmUtil.convertPOBookToParcelableBook(getRealmBook(position));
                clickListener.onBookClick(book, false);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RealmBook realmBook = realmResults.get(position);

        if ("".equals(realmBook.getPublishedDate())
                || realmBook.getPublishedDate() == null)
            holder.removeView(2);

        holder.binding.setBook(realmBook);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return realmResults != null ? realmResults.size() : 0;
       // return this.realmResults.size();
    }

    private RealmBook getRealmBook(int position) {
        return this.realmResults.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        GridItemCollectionBinding binding;

        ViewHolder(GridItemCollectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void removeView(int position) {
            binding.rlGrid.removeView(binding.rlGrid.getChildAt(position));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.addRule(RelativeLayout.BELOW, this.binding.tvTitle.getId());
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            this.binding.tvAuthors.setLayoutParams(layoutParams);
        }

    }

}