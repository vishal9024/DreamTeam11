package os.com.ui.joinedContest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_joined_contest.view.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.interfaces.OnClickDialogue
import os.com.ui.contest.activity.ContestDetailActivity
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamSelectActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.activity.FixtureJoinedContestActivity
import os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse.JoinedContestData
import os.com.utils.networkUtils.NetworkUtils

class JoinedFixturesContestAdapter(
    val mContext: AppCompatActivity,
    var match: Match?,
    var matchType: Int,
    var data: ArrayList<JoinedContestData>
) :
    RecyclerView.Adapter<JoinedFixturesContestAdapter.AppliedCouponCodeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_joined_contest, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        holder.itemView.card_view.setOnClickListener {
            mContext.startActivity(
                Intent(mContext, ContestDetailActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                    IntentConstant.CONTEST_TYPE, matchType
                ).putExtra(IntentConstant.DATA, data[holder.adapterPosition]).putExtra(IntentConstant.FROM,AppRequestCodes.JOINED)
            )
        }
        if (!data!!.get(holder.adapterPosition).entry_fee.isEmpty() && data.get(holder.adapterPosition).entry_fee.toFloat() > 0) {
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
        holder.itemView.txt_EndValue.text = data.get(holder.adapterPosition).total_teams + " " +
                mContext.getString(R.string.teams)
//        if (data.get(holder.adapterPosition).is_joined)
//            holder.itemView.txt_Join.text = mContext.getString(R.string.joined)
//        else
//            holder.itemView.txt_Join.text = mContext.getString(R.string.join)
//
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

        if (!data.get(holder.adapterPosition).multiple_team!!) {
            if (data.get(holder.adapterPosition).is_joined!!) {
                val total_teams =
                    data.get(holder.adapterPosition).total_teams.toInt() - data.get(holder.adapterPosition).teams_joined.toInt()
                if (total_teams > 0) {
                    holder.itemView.txt_Join.text = mContext.getString(R.string.invite)
                } else {
                    holder.itemView.txt_Join.text = mContext.getString(R.string.joined)
                }
            } else {
                holder.itemView.txt_Join.text = mContext.getString(R.string.join)
            }
        } else {
            if (data.get(holder.adapterPosition).is_joined!!) {
                holder.itemView.txt_Join.text = mContext.getString(R.string.join_plus)
                val total_teams =
                    data.get(holder.adapterPosition).total_teams.toInt() - data.get(holder.adapterPosition).teams_joined.toInt()
                if (total_teams > 0) {
                    holder.itemView.txt_Join.text = mContext.getString(R.string.join_new)
                } else {
                    holder.itemView.txt_Join.text = mContext.getString(R.string.joined)
                }
            } else {
                holder.itemView.txt_Join.text = mContext.getString(R.string.join)
            }
        }

        holder.itemView.txt_Join.setOnClickListener {
            if (!data.get(holder.adapterPosition).is_joined!!) {
                if (FantasyApplication.getInstance().teamCount==0 ) {
                    mContext.startActivityForResult(
                        Intent(mContext, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                            IntentConstant.CONTEST_TYPE,
                            matchType
                        ).putExtra(IntentConstant.CONTEST_ID, data[position].contest_id)
                            .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.JOIN),
                        AppRequestCodes.UPDATE_ACTIVITY
                    )
                } else if (FantasyApplication.getInstance().teamCount == 1) {
                    if (NetworkUtils.isConnected()) {
                        (mContext as BaseActivity).checkAmountWallet(
                            match!!.match_id,
                            match!!.series_id, data[position].contest_id, "", object : OnClickDialogue {
                                override fun onClick(tag: String, success: Boolean) {
                                    (mContext as FixtureJoinedContestActivity).callGetJoinedContestListApi()
                                }
                            }
                        )
                    } else
                        Toast.makeText(
                            mContext,
                            mContext.getString(R.string.error_network_connection),
                            Toast.LENGTH_LONG
                        ).show()
                } else {
                    mContext.startActivityForResult(
                        Intent(mContext, MyTeamSelectActivity::class.java).putExtra(
                            IntentConstant.MATCH,
                            match
                        ).putExtra(
                            IntentConstant.CONTEST_TYPE,
                            matchType
                        ).putExtra(IntentConstant.CONTEST_ID, data[position].contest_id).putExtra(
                            IntentConstant.FOR,
                            AppRequestCodes.JOIN
                        ), AppRequestCodes.UPDATEVIEW
                    )
                }
            } else {
                if (holder.itemView.txt_Join.text.toString().equals(mContext.getString(R.string.join_new))) {
                    if(data[position].my_team_ids!!.size== FantasyApplication.getInstance().teamCount){
                        mContext.startActivityForResult(
                            Intent(mContext, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                                IntentConstant.CONTEST_TYPE,
                                matchType
                            ).putExtra(IntentConstant.CONTEST_ID, data[position].contest_id)
                                .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.JOIN),
                            AppRequestCodes.UPDATE_ACTIVITY
                        )
                    }else{
                        mContext.startActivityForResult(
                            Intent(mContext, MyTeamSelectActivity::class.java).putExtra(
                                IntentConstant.MATCH,
                                match
                            ).putExtra(
                                IntentConstant.CONTEST_TYPE,
                                matchType
                            ) .putExtra(IntentConstant.TEAM_ID, data[position].my_team_ids)
                                .putExtra(IntentConstant.CONTEST_ID, data[position].contest_id).putExtra(
                                    IntentConstant.FOR,
                                    AppRequestCodes.JOIN_PLUS
                                ), AppRequestCodes.UPDATEVIEW
                        )
                    }

                } else if (holder.itemView.txt_Join.text.toString().equals(mContext.getString(R.string.invite))) {
                    val shareCode =
                        "You've been challanged! \n\nThink you can beat me? Join the contest on " + mContext.getString(R.string.app_name) + " for the " + match!!.series_name + " match and prove it! \n\nUse Contest Code " + data[position].invite_code.capitalize() + " & join the action NOW!"
                    prepareShareIntent(shareCode)
                }
            }
//            (mContext as BaseActivity).showJoinContestDialogue(mContext,match,matchType)
        }


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
    public fun prepareShareIntent(shareCode: String) {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.putExtra(Intent.EXTRA_TEXT,  shareCode)
        sharingIntent.type = "text/plain";
        mContext.startActivity(Intent.createChooser(sharingIntent, "Invite"))
    }
    override fun getItemCount(): Int {
        return data.size
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}