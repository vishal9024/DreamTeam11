package os.com.ui.createTeam.apiResponse.playerListResponse

import android.os.Parcel
import android.os.Parcelable

class GetPlayerListResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetPlayerListResponse> {
        override fun createFromParcel(parcel: Parcel): GetPlayerListResponse {
            return GetPlayerListResponse(parcel)
        }

        override fun newArray(size: Int): Array<GetPlayerListResponse?> {
            return arrayOfNulls(size)
        }
    }

}
