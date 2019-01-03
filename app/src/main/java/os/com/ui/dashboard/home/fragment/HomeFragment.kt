package os.com.ui.dashboard.home.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseFragment
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.home.adapter.MatchCompletedAdapter
import os.com.ui.dashboard.home.adapter.MatchFixturesAdapter
import os.com.ui.dashboard.home.adapter.MatchLiveAdapter
import os.com.ui.dashboard.home.adapter.SlidingImageAdapterHomeBanner
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate


/**
 * Created by heenas on 3/5/2018.
 */
class HomeFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.txt_Fixtures -> matchSelector(FIXTURES)
            R.id.txt_Live -> matchSelector(LIVE)
            R.id.txt_Results -> matchSelector(RESULTS)
        }

    }

    var fixturesMatchList: MutableList<Match> = ArrayList()
    var completedMatchList: MutableList<Match> = ArrayList()
    var liveMatchList: MutableList<Match> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        if (!pref!!.isLogin) {
            toolbar.visibility = View.GONE
        }
        matchSelector(FIXTURES)
        callGetMatchListApi()
        Handler().postDelayed(Runnable {
            setHomeBannerAdapter()
        }, 100)
        txt_Fixtures.setOnClickListener(this)
        txt_Live.setOnClickListener(this)
        txt_Results.setOnClickListener(this)

    }

    private fun setHomeBannerAdapter() {
        if (!isAdded)
            return
        viewPager_Banner.adapter = SlidingImageAdapterHomeBanner(activity!!)
        viewPager_Banner.setClipToPadding(false);
        viewPager_Banner.setPadding(50, 0, 50, 0)
        viewPager_Banner.setPageMargin(10)
        viewPager_Banner.startAutoScroll()
        viewPager_Banner.isCycle = true
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
        when (value) {
            LIVE -> {
                txt_title.visibility = GONE
                txt_Live.isSelected = true
                view1.visibility = View.GONE
                view2.visibility = View.GONE
                setLiveAdapter()
            }
            FIXTURES -> {
                txt_title.visibility = VISIBLE
                txt_title.setText(R.string.select_a_match)
                txt_Fixtures.isSelected = true
                view1.visibility = View.GONE
                setFixturesAdapter()
            }
            RESULTS -> {
                txt_title.visibility = GONE
                txt_title.setText(R.string.results)
                txt_Results.isSelected = true
                view2.visibility = View.GONE
                setCompletedAdapter()
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun setFixturesAdapter() {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView_Match!!.layoutManager = llm
        recyclerView_Match!!.setHasFixedSize(false)
        recyclerView_Match!!.adapter = MatchFixturesAdapter(context!!, fixturesMatchList)
    }

    @SuppressLint("WrongConstant")
    private fun setLiveAdapter() {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView_Match!!.layoutManager = llm
        recyclerView_Match!!.setHasFixedSize(false)
        recyclerView_Match!!.adapter = MatchLiveAdapter(context!!, liveMatchList)
    }

    @SuppressLint("WrongConstant")
    private fun setCompletedAdapter() {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView_Match!!.layoutManager = llm
        recyclerView_Match!!.setHasFixedSize(false)
        recyclerView_Match!!.adapter = MatchCompletedAdapter(context!!, completedMatchList)
    }

    private fun callGetMatchListApi() {
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(activity!!)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .getMatchList(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(activity)
                if (response.response!!.status) {
//                    AppDelegate.showToast(activity, response.response!!.message)
                    fixturesMatchList = response.response!!.data!!.upcoming_match as MutableList<Match>
                    liveMatchList = response.response!!.data!!.live_match as MutableList<Match>
                    completedMatchList = response.response!!.data!!.completed_match as MutableList<Match>
                    setFixturesAdapter()
                } else {
//                    AppDelegate.showToast(activity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(activity)
            }
        }
    }
}