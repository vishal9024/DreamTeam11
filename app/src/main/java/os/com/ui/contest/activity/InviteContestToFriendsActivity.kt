package os.com.ui.contest.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_invite_contset_friends.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate


class InviteContestToFriendsActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {

            R.id.txt_Invite -> {
                var shareCode = ""
                shareCode =
                        "You've been challanged! \n\nThink you can beat me? Join the contest on " +
                        getString(R.string.app_name) + " for the " + match!!.series_name +
                        " match and prove it! \n\nUse Contest Code " + contestCode.capitalize() +
                        " & join the action NOW!"
                AppDelegate.prepareShareIntent(shareCode, this, getString(R.string.invite))

            }
        }
    }

    override fun onBackPressed() {
        val intent =Intent()
        setResult(Activity.RESULT_OK,intent)
        super.onBackPressed()
    }
    override fun onDestroy() {
        val intent =Intent()
        setResult(Activity.RESULT_OK,intent)
        super.onDestroy()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_contset_friends)
        initViews()
    }

//    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    var contestCode = ""
    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.invite_friends)
            setMenu(false, false, false, false, false)
            txt_Invite.setOnClickListener(this)
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
            contestCode = intent.getStringExtra(IntentConstant.CONTEST_CODE)
            txt_code.setText(contestCode)
           txt_Title.text = match!!.series_name
            txt_Team1.text = match!!.local_team_name
            txt_Team2.text = match!!.visitor_team_name
            ImageLoader.getInstance().displayImage(
                match!!.local_team_flag,
                cimg_Match2,
                FantasyApplication.getInstance().options
            )
            ImageLoader.getInstance().displayImage(
                match!!.visitor_team_flag,
                cimg_Match1,
                FantasyApplication.getInstance().options
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}