package os.com.ui.joinedContest.dialogues

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.bottom_sheet_point_breakup.*
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.ui.joinedContest.adapter.PriceBreakUpAdapter
import os.com.ui.joinedContest.apiResponse.getSeriesPlayerListResponse.Data
import os.com.ui.joinedContest.apiResponse.getSeriesPlayerListResponse.PriceBreakUpData

class BottomSheetPriceBreakUpFragment() : BottomSheetDialogFragment() {

    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }
    var priceBreakUpList: MutableMap<String, PriceBreakUpData> = HashMap()
    var priceBreakUp: ArrayList<String> = ArrayList()
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        //Get the content View
        val contentView = View.inflate(context, R.layout.bottom_sheet_point_breakup, null)
        dialog.setContentView(contentView)
        //Set the coordinator layout behavior
        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        //Set callback
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        val data = arguments!!.getParcelable<Data>(Tags.DATA)

        addData(data)
        setData(data)

        dialog.img_Close.setOnClickListener { dismiss() }
    }

    private fun setData(data: Data?) {
        ImageLoader.getInstance().displayImage(
            data!!.player_image,
            dialog.imvUserProfile,
            FantasyApplication.getInstance().options
        )
        dialog.txtPoints.text = data.points
        dialog.txt_label.text = data.player_name
        dialog.txtSelectedBy.text = data.selection_percent
        dialog.txtCredits.text = data.player_credit
        if (!data.team_number!!.isEmpty()) {
            var name = StringBuilder()
            for (dat in data.team_number!!)
                name.append("T" + dat).append(", ")

            var finalName = name.toString()
            if (finalName.length > 3)
                finalName = finalName.substring(0, finalName.length - 2)
            dialog.txt_status.text = finalName
            dialog.img_playerSelected.isSelected = true
        } else {
            dialog.txt_status.text = getString(R.string.not_in_your_team)
            dialog.img_playerSelected.isSelected = false
        }
    }

    private fun addData(data: Data) {
        if (data.player_breckup!!.starting11 != null) {
            priceBreakUpList.put("Starting 11", data.player_breckup!!.starting11!!)
            priceBreakUp.add("Starting 11")
        }
        if (data.player_breckup!!.runs != null) {
            priceBreakUpList.put("Runs", data.player_breckup!!.runs!!)
            priceBreakUp.add("Runs")
        }
        if (data.player_breckup!!.fours != null) {
            priceBreakUpList.put("4's", data.player_breckup!!.fours!!)
            priceBreakUp.add("4's")
        }
        if (data.player_breckup!!.sixes != null) {
            priceBreakUpList.put("6's", data.player_breckup!!.sixes!!)
            priceBreakUp.add("6's")
        }
        if (data.player_breckup!!.strike_rate != null) {
            priceBreakUpList.put("S/R", data.player_breckup!!.strike_rate!!)
            priceBreakUp.add("S/R")
        }
        if (data.player_breckup!!.half_century != null) {
            priceBreakUpList.put("50", data.player_breckup!!.half_century!!)
            priceBreakUp.add("50")
        }
        if (data.player_breckup!!.duck != null) {
            priceBreakUpList.put("Duck", data.player_breckup!!.duck!!)
            priceBreakUp.add("Duck")
        }
        if (data.player_breckup!!.wickets != null) {
            priceBreakUpList.put("Wkts", data.player_breckup!!.wickets!!)
            priceBreakUp.add("Wkts")
        }
        if (data.player_breckup!!.maiden_over != null) {
            priceBreakUpList.put("Maiden Over", data.player_breckup!!.maiden_over!!)
            priceBreakUp.add("Maiden Over")
        }
        if (data.player_breckup!!.eco_rate != null) {
            priceBreakUpList.put("E/R", data.player_breckup!!.eco_rate!!)
            priceBreakUp.add("E/R")
        }
        if (data.player_breckup!!.bonus != null) {
            priceBreakUpList.put("Bonus", data.player_breckup!!.bonus!!)
            priceBreakUp.add("Bonus")
        }
        if (data.player_breckup!!.catch != null) {
            priceBreakUpList.put("Catch", data.player_breckup!!.catch!!)
            priceBreakUp.add("Catch")
        }
        if (data.player_breckup!!.run_outStumping != null) {
            priceBreakUpList.put("Run Out/Stumping", data.player_breckup!!.run_outStumping!!)
            priceBreakUp.add("Run Out/Stumping")
        }
        if (data.player_breckup!!.total_point != null) {
            priceBreakUpList.put("Total Point", data.player_breckup!!.total_point!!)
            priceBreakUp.add("Total Point")
        }

        setAdapter()
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        dialog.rv_rank!!.layoutManager = llm
        dialog.rv_rank!!.adapter = PriceBreakUpAdapter(context!!, priceBreakUp, priceBreakUpList)
    }
}


