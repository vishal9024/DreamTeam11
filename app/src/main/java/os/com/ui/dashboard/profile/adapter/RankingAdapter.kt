package os.com.ui.dashboard.profile.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.item_ranking.view.*
import os.com.R
import os.com.application.FantasyApplication
import os.com.data.Prefs
import os.com.ui.dashboard.profile.activity.MatchStatesActivity
import os.com.ui.dashboard.profile.activity.MyProfileActivity
import os.com.ui.dashboard.profile.activity.OtherUserProfileActivity
import os.com.ui.dashboard.profile.apiResponse.SeriesRankingListResponse


class RankingAdapter(
    val mContext: Context,
    val rankingList: MutableList<SeriesRankingListResponse.ResponseBean.DataBean>,
    val user_id: String
) : RecyclerView.Adapter<RankingAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            if (rankingList[position].user_image != null && !rankingList[position].user_image.equals(""))
                ImageLoader.getInstance().displayImage(
                    rankingList[position].user_image,
                    holder.itemView.cimg_player,
                    FantasyApplication.getInstance().options
                )

            if (rankingList[position].user_id != null && ("" + rankingList[position].user_id).equals(user_id)) {
                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorASPrimary))
                holder.itemView.txt_TeamName.setTextColor(mContext.resources.getColor(R.color.textColorWhite))
                holder.itemView.txt_Points.setTextColor(mContext.resources.getColor(R.color.textColorWhite))
                holder.itemView.txt_rank.setTextColor(mContext.resources.getColor(R.color.textColorWhite))
            }
            if (rankingList[position].team_name != null)
                holder.itemView.txt_TeamName.text = rankingList[position].team_name

            if (rankingList[position].points != null)
                holder.itemView.txt_Points.text = "" + rankingList[position].points + " Points"

            if (rankingList[position].rank != null) {
                holder.itemView.txt_rank.text = "#" + rankingList[position].rank
                if (rankingList[position].rank == 1) {
                    holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.light_pink))
                    holder.itemView.img_kingflag.visibility = View.VISIBLE
                    holder.itemView.txt_rank.setTextColor(mContext.resources.getColor(R.color.orange))
                } else {
                    holder.itemView.img_kingflag.visibility = View.INVISIBLE
                    try {
                        if (position % 2 == 0) {
                            if (rankingList[position].user_id != null && !("" + rankingList[position].user_id).equals(
                                    user_id
                                ) && rankingList[position].rank != 1
                            )
                                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.lightWhite))
                        } else {
                            if (rankingList[position].user_id != null && !("" + rankingList[position].user_id).equals(
                                    user_id
                                ) && rankingList[position].rank != 1
                            )
                                holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.white))
                        }

                    } catch (e: Exception) {
                        holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
                    }
                }

            }

            if (rankingList[position].rank != null && rankingList[position].previous_rank != null) {
                if (rankingList[position].rank == rankingList[position].previous_rank)
                    holder.itemView.img_ranking.visibility = View.INVISIBLE
                else if (rankingList[position].rank < rankingList[position].previous_rank)
                    holder.itemView.img_ranking.setImageDrawable(mContext.resources.getDrawable(R.mipmap.downflag))
                else
                    holder.itemView.img_ranking.setImageDrawable(mContext.resources.getDrawable(R.mipmap.upflag))
            }

            holder.itemView.cimg_player.setOnClickListener {

                if (rankingList[position].user_id != null) {
                    if (rankingList[position].user_id.equals(Prefs(mContext).userdata!!.user_id)) {
                        var intent = Intent(mContext, MyProfileActivity::class.java)
                        intent.putExtra("data", "" + rankingList[position].user_id)
                        mContext.startActivity(intent)
                    } else {
                        var intent = Intent(mContext, OtherUserProfileActivity::class.java)
                        intent.putExtra("data", "" + rankingList[position].user_id)
                        mContext.startActivity(intent)

                    }
                }
            }

            holder.itemView.ll_main.setOnClickListener {
                if (rankingList[position].series_id != null) {
                    var intent = Intent(mContext, MatchStatesActivity::class.java)
                    intent.putExtra("data", "" + rankingList[position].series_id)
                    intent.putExtra("user_id", "" + rankingList[position].user_id)
                    mContext.startActivity(intent)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return rankingList.size
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}