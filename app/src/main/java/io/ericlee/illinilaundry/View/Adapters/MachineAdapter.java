package io.ericlee.illinilaundry.View.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import java.util.List;

import io.ericlee.illinilaundry.Model.Machine;
import io.ericlee.illinilaundry.R;
import io.ericlee.illinilaundry.ViewModel.MachineViewModel;
import io.ericlee.illinilaundry.databinding.MachineLayoutBinding;

/**
 * @author dl-eric
 */

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.BindingHolder> {
    private List<Machine> mMachines;
    private Context mContext;

    public MachineAdapter(List<Machine> machines, Context context) {
        mContext = context;
        mMachines = machines;
    }

    @Override
    public MachineAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MachineLayoutBinding postBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.machine_layout,
                parent,
                false);
        return new MachineAdapter.BindingHolder(postBinding);
    }

    @Override
    public void onBindViewHolder(MachineAdapter.BindingHolder holder, int position) {
        MachineLayoutBinding postBinding = holder.binding;
        postBinding.setViewModel(new MachineViewModel(mContext, mMachines.get(position)));
    }

    @Override
    public int getItemCount() {
        return mMachines.size();
    }

    public void setItems(List<Machine> machines) {
        if (machines.size() != mMachines.size()) {
            mMachines = machines;
            notifyDataSetChanged();
        } else {
            mMachines = machines;
            for (int i = 0; i < mMachines.size(); i++) {
                notifyItemChanged(i);
            }
        }
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private MachineLayoutBinding binding;

        public BindingHolder(MachineLayoutBinding binding) {
            super(binding.machineLayout);
            this.binding = binding;
        }
    }
}
