package os.com.ui.joinedContest.apiResponse.DreamTeamResponse

import android.os.Parcel
import android.os.Parcelable

class Substitute() : Parcelable {
    var name: String = ""
    var point: String = ""
    var player_id: String = ""
    var image: String = ""
    var role: String = ""
    var credits: String = ""

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        point = parcel.readString()
        player_id = parcel.readString()
        image = parcel.readString()
        role = parcel.readString()
        credits = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(point)
        parcel.writeString(player_id)
        parcel.writeString(image)
        parcel.writeString(role)
        parcel.writeString(credits)
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