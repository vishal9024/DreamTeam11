package os.com.ui.dashboard.home.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseFragment
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.home.adapter.*
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by heenas on 3/5/2018.
 */
class HomeFragment : BaseFragment(), View.OnClickListener, AppBarLayout.OnOffsetChangedListener {
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (verticalOffset == 0) {
            if (txt_Fixtures.isSelected)
                if (!pref!!.isLogin) {
                    ll_matchSelector.visibility = GONE
                    txt_title.visibility = GONE
                    txt_toolbartitle.visibility = VISIBLE
                } else
                    txt_title.visibility = VISIBLE
        } else {
            txt_title.visibility = GONE
        }
    }

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
            txt_toolbartitle.visibility = View.VISIBLE
            ll_matchSelector.visibility = View.GONE
            txt_title.visibility = View.GONE
        } else {
            txt_toolbartitle.visibility = View.GONE
            cl_main.visibility = View.VISIBLE
            txt_title.visibility = View.VISIBLE
        }
        app_bar.addOnOffsetChangedListener(this);
        matchSelector(FIXTURES)
        setFixturesAdapter()
        setCompletedAdapter()
        setLiveAdapter()
        if (NetworkUtils.isConnected()) {
            callGetMatchListApi()
        } else
            Toast.makeText(activity, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()


        Handler().postDelayed(Runnable {
            setBanner()
        }, 100)
        txt_Fixtures.setOnClickListener(this)
        txt_Live.setOnClickListener(this)
        txt_Results.setOnClickListener(this)

    }

    private val bannerFragment: ArrayList<Fragment> = ArrayList()
    private var bannerPagerAdapter: PagerAdapter? = null
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500//delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.
    internal fun setBanner() {
        if (!isAdded)
            return
//        if (newsModelArrayList != null) {
        for (i in 0..11) {
            val fragment = BannerFragment()
            val bundle = Bundle()
//                bundle.putParcelable(Tags.DATA, newsModelArrayList.get(i))
            fragment.arguments = bundle
            bannerFragment.add(fragment)
        }
        AppDelegate.LogT("bannerFragment. size==>" + bannerFragment.size + "")
        bannerPagerAdapter = PagerAdapter(activity!!.getSupportFragmentManager(), bannerFragment)
        viewPager_Banner.setAdapter(bannerPagerAdapter)
        viewPager_Banner.setClipToPadding(false);
        viewPager_Banner.setPadding(50, 0, 50, 0)
        viewPager_Banner.setPageMargin(10)
        viewPager_Banner.setCurrentItem(0)
        viewPager_Banner.startAutoScroll()
        viewPager_Banner.isCycle = true
    }


    private fun setHomeBannerAdapter() {
        if (!isAdded)
            return
        viewPager_Banner.adapter = SlidingImageAdapterHomeBanner(activity!!)
        viewPager_Banner.setClipToPadding(false);
        viewPager_Banner.setPadding(50, 0, 50, 0)
        viewPager_Banner.setPageMargin(10)

//        val handler = Handler()
//        val Update = Runnable {
//            if (currentPage == 4) {
//                currentPage = 0
//            }
//            viewPager_Banner.setCurrentItem(currentPage++, true)
//        }
//        timer = Timer() // This will create a new Thread
//        timer!!.schedule(object : TimerTask() { // task to be scheduled
//            override  fun run() {
//                handler.post(Update)
//            }
//        }, DELAY_MS, PERIOD_MS)

//        viewPager_Banner.startAutoScroll()
//        viewPager_Banner.isCycle = true
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
                recyclerView_liveMatch.visibility = VISIBLE
            }
            FIXTURES -> {
                if (!pref!!.isLogin) {
                    ll_matchSelector.visibility = GONE
                    txt_title.visibility = GONE
                    txt_toolbartitle.visibility = VISIBLE
                } else
                    txt_title.visibility = VISIBLE
                txt_title.setText(R.string.select_a_match)
                txt_Fixtures.isSelected = true
                view1.visibility = View.GONE
                recyclerView_fixMatch.visibility = VISIBLE
            }
            RESULTS -> {
                txt_title.visibility = GONE
                txt_title.setText(R.string.results)
                txt_Results.isSelected = true
                view2.visibility = View.GONE
                recyclerView_CompleteMatch.visibility = VISIBLE
            }
        }
    }

    var fixturesAdapter: MatchFixturesAdapter? = null
    var liveAdapter: MatchLiveAdapter? = null
    var completedAdapter: MatchCompletedAdapter? = null
    @SuppressLint("WrongConstant")
    private fun setFixturesAdapter() {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView_fixMatch!!.layoutManager = llm
        recyclerView_fixMatch!!.setHasFixedSize(true)
        fixturesAdapter = MatchFixturesAdapter(context!!, fixturesMatchList)
        recyclerView_fixMatch!!.adapter = fixturesAdapter
    }

    @SuppressLint("WrongConstant")
    private fun setLiveAdapter() {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView_liveMatch!!.layoutManager = llm
        recyclerView_liveMatch!!.setHasFixedSize(true)
        liveAdapter = MatchLiveAdapter(context!!, liveMatchList)
        recyclerView_liveMatch!!.adapter = liveAdapter
    }

    @SuppressLint("WrongConstant")
    private fun setCompletedAdapter() {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView_CompleteMatch!!.layoutManager = llm
        recyclerView_CompleteMatch!!.setHasFixedSize(true)
        completedAdapter = MatchCompletedAdapter(context!!, completedMatchList)
        recyclerView_CompleteMatch!!.adapter = completedAdapter
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
                    setCompletedAdapter()
                    setLiveAdapter()
                    recyclerView_fixMatch!!.adapter!!.notifyDataSetChanged()
                    recyclerView_liveMatch!!.adapter!!.notifyDataSetChanged()
                    recyclerView_CompleteMatch!!.adapter!!.notifyDataSetChanged()

                } else {
//                    AppDelegate.showToast(activity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(activity)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fixturesAdapter!!.stopUpdateTimer()
    }
}