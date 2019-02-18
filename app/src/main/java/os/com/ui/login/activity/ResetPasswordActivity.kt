package os.com.ui.login.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_forget_password.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class ResetPasswordActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.btn_RestPassword -> {
                    //email check
                    if (!mEmail.isEmpty()) {
                        if (NetworkUtils.isConnected()) {
                            callForgotPasswordApi()
                        } else
                            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
                    }
                }
                R.id.txt_ResendEmail -> {
                    //email check
                    if (!TextUtils.isEmpty(mEmail)) {
                        if (NetworkUtils.isConnected()) {
                            callForgotPasswordApi()
                        } else
                            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
                    }
                }
                R.id.txt_LoginWithMobile -> {
                    onBackPressed()
                    finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        initViews()
    }

    private var mEmail = ""
    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.reset_password)
            setMenu(false, false, false, false, false)
            if (intent.hasExtra("email"))
                mEmail = intent.getStringExtra("email")
            txt_EmailId.text = mEmail
            txt_MailId.text = mEmail
            btn_RestPassword.setOnClickListener(this)
            txt_LoginWithMobile.setOnClickListener(this)
            txt_ResendEmail.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callForgotPasswordApi() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@ResetPasswordActivity)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.email] = mEmail
                    val request = ApiClient.client
                        .getRetrofitService()
                        .forgot_password(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@ResetPasswordActivity)
                    if (response.response!!.status!!) {
                        AppDelegate.showToast(this@ResetPasswordActivity, response.response!!.message!!)
                        ll_success.visibility = VISIBLE
                        ll_main.visibility = GONE
                    } else {
                        ll_success.visibility = GONE
                        ll_main.visibility = VISIBLE
                        AppDelegate.showToast(this@ResetPasswordActivity, response.response!!.message!!)
                        logoutIfDeactivate(response.response!!.message!!)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@ResetPasswordActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}