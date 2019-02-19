package os.com.ui.createTeam.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_playerdetail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.createTeam.adapter.PlayerDetailFantasyStatusAdapter
import os.com.ui.createTeam.apiResponse.PlayerdetailResponse
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*


class PlayerDetailActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
        }
    }

    private var seriesId = ""
    private var playerId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)
        initViews()

        if (intent.hasExtra("series_id"))
            seriesId = intent.getStringExtra("series_id")
        if (intent.hasExtra("player_id"))
            playerId = intent.getStringExtra("player_id")
        if (NetworkUtils.isConnected()) {
            if (seriesId != null && !seriesId.equals("") && playerId != null && !playerId.equals(""))
                getPlayerDetailData(seriesId, playerId)
        } else
            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
    }


    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.player_detail)
        setMenu(false, false, false, false,false)
    }

    private fun getPlayerDetailData(series_id: String, playerId: String) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@PlayerDetailActivity)
                try {
                    var map = HashMap<String, String>()
                    if (pref!!.isLogin)
                        map[Tags.user_id] = pref!!.userdata!!.user_id
                    else
                        map[Tags.user_id]= ""
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.series_id] = series_id
                    map[Tags.player_id] = playerId
                    val request = ApiClient.client
                        .getRetrofitService()
                        .seriesPlayerDetail(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@PlayerDetailActivity)
                    if (response.response!!.status) {
                        if (response.response!!.data != null)
                            setData(response.response!!.data)
//                        mSeriesList = response.response.data
//                        initSeries(mSeriesList!!)
//                        finish()
                    } else {
                        logoutIfDeactivate(response.response!!.message)
                        if (response.response!!.message != null)
                            AppDelegate.showToast(this@PlayerDetailActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@PlayerDetailActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setData(data: PlayerdetailResponse.ResponseBean.DataBean) {
        try {
            ll_level.visibility=View.VISIBLE
            ll_about.visibility=View.VISIBLE
            if (data.player_image != null && data.player_image != "")
                ImageLoader.getInstance().displayImage(
                    data.player_image,
                    imvPlayer,
                    FantasyApplication.getInstance().options
                )
            if (data.player_name != null)
                txt_PlayerName.setText(data.player_name)
            if (data.player_credit!= null)
                txtCredits.setText(data.player_credit)
            if (data.player_total_points!= null)
                txt_totalPoints.setText(data.player_total_points)
            if (data.bats_type!= null)
                txtBatsType.setText(data.bats_type)
            if (data.bowls_type != null)
                txtBowlsType.setText(data.bowls_type)
            if (data.nationality != null)
                txtNationality.setText(data.nationality)
            if (data.birthday!= null)
                txtBirthday.setText(data.birthday)

            if (data.match_detail!= null && data.match_detail.size>0 ){
                txt_FantasyStatus.visibility=View.VISIBLE
                ll_AllContest.visibility=View.VISIBLE
                setAdapter(data.match_detail)
            }
//            ll_match.visibility = View.VISIBLE
//            ll_Series.visibility = View.VISIBLE
//            ll_AllContest.visibility = View.VISIBLE
//            if (data.team_name != null)
//                txt_TeamName.text = data.team_name
//
//            if (data.image != null && !data.image.equals(""))
//                ImageLoader.getInstance().displayImage(
//                    data.image,
//                    cimg_player,
//                    FantasyApplication.getInstance().options
//                )
//            if (data.series_name != null)
//                txtSeriesName.text = data.series_name
//            if (data.totalRank != null)
//                txt_Rank.text = "#" + data.totalRank
//            if (data.total_points != null)
//                txtPoints.text = "" + data.total_points
//            if (data.point_detail != null && data.point_detail.size > 0) {
//                txt_team.text = "match(" + data.point_detail.size + ")"
//                setAdapter(data.point_detail)
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(match_detail: MutableList<PlayerdetailResponse.ResponseBean.DataBean.MatchDetailBean>) {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_FantasyStatus!!.layoutManager = llm
        rv_FantasyStatus!!.adapter = PlayerDetailFantasyStatusAdapter(this,match_detail)
    }


}