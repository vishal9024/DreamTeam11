package os.com.ui.createTeam.apiResponse.createTeamResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var  team_id = ""

    constructor(parcel: Parcel) : this() {
        team_id = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(team_id)
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