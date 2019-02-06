package os.com.ui.dashboard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rec_tran_child_row.view.*
import os.com.R
import os.com.ui.dashboard.profile.apiResponse.RecentTransactionResponse


class RecentTransactionChildListAdapter(val mContext: Context, val info: MutableList<RecentTransactionResponse.ResponseBean.DataBean.InfoBean>) : RecyclerView.Adapter<RecentTransactionChildListAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rec_tran_child_row, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            holder.itemView.txt_amount.text = info[position].amount!!
            holder.itemView.txtAmountType.text = info[position].txn_type!!
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    override fun getItemCount(): Int {
        return info.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }


}