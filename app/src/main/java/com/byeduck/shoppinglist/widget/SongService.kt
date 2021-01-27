package com.byeduck.shoppinglist.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast
import com.byeduck.shoppinglist.R

internal var mediaPlayer: MediaPlayer? = null

class SongService : Service() {

    private val widgetSongs = arrayOf(
        Song(R.raw.song1, "Billy Jean"), Song(R.raw.song2, "Smooth Criminal")
    )

//    companion object {
//        private var mediaPlayer: MediaPlayer? = null
//    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.extras?.getString("SERVICE_ACTION", "START_STOP")
        var mp = mediaPlayer
        when (action) {
            "START_STOP" -> {
                if (mp != null) {
                    if (mp.isPlaying) mp.pause() else mp.start()
                }
            }
            "CHANGE" -> {
                val widgetId = intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
                    ?: AppWidgetManager.INVALID_APPWIDGET_ID
                val songPrefId = getSongPrefId(widgetId)
                val sharedPreferences =
                    applicationContext.getSharedPreferences(WIDGET_PREFS_NAME, MODE_PRIVATE)
                val prevSongId = sharedPreferences.getInt(songPrefId, -1)
                val songId = (prevSongId + 1) % widgetSongs.size
                mp?.stop()
                mp = MediaPlayer.create(applicationContext, widgetSongs[songId].resourceId)
                mp.setOnPreparedListener { mPlayer ->
                    run {
                        mPlayer.start()
                        mediaPlayer = mPlayer
                        sharedPreferences.edit()
                            .putInt(songPrefId, songId)
                            .apply()
                    }
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        Toast.makeText(applicationContext, "DESTROOOOYEED!", Toast.LENGTH_LONG).show()
        mediaPlayer?.stop()
    }


    private fun getSongPrefId(widgetId: Int) = "${widgetId}_SNG"

}