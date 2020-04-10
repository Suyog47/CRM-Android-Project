package com.example.demoapp.BackgroundProcessClass;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.demoapp.DailyActivityClasses.NotificationWindow;
import com.example.demoapp.DailyActivityClasses.SetTimer;
import com.example.demoapp.R;
import com.example.demoapp.SqlliteDBClasses.DActivitySqlliteDbService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class BackgroundNotificationService extends BroadcastReceiver {
    String act;
    int timeStatus;
    DActivitySqlliteDbService db;
    SetTimer st = new SetTimer();
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
    NotificationCompat.BigTextStyle bigText;


    //Function to get Activity and Timer-Status from db send Notification accordingly
    @Override
    public void onReceive(Context context, Intent intent) {
        st.placeTimer(context);
        db = new DActivitySqlliteDbService(context);
        String Day = formatter.format(calendar.getTime());
        Cursor dt = db.getActivities(Day);
        while(dt.moveToNext()){
            act = dt.getString(1);
            timeStatus = dt.getInt(3);
        }

        if(timeStatus == 1){
            Log.i(TAG,"Timer is 1");
            showNotification(context, act); }
    }


    //Function to send Notification
    private void showNotification(Context context, String act) {
        Log.i(TAG,"ShowNotification Called");
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
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        mNotificationManager.notify(01, mBuilder.build());
    }
}
