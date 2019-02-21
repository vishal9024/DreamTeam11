package os.com.ui.signup.apiResponse.beforeJoinContest

import android.os.Parcel
import android.os.Parcelable

class BeforeJoinContestResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BeforeJoinContestResponse> {
        override fun createFromParcel(parcel: Parcel): BeforeJoinContestResponse {
            return BeforeJoinContestResponse(parcel)
        }

        override fun newArray(size: Int): Array<BeforeJoinContestResponse?> {
            return arrayOfNulls(size)
        }
    }

}
