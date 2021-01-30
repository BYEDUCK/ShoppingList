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
import com.byeduck.shoppinglist.shops.AddEditViewShopActivity

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
        sharedPreferences = getSharedPreferences(WIDGET_PREFS_NAME, MODE_PRIVATE)
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
        val myComponent = ComponentName(
            "com.byeduck.shoppinglist",
            "com.byeduck.shoppinglist.widget.ShoppingListWidget"
        )
        val changeImgBroadcast = Intent().apply {
            action = "com.byeduck.shoppinglist.widget.CHANGE_IMAGE"
            component = myComponent
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        val changeImgPendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            changeImgBroadcast,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val changeSongBroadcast = Intent().apply {
            action = "com.byeduck.shoppinglist.widget.CHANGE_SONG"
            component = myComponent
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        val changeSongPendingIntent = PendingIntent.getBroadcast(
            this,
            2,
            changeSongBroadcast,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val startStopSongBroadcast = Intent().apply {
            action = "com.byeduck.shoppinglist.widget.START_STOP_SONG"
            component = myComponent
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        val startStopSongPendingIntent = PendingIntent.getBroadcast(
            this,
            3,
            startStopSongBroadcast,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val pauseResumeSongBroadcast = Intent().apply {
            action = "com.byeduck.shoppinglist.widget.PAUSE_RESUME_SONG"
            component = myComponent
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        val pauseResumePendingIntent = PendingIntent.getBroadcast(
            this,
            4,
            pauseResumeSongBroadcast,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        remoteViews.setOnClickPendingIntent(R.id.goToPageBtn, goToPagePendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.changeImgBtn, changeImgPendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.changeSongBtn, changeSongPendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.startStopSongBtn, startStopSongPendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.pauseResumeSongBtn, pauseResumePendingIntent)
        remoteViews.setRemoteAdapter(
            R.id.favouriteShopsList,
            Intent(this, FavouriteShopsRemoteViewsService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                // https://android.googlesource.com/platform/development/+/master/samples/StackWidget/src/com/example/android/stackwidget/StackWidgetProvider.java
                // When intents are compared, the extras are ignored, so we need to embed the extras
                // into the data so that the extras will not be ignored.
                data = Uri.parse(this.toUri(Intent.URI_INTENT_SCHEME))
            }
        )
        remoteViews.setPendingIntentTemplate(
            R.id.favouriteShopsList, PendingIntent.getActivity(
                this, 0, Intent(this, AddEditViewShopActivity::class.java).apply {
                    putExtra("view", true)
                    data = Uri.parse(this.toUri(Intent.URI_INTENT_SCHEME))
                },
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
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

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun getUrlId(appWidgetId: Int) = "${appWidgetId}_URL"
}