package edu.rosehulman.settingsmanager;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


/**
 * Implementation of App Widget functionality.
 */
public class SettingsManagerWidgetProvider extends AppWidgetProvider {

	public static final String SET_PROFILE = "edu.rosehulman.settingsmanger.SET_PROFILE";
	public static final String ID_FILE = "WidgetIDs_FILE";

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
			

			/*Intent setProfileIntent = new Intent(context,
					SettingsManagerWidgetProvider.class);
			setProfileIntent.setAction(SettingsManagerWidgetProvider.SET_PROFILE);
			setProfileIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetIds[i]);
			PendingIntent setProfilePendingIntent = PendingIntent.getBroadcast(
					context, 0, setProfileIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setPendingIntentTemplate(R.id.widgit_list_view,
					setProfilePendingIntent);*/
			appWidgetManager.updateAppWidget(appWidgetIds[i], null);
			appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
			// updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
			Log.d("SET", "Updated widget "+appWidgetIds[i]);
		}
		writeWidgetIds(context, appWidgetIds);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onEnabled(Context context) {
		// Enter relevant functionality for when the first widget is created
		
	}

	@Override
	public void onDisabled(Context context) {
		// Enter relevant functionality for when the last widget is disabled
	}

	/*static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {

		CharSequence widgetText = context.getString(R.string.appwidget_text);
		// Construct the RemoteViews object
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.settings_manager_widget);
		// views.setTextViewText(R.id.appwidget_text, widgetText);

		// views.setRemoteAdapter(appWidgetId, intent)
		// Instruct the widget manager to update the widget
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}*/

	@Override
	public void onReceive(Context context, Intent intent) {
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		if (intent.getAction().equals(SET_PROFILE)) {
			Toast.makeText(context, "CLCIKED", Toast.LENGTH_SHORT).show();
		}
		if (intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")){
			Log.d("SET", "Update Broadcast Detected");
			onUpdate(context, AppWidgetManager.getInstance(context), intListToPrimative(readWidgetIds(context)));
		}
		super.onReceive(context, intent);
	}
	
	@Override
	public void onDeleted (Context context, int[] appWidgetIds) {
        ArrayList<Integer> file = readWidgetIds(context);
        for (int id : appWidgetIds){
        	if (file.contains((Integer) id)) file.remove((Integer) id);
        }
        
        writeWidgetIds(context,file);
		super.onDeleted(context, appWidgetIds);
    }
	
	public static ArrayList<Integer> readWidgetIds(Context context){
		ArrayList<Integer> result = null;
		try {
			FileInputStream fileRead = context.openFileInput(ID_FILE);
			ObjectInputStream objectInput = new ObjectInputStream(fileRead);
			result = (ArrayList<Integer>) objectInput.readObject();
			objectInput.close();
			fileRead.close();
		} catch (FileNotFoundException e) {
			result = new ArrayList<Integer>();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void writeWidgetIds(Context context,int[] ids){
		ArrayList<Integer> saveIds = new ArrayList<Integer>();
		for (int id : ids){
			saveIds.add(id);
		}
		try {
			FileOutputStream fileCreate = context.openFileOutput(ID_FILE, Context.MODE_PRIVATE);
			ObjectOutputStream objectCreate = new ObjectOutputStream(fileCreate);
			objectCreate.writeObject(saveIds);
			objectCreate.close();
			fileCreate.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void writeWidgetIds(Context context, ArrayList<Integer> saveIds){
		try {
			FileOutputStream fileCreate = context.openFileOutput(ID_FILE, Context.MODE_PRIVATE);
			ObjectOutputStream objectCreate = new ObjectOutputStream(fileCreate);
			objectCreate.writeObject(saveIds);
			objectCreate.close();
			fileCreate.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static int[] intListToPrimative(ArrayList<Integer> list){
		int[] result = new int[list.size()];
		for (int i=0; i < result.length; i++)
	    {
	        result[i] = list.get(i).intValue();
	    }
		return result;
	}
}