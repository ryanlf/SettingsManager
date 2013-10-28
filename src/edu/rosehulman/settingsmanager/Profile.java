package edu.rosehulman.settingsmanager;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

public class Profile implements Serializable {
	
	private String profileName;
	private int systemVolume;
	private int ringerVolume;
	private int notificationVolume;
	private String notificationRingtone;
	private int alarmVolume;
	
	public Profile(){
		profileName = null;
		systemVolume = 0;
		ringerVolume = 0;
		notificationVolume = 0;
		notificationRingtone = "";
		alarmVolume = 0;
	}
	
	@Override
	public String toString(){
		return profileName;
	}
	
	public int getSystemVolume(){ return systemVolume; }

	public void setSystemVolume(int progress) {	systemVolume = progress; }
	
	public int getRingerVolume(){ return ringerVolume; }

	public void setRingerVolume(int progress) {	ringerVolume = progress; }
	
	public int getNotificationVolume(){	return notificationVolume; }

	public void setNotificationVolume(int progress) { notificationVolume = progress; }

	public String getNotificationRingtone(){	return notificationRingtone; }

	public void setNotificationRingtone(String ringtone) { notificationRingtone = ringtone; }
	
	public int getAlarmVolume(){ return alarmVolume; }

	public void setAlarmVolume(int progress) { alarmVolume = progress; }

	public void setName(String string) {
		profileName = string;
	}
	public void setSettings(Activity parent){
		AudioManager mAudioManager=(AudioManager) parent.getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, systemVolume, 0);
		mAudioManager.setStreamVolume(AudioManager.STREAM_RING, ringerVolume, 0);
		mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notificationVolume, 0);
		mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, alarmVolume, 0);
		
		
		
	}

}
