package os.com.ui.contest.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_all_contest.*
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
import os.com.ui.contest.dialogues.BottomSheetFilterFragment
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamSelectActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.activity.FixtureJoinedContestActivity
import os.com.utils.networkUtils.NetworkUtils


class AllContestActivity : BaseActivity(), View.OnClickListener, OnClickRecyclerView,
    BottomSheetFilterFragment.OnClickFilter {
    override fun onClick(tag: String, finalArrayList: ArrayList<Contest>) {
        if (tag.equals("DATA")) {
            setAdapter(finalArrayList)
        }
    }

    override fun onClickItem(tag: String, position: Int) {
        if (tag.equals(Tags.JoinContestDialog)) {

            if (FantasyApplication.getInstance().teamCount == 0) {
                startActivityForResult(
                    Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    ).putExtra(IntentConstant.CONTEST_ID, contests!![position].contest_id)
                        .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.JOIN), AppRequestCodes.UPDATE_ACTIVITY
                )
            } else if (FantasyApplication.getInstance().teamCount == 1) {
                if (NetworkUtils.isConnected()) {
                    checkAmountWallet(
                        match!!.match_id,
                        match!!.series_id, contests!![position].contest_id, "", object : OnClickDialogue {
                            override fun onClick(tag: String, success: Boolean) {
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
                    ).putExtra(IntentConstant.CONTEST_ID, contests!![position].contest_id).putExtra(
                        IntentConstant.FOR,
                        AppRequestCodes.JOIN
                    ), AppRequestCodes.UPDATEVIEW
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
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is BottomSheetFilterFragment) {
            val headlinesFragment = fragment as BottomSheetFilterFragment
            headlinesFragment.setOnFilterListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (os.com.application.FantasyApplication.getInstance().teamCount == 0) {
            ll_viewTeam.visibility = View.GONE
            btn_CreateTeam.visibility = VISIBLE
            var count = os.com.application.FantasyApplication.getInstance().teamCount + 1
            btn_CreateTeam.text = getString(R.string.create_team) + " " + count
        } else {
            ll_viewTeam.visibility = View.VISIBLE
            btn_CreateTeam.visibility = GONE
        }
        txt_joined_contest.text = FantasyApplication.getInstance().joinedCount.toString()
        txt_MyTeams.text = FantasyApplication.getInstance().teamCount.toString()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.rl_winning -> {
                sortBySelector(WINNING)
            }
            R.id.rl_Winners -> {
                sortBySelector(WINNERS)
            }
            R.id.rl_Team -> {
                sortBySelector(TEAMS)
            }
            R.id.rl_EntryFee -> {
                sortBySelector(ENTRY_FEE)
            }
            R.id.ll_myteam -> {
                startActivity(
                    Intent(this, MyTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    )
                )
            }
            R.id.ll_joinedContests -> {
                startActivity(
                    Intent(this, FixtureJoinedContestActivity::class.java).putExtra(
                        IntentConstant.MATCH,
                        match
                    ).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    )
                )
            }
            R.id.btn_CreateTeam -> {
                startActivityForResult(
                    Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    ).putExtra(IntentConstant.CONTEST_ID, "")
                        .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.CREATE),
                    AppRequestCodes.UPDATE_ACTIVITY
                )
            }
//            R.id.txt_Signup -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
//            }
        }
    }

//    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    var FROM = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_contest)
        initViews()
    }

    var joined_contest = 0
    var contests: ArrayList<Contest>? = ArrayList()
    var filterContests: ArrayList<Contest>? = ArrayList()
    private fun initViews() {
        contests = intent.getParcelableArrayListExtra(IntentConstant.DATA)
        match = intent.getParcelableExtra(IntentConstant.MATCH)
        matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
        FROM = intent.getIntExtra(IntentConstant.FROM, 0)
        joined_contest = intent.getIntExtra("joined_contest", 0)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.all_contest)
        if (FROM == 1) {
            setMenu(false, true, true, false, false)
            filterContests = intent.getParcelableArrayListExtra(IntentConstant.CONTEST)
        } else
            setMenu(false, true, false, false, false)
        var localTeamName = match!!.local_team_name
        var visitorTeamName = match!!.visitor_team_name
        if (match!!.local_team_name.length > 5)
            localTeamName = match!!.local_team_name.substring(0, 4)
        if (match!!.visitor_team_name.length > 5)
            visitorTeamName = match!!.visitor_team_name.substring(0, 4)

        txt_matchVS.text = localTeamName + " " + getString(R.string.vs) + " " + visitorTeamName
        if (matchType == IntentConstant.FIXTURE) {
            if (!match!!.star_date.isEmpty()) {
                val strt_date = match!!.star_date.split("T")
                val dateTime = strt_date.get(0) + " " + match!!.star_time
                countTimer!!.startUpdateTimer(this,dateTime, txt_CountDownTimer)
            }
        } else if (matchType == IntentConstant.COMPLETED) {
            txt_CountDownTimer.setText(getString(R.string.completed))
        } else {
            txt_CountDownTimer.setText(getString(R.string.in_progress))
            txt_CountDownTimer.setTextColor(resources.getColor(R.color.dark_yellow))
        }
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
        filterBootomSheet()
        if (FROM == 1)
            setAdapter(filterContests)
        else
            setAdapter(contests)
        btn_CreateTeam.setOnClickListener(this)
        ll_myteam.setOnClickListener(this)
        ll_joinedContests.setOnClickListener(this)
        rl_winning.setOnClickListener(this)
        rl_Winners.setOnClickListener(this)
        rl_Team.setOnClickListener(this)
        rl_EntryFee.setOnClickListener(this)
        sortBySelector(WINNING)
