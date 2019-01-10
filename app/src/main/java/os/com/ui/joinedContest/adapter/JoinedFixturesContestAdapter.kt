package os.com.ui.joinedContest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_joined_contest.view.*
import os.com.R
import os.com.ui.contest.apiResponse.getContestList.ContestCategory
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse.Data

class JoinedFixturesContestAdapter(
    val mContext: AppCompatActivity,
    var contestList: ArrayList<ContestCategory>,
    var match: Match?,
    var matchType: Int,
    var data: ArrayList<Data>
) :
    RecyclerView.Adapter<JoinedFixturesContestAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_joined_contest, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
//        holder.itemView.card_view.setOnClickListener {
//            mContext.startActivity(Intent(mContext, FixtureJoinedContestDetailActivity::class.java))
//        }
        holder.itemView.txt_TotalWinnings.text = mContext.getString(R.string.Rs) + " " +
                data.get(holder.adapterPosition).prize_money
        holder.itemView.txt_Winners.text = data.get(holder.adapterPosition).total_winners
        holder.itemView.txt_EntryFee.text = mContext.getString(R.string.Rs) + " " +
                data.get(holder.adapterPosition).entry_fee
        holder.itemView.txt_EndValue.text = data.get(holder.adapterPosition).total_teams + " " +
                mContext.getString(R.string.teams)
        if (data.get(holder.adapterPosition).is_joined)
            holder.itemView.txt_Join.text = mContext.getString(R.string.joined)
        else
            holder.itemView.txt_Join.text = mContext.getString(R.string.join)
        if (!data.get(holder.adapterPosition).total_teams.isEmpty() && !data.get(holder.adapterPosition).teams_joined.isEmpty()) {
            val strtValue =
                data.get(holder.adapterPosition).total_teams.toInt() - data.get(holder.adapterPosition).teams_joined.toInt()
            holder.itemView.txt_StartValue.text = mContext.getString(R.string.only) + " " + strtValue.toString() + " " +
                    mContext.getString(R.string.spots_left)
            holder.itemView.crs_Progress.setMinValue(0f)
            holder.itemView.crs_Progress.setMaxValue(data.get(holder.adapterPosition).total_teams.toFloat())
            holder.itemView.crs_Progress.setMinStartValue(0f);
            holder.itemView.crs_Progress.setMaxStartValue(data.get(holder.adapterPosition).teams_joined.toFloat());
            holder.itemView.crs_Progress.apply();
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}