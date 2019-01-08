package os.com.ui.createTeam.apiResponse.myTeamListResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var captain_player_id: String = ""
    var vice_captain_player_id: String = ""
    var total_bowler: String = ""
    var total_batsman: String = ""
    var total_wicketkeeper: String = ""
    var total_allrounder: String = ""
    var player_details: ArrayList<PlayerRecord>? = null

    constructor(parcel: Parcel) : this() {
        captain_player_id = parcel.readString()
        vice_captain_player_id = parcel.readString()
        total_bowler = parcel.readString()
        total_batsman = parcel.readString()
        total_wicketkeeper = parcel.readString()
        total_allrounder = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(captain_player_id)
        parcel.writeString(vice_captain_player_id)
        parcel.writeString(total_bowler)
        parcel.writeString(total_batsman)
        parcel.writeString(total_wicketkeeper)
        parcel.writeString(total_allrounder)
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