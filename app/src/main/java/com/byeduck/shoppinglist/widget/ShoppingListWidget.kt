package com.byeduck.shoppinglist.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.byeduck.shoppinglist.R

/**
 * Implementation of App Widget functionality.
 */
class ShoppingListWidget : AppWidgetProvider() {

    private val widgetImages = arrayOf(
        R.drawable.photo1, R.drawable.photo2, R.drawable.photo3
    )

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("ON", "UPDATE")
    }

    override fun onEnabled(context: Context) {
        Log.d("ON", "ENABLED")
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val widgetId = intent?.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID) ?: return
        val sharedPrefs =
            context?.getSharedPreferences(WIDGET_PREFS_NAME, MODE_PRIVATE) ?: return
        if (intent.action.equals("com.byeduck.shoppinglist.widget.CHANGE_IMAGE")) {
            val imgPrefId = getImgPrefId(widgetId)
            val prevImgId = sharedPrefs.getInt(imgPrefId, 0)
            val currentId = (prevImgId + 1) % widgetImages.size
            val appWidgetManager = AppWidgetManager.getInstance(context)
            updateAppWidgetImage(context, appWidgetManager, widgetId, widgetImages[currentId])
            sharedPrefs.edit()
                .putInt(imgPrefId, currentId)
                .apply()
        } else if (intent.action.equals("com.byeduck.shoppinglist.widget.CHANGE_SONG")) {
            val serviceIntent = Intent(context, SongService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                putExtra("SERVICE_ACTION", "CHANGE")
            }
            context.startForegroundService(serviceIntent)
        } else if (intent.action.equals("com.byeduck.shoppinglist.widget.START_STOP_SONG")) {
            val serviceIntent = Intent(context, SongService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                putExtra("SERVICE_ACTION", "START_STOP")
            }
            context.startService(serviceIntent)
        }
    }

    private fun getImgPrefId(widgetId: Int) = "${widgetId}_IMG"
}

internal class Song(
    val resourceId: Int,
    val name: String
)

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