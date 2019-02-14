package os.com.ui.notification.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_notification.view.*
import os.com.R
import os.com.constant.IntentConstant
import os.com.constant.IntentConstant.COMPLETED
import os.com.constant.IntentConstant.LIVE
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.dashboard.profile.activity.MyAccountActivity
import os.com.ui.invite.activity.InviteFriendsActivity
import os.com.ui.joinedContest.activity.CompletedJoinedContestActivity
import os.com.ui.notification.apiResponse.notificationResponse.Data


class NotificationAdapter(
    val mContext: Context,
    val data: ArrayList<Data>?
) : RecyclerView.Adapter<NotificationAdapter.AppliedCouponCodeHolder>() {
    /*1 = admin
    2 = signup
    3 = account (transaction & bonus)
    4 = contest start
    5 = contest end
    6 = contest winning*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        holder.itemView.card_view.setOnClickListener {
            try {
                if (data != null) {
                    if (data[position].nitification_type.equals("1")) {
//                       mContext. startActivity(Intent(mContext, NotificationActivity::class.java))
                    } else if (data!![position].nitification_type.equals("2")) {
                        mContext.startActivity(Intent(mContext, InviteFriendsActivity::class.java))
                    } else if (data!![position].nitification_type.equals("3")) {
                        mContext.startActivity(Intent(mContext, MyAccountActivity::class.java))
                    } else if (data!![position].nitification_type.equals("4") || data!![position].nitification_type.equals(
                            "5"
                        ) || data!![position].nitification_type.equals("6")
                    ) {
                        var contest_id = data[position].match_data!!.contestId
                        val match: Match = Match()
                        match.series_id = data[position].match_data!!.series_id!!
                        match.match_id = data[position].match_data!!.match_id!!
                        match.local_team_id = data[position].match_data!!.local_team_id!!
                        match.local_team_name = data[position].match_data!!.local_team_name!!
                        match.visitor_team_id = data[position].match_data!!.visitor_team_id!!
                        match.visitor_team_name = data[position].match_data!!.visitor_team_name!!
                        match.star_date = data[position].match_data!!.strDate!!
                        match.star_time = data[position].match_data!!.strTime!!
                        match.series_name = ""
                        match.local_team_flag = ""
                        match.visitor_team_flag = ""
                        match.total_contest = ""
                        match.guru_url = ""
                        var type = LIVE
                        type = if (data[position].nitification_type.equals("4"))
                            LIVE
                        else
                            COMPLETED
                        mContext.startActivity(
                            Intent(mContext, CompletedJoinedContestActivity::class.java).putExtra(
                                IntentConstant.MATCH,
                                match
                            ).putExtra(
                                IntentConstant.CONTEST_TYPE, type
                            ).putExtra(IntentConstant.CONTEST_ID, contest_id)
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
//        holder.itemView.ll_totalWinners.setOnClickListener {
//            //            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java))
//        }
//        holder.itemView.ll_totalWinnings.setOnClickListener {
//            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java))
//        }

        holder.itemView.txt_Title.text = data!![position].title
        holder.itemView.txt_Notification.text = data!![position].notification
        holder.itemView.txt_Date.text = data[position].date
    }

    override fun getItemCount(): Int {
        return data!!.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}