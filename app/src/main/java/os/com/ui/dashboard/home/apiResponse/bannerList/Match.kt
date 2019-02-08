package os.com.ui.dashboard.home.apiResponse.bannerList

import android.os.Parcel
import android.os.Parcelable

class Match() : Parcelable {
    var series_id = ""
    var match_id = ""
    var series_name = ""
    var local_team_id = ""
    var local_team_name = ""
    var local_team_flag = ""
    var visitor_team_id = ""
    var visitor_team_name = ""
    var visitor_team_flag = ""
    var star_date = ""
    var star_time = ""
    var total_contest = ""
    var guru_url = ""
    constructor(parcel: Parcel) : this() {
        series_id = parcel.readString()
        match_id = parcel.readString()
        series_name = parcel.readString()
        local_team_id = parcel.readString()
        local_team_name = parcel.readString()
        local_team_flag = parcel.readString()
        visitor_team_id = parcel.readString()
        visitor_team_name = parcel.readString()
        visitor_team_flag = parcel.readString()
        star_date = parcel.readString()
        star_time = parcel.readString()
        total_contest = parcel.readString()
        guru_url = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(series_id)
        parcel.writeString(match_id)
        parcel.writeString(series_name)
        parcel.writeString(local_team_id)
        parcel.writeString(local_team_name)
        parcel.writeString(local_team_flag)
        parcel.writeString(visitor_team_id)
        parcel.writeString(visitor_team_name)
        parcel.writeString(visitor_team_flag)
        parcel.writeString(star_date)
        parcel.writeString(star_time)
        parcel.writeString(total_contest)
        parcel.writeString(guru_url)
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