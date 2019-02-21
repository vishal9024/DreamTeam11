package os.com.ui.contest.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_winners_list_bottomsheet.view.*
import os.com.R
import os.com.interfaces.OnClickRecyclerView
import os.com.ui.contest.apiResponse.contestSizePriceBreakUp.Data
import java.util.*


class BottomSheetWinnerListAdapter(
    val mContext: Context,
    var data: ArrayList<Data>?,
    var winning_amount: String,
    val clickRecyclerView: OnClickRecyclerView
) :
    RecyclerView.Adapter<BottomSheetWinnerListAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_winners_list_bottomsheet, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            if (position == 0)
                holder.itemView.txt_team.text = data!![position].title + " "+ mContext.getString(R.string.winners) +" "+ mContext.getString(R.string.recommended)
            else
                holder.itemView.txt_team.text = data!![position].title + " "+ mContext.getString(R.string.winners)
            holder.itemView.img_check.isSelected = data!![position].isSelected
            holder.itemView.ll.setOnClickListener {
                clickRecyclerView.onClickItem("click", position)
            }
            holder.contestAdapter.infoList(data!![position].info!!, winning_amount)
            holder.contestAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
        }
    }

    override fun getItemCount(): Int {
        return data!!.size;
    }

    @SuppressLint("WrongConstant")
    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contestAdapter = BottomSheetWinnerRankListAdapter(mContext)

        /* init method call itself when class call & set layout manger for recycler view*/
        init {
            val llm = LinearLayoutManager(mContext)
            llm.orientation = LinearLayoutManager.VERTICAL
            itemView.rv_rank.layoutManager = llm
            itemView.rv_rank.adapter = contestAdapter
        }
    }

}