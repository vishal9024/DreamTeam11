package os.com.ui.signup.apiResponse.beforeJoinContest

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var run: String = ""
    var catch: String = ""
    var wicket: String = ""

    constructor(parcel: Parcel) : this() {
        run = parcel.readString()
        catch = parcel.readString()
        wicket = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(run)
        parcel.writeString(catch)
        parcel.writeString(wicket)
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