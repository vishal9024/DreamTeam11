package os.com.ui.dashboard.myContest.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.home_fragment.*
import os.com.AppBase.BaseFragment
import os.com.R
import os.com.ui.dashboard.myContest.adapter.MyContestCompletedAdapter
import os.com.ui.dashboard.myContest.adapter.MyContestFixturesAdapter
import os.com.ui.dashboard.myContest.adapter.MyContestLiveAdapter


/**
 * Created by heenas on 3/5/2018.
 */
class MyContestFragment : BaseFragment(), View.OnClickListener {
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

    private fun initViews() {
        viewPager_Banner.visibility= GONE
        matchSelector(FIXTURES)
//        setHomeBannerAdapter()
        txt_Fixtures.setOnClickListener(this)
        txt_Live.setOnClickListener(this)
        txt_Results.setOnClickListener(this)
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
    var fixturesAdapter: MyContestFixturesAdapter? = null
    var liveAdapter: MyContestLiveAdapter? = null
    var completedAdapter: MyContestCompletedAdapter? = null

    @SuppressLint("WrongConstant")
    private fun setFixturesAdapter() {
        if (fixturesAdapter == null) {
            val llm = LinearLayoutManager(context)
            llm.orientation = LinearLayoutManager.VERTICAL
            recyclerView_fixMatch!!.layoutManager = llm
            recyclerView_fixMatch!!.setHasFixedSize(true)
            fixturesAdapter = MyContestFixturesAdapter(context!!)
            recyclerView_fixMatch!!.adapter = fixturesAdapter
        } else
            recyclerView_fixMatch!!.adapter!!.notifyDataSetChanged()
    }

    @SuppressLint("WrongConstant")
    private fun setLiveAdapter() {
        if (liveAdapter == null) {
            val llm = LinearLayoutManager(context)
            llm.orientation = LinearLayoutManager.VERTICAL
            recyclerView_liveMatch!!.layoutManager = llm
            recyclerView_liveMatch!!.setHasFixedSize(true)
            liveAdapter = MyContestLiveAdapter(context!!)
            recyclerView_liveMatch!!.adapter = liveAdapter
        } else
            recyclerView_liveMatch!!.adapter!!.notifyDataSetChanged()
    }

    @SuppressLint("WrongConstant")
    private fun setCompletedAdapter() {
        if (completedAdapter == null) {
            val llm = LinearLayoutManager(context)
            llm.orientation = LinearLayoutManager.VERTICAL
            recyclerView_CompleteMatch!!.layoutManager = llm
            recyclerView_CompleteMatch!!.setHasFixedSize(true)
            completedAdapter = MyContestCompletedAdapter(context!!)
            recyclerView_CompleteMatch!!.adapter = completedAdapter
        } else
            recyclerView_CompleteMatch!!.adapter!!.notifyDataSetChanged()
    }
}