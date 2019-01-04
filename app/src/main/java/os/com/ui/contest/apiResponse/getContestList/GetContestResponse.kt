package os.com.ui.contest.apiResponse.getContestList

import android.os.Parcel
import android.os.Parcelable

class GetContestResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetContestResponse> {
        override fun createFromParcel(parcel: Parcel): GetContestResponse {
            return GetContestResponse(parcel)
        }

        override fun newArray(size: Int): Array<GetContestResponse?> {
            return arrayOfNulls(size)
        }
    }

}
