package os.com.ui.joinedContest.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.replace_substitute.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.SelectPlayerInterface
import os.com.networkCall.ApiClient
import os.com.ui.createTeam.apiResponse.myTeamListResponse.Substitute
import os.com.ui.createTeam.apiResponse.playerListResponse.Data
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.joinedContest.adapter.ReplaceSubstituteAdapter
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.set


class ReplaceWithSubstituteActivity : BaseActivity(), View.OnClickListener, SelectPlayerInterface {
    var replacedPlayerId = ""
    var captain_id = ""
    var vice_captain_id = ""
    override fun onClickItem(tag: Int, position: Int, isSelected: Boolean) {
        if (substituteDetail!!.role.contains("Wicketkeeper", true)) {
            for (i in filteredPlayersList.indices)
                filteredPlayersList[i].isSelected = false
            filteredPlayersList[position].isSelected = isSelected
            if (isSelected) {
                replacedPlayerId = playerListfinal!![position].player_id
                btn_replaceSubstitute.isEnabled = true
            } else {
                replacedPlayerId = ""
                btn_replaceSubstitute.isEnabled = false
            }
            rv_players.adapter!!.notifyDataSetChanged()
        } else {
            var filteredList: List<Data> = ArrayList()
            when {
                filteredPlayersList[position].player_record!!.playing_role.contains("Bowler", true) -> filteredList =
                        playerListfinal!!.filter { it.player_record!!.playing_role.contains("Bowler", true) }
                filteredPlayersList[position].player_record!!.playing_role.contains("Batsman", true) -> filteredList =
                        playerListfinal!!.filter { it.player_record!!.playing_role.contains("Batsman", true) }
                filteredPlayersList[position].player_record!!.playing_role.contains(
                    "Allrounder",
                    true
                ) -> filteredList =
                        playerListfinal!!.filter { it.player_record!!.playing_role.contains("Allrounder", true) }
            }
            when {
                filteredPlayersList[position].player_record!!.playing_role.contains("Bowler", true) -> {
                    if (!filteredList.isEmpty() && filteredList.size < 5) {
                        val totalCredit =
                            calculateTotalCredit(filteredPlayersList[position].player_record!!.player_credit)
                        if (totalCredit > 100) {
                            AppDelegate.showToast(this, "You do not have enough credit to select this player.")
                        } else {
                            update(position, isSelected)
                        }
                    } else {
                        AppDelegate.showToast(this, "You can not select more than five Bowlers.")
                    }
                }
                filteredPlayersList[position].player_record!!.playing_role.contains("Batsman", true) -> {
                    if (!filteredList.isEmpty() && filteredList.size < 5) {
                        val totalCredit =
                            calculateTotalCredit(filteredPlayersList[position].player_record!!.player_credit)
                        if (totalCredit > 100) {
                            AppDelegate.showToast(this, "You do not have enough credit to select this player.")
                        } else {
                            update(position, isSelected)
                        }
                    } else {
                        AppDelegate.showToast(this, "You can not select more than five Batsmen.")
                    }
                }
                filteredPlayersList[position].player_record!!.playing_role.contains("Allrounder", true) -> {
                    if (!filteredList.isEmpty() && filteredList.size < 3) {
                        val totalCredit =
                            calculateTotalCredit(filteredPlayersList[position].player_record!!.player_credit)
                        if (totalCredit > 100) {
                            AppDelegate.showToast(this, "You do not have enough credit to select this player.")
                        } else {
                            update(position, isSelected)
                        }
                    } else {
                        AppDelegate.showToast(this, "You can not select more than three All-Rounder.")
                    }
                }
            }
        }
    }

    fun update(position: Int, isSelected: Boolean) {
        for (i in filteredPlayersList.indices)
            filteredPlayersList[i].isSelected = false
        filteredPlayersList[position].isSelected = isSelected
        if (isSelected) {
            replacedPlayerId = playerListfinal!![position].player_id
            btn_replaceSubstitute.isEnabled = true
        } else {
            replacedPlayerId = ""
            btn_replaceSubstitute.isEnabled = false
        }
        rv_players.adapter!!.notifyDataSetChanged()
    }

    fun calculateTotalCredit(playerCredit: String): Float {
        val totalCredit = totalCredit - playerCredit.toFloat() + substituteDetail!!.credits.toFloat()
        return totalCredit
    }

    override fun onClickItem(tag: String, position: Int) {
    }

