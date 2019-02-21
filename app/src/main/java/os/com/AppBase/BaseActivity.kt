package os.com.AppBase

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.action_bar_notification_icon.view.*
import kotlinx.android.synthetic.main.dialogue_join_contest.*
import kotlinx.android.synthetic.main.dialogue_tooltip.view.*
import kotlinx.android.synthetic.main.dialogue_wallet.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.R
import os.com.application.FantasyApplication
import os.com.channel.NotificationCountChannel
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.PrefConstant
import os.com.constant.Tags
import os.com.data.Prefs
import os.com.interfaces.OnClickDialogue
import os.com.networkCall.ApiClient
import os.com.ui.addCash.activity.AddCashActivity
import os.com.ui.contest.apiResponse.joinContestWalletAmountResponse.Data
import os.com.ui.dashboard.profile.activity.WithdrawCashActivity
import os.com.ui.dashboard.profile.activity.WithdrawRequestActivity
import os.com.ui.dashboard.profile.apiResponse.MyAccountResponse
import os.com.ui.login.activity.LoginActivity
import os.com.ui.notification.activity.NotificationActivity
import os.com.ui.winningBreakup.apiResponse.contestPriceBreakupResponse.PriceBreakUp
import os.com.ui.winningBreakup.dialogues.BottomSheetWinningListFragment
import os.com.utils.AppDelegate
import os.com.utils.CountTimer
import os.com.utils.networkUtils.NetworkUtils
import kotlin.math.roundToInt


/**
 * Created by heenas on 2/21/2018.
 */
open class BaseActivity : AppCompatActivity() {
    private lateinit var notificationView: View
    private var progressDialog: ProgressDialog? = null
    var pref: Prefs? = null

    var notif = false
    var wallet = false
    var filter = false
    var edit = false
    var guru = false

    var countTimer: CountTimer? = CountTimer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0);
        pref = Prefs(this)

        GlobalScope.launch {
            val value = NotificationCountChannel.getInstance().notificationCountChannel.receive()
            AppDelegate.LogT("value is=>" + value)
            if (menu != null) {
                getViewOfCartMenuItem(menu!!)
                if (notificationView != null) {
                    notificationView.notifItemCountTv.text = value.toString()
                    setDynamicallyParam(value)
                    if (value == 0)
                        notificationView.notifItemCountTv.visibility = View.GONE
                    else
                        notificationView.notifItemCountTv.visibility = View.VISIBLE

                } else {
                    pref!!.putIntValue(PrefConstant.UNREAD_COUNT, value)
                    getViewOfCartMenuItem(menu!!)
                }
            }
        }
    }

    /* */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter -> {
            }
