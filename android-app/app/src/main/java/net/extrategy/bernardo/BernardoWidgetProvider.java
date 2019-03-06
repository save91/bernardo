package net.extrategy.bernardo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import net.extrategy.bernardo.network.BernardoIntentService;
import net.extrategy.bernardo.ui.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BernardoWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                boolean isLoading, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bernardo_widget_provider);



        Intent intent = new Intent(context, BernardoIntentService.class);
        intent.putExtra(BernardoIntentService.EXTRA_ACTION, BernardoIntentService.ACTION_DOOR);

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (isLoading) {
            views.setTextViewText(R.id.text_widget, context.getString(R.string.open));
        } else {
            views.setTextViewText(R.id.text_widget, context.getString(R.string.app_name));
        }

        views.setOnClickPendingIntent(R.id.button_door_widget, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, false, appWidgetId);
        }
    }

    public static void updateLoading(Context context, AppWidgetManager appWidgetManager,
                                          boolean isLoading, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, isLoading, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

