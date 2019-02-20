package os.com.ui.joinedContest.apiResponse.DreamTeamResponse

import android.os.Parcel
import android.os.Parcelable

class PlayerRecord() : Parcelable {
    var name :String?=StringUtils.toNull("")
    var player_id:String?=StringUtils.toNull("")
    var image :String?=StringUtils.toNull("")
    var role :String?=StringUtils.toNull("")
    var credit :String?=StringUtils.toNull("")
    var point :String?=StringUtils.toNull("")
    var is_local_team = false
    var in_dream_team = false

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        player_id = parcel.readString()
        image = parcel.readString()
        role = parcel.readString()
        credit = parcel.readString()
        point = parcel.readString()
        is_local_team = parcel.readByte() != 0.toByte()
        in_dream_team = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString((if (name == null) "" else name))
        parcel.writeString((if (player_id == null) "" else player_id))
        parcel.writeString((if (image == null) "" else image))
        parcel.writeString((if (role == null) "" else role))
        parcel.writeString((if (credit == null) "" else credit))
        parcel.writeString((if (point == null) "" else point))
        parcel.writeByte(if (is_local_team) 1 else 0)
        parcel.writeByte(if (in_dream_team) 1 else 0)
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

object StringUtils {
    fun toNull(value: String?): String? {
        return if (value == null || value.isEmpty()) null else value
    }
}