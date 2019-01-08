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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.action_bar_notification_icon.view.*
import kotlinx.android.synthetic.main.dialogue_join_contest.*
import kotlinx.android.synthetic.main.dialogue_wallet.view.*
import os.com.R
import os.com.constant.IntentConstant
import os.com.data.Prefs
import os.com.ui.createTeam.activity.ChooseTeamActivity
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.login.activity.LoginActivity
import os.com.ui.notification.activity.NotificationActivity


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

    public fun showJoinContestDialogue(
        activity: AppCompatActivity,
        match: Match?,
        matchType: Int
    ) {
        var dialogue = Dialog(this)
        dialogue.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogue.setContentView(R.layout.dialogue_join_contest)
        dialogue.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialogue.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogue.setCancelable(false)
        dialogue.setCanceledOnTouchOutside(false)
        dialogue.setTitle(null)
        dialogue.img_Close.setOnClickListener {
            dialogue.dismiss()
        }
        dialogue.btn_Join.setOnClickListener {
            startActivity(
                Intent(this, ChooseTeamActivity::class.java).putExtra(IntentConstant.MATCH, match).putExtra(IntentConstant.CONTEST_TYPE, matchType)
            )
            dialogue.dismiss()
        }

        if (dialogue.isShowing)
            dialogue.dismiss()
        dialogue.show()
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
            // callLogoutAPI()
            Prefs(this).clearSharedPreference()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        logoutAlertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE,
            getString(R.string.no)
        ) { dialog, id ->
            logoutAlertDialog.dismiss()
        }
        logoutAlertDialog.show()
    }
}





