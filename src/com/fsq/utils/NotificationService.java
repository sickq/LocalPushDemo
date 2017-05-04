package com.fsq.utils;

import java.util.Calendar;
import java.util.List;

import com.fsq.localpushdemo.MainActivity;
import com.fsq.pushmanager.LocalPushManager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {

	private static final String TAG = "TAG";
    private static final int    CHECK_TICK = 1*5*1000;
    private static final String SERVICE_NAME = "com.fsq.utils.NotificationService";
     
    private NotificationService m_service = null;
    private static NotificationManager m_notificationMgr = null;
    private NotifyThread m_notifyThread = null;
 
    @Override
    public void onCreate() {
        super.onCreate();
        m_service = this;
        m_notificationMgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(m_notificationMgr == null)
        {
            Log.i(TAG, "NotificationService noticationMgr null");
        }
        
        m_notifyThread = new NotifyThread();
        m_notifyThread.start();
        
        Log.i(TAG, "NotificationService onCreate...");
    }
 
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	
        Log.i(TAG, "NotificationService onStartCommand...");

        LocalPushManager.getInstance().getNotificationListCreate(getApplicationContext());
        
        return super.onStartCommand(intent, flags, startId);
    }
 
    @Override
    public void onDestroy() {
    	
        Log.i(TAG, "NotificationService onDestroy...");
         
        if(m_notifyThread != null)
        {
            m_notifyThread.stopThread();
        }
        
        super.onDestroy();
    }
 
	public void notify(int notifyId, LocalPushMessage msg)
    {    
        if(m_notificationMgr != null)
        {
        	Log.i(TAG, "Really?");
        	Intent intent = new Intent(this, MainActivity.class);
        	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        	
            Notification localNotification = new Notification();
            
            Notification.Builder builder = new Notification.Builder(getBaseContext())
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(msg.getSmall_icon())
            .setContentIntent(pendingIntent)
            .setContentTitle(msg.getTitle())
            .setContentText(msg.getContent())
            .setAutoCancel(true)
            .setTicker(msg.getContent());
            
            if(msg.getRing() == 1)
            	builder.setDefaults(Notification.DEFAULT_VIBRATE);
            
            localNotification = builder.build();
            
            m_notificationMgr.notify(notifyId, localNotification);
        }
    }
     
    public static boolean isRunning(Context context) 
    {
        ActivityManager activityMgr = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        if(activityMgr != null)
        {
            List<RunningServiceInfo> serviceList = activityMgr.getRunningServices(50);
             
            if(serviceList.isEmpty())
            {
                return false;
            }
 
            for (int i = 0, n = serviceList.size(); i < n; ++i) 
            {
                if(serviceList.get(i).service.getClassName().toString().equals(SERVICE_NAME))
                {
                    return true;
                }
            }
        }
         
        return false;
    }
    
    public static void cancelAllPush(){
    	if(m_notificationMgr != null) {
    		m_notificationMgr.cancelAll();
    	}
    }
    
    private class NotifyThread extends Thread
    {
        private boolean m_bStop = false;
         
        public synchronized void stopThread()
        {
        	Log.i(TAG, "Notification stopThread");
            m_bStop = true;
        }
         
        @Override
        public void run() 
        {
            Log.i(TAG, "NotifyThread run...");

            while(!m_bStop)
            {
                checkNotify();
                 
                try
                {
                    sleep(CHECK_TICK);
                }
                catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
            }
            
            Log.i(TAG, "NotifyThread stop...");
        }
         
        public void checkNotify()
        {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);
            
            Log.i(TAG, "year:" + year + " month:" + month + " day:" + day);
            Log.i(TAG, "hour:" + hour + " m:" + minute + " s:" + second);
            
            List<LocalPushMessage> list = LocalPushManager.getInstance().getLocalNotification();
            int count = list.size();
            Log.i(TAG, "count:" + count);
            boolean isNotify = false;
            for(int i = count - 1; i >= 0; i--) {
            	LocalPushMessage message = list.get(i);
            	Log.i("TAG", message.getHashMap().toString());
            	long timeStamp = message.getPushTimeMillis();
            	long systemTimeStamp = System.currentTimeMillis();
            	if(timeStamp <= systemTimeStamp) {
            		m_service.notify(message.getIndex(), message);
            		list.remove(message);
            		isNotify = true;
            	}
            }
            
            if(isNotify){
            	LocalPushManager.getInstance().saveNotificationList(getApplicationContext());
            }
        }
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
