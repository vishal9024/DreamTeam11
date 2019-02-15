package os.com.ui.login.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_change_password.*
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
                    if (!TextUtils.isEmpty(mEmail)){
                        if (NetworkUtils.isConnected()) {
                            savePassword()
                        } else
                            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
                    }
                }
                R.id.txt_ResendEmail -> {
                    //email check
                    if (!TextUtils.isEmpty(mEmail)){
                        if (NetworkUtils.isConnected()) {
                            savePassword()
                        } else
                            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
                    }
                }
                R.id.txt_LoginWithMobile -> {
                    startActivity(Intent(this, LoginActivity::class.java))
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

    private var mEmail=""
    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.reset_password)
            setMenu(false, false, false, false,false)
            if (intent.hasExtra("email"))
                mEmail = intent.getStringExtra("email")
            txt_EmailId.text=mEmail
            btn_RestPassword.setOnClickListener(this)
            txt_LoginWithMobile.setOnClickListener(this)
            txt_ResendEmail.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun savePassword() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@ResetPasswordActivity)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.password] = et_ConfirmPassword.text.toString()
                    map[Tags.old_password] = et_OldPassword.text.toString()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .change_pasword(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@ResetPasswordActivity)
                    if (response.response!!.status) {
                        AppDelegate.showToast(this@ResetPasswordActivity, response.response!!.message)
                        finish()
                    } else {
                        AppDelegate.showToast(this@ResetPasswordActivity, response.response!!.message)
                        logoutIfDeactivate(response.response!!.message)
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