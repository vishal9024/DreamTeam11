package os.com.ui.contest.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_all_contest.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickRecyclerView
import os.com.ui.contest.adapter.AllContestAdapter
import os.com.ui.contest.apiResponse.getContestList.Contest
import os.com.ui.createTeam.activity.myTeam.MyTeamActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.activity.FixtureJoinedContestActivity
import os.com.utils.CountTimer


class AllContestActivity : BaseActivity(), View.OnClickListener, OnClickRecyclerView {
    override fun onClickItem(tag: String, position: Int) {
        if (tag.equals(Tags.JoinContestDialog)){
            showJoinContestDialogue(this, match!!, contests!![position].contest_id, matchType)
        }
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
                startActivity(Intent(this, MyTeamActivity::class.java))
            }
            R.id.ll_joinedContests -> {
                startActivity(Intent(this, FixtureJoinedContestActivity::class.java))
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

    var contests: MutableList<Contest>? = ArrayList()
    private fun initViews() {
        contests = intent.getParcelableArrayListExtra(IntentConstant.DATA)
        match = intent.getParcelableExtra(IntentConstant.MATCH)
        matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
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
        setAdapter()
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
        rv_Contest!!.adapter = AllContestAdapter(this, contests,this)
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