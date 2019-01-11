package os.com.ui.contest.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_megacontest.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_megacontest.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickDialogue
import os.com.networkCall.ApiClient
import os.com.ui.contest.adapter.TeamsAdapter
import os.com.ui.contest.apiResponse.getContestDetail.Data
import os.com.ui.contest.apiResponse.getContestDetail.Team
import os.com.ui.contest.apiResponse.getContestList.Contest
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamSelectActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate
import os.com.utils.CountTimer
import os.com.utils.networkUtils.NetworkUtils


class ContestDetailActivity : BaseActivity(), View.OnClickListener {
    var callApi = false
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_joinContest -> {
                callApi = true
                startActivityForResult(
                    Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    ).putExtra(IntentConstant.CONTEST_ID, contest!!.contest_id)
                        .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.JOIN),
                    AppRequestCodes.UPDATE_ACTIVITY
                )
            }
            R.id.ll_winners -> {
                if (!contest!!.total_winners.isEmpty() && contest!!.total_winners.toInt() > 0)
                    callWinningBreakupApi(
                        contest!!.contest_id,
                        contest!!.breakup_detail!!,
                        contest!!.prize_money
                    )
//                val bottomSheetDialogFragment = BottomSheetWinningListFragment()
//                var bundle = Bundle()
//                bundle.putString(Tags.contest_id, contest!!.contest_id)
//                bottomSheetDialogFragment.arguments = bundle
//                bottomSheetDialogFragment.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")
            }
            R.id.txt_Join -> {
                if (!data!!.is_joined)
                    if (FantasyApplication.getInstance().teamCount == 0) {
                        callApi = true
                        startActivityForResult(
                            Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                                IntentConstant.CONTEST_TYPE,
                                matchType
                            ).putExtra(IntentConstant.CONTEST_ID, contest!!.contest_id)
                                .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.JOIN),
                            AppRequestCodes.UPDATE_ACTIVITY
                        )
                    } else if (FantasyApplication.getInstance().teamCount == 1) {

                        if (NetworkUtils.isConnected()) {
                            checkAmountWallet(
                                match!!.match_id,
                                match!!.series_id, contest!!.contest_id, "", object : OnClickDialogue {
                                    override fun onClick(tag: String, success: Boolean) {
//                                    if (tag.equals(Tags.success) && success)
//                                        finish()
                                    }

                                }
                            )
                        } else
                            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
                    } else {
                        callApi = true
                        startActivityForResult(
                            Intent(this, MyTeamSelectActivity::class.java).putExtra(
                                IntentConstant.MATCH,
                                match
                            ).putExtra(
                                IntentConstant.CONTEST_TYPE,
                                matchType
                            ).putExtra(IntentConstant.CONTEST_ID, contest!!.contest_id), AppRequestCodes.UPDATEVIEW
                        )
                    }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            if (NetworkUtils.isConnected()) {
//                callGetTeamListApi()
            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        if (callApi)
            if (NetworkUtils.isConnected()) {
                callContestDetailApi()
            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()

    }

    var contest: Contest? = null
    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_megacontest)
        initViews()
    }

    private fun initViews() {
        if (intent != null) {
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
            contest = intent.getParcelableExtra(IntentConstant.DATA)
            txt_matchVS.text = match!!.local_team_name + " " + getString(R.string.vs) + " " + match!!.visitor_team_name
            if (matchType == IntentConstant.FIXTURE) {
                if (!match!!.star_date.isEmpty()) {
                    val strt_date = match!!.star_date.split("T")
                    val dateTime = strt_date.get(0) + " " + match!!.star_time
                    countTimer!!.startUpdateTimer(dateTime, txt_CountDownTimer)
                }
            } else if (matchType == IntentConstant.FIXTURE) {
                txt_CountDownTimer.setText(getString(R.string.completed))
            } else
                txt_CountDownTimer.setText(getString(R.string.in_progress))
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.contest_detail)
        setMenu(false, false, false, false)
        setData()
        if (NetworkUtils.isConnected()) {
            callContestDetailApi()
        } else
            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()

        val mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        mBottomSheetBehavior.peekHeight = 0

        mBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        ll_winners.setOnClickListener(this)
        btn_joinContest.setOnClickListener(this)
        txt_Join.setOnClickListener(this)
    }

    private fun setData() {
        txt_TotalWinnings.text = getString(R.string.Rs) + " " +
                contest!!.prize_money
        txt_Winners.text = contest!!.total_winners
        txt_EntryFee.text = getString(R.string.Rs) + " " +
                contest!!.entry_fee
        txt_EndValue.text = contest!!.total_teams + " " +
                getString(R.string.teams)

        if (!contest!!.total_teams.isEmpty() && !contest!!.teams_joined.isEmpty()) {
            val strtValue =
                contest!!.total_teams.toInt() - contest!!.teams_joined.toInt()
            txt_StartValue.text = getString(R.string.only) + " " + strtValue.toString() + " " +
                    getString(R.string.spots_left)
            crs_Progress.setMinValue(0f)
            crs_Progress.setMaxValue(contest!!.total_teams.toFloat())
            crs_Progress.setMinStartValue(0f);
            crs_Progress.setMaxStartValue(contest!!.teams_joined.toFloat());
            crs_Progress.apply();
        }
//        cl_m.visibility=VISIBLE
    }

    var data: Data? = null
    private fun callContestDetailApi() {
        callApi = false
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.contest_id] = contest!!.contest_id
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id

        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@ContestDetailActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .contest_detail(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@ContestDetailActivity)
                if (response.response!!.status) {
                    data = response.response!!.data!!
                    if (!response.response!!.data!!.joined_team_list!!.isEmpty())
                        txt_TeamCount.setText(
                            response.response!!.data!!.joined_team_list!!.size.toString() + " " + getString(
                                R.string.teams
                            )
                        )
                    else
                        txt_TeamCount.setText("0 " + getString(R.string.teams))
                    if (!data!!.entry_fee.isEmpty() && data!!.entry_fee.toFloat() > 0) {
                        ll_scoreBoard.visibility = VISIBLE
                        ll_practice.visibility = GONE
                    } else {
                        ll_scoreBoard.visibility = GONE
                        ll_practice.visibility = VISIBLE
                    }

                    if (!response.response!!.data!!.join_multiple_teams) {
                        cl_m.visibility = GONE
                        cl_m1.visibility = GONE
                        if (data!!.is_joined) {
                            cl_join.visibility = GONE
                            cl_viewJoined.visibility = VISIBLE
                            var total_teams = data!!.total_teams.toInt() - data!!.teams_joined.toInt()
                            if (total_teams > 0) {
                                ll_bottom.visibility = VISIBLE
                                btn_InviteFriends.visibility = VISIBLE
                                btn_joinContest.visibility = GONE
                                view11.visibility = GONE
                                txt_Join.text = getString(R.string.joined)
                            } else {
                                ll_bottom.visibility = GONE
                                txt_Join.text = getString(R.string.invite)
                            }
                        } else {
                            ll_bottom.visibility = GONE
                            cl_join.visibility = VISIBLE
                            cl_viewJoined.visibility = GONE
                            txt_Join.text = getString(R.string.join_this_contest)
                        }
                    } else {
                        cl_m.visibility = VISIBLE
                        cl_m1.visibility = VISIBLE
                        if (data!!.is_joined) {
                            cl_join.visibility = GONE
                            cl_viewJoined.visibility = VISIBLE
                            txt_Join.text = getString(R.string.join_plus)
                            var total_teams = data!!.total_teams.toInt() - data!!.teams_joined.toInt()
                            if (total_teams > 0) {
                                ll_bottom.visibility = VISIBLE
                                btn_InviteFriends.visibility = VISIBLE
                                btn_joinContest.visibility = VISIBLE
                                view11.visibility = VISIBLE
                                txt_Join.text = getString(R.string.joined)
                            } else {
                                ll_bottom.visibility = GONE
                                txt_Join.text = getString(R.string.invite)
                            }
                        } else {
                            cl_join.visibility = VISIBLE
                            cl_viewJoined.visibility = GONE
                            txt_Join.text = getString(R.string.join_this_contest)
                        }
                    }
                    setAdapter(response.response!!.data!!.joined_team_list!!)
                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@ContestDetailActivity)
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(joined_team_list: ArrayList<Team>) {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = TeamsAdapter(this, joined_team_list)
    }
}