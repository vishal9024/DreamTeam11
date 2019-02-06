package os.com.ui.notification.apiResponse.notificationResponse

import android.os.Parcel
import android.os.Parcelable

class Response() : Parcelable {
    var status: Boolean = false
    var message: String? = null
    var data: ArrayList<Data>? = null

    constructor(parcel: Parcel) : this() {
        status = parcel.readByte() != 0.toByte()
        message = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (status) 1 else 0)
        parcel.writeString((if (message == null) "" else message))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Response> {
        override fun createFromParcel(parcel: Parcel): Response {
            return Response(parcel)
        }

        override fun newArray(size: Int): Array<Response?> {
            return arrayOfNulls(size)
        }
    }


}