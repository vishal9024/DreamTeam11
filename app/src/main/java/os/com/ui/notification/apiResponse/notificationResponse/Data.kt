package os.com.ui.notification.apiResponse.notificationResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var id: String? = null
    var user_id: String? = null
    var nitification_type: String? = null
    var notification: String? = null
    var date: String? = null
    var title:String?=null
    var status: String? = null
    var match_data: Match? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        user_id = parcel.readString()
        nitification_type = parcel.readString()
        notification = parcel.readString()
        title = parcel.readString()
        date = parcel.readString()
        status = parcel.readString()
        match_data = parcel.readParcelable(Match::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString((if (id == null) "" else id))
        parcel.writeString((if (user_id == null) "" else user_id))
        parcel.writeString((if (nitification_type == null) "" else nitification_type))
        parcel.writeString((if (notification == null) "" else notification))
        parcel.writeString((if (title == null) "" else title))
        parcel.writeString((if (date == null) "" else date))
        parcel.writeString((if (status == null) "" else status))
        parcel.writeParcelable(match_data, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }


}