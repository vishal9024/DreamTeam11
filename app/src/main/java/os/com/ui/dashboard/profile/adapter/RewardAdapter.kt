package os.com.ui.dashboard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_reward.view.*
import os.com.BuildConfig
import os.com.R
import os.com.ui.dashboard.profile.apiResponse.ProfileResponse


class RewardAdapter(
    val mContext: Context,
    val data: MutableList<ProfileResponse.ResponseBean.DataBean.RewardsBean>
) : RecyclerView.Adapter<RewardAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reward, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            if (BuildConfig.APPLICATION_ID == "os.realbash") {
                holder.itemView.cimg_user.setImageResource(R.mipmap.cashbonus25)
            }
            if (data[position].date!=null)
            holder.itemView.txt_InviteFriends.text=data[position].date
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

}