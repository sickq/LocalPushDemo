package com.fsq.localpushdemo;

import com.fsq.pushmanager.LocalPushManager;
import com.fsq.utils.LocalPushMessage;
import com.fsq.utils.NotificationService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService();
    }
    
	@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
	}

	@Override
	protected void onPause()
	{
		AddSomePush();
		
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		LocalPushManager.getInstance().clearLocalNotification(this);
		
		super.onResume();
	}


	@Override
	protected void onStop()
	{
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

    
    public void startService()
    {
    	if(!NotificationService.isRunning(this))
        {
            Intent startIntent = new Intent(this, NotificationService.class);
            startService(startIntent);
        }
    }

    public void AddSomePush()
    {
    	AddLocalNotification("OneTitle", "OneContent", "20170504", "12", "30");
    	
    	AddLocalNotification("TwoTitle", "TwoContent", "20170504", "13", "00");
    	
    	AddLocalNotification("ThreeTitle", "ThreeContent", "20170504", "18", "15");
    	
    	AddLocalNotification("FourTitle", "FourContent", "20170504", "20", "30");
    	
    	AddLocalNotification("FiveTitle", "FiveContent", "20170504", "21", "00");
    }
    
    public void AddLocalNotification(String title, String content, String date, String hour, String min)
    {
    	LocalPushMessage msg = new LocalPushMessage();
    	msg.setTitle(title);
    	msg.setContent(content);

    	msg.setDate(date);
    	msg.setHour(hour);
    	msg.setMin(min);
    	msg.setRing(1);

		msg.setSmall_icon(getResources().getIdentifier("notify_icon", "drawable", getPackageName()));
		
    	LocalPushManager.getInstance().addLocalNotification(MainActivity.this, msg);
		//LocalPushManager.getInstance().addLocalNotificationAlarmManager(MainActivity.this, msg);
    }
}
