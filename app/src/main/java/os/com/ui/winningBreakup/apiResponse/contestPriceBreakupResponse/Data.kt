package os.com.ui.winningBreakup.apiResponse.contestPriceBreakupResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var winning_price: String = ""
    var breakup_detail: ArrayList<PriceBreakUp>? = null

    constructor(parcel: Parcel) : this() {
        winning_price = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(winning_price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }

}