//            R.id.menu_notification -> {
////                startActivity(Intent(this,ContestDetailActivity::class.java))
//                return true
//            }
            R.id.menu_wallet -> {
                val view = findViewById<View>(R.id.menu_wallet)
                if (walletPopupWindow == null)
                    initWalletPopUp(view)
                else
                    my_account_call(view)
//                    showWalletPopUp(view, response.response!!.data)
                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        /* set layout of menu*/
        menuInflater.inflate(R.menu.action_menu, menu)
        this.menu = menu
        /* if child activity is product activity then visible filer menu icon*/
        menu.findItem(R.id.menu_filter).isVisible = filter
//        menu.findItem(R.id.menu_notification).isVisible = notif
//        menu.findItem(R.id.menu_wallet).isVisible = wallet
        menu.findItem(R.id.menu_edit).isVisible = edit
        menu.findItem(R.id.menu_guru).isVisible = guru

        if (pref!!.isLogin) {
            menu.findItem(R.id.menu_notification).isVisible = notif
            menu.findItem(R.id.menu_wallet).isVisible = wallet
        } else {
            menu.findItem(R.id.menu_notification).isVisible = false
            menu.findItem(R.id.menu_wallet).isVisible = false
        }
//        menu.findItem(R.id.menu_sort).isVisible = driveActivityName == ProductActivity().javaClass.name
//        if (driveActivityName == ProductActivity().javaClass.name){
//        }
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            val spanString = SpannableString(menu.getItem(i).title.toString())
            spanString.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.whiteTab)),
                0,
                spanString.length,
                0
            ) //fix the color to white
            item.title = spanString
        }

        getViewOfCartMenuItem(menu)
        /* se click listener of toolbar cart icon*/
        notificationView.setOnClickListener {
            //            startActivity(Intent(this,ContestDetailActivity::class.java))
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        return true
    }

    fun setMenu(notif: Boolean, wallet: Boolean, filter: Boolean, edit: Boolean, guru: Boolean) {
        this.notif = notif
        this.wallet = wallet
        this.filter = filter
        this.edit = edit
        this.guru = guru
        if (menu != null) {
            menu!!.findItem(R.id.menu_filter).isVisible = filter
            menu!!.findItem(R.id.menu_notification).isVisible = notif
            menu!!.findItem(R.id.menu_wallet).isVisible = wallet
            if (pref!!.isLogin) {
                menu!!.findItem(R.id.menu_notification).isVisible = notif
                menu!!.findItem(R.id.menu_wallet).isVisible = wallet
            } else {
                menu!!.findItem(R.id.menu_notification).isVisible = false
                menu!!.findItem(R.id.menu_wallet).isVisible = false
            }

            menu!!.findItem(R.id.menu_edit).isVisible = edit
            menu!!.findItem(R.id.menu_guru).isVisible = guru
        }
    }


    private var walletPopupWindow: PopupWindow? = null

    private lateinit var popupWindowView: View
    private fun initWalletPopUp(anchorView: View) {
        try {


            /* set view for filter popup window*/
            walletPopupWindow = PopupWindow(this)
            popupWindowView = layoutInflater.inflate(R.layout.dialogue_wallet, null) as View
            walletPopupWindow!!.contentView = popupWindowView

            /* set visibility of brands list*/
            popupWindowView.img_close.setOnClickListener {
                walletPopupWindow!!.dismiss()
            }
            popupWindowView.ll_bottom.setOnClickListener {
                walletPopupWindow!!.dismiss()
            }
            popupWindowView.imvBonusInfo.setOnClickListener { view ->

                initToolTipPopUp(view, getString(R.string.bonus_info_text))
//                    if(popupWindowView.infoTip.visibility== View.VISIBLE)
//                        popupWindowView.infoTip.visibility=View.GONE
//                        else popupWindowView.infoTip.visibility=View.VISIBLE
//                SimpleTooltip.Builder(this)
//                    .anchorView(view)
//                    .text("Test")
//                    .build()
//                    .show()
            }
            popupWindowView.imvWinningInfo.setOnClickListener { view ->
                initToolTipPopUp(view, getString(R.string.winning_info_text))
                //                SimpleTooltip.Builder(baseContext)
//                    .anchorView(it)
//                    .text(resources.getString(R.string.winning_info_text))
//                    .build()
//                    .show()
            }
            popupWindowView.imvDepositedInfo.setOnClickListener { view ->
                initToolTipPopUp(view, getString(R.string.deposited_info_text))
                //                SimpleTooltip.Builder(baseContext)
//                    .anchorView(it)
//                    .text(resources.getString(R.string.deposited_info_text))
//                    .build()
//                    .show()
            }
            /* show popup window*/
            my_account_call(anchorView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initToolTipPopUp(anchorView: View, type: String) {
        try {


            /* set view for filter popup window*/
            walletPopupWindow = PopupWindow(this)
            popupWindowView = layoutInflater.inflate(R.layout.dialogue_tooltip, null) as View
            walletPopupWindow!!.contentView = popupWindowView

//            walletPopupWindow!!.height = WindowManager.LayoutParams.WRAP_CONTENT
//            walletPopupWindow!!.width = WindowManager.LayoutParams.MATCH_PARENT
            walletPopupWindow!!.isOutsideTouchable = true
            walletPopupWindow!!.isFocusable = true
            popupWindowView.txt_Tooltip.setText(type)

            var background = ColorDrawable(android.graphics.Color.BLACK)
            background.alpha = 10
            walletPopupWindow!!.setBackgroundDrawable(background);
            val rectangle = Rect()
            val window = window
            window.decorView.getWindowVisibleDisplayFrame(rectangle)
            walletPopupWindow!!.showAsDropDown(anchorView)
            /* show popup window*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showWalletPopUp(
        anchorView: View,
        data: MyAccountResponse.ResponseBean.DataBean
    ) {
        walletPopupWindow!!.height = WindowManager.LayoutParams.WRAP_CONTENT
        walletPopupWindow!!.width = WindowManager.LayoutParams.MATCH_PARENT
        walletPopupWindow!!.isOutsideTouchable = true
        walletPopupWindow!!.isFocusable = true

        popupWindowView.txtTotalAmount.setText("₹ " + data.total_balance)
        popupWindowView.txtDepositedAmount.setText("₹ " + data.deposit_amount)
        popupWindowView.txtWinningsAmount.setText("₹ " + data.winngs_amount)
        popupWindowView.txtCashBonusAmount.setText("₹ " + data.bonus)
        popupWindowView.txt_Withdraw.setOnClickListener {
            if (data.isAccount_verified) {
                startActivity(Intent(this, WithdrawRequestActivity::class.java))
            } else {
                startActivity(Intent(this, WithdrawCashActivity::class.java))
            }
        }
        popupWindowView.txt_Add.setOnClickListener {
            var currentBalance = "0.0"
            currentBalance = data!!.total_balance!!.toString()
            startActivityForResult(
                Intent(this, AddCashActivity::class.java).putExtra(
                    IntentConstant.currentBalance,
                    currentBalance
                ).putExtra(IntentConstant.AddType, IntentConstant.ADD), AppRequestCodes.REFRESH_WALLET
            )
        }
        var background = ColorDrawable(android.graphics.Color.BLACK)
        background.alpha = 10
        walletPopupWindow!!.setBackgroundDrawable(background);
        val rectangle = Rect()
        val window = window
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        walletPopupWindow!!.showAsDropDown(anchorView)
    }

    /* get view of cart menu item*/
    private fun getViewOfCartMenuItem(menu: Menu): View {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item.itemId == R.id.menu_notification) {
                notificationView = item.actionView
                /* get cart item quantity and set it*/
                val cartQuantity = pref!!.getIntValue(PrefConstant.UNREAD_COUNT, 0)
                notificationView.notifItemCountTv.text = cartQuantity.toString()
                setDynamicallyParam(cartQuantity)
                if (cartQuantity == 0)
                    notificationView.notifItemCountTv.visibility = View.GONE
                else
                    notificationView.notifItemCountTv.visibility = View.VISIBLE

                return notificationView
            }
        }
        return notificationView
    }

    /* change height width of cart item count*/
    private fun setDynamicallyParam(cartItemQuantity: Int) {
        if (cartItemQuantity.toString().length > 2) {
            notificationView.notifItemCountTv.measure(0, 0)
            val width = notificationView.notifItemCountTv.measuredWidth

            val linearPram = RelativeLayout.LayoutParams(width, width)
            val marginInDp = -5
            val marginInPx = marginInDp * resources.displayMetrics.density
            linearPram.setMargins(marginInPx.toInt(), marginInPx.toInt(), 0, 0)
            linearPram.addRule(RelativeLayout.END_OF, R.id.notif_icon)
            notificationView.notifItemCountTv.layoutParams = linearPram
        }
    }

    var match_idBase: String = ""
    var series_idBase: String = ""
    var contest_idBase: String = ""
    var team_idBase: String = ""
    var onClickDialogueBase: OnClickDialogue? = null
    fun checkAmountWallet(
        match_id: String,
        series_id: String,
        contest_id: String,
        team_id: String,
        onClickDialogue: OnClickDialogue
    ) {
        val walletRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            walletRequest[Tags.user_id] = pref!!.userdata!!.user_id
        walletRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        walletRequest[Tags.match_id] = match_id
        walletRequest[Tags.contest_id] = contest_id
        walletRequest[Tags.series_id] = series_id

        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@BaseActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .join_contest_wallet_amount(walletRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@BaseActivity)
                if (response.response!!.status) {
                    var entryFee = 0f
                    var bonus = 0f
                    var toPay = 0f
                    if (!response.response!!.data!!.entry_fee.isEmpty())
                        entryFee = response.response!!.data!!.entry_fee.toFloat()
                    if (!response.response!!.data!!.usable_bonus.isEmpty())
                        bonus = response.response!!.data!!.usable_bonus.toFloat() +
                                response.response!!.data!!.winning_balance.toFloat() +
                                response.response!!.data!!.cash_balance.toFloat()
                    toPay = entryFee - bonus
                    if (entryFee > 0 && toPay > 0) {
                        match_idBase = match_id
                        series_idBase = series_id
                        contest_idBase = contest_id
                        team_idBase = team_id
                        onClickDialogueBase = onClickDialogue
                        showAddCashDialog(bonus, toPay, onClickDialogue)
                    } else
                        showJoinContestDialogue(
                            response.response!!.data!!,
                            match_id,
                            series_id,
                            contest_id,
                            team_id,
                            onClickDialogue
                        )
                } else {
                    logoutIfDeactivate(response.response!!.message)
                    AppDelegate.showToast(this@BaseActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@BaseActivity)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppRequestCodes.ADD_CASH && resultCode == Activity.RESULT_OK)
            checkAmountWallet(
                match_idBase,
                series_idBase,
                contest_idBase,
                team_idBase,
                onClickDialogueBase!!
            )
        else if (requestCode == AppRequestCodes.ADD_CASH && resultCode == Activity.RESULT_CANCELED) {
            onClickDialogueBase!!.onClick(Tags.fail, false)
        }
        if (requestCode == AppRequestCodes.REFRESH_WALLET && resultCode == Activity.RESULT_OK) {
            val view = findViewById<View>(R.id.menu_wallet)
            if (walletPopupWindow!!.isShowing)
                if (walletPopupWindow == null)
                    initWalletPopUp(view)
                else
                    my_account_call(view)
        }
    }

    fun showJoinContestDialogue(
        data: Data,
        match_id: String,
        series_id: String,
        contest_id: String,
        team_id: String,
        onClickDialogue: OnClickDialogue
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
        var entryFee = 0f
        var bonus = 0f
        var toPay = 0f
        if (!data.cash_balance.isEmpty())
            cash = data.cash_balance.toFloat()
        if (!data.winning_balance.isEmpty())
            winning = data.winning_balance.toFloat()
        if (!data.entry_fee.isEmpty())
            entryFee = data.entry_fee.toFloat()
        if (!data.usable_bonus.isEmpty())
            bonus = data!!.usable_bonus.toFloat() + data!!.winning_balance.toFloat() + data!!.cash_balance.toFloat()

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
            onClickDialogue.onClick(Tags.cancel, false)
            dialogue.dismiss()
        }
        dialogue.img_info.setOnClickListener {
            SimpleTooltip.Builder(baseContext)
                .anchorView(it)
                .animated(false)
                .text(resources.getString(R.string.joinContestInfo))
                .build()
                .show()
//            initToolTipPopUp(it,getString(R.string.joinContestInfo))
        }
        dialogue.btn_Join.setOnClickListener {
            joinContest(match_id, series_id, contest_id, team_id, onClickDialogue)
            dialogue.dismiss()
        }
        if (dialogue.isShowing)
            dialogue.dismiss()
        dialogue.show()
    }

    fun joinContest(
        match_id: String,
        series_id: String,
        contest_id: String,
        team_id: String,
        onClickDialogue: OnClickDialogue
    ) {
        val walletRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            walletRequest[Tags.user_id] = pref!!.userdata!!.user_id
        else
            walletRequest[Tags.user_id] = ""
        walletRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        walletRequest[Tags.match_id] = match_id
        walletRequest[Tags.contest_id] = contest_id
        walletRequest[Tags.series_id] = series_id
        walletRequest[Tags.team_id] = team_id

        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@BaseActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .join_contest(walletRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@BaseActivity)
                if (response.response!!.status) {
                    AppDelegate.showToast(this@BaseActivity, response.response!!.message)
                    onClickDialogue.onClick(Tags.success, true)
                } else {
                    logoutIfDeactivate(response.response!!.message)
                    onClickDialogue.onClick(Tags.fail, false)
                    AppDelegate.showToast(this@BaseActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@BaseActivity)
            }
        }

    }

    public fun showAddCashDialog(bonus: Float, toPay: Float, onClickDialogue: OnClickDialogue) {
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
                Intent(this@BaseActivity, AddCashActivity::class.java).putExtra(
                    IntentConstant.currentBalance,
                    bonus.toString()
                ).putExtra(IntentConstant.AddType, IntentConstant.TO_JOIN)
                    .putExtra(IntentConstant.AMOUNT_TO_ADD, toPay.roundToInt().toString()),
                AppRequestCodes.ADD_CASH
            )
        }
        logoutAlertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE,
            getString(R.string.cancel)
        ) { dialog, id ->
            onClickDialogue.onClick(Tags.fail, false)
            logoutAlertDialog.dismiss()
        }
        logoutAlertDialog.show()
    }

    public fun showLogoutDialog() {
        val logoutAlertDialog = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle).create()
        logoutAlertDialog.setTitle(getString(R.string.app_name))
        logoutAlertDialog.setMessage(getString(R.string.want_to_logout))
        logoutAlertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE,
            getString(R.string.yes)
        ) { dialog, id ->
            logoutAlertDialog.dismiss()
            if (NetworkUtils.isConnected()) {
                callLogoutAPI()
            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()


        }
        logoutAlertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE,
            getString(R.string.no)
        ) { dialog, id ->
            logoutAlertDialog.dismiss()
        }
        logoutAlertDialog.show()
    }

    private fun callLogoutAPI() {
        val logoutRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            logoutRequest[Tags.user_id] = pref!!.userdata!!.user_id
        logoutRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@BaseActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .logout(logoutRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@BaseActivity)
                if (response.response!!.status!!) {
                    logout()
//                    Prefs(this@BaseActivity).clearSharedPreference()
//                    val intent = Intent(this@BaseActivity, LoginActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                    intent.putExtra("show",false)
//                    startActivity(intent)
//                    finish()
                } else {
                    logoutIfDeactivate(response.response!!.message!!)
                    AppDelegate.showToast(this@BaseActivity, response.response!!.message!!)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@BaseActivity)
            }
        }
    }

    private fun my_account_call(anchorView: View) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@BaseActivity)
                try {
                    var map = java.util.HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .user_account_datail(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response)
                    AppDelegate.hideProgressDialog(this@BaseActivity)
                    if (response.response!!.isStatus) {
                        showWalletPopUp(anchorView, response.response!!.data)
//
                    } else {
                        logoutIfDeactivate(response.response!!.message)
                        AppDelegate.showToast(this@BaseActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@BaseActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    public fun callWinningBreakupApi(
        contest_id: String,
        breakup_detail: ArrayList<PriceBreakUp>,
        prize_money: String

    ) {
        val bottomSheetDialogFragment = BottomSheetWinningListFragment()
        var bundle = Bundle()
        bundle.putParcelableArrayList(Tags.contest_id, breakup_detail)
        bundle.putString(Tags.winning_prize, prize_money)
        bottomSheetDialogFragment.arguments = bundle
        bottomSheetDialogFragment.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")


//        val loginRequest = HashMap<String, String>()
//        if (pref!!.isLogin)
//            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
//        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
//        loginRequest[Tags.contest_id] = contest_id
//        GlobalScope.launch(Dispatchers.Main) {
//            AppDelegate.showProgressDialog(this@BaseActivity)
//            try {
//                val request = ApiClient.client
//                    .getRetrofitService()
//                    .contest_price_breakup(loginRequest)
//                val response = request.await()
//                AppDelegate.LogT("Response=>" + response);
//                AppDelegate.hideProgressDialog(this@BaseActivity)
//                if (response.response!!.status) {
//                    val bottomSheetDialogFragment = BottomSheetWinningListFragment()
//                    var bundle = Bundle()
//                    bundle.putParcelable(Tags.contest_id, response.response!!.data!!)
//                    bundle.putString(Tags.winning_prize, response.response!!.data!!)
//                    bottomSheetDialogFragment.arguments = bundle
//                    bottomSheetDialogFragment.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")
//
//
//                } else {
//                }
//            } catch (exception: Exception) {
//                AppDelegate.hideProgressDialog(this@BaseActivity)
//            }
//        }
    }

    public fun logoutIfDeactivate(msg: String) {
        try {
            if (!msg.isEmpty()) {
                if (msg.equals("Invalid user id.", true))
                    logout()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun logout() {
        Prefs(this@BaseActivity).clearSharedPreference()
        val intent = Intent(this@BaseActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("show", false)
        startActivity(intent)
        finish()
    }
}







