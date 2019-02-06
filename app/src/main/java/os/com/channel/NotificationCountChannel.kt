package os.com.channel

import kotlinx.coroutines.channels.Channel


class NotificationCountChannel  {

    val notificationCountChannel = Channel<Int>()

    companion object {

        private var mInstance: NotificationCountChannel? = null

            fun getInstance() : NotificationCountChannel {
                if (mInstance == null) {
                    mInstance = NotificationCountChannel()
                }
                return mInstance as NotificationCountChannel
            }
    }
}