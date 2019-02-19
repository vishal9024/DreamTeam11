package os.com.ui.createTeam.apiResponse;

import java.io.Serializable;
import java.util.List;

public class PlayerdetailResponse implements Serializable {


    /**
     * response : {"status":true,"message":null,"data":{"player_name":"Yasmin Daswani","player_image":"http://real11.com/uploads/player_image/yasmin_daswani.png","player_credit":"7","bats_type":"Right-hand bat","bowls_type":null,"nationality":"Hong Kong","birthday":"Sep 21, 1994","player_total_points":0,"match_detail":[{"Match":"China Women vs Thailand Women","date":"Feb 18, 2019","player_points":0,"selected_by":"0%"},{"Match":"Malaysia Women vs Kuwait Women","date":"Feb 18, 2019","player_points":0,"selected_by":"0%"},{"Match":"United Arab Emirates Women vs Hong Kong Women","date":"Feb 18, 2019","player_points":"5.5","selected_by":"0%"}]}}
     */

    public ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * status : true
         * message : null
         * data : {"player_name":"Yasmin Daswani","player_image":"http://real11.com/uploads/player_image/yasmin_daswani.png","player_credit":"7","bats_type":"Right-hand bat","bowls_type":null,"nationality":"Hong Kong","birthday":"Sep 21, 1994","player_total_points":0,"match_detail":[{"Match":"China Women vs Thailand Women","date":"Feb 18, 2019","player_points":0,"selected_by":"0%"},{"Match":"Malaysia Women vs Kuwait Women","date":"Feb 18, 2019","player_points":0,"selected_by":"0%"},{"Match":"United Arab Emirates Women vs Hong Kong Women","date":"Feb 18, 2019","player_points":"5.5","selected_by":"0%"}]}
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

        public static class DataBean {
            /**
             * player_name : Yasmin Daswani
             * player_image : http://real11.com/uploads/player_image/yasmin_daswani.png
             * player_credit : 7
             * bats_type : Right-hand bat
             * bowls_type : null
             * nationality : Hong Kong
             * birthday : Sep 21, 1994
             * player_total_points : 0
             * match_detail : [{"Match":"China Women vs Thailand Women","date":"Feb 18, 2019","player_points":0,"selected_by":"0%"},{"Match":"Malaysia Women vs Kuwait Women","date":"Feb 18, 2019","player_points":0,"selected_by":"0%"},{"Match":"United Arab Emirates Women vs Hong Kong Women","date":"Feb 18, 2019","player_points":"5.5","selected_by":"0%"}]
             */

            public String player_name;
            public String player_image;
            public String player_credit;
            public String bats_type;
            public String bowls_type;
            public String nationality;
            public String birthday;
            public String player_total_points;
            public List<MatchDetailBean> match_detail;

            public String getPlayer_name() {
                return player_name;
            }

            public void setPlayer_name(String player_name) {
                this.player_name = player_name;
            }

            public String getPlayer_image() {
                return player_image;
            }

            public void setPlayer_image(String player_image) {
                this.player_image = player_image;
            }

            public String getPlayer_credit() {
                return player_credit;
            }

            public void setPlayer_credit(String player_credit) {
                this.player_credit = player_credit;
            }

            public String getBats_type() {
                return bats_type;
            }

            public void setBats_type(String bats_type) {
                this.bats_type = bats_type;
            }

            public String getBowls_type() {
                return bowls_type;
            }

            public void setBowls_type(String bowls_type) {
                this.bowls_type = bowls_type;
            }

            public String getNationality() {
                return nationality;
            }

            public void setNationality(String nationality) {
                this.nationality = nationality;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getPlayer_total_points() {
                return player_total_points;
            }

            public void setPlayer_total_points(String player_total_points) {
                this.player_total_points = player_total_points;
            }

            public List<MatchDetailBean> getMatch_detail() {
                return match_detail;
            }

            public void setMatch_detail(List<MatchDetailBean> match_detail) {
                this.match_detail = match_detail;
            }

            public static class MatchDetailBean {
                /**
                 * Match : China Women vs Thailand Women
                 * date : Feb 18, 2019
                 * player_points : 0
                 * selected_by : 0%
                 */

                public String Match;
                public String date;
                public String player_points;
                public String selected_by;

                public String getMatch() {
                    return Match;
                }

                public void setMatch(String Match) {
                    this.Match = Match;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getPlayer_points() {
                    return player_points;
                }

                public void setPlayer_points(String player_points) {
                    this.player_points = player_points;
                }

                public String getSelected_by() {
                    return selected_by;
                }

                public void setSelected_by(String selected_by) {
                    this.selected_by = selected_by;
                }
            }
        }
    }
}
