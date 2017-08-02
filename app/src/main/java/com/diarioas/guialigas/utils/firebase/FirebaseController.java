package com.diarioas.guialigas.utils.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.diarioas.guialigas.MainActivity;
import com.diarioas.guialigas.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
import java.util.Random;

public class FirebaseController extends FirebaseMessagingService {

  private static final String TAG = "Firebase_Controller";
  public static  int NOTIFICATION_ID = 1;
  public static final String NOTIFICATION_URL_KEY = "url";

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    Log.d(TAG, "From: " + remoteMessage.getFrom());
    Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

    Map<String, String> map = remoteMessage.getData();
    String url = null;
    if (map != null ) {
      url = map.get(NOTIFICATION_URL_KEY);
    }
    generateNotification(remoteMessage.getNotification().getBody(), url);
  }

  private void generateNotification(String messageBody, String url) {

    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.icon)
        .setContentTitle(getString(R.string.app_name))
        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setContentIntent(getPendingIntent(url));

    NotificationManager notificationManager =
        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    if (NOTIFICATION_ID > 1073741824) {
      NOTIFICATION_ID = 0;
    }
    notificationManager.notify(NOTIFICATION_ID++ , mNotifyBuilder.build());
  }

  public PendingIntent getPendingIntent(String url){
    Intent intent = new Intent(this, MainActivity.class);
    if(url != null){
      intent.putExtra(NOTIFICATION_URL_KEY, url);
    }
    intent.setAction(Long.toString(System.currentTimeMillis()));

    return PendingIntent.getActivity(this, new Random().nextInt(1000), intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

}
