package os.com.ui.joinedContest.activity

import android.Manifest
import android.annotation.SuppressLint
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
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickRecyclerView
import os.com.networkCall.ApiClient
import os.com.ui.contest.apiResponse.getContestDetail.Data
import os.com.ui.contest.apiResponse.getContestDetail.Team
import os.com.ui.createTeam.activity.TeamPreviewActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.adapter.LeaderShipTeamsAdapter
import os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse.JoinedContestData
import os.com.utils.AppDelegate
import os.com.utils.CountTimer
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
                if (!data!!.total_winners.isEmpty() && data!!.total_winners.toInt() > 0)
                    callWinningBreakupApi(
                        joinedContest!!.contest_id,
                        data!!.breakup_detail!!,
                        data!!.prize_money
                    )
            }
            R.id.btn_dreamTeam -> {
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
                    DownloadTeam()
                }

            }
            R.id.btn_InviteFriends -> {
                tellUrFriends()
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
        try {
            // play market available
            packageManager?.getPackageInfo("com.android.vending", 0)
            // not available
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            // should use browser
            link = "https://play.google.com/store/apps/details?id="
        }
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

    fun DownloadTeam() {
        var name =
            localTeamName + getString(R.string.vs) + visitorTeamName + "-" + data!!.invite_code + "-" + System.currentTimeMillis()
        //
        val request =
            DownloadManager.Request(Uri.parse("http://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf"))
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
            DownloadTeam()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joined_completedcontest_leadership)
        initViews()
    }

    var localTeamName = ""
    var visitorTeamName = ""
    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    var contest_id = ""
    var joinedContest: JoinedContestData? = null
    private fun initViews() {
        if (intent != null) {
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
            joinedContest = intent.getParcelableExtra(IntentConstant.DATA)
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
                countTimer!!.startUpdateTimer(dateTime, txt_CountDownTimer)
            }
        } else if (matchType == IntentConstant.COMPLETED) {
            txt_CountDownTimer.setText(getString(R.string.completed))
        } else
            txt_CountDownTimer.setText(getString(R.string.in_progress))
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.joined_Contest)
        setMenu(false, false, false, false, false)
        if (NetworkUtils.isConnected()) {
            callContestDetailApi()
            callMatchScoreApi()
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
    }

    var data: Data? = null
    private fun callContestDetailApi() {
//        callApi = false
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.contest_id] = joinedContest!!.contest_id
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id

        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@LeaderShipBoardActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .contest_detail(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
                if (response.response!!.status) {
                    scrollView.visibility = View.VISIBLE
                    data = response.response!!.data!!
                    setdata(data!!)
                    UpdateView(data!!)

                } else {
                }
            } catch (exception: Exception) {
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
    private fun callMatchScoreApi() {
        try {
            val map = HashMap<String, String>()
            if (pref!!.isLogin)
                map[Tags.user_id] = pref!!.userdata!!.user_id
            map[Tags.language] = FantasyApplication.getInstance().getLanguage()
            map[Tags.match_id] = match!!.match_id/*"13071965317"*/
            map[Tags.series_id] = match!!.series_id/*"13071965317"*/
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@LeaderShipBoardActivity)
                try {
                    val request = ApiClient.client
                        .getRetrofitService()
                        .match_scores(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
                    if (response.response!!.status) {
                        updateScoreBoard(response.response!!.data)
                    } else {
                    }
                } catch (exception: Exception) {
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
            ll_visitorTeamScore.visibility = VISIBLE
            txt_localTeamScore.text = localTeamName + "  " + data!!.local_team_score
            txt_visitorTeamScore.text = visitorTeamName + "  " + data.vistor_team_score
            txt_WinBy.text = data.comment
        } else {
            ll_visitorTeamScore.visibility = GONE
            txt_WinBy.visibility = GONE
            card_view1.visibility = VISIBLE
            txt_localTeamScore.text = getString(R.string.match_not_started)
        }
    }
    fun selectorRank(p: Team): Int = p.rank.toInt()
    @SuppressLint("WrongConstant")
    private fun setAdapter() {
//        joined_team_list.sortWith(Comparator  { t,  -> t.user_id.compareTo(pref!!.userdata!!.user_id) })
        joined_team_list.sortedWith(Comparator { t: Team, u: Team -> t.user_id.compareTo(pref!!.userdata!!.user_id) })
        var teams=joined_team_list.filter { it.user_id.equals(pref!!.userdata!!.user_id)}as ArrayList
        var newTeam=joined_team_list.filter { !it.user_id.equals(pref!!.userdata!!.user_id)} as ArrayList
        newTeam. sortBy { selectorRank(it) }
          joined_team_list.clear()
        joined_team_list.addAll(teams)
        joined_team_list.addAll(newTeam)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = LeaderShipTeamsAdapter(this, joined_team_list, this, matchType)
    }

    override fun onClickItem(tag: String, position: Int) {
        if (tag.equals("Preview")) {
            callGetTeamListApi(
                joined_team_list[position].user_id,
                joined_team_list[position].team_no,
                joined_team_list[position].team_name
            )
        }
    }

    private fun callGetTeamListApi(user_id: String, teamNo: String, team_name: String) {
        val loginRequest = HashMap<String, String>()
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
                    .player_team_list(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
                if (response.response!!.status) {
                    var teamName = getString(R.string.team) + teamNo
                    if (!team_name.isEmpty()) {
                        teamName = team_name + "(T" + teamNo + ")"
                    }
                    startActivity(
                        Intent(this@LeaderShipBoardActivity, TeamPreviewActivity::class.java).putExtra(
                            "show",
                            1
                        ).putExtra(IntentConstant.DATA, response.response!!.data!![0]).putParcelableArrayListExtra(
                            IntentConstant.SELECT_PLAYER,
                            response.response!!.data!![0].player_details
                        )
                            .putExtra("substitute", response.response!!.data!![0].substitute_detail)
                            .putExtra("teamName", teamName)
                            .putExtra("points", true)
                    )
                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@LeaderShipBoardActivity)
            }
        }
    }
}