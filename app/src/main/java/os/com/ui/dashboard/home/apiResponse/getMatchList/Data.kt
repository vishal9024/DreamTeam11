package os.com.ui.dashboard.home.apiResponse.getMatchList

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {

    var upcoming_match: List<Match>? = null
    var live_match: List<Match>? = null
    var completed_match: List<Match>? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Data(upcoming_match=$upcoming_match, live_match=$live_match, completed_match=$completed_match)"
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