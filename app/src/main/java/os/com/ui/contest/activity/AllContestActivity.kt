package os.com.ui.contest.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_all_contest.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickDialogue
import os.com.interfaces.OnClickRecyclerView
import os.com.ui.contest.adapter.AllContestAdapter
import os.com.ui.contest.apiResponse.getContestList.Contest
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamSelectActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.activity.FixtureJoinedContestActivity
import os.com.utils.CountTimer
import os.com.utils.networkUtils.NetworkUtils


class AllContestActivity : BaseActivity(), View.OnClickListener, OnClickRecyclerView {
    override fun onClickItem(tag: String, position: Int) {
        if (tag.equals(Tags.JoinContestDialog)) {
            if (FantasyApplication.getInstance().teamCount == 0) {
                startActivityForResult(
                    Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    ).putExtra(IntentConstant.CONTEST_ID, contests!![position].contest_id)
                        .putExtra(IntentConstant.CREATE_OR_JOIN, IntentConstant.JOIN), AppRequestCodes.UPDATE_ACTIVITY
                )
            } else if (FantasyApplication.getInstance().teamCount == 1) {
                if (NetworkUtils.isConnected()) {
                    checkAmountWallet(
                        match!!.match_id,
                        match!!.series_id, contests!![position].contest_id, "", object : OnClickDialogue {
                            override fun onClick(tag: String, success: Boolean) {
//                                    if (tag.equals(Tags.success) && success)
//                                        finish()
                            }
                        }
                    )
                } else
                    Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
            } else {
                startActivityForResult(
                    Intent(this, MyTeamSelectActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    ).putExtra(IntentConstant.CONTEST_ID, contests!![position].contest_id), AppRequestCodes.UPDATEVIEW
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (FantasyApplication.getInstance().teamCount == 0) {
            ll_viewTeam.visibility = View.GONE
            btn_CreateTeam.visibility = View.VISIBLE
            var count = os.com.application.FantasyApplication.getInstance().teamCount + 1
            btn_CreateTeam.text = getString(R.string.create_team) + " " + count
        } else {
            ll_viewTeam.visibility = View.VISIBLE
            btn_CreateTeam.visibility = View.GONE
        }
//        if (resultCode== Activity.RESULT_OK)
//            UpdateView()
    }

    override fun onResume() {
        super.onResume()
        if (os.com.application.FantasyApplication.getInstance().teamCount== 0) {
            ll_viewTeam.visibility = View.GONE
            btn_CreateTeam.visibility = VISIBLE
            var count = os.com.application.FantasyApplication.getInstance().teamCount + 1
            btn_CreateTeam.text = getString(R.string.create_team) + " " + count
        } else {
            ll_viewTeam.visibility = View.VISIBLE
            btn_CreateTeam.visibility = GONE
        }
        txt_joined_contest.text = FantasyApplication.getInstance().teamCount.toString()
        txt_MyTeams.text = FantasyApplication.getInstance().joinedCount.toString()
    }
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.txt_winning -> {
                sortBySelector(WINNING)
            }
            R.id.txt_Winners -> {
                sortBySelector(WINNERS)
            }
            R.id.txt_Team -> {
                sortBySelector(TEAMS)
            }
            R.id.txt_EntryFee -> {
                sortBySelector(ENTRY_FEE)
            }
            R.id.ll_myteam -> {
                startActivity(Intent(this, MyTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(IntentConstant.CONTEST_TYPE, matchType))
            }
            R.id.ll_joinedContests -> {
                startActivity(Intent(this, FixtureJoinedContestActivity::class.java))
            }
            R.id.btn_CreateTeam->{
                startActivityForResult(
                    Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    ).putExtra(IntentConstant.CONTEST_ID, "")
                        .putExtra(IntentConstant.CREATE_OR_JOIN, IntentConstant.CREATE),   AppRequestCodes.UPDATE_ACTIVITY
                )
            }
//            R.id.txt_Signup -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
//            }
        }
    }

    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_contest)
        initViews()
    }
var joined_contest=0
    var contests: MutableList<Contest>? = ArrayList()
    private fun initViews() {
        contests = intent.getParcelableArrayListExtra(IntentConstant.DATA)
        match = intent.getParcelableExtra(IntentConstant.MATCH)
        matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
        joined_contest=intent.getIntExtra("joined_contest",0)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.all_contest)

        setMenu(false, true, false, false)
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
        txt_joined_contest.text = joined_contest.toString()
        txt_MyTeams.text = FantasyApplication.getInstance().teamCount.toString()
        if (FantasyApplication.getInstance().teamCount == 0) {
            ll_viewTeam.visibility = View.GONE
            btn_CreateTeam.visibility = View.VISIBLE
            var count = os.com.application.FantasyApplication.getInstance().teamCount + 1
            btn_CreateTeam.text = getString(R.string.create_team) + " " + count
        } else {
            ll_viewTeam.visibility = View.VISIBLE
            btn_CreateTeam.visibility = View.GONE
        }
        setAdapter()
        btn_CreateTeam.setOnClickListener(this)
        ll_myteam.setOnClickListener(this)
        ll_joinedContests.setOnClickListener(this)
        txt_winning.setOnClickListener(this)
        txt_Winners.setOnClickListener(this)
        txt_Team.setOnClickListener(this)
        txt_EntryFee.setOnClickListener(this)
        sortBySelector(WINNING)
//        txt_Signup.setOnClickListener(this)
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = AllContestAdapter(this, contests, matchType, match, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countTimer != null)
            countTimer!!.stopUpdateTimer()
    }

    private var WINNERS = 1
    private var WINNING = 2
    private var ENTRY_FEE = 3
    private var TEAMS = 4
    fun sortBySelector(value: Int) {
        txt_winning.isSelected = false
        txt_Winners.isSelected = false
        txt_Team.isSelected = false
        txt_EntryFee.isSelected = false
        when (value) {
            WINNERS -> {
                txt_Winners.isSelected = true
            }
            WINNING -> {
                txt_winning.isSelected = true
            }
            ENTRY_FEE -> {
                txt_EntryFee.isSelected = true
            }
            TEAMS -> {
                txt_Team.isSelected = true
            }
        }

    }

}