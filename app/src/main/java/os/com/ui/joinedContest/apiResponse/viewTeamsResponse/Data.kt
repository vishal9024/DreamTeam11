package os.com.ui.joinedContest.apiResponse.viewTeamsResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var  url = ""

    constructor(parcel: Parcel) : this() {
        url = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
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