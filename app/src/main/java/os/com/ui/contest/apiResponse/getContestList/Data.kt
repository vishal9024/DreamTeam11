package os.com.ui.contest.apiResponse.getContestList

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var match_contest: ArrayList<ContestCategory>? = null
    var my_teams = ""
    var my_contests = ""

    constructor(parcel: Parcel) : this() {
        match_contest = parcel.createTypedArrayList(ContestCategory)
        my_teams = parcel.readString()
        my_contests = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(match_contest)
        parcel.writeString(my_teams)
        parcel.writeString(my_contests)
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