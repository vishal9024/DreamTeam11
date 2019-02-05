package os.com.ui.joinedContest.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.item_playerstats_completed_joined.view.*
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant.COMPLETED
import os.com.constant.Tags
import os.com.ui.joinedContest.apiResponse.getSeriesPlayerListResponse.Data
import os.com.ui.joinedContest.dialogues.BottomSheetPriceBreakUpFragment

class PlayerStatsAdapter(
    val mContext: AppCompatActivity,
    val playerPoints: ArrayList<Data>,
    val matchType: Int
) :
    RecyclerView.Adapter<PlayerStatsAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_playerstats_completed_joined, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            holder.itemView.img_playerSelected.isSelected = playerPoints[position].in_contest
            if (matchType == COMPLETED)
                holder.itemView.img_starPlayer.visibility = VISIBLE
            else
                holder.itemView.img_starPlayer.visibility = GONE
            holder.itemView.txt_SelectedBy.text = playerPoints[position].selection_percent
            holder.itemView.txt_PlayerName.text = playerPoints[position].player_name
            holder.itemView.txt_rank.text = playerPoints[position].points
            holder.itemView.txt_SelectedBy.text = playerPoints[position].selection_percent
            ImageLoader.getInstance().displayImage(
                playerPoints[position].player_image,
                holder.itemView.cimg_player,
                FantasyApplication.getInstance().options
            )
            holder.itemView.ll_main.setOnClickListener {
                if (playerPoints[position].player_breckup != null) {
                    val bottomSheetDialogFragment = BottomSheetPriceBreakUpFragment()
                    var bundle = Bundle()
                    bundle.putParcelable(Tags.DATA, playerPoints[position])
                    bottomSheetDialogFragment.arguments = bundle
                    bottomSheetDialogFragment.show(mContext.supportFragmentManager, "Bottom Sheet Dialog Fragment")
                }
            }
//
            holder.itemView.img_starPlayer.isSelected = playerPoints[position].in_dream_team
        } catch (e: Exception) {
//            holder.itemView.ll_main.setBackgroundColor(mContext.resources.getColor(R.color.colorContestItemBackground))
        }
    }

    override fun getItemCount(): Int {
        return playerPoints.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}