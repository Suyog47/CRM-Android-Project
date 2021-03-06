package com.example.demoapp.BackgroundProcessClass;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.demoapp.DailyActivityClasses.NotificationWindow;
import com.example.demoapp.DailyActivityClasses.SetTimer;
import com.example.demoapp.R;
import com.example.demoapp.SqlliteDBClasses.DActivitySqlliteDbService;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class BackgroundNotificationService extends BroadcastReceiver {
    String act;
    int timeStatus;
    SetTimer st = new SetTimer();
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
    NotificationCompat.BigTextStyle bigText;


    //Function to get Activity and Timer-Status from db send Notification accordingly
    @Override
    public void onReceive(Context context, Intent intent) {
        st.placeTimer(context);
        String Day = formatter.format(calendar.getTime());
        Cursor dt = new DActivitySqlliteDbService(context).getActivities(Day);
        while(dt.moveToNext()){
            act = dt.getString(1);
            timeStatus = dt.getInt(3);
        }

        if(timeStatus == 1){
            showNotification(context, act); }
    }


    //Function to send Notification
    private void showNotification(Context context, String act) {
        bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(act);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, NotificationWindow.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)

                        .setSmallIcon(R.drawable.splash2)
                        .setContentTitle("Today's Activity")
                        .setContentText("Expand to See")
                        .setContentIntent(contentIntent)
                        .setStyle(bigText)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        //Code to set Notification Channel if Android Version(Api level) >= Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "01";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "CRM Notification channel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        mNotificationManager.notify(01, mBuilder.build());
    }
}
