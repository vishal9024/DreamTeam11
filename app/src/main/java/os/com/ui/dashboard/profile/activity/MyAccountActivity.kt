package os.com.ui.dashboard.profile.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.activity_my_account.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.utils.AppDelegate
import java.util.*

class MyAccountActivity : BaseActivity(), View.OnClickListener {

    private var accountVerified= false

    override fun onClick(view: View?) {
          try{
              when (view!!.id) {
                  R.id.txtRecTransaction -> {
                      startActivity(Intent(this, RecentTansActivity::class.java))
                  }
                  R.id.cv_payments -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
                  }
                  R.id.txt_Withdraw -> {
                      if(accountVerified){
                          startActivity(Intent(this, WithdrawRequestActivity::class.java))
                      }else{
                          startActivity(Intent(this, WithdrawCashActivity::class.java))
                      }
                  }
                  R.id.imvBonusInfo -> {
                      SimpleTooltip.Builder(this)
                          .anchorView(view)
                          .text(resources.getString(R.string.bonus_info_text))
                          .build()
                          .show()
                  }
                  R.id.imvWinningInfo -> {
                      SimpleTooltip.Builder(this)
                          .anchorView(view)
                          .text(resources.getString(R.string.winning_info_text))
                          .build()
                          .show()
                  }
                  R.id.imvDepositedInfo -> {
                      SimpleTooltip.Builder(this)
                          .anchorView(view)
                          .text(resources.getString(R.string.deposited_info_text))
                          .build()
                          .show()
                  }
              }
              } catch (e: Exception) {
                      e.printStackTrace()
              }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)
        initViews()
    }

    private fun initViews() {
        try{
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.my_account)
        setMenu(false,false,false,false,false)
            if (intent.hasExtra("accountVerified"))
                accountVerified = intent.getBooleanExtra("accountVerified", false)
            txtRecTransaction.setOnClickListener(this)
        cv_payments.setOnClickListener(this)
            txt_Withdraw.setOnClickListener(this)
            imvBonusInfo.setOnClickListener(this)
            imvWinningInfo.setOnClickListener(this)
            imvDepositedInfo.setOnClickListener(this)
            my_account_call()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun my_account_call() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@MyAccountActivity)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .user_account_datail(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response)
                    AppDelegate.hideProgressDialog(this@MyAccountActivity)
                    if (response.response!!.isStatus) {
                        accountVerified=response.response.data.isAccount_verified
                        txtTotalAmount.setText("₹ "+response.response.data.total_balance)
                        txtDepositedAmount.setText("₹ "+response.response.data.deposit_amount)
                        txtWinningsAmount.setText("₹ "+response.response.data.winngs_amount)
                        txtCashBonusAmount.setText("₹ "+response.response.data.bonus)
                    } else {
                        AppDelegate.showToast(this@MyAccountActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@MyAccountActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}