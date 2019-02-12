package os.com.ui.invite.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_invite_friends.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.networkCall.ApiConstant
import os.com.ui.dashboard.more.activity.WebViewActivity
import os.com.ui.invite.apiResponse.InviteFreindDetailResponse
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*


class InviteFriendsActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.tv_how_it_work -> {
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("PAGE_SLUG", "How it work")
                intent.putExtra("URL", ApiConstant.getWebViewUrl()+ ApiConstant.how_it_works_tab)
                startActivity(intent)
            }
            R.id.tv_rule_for_fair_play -> {
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("PAGE_SLUG", "Rule for fair play")
                intent.putExtra("URL", ApiConstant.getWebViewUrl()+ ApiConstant.how_fair_play_tab)
                startActivity(intent)
            }R.id.card_view_bottom-> {
            if (mData!=null) {
                val intent = Intent(this, InviteFriendDetailActivity::class.java)
                intent.putExtra("data", mData)
                startActivity(intent)
            }
            }
            R.id.txt_Invite -> {
                val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                sharingIntent.putExtra(android.content.Intent.EXTRA_TITLE, getString(R.string.app_name))
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "share with using referral code: "+txt_code!!.text)
                startActivity(Intent.createChooser(sharingIntent, "share using"))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_friends)
        initViews()
    }

    private fun initViews() {
          try{
              setSupportActionBar(toolbar)
              supportActionBar!!.setDisplayHomeAsUpEnabled(true)
              supportActionBar!!.setDisplayShowHomeEnabled(true)
              supportActionBar!!.setDisplayShowTitleEnabled(false)
              toolbarTitleTv.setText(R.string.invite_friends)
              setMenu(false, false, false, false,false)
              txt_Invite.setOnClickListener(this)
              if (NetworkUtils.isConnected()) {
                  getInviteFreindDetailList()
              } else
                  Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
              tv_how_it_work.setOnClickListener(this)
              tv_rule_for_fair_play.setOnClickListener(this)
              card_view_bottom.setOnClickListener(this)
              txt_label.setText("kick off your friends " + getString(R.string.app_name) + " Journey!")
              txt_code.setText(pref!!.userdata!!.refer_id)
          } catch (e: Exception) {
              e.printStackTrace()
          }
    }

    private fun getInviteFreindDetailList() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@InviteFriendsActivity)
                try {
                    var map = HashMap<String, String>()
                    if (pref!!.isLogin)
                        map[Tags.user_id] = pref!!.userdata!!.user_id
                    else
                        map[Tags.user_id]= ""
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .friend_referal_detail(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@InviteFriendsActivity)
                    if (response.response!!.status) {
                        setData(response.response.data)
//                        finish()
                    } else {
                        if (response.response!!.message != null)
                            AppDelegate.showToast(this@InviteFriendsActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@InviteFriendsActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var mData: InviteFreindDetailResponse.ResponseBean.DataBean?=null

    private fun setData(data: InviteFreindDetailResponse.ResponseBean.DataBean?) {
          try{
              if (data!=null){
                  mData=data
                  if (data.total_fields!=null)
                      if (data.total_fields>0) {
                          if (data.total_fields==1)
                          txtFriendsCount.text = "" + data.total_fields + " Friend Joined"
                          else txtFriendsCount.text = "" + data.total_fields + " Friends Joined"
                          llInvited.visibility=View.VISIBLE
                          txtNotInviteFriend.visibility=View.GONE
                      }else{
                          llInvited.visibility=View.GONE
                          txtNotInviteFriend.visibility=View.VISIBLE
                      }
                  if (data.to_be_earnd!=null)
                      txtEranAmount.text="₹ "+data.to_be_earnd
              } else
                  card_view_bottom.visibility=View.GONE

          } catch (e: Exception) {
              e.printStackTrace()
          }
    }

}