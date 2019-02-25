package os.com.ui.createTeam.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Gravity
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.amlcurran.showcaseview.OnShowcaseEventListener
import com.github.amlcurran.showcaseview.ShowcaseView
import com.github.amlcurran.showcaseview.targets.ViewTarget
import kotlinx.android.synthetic.main.activity_choose_team.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_choose_team.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.BuildConfig
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.AppRequestCodes.CREATE_CONTEST
import os.com.constant.AppRequestCodes.UPDATE_ACTIVITY
import os.com.constant.IntentConstant
import os.com.constant.PrefConstant
import os.com.constant.Tags
import os.com.interfaces.SelectPlayerInterface
import os.com.networkCall.ApiClient
import os.com.ui.createTeam.adapter.PlayerListAdapter
import os.com.ui.createTeam.apiResponse.SelectPlayer
import os.com.ui.createTeam.apiResponse.myTeamListResponse.Substitute
import os.com.ui.createTeam.apiResponse.playerListResponse.Data
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.dashboard.more.activity.WebViewActivity
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.set


class ChooseTeamActivity : BaseActivity(), View.OnClickListener, SelectPlayerInterface, OnShowcaseEventListener {

    var exeedCredit = false

    companion object {
        var chooseTeamActivity: ChooseTeamActivity? = null
    }

    override fun onClickItem(tag: Int, position: Int, isSelected: Boolean) {

        if (tag == WK) {
            var player_credit = 0.0
            if (isSelected) {

                if (selectPlayer!!.wk_selected < selectPlayer!!.wk_count) {
                    if (wkList!![position].team_id.equals(match!!.local_team_id)) {
                        if (selectPlayer!!.localTeamplayerCount >= 7)
                            return
                    } else {
                        if (selectPlayer!!.visitorTeamPlayerCount >= 7)
                            return
                    }

                    var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                    var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                    if (wkList!![position].team_id.equals(match!!.local_team_id))
                        localTeamplayerCount = selectPlayer!!.localTeamplayerCount + 1
                    else
                        visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount + 1

                    if (!wkList!!.get(position).player_record!!.player_credit.isEmpty()) {
                        player_credit = wkList!!.get(position).player_record!!.player_credit.toDouble()
                    }
                    var total_credit = selectPlayer!!.total_credit + player_credit
                    if (total_credit > 100) {
                        exeedCredit = true
                        rv_Player.adapter!!.notifyDataSetChanged()
                        showSnackBarView(toolbar,"You do not have enough credits to select this player.")
                        return
                    }

                    wkList!!.get(position).isSelected = isSelected
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
                    txt_substitute.setText("Substitute")
                }
            } else {
                if (selectPlayer!!.wk_selected > 0) {
                    view.visibility = GONE
                    if (!wkList!!.get(position).player_record!!.player_credit.isEmpty()) {
                        player_credit = wkList!!.get(position).player_record!!.player_credit.toDouble()
                    }
                    val total_credit = selectPlayer!!.total_credit - player_credit
                    var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                    var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                    if (wkList!![position].team_id.equals(match!!.local_team_id))
                        localTeamplayerCount = selectPlayer!!.localTeamplayerCount - 1
                    else
                        visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount - 1
                    wkList!!.get(position).isSelected = isSelected
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
                    txt_substitute.setText("Substitute")
                }
            }
        } else if (tag == AR) {
            var player_credit = 0.0
            if (isSelected) {
                if (arList!![position].team_id.equals(match!!.local_team_id)) {
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
                            if (!arList!!.get(position).player_record!!.player_credit.isEmpty()) {
                                player_credit = arList!!.get(position).player_record!!.player_credit.toDouble()
                            }
                            val total_credit = selectPlayer!!.total_credit + player_credit
                            if (total_credit > 100) {
                                exeedCredit = true
                                rv_Player.adapter!!.notifyDataSetChanged()
                                showSnackBarView(toolbar,"You do not have enough credits to select this player.")
                                return
                            }
                            var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                            var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                            if (arList!![position].team_id.equals(match!!.local_team_id))
                                localTeamplayerCount = selectPlayer!!.localTeamplayerCount + 1
                            else
                                visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount + 1
                            arList!!.get(position).isSelected = isSelected
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
                            txt_substitute.setText("Substitute")
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
                    arList!!.get(position).isSelected = isSelected
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
                    txt_substitute.setText("Substitute")
                }
            }
        } else if (tag == BAT) {
            var player_credit = 0.0
            if (isSelected) {
                if (batsmenList!![position].team_id.equals(match!!.local_team_id)) {
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
                            if (!batsmenList!!.get(position).player_record!!.player_credit.isEmpty()) {
                                player_credit = batsmenList!!.get(position).player_record!!.player_credit.toDouble()
                            }
                            var total_credit = selectPlayer!!.total_credit + player_credit
                            if (total_credit > 100) {
                                exeedCredit = true
                                rv_Player.adapter!!.notifyDataSetChanged()
                                showSnackBarView(toolbar,"You do not have enough credits to select this player.")
                                return
                            }
                            var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                            var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                            if (batsmenList!![position].team_id.equals(match!!.local_team_id))
                                localTeamplayerCount = selectPlayer!!.localTeamplayerCount + 1
                            else
                                visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount + 1

                            batsmenList!!.get(position).isSelected = isSelected
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
                            txt_substitute.setText("Substitute")
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

                    batsmenList!!.get(position).isSelected = isSelected
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
                    txt_substitute.setText("Substitute")
                }
            }

        } else if (tag == BOWLER) {
            var player_credit = 0.0
            if (isSelected) {
                if (bowlerList!![position].team_id.equals(match!!.local_team_id)) {
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
                            if (!bowlerList!!.get(position).player_record!!.player_credit.isEmpty()) {
                                player_credit = bowlerList!!.get(position).player_record!!.player_credit.toDouble()
                            }
                            var total_credit = selectPlayer!!.total_credit + player_credit
                            if (total_credit > 100) {
                                exeedCredit = true
                                rv_Player.adapter!!.notifyDataSetChanged()
                                showSnackBarView(toolbar,"You do not have enough credits to select this player.")
                                return
                            }

                            var localTeamplayerCount = selectPlayer!!.localTeamplayerCount
                            var visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount
                            if (bowlerList!![position].team_id.equals(match!!.local_team_id))
                                localTeamplayerCount = selectPlayer!!.localTeamplayerCount + 1
                            else
                                visitorTeamPlayerCount = selectPlayer!!.visitorTeamPlayerCount + 1
                            bowlerList!!.get(position).isSelected = isSelected
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
                            txt_substitute.setText("Substitute")
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

                    bowlerList!!.get(position).isSelected = isSelected
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
                    txt_substitute.setText("Substitute")
                }
            }

        }
        rv_Player.adapter!!.notifyDataSetChanged()
    }

