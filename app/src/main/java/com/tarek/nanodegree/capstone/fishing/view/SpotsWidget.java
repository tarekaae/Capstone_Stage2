package com.tarek.nanodegree.capstone.fishing.view;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.tarek.nanodegree.capstone.fishing.R;

/**
 * Implementation of App Widget functionality.
 */
public class SpotsWidget extends AppWidgetProvider {

    public static final String TOTAL_SPOTS = "widget_Total_spots" ;
    public static final String FISH_SPECIES = "widget_fish_species" ;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        SharedPreferences preferences = context.getSharedPreferences("pref", 0);
         // Construct the RemoteViews object
        CharSequence defaultName = context.getString(R.string.appwidget_text);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.spots_widget);

        String titleString = preferences.getString(TOTAL_SPOTS, defaultName.toString());
        views.setTextViewText(R.id.appwidget_title, titleString +" " + context.getString(R.string.spots));


        String bodyString = preferences.getString(FISH_SPECIES, "");
        String[] types = bodyString.split("-");
        String arranged = "";
        for (int i = 0; i < types.length; i++) {
            arranged = arranged + "\n" + "-" + types[i];
        }
        views.setTextViewText(R.id.appwidget_body, arranged);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

