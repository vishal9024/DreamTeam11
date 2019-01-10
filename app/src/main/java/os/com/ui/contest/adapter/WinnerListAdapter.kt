package os.com.ui.contest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_winning_list.view.*
import os.com.R


class WinnerListAdapter(val mContext: Context) : RecyclerView.Adapter<WinnerListAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_winners_list, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
//            if (position%2==0){
//                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
//            }else{
//                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.white))
//            }


        }catch (e:Exception){
            holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
        }

//        holder.itemView.txt_Join.setOnClickListener {
//            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java))
//        }
    }

//    @SuppressLint("WrongConstant")
//    private fun setAdapter() {
//        val llm = LinearLayoutManager(context)
//        llm.orientation = LinearLayoutManager.VERTICAL
//        dialog. rv_rank!!.layoutManager = llm
//        dialog.rv_rank!!.adapter = WinnerListAdapter(context!!)
//    }

    override fun getItemCount(): Int {
        return 5;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}