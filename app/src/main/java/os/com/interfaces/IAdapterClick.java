package os.com.interfaces;

/**
 * Created by t122 on 11-Aug-17.
 */

public interface IAdapterClick {
    interface IHomeView{
        void onClick(String tag);
    }
    interface ITitleChange {
        void show(boolean show);
        void show(boolean show, String title);
    }
    interface IItemClick {
        void onClick(int position);
    }
    interface IActiveJobItemClick {
        void onClick(int position);
        void onChatClick(int position);
    }
}
