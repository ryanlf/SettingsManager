package edu.rosehulman.settingsmanager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


/**
 * Implementation of App Widget functionality.
 */
public class SettingsManagerWidgetProvider extends AppWidgetProvider {

	public static final String KEY_POSITION = "KEY_POSITION";
	public static String KEY_PROFILE = "KEY_PROFILE";
	public static final String ID_FILE = "WidgetIDs_FILE";
	public static final String SET_PROFILE_ACTION = "edu.rosehulman.settingsmanager.SET_PROFILE_ACTION";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// There may be multiple widgets active, so update all of them
		Log.d("SET", "On Update Called");
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			// Set up the intent that starts the StackViewService, which will
			// provide the views for this collection.
			Intent intent = new Intent(context,
					SettingsManagerWidgetService.class);
			// Add the app widget ID to the intent extras.
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetIds[i]);
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			// Instantiate the RemoteViews object for the app widget layout.
			RemoteViews rv = new RemoteViews(context.getPackageName(),
					R.layout.settings_manager_widget);
			// Set up the RemoteViews object to use a RemoteViews adapter.
			// This adapter connects
			// to a RemoteViewsService through the specified intent.
			// This is how you populate the data.
			
			rv.setRemoteAdapter(appWidgetIds[i], R.id.widgit_list_view, intent);

			// The empty view is displayed when the collection has no items.
			// It should be in the same layout used to instantiate the
			// RemoteViews
			// object above.
			rv.setEmptyView(R.id.widgit_list_view, R.id.empty_text_view_widget);
			
			Intent setProfileIntent = new Intent(context,
					SettingsManagerWidgetProvider.class);
			setProfileIntent.setAction(SettingsManagerWidgetProvider.SET_PROFILE_ACTION);
			setProfileIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetIds[i]);
			
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			PendingIntent setProfilePendingIntent = PendingIntent.getBroadcast(
					context, 0, setProfileIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setPendingIntentTemplate(R.id.widgit_list_view,
					setProfilePendingIntent);

			
			appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
			Log.d("SET", "Updated widget "+appWidgetIds[i]);
		}
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}


	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("SET","Recived Something " + intent.getAction().toString());
		if (intent.getAction().equals(SettingsManagerWidgetProvider.SET_PROFILE_ACTION)) {
			Bundle extras = intent.getExtras();
			int position = extras.getInt(SettingsManagerWidgetProvider.KEY_POSITION);
			String test = (String) extras.get("SETString");
			int id = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
			if (position == -1) Log.d("SET","Position Null");
			else {
				ArrayList<Profile> mProfileData = new ArrayList<Profile>();
				try {
					FileInputStream fileRead = context.openFileInput(context
							.getString(R.string.data_file_name));
					ObjectInputStream objectInput = new ObjectInputStream(fileRead);
					mProfileData = (ArrayList<Profile>) objectInput.readObject();
					objectInput.close();
					fileRead.close();
					Log.d("SET", "ReadFile " + mProfileData.size());
				} catch (FileNotFoundException e) {
					mProfileData = new ArrayList<Profile>();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				Intent setProf = new Intent();
				setProf.setClassName("edu.rosehulman.settingsmanager", "edu.rosehulman.settingsmanager.SetProfileService");
				setProf.putExtra(SettingsManagerWidgetProvider.KEY_PROFILE, mProfileData.get(position));
				context.startService(setProf);
			}
		}
		if (intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")){
			Log.d("SET", "Update Broadcast Detected");
		}
		super.onReceive(context, intent);
	}
	
	@Override
	public void onDeleted (Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
    }
}