package os.com.ui.createTeam.apiResponse.myTeamListResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var isSelected: Boolean = false
    var isJOINED: Boolean = false
    var teamid: String = ""
    var captain_player_id: String = ""
    var vice_captain_player_id: String = ""
    var total_bowler: String = ""
    var total_batsman: String = ""
    var total_wicketkeeper: String = ""
    var total_allrounder: String = ""
    var player_details: ArrayList<PlayerRecord>? = null
   var substitute_detail: Substitute?=null

    constructor(parcel: Parcel) : this() {
        isSelected = parcel.readByte() != 0.toByte()
        isJOINED = parcel.readByte() != 0.toByte()
        teamid = parcel.readString()
        captain_player_id = parcel.readString()
        vice_captain_player_id = parcel.readString()
        total_bowler = parcel.readString()
        total_batsman = parcel.readString()
        total_wicketkeeper = parcel.readString()
        total_allrounder = parcel.readString()
        substitute_detail = parcel.readParcelable(Substitute::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeByte(if (isJOINED) 1 else 0)
        parcel.writeString(teamid)
        parcel.writeString(captain_player_id)
        parcel.writeString(vice_captain_player_id)
        parcel.writeString(total_bowler)
        parcel.writeString(total_batsman)
        parcel.writeString(total_wicketkeeper)
        parcel.writeString(total_allrounder)
        parcel.writeParcelable(substitute_detail, flags)
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