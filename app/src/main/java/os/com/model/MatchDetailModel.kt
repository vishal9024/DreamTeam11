package os.com.model

import android.os.Parcel
import android.os.Parcelable
import os.com.ui.createTeam.apiResponse.playerListResponse.Data

class MatchDetailModel() : Parcelable {

    var type:Int?= null
    var playerList: ArrayList<Data>?=null

    constructor(parcel: Parcel) : this() {
        type = parcel.readValue(Int::class.java.classLoader) as? Int
        playerList = parcel.createTypedArrayList(Data)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(type)
        parcel.writeTypedList(playerList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MatchDetailModel> {
        override fun createFromParcel(parcel: Parcel): MatchDetailModel {
            return MatchDetailModel(parcel)
        }

        override fun newArray(size: Int): Array<MatchDetailModel?> {
            return arrayOfNulls(size)
        }
    }

}