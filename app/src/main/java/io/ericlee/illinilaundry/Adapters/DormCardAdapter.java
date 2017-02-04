package io.ericlee.illinilaundry.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.R;
import io.ericlee.illinilaundry.ViewModel.DormViewModel;

/**
 * @author dl-eric
 */

public class DormCardAdapter extends RecyclerView.Adapter<DormCardAdapter.BindingHolder> {
    private List<Dorm> mDorms;
    private Context mContext;

    public DormCardAdapter(Context context) {
        mContext = context;
        mDorms = new ArrayList<>();
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPostBinding postBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.card_dorms,
                parent,
                false);
        return new BindingHolder(postBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ItemPostBinding postBinding = holder.binding;
        postBinding.setViewModel(new DormViewModel(mContext, mDorms.get(position)));
    }

    @Override
    public int getItemCount() {
        return mDorms.size();
    }

    public void setItems(List<Dorm> dorms) {
        mDorms = dorms;
        notifyDataSetChanged();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemPostBinding binding;

        public BindingHolder(ItemPostBinding binding) {
            super(binding.cardView);
            this.binding = binding;
        }
    }
}
