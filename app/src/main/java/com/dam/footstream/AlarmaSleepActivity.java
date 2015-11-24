package com.dam.footstream;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dam.receivers.AlarmReceiver;

import org.w3c.dom.Text;

import java.util.Calendar;

public class AlarmaSleepActivity extends AppCompatActivity {


    private PendingIntent pendingIntent;
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
                    showTimePickerDialog(alarmBox, alarmClock);
                } else {
                    cancel();
                }
            }
        });

        sleepBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    showTimePickerDialog(sleepBox, sleepClock);
            }
        });
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void startAt(int hour, int min) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 20;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);

        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, pendingIntent);
    }

    private class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private CheckBox box;
        private TextView clock;

        public TimePickerFragment(CheckBox box, TextView clock) {
            this.box = box;
            this.clock = clock;
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
            startAt(hourOfDay, minute);
            setAlarm();
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            box.setChecked(false);
            cancel();
        }
    }

    private void setAlarm() {

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(AlarmaSleepActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(AlarmaSleepActivity.this, 0, alarmIntent, 0);

    }

    void showTimePickerDialog(CheckBox box, TextView clock) {
        DialogFragment newFragment = new TimePickerFragment(box, clock);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

}