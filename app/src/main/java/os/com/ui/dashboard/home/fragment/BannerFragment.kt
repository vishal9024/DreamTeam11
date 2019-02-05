package os.com.ui.dashboard.home.fragment

//import kotlinx.android.synthetic.main.child_banner_layout.*
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.bannerlayout_cashfantasy.*
import os.com.BuildConfig
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.ui.dashboard.home.apiResponse.bannerList.Data
import os.com.ui.invite.activity.InviteFriendsActivity


class BannerFragment : Fragment() {
    /*1=>'Match Type',2=>'Invite Type',3=>'Offer Type'*/
    private var rootview: View? = null
    private var bundle: Bundle? = null
    var banner: Data? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (BuildConfig.APPLICATION_ID == "os.cashfantasy")
            rootview = inflater.inflate(R.layout.bannerlayout_cashfantasy, container, false)
        else
            rootview = inflater.inflate(R.layout.child_banner_layout, container, false)
        bundle = arguments
        banner = bundle!!.getParcelable(Tags.DATA)
        ImageLoader.getInstance().displayImage(banner!!.image, imv_product, FantasyApplication.getInstance().options)
        if (banner!!.type.equals("1")) {

        } else if (banner!!.type.equals("2")) {
            startActivity(Intent(activity, InviteFriendsActivity::class.java))
        } else if (banner!!.type.equals("3")) {
//            var currentBalance = "0.0"
//            if (banner!!.offer != null)
//                currentBalance =
//                        (banner!!.offer!!.().toFloat() + mData!!.total_cash_amount.toFloat() + mData!!.total_winning_amount.toFloat()).toString()
//            startActivity(
//                Intent(activity, AddCashActivity::class.java).putExtra(
//                    IntentConstant.currentBalance,
//                    currentBalance
//                ).putExtra(IntentConstant.AddType, IntentConstant.ADD)
//            )
        }
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}

