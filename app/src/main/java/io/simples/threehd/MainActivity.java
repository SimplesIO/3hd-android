package io.simples.threehd;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        final Button scheduleAlertBtn = (Button) findViewById(R.id.scheduleAlertBtn);
        scheduleAlertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND, 5);
                
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

                Toast.makeText(getApplicationContext(), "Alarme agendado p/ 3horas", Toast.LENGTH_SHORT);
            }
        });

    }
}
