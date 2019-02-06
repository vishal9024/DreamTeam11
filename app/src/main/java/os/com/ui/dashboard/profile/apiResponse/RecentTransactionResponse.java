package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;
import java.util.List;

public class RecentTransactionResponse implements Serializable {

    /**
     * response : {"status":true,"message":"Transactions History","data":[{"date":"2019-01-24","info":[{"amount":"+ 599","txn_type":"","transaction_id":"","txn_date":"24 January,06:45:00 pm","team_name":"Huhuzzz"}]},{"date":"2019-01-25","info":[{"amount":"+ 599","txn_type":"Deposited Cash","transaction_id":"DD201901241548338646","txn_date":"25 January,12:15:00 am","team_name":"Huhuzzz"},{"amount":"+ 599","txn_type":"Deposited Cash","transaction_id":"DD201901241548338716","txn_date":"25 January,01:14:00 am","team_name":"Huhuzzz"},{"amount":"+ 100","txn_type":"Deposited Cash","transaction_id":"DD201901251548391664","txn_date":"25 January,03:42:00 pm","team_name":"Huhuzzz"},{"amount":"+ 100","txn_type":"Deposited Cash","transaction_id":"DD201901251548395803","txn_date":"25 January,04:37:00 pm","team_name":"Huhuzzz"},{"amount":"+ 100","txn_type":"Deposited Cash","transaction_id":"DD201901251548395834","txn_date":"25 January,04:37:00 pm","team_name":"Huhuzzz"},{"amount":"+ 599","txn_type":"Deposited Cash","transaction_id":"DD201901251548396117","txn_date":"25 January,05:56:00 pm","team_name":"Huhuzzz"},{"amount":"+ 100","txn_type":"Deposited Cash","transaction_id":"DD201901251548400731","txn_date":"25 January,06:48:00 pm","team_name":"Huhuzzz"},{"amount":"+ 100000","txn_type":"Deposited Cash","transaction_id":"DD201901251548412830","txn_date":"25 January,09:28:00 pm","team_name":"Huhuzzz"},{"amount":"+ 100000","txn_type":"Deposited Cash","transaction_id":"DD201901251548413002","txn_date":"25 January,09:20:00 pm","team_name":"Huhuzzz"},{"amount":"+ 100000","txn_type":"Deposited Cash","transaction_id":"DD201901251548417738","txn_date":"25 January,11:15:00 pm","team_name":"Huhuzzz"}]},{"date":"2019-01-28","info":[{"amount":"+ 200","txn_type":"Deposited Cash","transaction_id":"DD201901281548675393","txn_date":"28 January,10:30:00 pm","team_name":"Huhuzzz"}]},{"date":"2019-01-30","info":[{"amount":"- 60","txn_type":"Joined A Contest","transaction_id":"JL20190130154882926241","txn_date":"30 January,11:02:00 am","team_name":"Huhuzzz"},{"amount":"- 60","txn_type":"Joined A Contest","transaction_id":"JL20190130154882976441","txn_date":"30 January,11:24:00 am","team_name":"Huhuzzz"},{"amount":"- 326","txn_type":"Joined A Contest","transaction_id":"JL20190130154883776241","txn_date":"30 January,02:42:00 pm","team_name":"Huhuzzz"},{"amount":"- 652","txn_type":"Joined A Contest","transaction_id":"JL20190130154884529041","txn_date":"30 January,04:10:00 pm","team_name":"Huhuzzz"},{"amount":"- 80","txn_type":"Joined A Contest","transaction_id":"JL20190130154884533941","txn_date":"30 January,04:59:00 pm","team_name":"Huhuzzz"}]},{"date":"2019-01-31","info":[{"amount":"+ 200","txn_type":"Deposited Cash","transaction_id":"DD20190131154892784941","txn_date":"31 January,03:14:00 pm","team_name":"Huhuzzz"},{"amount":"+ 599","txn_type":"Deposited Cash","transaction_id":"DD20190131154892800541","txn_date":"31 January,03:14:00 pm","team_name":"Huhuzzz"},{"amount":"+ 599","txn_type":"Deposited Cash","transaction_id":"DD20190131154892825141","txn_date":"31 January,03:19:00 pm","team_name":"Huhuzzz"},{"amount":"- 60","txn_type":"Joined A Contest","transaction_id":"JL20190131154893010641","txn_date":"31 January,03:46:00 pm","team_name":"Huhuzzz"},{"amount":"- 60","txn_type":"Joined A Contest","transaction_id":"JL20190131154893014141","txn_date":"31 January,03:21:00 pm","team_name":"Huhuzzz"},{"amount":"- 60","txn_type":"Joined A Contest","transaction_id":"JL20190131154893017341","txn_date":"31 January,03:53:00 pm","team_name":"Huhuzzz"}]}]}
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
         * message : Transactions History
         * data : [{"date":"2019-01-24","info":[{"amount":"+ 599","txn_type":"","transaction_id":"","txn_date":"24 January,06:45:00 pm","team_name":"Huhuzzz"}]},{"date":"2019-01-25","info":[{"amount":"+ 599","txn_type":"Deposited Cash","transaction_id":"DD201901241548338646","txn_date":"25 January,12:15:00 am","team_name":"Huhuzzz"},{"amount":"+ 599","txn_type":"Deposited Cash","transaction_id":"DD201901241548338716","txn_date":"25 January,01:14:00 am","team_name":"Huhuzzz"},{"amount":"+ 100","txn_type":"Deposited Cash","transaction_id":"DD201901251548391664","txn_date":"25 January,03:42:00 pm","team_name":"Huhuzzz"},{"amount":"+ 100","txn_type":"Deposited Cash","transaction_id":"DD201901251548395803","txn_date":"25 January,04:37:00 pm","team_name":"Huhuzzz"},{"amount":"+ 100","txn_type":"Deposited Cash","transaction_id":"DD201901251548395834","txn_date":"25 January,04:37:00 pm","team_name":"Huhuzzz"},{"amount":"+ 599","txn_type":"Deposited Cash","transaction_id":"DD201901251548396117","txn_date":"25 January,05:56:00 pm","team_name":"Huhuzzz"},{"amount":"+ 100","txn_type":"Deposited Cash","transaction_id":"DD201901251548400731","txn_date":"25 January,06:48:00 pm","team_name":"Huhuzzz"},{"amount":"+ 100000","txn_type":"Deposited Cash","transaction_id":"DD201901251548412830","txn_date":"25 January,09:28:00 pm","team_name":"Huhuzzz"},{"amount":"+ 100000","txn_type":"Deposited Cash","transaction_id":"DD201901251548413002","txn_date":"25 January,09:20:00 pm","team_name":"Huhuzzz"},{"amount":"+ 100000","txn_type":"Deposited Cash","transaction_id":"DD201901251548417738","txn_date":"25 January,11:15:00 pm","team_name":"Huhuzzz"}]},{"date":"2019-01-28","info":[{"amount":"+ 200","txn_type":"Deposited Cash","transaction_id":"DD201901281548675393","txn_date":"28 January,10:30:00 pm","team_name":"Huhuzzz"}]},{"date":"2019-01-30","info":[{"amount":"- 60","txn_type":"Joined A Contest","transaction_id":"JL20190130154882926241","txn_date":"30 January,11:02:00 am","team_name":"Huhuzzz"},{"amount":"- 60","txn_type":"Joined A Contest","transaction_id":"JL20190130154882976441","txn_date":"30 January,11:24:00 am","team_name":"Huhuzzz"},{"amount":"- 326","txn_type":"Joined A Contest","transaction_id":"JL20190130154883776241","txn_date":"30 January,02:42:00 pm","team_name":"Huhuzzz"},{"amount":"- 652","txn_type":"Joined A Contest","transaction_id":"JL20190130154884529041","txn_date":"30 January,04:10:00 pm","team_name":"Huhuzzz"},{"amount":"- 80","txn_type":"Joined A Contest","transaction_id":"JL20190130154884533941","txn_date":"30 January,04:59:00 pm","team_name":"Huhuzzz"}]},{"date":"2019-01-31","info":[{"amount":"+ 200","txn_type":"Deposited Cash","transaction_id":"DD20190131154892784941","txn_date":"31 January,03:14:00 pm","team_name":"Huhuzzz"},{"amount":"+ 599","txn_type":"Deposited Cash","transaction_id":"DD20190131154892800541","txn_date":"31 January,03:14:00 pm","team_name":"Huhuzzz"},{"amount":"+ 599","txn_type":"Deposited Cash","transaction_id":"DD20190131154892825141","txn_date":"31 January,03:19:00 pm","team_name":"Huhuzzz"},{"amount":"- 60","txn_type":"Joined A Contest","transaction_id":"JL20190131154893010641","txn_date":"31 January,03:46:00 pm","team_name":"Huhuzzz"},{"amount":"- 60","txn_type":"Joined A Contest","transaction_id":"JL20190131154893014141","txn_date":"31 January,03:21:00 pm","team_name":"Huhuzzz"},{"amount":"- 60","txn_type":"Joined A Contest","transaction_id":"JL20190131154893017341","txn_date":"31 January,03:53:00 pm","team_name":"Huhuzzz"}]}]
         */

