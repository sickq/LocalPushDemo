package com.fsq.pushmanager;

import com.fsq.localpushdemo.MainActivity;
import com.fsq.utils.LocalPushMessage;
import com.fsq.utils.NotificationService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class LocalPushReceiver extends BroadcastReceiver {

	@SuppressWarnings("static-access")
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().startsWith("VIDEO_TIMER")){
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
			
			Bundle bundle = intent.getExtras();
			int index = bundle.getInt("index");
			String title = bundle.getString("title");
			String content = bundle.getString("content");
			String date = bundle.getString("date");
			String hour = bundle.getString("hour");
			String min = bundle.getString("min");
			int ring = bundle.getInt("ring");
			int small_icon = bundle.getInt("small_icon");
			LocalPushMessage msg = new LocalPushMessage(index, title, content, date, hour, min, ring, small_icon);
			
			
			Log.i("TAG", bundle.toString());
        	Log.i("TAG", "Really?Alarm");
        	
            Notification localNotification = new Notification();
            
            Notification.Builder builder = new Notification.Builder(context)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(msg.getSmall_icon())
            .setContentIntent(pendingIntent)
            .setContentTitle(msg.getTitle())
            .setContentText(msg.getContent())
            .setAutoCancel(true)
            .setTicker(msg.getContent());
            
            //if(msg.getRing() == 1)
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
            
            localNotification = builder.build();
            
            NotificationManager manager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
            
            manager.notify(msg.getIndex(), localNotification);
			
		}
		
		if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
			Log.e("TAG", "chongqi");
			startService(context);
		}
		
		if(Intent.ACTION_USER_PRESENT.equals(intent.getAction())){
			Log.e("TAG", "chongqipresent");
			startService(context);
		}
		
	}
	
	private void startService(Context context)
	{
		if(!NotificationService.isRunning(context))
        {
            Intent startIntent = new Intent(context, NotificationService.class);
            context.startService(startIntent);
        }
	}

}
