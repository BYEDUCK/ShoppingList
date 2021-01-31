package com.byeduck.shoppinglist.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.byeduck.shoppinglist.R
import com.byeduck.shoppinglist.shops.AddEditViewShopActivity

class UpdateWidgetService {

    companion object {

        private val myComponent = ComponentName(
            "com.byeduck.shoppinglist",
            "com.byeduck.shoppinglist.widget.ShoppingListWidget"
        )

        val widgetImages = arrayOf(
            R.drawable.photo1, R.drawable.photo2, R.drawable.photo3
        )

        val widgetSongs = arrayOf(
            Song(R.raw.song1, "Billy Jean"), Song(R.raw.song2, "Smooth Criminal")
        )

        fun updateWidget(context: Context, widgetId: Int): RemoteViews {
            val sharedPreferences = context.getSharedPreferences(WIDGET_PREFS_NAME, MODE_PRIVATE)
            val remoteViews = RemoteViews(context.packageName, R.layout.shopping_list_widget)
            val songId = sharedPreferences.getInt(getSongPrefId(widgetId), 0)
            val url = sharedPreferences.getString(getUrlPrefId(widgetId), "https://byeduck.com")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            val goToPagePendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT
            )

            val changeImgBroadcast = Intent().apply {
                action = WIDGET_ACTION_CHANGE_IMAGE
                component = myComponent
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }
            val changeImgPendingIntent = PendingIntent.getBroadcast(
                context,
                1,
                changeImgBroadcast,
                PendingIntent.FLAG_IMMUTABLE
            )
            val changeSongBroadcast = Intent().apply {
                action = WIDGET_ACTION_CHANGE_SONG
                component = myComponent
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }
            val changeSongPendingIntent = PendingIntent.getBroadcast(
                context,
                2,
                changeSongBroadcast,
                PendingIntent.FLAG_IMMUTABLE
            )
            val startStopSongBroadcast = Intent().apply {
                action = WIDGET_ACTION_START_STOP_SONG
                component = myComponent
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }
            val startStopSongPendingIntent = PendingIntent.getBroadcast(
                context,
                3,
                startStopSongBroadcast,
                PendingIntent.FLAG_IMMUTABLE
            )
            val pauseResumeSongBroadcast = Intent().apply {
                action = WIDGET_ACTION_PAUSE_RESUME_SONG
                component = myComponent
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }
            val pauseResumePendingIntent = PendingIntent.getBroadcast(
                context,
                4,
                pauseResumeSongBroadcast,
                PendingIntent.FLAG_IMMUTABLE
            )

            remoteViews.setOnClickPendingIntent(R.id.goToPageBtn, goToPagePendingIntent)
            remoteViews.setOnClickPendingIntent(R.id.changeImgBtn, changeImgPendingIntent)
            remoteViews.setOnClickPendingIntent(R.id.changeSongBtn, changeSongPendingIntent)
            remoteViews.setOnClickPendingIntent(R.id.startStopSongBtn, startStopSongPendingIntent)
            remoteViews.setOnClickPendingIntent(R.id.pauseResumeSongBtn, pauseResumePendingIntent)
            remoteViews.setTextViewText(R.id.songTitleTxt, widgetSongs[songId].name)
            remoteViews.setRemoteAdapter(
                R.id.favouriteShopsList,
                Intent(context, FavouriteShopsRemoteViewsService::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    // https://android.googlesource.com/platform/development/+/master/samples/StackWidget/src/com/example/android/stackwidget/StackWidgetProvider.java
                    // When intents are compared, the extras are ignored, so we need to embed the extras
                    // into the data so that the extras will not be ignored.
                    data = Uri.parse(this.toUri(Intent.URI_INTENT_SCHEME))
                }
            )
            remoteViews.setPendingIntentTemplate(
                R.id.favouriteShopsList, PendingIntent.getActivity(
                    context, 5, Intent(context, AddEditViewShopActivity::class.java).apply {
                        putExtra("view", true)
                        data = Uri.parse(this.toUri(Intent.URI_INTENT_SCHEME))
                    },
                    PendingIntent.FLAG_IMMUTABLE
                )
            )

            return remoteViews
        }

        fun getUrlPrefId(widgetId: Int) = "${widgetId}_URL"
        fun getSongPrefId(widgetId: Int) = "${widgetId}_SNG"
        fun getImgPrefId(widgetId: Int) = "${widgetId}_IMG"
    }

}