package os.com.ui.dashboard.profile.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Toast
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.content_myprofile.*
import kotlinx.android.synthetic.main.fragment_myprofile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.AppBase.BaseFragment
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.profile.activity.ChangePasswordActivity
import os.com.ui.dashboard.profile.activity.FullProfileActivity
import os.com.ui.dashboard.profile.activity.MyAccountActivity
import os.com.ui.dashboard.profile.activity.RankingActivity
import os.com.ui.dashboard.profile.apiResponse.ProfileResponse
import os.com.ui.invite.activity.InviteFriendsActivity
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils


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
                R.id.btn_InviteFriends -> {
                    startActivity(Intent(activity, InviteFriendsActivity::class.java))
                }
                R.id.txtLogout -> {
                    (activity as BaseActivity).showLogoutDialog()
//                val intent = Intent(activity, LoginActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//                activity!!.finish()
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
            btn_InviteFriends.setOnClickListener(this)
            txtLogout.setOnClickListener(this)
            setData()
            if (pref!!.isLogin) {
               if(! pref!!.userdata!!.fb_id.isEmpty() ||! pref!!.userdata!!.google_id.isEmpty()  ){
                   txt_changePassword.visibility=GONE
               }
                if (NetworkUtils.isConnected()) {
                    getProfileData()

                } else
                    Toast.makeText(context!!, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setData() {
        try {
            if (pref!!.userdata != null) {
                if (pref!!.userdata!!.email != null && pref!!.userdata!!.email != "")
                    txtEmail.text = pref!!.userdata!!.email
                if (pref!!.userdata!!.phone != null && pref!!.userdata!!.phone != "")
                    txtMobile.text = pref!!.userdata!!.phone
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getProfileData() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(activity!!)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .profile(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(activity)
                    if (response.response!!.status) {
                        if (response.response.data != null)
                            initData(response.response.data)
//                        AppDelegate.showToast(context, response.response!!.message)
                    } else {
                        AppDelegate.showToast(context, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(activity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var mData: ProfileResponse.ResponseBean.DataBean? = null

    private fun initData(data: ProfileResponse.ResponseBean.DataBean?) {
        try {
            mData = data
            if (mData != null) {
                if (mData!!.team_name != null && mData!!.team_name != "") {
                    txt_name.setText(mData!!.team_name)
                    main_textview_title.setText(mData!!.team_name)
                }
                if (mData!!.total_cash_amount != null && mData!!.total_cash_amount != "")
                    txtCashDeposited.setText("\u20B9 "+mData!!.total_cash_amount)
                if (mData!!.total_winning_amount != null && mData!!.total_winning_amount != "")
                    txtCashWinnings.setText("\u20B9 "+mData!!.total_winning_amount)
                if (mData!!.cash_bonus_amount != null && mData!!.cash_bonus_amount != "")
                    txtCashBonus.setText("\u20B9 "+mData!!.cash_bonus_amount)
                if (mData!!.series_wins != null && mData!!.series_wins != "")
                    txtSeries.setText(mData!!.series_wins)
            }
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
}