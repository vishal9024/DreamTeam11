package os.com.ui.contest.apiResponse.entryFeeResponse

import android.os.Parcel
import android.os.Parcelable

class EntryFeeResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntryFeeResponse> {
        override fun createFromParcel(parcel: Parcel): EntryFeeResponse {
            return EntryFeeResponse(parcel)
        }

        override fun newArray(size: Int): Array<EntryFeeResponse?> {
            return arrayOfNulls(size)
        }
    }

}
