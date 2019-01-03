package os.com.ui.dashboard.home.apiResponse.getMatchList

import android.os.Parcel
import android.os.Parcelable

class GetMatchResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetMatchResponse> {
        override fun createFromParcel(parcel: Parcel): GetMatchResponse {
            return GetMatchResponse(parcel)
        }

        override fun newArray(size: Int): Array<GetMatchResponse?> {
            return arrayOfNulls(size)
        }
    }

}
