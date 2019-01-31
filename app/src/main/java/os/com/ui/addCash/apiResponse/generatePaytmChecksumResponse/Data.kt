package os.com.ui.addCash.apiResponse.generatePaytmChecksumResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var  checksum = ""

    constructor(parcel: Parcel) : this() {
        checksum = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(checksum)
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