    override fun onClickItem(tag: String, position: Int) {
    }

    var substituteDetail: Substitute? = null
    var contest_id = ""
    var team_id = ""
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.img_wk -> {
                playerTypeSelector(WK, wkList)
            }
            R.id.img_ar -> {
                playerTypeSelector(AR, arList)
            }
            R.id.img_bat -> {
                playerTypeSelector(BAT, batsmenList)
            }
            R.id.img_bowler -> {
                playerTypeSelector(BOWLER, bowlerList)
            }
            R.id.txt_substitute -> {
                addSubstituteData()
            }
            R.id.btn_Next -> {
                val remainingPlayer = 11 - selectPlayer!!.selectedPlayer
                if (BuildConfig.APPLICATION_ID == "os.real11" || BuildConfig.APPLICATION_ID == "os.cashfantasy") {
                    if (selectPlayer!!.selectedPlayer != 11)
                        showSnackBar(cl, "Pick " + remainingPlayer + " more player to complete your team.")
                    else if (!selectPlayer!!.substitute) {
                        showSnackBar(cl, "Please select substitute.")
                    } else {
                        gotoNext()
                    }
                } else {
                    if (selectPlayer!!.selectedPlayer != 11)
                        showSnackBar(cl, "Pick " + remainingPlayer + " more player to complete your team.")
                    else {
                        gotoNext()
                    }
                }
            }
        }
    }

    fun showSnackBar(view: View, msg: String) {
//        val snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
//        val view = snack.getView()
//        view.setBackgroundColor(ContextCompat.getColor(this, R.color.vicecaptainColor));
//        val params = view.getLayoutParams() as CoordinatorLayout.LayoutParams
//        params.gravity = Gravity.TOP
//        view.setLayoutParams(params)
//        snack.show()
        showSnackBarView(view,msg)
    }

    fun gotoNext() {
        var requestCode = UPDATE_ACTIVITY
        if (createOrJoin == CREATE_CONTEST)
            requestCode = CREATE_CONTEST
        startActivityForResult(
            Intent(this, Choose_C_VC_Activity::class.java)
                .putExtra(IntentConstant.MATCH, match)
                .putExtra(IntentConstant.CONTEST_TYPE, matchType)
                .putExtra(IntentConstant.SELECT_PLAYER, selectPlayer)
                .putExtra(IntentConstant.ISEDIT, from)
                .putExtra(IntentConstant.TEAM_ID, team_id)
                .putExtra(IntentConstant.CONTEST_ID, contest_id)
                .putExtra(IntentConstant.CREATE_OR_JOIN, createOrJoin)
                .putParcelableArrayListExtra(
                    IntentConstant.WK,
                    wkList as java.util.ArrayList<out Parcelable>
                )
                .putParcelableArrayListExtra(
                    IntentConstant.BATSMEN,
                    batsmenList as java.util.ArrayList<out Parcelable>
                )
                .putParcelableArrayListExtra(
                    IntentConstant.BOWLER,
                    bowlerList as java.util.ArrayList<out Parcelable>
                )
//                        .addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
                .putParcelableArrayListExtra(
                    IntentConstant.AR,
                    arList as java.util.ArrayList<out Parcelable>
                )
            , requestCode
        )
    }

    private fun addSubstituteData() {
        startActivityForResult(
            Intent(this, ChooseSubstituteActivity::class.java)
                .putExtra(IntentConstant.MATCH, match)
                .putExtra(IntentConstant.CONTEST_TYPE, matchType)
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
                .putParcelableArrayListExtra(IntentConstant.AR, arList as java.util.ArrayList<out Parcelable>),
            AppRequestCodes.SELECT_SUBSTITUTE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppRequestCodes.SELECT_SUBSTITUTE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val isSubstitute = data.getBooleanExtra("isSubstitute", false)
//                    if (isSubstitute) {
                    val substitute_id = data.getStringExtra("substitute_id")
                    for (i in wkList!!.indices) {
                        wkList!![i].isSubstitute = wkList!![i].player_id == substitute_id
                    }
                    for (i in bowlerList!!.indices) {
                        bowlerList!![i].isSubstitute = bowlerList!![i].player_id == substitute_id
                    }
                    for (i in batsmenList!!.indices) {
                        batsmenList!![i].isSubstitute = batsmenList!![i].player_id == substitute_id
                    }
                    for (i in arList!!.indices) {
                        arList!![i].isSubstitute = arList!![i].player_id == substitute_id
                    }
                    selectPlayer!!.substitute = true
                    selectPlayer!!.substitute_id = substitute_id
                    txt_substitute.setText("1 Substitute")
                    updateUi()
                }
            }
        } else if (requestCode == AppRequestCodes.CREATE_CONTEST && resultCode == Activity.RESULT_OK) {
            val intent = Intent()
            intent.putExtra(IntentConstant.TEAM_ID, data!!.getStringExtra(IntentConstant.TEAM_ID))
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else if (requestCode == AppRequestCodes.UPDATE_ACTIVITY || requestCode == AppRequestCodes.EDIT || requestCode == AppRequestCodes.CLONE)
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_team)
        chooseTeamActivity = this
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        chooseTeamActivity = null
        if (countTimer != null)
            countTimer!!.stopUpdateTimer()
    }

    //    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    var from = 0
    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.choose_team)
        if (BuildConfig.APPLICATION_ID == "os.real11")
            setMenu(false, false, false, false, false)
        else
            setMenu(false, false, false, false, true)
        getIntentData()
        if (NetworkUtils.isConnected()) {
            if (match != null)
                callGetContestListApi()
        } else
            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()


        img_wk.setOnClickListener(this)
        img_ar.setOnClickListener(this)
        img_bat.setOnClickListener(this)
        img_bowler.setOnClickListener(this)
        playerTypeSelector(WK, wkList)
        btn_Next.setOnClickListener(this)
        if (BuildConfig.APPLICATION_ID == "os.real11" || BuildConfig.APPLICATION_ID == "os.cashfantasy") {
            txt_substitute.visibility = VISIBLE
            txt_substitute.isEnabled = false
            txt_PickPlayer.gravity = Gravity.START
        }

    }

    var counterValue = 0
    override fun onShowcaseViewShow(showcaseView: ShowcaseView?) {
    }

    override fun onShowcaseViewHide(showcaseView: ShowcaseView?) {
        when (counterValue) {
            1 -> callIntroductionScreen(
                R.id.ll_Credit,
                "Credit Counter",
                "Use 100 credits to pick your players", ShowcaseView.ABOVE_SHOWCASE
            )
            2 -> callIntroductionScreen(
                R.id.ll_players,
                "Player Counter",
                "Pick 11 players to create your team"
                ,
                ShowcaseView.ABOVE_SHOWCASE
            )
            3 -> callIntroductionScreen(
                R.id.ll_Credits,
                "Player's Credits",
                "Cost of adding a player to your team"
                ,
                ShowcaseView.BELOW_SHOWCASE
            )
//            4 -> callIntroductionScreen(
//                R.id.ll_players,
//                "Player Counter",
//                "Pick 11 players to create your team"
//                ,
//                ShowcaseView.ABOVE_SHOWCASE
//            )
        }

    }

    override fun onShowcaseViewDidHide(showcaseView: ShowcaseView?) {
    }

    override fun onShowcaseViewTouchBlocked(motionEvent: MotionEvent?) {
    }

    fun callIntroductionScreen(
        target: Int,
        title: String,
        description: String,
        abovE_SHOWCASE: Int
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            var scv = ShowcaseView.Builder(this@ChooseTeamActivity)
                .withMaterialShowcase()
                .setTarget(ViewTarget(target, this@ChooseTeamActivity))
                .setContentTitle(title)
                .setContentText(description)
                .setStyle(R.style.CustomShowcaseTheme)
                .setShowcaseEventListener(this@ChooseTeamActivity)

            counterValue = counterValue + 1
            var buil = scv.build()
            buil.hideButton()
//            buil.animation = null
            buil.forceTextPosition(abovE_SHOWCASE)

            delay(2500)
            buil.hide()

        }
    }

    var localTeamName = ""
    var visitorTeamName = ""
    var createOrJoin = AppRequestCodes.CREATE
    private fun getIntentData() {
        if (intent != null) {
            createOrJoin = intent.getIntExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.CREATE)
            if (createOrJoin == AppRequestCodes.JOIN)
                contest_id = intent.getStringExtra(IntentConstant.CONTEST_ID)
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
            from = intent.getIntExtra(IntentConstant.ISEDIT, 0)
            if (from == AppRequestCodes.CLONE || from == AppRequestCodes.EDIT) {
                playerListPreview = intent.getParcelableExtra(IntentConstant.DATA)
                playerListEdit = intent.getParcelableArrayListExtra(IntentConstant.SELECT_PLAYER)
                substituteDetail = intent.getParcelableExtra("substitute")
            }
            localTeamName = match!!.local_team_name
            visitorTeamName = match!!.visitor_team_name
            if (match!!.local_team_name.length > 5)
                localTeamName = match!!.local_team_name.substring(0, 4)
            if (match!!.visitor_team_name.length > 5)
                visitorTeamName = match!!.visitor_team_name.substring(0, 4)

            txt_matchVS.text = localTeamName + " " + getString(R.string.vs) + " " + visitorTeamName
            if (matchType == IntentConstant.FIXTURE) {
                if (!match!!.star_date.isEmpty()) {
                    val strt_date = match!!.star_date.split("T")
                    val dateTime = strt_date.get(0) + " " + match!!.star_time
                    countTimer!!.startUpdateTimer(this, dateTime, txt_CountDownTimer)
                }
            } else if (matchType == IntentConstant.COMPLETED) {
                txt_CountDownTimer.setText(getString(R.string.completed))
            } else {
                txt_CountDownTimer.setText(getString(R.string.in_progress))
                txt_CountDownTimer.setTextColor(resources.getColor(R.color.dark_yellow))
            }
        }
        createTeamData()
    }

    var bowlerList: MutableList<Data>? = ArrayList()
    var arList: MutableList<Data>? = ArrayList()
    var wkList: MutableList<Data>? = ArrayList()
    var batsmenList: MutableList<Data>? = ArrayList()

    var playerList: MutableList<Data>? = ArrayList()

    var playerListPreview: os.com.ui.createTeam.apiResponse.myTeamListResponse.Data? = null
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
                    AppDelegate.LogT("PlayerList=>" + playerList!!.size)
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
                        if (playerList!![i].player_record!!.playing_role.contains("Wicketkeeper", true)) {
                            wkList!!.add(playerList!![i])
                        }
                    }
                    playerList!!.removeAll(wkList!!)
                    AppDelegate.LogT("Size==>" + playerList!!.size)
                    for (i in 0 until playerList!!.size) {
                        playerList!![i].isCaptain = false
                        playerList!![i].isViceCaptain = false
                        playerList!![i].isSelected = false
                        if (playerList!![i].player_record!!.playing_role.contains("Bowler", true)) {
                            bowlerList!!.add(playerList!![i])
                        } else if (playerList!![i].player_record!!.playing_role.contains("Batsman", true)) {
                            batsmenList!!.add(playerList!![i])
                        } else if (playerList!![i].player_record!!.playing_role.contains("Allrounder", true)) {
                            arList!!.add(playerList!![i])
                        }
                    }
                    if (from == AppRequestCodes.CLONE || from == AppRequestCodes.EDIT)
                        updateData()
                    else playerTypeSelector(WK, wkList)

                    if (!pref!!.getBooleanValuefromTemp(PrefConstant.SKIP_CREATETEAM_INSTRUCTION, false)) {
                        pref!!.putBooleanValueinTemp(PrefConstant.SKIP_CREATETEAM_INSTRUCTION, true)
                        callIntroductionScreen(
                            R.id.img_bowler,
                            "Player Category",
                            "Select a balanced team to help you win",
                            ShowcaseView.BELOW_SHOWCASE
                        )
                    }
                } else {
                    logoutIfDeactivate(response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@ChooseTeamActivity)
            }
        }
    }

    private fun updateData() {
        if (from == AppRequestCodes.EDIT)
            team_id = playerListPreview!!.teamid
        var localTeamplayerCount = 0
        var visitorTeamPlayerCount = 0
        var total_credit = 0.0
        var totalWk = 0
        var totalAR = 0
        var totalBat = 0
        var totalBowler = 0
        for (position in wkList!!.indices) {
            for (playerData in playerListEdit!!) {
                if (wkList!![position].player_id == playerData.player_id) {
                    wkList!![position].isSelected = true
                    if (wkList!![position].player_id == playerListPreview!!.captain_player_id)
                        wkList!![position].isCaptain = true
                    if (wkList!![position].player_id == playerListPreview!!.vice_captain_player_id)
                        wkList!![position].isViceCaptain = true
                    if (wkList!![position].team_id.equals(match!!.local_team_id))
                        localTeamplayerCount = localTeamplayerCount + 1
                    else
                        visitorTeamPlayerCount = visitorTeamPlayerCount + 1

                    var player_credit = 0.0
                    if (!wkList!!.get(position).player_record!!.player_credit.isEmpty()) {
                        player_credit = wkList!!.get(position).player_record!!.player_credit.toDouble()
                    }
                    total_credit = total_credit + player_credit
                    totalWk = totalWk + 1
                }

                if (BuildConfig.APPLICATION_ID == "os.real11" || BuildConfig.APPLICATION_ID == "os.cashfantasy") {
                    if (substituteDetail != null)
                        if (wkList!![position].player_id.equals(substituteDetail!!.player_id)) {
                            wkList!![position].isSubstitute = true
                            selectPlayer!!.substitute = true
                            selectPlayer!!.substitute_id = substituteDetail!!.player_id
                            txt_substitute.setText("1 Substitute")
                        }

                }
            }

        }
        for (position in arList!!.indices) {
            for (playerData in playerListEdit!!) {
                if (arList!![position].player_id == playerData.player_id) {
                    arList!![position].isSelected = true
                    if (arList!![position].player_id == playerListPreview!!.captain_player_id)
                        arList!![position].isCaptain = true
                    if (arList!![position].player_id == playerListPreview!!.vice_captain_player_id)
                        arList!![position].isViceCaptain = true
                    if (arList!![position].team_id.equals(match!!.local_team_id))
                        localTeamplayerCount = localTeamplayerCount + 1
                    else
                        visitorTeamPlayerCount = visitorTeamPlayerCount + 1

                    var player_credit = 0.0
                    if (!arList!!.get(position).player_record!!.player_credit.isEmpty()) {
                        player_credit = arList!!.get(position).player_record!!.player_credit.toDouble()
                    }
                    total_credit = total_credit + player_credit
                    totalAR = totalAR + 1
                }

                if (BuildConfig.APPLICATION_ID == "os.real11" || BuildConfig.APPLICATION_ID == "os.cashfantasy") {
                    if (substituteDetail != null)
                        if (arList!![position].player_id.equals(substituteDetail!!.player_id)) {
                            arList!![position].isSubstitute = true
                            selectPlayer!!.substitute = true
                            selectPlayer!!.substitute_id = substituteDetail!!.player_id
                            txt_substitute.setText("1 Substitute")
                        }
                }
            }

        }
        for (position in batsmenList!!.indices) {
            for (playerData in playerListEdit!!) {
                if (batsmenList!![position].player_id == playerData.player_id) {
                    batsmenList!![position].isSelected = true
                    if (batsmenList!![position].player_id == playerListPreview!!.captain_player_id)
                        batsmenList!![position].isCaptain = true
                    if (batsmenList!![position].player_id == playerListPreview!!.vice_captain_player_id)
                        batsmenList!![position].isViceCaptain = true
                    if (batsmenList!![position].team_id.equals(match!!.local_team_id))
                        localTeamplayerCount = localTeamplayerCount + 1
                    else
                        visitorTeamPlayerCount = visitorTeamPlayerCount + 1


                    var player_credit = 0.0
                    if (!batsmenList!!.get(position).player_record!!.player_credit.isEmpty()) {
                        player_credit = batsmenList!!.get(position).player_record!!.player_credit.toDouble()
                    }
                    total_credit = total_credit + player_credit
                    totalBat = totalBat + 1
                }

                if (BuildConfig.APPLICATION_ID == "os.real11" || BuildConfig.APPLICATION_ID == "os.cashfantasy") {
                    if (substituteDetail != null)
                        if (batsmenList!![position].player_id.equals(substituteDetail!!.player_id)) {
                            batsmenList!![position].isSubstitute = true
                            selectPlayer!!.substitute = true
                            selectPlayer!!.substitute_id = substituteDetail!!.player_id
                            txt_substitute.setText("1 Substitute")
                        }
                }
            }

        }
        for (position in bowlerList!!.indices) {
            for (playerData in playerListEdit!!) {
                if (bowlerList!![position].player_id == playerData.player_id) {
                    bowlerList!![position].isSelected = true
                    if (bowlerList!![position].player_id == playerListPreview!!.captain_player_id)
                        bowlerList!![position].isCaptain = true
                    if (bowlerList!![position].player_id == playerListPreview!!.vice_captain_player_id)
                        bowlerList!![position].isViceCaptain = true
                    if (bowlerList!![position].team_id.equals(match!!.local_team_id))
                        localTeamplayerCount = localTeamplayerCount + 1
                    else
                        visitorTeamPlayerCount = visitorTeamPlayerCount + 1

                    var player_credit = 0.0
                    if (!bowlerList!!.get(position).player_record!!.player_credit.isEmpty()) {
                        player_credit = bowlerList!!.get(position).player_record!!.player_credit.toDouble()
                    }
                    total_credit = total_credit + player_credit
                    totalBowler = totalBowler + 1
                }

                if (BuildConfig.APPLICATION_ID == "os.real11" || BuildConfig.APPLICATION_ID == "os.cashfantasy") {
                    if (substituteDetail != null)
                        if (bowlerList!![position].player_id.equals(substituteDetail!!.player_id)) {
                            bowlerList!![position].isSubstitute = true
                            selectPlayer!!.substitute = true
                            selectPlayer!!.substitute_id = substituteDetail!!.player_id
                            txt_substitute.setText("1 Substitute")
                        }
                }

            }
        }
        updateTeamData(
            0,
            totalWk,
            totalBat,
            totalAR,
            totalBowler,
            11,
            localTeamplayerCount,
            visitorTeamPlayerCount,
            total_credit
        )
        playerTypeSelector(WK, wkList)
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(playerlist: MutableList<Data>, type: Int) {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Player!!.layoutManager = llm
        rv_Player!!.adapter = PlayerListAdapter(
            this,
            playerlist,
            type,
            this,
            selectPlayer,
            localTeamName,
            visitorTeamName,
            match!!.local_team_id,
            match!!.visitor_team_id
        )
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
        updateUi()
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
        exeedCredit = false
        selectPlayer!!.extra_player = extra_player
        selectPlayer!!.wk_selected = wk_selected
        selectPlayer!!.bat_selected = bat_selected
        selectPlayer!!.ar_selected = ar_selected
        selectPlayer!!.bowl_selected = bowl_selected
        selectPlayer!!.selectedPlayer = selectedPlayer
        selectPlayer!!.localTeamplayerCount = localTeamplayerCount
        selectPlayer!!.visitorTeamPlayerCount = visitorTeamPlayerCount
        selectPlayer!!.total_credit = total_credit
        updateUi()
    }

    private fun updateUi() {
        txt_WKCount.text = selectPlayer!!.wk_selected.toString()
        txt_ARCount.text = selectPlayer!!.ar_selected.toString()
        txt_BOWLERCount.text = selectPlayer!!.bowl_selected.toString()
        txt_BATCount.text = selectPlayer!!.bat_selected.toString()
        if (BuildConfig.APPLICATION_ID == "os.real11" || BuildConfig.APPLICATION_ID == "os.cashfantasy") {
            txt_substitute.isEnabled = selectPlayer!!.selectedPlayer == 11
            txt_substitute.setOnClickListener(this)
            if (selectPlayer!!.selectedPlayer == 11 && selectPlayer!!.substitute)
                btn_Next.setBackgroundResource(R.drawable.button_selected)
            else
                btn_Next.setBackgroundResource(R.drawable.button_backgroundteam)
        } else {
            if (selectPlayer!!.selectedPlayer == 11)
                btn_Next.setBackgroundResource(R.drawable.button_selected)
            else
                btn_Next.setBackgroundResource(R.drawable.button_backgroundteam)
//            btn_Next.isActivated = selectPlayer!!.selectedPlayer == 11
        }
        txt_player.text = selectPlayer!!.selectedPlayer.toString() + "/11"
        txt_credits.text = String.format("%.2f", 100 - selectPlayer!!.total_credit) + "/100"
        txt_local.text = match!!.local_team_name
        txt_local_count.text = selectPlayer!!.localTeamplayerCount.toString()
        txt_visitor_count.text = selectPlayer!!.visitorTeamPlayerCount.toString()
        txt_visitor.text = match!!.visitor_team_name
        updateCircle()
    }

    private fun updateCircle() {
        txt_WKCount.isSelected = selectPlayer!!.wk_selected == selectPlayer!!.wk_count
        AppDelegate.LogT("Select Player==>+" + selectPlayer)
        if (selectPlayer!!.selectedPlayer == 11) {
            txt_ARCount.setBackgroundResource(R.drawable.count_selector)
            txt_BATCount.setBackgroundResource(R.drawable.count_selector)
            txt_BOWLERCount.setBackgroundResource(R.drawable.count_selector)
            txt_BOWLERCount.isSelected = true
            txt_ARCount.isSelected = true
            txt_BATCount.isSelected = true
            txt_WKCount.isSelected = true
        } else {
            txt_ARCount.setBackgroundResource(R.drawable.count_selector)
            txt_BATCount.setBackgroundResource(R.drawable.count_selector)
            txt_BOWLERCount.setBackgroundResource(R.drawable.count_selector)
            when {
                selectPlayer!!.ar_selected == 0 -> txt_ARCount.isSelected = false
                selectPlayer!!.ar_selected == selectPlayer!!.ar_maxcount -> txt_ARCount.isSelected = true
                selectPlayer!!.ar_selected >= selectPlayer!!.ar_mincount && selectPlayer!!.extra_player == 0 -> txt_ARCount.isSelected =
                        true
                selectPlayer!!.ar_selected > 0 -> {
                    txt_ARCount.isSelected = true
                    txt_ARCount.setBackgroundResource(R.drawable.rounded_square_count_update)
                }
                else -> txt_ARCount.isSelected = false
            }

            when {
                selectPlayer!!.bat_selected == 0 -> txt_BATCount.isSelected = false
                selectPlayer!!.bat_selected == selectPlayer!!.bat_maxcount -> txt_BATCount.isSelected = true
                selectPlayer!!.bat_selected >= selectPlayer!!.bat_mincount && selectPlayer!!.extra_player == 0 -> txt_BATCount.isSelected =
                        true
                selectPlayer!!.bat_selected > 0 -> {
                    txt_BATCount.isSelected = true
                    txt_BATCount.setBackgroundResource(R.drawable.rounded_square_count_update)
                }
                else -> txt_BATCount.isSelected = false
            }

            when {
                selectPlayer!!.bowl_selected == 0 -> txt_BOWLERCount.isSelected = false
                selectPlayer!!.bowl_selected == selectPlayer!!.bowl_maxcount -> txt_BOWLERCount.isSelected = true
                selectPlayer!!.bowl_selected >= selectPlayer!!.bowl_mincount && selectPlayer!!.extra_player == 0 -> txt_BOWLERCount.isSelected =
                        true
                selectPlayer!!.bowl_selected > 0 -> {
                    txt_BOWLERCount.isSelected = true
                    txt_BOWLERCount.setBackgroundResource(R.drawable.rounded_square_count_update)
                }
                else -> txt_BOWLERCount.isSelected = false
            }
        }
    }

    fun playerTypeSelector(value: Int, arrayList: MutableList<Data>?) {
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
                setAdapter(arrayList!!, WK)
            }
            BAT -> {
                txt_BAT.isSelected = true
                img_bat.isSelected = true
                txt_PickPlayer.setText(R.string.pick_3_5_batsmen)
                setAdapter(arrayList!!, BAT)
            }
            AR -> {
                txt_AR.isSelected = true
                img_ar.isSelected = true
                txt_PickPlayer.setText(R.string.pick_1_3_allrounder)
                setAdapter(arrayList!!, AR)
            }
            BOWLER -> {
                img_bowler.isSelected = true
                txt_BOWLER.isSelected = true
                txt_PickPlayer.setText(R.string.pick_3_5_bowler)
                setAdapter(arrayList!!, BOWLER)
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_guru -> {
                if (!match!!.guru_url.isEmpty()) {
                    val intent = Intent(this, WebViewActivity::class.java)
                    intent.putExtra("PAGE_SLUG", getString(R.string.guru))
                    intent.putExtra("URL", match!!.guru_url)
                    startActivity(intent)
                } else {
                    val viewf = findViewById<View>(R.id.menu_guru)
//                    val snack = Snackbar.make(viewf, "Guru advice for this round is coming soon!", Snackbar.LENGTH_LONG)
//                    val view = snack.getView()
//                    view.setBackgroundColor(ContextCompat.getColor(this, R.color.vicecaptainColor));
//                    val params = view.getLayoutParams() as CoordinatorLayout.LayoutParams
//                    params.gravity = Gravity.TOP
//                    view.setLayoutParams(params)
//                    snack.show()
                    showSnackBar(viewf,"Guru advice for this round is coming soon!")
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}