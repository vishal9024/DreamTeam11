package os.com.ui.contest.adapter.ContestAdapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.item_contest_header.view.*
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.ui.contest.activity.AllContestActivity
import os.com.ui.contest.activity.ContestActivity
import os.com.ui.contest.apiResponse.getContestList.ContestCategory
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match


class ContestMainAdapter(
    val mContext: ContestActivity,
    var contestList: ArrayList<ContestCategory>,
    var match: Match?,
    var matchType: Int
) :
    RecyclerView.Adapter<ContestMainAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contest_header, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        ImageLoader.getInstance().displayImage(
            contestList[position].category_image,
            holder.itemView.img_category,
            FantasyApplication.getInstance().options
        )
        holder.itemView.txt_categoryTitle.text = contestList[position].category_title
        holder.itemView.txt_categoryDesc.text = contestList[position].category_desc
        if (contestList[position].contests!!.size > 3) {
            var count = contestList[position].contests!!.size-3
            holder.itemView.txt_view_all.text=count.toString()+" "+mContext.getString(R.string.view_all)
            holder.itemView.ll_view_all.visibility = View.VISIBLE
        }
        else
            holder.itemView.ll_view_all.visibility = View.GONE
        holder.itemView.ll_view_all.setOnClickListener {
            mContext.startActivity(
                Intent(mContext, AllContestActivity::class.java)
                    .putParcelableArrayListExtra(IntentConstant.DATA, contestList.get(holder.adapterPosition).contests)
                    .putExtra(IntentConstant.MATCH, match)
                    .putExtra(IntentConstant.CONTEST_TYPE, matchType)
            )
        }
        holder.contestAdapter.contestList(contestList[position].contests!!,match!!,matchType)
        holder.contestAdapter.notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return contestList.size
    }

    @SuppressLint("WrongConstant")
    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contestAdapter = ContestAdapter(mContext)
        /* init method call itself when class call & set layout manger for recycler view*/
        init {
            val llm = LinearLayoutManager(mContext)
            llm.orientation = LinearLayoutManager.VERTICAL
            itemView.rv_subContest.layoutManager = llm
            itemView.rv_subContest.adapter = contestAdapter


        }
    }


}