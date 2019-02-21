package os.com.ui.joinedContest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_winners_list.view.*
import os.com.R
import os.com.ui.joinedContest.apiResponse.getSeriesPlayerListResponse.PriceBreakUpData


class PriceBreakUpAdapter(
    val mContext: Context,
    val breakup_detail: ArrayList<String>,
    val priceBreakUpList: MutableMap<String, PriceBreakUpData>
) : RecyclerView.Adapter<PriceBreakUpAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_winners_list, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
//        try {
//            if (position % 2 == 0) {
//                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
//            } else {
//                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.white))
//            }
//        } catch (e: Exception) {
//            holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
//        }
        holder.itemView.txt_TeamName.text = breakup_detail[position]
        holder.itemView.txt_actual.text = priceBreakUpList[breakup_detail[position]]!!.actual
        holder.itemView.txt_rank.text = priceBreakUpList[breakup_detail[position]]!!.points
        if (breakup_detail[position].equals("Starting 11") || breakup_detail[position].equals("Duck")) {
            if (!priceBreakUpList[breakup_detail[position]]!!.actual.isEmpty()) {
                if (priceBreakUpList[breakup_detail[position]]!!.actual.equals("1"))
                    holder.itemView.txt_actual.text = "Yes"
                else
                    holder.itemView.txt_actual.text = "No"
            } else {
                holder.itemView.txt_actual.text = "No"
            }
        }

        if(priceBreakUpList[breakup_detail[position]]!!.actual.isEmpty()){
            holder.itemView.txt_actual.text = "0"
        }

    }


    override fun getItemCount(): Int {
        return breakup_detail.size
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}