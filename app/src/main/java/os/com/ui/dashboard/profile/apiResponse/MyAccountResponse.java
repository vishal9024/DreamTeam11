package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;

public class MyAccountResponse implements Serializable {

    /**
     * response : {"status":true,"message":"","data":{"bonus":"43","deposit_amount":"306274.4","winngs_amount":"0","total_balance":306317.4}}
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
         * data : {"bonus":"43","deposit_amount":"306274.4","winngs_amount":"0","total_balance":306317.4}
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
             * bonus : 43
             * deposit_amount : 306274.4
             * winngs_amount : 0
             * total_balance : 306317.4
             */

            private String bonus;
            private String deposit_amount;
            private String winngs_amount;
            private double total_balance;
private boolean account_verified;
            public String getBonus() {
                return bonus;
            }

            public void setBonus(String bonus) {
                this.bonus = bonus;
            }

            public String getDeposit_amount() {
                return deposit_amount;
            }

            public void setDeposit_amount(String deposit_amount) {
                this.deposit_amount = deposit_amount;
            }

            public String getWinngs_amount() {
                return winngs_amount;
            }

            public void setWinngs_amount(String winngs_amount) {
                this.winngs_amount = winngs_amount;
            }

            public double getTotal_balance() {
                return total_balance;
            }

            public boolean isAccount_verified() {
                return account_verified;
            }

            public void setAccount_verified(boolean account_verified) {
                this.account_verified = account_verified;
            }

            public void setTotal_balance(double total_balance) {
                this.total_balance = total_balance;
            }
        }
    }
}
