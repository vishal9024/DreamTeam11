package os.com.ui.winningBreakup.dialogues

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_winninglist.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.data.Prefs
import os.com.networkCall.ApiClient
import os.com.ui.winningBreakup.adapter.WinningsListAdapter
import os.com.ui.winningBreakup.apiResponse.contestPriceBreakupResponse.Data
import os.com.ui.winningBreakup.apiResponse.contestPriceBreakupResponse.PriceBreakUp
import os.com.utils.AppDelegate

class BottomSheetWinningListFragment() : BottomSheetDialogFragment() {

    init {
//        val contest_id = arguments!!.getString(Tags.contest_id)
//        callWinningBreakupApi(contest_id)
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

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        //Get the content View
        val contentView = View.inflate(context, R.layout.bottom_sheet_winninglist, null)
        dialog.setContentView(contentView)
        //Set the coordinator layout behavior
        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        //Set callback
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        val data = arguments!!.getParcelable<Data>(Tags.contest_id)
        dialog.txt_prizeMoney.text = getString(R.string.Rs) + " " + data!!.winning_price
        setAdapter(data!!.breakup_detail!!)
        dialog.img_Close.setOnClickListener { dismiss() }
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(breakup_detail: ArrayList<PriceBreakUp>) {

        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        dialog.rv_Prize!!.layoutManager = llm
        dialog.rv_Prize!!.adapter = WinningsListAdapter(context!!, breakup_detail)
    }
}