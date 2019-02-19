package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;

public class VerifyPanResponse implements Serializable {


    /**
     * response : {"status":true,"message":"Your PAN details have been submitted.","data":{"pan_image":"1550554671130.jpg","user_id":"130","pan_name":"Pawan Tanwar","pan_card":"ASDFG1234A","date_of_birth":"19-11-1996","state":"Rajasthan","aadhar_card":"455548424448","created":"2019-02-19 11:07:51","id":22}}
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
         * message : Your PAN details have been submitted.
         * data : {"pan_image":"1550554671130.jpg","user_id":"130","pan_name":"Pawan Tanwar","pan_card":"ASDFG1234A","date_of_birth":"19-11-1996","state":"Rajasthan","aadhar_card":"455548424448","created":"2019-02-19 11:07:51","id":22}
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
             * pan_image : 1550554671130.jpg
             * user_id : 130
             * pan_name : Pawan Tanwar
             * pan_card : ASDFG1234A
             * date_of_birth : 19-11-1996
             * state : Rajasthan
             * aadhar_card : 455548424448
             * created : 2019-02-19 11:07:51
             * id : 22
             */

            public String pan_image;
            public String user_id;
            public String pan_name;
            public String pan_card;
            public String date_of_birth;
            public String state;
            public String aadhar_card;
            public String created;
            public int id;

            public String getPan_image() {
                return pan_image;
            }

            public void setPan_image(String pan_image) {
                this.pan_image = pan_image;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getPan_name() {
                return pan_name;
            }

            public void setPan_name(String pan_name) {
                this.pan_name = pan_name;
            }

            public String getPan_card() {
                return pan_card;
            }

            public void setPan_card(String pan_card) {
                this.pan_card = pan_card;
            }

            public String getDate_of_birth() {
                return date_of_birth;
            }

            public void setDate_of_birth(String date_of_birth) {
                this.date_of_birth = date_of_birth;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getAadhar_card() {
                return aadhar_card;
            }

            public void setAadhar_card(String aadhar_card) {
                this.aadhar_card = aadhar_card;
            }

            public String getCreated() {
                return created;
            }

            public void setCreated(String created) {
                this.created = created;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
