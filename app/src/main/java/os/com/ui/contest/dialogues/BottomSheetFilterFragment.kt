package os.com.ui.contest.dialogues

import android.app.Dialog
import android.content.Intent
import android.os.Parcelable
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_filter.*
import os.com.R
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.ui.contest.activity.AllContestActivity
import os.com.ui.contest.apiResponse.getContestList.Contest
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate

class BottomSheetFilterFragment : BottomSheetDialogFragment(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.entry_1_100 ->
                dialog.entry_1_100.isSelected = !dialog.entry_1_100.isSelected
            R.id.entry_101_1000 ->
                dialog.entry_101_1000.isSelected = !dialog.entry_101_1000.isSelected
            R.id.entry_1001_5000 ->
                dialog.entry_1001_5000.isSelected = !dialog.entry_1001_5000.isSelected
            R.id.entry_5000_more ->
                dialog.entry_5000_more.isSelected = !dialog.entry_5000_more.isSelected

            R.id.winning_1_10000 ->
                dialog.winning_1_10000.isSelected = !dialog.winning_1_10000.isSelected
            R.id.winning_10001_50000 ->
                dialog.winning_10001_50000.isSelected = !dialog.winning_10001_50000.isSelected
            R.id.winning_50001_1lac ->
                dialog.winning_50001_1lac.isSelected = !dialog.winning_50001_1lac.isSelected
            R.id.winning_1lac_10lac ->
                dialog.winning_1lac_10lac.isSelected = !dialog.winning_1lac_10lac.isSelected
            R.id.winning_10lac_25lac ->
                dialog.winning_10lac_25lac.isSelected = !dialog.winning_10lac_25lac.isSelected
            R.id.winning_25lac_more ->
                dialog.winning_25lac_more.isSelected = !dialog.winning_25lac_more.isSelected

            R.id.contest_confirmed ->
                dialog.contest_confirmed.isSelected = !dialog.contest_confirmed.isSelected
            R.id.contest_multientry ->
                dialog.contest_multientry.isSelected = !dialog.contest_multientry.isSelected
            R.id.contest_multiwinner ->
                dialog.contest_multiwinner.isSelected = !dialog.contest_multiwinner.isSelected

            R.id.contestsize_2 ->
                dialog.contestsize_2.isSelected = !dialog.contestsize_2.isSelected
            R.id.contestsize_3_10 ->
                dialog.contestsize_3_10.isSelected = !dialog.contestsize_3_10.isSelected
            R.id.contestsize_11_20 ->
                dialog.contestsize_11_20.isSelected = !dialog.contestsize_11_20.isSelected
            R.id.contestsize_21_100 ->
                dialog.contestsize_21_100.isSelected = !dialog.contestsize_21_100.isSelected
            R.id.contestsize_101_1000 ->
                dialog.contestsize_101_1000.isSelected = !dialog.contestsize_101_1000.isSelected
            R.id.contestsize_1001_10000 ->
                dialog.contestsize_1001_10000.isSelected = !dialog.contestsize_1001_10000.isSelected
            R.id.contestsize_10001_50000 ->
                dialog.contestsize_10001_50000.isSelected = !dialog.contestsize_10001_50000.isSelected
            R.id.contestsize_50000_more ->
                dialog.contestsize_50000_more.isSelected = !dialog.contestsize_50000_more.isSelected

            R.id.btn_CreateTeam -> {
                filterEntryArray()
            }
        }
    }

    //Bottom Sheet Callback
    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }
    var contestList: ArrayList<Contest> = ArrayList()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        //Get the content View
        val contentView = View.inflate(context, R.layout.bottom_sheet_filter, null)
        dialog.setContentView(contentView)
        //Set the coordinator layout behavior
        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        //Set callback
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        }
        contestList = arguments!!.getParcelableArrayList<Contest>(Tags.DATA)
        match = arguments!!.getParcelable(IntentConstant.MATCH)
        matchType = arguments!!.getInt(IntentConstant.CONTEST_TYPE)

        dialog.txt_close.setOnClickListener { dismiss() }
        dialog.entry_1_100.setOnClickListener(this)
        dialog.entry_101_1000.setOnClickListener(this)
        dialog.entry_1001_5000.setOnClickListener(this)
        dialog.entry_5000_more.setOnClickListener(this)

        dialog.winning_1_10000.setOnClickListener(this)
        dialog.winning_10001_50000.setOnClickListener(this)
        dialog.winning_50001_1lac.setOnClickListener(this)
        dialog.winning_1lac_10lac.setOnClickListener(this)
        dialog.winning_10lac_25lac.setOnClickListener(this)
        dialog.winning_25lac_more.setOnClickListener(this)

        dialog.contest_confirmed.setOnClickListener(this)
        dialog.contest_multientry.setOnClickListener(this)
        dialog.contest_multiwinner.setOnClickListener(this)

        dialog.contestsize_2.setOnClickListener(this)
        dialog.contestsize_3_10.setOnClickListener(this)
        dialog.contestsize_11_20.setOnClickListener(this)
        dialog.contestsize_21_100.setOnClickListener(this)
        dialog.contestsize_101_1000.setOnClickListener(this)
        dialog.contestsize_1001_10000.setOnClickListener(this)
        dialog.contestsize_10001_50000.setOnClickListener(this)
        dialog.contestsize_50000_more.setOnClickListener(this)

        dialog.btn_CreateTeam.setOnClickListener(this)
    }

    fun filterEntryArray() {
        var finalArrayList: ArrayList<Contest> = ArrayList()
        if (dialog.entry_1_100.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.entry_fee.toInt() in 1..100 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.entry_101_1000.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.entry_fee.toInt() in 101..1000 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.entry_1001_5000.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.entry_fee.toInt() in 1001..5000 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.entry_5000_more.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.entry_fee.toInt() > 5000 }
            finalArrayList.addAll(filterContestList)
        }
        finalArrayList.distinct()
        if (!dialog.entry_1_100.isSelected && !dialog.entry_101_1000.isSelected && !dialog.entry_1001_5000.isSelected && !dialog.entry_5000_more.isSelected)
            finalArrayList.addAll(contestList)
        filterWinningArray(finalArrayList)
    }

    fun filterContestSizeArray(contestList: ArrayList<Contest>) {
        var finalArrayList: ArrayList<Contest> = ArrayList()

        if (dialog.contestsize_2.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.total_teams.toInt() == 2 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.contestsize_3_10.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.total_teams.toInt() in 3..10 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.contestsize_11_20.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.total_teams.toInt() in 11..20 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.contestsize_21_100.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.total_teams.toInt() in 21..100 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.contestsize_101_1000.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.total_teams.toInt() in 101..1000 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.contestsize_1001_10000.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.total_teams.toInt() in 1001..10000 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.contestsize_10001_50000.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.total_teams.toInt() in 10001..50000 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.contestsize_50000_more.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.total_teams.toInt() > 50000 }
            finalArrayList.addAll(filterContestList)
        }
        finalArrayList.distinct()
        if (!dialog.contestsize_2.isSelected && !dialog.contestsize_3_10.isSelected
            && !dialog.contestsize_11_20.isSelected && !dialog.contestsize_21_100.isSelected
            && !dialog.contestsize_101_1000.isSelected && !dialog.contestsize_1001_10000.isSelected
            && !dialog.contestsize_10001_50000.isSelected && !dialog.contestsize_50000_more.isSelected
        )
            finalArrayList.addAll(contestList)
        if (!finalArrayList.isEmpty())
            filterValue(finalArrayList)
        else
            AppDelegate.showToast(context,"No Contest Found.")

    }

    fun filterContestArray(contestList: ArrayList<Contest>) {
        var finalArrayList: ArrayList<Contest> = ArrayList()

        if (dialog.contest_multientry.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.multiple_team!! }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.contest_multiwinner.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.total_winners.toInt() > 1 }
            finalArrayList.addAll(filterContestList)
        }
