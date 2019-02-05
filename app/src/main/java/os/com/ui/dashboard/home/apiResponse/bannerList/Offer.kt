package os.com.ui.dashboard.home.apiResponse.bannerList

import android.os.Parcel
import android.os.Parcelable

class Offer() : Parcelable {
    var coupon_code_id = ""
    var user_id = ""
    var applied_on = ""
    var min_amount = ""
    var in_percentage = false
    var created = ""
    var discount_amount = ""
    var max_discount = ""
    var id = ""
    var coupon_id = ""

    constructor(parcel: Parcel) : this() {
        coupon_code_id = parcel.readString()
        user_id = parcel.readString()
        applied_on = parcel.readString()
        min_amount = parcel.readString()
        in_percentage = parcel.readByte() != 0.toByte()
        created = parcel.readString()
        discount_amount = parcel.readString()
        max_discount = parcel.readString()
        id = parcel.readString()
        coupon_id = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(coupon_code_id)
        parcel.writeString(user_id)
        parcel.writeString(applied_on)
        parcel.writeString(min_amount)
        parcel.writeByte(if (in_percentage) 1 else 0)
        parcel.writeString(created)
        parcel.writeString(discount_amount)
        parcel.writeString(max_discount)
        parcel.writeString(id)
        parcel.writeString(coupon_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Offer> {
        override fun createFromParcel(parcel: Parcel): Offer {
            return Offer(parcel)
        }

        override fun newArray(size: Int): Array<Offer?> {
            return arrayOfNulls(size)
        }
    }


}