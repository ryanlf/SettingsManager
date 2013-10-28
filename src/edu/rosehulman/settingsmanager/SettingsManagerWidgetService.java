package edu.rosehulman.settingsmanager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class SettingsManagerWidgetService extends RemoteViewsService {

	@Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new SettingsManagerRemoteViewsFactory(this.getApplicationContext(), intent);
    }

}

class SettingsManagerRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
	
	private Context mContext;
	private int mAppWidgetId;
	private ArrayList<Profile> mProfileData;

	public SettingsManagerRemoteViewsFactory(Context applicationContext,
			Intent intent) {
		 mContext = applicationContext;
	        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
	                AppWidgetManager.INVALID_APPWIDGET_ID);
	        try {
				FileInputStream fileRead = mContext.openFileInput(mContext.getString(R.string.data_file_name));
				ObjectInputStream objectInput = new ObjectInputStream(fileRead);
				mProfileData = (ArrayList<Profile>) objectInput.readObject();
				objectInput.close();
				fileRead.close();
				Log.d("SET", "ReadFile "+ mProfileData.size());
			} catch (FileNotFoundException e) {
				mProfileData = new ArrayList<Profile>();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}}
	

	@Override
	public int getCount() {
		
		return mProfileData.size();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public RemoteViews getLoadingView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		// Construct a remote views item based on the app widget item XML file, 
	    // and set the text based on the position.
	    RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
	    rv.setTextViewText(R.id.widget_text_row, mProfileData.get(position).toString());
	    // Return the remote views object.
	    return rv;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
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
	
}
