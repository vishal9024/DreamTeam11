package os.com.ui.contest.apiResponse.getContestDetail

import android.os.Parcel
import android.os.Parcelable

class Team() : Parcelable {
    var user_id = ""
    var team_name = ""
    var team_no = ""

    constructor(parcel: Parcel) : this() {
        user_id = parcel.readString()
        team_name = parcel.readString()
        team_no = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user_id)
        parcel.writeString(team_name)
        parcel.writeString(team_no)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Team> {
        override fun createFromParcel(parcel: Parcel): Team {
            return Team(parcel)
        }

        override fun newArray(size: Int): Array<Team?> {
            return arrayOfNulls(size)
        }
    }


}