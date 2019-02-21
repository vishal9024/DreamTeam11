package os.com.ui.dashboard.profile.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.profile.adapter.CustomSpinnerAdapter
import os.com.ui.dashboard.profile.adapter.RankingAdapter
import os.com.ui.dashboard.profile.apiResponse.SeriesListResponse
import os.com.ui.dashboard.profile.apiResponse.SeriesRankingListResponse
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class RankingActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try {
//        when (view!!.id) {
//            R.id.btn_CreateTeam -> {
//            startActivity(Intent(this, ChooseTeamActivity::class.java))
//        }
//            R.id.txt_Signup -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
//            }
//        }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val seriesList = ArrayList<String>()
    private var series = ""

    private var mSeriesId: String = ""
    private var mSeriesIdBack: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
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
            if (intent.hasExtra("data"))
                mSeriesIdBack = intent.getStringExtra("data")
            if (NetworkUtils.isConnected()) {
                if (mSeriesIdBack == "")
                    getSeriesList()
                else {
                    getSeriesList()
                    getSeriesRankingList(mSeriesIdBack)
                }

            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
//        btn_CreateTeam.setOnClickListener(this)
//        txt_Signup.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var mSeriesList: MutableList<SeriesListResponse.ResponseBean.DataBean>? = null

    private fun getSeriesList() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@RankingActivity)
                try {
                    var map = HashMap<String, String>()
                    if (pref!!.isLogin)
                        map[Tags.user_id] = pref!!.userdata!!.user_id
                    else
                        map[Tags.user_id]= ""
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .seriesList(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@RankingActivity)
                    if (response.response!!.status) {
                        mSeriesList = response.response.data
                        initSeries(mSeriesList!!)
//                        finish()
                    } else {
                        logoutIfDeactivate(response.response!!.message)
                        if (response.response!!.message != null)
                            AppDelegate.showToast(this@RankingActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@RankingActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var rankingList: MutableList<SeriesRankingListResponse.ResponseBean.DataBean>? = null

    private fun getSeriesRankingList(mSeriesId: String) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@RankingActivity)
                try {
                    var map = HashMap<String, String>()
                    if (pref!!.isLogin)
                        map[Tags.user_id] = pref!!.userdata!!.user_id
                    else
                        map[Tags.user_id]= ""
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.series_id] = mSeriesId
                    val request = ApiClient.client
                        .getRetrofitService()
                        .series_ranking(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@RankingActivity)
                    if (response.response!!.status) {
                        rankingList = response.response.data
                        if (rankingList != null)
                            setAdapter(rankingList!!)
//                        finish()
                    } else {
                       logoutIfDeactivate(response.response!!.message)
                        if (response.response!!.message != null)
                            AppDelegate.showToast(this@RankingActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@RankingActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initSeries(data: MutableList<SeriesListResponse.ResponseBean.DataBean>) {
        try {
            for (i in 0..data.size - 1) {
                seriesList.add(data[i].series_name)
            }
            val spinnerAdapter = CustomSpinnerAdapter(this, seriesList)
            spn_match.setAdapter(spinnerAdapter)
            spn_match.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val layout = parent.getChildAt(0) as RelativeLayout
                    if (layout != null) {
                        val selectedText = layout.findViewById(R.id.txtItem) as TextView
                        selectedText?.setTextColor(Color.BLACK)
                        series = parent.getItemAtPosition(position).toString()
                          try{
                              if (mSeriesIdBack == "") {
                                  if (mSeriesList != null && mSeriesList!!.size > 0)
                                      for (item in mSeriesList!!) {
                                          if (item.series_name.equals(series)) {
                                              mSeriesId = item.series_id
                                              if (mSeriesId != null)
                                                  getSeriesRankingList(mSeriesId!!)
                                              break
                                          }
                                      }
                              } else {
                                  for (item in mSeriesList!!) {
                                      if (item.series_id.equals(mSeriesIdBack)) {
                                          var pos = seriesList.indexOf(item.series_name)
                                          spn_match.setSelection(pos)
                                          mSeriesIdBack = ""
                                          break
                                      }
                                  }
                              }
                          } catch (e: Exception) {
                              e.printStackTrace()
                          }
                    }
                }
                    override fun onNothingSelected(parent: AdapterView<*>) {}
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        @SuppressLint("WrongConstant")
        private fun setAdapter(rankingList: MutableList<SeriesRankingListResponse.ResponseBean.DataBean>) {
            try {
                val llm = LinearLayoutManager(this)
                llm.orientation = LinearLayoutManager.VERTICAL
                rv_Contest!!.layoutManager = llm
                rv_Contest!!.adapter = RankingAdapter(this, rankingList, pref!!.userdata!!.user_id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }