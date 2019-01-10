package os.com.ui.contest.apiResponse.joinContestWalletAmountResponse

import android.os.Parcel
import android.os.Parcelable

class Data() : Parcelable {
    var cash_balance: String = ""
    var winning_balance: String = ""
    var usable_bonus: String = ""
    var entry_fee: String = ""

    constructor(parcel: Parcel) : this() {
        cash_balance = parcel.readString()
        winning_balance = parcel.readString()
        usable_bonus = parcel.readString()
        entry_fee = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cash_balance)
        parcel.writeString(winning_balance)
        parcel.writeString(usable_bonus)
        parcel.writeString(entry_fee)
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