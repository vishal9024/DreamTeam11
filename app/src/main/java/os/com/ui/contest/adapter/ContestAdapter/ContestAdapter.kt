package os.com.ui.contest.adapter.ContestAdapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_contest.view.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
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
import os.com.utils.networkUtils.NetworkUtils


class ContestAdapter(val mContext: ContestActivity) : RecyclerView.Adapter<ContestAdapter.AppliedCouponCodeHolder>() {
        var contest: List<Contest> = ArrayList()
    var match: Match? = null
    var matchType: Int = IntentConstant.FIXTURE
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contest, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        if (!contest.get(holder.adapterPosition).entry_fee.isEmpty() && contest.get(holder.adapterPosition).entry_fee.toFloat() > 0) {
            holder.itemView.ll_scoreBoard.visibility = VISIBLE
            holder.itemView.ll_practice.visibility = GONE
        } else {
            holder.itemView.ll_scoreBoard.visibility = GONE
            holder.itemView.ll_practice.visibility = VISIBLE
        }
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
            holder.itemView.txt_StartValue.text = mContext.getString(R.string.only) + " " + strtValue.toString() + " " +
                    mContext.getString(R.string.spots_left)
            holder.itemView.crs_Progress.setMinValue(0f)
            holder.itemView.crs_Progress.setMaxValue(contest.get(holder.adapterPosition).total_teams.toFloat())
            holder.itemView.crs_Progress.setMinStartValue(0f);
            holder.itemView.crs_Progress.setMaxStartValue(contest.get(holder.adapterPosition).teams_joined.toFloat());
            holder.itemView.crs_Progress.apply();
        }

        holder.itemView.ll_entryFee.setOnClickListener {
            mContext.startActivityForResult(
                Intent(mContext, ContestDetailActivity::class.java).putExtra(
                    IntentConstant.MATCH,
                    match
                ).putExtra(IntentConstant.CONTEST_TYPE, matchType).putExtra(
                    IntentConstant.DATA,
                    contest[holder.adapterPosition]
                ) , UPDATE_ACTIVITY
            )
        }

        holder.itemView.ll_totalWinners.setOnClickListener {
            if (!contest.get(position).total_winners.isEmpty() && contest.get(position).total_winners.toInt() > 0)
                (mContext as BaseActivity).callWinningBreakupApi(
                    contest[position].contest_id,
                    contest[position].breakup_detail,
                    contest[position].prize_money
                )
        }
        holder.itemView.ll_totalWinnings.setOnClickListener {
//            mContext.callApi = true
            mContext.startActivityForResult(
                Intent(mContext, ContestDetailActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                    IntentConstant.CONTEST_TYPE, matchType
                ).putExtra(IntentConstant.DATA, contest[holder.adapterPosition])
                    , UPDATE_ACTIVITY
            )
        }
        holder.itemView.ll_practice.setOnClickListener {
//            mContext.callApi = true
            mContext.startActivityForResult(
                Intent(mContext, ContestDetailActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                    IntentConstant.CONTEST_TYPE, matchType
                ).putExtra(IntentConstant.DATA, contest[holder.adapterPosition])
                , UPDATE_ACTIVITY
            )
        }
        if (!contest.get(holder.adapterPosition).multiple_team!!) {
            if (contest.get(holder.adapterPosition).is_joined!!) {
                val total_teams =
                    contest.get(holder.adapterPosition).total_teams.toInt() - contest.get(holder.adapterPosition).teams_joined.toInt()
                if (total_teams > 0) {
                    holder.itemView.txt_Join.text = mContext.getString(R.string.invite)
                } else {
                    holder.itemView.txt_Join.text = mContext.getString(R.string.joined)
                }
            } else {
                holder.itemView.txt_Join.text = mContext.getString(R.string.join)
            }
        } else {
            if (contest.get(holder.adapterPosition).is_joined!!) {
                holder.itemView.txt_Join.text = mContext.getString(R.string.join_plus)
                val total_teams =
                    contest.get(holder.adapterPosition).total_teams.toInt() - contest.get(holder.adapterPosition).teams_joined.toInt()
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
            if (!contest.get(holder.adapterPosition).is_joined!!) {
                if (FantasyApplication.getInstance().teamCount==0 ) {
//                    mContext.callApi = true
                    mContext.startActivityForResult(
                        Intent(mContext, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                            IntentConstant.CONTEST_TYPE,
                            matchType
                        ).putExtra(IntentConstant.CONTEST_ID, contest[position].contest_id)
                            .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.JOIN), UPDATE_ACTIVITY
                    )
                } else if (FantasyApplication.getInstance().teamCount == 1) {
//                    mContext.callApi = true
                    if (NetworkUtils.isConnected()) {
                        (mContext as BaseActivity).checkAmountWallet(
                            match!!.match_id,
                            match!!.series_id, contest[position].contest_id, "", object : OnClickDialogue {
                                override fun onClick(tag: String, success: Boolean) {
                                    mContext.callGetContestListApi()
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
//                    mContext.callApi = true
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
                        ), UPDATEVIEW
                    )
                }
            } else {
                if (holder.itemView.txt_Join.text.toString().equals(mContext.getString(R.string.join_new))) {
//                    mContext.callApi = true
                    if(contest[position].my_team_ids.size==FantasyApplication.getInstance().teamCount){
                        mContext.startActivityForResult(
                            Intent(mContext, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                                IntentConstant.CONTEST_TYPE,
                                matchType
                            ).putExtra(IntentConstant.CONTEST_ID, contest[position].contest_id)
                                .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.JOIN), UPDATE_ACTIVITY
                        )
                    }else{
                        mContext.startActivityForResult(
                            Intent(mContext, MyTeamSelectActivity::class.java).putExtra(
                                IntentConstant.MATCH,
                                match
                            ).putExtra(
                                IntentConstant.CONTEST_TYPE,
                                matchType
                            ) .putExtra(IntentConstant.TEAM_ID, contest[position].my_team_ids)
                                .putExtra(IntentConstant.CONTEST_ID, contest[position].contest_id).putExtra(
                                    IntentConstant.FOR,
                                    AppRequestCodes.JOIN_PLUS
                                ), UPDATEVIEW
                        )
                    }

                } else if (holder.itemView.txt_Join.text.toString().equals(mContext.getString(R.string.invite))) {
                    val shareCode =
                        "You've been challanged! \n\nThink you can beat me? Join the contest on " + mContext.getString(R.string.app_name) + " for the " + match!!.series_name + " match and prove it! \n\nUse Contest Code " + contest[position].invite_code.capitalize() + " & join the action NOW!"
                    prepareShareIntent(shareCode)
                }
            }
//            (mContext as BaseActivity).showJoinContestDialogue(mContext,match,matchType)
        }
    }

    public fun prepareShareIntent(shareCode: String) {
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "https://56.octallabs.com/drewel/product_details/" + productId)
//            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, shareImagePath, "https://56.octallabs.com/drewel/product_details/"  + productId, "https://56.octallabs.com/drewel/product_details/"  + productId)))
        sharingIntent.putExtra(Intent.EXTRA_TEXT,  shareCode)
        sharingIntent.type = "text/plain";
        mContext.startActivity(Intent.createChooser(sharingIntent, "Invite"))
    }

    fun contestList(data: List<Contest>, match: Match, matchType: Int) {
        if (contest !== data) {
            contest = data
            notifyDataSetChanged()
        }
        this.match = match
        this.matchType = matchType
    }


    override fun getItemCount(): Int {
        if (contest.size > 3)
            return 3
        else return contest.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}