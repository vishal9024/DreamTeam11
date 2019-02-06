package os.com.ui.invite.apiResponse;

import java.io.Serializable;
import java.util.List;

public class InviteFreindDetailResponse implements Serializable {
    /**
     * response : {"status":true,"message":null,"data":{"total_earnd":100,"to_be_earnd":100,"total_fields":2,"friend_detail":[{"image":"","team_name":"Nidhiiiii","received_amount":100,"total_amount":"100"},{"image":"","team_name":"USU7AK31J","received_amount":0,"total_amount":"100"}]}}
     */

    public ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean implements Serializable {
        /**
         * status : true
         * message : null
         * data : {"total_earnd":100,"to_be_earnd":100,"total_fields":2,"friend_detail":[{"image":"","team_name":"Nidhiiiii","received_amount":100,"total_amount":"100"},{"image":"","team_name":"USU7AK31J","received_amount":0,"total_amount":"100"}]}
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

        public static class DataBean implements Serializable {
            /**
             * total_earnd : 100
             * to_be_earnd : 100
             * total_fields : 2
             * friend_detail : [{"image":"","team_name":"Nidhiiiii","received_amount":100,"total_amount":"100"},{"image":"","team_name":"USU7AK31J","received_amount":0,"total_amount":"100"}]
             */

            public int total_earnd;
            public int to_be_earnd;
            public int total_fields;
            public List<FriendDetailBean> friend_detail;

            public int getTotal_earnd() {
                return total_earnd;
            }

            public void setTotal_earnd(int total_earnd) {
                this.total_earnd = total_earnd;
            }

            public int getTo_be_earnd() {
                return to_be_earnd;
            }

            public void setTo_be_earnd(int to_be_earnd) {
                this.to_be_earnd = to_be_earnd;
            }

            public int getTotal_fields() {
                return total_fields;
            }

            public void setTotal_fields(int total_fields) {
                this.total_fields = total_fields;
            }

            public List<FriendDetailBean> getFriend_detail() {
                return friend_detail;
            }

            public void setFriend_detail(List<FriendDetailBean> friend_detail) {
                this.friend_detail = friend_detail;
            }

            public static class FriendDetailBean implements Serializable {
                /**
                 * image : 
                 * team_name : Nidhiiiii
                 * received_amount : 100
                 * total_amount : 100
                 */

                public String image;
                public String team_name;
                public int received_amount;
                public String total_amount;

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public String getTeam_name() {
                    return team_name;
                }

                public void setTeam_name(String team_name) {
                    this.team_name = team_name;
                }

                public int getReceived_amount() {
                    return received_amount;
                }

                public void setReceived_amount(int received_amount) {
                    this.received_amount = received_amount;
                }

                public String getTotal_amount() {
                    return total_amount;
                }

                public void setTotal_amount(String total_amount) {
                    this.total_amount = total_amount;
                }
            }
        }
    }
}
