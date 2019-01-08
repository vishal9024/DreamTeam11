package os.com.ui.createTeam.apiResponse.myTeamListResponse

import android.os.Parcel
import android.os.Parcelable

class GetTeamListResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetTeamListResponse> {
        override fun createFromParcel(parcel: Parcel): GetTeamListResponse {
            return GetTeamListResponse(parcel)
        }

        override fun newArray(size: Int): Array<GetTeamListResponse?> {
            return arrayOfNulls(size)
        }
    }

}
