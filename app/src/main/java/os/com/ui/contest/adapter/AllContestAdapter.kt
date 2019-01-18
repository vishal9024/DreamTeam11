package os.com.ui.contest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_all_contest.view.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickRecyclerView
import os.com.ui.contest.activity.ContestDetailActivity
import os.com.ui.contest.apiResponse.getContestList.Contest
import os.com.ui.createTeam.activity.myTeam.MyTeamSelectActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match

class AllContestAdapter(
    val mContext: AppCompatActivity,
    val contest: MutableList<Contest>?,
    var matchType: Int,
    var match: Match?
    , val onClickRecyclerView: OnClickRecyclerView
) :
    RecyclerView.Adapter<AllContestAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_all_contest, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        if (!contest!!.get(holder.adapterPosition).entry_fee.isEmpty() && contest.get(holder.adapterPosition).entry_fee.toFloat() > 0) {
            holder.itemView.ll_scoreBoard.visibility = VISIBLE
            holder.itemView.ll_practice.visibility = GONE
        } else {
            holder.itemView.ll_scoreBoard.visibility = GONE
            holder.itemView.ll_practice.visibility = VISIBLE
        }
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
        if (!contest.get(holder.adapterPosition).multiple_team!!) {
            if (contest.get(holder.adapterPosition).is_joined!!) {
                val total_teams = contest.get(holder.adapterPosition).total_teams.toInt() - contest.get(holder.adapterPosition).teams_joined.toInt()
                if (total_teams> 0) {
                    holder.itemView. txt_Join.text =mContext. getString(R.string.invite)
                } else {
                    holder.itemView.txt_Join.text =mContext.  getString(R.string.joined)
                }
            } else {
                holder.itemView. txt_Join.text = mContext. getString(R.string.join)
            }
        } else {
            if (contest.get(holder.adapterPosition).is_joined!!) {
                holder.itemView. txt_Join.text = mContext. getString(R.string.join_plus)
                val total_teams = contest.get(holder.adapterPosition).total_teams.toInt() - contest.get(holder.adapterPosition).teams_joined.toInt()
                if (total_teams > 0) {
                    holder.itemView.txt_Join.text =mContext.  getString(R.string.join_new)
                } else {
                    holder.itemView. txt_Join.text =mContext.  getString(R.string.joined)
                }
            } else {
                holder.itemView. txt_Join.text =mContext.  getString(R.string.join)
            }
        }

        holder.itemView.ll_entryFee.setOnClickListener {
            mContext.startActivity(
                Intent(mContext, ContestDetailActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                    IntentConstant.CONTEST_TYPE, matchType
                ).putExtra(IntentConstant.DATA, contest[holder.adapterPosition])
            )
        }

        holder.itemView.ll_totalWinners.setOnClickListener {
            try {
                if (!contest.get(position).total_winners.isEmpty() && contest.get(position).total_winners!!.toInt() > 0)
                    (mContext as BaseActivity).callWinningBreakupApi(
                        contest[position].contest_id,
                        contest[position].breakup_detail!!,
                        contest[position].prize_money
                    )
            }catch (e: Exception){

            }
//            val bottomSheetDialogFragment = BottomSheetWinningListFragment()
//            var bundle = Bundle()
//            bundle.putString(Tags.contest_id, contest[holder.adapterPosition].contest_id)
//            bottomSheetDialogFragment.arguments = bundle
//            bottomSheetDialogFragment.show(mContext.supportFragmentManager, "Bottom Sheet Dialog Fragment")
        }
        holder.itemView.ll_totalWinnings.setOnClickListener {
            mContext.startActivity(
                Intent(mContext, ContestDetailActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                    IntentConstant.CONTEST_TYPE, matchType
                ).putExtra(IntentConstant.DATA, contest[holder.adapterPosition])
            )
        }
        holder.itemView.ll_practice.setOnClickListener {
            mContext.startActivity(
                Intent(mContext, ContestDetailActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                    IntentConstant.CONTEST_TYPE, matchType
                ).putExtra(IntentConstant.DATA, contest[holder.adapterPosition])
            )
        }
        holder.itemView.txt_Join.setOnClickListener {
            if (!contest.get(holder.adapterPosition).is_joined!!)
                onClickRecyclerView.onClickItem(Tags.JoinContestDialog, holder.adapterPosition)
            else{
                if (holder.itemView.txt_Join.text.toString().equals(mContext.getString(R.string.join_new))){
                    mContext.startActivityForResult(
                        Intent(mContext, MyTeamSelectActivity::class.java).putExtra(
                            IntentConstant.MATCH,
                            match
                        ).putExtra(
                            IntentConstant.CONTEST_TYPE,
                            matchType
                        ).putExtra(IntentConstant.CONTEST_ID, contest[position].contest_id).putExtra(
                            IntentConstant.FOR,
                            AppRequestCodes.JOIN
                        ), AppRequestCodes.UPDATEVIEW
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return contest!!.size
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}