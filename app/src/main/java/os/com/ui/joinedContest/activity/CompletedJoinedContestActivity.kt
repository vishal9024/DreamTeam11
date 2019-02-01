package os.com.ui.joinedContest.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_joined_completed_contestlist.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.contest.apiResponse.matchScoreResponse.Data
import os.com.ui.dashboard.home.adapter.MatchFixturesAdapter
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.adapter.JoinedCompletedContestAdapter
import os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse.JoinedContestData
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*


class CompletedJoinedContestActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.txt_ViewPlayerStats -> {
                startActivity(
                    Intent(this, PlayerStatsActivity::class.java).putExtra(
                        IntentConstant.MATCH,
                        match
                    ).putExtra(IntentConstant.CONTEST_TYPE, matchType)
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joined_completed_contestlist)
        initViews()
    }

    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    var localTeamName = ""
    var visitorTeamName = ""
    private fun initViews() {
        try {
            card_view1.visibility = GONE
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.join_Contest)
            setMenu(false, false, false, false,false)
            txt_ViewPlayerStats.setOnClickListener(this)
            if (intent != null) {
                match = intent.getParcelableExtra(IntentConstant.MATCH)
                matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
                localTeamName = match!!.local_team_name
                visitorTeamName = match!!.visitor_team_name
                if (match!!.local_team_name.length > 5)
                    localTeamName = match!!.local_team_name.substring(0, 4)
                if (match!!.visitor_team_name.length > 5)
                    visitorTeamName = match!!.visitor_team_name.substring(0, 4)
                txt_matchVS.text = localTeamName + " " + getString(R.string.vs) + " " + visitorTeamName
                txt_NoContestJoinedForMatch.text = " " + getString(R.string.FOR) + " " +
                        localTeamName + " " + getString(
                    R.string.vs
                ) + " " + visitorTeamName
                if (matchType == IntentConstant.LIVE) {
                    callMatchScoreApi()
                    if (!match!!.star_date.isEmpty()) {
                        val strt_date = match!!.star_date.split("T")
                        val dateTime = strt_date.get(0) + " " + match!!.star_time
                        startUpdateTimer(dateTime)
                    }
                    txt_CountDownTimer.setText(getString(R.string.in_progress))
                } else if (matchType == IntentConstant.COMPLETED) {
                    callMatchScoreApi()
                    txt_CountDownTimer.setText(getString(R.string.completed))
                } else
                    txt_CountDownTimer.setText(getString(R.string.in_progress))
                if (NetworkUtils.isConnected()) {
                    callGetJoinedContestListApi()
                } else
                    AppDelegate.showToast(this, getString(R.string.error_network_connection))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callGetJoinedContestListApi() {
        try {
            val map = HashMap<String, String>()
            if (pref!!.isLogin)
                map[Tags.user_id] = pref!!.userdata!!.user_id
            map[Tags.language] = FantasyApplication.getInstance().getLanguage()
            map[Tags.match_id] = match!!.match_id/*"13071965317"*/
            map[Tags.series_id] = match!!.series_id/*"13071965317"*/
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@CompletedJoinedContestActivity)
                try {
                    val request = ApiClient.client
                        .getRetrofitService()
                        .getJoinedContestlist(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@CompletedJoinedContestActivity)
                    if (response.response!!.status) {
                        constraint_layout.visibility= VISIBLE
                        if (!response.response!!.data!!.joined_contest!!.isEmpty()) {
                            setAdapterJoinedContest(response.response!!.data!!.joined_contest!!)
                            cl_noJoinedContest.visibility = View.GONE
                        } else {
                            cl_noJoinedContest.visibility = View.VISIBLE
                            setAdapterUpcomingContest(response.response!!.data!!.upcoming_match!!)
                        }
                    } else {
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@CompletedJoinedContestActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    private fun setAdapterJoinedContest(joined_contest: ArrayList<JoinedContestData>) {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = JoinedCompletedContestAdapter(this, match, matchType, joined_contest)
    }

    @SuppressLint("WrongConstant")
    private fun setAdapterUpcomingContest(matchList: ArrayList<Match>) {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = MatchFixturesAdapter(this, matchList)
    }

    private fun callMatchScoreApi() {
        try {
            callApi = false
            val map = HashMap<String, String>()
            if (pref!!.isLogin)
                map[Tags.user_id] = pref!!.userdata!!.user_id
            map[Tags.language] = FantasyApplication.getInstance().getLanguage()
            map[Tags.match_id] = match!!.match_id/*"13071965317"*/
            map[Tags.series_id] = match!!.series_id/*"13071965317"*/
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@CompletedJoinedContestActivity)
                try {
                    val request = ApiClient.client
                        .getRetrofitService()
                        .match_scores(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@CompletedJoinedContestActivity)
                    if (response.response!!.status) {
                        updateScoreBoard(response.response!!.data)
                    } else {
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@CompletedJoinedContestActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateScoreBoard(data: Data?) {
        if(data!!.match_started) {
            card_view1.visibility = VISIBLE
            txt_WinBy.visibility = VISIBLE
            ll_visitorTeamScore.visibility = VISIBLE
            txt_localTeamScore.text = localTeamName + "  " + data!!.local_team_score
            txt_visitorTeamScore.text = visitorTeamName + "  " + data.vistor_team_score
            txt_WinBy.text = data.comment
        }else{
            ll_visitorTeamScore.visibility = GONE
            txt_WinBy.visibility = GONE
            card_view1.visibility = VISIBLE
            txt_localTeamScore.text = getString(R.string.match_not_started)
        }
    }

    private val updateRemainingTimeRunnable = Runnable {
        val currentTime = System.currentTimeMillis()
        updateTimeRemaining(currentTime)
    }
    var callApi = true
    fun updateTimeRemaining(currentTime: Long) {
        try {
            val timeDiff = AppDelegate.getTimeStampFromDate(dateTime!!)!! - currentTime
            AppDelegate.LogT("timeDiff=" + timeDiff)
            if (timeDiff > 0) {
                val seconds = (timeDiff / 1000).toInt() % 60
                val minutes = (timeDiff / (1000 * 60) % 60).toInt()
                val hours = (timeDiff / (1000 * 60 * 60)).toInt()
                ll_visitorTeamScore.visibility = GONE
                txt_WinBy.visibility = GONE
                card_view1.visibility = VISIBLE
                txt_localTeamScore.text = getString(R.string.match_not_started)
            } else {
                txt_WinBy.visibility = VISIBLE
                ll_visitorTeamScore.visibility = VISIBLE
                if (callApi) {
                    callApi = false
                    callMatchScoreApi()
                }
//                textView!!.text = "Expired!!"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopUpdateTimer()
    }

    internal var dateTime: String? = null
    internal var tmr: Timer? = null
    private val mHandler = Handler()
    fun startUpdateTimer(dateTime: String) {
        this.dateTime = dateTime
        tmr = Timer()
        tmr!!.schedule(object : TimerTask() {
            override fun run() {
                mHandler.post(updateRemainingTimeRunnable)
            }
        }, 1000, 1000)
    }

    private fun stopUpdateTimer() {
        if (tmr != null) {
            tmr!!.cancel()
        }

    }
}