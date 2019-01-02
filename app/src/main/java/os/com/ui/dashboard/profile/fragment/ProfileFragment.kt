package os.com.ui.dashboard.profile.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.content_myprofile.*
import kotlinx.android.synthetic.main.fragment_myprofile.*
import os.com.AppBase.BaseFragment
import os.com.R
import os.com.ui.dashboard.profile.activity.ChangePasswordActivity
import os.com.ui.dashboard.profile.activity.FullProfileActivity
import os.com.ui.dashboard.profile.activity.MyAccountActivity
import os.com.ui.dashboard.profile.activity.RankingActivity


/**
 * Created by heenas on 3/5/2018.
 */
class ProfileFragment : BaseFragment(), View.OnClickListener, AppBarLayout.OnOffsetChangedListener {
    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.txt_MyAccount -> {
                    startActivity(Intent(activity, MyAccountActivity::class.java))
                }
                R.id.txt_fullProfile -> {
                    startActivity(Intent(activity, FullProfileActivity::class.java))
                }
                R.id.txt_changePassword -> {
                    startActivity(Intent(activity, ChangePasswordActivity::class.java))
                }
                R.id.txt_Ranking -> {
                    startActivity(Intent(activity, RankingActivity::class.java))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true

//    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
//    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
//    private val ALPHA_ANIMATIONS_DURATION = 200
//

    private var appBarExpanded = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_myprofile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            Handler().postDelayed(Runnable {
                initViews()
            }, 10)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initViews() {
        try {
            app_bar.addOnOffsetChangedListener(this)
            startAlphaAnimation(toolbar, 0, View.INVISIBLE)
            txt_MyAccount.setOnClickListener(this)
            txt_fullProfile.setOnClickListener(this)
            txt_changePassword.setOnClickListener(this)
            txt_Ranking.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        try {
            val maxScroll = appBarLayout.totalScrollRange
            val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()

            handleAlphaOnTitle(percentage)
            handleToolbarTitleVisibility(percentage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        try {
            if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

                if (!mIsTheTitleVisible) {
                    startAlphaAnimation(toolbar!!, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                    mIsTheTitleVisible = true
                }

            } else {

                if (mIsTheTitleVisible) {
                    startAlphaAnimation(toolbar!!, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                    mIsTheTitleVisible = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        try {
            if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
                if (mIsTheTitleContainerVisible) {
                    startAlphaAnimation(ll_main!!, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                    mIsTheTitleContainerVisible = false
                }

            } else {

                if (!mIsTheTitleContainerVisible) {
                    startAlphaAnimation(ll_main!!, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                    mIsTheTitleContainerVisible = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {

        private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.6f
        private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
        private val ALPHA_ANIMATIONS_DURATION = 200

        fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
            val alphaAnimation = if (visibility == View.VISIBLE)
                AlphaAnimation(0f, 1f)
            else
                AlphaAnimation(1f, 0f)

            alphaAnimation.duration = duration
            alphaAnimation.fillAfter = true
            v.startAnimation(alphaAnimation)
        }
    }
//    private fun initViews() {
//        main_appbar.addOnOffsetChangedListener(this)
//
//        startAlphaAnimation(main_textview_title, 0, View.INVISIBLE)
//    }
//
//  override  fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
//        val maxScroll = appBarLayout.getTotalScrollRange()
//        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()
//
//        handleAlphaOnTitle(percentage)
//        handleToolbarTitleVisibility(percentage)
//    }
//
//    private fun handleToolbarTitleVisibility(percentage: Float) {
//        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
//
//            if (!mIsTheTitleVisible) {
//                startAlphaAnimation(main_textview_title, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
//                mIsTheTitleVisible = true
//            }
//
//        } else {
//
//            if (mIsTheTitleVisible) {
//                startAlphaAnimation(main_textview_title, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
//                mIsTheTitleVisible = false
//            }
//        }
//    }
//
//    private fun handleAlphaOnTitle(percentage: Float) {
//        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
//            if (mIsTheTitleContainerVisible) {
//                startAlphaAnimation(main_linearlayout_title, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
//                mIsTheTitleContainerVisible = false
//            }
//
//        } else {
//
//            if (!mIsTheTitleContainerVisible) {
//                startAlphaAnimation(main_linearlayout_title, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
//                mIsTheTitleContainerVisible = true
//            }
//        }
//    }
//
//    fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
//        val alphaAnimation = if (visibility == View.VISIBLE)
//            AlphaAnimation(0f, 1f)
//        else
//            AlphaAnimation(1f, 0f)
//
//        alphaAnimation.duration = duration
//        alphaAnimation.fillAfter = true
//        v.startAnimation(alphaAnimation)
//    }

}