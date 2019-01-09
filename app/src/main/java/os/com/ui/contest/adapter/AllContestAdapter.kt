package os.com.ui.contest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_all_contest.view.*
import os.com.R
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickRecyclerView
import os.com.ui.contest.activity.ContestDetailActivity
import os.com.ui.contest.apiResponse.getContestList.Contest
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.winningBreakup.dialogues.BottomSheetWinningListFragment

class AllContestAdapter(
    val mContext: AppCompatActivity,
    val contest: MutableList<Contest>?,
    var matchType:Int,
    var match: Match?
    ,val onClickRecyclerView: OnClickRecyclerView
) :
    RecyclerView.Adapter<AllContestAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_all_contest, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {

        holder.itemView.txt_TotalWinnings.text = mContext.getString(R.string.Rs) + " " +
                contest!!.get(holder.adapterPosition).prize_money
        holder.itemView.txt_Winners.text = contest.get(holder.adapterPosition).total_winners
        holder.itemView.txt_EntryFee.text = mContext.getString(R.string.Rs) + " " +
                contest.get(holder.adapterPosition).entry_fee
        holder.itemView.txt_EndValue.text = contest.get(holder.adapterPosition).total_teams + " " +
                mContext.getString(R.string.teams)

        if (!contest.get(holder.adapterPosition).total_teams.isEmpty() && !contest.get(holder.adapterPosition).teams_joined.isEmpty()) {
            val strtValue =
                contest.get(holder.adapterPosition).total_teams.toInt() - contest.get(holder.adapterPosition).teams_joined.toInt()
            holder.itemView.txt_StartValue.text = mContext.getString(R.string.only) + " " + strtValue.toString() + " " +
                    mContext.getString(R.string.spots_left)
            holder.itemView.crs_Progress.setMinValue(0f)
            holder.itemView.crs_Progress.setMaxValue(contest.get(holder.adapterPosition).total_teams.toFloat())
            holder.itemView.crs_Progress.setMinStartValue(0f);
            holder.itemView.crs_Progress.setMaxStartValue(contest.get(holder.adapterPosition).teams_joined.toFloat());
            holder.itemView.crs_Progress.apply();
        }

        holder.itemView.ll_entryFee.setOnClickListener {
            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                IntentConstant.CONTEST_TYPE, matchType))
        }

        holder.itemView.ll_totalWinners.setOnClickListener {
            val bottomSheetDialogFragment = BottomSheetWinningListFragment()
            bottomSheetDialogFragment.show(mContext.supportFragmentManager, "Bottom Sheet Dialog Fragment")
        }
        holder.itemView.ll_totalWinnings.setOnClickListener {
            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                IntentConstant.CONTEST_TYPE, matchType))
        }
        holder.itemView.txt_Join.setOnClickListener {
            onClickRecyclerView.onClickItem(Tags.JoinContestDialog,holder.adapterPosition)

        }
    }

    override fun getItemCount(): Int {
        return contest!!.size
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}