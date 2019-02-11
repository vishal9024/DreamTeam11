package os.com.ui.createTeam.adapter.cvcAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_player.view.*
import os.com.BuildConfig
import os.com.R
import os.com.interfaces.OnClickCVC
import os.com.ui.createTeam.apiResponse.PlayerListModel


class ChooseC_VC_AdapterMain(
    val mContext: AppCompatActivity,
    val onClickCVC: OnClickCVC,
    val playerList: MutableList<PlayerListModel>,
    val localTeamName: String,
    val visitorTeamName: String,
    val local_team_id: String,
    val visitor_team_id: String

) :

    RecyclerView.Adapter<ChooseC_VC_AdapterMain.AppliedCouponCodeHolder>() {
    private var WK = 1
    private var BAT = 2
    private var AR = 3
    private var BOWLER = 4
    private var SUBSTITUTE = 5
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {

        if (BuildConfig.APPLICATION_ID == "os.real11") {
            holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.white))
        }

        when {
            playerList[position].type == WK -> holder.itemView.txt_playerType.text = mContext.getString(R.string.wicket_keeper)
            playerList[position].type == BAT -> holder.itemView.txt_playerType.text = mContext.getString(R.string.batsman)
            playerList[position].type == AR -> holder.itemView.txt_playerType.text = mContext.getString(R.string.allrounder)
            playerList[position].type == BOWLER -> holder.itemView.txt_playerType.text = mContext.getString(R.string.bowler)
            playerList[position].type == SUBSTITUTE -> holder.itemView.txt_playerType.text = mContext.getString(R.string.substitute)
        }
        holder.contestAdapter.contestList(playerList[position].type!!, playerList[position].playerList!!,   localTeamName,
            visitorTeamName,
            local_team_id,
            visitor_team_id)
        holder.contestAdapter.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val contestAdapter = ChooseC_VC_Adapter(mContext, onClickCVC)

        /* init method call itself when class call & set layout manger for recycler view*/
        init {
            val llm = LinearLayoutManager(mContext)
            llm.orientation = LinearLayoutManager.VERTICAL
            itemView.rv_playerList.layoutManager = llm
            itemView.rv_playerList.adapter = contestAdapter


        }
    }


}