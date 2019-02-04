package os.com.ui.dashboard.profile.apiResponse;

import java.io.Serializable;
import java.util.List;

public class AvtarListResponse implements Serializable {

    /**
     * response : {"status":true,"message":null,"data":[{"id":1,"image":"https://72.octallabs.com/real11/uploads/avetars/boy.png"},{"id":2,"image":"https://72.octallabs.com/real11/uploads/avetars/girl-2.png"},{"id":3,"image":"https://72.octallabs.com/real11/uploads/avetars/girl.png"},{"id":4,"image":"https://72.octallabs.com/real11/uploads/avetars/man.png"},{"id":5,"image":"https://72.octallabs.com/real11/uploads/avetars/avatar1.png"},{"id":6,"image":"https://72.octallabs.com/real11/uploads/avetars/avatar2.png"},{"id":7,"image":"https://72.octallabs.com/real11/uploads/avetars/avatar3.png"},{"id":8,"image":"https://72.octallabs.com/real11/uploads/avetars/avatar4.png"},{"id":9,"image":"https://72.octallabs.com/real11/uploads/avetars/avatar5.png"},{"id":10,"image":"https://72.octallabs.com/real11/uploads/avetars/avatarsix.png"}]}
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
         * data : [{"id":1,"image":"https://72.octallabs.com/real11/uploads/avetars/boy.png"},{"id":2,"image":"https://72.octallabs.com/real11/uploads/avetars/girl-2.png"},{"id":3,"image":"https://72.octallabs.com/real11/uploads/avetars/girl.png"},{"id":4,"image":"https://72.octallabs.com/real11/uploads/avetars/man.png"},{"id":5,"image":"https://72.octallabs.com/real11/uploads/avetars/avatar1.png"},{"id":6,"image":"https://72.octallabs.com/real11/uploads/avetars/avatar2.png"},{"id":7,"image":"https://72.octallabs.com/real11/uploads/avetars/avatar3.png"},{"id":8,"image":"https://72.octallabs.com/real11/uploads/avetars/avatar4.png"},{"id":9,"image":"https://72.octallabs.com/real11/uploads/avetars/avatar5.png"},{"id":10,"image":"https://72.octallabs.com/real11/uploads/avetars/avatarsix.png"}]
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

        public static class DataBean implements Serializable {
            /**
             * id : 1
             * image : https://72.octallabs.com/real11/uploads/avetars/boy.png
             */

            public int id;
            public String image;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }
    }
}
