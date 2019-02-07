package os.com.ui.contest.apiResponse.entryFeeResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var  entry_fee:String ?=null

    constructor(parcel: Parcel) : this() {
        entry_fee = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString((if (entry_fee == null) "" else entry_fee))
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