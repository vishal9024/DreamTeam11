package os.com.ui.login.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_password.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.DashBoardActivity
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class PasswordActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.btn_Login -> {
                    // password check
                    if (TextUtils.isEmpty(et_password.text.toString()))
                        AppDelegate.showToast(this, getString(R.string.empty_password))
                    else if (et_password.text.toString().length < 6)
                        AppDelegate.showToast(this, getString(R.string.invalid_password_2))
                    else if (!(et_password.text.toString().matches(".*[A-Za-z]+.*[0-9]+.*".toRegex()) || et_password.text.toString().matches(
                            ".*[0-9]+.*[A-Za-z]+.*".toRegex()
                        ))
                    )
                        AppDelegate.showToast(this, getString(R.string.invalid_password_2))
                    else {
                        AppDelegate.hideKeyBoard(this)
                        if (NetworkUtils.isConnected()) {
                            savePassword()
                        } else
                            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
                    }
                }
                R.id.txt_ForgetPassword -> {
                    if (!TextUtils.isEmpty(mEmail)) {
                        startActivity(Intent(this, ResetPasswordActivity::class.java).putExtra("email", mEmail))
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
        setContentView(R.layout.activity_password)
        initViews()
    }

    private var mEmail = ""
    var from = false

    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
//            toolbarTitleTv.setText(R.string.change_password)
            setMenu(false, false, false, false, false)
            from = intent.getBooleanExtra(IntentConstant.TYPE, false)
            if (intent.hasExtra("email"))
                mEmail = intent.getStringExtra("email")
            txt_EmailId.text = mEmail
            btn_Login.setOnClickListener(this)
            txt_ForgetPassword.setOnClickListener(this)
            txt_LoginWithMobile.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun savePassword() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@PasswordActivity)
                try {
                    var map = HashMap<String, String>()
                    map["email"] = mEmail
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.password] = et_password.text.toString()
                    map["device_id"] = pref!!.fcMtokeninTemp
                    map["device_type"] = Tags.device_type
                    val request = ApiClient.client
                        .getRetrofitService()
                        .login_password(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response)
                    AppDelegate.hideProgressDialog(this@PasswordActivity)
                    if (response.response!!.status) {
                        AppDelegate.showToast(this@PasswordActivity, response.response!!.message)
                        pref!!.userdata = response.response!!.data
                        pref!!.isLogin = true
                        if (from){
                            val intent = Intent()
                            setResult(Activity.RESULT_OK,intent)
                            finish()
                        }else {
                            startActivity(
                                Intent(
                                    this@PasswordActivity,
                                    DashBoardActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
                        }
                    } else {
                        AppDelegate.showToast(this@PasswordActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@PasswordActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}