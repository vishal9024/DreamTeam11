package os.com.ui.dashboard.home.apiResponse.getMatchList

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {

    var upcoming_match: List<Match>? = null
    var live_match: List<Match>? = null
    var completed_match: List<Match>? = null
    var server_time: String? = null

    constructor(parcel: Parcel) : this() {
        upcoming_match = parcel.createTypedArrayList(Match)
        live_match = parcel.createTypedArrayList(Match)
        completed_match = parcel.createTypedArrayList(Match)
        server_time = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(upcoming_match)
        parcel.writeTypedList(live_match)
        parcel.writeTypedList(completed_match)
        parcel.writeString(if (server_time == null) "" else server_time)
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