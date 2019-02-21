package os.com.ui.joinedContest.apiResponse.getSeriesPlayerListResponse

import android.os.Parcel
import android.os.Parcelable

class PriceBreakUp() : Parcelable {
    var starting11: PriceBreakUpData? = null
    var runs: PriceBreakUpData? = null
    var fours: PriceBreakUpData? = null
    var sixes: PriceBreakUpData? = null
    var strike_rate: PriceBreakUpData? = null
    var half_century: PriceBreakUpData? = null
    var duck: PriceBreakUpData? = null
    var wickets: PriceBreakUpData? = null
    var maiden_over: PriceBreakUpData? = null
    var eco_rate: PriceBreakUpData? = null
    var bonus: PriceBreakUpData? = null
    var catch: PriceBreakUpData? = null
    var run_outStumping: PriceBreakUpData? = null
    var total_point: PriceBreakUpData? = null

    constructor(parcel: Parcel) : this() {
        starting11 = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        runs = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        fours = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        sixes = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        strike_rate = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        half_century = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        duck = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        wickets = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        maiden_over = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        eco_rate = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        bonus = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        catch = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        run_outStumping = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
        total_point = parcel.readParcelable(PriceBreakUpData::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(starting11, flags)
        parcel.writeParcelable(runs, flags)
        parcel.writeParcelable(fours, flags)
        parcel.writeParcelable(sixes, flags)
        parcel.writeParcelable(strike_rate, flags)
        parcel.writeParcelable(half_century, flags)
        parcel.writeParcelable(duck, flags)
        parcel.writeParcelable(wickets, flags)
        parcel.writeParcelable(maiden_over, flags)
        parcel.writeParcelable(eco_rate, flags)
        parcel.writeParcelable(bonus, flags)
        parcel.writeParcelable(catch, flags)
        parcel.writeParcelable(run_outStumping, flags)
        parcel.writeParcelable(total_point, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PriceBreakUp> {
        override fun createFromParcel(parcel: Parcel): PriceBreakUp {
            return PriceBreakUp(parcel)
        }

        override fun newArray(size: Int): Array<PriceBreakUp?> {
            return arrayOfNulls(size)
        }
    }


}