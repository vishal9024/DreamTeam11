package os.com.ui.dashboard.home.adapter

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.item_match.view.*
import os.com.BuildConfig
import os.com.R
import os.com.application.FantasyApplication
import os.com.ui.contest.activity.ContestActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate


class MatchFixturesAdapter(val mContext: Context, var matchList: List<Match>) :
    RecyclerView.Adapter<MatchFixturesAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            if (BuildConfig.APPLICATION_ID == "os.real11") {
                holder.itemView.txt_Countdown.setTextColor(mContext.resources.getColor(R.color.colorRed))
                for (drawable in holder.itemView.txt_Countdown.compoundDrawables) {
                    if (drawable != null) {
                        drawable.colorFilter = PorterDuffColorFilter(
                            mContext.resources.getColor(R.color.colorRed), PorterDuff.Mode.SRC_IN
                        )
                    }
                }
            }
            holder.itemView.view2.visibility = View.GONE
            holder.itemView.txt_contestJoined.visibility = View.GONE
            holder.itemView.card_view.setOnClickListener {
                mContext.startActivity(Intent(mContext, ContestActivity::class.java))
            }
            holder.itemView.txt_Title.text = matchList.get(position).series_name
            holder.itemView.txt_Team1.text = matchList.get(position).local_team_name
            holder.itemView.txt_Team2.text = matchList.get(position).visitor_team_name

            ImageLoader.getInstance().displayImage(
                matchList[position].local_team_flag,
                holder.itemView.cimg_Match1,
                FantasyApplication.getInstance().options
            )
            ImageLoader.getInstance().displayImage(
                matchList[position].visitor_team_flag,
                holder.itemView.cimg_Match2,
                FantasyApplication.getInstance().options
            )

        } catch (e: Exception) {
            AppDelegate.LogE(e.printStackTrace().toString())
        }


    }


    override fun getItemCount(): Int {
        return matchList.size
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}