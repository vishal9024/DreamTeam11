package os.com.ui.dashboard.profile.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_change_password.*
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
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class ChangePasswordActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.btn_ChangePassword -> {
                    //old password check
                    if (TextUtils.isEmpty(et_OldPassword.text.toString()))
                        AppDelegate.showToast(this, getString(R.string.old_empty_password))
                    else if (et_OldPassword.text.toString().length < 6)
                        AppDelegate.showToast(this, getString(R.string.old_short_password))
                    else if (!(et_OldPassword.text.toString().matches(".*[A-Za-z]+.*[0-9]+.*".toRegex()) || et_OldPassword.text.toString().matches(
                            ".*[0-9]+.*[A-Za-z]+.*".toRegex()
                        ))
                    )
                        AppDelegate.showToast(this, getString(R.string.invalid_password_2))

                    //new password check
                    else if (TextUtils.isEmpty(et_NewPassword.text.toString()))
                        AppDelegate.showToast(this, getString(R.string.new_empty_password))
                    else if (et_NewPassword.text.toString().length < 6)
                        AppDelegate.showToast(this, getString(R.string.new_short_password))
                    else if (!(et_NewPassword.text.toString().matches(".*[A-Za-z]+.*[0-9]+.*".toRegex()) || et_NewPassword.text.toString().matches(
                            ".*[0-9]+.*[A-Za-z]+.*".toRegex()
                        ))
                    )
                        AppDelegate.showToast(this, getString(R.string.new_invalid_password))

                    //confirm password check
                    else if (TextUtils.isEmpty(et_ConfirmPassword.text.toString()))
                        AppDelegate.showToast(this, getString(R.string.confrim_empty_password))
                    else if (et_ConfirmPassword.text.toString().length < 6)
                        AppDelegate.showToast(this, getString(R.string.confrim_short_password))
                    else if (!(et_ConfirmPassword.text.toString().matches(".*[A-Za-z]+.*[0-9]+.*".toRegex()) || et_ConfirmPassword.text.toString().matches(
                            ".*[0-9]+.*[A-Za-z]+.*".toRegex()
                        ))
                    )
                        AppDelegate.showToast(this, getString(R.string.confirm_invalid_password))
                    else if (et_ConfirmPassword.text.toString() != et_NewPassword.text.toString())
                        AppDelegate.showToast(this, getString(R.string.not_same_password))

                    else {
                        AppDelegate.hideKeyBoard(this)
                        if (NetworkUtils.isConnected()) {
                            savePassword()
                        } else
                            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        initViews()
    }

    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.change_password)
            setMenu(false, false, false, false,false)
            btn_ChangePassword.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun savePassword() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@ChangePasswordActivity)
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
                    AppDelegate.hideProgressDialog(this@ChangePasswordActivity)
                    if (response.response!!.status) {
                        AppDelegate.showToast(this@ChangePasswordActivity, response.response!!.message)
                        finish()
                    } else {
                        logoutIfDeactivate(response.response!!.message)
                        AppDelegate.showToast(this@ChangePasswordActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@ChangePasswordActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}