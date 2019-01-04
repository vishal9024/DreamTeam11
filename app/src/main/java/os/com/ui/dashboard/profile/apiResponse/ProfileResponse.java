package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;

public class ProfileResponse implements Serializable {

    /**
     * response : {"status":true,"message":"Success.","data":{"team_name":"","total_cash_amount":"0","total_winning_amount":"0","cash_bonus_amount":"0","invite_friend_code":"JxueDIVR1h","series_wins":"0","gender":1}}
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
         * data : {"team_name":"","total_cash_amount":"0","total_winning_amount":"0","cash_bonus_amount":"0","invite_friend_code":"JxueDIVR1h","series_wins":"0","gender":1}
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

        public static class DataBean implements Serializable{
            /**
             * team_name : 
             * total_cash_amount : 0
             * total_winning_amount : 0
             * cash_bonus_amount : 0
             * invite_friend_code : JxueDIVR1h
             * series_wins : 0
             * gender : 1
             */

            public String team_name;
            public String total_cash_amount;
            public String total_winning_amount;
            public String cash_bonus_amount;
            public String invite_friend_code;
            public String series_wins;
            public String gender;

            public String getTeam_name() {
                return team_name;
            }

            public void setTeam_name(String team_name) {
                this.team_name = team_name;
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

            public String getSeries_wins() {
                return series_wins;
            }

            public void setSeries_wins(String series_wins) {
                this.series_wins = series_wins;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }
        }
    }
}
