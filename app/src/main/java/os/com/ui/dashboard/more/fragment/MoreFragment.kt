package os.com.ui.dashboard.more.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zopim.android.sdk.api.ZopimChat
import com.zopim.android.sdk.model.VisitorInfo
import com.zopim.android.sdk.prechat.ZopimChatActivity
import kotlinx.android.synthetic.main.fragment_more.*
import os.com.AppBase.BaseFragment
import os.com.BuildConfig
import os.com.R
import os.com.data.Prefs
import os.com.networkCall.ApiConstant
import os.com.ui.dashboard.DashBoardActivity
import os.com.ui.dashboard.more.activity.WebViewActivity
import os.com.ui.invite.activity.InviteCodeActivity
import os.com.ui.invite.activity.InviteFriendsActivity


/**
 * Created by heenas on 3/5/2018.
 */
class MoreFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(view: View?) {
          try{
              when (view!!.id) {
                  R.id.tv_invite_friends -> startActivity(Intent(activity, InviteFriendsActivity::class.java))
                  R.id.tv_contest_invite_code -> startActivity(Intent(activity, InviteCodeActivity::class.java))
                  R.id.tv_fantasy_point_system -> {
                      val intent = Intent(activity, WebViewActivity::class.java)
                      intent.putExtra("PAGE_SLUG", "Fantasy Point System")
                      intent.putExtra("URL", ApiConstant.getWebViewUrl() + ApiConstant.point_system)
                      startActivity(intent)
                  }
                  R.id.tv_how_to_play -> {
                      val intent = Intent(activity, WebViewActivity::class.java)
                      intent.putExtra("PAGE_SLUG", "How to Play")
                      intent.putExtra("URL", ApiConstant.getWebViewUrl() + ApiConstant.how_to_play_tab)
                      startActivity(intent)

                  }
                  R.id.tv_helpDesk -> {
                      val intent = Intent(activity, WebViewActivity::class.java)
                      intent.putExtra("PAGE_SLUG", "Helpdesk")
                      intent.putExtra("URL", ApiConstant.getWebViewUrl() + ApiConstant.help)
                      startActivity(intent)
                  }
                  R.id.tv_work_with_us -> {
                      val intent = Intent(activity, WebViewActivity::class.java)
                      intent.putExtra("PAGE_SLUG", "Work with Us")
                      intent.putExtra("URL", ApiConstant.getWebViewUrl() + ApiConstant.static_tab)
                      startActivity(intent)
                  }
                  R.id.tv_about -> {
                      val intent = Intent(activity, WebViewActivity::class.java)
                      intent.putExtra("PAGE_SLUG", "About Us")
                      intent.putExtra("URL", ApiConstant.getWebViewUrl() + ApiConstant.static)
                      startActivity(intent)
                  }
                  R.id.tv_rules -> {
                      val intent = Intent(activity, WebViewActivity::class.java)
                      intent.putExtra("PAGE_SLUG", "Legality")
                      intent.putExtra("URL", ApiConstant.getWebViewUrl() + ApiConstant.legality_tab)
                      startActivity(intent)
                  }
//                  R.id.tv_logout -> {
//                      (activity as BaseActivity).showLogoutDialog()
//                  }
                  R.id.llZendeskChat -> {
                      val config = ZopimChat.SessionConfig()
                          .department("A department")
                      ZopimChatActivity.startActivity(context, config)
                  }
              }
          } catch (e: Exception) {
              e.printStackTrace()
          }

    }
    /* show logout confirmation popup to user*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          try{
              ZopimChat.init(getString(R.string.chat_key))
              var visitorData = VisitorInfo.Builder()
                  .name(Prefs(activity!!).userdata!!.first_name + " " + Prefs(activity!!).userdata!!.last_name)
//                            .email("visitor@example.com")
                  .phoneNumber(Prefs(activity!!).userdata!!.phone)
              ZopimChat.setVisitorInfo(visitorData.build())
              initViews()
          } catch (e: Exception) {
              e.printStackTrace()
          }
    }

    private fun initViews() {
        try {

            if (activity is DashBoardActivity)
                (activity as DashBoardActivity).setMenu(true, false, false, false, false)
            tv_invite_friends.setOnClickListener(this)
            tv_contest_invite_code.setOnClickListener(this)
            tv_fantasy_point_system.setOnClickListener(this)
            tv_how_to_play.setOnClickListener(this)
            tv_helpDesk.setOnClickListener(this)
            tv_work_with_us.setOnClickListener(this)
            tv_about.setOnClickListener(this)
            tv_rules.setOnClickListener(this)
//            tv_logout.setOnClickListener(this)
            llZendeskChat.setOnClickListener(this)
            if (BuildConfig.VERSION_CODE != 0) {
                tv_version_code.visibility = View.VISIBLE
                tv_version_code.text =
                    activity!!.resources.getString(R.string.version) + " " + BuildConfig.VERSION_CODE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


//        startActivity(Intent(this, WebViewActivity::class.java).putExtra(IntentConstant.PAGE_SLUG, "terms-of-use"))
    }


}