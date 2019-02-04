package os.com.ui.dashboard.profile.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_ranking.view.*
import os.com.R
import os.com.ui.dashboard.profile.activity.OtherUserProfileActivity


class RankingAdapter(val mContext: Context) : RecyclerView.Adapter<RankingAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
          try{
              holder.itemView.ll_main.setOnClickListener {
                  mContext.startActivity(Intent(mContext, OtherUserProfileActivity::class.java))
              }
          } catch (e: Exception) {
              e.printStackTrace()
          }
    }

    override fun getItemCount(): Int {
        return 15;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}