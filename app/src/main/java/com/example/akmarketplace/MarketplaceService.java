package com.example.akmarketplace;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MarketplaceService extends Service {

    private MarketplaceApp app;
    private Timer timer;
    private static final String CHANNEL_ID = "ForeGroundServiceChannel";
    private ArrayList<NotificationAK> notifications;
    private ArrayList<NotificationAK> filteredNotifications;
    private String targetEmail;
    //HashMap<String, NotificationAK> notifMap;


    @Override
    public void onCreate() {
        app = (MarketplaceApp) getApplication();
        targetEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        //notifMap = new HashMap<>();
        notifications = new ArrayList<>();
        filteredNotifications = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Test", "FOREGROUND Service started");

        targetEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        //notifMap = new HashMap<>();
        notifications = new ArrayList<>();
        filteredNotifications = new ArrayList<>();

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, BrowseActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("AK Marketplace App Foreground Service")
                .setContentText("A foreground service is running...")
                .setSmallIcon(R.drawable.ak_marketplace_icon_foreground)
                .setContentIntent(pendingIntent)
                .setOngoing(false) //non-sticky notification
                .build();

        startForeground(1, notification);

        //TS: when the system attemps to re-create the service
        //onStartCommand will be called again (not onCreate)
        //So call the startTimer() here
        startTimer();

        return START_STICKY;
    }

    private void createNotificationChannel()
    {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,"Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d("News reader", "Service bound - not used!");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("News reader", "Service destroyed");

    }

    private void startTimer() {
        TimerTask task = new TimerTask()
        {

            @Override
            public void run() {
                Log.d("Test", "Timer task started");
                notifications = new ArrayList<>();
                filteredNotifications = new ArrayList<>();

                BrowseActivity.db.collection("notifications").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NotificationAK notif = document.toObject(NotificationAK.class);
                                notifications.add(notif);

                            }
                            for (NotificationAK n : notifications) {
                                if (n.getSellerEmail().equals(targetEmail)) {
                                    filteredNotifications.add(n);
                                }
                            }
                            for (NotificationAK nf : filteredNotifications) {
                                String notifText = nf.getBuyerName() + " wants to buy " + nf.getItemName() + "!!";
                                sendNotification(notifText, nf.getItemId());

                                BrowseActivity.db.collection("notifications").document(Long.toString(nf.getItemId())).delete();

                            }
                        }
                    }
                });

                // display notification
                //sendNotification("Select to view updated feed.");


            }
        };

        timer = new Timer(true);
        int delay = 1000 * 5;//1000 * 60 * 60;      // 1 hour
        int interval = 1000 * 5;//1000 * 60 * 60;   // 1 hour
        timer.schedule(task, delay, interval);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void sendNotification(String text, long id )
    {

        // create the intent for the notification
        Intent notificationIntent = new Intent(this, ProfileActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // create the pending intent
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, flags);

        // create the variables for the notification
        int icon = R.drawable.ak_marketplace_icon_foreground;
        CharSequence tickerText = "Buyer!";
        CharSequence contentTitle = getText(R.string.app_name);
        CharSequence contentText = text;


        NotificationChannel notificationChannel =
                new NotificationChannel("Channel_ID", "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);



        // create the notification and set its data
        Notification notification = new NotificationCompat
                .Builder(this, "Channel_ID")
                .setSmallIcon(icon)
                .setTicker(tickerText)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId("Channel_ID")
                .build();

        final int NOTIFICATION_ID = (int) id;
        manager.notify(NOTIFICATION_ID, notification);
    }


}