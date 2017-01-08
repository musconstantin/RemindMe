package com.example.marius.remindme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

/**
 * Created by marius on 08.01.2017.
 */

public class NotificationReceiver extends BroadcastReceiver{
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int eventId = Integer.parseInt(intent.getExtras().get("id").toString());
        Log.e("test", "" + eventId);

        DBHelper db = new DBHelper(context);
        Cursor cursor = db.getRow(eventId);
        cursor.moveToFirst();
        String contentTitle = cursor.getString(1);
        String contentText = cursor.getString(2);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_event_black_48dp)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setDefaults(-1);
        Intent resultIntent = new Intent(context, EventAdd.class);
        resultIntent.putExtra("currentEventId", eventId);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, eventId, resultIntent, 0);
        notificationBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(eventId, notificationBuilder.build());
    }
}
