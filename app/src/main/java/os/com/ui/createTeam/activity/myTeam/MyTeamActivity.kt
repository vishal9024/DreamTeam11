package os.com.ui.createTeam.activity.myTeam

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import os.com.interfaces.OnClickRecyclerView
import os.com.networkCall.ApiClient
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.createTeam.adapter.MyTeamAdapter
import os.com.ui.createTeam.apiResponse.myTeamListResponse.Data
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate
import os.com.utils.CountTimer
import os.com.utils.networkUtils.NetworkUtils

class MyTeamActivity : BaseActivity(), View.OnClickListener, OnClickRecyclerView {
    override fun onClickItem(tag: String, position: Int) {
        if (tag.equals(Tags.clone)) {
            startActivityForResult(
                Intent(this, ChooseTeamActivity::class.java).putExtra(
                    IntentConstant.DATA,
                    data[position]
                ).putParcelableArrayListExtra(
                    IntentConstant.SELECT_PLAYER,
                    data[position].player_details
                ).putExtra(IntentConstant.MATCH, match).putExtra(IntentConstant.CONTEST_TYPE, matchType)
                    .putExtra("substitute",data[position].substitute_detail)
                    .putExtra(IntentConstant.ISEDIT, AppRequestCodes.CLONE),
                AppRequestCodes.CLONE
            )
        } else if (tag.equals(Tags.edit)) {
            startActivityForResult(
                Intent(this, ChooseTeamActivity::class.java).putExtra(
                    IntentConstant.DATA,
                    data[position]
                ).putParcelableArrayListExtra(
                    IntentConstant.SELECT_PLAYER,
                    data[position].player_details
                ).putExtra("substitute",data[position].substitute_detail)
                    .putExtra(IntentConstant.MATCH, match).putExtra(IntentConstant.CONTEST_TYPE, matchType)
                    .putExtra(IntentConstant.ISEDIT, AppRequestCodes.EDIT),
                AppRequestCodes.EDIT
            )
        }
    }
    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_CreateTeam -> {
                startActivityForResult(
                    Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                        IntentConstant.CONTEST_TYPE,
                        matchType
                    ).putExtra(IntentConstant.CONTEST_ID, "")
                        .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.CREATE),   AppRequestCodes.UPDATE_ACTIVITY
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
            var count = os.com.application.FantasyApplication.getInstance().teamCount + 1
            btn_CreateTeam.text = getString(R.string.create_team) + " " + count

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK)
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

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.my_team)
        var count=os.com.application.FantasyApplication.getInstance().teamCount + 1
        btn_CreateTeam.setText(getString(R.string.create_team)+" " +count )
        if (intent != null) {
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
            var localTeamName=match!!.local_team_name
            var visitorTeamName=match!!.visitor_team_name
            if (match!!.local_team_name.length>5)
                localTeamName=match!!.local_team_name.substring(0,4)
            if (match!!.visitor_team_name.length>5)
                visitorTeamName=match!!.visitor_team_name.substring(0,4)

            txt_matchVS.text = localTeamName+ " " + getString(R.string.vs) + " " + visitorTeamName
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

        setMenu(true, false, false, false,false)
        if (NetworkUtils.isConnected()) {
            callGetTeamListApi()
        } else
            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
//        btn_CreateTeam.setOnClickListener(this)
//        txt_Signup.setOnClickListener(this)
        btn_CreateTeam.setOnClickListener(this)
    }

    var data: ArrayList<Data> = ArrayList()
    private fun callGetTeamListApi() {
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        else
            loginRequest[Tags.user_id] = ""
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@MyTeamActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .player_team_list(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@MyTeamActivity)
                if (response.response!!.status) {
                    data = response.response!!.data!!
                    FantasyApplication.getInstance().teamCount = data.size
                    var count = os.com.application.FantasyApplication.getInstance().teamCount + 1
                    btn_CreateTeam.setText(getString(R.string.create_team) + " " + count)
                    setAdapter()
                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@MyTeamActivity)
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = MyTeamAdapter(this, data, this)
    }
}