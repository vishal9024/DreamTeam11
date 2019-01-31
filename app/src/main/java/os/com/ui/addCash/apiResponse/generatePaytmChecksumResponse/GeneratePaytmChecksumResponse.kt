package os.com.ui.addCash.apiResponse.generatePaytmChecksumResponse

import android.os.Parcel
import android.os.Parcelable

class GeneratePaytmChecksumResponse() : Parcelable {
    var response: Response? = null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GeneratePaytmChecksumResponse> {
        override fun createFromParcel(parcel: Parcel): GeneratePaytmChecksumResponse {
            return GeneratePaytmChecksumResponse(parcel)
        }

        override fun newArray(size: Int): Array<GeneratePaytmChecksumResponse?> {
            return arrayOfNulls(size)
        }
    }

}
