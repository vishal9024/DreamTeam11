package os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
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