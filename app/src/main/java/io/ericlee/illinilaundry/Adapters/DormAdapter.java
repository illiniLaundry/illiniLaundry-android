package io.ericlee.illinilaundry.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.ericlee.illinilaundry.Model.Alarm;
import io.ericlee.illinilaundry.Model.AlarmReceiver;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.Model.Machine;
import io.ericlee.illinilaundry.Model.TinyDB;
import io.ericlee.illinilaundry.R;

/**
 * Created by Eric on 6/9/2016.
 */
public class DormAdapter extends RecyclerView.Adapter<DormAdapter.ViewHolder> {
    private ArrayList<Machine> mDataset;
    private Dorm dorm;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView machineNumber;
        public TextView machineType;
        public TextView machineAvailable;
        public TextView machineTimeRemaining;
        public LinearLayout layout;

        public ViewHolder(View v) {
            super(v);
            machineNumber = (TextView) v.findViewById(R.id.textMachineNumber);
            machineType = (TextView) v.findViewById(R.id.textMachineType);
            machineAvailable = (TextView) v.findViewById(R.id.textMachineStatus);
            machineTimeRemaining = (TextView) v.findViewById(R.id.textMachineTimeRemaining);
            layout = (LinearLayout) v.findViewById(R.id.machineLayout);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            Log.i("Click", "HIT!:" + machineNumber.getText());

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            if (machineAvailable.getText().equals("Available")) {
//                builder.setTitle("Alarm")
//                        .setMessage("Do you want to create an alarm to let you know when this " +
//                                "machine is no longer available?");
//
//                // Add the buttons
//                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User clicked OK button
//                    }
//                });
//                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
            } else {
                builder.setTitle("Alarm")
                        .setMessage("The machine should be done in about " +
                                machineTimeRemaining.getText() +
                                ". Create an alarm for machine "
                                + machineNumber.getText() + "?");

                // Add the buttons
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        TinyDB preferences = TinyDB.getInstance(v.getContext());

                        ArrayList<Object> temp = preferences.getListObject("alarms", Alarm.class);
                        ArrayList<Alarm> alarms = new ArrayList<>();

                        for(Object o : temp) {
                            alarms.add((Alarm) o);
                        }

                        String str = machineTimeRemaining.getText().toString();
                        // Attempt to extract digits
                        str = str.replaceAll("\\D+", "");

                        try {
                            // Verify that this machine has a numerical time remaining.
                            Integer.parseInt(str);

                            Alarm alarm = new Alarm(new Machine(machineNumber.getText().toString(),
                                    machineType.getText().toString(),
                                    machineAvailable.getText().toString(),
                                    machineTimeRemaining.getText().toString(), dorm));

                            Log.i("Alarm Compare", alarm.getMachine().getMachineNumber());
                            Log.i("Alarm Compare", machineNumber.getText().toString());
                            boolean found = false;

                            for(Alarm a : alarms) {
                                if(a.getHashcode() == alarm.getHashcode()) {
                                    Toast.makeText(v.getContext(), "An alarm for this machine already exists!", Toast.LENGTH_SHORT).show();
                                    found = true;
                                }
                            }

                            if(!found) {
                                AlarmReceiver alarmReceiver = new AlarmReceiver();
                                alarmReceiver.startAlarm(v.getContext(), alarm);
                                Log.i("Alarms", "Attempt to start alarm!");
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();

                            Toast.makeText(v.getContext(), "I'm sorry, Dave, I'm afraid I can't do that.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                // TODO: Temporaryily have this bit here since we don't have the other option
                // TODO: of clicking Available machines
                AlertDialog dialog = builder.create();
                dialog.show();
            }

//            AlertDialog dialog = builder.create();
//            dialog.show();
        }
    }

    public DormAdapter(ArrayList<Machine> dataset) {
        mDataset = dataset;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView machineNumber = (TextView) holder.itemView.findViewById(R.id.textMachineNumber);
        TextView machineStatus = (TextView) holder.itemView.findViewById(R.id.textMachineStatus);
        TextView machineTimeRemaining = (TextView) holder.itemView.findViewById(R.id.textMachineTimeRemaining);
        TextView machineType = (TextView) holder.itemView.findViewById(R.id.textMachineType);

        Context context = holder.itemView.getContext();

        if (mDataset.get(position).getMachineStatus().contains("In Use")) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.softRed));
            machineNumber.setTextColor(ContextCompat.getColor(context, R.color.textColor));
            machineStatus.setTextColor(ContextCompat.getColor(context, R.color.textColor));
            machineTimeRemaining.setTextColor(ContextCompat.getColor(context, R.color.textColor));
            machineType.setTextColor(ContextCompat.getColor(context, R.color.textColor));

        } else if (mDataset.get(position).getMachineStatus().equals("Available")) {
            //holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.softGreen));
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary));
            machineNumber.setTextColor(Color.WHITE);
            machineStatus.setTextColor(Color.GREEN);
            machineTimeRemaining.setTextColor(Color.WHITE);
            machineType.setTextColor(Color.WHITE);
        }

        holder.machineAvailable.setText(mDataset.get(position).getMachineStatus());
        holder.machineType.setText(mDataset.get(position).getMachineType());
        holder.machineNumber.setText(mDataset.get(position).getMachineNumber());
        holder.machineTimeRemaining.setText(mDataset.get(position).getMachineTimeRemaining());

        dorm = mDataset.get(position).getDorm();
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
