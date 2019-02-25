package os.com.ui.joinedContest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.item_teamplayer.view.*
import os.com.BuildConfig
import os.com.R
import os.com.application.FantasyApplication
import os.com.interfaces.SelectPlayerInterface
import os.com.ui.createTeam.apiResponse.playerListResponse.Data

class ReplaceSubstituteAdapter(
    val mContext: Context,
    var playerList: MutableList<Data>?,
    var onClickRecyclerView: SelectPlayerInterface,
   var localTeamName: String,
    var visitorTeamName: String,
    var local_team_id: String,
    var visitor_team_id: String
) : RecyclerView.Adapter<ReplaceSubstituteAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        if (BuildConfig.APPLICATION_ID == "os.real11")
            return AppliedCouponCodeHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_teamplayer_real11,
                    parent,
                    false
                )
            )
        else
            return AppliedCouponCodeHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_teamplayer,
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            if (position % 2 == 0) {
                holder.itemView.ll_ADD.setBackgroundColor(mContext.resources.getColor(R.color.colorItemBackgroundGray))
                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorItemBackgroundLightGray))
            } else {
                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.white))
                holder.itemView.ll_ADD.setBackgroundColor(mContext.resources.getColor(R.color.colorItemBackgroundLightGray))
            }
        } catch (e: Exception) {
            holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
        }
        if (playerList!![position].player_record != null) {
            if (BuildConfig.APPLICATION_ID == "os.cashfantasy") {
                holder.itemView.img_player.visibility = View.VISIBLE
                holder.itemView.cimg_player.visibility = View.GONE
                ImageLoader.getInstance().displayImage(
                    playerList!![position].player_record!!.image,
                    holder.itemView.img_player,
                    FantasyApplication.getInstance().options
                )
            } else {
                holder.itemView.img_player.visibility = View.GONE
                holder.itemView.cimg_player.visibility = View.VISIBLE
                ImageLoader.getInstance().displayImage(
                    playerList!![position].player_record!!.image,
                    holder.itemView.cimg_player,
                    FantasyApplication.getInstance().options
                )
            }
            holder.itemView.txt_PlayerName.text = playerList!![position].player_record!!.player_name

            if (local_team_id.equals(playerList!![position].team_id))
                holder.itemView.txt_Country.text = localTeamName
            else
                holder.itemView.txt_Country.text = visitorTeamName

            holder.itemView.txt_Country.text = playerList!![position].player_record!!.country
            holder.itemView.txt_Credits.text = playerList!![position].player_record!!.player_credit
        }
        if (BuildConfig.APPLICATION_ID == "os.real11")
            holder.itemView.txt_Avg.text=playerList!![position].selected_by
        else
            holder.itemView.txt_Avg.text=playerList!![position].player_points+" "+ mContext.getString(R.string.points)

        holder.itemView.img_add.isSelected = playerList!![position].isSelected

        if (BuildConfig.APPLICATION_ID == "os.cashfantasy")
            holder.itemView.ll_main.setOnClickListener {
                if (playerList!![position].isSelected)
                    onClickRecyclerView.onClickItem(5, holder.adapterPosition, false)
                else
                    onClickRecyclerView.onClickItem(5, holder.adapterPosition, true)
            }
        holder.itemView.img_add.setOnClickListener {
            if (playerList!![position].isSelected)
                onClickRecyclerView.onClickItem(5, holder.adapterPosition, false)
            else
                onClickRecyclerView.onClickItem(5, holder.adapterPosition, true)
        }
        holder.itemView.cimg_player.setOnClickListener {
            //  mContext.startActivity(Intent(mContext, PlayerDetailActivity::class.java))
        }
//        holder.itemView.ll_main.alpha = 1.0f
//        if (!playerList!![position].isSelected)
//            holder.itemView.ll_main.alpha = 0.5f
//        else
//            holder.itemView.ll_main.alpha = 1.0f
    }

    override fun getItemCount(): Int {
        return playerList!!.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}