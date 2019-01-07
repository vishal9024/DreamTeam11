package os.com.ui.createTeam.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_choose_team.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.BuildConfig
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.interfaces.SelectPlayerInterface
import os.com.networkCall.ApiClient
import os.com.ui.createTeam.adapter.PlayerListAdapter
import os.com.ui.createTeam.apiResponse.SelectPlayer
import os.com.ui.createTeam.apiResponse.playerListResponse.Data
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate
import os.com.utils.CountTimer
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.set

class ChooseTeamActivity : BaseActivity(), View.OnClickListener, SelectPlayerInterface {
    override fun onClickItem(tag: Int, position: Int, isSelected: Boolean) {
        if (tag == WK) {
            var player_credit = 0.0
            if (isSelected) {
                if (selectPlayer!!.wk_selected < selectPlayer!!.wk_count) {
                    if (!wkList!!.get(position).player_record!!.player_credit.isEmpty()) {
                        player_credit = wkList!!.get(position).player_record!!.player_credit.toDouble()
                    }
                    var total_credit = selectPlayer!!.total_credit + player_credit
                    if (total_credit > 100) {
                        view.visibility = VISIBLE
                        return
                    }
                    var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                    var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                    if (wkList!![position].team_id.equals(match!!.local_team_id))
                        localTeamplayerCount = selectPlayer!!.localTeamplayerCount + 1
                    else
                        visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount + 1
                    updateTeamData(
                        selectPlayer!!.extra_player,
                        selectPlayer!!.wk_selected + 1,
                        selectPlayer!!.bat_selected,
                        selectPlayer!!.ar_selected,
                        selectPlayer!!.bowl_selected,
                        selectPlayer!!.selectedPlayer + 1,
                        localTeamplayerCount,
                        visitorTeamPlayerCount,
                        total_credit
                    )
                    wkList!!.get(position).isSelected = isSelected
                }
            } else {
                if (selectPlayer!!.wk_selected > 0) {
                    view.visibility = GONE
                    if (!wkList!!.get(position).player_record!!.player_credit.isEmpty()) {
                        player_credit = wkList!!.get(position).player_record!!.player_credit.toDouble()
                    }
                    var total_credit = selectPlayer!!.total_credit - player_credit
                    var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                    var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                    if (wkList!![position].team_id.equals(match!!.local_team_id))
                        localTeamplayerCount = selectPlayer!!.localTeamplayerCount - 1
                    else
                        visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount - 1

                    updateTeamData(
                        selectPlayer!!.extra_player,
                        selectPlayer!!.wk_selected - 1,
                        selectPlayer!!.bat_selected,
                        selectPlayer!!.ar_selected,
                        selectPlayer!!.bowl_selected,
                        selectPlayer!!.selectedPlayer - 1,
                        localTeamplayerCount,
                        visitorTeamPlayerCount,
                        total_credit
                    )
                    wkList!!.get(position).isSelected = isSelected
                }
            }
        } else if (tag == AR) {
            var player_credit = 0.0
            if (isSelected) {
                if (selectPlayer!!.ar_selected < selectPlayer!!.ar_maxcount) {
                    if (selectPlayer!!.selectedPlayer < 11) {
                        if (selectPlayer!!.ar_selected < selectPlayer!!.ar_mincount || selectPlayer!!.extra_player > 0) {
                            var extra = selectPlayer!!.extra_player
                            if (selectPlayer!!.ar_selected >= selectPlayer!!.ar_mincount) {
                                extra = selectPlayer!!.extra_player - 1
                            }
                            if (!arList!!.get(position).player_record!!.player_credit.isEmpty()) {
                                player_credit = arList!!.get(position).player_record!!.player_credit.toDouble()
                            }
                            var total_credit = selectPlayer!!.total_credit + player_credit
                            if (total_credit > 100) {
                                view.visibility = VISIBLE
                                return
                            }
                            var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                            var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                            if (arList!![position].team_id.equals(match!!.local_team_id))
                                localTeamplayerCount = selectPlayer!!.localTeamplayerCount + 1
                            else
                                visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount + 1

                            updateTeamData(
                                extra,
                                selectPlayer!!.wk_selected,
                                selectPlayer!!.bat_selected,
                                selectPlayer!!.ar_selected + 1,
                                selectPlayer!!.bowl_selected,
                                selectPlayer!!.selectedPlayer + 1,
                                localTeamplayerCount,
                                visitorTeamPlayerCount,
                                total_credit
                            )
                            arList!!.get(position).isSelected = isSelected
                        }
                    }
                }
            } else {
                if (selectPlayer!!.ar_selected > 0) {
                    view.visibility = GONE
                    if (!arList!!.get(position).player_record!!.player_credit.isEmpty()) {
                        player_credit = arList!!.get(position).player_record!!.player_credit.toDouble()
                    }
                    var total_credit = selectPlayer!!.total_credit - player_credit
                    var extra = selectPlayer!!.extra_player
                    if (selectPlayer!!.ar_selected > selectPlayer!!.ar_mincount) {
                        extra = selectPlayer!!.extra_player + 1
                    }
                    var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                    var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                    if (arList!![position].team_id.equals(match!!.local_team_id))
                        localTeamplayerCount = selectPlayer!!.localTeamplayerCount - 1
                    else
                        visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount - 1

                    updateTeamData(
                        extra,
                        selectPlayer!!.wk_selected,
                        selectPlayer!!.bat_selected,
                        selectPlayer!!.ar_selected - 1,
                        selectPlayer!!.bowl_selected,
                        selectPlayer!!.selectedPlayer - 1,
                        localTeamplayerCount,
                        visitorTeamPlayerCount,
                        total_credit
                    )
                    arList!!.get(position).isSelected = isSelected
                }
            }


        } else if (tag == BAT) {
            var player_credit = 0.0
            if (isSelected) {
                if (selectPlayer!!.bat_selected < selectPlayer!!.bat_maxcount) {
                    if (selectPlayer!!.selectedPlayer < 11) {
                        if (selectPlayer!!.bat_selected < selectPlayer!!.bat_mincount || selectPlayer!!.extra_player > 0) {
                            var extra = selectPlayer!!.extra_player
                            if (selectPlayer!!.bat_selected >= selectPlayer!!.bat_mincount) {
                                extra = selectPlayer!!.extra_player - 1
                            }
                            if (!batsmenList!!.get(position).player_record!!.player_credit.isEmpty()) {
                                player_credit = batsmenList!!.get(position).player_record!!.player_credit.toDouble()
                            }
                            var total_credit = selectPlayer!!.total_credit + player_credit
                            if (total_credit > 100) {
                                view.visibility = VISIBLE
                                return
                            }
                            var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                            var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                            if (batsmenList!![position].team_id.equals(match!!.local_team_id))
                                localTeamplayerCount = selectPlayer!!.localTeamplayerCount + 1
                            else
                                visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount + 1

                            updateTeamData(
                                extra,
                                selectPlayer!!.wk_selected,
                                selectPlayer!!.bat_selected + 1,
                                selectPlayer!!.ar_selected,
                                selectPlayer!!.bowl_selected,
                                selectPlayer!!.selectedPlayer + 1,
                                localTeamplayerCount,
                                visitorTeamPlayerCount,
                                total_credit
                            )
                            batsmenList!!.get(position).isSelected = isSelected
                        }
                    }
                }
            } else {
                if (selectPlayer!!.bat_selected > 0) {
                    view.visibility = GONE
                    if (!batsmenList!!.get(position).player_record!!.player_credit.isEmpty()) {
                        player_credit = batsmenList!!.get(position).player_record!!.player_credit.toDouble()
                    }
                    var extra = selectPlayer!!.extra_player
                    if (selectPlayer!!.bat_selected > selectPlayer!!.bat_mincount) {
                        extra = selectPlayer!!.extra_player + 1
                    }
                    var total_credit = selectPlayer!!.total_credit - player_credit
                    var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                    var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                    if (batsmenList!![position].team_id.equals(match!!.local_team_id))
                        localTeamplayerCount = selectPlayer!!.localTeamplayerCount - 1
                    else
                        visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount - 1


                    updateTeamData(
                        extra,
                        selectPlayer!!.wk_selected,
                        selectPlayer!!.bat_selected - 1,
                        selectPlayer!!.ar_selected,
                        selectPlayer!!.bowl_selected,
                        selectPlayer!!.selectedPlayer - 1,
                        localTeamplayerCount,
                        visitorTeamPlayerCount,
                        total_credit
                    )
                    batsmenList!!.get(position).isSelected = isSelected
                }
            }

        } else if (tag == BOWLER) {
            var player_credit = 0.0
            if (isSelected) {
                if (selectPlayer!!.bowl_selected < selectPlayer!!.bowl_maxcount) {
                    if (selectPlayer!!.selectedPlayer < 11) {
                        if (selectPlayer!!.bowl_selected < selectPlayer!!.bowl_mincount || selectPlayer!!.extra_player > 0) {
                            var extra = selectPlayer!!.extra_player
                            if (selectPlayer!!.bowl_selected >= selectPlayer!!.bowl_mincount) {
                                extra = selectPlayer!!.extra_player - 1
                            }
                            if (!bowlerList!!.get(position).player_record!!.player_credit.isEmpty()) {
                                player_credit = bowlerList!!.get(position).player_record!!.player_credit.toDouble()
                            }
                            var total_credit = selectPlayer!!.total_credit + player_credit
                            if (total_credit > 100) {
                                view.visibility = VISIBLE
                                return
                            }

                            var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                            var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                            if (bowlerList!![position].team_id.equals(match!!.local_team_id))
                                localTeamplayerCount = selectPlayer!!.localTeamplayerCount + 1
                            else
                                visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount + 1

                            updateTeamData(
                                extra,
                                selectPlayer!!.wk_selected,
                                selectPlayer!!.bat_selected,
                                selectPlayer!!.ar_selected,
                                selectPlayer!!.bowl_selected + 1,
                                selectPlayer!!.selectedPlayer + 1,
                                localTeamplayerCount,
                                visitorTeamPlayerCount,
                                total_credit
                            )
                            bowlerList!!.get(position).isSelected = isSelected
                        }
                    }
                }
            } else {
                if (selectPlayer!!.bowl_selected > 0) {
                    view.visibility = GONE
                    if (!bowlerList!!.get(position).player_record!!.player_credit.isEmpty()) {
                        player_credit = bowlerList!!.get(position).player_record!!.player_credit.toDouble()
                    }
                    var total_credit = selectPlayer!!.total_credit - player_credit
                    var extra = selectPlayer!!.extra_player
                    if (selectPlayer!!.bowl_selected > selectPlayer!!.bowl_mincount) {
                        extra = selectPlayer!!.extra_player + 1
                    }
                    var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                    var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                    if (bowlerList!![position].team_id.equals(match!!.local_team_id))
                        localTeamplayerCount = selectPlayer!!.localTeamplayerCount - 1
                    else
                        visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount - 1

                    updateTeamData(
                        extra,
                        selectPlayer!!.wk_selected,
                        selectPlayer!!.bat_selected,
                        selectPlayer!!.ar_selected,
                        selectPlayer!!.bowl_selected - 1,
                        selectPlayer!!.selectedPlayer - 1,
                        localTeamplayerCount,
                        visitorTeamPlayerCount,
                        total_credit
                    )
                    bowlerList!!.get(position).isSelected = isSelected
                }
            }

        }
        rv_Player.adapter!!.notifyDataSetChanged()
    }

