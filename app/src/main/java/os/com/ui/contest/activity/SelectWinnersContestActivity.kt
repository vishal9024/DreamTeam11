package os.com.ui.contest.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_select_winners_contest.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_select_winners_contest.*
import kotlinx.android.synthetic.main.dialogue_join_contest.*
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
import os.com.constant.Tags.contest_id
import os.com.networkCall.ApiClient
import os.com.ui.addCash.activity.AddCashActivity
import os.com.ui.contest.adapter.BottomSheetWinnerRankListAdapter
import os.com.ui.contest.apiResponse.contestSizePriceBreakUp.Data
import os.com.ui.contest.dialogues.BottomSheetWinnerListFragment
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.createTeam.activity.myTeam.MyTeamSelectActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import kotlin.math.roundToInt


class SelectWinnersContestActivity : BaseActivity(), View.OnClickListener,
    BottomSheetWinnerListFragment.OnClickWinning {
    override fun onClick(tag: String, position: Int) {
        if (position == 0)
            txt_winners.text = priceBreakUpList!![position].title + " " + getString(R.string.recommended)
        else
            txt_winners.text = priceBreakUpList[position].title
        winners_count = priceBreakUpList[position].title!!
        winnerListAdapter!!.infoList(priceBreakUpList[position].info!!, winning_amount)
        winnerListAdapter!!.notifyDataSetChanged()
    }

    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.btn_CreateSelectContest -> {
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
                            Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(
                                IntentConstant.CONTEST_TYPE,
                                matchType
                            )
                                .putExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.CREATE_CONTEST),
                            AppRequestCodes.CREATE_CONTEST
                        )
                    }
                }
                R.id.ll_winnersSelect -> {
                    val bottomSheetDialogFragment = BottomSheetWinnerListFragment()
                    val bundle = Bundle()
                    bundle.putParcelableArrayList(Tags.DATA, priceBreakUpList)
                    bundle.putString(IntentConstant.WINNING_AMOUNT, winning_amount)
                    bottomSheetDialogFragment.arguments = bundle
                    bottomSheetDialogFragment.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is BottomSheetWinnerListFragment) {
            val headlinesFragment = fragment as BottomSheetWinnerListFragment
            headlinesFragment.setOnClickWinningListener(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_winners_contest)
        initViews()
    }

    var team_id = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CREATE_CONTEST) {
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
    }

    fun checkAmountWallet(
    ) {
        val walletRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            walletRequest[Tags.user_id] = pref!!.userdata!!.user_id
        else
            walletRequest[Tags.user_id] = ""
        walletRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        walletRequest[Tags.match_id] = match!!.match_id
        walletRequest[Tags.contest_id] = contest_id
        walletRequest[Tags.series_id] = match!!.series_id

        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@SelectWinnersContestActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .join_contest_wallet_amount(walletRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@SelectWinnersContestActivity)
                if (response.response!!.status) {
                    var entryFee = entry_fee.toFloat()
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
                    logoutIfDeactivate(response.response!!.message)
                    AppDelegate.showToast(this@SelectWinnersContestActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@SelectWinnersContestActivity)
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
        var entryFee = entry_fee.toFloat()
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
        else walletRequest[Tags.user_id] = ""
        walletRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        walletRequest[Tags.match_id] = match!!.match_id
        walletRequest[Tags.series_id] = match!!.series_id
        walletRequest[Tags.team_id] = team_id
        walletRequest["contest_name"] = team_name
        walletRequest["winning_amount"] = winning_amount
        walletRequest["contest_size"] = contest_size
        walletRequest["entry_fee"] = entry_fee
        if (allow_multiple_teams)
            walletRequest["join_multiple"] = "yes"
        else
            walletRequest["join_multiple"] = "no"
        walletRequest["winners_count"] = winners_count
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@SelectWinnersContestActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .create_contest(walletRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@SelectWinnersContestActivity)
                if (response.response!!.status) {
                    startActivityForResult(
                        Intent(this@SelectWinnersContestActivity, InviteContestToFriendsActivity::class.java)
                            .putExtra(IntentConstant.MATCH, match)
                            .putExtra(IntentConstant.CONTEST_TYPE, matchType)
                            .putExtra(IntentConstant.CONTEST_CODE, response.response!!.data!!.invite_code)
                        , AppRequestCodes.INVITE_CONTEST
                    )
                } else {
                    logoutIfDeactivate(response.response!!.message)
                    AppDelegate.showToast(this@SelectWinnersContestActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@SelectWinnersContestActivity)
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
                Intent(this@SelectWinnersContestActivity, AddCashActivity::class.java).putExtra(
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


//    var countTimer: CountTimer? = CountTimer()
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    var createOrJoin = AppRequestCodes.JOIN
    var allow_multiple_teams = false
    var winning_amount = ""
    var contest_size = ""
    var team_name = ""
    var winners_count = ""
    var entry_fee = ""
    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.create_contest)
            setMenu(false, false, false, false, false)
            winnerBootomSheet()
            setAdapter()
            getIntentData()
            btn_CreateSelectContest.setOnClickListener(this)
            ll_winnersSelect.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var priceBreakUpList: ArrayList<Data> = ArrayList()
    private fun getIntentData() {
        if (intent != null) {
            createOrJoin = intent.getIntExtra(IntentConstant.CREATE_OR_JOIN, AppRequestCodes.CREATE_CONTEST)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
            contest_size = intent.getStringExtra(IntentConstant.CONTEST_SIZE)
            winning_amount = intent.getStringExtra(IntentConstant.WINNING_AMOUNT)
            team_name = intent.getStringExtra(IntentConstant.TEAM_NAME)
            entry_fee = intent.getStringExtra(IntentConstant.ENTRY_FEE)
            allow_multiple_teams = intent.getBooleanExtra(IntentConstant.ALLOW_MULTIPLE_TEAMS, false)
            priceBreakUpList = intent.getParcelableArrayListExtra(IntentConstant.DATA)
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            if (!team_name.isEmpty()) {
                toolbarTitleTv.text = team_name
            } else {
                toolbarTitleTv.text = "My New Contest"
            }
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
                    countTimer!!.startUpdateTimer(this,dateTime, txt_CountDownTimer)
                }
            } else if (matchType == IntentConstant.COMPLETED) {
                txt_CountDownTimer.setText(getString(R.string.completed))
            } else {
                txt_CountDownTimer.setText(getString(R.string.in_progress))
                txt_CountDownTimer.setTextColor(resources.getColor(R.color.dark_yellow))
            }
            txt_TotalWinnings.setText(contest_size)
            txt_Winners.text = getString(R.string.Rs) + " " + winning_amount
            txt_EntryFees.text = getString(R.string.Rs) + " " + entry_fee
            txt_winners.text = priceBreakUpList!![0].title + " " + getString(R.string.recommended)
            winnerListAdapter!!.infoList(priceBreakUpList[0].info!!, winning_amount)
            winnerListAdapter!!.notifyDataSetChanged()
            winners_count = priceBreakUpList[0].title!!
        }
    }

    var winnerListAdapter: BottomSheetWinnerRankListAdapter? = null
    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        try {
            val llm = LinearLayoutManager(this)
            llm.orientation = LinearLayoutManager.VERTICAL
            rv_rank!!.layoutManager = llm
            winnerListAdapter = BottomSheetWinnerRankListAdapter(this)
            rv_rank!!.adapter = winnerListAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun winnerBootomSheet() {
        try {
            val mBottomSheetBehaviorfilter = BottomSheetBehavior.from(bottom_sheet_filter)
            mBottomSheetBehaviorfilter.state = BottomSheetBehavior.STATE_COLLAPSED
            mBottomSheetBehaviorfilter.peekHeight = 0
            //If you want to handle callback of Sheet Behavior you can use below code
            mBottomSheetBehaviorfilter.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}