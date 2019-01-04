package os.com.ui.dashboard.home.apiResponse.getMatchList

import android.os.Parcel
import android.os.Parcelable

class Match() :Parcelable {
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
    }

    override fun toString(): String {
        return "Match(series_id='$series_id', match_id='$match_id', series_name='$series_name', local_team_id='$local_team_id', local_team_name='$local_team_name', local_team_flag='$local_team_flag', visitor_team_id='$visitor_team_id', visitor_team_name='$visitor_team_name', visitor_team_flag='$visitor_team_flag', star_date='$star_date', star_time='$star_time')"
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