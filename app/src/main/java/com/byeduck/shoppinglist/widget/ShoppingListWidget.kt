package com.byeduck.shoppinglist.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.byeduck.shoppinglist.R

/**
 * Implementation of App Widget functionality.
 */
class ShoppingListWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (widgetId in appWidgetIds) {
            Log.d("WIDGET UPDATE", "widget id : $widgetId")
            val remoteViews = UpdateWidgetService.updateWidget(context, widgetId)
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    override fun onEnabled(context: Context) {
        Log.d("ON", "ENABLED")
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        val sharedPrefs = context.getSharedPreferences(WIDGET_PREFS_NAME, MODE_PRIVATE)
        sharedPrefs.edit().clear().apply()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val widgetId = intent?.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID) ?: return
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            return
        }
        Log.d("WIDGET RECEIVE", "widget id : $widgetId")
        val sharedPrefs =
            context?.getSharedPreferences(WIDGET_PREFS_NAME, MODE_PRIVATE) ?: return
        when {
            intent.action.equals(WIDGET_ACTION_CHANGE_IMAGE) -> {
                Toast.makeText(context, "CHANGE IMG", Toast.LENGTH_SHORT).show()
                val imgPrefId = UpdateWidgetService.getImgPrefId(widgetId)
                val prevImgId = sharedPrefs.getInt(imgPrefId, 0)
                val currentId = (prevImgId + 1) % UpdateWidgetService.widgetImages.size
                val appWidgetManager = AppWidgetManager.getInstance(context)
                updateAppWidgetImage(
                    context,
                    appWidgetManager,
                    widgetId,
                    UpdateWidgetService.widgetImages[currentId]
                )
                sharedPrefs.edit()
                    .putInt(imgPrefId, currentId)
                    .apply()
            }
            intent.action.equals(WIDGET_ACTION_CHANGE_SONG) -> {
                val serviceIntent = Intent(context, SongService::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    putExtra(EXTRA_SERVICE_ACTION, SERVICE_ACTION_CHANGE)
                }
                context.startForegroundService(serviceIntent)
            }
            intent.action.equals(WIDGET_ACTION_START_STOP_SONG) -> {
                val serviceIntent = Intent(context, SongService::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    putExtra(EXTRA_SERVICE_ACTION, SERVICE_ACTION_START_STOP)
                }
                context.startForegroundService(serviceIntent)
            }
            intent.action.equals(WIDGET_ACTION_PAUSE_RESUME_SONG) -> {
                val serviceIntent = Intent(context, SongService::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    putExtra(EXTRA_SERVICE_ACTION, SERVICE_ACTION_PAUSE_RESUME)
                }
                context.startForegroundService(serviceIntent)
            }
        }
    }
}

internal fun updateAppWidgetImage(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    img: Int
) {
    val views = RemoteViews(context.packageName, R.layout.shopping_list_widget)
    views.setImageViewResource(R.id.imageView, img)
    appWidgetManager.updateAppWidget(appWidgetId, views)
}