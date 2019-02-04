package os.com.ui.dashboard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import os.com.R


class PerformanceAdapter(val mContext: Context) : RecyclerView.Adapter<PerformanceAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_performance, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
          try{
//              holder.itemView.ll_main.setOnClickListener {
//                  mContext.startActivity(Intent(mContext, OtherUserProfileActivity::class.java))
//              }
          } catch (e: Exception) {
              e.printStackTrace()
          }
    }

    override fun getItemCount(): Int {
        return 10
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}