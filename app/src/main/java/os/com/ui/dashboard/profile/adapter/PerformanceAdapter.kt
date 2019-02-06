package os.com.ui.dashboard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.item_performance.view.*
import os.com.R
import os.com.application.FantasyApplication
import os.com.ui.dashboard.profile.apiResponse.OtherUserProfileResponse


class PerformanceAdapter(
    val mContext: Context,
    val recent_performance: MutableList<OtherUserProfileResponse.ResponseBean.DataBean.RecentPerformanceBean>,
    val team_name: String
) : RecyclerView.Adapter<PerformanceAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_performance, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            var localTeamName = ""
            var visitorTeamName = ""
            if (recent_performance[position].local_team_name != null) {
                localTeamName = recent_performance[position].local_team_name
                if (recent_performance[position].local_team_name.length > 3)
                    localTeamName = recent_performance[position].local_team_name.substring(0, 3)
            }
            if (recent_performance[position].visitor_team_name != null) {
                visitorTeamName = recent_performance[position].visitor_team_name
                if (recent_performance[position].visitor_team_name.length > 5)
                    visitorTeamName = recent_performance[position].visitor_team_name.substring(0, 3)
            }
            holder.itemView.tvTeamName.text =
                localTeamName.toUpperCase() + " " + mContext.resources.getString(R.string.vs) + " " + visitorTeamName.toUpperCase()

            if (recent_performance[position].local_team_flag != null && !recent_performance[position].local_team_flag.equals(
                    ""
                )
            )
                ImageLoader.getInstance().displayImage(
                    recent_performance[position].local_team_flag,
                    holder.itemView.imvTeamLeft,
                    FantasyApplication.getInstance().options
                )
            if (recent_performance[position].visitor_team_flag != null && !recent_performance[position].visitor_team_flag.equals(
                    ""
                )
            )
                ImageLoader.getInstance().displayImage(
                    recent_performance[position].visitor_team_flag,
                    holder.itemView.imvTeamRight,
                    FantasyApplication.getInstance().options
                )
            if (recent_performance[position].match_date != null)
                holder.itemView.tvDate.text = recent_performance[position].match_date
            if (recent_performance[position].match_comment != null)
                holder.itemView.tvTeamInfo.text = recent_performance[position].match_comment
            if (team_name != null)
                holder.itemView.tvTeamNameRight.text = team_name
            if (recent_performance[position].my_points != null)
                holder.itemView.tvPointValueLeft.text = recent_performance[position].my_points
            if (recent_performance[position].my_team != null)
                holder.itemView.tvTeamCount.text = "team " + recent_performance[position].my_team
            if (recent_performance[position].friend_points != null)
                holder.itemView.tvPointValueRight.text = "" + recent_performance[position].friend_points
            if (recent_performance[position].friend_team != null)
                holder.itemView.tvTeamCountRight.text = "team " + recent_performance[position].friend_team
            if (recent_performance[position].my_points != null && !recent_performance[position].my_points.equals("")
                && recent_performance[position].friend_points!= null && !recent_performance[position].friend_points.equals("")){
                var myPoints= recent_performance[position].my_points.toInt()
                var friendPoints =recent_performance[position].friend_points.toInt()
                if (myPoints<friendPoints) {
                    holder.itemView.imvTrophyLeft.visibility = View.GONE
                    holder.itemView.imvTrophyRight.visibility = View.VISIBLE
                }else if (myPoints>friendPoints){
                    holder.itemView.imvTrophyLeft.visibility = View.VISIBLE
                    holder.itemView.imvTrophyRight.visibility = View.GONE
                }
            }
//              holder.itemView.ll_main.setOnClickListener {
//                  mContext.startActivity(Intent(mContext, OtherUserProfileActivity::class.java))
//              }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return recent_performance.size
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}