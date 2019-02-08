package os.com.ui.contest.dialogues

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_winner.*
import os.com.R
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickRecyclerView
import os.com.ui.contest.adapter.BottomSheetWinnerListAdapter
import os.com.ui.contest.apiResponse.contestSizePriceBreakUp.Data

class BottomSheetWinnerListFragment : BottomSheetDialogFragment() {
    //Bottom Sheet Callback

    var onClickFilter: OnClickWinning? = null

    fun setOnClickWinningListener(activity: Activity) {
        onClickFilter = activity as OnClickWinning
    }

    interface OnClickWinning {
        fun onClick(tag: String, position: Int)
    }

    var winning_amount = ""
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
    var data: ArrayList<Data> = ArrayList()
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
            data = arguments!!.getParcelableArrayList<Data>(Tags.DATA)
            winning_amount = arguments!!.getString(IntentConstant.WINNING_AMOUNT)
            setAdapter(data)
            dialog.img_Close.setOnClickListener { dismiss() }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(data: java.util.ArrayList<Data>?) {
        try {
            val llm = LinearLayoutManager(context)
            llm.orientation = LinearLayoutManager.VERTICAL
            dialog.rv_rank!!.layoutManager = llm
            dialog.rv_rank!!.adapter =
                    BottomSheetWinnerListAdapter(context!!, data, winning_amount, object : OnClickRecyclerView {
                        override fun onClickItem(tag: String, position: Int) {
                            for (i in data!!.indices) {
                                data[i].isSelected = false
                            }
                            data[position].isSelected = true
                            dialog.rv_rank!!.adapter!!.notifyDataSetChanged()
                            onClickFilter!!.onClick("click", position)
                            dismiss()
                        }
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}