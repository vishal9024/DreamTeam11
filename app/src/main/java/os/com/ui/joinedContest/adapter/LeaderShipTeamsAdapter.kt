package os.com.ui.joinedContest.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_completed_leadershipteams.view.*
import os.com.BuildConfig
import os.com.R
import os.com.constant.IntentConstant
import os.com.constant.IntentConstant.LIVE
import os.com.data.Prefs
import os.com.interfaces.OnClickRecyclerView
import os.com.ui.contest.apiResponse.getContestDetail.Team
import os.com.ui.dashboard.profile.activity.OtherUserProfileActivity


class LeaderShipTeamsAdapter(
    val mContext: Context,
    val match_status: String,
    var joined_team_list: ArrayList<Team>,
    val onClickRecyclerView: OnClickRecyclerView,
    var matchType: Int
) : RecyclerView.Adapter<LeaderShipTeamsAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_completed_leadershipteams, parent, false)
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
            mContext.startActivity(
                Intent(mContext, OtherUserProfileActivity::class.java).putExtra(
                    "data",
                    "" + joined_team_list[position].user_id
                )
            )

        }
//        ImageLoader.getInstance().displayImage(
//            joined_team_list[position].image,
//            holder.itemView.cimg_player,
//            FantasyApplication.getInstance().options
//        )
        holder.itemView.txt_TeamName.setText(joined_team_list[holder.adapterPosition].team_name + "(T" + joined_team_list[holder.adapterPosition].team_no + ")")
        if (!joined_team_list[holder.adapterPosition].rank.isEmpty() && joined_team_list[holder.adapterPosition].rank.toLong() > 0)
            holder.itemView.txt_rank.setText("#" + joined_team_list[holder.adapterPosition].rank)
        else
            holder.itemView.txt_rank.setText("-")

        if (!joined_team_list[holder.adapterPosition].winning_amount.isEmpty() && joined_team_list[holder.adapterPosition].winning_amount.toDouble() > 0) {
            holder.itemView.txt_prizeMoney.setText(mContext.getString(R.string.Rs) + " " + joined_team_list[holder.adapterPosition].winning_amount)
            holder.itemView.txt_prizeMoney.visibility = View.VISIBLE
        } else
            holder.itemView.txt_prizeMoney.visibility = GONE

        holder.itemView.txt_Points.setText(joined_team_list[holder.adapterPosition].point + mContext.getString(R.string.points))
        holder.itemView.ll_main.setOnClickListener {
            onClickRecyclerView.onClickItem("Preview", position)
        }
        if (matchType == IntentConstant.LIVE) {
            if (!joined_team_list[holder.adapterPosition].rank.isEmpty() && !joined_team_list[holder.adapterPosition].previous_rank.isEmpty()) {
                if (joined_team_list[holder.adapterPosition].rank.toLong() > joined_team_list[holder.adapterPosition].previous_rank.toLong()) {
                    holder.itemView.img_rank.visibility = View.VISIBLE
                    holder.itemView.img_rank.setImageResource(R.mipmap.upflag)
                } else {
                    holder.itemView.img_rank.visibility = View.VISIBLE
                    holder.itemView.img_rank.setImageResource(R.mipmap.downflag)
                }
            } else
                holder.itemView.img_rank.visibility = View.GONE
            holder.itemView.txt_prizeMoney.visibility = View.GONE
        } else {
            holder.itemView.img_rank.visibility = View.GONE
            holder.itemView.txt_prizeMoney.visibility = View.VISIBLE
        }
        if (matchType == LIVE && match_status.equals("Not Started", ignoreCase = true)) {
            holder.itemView.img_rank.visibility = View.GONE
        }
//        holder.itemView.txt_Join.setOnClickListener {
//            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java))
//        }
        if (BuildConfig.APPLICATION_ID == "os.real11" || BuildConfig.APPLICATION_ID == "os.cashfantasy") {
            if (matchType == LIVE && match_status.equals("Not Started", ignoreCase = true)) {
                if (joined_team_list[holder.adapterPosition].user_id.equals(Prefs(mContext).userdata!!.user_id)
                    && joined_team_list[holder.adapterPosition].substitute_status.equals("0")
                ) {
                    holder.itemView.ll_substitute.visibility = View.VISIBLE
                    holder.itemView.ll_Ranking.visibility = View.GONE
                    holder.itemView.img_rank.visibility = View.GONE
                    holder.itemView.replace_with_substitute.setOnClickListener {
                        onClickRecyclerView.onClickItem("substitute", position)
                    }
                } else holder.itemView.ll_substitute.visibility = View.GONE
            } else holder.itemView.ll_substitute.visibility = View.GONE
        } else holder.itemView.ll_substitute.visibility = View.GONE
        }


        override fun getItemCount(): Int {
            return joined_team_list.size
        }

        inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        }


    }