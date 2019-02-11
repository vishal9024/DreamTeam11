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
import os.com.constant.AppRequestCodes.CREATE_CONTEST
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickDialogue
import os.com.interfaces.SelectPlayerInterface
import os.com.networkCall.ApiClient
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.createTeam.adapter.SelectTeamAdapter
import os.com.ui.createTeam.apiRequest.SwitchTeamRequest
import os.com.ui.createTeam.apiResponse.myTeamListResponse.Data
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate
import os.com.utils.CountTimer
import os.com.utils.networkUtils.NetworkUtils

class MyTeamSelectActivity : BaseActivity(), View.OnClickListener, SelectPlayerInterface, OnClickDialogue {
    override fun onClick(tag: String, success: Boolean) {
        if (tag.equals(Tags.success) && success) {
            var intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onClickItem(tag: Int, position: Int, isSelected: Boolean) {
        when (FOR) {
            AppRequestCodes.JOIN  -> {
                for (i in data.indices) {
                    data[i].isSelected = false
                }
                data[position].isSelected = true
                team_id = data[position].teamid
            }
            AppRequestCodes.CREATE_CONTEST  -> {
                for (i in data.indices) {
                    data[i].isSelected = false
                }
                data[position].isSelected = true
                team_id = data[position].teamid
            }
            AppRequestCodes.JOIN_PLUS -> {
                for (i in data.indices) {
                    data[i].isSelected = data[i].isJOINED
                }
                data[position].isSelected = isSelected
                team_id = data[position].teamid
            }
            AppRequestCodes.SWITCH -> {
                val filterSelectedList: List<Data> = data.filter { it.isSelected }
                if (isSelected) {
                    if (filterSelectedList.size == my_team_ids.size) {
                        AppDelegate.showToast(
                            this,
                            getString(R.string.you_cant_select_more_than) + " " + my_team_ids.size + " " + getString(R.string.teams)
                        )
                    } else {
                        data[position].isSelected = isSelected
                    }
                } else
                    data[position].isSelected = isSelected
            }
        }

        rv_Contest!!.adapter!!.notifyDataSetChanged()
    }

    var team_id = ""
    override fun onClickItem(tag: String, position: Int) {
    }

    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_CreateTeam -> {
//                callApi = true
                startActivityForResult(
                    Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    ).putExtra(IntentConstant.CONTEST_ID, contest_id)
                        .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.CREATE),
                    AppRequestCodes.UPDATE_ACTIVITY
                )
            }
            R.id.btn_joinContest -> {
                when (FOR) {
                    AppRequestCodes.CREATE_CONTEST->{
                        if (!team_id.isEmpty()){
                            val intent = Intent()
                            intent.putExtra(IntentConstant.TEAM_ID,team_id)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    }
                    AppRequestCodes.JOIN  -> {
                        if (!team_id.isEmpty())
                            if (NetworkUtils.isConnected()) {
                                checkAmountWallet(
                                    match!!.match_id,
                                    match!!.series_id, contest_id, team_id, this
                                )
                            } else
                                Toast.makeText(
                                    this,
                                    getString(R.string.error_network_connection),
                                    Toast.LENGTH_LONG
                                ).show()

                    }
                    AppRequestCodes.JOIN_PLUS -> {
                        if (!team_id.isEmpty())
                            if (NetworkUtils.isConnected()) {
                                checkAmountWallet(
                                    match!!.match_id,
                                    match!!.series_id, contest_id, team_id, this
                                )
                            } else
                                Toast.makeText(
                                    this,
                                    getString(R.string.error_network_connection),
                                    Toast.LENGTH_LONG
                                ).show()

                    }
                    AppRequestCodes.SWITCH -> {
                        var newTeamArray: ArrayList<String> = ArrayList()
                        for (i in data.indices) {
                            if (data[i].isSelected)
                                newTeamArray.add(data[i].teamid)
                        }
                        if (!my_team_ids.isEmpty())
                            if (newTeamArray.size < my_team_ids.size)
                                AppDelegate.showToast(
                                    this,
                                    getString(R.string.please_select_any) + " " + my_team_ids.size + " " + getString(R.string.teams)
                                )
                            else if (newTeamArray.size == my_team_ids.size) {
                                var switchTeamRequest = SwitchTeamRequest()
                                if (pref!!.isLogin)
                                    switchTeamRequest.user_id = pref!!.userdata!!.user_id
                                else
                                    switchTeamRequest.user_id= ""
                                switchTeamRequest.language = FantasyApplication.getInstance().getLanguage()
                                switchTeamRequest.match_id = match!!.match_id
                                switchTeamRequest.contest_id = contest_id
                                switchTeamRequest.series_id = match!!.series_id
                                switchTeamRequest.team_id = newTeamArray
                                callSwitchTeamApi(switchTeamRequest)
                            }
                    }
                }


            }
        }
    }

    private fun callSwitchTeamApi(switchTeamRequest: SwitchTeamRequest) {
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@MyTeamSelectActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .switch_team(switchTeamRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@MyTeamSelectActivity)
                if (response.response!!.status) {
                    finish()
                } else {
                    AppDelegate.showToast(this@MyTeamSelectActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@MyTeamSelectActivity)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        var count = os.com.application.FantasyApplication.getInstance().teamCount + 1
        btn_CreateTeam.text = getString(R.string.create_team) + " " + count
//        if (callApi)
//            if (NetworkUtils.isConnected()) {
//                callGetTeamListApi()
//            } else
//                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()

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

    var FOR = AppRequestCodes.JOIN
    var contest_id = ""
    var my_team_ids: ArrayList<String> = ArrayList()
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

            FOR = intent.getIntExtra(IntentConstant.FOR, AppRequestCodes.JOIN)
            if (FOR != CREATE_CONTEST)
                contest_id = intent.getStringExtra(IntentConstant.CONTEST_ID)
            if (FOR == AppRequestCodes.JOIN_PLUS || FOR == AppRequestCodes.SWITCH)
                my_team_ids = intent.getStringArrayListExtra(IntentConstant.TEAM_ID)
            var localTeamName = match!!.local_team_name
            var visitorTeamName = match!!.visitor_team_name
            if (match!!.local_team_name.length > 5)
                localTeamName = match!!.local_team_name.substring(0, 4)
            if (match!!.visitor_team_name.length > 5)
                visitorTeamName = match!!.visitor_team_name.substring(0, 4)

            txt_matchVS.text = localTeamName + " " + getString(R.string.vs) + " " + visitorTeamName
            if (matchType == IntentConstant.FIXTURE) {
                if (!match!!.star_date.isEmpty()) {
                    val strt_date = match!!.star_date.split("T")
                    val dateTime = strt_date.get(0) + " " + match!!.star_time
                    countTimer!!.startUpdateTimer(dateTime, txt_CountDownTimer)
                }
            } else if (matchType == IntentConstant.COMPLETED) {
                txt_CountDownTimer.setText(getString(R.string.completed))
            } else
                txt_CountDownTimer.setText(getString(R.string.in_progress))


        }
        setMenu(true, false, false, false, false)
        if (NetworkUtils.isConnected()) {
            callGetTeamListApi()
        } else
            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
        btn_CreateTeam.setOnClickListener(this)
        btn_joinContest.setOnClickListener(this)
    }

    var data: ArrayList<Data> = ArrayList()
    private fun callGetTeamListApi() {
//        callApi = false
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        else
            loginRequest[Tags.user_id] = ""
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
                AppDelegate.LogT("Response=>" + response)
                AppDelegate.hideProgressDialog(this@MyTeamSelectActivity)
                if (response.response!!.status) {
                    data = response.response!!.data!!
                    if (!data.isEmpty()) {
                        if (FOR == AppRequestCodes.JOIN) {
                            data.get(0).isSelected = true
                            team_id = data[0].teamid
                        }
                        FantasyApplication.getInstance().teamCount = data.size
                        var count = os.com.application.FantasyApplication.getInstance().teamCount + 1
                        btn_CreateTeam.setText(getString(R.string.create_team) + " " + count)
                    }
                    for (i in data.indices) {
                        for (j in my_team_ids.indices) {
                            if (data[i].teamid == my_team_ids[j]) {
                                data[i].isJOINED = true
                                data[i].isSelected = true
                            }
                        }
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
        rv_Contest!!.adapter = SelectTeamAdapter(this, data, this, FOR, my_team_ids)
    }
}