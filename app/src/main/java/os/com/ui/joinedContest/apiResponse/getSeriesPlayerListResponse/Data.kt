package os.com.ui.joinedContest.apiResponse.getSeriesPlayerListResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var player_id: String = ""
    var player_name: String = ""
    var player_image: String = ""
    var selection_percent: String = ""
    var points: String = ""
    var in_contest: Boolean = false

    constructor(parcel: Parcel) : this() {
        player_id = parcel.readString()
        player_name = parcel.readString()
        player_image = parcel.readString()
        selection_percent = parcel.readString()
        points = parcel.readString()
        in_contest = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(player_id)
        parcel.writeString(player_name)
        parcel.writeString(player_image)
        parcel.writeString(selection_percent)
        parcel.writeString(points)
        parcel.writeByte(if (in_contest) 1 else 0)
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