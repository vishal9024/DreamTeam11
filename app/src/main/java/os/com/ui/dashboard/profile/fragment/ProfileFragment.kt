package os.com.ui.dashboard.profile.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.View.GONE
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.content_myprofile.*
import kotlinx.android.synthetic.main.dailog_team_image.*
import kotlinx.android.synthetic.main.fragment_myprofile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.AppBase.BaseFragment
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.IAdapterClick
import os.com.networkCall.ApiClient
import os.com.ui.addCash.activity.AddCashActivity
import os.com.ui.dashboard.profile.activity.ChangePasswordActivity
import os.com.ui.dashboard.profile.activity.FullProfileActivity
import os.com.ui.dashboard.profile.activity.MyAccountActivity
import os.com.ui.dashboard.profile.activity.RankingActivity
import os.com.ui.dashboard.profile.adapter.TeamImageAdapter
import os.com.ui.dashboard.profile.apiResponse.AvtarListResponse
import os.com.ui.dashboard.profile.apiResponse.ProfileResponse
import os.com.ui.invite.activity.InviteFriendsActivity
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils

/**
 * Created by heenas on 3/5/2018.
 */
class ProfileFragment : BaseFragment(), View.OnClickListener, AppBarLayout.OnOffsetChangedListener,
    IAdapterClick.IItemClick {


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
                R.id.txt_ApplyCode -> {
                    if (!et_email.text.toString().isEmpty())
                        ApplyOfferCodeApi()
                }
                R.id.imvEditImage -> {
                    showDialog();
                }
                R.id.txtLogout -> {
                    (activity as BaseActivity).showLogoutDialog()
//                val intent = Intent(activity, LoginActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//                activity!!.finish()
                }
                R.id.txt_AddCash -> {
                    var currentBalance = "0.0"
                    if (mData != null)
                        currentBalance =
                                (mData!!.getCash_bonus_amount().toFloat() + mData!!.total_cash_amount.toFloat() + mData!!.total_winning_amount.toFloat()).toString()
                    startActivity(
                        Intent(activity, AddCashActivity::class.java).putExtra(
                            IntentConstant.currentBalance,
                            currentBalance
                        ).putExtra(IntentConstant.AddType, IntentConstant.ADD)
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun ApplyOfferCodeApi() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                if (activity != null)
                    AppDelegate.showProgressDialog(activity!!)
                try {
                    val map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.coupon_code] = et_email.text.toString()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .apply_coupon_code(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(activity)
                    if (response.response!!.status) {
                        var currentBalance = "0.0"
                        if (mData != null)
                            currentBalance =
                                    (mData!!.getCash_bonus_amount().toFloat() + mData!!.total_cash_amount.toFloat() + mData!!.total_winning_amount.toFloat()).toString()
                        startActivity(
                            Intent(activity, AddCashActivity::class.java).putExtra(
                                Tags.DATA,
                                response.response!!.data
                            ).putExtra("currentBalance", currentBalance).putExtra("AddType", IntentConstant.OFFER)
                        )
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

    var dialogue: Dialog? = null

    private fun showDialog() {
        try {
            dialogue = Dialog(context)
            dialogue!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogue!!.setContentView(R.layout.dailog_team_image)
            dialogue!!.window.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialogue!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogue!!.setCancelable(false)
            dialogue!!.setCanceledOnTouchOutside(false)
            dialogue!!.setTitle(null)
            if (NetworkUtils.isConnected()) {
                getTeamImageData()
            } else
                Toast.makeText(context!!, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()


            dialogue!!.img_Close.setOnClickListener {
                dialogue!!.dismiss()
            }
            dialogue!!.btnSave.setOnClickListener {
                //            joinContest(match_id, series_id, contest_id, team_id, onClickDialogue)
                if (selectedImageId != null)
                    getUpdateTeamImage(selectedImageId!!)
                dialogue!!.dismiss()
            }
            if (dialogue!!.isShowing)
                dialogue!!.dismiss()
            dialogue!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true

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
            txt_AddCash.setOnClickListener(this)
            imvEditImage.setOnClickListener(this)
            txt_ApplyCode.setOnClickListener(this)
            setData()
            if (pref!!.isLogin) {
                if (!pref!!.userdata!!.fb_id.isEmpty() || !pref!!.userdata!!.google_id.isEmpty()) {
                    txt_changePassword.visibility = GONE
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

    private var mData: ProfileResponse.ResponseBean.DataBean? = null

    private fun getProfileData() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                if (activity != null)
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

    var avtarList = ArrayList<AvtarListResponse.ResponseBean.DataBean>()

    private fun getTeamImageData() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                if (activity != null)
                    AppDelegate.showProgressDialog(activity!!)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .avetar_list(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(activity)
                    if (response.response!!.status) {
                        if (response.response.data != null) {
                            avtarList = response.response.data as ArrayList<AvtarListResponse.ResponseBean.DataBean>
                            if (dialogue != null)
                                setDialogAdapter()
                        }
//                        AppDelegate.showToast(context, response.response!!.message)
                    } else {
                        if (response.response!!.message != null)
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

    private fun getUpdateTeamImage(selectedImageId: Int) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                if (activity != null)
                    AppDelegate.showProgressDialog(activity!!)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.img_id] = "" + selectedImageId
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .update_user_image(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(activity)
                    if (response.response!!.status) {
                        if (NetworkUtils.isConnected()) {
                            getProfileData()
                        } else
                            Toast.makeText(
                                context!!,
                                getString(R.string.error_network_connection),
                                Toast.LENGTH_LONG
                            ).show()
//                        AppDelegate.showToast(context, response.response!!.message)
                    } else {
                        if (response.response!!.message != null)
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

    var avtarImageAdapter: TeamImageAdapter? = null

    private fun setDialogAdapter() {
        try {
            if (dialogue != null) {
                if (avtarList.size > 0) {
                    var llm = GridLayoutManager(context, 2)
                    dialogue!!.rv_image!!.layoutManager = llm
                    avtarImageAdapter = TeamImageAdapter(context!!, -1, avtarList, this)
                    dialogue!!.rv_image!!.adapter = avtarImageAdapter
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var selectedImageId: Int? = null

    override fun onClick(position: Int) {
        selectedImageId = avtarList[position].id
        avtarImageAdapter!!.selectImageId(avtarList[position].id)
        avtarImageAdapter!!.notifyDataSetChanged()
    }

    private fun initData(data: ProfileResponse.ResponseBean.DataBean?) {
        try {
            mData = data
            if (mData != null) {
                if (mData!!.team_name != null && mData!!.team_name != "") {
                    txt_name.setText(mData!!.team_name)
                    main_textview_title.setText(mData!!.team_name)
                }
                if (mData!!.contest_level != null) {
                    txt_StartValue.setText("" + mData!!.contest_level)
                    txt_EndValue.setText("" + (mData!!.contest_level + 1))
                    main_textview_subtitle.setText("" + mData!!.contest_level)
                    tvUnlockLevel.setText("Unlock these rewards at level " + (mData!!.contest_level + 1))

                }
                if (mData!!.image != null && mData!!.image != "")
                    ImageLoader.getInstance().displayImage(
                        mData!!.image,
                        imvTeamImage,
                        FantasyApplication.getInstance().options
                    )
                if (mData!!.total_cash_amount != null && mData!!.total_cash_amount != "")
                    txtCashDeposited.setText("\u20B9 " + mData!!.total_cash_amount)
                if (mData!!.total_winning_amount != null && mData!!.total_winning_amount != "")
                    txtCashWinnings.setText("\u20B9 " + mData!!.total_winning_amount)
                if (mData!!.cash_bonus_amount != null && mData!!.cash_bonus_amount != "")
                    txtCashBonus.setText("\u20B9 " + mData!!.cash_bonus_amount)

                if (mData!!.contest_finished != null) {
                    txtContest.setText("" + mData!!.contest_finished)
                    var contest = mData!!.contest_finished % 20
                    tvRemianContest.setText("" + (20 - contest))
                }
                if (mData!!.total_match != null)
                    txtMatch.setText("" + mData!!.total_match)
                if (mData!!.total_series != null)
                    txtSeries.setText("" + mData!!.total_series)
                if (mData!!.series_wins != null && mData!!.series_wins != "")
                    txtWins.setText("" + mData!!.series_wins)

                if (mData!!.referal_bonus != null && mData!!.referal_bonus != "")
                    btn_InviteFriends.setText(context!!.resources.getString(R.string.invite_friends) + " & get ₹ " + mData!!.series_wins)
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