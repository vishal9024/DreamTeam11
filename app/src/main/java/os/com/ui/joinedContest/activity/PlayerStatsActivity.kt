package os.com.ui.joinedContest.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_completed_player_stats.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.adapter.PlayerStatsAdapter
import os.com.ui.joinedContest.apiResponse.getSeriesPlayerListResponse.Data
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils


class PlayerStatsActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_player_stats)
        initViews()
    }

    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.player_points)
        setMenu(false, false, false, false)
        if (intent != null) {
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
        }
        if (NetworkUtils.isConnected()) {
            callPlayerStatsApi()
        } else
            AppDelegate.showToast(this, getString(R.string.error_network_connection))
    }
    var playerPoints:ArrayList<Data> = ArrayList()
    private fun callPlayerStatsApi() {
        try {
            val map = HashMap<String, String>()
            if (pref!!.isLogin)
                map[Tags.user_id] = pref!!.userdata!!.user_id
            map[Tags.language] = FantasyApplication.getInstance().getLanguage()
            map[Tags.match_id] = match!!.match_id/*"13071965317"*/
            map[Tags.series_id] = match!!.series_id/*"13071965317"*/
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@PlayerStatsActivity)
                try {
                    val request = ApiClient.client
                        .getRetrofitService()
                        .getSeriesPlayerList(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@PlayerStatsActivity)
                    if (response.response!!.status) {
                        playerPoints=response.response!!.data!!
                            setAdapter()
                    } else {
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@PlayerStatsActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Players!!.layoutManager = llm
        rv_Players!!.adapter = PlayerStatsAdapter(this, playerPoints,matchType)
    }


}