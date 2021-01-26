package com.byeduck.shoppinglist.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.databinding.ActivityAppWidgetConfigBinding

class AppWidgetConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppWidgetConfigBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppWidgetConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        widgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val url = sharedPreferences.getString(getUrlId(widgetId), "https://byeduck.com")
        binding.urlTxt.setText(url)
    }

    fun applyConfig(ignored: View) {
        val url = binding.urlTxt.text.toString()
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val remoteViews = RemoteViews(packageName, R.layout.shopping_list_widget)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        val goToPagePendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val changeImgBroadcast = Intent().apply {
            action = "com.byeduck.shoppinglist.widget.CHANGE_IMAGE"
            component = ComponentName(
                "com.byeduck.shoppinglist",
                "com.byeduck.shoppinglist.widget.ShoppingListWidget"
            )
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        val changeImgPendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            changeImgBroadcast,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        remoteViews.setOnClickPendingIntent(R.id.goToPageBtn, goToPagePendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.changeImgBtn, changeImgPendingIntent)
        appWidgetManager.updateAppWidget(widgetId, remoteViews)
        sharedPreferences.edit()
            .putString(getUrlId(widgetId), url)
            .apply()
        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        setResult(RESULT_OK, resultValue)
        finish()
    }

    private fun getUrlId(appWidgetId: Int) = "${appWidgetId}_URL"
}