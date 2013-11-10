package edu.rosehulman.settingsmanager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class SettingsManagerWidgetService extends RemoteViewsService {
	public static String EXTRA_WORD = "EXTRA_WORD";

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		Log.d("SET","RemoteViewsService Called");
		return new SettingsManagerRemoteViewsFactory(
				this.getApplicationContext(), intent);
	}

}

class SettingsManagerRemoteViewsFactory implements
		RemoteViewsService.RemoteViewsFactory {

	private Context mContext;
	private int mAppWidgetId;
	private ArrayList<Profile> mProfileData;
	

	public SettingsManagerRemoteViewsFactory(Context applicationContext,
			Intent intent) {
		Log.d("SET","RemoteViewsServiceFactory constructor Called");
		mContext = applicationContext;
		mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
		Log.d("Settings","In Remote Views Factory");
		try {
			FileInputStream fileRead = mContext.openFileInput(mContext
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
		/*mProfileData = new ArrayList<Profile>();
		for (int k = 1; k < 20; k++){
		Profile profile = new Profile();
		profile.setName("Profile " + k);
		mProfileData.add(profile);
		}*/
	}

	@Override
	public int getCount() {
		Log.d("SET","RemoteViewsServiceFactory getCount Called");

		return mProfileData.size();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		Log.d("SET","RemoteViewsServiceFactory getItmeId Called");
		return position;
	}

	@Override
	public RemoteViews getLoadingView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		updateProfiles();
		Log.d("SET", "Updating Remote view at " + position);
		// Construct a remote views item based on the app widget item XML file, 
	    // and set the text based on the position.
	    RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
	    rv.setTextViewText(R.id.widget_text_row, mProfileData.get(position).toString());
	    
	    Intent i = new Intent();
	    Bundle extras = new Bundle();
	    
	    extras.putString(SettingsManagerWidgetService.EXTRA_WORD, mProfileData.get(position).toString());
	    extras.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
	    i.putExtras(extras);
	    rv.setOnClickFillInIntent(android.R.id.text1, i);
	    // Return the remote views object.
	    return rv;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onCreate() {

	}

	@Override
	public void onDataSetChanged() {
		Log.d("SET", "Data Change");

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

	}
	
	private void updateProfiles(){
		try {
			FileInputStream fileRead = mContext.openFileInput(mContext
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
	}
	

}
