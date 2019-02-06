package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;
import java.util.List;

public class OtherUserProfileResponse implements Serializable {

    /**
     * response : {"status":true,"message":null,"data":{"team_name":"Daredevl11","image":"https://72.octallabs.com/real11/uploads/users/user_154868231617.png","contest_level":2,"contest_finished":29,"total_match":18,"total_series":3,"series_wins":0,"recent_performance":[{"visitor_team_name":"Pakistan","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagpakistan.png","local_team_name":"South Africa","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagsouth_africa.png","match_date":"2019-01-30","match_comment":"South Africa won by 7 wickets (with 60 balls remaining)","my_points":"80","my_team":1,"friend_points":"60","friend_team":3},{"visitor_team_name":"Matabeleland Tuskers","visitor_team_flag":"","local_team_name":"Mashonaland Eagles","local_team_flag":"","match_date":"2019-01-26","match_comment":"","my_points":"5632","my_team":1,"friend_points":"5632","friend_team":1},{"visitor_team_name":"Adelaide Strikers","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagadelaide strikers.jpg","local_team_name":"Hobart Hurricanes","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flaghobart hurricanes.jpg","match_date":"2019-01-31","match_comment":"Adelaide Strikers won the toss and elected to field","my_points":"4","my_team":1,"friend_points":"4","friend_team":2},{"visitor_team_name":"Pakistan","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagpakistan.png","local_team_name":"South Africa","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagsouth_africa.png","match_date":"2019-01-25","match_comment":"","my_points":"12544","my_team":1,"friend_points":"6400","friend_team":1},{"visitor_team_name":"Sydney Thunder","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagsydney thunder.jpg","local_team_name":"Melbourne Renegades","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagmelbourne renegades.jpg","match_date":"2019-01-30","match_comment":"Melb Reneg won by 27 runs","my_points":"123","my_team":1,"friend_points":"41","friend_team":2},{"visitor_team_name":"Sylhet Sixers","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagsylhet_sixers.png","local_team_name":"Rangpur Riders","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagrangpur_riders.png","match_date":"2019-01-19","match_comment":"","my_points":"0","my_team":2,"friend_points":"0","friend_team":1},{"visitor_team_name":"India","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagindia.png","local_team_name":"Australia","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagaustralia.png","match_date":"2019-01-18","match_comment":"","my_points":"0","my_team":1,"friend_points":"0","friend_team":13},{"visitor_team_name":"India Women","visitor_team_flag":"","local_team_name":"New Zealand Women","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagnew_zealand_women.png","match_date":"2019-02-06","match_comment":"","my_points":"0","my_team":1,"friend_points":"0","friend_team":3},{"visitor_team_name":"India","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagindia.png","local_team_name":"New Zealand","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagnew_zealand.png","match_date":"2019-02-06","match_comment":"","my_points":"0","my_team":1,"friend_points":"0","friend_team":1}]}}
     */

