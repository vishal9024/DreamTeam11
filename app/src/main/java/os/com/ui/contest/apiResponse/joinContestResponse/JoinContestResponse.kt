package os.com.ui.contest.apiResponse.joinContestResponse

import android.os.Parcel
import android.os.Parcelable

class JoinContestResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JoinContestResponse> {
        override fun createFromParcel(parcel: Parcel): JoinContestResponse {
            return JoinContestResponse(parcel)
        }

        override fun newArray(size: Int): Array<JoinContestResponse?> {
            return arrayOfNulls(size)
        }
    }

}
