package os.com.ui.createTeam.adapter

import android.content.Context
import android.content.Intent
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
import os.com.ui.createTeam.activity.PlayerDetailActivity
import os.com.ui.createTeam.apiResponse.SelectPlayer
import os.com.ui.createTeam.apiResponse.playerListResponse.Data

class PlayerListAdapter(
    val mContext: Context,
    var playerList: MutableList<Data>?,
    var type: Int,
    var onClickRecyclerView: SelectPlayerInterface,
    selectPlayer: SelectPlayer?
) : RecyclerView.Adapter<PlayerListAdapter.AppliedCouponCodeHolder>() {
    private var WK = 1
    private var BAT = 2
    private var AR = 3
    private var BOWLER = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        if (BuildConfig.APPLICATION_ID == "os.real11")
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_teamplayer, parent, false)
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
        if (playerList!![position].player_record!=null) {
            ImageLoader.getInstance().displayImage(
                playerList!![position].player_record!!.image,
                holder.itemView.cimg_player,
                FantasyApplication.getInstance().options
            )

            holder.itemView.txt_PlayerName.text = playerList!![position].player_record!!.player_name
            holder.itemView.txt_Country.text = playerList!![position].player_record!!.country
//        holder.itemView. txt_Avg.text=playerList!![position].player_record!!
            holder.itemView.txt_Credits.text = playerList!![position].player_record!!.player_credit
        }
        holder.itemView.img_add.isSelected = playerList!![position]!!.isSelected
        holder.itemView.img_add.setOnClickListener {
            if (playerList!![position]!!.isSelected)
                onClickRecyclerView.onClickItem(type, holder.adapterPosition, false)
            else
                onClickRecyclerView.onClickItem(type, holder.adapterPosition, true)
        }
        holder.itemView.cimg_player.setOnClickListener {
            mContext.startActivity(Intent(mContext, PlayerDetailActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return playerList!!.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}