package os.com.ui.invite.activity

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_invite_friends.*
import os.com.AppBase.BaseActivity
import os.com.R
import android.content.Intent



class InviteFriendsActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
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
        txt_label.setText("kick off your friends " + getString(R.string.app_name) + " Journey!")
        txt_code.setText(pref!!.userdata!!.refer_id)
    }

}