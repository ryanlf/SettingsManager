package edu.rosehulman.settingsmanager;

import java.io.Serializable;

public class Profile implements Serializable {
	
	private String profileName;
	private int volumeLevel;
	
	public Profile(){
		profileName = null;
		volumeLevel = 0;
	}
	
	public Profile(String profileName,int volumeLevel) {
		this.profileName = profileName;
		this.volumeLevel = volumeLevel;
	}
	
	@Override
	public String toString(){
		return profileName;
	}
	
	public int getVolumeLevel(){
		return volumeLevel;
	}

	public void setVolume(int progress) {
		volumeLevel = progress;
		
	}

	public void setName(String string) {
		profileName = string;
		
	}

}
