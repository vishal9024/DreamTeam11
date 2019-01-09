package os.com.ui.createTeam.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_choose_c_vc.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.OnClickCVC
import os.com.networkCall.ApiClient
import os.com.ui.createTeam.adapter.ChooseC_VC_Adapter
import os.com.ui.createTeam.apiResponse.SelectPlayer
import os.com.ui.createTeam.apiResponse.playerListResponse.Data
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate
import os.com.utils.CountTimer
import os.com.utils.networkUtils.NetworkUtils


class Choose_C_VC_Activity : BaseActivity(), View.OnClickListener, OnClickCVC {
    public var isShowingWk = false
    public var isShowingbat = false
    public var isShowingbowl = false
    public var isShowingAr = false
    var bowlerList: MutableList<Data>? = ArrayList()
    var arList: MutableList<Data>? = ArrayList()
    var wkList: MutableList<Data>? = ArrayList()
    var batsmenList: MutableList<Data>? = ArrayList()
    var match: Match? = null
    private var captain = ""
    private var vicecaptain = ""
    var selectPlayer: SelectPlayer? = null
    //    var contest_id = ""
    override fun onClick(tag: String, position: Int) {
        if (tag == "c") {
            for (i in playerList.indices) {
                playerList[i].isCaptain = false
            }
            if (playerList[position].isViceCaptain) {
                playerList[position].isViceCaptain = false
                vicecaptain = ""
            }
            playerList[position].isCaptain = true
            captain = playerList[position].player_id
            isShowingWk = false
            isShowingbat = false
            isShowingbowl = false
            isShowingAr = false
            adapter!!.notifyDataSetChanged()
        } else if (tag == "vc") {
            for (i in playerList.indices) {
                playerList[i].isViceCaptain = false
            }
            if (playerList[position].isCaptain) {
                playerList[position].isCaptain = false
                captain = ""
            }
            playerList[position].isViceCaptain = true
            vicecaptain = playerList[position].player_id
            isShowingWk = false
            isShowingbat = false
            isShowingbowl = false
            isShowingAr = false
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_CreateTeam -> {
                if (captain.isEmpty()) {
                    AppDelegate.showToast(this, getString(R.string.select_captain))
                } else if (vicecaptain.isEmpty()) {
                    AppDelegate.showToast(this, getString(R.string.select_vc))
                } else if (NetworkUtils.isConnected()) {
                    var player_ids: ArrayList<String> = ArrayList()
                    for (i in playerList) {
                        player_ids.add(i.player_id)
                    }
                    if (NetworkUtils.isConnected()) {
                        callCreateTeamApi(player_ids)
                    } else
                        Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()


                } else
                    AppDelegate.showToast(this, getString(R.string.error_network_connection))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_c_vc)
        initViews()
    }


    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(getString(R.string.choose_c_vc_title))
        setMenu(false, false, false, false)
        getData()
        setAdapter()
        btn_CreateTeam.setOnClickListener(this)
//        txt_Signup.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countTimer != null)
            countTimer!!.stopUpdateTimer()
    }

    var countTimer: CountTimer? = CountTimer()
    var matchType = IntentConstant.FIXTURE
    var team_id = ""
    private fun getData() {
//        contest_id = intent.getStringExtra(IntentConstant.CONTEST_ID)
        selectPlayer = intent.getParcelableExtra(IntentConstant.SELECT_PLAYER)
        match = intent.getParcelableExtra(IntentConstant.MATCH)
        matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
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
        var bowlerList: MutableList<Data>? = ArrayList()
        var arList: MutableList<Data>? = ArrayList()
        var wkList: MutableList<Data>? = ArrayList()
        var batsmenList: MutableList<Data>? = ArrayList()
        wkList = intent.getParcelableArrayListExtra(IntentConstant.WK)
        arList = intent.getParcelableArrayListExtra(IntentConstant.AR)
        bowlerList = intent.getParcelableArrayListExtra(IntentConstant.BOWLER)
        batsmenList = intent.getParcelableArrayListExtra(IntentConstant.BATSMEN)
        if (intent.getIntExtra(IntentConstant.ISEDIT, 0) ==AppRequestCodes.EDIT) {
            team_id = intent.getStringExtra(IntentConstant.TEAM_ID)
        }

        for (i in wkList) {
            if (i.isSelected) {
                this.wkList!!.add(i)
                if (i.isCaptain)
                    captain = i.player_id
                if (i.isViceCaptain)
                    vicecaptain = i.player_id
            }
        }
        for (i in arList) {
            if (i.isSelected) {
                this.arList!!.add(i)
                if (i.isCaptain)
                    captain = i.player_id
                if (i.isViceCaptain)
                    vicecaptain = i.player_id
            }
        }
        for (i in bowlerList) {
            if (i.isSelected) {
                this.bowlerList!!.add(i)
                if (i.isCaptain)
                    captain = i.player_id
                if (i.isViceCaptain)
                    vicecaptain = i.player_id
            }
        }
        for (i in batsmenList) {
            if (i.isSelected) {
                this.batsmenList!!.add(i)
                if (i.isCaptain)
                    captain = i.player_id
                if (i.isViceCaptain)
                    vicecaptain = i.player_id
            }
        }

        playerList.addAll(this.wkList!!)
        playerList.addAll(this.bowlerList!!)
        playerList.addAll(this.arList!!)
        playerList.addAll(this.batsmenList!!)
    }

    var playerList: MutableList<Data> = ArrayList()

    var adapter: ChooseC_VC_Adapter? = null
    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Players!!.layoutManager = llm
        adapter = ChooseC_VC_Adapter(this, this, playerList)
        rv_Players!!.adapter = adapter
    }

    private fun callCreateTeamApi(player_id: ArrayList<String>) {
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id
//      loginRequest[Tags.contest_id] = contest_id
        loginRequest[Tags.team_id]=team_id
        loginRequest["captain"] = captain
        loginRequest["vice_captain"] = vicecaptain
        loginRequest["player_id"] = player_id.toString()
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@Choose_C_VC_Activity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .create_team(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@Choose_C_VC_Activity)
                if (response.response!!.status!!) {
                    startActivity(
                        Intent(
                            this@Choose_C_VC_Activity,
                            TeamPreviewActivity::class.java
                        ).putParcelableArrayListExtra(
                            IntentConstant.DATA,
                            playerList as java.util.ArrayList<out Parcelable>
                        )
                    )
                    finish()
                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@Choose_C_VC_Activity)
            }
        }
    }
}