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

public class SettingsManagerWidgetService extends RemoteViewsService {


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
	}

	@Override
	public int getCount() {
		Log.d("SET","RemoteViewsServiceFactory getCount Called");

		return mProfileData.size();
	}

	@Override
	public long getItemId(int position) {
		Log.d("SET","RemoteViewsServiceFactory getItmeId Called");
		return position;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext.getApplicationContext());
		Log.d("SET", "Updating Remote view at " + position);
		// Construct a remote views item based on the app widget item XML file, 
	    // and set the text based on the position.
	    RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
	    rv.setTextViewText(R.id.widget_text_row, mProfileData.get(position).toString()); 
	    
	    
	    Bundle extras = new Bundle();
	    //
	    //extras.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
	    extras.putInt(SettingsManagerWidgetProvider.KEY_POSITION,position);
	    extras.putString("SETString", "TEST TEST");
	    //extras.putSerializable(SettingsManagerWidgetProvider.KEY_PROFILE, mProfileData.get(position));
	    Intent i = new Intent();
	    
	    i.putExtras(extras);
	    rv.setOnClickFillInIntent(R.id.widget_text_row, i);
	    
	    // Return the remote views object.
	    return rv;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
		mProfileData = new ArrayList<Profile>();
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
	}

	@Override
	public void onDataSetChanged() {
		Log.d("SET", "Data Change");
		updateProfiles();
	}

	@Override
	public void onDestroy() {
		mProfileData.clear();
		mProfileData = new ArrayList<Profile>();
	}
	
	private void updateProfiles(){
		mProfileData = new ArrayList<Profile>();
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
