package os.com.ui.contest.apiResponse.getContestDetail

import android.os.Parcel
import android.os.Parcelable
import os.com.ui.winningBreakup.apiResponse.contestPriceBreakupResponse.PriceBreakUp

class Data() : Parcelable {
    var prize_money = ""
    var total_teams = ""
    var entry_fee = ""
    var invite_code = ""
    var join_multiple_teams = false
    var total_winners = ""
    var teams_joined = ""
    var is_joined = false
    var total_point = ""
    var my_team_ids: ArrayList<String>? = null
    var joined_team_list: ArrayList<Team>? = null
    var breakup_detail: ArrayList<PriceBreakUp>? = null
    var match_status:String=""

    constructor(parcel: Parcel) : this() {
        total_point = parcel.readString()
        prize_money = parcel.readString()
        total_teams = parcel.readString()
        entry_fee = parcel.readString()
        invite_code = parcel.readString()
        join_multiple_teams = parcel.readByte() != 0.toByte()
        total_winners = parcel.readString()
        teams_joined = parcel.readString()
        is_joined = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString((if (total_point == null) "" else total_point))
        parcel.writeString(prize_money)
        parcel.writeString(total_teams)
        parcel.writeString(entry_fee)
        parcel.writeString(invite_code)
        parcel.writeByte(if (join_multiple_teams) 1 else 0)
        parcel.writeString(total_winners)
        parcel.writeString(teams_joined)
        parcel.writeByte(if (is_joined) 1 else 0)
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