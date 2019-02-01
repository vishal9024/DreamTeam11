package os.com.ui.joinedContest.apiResponse.getSeriesPlayerListResponse

import android.os.Parcel
import android.os.Parcelable

class PriceBreakUpData() :Parcelable {
    var actual: String = ""
    var points: String = ""

    constructor(parcel: Parcel) : this() {
        actual = parcel.readString()
        points = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(actual)
        parcel.writeString(points)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "PriceBreakUp(rank='$actual', price='$points')"
    }

    companion object CREATOR : Parcelable.Creator<PriceBreakUpData> {
        override fun createFromParcel(parcel: Parcel): PriceBreakUpData {
            return PriceBreakUpData(parcel)
        }

        override fun newArray(size: Int): Array<PriceBreakUpData?> {
            return arrayOfNulls(size)
        }
    }

}