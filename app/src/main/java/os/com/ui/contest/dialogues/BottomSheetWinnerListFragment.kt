package os.com.ui.contest.dialogues

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_winner.*
import os.com.R
import os.com.ui.contest.adapter.BottomSheetWinnerListAdapter

class BottomSheetWinnerListFragment : BottomSheetDialogFragment() {
    //Bottom Sheet Callback
    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            try {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        try {
            //Get the content View
            val contentView = View.inflate(context, R.layout.bottom_sheet_winner, null)
            dialog.setContentView(contentView)

            //Set the coordinator layout behavior
            val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior

            //Set callback
            if (behavior != null && behavior is BottomSheetBehavior<*>) {
                behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
            }
            setAdapter()
            dialog.img_Close.setOnClickListener { dismiss() }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        try {
            val llm = LinearLayoutManager(context)
            llm.orientation = LinearLayoutManager.VERTICAL
            dialog.rv_rank!!.layoutManager = llm
            dialog.rv_rank!!.adapter = BottomSheetWinnerListAdapter(context!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}