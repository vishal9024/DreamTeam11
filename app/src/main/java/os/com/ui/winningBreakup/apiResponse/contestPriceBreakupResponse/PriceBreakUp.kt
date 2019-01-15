package os.com.ui.winningBreakup.apiResponse.contestPriceBreakupResponse

import android.os.Parcel
import android.os.Parcelable

class PriceBreakUp() :Parcelable {
    var rank: String = ""
    var price: String = ""

    constructor(parcel: Parcel) : this() {
        rank = parcel.readString()
        price = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(rank)
        parcel.writeString(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "PriceBreakUp(rank='$rank', price='$price')"
    }

    companion object CREATOR : Parcelable.Creator<PriceBreakUp> {
        override fun createFromParcel(parcel: Parcel): PriceBreakUp {
            return PriceBreakUp(parcel)
        }

        override fun newArray(size: Int): Array<PriceBreakUp?> {
            return arrayOfNulls(size)
        }
    }

}