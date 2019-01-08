package os.com.ui.dashboard.home.adapter

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import os.com.R

class SlidingImageAdapterHomeBanner(private val context: FragmentActivity/*, private val IMAGES: List<Home>*/) :
    PagerAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return 11
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.child_banner_layout, view, false)!!

        val imageView = imageLayout.findViewById<View>(R.id.imv_product) as ImageView

//        ImageLoader.getInstance().displayImage(IMAGES[position].img, imageView, FantasyApplication.getInstance().options)
//        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        view.addView(imageLayout, 0)
//        imageView.setOnClickListener{context.startActivity(Intent(context, ZoomActivity::class.java).putExtra("URL",IMAGES[position].img))}
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

}

