package os.com.ui.joinedContest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.item_joined_contest.view.*
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.ui.contest.activity.AllContestActivity
import os.com.ui.contest.apiResponse.getContestList.ContestCategory
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.activity.FixtureJoinedContestDetailActivity

class JoinedFixturesContestAdapter(val mContext: AppCompatActivity,
                                   var contestList: ArrayList<ContestCategory>,
                                   var match: Match?,
                                   var matchType: Int) :
    RecyclerView.Adapter<JoinedFixturesContestAdapter.AppliedCouponCodeHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_joined_contest, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        holder.itemView.card_view.setOnClickListener {
            mContext.startActivity(Intent(mContext, FixtureJoinedContestDetailActivity::class.java))
        }
//
//        holder.itemView.ll_totalWinners.setOnClickListener {
//            val bottomSheetDialogFragment = BottomSheetWinningListFragment()
//            bottomSheetDialogFragment.show(mContext.supportFragmentManager, "Bottom Sheet Dialog Fragment")
//            //            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java))
//        }
//        holder.itemView.ll_totalWinnings.setOnClickListener {
//            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java))
//        }
//        holder.itemView.txt_Join.setOnClickListener {
//            (mContext as BaseActivity).showJoinContestDialogue(mContext)
////            mContext.startActivity(Intent(mContext, ChooseTeamActivity::class.java))
//        }
    }

    override fun getItemCount(): Int {
        return 2;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}