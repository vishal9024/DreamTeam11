package os.com.ui.dashboard.myContest.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.AppBase.BaseFragment
import os.com.BuildConfig
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.interfaces.OnClickRecyclerView
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.dashboard.myContest.adapter.MyContestCompletedAdapter
import os.com.ui.dashboard.myContest.adapter.MyContestFixturesAdapter
import os.com.ui.dashboard.myContest.adapter.MyContestLiveAdapter
import os.com.utils.AppDelegate
import os.com.utils.AppDelegate.getTimeStampFromDateServer
import os.com.utils.networkUtils.NetworkUtils
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.set


/**
 * Created by heenas on 3/5/2018.
 */
class MyContestFragment : BaseFragment(), View.OnClickListener,  OnClickRecyclerView {
    override fun onClickItem(tag: String, position: Int) {
        if (!isAdded)
            return
        fixturesMatchList.removeAt(position)
        fixturesAdapter!!.notifyDataSetChanged()
    }
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.txt_Fixtures -> matchSelector(FIXTURES)
            R.id.txt_Live -> matchSelector(LIVE)
            R.id.txt_Results -> matchSelector(RESULTS)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initTabLayout(tabLayout: TabLayout) {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fixtures)), true);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.live)), false);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.results)), false);
        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when {
                    tab.text == getString(R.string.fixtures) -> matchSelector(FIXTURES)
                    tab.text == getString(R.string.live) -> matchSelector(LIVE)
                    else -> matchSelector(RESULTS)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun initViews() {
        viewPager_Banner.visibility = GONE
        matchSelector(FIXTURES)
//        setHomeBannerAdapter()
        txt_Fixtures.setOnClickListener(this)
        txt_Live.setOnClickListener(this)
        txt_Results.setOnClickListener(this)
        if (pref!!.isLogin)
            if (BuildConfig.APPLICATION_ID.equals("os.cashfantasy")) {
//                tabLayoutfsl.visibility = GONE
                ll_matchSelector.visibility = GONE
                tabLayout.visibility = View.VISIBLE
                initTabLayout(tabLayout)
            } else if (BuildConfig.APPLICATION_ID.equals("os.fsl")) {
//                tabLayoutfsl.visibility = VISIBLE
                ll_matchSelector.visibility = GONE
                tabLayout.visibility = View.VISIBLE
                initTabLayout(tabLayout)
            } else {
//                tabLayoutfsl.visibility = GONE
                ll_matchSelector.visibility = View.VISIBLE
                tabLayout.visibility = GONE
            }

        setFixturesAdapter()
        setCompletedAdapter()
        setLiveAdapter()
        if (NetworkUtils.isConnected()) {
            callGetMatchListApi(View.VISIBLE)
        } else
            Toast.makeText(activity, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
        swipeToRefresh.setOnRefreshListener {
            if (AppDelegate.isNetworkAvailable(activity!!))
                refreshItems()
        }
    }

    private fun refreshItems() {
        GlobalScope.launch {
            delay(2000)
            try {
                if (isAdded)
                    if (AppDelegate.isNetworkAvailable(activity!!))
                        callGetMatchListApi(View.GONE)
            } catch (e: Exception) {
                swipeToRefresh.isRefreshing = false
            }
        }
    }

    private var LIVE = 1
    private var FIXTURES = 2
    private var RESULTS = 3

    fun matchSelector(value: Int) {
        txt_Fixtures.isSelected = false
        txt_Live.isSelected = false
        txt_Results.isSelected = false
        view1.visibility = View.VISIBLE
        view2.visibility = View.VISIBLE
        recyclerView_fixMatch.visibility = GONE
        recyclerView_liveMatch.visibility = GONE
        recyclerView_CompleteMatch.visibility = GONE
        when (value) {
            LIVE -> {
                txt_title.visibility = GONE
                txt_Live.isSelected = true
                view1.visibility = View.GONE
                view2.visibility = View.GONE
                recyclerView_liveMatch.visibility = View.VISIBLE
                setLiveAdapter()
            }
            FIXTURES -> {
                txt_title.visibility = GONE
                txt_title.setText(R.string.select_a_match)
                txt_Fixtures.isSelected = true
                view1.visibility = View.GONE
                recyclerView_fixMatch.visibility = View.VISIBLE
                setFixturesAdapter()
            }
            RESULTS -> {
                txt_title.visibility = GONE
                txt_title.setText(R.string.results)
                txt_Results.isSelected = true
                view2.visibility = View.GONE
                recyclerView_CompleteMatch.visibility = View.VISIBLE
                setCompletedAdapter()
            }
        }
    }


    var fixturesMatchList: MutableList<Match> = ArrayList()
    var completedMatchList: MutableList<Match> = ArrayList()
    var liveMatchList: MutableList<Match> = ArrayList()
    var fixturesAdapter: MyContestFixturesAdapter? = null
    var liveAdapter: MyContestLiveAdapter? = null
    var completedAdapter: MyContestCompletedAdapter? = null
    private fun callGetMatchListApi(visibility: Int) {
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        else
            loginRequest[Tags.user_id]= ""
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        GlobalScope.launch(Dispatchers.Main) {
            if (!isAdded)
                return@launch
            if (visibility == View.VISIBLE)
                AppDelegate.showProgressDialog(activity!!)
                        try {
                            val request = ApiClient.client
                                .getRetrofitService()
                                .joined_contest_matches(loginRequest)
                            val response = request.await()
                            AppDelegate.LogT("Response=>" + response);
                            AppDelegate.hideProgressDialog(activity)
                            swipeToRefresh.isRefreshing = false
                            if (response.response!!.status) {
//                    AppDelegate.showToast(activity, response.response!!.message)
                                FantasyApplication.getInstance().server_time=getTimeStampFromDateServer(response.response!!.data!!.server_time!!)!!
                                fixturesMatchList = response.response!!.data!!.upcoming_match as MutableList<Match>
                                liveMatchList = response.response!!.data!!.live_match as MutableList<Match>
                                completedMatchList = response.response!!.data!!.completed_match as MutableList<Match>
                                setFixturesAdapter()
                                setCompletedAdapter()
                                setLiveAdapter()
                                recyclerView_fixMatch!!.adapter!!.notifyDataSetChanged()
                                recyclerView_liveMatch!!.adapter!!.notifyDataSetChanged()
                                recyclerView_CompleteMatch!!.adapter!!.notifyDataSetChanged()
                            } else {
                                (activity as BaseActivity).logoutIfDeactivate(response.response!!.message)
//                    AppDelegate.showToast(activity, response.response!!.message)
                            }
                        } catch (exception: Exception) {
                            if (!isAdded)
                                return@launch
                            swipeToRefresh.isRefreshing = false
                            AppDelegate.hideProgressDialog(activity)
                        }
        }
    }

    @SuppressLint("WrongConstant")
    private fun setFixturesAdapter() {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView_fixMatch!!.layoutManager = llm
        recyclerView_fixMatch!!.setHasFixedSize(true)
        fixturesAdapter = MyContestFixturesAdapter(context!!, fixturesMatchList,this)
        recyclerView_fixMatch!!.adapter = fixturesAdapter
    }

    @SuppressLint("WrongConstant")
    private fun setLiveAdapter() {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView_liveMatch!!.layoutManager = llm
        recyclerView_liveMatch!!.setHasFixedSize(true)
        liveAdapter = MyContestLiveAdapter(context!!, liveMatchList)
        recyclerView_liveMatch!!.adapter = liveAdapter
    }

    @SuppressLint("WrongConstant")
    private fun setCompletedAdapter() {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView_CompleteMatch!!.layoutManager = llm
        recyclerView_CompleteMatch!!.setHasFixedSize(true)
        completedAdapter = MyContestCompletedAdapter(context!!, completedMatchList)
        recyclerView_CompleteMatch!!.adapter = completedAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        fixturesAdapter!!.stopUpdateTimer()
    }
}