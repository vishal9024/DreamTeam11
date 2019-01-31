package os.com.ui.joinedContest.apiResponse.getSeriesPlayerListResponse

import android.os.Parcel
import android.os.Parcelable

class GetSeriesPlayerListResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetSeriesPlayerListResponse> {
        override fun createFromParcel(parcel: Parcel): GetSeriesPlayerListResponse {
            return GetSeriesPlayerListResponse(parcel)
        }

        override fun newArray(size: Int): Array<GetSeriesPlayerListResponse?> {
            return arrayOfNulls(size)
        }
    }

}
