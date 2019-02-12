package os.com.ui.dashboard.profile.activity

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_withdraw_cash.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.profile.fragment.BankFragment
import os.com.ui.dashboard.profile.fragment.MobileAndEmailFragment
import os.com.ui.dashboard.profile.fragment.PanFragment
import os.com.utils.AppDelegate
import java.util.*

class WithdrawCashActivity : BaseActivity(), View.OnClickListener {

    private var selectedFragment: Fragment? = null

    companion object {
        public var emailVerify = false
        public var panVerify = 0
        public var bankVerify = 0

    }


    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.tabMobileAndEmail -> {
                    tabMobileAndEmail.setTextColor(resources.getColor(R.color.colorSecondary))
                    tabPAN.setTextColor(resources.getColor(R.color.black))
                    tabBank.setTextColor(resources.getColor(R.color.black))
                    tabMobileAndEmail.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.tabs_bg))
                    tabPAN.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.tabs_white_bg))
                    tabBank.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.tabs_white_bg))
                    selectedFragment = MobileAndEmailFragment()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    transaction.replace(R.id.frame_layout, selectedFragment as MobileAndEmailFragment)
                    transaction.commit()
                }
                R.id.tabPAN -> {
                    tabMobileAndEmail.setTextColor(resources.getColor(R.color.black))
                    tabPAN.setTextColor(resources.getColor(R.color.colorSecondary))
                    tabBank.setTextColor(resources.getColor(R.color.black))
                    tabMobileAndEmail.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.tabs_white_bg))
                    tabPAN.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.tabs_bg))
                    tabBank.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.tabs_white_bg))
                    selectedFragment = PanFragment()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    transaction.replace(R.id.frame_layout, selectedFragment as PanFragment)
                    transaction.commit()
                }
                R.id.tabBank -> {
                    tabMobileAndEmail.setTextColor(resources.getColor(R.color.black))
                    tabPAN.setTextColor(resources.getColor(R.color.black))
                    tabBank.setTextColor(resources.getColor(R.color.colorSecondary))
                    tabMobileAndEmail.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.tabs_white_bg))
                    tabPAN.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.tabs_white_bg))
                    tabBank.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.tabs_bg))
                    selectedFragment = BankFragment()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    transaction.replace(R.id.frame_layout, selectedFragment as BankFragment)
                    transaction.commit()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw_cash)
        initViews()
    }

    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.withdraw)
            //setMenu(false, false, false, false,false)
            selectedFragment = MobileAndEmailFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, selectedFragment as MobileAndEmailFragment)
                .commit()
            tabMobileAndEmail.setOnClickListener(this)
            tabPAN.setOnClickListener(this)
            tabBank.setOnClickListener(this)
            //withdraw_cash()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //0 false 1 true 2 verifying

    private fun withdraw_cash() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@WithdrawCashActivity)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .withdraw_cash(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@WithdrawCashActivity)
                    if (response.response!!.isStatus) {
                        emailVerify = response.response.data.isEmail_verify
                        panVerify = response.response.data.pen_verify
                        bankVerify = response.response.data.bank_account_verify
                        //AppDelegate.showToast(this@WithdrawCashActivity, response.response!!.message)
                        //finish()
                    } else {
                        logoutIfDeactivate(response.response!!.message)
                        AppDelegate.showToast(this@WithdrawCashActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@WithdrawCashActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}