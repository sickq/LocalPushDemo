package com.fsq.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.util.Log;

public class LocalPushMessage {
	
	private int index;
	private String title;
	private String content;
	private String date;
	private String hour;
	private String min;
	private int ring;
	private int small_icon;
	private HashMap<String, String> hashMap;
	
	public LocalPushMessage(int index, String title, String content, String date, String hour, String min, int ring, int small_icon){
		this.index = index;
		this.title = title;
		this.content = content;
		this.date = date;
		this.hour = hour;
		this.min = min;
		this.ring = ring;
		this.small_icon = small_icon;
	}
	
	public LocalPushMessage(){
		
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	
	public int getRing() {
		return ring;
	}
	public void setRing(int ring) {
		this.ring = ring;
	}
	
	public int getSmall_icon() {
		return small_icon;
	}
	public void setSmall_icon(int small_icon) {
		this.small_icon = small_icon;
	}

	@SuppressLint("SimpleDateFormat") 
	public long getPushTimeMillis() {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStr = this.date + this.hour + this.min + "00";
		Date time;
		try {
			time = localSimpleDateFormat.parse(timeStr);
			//Calendar calendar = Calendar.getInstance();
			//calendar.setTime(time);
			return time.getTime();
		}catch(ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public HashMap<String, String> getHashMap() {
		if(hashMap == null){
			hashMap = new HashMap<String, String>();
			hashMap.put("index", index + "");
			hashMap.put("title", title);
			hashMap.put("content", content);
			hashMap.put("date", date);
			hashMap.put("hour", hour);
			hashMap.put("min", min);
			hashMap.put("ring", ring + "");
			hashMap.put("small_icon", small_icon + "");
		}
		return hashMap;
	}
	
	public void convertFromHashMap(HashMap<String, String> hashMap){
		this.hashMap = hashMap;
		this.index = Integer.parseInt(hashMap.get("index"));
		this.title = hashMap.get("title");
		this.content = hashMap.get("content");
		this.date = hashMap.get("date");
		this.hour = hashMap.get("hour");
		this.min = hashMap.get("min");
		this.ring = Integer.parseInt(hashMap.get("ring"));
		this.small_icon = Integer.parseInt(hashMap.get("small_icon"));
	}
}
