package os.com.ui.dashboard.home.fragment

//import kotlinx.android.synthetic.main.child_banner_layout.*
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.nostra13.universalimageloader.core.ImageLoader
import os.com.BuildConfig
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.ui.addCash.activity.AddCashActivity
import os.com.ui.contest.activity.ContestActivity
import os.com.ui.dashboard.home.apiResponse.bannerList.Data
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
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
        val img=rootview!!.findViewById<ImageView>(R.id.imv_product)
        ImageLoader.getInstance().displayImage(banner!!.image, img, FantasyApplication.getInstance().options)
        img.setOnClickListener{
            if (banner!!.type.equals("1")) {
                if (!banner!!.upcoming!!.match_id.isEmpty()) {
                    val match: os.com.ui.dashboard.home.apiResponse.getMatchList.Match = Match()
                    match.series_id = banner!!.upcoming!!.series_id
                    match.match_id = banner!!.upcoming!!.match_id
                    match.series_name = banner!!.upcoming!!.series_name
                    match.local_team_id = banner!!.upcoming!!.local_team_id
                    match.local_team_name = banner!!.upcoming!!.local_team_name
                    match.local_team_flag = banner!!.upcoming!!.local_team_flag
                    match.visitor_team_id = banner!!.upcoming!!.visitor_team_id
                    match.visitor_team_name = banner!!.upcoming!!.visitor_team_name
                    match.visitor_team_flag = banner!!.upcoming!!.visitor_team_flag
                    match.star_date = banner!!.upcoming!!.star_date
                    match.star_time = banner!!.upcoming!!.star_time
                    match.total_contest = banner!!.upcoming!!.total_contest
                    match.guru_url = banner!!.upcoming!!.guru_url
                    startActivity(
                        Intent(activity, ContestActivity::class.java).putExtra(
                            IntentConstant.DATA,
                            match
                        ).putExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
                    )
                }
            } else if (banner!!.type.equals("2")) {
                startActivity(Intent(activity, InviteFriendsActivity::class.java))
            } else if (banner!!.type.equals("3")) {
                val currentBalance = "0.0"
                startActivity(
                    Intent(activity, AddCashActivity::class.java)
                        .putExtra(
                            Tags.DATA,
                            banner!!.offer
                        ).putExtra(
                            IntentConstant.currentBalance,
                            currentBalance
                        ).putExtra(IntentConstant.AddType, IntentConstant.OFFER_BANNER)
                )
            }
        }


        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}

