package os.com.ui.contest.adapter.ContestAdapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_contest.view.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes.UPDATEVIEW
import os.com.constant.AppRequestCodes.UPDATE_ACTIVITY
import os.com.constant.IntentConstant
import os.com.interfaces.OnClickDialogue
import os.com.ui.contest.activity.ContestActivity
import os.com.ui.contest.activity.ContestDetailActivity
import os.com.ui.contest.apiResponse.getContestList.Contest
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamSelectActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.winningBreakup.dialogues.BottomSheetWinningListFragment
import os.com.utils.networkUtils.NetworkUtils


class ContestAdapter(val mContext: ContestActivity) : RecyclerView.Adapter<ContestAdapter.AppliedCouponCodeHolder>() {
    var contest: List<Contest> = ArrayList()
    var match: Match?=null
    var matchType: Int=IntentConstant.FIXTURE
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contest, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        holder.itemView.txt_TotalWinnings.text = mContext.getString(R.string.Rs) + " " +
                contest.get(holder.adapterPosition).prize_money
        holder.itemView.txt_Winners.text = contest.get(holder.adapterPosition).total_winners
        holder.itemView.txt_EntryFee.text = mContext.getString(R.string.Rs) + " " +
                contest.get(holder.adapterPosition).entry_fee
        holder.itemView.txt_EndValue.text = contest.get(holder.adapterPosition).total_teams + " " +
                mContext.getString(R.string.teams)

        if (!contest.get(holder.adapterPosition).total_teams.isEmpty() && !contest.get(holder.adapterPosition).teams_joined.isEmpty()) {
            val strtValue =
                contest.get(holder.adapterPosition).total_teams.toInt() - contest.get(holder.adapterPosition).teams_joined.toInt()
            holder.itemView.txt_StartValue.text = mContext.getString(R.string.only) + " " + strtValue.toString() + " "+mContext.getString(R.string.spots_left)
            holder.itemView.crs_Progress.setMinValue(0f)
            holder.itemView.crs_Progress.setMaxValue(contest.get(holder.adapterPosition).total_teams.toFloat())
            holder.itemView.crs_Progress.setMinStartValue(0f);
            holder.itemView.crs_Progress.setMaxStartValue(contest.get(holder.adapterPosition).teams_joined.toFloat());
            holder.itemView.crs_Progress.apply();
        }

        holder.itemView.ll_entryFee.setOnClickListener {
            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(IntentConstant.CONTEST_TYPE, matchType).putExtra(IntentConstant.DATA,contest[holder.adapterPosition]))
        }
        holder.itemView.ll_totalWinners.setOnClickListener {
            val bottomSheetDialogFragment = BottomSheetWinningListFragment()
            bottomSheetDialogFragment.show(mContext.supportFragmentManager, "Bottom Sheet Dialog Fragment")
        }
        holder.itemView.ll_totalWinnings.setOnClickListener {
            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                IntentConstant.CONTEST_TYPE, matchType).putExtra(IntentConstant.DATA,contest[holder.adapterPosition]))
        }
        holder.itemView.txt_Join.setOnClickListener {
            if (FantasyApplication.getInstance().teamCount==0) {
                mContext.startActivityForResult(
                    Intent(mContext, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    ).putExtra(IntentConstant.CONTEST_ID, contest[position].contest_id)
                        .putExtra(IntentConstant.CREATE_OR_JOIN, IntentConstant.JOIN),UPDATE_ACTIVITY
                )
            }else if (FantasyApplication.getInstance().teamCount==1){
                    if (NetworkUtils.isConnected()) {
                        (mContext as BaseActivity) .checkAmountWallet(
                            match!!.match_id,
                            match!!.series_id,  contest[position].contest_id, "",object: OnClickDialogue{
                                override fun onClick(tag: String, success: Boolean) {
//                                    if (tag.equals(Tags.success) && success)
//                                        finish()
                                }
                            }
                        )
                    } else
                        Toast.makeText(mContext,mContext. getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
            }else{
                mContext.startActivityForResult(
                    Intent(mContext, MyTeamSelectActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    ).putExtra(IntentConstant.CONTEST_ID, contest[position].contest_id),UPDATEVIEW
                )
            }
//            (mContext as BaseActivity).showJoinContestDialogue(mContext,match,matchType)
        }
    }

    fun contestList(data: List<Contest>,match: Match,matchType:Int) {
        if (contest !== data) {
            contest = data
            notifyDataSetChanged()
        }
        this.match=match
        this.matchType=matchType
    }


    override fun getItemCount(): Int {
        if (contest.size > 3)
            return 3
        else return contest.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}