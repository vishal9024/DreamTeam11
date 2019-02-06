package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;
import java.util.List;

public class SeriesListResponse implements Serializable {


    /**
     * response : {"status":true,"message":"Series List.","data":[{"series_id":5433,"series_name":"Australia Women vs New Zealand"},{"series_id":1064,"series_name":"CSA Provincial One-Day Challenge"},{"series_id":5442,"series_name":"CSA Women Provincial T20"},{"series_id":5451,"series_name":"England U19 vs Bangladesh U19"},{"series_id":1010,"series_name":"ICC Cricket World Cup"},{"series_id":5450,"series_name":"India Women vs New Zealand"},{"series_id":1239,"series_name":"Momentum One Day Cup"},{"series_id":1032,"series_name":"New Zealand vs India"},{"series_id":3079,"series_name":"Pakistan Super League"},{"series_id":1022,"series_name":"Plunket Shield"},{"series_id":1160,"series_name":"Premier League"},{"series_id":3136,"series_name":"Pro 50"},{"series_id":1004,"series_name":"Sheffield Shield"},{"series_id":1023,"series_name":"South Africa vs Pakistan"},{"series_id":5448,"series_name":"Sri Lanka Women vs South Africa"},{"series_id":3128,"series_name":"Super Smash"},{"series_id":1058,"series_name":"Twenty20 Big Bash"},{"series_id":1181,"series_name":"West Indies vs England"},{"series_id":5452,"series_name":"West Indies Women vs Pakistan"},{"series_id":3135,"series_name":"Women National Cricket League"}]}
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
         * message : Series List.
         * data : [{"series_id":5433,"series_name":"Australia Women vs New Zealand"},{"series_id":1064,"series_name":"CSA Provincial One-Day Challenge"},{"series_id":5442,"series_name":"CSA Women Provincial T20"},{"series_id":5451,"series_name":"England U19 vs Bangladesh U19"},{"series_id":1010,"series_name":"ICC Cricket World Cup"},{"series_id":5450,"series_name":"India Women vs New Zealand"},{"series_id":1239,"series_name":"Momentum One Day Cup"},{"series_id":1032,"series_name":"New Zealand vs India"},{"series_id":3079,"series_name":"Pakistan Super League"},{"series_id":1022,"series_name":"Plunket Shield"},{"series_id":1160,"series_name":"Premier League"},{"series_id":3136,"series_name":"Pro 50"},{"series_id":1004,"series_name":"Sheffield Shield"},{"series_id":1023,"series_name":"South Africa vs Pakistan"},{"series_id":5448,"series_name":"Sri Lanka Women vs South Africa"},{"series_id":3128,"series_name":"Super Smash"},{"series_id":1058,"series_name":"Twenty20 Big Bash"},{"series_id":1181,"series_name":"West Indies vs England"},{"series_id":5452,"series_name":"West Indies Women vs Pakistan"},{"series_id":3135,"series_name":"Women National Cricket League"}]
         */

        public boolean status;
        public String message;
        public List<DataBean> data;

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

        public static class DataBean implements Serializable{
            /**
             * series_id : 5433
             * series_name : Australia Women vs New Zealand
             */

            public int series_id;
            public String series_name;

            public int getSeries_id() {
                return series_id;
            }

            public void setSeries_id(int series_id) {
                this.series_id = series_id;
            }

            public String getSeries_name() {
                return series_name;
            }

            public void setSeries_name(String series_name) {
                this.series_name = series_name;
            }
        }
    }
}
