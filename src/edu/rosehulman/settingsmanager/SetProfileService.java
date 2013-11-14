package edu.rosehulman.settingsmanager;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * This Class is used by the widget to call the profile.setSettings, as its context is not powerful enough to use.
 * @author Logan
 *
 */
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
