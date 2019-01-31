package os.com.ui.invite.apiResponse.getContestInviteResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
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

    internal var prize_money = ""
    internal var contest_id = ""
    internal var entry_fee = ""
    internal var category_id = ""
    internal var total_teams = ""

    internal var my_teams_count = ""
    internal var teams_joined = ""
    internal var is_joined: Boolean? = false
    internal var multiple_team: Boolean? = false

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
        prize_money = parcel.readString()
        contest_id = parcel.readString()
        entry_fee = parcel.readString()
        category_id = parcel.readString()
        total_teams = parcel.readString()
        my_teams_count = parcel.readString()
        teams_joined = parcel.readString()
        is_joined = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        multiple_team = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
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
        parcel.writeString(prize_money)
        parcel.writeString(contest_id)
        parcel.writeString(entry_fee)
        parcel.writeString(category_id)
        parcel.writeString(total_teams)
        parcel.writeString(my_teams_count)
        parcel.writeString(teams_joined)
        parcel.writeValue(is_joined)
        parcel.writeValue(multiple_team)
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