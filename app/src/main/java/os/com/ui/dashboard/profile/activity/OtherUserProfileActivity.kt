package os.com.ui.dashboard.profile.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.content_other_user_profile.*
import kotlinx.android.synthetic.main.toolbar_other_user_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.profile.adapter.PerformanceAdapter
import os.com.ui.dashboard.profile.apiResponse.OtherUserProfileResponse
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class OtherUserProfileActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
          try{
              when (view!!.id) {
                  R.id.cv_transactions -> {
                      startActivity(Intent(this, RecentTansActivity::class.java))
                  }
                  R.id.cv_payments -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
                  }
              }
          } catch (e: Exception) {
              e.printStackTrace()
          }
    }
    private var friend_user_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user_profile)
        initViews()
    }

    private fun initViews() {
        try{
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
//            tvtitle.setText(R.string.my_account)
        setMenu(false,false,false,false,false)

            if (intent.hasExtra("data"))
                friend_user_id = intent.getStringExtra("data")
            if (NetworkUtils.isConnected()) {
                if (friend_user_id != null && !friend_user_id.equals(""))
                    getUserProfileData(friend_user_id)
            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getUserProfileData(friend_user_id: String) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@OtherUserProfileActivity)
                try {
                    var map = HashMap<String, String>()
                    if (pref!!.isLogin)
                        map[Tags.user_id] = pref!!.userdata!!.user_id
                    else
                        map[Tags.user_id]= ""
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.friend_user_id] = friend_user_id
                    val request = ApiClient.client
                        .getRetrofitService()
                        .team_profile_comparision(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@OtherUserProfileActivity)
                    if (response.response!!.status) {
                        if (response.response!!.data != null)
                            setData(response.response!!.data)
//                        mSeriesList = response.response.data
//                        initSeries(mSeriesList!!)
//                        finish()
                    } else {
                        logoutIfDeactivate(response.response!!.message)
                        if (response.response!!.message != null)
                            AppDelegate.showToast(this@OtherUserProfileActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@OtherUserProfileActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setData(data: OtherUserProfileResponse.ResponseBean.DataBean) {
        try{
            var team_name=""
            if (data.team_name != null) {
                tvtitle.text = data.team_name
                team_name=data.team_name
            }

            if (data.contest_level != null)
                tvsubtitle.text = "Level "+data.contest_level

            if (data.image != null && !data.image.equals(""))
                ImageLoader.getInstance().displayImage(
                    data.image,
                    imvUserProfile,
                    FantasyApplication.getInstance().options
                )
            if (data.contest_finished != null)
                txtContest.text = ""+data.contest_finished
            if (data.total_match != null)
                txtMatch.text = "" + data.total_match
            if (data.total_series != null)
                txtSeries.text = "" + data.total_series
            if (data.series_wins != null)
                txtWins.text = "" + data.series_wins

            if (data.recent_performance!=null && data.recent_performance.size>0) {
                ll_RecentPerformance.visibility=View.VISIBLE
                setAdapter(data.recent_performance, team_name)
            }else{
                ll_RecentPerformance.visibility=View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(
        recent_performance: MutableList<OtherUserProfileResponse.ResponseBean.DataBean.RecentPerformanceBean>,
        team_name: String
    ) {
        try{
            val llm = LinearLayoutManager(this)
            llm.orientation = LinearLayoutManager.VERTICAL
            rv_List!!.layoutManager = llm
            rv_List!!.adapter = PerformanceAdapter(this,recent_performance,team_name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}