package os.com.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by heena on 23/8/17.
 */
class SocialModel() : Parcelable {
    internal var image: String = ""
    internal var first_name: String = ""
    internal var last_name: String = ""
    internal var username: String = ""
    internal var fb_id: String = ""
    internal var google_id: String = ""
    internal var email_address: String = ""
    internal var birthday: String = ""

    constructor(parcel: Parcel) : this() {
        image = parcel.readString()
        first_name = parcel.readString()
        last_name = parcel.readString()
        username = parcel.readString()
        fb_id = parcel.readString()
        google_id = parcel.readString()
        email_address = parcel.readString()
        birthday = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(first_name)
        parcel.writeString(last_name)
        parcel.writeString(username)
        parcel.writeString(fb_id)
        parcel.writeString(google_id)
        parcel.writeString(email_address)
        parcel.writeString(birthday)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SocialModel> {
        override fun createFromParcel(parcel: Parcel): SocialModel {
            return SocialModel(parcel)
        }

        override fun newArray(size: Int): Array<SocialModel?> {
            return arrayOfNulls(size)
        }
    }


}