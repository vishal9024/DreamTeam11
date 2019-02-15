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
         * data : {"team_name":"Huhuzzz","
         * contest_level":2,
         * "paid_contest_count":21,
         * "total_cash_amount":"306411",
         * "total_winning_amount":"0",
         * "cash_bonus_amount":"191.4",
         * "invite_friend_code":"6DRzVI3O23",
         * "contest_finished":22,
         * "total_match":9,
         * "total_series":2,
         * "series_wins":"0",
         * "team_name_updated":1,
         * "image":"https://72.octallabs.com/real11/uploads/users/user_154841785241.png",
         * "refered_to_friend":[{"user_id":42,"team_name":"Nidhiiiii","image":""},
         * {"user_id":44,"team_name":"USU7AK31J","image":""}],
         * "gender":"Male",
         * "rewards":[],
         * "referal_bonus":"100"}
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
            public String contest_level;
            public String paid_contest_count;
            public String total_cash_amount;
            public String total_winning_amount;
            public String cash_bonus_amount;
            public String invite_friend_code;
            public String contest_finished;
            public String total_match;
            public String total_series;
            public String series_wins;
            public String team_name_updated;
            public String image;
            public String gender;
            public String account_verified;
            public String referal_bonus;
            public List<ReferedToFriendBean> refered_to_friend;
            public List<RewardsBean> rewards;
            public List<SeriesRanksBean> series_ranks;

            public String getAccount_verified() {
                return account_verified;
            }

            public void setAccount_verified(String account_verified) {
                this.account_verified = account_verified;
            }

            public String getTeam_name() {
                return team_name;
            }

            public void setTeam_name(String team_name) {
                this.team_name = team_name;
            }

            public String getContest_level() {
                return contest_level;
            }

            public void setContest_level(String contest_level) {
                this.contest_level = contest_level;
            }

            public String getPaid_contest_count() {
                return paid_contest_count;
            }

            public void setPaid_contest_count(String paid_contest_count) {
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

            public String getContest_finished() {
                return contest_finished;
            }

            public void setContest_finished(String contest_finished) {
                this.contest_finished = contest_finished;
            }

            public String getTotal_match() {
                return total_match;
            }

            public void setTotal_match(String total_match) {
                this.total_match = total_match;
            }

            public String getTotal_series() {
                return total_series;
            }

            public void setTotal_series(String total_series) {
                this.total_series = total_series;
            }

            public String getSeries_wins() {
                return series_wins;
            }

            public void setSeries_wins(String series_wins) {
                this.series_wins = series_wins;
            }

            public String getTeam_name_updated() {
                return team_name_updated;
            }

            public void setTeam_name_updated(String team_name_updated) {
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

            public List<RewardsBean> getRewards() {
                return rewards;
            }

            public void setRewards(List<RewardsBean> rewards) {
                this.rewards = rewards;
            }

            public List<SeriesRanksBean> getSeries_ranks() {
                return series_ranks;
            }

            public void setSeries_ranks(List<SeriesRanksBean> series_ranks) {
                this.series_ranks = series_ranks;
            }

            public static class ReferedToFriendBean implements Serializable {
                /**
                 * user_id : 42
                 * team_name : Nidhiiiii
                 * image :
                 */

                public String user_id;
                public String team_name;
                public String image;

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
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

            public static class RewardsBean implements Serializable {
                /**
                 * date : 2019-02-04
                 * amount : 10
                 */

                public String date;
                public String amount;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getAmount() {
                    return amount;
                }

                public void setAmount(String amount) {
                    this.amount = amount;
                }
            }

            public static class SeriesRanksBean implements Serializable {
                /**
                 * points : 0
                 * rank : 0
                 * series_name : Australia in India
                 * series_id : 1003
                 * previous_rank : 0
                 */

                public String points;
                public String rank;
                public String series_name;
                public String series_id;
                public String previous_rank;

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

                public String getSeries_id() {
                    return series_id;
                }

                public void setSeries_id(String series_id) {
                    this.series_id = series_id;
                }

                public String getPrevious_rank() {
                    return previous_rank;
                }

                public void setPrevious_rank(String previous_rank) {
                    this.previous_rank = previous_rank;
                }
            }
        }
    }
}
