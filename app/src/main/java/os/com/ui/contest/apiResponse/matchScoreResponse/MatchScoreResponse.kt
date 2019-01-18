package os.com.ui.contest.apiResponse.matchScoreResponse

import android.os.Parcel
import android.os.Parcelable

class MatchScoreResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MatchScoreResponse> {
        override fun createFromParcel(parcel: Parcel): MatchScoreResponse {
            return MatchScoreResponse(parcel)
        }

        override fun newArray(size: Int): Array<MatchScoreResponse?> {
            return arrayOfNulls(size)
        }
    }

}
