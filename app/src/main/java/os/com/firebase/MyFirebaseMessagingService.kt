package os.com.firebase

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import os.com.R
import os.com.constant.IntentConstant
import os.com.constant.PrefConstant
import os.com.data.Prefs
import os.com.ui.dashboard.DashBoardActivity
import os.com.ui.splash.activity.SplashActivity
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {
    var unread = 0
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Log.e("Remote Message=", remoteMessage!!.data.toString())
        var data = sendNotif(remoteMessage.data)

        unread = Prefs(this).getIntValue(PrefConstant.UNREAD_COUNT, 0)
        unread = data.badge_count!!.toInt()
        Prefs(this).putIntValue(PrefConstant.UNREAD_COUNT, unread)
//        ShortcutBadger.applyCount(this, Prefs(this).getIntValue(PrefConstant.UNREAD_COUNT,0))
        if (isAppIsInBackground(this))
            generateNotification(data)
    }
    /*pendingCart
    orderPlaced
    orderCancelled
    productAvailable
    general
    deliveryStatusChange
    deliveryBoyAssigned*/

    object NotificationType {
        const val admin = "1"
        const val signup = "2"
        const val bonus = "3"
        const val match_start = "4"
        const val match_end = "5"
        const val winning_amount = "6"
    }

    //
    private fun generateNotification(data: PNModel) {

        val largeIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_foreground)
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        val mBuilder = NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))

            .setContentTitle(getString(R.string.app_name))
            .setContentText(data.title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setLargeIcon(largeIcon)
            .setStyle(NotificationCompat.BigTextStyle().bigText(data.title))
            .setContentIntent(setNotificationIntent(data))
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher)
            mBuilder.color = ContextCompat.getColor(this, R.color.colorPrimary)
        } else
            mBuilder.setSmallIcon(R.mipmap.ic_launcher)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.default_notification_channel_name)
            val description = getString(R.string.default_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(getString(R.string.default_notification_channel_id), name, importance)
            mChannel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(mChannel)
        }
//        val id = (System.currentTimeMillis() * (Math.random() * 100).toInt()).toInt()
        notificationManager.notify(getRandomNumer(), mBuilder.build())


    }

    //
    private fun setNotificationIntent(data: PNModel): PendingIntent {
        var notificationIntent: Intent? = null
//        if (data.type.equals(NotificationType.admin)) {
        notificationIntent = Intent(this, DashBoardActivity::class.java)
        notificationIntent.putExtra("notification_Data", data)
//            notificationIntent = Intent(this, NotificationActivity::class.java)
//        } else if (data.notification_type.equals(NotificationType.orderPlaced)) {
//            notificationIntent = Intent(this, MyOrderDetailActivity::class.java)
//            notificationIntent.putExtra(IntentConstant.ORDER_ID, data.item_id)
//        } else if (data.notification_type.equals(NotificationType.orderCancelled)) {
//            notificationIntent = Intent(this, MyOrderDetailActivity::class.java)
//            notificationIntent.putExtra(IntentConstant.ORDER_ID, data.item_id)
//        } else if (data.notification_type.equals(NotificationType.productAvailable)) {
//            notificationIntent = Intent(this, ProductDetailActivity::class.java)
//            notificationIntent.putExtra(IntentConstant.PRODUCT_ID, data.item_id)
//        } else if (data.notification_type.equals(NotificationType.deliveryStatusChange)) {
//            notificationIntent = Intent(this, MyOrderDetailActivity::class.java)
//            notificationIntent.putExtra(IntentConstant.ORDER_ID, data.item_id)
//        }else if (data.notification_type.equals(NotificationType.OrderDelivered)) {
//            notificationIntent = Intent(this, MyOrderDetailActivity::class.java)
//            notificationIntent.putExtra(IntentConstant.ORDER_ID, data.item_id)
//        } else if (data.notification_type.equals(NotificationType.deliveryBoyAssigned)) {
//            notificationIntent = Intent(this, MyOrderDetailActivity::class.java)
//            notificationIntent.putExtra(IntentConstant.ORDER_ID, data.item_id)
//        } else if (data.notification_type.equals(NotificationType.general)) {
////          notificationIntent!!.putExtra(IntentConstant.ORDER_ID, data.item_id)
//            notificationIntent = Intent(this, NotificationActivity2::class.java)
//        }

//      val notificationIntent = Intent(this, ProductDetailActivity::class.java)
        notificationIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val parentIntent = Intent(this, SplashActivity::class.java)
        parentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        /* create stack builder if you want to open parent activty on notification click*/
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(SplashActivity::class.java)
//        notificationIntent.putExtra(IntentConstant.ORDER_ID, data.item_id)
        notificationIntent.putExtra(IntentConstant.FROM, 1)
        /* add all notification to stack*/
        stackBuilder.addNextIntent(parentIntent)
        stackBuilder.addNextIntent(notificationIntent)
        /* get pending intent from stack builder*/
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT)
    }

    private fun sendNotif(data: Map<String, String>): PNModel {
        var notificationsModel = PNModel()
        notificationsModel.type = data["type"]
        notificationsModel.body = data["body"]
        notificationsModel.title = data["title"]
        notificationsModel.badge_count = data["badge_count"]
        notificationsModel.matchData = data["matchData"]
//        val match = Match()
//        if (!data["matchData"]!!.isEmpty()) {
//            val jsonObject = JSONObject(data["matchData"])
//            if (jsonObject.has("contestId"))
//                match.contestId = jsonObject.optString("contestId")
//            if (jsonObject.has("visitor_team_name"))
//                match.visitor_team_name = jsonObject.optString("visitor_team_name")
//            if (jsonObject.has("match_id"))
//                match.match_id = jsonObject.optString("match_id")
//            if (jsonObject.has("visitor_team_id"))
//                match.visitor_team_id = jsonObject.optString("visitor_team_id")
//            if (jsonObject.has("strTime"))
//                match.strTime = jsonObject.optString("strTime")
//            if (jsonObject.has("strDate"))
//                match.strDate = jsonObject.optString("strDate")
//            if (jsonObject.has("local_team_id"))
//                match.local_team_id = jsonObject.optString("local_team_id")
//            if (jsonObject.has("series_id"))
//                match.series_id = jsonObject.optString("series_id")
//            if (jsonObject.has("local_team_name"))
//                match.local_team_name = jsonObject.optString("local_team_name")
//        }
//        notificationsModel.matchData = match!!
//        notificationsModel.matchData = data["matchData"]

        Log.e("Notification==>", notificationsModel.toString())
        return notificationsModel
    }

    //
    fun getRandomNumer(): Int {
        val r = Random
        return r.nextInt(80 - 65) + 65
    }

    //
    private fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            val componentInfo = taskInfo[0].topActivity
            if (componentInfo.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }
}
