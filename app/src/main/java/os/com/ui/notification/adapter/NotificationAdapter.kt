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
            //            if (data!![position].nitification_type.equals("signup"))
//            mContext.startActivity(Intent(mContext, SignUpActivity::class.java))
//            else if (data!![position].nitification_type.equals("account"))
//                mContext.startActivity(Intent(mContext, SignUpActivity::class.java))
//            else if (data!![position].nitification_type.equals("free contest"))
//                mContext.startActivity(Intent(mContext, SignUpActivity::class.java))
//            else if (data!![position].nitification_type.equals("signup"))
//                mContext.startActivity(Intent(mContext, SignUpActivity::class.java))
//            else if (data!![position].nitification_type.equals("signup"))
//                mContext.startActivity(Intent(mContext, SignUpActivity::class.java))
//            else if (data!![position].nitification_type.equals("signup"))
//                mContext.startActivity(Intent(mContext, SignUpActivity::class.java))

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