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


class BottomSheetWinnerListAdapter(val mContext: Context) :
    RecyclerView.Adapter<BottomSheetWinnerListAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_winners_list_bottomsheet, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
//            if (position%2==0){
//                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
//            }else{
//                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.white))
//            }
            setAdapter(holder.itemView.rv_rank)

        } catch (e: Exception) {
            holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
        }

//        holder.itemView.txt_Join.setOnClickListener {
//            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java))
//        }
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(rv_rank: RecyclerView) {
        try {
            val llm = LinearLayoutManager(mContext)
            llm.orientation = LinearLayoutManager.VERTICAL
            rv_rank!!.layoutManager = llm
            rv_rank!!.adapter = BottomSheetWinnerRankListAdapter(mContext!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return 5;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}