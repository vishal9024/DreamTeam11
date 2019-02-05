package os.com.ui.dashboard.profile.apiResponse.ApplyCouponCodeResponse

import android.os.Parcel
import android.os.Parcelable

class ApplyCouponCodeResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApplyCouponCodeResponse> {
        override fun createFromParcel(parcel: Parcel): ApplyCouponCodeResponse {
            return ApplyCouponCodeResponse(parcel)
        }

        override fun newArray(size: Int): Array<ApplyCouponCodeResponse?> {
            return arrayOfNulls(size)
        }
    }

}
