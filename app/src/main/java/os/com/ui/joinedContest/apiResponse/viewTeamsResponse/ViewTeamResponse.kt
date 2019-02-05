package os.com.ui.joinedContest.apiResponse.viewTeamsResponse

import android.os.Parcel
import android.os.Parcelable

class ViewTeamResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ViewTeamResponse> {
        override fun createFromParcel(parcel: Parcel): ViewTeamResponse {
            return ViewTeamResponse(parcel)
        }

        override fun newArray(size: Int): Array<ViewTeamResponse?> {
            return arrayOfNulls(size)
        }
    }

}
