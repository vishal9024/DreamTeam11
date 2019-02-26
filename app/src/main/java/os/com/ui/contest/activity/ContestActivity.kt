package os.com.ui.contest.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.amlcurran.showcaseview.OnShowcaseEventListener
import com.github.amlcurran.showcaseview.ShowcaseView
import com.github.amlcurran.showcaseview.targets.ViewTarget
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_contest.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_contest.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.PrefConstant
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.contest.adapter.ContestAdapter.ContestMainAdapter
import os.com.ui.contest.apiResponse.FilterModel
import os.com.ui.contest.apiResponse.getContestList.Contest
import os.com.ui.contest.apiResponse.getContestList.ContestCategory
import os.com.ui.contest.dialogues.BottomSheetFilterFragment
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.invite.activity.InviteCodeActivity
import os.com.ui.joinedContest.activity.FixtureJoinedContestActivity
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*


class ContestActivity : BaseActivity(), View.OnClickListener, OnShowcaseEventListener {
    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.ll_myteam -> {
                    startActivity(
                        Intent(this, MyTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                            IntentConstant.CONTEST_TYPE,
                            matchType
                        )
                    )
                }
                R.id.rl_enterContestCode -> {
                    startActivity(Intent(this, InviteCodeActivity::class.java))
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
                R.id.rl_createContest -> {

                    startActivityForResult(
                        Intent(this, CreateContestActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                            IntentConstant.CONTEST_TYPE,
                            matchType
                        ).putExtra(IntentConstant.CONTEST_ID, "")
                            .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.JOIN),
                        AppRequestCodes.CREATE_CONTEST
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
                R.id.ll_AllContest -> {
                    startActivity(
                        Intent(
                            this,
                            AllContestActivity::class.java
                        ).putParcelableArrayListExtra(IntentConstant.DATA, contests!!)
                            .putExtra(IntentConstant.MATCH, match)
                            .putExtra(IntentConstant.CONTEST_TYPE, matchType)
                    )
                }
                R.id.card_view -> {
                    startActivity(
                        Intent(
                            this,
                            AllContestActivity::class.java
                        ).putParcelableArrayListExtra(IntentConstant.DATA, contests!!)
                            .putExtra(IntentConstant.MATCH, match)
                            .putExtra(IntentConstant.CONTEST_TYPE, matchType)
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contest)
        initViews()
    }

    //    var callApi = false
    override fun onResume() {
        super.onResume()
        if (os.com.application.FantasyApplication.getInstance().teamCount == 0) {
            ll_viewTeam.visibility = View.GONE
            btn_CreateTeam.visibility = VISIBLE
            val count = os.com.application.FantasyApplication.getInstance().teamCount + 1
            btn_CreateTeam.text = getString(R.string.create_team) + " " + count
        } else {
            ll_viewTeam.visibility = View.VISIBLE
            btn_CreateTeam.visibility = GONE
        }
        txt_joined_contest.text = FantasyApplication.getInstance().joinedCount.toString()
        txt_MyTeams.text = FantasyApplication.getInstance().teamCount.toString()
        setMenu(false, true, true, false, false)
        if (!pref!!.isLogin)
            ll_ContestType.visibility = GONE
        else
            ll_ContestType.visibility = VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            if (NetworkUtils.isConnected()) {
                callGetContestListApi()
            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
    }

    //    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    private fun initViews() {
        try {
            match = intent.getParcelableExtra(IntentConstant.DATA)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.contest)

            setMenu(false, true, true, false, false)
            setAdapter()
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
                    countTimer!!.startUpdateTimer(this, dateTime, txt_CountDownTimer)
                }
            } else if (matchType == IntentConstant.COMPLETED) {
                txt_CountDownTimer.setText(getString(R.string.completed))
            } else {
                txt_CountDownTimer.setText(getString(R.string.in_progress))
                txt_CountDownTimer.setTextColor(resources.getColor(R.color.dark_yellow))
            }
            if (NetworkUtils.isConnected()) {
                callGetContestListApi()
            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()

            card_view.setOnClickListener(this)
            rl_enterContestCode.setOnClickListener(this)
            rl_createContest.setOnClickListener(this)
            btn_CreateTeam.setOnClickListener(this)
            ll_myteam.setOnClickListener(this)
            ll_joinedContests.setOnClickListener(this)
            ll_AllContest.setOnClickListener(this)
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
            filterBootomSheet()
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun filterBootomSheet() {
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
                var bundle = Bundle()
                bundle.putParcelableArrayList(Tags.DATA, contests)
                bundle.putParcelable(IntentConstant.MATCH, match)
                bundle.putInt(IntentConstant.CONTEST_TYPE, matchType)
                bundle.putInt(IntentConstant.FROM, 0)
                bottomSheetDialogFragment.arguments = bundle
                bottomSheetDialogFragment.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    var joined_contest = 0
    var contests: ArrayList<Contest>? = ArrayList()
    var contestList: ArrayList<ContestCategory> = ArrayList()
    public fun callGetContestListApi() {
//        callApi = false
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        else
            loginRequest[Tags.user_id] = ""
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.match_id] = match!!.match_id/*"13071965317"*/
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@ContestActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .getContestlist(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@ContestActivity)
                if (response.response!!.status) {
                    contestList = response.response!!.data!!.match_contest!!
                    ll_bottom.visibility = View.VISIBLE
                    setAdapter()
                    if (!pref!!.isLogin) {
                        ll_viewTeam.visibility = View.GONE
                        btn_CreateTeam.visibility = VISIBLE
                    } else {
                        if (response.response!!.data!!.my_contests != null) {
                            joined_contest = response.response!!.data!!.my_contests.toInt()
                            FantasyApplication.getInstance().joinedCount =
                                    response.response!!.data!!.my_contests.toInt()
                        }
                        txt_joined_contest.text = response.response!!.data!!.my_contests
                        txt_MyTeams.text = response.response!!.data!!.my_teams
                        if (response.response!!.data!!.my_teams != null) {
                            FantasyApplication.getInstance().teamCount = response.response!!.data!!.my_teams.toInt()
                            if (response.response!!.data!!.my_teams.toInt() == 0) {
                                ll_viewTeam.visibility = View.GONE
                                btn_CreateTeam.visibility = VISIBLE
                                var count = os.com.application.FantasyApplication.getInstance().teamCount + 1
                                btn_CreateTeam.text = getString(R.string.create_team) + " " + count
                            } else {
                                ll_viewTeam.visibility = View.VISIBLE
                                btn_CreateTeam.visibility = GONE
                            }
                        }
                    }
                    if (contestList.isEmpty()) {
                        card_view.visibility = View.GONE
                        ll_viewTeam.visibility = View.GONE
                        btn_CreateTeam.visibility = GONE
                    }
                    contests!!.clear()
                    for (contest in contestList) {
                        contests!!.addAll(contest.contests!!)
                        txt_AllContestCount.text = contests!!.size.toString() + " " + getString(R.string.contest)
                    }
//                    instruction()
                } else {
                    logoutIfDeactivate(response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@ContestActivity)
            }
        }
    }

     fun instruction() {
        if (!pref!!.getBooleanValuefromTemp(PrefConstant.SKIP_CONTEST_INSTRUCTION, false)) {
            pref!!.putBooleanValueinTemp(PrefConstant.SKIP_CONTEST_INSTRUCTION, true)
            callIntroductionScreen(
                R.id.ll_totalWinnings,
                getString(R.string.winnings),
                "Total amount to be won",
                ShowcaseView.BELOW_SHOWCASE
            )
        }
    }

     fun instructionSkip() {
        counterValue = 2
        if (!pref!!.getBooleanValuefromTemp(PrefConstant.SKIP_CONTEST_INSTRUCTION, false)) {
            pref!!.putBooleanValueinTemp(PrefConstant.SKIP_CONTEST_INSTRUCTION, true)
            callIntroductionScreen(
                R.id.ll_entry,
                getString(R.string.entry),
                "Amount to pay to join this contest"
                ,
                ShowcaseView.BELOW_SHOWCASE
            )
        }
    }

    var counterValue = 0
    override fun onShowcaseViewShow(showcaseView: ShowcaseView?) {
    }

    override fun onShowcaseViewHide(showcaseView: ShowcaseView?) {
        when (counterValue) {
            1 -> callIntroductionScreen(
                R.id.ll_totalWinners,
                "No. of Winners",
                "Tap here to see the winning breakup", ShowcaseView.BELOW_SHOWCASE
            )
            2 -> callIntroductionScreen(
                R.id.ll_entryFee,
                getString(R.string.entry),
                "Amount to pay to join this contest"
                ,
                ShowcaseView.BELOW_SHOWCASE
            )
            3 -> {
                callIntroductionScreen(
                    R.id.txt_EndValue,
                    "Contest Limit",
                    "Max no. of teams that can join this contest"
                    ,
                    ShowcaseView.BELOW_SHOWCASE
                )

            }
//            4 -> callIntroductionScreen(
//                R.id.ll_players,
//                "Player Counter",
//                "Pick 11 players to create your team"
//                ,
//                ShowcaseView.ABOVE_SHOWCASE
//            )
        }
    }

    override fun onShowcaseViewDidHide(showcaseView: ShowcaseView?) {
    }

    override fun onShowcaseViewTouchBlocked(motionEvent: MotionEvent?) {
    }

    var position = 0
    var scv: ShowcaseView.Builder? = null
    var buil: ShowcaseView? = null
    fun callIntroductionScreen(
        target: Int,
        title: String,
        description: String,
        abovE_SHOWCASE: Int
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            scv = ShowcaseView.Builder(this@ContestActivity)
                .withMaterialShowcase()
//                .setTarget(ViewTarget(target, this@ContestActivity))
                .setContentTitle(title)
                .setContentText(description)
                .hideOnTouchOutside()
                .setStyle(R.style.CustomShowcaseTheme)
                .setShowcaseEventListener(this@ContestActivity)
            counterValue = counterValue + 1
            buil = scv!!.build()
            buil!!.hideButton()
            buil!!.setShowcase(ViewTarget(target, this@ContestActivity), true)
//            buil!!.setFadeDurations(0,0)
            buil!!.forceTextPosition(abovE_SHOWCASE)
            delay(2500)
            buil!!.hide()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FantasyApplication.getInstance().filterModel = FilterModel()
        if (countTimer != null)
            countTimer!!.stopUpdateTimer()
    }

    var contestMainAdapter: ContestMainAdapter? = null
    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        contestMainAdapter = ContestMainAdapter(this, contestList, match, matchType)
        rv_Contest!!.adapter = contestMainAdapter
        if (!contestList.isEmpty())
            card_view.visibility = View.VISIBLE
    }
}