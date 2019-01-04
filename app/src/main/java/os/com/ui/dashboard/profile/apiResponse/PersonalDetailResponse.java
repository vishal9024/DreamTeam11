package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;

public class PersonalDetailResponse implements Serializable {


    /**
     * response : {"status":true,"message":"Success.","data":{"name":" ","email":"saa@ff.ggg","dob":"","phone":"8209374343","address":"","city":"","state":"","country":"","pincode":""}}
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
         * data : {"name":" ","email":"saa@ff.ggg","dob":"","phone":"8209374343","address":"","city":"","state":"","country":"","pincode":""}
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
             * name :
             * email : saa@ff.ggg
             * dob :
             * phone : 8209374343
             * address :
             * city :
             * state :
             * country :
             * pincode :
             */
            public String name;
            public String email;
            public String dob;
            public String phone;
            public String address;
            public String city;
            public String state;
            public String country;
            public String pincode;
            public String team_name;
            public String gender;

            public String getTeam_name() {
                return team_name;
            }

            public void setTeam_name(String team_name) {
                this.team_name = team_name;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getDob() {
                return dob;
            }

            public void setDob(String dob) {
                this.dob = dob;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getPincode() {
                return pincode;
            }

            public void setPincode(String pincode) {
                this.pincode = pincode;
            }
        }
    }
}
