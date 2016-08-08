package io.ericlee.illinilaundry.Model;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import io.ericlee.illinilaundry.R;

public class AlarmReceiver extends BroadcastReceiver {
    private TinyDB preferences;
    private ArrayList<Integer> alarmIDs;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wlTag");

        wl.acquire();

        // Do stuff here
        preferences = TinyDB.getInstance(context);
        Bundle bundle = intent.getExtras();
        int alarmID = bundle.getInt("alarmid");
        String dormName = bundle.getString("dormName");
        String machineNumber = bundle.getString("machineNumber");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(dormName + ": " + machineNumber)
                .setContentText("Suh");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(alarmID, mBuilder.build());
        Log.i("Alarms", "Code Run!");

        ArrayList<Integer> alarmIDs = preferences.getListInt("alarmids");
        alarmIDs.remove(alarmIDs.indexOf(alarmID));
        preferences.putListInt("alarmids", alarmIDs);

        wl.release();
    }

    public void startAlarm(Context context, String machineNumber, String dormName) {
        preferences = TinyDB.getInstance(context);
        alarmIDs = preferences.getListInt("alarmids");
        Log.i("Initial Alarm ID", alarmIDs.toString());
        if(alarmIDs.size() >= 9) {
            // Max number of alarms reached
            Toast.makeText(context, "Max number of alarms reached!", Toast.LENGTH_SHORT).show();
            Log.i("Alarms", "Max alarms reached.");
        } else {
            int alarmID = (machineNumber + dormName).hashCode();

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("alarmid", alarmID);    // AlarmID won't be -1, because this code won't
                                                    // run if there is ever a case where alarmID
                                                    // won't get instantiated.
            intent.putExtra("machineNumber", machineNumber);
            intent.putExtra("dormName", dormName);
            PendingIntent pi = PendingIntent.getBroadcast(context, alarmID, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
            Log.i("Alarms", "Sent to broadcast!");

            alarmIDs.add(alarmID);
            preferences.putListInt("alarmids", alarmIDs);
            Log.i("Alarms", alarmIDs.toString());
        }
    }

    public void cancelAlarm(Context context) {
        preferences = TinyDB.getInstance(context);
        alarmIDs = preferences.getListInt("alarmids");

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0); //TODO: specify alarm ID here
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
