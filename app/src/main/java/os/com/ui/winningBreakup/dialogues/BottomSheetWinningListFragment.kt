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
import os.com.ui.winningBreakup.apiResponse.contestPriceBreakupResponse.PriceBreakUp
import os.com.utils.AppDelegate

class BottomSheetWinningListFragment() : BottomSheetDialogFragment() {

    init {
        val contest_id = arguments!!.getString(Tags.contest_id)
        callWinningBreakupApi(contest_id)
    }

    private fun callWinningBreakupApi(contest_id: String) {
        val loginRequest = HashMap<String, String>()
        if (Prefs(activity).isLogin)
            loginRequest[Tags.user_id] = Prefs(activity).userdata!!.user_id
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.contest_id] = contest_id
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(activity!!)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .contest_price_breakup(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(activity!!)
                if (response.response!!.status) {
                    dialog.txt_prizeMoney.text=getString(R.string.Rs)+" "+response.response!!.data!!.winning_price
                    setAdapter(response.response!!.data!!.breakup_detail!!)
                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(activity!!)
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
        }
        dialog.img_Close.setOnClickListener { dismiss() }
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(breakup_detail: ArrayList<PriceBreakUp>) {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        dialog.rv_Prize!!.layoutManager = llm
        dialog.rv_Prize!!.adapter = WinningsListAdapter(context!!,breakup_detail)
    }
}