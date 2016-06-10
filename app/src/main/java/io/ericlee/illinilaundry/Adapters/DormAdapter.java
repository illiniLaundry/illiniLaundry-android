package io.ericlee.illinilaundry.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.ericlee.illinilaundry.Model.Machine;
import io.ericlee.illinilaundry.R;

/**
 * Created by Eric on 6/9/2016.
 */
public class DormAdapter extends RecyclerView.Adapter<DormAdapter.ViewHolder> {
    private ArrayList<Machine> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView machineNumber;
        public TextView machineType;
        public TextView machineAvailable;

        public ViewHolder(View v) {
            super(v);
            machineNumber = (TextView) v.findViewById(R.id.textMachineNumber);
            machineType = (TextView) v.findViewById(R.id.textMachineType);
            machineAvailable = (TextView) v.findViewById(R.id.textMachineStatus);
        }
    }

    public DormAdapter(ArrayList<Machine> dataset) {
        mDataset = dataset;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.machineAvailable.setText(mDataset.get(position).getMachineStatus());
        holder.machineType.setText(mDataset.get(position).getMachineType());
        holder.machineNumber.setText(mDataset.get(position).getMachineNumber() + "");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.machine_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
