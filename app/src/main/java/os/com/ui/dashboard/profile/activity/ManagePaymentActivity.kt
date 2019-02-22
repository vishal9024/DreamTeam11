package os.com.ui.dashboard.profile.activity

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_manage_payment.*
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

class ManagePaymentActivity : BaseActivity(), View.OnClickListener {

    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.ll_paytm -> {
//                    startActivity(Intent(this, RecentTansActivity::class.java))
                }
                R.id.ll_cashfree -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_payment)
        initViews()
    }
    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.manage_payment)
            setMenu(false, false, false, false, false)
            ll_paytm.setOnClickListener(this)
            ll_cashfree.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun my_account_call() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@ManagePaymentActivity)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .user_account_datail(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response)
                    AppDelegate.hideProgressDialog(this@ManagePaymentActivity)
                    if (response.response!!.isStatus) {

                    } else {
                        logoutIfDeactivate(response.response!!.message)
                        AppDelegate.showToast(this@ManagePaymentActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@ManagePaymentActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}