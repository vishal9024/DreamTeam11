package os.com.AppBase

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.action_bar_notification_icon.view.*
import kotlinx.android.synthetic.main.dialogue_join_contest.*
import kotlinx.android.synthetic.main.dialogue_wallet.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.Tags
import os.com.data.Prefs
import os.com.interfaces.OnClickDialogue
import os.com.networkCall.ApiClient
import os.com.ui.addCash.activity.AddCashActivity
import os.com.ui.contest.apiResponse.joinContestWalletAmountResponse.Data
import os.com.ui.login.activity.LoginActivity
import os.com.ui.notification.activity.NotificationActivity
import os.com.ui.winningBreakup.dialogues.BottomSheetWinningListFragment
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils


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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0);
        pref = Prefs(this)
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
                    showWalletPopUp(view)
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
        menu.findItem(R.id.menu_notification).isVisible = notif
        menu.findItem(R.id.menu_wallet).isVisible = wallet
        menu.findItem(R.id.menu_edit).isVisible = edit
//        menu.findItem(R.id.menu_sort).isVisible = driveActivityName == ProductActivity().javaClass.name
//        if (driveActivityName == ProductActivity().javaClass.name){
//        }
        getViewOfCartMenuItem(menu)
        /* se click listener of toolbar cart icon*/
        notificationView.setOnClickListener {
            //            startActivity(Intent(this,ContestDetailActivity::class.java))
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        return true
    }

    fun setMenu(notif: Boolean, wallet: Boolean, filter: Boolean, edit: Boolean) {
        this.notif = notif
        this.wallet = wallet
        this.filter = filter
        this.edit = edit
        if (menu != null) {
            menu!!.findItem(R.id.menu_filter).isVisible = filter
            menu!!.findItem(R.id.menu_notification).isVisible = notif
            menu!!.findItem(R.id.menu_wallet).isVisible = wallet
            menu!!.findItem(R.id.menu_edit).isVisible = edit
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
            /* show popup window*/
            showWalletPopUp(anchorView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showWalletPopUp(anchorView: View) {
        walletPopupWindow!!.height = WindowManager.LayoutParams.WRAP_CONTENT
        walletPopupWindow!!.width = WindowManager.LayoutParams.MATCH_PARENT
        walletPopupWindow!!.isOutsideTouchable = true
        walletPopupWindow!!.isFocusable = true
//        walletPopupWindow!!.setBackgroundDrawable(
//            BitmapDrawable(
//                resources,
//                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
//            )
//        )
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
//                val cartQuantity = pref!!.getStringValue(PrefConstant.KEY_CART_ITEM_COUNT, "")
                val cartQuantity = "1"
                notificationView.notifItemCountTv.text = cartQuantity
                setDynamicallyParam(cartQuantity)
//                if (pref!!.storeIdMatch() || !AppDelegate.isValidString(pref!!.getStringValue(PrefConstant.KEY_STORE_ID, "")))
//                    cartItemView.cartItemCountTv.text = cartQuantity
//                else
//                    cartItemView.cartItemCountTv.text = "X"
                if (cartQuantity == "0" || cartQuantity.isEmpty())
                    notificationView.notifItemCountTv.visibility = View.GONE
                else
                    notificationView.notifItemCountTv.visibility = View.VISIBLE

                return notificationView
            }
        }
        return notificationView
    }

    /* change height width of cart item count*/
    private fun setDynamicallyParam(cartItemQuantity: String) {
        if (cartItemQuantity.length > 2) {
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
                        bonus = response.response!!.data!!.usable_bonus.toFloat()
                    toPay = entryFee - bonus
                    if (entryFee > 0 && toPay > 0) {
                        startActivityForResult(
                            Intent(this@BaseActivity, AddCashActivity::class.java),
                            AppRequestCodes.ADD_CASH
                        )
//                        onClickDialogue.onClick(Tags.fail,false)
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
                    AppDelegate.showToast(this@BaseActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@BaseActivity)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /**/
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
        dialogue.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialogue.window.setBackgroundDrawable(ColorDrawable(Color.BLACK))
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
            bonus = data.usable_bonus.toFloat()
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
        dialogue.txt_bonus.text = getString(R.string.Rs) + String.format("%.2f", bonus)
        dialogue.txt_topay.text = getString(R.string.Rs) + String.format("%.2f", toPay)
        dialogue.img_Close.setOnClickListener {
            onClickDialogue.onClick(Tags.cancel, false)
            dialogue.dismiss()
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
                    onClickDialogue.onClick(Tags.fail, false)
                    AppDelegate.showToast(this@BaseActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@BaseActivity)
            }
        }

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
                    Prefs(this@BaseActivity).clearSharedPreference()
                    val intent = Intent(this@BaseActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    AppDelegate.showToast(this@BaseActivity, response.response!!.message!!)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@BaseActivity)
            }
        }
    }

    public fun callWinningBreakupApi(
        contest_id: String

    ) {
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        loginRequest[Tags.contest_id] = contest_id
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@BaseActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .contest_price_breakup(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@BaseActivity)
                if (response.response!!.status) {
                    val bottomSheetDialogFragment = BottomSheetWinningListFragment()
                    var bundle = Bundle()
                    bundle.putParcelable(Tags.contest_id, response.response!!.data!!)
                    bottomSheetDialogFragment.arguments = bundle
                    bottomSheetDialogFragment.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")


                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@BaseActivity)
            }
        }
    }
}





