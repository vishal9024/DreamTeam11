package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;
import java.util.List;

public class ProfileResponse implements Serializable {


    /**
     * response : {"status":true,"message":"Success.","data":{"team_name":"Huhuzzz","contest_level":2,"paid_contest_count":21,"total_cash_amount":"306411","total_winning_amount":"0","cash_bonus_amount":"191.4","invite_friend_code":"6DRzVI3O23","contest_finished":22,"total_match":9,"total_series":2,"series_wins":"0","team_name_updated":1,"image":"https://72.octallabs.com/real11/uploads/users/user_154841785241.png","refered_to_friend":[{"user_id":42,"team_name":"Nidhiiiii","image":""},{"user_id":44,"team_name":"USU7AK31J","image":""}],"gender":"Male","rewards":[],"referal_bonus":"100"}}
     */

    public ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean implements Serializable{
        /**
         * status : true
         * message : Success.
         * data : {"team_name":"Huhuzzz","contest_level":2,"paid_contest_count":21,"total_cash_amount":"306411","total_winning_amount":"0","cash_bonus_amount":"191.4","invite_friend_code":"6DRzVI3O23","contest_finished":22,"total_match":9,"total_series":2,"series_wins":"0","team_name_updated":1,"image":"https://72.octallabs.com/real11/uploads/users/user_154841785241.png","refered_to_friend":[{"user_id":42,"team_name":"Nidhiiiii","image":""},{"user_id":44,"team_name":"USU7AK31J","image":""}],"gender":"Male","rewards":[],"referal_bonus":"100"}
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

        public static class DataBean  implements Serializable{
            /**
             * team_name : Huhuzzz
             * contest_level : 2
             * paid_contest_count : 21
             * total_cash_amount : 306411
             * total_winning_amount : 0
             * cash_bonus_amount : 191.4
             * invite_friend_code : 6DRzVI3O23
             * contest_finished : 22
             * total_match : 9
             * total_series : 2
             * series_wins : 0
             * team_name_updated : 1
             * image : https://72.octallabs.com/real11/uploads/users/user_154841785241.png
             * refered_to_friend : [{"user_id":42,"team_name":"Nidhiiiii","image":""},{"user_id":44,"team_name":"USU7AK31J","image":""}]
             * gender : Male
             * rewards : []
             * referal_bonus : 100
             */

            public String team_name;
            public int contest_level;
            public int paid_contest_count;
            public String total_cash_amount;
            public String total_winning_amount;
            public String cash_bonus_amount;
            public String invite_friend_code;
            public int contest_finished;
            public int total_match;
            public int total_series;
            public String series_wins;
            public int team_name_updated;
            public String image;
            public String gender;
            public String referal_bonus;
            public List<ReferedToFriendBean> refered_to_friend;
            public List<?> rewards;

            public String getTeam_name() {
                return team_name;
            }

            public void setTeam_name(String team_name) {
                this.team_name = team_name;
            }

            public int getContest_level() {
                return contest_level;
            }

            public void setContest_level(int contest_level) {
                this.contest_level = contest_level;
            }

            public int getPaid_contest_count() {
                return paid_contest_count;
            }

            public void setPaid_contest_count(int paid_contest_count) {
                this.paid_contest_count = paid_contest_count;
            }

            public String getTotal_cash_amount() {
                return total_cash_amount;
            }

            public void setTotal_cash_amount(String total_cash_amount) {
                this.total_cash_amount = total_cash_amount;
            }

            public String getTotal_winning_amount() {
                return total_winning_amount;
            }

            public void setTotal_winning_amount(String total_winning_amount) {
                this.total_winning_amount = total_winning_amount;
            }

            public String getCash_bonus_amount() {
                return cash_bonus_amount;
            }

            public void setCash_bonus_amount(String cash_bonus_amount) {
                this.cash_bonus_amount = cash_bonus_amount;
            }

            public String getInvite_friend_code() {
                return invite_friend_code;
            }

            public void setInvite_friend_code(String invite_friend_code) {
                this.invite_friend_code = invite_friend_code;
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

            public String getSeries_wins() {
                return series_wins;
            }

            public void setSeries_wins(String series_wins) {
                this.series_wins = series_wins;
            }

            public int getTeam_name_updated() {
                return team_name_updated;
            }

            public void setTeam_name_updated(int team_name_updated) {
                this.team_name_updated = team_name_updated;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getReferal_bonus() {
                return referal_bonus;
            }

            public void setReferal_bonus(String referal_bonus) {
                this.referal_bonus = referal_bonus;
            }

            public List<ReferedToFriendBean> getRefered_to_friend() {
                return refered_to_friend;
            }

            public void setRefered_to_friend(List<ReferedToFriendBean> refered_to_friend) {
                this.refered_to_friend = refered_to_friend;
            }

            public List<?> getRewards() {
                return rewards;
            }

            public void setRewards(List<?> rewards) {
                this.rewards = rewards;
            }

            public static class ReferedToFriendBean implements Serializable{
                /**
                 * user_id : 42
                 * team_name : Nidhiiiii
                 * image :
                 */

                public int user_id;
                public String team_name;
                public String image;

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

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }
            }
        }
    }
}
