package os.com.ui.createTeam.apiResponse.myTeamListResponse

import android.os.Parcel
import android.os.Parcelable
class PlayerRecord() :Parcelable {
    var name = ""
    var player_id = ""
    var image = ""
    var role = ""
    var credits = ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        player_id = parcel.readString()
        image = parcel.readString()
        role = parcel.readString()
        credits = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(player_id)
        parcel.writeString(image)
        parcel.writeString(role)
        parcel.writeString(credits)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayerRecord> {
        override fun createFromParcel(parcel: Parcel): PlayerRecord {
            return PlayerRecord(parcel)
        }

        override fun newArray(size: Int): Array<PlayerRecord?> {
            return arrayOfNulls(size)
        }
    }

}