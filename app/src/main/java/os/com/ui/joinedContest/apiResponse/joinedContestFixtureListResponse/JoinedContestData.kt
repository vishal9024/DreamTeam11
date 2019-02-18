package os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse

import android.os.Parcel
import android.os.Parcelable
import os.com.ui.winningBreakup.apiResponse.contestPriceBreakupResponse.PriceBreakUp
import java.util.*

class JoinedContestData() : Parcelable {
    var entry_fee: String = ""
    var prize_money: String = ""
    var total_teams: String = ""
    var category_id: String = ""
    var contest_id: String = ""
    var total_winners: String = ""
    var teams_joined: String = ""
    var invite_code: String = ""
    var is_joined = false
    var multiple_team = false
    var confirm_winning=false
    var my_team_ids: ArrayList<String>? = null
    var breakup_detail: ArrayList<PriceBreakUp>? = null
    var points_earned: String = ""
    var my_rank: String = ""
    var team_number: ArrayList<String>? = null

    constructor(parcel: Parcel) : this() {
        entry_fee = parcel.readString()
        prize_money = parcel.readString()
        total_teams = parcel.readString()
        category_id = parcel.readString()
        contest_id = parcel.readString()
        total_winners = parcel.readString()
        teams_joined = parcel.readString()
        invite_code = parcel.readString()
        is_joined = parcel.readByte() != 0.toByte()
        multiple_team = parcel.readByte() != 0.toByte()
        confirm_winning = parcel.readByte() != 0.toByte()
        my_team_ids = parcel.createStringArrayList()
        breakup_detail = parcel.createTypedArrayList(PriceBreakUp)
        points_earned = parcel.readString()
        my_rank = parcel.readString()
        team_number = parcel.createStringArrayList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(entry_fee)
        parcel.writeString(prize_money)
        parcel.writeString(total_teams)
        parcel.writeString(category_id)
        parcel.writeString(contest_id)
        parcel.writeString(total_winners)
        parcel.writeString(teams_joined)
        parcel.writeString(invite_code)
        parcel.writeByte(if (is_joined) 1 else 0)
        parcel.writeByte(if (multiple_team) 1 else 0)
        parcel.writeByte(if (confirm_winning) 1 else 0)
        parcel.writeStringList(my_team_ids)
        parcel.writeTypedList(breakup_detail)
        parcel.writeString(points_earned)
        parcel.writeString(my_rank)
        parcel.writeStringList(team_number)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JoinedContestData> {
        override fun createFromParcel(parcel: Parcel): JoinedContestData {
            return JoinedContestData(parcel)
        }

        override fun newArray(size: Int): Array<JoinedContestData?> {
            return arrayOfNulls(size)
        }
    }


}