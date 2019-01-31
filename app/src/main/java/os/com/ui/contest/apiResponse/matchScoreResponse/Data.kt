package os.com.ui.contest.apiResponse.matchScoreResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var local_team_score: String = ""
    var match_started: Boolean = false
    var vistor_team_score: String = ""
    var comment: String = ""

    constructor(parcel: Parcel) : this() {
        local_team_score = parcel.readString()
        match_started = parcel.readByte() != 0.toByte()
        vistor_team_score = parcel.readString()
        comment = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(local_team_score)
        parcel.writeByte(if (match_started) 1 else 0)
        parcel.writeString(vistor_team_score)
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