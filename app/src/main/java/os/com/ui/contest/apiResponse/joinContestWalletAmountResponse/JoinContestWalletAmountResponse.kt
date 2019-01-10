package os.com.ui.contest.apiResponse.joinContestWalletAmountResponse

import android.os.Parcel
import android.os.Parcelable

class JoinContestWalletAmountResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JoinContestWalletAmountResponse> {
        override fun createFromParcel(parcel: Parcel): JoinContestWalletAmountResponse {
            return JoinContestWalletAmountResponse(parcel)
        }

        override fun newArray(size: Int): Array<JoinContestWalletAmountResponse?> {
            return arrayOfNulls(size)
        }
    }

}
