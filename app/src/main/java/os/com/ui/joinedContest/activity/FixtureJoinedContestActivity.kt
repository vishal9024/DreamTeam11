package os.com.ui.joinedContest.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_joined_contest.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.adapter.JoinedFixturesContestAdapter
import os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse.JoinedContestData
import os.com.utils.AppDelegate
import os.com.utils.CountTimer
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class FixtureJoinedContestActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joined_contest)
        initViews()
    }

    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.joined_Contest)
            setMenu(false, false, false, false,false)

//              setAdapter()
            if (intent != null) {
                match = intent.getParcelableExtra(IntentConstant.MATCH)
                matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
                var localTeamName=match!!.local_team_name
                var visitorTeamName=match!!.visitor_team_name
                if (match!!.local_team_name.length>5)
                    localTeamName=match!!.local_team_name.substring(0,4)
                if (match!!.visitor_team_name.length>5)
                    visitorTeamName=match!!.visitor_team_name.substring(0,4)

                txt_matchVS.text = localTeamName+ " " + getString(R.string.vs) + " " + visitorTeamName
                if (matchType == IntentConstant.FIXTURE) {
                    if (!match!!.star_date.isEmpty()) {
                        val strt_date = match!!.star_date.split("T")
                        val dateTime = strt_date.get(0) + " " + match!!.star_time
                        countTimer!!.startUpdateTimer(dateTime, txt_CountDownTimer)
                    }
                } else if (matchType == IntentConstant.COMPLETED) {
                    txt_CountDownTimer.setText(getString(R.string.completed))
                } else
                    txt_CountDownTimer.setText(getString(R.string.in_progress))
                if (NetworkUtils.isConnected()) {
                    callGetJoinedContestListApi()
                } else
                    Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    public fun callGetJoinedContestListApi() {
        try {
            val map = HashMap<String, String>()
            if (pref!!.isLogin)
                map[Tags.user_id] = pref!!.userdata!!.user_id
            else
                map[Tags.user_id] = ""
            map[Tags.language] = FantasyApplication.getInstance().getLanguage()
            map[Tags.match_id] = match!!.match_id/*"13071965317"*/
            map[Tags.series_id] = match!!.series_id/*"13071965317"*/
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@FixtureJoinedContestActivity)
                try {
                    val request = ApiClient.client
                        .getRetrofitService()
                        .getJoinedContestlist(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@FixtureJoinedContestActivity)
                    if (response.response!!.status) {
                        setAdapter(response.response!!.data!!.joined_contest!!)
                    } else {
                      logoutIfDeactivate(response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@FixtureJoinedContestActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK)
            callGetJoinedContestListApi()
    }
    var joinedFixturesContestAdapter: JoinedFixturesContestAdapter? = null
    @SuppressLint("WrongConstant")
    private fun setAdapter(data: ArrayList<JoinedContestData>) {
        try {
            val llm = LinearLayoutManager(this)
            llm.orientation = LinearLayoutManager.VERTICAL
            rv_Contest!!.layoutManager = llm
            joinedFixturesContestAdapter = JoinedFixturesContestAdapter(this, match, matchType, data)
            rv_Contest!!.adapter = joinedFixturesContestAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}