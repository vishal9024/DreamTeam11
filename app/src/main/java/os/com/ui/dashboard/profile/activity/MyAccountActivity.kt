package os.com.ui.dashboard.profile.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
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
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.txtRecTransaction -> {
            startActivity(Intent(this, RecentTansActivity::class.java))
        }
            R.id.cv_payments -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
            }
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
            txtRecTransaction.setOnClickListener(this)
        cv_payments.setOnClickListener(this)
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