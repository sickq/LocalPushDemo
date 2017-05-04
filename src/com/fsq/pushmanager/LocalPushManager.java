package com.fsq.pushmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fsq.utils.LocalPushMessage;
import com.fsq.utils.NotificationService;
import com.fsq.utils.SharedPreferencesManager;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocalPushManager {
	
	private LocalPushManager() {
	}
	
	private static LocalPushManager instance;
	
	public static synchronized LocalPushManager getInstance() {
		if(instance == null)
			instance = new LocalPushManager();
		return instance;
	}
	
	private static List<LocalPushMessage> notificationList = new ArrayList<LocalPushMessage>();
	
	private Context mContext;
	
	public void addLocalNotification(Context cxt, LocalPushMessage msg) {
		if(mContext == null)
			mContext = cxt;
		
		boolean isFind = false;
		for(int i = 0; i < notificationList.size(); i++){
			if(notificationList.get(i).getContent().equals(msg.getContent())) {
				isFind = true;
				break;
			}
		}
		
		if(!isFind) {
			msg.setIndex(getLocalNotificationIndexShouldBe());
			notificationList.add(msg);
			
			saveNotificationList(cxt);
		}
	}
	/*
	public void addLocalNotificationAlarmManager(Context cxt, LocalPushMessage msg) {
		if(mContext == null)
			mContext = cxt;
		
		boolean isFind = false;
		for(int i = 0; i < notificationList.size(); i++){
			if(notificationList.get(i).getContent().equals(msg.getContent())) {
				isFind = true;
				break;
			}
		}
		
		if(!isFind) {
			msg.setIndex(getLocalNotificationIndexShouldBe());
			notificationList.add(msg);
			Intent intent = new Intent(mContext, LocalPushReceiver.class);
			intent.setAction("VIDEO_TIMER" + msg.getIndex());
			intent.putExtra("index", msg.getIndex());
			intent.putExtra("date", msg.getDate());
			intent.putExtra("hour", msg.getHour());
			intent.putExtra("min", msg.getMin());
			intent.putExtra("title", msg.getTitle());
			intent.putExtra("content", msg.getContent());
			intent.putExtra("small_icon", msg.getSmall_icon());
			intent.putExtra("ring", 1);
			
			PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent, 0);
			AlarmManager am = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
			
			Log.i("TAG", intent.getExtras().toString());
			
			am.set(AlarmManager.RTC_WAKEUP, msg.getPushTimeMillis(), sender);
			
		}
	}
	*/
	
	public void clearLocalNotification(Context context){
		notificationList.clear();
		NotificationService.cancelAllPush();
		saveNotificationList(context);
	}
	
	public List<LocalPushMessage> getLocalNotification(){
		return notificationList;
	}
	
	public void getNotificationListCreate(Context context){
		String jsonString = SharedPreferencesManager.getPrefsStr(context.getApplicationContext(), LocalPushManager.PUSH_MESSAGES_KEY);
        notificationList = convertPushMessageFromJson(jsonString);
	}
	
	public void saveNotificationList(Context context){
        String jsonString = convertPushMessageJson(notificationList);
		SharedPreferencesManager.savePrefs(context, PUSH_MESSAGES_KEY, jsonString);
	}
	
	public int getLocalNotificationIndexShouldBe() {
		if(notificationList != null && notificationList.size() > 0) {
			int count = notificationList.size();
			LocalPushMessage message = notificationList.get(count - 1);
			if(message != null) {
				return message.getIndex() + 1;
			}
		}
		return 1;
	}
	
	public static final String PUSH_MESSAGES_KEY = "PUSH_MESSAGES_KEY";
	
	public static String convertPushMessageJson(List<LocalPushMessage> list){
		JSONArray jsonArray = new JSONArray();
		int count = list.size();
		for(int i = 0; i < count; i++){
			LocalPushMessage msg = list.get(i);
			Log.i("TAG", msg.getContent());
			Log.i("TAG", msg.getHashMap().toString());
			JSONObject jsonItem = new JSONObject(msg.getHashMap());
			jsonArray.put(jsonItem);
		}
		return jsonArray.toString();
	}
	
	public static List<LocalPushMessage> convertPushMessageFromJson(String jsonString){
		List<LocalPushMessage> list = new ArrayList<LocalPushMessage>();
		try{
			JSONArray jsonArray = new JSONArray(jsonString);
			int count = jsonArray.length();
			for(int i = 0 ; i < count; i++){
				JSONObject jsonItem = jsonArray.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				JsonToHashMap(jsonItem, map);
				LocalPushMessage msg = new LocalPushMessage();
				msg.convertFromHashMap(map);
				list.add(msg);
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		return list;
	}
	
	private static void JsonToHashMap(JSONObject jsonItem, HashMap<String, String> rstList) {
        try{
			for (Iterator<String> keyStr = jsonItem.keys(); keyStr.hasNext();){
				String key = keyStr.next().trim();
				String value = jsonItem.getString(key);
				rstList.put(key, value);
			}
        }catch(JSONException e){
        	e.printStackTrace();
        }
    }
}
