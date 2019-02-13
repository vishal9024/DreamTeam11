package os.com.firebase

import android.os.Parcel
import android.os.Parcelable
import os.com.ui.notification.apiResponse.notificationResponse.Match

class PNModel() : Parcelable  {
    var type:String?=null
    var body:String?= null
    var title:String?= null
    var badge_count:String?= null
    var matchData :Match?= null

    constructor(parcel: Parcel) : this() {
        type = parcel.readString()
        body = parcel.readString()
        title = parcel.readString()
        badge_count = parcel.readString()
        matchData = parcel.readParcelable(Match::class.java.classLoader)
    }


//    private var matchData: MatchDataBean? = null
//
//    fun getMatchData(): MatchDataBean? {
//        return matchData
//    }
//
//    fun setMatchData(matchData: MatchDataBean) {
//        this.matchData = matchData
//    }
//
//    class MatchDataBean :Serializable{
//        /**
//         * contestId : 16
//         * strTime : 21:30
//         * visitor_team_name : Pakistan
//         * match_id : 13071965818
//         * visitor_team_id : 2387
//         * strDate : 2019-02-06T00:00:00
//         * local_team_id : 2432
//         * series_id : 1023
//         * local_team_name : South Africa
//         */
//
//        var contestId: Int = 0
//        var strTime: String? = null
//        var visitor_team_name: String? = null
//        var match_id: String? = null
//        var visitor_team_id: String? = null
//        var strDate: String? = null
//        var local_team_id: String? = null
//        var series_id: Int = 0
//        var local_team_name: String? = null
//    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(body)
        parcel.writeString(title)
        parcel.writeString(badge_count)
        parcel.writeParcelable(matchData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PNModel> {
        override fun createFromParcel(parcel: Parcel): PNModel {
            return PNModel(parcel)
        }

        override fun newArray(size: Int): Array<PNModel?> {
            return arrayOfNulls(size)
        }
    }
}