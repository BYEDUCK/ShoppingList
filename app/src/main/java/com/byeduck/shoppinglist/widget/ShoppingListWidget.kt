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
            context?.getSharedPreferences(PREFS_NAME, MODE_PRIVATE) ?: return
        if (intent.action.equals("com.byeduck.shoppinglist.widget.CHANGE_IMAGE")) {
            val imgPrefId = getImgPrefId(widgetId)
            val prevImg = sharedPrefs.getInt(imgPrefId, R.drawable.photo1)
            val currentImg =
                if (prevImg == R.drawable.photo1) R.drawable.photo2 else R.drawable.photo1
            val appWidgetManager = AppWidgetManager.getInstance(context)
            updateAppWidgetImage(context, appWidgetManager, widgetId, currentImg)
            sharedPrefs.edit()
                .putInt(imgPrefId, currentImg)
                .apply()
        }
    }

    private fun getImgPrefId(widgetId: Int) = "${widgetId}_IMG"
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