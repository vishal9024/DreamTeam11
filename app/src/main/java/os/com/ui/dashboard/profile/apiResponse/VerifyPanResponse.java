package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;

public class VerifyPanResponse implements Serializable {

    /**
     * response : {"status":true,"message":"","data":{"mobile_no":"9782966743","email":"uttam.kumar@octalinfosolution.com","email_verify":true,"pen_verify":2,"bank_account_verify":0}}
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
         * data : {"mobile_no":"9782966743","email":"uttam.kumar@octalinfosolution.com","email_verify":true,"pen_verify":2,"bank_account_verify":0}
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
             * mobile_no : 9782966743
             * email : uttam.kumar@octalinfosolution.com
             * email_verify : true
             * pen_verify : 2
             * bank_account_verify : 0
             */

            private String mobile_no;
            private String email;
            private boolean email_verify;
            private int pen_verify;
            private int bank_account_verify;

            public String getMobile_no() {
                return mobile_no;
            }

            public void setMobile_no(String mobile_no) {
                this.mobile_no = mobile_no;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public boolean isEmail_verify() {
                return email_verify;
            }

            public void setEmail_verify(boolean email_verify) {
                this.email_verify = email_verify;
            }

            public int getPen_verify() {
                return pen_verify;
            }

            public void setPen_verify(int pen_verify) {
                this.pen_verify = pen_verify;
            }

            public int getBank_account_verify() {
                return bank_account_verify;
            }

            public void setBank_account_verify(int bank_account_verify) {
                this.bank_account_verify = bank_account_verify;
            }
        }
    }
}
