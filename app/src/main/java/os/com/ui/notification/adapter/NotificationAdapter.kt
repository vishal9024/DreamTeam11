package os.com.ui.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_notification.view.*
import os.com.R
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
//            try {
//                if (data != null) {
//                    if (data!![position].nitification_type.equals("1")) {
//                        Log.e("title: ", data.title)
//                        startActivity(Intent(this@DashBoardActivity, NotificationActivity::class.java))
//                    } else if (data.type.equals("2")) {
//                        Log.e("title: ", data.title)
//                        startActivity(Intent(this@DashBoardActivity, MyProfileActivity::class.java))
//                    } else if (data.type.equals("3")) {
//                        Log.e("title: ", data.title)
//                        startActivity(Intent(this@DashBoardActivity, MyAccountActivity::class.java))
//                    } else if (data!![position].nitification_type.equals("4") || data!![position].nitification_type.equals("5") || data!![position].nitification_type.equals("6")) {
//                        Log.e("title: ", data.title)
//                        var contest_id = ""
//                        var match: Match = Match()
//                        var jsonObject = JSONObject(data.matchData)
//                        if (jsonObject.has("contestId"))
//                            contest_id = jsonObject.optString("contestId")
//                        if (jsonObject.has("visitor_team_name"))
//                            match.visitor_team_name = jsonObject.optString("visitor_team_name")
//                        if (jsonObject.has("match_id"))
//                            match.match_id = jsonObject.optString("match_id")
//                        if (jsonObject.has("visitor_team_id"))
//                            match.visitor_team_id = jsonObject.optString("visitor_team_id")
//                        if (jsonObject.has("strTime"))
//                            match.star_time = jsonObject.optString("strTime")
//                        if (jsonObject.has("strDate"))
//                            match.star_date = jsonObject.optString("strDate")
//                        if (jsonObject.has("local_team_id"))
//                            match.local_team_id = jsonObject.optString("local_team_id")
//                        if (jsonObject.has("series_id"))
//                            match.series_id = jsonObject.optString("series_id")
//                        if (jsonObject.has("local_team_name"))
//                            match.local_team_name = jsonObject.optString("local_team_name")
//
//                        startActivity(
//                            Intent(this, LeaderShipBoardActivity::class.java).putExtra(
//                                IntentConstant.MATCH,
//                                match
//                            ).putExtra(
//                                IntentConstant.CONTEST_TYPE, IntentConstant.LIVE
//                            ).putExtra(IntentConstant.CONTEST_ID, contest_id)
//                                .putExtra(
//                                    IntentConstant.FROM, AppRequestCodes.JOINED
//                                )
//                        )
//                    }
//
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
        }
//        holder.itemView.ll_totalWinners.setOnClickListener {
//            //            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java))
//        }
//        holder.itemView.ll_totalWinnings.setOnClickListener {
//            mContext.startActivity(Intent(mContext, ContestDetailActivity::class.java))
//        }

        holder.itemView.txt_Notification.text = data!![position].notification
        holder.itemView.txt_Date.text = data[position].date
    }

    override fun getItemCount(): Int {
        return data!!.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}