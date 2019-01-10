package os.com.ui.contest.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_megacontest.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_megacontest.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.interfaces.OnClickDialogue
import os.com.ui.contest.adapter.TeamsAdapter
import os.com.ui.contest.apiResponse.getContestList.Contest
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamSelectActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.winningBreakup.dialogues.BottomSheetWinningListFragment
import os.com.utils.CountTimer
import os.com.utils.networkUtils.NetworkUtils


class ContestDetailActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_CreateTeam -> {
                startActivityForResult(
                    Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    ). putExtra(IntentConstant.CONTEST_ID,contest!!.contest_id)
                        .putExtra(IntentConstant.CREATE_OR_JOIN, IntentConstant.CREATE),
                    AppRequestCodes.UPDATE_ACTIVITY
                )
            }
            R.id.ll_winners -> {
                val bottomSheetDialogFragment = BottomSheetWinningListFragment()
                bottomSheetDialogFragment.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")
            }
            R.id.txt_Join -> {
                if (FantasyApplication.getInstance().teamCount==0) {
                    startActivityForResult(
                        Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                            IntentConstant.CONTEST_TYPE,
                            matchType
                        ).putExtra(IntentConstant.CONTEST_ID, contest!!.contest_id)
                            .putExtra(IntentConstant.CREATE_OR_JOIN, IntentConstant.JOIN),AppRequestCodes.UPDATE_ACTIVITY
                    )
                }else if (FantasyApplication.getInstance().teamCount==1){
                    if (NetworkUtils.isConnected()) {
                        checkAmountWallet(
                            match!!.match_id,
                            match!!.series_id,  contest!!.contest_id, "",object: OnClickDialogue {
                                override fun onClick(tag: String, success: Boolean) {
//                                    if (tag.equals(Tags.success) && success)
//                                        finish()
                                }

                            }
                        )
                    } else
                        Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
                }else{
                    startActivityForResult(
                        Intent(this, MyTeamSelectActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
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
        if (resultCode== Activity.RESULT_OK)
            if (NetworkUtils.isConnected()) {
//                callGetTeamListApi()
            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
    }

    var contest: Contest ?=null
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
        toolbarTitleTv.setText(R.string.mega_contest)
        setMenu(false, false, false, false)
        setAdapter()
        val mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        //By default set BottomSheet Behavior as Collapsed and Height 0
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        mBottomSheetBehavior.peekHeight = 0

        //If you want to handle callback of Sheet Behavior you can use below code
        mBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
//                    BottomSheetBehavior.STATE_COLLAPSED -> Log.d(FragmentActivity.TAG, "State Collapsed")
//                    BottomSheetBehavior.STATE_DRAGGING -> Log.d(FragmentActivity.TAG, "State Dragging")
//                    BottomSheetBehavior.STATE_EXPANDED -> Log.d(FragmentActivity.TAG, "State Expanded")
//                    BottomSheetBehavior.STATE_HIDDEN -> Log.d(FragmentActivity.TAG, "State Hidden")
//                    BottomSheetBehavior.STATE_SETTLING -> Log.d(FragmentActivity.TAG, "State Settling")
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        ll_winners.setOnClickListener(this)
        btn_CreateTeam.setOnClickListener(this)
        txt_Join.setOnClickListener(this)
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = TeamsAdapter(this)
    }
}