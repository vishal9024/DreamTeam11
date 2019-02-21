package os.com.utils

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import os.com.BuildConfig
import os.com.R
import os.com.ui.dashboard.DashBoardActivity
import java.util.*

class CountTimer {
    private val updateRemainingTimeRunnable = Runnable {
        val currentTime = System.currentTimeMillis()
        updateTimeRemaining(currentTime)
    }
var close:Boolean=true
    fun updateTimeRemaining(currentTime: Long) {
        try {
            val timeDiff = AppDelegate.getTimeStampFromDate(dateTime!!)!! - currentTime
            if (timeDiff > 0) {
                val seconds = (timeDiff / 1000).toInt() % 60
                val minutes = (timeDiff / (1000 * 60) % 60).toInt()
                val hours = (timeDiff / (1000 * 60 * 60)).toInt()
                if (hours > 72) {
                    textView!!.text = AppDelegate.convertTimestampToDate(
                        AppDelegate.getTimeStampFromDate(
                            dateTime!!
                        )!!
                    )
                } else {
                    textView!!.setText(hours.toString() + "h " + minutes + "m " + seconds + "s")
                }
            } else {
                stopUpdateTimer()
                if (tmr != null) {
                    tmr = null
                }
                if (close) {
                    close=false
                    AppDelegate.showToast(context, context!!.getString(R.string.time_out))
                    context!!.startActivity(
                        Intent(
                            context,
                            DashBoardActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    context!!.finishAffinity()

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    internal var dateTime: String? = null
    internal var context: AppCompatActivity? = null
    var textView: AppCompatTextView? = null
    internal var tmr: Timer? = null
    private val mHandler = Handler()
    fun startUpdateTimer(context: AppCompatActivity, dateTime: String, textView: AppCompatTextView) {
        close=true
        this.dateTime = dateTime
        this.textView = textView
        this.context = context
        if (BuildConfig.APPLICATION_ID == "os.real11") {
            textView.setTextColor(context.resources.getColor(R.color.colorRed))
            for (drawable in textView.compoundDrawables) {
                if (drawable != null) {
                    drawable.colorFilter = PorterDuffColorFilter(
                        context.resources.getColor(R.color.colorRed), PorterDuff.Mode.SRC_IN
                    )
                }
            }
        }
        tmr = Timer()
        tmr!!.schedule(object : TimerTask() {
            override fun run() {
                mHandler.post(updateRemainingTimeRunnable)
            }
        }, 1000, 1000)
    }

    fun stopUpdateTimer() {
        if (tmr != null) {
            tmr!!.cancel()
        }

    }
}