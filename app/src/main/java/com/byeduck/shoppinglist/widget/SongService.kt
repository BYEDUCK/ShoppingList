package com.byeduck.shoppinglist.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.RemoteViews
import android.widget.Toast
import com.byeduck.shoppinglist.R

class SongService : Service() {

    companion object {
        private var mediaPlayer: MediaPlayer? = null

        fun getMediaPlayer(context: Context, songId: Int): MediaPlayer =
            MediaPlayer.create(context, songId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.extras?.getString(EXTRA_SERVICE_ACTION, SERVICE_ACTION_START_STOP)
        val widgetId = intent?.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
            ?: AppWidgetManager.INVALID_APPWIDGET_ID
        val sharedPreferences =
            applicationContext.getSharedPreferences(WIDGET_PREFS_NAME, MODE_PRIVATE)
        val songPrefId = UpdateWidgetService.getSongPrefId(widgetId)
        val songId = sharedPreferences.getInt(songPrefId, 0)
        when (action) {
            SERVICE_ACTION_PAUSE_RESUME -> {
                Toast.makeText(applicationContext, "PAUSE/RESUME", Toast.LENGTH_SHORT).show()
                val mp = mediaPlayer ?: return START_NOT_STICKY
                if (mp.isPlaying) mp.pause() else mp.start()
            }
            SERVICE_ACTION_START_STOP -> {
                Toast.makeText(applicationContext, "START/STOP", Toast.LENGTH_SHORT).show()
                val mp = mediaPlayer ?: getMediaPlayer(
                    applicationContext,
                    UpdateWidgetService.widgetSongs[songId].resourceId
                )
                if (mp.isPlaying) mp.stop() else {
                    mp.setOnPreparedListener { it.start() }
                }
                mediaPlayer = mp
            }
            SERVICE_ACTION_CHANGE -> {
                Toast.makeText(applicationContext, "CHANGE", Toast.LENGTH_SHORT).show()
                val nextSongId = (songId + 1) % UpdateWidgetService.widgetSongs.size
                mediaPlayer?.stop()
                updateSongTitle(nextSongId, widgetId)
                sharedPreferences.edit()
                    .putInt(songPrefId, nextSongId)
                    .apply()
            }
            else -> return START_NOT_STICKY
        }
        return START_STICKY
    }

    override fun onDestroy() {
    }

    private fun updateSongTitle(songId: Int, widgetId: Int) {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.shopping_list_widget)
        remoteViews.setTextViewText(R.id.songTitleTxt, UpdateWidgetService.widgetSongs[songId].name)
        appWidgetManager.updateAppWidget(widgetId, remoteViews)
    }

}