//        if (dialog.contest_confirmed.isSelected) {
//            val filterContestList: List<Contest> = contestList.filter { it.multiple_team!! }
//            finalArrayList.addAll(filterContestList)
//        }

        finalArrayList.distinct()
        if (!dialog.contest_multientry.isSelected && !dialog.contest_multiwinner.isSelected && !dialog.contest_confirmed.isSelected)
            finalArrayList.addAll(contestList)
        filterContestSizeArray(finalArrayList)
    }

    fun filterWinningArray(contestList: ArrayList<Contest>) {
        var finalArrayList: ArrayList<Contest> = ArrayList()

        if (dialog.winning_1_10000.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.prize_money.toInt() in 1..10000 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.winning_10001_50000.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.prize_money.toInt() in 10001..50000 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.winning_50001_1lac.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.prize_money.toInt() in 50001..100000 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.winning_1lac_10lac.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.prize_money.toInt() in 100001..1000000 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.winning_10lac_25lac.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.prize_money.toInt() in 1000000..2500000 }
            finalArrayList.addAll(filterContestList)
        }
        if (dialog.winning_25lac_more.isSelected) {
            val filterContestList: List<Contest> = contestList.filter { it.prize_money.toInt() > 2500000 }
            finalArrayList.addAll(filterContestList)
        }
        finalArrayList.distinct()
        if (!dialog.winning_1_10000.isSelected && !dialog.winning_10001_50000.isSelected
            && !dialog.winning_50001_1lac.isSelected && !dialog.winning_1lac_10lac.isSelected
            && !dialog.winning_10lac_25lac.isSelected && !dialog.winning_25lac_more.isSelected
        )
            finalArrayList.addAll(contestList)
        filterContestArray(finalArrayList)
    }

    fun filterValue(finalArrayList: ArrayList<Contest>) {
        finalArrayList.distinct()
        startActivity(
            Intent(
                context,
                AllContestActivity::class.java
            ).putParcelableArrayListExtra(IntentConstant.DATA, finalArrayList as java.util.ArrayList<out Parcelable>)
                .putExtra(IntentConstant.MATCH, match)
                .putExtra(IntentConstant.CONTEST_TYPE, matchType)
        )
        dialog.dismiss()
    }
}