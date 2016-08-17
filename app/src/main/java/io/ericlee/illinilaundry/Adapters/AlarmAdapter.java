package io.ericlee.illinilaundry.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.ericlee.illinilaundry.Model.Alarm;
import io.ericlee.illinilaundry.Model.TinyDB;
import io.ericlee.illinilaundry.R;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    private ArrayList<Alarm> mDataset;
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
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Delete this Alarm")
                        .setMessage("Are you sure you want to delete " + holder.alarmName.getText() + "?");

                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TinyDB preferences = TinyDB.getInstance(v.getContext());
                        ArrayList<Object> oldAlarms = preferences.getListObject("alarms", Alarm.class);
                        ArrayList<Object> newAlarms = new ArrayList<Object>(oldAlarms.size() - 1);

                        for(Object o: oldAlarms) {
                            if(((Alarm)o).getHashcode() != alarm.getHashcode()) {
                                newAlarms.add((Alarm)o);
                            }
                        }

                        preferences.putListObject("alarms", newAlarms);
                        mDataset.remove(alarm);
                        notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton(android.R.string.no, new  DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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
