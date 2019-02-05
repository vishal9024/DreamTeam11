package os.com.ui.dashboard.home.apiResponse.bannerList

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var image: String? = null
    var type: String? = null
    var offer: Offer? = null
    var upcoming: Match? = null

    constructor(parcel: Parcel) : this() {
        image = parcel.readString()
        type = parcel.readString()
        offer = parcel.readParcelable(Offer::class.java.classLoader)
        upcoming = parcel.readParcelable(Match::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(type)
        parcel.writeParcelable(offer, flags)
        parcel.writeParcelable(upcoming, flags)
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