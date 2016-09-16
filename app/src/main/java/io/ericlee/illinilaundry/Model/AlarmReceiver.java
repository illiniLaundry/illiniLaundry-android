package io.ericlee.illinilaundry.Model;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import io.ericlee.illinilaundry.R;

public class AlarmReceiver extends BroadcastReceiver {
    private TinyDB preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wl_alarm");

        wl.acquire();

        // Do stuff here
        Bundle bundle = intent.getExtras();
        Alarm alarm = (Alarm) bundle.getSerializable("alarm");

        new RefreshData(bundle, alarm, context).execute();

        // Vibrate the phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 300, 200, 300, 200, 300, 200};
        vibrator.vibrate(pattern, -1);

        wl.release();
    }

    public void startAlarm(Context context, Alarm alarm) {
        preferences = TinyDB.getInstance(context);
        ArrayList<Object> temp = preferences.getListObject("alarms", Alarm.class);
        ArrayList<Alarm> alarms = new ArrayList<>();

        for (Object o : temp) {
            alarms.add((Alarm) o);
        }

        if (alarms.size() >= 9) {
            // Max number of alarms reached
            Toast.makeText(context, "Max number of alarms reached!", Toast.LENGTH_SHORT).show();
            Log.i("Alarms", "Max alarms reached.");
        } else {
            int alarmID = alarm.getHashcode();

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("alarm", alarm);
            PendingIntent pi = PendingIntent.getBroadcast(context, alarmID, intent, 0);

            // onReceive will be called every minute until canceled.
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5, pi);
            Log.i("Alarms", "Sent to broadcast!");

            alarms.add(alarm);
            temp.clear();

            for (Alarm a : alarms) {
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
                .setContentText(alarm.getMachine().getMachineType() + " has finished!");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(alarm.getHashcode(), mBuilder.build());
        Log.i("Alarms", "Code Run!");

        ArrayList<Object> temp = preferences.getListObject("alarms", Alarm.class);
        ArrayList<Alarm> alarms = new ArrayList<>();

        for (int i = 0; i < temp.size(); i++) {
            if (((Alarm) temp.get(i)).getHashcode() == alarm.getHashcode()) {
                temp.remove(temp.get(i));
                i--;
            }
        }

        temp.clear();

        for (Alarm a : alarms) {
            temp.add(a);
        }
        preferences.putListObject("alarms", temp);

        cancelAlarm(context, alarm);
    }

    public class RefreshData extends AsyncTask<Void, Void, Void> {
        private Bundle bundle;
        private Alarm alarm;
        private Context context;

        public RefreshData(Bundle bundle, Alarm alarm, Context context) {
            this.bundle = bundle;
            this.alarm = alarm;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (!alarm.getMachine().getMachineStatus().contains("Available")) {
                try {
                    Document illini = Jsoup.connect(alarm.getDorm().getPageUrl()).get();

                    // Parse general information
                    Element table = illini.select("tbody").last();
                    Elements rows = table.select("tr");

                    //TODO: case where the machine number is e.g L3
                    try {
                        int indexForMachineNumber = Integer.parseInt(alarm.getMachine().getMachineNumber());

                        // check for announcement
                        Element firstRow = rows.get(0);
                        Elements firstRowCols = firstRow.select("td");

                        if (firstRowCols.size() == 3) {
                            indexForMachineNumber++;
                        }
                        Log.i("Index for machine num", indexForMachineNumber + "");
                        Element row = rows.get(indexForMachineNumber);
                        Elements cols = row.select("td");

                        String updatedAvailability = cols.get(4).text();
                        Log.i("Updated Availability", updatedAvailability);

                        if(updatedAvailability.contains("Available")) {
                            pushNotification(context, alarm);
                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        // Remove faulty machine that gave us trouble
                        ArrayList<Object> temp = preferences.getListObject("alarms", Alarm.class);
                        ArrayList<Object> newAlarmsAsObjects = new ArrayList<>(temp.size() - 1);

                        for (Object o : temp) {
                            Alarm a = (Alarm) o;
                            if (a.getHashcode() != alarm.getHashcode()) {
                                newAlarmsAsObjects.add(o);
                            }
                        }

                        preferences.putListObject("alarms", newAlarmsAsObjects);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