    var substituteDetail: Substitute? = null
    var contest_id = ""
    var team_id = ""
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_replaceSubstitute -> {
                if (!replacedPlayerId.isEmpty()) {
                    if (NetworkUtils.isConnected()) {
                        callReplaceSubstituteApi()
                    } else
                        Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.replace_substitute)
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countTimer != null)
            countTimer!!.stopUpdateTimer()
    }

    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    var teamNo = ""
    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.replace_a_player)
        btn_replaceSubstitute.setOnClickListener(this)
        setMenu(false, false, false, false, false)
        getIntentData()
        if (NetworkUtils.isConnected()) {
            if (match != null)
                callGetContestListApi()
        } else
            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
    }

    private fun callReplaceSubstituteApi() {
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        else
            loginRequest[Tags.user_id] = ""
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id
        loginRequest[Tags.contest_id] = contest_id
        loginRequest[Tags.team_no] = teamNo
        loginRequest[Tags.player_id] = replacedPlayerId
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@ReplaceWithSubstituteActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .replace_player(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@ReplaceWithSubstituteActivity)
                if (response.response!!.status!!) {
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    logoutIfDeactivate(response.response!!.message!!)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@ReplaceWithSubstituteActivity)
            }
        }
    }

    var localTeamName = ""
    var visitorTeamName = ""
    private fun getIntentData() {
        if (intent != null) {
            captain_id = intent.getStringExtra(IntentConstant.CAPTAIN_ID)
            vice_captain_id = intent.getStringExtra(IntentConstant.VICE_CAPTAIN_ID)
            contest_id = intent.getStringExtra(IntentConstant.CONTEST_ID)
            teamNo = intent.getStringExtra(IntentConstant.TEAM_ID)
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
            playerListEdit = intent.getParcelableArrayListExtra(IntentConstant.SELECT_PLAYER)
            substituteDetail = intent.getParcelableExtra("substitute")
            localTeamName = match!!.local_team_name
            visitorTeamName = match!!.visitor_team_name
            if (match!!.local_team_name.length > 5)
                localTeamName = match!!.local_team_name.substring(0, 4)
            if (match!!.visitor_team_name.length > 5)
                visitorTeamName = match!!.visitor_team_name.substring(0, 4)
            ImageLoader.getInstance().displayImage(
                substituteDetail!!.image,
                imvUserProfile,
                FantasyApplication.getInstance().options
            )
            txt_label.text = substituteDetail!!.name
            txtPoints.text = substituteDetail!!.credits

        }
    }


    var playerListfinal: MutableList<Data>? = ArrayList()
    var playerList: MutableList<Data>? = ArrayList()
    var playerListEdit: ArrayList<os.com.ui.createTeam.apiResponse.myTeamListResponse.PlayerRecord>? = null
    private fun callGetContestListApi() {
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        else
            loginRequest[Tags.user_id] = ""
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id
        loginRequest[Tags.local_team_id] = match!!.local_team_id
        loginRequest[Tags.visitor_team_id] = match!!.visitor_team_id
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@ReplaceWithSubstituteActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .getPlayerList(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@ReplaceWithSubstituteActivity)
                if (response.response!!.status) {
                    playerList = response.response!!.data!!
                    var playerListNull: MutableList<Data>? = ArrayList()
                    for (i in 0 until response.response!!.data!!.size) {
                        if (response.response!!.data!![i].player_record == null) {
                            playerListNull!!.add(response.response!!.data!![i])
                        }
                    }
                    playerList!!.removeAll(playerListNull!!)
                    AppDelegate.LogT("Size b4==>" + playerList!!.size)
                    updateData()
                } else {
                    logoutIfDeactivate(response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@ReplaceWithSubstituteActivity)
            }
        }
    }

    var totalCredit = 0f
    var filteredPlayersList: List<Data> = ArrayList()
    private fun updateData() {
        for (position in playerList!!.indices) {
            for (playerData in playerListEdit!!) {
                if (playerList!![position].player_id == playerData.player_id) {
                    playerListfinal!!.add(playerList!![position])
                    totalCredit = totalCredit + playerList!![position].player_record!!.player_credit.toFloat()
                }
            }
        }
        var filteredPlayerList: List<Data> = ArrayList()
        when {
            substituteDetail!!.role.contains("Wicketkeeper", true) -> filteredPlayerList =
                    playerListfinal!!.filter { it.player_record!!.playing_role.contains("Wicketkeeper", true) }
            substituteDetail!!.role.contains("Bowler", true) || substituteDetail!!.role.contains(
                "Batsman",
                true
            ) || substituteDetail!!.role.contains("Allrounder", true) -> filteredPlayerList =
                    playerListfinal!!.filter {
                        it.player_record!!.playing_role.contains(
                            "Bowler",
                            true
                        ) || it.player_record!!.playing_role.contains(
                            "Batsman",
                            true
                        ) || it.player_record!!.playing_role.contains("Allrounder", true)
                    }
//            substituteDetail!!.role.contains("Batsman", true) -> filteredPlayerList =
//                    playerListfinal!!.filter { it.player_record!!.playing_role.contains("Batsman", true) }
//            substituteDetail!!.role.contains("Allrounder", true) -> filteredPlayerList =
//                    playerListfinal!!.filter { it.player_record!!.playing_role.contains("Allrounder", true) }

        }
        filteredPlayersList = filteredPlayerList.filter {
            !it.player_record!!.player_id.equals(captain_id) && !it.player_record!!.player_id.equals(vice_captain_id)
        }
        setAdapter(filteredPlayersList as MutableList<Data>)
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(playerlist: MutableList<Data>) {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_players!!.layoutManager = llm
        rv_players!!.adapter = ReplaceSubstituteAdapter(
            this, playerlist, this, localTeamName,
            visitorTeamName,
            match!!.local_team_id,
            match!!.visitor_team_id
        )
    }

}