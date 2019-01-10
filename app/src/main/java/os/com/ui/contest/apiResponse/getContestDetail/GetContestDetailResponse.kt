package os.com.ui.contest.apiResponse.getContestDetail

import android.os.Parcel
import android.os.Parcelable

class GetContestDetailResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetContestDetailResponse> {
        override fun createFromParcel(parcel: Parcel): GetContestDetailResponse {
            return GetContestDetailResponse(parcel)
        }

        override fun newArray(size: Int): Array<GetContestDetailResponse?> {
            return arrayOfNulls(size)
        }
    }

}
