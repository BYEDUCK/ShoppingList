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
        val widgetSongs = arrayOf(
            Song(R.raw.song1, "Billy Jean"), Song(R.raw.song2, "Smooth Criminal")
        )

        fun getMediaPlayer(context: Context, songId: Int): MediaPlayer =
            MediaPlayer.create(context, songId)

        fun getSongPrefId(widgetId: Int) = "${widgetId}_SNG"
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
        val songPrefId = getSongPrefId(widgetId)
        val songId = sharedPreferences.getInt(songPrefId, 0)
        when (action) {
            SERVICE_ACTION_PAUSE_RESUME -> {
                val mp = mediaPlayer ?: return START_NOT_STICKY
                if (mp.isPlaying) mp.pause() else mp.start()
            }
            SERVICE_ACTION_START_STOP -> {
                val mp = mediaPlayer ?: getMediaPlayer(
                    applicationContext,
                    widgetSongs[songId].resourceId
                )
                if (mp.isPlaying) mp.stop() else {
                    mp.setOnPreparedListener { it.start() }
                }
                mediaPlayer = mp
            }
            SERVICE_ACTION_CHANGE -> {
                val nextSongId = (songId + 1) % widgetSongs.size
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
        Toast.makeText(applicationContext, "DESTROOOOYEED!", Toast.LENGTH_LONG).show()
        mediaPlayer?.stop()
    }

    private fun updateSongTitle(songId: Int, widgetId: Int) {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.shopping_list_widget)
        remoteViews.setTextViewText(R.id.songTitleTxt, widgetSongs[songId].name)
        appWidgetManager.updateAppWidget(widgetId, remoteViews)
    }

}