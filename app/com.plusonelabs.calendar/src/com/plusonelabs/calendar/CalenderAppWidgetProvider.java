package com.plusonelabs.calendar;

import java.util.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class CalenderAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		for (int i = 0; i < appWidgetIds.length; i++) {
			int widgetId = appWidgetIds[i];

			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);

			Intent intent = new Intent(context, CalendarWidgetService.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			rv.setRemoteAdapter(R.id.event_list, intent);

			rv.setPendingIntentTemplate(R.id.event_list,
					CalendarIntentUtil.createOpenCalendarEventPendingIntent(context));

			intent = CalendarIntentUtil.createOpenCalendarAtDayIntent(context,
					System.currentTimeMillis());
			rv.setOnClickFillInIntent(R.id.empty_event_list, intent);

			rv.setTextViewText(R.id.calendar_current_date,
					CalendarRemoteViewsFactory.dayStringFormatter.format(new Date()).toUpperCase()
							+ CalendarRemoteViewsFactory.dayDateFormatter.format(new Date()));

			Intent startConfigIntent = new Intent(context, CalendarConfigurationActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startConfigIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setOnClickPendingIntent(R.id.overflow_menu, pendingIntent);

			rv.setEmptyView(R.id.event_list, R.id.empty_event_list);

			appWidgetManager.updateAppWidget(widgetId, rv);
		}
	}

	public static void updateEventListOnAllWidgets(Context context,
			AppWidgetManager appWidgetManager) {
		ComponentName compName = new ComponentName(context, CalenderAppWidgetProvider.class);
		int[] widgetIds = appWidgetManager.getAppWidgetIds(compName);
		appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.event_list);
	}

}
