package os.com.ui.invite.apiResponse.getContestInviteResponse

import android.os.Parcel
import android.os.Parcelable

class GetContestInviteResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetContestInviteResponse> {
        override fun createFromParcel(parcel: Parcel): GetContestInviteResponse {
            return GetContestInviteResponse(parcel)
        }

        override fun newArray(size: Int): Array<GetContestInviteResponse?> {
            return arrayOfNulls(size)
        }
    }

}
