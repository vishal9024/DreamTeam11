package os.com.ui.contest.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_create_contest.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate
import os.com.utils.CountTimer
import java.util.*

class CreateContestActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.btn_CreateContest -> {
                    callWinningBreakUpApi()

                }


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callWinningBreakUpApi() {
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.contest_size] = et_contest_size.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@CreateContestActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .contest_price_breakup(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@CreateContestActivity)
                if (response.response!!.status) {
                    startActivityForResult(
                        Intent(this@CreateContestActivity, SelectWinnersContestActivity::class.java)
                            .putParcelableArrayListExtra(IntentConstant.DATA, response.response!!.data)
                            .putExtra(IntentConstant.MATCH, match)
                            .putExtra(IntentConstant.CONTEST_TYPE, matchType)
                            .putExtra(IntentConstant.CONTEST_SIZE, et_contest_size.text.toString())
                            .putExtra(IntentConstant.WINNING_AMOUNT, et_winning_amount.text.toString())
                            .putExtra(IntentConstant.ALLOW_MULTIPLE_TEAMS, switch_multipleTeam.isChecked)
                            .putExtra(IntentConstant.TEAM_NAME, et_contestName.text.toString())
                            .putExtra(IntentConstant.CONTEST_ID, "")
                            .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.CREATE_CONTEST),
                        AppRequestCodes.UPDATE_ACTIVITY
                    )
                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@CreateContestActivity)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_contest)
        initViews()
    }

    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.create_contest)
            setMenu(false, false, false, false, false)
            getIntentData()
            btn_CreateContest.setOnClickListener(this)
            AddTextChangeListener(et_winning_amount)
            AddTextChangeListener(et_contest_size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    var createOrJoin = AppRequestCodes.JOIN
    private fun getIntentData() {
        if (intent != null) {
            createOrJoin = intent.getIntExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.CREATE)
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)

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
    }

    private fun AddTextChangeListener(editText: TextInputEditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkCall()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun checkCall() {
        if (!et_winning_amount.text.toString().isEmpty() && !et_contest_size.text.toString().isEmpty()) {
            if (et_winning_amount.text.toString().toInt() >= 5 && et_contest_size.text.toString().toInt() >= 2) {
                GlobalScope.launch(Dispatchers.Main) {
                    delay(100)
                    callEntryFeeApi()
                }
            } else {
                txt_EntryFeeAmount.text = "-"
                btn_CreateContest.isEnabled = false
            }
        } else {
            txt_EntryFeeAmount.text = "-"
            btn_CreateContest.isEnabled = false
        }
    }

    private fun callEntryFeeApi() {
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.contest_size] = et_contest_size.text.toString()
        loginRequest[Tags.winning_amount] = et_winning_amount.text.toString()

        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@CreateContestActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .entryPerTeam(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@CreateContestActivity)
                if (response.response!!.status) {
                    if (!response.response!!.data!!.entry_fee!!.isEmpty()) {
                        if (response.response!!.data!!.entry_fee!!.toFloat() >= 5) {
                            txt_EntryFeeAmount.text = getString(R.string.Rs) + " " +
                                    response.response!!.data!!.entry_fee
                            btn_CreateContest.isEnabled = true
                        } else {
                            txt_EntryFeeAmount.text = "-"
                            btn_CreateContest.isEnabled = false
                            AppDelegate.showSnackBar(
                                toolbar,
                                this@CreateContestActivity,
                                getString(R.string.entryFeeValidation)
                            )
                        }
                    } else {
                        txt_EntryFeeAmount.text = "-"
                        btn_CreateContest.isEnabled = false
                        AppDelegate.showSnackBar(
                            toolbar,
                            this@CreateContestActivity,
                            getString(R.string.entryFeeValidation)
                        )
                    }
                } else {
                    txt_EntryFeeAmount.text = "-"
                    btn_CreateContest.isEnabled = false
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@CreateContestActivity)
            }
        }
    }
}