package os.com.ui.invite.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_invitecode.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes.INVITE_CONTEST
import os.com.constant.IntentConstant
import os.com.constant.IntentConstant.FIXTURE
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.contest.activity.ContestDetailActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.invite.apiResponse.getContestInviteResponse.Data
import os.com.utils.AppDelegate

class InviteCodeActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_join -> {
                if (!et_inviteCode.text.toString().isEmpty())
                    callInviteContestCode()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitecode)
        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.invite_code)
        setMenu(false, false, false, false)
        btn_join.setOnClickListener(this)
    }

    private fun callInviteContestCode() {
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.invite_code] = et_inviteCode.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@InviteCodeActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .apply_contest_invite_code(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@InviteCodeActivity)
                if (response.response!!.status) {
                    getData(response.response!!.data!!)
                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@InviteCodeActivity)
            }
        }
    }


    fun getData(data: Data) {
        var match = Match()
        match.series_id = data.series_id
        match.match_id = data.match_id
        match.series_name = data.series_name
        match.local_team_id = data.local_team_id
        match.local_team_name = data.local_team_name
        match.local_team_flag = data.local_team_flag
        match.visitor_team_id = data.visitor_team_id
        match.visitor_team_name = data.visitor_team_name
        match.visitor_team_flag = data.visitor_team_flag
        match.star_date = data.star_date
        match.star_time = data.star_time
        match.total_contest = data.total_contest


        startActivity(
            Intent(this, ContestDetailActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                IntentConstant.CONTEST_TYPE, FIXTURE
            ).putExtra(IntentConstant.DATA, data.contest_id)
                .putExtra(IntentConstant.FROM, INVITE_CONTEST)
        )
    }
}