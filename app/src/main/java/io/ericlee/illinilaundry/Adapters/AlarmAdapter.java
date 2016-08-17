package io.ericlee.illinilaundry.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.ericlee.illinilaundry.Model.Alarm;
import io.ericlee.illinilaundry.Model.MachineParser;
import io.ericlee.illinilaundry.R;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    private ArrayList<Alarm> mDataset;
    private AlarmAdapter alarmAdapter = this;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView alarmName;
        private TextView alarmTimeRemaining;

        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.alarmImage);
            alarmName = (TextView) v.findViewById(R.id.alarmName);
            alarmTimeRemaining = (TextView) v.findViewById(R.id.alarmTimeRemaining);
        }
    }

    public AlarmAdapter(ArrayList<Alarm> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_alarms, parent, false);
        context = parent.getContext();
        return new AlarmAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Alarm alarm = mDataset.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MachineParser machineParser = new MachineParser(alarm, alarmAdapter, mDataset, holder, context);
                machineParser.execute();
            }
        });

        if(alarm.getMachine().getMachineType().contains("Washer")) {
            holder.image.setImageResource(R.drawable.bigwasher);
        } else {
            holder.image.setImageResource(R.drawable.bigdryer);
        }

        holder.alarmName.setText(alarm.getAlarmName());
        holder.alarmTimeRemaining.setText(alarm.getMachine().getMachineTimeRemaining());
    }

    public void onItemDismiss(int position) {
        Log.i("item Dismiss", "CALL");
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
