package os.com.ui.createTeam.adapter

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
import os.com.ui.createTeam.activity.ChooseTeamActivity.Companion.exeedCredit
import os.com.ui.createTeam.apiResponse.SelectPlayer
import os.com.ui.createTeam.apiResponse.playerListResponse.Data

class PlayerListAdapter(
    val mContext: Context,
    var playerList: MutableList<Data>?,
    var type: Int,
    var onClickRecyclerView: SelectPlayerInterface,
    var selectPlayer: SelectPlayer?,
    var localTeamName: String,
    var visitorTeamName: String,
    var local_team_id: String,
    var visitor_team_id: String
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

//        if (BuildConfig.APPLICATION_ID == "os.real11")
//            holder.itemView.txt_Avg.text = playerList!![position].selected_by
//        else
            holder.itemView.txt_Avg.text = playerList!![position].player_points + " " +
                    mContext.getString(R.string.points)
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
//        holder.itemView. txt_Avg.text=playerList!![position].player_record!!
            holder.itemView.txt_Credits.text = playerList!![position].player_record!!.player_credit
        }
        holder.itemView.img_add.isSelected = playerList!![position]!!.isSelected
        holder.itemView.ll_main.setOnClickListener {
            if (playerList!![position].isSelected)
                onClickRecyclerView.onClickItem(type, holder.adapterPosition, false)
            else
                onClickRecyclerView.onClickItem(type, holder.adapterPosition, true)
        }
        holder.itemView.img_add.setOnClickListener {
            if (playerList!![position].isSelected)
                onClickRecyclerView.onClickItem(type, holder.adapterPosition, false)
            else
                onClickRecyclerView.onClickItem(type, holder.adapterPosition, true)
        }
        holder.itemView.cimg_player.setOnClickListener {
            onClickRecyclerView.onClickItem(type.toString(), holder.adapterPosition)
        }
        holder.itemView.ll_main.alpha = 1.0f
        if (playerList!![position].team_id.equals(local_team_id)) {
            if (selectPlayer!!.localTeamplayerCount == 7) {
                if (!playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha = 0.5f
                else
                    holder.itemView.ll_main.alpha = 1.0f
            } else {
                checkList(holder, position)
//                holder.itemView.ll_main.alpha = 1.0f
            }

        } else if (playerList!![position].team_id.equals(visitor_team_id)) {
            if (selectPlayer!!.visitorTeamPlayerCount == 7) {
                if (!playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha = 0.5f
                else
                    holder.itemView.ll_main.alpha = 1.0f
            } else {
                checkList(holder, position)
//                holder.itemView.ll_main.alpha = 1.0f
            }
        }
    }

    fun checkList(holder: AppliedCouponCodeHolder, position: Int) {
        if (type == WK) {

            if (selectPlayer!!.wk_selected == selectPlayer!!.wk_count) {
                if (!playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha = 0.5f
                else
                    holder.itemView.ll_main.alpha = 1.0f
            } else if (/*(mContext as ChooseTeamActivity).*/exeedCredit) {
                if (playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha =1.0f
                else if (100-selectPlayer!!.total_credit>=playerList!![position].player_record!!.player_credit.toDouble())
                    holder.itemView.ll_main.alpha =1.0f
                else
                    holder.itemView.ll_main.alpha =0.5f
            }
        } else if (type == AR) {
            if (selectPlayer!!.ar_selected == selectPlayer!!.ar_maxcount) {
                if (!playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha = 0.5f
                else
                    holder.itemView.ll_main.alpha = 1.0f
            } else if (selectPlayer!!.ar_selected >= selectPlayer!!.ar_mincount && selectPlayer!!.extra_player == 0) {
                if (!playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha = 0.5f
                else
                    holder.itemView.ll_main.alpha = 1.0f
            } else if (/*(mContext as ChooseTeamActivity).*/exeedCredit) {
                if (playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha =1.0f
                else if (100-selectPlayer!!.total_credit>=playerList!![position].player_record!!.player_credit.toDouble())
                    holder.itemView.ll_main.alpha =1.0f
                else
                    holder.itemView.ll_main.alpha =0.5f
            }
        } else if (type == BAT) {
            if (selectPlayer!!.bat_selected == selectPlayer!!.bat_maxcount) {
                if (!playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha = 0.5f
                else
                    holder.itemView.ll_main.alpha = 1.0f
            } else if (selectPlayer!!.bat_selected >= selectPlayer!!.bat_mincount && selectPlayer!!.extra_player == 0) {
                if (!playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha = 0.5f
                else
                    holder.itemView.ll_main.alpha = 1.0f
            } else if (/*(mContext as ChooseTeamActivity).*/exeedCredit) {
                if (playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha =1.0f
                else if (100-selectPlayer!!.total_credit>=playerList!![position].player_record!!.player_credit.toDouble())
                    holder.itemView.ll_main.alpha =1.0f
                else
                    holder.itemView.ll_main.alpha =0.5f
            }
        } else if (type == BOWLER) {
            if (selectPlayer!!.bowl_selected == selectPlayer!!.bowl_maxcount) {
                if (!playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha = 0.5f
                else
                    holder.itemView.ll_main.alpha = 1.0f
            } else if (selectPlayer!!.bowl_selected >= selectPlayer!!.bowl_mincount && selectPlayer!!.extra_player == 0) {
                if (!playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha = 0.5f
                else
                    holder.itemView.ll_main.alpha = 1.0f
            } else if (/*(mContext as ChooseTeamActivity).*/exeedCredit) {
                if (playerList!![position].isSelected)
                    holder.itemView.ll_main.alpha =1.0f
                else if (100-selectPlayer!!.total_credit>=playerList!![position].player_record!!.player_credit.toDouble())
                    holder.itemView.ll_main.alpha =1.0f
                else
                    holder.itemView.ll_main.alpha =0.5f
            }
        }
    }

    override fun getItemCount(): Int {
        return playerList!!.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}