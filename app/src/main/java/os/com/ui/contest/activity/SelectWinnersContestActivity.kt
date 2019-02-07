package os.com.ui.contest.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_contest.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_select_winners_contest.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.ui.contest.adapter.BottomSheetWinnerRankListAdapter
import os.com.ui.contest.apiResponse.contestSizePriceBreakUp.Data
import os.com.ui.contest.dialogues.BottomSheetWinnerListFragment
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.CountTimer


class SelectWinnersContestActivity : BaseActivity(), View.OnClickListener,
    BottomSheetWinnerListFragment.OnClickWinning {
    override fun onClick(tag: String, position: Int) {
        winnerListAdapter!!.infoList(priceBreakUpList[position].info!!, winning_amount)
        winnerListAdapter!!.notifyDataSetChanged()
    }

    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.btn_CreateSelectContest -> {
                    finish()
                }
                R.id.ll_winnersSelect -> {
                    val bottomSheetDialogFragment = BottomSheetWinnerListFragment()
                    val bundle = Bundle()
                    bundle.putParcelableArrayList(Tags.DATA, priceBreakUpList)
                    bundle.putString(IntentConstant.WINNING_AMOUNT, winning_amount)
                    bottomSheetDialogFragment.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is BottomSheetWinnerListFragment) {
            val headlinesFragment = fragment as BottomSheetWinnerListFragment
            headlinesFragment.setOnClickWinningListener(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_winners_contest)
        initViews()
    }

    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    var createOrJoin = AppRequestCodes.JOIN
    var allow_multiple_teams = false
    var winning_amount = ""
    var contest_size = ""
    var team_name = "MY NEW CONTEST"

    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.create_contest)
            setMenu(false, false, false, false, false)
            winnerBootomSheet()
            getIntentData()
            setAdapter()
            btn_CreateSelectContest.setOnClickListener(this)
            ll_winnersSelect.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var priceBreakUpList: ArrayList<Data> = ArrayList()
    private fun getIntentData() {
        if (intent != null) {
            createOrJoin = intent.getIntExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.CREATE_CONTEST)
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
            contest_size = intent.getStringExtra(IntentConstant.CONTEST_SIZE)
            winning_amount = intent.getStringExtra(IntentConstant.WINNING_AMOUNT)
            team_name = intent.getStringExtra(IntentConstant.TEAM_NAME)
            allow_multiple_teams = intent.getBooleanExtra(IntentConstant.ALLOW_MULTIPLE_TEAMS, false)
            priceBreakUpList = intent.getParcelableArrayListExtra(IntentConstant.DATA)

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
                    countTimer!!.startUpdateTimer(dateTime, txt_CountDownTimer)
                }
            } else if (matchType == IntentConstant.COMPLETED) {
                txt_CountDownTimer.setText(getString(R.string.completed))
            } else
                txt_CountDownTimer.setText(getString(R.string.in_progress))
        }
    }

    var winnerListAdapter: BottomSheetWinnerRankListAdapter? = null
    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        try {
            val llm = LinearLayoutManager(this)
            llm.orientation = LinearLayoutManager.VERTICAL
            rv_rank!!.layoutManager = llm
            rv_rank!!.adapter = winnerListAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun winnerBootomSheet() {
        try {
            val mBottomSheetBehaviorfilter = BottomSheetBehavior.from(bottom_sheet_filter)
            mBottomSheetBehaviorfilter.state = BottomSheetBehavior.STATE_COLLAPSED
            mBottomSheetBehaviorfilter.peekHeight = 0
            //If you want to handle callback of Sheet Behavior you can use below code
            mBottomSheetBehaviorfilter.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}