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
            R.id.txt_Players -> {
                sortBySelector(Players)
            }
            R.id.txt_SelectedBy -> {
                sortBySelector(SelectedBy)
            }
            R.id.txt_Points -> {
                sortBySelector(Points)
            }
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
        setMenu(false, false, false, false,false)
        if (intent != null) {
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
        }
        if (NetworkUtils.isConnected()) {
            callPlayerStatsApi()
        } else
            AppDelegate.showToast(this, getString(R.string.error_network_connection))
        txt_Players.setOnClickListener(this)
        txt_SelectedBy.setOnClickListener(this)
        txt_Points.setOnClickListener(this)
        txt_Players.isEnabled = false
        txt_SelectedBy.isEnabled = false
        txt_Points.isEnabled = false
    }

    var playerPoints: ArrayList<Data> = ArrayList()
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
                        txt_Players.isEnabled = true
                        txt_SelectedBy.isEnabled = true
                        txt_Points.isEnabled = true
                        playerPoints = response.response!!.data!!
                        setAdapter()
                        sortBySelector(Players)
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
        rv_Players!!.adapter = PlayerStatsAdapter(this, playerPoints, matchType)
    }

    var isAsc = true
    fun selectorPlayers(p: Data): String = p.player_name
    fun selectorSelectedBy(p: Data): Int = p.selection_percent.toInt()
    fun selectorPoints(p: Data): Double = p.points.toDouble()
    private var Players = 1
    private var SelectedBy = 2
    private var Points = 3
    fun sortBySelector(value: Int) {
        txt_Players.isSelected = false
        txt_SelectedBy.isSelected = false
        txt_Points.isSelected = false

        txt_Players.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        txt_SelectedBy.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        txt_Points.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        when (value) {
            Players -> {
                txt_Players.isSelected = true
                if (isAsc) {
                    isAsc = false
                    playerPoints!!.sortBy { selectorPlayers(it) }
                    txt_Players.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowup, 0);
                } else {
                    isAsc = true
                    playerPoints!!.sortByDescending { selectorPlayers(it) }
                    txt_Players.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowdown, 0);
                }

                rv_Players!!.adapter!!.notifyDataSetChanged()
            }
            SelectedBy -> {
                txt_SelectedBy.isSelected = true
                if (isAsc) {
                    isAsc = false
                    playerPoints!!.sortByDescending { selectorSelectedBy(it) }
                    txt_SelectedBy.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowdown, 0);
                } else {
                    isAsc = true
                    playerPoints!!.sortBy { selectorSelectedBy(it) }
                    txt_SelectedBy.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowup, 0);
                }
                rv_Players!!.adapter!!.notifyDataSetChanged()
            }
            Points -> {
                txt_Points.isSelected = true
                if (isAsc) {
                    isAsc = false
                    playerPoints!!.sortByDescending { selectorPoints(it) }
                    txt_Points.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowdown, 0);
                } else {
                    isAsc = true
                    playerPoints!!.sortBy { selectorPoints(it) }
                    txt_Points.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrowup, 0);
                }
                rv_Players!!.adapter!!.notifyDataSetChanged()
            }
        }
    }
}