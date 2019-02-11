package os.com.ui.createTeam.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_choose_substitute.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.interfaces.SelectPlayerInterface
import os.com.ui.createTeam.adapter.PlayerListSubstituteAdapter
import os.com.ui.createTeam.apiResponse.SelectPlayer
import os.com.ui.createTeam.apiResponse.playerListResponse.Data
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.CountTimer

class ChooseSubstituteActivity : BaseActivity(), View.OnClickListener, SelectPlayerInterface {

    override fun onClickItem(tag: Int, position: Int, isSelected: Boolean) {
        if (tag == 5) {
            for (i in playerList!!.indices)
                playerList!![i].isSubstitute = false
            playerList!![position].isSubstitute = isSelected
            rv_Player.adapter!!.notifyDataSetChanged()
            if (isSelected) {
                substitute_id = playerList!![position].player_id
                addSubstitute(true, substitute_id)
                btn_SelectSubstitute.isEnabled = true
                if (!substitute_id.isEmpty()) {
                    val intent = Intent()
                    intent.putExtra("isSubstitute", true)
                    intent.putExtra("substitute_id", substitute_id)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            } else {
                btn_SelectSubstitute.isEnabled = false
                substitute_id = ""
                addSubstitute(false, "")
            }

        }

    }

    override fun onClickItem(tag: String, position: Int) {
    }

    var substitute_id = ""
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_SelectSubstitute -> {
                if (!substitute_id.isEmpty()) {
                    val intent = Intent()
                    intent.putExtra("isSubstitute", true)
                    intent.putExtra("substitute_id", substitute_id)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppRequestCodes.UPDATE_ACTIVITY || requestCode == AppRequestCodes.EDIT || requestCode == AppRequestCodes.CLONE)
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent()
                setResult(Activity.RESULT_OK)
                finish()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_substitute)
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countTimer != null)
            countTimer!!.stopUpdateTimer()
    }

    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    var from = 0
    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.select_substitute)
        setMenu(false, false, false, false, false)
        getData()
    }

    var localTeamName = ""
    var visitorTeamName = ""
    fun getData() {
        btn_SelectSubstitute.setOnClickListener(this)
        selectPlayer = intent.getParcelableExtra(IntentConstant.SELECT_PLAYER)
        match = intent.getParcelableExtra(IntentConstant.MATCH)
        matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
        localTeamName = match!!.local_team_name
        visitorTeamName = match!!.visitor_team_name
        if (match!!.local_team_name.length > 5)
            localTeamName = match!!.local_team_name.substring(0, 4)
        if (match!!.visitor_team_name.length > 5)
            visitorTeamName = match!!.visitor_team_name.substring(0, 4)

        txt_matchVS.text = localTeamName + " " + getString(R.string.vs) + " " + visitorTeamName
        from = intent.getIntExtra(IntentConstant.ISEDIT, 0)

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
        wkList = intent.getParcelableArrayListExtra(IntentConstant.WK)
        arList = intent.getParcelableArrayListExtra(IntentConstant.AR)
        bowlerList = intent.getParcelableArrayListExtra(IntentConstant.BOWLER)
        batsmenList = intent.getParcelableArrayListExtra(IntentConstant.BATSMEN)
        playerList!!.addAll(this.wkList!!)
        playerList!!.addAll(this.bowlerList!!)
        playerList!!.addAll(this.arList!!)
        playerList!!.addAll(this.batsmenList!!)
        setAdapter(playerList!!)
    }

    var bowlerList: MutableList<Data>? = ArrayList()
    var arList: MutableList<Data>? = ArrayList()
    var wkList: MutableList<Data>? = ArrayList()
    var batsmenList: MutableList<Data>? = ArrayList()
    var playerList: MutableList<Data>? = ArrayList()

    @SuppressLint("WrongConstant")
    private fun setAdapter(playerlist: MutableList<Data>) {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Player!!.layoutManager = llm
        rv_Player!!.adapter = PlayerListSubstituteAdapter(
            this,
            playerlist,
            this,
            selectPlayer,
            localTeamName,
            visitorTeamName,
            match!!.local_team_id,
            match!!.visitor_team_id
        )
    }

    var selectPlayer: SelectPlayer? = null
    fun addSubstitute(isSelectedSubstitute: Boolean, substitute_id: String) {
        selectPlayer!!.substitute_id = substitute_id
        selectPlayer!!.substitute = false
    }
}