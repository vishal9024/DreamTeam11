package os.com.ui.contest.apiResponse.getContestList

import android.os.Parcel
import android.os.Parcelable
import os.com.ui.winningBreakup.apiResponse.contestPriceBreakupResponse.PriceBreakUp
import java.util.*

class Contest protected constructor(`in`: Parcel) : Parcelable {
    internal var entry_fee = ""
    internal var prize_money = ""
    internal var total_teams = ""
    internal var category_id = ""
    internal var contest_id = ""
    internal var total_winners = ""
    internal var teams_joined = ""
    internal var is_joined: Boolean? = false
    internal var multiple_team: Boolean? = false
    internal var invite_code = ""
    internal var breakup_detail: ArrayList<PriceBreakUp>

    init {
        entry_fee = `in`.readString()
        prize_money = `in`.readString()
        total_teams = `in`.readString()
        category_id = `in`.readString()
        contest_id = `in`.readString()
        total_winners = `in`.readString()
        teams_joined = `in`.readString()
        val tmpIs_joined = `in`.readByte()
        is_joined = if (tmpIs_joined.toInt() == 0) null else tmpIs_joined.toInt() == 1
        val tmpMultiple_team = `in`.readByte()
        multiple_team = if (tmpMultiple_team.toInt() == 0) null else tmpMultiple_team.toInt() == 1
        invite_code = `in`.readString()
        breakup_detail = `in`.createTypedArrayList(PriceBreakUp)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(entry_fee)
        dest.writeString(prize_money)
        dest.writeString(total_teams)
        dest.writeString(category_id)
        dest.writeString(contest_id)
        dest.writeString(total_winners)
        dest.writeString(teams_joined)
        dest.writeByte((if (is_joined == null) 0 else if (is_joined!!) 1 else 2).toByte())
        dest.writeByte((if (multiple_team == null) 0 else if (multiple_team!!) 1 else 2).toByte())
        dest.writeString(invite_code)
        dest.writeTypedList(breakup_detail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

      @JvmField val CREATOR: Parcelable.Creator<Contest> = object : Parcelable.Creator<Contest> {
            override fun createFromParcel(`in`: Parcel): Contest {
                return Contest(`in`)
            }

            override fun newArray(size: Int): Array<Contest?> {
                return arrayOfNulls(size)
            }
        }
    }
}
