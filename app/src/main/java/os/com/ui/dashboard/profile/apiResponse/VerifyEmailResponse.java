package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;

public class VerifyEmailResponse implements Serializable {

    /**
     * response : {"status":true,"message":"We sent you to verify your email, Please click on the verification link in the mail","data":{}}
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
         * message : We sent you to verify your email, Please click on the verification link in the mail
         * data : {}
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
        }
    }
}
