package os.com.ui.createTeam.apiResponse.createTeamResponse

import android.os.Parcel
import android.os.Parcelable

class CreateTeamResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreateTeamResponse> {
        override fun createFromParcel(parcel: Parcel): CreateTeamResponse {
            return CreateTeamResponse(parcel)
        }

        override fun newArray(size: Int): Array<CreateTeamResponse?> {
            return arrayOfNulls(size)
        }
    }

}
