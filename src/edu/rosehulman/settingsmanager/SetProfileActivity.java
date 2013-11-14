package edu.rosehulman.settingsmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SetProfileActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("SET", "Trying to use Prof Activity");
		Intent intent = this.getIntent();
		Profile prof = (Profile) intent.getSerializableExtra(SettingsManagerWidgetProvider.KEY_PROFILE);
		if (prof == null) Log.d("SET", "ACTIVITY GOT PROFILE NULL");
		else {
			prof.setSettings(this);
			Log.d("SET", "PROFILE " + prof.toString());
		}
		this.finish();
	}
}
