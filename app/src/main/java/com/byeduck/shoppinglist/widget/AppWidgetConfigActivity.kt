package com.byeduck.shoppinglist.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        Log.d("CONFIG ACTIVITY", "widget id : $widgetId")
        sharedPreferences = getSharedPreferences(WIDGET_PREFS_NAME, MODE_PRIVATE)
        val url = sharedPreferences.getString(
            UpdateWidgetService.getUrlPrefId(widgetId),
            "https://byeduck.com"
        )
        binding.urlTxt.setText(url)
    }

    fun applyConfig(ignored: View) {
        val url = binding.urlTxt.text.toString()
        sharedPreferences.edit()
            .putString(UpdateWidgetService.getUrlPrefId(widgetId), url)
            .putInt(UpdateWidgetService.getSongPrefId(widgetId), 0)
            .putInt(UpdateWidgetService.getImgPrefId(widgetId), 0)
            .apply()

        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val remoteViews = UpdateWidgetService.updateWidget(applicationContext, widgetId)
        appWidgetManager.updateAppWidget(widgetId, remoteViews)
        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        setResult(RESULT_OK, resultValue)
        finish()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}