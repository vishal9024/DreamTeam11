package os.com.ui.dashboard.profile.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.activity_withraw_request.*
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

class WithdrawRequestActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.btWithdrawNow -> {
                    if(TextUtils.isEmpty(edtWithdrawAmount.text.toString().trim())){
                        Toast.makeText(this@WithdrawRequestActivity!!, getString(R.string.enter_email_to_proceed), Toast.LENGTH_LONG).show()
                    }else{
                        withdraw_request_call(edtWithdrawAmount.text.toString().trim())
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withraw_request)
        initViews()
    }

    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.withdraw)
            setMenu(false, false, false, false, false)
            bank_details()
            btWithdrawNow.setOnClickListener(this)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun bank_details() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@WithdrawRequestActivity)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .bank_details(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response)
                    AppDelegate.hideProgressDialog(this@WithdrawRequestActivity)
                    if (response.response!!.isStatus) {
                        txtWithdrawBankName.setText(response.response.data.bank_name)
                        txtWithdrawAccountNumber.setText("A/C "+response.response.data.account_no)
                        txtTotalWonnings.setText("₹ "+response.response.data.winning_amount)
                    } else {
                        AppDelegate.showToast(this@WithdrawRequestActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@WithdrawRequestActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun withdraw_request_call(amount: String) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@WithdrawRequestActivity)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.withdraw_amount] = amount
                    val request = ApiClient.client
                        .getRetrofitService()
                        .transation_history(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response)
                    AppDelegate.hideProgressDialog(this@WithdrawRequestActivity)
                    if (response.response!!.isStatus) {
                        AppDelegate.showToast(this@WithdrawRequestActivity, response.response!!.message)
                        finish()
                    } else {
                        AppDelegate.showToast(this@WithdrawRequestActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@WithdrawRequestActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}