package marrit.marritleenstra_pset6_1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Marrit on 30-11-2017.
 * Broadcast Receiver Class that raises a notification when onReceive.
 */

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("DEBUG", "alarm received");

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Reminder")
                .setContentText("Did you eat vegetarian today?")
                .addAction(R.drawable.ic_yes, "yes", resultPendingIntent)
                .addAction(R.drawable.ic_no, "no", resultPendingIntent)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(resultPendingIntent)
                .setVibrate(new long[]{1000,1000,1000});
        notificationManager.notify(1, mNotificationBuilder.build());





    }
}
