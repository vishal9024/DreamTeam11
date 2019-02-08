package os.com.ui.contest.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_create_contest.*
import kotlinx.android.synthetic.main.dialogue_join_contest.*
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
import os.com.ui.addCash.activity.AddCashActivity
import os.com.ui.contest.apiResponse.contestSizePriceBreakUp.Data
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamSelectActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate
import os.com.utils.CountTimer
import os.com.utils.networkUtils.NetworkUtils
import java.util.*
import kotlin.math.roundToInt

class CreateContestActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.btn_CreateContest -> {
                    if (!et_contest_size.text.toString().isEmpty() && et_contest_size.text.toString().toInt() == 2 || et_winning_amount.text.toString().toInt() == 0) {
                        if (FantasyApplication.getInstance().teamCount > 0) {
                            startActivityForResult(
                                Intent(this, MyTeamSelectActivity::class.java).putExtra(IntentConstant.MATCH, match)
                                    .putExtra(IntentConstant.CONTEST_TYPE, matchType)
                                    .putExtra(IntentConstant.CONTEST_ID, "")
                                    .putExtra(IntentConstant.FOR, AppRequestCodes.CREATE_CONTEST),
                                AppRequestCodes.CREATE_CONTEST
                            )
                        } else {
                            startActivityForResult(
                                Intent(this, ChooseTeamActivity::class.java).putExtra(
                                    IntentConstant.MATCH,
                                    match
                                ).putExtra(
                                    IntentConstant.CONTEST_TYPE,
                                    matchType
                                )
                                    .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.CREATE_CONTEST),
                                AppRequestCodes.CREATE_CONTEST
                            )
                        }
                    } else {
                        callWinningBreakUpApi()
                    }
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
                    .contest_prize_breakup(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@CreateContestActivity)
                if (response.response!!.status) {
                    sendIntent(response.response!!.data)
                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@CreateContestActivity)
            }
        }
    }

    private fun sendIntent(data: ArrayList<Data>?) {

        startActivityForResult(
            Intent(this, SelectWinnersContestActivity::class.java)
                .putExtra(IntentConstant.MATCH, match)
                .putExtra(IntentConstant.CONTEST_TYPE, matchType)
                .putExtra(IntentConstant.CONTEST_SIZE, et_contest_size.text.toString())
                .putExtra(IntentConstant.WINNING_AMOUNT, et_winning_amount.text.toString())
                .putExtra(IntentConstant.ALLOW_MULTIPLE_TEAMS, switch_multipleTeam.isChecked)
                .putExtra(IntentConstant.TEAM_NAME, et_contestName.text.toString())
                .putExtra(IntentConstant.ENTRY_FEE, entryFee)
                .putParcelableArrayListExtra(IntentConstant.DATA, data)
            , AppRequestCodes.INVITE_CONTEST
        )
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
        btn_CreateContest.text = getString(R.string.create_contest)
        if (!et_winning_amount.text.toString().isEmpty() && !et_contest_size.text.toString().isEmpty()) {
            if (et_winning_amount.text.toString().toInt() == 0 && et_contest_size.text.toString().toInt() >= 2) {
                txt_EntryFeeAmount.text = getString(R.string.Rs) + " " +
                        String.format("%.2f", 0.00)
                entryFee = String.format("%.2f", 0.00)
                btn_CreateContest.isEnabled = true
            } else if (et_contest_size.text.toString().toInt() >= 2) {
                GlobalScope.launch(Dispatchers.Main) {
                    delay(300)
                    callEntryFeeApi()
                }
            } else {
                if (!et_contest_size.text.toString().isEmpty() && et_contest_size.text.toString().toInt() < 2)
                    showToolbar("Contest size should not less than 2")
                entryFee = ""
                txt_EntryFeeAmount.text = "-"
                btn_CreateContest.isEnabled = false
            }
        } else {
            entryFee = ""
            txt_EntryFeeAmount.text = "-"
            btn_CreateContest.isEnabled = false
        }
    }

    var entryFee = ""
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
                                    String.format("%.2f", response.response!!.data!!.entry_fee!!.toFloat())
                            entryFee = String.format("%.2f", response.response!!.data!!.entry_fee!!.toFloat())
                            btn_CreateContest.isEnabled = true
                            if (et_contest_size.text.toString().toInt() != 2)
                                btn_CreateContest.text = getString(R.string.choose_winning_breakup)
                        } else {
                            entryFee = ""
                            txt_EntryFeeAmount.text = "-"
                            btn_CreateContest.isEnabled = false
                            showToolbar(
                                getString(R.string.entryFeeValidation)
                            )
                        }
                    } else {
                        txt_EntryFeeAmount.text = "-"
                        entryFee = ""
                        btn_CreateContest.isEnabled = false
                        showToolbar(
                            getString(R.string.entryFeeValidation)
                        )
                    }
                } else {
                    txt_EntryFeeAmount.text = "-"
                    entryFee = ""
                    btn_CreateContest.isEnabled = false
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@CreateContestActivity)
            }
        }
    }

    fun showToolbar(msg: String) {
        val snack = Snackbar.make(toolbar, msg, Snackbar.LENGTH_LONG)
        val view = snack.getView()
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.vicecaptainColor));
        val params = view.getLayoutParams() as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.setLayoutParams(params)
        snack.show()
    }

    var team_id = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppRequestCodes.CREATE_CONTEST && (resultCode == Activity.RESULT_OK)) {
            if (data != null) {
                team_id = data.getStringExtra(IntentConstant.TEAM_ID)
                if (!team_id.isEmpty())
                    if (NetworkUtils.isConnected()) {
                        checkAmountWallet()
                    } else
                        Toast.makeText(
                            this,
                            getString(R.string.error_network_connection),
                            Toast.LENGTH_LONG
                        ).show()
            }
        } else if (requestCode == AppRequestCodes.ADD_CASH_CONTEST && resultCode == Activity.RESULT_OK)
            checkAmountWallet()
        else if (requestCode == AppRequestCodes.ADD_CASH_CONTEST && resultCode == Activity.RESULT_CANCELED) {
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        } else if (requestCode == AppRequestCodes.INVITE_CONTEST && resultCode == Activity.RESULT_OK) {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun checkAmountWallet(
    ) {
        val walletRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            walletRequest[Tags.user_id] = pref!!.userdata!!.user_id
        walletRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        walletRequest[Tags.match_id] = match!!.match_id
        walletRequest[Tags.contest_id] = Tags.contest_id
        walletRequest[Tags.series_id] = match!!.series_id

        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@CreateContestActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .join_contest_wallet_amount(walletRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@CreateContestActivity)
                if (response.response!!.status) {
                    var entryFee = entryFee.toFloat()
                    var bonus = 0f
                    var toPay = 0f
                    if (!response.response!!.data!!.usable_bonus.isEmpty())
                        bonus = response.response!!.data!!.usable_bonus.toFloat() +
                                response.response!!.data!!.winning_balance.toFloat() +
                                response.response!!.data!!.cash_balance.toFloat()
                    toPay = entryFee - bonus
                    if (entryFee > 0 && toPay > 0) {
                        showAddCashDialog(bonus, toPay)
                    } else
                        showJoinContestDialogue(
                            response.response!!.data!!
                        )
                } else {
                    AppDelegate.showToast(this@CreateContestActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@CreateContestActivity)
            }
        }
    }


    fun showJoinContestDialogue(
        data: os.com.ui.contest.apiResponse.joinContestWalletAmountResponse.Data
    ) {
        var dialogue = Dialog(this)
        dialogue.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogue.setContentView(R.layout.dialogue_join_contest)
        dialogue.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialogue.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogue.setCancelable(false)
        dialogue.setCanceledOnTouchOutside(false)
        dialogue.setTitle(null)
        var cash = 0f
        var winning = 0f
        var entryFee = entryFee.toFloat()
        var bonus = 0f
        var toPay = 0f
        if (!data.cash_balance.isEmpty())
            cash = data.cash_balance.toFloat()
        if (!data.winning_balance.isEmpty())
            winning = data.winning_balance.toFloat()
        if (!data.usable_bonus.isEmpty())
            bonus = data.usable_bonus.toFloat() + data!!.winning_balance.toFloat() + data!!.cash_balance.toFloat()

        toPay = entryFee - bonus
        if (bonus >= entryFee)
            toPay = 0f
        var total = cash + winning
        dialogue.txt_label.text = getString(R.string.unutilized_balance_winnings_rs_0) + " " + getString(R.string.Rs) +
                String.format(
                    "%.2f",
                    total
                )
        dialogue.txt_EntryFee.text = getString(R.string.Rs) + String.format("%.2f", entryFee)
        dialogue.txt_bonus.text = getString(R.string.Rs) + String.format("%.2f", data!!.usable_bonus.toFloat())
        dialogue.txt_topay.text = getString(R.string.Rs) + String.format("%.2f", toPay)
        dialogue.img_Close.setOnClickListener {
            //            onClickDialogue.onClick(Tags.cancel, false)
            dialogue.dismiss()
        }
        dialogue.btn_Join.setOnClickListener {
            joinContest()
            dialogue.dismiss()
        }
        if (dialogue.isShowing)
            dialogue.dismiss()
        dialogue.show()
    }


    fun joinContest() {
        val walletRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            walletRequest[Tags.user_id] = pref!!.userdata!!.user_id
        walletRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        walletRequest[Tags.match_id] = match!!.match_id
        walletRequest[Tags.series_id] = match!!.series_id
        walletRequest[Tags.team_id] = team_id
        walletRequest["contest_name"] = et_contestName.text.toString()
        walletRequest["winning_amount"] = et_winning_amount.text.toString()
        walletRequest["contest_size"] = et_contest_size.text.toString()
        walletRequest["entry_fee"] = entryFee
        if (switch_multipleTeam.isChecked)
            walletRequest["join_multiple"] = "yes"
        else
            walletRequest["join_multiple"] = "no"
        walletRequest["winners_count"] = "1"
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@CreateContestActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .create_contest(walletRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@CreateContestActivity)
                if (response.response!!.status) {
                    startActivityForResult(
                        Intent(this@CreateContestActivity, InviteContestToFriendsActivity::class.java)
                            .putExtra(IntentConstant.MATCH, match)
                            .putExtra(IntentConstant.CONTEST_TYPE, matchType)
                            .putExtra(IntentConstant.CONTEST_CODE, response.response!!.data!!.invite_code)
                        , AppRequestCodes.INVITE_CONTEST
                    )
                } else {
                    AppDelegate.showToast(this@CreateContestActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@CreateContestActivity)
            }
        }
    }

    public fun showAddCashDialog(bonus: Float, toPay: Float) {
        var message = "Low balance! Please add ₹ " + String.format("%.2f", toPay) + " to join contest."

        val logoutAlertDialog = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle).create()
        logoutAlertDialog.setTitle(getString(R.string.app_name))
        logoutAlertDialog.setMessage(message)
        logoutAlertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            getString(R.string.add_cash)
        ) { dialog, id ->
            logoutAlertDialog.dismiss()
            startActivityForResult(
                Intent(this@CreateContestActivity, AddCashActivity::class.java).putExtra(
                    IntentConstant.currentBalance,
                    bonus.toString()
                ).putExtra(IntentConstant.AddType, IntentConstant.TO_JOIN)
                    .putExtra(IntentConstant.AMOUNT_TO_ADD, toPay.roundToInt().toString()),
                AppRequestCodes.ADD_CASH_CONTEST
            )
        }
        logoutAlertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE,
            getString(R.string.cancel)
        ) { dialog, id ->

            //            onClickDialogue.onClick(Tags.fail, false)
            logoutAlertDialog.dismiss()
        }
        logoutAlertDialog.show()
    }
}