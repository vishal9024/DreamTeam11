package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;

public class VerifyBankAccountResponse implements Serializable {

    /**
     * response : {"status":true,"message":"Your Bank detail have been submitted.","data":{"branch":"MANSAROVAR JAIPUR","ifsc_code":"HDFC0000987","bank_name":"HDFC BANK","id":9,"created":"2019-02-05 17:48:36","bank_image":"154936911641.jpeg","user_id":"41","account_number":"546545646546546"}}
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
         * message : Your Bank detail have been submitted.
         * data : {"branch":"MANSAROVAR JAIPUR","ifsc_code":"HDFC0000987","bank_name":"HDFC BANK","id":9,"created":"2019-02-05 17:48:36","bank_image":"154936911641.jpeg","user_id":"41","account_number":"546545646546546"}
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
             * branch : MANSAROVAR JAIPUR
             * ifsc_code : HDFC0000987
             * bank_name : HDFC BANK
             * id : 9
             * created : 2019-02-05 17:48:36
             * bank_image : 154936911641.jpeg
             * user_id : 41
             * account_number : 546545646546546
             */

            private String branch;
            private String ifsc_code;
            private String bank_name;
            private int id;
            private String created;
            private String bank_image;
            private String user_id;
            private String account_number;

            public String getBranch() {
                return branch;
            }

            public void setBranch(String branch) {
                this.branch = branch;
            }

            public String getIfsc_code() {
                return ifsc_code;
            }

            public void setIfsc_code(String ifsc_code) {
                this.ifsc_code = ifsc_code;
            }

            public String getBank_name() {
                return bank_name;
            }

            public void setBank_name(String bank_name) {
                this.bank_name = bank_name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public String getBank_image() {
                return bank_image;
            }

            public void setBank_image(String bank_image) {
                this.bank_image = bank_image;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getAccount_number() {
                return account_number;
            }

            public void setAccount_number(String account_number) {
                this.account_number = account_number;
            }
        }
    }
}
