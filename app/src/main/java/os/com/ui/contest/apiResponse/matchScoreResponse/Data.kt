package os.com.ui.contest.apiResponse.matchScoreResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var localteam_score: String = ""
    var localteam_over: String = ""
    var visitorteam_score: String = ""
    var visitorteam_over: String = ""
    var comment: String = ""

    constructor(parcel: Parcel) : this() {
        localteam_score = parcel.readString()
        localteam_over = parcel.readString()
        visitorteam_score = parcel.readString()
        visitorteam_over = parcel.readString()
        comment = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(localteam_score)
        parcel.writeString(localteam_over)
        parcel.writeString(visitorteam_score)
        parcel.writeString(visitorteam_over)
        parcel.writeString(comment)
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