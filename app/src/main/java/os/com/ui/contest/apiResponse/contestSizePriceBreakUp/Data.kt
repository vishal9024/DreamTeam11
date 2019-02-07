package os.com.ui.contest.apiResponse.contestSizePriceBreakUp

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var title: String ?=null
    var isSelected:Boolean= false
    var info: ArrayList<Info> ?=null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        isSelected = parcel.readByte() != 0.toByte()
        info=parcel.createTypedArrayList(Info)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeByte(if (isSelected) 1 else 0)
        info=parcel.createTypedArrayList(Info)
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