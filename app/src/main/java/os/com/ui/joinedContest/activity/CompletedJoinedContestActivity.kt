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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickRecyclerView
import os.com.networkCall.ApiClient
import os.com.ui.contest.apiResponse.matchScoreResponse.Data
import os.com.ui.createTeam.activity.TeamPreviewActivity
import os.com.ui.dashboard.home.adapter.MatchFixturesAdapter
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.adapter.JoinedCompletedContestAdapter
import os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse.JoinedContestData
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*


class CompletedJoinedContestActivity : BaseActivity(), View.OnClickListener, OnClickRecyclerView {
    override fun onClickItem(tag: String, position: Int) {
    }

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
            R.id.btn_dreamTeam -> {
                callGetTeamListApi(
                    "DreamTeam",
                    pref!!.userdata!!.user_id,
                    "",
                    "Dream Team"
                )
            }
        }
    }

    private fun callGetTeamListApi(tag: String, user_id: String, teamNo: String, team_name: String) {
        val loginRequest = HashMap<String, String>()
        loginRequest[Tags.user_id] = user_id
        loginRequest[Tags.team_no] = teamNo
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@CompletedJoinedContestActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .dream_team(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@CompletedJoinedContestActivity)
                if (response.response!!.status) {
//                    AppDelegate.showToast(this@CompletedJoinedContestActivity, response.response!!.message!!)
                    if (response.response!!.data!=null){

                    }else{
                        AppDelegate.showToast(this@CompletedJoinedContestActivity,getString(R.string.no_dream_team_found))
                    }
                    startActivity(
                        Intent(this@CompletedJoinedContestActivity, TeamPreviewActivity::class.java).putExtra(
                            "show",
                            1
                        ).putExtra(IntentConstant.DATA, response.response!!.data!!).putParcelableArrayListExtra(
                            IntentConstant.SELECT_PLAYER,
                            response.response!!.data!!.player_details
                        )
                            .putExtra("substitute", response.response!!.data!!.substitute_detail)
                            .putExtra("teamName", team_name)
                            .putExtra("points", true)
                            .putExtra("DreamTeam", true)
                    )
                } else {
                    logoutIfDeactivate(response.response!!.message!!)
                    AppDelegate.showToast(this@CompletedJoinedContestActivity, response.response!!.message!!)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@CompletedJoinedContestActivity)
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
            setMenu(false, false, false, false, false)
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
                    callMatchScoreApi(VISIBLE)
                    if (!match!!.star_date.isEmpty()) {
                        val strt_date = match!!.star_date.split("T")
                        val dateTime = strt_date.get(0) + " " + match!!.star_time
                        startUpdateTimer(dateTime)
                    }
                    txt_CountDownTimer.setText(getString(R.string.in_progress))
                    txt_CountDownTimer.setTextColor(resources.getColor(R.color.dark_yellow))
                } else if (matchType == IntentConstant.COMPLETED) {
                    callMatchScoreApi(VISIBLE)
                    txt_CountDownTimer.setText(getString(R.string.completed))
                } else {
                    txt_CountDownTimer.setText(getString(R.string.in_progress))
                    txt_CountDownTimer.setTextColor(resources.getColor(R.color.dark_yellow))
                }
                if (NetworkUtils.isConnected()) {
                    callGetJoinedContestListApi(VISIBLE)
                } else
                    AppDelegate.showToast(this, getString(R.string.error_network_connection))
                btn_dreamTeam.setOnClickListener(this)
            }

            swipeToRefresh.setOnRefreshListener {
                if (AppDelegate.isNetworkAvailable(this))
                    refreshItems()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun refreshItems() {
        GlobalScope.launch {
            delay(200)
            try {
                if (AppDelegate.isNetworkAvailable(this@CompletedJoinedContestActivity)) {
                    callGetJoinedContestListApi(View.GONE)
                    callMatchScoreApi(View.GONE)
                }
            } catch (e: Exception) {
                swipeToRefresh.isRefreshing = false
            }
        }
    }

    private fun callGetJoinedContestListApi(visibility: Int) {
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
                if (visibility == VISIBLE)
                    AppDelegate.showProgressDialog(this@CompletedJoinedContestActivity)
                try {
                    val request = ApiClient.client
                        .getRetrofitService()
                        .getJoinedContestlist(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@CompletedJoinedContestActivity)
                    swipeToRefresh.isRefreshing = false
                    if (response.response!!.status) {
                        constraint_layout.visibility = VISIBLE
                        if (matchType == IntentConstant.COMPLETED)
                            ll_bottom.visibility = VISIBLE
                        else
                            ll_bottom.visibility = GONE
                        if (!response.response!!.data!!.joined_contest!!.isEmpty()) {
                            setAdapterJoinedContest(response.response!!.data!!.joined_contest!!)
                            cl_noJoinedContest.visibility = View.GONE
                        } else {
                            cl_noJoinedContest.visibility = View.VISIBLE
                            setAdapterUpcomingContest(response.response!!.data!!.upcoming_match!!)
                        }
                    } else {
                        logoutIfDeactivate(response.response!!.message)
                    }
                } catch (exception: Exception) {
                    swipeToRefresh.isRefreshing = false
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
        rv_Contest!!.adapter = MatchFixturesAdapter(this, matchList, this)
    }

    private fun callMatchScoreApi(visibility: Int) {
        try {
            callApi = false
            val map = HashMap<String, String>()
            if (pref!!.isLogin)
                map[Tags.user_id] = pref!!.userdata!!.user_id
            else
                map[Tags.user_id] = ""
            map[Tags.language] = FantasyApplication.getInstance().getLanguage()
            map[Tags.match_id] = match!!.match_id/*"13071965317"*/
            map[Tags.series_id] = match!!.series_id/*"13071965317"*/
            GlobalScope.launch(Dispatchers.Main) {
                if (visibility == VISIBLE)
                    AppDelegate.showProgressDialog(this@CompletedJoinedContestActivity)
                try {
                    val request = ApiClient.client
                        .getRetrofitService()
                        .match_scores(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    swipeToRefresh.isRefreshing = false
                    AppDelegate.hideProgressDialog(this@CompletedJoinedContestActivity)
                    if (response.response!!.status) {
//                        if (response.response!!.data != null) {
                        updateScoreBoard(response.response!!.data)
//                        } else
//                        {
//                            txt_scoreBoard.visibility = GONE
//                            ll_visitorTeamScore.visibility = GONE
//                            txt_WinBy.visibility = GONE
//                            card_view1.visibility = VISIBLE
//                            txt_localTeamScore.text = getString(R.string.match_not_started)
//                        }
                    } else {
//                        updateScoreBoard(response.response!!.data)
                        logoutIfDeactivate(response.response!!.message)
                    }
                } catch (exception: Exception) {
                    swipeToRefresh.isRefreshing = false
                    AppDelegate.hideProgressDialog(this@CompletedJoinedContestActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateScoreBoard(data: Data?) {
        if (data!!.match_started) {
            card_view1.visibility = VISIBLE
            txt_WinBy.visibility = VISIBLE
            txt_scoreBoard.visibility = VISIBLE
            ll_visitorTeamScore.visibility = VISIBLE
            txt_localTeamScore.text = localTeamName + "  " + data!!.local_team_score
            txt_visitorTeamScore.text = visitorTeamName + "  " + data.vistor_team_score
            txt_WinBy.text = data.comment
        } else {
            txt_scoreBoard.visibility = GONE
            ll_visitorTeamScore.visibility = GONE
            txt_WinBy.visibility = GONE
            card_view1.visibility = VISIBLE
            if (!data.comment.isEmpty())
                txt_localTeamScore.text = data.comment
            else
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
                    callMatchScoreApi(VISIBLE)
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