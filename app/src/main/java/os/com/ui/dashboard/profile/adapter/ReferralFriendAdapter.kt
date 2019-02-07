package os.com.ui.dashboard.profile.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.item_refferral_friend.view.*
import os.com.R
import os.com.application.FantasyApplication
import os.com.data.Prefs
import os.com.ui.dashboard.profile.activity.MyProfileActivity
import os.com.ui.dashboard.profile.activity.OtherUserProfileActivity
import os.com.ui.dashboard.profile.apiResponse.ProfileResponse


class ReferralFriendAdapter(
    val mContext: Context,
    val data: MutableList<ProfileResponse.ResponseBean.DataBean.ReferedToFriendBean>
) : RecyclerView.Adapter<ReferralFriendAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_refferral_friend, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            if (data[position].image != null && !data[position].image.equals(""))
                ImageLoader.getInstance().displayImage(
                    data[position].image,
                    holder.itemView.cimg_user,
                    FantasyApplication.getInstance().options
                )
            if (data[position].team_name != null)
                holder.itemView.txt_InviteFriends.text = data[position].team_name
            holder.itemView.setOnClickListener {
                if (data[position].user_id != null) {
                    if (data[position].user_id.equals(Prefs(mContext)!!.userdata!!.user_id)) {
                        var intent = Intent(mContext, MyProfileActivity::class.java)
                        intent.putExtra("data", "" + data[position].user_id)
                        mContext.startActivity(intent)
                    } else {
                        var intent = Intent(mContext, OtherUserProfileActivity::class.java)
                        intent.putExtra("data", "" + data[position].user_id)
                        mContext.startActivity(intent)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

}