    override fun onClickItem(tag: String, position: Int) {
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.img_wk -> {
                playerTypeSelector(WK)
            }
            R.id.img_ar -> {
                playerTypeSelector(AR)
            }
            R.id.img_bat -> {
                playerTypeSelector(BAT)
            }
            R.id.img_bowler -> {
                playerTypeSelector(BOWLER)
            }
            R.id.btn_Next -> {
                startActivity(
                    Intent(this, Choose_C_VC_Activity::class.java)
                        .putExtra(IntentConstant.CONTEST_ID, contest_id)
                        .putExtra(IntentConstant.MATCH, match)
                        .putExtra(IntentConstant.SELECT_PLAYER, selectPlayer)
                        .putParcelableArrayListExtra(IntentConstant.WK, wkList as java.util.ArrayList<out Parcelable>)
                        .putParcelableArrayListExtra(
                            IntentConstant.BATSMEN,
                            batsmenList as java.util.ArrayList<out Parcelable>
                        )
                        .putParcelableArrayListExtra(
                            IntentConstant.BOWLER,
                            bowlerList as java.util.ArrayList<out Parcelable>
                        )
                        .putParcelableArrayListExtra(IntentConstant.AR, arList as java.util.ArrayList<out Parcelable>)
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_team)
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countTimer != null)
            countTimer!!.stopUpdateTimer()
    }

    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var contest_id: String = ""
    var matchType = IntentConstant.FIXTURE
    private fun initViews() {
        if (intent != null) {
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            contest_id = intent.getStringExtra(IntentConstant.CONTEST_ID)
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
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.choose_team)
        setMenu(false, false, false, false)
        createTeamData()
        if (match != null)
            callGetContestListApi()
        img_wk.setOnClickListener(this)
        img_ar.setOnClickListener(this)
        img_bat.setOnClickListener(this)
        img_bowler.setOnClickListener(this)
        playerTypeSelector(WK)
        btn_Next.setOnClickListener(this)
        if (BuildConfig.APPLICATION_ID == "os.real11" || BuildConfig.APPLICATION_ID == "os.cashfantasy") {
            txt_substitute.visibility = VISIBLE
            txt_PickPlayer.gravity = Gravity.START
        }
    }

