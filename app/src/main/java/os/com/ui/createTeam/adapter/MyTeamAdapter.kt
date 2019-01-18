package os.com.ui.createTeam.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_my_team.view.*
import os.com.R
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickRecyclerView
import os.com.ui.createTeam.activity.TeamPreviewActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamActivity
import os.com.ui.createTeam.apiResponse.myTeamListResponse.Data


class MyTeamAdapter(val mContext: MyTeamActivity, var data: ArrayList<Data>,var onClickRecyclerView: OnClickRecyclerView) :
    RecyclerView.Adapter<MyTeamAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_team, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        holder.itemView.rl_clone.setOnClickListener {
            onClickRecyclerView.onClickItem(Tags.clone,holder.adapterPosition)
//            mContext.startActivity(Intent(mContext, TeamPreviewActivity::class.java).putExtra("show", 1).putExtra(IntentConstant.DATA, data[holder.adapterPosition]))

            //            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java))
        }
        holder.itemView.rl_preview.setOnClickListener {
            mContext.startActivity(Intent(mContext, TeamPreviewActivity::class.java).putExtra("show", 1).putExtra(IntentConstant.DATA, data[holder.adapterPosition]).putParcelableArrayListExtra(IntentConstant.SELECT_PLAYER,data[holder.adapterPosition].player_details)
                .putExtra("substitute",data[holder.adapterPosition].substitute_detail)
            )
        }
        holder.itemView.rl_edit.setOnClickListener {
            onClickRecyclerView.onClickItem(Tags.edit,holder.adapterPosition)
            //            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java))
        }
        var count=position+1
        holder.itemView.txt_team.text = mContext.getString(R.string.team) + " " + count
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