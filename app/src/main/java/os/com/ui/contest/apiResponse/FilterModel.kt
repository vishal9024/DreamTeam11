package os.com.ui.contest.apiResponse

import android.os.Parcel
import android.os.Parcelable

class FilterModel() : Parcelable {
    var entry_1_100=false
    var entry_101_1000=false
    var entry_1001_5000=false
    var entry_5000_more=false

    var winning_1_10000=false
    var winning_10001_50000=false
    var winning_50001_1lac=false
    var winning_1lac_10lac=false
    var winning_10lac_25lac=false
    var winning_25lac_more=false

    var contest_confirmed=false
    var contest_multientry=false
    var contest_multiwinner=false

    var contestsize_2=false
    var contestsize_3_10=false
    var contestsize_11_20=false
    var contestsize_21_100=false
    var contestsize_101_1000=false
    var contestsize_1001_10000=false
    var contestsize_10001_50000=false
    var contestsize_50000_more=false

    constructor(parcel: Parcel) : this() {
        entry_1_100 = parcel.readByte() != 0.toByte()
        entry_101_1000 = parcel.readByte() != 0.toByte()
        entry_1001_5000 = parcel.readByte() != 0.toByte()
        entry_5000_more = parcel.readByte() != 0.toByte()
        winning_1_10000 = parcel.readByte() != 0.toByte()
        winning_10001_50000 = parcel.readByte() != 0.toByte()
        winning_50001_1lac = parcel.readByte() != 0.toByte()
        winning_1lac_10lac = parcel.readByte() != 0.toByte()
        winning_10lac_25lac = parcel.readByte() != 0.toByte()
        winning_25lac_more = parcel.readByte() != 0.toByte()
        contest_confirmed = parcel.readByte() != 0.toByte()
        contest_multientry = parcel.readByte() != 0.toByte()
        contest_multiwinner = parcel.readByte() != 0.toByte()
        contestsize_2 = parcel.readByte() != 0.toByte()
        contestsize_3_10 = parcel.readByte() != 0.toByte()
        contestsize_11_20 = parcel.readByte() != 0.toByte()
        contestsize_21_100 = parcel.readByte() != 0.toByte()
        contestsize_101_1000 = parcel.readByte() != 0.toByte()
        contestsize_1001_10000 = parcel.readByte() != 0.toByte()
        contestsize_10001_50000 = parcel.readByte() != 0.toByte()
        contestsize_50000_more = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (entry_1_100) 1 else 0)
        parcel.writeByte(if (entry_101_1000) 1 else 0)
        parcel.writeByte(if (entry_1001_5000) 1 else 0)
        parcel.writeByte(if (entry_5000_more) 1 else 0)
        parcel.writeByte(if (winning_1_10000) 1 else 0)
        parcel.writeByte(if (winning_10001_50000) 1 else 0)
        parcel.writeByte(if (winning_50001_1lac) 1 else 0)
        parcel.writeByte(if (winning_1lac_10lac) 1 else 0)
        parcel.writeByte(if (winning_10lac_25lac) 1 else 0)
        parcel.writeByte(if (winning_25lac_more) 1 else 0)
        parcel.writeByte(if (contest_confirmed) 1 else 0)
        parcel.writeByte(if (contest_multientry) 1 else 0)
        parcel.writeByte(if (contest_multiwinner) 1 else 0)
        parcel.writeByte(if (contestsize_2) 1 else 0)
        parcel.writeByte(if (contestsize_3_10) 1 else 0)
        parcel.writeByte(if (contestsize_11_20) 1 else 0)
        parcel.writeByte(if (contestsize_21_100) 1 else 0)
        parcel.writeByte(if (contestsize_101_1000) 1 else 0)
        parcel.writeByte(if (contestsize_1001_10000) 1 else 0)
        parcel.writeByte(if (contestsize_10001_50000) 1 else 0)
        parcel.writeByte(if (contestsize_50000_more) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FilterModel> {
        override fun createFromParcel(parcel: Parcel): FilterModel {
            return FilterModel(parcel)
        }

        override fun newArray(size: Int): Array<FilterModel?> {
            return arrayOfNulls(size)
        }
    }
}

