package os.com.ui.createTeam.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_select_team.view.*
import os.com.R
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.SelectPlayerInterface
import os.com.ui.createTeam.activity.TeamPreviewActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamSelectActivity
import os.com.ui.createTeam.apiResponse.myTeamListResponse.Data
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match


class SelectTeamAdapter(
    val mContext: MyTeamSelectActivity,
    var data: ArrayList<Data>,
    var match: Match,
    var onClickRecyclerView: SelectPlayerInterface,
    val FOR: Int,
    val my_team_ids: ArrayList<String>
) :
    RecyclerView.Adapter<SelectTeamAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_team, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        var count = position + 1
        holder.itemView.txt_team.text = mContext.getString(R.string.team) + " " + count
        holder.itemView.rl_clone.setOnClickListener {
            onClickRecyclerView.onClickItem(Tags.clone, holder.adapterPosition)
        }

        holder.itemView.rl_preview.setOnClickListener {
            mContext.startActivity(
                Intent(mContext, TeamPreviewActivity::class.java).putExtra("show", 1).putExtra(
                    IntentConstant.DATA,
                    data[holder.adapterPosition]
                ).putParcelableArrayListExtra(IntentConstant.SELECT_PLAYER, data[holder.adapterPosition].player_details)
                    .putExtra("substitute", data[holder.adapterPosition].substitute_detail)
                    .putExtra("teamName", holder.itemView.txt_team.text.toString())
                    .putExtra(IntentConstant.MATCH, match)
            )
        }
        if (FOR == AppRequestCodes.JOIN_PLUS)
            if (data[position].isJOINED)
                holder.itemView.card_view.alpha = 0.7f
            else
                holder.itemView.card_view.alpha = 1f
        holder.itemView.card_view.setOnClickListener {
            if (FOR == AppRequestCodes.JOIN_PLUS) {
                if (!data[position].isJOINED) {
                    if (data[position].isSelected)
                        onClickRecyclerView.onClickItem(0, holder.adapterPosition, false)
                    else
                        onClickRecyclerView.onClickItem(0, holder.adapterPosition, true)
                }
            } else {
                if (data[position].isSelected)
                    onClickRecyclerView.onClickItem(0, holder.adapterPosition, false)
                else
                    onClickRecyclerView.onClickItem(0, holder.adapterPosition, true)
            }
        }
        holder.itemView.rl_edit.setOnClickListener {
            onClickRecyclerView.onClickItem(Tags.edit, holder.adapterPosition)
        }
        holder.itemView.txt_team.isClickable = false
        if (data[position].isSelected) {
            holder.itemView.img_check.isSelected = true
            holder.itemView.card_view.setCardBackgroundColor(mContext.resources.getColor(R.color.colorSecondary))
        } else {
            holder.itemView.img_check.isSelected = false
            holder.itemView.card_view.setCardBackgroundColor(mContext.resources.getColor(R.color.white))
        }


        for (player in data[holder.adapterPosition].player_details!!) {
            if (player.player_id.equals(data[holder.adapterPosition].captain_player_id))
                holder.itemView.txt_captain.text = player.name

            if (player.player_id.equals(data[holder.adapterPosition].vice_captain_player_id))
                holder.itemView.txt_vicecaptain.text = player.name
        }
        holder.itemView.txt_wk.text = data[holder.adapterPosition].total_wicketkeeper
        holder.itemView.txt_bat.text = data[holder.adapterPosition].total_batsman
        holder.itemView.txt_all.text = data[holder.adapterPosition].total_allrounder
        holder.itemView.txt_bowl.text = data[holder.adapterPosition].total_bowler
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}