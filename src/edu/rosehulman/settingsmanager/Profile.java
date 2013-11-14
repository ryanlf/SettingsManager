package edu.rosehulman.settingsmanager;

import java.io.Serializable;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;

public class Profile implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7799129024099471190L;
	private String profileName;
	private int systemVolume;
	private int ringerVolume;
	private int notificationVolume;
	private String notificationRingtone;
	private String notificationRingtoneName;
	private String ringerRingtone;
	private String ringerRingtoneName;
	private String alarmRingtone;
	private String alarmRingtoneName;
	private int alarmVolume;
	private int brightness;
	
	public Profile(){
		profileName = "";
		systemVolume = 0;
		ringerVolume = 0;
		notificationVolume = 0;
		notificationRingtone = "";
		notificationRingtoneName = "";
		ringerRingtone = "";
		ringerRingtoneName = "";
		alarmRingtone = "";
		alarmRingtoneName = "";
		alarmVolume = 0;
		brightness = 0;
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

	public Uri getNotificationRingtone(){	return Uri.parse(notificationRingtone); }
	
	public String getNotificationRingtoneName(){	return notificationRingtoneName; }

	public void setNotificationRingtone(Uri ringtone, Context context) { 
		notificationRingtone = ringtone.toString();
		notificationRingtoneName = RingtoneManager.getRingtone(context, Uri.parse(notificationRingtone)).getTitle(context);
	}

	public Uri getRingerRingtone(){	return Uri.parse(ringerRingtone); }
	
	public String getRingerRingtoneName(){	return ringerRingtoneName; }

	public void setRingerRingtone(Uri ringtone, Context context) { 
		ringerRingtone = ringtone.toString();
		ringerRingtoneName = RingtoneManager.getRingtone(context, Uri.parse(ringerRingtone)).getTitle(context);
	}
	
	public int getAlarmVolume(){ return alarmVolume; }

	public Uri getAlarmRingtone(){	return Uri.parse(alarmRingtone); }
	
	public String getAlarmRingtoneName(){	return alarmRingtoneName; }

	public void setAlarmRingtone(Uri ringtone, Context context) { 
		alarmRingtone = ringtone.toString();
		alarmRingtoneName = RingtoneManager.getRingtone(context, Uri.parse(alarmRingtone)).getTitle(context);
	}
	
	public void setAlarmVolume(int progress) { alarmVolume = progress; }
	
	public int getBrightness() { return brightness; }
	
	public void setBrightness(int progress) { brightness = progress; }

	public void setName(String string) {
		profileName = string;
	}
	public void setSettings(Activity parent){
		AudioManager mAudioManager=(AudioManager) parent.getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, systemVolume, 0);
		mAudioManager.setStreamVolume(AudioManager.STREAM_RING, ringerVolume, 0);
		mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notificationVolume, 0);
		mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, alarmVolume, 0);

		RingtoneManager.setActualDefaultRingtoneUri(parent, RingtoneManager.TYPE_NOTIFICATION, Uri.parse(notificationRingtone));
		RingtoneManager.setActualDefaultRingtoneUri(parent, RingtoneManager.TYPE_RINGTONE, Uri.parse(ringerRingtone));
		RingtoneManager.setActualDefaultRingtoneUri(parent, RingtoneManager.TYPE_ALARM, Uri.parse(alarmRingtone));
		
		Settings.System.putInt(parent.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
	}

	public void setSettings(Service parent) {
		AudioManager mAudioManager=(AudioManager) parent.getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, systemVolume, 0);
		mAudioManager.setStreamVolume(AudioManager.STREAM_RING, ringerVolume, 0);
		mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notificationVolume, 0);
		mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, alarmVolume, 0);

		RingtoneManager.setActualDefaultRingtoneUri(parent, RingtoneManager.TYPE_NOTIFICATION, Uri.parse(notificationRingtone));
		RingtoneManager.setActualDefaultRingtoneUri(parent, RingtoneManager.TYPE_RINGTONE, Uri.parse(ringerRingtone));		
	}
	
	
}
