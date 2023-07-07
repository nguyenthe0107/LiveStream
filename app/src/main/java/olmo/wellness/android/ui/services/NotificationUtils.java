package olmo.wellness.android.ui.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Pair;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import olmo.wellness.android.R;

public class NotificationUtils {
    private static final String DEFAULT = "Others";
    private static final String ORDERS = "Orders";
    private static final String REWARDS = "Rewards";
    private static final String REFERRALS = "Referrals";

    private static final List<String> listChannelName = Arrays.asList(ORDERS, REWARDS, REFERRALS, DEFAULT);

    private static String TAG = NotificationUtils.class.getSimpleName();

    public synchronized static void initNotificationChannels(Context context) { //for only android O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            removeAllOldChannel(notificationManager);
            for (int i = 0; i < listChannelName.size(); ++i) {
                NotificationChannel channel = createNotificationChannel(context, listChannelName.get(i));
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /*public static void showNotification(Context context, Intent intent, FCMModel fcm) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Generate chanel id
        String channel_name = getChannelByFcmAction(fcm).first;
        String chanel_id = getChannelByFcmAction(fcm).second;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, chanel_id)
                .setContentTitle(fcm.getNotification().getTitle())
                .setContentText(fcm.getNotification().getBody())
                .setSound(sound)
                //Vibration
                //The first index of array is number of milliseconds to wait before turning the vibrator on
                // the second index is number of millisecond to vibrate before turning it off.
                //  index one by one it's follow meaning like this delay, vibrate, sleep, vibrate }
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            builder.setSmallIcon(R.drawable.ic_logo)
                    .setColor(context.getResources().getColor(R.color.color_main));
        }

        builder.setContentIntent(resultPendingIntent);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(fcm.getNotification().getBody()));

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Custom sound for android O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null) {
                NotificationChannel channel = notificationManager.getNotificationChannel(getChannelIdByName(channel_name));
                if (channel != null) {
                    builder.setChannelId(channel.getId());
                    notification = builder.build();
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    notificationManager.createNotificationChannel(channel);
                }
            }
        }

        // Fake ID to enable multiple notification
        int nid = getNotificationId(fcm);
        notificationManager.notify(nid, notification);
    }*/

    private static void notifyNotification(Context context, NotificationCompat.Builder builder) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                (Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Fake ID to enable multiple notification
        int nid = new Random(System.currentTimeMillis()).nextInt(1000);
        notificationManager.notify(nid, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound(Context context) {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo
                        .IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                (Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private synchronized static void removeAllOldChannel(NotificationManager notificationManager) {
        List<NotificationChannel> listChannel = notificationManager.getNotificationChannels();

        if (listChannel != null) {
            for (int channelIdx = 0; channelIdx < listChannel.size(); ++channelIdx) {
                notificationManager.deleteNotificationChannel(listChannel.get(channelIdx).getId());
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private static NotificationChannel createNotificationChannel(Context context, String channelName) {
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName());
        String channelId = getChannelIdByName(channelName);

        NotificationChannel channel = new NotificationChannel(channelId, channelName,
                NotificationManager.IMPORTANCE_HIGH);

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setSound(sound, attributes);

        return channel;
    }

    private static String getChannelIdByName(String channelName) {
        if (!listChannelName.contains(channelName)) {
            return listChannelName.indexOf(DEFAULT) + DEFAULT;
        }
        return listChannelName.indexOf(channelName) + channelName; //add index + channel_id for sorting on system
    }
}