        private boolean status;
        private String message;
        private List<DataBean> data;

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

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * date : 2019-01-24
             * info : [{"amount":"+ 599","txn_type":"","transaction_id":"","txn_date":"24 January,06:45:00 pm","team_name":"Huhuzzz"}]
             */

            private String date;
            private List<InfoBean> info;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public List<InfoBean> getInfo() {
                return info;
            }

            public void setInfo(List<InfoBean> info) {
                this.info = info;
            }

            public static class InfoBean {
                /**
                 * amount : + 599
                 * txn_type :
                 * transaction_id :
                 * txn_date : 24 January,06:45:00 pm
                 * team_name : Huhuzzz
                 */

                private String amount;
                private String txn_type;
                private String transaction_id;
                private String txn_date;
                private String team_name;

                public String getAmount() {
                    return amount;
                }

                public void setAmount(String amount) {
                    this.amount = amount;
                }

                public String getTxn_type() {
                    return txn_type;
                }

                public void setTxn_type(String txn_type) {
                    this.txn_type = txn_type;
                }

                public String getTransaction_id() {
                    return transaction_id;
                }

                public void setTransaction_id(String transaction_id) {
                    this.transaction_id = transaction_id;
                }

                public String getTxn_date() {
                    return txn_date;
                }

                public void setTxn_date(String txn_date) {
                    this.txn_date = txn_date;
                }

                public String getTeam_name() {
                    return team_name;
                }

                public void setTeam_name(String team_name) {
                    this.team_name = team_name;
                }
            }
        }
    }
}
