package os.com.ui.dashboard.home.apiResponse.bannerList

import android.os.Parcel
import android.os.Parcelable

class GetBannerResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetBannerResponse> {
        override fun createFromParcel(parcel: Parcel): GetBannerResponse {
            return GetBannerResponse(parcel)
        }

        override fun newArray(size: Int): Array<GetBannerResponse?> {
            return arrayOfNulls(size)
        }
    }

}
