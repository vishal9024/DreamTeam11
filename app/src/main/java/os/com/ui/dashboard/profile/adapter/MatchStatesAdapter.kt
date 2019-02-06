package os.com.ui.dashboard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_match_states.view.*
import os.com.R
import os.com.ui.dashboard.profile.apiResponse.TeamStatesResponse


class MatchStatesAdapter(
    val mContext: Context,
    val data: MutableList<TeamStatesResponse.ResponseBean.DataBean.PointDetailBean>
) : RecyclerView.Adapter<MatchStatesAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match_states, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            var local_team=""
            var visitor_team=""
            var team_count=""

            if (data[position].local_team!= null)
                local_team=data[position].local_team
            if (data[position].visitor_team!= null)
                visitor_team=data[position].visitor_team
            if (data[position].team_count!= null)
                team_count=data[position].team_count
            holder.itemView.txt_matchVS.text=local_team+" VS "+visitor_team+"("+team_count+")"

            if (data[position].points!= null)
                holder.itemView.txt_points.text = data[position].points
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}