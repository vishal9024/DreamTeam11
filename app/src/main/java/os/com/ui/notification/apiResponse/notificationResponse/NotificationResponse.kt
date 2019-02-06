package os.com.ui.notification.apiResponse.notificationResponse

import android.os.Parcel
import android.os.Parcelable

class NotificationResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationResponse> {
        override fun createFromParcel(parcel: Parcel): NotificationResponse {
            return NotificationResponse(parcel)
        }

        override fun newArray(size: Int): Array<NotificationResponse?> {
            return arrayOfNulls(size)
        }
    }

}
