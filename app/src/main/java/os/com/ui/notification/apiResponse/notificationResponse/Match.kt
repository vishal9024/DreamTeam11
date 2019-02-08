package os.com.ui.notification.apiResponse.notificationResponse

import android.os.Parcel
import android.os.Parcelable
import os.com.ui.dashboard.home.apiResponse.bannerList.Match

class Match() : Parcelable {
    var match_id: String? = null
    var series_id: String? = null
    var contestId: String? = null
    var strDate: String? = null
    var strTime: String? = null
    var visitor_team_name:String?=null
    var local_team_name: String? = null
    var local_team_id:String?=null
    var visitor_team_id: String? = null

    constructor(parcel: Parcel) : this() {
        match_id = parcel.readString()
        series_id = parcel.readString()
        contestId = parcel.readString()
        strDate = parcel.readString()
        visitor_team_name = parcel.readString()
        strTime = parcel.readString()
        local_team_name = parcel.readString()
        local_team_id = parcel.readString()
        visitor_team_id = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString((if (match_id == null) "" else match_id))
        parcel.writeString((if (series_id == null) "" else series_id))
        parcel.writeString((if (contestId == null) "" else contestId))
        parcel.writeString((if (strDate == null) "" else strDate))
        parcel.writeString((if (visitor_team_name == null) "" else visitor_team_name))
        parcel.writeString((if (strTime == null) "" else strTime))
        parcel.writeString((if (local_team_name == null) "" else local_team_name))
        parcel.writeString((if (local_team_id == null) "" else local_team_id))
        parcel.writeString((if (visitor_team_id == null) "" else visitor_team_id))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Match> {
        override fun createFromParcel(parcel: Parcel): Match {
            return Match(parcel)
        }

        override fun newArray(size: Int): Array<Match?> {
            return arrayOfNulls(size)
        }
    }


}