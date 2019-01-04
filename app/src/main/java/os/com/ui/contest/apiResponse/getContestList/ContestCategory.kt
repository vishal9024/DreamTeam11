package os.com.ui.contest.apiResponse.getContestList

import android.os.Parcel
import android.os.Parcelable

class ContestCategory() : Parcelable {
    var category_title = ""
    var category_desc = ""
    var category_image = ""
    var contests: ArrayList<Contest>? = null

    constructor(parcel: Parcel) : this() {
        category_title = parcel.readString()
        category_desc = parcel.readString()
        category_image = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category_title)
        parcel.writeString(category_desc)
        parcel.writeString(category_image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContestCategory> {
        override fun createFromParcel(parcel: Parcel): ContestCategory {
            return ContestCategory(parcel)
        }

        override fun newArray(size: Int): Array<ContestCategory?> {
            return arrayOfNulls(size)
        }
    }


}