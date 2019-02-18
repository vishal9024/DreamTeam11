package os.com.ui.dashboard.home.adapter

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.item_match.view.*
import os.com.BuildConfig
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.interfaces.OnClickRecyclerView
import os.com.ui.contest.activity.ContestActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate
import java.util.*
import com.github.amlcurran.showcaseview.targets.ViewTarget
import com.github.amlcurran.showcaseview.ShowcaseView




class MatchFixturesAdapter(
    val mContext: Context,
    var matchList: List<Match>,
    val onClickRecyclerView: OnClickRecyclerView
) :
    RecyclerView.Adapter<MatchFixturesAdapter.AppliedCouponCodeHolder>() {
    internal var tmr: Timer? = null
    private val mHandler = Handler()
    private var lstHolders: MutableList<AppliedCouponCodeHolder>? = null
    private val updateRemainingTimeRunnable = Runnable {
        synchronized(lstHolders!!) {
            val currentTime = System.currentTimeMillis()
            for (holder in lstHolders!!) {
                holder.updateTimeRemaining(currentTime)
            }
        }
    }

    init {
        lstHolders = ArrayList<AppliedCouponCodeHolder>()
//        Handler().postDelayed({ startUpdateTimer() }, 2000)
        startUpdateTimer()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        synchronized(lstHolders!!) {
            lstHolders!!.add(holder)
        }

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
                if (!matchList.get(position).total_contest.isEmpty())
                    if (matchList.get(position).total_contest.toLong() > 0)
                        mContext.startActivity(
                            Intent(mContext, ContestActivity::class.java).putExtra(
                                IntentConstant.DATA,
                                matchList.get(position)
                            ).putExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
                        )
                    else
                        AppDelegate.showAlert(mContext, mContext.getString(R.string.coming_soon))
            }
            holder.itemView.txt_Title.text = matchList.get(position).series_name
            holder.itemView.txt_Team1.text = matchList.get(position).local_team_name
            holder.itemView.txt_Team2.text = matchList.get(position).visitor_team_name

            ImageLoader.getInstance().displayImage(
                matchList[position].local_team_flag,
                holder.itemView.cimg_Match2,
                FantasyApplication.getInstance().options
            )
            ImageLoader.getInstance().displayImage(
                matchList[position].visitor_team_flag,
                holder.itemView.cimg_Match1,
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

        fun updateTimeRemaining(currentTime: Long) {
            try {
                if (adapterPosition != -1) {
                    val contestsModel = matchList.get(getAdapterPosition())
                    if (!contestsModel.star_date.isEmpty()) {
                        val strt_date = contestsModel.star_date.split("T")
                        val dateTime = strt_date.get(0) + " " + contestsModel.star_time
                        val timeDiff = AppDelegate.getTimeStampFromDate(dateTime)!! - currentTime
                        if (timeDiff > 0) {
                            val seconds = (timeDiff / 1000).toInt() % 60
                            val minutes = (timeDiff / (1000 * 60) % 60).toInt()
                            val hours = (timeDiff / (1000 * 60 * 60)).toInt()
                            if (hours > 72) {
                                itemView.txt_Countdown.text = AppDelegate.convertTimestampToDate(
                                    AppDelegate.getTimeStampFromDate(
                                        dateTime
                                    )!!
                                )
                            } else {
                                itemView.txt_Countdown.setText(hours.toString() + "h " + minutes + "m " + seconds + "s")
                            }
                        } else {
                            if (onClickRecyclerView != null)
                                onClickRecyclerView.onClickItem("remove", adapterPosition)
                            itemView.txt_Countdown.setText("0 sec")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startUpdateTimer() {
        tmr = Timer()
        tmr!!.schedule(object : TimerTask() {
            override fun run() {
                mHandler.post(updateRemainingTimeRunnable)
            }
        }, 1000, 1000)
    }

    fun stopUpdateTimer() {
        if (tmr != null) {
            tmr!!.cancel()
        }

    }

}