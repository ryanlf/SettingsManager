package edu.rosehulman.settingsmanager;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class SettingsManagerWidgetProvider extends AppWidgetProvider {

	public static final String SET_PROFILE = "edu.rosehulman.settingsmanger.SET_PROFILE";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// There may be multiple widgets active, so update all of them
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
			setProfileIntent.setAction(SettingsManagerWidgetProvider.SET_PROFILE);
			setProfileIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetIds[i]);
			PendingIntent setProfilePendingIntent = PendingIntent.getBroadcast(
					context, 0, setProfileIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setPendingIntentTemplate(R.id.widgit_list_view,
					setProfilePendingIntent);
			appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
			// updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
		}
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
		super.onReceive(context, intent);
	}
}
