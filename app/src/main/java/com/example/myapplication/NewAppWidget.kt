package com.example.myapplication

import android.app.PendingIntent
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.RemoteViews
import android.widget.Toast

/**
 * Implementation of App Widget functionality.
 */
var imageCounter = 0
var musicCounter = 0
lateinit var player: MediaPlayer
var myFlag = -1

class NewAppWidget : AppWidgetProvider() {

    override fun onUpdate(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action == context?.getString(R.string.action1)) {
            context?.updateImage()
        }
        // music play
        if (intent?.action == context?.getString(R.string.playMusic)) {
            Toast.makeText(context, "Start Music", Toast.LENGTH_SHORT).show()
            if (myFlag == -1) {
                player = MediaPlayer.create(context, R.raw.rasputin)
                myFlag++
                musicCounter++
            }
            player.start()
        }
        // music stop
        if (intent?.action == "com.example.myapplication.musicstop") {
            Toast.makeText(context, "Stop Music", Toast.LENGTH_SHORT).show()
            player.pause()
        }
        // music next
        if (intent?.action == "com.example.myapplication.musicnext") {
            Toast.makeText(context, "Next Music", Toast.LENGTH_SHORT).show()
            player.stop()
            if (musicCounter == 0) {
                player = MediaPlayer.create(context, R.raw.rasputin)
                musicCounter++
            } else if (musicCounter == 1) {
                player = MediaPlayer.create(context, R.raw.macarena)
                musicCounter--
            }
            player.start()
        }

    }


    private fun Context.updateImage() {
        val widgetViews = RemoteViews(this.packageName, R.layout.new_app_widget)
        if (imageCounter == 0) {
            imageCounter++
            widgetViews.setImageViewResource(R.id.imageView, R.drawable._1)
            Toast.makeText(this, "Image set to 1", Toast.LENGTH_SHORT).show()
        } else {
            imageCounter--
            widgetViews.setImageViewResource(R.id.imageView, R.drawable._2)
            Toast.makeText(this, "Image set to 2", Toast.LENGTH_SHORT).show()
        }
        val widgetComponent = ComponentName(this, NewAppWidget::class.java)
        val widgetManager = AppWidgetManager.getInstance(this)
        widgetManager.updateAppWidget(widgetComponent, widgetViews)
    }

}


    internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
    ) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.new_app_widget)
        //// klikajac w przycisk widget_bt1 uruchamiamy to co nizej i odpalamy przegladarke
        val intentWWW = Intent(Intent.ACTION_VIEW)
        intentWWW.data = Uri.parse("https://www.google.com")
        val pendingWWW = PendingIntent.getActivity(
                context,
                0,
                intentWWW,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        views.setOnClickPendingIntent(R.id.widget_bt1, pendingWWW)
        ////
        //// przycisk widget_bt2 (dopisujemy w manifescie  <action android:name="@string/action1"/> i przechodzi do apki wyswietla toasta on recieve  zmienia obrazki
        val intentAction = Intent(context.getString(R.string.action1))
        intentAction.component = ComponentName(context, NewAppWidget::class.java)
        val pendingAction = PendingIntent.getBroadcast(
                context,
                0,
                intentAction,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.widget_bt2, pendingAction)
        ////
        //// muzyka - start
        val intentMusicStart = Intent(context.getString(R.string.playMusic))
        intentMusicStart.component = ComponentName(context, NewAppWidget::class.java)
        val pendingMusicStart = PendingIntent.getBroadcast(
                context,
                64512,
                intentMusicStart,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.bt_start, pendingMusicStart)

        //// music - stop
        val intentStop = Intent("com.example.myapplication.musicstop")
        intentStop.component = ComponentName(context, NewAppWidget::class.java)
        val pendingStop = PendingIntent.getBroadcast(
                context,
                43212,
                intentStop,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.bt_stop, pendingStop)
        //// music - next
        val intentMusicNextSong = Intent("com.example.myapplication.musicnext")
        intentMusicNextSong.component = ComponentName(context, NewAppWidget::class.java)
        val pendingMusicNextSong = PendingIntent.getBroadcast(
                context,
                24123,
                intentMusicNextSong,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        views.setOnClickPendingIntent(R.id.bt_next,pendingMusicNextSong)
        // Instruct the widget manager to update the widget

        appWidgetManager.updateAppWidget(appWidgetId, views)
}