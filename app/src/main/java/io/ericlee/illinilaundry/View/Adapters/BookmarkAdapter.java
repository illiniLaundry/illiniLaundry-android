package io.ericlee.illinilaundry.View.Adapters;

/**
 * Created by Eric on 11/10/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.ericlee.illinilaundry.View.Activities.DormActivity;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.Model.DormInformation;
import io.ericlee.illinilaundry.R;
import io.ericlee.illinilaundry.ViewModel.DormViewModel;
import io.ericlee.illinilaundry.databinding.CardDormsBinding;
import io.ericlee.illinilaundry.databinding.CardDormsBookmarkBinding;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BindingHolder> {
    private ArrayList<Dorm> mDataset;
    private Context mContext;


    public static class BindingHolder extends RecyclerView.ViewHolder {
        private CardDormsBookmarkBinding binding;

        public BindingHolder(CardDormsBookmarkBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }
    }

    public BookmarkAdapter(Context context, ArrayList<Dorm> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }


    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardDormsBookmarkBinding postBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.card_dorms_bookmark,
                parent,
                false);
        return new BookmarkAdapter.BindingHolder(postBinding);
    }

    private BindingHolder holder;
    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        CardDormsBookmarkBinding postBinding = holder.binding;
        postBinding.setViewModel(new DormViewModel(mContext, mDataset.get(position)));
        postBinding.image.setImageResource(postBinding.getViewModel().getImageResource());

        this.holder = holder;
        setFadeInAnimation(holder.itemView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setItems(ArrayList<Dorm> dorms) {
        mDataset = dorms;
        notifyDataSetChanged();
    }

    public void onItemDismiss(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(String name) {
        for (int i = 0; i < mDataset.size(); i++) {
            if (mDataset.get(i).getName().equals(name)) {
                onItemDismiss(i);

                return;
            }
        }
    }

    private void setFadeInAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }
}