    public ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean implements Serializable {
        /**
         * status : true
         * message : null
         * data : {"team_name":"Daredevl11","image":"https://72.octallabs.com/real11/uploads/users/user_154868231617.png","contest_level":2,"contest_finished":29,"total_match":18,"total_series":3,"series_wins":0,"recent_performance":[{"visitor_team_name":"Pakistan","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagpakistan.png","local_team_name":"South Africa","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagsouth_africa.png","match_date":"2019-01-30","match_comment":"South Africa won by 7 wickets (with 60 balls remaining)","my_points":"80","my_team":1,"friend_points":"60","friend_team":3},{"visitor_team_name":"Matabeleland Tuskers","visitor_team_flag":"","local_team_name":"Mashonaland Eagles","local_team_flag":"","match_date":"2019-01-26","match_comment":"","my_points":"5632","my_team":1,"friend_points":"5632","friend_team":1},{"visitor_team_name":"Adelaide Strikers","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagadelaide strikers.jpg","local_team_name":"Hobart Hurricanes","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flaghobart hurricanes.jpg","match_date":"2019-01-31","match_comment":"Adelaide Strikers won the toss and elected to field","my_points":"4","my_team":1,"friend_points":"4","friend_team":2},{"visitor_team_name":"Pakistan","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagpakistan.png","local_team_name":"South Africa","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagsouth_africa.png","match_date":"2019-01-25","match_comment":"","my_points":"12544","my_team":1,"friend_points":"6400","friend_team":1},{"visitor_team_name":"Sydney Thunder","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagsydney thunder.jpg","local_team_name":"Melbourne Renegades","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagmelbourne renegades.jpg","match_date":"2019-01-30","match_comment":"Melb Reneg won by 27 runs","my_points":"123","my_team":1,"friend_points":"41","friend_team":2},{"visitor_team_name":"Sylhet Sixers","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagsylhet_sixers.png","local_team_name":"Rangpur Riders","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagrangpur_riders.png","match_date":"2019-01-19","match_comment":"","my_points":"0","my_team":2,"friend_points":"0","friend_team":1},{"visitor_team_name":"India","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagindia.png","local_team_name":"Australia","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagaustralia.png","match_date":"2019-01-18","match_comment":"","my_points":"0","my_team":1,"friend_points":"0","friend_team":13},{"visitor_team_name":"India Women","visitor_team_flag":"","local_team_name":"New Zealand Women","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagnew_zealand_women.png","match_date":"2019-02-06","match_comment":"","my_points":"0","my_team":1,"friend_points":"0","friend_team":3},{"visitor_team_name":"India","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagindia.png","local_team_name":"New Zealand","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagnew_zealand.png","match_date":"2019-02-06","match_comment":"","my_points":"0","my_team":1,"friend_points":"0","friend_team":1}]}
         */

        public boolean status;
        public String message;
        public DataBean data;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean implements Serializable {
            /**
             * team_name : Daredevl11
             * image : https://72.octallabs.com/real11/uploads/users/user_154868231617.png
             * contest_level : 2
             * contest_finished : 29
             * total_match : 18
             * total_series : 3
             * series_wins : 0
             * recent_performance : [{"visitor_team_name":"Pakistan","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagpakistan.png","local_team_name":"South Africa","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagsouth_africa.png","match_date":"2019-01-30","match_comment":"South Africa won by 7 wickets (with 60 balls remaining)","my_points":"80","my_team":1,"friend_points":"60","friend_team":3},{"visitor_team_name":"Matabeleland Tuskers","visitor_team_flag":"","local_team_name":"Mashonaland Eagles","local_team_flag":"","match_date":"2019-01-26","match_comment":"","my_points":"5632","my_team":1,"friend_points":"5632","friend_team":1},{"visitor_team_name":"Adelaide Strikers","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagadelaide strikers.jpg","local_team_name":"Hobart Hurricanes","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flaghobart hurricanes.jpg","match_date":"2019-01-31","match_comment":"Adelaide Strikers won the toss and elected to field","my_points":"4","my_team":1,"friend_points":"4","friend_team":2},{"visitor_team_name":"Pakistan","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagpakistan.png","local_team_name":"South Africa","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagsouth_africa.png","match_date":"2019-01-25","match_comment":"","my_points":"12544","my_team":1,"friend_points":"6400","friend_team":1},{"visitor_team_name":"Sydney Thunder","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagsydney thunder.jpg","local_team_name":"Melbourne Renegades","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagmelbourne renegades.jpg","match_date":"2019-01-30","match_comment":"Melb Reneg won by 27 runs","my_points":"123","my_team":1,"friend_points":"41","friend_team":2},{"visitor_team_name":"Sylhet Sixers","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagsylhet_sixers.png","local_team_name":"Rangpur Riders","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagrangpur_riders.png","match_date":"2019-01-19","match_comment":"","my_points":"0","my_team":2,"friend_points":"0","friend_team":1},{"visitor_team_name":"India","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagindia.png","local_team_name":"Australia","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagaustralia.png","match_date":"2019-01-18","match_comment":"","my_points":"0","my_team":1,"friend_points":"0","friend_team":13},{"visitor_team_name":"India Women","visitor_team_flag":"","local_team_name":"New Zealand Women","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagnew_zealand_women.png","match_date":"2019-02-06","match_comment":"","my_points":"0","my_team":1,"friend_points":"0","friend_team":3},{"visitor_team_name":"India","visitor_team_flag":"https://72.octallabs.com/real11/uploads/team_flagindia.png","local_team_name":"New Zealand","local_team_flag":"https://72.octallabs.com/real11/uploads/team_flagnew_zealand.png","match_date":"2019-02-06","match_comment":"","my_points":"0","my_team":1,"friend_points":"0","friend_team":1}]
             */

            public String team_name;
            public String image;
            public int contest_level;
            public int contest_finished;
            public int total_match;
            public int total_series;
            public int series_wins;
            public List<RecentPerformanceBean> recent_performance;

            public String getTeam_name() {
                return team_name;
            }

            public void setTeam_name(String team_name) {
                this.team_name = team_name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getContest_level() {
                return contest_level;
            }

            public void setContest_level(int contest_level) {
                this.contest_level = contest_level;
            }

            public int getContest_finished() {
                return contest_finished;
            }

            public void setContest_finished(int contest_finished) {
                this.contest_finished = contest_finished;
            }

            public int getTotal_match() {
                return total_match;
            }

            public void setTotal_match(int total_match) {
                this.total_match = total_match;
            }

            public int getTotal_series() {
                return total_series;
            }

            public void setTotal_series(int total_series) {
                this.total_series = total_series;
            }

            public int getSeries_wins() {
                return series_wins;
            }

            public void setSeries_wins(int series_wins) {
                this.series_wins = series_wins;
            }

            public List<RecentPerformanceBean> getRecent_performance() {
                return recent_performance;
            }

            public void setRecent_performance(List<RecentPerformanceBean> recent_performance) {
                this.recent_performance = recent_performance;
            }

            public static class RecentPerformanceBean implements Serializable {
                /**
                 * visitor_team_name : Pakistan
                 * visitor_team_flag : https://72.octallabs.com/real11/uploads/team_flagpakistan.png
                 * local_team_name : South Africa
                 * local_team_flag : https://72.octallabs.com/real11/uploads/team_flagsouth_africa.png
                 * match_date : 2019-01-30
                 * match_comment : South Africa won by 7 wickets (with 60 balls remaining)
                 * my_points : 80
                 * my_team : 1
                 * friend_points : 60
                 * friend_team : 3
                 */

                public String visitor_team_name;
                public String visitor_team_flag;
                public String local_team_name;
                public String local_team_flag;
                public String match_date;
                public String match_comment;
                public String my_points;
                public int my_team;
                public String friend_points;
                public int friend_team;

                public String getVisitor_team_name() {
                    return visitor_team_name;
                }

                public void setVisitor_team_name(String visitor_team_name) {
                    this.visitor_team_name = visitor_team_name;
                }

                public String getVisitor_team_flag() {
                    return visitor_team_flag;
                }

                public void setVisitor_team_flag(String visitor_team_flag) {
                    this.visitor_team_flag = visitor_team_flag;
                }

                public String getLocal_team_name() {
                    return local_team_name;
                }

                public void setLocal_team_name(String local_team_name) {
                    this.local_team_name = local_team_name;
                }

                public String getLocal_team_flag() {
                    return local_team_flag;
                }

                public void setLocal_team_flag(String local_team_flag) {
                    this.local_team_flag = local_team_flag;
                }

                public String getMatch_date() {
                    return match_date;
                }

                public void setMatch_date(String match_date) {
                    this.match_date = match_date;
                }

                public String getMatch_comment() {
                    return match_comment;
                }

                public void setMatch_comment(String match_comment) {
                    this.match_comment = match_comment;
                }

                public String getMy_points() {
                    return my_points;
                }

                public void setMy_points(String my_points) {
                    this.my_points = my_points;
                }

                public int getMy_team() {
                    return my_team;
                }

                public void setMy_team(int my_team) {
                    this.my_team = my_team;
                }

                public String getFriend_points() {
                    return friend_points;
                }

                public void setFriend_points(String friend_points) {
                    this.friend_points = friend_points;
                }

                public int getFriend_team() {
                    return friend_team;
                }

                public void setFriend_team(int friend_team) {
                    this.friend_team = friend_team;
                }
            }
        }
    }
}