//        txt_Signup.setOnClickListener(this)
    }

    private fun filterBootomSheet() {
        val mBottomSheetBehaviorfilter = BottomSheetBehavior.from(bottom_sheet_filter)
        mBottomSheetBehaviorfilter.state = BottomSheetBehavior.STATE_COLLAPSED
        mBottomSheetBehaviorfilter.peekHeight = 0
        mBottomSheetBehaviorfilter.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter -> {
                val view = findViewById<View>(R.id.menu_filter)
                val bottomSheetDialogFragment = BottomSheetFilterFragment()
                val bundle = Bundle()
                bundle.putParcelableArrayList(Tags.DATA, contests!!)
                bundle.putParcelable(IntentConstant.MATCH, match)
                bundle.putInt(IntentConstant.CONTEST_TYPE, matchType)
                bundle.putInt(IntentConstant.FROM, 1)
                bottomSheetDialogFragment.arguments = bundle
                bottomSheetDialogFragment.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(filterContests: ArrayList<Contest>?) {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = AllContestAdapter(this, filterContests, matchType, match, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countTimer != null)
            countTimer!!.stopUpdateTimer()
    }

    fun selectorWINNERS(p: Contest): Int = p.total_winners.toInt()
    fun selectorWINNING(p: Contest): Int = p.prize_money.toInt()
    fun selectorENTRY_FEE(p: Contest): Double = p.entry_fee.toDouble()
    fun selectorTEAMS(p: Contest): Long = p.total_teams.toLong()
    private var WINNERS = 1
    private var WINNING = 2
    private var ENTRY_FEE = 3
    private var TEAMS = 4
    var isAsc = true

    fun sortBySelector(value: Int) {
        txt_winning.isSelected = false
        txt_Winners.isSelected = false
        txt_Team.isSelected = false
        txt_EntryFee.isSelected = false

        txt_Winners.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        txt_winning.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        txt_EntryFee.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        txt_Team.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        when (value) {
            WINNERS -> {
                txt_Winners.isSelected = true
                if (isAsc) {
                    isAsc = false
                    contests!!.sortBy { selectorWINNERS(it) }
                    txt_Winners.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowup, 0);
                } else {
                    isAsc = true
                    contests!!.sortByDescending { selectorWINNERS(it) }
                    txt_Winners.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowdown, 0);
                }

                rv_Contest!!.adapter!!.notifyDataSetChanged()
            }
            WINNING -> {
                txt_winning.isSelected = true
                if (isAsc) {
                    isAsc = false
                    contests!!.sortByDescending { selectorWINNING(it) }
                    txt_winning.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowdown, 0);
                } else {
                    isAsc = true
                    contests!!.sortBy { selectorWINNING(it) }
                    txt_winning.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowup, 0);
                }
                rv_Contest!!.adapter!!.notifyDataSetChanged()
            }
            ENTRY_FEE -> {
                txt_EntryFee.isSelected = true
                if (isAsc) {
                    isAsc = false
                    contests!!.sortByDescending { selectorENTRY_FEE(it) }
                    txt_EntryFee.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowdown, 0);
                } else {
                    isAsc = true
                    contests!!.sortBy { selectorENTRY_FEE(it) }
                    txt_EntryFee.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowup, 0);
                }
                rv_Contest!!.adapter!!.notifyDataSetChanged()
            }
            TEAMS -> {
                txt_Team.isSelected = true
                if (isAsc) {
                    isAsc = false
                    contests!!.sortByDescending { selectorTEAMS(it) }
                    txt_Team.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowdown, 0);
                } else {
                    isAsc = true
                    contests!!.sortBy { selectorTEAMS(it) }
                    txt_Team.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowup, 0);
                }
                rv_Contest!!.adapter!!.notifyDataSetChanged()
            }
        }
    }
}