package os.com.ui.signup.apiRequest

import android.os.Parcel
import android.os.Parcelable

class VerifyOtpRequest() : Parcelable {
    var otp: String = ""
    var device_id: String = ""
    var device_type: String = ""
    var language: String = ""
    var user_id: String = ""
    var is_signup: Boolean = false

    constructor(parcel: Parcel) : this() {
        otp = parcel.readString()
        device_id = parcel.readString()
        device_type = parcel.readString()
        language = parcel.readString()
        user_id = parcel.readString()
        is_signup = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(otp)
        parcel.writeString(device_id)
        parcel.writeString(device_type)
        parcel.writeString(language)
        parcel.writeString(user_id)
        parcel.writeByte(if (is_signup) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VerifyOtpRequest> {
        override fun createFromParcel(parcel: Parcel): VerifyOtpRequest {
            return VerifyOtpRequest(parcel)
        }

        override fun newArray(size: Int): Array<VerifyOtpRequest?> {
            return arrayOfNulls(size)
        }
    }


}