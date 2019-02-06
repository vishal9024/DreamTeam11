package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;
import java.util.List;

public class TeamStatesResponse implements Serializable {


    /**
     * response : {"status":true,"message":null,"data":{"team_name":"Huhuzzz","image":"https://72.octallabs.com/real11/uploads/users/user_154927158041.png","series_name":"Twenty20 Big Bash","total_points":127,"totalRank":2,"point_detail":[{"local_team":"Hobart Hurricanes","visitor_team":"Adelaide Strikers","team_count":"T1","points":"4"},{"local_team":"Melbourne Renegades","visitor_team":"Sydney Thunder","team_count":"T1","points":"123"}]}}
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
         * data : {"team_name":"Huhuzzz","image":"https://72.octallabs.com/real11/uploads/users/user_154927158041.png","series_name":"Twenty20 Big Bash","total_points":127,"totalRank":2,"point_detail":[{"local_team":"Hobart Hurricanes","visitor_team":"Adelaide Strikers","team_count":"T1","points":"4"},{"local_team":"Melbourne Renegades","visitor_team":"Sydney Thunder","team_count":"T1","points":"123"}]}
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
             * team_name : Huhuzzz
             * image : https://72.octallabs.com/real11/uploads/users/user_154927158041.png
             * series_name : Twenty20 Big Bash
             * total_points : 127
             * totalRank : 2
             * point_detail : [{"local_team":"Hobart Hurricanes","visitor_team":"Adelaide Strikers","team_count":"T1","points":"4"},{"local_team":"Melbourne Renegades","visitor_team":"Sydney Thunder","team_count":"T1","points":"123"}]
             */

            public String team_name;
            public String image;
            public String series_name;
            public int total_points;
            public int totalRank;
            public List<PointDetailBean> point_detail;

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

            public String getSeries_name() {
                return series_name;
            }

            public void setSeries_name(String series_name) {
                this.series_name = series_name;
            }

            public int getTotal_points() {
                return total_points;
            }

            public void setTotal_points(int total_points) {
                this.total_points = total_points;
            }

            public int getTotalRank() {
                return totalRank;
            }

            public void setTotalRank(int totalRank) {
                this.totalRank = totalRank;
            }

            public List<PointDetailBean> getPoint_detail() {
                return point_detail;
            }

            public void setPoint_detail(List<PointDetailBean> point_detail) {
                this.point_detail = point_detail;
            }

            public static class PointDetailBean implements Serializable{
                /**
                 * local_team : Hobart Hurricanes
                 * visitor_team : Adelaide Strikers
                 * team_count : T1
                 * points : 4
                 */

                public String local_team;
                public String visitor_team;
                public String team_count;
                public String points;

                public String getLocal_team() {
                    return local_team;
                }

                public void setLocal_team(String local_team) {
                    this.local_team = local_team;
                }

                public String getVisitor_team() {
                    return visitor_team;
                }

                public void setVisitor_team(String visitor_team) {
                    this.visitor_team = visitor_team;
                }

                public String getTeam_count() {
                    return team_count;
                }

                public void setTeam_count(String team_count) {
                    this.team_count = team_count;
                }

                public String getPoints() {
                    return points;
                }

                public void setPoints(String points) {
                    this.points = points;
                }
            }
        }
    }
}
