package os.com.ui.dashboard.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import os.com.BuildConfig
import os.com.R


class BannerFragment : Fragment() {

    private var rootview: View? = null
    private var bundle: Bundle? = null
    internal var imageLoader: com.nostra13.universalimageloader.core.ImageLoader =
        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
    internal var options = DisplayImageOptions.Builder().cacheInMemory(true)
        .cacheOnDisc(true).resetViewBeforeLoading(true)/*.showImageForEmptyUri(R.drawable)*/.build()
    //    private NewsModel newsModel;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (BuildConfig.APPLICATION_ID == "os.cashfantasy")
            rootview = inflater.inflate(R.layout.bannerlayout_cashfantasy, container, false)
        else
            rootview = inflater.inflate(R.layout.child_banner_layout, container, false)
        bundle = arguments
        //        newsModel = bundle.getParcelable(Tags.DATA);
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity!!))
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}

