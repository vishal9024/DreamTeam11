package os.com.firebase

import java.io.Serializable

class PNModel: Serializable  {
    var type:String?=null
    var body:String?= null
    var title:String?= null
    var badge_count:String?= null
    var matchData :String?= null



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
}