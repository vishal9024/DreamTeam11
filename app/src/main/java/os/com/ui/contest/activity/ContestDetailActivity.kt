package os.com.ui.contest.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_megacontest.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_megacontest.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.contest.adapter.TeamsAdapter
import os.com.ui.winningBreakup.dialogues.BottomSheetWinningListFragment


class ContestDetailActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_CreateTeam -> {
                startActivity(Intent(this, ChooseTeamActivity::class.java))
            }
            R.id.ll_winners -> {
                val bottomSheetDialogFragment = BottomSheetWinningListFragment()
                bottomSheetDialogFragment.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")
            }
            R.id.txt_Join -> {
                showJoinContestDialogue(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_megacontest)
        initViews()
    }


    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.mega_contest)
        setMenu(false, false, false, false)
        setAdapter()
        val  mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
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