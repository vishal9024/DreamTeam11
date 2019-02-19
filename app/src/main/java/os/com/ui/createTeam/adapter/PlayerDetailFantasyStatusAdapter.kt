package os.com.ui.createTeam.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_fantasy_status.view.*
import os.com.R
import os.com.ui.createTeam.apiResponse.PlayerdetailResponse


class PlayerDetailFantasyStatusAdapter(
    val mContext: Context,
    val match_detail: MutableList<PlayerdetailResponse.ResponseBean.DataBean.MatchDetailBean>
) : RecyclerView.Adapter<PlayerDetailFantasyStatusAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fantasy_status, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            if (match_detail[position].Match != null)
                holder.itemView.txtMatchName.text = match_detail[position].Match
            if (match_detail[position].date != null)
                holder.itemView.txtDate.text = match_detail[position].date
            if (match_detail[position].getPlayer_points() != null)
                holder.itemView.txtPoints.text = match_detail[position].player_points
            if (match_detail[position].selected_by != null)
                holder.itemView.txtSelectedBy.text = match_detail[position].selected_by
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return match_detail.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}