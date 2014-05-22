package io.simples.threehd;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

import io.simples.threehd.receiver.AlarmReceiver;


public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_CODE = 0;
    private static final String REMINDER = "REMINDER";
    private static final String IS_SCHEDULED = "IS_SCHEDULED";

    private PendingIntent pendingIntent;

    private SharedPreferences sharedPreferences;
    private Button scheduleAlertBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        scheduleAlertBtn = (Button) findViewById(R.id.scheduleAlertBtn);

        sharedPreferences = getSharedPreferences(REMINDER, MODE_PRIVATE);
        if(sharedPreferences.getBoolean(IS_SCHEDULED, false)) {
            scheduleAlertBtn.setText(R.string.cancel_schedule);
        } else {
            scheduleAlertBtn.setText(R.string.remind_me);
        }

        scheduleAlertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getApplicationContext().getSharedPreferences(REMINDER, MODE_PRIVATE);
                if(sharedPreferences.getBoolean(IS_SCHEDULED, false)) {
                    cancelSchedule();
                } else {
                    setAlarmBroadcast();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void cancelSchedule() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        if(scheduleAlertBtn == null) {
            scheduleAlertBtn = (Button) findViewById(R.id.scheduleAlertBtn);
        }
        scheduleAlertBtn.setText(R.string.remind_me);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(REMINDER, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_SCHEDULED, false);
        editor.commit();

        Toast.makeText(getApplicationContext(), R.string.alarm_canceled, Toast.LENGTH_SHORT).show();
    }

    private void setAlarmBroadcast() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 10);

        if(scheduleAlertBtn == null) {
            scheduleAlertBtn = (Button) findViewById(R.id.scheduleAlertBtn);
        }
        scheduleAlertBtn.setText(R.string.cancel_schedule);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(REMINDER, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_SCHEDULED, true);
        editor.commit();

        Toast.makeText(getApplicationContext(), R.string.alarm_scheduled, Toast.LENGTH_SHORT).show();
    }
}
