package edu.rosehulman.settingsmanager;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SetProfileService extends IntentService {
	public SetProfileService() {
		super("SetProfileService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("SET","Service working");
		Profile prof = (Profile) intent.getSerializableExtra(SettingsManagerWidgetProvider.KEY_PROFILE);
		prof.setSettings(this);
		
	}
}
