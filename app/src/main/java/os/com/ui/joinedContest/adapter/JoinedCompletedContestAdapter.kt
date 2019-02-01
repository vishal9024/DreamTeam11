package os.com.ui.joinedContest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_joined_completed_list.view.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.activity.LeaderShipBoardActivity
import os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse.JoinedContestData

class JoinedCompletedContestAdapter(
    val mContext: AppCompatActivity,
    var match: Match?,
    var matchType: Int,
    var data: ArrayList<JoinedContestData>
) :
    RecyclerView.Adapter<JoinedCompletedContestAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_joined_completed_list, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        holder.itemView.txt_ViewLeaderShipBoard.setOnClickListener {
            mContext.startActivity(
                Intent(mContext, LeaderShipBoardActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                    IntentConstant.CONTEST_TYPE, matchType
                ).putExtra(IntentConstant.DATA, data[holder.adapterPosition]).putExtra(
                    IntentConstant.FROM,
                    AppRequestCodes.JOINED
                )
            )
        }
        if (!data.get(holder.adapterPosition).entry_fee.isEmpty() && data.get(holder.adapterPosition).entry_fee.toFloat() > 0) {
            holder.itemView.ll_scoreBoard.visibility = View.VISIBLE
            holder.itemView.ll_practice.visibility = View.GONE
        } else {
            holder.itemView.ll_scoreBoard.visibility = View.GONE
            holder.itemView.ll_practice.visibility = View.VISIBLE
        }
        holder.itemView.txt_TotalWinnings.text = mContext.getString(R.string.Rs) + " " +
                data.get(holder.adapterPosition).prize_money
        holder.itemView.txt_Winners.text = data.get(holder.adapterPosition).total_winners
        holder.itemView.txt_EntryFee.text = mContext.getString(R.string.Rs) + " " +
                data.get(holder.adapterPosition).entry_fee
        if (!data.get(holder.adapterPosition).my_team_ids!!.isEmpty()) {
            if (data.get(holder.adapterPosition).my_team_ids!!.size == 1) {
                if (!data.get(holder.adapterPosition).team_number!!.isEmpty())
                    holder.itemView.txt_joinedwith.text = mContext.getString(R.string.team) +
                            data.get(holder.adapterPosition).team_number!!.get(0)
            } else {
                holder.itemView.txt_joinedwith.text = data.get(holder.adapterPosition).my_team_ids!!.size.toString() +
                        mContext.getString(R.string.teams)
            }
        }
holder.itemView.ll_totalWinners.setOnClickListener {
    try {
        if (!data.get(position).total_winners.isEmpty() && data.get(position).total_winners!!.toInt() > 0)
            (mContext as BaseActivity).callWinningBreakupApi(
                data[position].contest_id,
                data[position].breakup_detail!!,
                data[position].prize_money
            )
    } catch (e: Exception) {

    }
}
        holder.itemView.txt_points.text = data.get(holder.adapterPosition).points_earned
        holder.itemView.txt_rank.text = data.get(holder.adapterPosition).my_rank
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}