package os.com.ui.contest.apiResponse.contestSizePriceBreakUp

import android.os.Parcel
import android.os.Parcelable

class ContestSizePriceBreakup() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContestSizePriceBreakup> {
        override fun createFromParcel(parcel: Parcel): ContestSizePriceBreakup {
            return ContestSizePriceBreakup(parcel)
        }

        override fun newArray(size: Int): Array<ContestSizePriceBreakup?> {
            return arrayOfNulls(size)
        }
    }

}
