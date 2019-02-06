package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;

public class UpdateAvtarResponse implements Serializable {


    /**
     * response : {"status":true,"message":"Avetar updated successfully.","data":{"id":41,"first_name":"","last_name":"Uttam","role_id":2,"email":"ukkkkkk@mailinator.com","phone":"9782966743","password":"$2y$10$JqhEWvTlQ59s3qqT5VAbPe.533J/P9LGlzRN.wvn.nENK7SWkuxge","team_name":"Huhuzzz","date_of_bith":"18-05-2004","gender":1,"country":"India","state":"Gujarat","city":"","postal_code":"","address":"","image":"user_154927132041.png","fb_id":"","google_id":"","refer_id":"6DRzVI3O23","otp":"","is_login":0,"last_login":null,"device_id":"","device_type":"","module_access":null,"current_password":null,"language":"en","cash_balance":"306411","winning_balance":"0","bonus_amount":"191.4","status":1,"is_updated":1,"email_verified":1,"verify_string":"","created":null,"modified":null,"full_name":" Uttam"}}
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
         * message : Avetar updated successfully.
         * data : {"id":41,"first_name":"","last_name":"Uttam","role_id":2,"email":"ukkkkkk@mailinator.com","phone":"9782966743","password":"$2y$10$JqhEWvTlQ59s3qqT5VAbPe.533J/P9LGlzRN.wvn.nENK7SWkuxge","team_name":"Huhuzzz","date_of_bith":"18-05-2004","gender":1,"country":"India","state":"Gujarat","city":"","postal_code":"","address":"","image":"user_154927132041.png","fb_id":"","google_id":"","refer_id":"6DRzVI3O23","otp":"","is_login":0,"last_login":null,"device_id":"","device_type":"","module_access":null,"current_password":null,"language":"en","cash_balance":"306411","winning_balance":"0","bonus_amount":"191.4","status":1,"is_updated":1,"email_verified":1,"verify_string":"","created":null,"modified":null,"full_name":" Uttam"}
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
             * id : 41
             * first_name : 
             * last_name : Uttam
             * role_id : 2
             * email : ukkkkkk@mailinator.com
             * phone : 9782966743
             * password : $2y$10$JqhEWvTlQ59s3qqT5VAbPe.533J/P9LGlzRN.wvn.nENK7SWkuxge
             * team_name : Huhuzzz
             * date_of_bith : 18-05-2004
             * gender : 1
             * country : India
             * state : Gujarat
             * city : 
             * postal_code : 
             * address : 
             * image : user_154927132041.png
             * fb_id : 
             * google_id : 
             * refer_id : 6DRzVI3O23
             * otp : 
             * is_login : 0
             * last_login : null
             * device_id : 
             * device_type : 
             * module_access : null
             * current_password : null
             * language : en
             * cash_balance : 306411
             * winning_balance : 0
             * bonus_amount : 191.4
             * status : 1
             * is_updated : 1
             * email_verified : 1
             * verify_string : 
             * created : null
             * modified : null
             * full_name :  Uttam
             */

            public int id;
            public String first_name;
            public String last_name;
            public int role_id;
            public String email;
            public String phone;
            public String password;
            public String team_name;
            public String date_of_bith;
            public int gender;
            public String country;
            public String state;
            public String city;
            public String postal_code;
            public String address;
            public String image;
            public String fb_id;
            public String google_id;
            public String refer_id;
            public String otp;
            public int is_login;
            public Object last_login;
            public String device_id;
            public String device_type;
            public Object module_access;
            public Object current_password;
            public String language;
            public String cash_balance;
            public String winning_balance;
            public String bonus_amount;
            public int status;
            public int is_updated;
            public int email_verified;
            public String verify_string;
            public Object created;
            public Object modified;
            public String full_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getFirst_name() {
                return first_name;
            }

            public void setFirst_name(String first_name) {
                this.first_name = first_name;
            }

            public String getLast_name() {
                return last_name;
            }

            public void setLast_name(String last_name) {
                this.last_name = last_name;
            }

            public int getRole_id() {
                return role_id;
            }

            public void setRole_id(int role_id) {
                this.role_id = role_id;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getTeam_name() {
                return team_name;
            }

            public void setTeam_name(String team_name) {
                this.team_name = team_name;
            }

            public String getDate_of_bith() {
                return date_of_bith;
            }

            public void setDate_of_bith(String date_of_bith) {
                this.date_of_bith = date_of_bith;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getPostal_code() {
                return postal_code;
            }

            public void setPostal_code(String postal_code) {
                this.postal_code = postal_code;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getFb_id() {
                return fb_id;
            }

            public void setFb_id(String fb_id) {
                this.fb_id = fb_id;
            }

            public String getGoogle_id() {
                return google_id;
            }

            public void setGoogle_id(String google_id) {
                this.google_id = google_id;
            }

            public String getRefer_id() {
                return refer_id;
            }

            public void setRefer_id(String refer_id) {
                this.refer_id = refer_id;
            }

            public String getOtp() {
                return otp;
            }

            public void setOtp(String otp) {
                this.otp = otp;
            }

            public int getIs_login() {
                return is_login;
            }

            public void setIs_login(int is_login) {
                this.is_login = is_login;
            }

            public Object getLast_login() {
                return last_login;
            }

            public void setLast_login(Object last_login) {
                this.last_login = last_login;
            }

            public String getDevice_id() {
                return device_id;
            }

            public void setDevice_id(String device_id) {
                this.device_id = device_id;
            }

            public String getDevice_type() {
                return device_type;
            }

            public void setDevice_type(String device_type) {
                this.device_type = device_type;
            }

            public Object getModule_access() {
                return module_access;
            }

            public void setModule_access(Object module_access) {
                this.module_access = module_access;
            }

            public Object getCurrent_password() {
                return current_password;
            }

            public void setCurrent_password(Object current_password) {
                this.current_password = current_password;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }

            public String getCash_balance() {
                return cash_balance;
            }

            public void setCash_balance(String cash_balance) {
                this.cash_balance = cash_balance;
            }

            public String getWinning_balance() {
                return winning_balance;
            }

            public void setWinning_balance(String winning_balance) {
                this.winning_balance = winning_balance;
            }

            public String getBonus_amount() {
                return bonus_amount;
            }

            public void setBonus_amount(String bonus_amount) {
                this.bonus_amount = bonus_amount;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getIs_updated() {
                return is_updated;
            }

            public void setIs_updated(int is_updated) {
                this.is_updated = is_updated;
            }

            public int getEmail_verified() {
                return email_verified;
            }

            public void setEmail_verified(int email_verified) {
                this.email_verified = email_verified;
            }

            public String getVerify_string() {
                return verify_string;
            }

            public void setVerify_string(String verify_string) {
                this.verify_string = verify_string;
            }

            public Object getCreated() {
                return created;
            }

            public void setCreated(Object created) {
                this.created = created;
            }

            public Object getModified() {
                return modified;
            }

            public void setModified(Object modified) {
                this.modified = modified;
            }

            public String getFull_name() {
                return full_name;
            }

            public void setFull_name(String full_name) {
                this.full_name = full_name;
            }
        }
    }
}
