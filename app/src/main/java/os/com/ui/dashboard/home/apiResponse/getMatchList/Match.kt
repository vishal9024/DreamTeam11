package os.com.ui.dashboard.home.apiResponse.getMatchList

class Match {
    var series_id = ""
    var match_id = ""
    var series_name = ""
    var local_team_id = ""
    var local_team_name = ""
    var local_team_flag = ""
    var visitor_team_id = ""
    var visitor_team_name = ""
    var visitor_team_flag = ""
    var star_date = ""
    var star_time = ""

    override fun toString(): String {
        return "Match(series_id='$series_id', match_id='$match_id', series_name='$series_name', local_team_id='$local_team_id', local_team_name='$local_team_name', local_team_flag='$local_team_flag', visitor_team_id='$visitor_team_id', visitor_team_name='$visitor_team_name', visitor_team_flag='$visitor_team_flag', star_date='$star_date', star_time='$star_time')"
    }


}