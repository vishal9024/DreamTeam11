package os.com.ui.contest.apiResponse.contestSizePriceBreakUp

import android.os.Parcel
import android.os.Parcelable

class Info() : Parcelable {
    var rank_size :String?=null
    var percent:String?=null

    constructor(parcel: Parcel) : this() {
        rank_size = parcel.readString()
        percent = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString((if (rank_size == null) "" else rank_size))
        parcel.writeString((if (percent == null) "" else percent))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Info> {
        override fun createFromParcel(parcel: Parcel): Info {
            return Info(parcel)
        }

        override fun newArray(size: Int): Array<Info?> {
            return arrayOfNulls(size)
        }
    }


}

object StringUtils {
    fun toNull(value: String?): String? {
        return if (value == null || value.isEmpty()) null else value
    }
}