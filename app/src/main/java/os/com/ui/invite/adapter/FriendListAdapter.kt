package os.com.ui.dashboard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.item_freind_list.view.*
import os.com.R
import os.com.application.FantasyApplication
import os.com.ui.invite.apiResponse.InviteFreindDetailResponse


class FriendListAdapter(
    val mContext: Context,
    val rankingList: MutableList<InviteFreindDetailResponse.ResponseBean.DataBean.FriendDetailBean>
) : RecyclerView.Adapter<FriendListAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_freind_list, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {

            var received_amount = 0.0F
            var total_amount = 0.0F
            if (rankingList[position].image != null && !rankingList[position].image.equals(""))
                ImageLoader.getInstance().displayImage(
                    rankingList[position].image,
                    holder.itemView.cimg_user,
                    FantasyApplication.getInstance().options
                )
            if (rankingList[position].team_name != null)
                holder.itemView.txtTeamName.text = rankingList[position].team_name
            if (rankingList[position].received_amount != null) {
                holder.itemView.txtReceivedAmount.text = "₹" + rankingList[position].received_amount
                received_amount=rankingList[position].received_amount.toFloat()
            }
            if (rankingList[position].total_amount != null) {
                holder.itemView.txtEarnAmount.text = "₹ " + rankingList[position].total_amount
                total_amount=rankingList[position].total_amount.toFloat()
            }
            try {
                holder.itemView.crs_Progress.setMinValue(0f)
                holder.itemView.crs_Progress.setMaxValue(total_amount)
                holder.itemView.crs_Progress.setMinStartValue(0f)
                holder.itemView.crs_Progress.setMaxStartValue(received_amount)
                holder.itemView.crs_Progress.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }



        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return rankingList.size
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}