    var bowlerList: MutableList<Data>? = ArrayList()
    var arList: MutableList<Data>? = ArrayList()
    var wkList: MutableList<Data>? = ArrayList()
    var batsmenList: MutableList<Data>? = ArrayList()

    var playerList: MutableList<Data>? = ArrayList()
    private fun callGetContestListApi() {
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.match_id] = match!!.match_id
        loginRequest[Tags.series_id] = match!!.series_id
        loginRequest[Tags.local_team_id] = match!!.local_team_id
        loginRequest[Tags.visitor_team_id] = match!!.visitor_team_id

        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@ChooseTeamActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .getPlayerList(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@ChooseTeamActivity)
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
                    for (i in 0 until playerList!!.size) {
                        playerList!![i].isCaptain = false
                        playerList!![i].isViceCaptain = false
                        playerList!![i].isSelected = false
                        if (playerList!![i].player_role.contains("Wicketkeeper", true)) {
                            wkList!!.add(playerList!![i])
                        }
                    }
                    playerList!!.removeAll(wkList!!)
                    AppDelegate.LogT("Size==>" + playerList!!.size)
                    for (i in 0 until playerList!!.size) {
                        playerList!![i].isCaptain = false
                        playerList!![i].isViceCaptain = false
                        playerList!![i].isSelected = false
                        if (playerList!![i].player_role.contains("Bowler", true)) {
                            bowlerList!!.add(playerList!![i])
                        } else if (playerList!![i].player_role.contains("Batsman", true)) {
                            batsmenList!!.add(playerList!![i])
                        } else if (playerList!![i].player_role.contains("Allrounder", true)) {
                            arList!!.add(playerList!![i])
                        }
                    }
                    playerTypeSelector(WK)
                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@ChooseTeamActivity)
            }
        }
    }


    @SuppressLint("WrongConstant")
    private fun setAdapter(playerlist: MutableList<Data>, type: Int) {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Player!!.layoutManager = llm
        rv_Player!!.adapter = PlayerListAdapter(this, playerlist, type, this, selectPlayer)
    }

    private var WK = 1
    private var BAT = 2
    private var AR = 3
    private var BOWLER = 4
    var selectPlayer: SelectPlayer? = null
    fun createTeamData() {
        selectPlayer = SelectPlayer()
        selectPlayer!!.extra_player = 3
        selectPlayer!!.wk_count = 1
        selectPlayer!!.wk_selected = 0
        selectPlayer!!.bat_mincount = 3
        selectPlayer!!.bat_maxcount = 5
        selectPlayer!!.bat_selected = 0
        selectPlayer!!.ar_mincount = 1
        selectPlayer!!.ar_maxcount = 3
        selectPlayer!!.ar_selected = 0
        selectPlayer!!.bowl_mincount = 3
        selectPlayer!!.bowl_maxcount = 5
        selectPlayer!!.bowl_selected = 0
        selectPlayer!!.selectedPlayer = 0
        selectPlayer!!.localTeamplayerCount = 0
        selectPlayer!!.visitorTeamPlayerCount = 0
        selectPlayer!!.total_credit = 0.0
        selectPlayer!!.substitute = false
    }

    fun updateTeamData(
        extra_player: Int,
        wk_selected: Int,
        bat_selected: Int,
        ar_selected: Int,
        bowl_selected: Int,
        selectedPlayer: Int,
        localTeamplayerCount: Int,
        visitorTeamPlayerCount: Int,
        total_credit: Double
    ) {
        selectPlayer!!.extra_player = extra_player
        selectPlayer!!.wk_selected = wk_selected
        selectPlayer!!.bat_selected = bat_selected
        selectPlayer!!.ar_selected = ar_selected
        selectPlayer!!.bowl_selected = bowl_selected
        selectPlayer!!.selectedPlayer = selectedPlayer
        selectPlayer!!.localTeamplayerCount = localTeamplayerCount
        selectPlayer!!.visitorTeamPlayerCount = visitorTeamPlayerCount
        selectPlayer!!.total_credit = total_credit
        selectPlayer!!.substitute = false
        updateUi()
    }

    private fun updateUi() {
        txt_WKCount.text = selectPlayer!!.wk_selected.toString()
        txt_ARCount.text = selectPlayer!!.ar_selected.toString()
        txt_BOWLERCount.text = selectPlayer!!.bowl_selected.toString()
        txt_BATCount.text = selectPlayer!!.bat_selected.toString()
        btn_Next.isEnabled = selectPlayer!!.selectedPlayer == 11
        txt_player.text = selectPlayer!!.selectedPlayer.toString() + "/11"
        txt_credits.text = String.format("%.2f", 100 - selectPlayer!!.total_credit) + "/100"
        txt_local.text = match!!.local_team_name
        txt_local_count.text = selectPlayer!!.localTeamplayerCount.toString()
        txt_visitor_count.text = selectPlayer!!.visitorTeamPlayerCount.toString()
        txt_visitor.text = match!!.visitor_team_name

    }

    fun playerTypeSelector(value: Int) {
        txt_WK.isSelected = false
        txt_AR.isSelected = false
        txt_BAT.isSelected = false
        txt_BOWLER.isSelected = false

        img_wk.isSelected = false
        img_ar.isSelected = false
        img_bat.isSelected = false
        img_bowler.isSelected = false

        when (value) {
            WK -> {
                txt_WK.isSelected = true
                img_wk.isSelected = true
                txt_PickPlayer.setText(R.string.pick_1_wicket_keeper)
                setAdapter(wkList!!, WK)
            }
            BAT -> {
                txt_BAT.isSelected = true
                img_bat.isSelected = true
                txt_PickPlayer.setText(R.string.pick_3_5_batsmen)
                setAdapter(batsmenList!!, BAT)
            }
            AR -> {
                txt_AR.isSelected = true
                img_ar.isSelected = true
                txt_PickPlayer.setText(R.string.pick_1_3_allrounder)
                setAdapter(arList!!, AR)
            }
            BOWLER -> {
                img_bowler.isSelected = true
                txt_BOWLER.isSelected = true
                txt_PickPlayer.setText(R.string.pick_3_5_bowler)
                setAdapter(bowlerList!!, BOWLER)
            }

        }

    }
}