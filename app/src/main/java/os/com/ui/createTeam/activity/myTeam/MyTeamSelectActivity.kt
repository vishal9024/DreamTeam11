package os.com.ui.createTeam.activity.myTeam

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_myteam.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickDialogue
import os.com.interfaces.SelectPlayerInterface
import os.com.networkCall.ApiClient
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.createTeam.adapter.SelectTeamAdapter
import os.com.ui.createTeam.apiResponse.myTeamListResponse.Data
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate
import os.com.utils.CountTimer
import os.com.utils.networkUtils.NetworkUtils

class MyTeamSelectActivity : BaseActivity(), View.OnClickListener, SelectPlayerInterface, OnClickDialogue {
    override fun onClick(tag: String, success: Boolean) {
        if (tag.equals(Tags.success) && success)
            finish()
    }

    override fun onClickItem(tag: Int, position: Int, isSelected: Boolean) {
        for (i in data.indices) {
            data[i].isSelected = false
        }
        data[position].isSelected = true
        team_id = data[position].teamid
        rv_Contest!!.adapter!!.notifyDataSetChanged()
    }

    var team_id = ""
    override fun onClickItem(tag: String, position: Int) {
    }

    var callApi = false
    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_CreateTeam -> {
                callApi = true
                startActivityForResult(
                    Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    ).putExtra(IntentConstant.CONTEST_ID, contest_id)
                        .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.CREATE), AppRequestCodes.UPDATE_ACTIVITY
                )
            }
            R.id.btn_joinContest -> {
                if (!team_id.isEmpty())
                    if (NetworkUtils.isConnected()) {
                        checkAmountWallet(
                            match!!.match_id,
                            match!!.series_id, contest_id, team_id, this
                        )
                    } else
                        Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        var count = os.com.application.FantasyApplication.getInstance().teamCount + 1
        btn_CreateTeam.text = getString(R.string.create_team) + " " + count
        if (callApi)
            if (NetworkUtils.isConnected()) {
                callGetTeamListApi()
            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            if (NetworkUtils.isConnected()) {
                callGetTeamListApi()
            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myteam)
        initViews()
    }

    var contest_id = ""
    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.my_team)
        view.visibility = VISIBLE
        btn_joinContest.visibility = View.VISIBLE

        var count = os.com.application.FantasyApplication.getInstance().teamCount + 1
        btn_CreateTeam.setText(getString(R.string.create_team) + " " + count)
        if (intent != null) {
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            contest_id = intent.getStringExtra(IntentConstant.CONTEST_ID)
            txt_matchVS.text = match!!.local_team_name + " " + getString(R.string.vs) + " " + match!!.visitor_team_name
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
        }
        setMenu(true, false, false, false)
        if (NetworkUtils.isConnected()) {
            callGetTeamListApi()
        } else
            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
        btn_CreateTeam.setOnClickListener(this)
        btn_joinContest.setOnClickListener(this)
//        btn_CreateTeam.setOnClickListener(this)
//        txt_Signup.setOnClickListener(this)
    }

    var data: ArrayList<Data> = ArrayList()
    private fun callGetTeamListApi() {
        callApi=false
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@MyTeamSelectActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .player_team_list(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@MyTeamSelectActivity)
                if (response.response!!.status) {
                    data = response.response!!.data!!
                    if (!data.isEmpty()) {
                        data.get(0).isSelected = true
                        team_id = data[0].teamid
                        FantasyApplication.getInstance().teamCount = data.size
                        var count = os.com.application.FantasyApplication.getInstance().teamCount + 1
                        btn_CreateTeam.setText(getString(R.string.create_team) + " " + count)
                    }
                    setAdapter()
                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@MyTeamSelectActivity)
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = SelectTeamAdapter(this, data, this)
    }
}