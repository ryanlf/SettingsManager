package edu.rosehulman.settingsmanager;

public class Profile {
	
	private String profileName;
	private int volumeLevel;
	
	public Profile(){
		profileName = "Add New";
		volumeLevel = -1;
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

}
