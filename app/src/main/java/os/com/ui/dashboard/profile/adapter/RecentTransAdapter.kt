package os.com.ui.dashboard.profile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_recent_trans.view.*
import os.com.R
import os.com.ui.dashboard.profile.apiResponse.RecentTransactionResponse


class RecentTransAdapter(val mContext: Context, val data: MutableList<RecentTransactionResponse.ResponseBean.DataBean>) : RecyclerView.Adapter<RecentTransAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_trans, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try{
            holder.itemView.txtdate.text = data[position].date!!
            setAdapter(holder.itemView.rvlisttransaction,data[position].info!!)

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return data.size;
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(rv_rank: RecyclerView, info: MutableList<RecentTransactionResponse.ResponseBean.DataBean.InfoBean>
    ) {
        try {
            val llm = LinearLayoutManager(mContext)
            llm.orientation = LinearLayoutManager.VERTICAL
            rv_rank!!.layoutManager = llm
            rv_rank!!.adapter = RecentTransactionChildListAdapter(mContext!!,info)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}