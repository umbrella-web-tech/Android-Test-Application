package com.test.testapplication.model.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import com.test.testapplication.R
import com.test.testapplication.model.store.sp.ServiceSharedPreferences
import com.test.testapplication.view.activities.MainActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TimerForegroundService : Service() {

    private val TIMER_VALUE_IN_SECONDS = 300L
    private val SERVICE_NOTIFICATION_ID = 17

    private var timerDisposable: Disposable? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //Start timer via Rx
        if (timerDisposable == null) {
            val timeLeft = ServiceSharedPreferences.getLeftTime(this)
            val times = if (timeLeft == -1L || timeLeft == 0L) TIMER_VALUE_IN_SECONDS else timeLeft + 1
            timerDisposable = Observable.interval(1, TimeUnit.SECONDS)
                    .take(times)
                    .map { times - 1 - it }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        ServiceSharedPreferences.setLeftTime(this, it)
                        updateNotificationTime(it);
                    }, {
                        stopService()
                        Toast.makeText(this, R.string.timer_error, Toast.LENGTH_SHORT).show()
                    }, {
                        stopService()
                    })
        }

        //Start foreground service
        startForeground(SERVICE_NOTIFICATION_ID, createNotification(getTimeString(TIMER_VALUE_IN_SECONDS)))
        return Service.START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        stopService()
        super.onDestroy()
    }

    /**
     * Update time msg in notification
     *
     * @param time Current time left value
     */
    private fun updateNotificationTime(time: Long) {
        val notification = createNotification(getTimeString(time))
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(SERVICE_NOTIFICATION_ID, notification)
    }

    /**
     * Create notification with mgs text
     *
     * @param text The text of msg
     */
    private fun createNotification(text: String): Notification {
        val channelId = getString(R.string.notification_channel_id)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .createNotificationChannel(NotificationChannel(channelId,
                            getString(R.string.notification_channel_name),
                            NotificationManager.IMPORTANCE_HIGH))
        }

        val contentIntent = PendingIntent.getActivity(this,
                0, Intent(this, MainActivity::class.java), 0);

        return NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(text)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(contentIntent)
                .build();
    }

    /**
     * Convert time from seconds to {@link String} looks like "Time left: mm:ss"
     *
     * @param seconds The time in seconds for converting
     */
    private fun getTimeString(seconds: Long): String {
        val m = (seconds % 3600) / 60;
        val s = seconds % 60;

        return String.format("Time left: %02d:%02d", m, s);
    }

    /**
     * Stop service and remove notification
     */
    private fun stopService() {
        if (timerDisposable?.isDisposed == false)
            timerDisposable?.dispose()
        ServiceSharedPreferences.setLeftTime(this, -1)
        stopForeground(true)
        stopSelf()
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .cancel(SERVICE_NOTIFICATION_ID)
    }
}
