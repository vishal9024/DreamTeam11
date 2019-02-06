package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;

public class IfscCodeReponse implements Serializable {

    /**
     * status : success
     * data : {"_id":"5a90ef23ec4a372ad2956e66","STATE":"RAJASTHAN","BANK":"HDFC BANK","IFSC":"HDFC0000644","BRANCH":"JAIPUR - TONK ROAD","ADDRESS":"A-1, LAL KOTHI, DISTRICT SHOPPING CENTRE, SHREE PLAZA, TONK ROAD JAIPUR RAJASTHAN 302015","CONTACT":"9875003333","CITY":"TONK","DISTRICT":"TONK","MICRCODE":"302240005"}
     * message :
     */

    private String status;
    private DataBean data;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * _id : 5a90ef23ec4a372ad2956e66
         * STATE : RAJASTHAN
         * BANK : HDFC BANK
         * IFSC : HDFC0000644
         * BRANCH : JAIPUR - TONK ROAD
         * ADDRESS : A-1, LAL KOTHI, DISTRICT SHOPPING CENTRE, SHREE PLAZA, TONK ROAD JAIPUR RAJASTHAN 302015
         * CONTACT : 9875003333
         * CITY : TONK
         * DISTRICT : TONK
         * MICRCODE : 302240005
         */

        private String _id;
        private String STATE;
        private String BANK;
        private String IFSC;
        private String BRANCH;
        private String ADDRESS;
        private String CONTACT;
        private String CITY;
        private String DISTRICT;
        private String MICRCODE;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getSTATE() {
            return STATE;
        }

        public void setSTATE(String STATE) {
            this.STATE = STATE;
        }

        public String getBANK() {
            return BANK;
        }

        public void setBANK(String BANK) {
            this.BANK = BANK;
        }

        public String getIFSC() {
            return IFSC;
        }

        public void setIFSC(String IFSC) {
            this.IFSC = IFSC;
        }

        public String getBRANCH() {
            return BRANCH;
        }

        public void setBRANCH(String BRANCH) {
            this.BRANCH = BRANCH;
        }

        public String getADDRESS() {
            return ADDRESS;
        }

        public void setADDRESS(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public String getCONTACT() {
            return CONTACT;
        }

        public void setCONTACT(String CONTACT) {
            this.CONTACT = CONTACT;
        }

        public String getCITY() {
            return CITY;
        }

        public void setCITY(String CITY) {
            this.CITY = CITY;
        }

        public String getDISTRICT() {
            return DISTRICT;
        }

        public void setDISTRICT(String DISTRICT) {
            this.DISTRICT = DISTRICT;
        }

        public String getMICRCODE() {
            return MICRCODE;
        }

        public void setMICRCODE(String MICRCODE) {
            this.MICRCODE = MICRCODE;
        }
    }
}
