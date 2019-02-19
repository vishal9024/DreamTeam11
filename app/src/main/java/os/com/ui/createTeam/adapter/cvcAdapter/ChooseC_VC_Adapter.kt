package os.com.ui.createTeam.adapter.cvcAdapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.sub_itemcvc.view.*
import os.com.R
import os.com.application.FantasyApplication
import os.com.interfaces.OnClickCVC
import os.com.ui.createTeam.activity.PlayerDetailActivity
import os.com.ui.createTeam.apiResponse.playerListResponse.Data


class ChooseC_VC_Adapter(
    val mContext: AppCompatActivity,
    val onClickCVC: OnClickCVC
) :
    RecyclerView.Adapter<ChooseC_VC_Adapter.AppliedCouponCodeHolder>() {
    var localTeamName: String = ""
    var visitorTeamName: String = ""
    var local_team_id: String = ""
    var visitor_team_id: String = ""
    var type: Int = 0
    var playerList: List<Data> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_itemcvc, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        holder.itemView.cimg_player.setOnClickListener {
            mContext.startActivity(Intent(mContext, PlayerDetailActivity::class.java)
                .putExtra("player_id",playerList[position].player_id)
                .putExtra("series_id",playerList[position].series_id))
        }
        if (playerList[position].player_record != null) {
            ImageLoader.getInstance().displayImage(
                playerList[position].player_record!!.image,
                holder.itemView.cimg_player,
                FantasyApplication.getInstance().options
            )
            holder.itemView.txt_PlayerName.text = playerList[position].player_record!!.player_name
            if (local_team_id.equals(playerList[position].team_id))
                holder.itemView.txt_Country.text = localTeamName
            else
                holder.itemView.txt_Country.text = visitorTeamName
//            holder.itemView.txt_Country.text = playerList[position].player_record!!.country
//          holder.itemView. txt_Avg.text=playerList!![position].player_record!!
        }
        holder.itemView.img_captain.isSelected = playerList[position].isCaptain
        holder.itemView.img_vc.isSelected = playerList[position].isViceCaptain

        if (type == 5)
            holder.itemView.ll_ADD.visibility = View.GONE
        else
            holder.itemView.ll_ADD.visibility = View.VISIBLE

        if (playerList[position].isCaptain)
            holder.itemView.img_c_vc.setImageResource(R.mipmap.v_points)
        if (playerList[position].isViceCaptain)
            holder.itemView.img_c_vc.setImageResource(R.mipmap.vc_points)
        if (!playerList[position].isCaptain && !playerList[position].isViceCaptain)
            holder.itemView.img_c_vc.setImageResource(0)

        holder.itemView.img_captain.setOnClickListener {
            onClickCVC.onClick("c", type, holder.adapterPosition)
        }
        holder.itemView.img_vc.setOnClickListener {
            onClickCVC.onClick("vc", type, holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    fun contestList(
        type: Int,
        data: ArrayList<Data>,
        localTeamName: String,
        visitorTeamName: String,
        local_team_id: String,
        visitor_team_id: String
    ) {
        this.localTeamName = localTeamName
        this.visitorTeamName = visitorTeamName
        this.local_team_id = local_team_id
        this.visitor_team_id = visitor_team_id
        this.type = type
        playerList = data
        notifyDataSetChanged()
    }


    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}