package ru.cppinfo.igo.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ru.cppinfo.igo.InterestActivity;
import ru.cppinfo.igo.R;

public class PushService extends FirebaseMessagingService {

    public PushService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification("Вам кто то ответил");
    }

    public void sendNotification(String messageBody) {
        Bundle args = new Bundle();
        args.putInt("key", 2);
        Intent intent = new Intent(this, InterestActivity.class);
        intent.putExtra("key", 2);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String channelId = "default_channel_id";
        String channelDescription = "Default Channel";

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelId, channelDescription, importance);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationBuilder.setSmallIcon(R.drawable.ic_notification_24dp);
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher_round));
            notificationBuilder.setContentTitle(this.getString(R.string.new_message));
            notificationBuilder.setContentText(messageBody);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
            notificationBuilder.setChannelId(channelId);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_notification_24dp);
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher_round));
            notificationBuilder.setContentTitle(this.getString(R.string.new_message));
            notificationBuilder.setContentText(messageBody);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
            notificationBuilder.setChannelId("chanelrand");
        }


        notificationManager.notify(0, notificationBuilder.build());
        Intent intent1 = new Intent();
        intent1.setAction("ru.alexanderklimov.action.CAT");
        intent1.putExtra("ru.alexanderklimov.broadcast.Message", "Срочно пришлите кота!");
        intent1.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent1);
    }
}
