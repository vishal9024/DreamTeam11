package os.com.ui.createTeam.apiResponse.myTeamListResponse

import android.os.Parcel
import android.os.Parcelable

class PlayerRecord() : Parcelable {
    var name :String?=null
    var player_id:String?=null
    var image :String?=null
    var role :String?=null
    var credits :String?=null
    var point :String?=null
    var points :String?=null
    var in_dream_team = false
    var is_local_team = false

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        player_id = parcel.readString()
        image = parcel.readString()
        role = parcel.readString()
        credits = parcel.readString()
        point = parcel.readString()
        points = parcel.readString()
        in_dream_team = parcel.readByte() != 0.toByte()
        is_local_team = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(player_id)
        parcel.writeString(image)
        parcel.writeString(role)
        parcel.writeString(credits)
        parcel.writeString(point)
        parcel.writeString(points)
        parcel.writeByte(if (in_dream_team) 1 else 0)
        parcel.writeByte(if (is_local_team) 1 else 0)
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