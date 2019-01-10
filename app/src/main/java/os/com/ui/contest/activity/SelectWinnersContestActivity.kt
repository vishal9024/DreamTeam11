package os.com.ui.contest.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_contest.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_select_winners_contest.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.ui.contest.adapter.WinnerListAdapter
import os.com.ui.contest.dialogues.BottomSheetWinnerListFragment


class SelectWinnersContestActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.btn_CreateSelectContest -> {
                    finish()
                }
                R.id.ll_winnersSelect -> {
                    val bottomSheetDialogFragment = BottomSheetWinnerListFragment()
                    bottomSheetDialogFragment.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_winners_contest)
        initViews()
    }

    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.create_contest)
            setMenu(false, false, false, false)
            winnerBootomSheet()
            setAdapter()
            btn_CreateSelectContest.setOnClickListener(this)
            ll_winnersSelect.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        try {
            val llm = LinearLayoutManager(this)
            llm.orientation = LinearLayoutManager.VERTICAL
            rv_rank!!.layoutManager = llm
            val winnerListAdapter = WinnerListAdapter(this)
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