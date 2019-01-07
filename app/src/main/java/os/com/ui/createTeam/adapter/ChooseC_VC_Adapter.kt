package os.com.ui.createTeam.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.item_player.view.*
import os.com.BuildConfig
import os.com.R
import os.com.application.FantasyApplication
import os.com.interfaces.OnClickCVC
import os.com.ui.createTeam.activity.Choose_C_VC_Activity
import os.com.ui.createTeam.activity.PlayerDetailActivity
import os.com.ui.createTeam.apiResponse.playerListResponse.Data


class ChooseC_VC_Adapter(val mContext: AppCompatActivity, val onClickCVC: OnClickCVC, val playerList: MutableList<Data>) :

    RecyclerView.Adapter<ChooseC_VC_Adapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        if (BuildConfig.APPLICATION_ID == "os.real11") {
            holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.white))
        }
        holder.itemView.cimg_player.setOnClickListener {
            mContext.startActivity(Intent(mContext, PlayerDetailActivity::class.java))
        }
        if (playerList[position].player_role.contains("Wicketkeeper", true)) {
            if (!(mContext as Choose_C_VC_Activity).isShowingWk) {
                (mContext as Choose_C_VC_Activity).isShowingWk=true
                holder.itemView.txt_playerType.visibility = View.VISIBLE
                holder.itemView.txt_playerType.text = "Wicket-Keeper"
            }else
                holder.itemView.txt_playerType.visibility = View.GONE
        } else if (playerList[position].player_role.contains("Batsman", true)) {
            if (!(mContext as Choose_C_VC_Activity).isShowingbat) {
                (mContext as Choose_C_VC_Activity).isShowingbat = true
                holder.itemView.txt_playerType.visibility = View.VISIBLE
                holder.itemView.txt_playerType.text = "Batsmen"
            }else
                holder.itemView.txt_playerType.visibility = View.GONE
        } else if (playerList[position].player_role.contains("Allrounder", true)) {
            if (!(mContext as Choose_C_VC_Activity).isShowingAr) {
                (mContext as Choose_C_VC_Activity).isShowingAr = true
                holder.itemView.txt_playerType.visibility = View.VISIBLE
                holder.itemView.txt_playerType.text = "All-Rounder"
            }else
                holder.itemView.txt_playerType.visibility = View.GONE
        } else if (playerList[position].player_role.contains("Bowler", true)) {
            if (!(mContext as Choose_C_VC_Activity).isShowingbowl) {
                (mContext as Choose_C_VC_Activity).isShowingbowl = true
                holder.itemView.txt_playerType.visibility = View.VISIBLE
                holder.itemView.txt_playerType.text = "Bowler"
            }else
                holder.itemView.txt_playerType.visibility = View.GONE
        } else {
            holder.itemView.txt_playerType.visibility = View.GONE
        }
        if (playerList!![position].player_record != null) {
            ImageLoader.getInstance().displayImage(
                playerList!![position].player_record!!.image,
                holder.itemView.cimg_player,
                FantasyApplication.getInstance().options
            )

            holder.itemView.txt_PlayerName.text = playerList!![position].player_record!!.player_name
            holder.itemView.txt_Country.text = playerList!![position].player_record!!.country
//        holder.itemView. txt_Avg.text=playerList!![position].player_record!!
        }
        holder.itemView.img_captain.isSelected = playerList[position].isCaptain
        holder.itemView.img_vc.isSelected = playerList[position].isViceCaptain
        holder.itemView.img_captain.setOnClickListener {
            onClickCVC.onClick("c", holder.adapterPosition)
        }
        holder.itemView.img_vc.setOnClickListener {
            onClickCVC.onClick("vc", holder.adapterPosition)
        }
    }


    override fun getItemCount(): Int {
        return playerList.size
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}