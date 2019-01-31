package os.com.ui.contest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_teams.view.*
import os.com.R
import os.com.data.Prefs
import os.com.interfaces.OnClickRecyclerView
import os.com.ui.contest.apiResponse.getContestDetail.Team

class TeamsAdapter(
    val mContext: Context,
    val joined_team_list: ArrayList<Team>,
    val onClickRecyclerView: OnClickRecyclerView
) : RecyclerView.Adapter<TeamsAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_teams, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            if (joined_team_list[position].user_id.equals(Prefs(mContext).userdata!!.user_id)) {
                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
            } else {
                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.white))
            }
        } catch (e: Exception) {
            holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
        }
        holder.itemView.cimg_player.setOnClickListener {
//            mContext.startActivity(Intent(mContext, PlayerDetailActivity::class.java))
        }
//        ImageLoader.getInstance().displayImage(
//            joined_team_list[position].image,
//            holder.itemView.cimg_player,
//            FantasyApplication.getInstance().options
//        )
        holder.itemView.txt_TeamName.setText(joined_team_list[holder.adapterPosition].team_name+"(T"+joined_team_list[holder.adapterPosition].team_no+")")
        holder.itemView.txt_rank.setText("-")
        holder.itemView.ll_main.setOnClickListener {
            onClickRecyclerView.onClickItem("Preview",position)
        }
    }


    override fun getItemCount(): Int {
        return joined_team_list.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}