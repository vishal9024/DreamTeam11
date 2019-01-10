package os.com.ui.joinedContest.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_joined_contest.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.contest.apiResponse.getContestList.Contest
import os.com.ui.contest.apiResponse.getContestList.ContestCategory
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.adapter.JoinedFixturesContestAdapter
import os.com.utils.AppDelegate
import os.com.utils.CountTimer
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class FixtureJoinedContestActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joined_contest)
        initViews()
    }

    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    private fun initViews() {
          try{
              setSupportActionBar(toolbar)
              supportActionBar!!.setDisplayHomeAsUpEnabled(true)
              supportActionBar!!.setDisplayShowHomeEnabled(true)
              supportActionBar!!.setDisplayShowTitleEnabled(false)
              toolbarTitleTv.setText(R.string.joined_Contest)
              setMenu(false, false, false, false)

//              setAdapter()
              if (intent != null) {
                  match = intent.getParcelableExtra(IntentConstant.MATCH)
                  matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
                  txt_matchVS.text = match!!.local_team_name + " " + getString(R.string.vs) + " " +
                          match!!.visitor_team_name
                  if (matchType == IntentConstant.FIXTURE) {
                      if (!match!!.star_date.isEmpty()) {
                          val strt_date = match!!.star_date.split("T")
                          val dateTime = strt_date.get(0) + " " + match!!.star_time
                          countTimer!!.startUpdateTimer(dateTime, txt_CountDownTimer)
                      }
                  } else if (matchType == IntentConstant.FIXTURE) {
                      txt_CountDownTimer.setText(getString(R.string.completed))
                  } else
                      txt_CountDownTimer.setText(getString(R.string.in_progress))
                  if (NetworkUtils.isConnected()) {
                      callGetJoinedContestListApi()
                  } else
                      Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
              }
              } catch (e: Exception) {
                      e.printStackTrace()
              }

    }

    var contests: ArrayList<Contest>? = ArrayList()
    var contestList: ArrayList<ContestCategory> = ArrayList()
    private fun callGetJoinedContestListApi() {
          try{
              val map = HashMap<String, String>()
              if (pref!!.isLogin)
                  map[Tags.user_id] = pref!!.userdata!!.user_id
              map[Tags.language] = FantasyApplication.getInstance().getLanguage()
              map[Tags.match_id] = match!!.match_id/*"13071965317"*/
              map[Tags.series_id] = match!!.series_id/*"13071965317"*/
              GlobalScope.launch(Dispatchers.Main) {
                  AppDelegate.showProgressDialog(this@FixtureJoinedContestActivity)
                  try {
                      val request = ApiClient.client
                          .getRetrofitService()
                          .getJoinedContestlist(map)
                      val response = request.await()
                      AppDelegate.LogT("Response=>" + response);
                      AppDelegate.hideProgressDialog(this@FixtureJoinedContestActivity)
                      if (response.response!!.status) {
                          contestList = response.response!!.data!!.match_contest!!
                          setAdapter()
                          if (!pref!!.isLogin) {
//                        ll_viewTeam.visibility = View.GONE
//                        btn_CreateTeam.visibility = View.VISIBLE
                          } else {
                              txt_joined_contest.text = response.response!!.data!!.my_contests
                              txt_MyTeams.text = response.response!!.data!!.my_teams
                              if (response.response!!.data!!.my_teams != null) {
                                  if (response.response!!.data!!.my_teams.toInt() == 0) {
//                                ll_viewTeam.visibility = View.GONE
//                                btn_CreateTeam.visibility = View.VISIBLE
                                  } else {
//                                ll_viewTeam.visibility = View.VISIBLE
//                                btn_CreateTeam.visibility = View.GONE
                                  }
                              }
                          }
                          for (contest in contestList) {
                              contests!!.addAll(contest.contests!!)
//                        txt_AllContestCount.text = contests!!.size.toString() + " " + getString(R.string.contest)
                          }
                      } else {
                      }
                  } catch (exception: Exception) {
                      AppDelegate.hideProgressDialog(this@FixtureJoinedContestActivity)
                  }
              }
              } catch (e: Exception) {
                      e.printStackTrace()
              }

    }
    var joinedFixturesContestAdapter: JoinedFixturesContestAdapter? = null
    @SuppressLint("WrongConstant")
    private fun setAdapter() {
          try{
              val llm = LinearLayoutManager(this)
              llm.orientation = LinearLayoutManager.VERTICAL
              rv_Contest!!.layoutManager = llm
              joinedFixturesContestAdapter = JoinedFixturesContestAdapter(this, contestList, match, matchType)
              rv_Contest!!.adapter = joinedFixturesContestAdapter
              } catch (e: Exception) {
                      e.printStackTrace()
              }
    }

}