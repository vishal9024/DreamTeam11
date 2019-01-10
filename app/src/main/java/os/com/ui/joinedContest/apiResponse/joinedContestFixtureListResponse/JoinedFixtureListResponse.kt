package os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse

import android.os.Parcel
import android.os.Parcelable

class JoinedFixtureListResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JoinedFixtureListResponse> {
        override fun createFromParcel(parcel: Parcel): JoinedFixtureListResponse {
            return JoinedFixtureListResponse(parcel)
        }

        override fun newArray(size: Int): Array<JoinedFixtureListResponse?> {
            return arrayOfNulls(size)
        }
    }

}
