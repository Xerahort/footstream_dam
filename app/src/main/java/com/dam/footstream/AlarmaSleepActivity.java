package com.dam.footstream;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dam.receivers.AlarmReceiver;
import com.dam.receivers.SleepReceiver;

import java.util.Calendar;

public class AlarmaSleepActivity extends AppCompatActivity {


    private PendingIntent pendingIntentAlarm, pendingIntentSleep;
    private CheckBox alarmBox, sleepBox;
    private TextView alarmClock, sleepClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma_sleep);

        alarmBox = (CheckBox) findViewById(R.id.checkAlarm);
        sleepBox = (CheckBox) findViewById(R.id.checkSleep);
        alarmClock = (TextView) findViewById(R.id.textAlarm);
        sleepClock = (TextView) findViewById(R.id.textSleep);

        alarmBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showTimePickerDialog(alarmBox, alarmClock, false);
                } else {
                    cancelAlarm();
                }
            }
        });

        sleepBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showTimePickerDialog(sleepBox, sleepClock, true);
                } else {
                    cancelSleep();
                }
            }
        });
    }

    private class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private CheckBox box;
        private TextView clock;
        boolean sleep = false;

        public TimePickerFragment(CheckBox box, TextView clock, boolean sleep) {
            this.box = box;
            this.clock = clock;
            this.sleep = sleep;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            clock.setText(hourOfDay + ":" + minute);
            box.setChecked(true);

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if(calSet.compareTo(calNow) <= 0){
                //Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            }

            if (sleep) setSleep(calSet);
            else setAlarm(calSet);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            box.setChecked(false);
            cancelAlarm();
        }
    }


    void showTimePickerDialog(CheckBox box, TextView clock, boolean sleep) {
        DialogFragment newFragment = new TimePickerFragment(box, clock, sleep);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void cancelAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntentAlarm);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void cancelSleep() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntentSleep);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void setAlarm(Calendar calendar) {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        /* Retrieve a PendingIntent that will perform a broadcast */
        //Intent alarmIntent = new Intent(AlarmaSleepActivity.this, AlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(AlarmaSleepActivity.this, 0, alarmIntent, 0);
        //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        pendingIntentAlarm = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentAlarm);
    }

    public void setSleep(Calendar calendar) {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        /* Retrieve a PendingIntent that will perform a broadcast */
        //Intent alarmIntent = new Intent(AlarmaSleepActivity.this, AlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(AlarmaSleepActivity.this, 0, alarmIntent, 0);
        //manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);

        Intent intent = new Intent(getBaseContext(), SleepReceiver.class);
        pendingIntentSleep = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentSleep);
    }

}