package os.com.ui.dashboard.myContest.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_match.view.*
import os.com.R
import os.com.ui.joinedContest.activity.CompletedJoinedContestActivity


class MyContestCompletedAdapter(val mContext: Context) : RecyclerView.Adapter<MyContestCompletedAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_contest, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        holder.itemView.txt_Countdown.setText(mContext.getString(R.string.completed))
        holder.itemView.txt_Countdown.setTextColor(mContext.resources.getColor(R.color.colorSecondary))
        holder.itemView.card_view.setOnClickListener {
            mContext.startActivity(Intent(mContext, CompletedJoinedContestActivity::class.java))
        }

    }


    override fun getItemCount(): Int {
        return 15;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}