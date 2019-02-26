package os.com.ui.createTeam.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_player_detail.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_playerdetail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.createTeam.adapter.PlayerDetailFantasyStatusAdapter
import os.com.ui.createTeam.apiResponse.PlayerdetailResponse
import os.com.ui.createTeam.apiResponse.SelectPlayer
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*


class PlayerDetailActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_add_or_remove -> {
                add_or_remove()
            }
        }
    }

    private var WK = 1
    private var BAT = 2
    private var AR = 3
    private var BOWLER = 4
    fun add_or_remove() {
        if (type == WK.toString()) {
            var player_credit = playerCredit
            if (!is_in_team) {
                if (selectPlayer!!.wk_selected < selectPlayer!!.wk_count) {
                    if (team_id.equals(match!!.local_team_id)) {
                        if (selectPlayer!!.localTeamplayerCount >= 7)
                            return
                    } else {
                        if (selectPlayer!!.visitorTeamPlayerCount >= 7)
                            return
                    }
                    var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                    var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                    if (team_id.equals(match!!.local_team_id))
                        localTeamplayerCount = selectPlayer!!.localTeamplayerCount + 1
                    else
                        visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount + 1

                    var total_credit = selectPlayer!!.total_credit + player_credit
                    if (total_credit > 100) {
                        if (ChooseTeamActivity!=null)
                            ChooseTeamActivity.exeedCredit=true
                        showSnackBarView(toolbar_layout, "You do not have enough credits to select this player.")
                        return
                    }

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
                    selectPlayer!!.substitute = false
                    selectPlayer!!.substitute_id = ""
                }else{
                    showSnackBarView(toolbar_layout,"You can add only one Wicket-Keeper")
                }
            } else {
                if (selectPlayer!!.wk_selected > 0) {
                    val total_credit = selectPlayer!!.total_credit - player_credit
                    var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                    var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                    if (team_id.equals(match!!.local_team_id))
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
                    selectPlayer!!.substitute = false
                    selectPlayer!!.substitute_id = ""
                }
            }
        } else if (type == AR.toString()) {
            val player_credit = playerCredit
            if (!is_in_team) {
                if (team_id.equals(match!!.local_team_id)) {
                    if (selectPlayer!!.localTeamplayerCount >= 7)
                        return
                } else {
                    if (selectPlayer!!.visitorTeamPlayerCount >= 7)
                        return
                }
                if (selectPlayer!!.ar_selected < selectPlayer!!.ar_maxcount) {
                    if (selectPlayer!!.selectedPlayer < 11) {
                        if (selectPlayer!!.ar_selected < selectPlayer!!.ar_mincount || selectPlayer!!.extra_player > 0) {
                            var extra = selectPlayer!!.extra_player
                            if (selectPlayer!!.ar_selected >= selectPlayer!!.ar_mincount) {
                                extra = selectPlayer!!.extra_player - 1
                            }
                            val total_credit = selectPlayer!!.total_credit + player_credit
                            if (total_credit > 100) {
                                if (ChooseTeamActivity!=null)
                                    ChooseTeamActivity.exeedCredit=true
                                showSnackBarView(toolbar_layout, "You do not have enough credits to select this player.")
                                return
                            }
                            var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                            var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                            if (team_id.equals(match!!.local_team_id))
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
                            selectPlayer!!.substitute = false
                            selectPlayer!!.substitute_id = ""
                        }
                    }
                }else{
                    showSnackBarView(toolbar_layout,"You can add 1-3 All- Rounder.")
                }
            } else {
                if (selectPlayer!!.ar_selected > 0) {

                    var total_credit = selectPlayer!!.total_credit - player_credit
                    var extra = selectPlayer!!.extra_player
                    if (selectPlayer!!.ar_selected > selectPlayer!!.ar_mincount) {
                        extra = selectPlayer!!.extra_player + 1
                    }
                    var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                    var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                    if (team_id.equals(match!!.local_team_id))
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
                    selectPlayer!!.substitute = false
                    selectPlayer!!.substitute_id = ""
                }
            }
        } else if (type == BAT.toString()) {
            var player_credit = playerCredit
            if (!is_in_team) {
                if (team_id.equals(match!!.local_team_id)) {
                    if (selectPlayer!!.localTeamplayerCount >= 7)
                        return
                } else {
                    if (selectPlayer!!.visitorTeamPlayerCount >= 7)
                        return
                }
                if (selectPlayer!!.bat_selected < selectPlayer!!.bat_maxcount) {
                    if (selectPlayer!!.selectedPlayer < 11) {
                        if (selectPlayer!!.bat_selected < selectPlayer!!.bat_mincount || selectPlayer!!.extra_player > 0) {
                            var extra = selectPlayer!!.extra_player
                            if (selectPlayer!!.bat_selected >= selectPlayer!!.bat_mincount) {
                                extra = selectPlayer!!.extra_player - 1
                            }
                            var total_credit = selectPlayer!!.total_credit + player_credit
                            if (total_credit > 100) {
                                if (ChooseTeamActivity!=null)
                                    ChooseTeamActivity.exeedCredit=true
                                showSnackBarView(toolbar_layout, "You do not have enough credits to select this player.")
                                return
                            }
                            var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                            var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                            if (team_id.equals(match!!.local_team_id))
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
                            selectPlayer!!.substitute = false
                            selectPlayer!!.substitute_id = ""
                        }
                    }
                }else{
                    showSnackBarView(toolbar_layout,"You can add 3-5 Batsmen.")
                }
            } else {
                if (selectPlayer!!.bat_selected > 0) {

                    var extra = selectPlayer!!.extra_player
                    if (selectPlayer!!.bat_selected > selectPlayer!!.bat_mincount) {
                        extra = selectPlayer!!.extra_player + 1
                    }
                    var total_credit = selectPlayer!!.total_credit - player_credit
                    var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                    var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                    if (team_id.equals(match!!.local_team_id))
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
                    selectPlayer!!.substitute = false
                    selectPlayer!!.substitute_id = ""
                }
            }

        } else if (type == BOWLER.toString()) {
            var player_credit = playerCredit
            if (!is_in_team) {
                if (team_id.equals(match!!.local_team_id)) {
                    if (selectPlayer!!.localTeamplayerCount >= 7)
                        return
                } else {
                    if (selectPlayer!!.visitorTeamPlayerCount >= 7)
                        return
                }
                if (selectPlayer!!.bowl_selected < selectPlayer!!.bowl_maxcount) {
                    if (selectPlayer!!.selectedPlayer < 11) {
                        if (selectPlayer!!.bowl_selected < selectPlayer!!.bowl_mincount || selectPlayer!!.extra_player > 0) {
                            var extra = selectPlayer!!.extra_player
                            if (selectPlayer!!.bowl_selected >= selectPlayer!!.bowl_mincount) {
                                extra = selectPlayer!!.extra_player - 1
                            }
                            val total_credit = selectPlayer!!.total_credit + player_credit
                            if (total_credit > 100) {
                                if (ChooseTeamActivity!=null)
                                    ChooseTeamActivity.exeedCredit=true
                                showSnackBarView(toolbar_layout, "You do not have enough credits to select this player.")
                                return
                            }

                            var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                            var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                            if (team_id.equals(match!!.local_team_id))
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
                            selectPlayer!!.substitute = false
                            selectPlayer!!.substitute_id = ""
                        }
                    }
                }else{
                    showSnackBarView(toolbar_layout,"You can add 3-5 Bowlers.")
                }
            } else {
                if (selectPlayer!!.bowl_selected > 0) {
                    val total_credit = selectPlayer!!.total_credit - player_credit
                    var extra = selectPlayer!!.extra_player
                    if (selectPlayer!!.bowl_selected > selectPlayer!!.bowl_mincount) {
                        extra = selectPlayer!!.extra_player + 1
                    }
                    var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                    var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                    if (team_id.equals(match!!.local_team_id))
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
                    selectPlayer!!.substitute = false
                    selectPlayer!!.substitute_id = ""
                }
            }
        }
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
        if (ChooseTeamActivity!=null)
            ChooseTeamActivity.exeedCredit=false
        selectPlayer!!.extra_player = extra_player
        selectPlayer!!.wk_selected = wk_selected
        selectPlayer!!.bat_selected = bat_selected
        selectPlayer!!.ar_selected = ar_selected
        selectPlayer!!.bowl_selected = bowl_selected
        selectPlayer!!.selectedPlayer = selectedPlayer
        selectPlayer!!.localTeamplayerCount = localTeamplayerCount
        selectPlayer!!.visitorTeamPlayerCount = visitorTeamPlayerCount
        selectPlayer!!.total_credit = total_credit

        val intent = Intent()
        intent.putExtra("position", position)
        intent.putExtra(IntentConstant.TYPE, type)
        intent.putExtra(IntentConstant.SELECT_PLAYER, selectPlayer)
        intent.putExtra(IntentConstant.is_in_team, is_in_team)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    var playerCredit = 0.0
    var team_id = ""
    var match: Match? = null
    var ADD_REMOVE_PLAYER = false
    private var seriesId = ""
    private var playerId = ""
    var selectPlayer: SelectPlayer? = null
    var type = ""
    var is_in_team = false
    var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)
        initViews()
        try {
            if (intent.hasExtra("series_id"))
                seriesId = intent.getStringExtra("series_id")
            if (intent.hasExtra("player_id"))
                playerId = intent.getStringExtra("player_id")

            ADD_REMOVE_PLAYER = intent.getBooleanExtra(IntentConstant.ADD_REMOVE_PLAYER, false)
            if (ADD_REMOVE_PLAYER) {
                selectPlayer = intent.getParcelableExtra(IntentConstant.SELECT_PLAYER)
                type = intent.getStringExtra(IntentConstant.TYPE)
                is_in_team = intent.getBooleanExtra(IntentConstant.is_in_team, false)
                team_id = intent.getStringExtra(IntentConstant.TEAM_ID)
                position = intent.getIntExtra("position", 0)
                playerCredit = intent.getDoubleExtra("player_credit", 0.0)
                match = intent.getParcelableExtra(IntentConstant.MATCH)
                if (is_in_team)
                    btn_add_or_remove.setText("Remove From My Team")
                else
                    btn_add_or_remove.setText("Add To My Team")
            }

            if (ADD_REMOVE_PLAYER)
                btn_add_or_remove.visibility = VISIBLE
            else
                btn_add_or_remove.visibility = GONE

            if (NetworkUtils.isConnected()) {
                if (seriesId != null && !seriesId.equals("") && playerId != null && !playerId.equals(""))
                    getPlayerDetailData(seriesId, playerId)
            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()

            btn_add_or_remove.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.player_name)
            setMenu(false, false, false, false, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var player_credit = 0.0
    private fun getPlayerDetailData(series_id: String, playerId: String) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@PlayerDetailActivity)
                try {
                    var map = HashMap<String, String>()
                    if (pref!!.isLogin)
                        map[Tags.user_id] = pref!!.userdata!!.user_id
                    else
                        map[Tags.user_id] = ""
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.series_id] = series_id
                    map[Tags.player_id] = playerId
                    val request = ApiClient.client
                        .getRetrofitService()
                        .seriesPlayerDetail(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@PlayerDetailActivity)
                    if (response.response!!.status) {
                        if (response.response!!.data != null)
                            setData(response.response!!.data)
                    } else {
                        logoutIfDeactivate(response.response!!.message)
                        if (response.response!!.message != null)
                            AppDelegate.showToast(this@PlayerDetailActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@PlayerDetailActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setData(data: PlayerdetailResponse.ResponseBean.DataBean) {
        try {
            rl.visibility = View.VISIBLE
            ll_level.visibility = View.VISIBLE
            ll_about.visibility = View.VISIBLE
            if (data.player_image != null && data.player_image != "")
                ImageLoader.getInstance().displayImage(
                    data.player_image,
                    imvPlayer,
                    FantasyApplication.getInstance().options
                )
            if (data.player_name != null)
                toolbarTitleTv.setText(data.player_name)
//                txt_PlayerName.setText(data.player_name)
            if (data.player_credit != null)
                txtCredits.setText(data.player_credit)
            if (data.player_total_points != null)
                txt_totalPoints.setText(data.player_total_points)
            if (data.bats_type != null)
                txtBatsType.setText(data.bats_type)
            if (data.bowls_type != null)
                txtBowlsType.setText(data.bowls_type)
            if (data.nationality != null)
                txtNationality.setText(data.nationality)
            if (data.birthday != null)
                txtBirthday.setText(data.birthday)

            if (data.match_detail != null && data.match_detail.size > 0) {
                txt_FantasyStatus.visibility = View.VISIBLE
                ll_AllContest.visibility = View.VISIBLE
                setAdapter(data.match_detail)
                txt_NotFoundData.visibility= GONE
            }else   txt_NotFoundData.visibility= VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(match_detail: MutableList<PlayerdetailResponse.ResponseBean.DataBean.MatchDetailBean>) {
        try {
            val llm = LinearLayoutManager(this)
            llm.orientation = LinearLayoutManager.VERTICAL
            rv_FantasyStatus!!.layoutManager = llm
            rv_FantasyStatus!!.adapter = PlayerDetailFantasyStatusAdapter(this, match_detail)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}