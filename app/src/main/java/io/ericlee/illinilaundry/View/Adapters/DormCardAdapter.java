package io.ericlee.illinilaundry.View.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import java.util.List;

import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.R;
import io.ericlee.illinilaundry.ViewModel.DormViewModel;
import io.ericlee.illinilaundry.databinding.CardDormsBinding;

/**
 * @author dl-eric
 */

public class DormCardAdapter extends RecyclerView.Adapter<DormCardAdapter.BindingHolder> {
    private List<Dorm> mDorms;
    private Context mContext;

    public DormCardAdapter(List<Dorm> dorms, Context context) {
        mContext = context;
        mDorms = dorms;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardDormsBinding postBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.card_dorms,
                parent,
                false);
        return new BindingHolder(postBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        CardDormsBinding postBinding = holder.binding;
        postBinding.setViewModel(new DormViewModel(mContext, mDorms.get(position)));
        postBinding.image.setImageResource(postBinding.getViewModel().getImageResource());
    }

    @Override
    public int getItemCount() {
        return mDorms.size();
    }

    public void setItems(List<Dorm> dorms) {
        if (mDorms.size() != dorms.size()) {
            mDorms = dorms;
            notifyDataSetChanged();
        } else {
            for (int i = 0; i < mDorms.size(); i++) {
                if (!mDorms.get(i).equals(dorms.get(i))) {
                    mDorms.set(i, dorms.get(i));
                    notifyItemChanged(i);
                }
            }
        }
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private CardDormsBinding binding;

        public BindingHolder(CardDormsBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }
    }
}
