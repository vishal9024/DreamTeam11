package os.com.ui.joinedContest.apiResponse.DreamTeamResponse

import android.os.Parcel
import android.os.Parcelable

class DreamTeamResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DreamTeamResponse> {
        override fun createFromParcel(parcel: Parcel): DreamTeamResponse {
            return DreamTeamResponse(parcel)
        }

        override fun newArray(size: Int): Array<DreamTeamResponse?> {
            return arrayOfNulls(size)
        }
    }

}
