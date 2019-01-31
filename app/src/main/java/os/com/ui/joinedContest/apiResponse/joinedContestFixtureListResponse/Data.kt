package os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse

import android.os.Parcel
import android.os.Parcelable
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match

class Data protected constructor(parcel: Parcel) : Parcelable {
    var joined_contest: ArrayList<JoinedContestData>? = null
    var upcoming_match: ArrayList<Match>? = null
    var my_team_count: String = ""

    init {
        joined_contest = parcel.createTypedArrayList(JoinedContestData)
        upcoming_match = parcel.createTypedArrayList(Match)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(joined_contest)
        parcel.writeTypedList(upcoming_match)
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