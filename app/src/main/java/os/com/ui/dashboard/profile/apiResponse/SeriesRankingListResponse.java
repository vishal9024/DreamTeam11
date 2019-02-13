package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;
import java.util.List;

public class SeriesRankingListResponse implements Serializable {


    /**
     * response : {"status":true,"message":null,"data":[{"points":127,"rank":2,"series_name":"Twenty20 Big Bash","series_id":1058,"previous_rank":2,"user_id":41,"team_name":"Huhuzzz","user_image":"https://72.octallabs.com/real11/uploads/users/user_154927158041.png"},{"points":405,"user_id":17,"series_id":1058,"rank":1,"previous_rank":1,"team_name":"Daredevl11","user_image":"https://72.octallabs.com/real11/uploads/users/user_154868231617.png"},{"points":4,"user_id":52,"series_id":1058,"rank":3,"previous_rank":3,"team_name":"g????????????????????","user_image":"https://72.octallabs.com/real11/uploads/users/user_1548844001.png"},{"points":4,"user_id":44,"series_id":1058,"rank":3,"previous_rank":3,"team_name":"USU7AK31J","user_image":""},{"points":-12,"user_id":16,"series_id":1058,"rank":5,"previous_rank":5,"team_name":"","user_image":""}]}
     */

    public ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean implements Serializable  {
        /**
         * status : true
         * message : null
         * data : [{"points":127,"rank":2,"series_name":"Twenty20 Big Bash","series_id":1058,"previous_rank":2,"user_id":41,"team_name":"Huhuzzz","user_image":"https://72.octallabs.com/real11/uploads/users/user_154927158041.png"},{"points":405,"user_id":17,"series_id":1058,"rank":1,"previous_rank":1,"team_name":"Daredevl11","user_image":"https://72.octallabs.com/real11/uploads/users/user_154868231617.png"},{"points":4,"user_id":52,"series_id":1058,"rank":3,"previous_rank":3,"team_name":"g????????????????????","user_image":"https://72.octallabs.com/real11/uploads/users/user_1548844001.png"},{"points":4,"user_id":44,"series_id":1058,"rank":3,"previous_rank":3,"team_name":"USU7AK31J","user_image":""},{"points":-12,"user_id":16,"series_id":1058,"rank":5,"previous_rank":5,"team_name":"","user_image":""}]
         */

        public boolean status;
        public String message;
        public List<DataBean> data;

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

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable {
            /**
             * points : 127
             * rank : 2
             * series_name : Twenty20 Big Bash
             * series_id : 1058
             * previous_rank : 2
             * user_id : 41
             * team_name : Huhuzzz
             * user_image : https://72.octallabs.com/real11/uploads/users/user_154927158041.png
             */

            public String points;
            public String rank;
            public String series_name;
            public int series_id;
            public String previous_rank;
            public int user_id;
            public String team_name;
            public String user_image;

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }

            public String getRank() {
                return rank;
            }

            public void setRank(String rank) {
                this.rank = rank;
            }

            public String getSeries_name() {
                return series_name;
            }

            public void setSeries_name(String series_name) {
                this.series_name = series_name;
            }

            public int getSeries_id() {
                return series_id;
            }

            public void setSeries_id(int series_id) {
                this.series_id = series_id;
            }

            public String getPrevious_rank() {
                return previous_rank;
            }

            public void setPrevious_rank(String previous_rank) {
                this.previous_rank = previous_rank;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getTeam_name() {
                return team_name;
            }

            public void setTeam_name(String team_name) {
                this.team_name = team_name;
            }

            public String getUser_image() {
                return user_image;
            }

            public void setUser_image(String user_image) {
                this.user_image = user_image;
            }
        }
    }
}
