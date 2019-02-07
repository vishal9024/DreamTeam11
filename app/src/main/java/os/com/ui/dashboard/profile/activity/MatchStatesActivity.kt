package os.com.ui.dashboard.profile.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_match_states.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.profile.adapter.MatchStatesAdapter
import os.com.ui.dashboard.profile.apiResponse.SeriesListResponse
import os.com.ui.dashboard.profile.apiResponse.TeamStatesResponse
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class MatchStatesActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try {
        when (view!!.id) {
            R.id.cimg_player -> {
                if (!userId.equals("")) {
                    var intent = Intent(this, OtherUserProfileActivity::class.java)
                    intent.putExtra("data",  userId)
                    startActivity(intent)
                }
            }
        }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var seriesId = ""
    private var userId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_states)
        initViews()
    }

    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.leaderboard)
            setMenu(false, false, false, false, false)
            cimg_player.setOnClickListener(this)
            if (intent.hasExtra("data"))
                seriesId = intent.getStringExtra("data")
            if (intent.hasExtra("user_id"))
                userId = intent.getStringExtra("user_id")
            if (NetworkUtils.isConnected()) {
                if (seriesId != null && !seriesId.equals("") && userId != null && !userId.equals(""))
                    getMatchStatesData(seriesId,userId)
            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
//        btn_CreateTeam.setOnClickListener(this)
//        txt_Signup.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var mSeriesList: MutableList<SeriesListResponse.ResponseBean.DataBean>? = null

    private fun getMatchStatesData(series_id: String, userId: String) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@MatchStatesActivity)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = userId
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.series_id] = series_id
                    val request = ApiClient.client
                        .getRetrofitService()
                        .team_states(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@MatchStatesActivity)
                    if (response.response!!.status) {
                        if (response.response!!.data != null)
                            setData(response.response!!.data)
//                        mSeriesList = response.response.data
//                        initSeries(mSeriesList!!)
//                        finish()
                    } else {
                        if (response.response!!.message != null)
                            AppDelegate.showToast(this@MatchStatesActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@MatchStatesActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setData(data: TeamStatesResponse.ResponseBean.DataBean) {
          try{

              ll_match.visibility=View.VISIBLE
              ll_Series.visibility=View.VISIBLE
              ll_AllContest.visibility=View.VISIBLE
              if (data.team_name != null)
                  txt_TeamName.text = data.team_name

              if (data.image != null && !data.image.equals(""))
                  ImageLoader.getInstance().displayImage(
                      data.image,
                      cimg_player,
                      FantasyApplication.getInstance().options
                  )
              if (data.series_name != null)
                  txtSeriesName.text = data.series_name
              if (data.totalRank != null)
                  txt_Rank.text = "#" + data.totalRank
              if (data.total_points != null)
                  txtPoints.text = "" + data.total_points
              if (data.point_detail!=null && data.point_detail.size>0) {
                  txt_team.text="match("+data.point_detail.size+")"
                  setAdapter(data.point_detail)
              }
          } catch (e: Exception) {
              e.printStackTrace()
          }

    }


    @SuppressLint("WrongConstant")
    private fun setAdapter(data: MutableList<TeamStatesResponse.ResponseBean.DataBean.PointDetailBean>) {
        try {
            val llm = LinearLayoutManager(this)
            llm.orientation = LinearLayoutManager.VERTICAL
            rv_Contest!!.layoutManager = llm
            rv_Contest!!.adapter = MatchStatesAdapter(this, data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}