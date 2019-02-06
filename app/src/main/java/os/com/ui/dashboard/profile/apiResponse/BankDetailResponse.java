package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;

public class BankDetailResponse implements Serializable {

    /**
     * response : {"status":true,"message":"","data":{"winning_amount":0,"bank_name":"PNB","account_no":"7852014452"}}
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * status : true
         * message :
         * data : {"winning_amount":0,"bank_name":"PNB","account_no":"7852014452"}
         */

        private boolean status;
        private String message;
        private DataBean data;

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
             * winning_amount : 0
             * bank_name : PNB
             * account_no : 7852014452
             */

            private int winning_amount;
            private String bank_name;
            private String account_no;

            public int getWinning_amount() {
                return winning_amount;
            }

            public void setWinning_amount(int winning_amount) {
                this.winning_amount = winning_amount;
            }

            public String getBank_name() {
                return bank_name;
            }

            public void setBank_name(String bank_name) {
                this.bank_name = bank_name;
            }

            public String getAccount_no() {
                return account_no;
            }

            public void setAccount_no(String account_no) {
                this.account_no = account_no;
            }
        }
    }
}
