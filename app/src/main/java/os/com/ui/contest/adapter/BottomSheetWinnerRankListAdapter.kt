package os.com.ui.contest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_winners_list_bottomsheet_item.view.*
import os.com.R
import os.com.ui.contest.apiResponse.contestSizePriceBreakUp.Info


class BottomSheetWinnerRankListAdapter(val mContext: Context) :
    RecyclerView.Adapter<BottomSheetWinnerRankListAdapter.AppliedCouponCodeHolder>() {
    var info: ArrayList<Info> = ArrayList()
    var winning_amount = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_winners_list_bottomsheet_item, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            val calculatePercentageAmount = (info[position].percent!!.toFloat() * winning_amount.toFloat()) / 100
            holder.itemView.txt_rank.text = info[position].rank_size
            holder.itemView.txt_percent.text = info[position].percent +" %"
            holder.itemView.txt_amount.text = mContext.getString(R.string.Rs)+" "+calculatePercentageAmount.toString()

        } catch (e: Exception) {
            holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
        }

    }

    fun infoList(data: ArrayList<Info>, winning_amount: String) {
        this.winning_amount = winning_amount
        if (info != data) {
            info = data
            notifyDataSetChanged()
        }
    }


    override fun getItemCount(): Int {
        return info.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}