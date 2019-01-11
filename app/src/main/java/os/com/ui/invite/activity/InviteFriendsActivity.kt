package os.com.ui.invite.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_invite_friends.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.networkCall.ApiConstant
import os.com.ui.dashboard.more.activity.WebViewActivity


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
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.invite_friends)
        setMenu(false, false, false, false)
        txt_Invite.setOnClickListener(this)
        tv_how_it_work.setOnClickListener(this)
        tv_rule_for_fair_play.setOnClickListener(this)
        txt_label.setText("kick off your friends " + getString(R.string.app_name) + " Journey!")
        txt_code.setText(pref!!.userdata!!.refer_id)
    }

}