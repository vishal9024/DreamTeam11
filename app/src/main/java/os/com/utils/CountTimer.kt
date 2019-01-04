package os.com.utils

import android.os.Handler
import androidx.appcompat.widget.AppCompatTextView
import java.util.*

class CountTimer {
    private val updateRemainingTimeRunnable = Runnable {
        val currentTime = System.currentTimeMillis()
        updateTimeRemaining(currentTime)
    }
    fun updateTimeRemaining(currentTime: Long) {
        try {
                val timeDiff = AppDelegate.getTimeStampFromDate(dateTime!!)!! - currentTime
                AppDelegate.LogT("timeDiff=" + timeDiff)
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
                    textView!!.setText("Expired!!")
                }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    internal var dateTime: String? = null
    var textView: AppCompatTextView?=null
    internal var tmr: Timer? = null
    private val mHandler = Handler()
    fun startUpdateTimer(dateTime: String,textView: AppCompatTextView) {
        this .dateTime=dateTime
        this .textView=textView
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