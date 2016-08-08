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
    private ArrayList<Alarm> alarms;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wl_alarm");

        wl.acquire();

        // Do stuff here
        Bundle bundle = intent.getExtras();
        Alarm alarm = (Alarm) bundle.getSerializable("alarm");

        //TODO: parse html again so we can get time remaining
        pushNotification(context, alarm);

        wl.release();
    }

    public void startAlarm(Context context, Alarm alarm) {
        preferences = TinyDB.getInstance(context);
        ArrayList<Object> temp = preferences.getListObject("alarms", Alarm.class);
        alarms = new ArrayList<>();

        for(Object o : temp) {
            alarms.add((Alarm) o);
        }

        if(alarms.size() >= 9) {
            // Max number of alarms reached
            Toast.makeText(context, "Max number of alarms reached!", Toast.LENGTH_SHORT).show();
            Log.i("Alarms", "Max alarms reached.");
        } else {
            int alarmID = alarm.getHashcode();

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("alarm", alarm);
            PendingIntent pi = PendingIntent.getBroadcast(context, alarmID, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60, pi);
            Log.i("Alarms", "Sent to broadcast!");

            alarms.add(alarm);
            temp.clear();

            for(Alarm a : alarms) {
                temp.add((Object) a);
            }

            preferences.putListObject("alarms", temp);
        }
    }

    public void cancelAlarm(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarm.getHashcode(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private void pushNotification(Context context, Alarm alarm) {
        preferences = TinyDB.getInstance(context);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.notificationicon)
                .setContentTitle(alarm.getAlarmName())
                .setContentText("Suh");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(alarm.getHashcode(), mBuilder.build());
        Log.i("Alarms", "Code Run!");

        ArrayList<Object> temp = preferences.getListObject("alarms", Alarm.class);
        ArrayList<Alarm> alarms = new ArrayList<>();

        for(int i = 0; i < temp.size(); i++) {
            if(((Alarm)temp.get(i)).getHashcode() == alarm.getHashcode()) {
                temp.remove(temp.get(i));
                i--;
            }
        }

        temp.clear();

        for(Alarm a : alarms) {
            temp.add(a);
        }
        preferences.putListObject("alarms", temp);

        cancelAlarm(context, alarm);
    }
}
