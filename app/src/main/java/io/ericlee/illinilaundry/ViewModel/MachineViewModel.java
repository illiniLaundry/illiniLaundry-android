package io.ericlee.illinilaundry.ViewModel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.AlarmClock;
import android.view.View;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import io.ericlee.illinilaundry.Model.Machine;

/**
 * @author dl-eric
 */

public class MachineViewModel {
    private Context context;
    private Machine machine;

    public MachineViewModel(Context context, Machine machine) {
        this.context = context;
        this.machine = machine;
    }

    public String getName() {
        return machine.getLabel();
    }

    public String getStatus() {
        return machine.getStatus();
    }

    public String getDescription() {
        return machine.getDescription();
    }

    public String getTimeRemaining() {
        return machine.getTimeRemaining().equals("0") ? "" : machine.getTimeRemaining();
    }

    public boolean getBusy() {
        return !machine.getStatus().contains("Available");
    }

    public View.OnClickListener setAlarm() {
        if (machine.getStatus().contains("In Use") && !machine.getTimeRemaining().contains("Unknown")) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StringBuilder message = new StringBuilder();
                    message.append("Do you want to create an alarm for ");
                    message.append(getDescription());
                    message.append(" : ");
                    message.append(getName());
                    message.append("?");

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Alarm Creation");
                    builder.setMessage(message);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            launchAlarm();
                        }
                    });
                    builder.setNegativeButton(android.R.string.no, null);

                    builder.show();
                }
            };
        }

        return null;
    }

    private void launchAlarm() {
        GregorianCalendar currentTime = new GregorianCalendar();

        int minutesUntilReady = Integer.parseInt(machine.getTimeRemaining().split(" ")[0]);
        currentTime.setTimeInMillis(currentTime.getTimeInMillis()
                + TimeUnit.MINUTES.toMillis(minutesUntilReady));

        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, getDescription() + " : " + getName());
        i.putExtra(AlarmClock.EXTRA_HOUR, currentTime.get(Calendar.HOUR));
        i.putExtra(AlarmClock.EXTRA_MINUTES, currentTime.get(Calendar.MINUTE));
        context.startActivity(i);
    }
}
