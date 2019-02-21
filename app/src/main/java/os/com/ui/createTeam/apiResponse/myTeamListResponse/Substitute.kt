package os.com.ui.createTeam.apiResponse.myTeamListResponse

import android.os.Parcel
import android.os.Parcelable

class Substitute() : Parcelable {
    var name: String = ""
    var point: String = ""
    var points: String = ""
    var player_id: String = ""
    var image: String = ""
    var role: String = ""
    var credits: String = ""
    var is_local_team = false

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        point = parcel.readString()
        points = parcel.readString()
        player_id = parcel.readString()
        image = parcel.readString()
        role = parcel.readString()
        credits = parcel.readString()
        is_local_team = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(point)
        parcel.writeString(points)
        parcel.writeString(player_id)
        parcel.writeString(image)
        parcel.writeString(role)
        parcel.writeString(credits)
        parcel.writeByte(if (is_local_team) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Substitute> {
        override fun createFromParcel(parcel: Parcel): Substitute {
            return Substitute(parcel)
        }

        override fun newArray(size: Int): Array<Substitute?> {
            return arrayOfNulls(size)
        }
    }


}