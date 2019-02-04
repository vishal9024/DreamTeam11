package os.com.ui.dashboard.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.item_image.view.*
import os.com.R
import os.com.application.FantasyApplication
import os.com.interfaces.IAdapterClick
import os.com.ui.dashboard.profile.apiResponse.AvtarListResponse


class TeamImageAdapter(
    val mContext: Context, var id: Int,
    val data: List<AvtarListResponse.ResponseBean.DataBean>,
    val mListener: IAdapterClick.IItemClick) : RecyclerView.Adapter<TeamImageAdapter.AppliedCouponCodeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppliedCouponCodeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return AppliedCouponCodeHolder(view)
    }

    override fun onBindViewHolder(holder: AppliedCouponCodeHolder, position: Int) {
        try {
            ImageLoader.getInstance().displayImage(
                data[position].image,
                holder.itemView.cimg_user,
                FantasyApplication.getInstance().options
            )
            if (data[position].id==id)
                holder.itemView.imvCheck.visibility = View.VISIBLE
            else holder.itemView.imvCheck.visibility = View.GONE

            holder.itemView.cimg_user.setOnClickListener(View.OnClickListener {
        mListener.onClick(position)
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    inner class AppliedCouponCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }
fun selectImageId(id: Int){
    this.id=id
}

}