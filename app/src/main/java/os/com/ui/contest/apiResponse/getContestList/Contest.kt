package os.com.ui.contest.apiResponse.getContestList

import android.os.Parcel
import android.os.Parcelable
import os.com.ui.winningBreakup.apiResponse.contestPriceBreakupResponse.PriceBreakUp

class Contest() : Parcelable {
    var entry_fee = ""
    var prize_money = ""
    var total_teams = ""
    var category_id = ""
    var contest_id = ""
    var total_winners = ""
    var teams_joined = ""
    var is_joined: Boolean = false
    var multiple_team = false
    var invite_code = ""
    var breakup_detail: ArrayList<PriceBreakUp>? = null

    constructor(parcel: Parcel) : this() {
        entry_fee = parcel.readString()
        prize_money = parcel.readString()
        total_teams = parcel.readString()
        category_id = parcel.readString()
        contest_id = parcel.readString()
        total_winners = parcel.readString()
        teams_joined = parcel.readString()
        is_joined = parcel.readByte() != 0.toByte()
        multiple_team = parcel.readByte() != 0.toByte()
        invite_code = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(entry_fee)
        parcel.writeString(prize_money)
        parcel.writeString(total_teams)
        parcel.writeString(category_id)
        parcel.writeString(contest_id)
        parcel.writeString(total_winners)
        parcel.writeString(teams_joined)
        parcel.writeByte(if (is_joined) 1 else 0)
        parcel.writeByte(if (multiple_team) 1 else 0)
        parcel.writeString(invite_code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contest> {
        override fun createFromParcel(parcel: Parcel): Contest {
            return Contest(parcel)
        }

        override fun newArray(size: Int): Array<Contest?> {
            return arrayOfNulls(size)
        }
    }


}