package edu.rosehulman.settingsmanager;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

public class Profile implements Serializable {
	
	private String profileName;
	private int systemVolume;
	
	public Profile(){
		profileName = null;
		systemVolume = 0;
	}
	
	public Profile(String profileName,int systemVolume) {
		this.profileName = profileName;
		this.systemVolume = systemVolume;
	}
	
	@Override
	public String toString(){
		return profileName;
	}
	
	public int getSystemVolume(){
		return systemVolume;
	}

	public void setVolume(int progress) {
		systemVolume = progress;
		
	}

	public void setName(String string) {
		profileName = string;
		
	}
	public void setSettings(Activity parent){
		AudioManager mAudioManager=(AudioManager) parent.getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, systemVolume, 0);
		
		
	}

}
