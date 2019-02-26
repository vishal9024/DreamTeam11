package os.com.ui.joinedContest.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_megacontest.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_joined_completed_contest_leadership.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickRecyclerView
import os.com.networkCall.ApiClient
import os.com.ui.contest.apiResponse.getContestDetail.Data
import os.com.ui.contest.apiResponse.getContestDetail.Team
import os.com.ui.createTeam.activity.TeamPreviewActivity
import os.com.ui.createTeam.apiResponse.myTeamListResponse.GetTeamListResponse
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.adapter.LeaderShipTeamsAdapter
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set


class LeaderShipBoardActivity : BaseActivity(), View.OnClickListener, OnClickRecyclerView {
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
            R.id.ll_winners -> {
                if (!data!!.total_winners.isEmpty() && data!!.total_winners.toLong() > 0)
                    callWinningBreakupApi(
                        contest_id,
                        data!!.breakup_detail!!,
                        data!!.prize_money
                    )
            }
            R.id.btn_dreamTeam -> {
                callDreamTeamApi(
                    "DreamTeam",
                    pref!!.userdata!!.user_id,
                    "",
                    "Dream Team"
                )
//                startActivity(Intent(this, TeamPreviewActivity::class.java).putExtra("show", 1))
            }
            R.id.btn_ViewTeams -> {
//                  val intent = Intent(this, DownloadService::class.java)
//                intent.putExtra("url", "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf")
//                intent.putExtra("receiver", DownloadReceiver(this,Handler(),name))
//                intent.putExtra("name", name)
//                startService(intent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 10
                    )
                } else {
                    callViewTeamsApi()

                }

            }
            R.id.btn_InviteFriends -> {
                tellUrFriends()
            }
        }
    }


    private fun callViewTeamsApi() {
//        callApi = false
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        else
            loginRequest[Tags.user_id] = ""
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.contest_id] = contest_id
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id

        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@LeaderShipBoardActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .leaderboard(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
                if (response.response!!.status) {
                    if (!response.response!!.data!!.url.isEmpty())
                        DownloadTeam(response.response!!.data!!.url)
                } else {
                    logoutIfDeactivate(response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
            }
        }
    }

    fun tellUrFriends() {
        var team_name = ""
        var rank = ""
        if (!data!!.my_team_ids!!.isEmpty()) {
            if (data!!.my_team_ids!!.size == 1) {
                if (pref!!.isLogin)
                    if (!data!!.joined_team_list!!.isEmpty()) {
                        val filterContestList: Team =
                            data!!.joined_team_list!!.filter { it.user_id.equals(pref!!.userdata!!.user_id) }
                                .single()
                        if (filterContestList != null) {
                            rank = filterContestList.rank
                            if (!filterContestList.team_name.isEmpty())
                                team_name = filterContestList.team_name
                        } else
                            team_name = "Team " + filterContestList.team_no
                    }
            }
        }

        var link = "market://details?id="
//        try {
//            // play market available
//            packageManager?.getPackageInfo("com.android.vending", 0)
//            // not available
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//            // should use browser
//            link = "https://play.google.com/store/apps/details?id="
//        }
        link = "https://play.google.com/store/apps/details?id="
        // starts external action
//                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link +packageName)))
        var shareCode = ""
        shareCode =
                "I just won playing Cricket on " +
                getString(R.string.app_name) + "| My team " + team_name + " finished  " + rank + " in the " +
                match!!.series_name +
                " match Click " + Uri.parse(link + packageName) +
                " & join and Play Fantasy Cricket!"
        AppDelegate.prepareShareIntent(shareCode, this, getString(R.string.share))
    }

    fun DownloadTeam(url: String) {
        var name =
            localTeamName + getString(R.string.vs) + visitorTeamName + "-" + data!!.invite_code + "-" + System.currentTimeMillis()
        //
        val request =
            DownloadManager.Request(Uri.parse(url))
        //                request.setDescription("ritioSome descn")
        request.setTitle(name)
        // in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name + ".pdf")
        // get download service and enqueue file
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            callViewTeamsApi()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joined_completedcontest_leadership)
        initViews()
    }

    var localTeamName = ""
    var visitorTeamName = ""
    //    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    var contest_id = ""
    //    var joinedContest: JoinedContestData? = null
    private fun initViews() {
        if (intent != null) {
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
            contest_id = intent.getStringExtra(IntentConstant.CONTEST_ID)
        }
        localTeamName = match!!.local_team_name
        visitorTeamName = match!!.visitor_team_name
        if (match!!.local_team_name.length > 5)
            localTeamName = match!!.local_team_name.substring(0, 4)
        if (match!!.visitor_team_name.length > 5)
            visitorTeamName = match!!.visitor_team_name.substring(0, 4)

        txt_matchVS.text = localTeamName + " " + getString(R.string.vs) + " " + visitorTeamName
        if (matchType == IntentConstant.FIXTURE) {
            if (!match!!.star_date.isEmpty()) {
                val strt_date = match!!.star_date.split("T")
                val dateTime = strt_date.get(0) + " " + match!!.star_time
                countTimer!!.startUpdateTimer(this, dateTime, txt_CountDownTimer)
            }
        } else if (matchType == IntentConstant.COMPLETED) {
            ll_bottom.visibility = VISIBLE
            txt_CountDownTimer.setText(getString(R.string.completed))
        } else {
            txt_CountDownTimer.setText(getString(R.string.in_progress))
            txt_CountDownTimer.setTextColor(resources.getColor(R.color.dark_yellow))
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.joined_Contest)
        setMenu(false, false, false, false, false)
        if (NetworkUtils.isConnected()) {
            callContestDetailApi(VISIBLE)
            callMatchScoreApi(VISIBLE)
        } else
            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()

        val mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        //By default set BottomSheet Behavior as Collapsed and Height 0
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        mBottomSheetBehavior.peekHeight = 0
        //If you want to handle callback of Sheet Behavior you can use below code
        mBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        ll_winners.setOnClickListener(this)
        btn_dreamTeam.setOnClickListener(this)
        txt_ViewPlayerStats.setOnClickListener(this)
        btn_ViewTeams.setOnClickListener(this)
        btn_InviteFriends.setOnClickListener(this)

        swipeToRefresh.setOnRefreshListener {
            if (AppDelegate.isNetworkAvailable(this))
                refreshItems()
        }
    }

    private fun refreshItems() {
        GlobalScope.launch {
            delay(200)
            try {
                if (NetworkUtils.isConnected()) {
                    callContestDetailApi(GONE)
                    callMatchScoreApi(GONE)
                }
            } catch (e: Exception) {
                swipeToRefresh.isRefreshing = false
            }
        }
    }

    var data: Data? = null
    private fun callContestDetailApi(visible: Int) {
//        callApi = false
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        else
            loginRequest[Tags.user_id] = ""
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.contest_id] = contest_id
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id

        GlobalScope.launch(Dispatchers.Main) {
            if (visible == VISIBLE)
                AppDelegate.showProgressDialog(this@LeaderShipBoardActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .contest_detail(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                swipeToRefresh.isRefreshing = false
                AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
                if (response.response!!.status) {
                    FantasyApplication.getInstance().server_time =
                            AppDelegate.getTimeStampFromDateServer(response.response!!.data!!.server_time!!)!!
                    scrollView.visibility = VISIBLE
                    data = response.response!!.data!!
                    setdata(data!!)
                    UpdateView(data!!)
                } else {
                    logoutIfDeactivate(response.response!!.message!!)
                }
            } catch (exception: Exception) {
                swipeToRefresh.isRefreshing = false
                AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
            }
        }
    }

    private fun setdata(data: Data) {
        txt_TotalWinnings.text = getString(R.string.Rs) + " " +
                data.prize_money
        txt_Winners.text = data.total_winners
        txt_EntryFee.text = getString(R.string.Rs) + " " +
                data.entry_fee
    }

    private fun UpdateView(data: Data) {
        if (!data.joined_team_list!!.isEmpty())
            txt_TeamCount.text = data!!.joined_team_list!!.size.toString() + " " + getString(
                R.string.teams
            )
        else
            txt_TeamCount.setText("0 " + getString(R.string.teams))
        if (!data.entry_fee.isEmpty() && data.entry_fee.toFloat() > 0) {
            ll_scoreBoard.visibility = VISIBLE
            ll_practice.visibility = GONE
        } else {
            ll_scoreBoard.visibility = GONE
            ll_practice.visibility = VISIBLE
        }
        joined_team_list = data.joined_team_list!!
        setAdapter()
    }

    var joined_team_list: ArrayList<Team> = ArrayList()
    private fun callMatchScoreApi(visible: Int) {
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
                if (visible == VISIBLE)
                    AppDelegate.showProgressDialog(this@LeaderShipBoardActivity)
                try {
                    val request = ApiClient.client
                        .getRetrofitService()
                        .match_scores(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    swipeToRefresh.isRefreshing = false
                    AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
                    if (response.response!!.status) {
                        scrollView.visibility = VISIBLE
                        updateScoreBoard(response.response!!.data)
//                        if (response.response!!.data != null) {
//                            updateScoreBoard(response.response!!.data)
//                        } else {
//                            txt_scoreBoard.visibility = GONE
//                            ll_visitorTeamScore.visibility = GONE
//                            txt_WinBy.visibility = GONE
//                            card_view1.visibility = VISIBLE
//                            txt_localTeamScore.text = getString(R.string.match_not_started)
//                        }
                    } else {
                        logoutIfDeactivate(response.response!!.message)
                    }
                } catch (exception: Exception) {
                    swipeToRefresh.isRefreshing = false
                    AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateScoreBoard(data: os.com.ui.contest.apiResponse.matchScoreResponse.Data?) {
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

    fun selectorRank(p: Team): Long = p.rank.toLong()
    @SuppressLint("WrongConstant")
    private fun setAdapter() {
//        joined_team_list.sortWith(Comparator  { t,  -> t.user_id.compareTo(pref!!.userdata!!.user_id) })
        joined_team_list.sortedWith(Comparator { t: Team, u: Team -> t.user_id.compareTo(pref!!.userdata!!.user_id) })
        var teams = joined_team_list.filter { it.user_id.equals(pref!!.userdata!!.user_id) } as ArrayList
        var newTeam = joined_team_list.filter { !it.user_id.equals(pref!!.userdata!!.user_id) } as ArrayList
        newTeam.sortBy { selectorRank(it) }
        joined_team_list.clear()
        joined_team_list.addAll(teams)
        joined_team_list.addAll(newTeam)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = LeaderShipTeamsAdapter(this, data!!.match_status, joined_team_list, this, matchType)
    }

    override fun onClickItem(tag: String, position: Int) {
        if (tag.equals("Preview") || tag.equals("substitute")) {
            callGetTeamListApi(
                tag,
                joined_team_list[position].user_id,
                joined_team_list[position].team_no,
                joined_team_list[position].team_name
            )
        }
    }

    private fun callDreamTeamApi(tag: String, user_id: String, teamNo: String, team_name: String) {
        val loginRequest = java.util.HashMap<String, String>()
        loginRequest[Tags.user_id] = user_id
        loginRequest[Tags.team_no] = teamNo
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@LeaderShipBoardActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .dream_team(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
                if (response.response!!.status) {
//                    AppDelegate.showToast(this@LeaderShipBoardActivity, response.response!!.message!!)
                    if (response.response!!.data != null) {

                    } else {
                        AppDelegate.showToast(this@LeaderShipBoardActivity, getString(R.string.no_dream_team_found))
                    }
                    startActivity(
                        Intent(this@LeaderShipBoardActivity, TeamPreviewActivity::class.java).putExtra(
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
                    AppDelegate.showToast(this@LeaderShipBoardActivity, response.response!!.message!!)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppRequestCodes.EDIT && resultCode == Activity.RESULT_OK)
            callContestDetailApi(VISIBLE)
    }

    private fun callGetTeamListApi(tag: String, user_id: String, teamNo: String, team_name: String) {
        val loginRequest = HashMap<String, String>()
        loginRequest[Tags.user_id] = user_id
        loginRequest[Tags.team_no] = teamNo
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@LeaderShipBoardActivity)
            try {
                var request =
                    ApiClient.client
                        .getRetrofitService()
                        .player_team_list(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
                if (response.response!!.status) {
                    if (tag.equals("Preview")) {
                        var teamName = getString(R.string.team) + teamNo
                        if (!team_name.isEmpty()) {
                            teamName = team_name + "(T" + teamNo + ")"
                        }
                        startActivity(
                            Intent(this@LeaderShipBoardActivity, TeamPreviewActivity::class.java).putExtra(
                                "show",
                                2
                            ).putExtra(IntentConstant.DATA, response.response!!.data!![0]).putParcelableArrayListExtra(
                                IntentConstant.SELECT_PLAYER,
                                response.response!!.data!![0].player_details
                            ).putExtra(IntentConstant.MATCH, match)
                                .putExtra("substitute", response.response!!.data!![0].substitute_detail)
                                .putExtra("teamName", teamName)
                                .putExtra("points", true)
                                .putExtra("pointsShow", 1)
                        )
                    } else if (tag.equals("substitute")) {
                        if (response.response!!.data!![0].substitute_detail!!.role.contains("Wicketkeeper", true)) {
                            if (response.response!!.data!![0].vice_captain_player_id.equals(response.response!!.data!![0].substitute_detail!!.player_id)
                                || response.response!!.data!![0].captain_player_id.equals(response.response!!.data!![0].substitute_detail!!.player_id)
                            )
                                AppDelegate.showSnackBar(
                                    toolbar,
                                    this@LeaderShipBoardActivity,
                                    getString(R.string.substitute_wicketKeeper_validation)
                                )
                            else
                                data(response, teamNo)
                        } else {
                            data(response, teamNo)
                        }


                    }
                } else {
                    logoutIfDeactivate(response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
            }
        }
    }

    fun data(response: GetTeamListResponse, teamNo: String) {
        startActivityForResult(
            Intent(
                this@LeaderShipBoardActivity,
                ReplaceWithSubstituteActivity::class.java
            ).putParcelableArrayListExtra(
                IntentConstant.SELECT_PLAYER,
                response.response!!.data!![0].player_details
            ).putExtra("substitute", response.response!!.data!![0].substitute_detail)
                .putExtra(IntentConstant.MATCH, match).putExtra(IntentConstant.CONTEST_TYPE, matchType)
                .putExtra(IntentConstant.CONTEST_ID, contest_id)
                .putExtra(IntentConstant.CAPTAIN_ID, response.response!!.data!![0].captain_player_id)
                .putExtra(
                    IntentConstant.VICE_CAPTAIN_ID,
                    response.response!!.data!![0].vice_captain_player_id
                )
                .putExtra(IntentConstant.TEAM_ID, teamNo)
            ,
            AppRequestCodes.EDIT
        )
    }
}