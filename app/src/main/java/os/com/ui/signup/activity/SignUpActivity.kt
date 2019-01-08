package os.com.ui.signup.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_signup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.model.SocialModel
import os.com.networkCall.ApiClient
import os.com.ui.signup.apiRequest.SignUpRequest
import os.com.utils.AppDelegate
import os.com.utils.ValidationUtil
import os.com.utils.networkUtils.NetworkUtils


class SignUpActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_Register -> {
                if (userData != null)
                    checkValidationSocial()
                else
                    checkValidation()

            }
            R.id.txt_Login -> {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        initViews()
    }

    var userData: SocialModel? = null

    private fun initViews() {
        toolbarTitleTv.setText(R.string.sign_up)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        btn_Register.setOnClickListener(this)
        txt_Login.setOnClickListener(this)
        try {
            userData = intent.getParcelableExtra(IntentConstant.DATA)
        } catch (e: Exception) {

        }

        if (userData != null) {
            et_Email.setText(userData!!.email_address)
            if (!userData!!.email_address.isEmpty())
                et_Email.isEnabled = false
            et_Password.visibility = View.GONE
        }
    }

    private fun checkValidationSocial() {
        if (et_Mobile.text.toString().isEmpty())
            AppDelegate.showToast(this, getString(R.string.enter_phone_number))
        else if (!ValidationUtil.isPhoneValid(et_Mobile.text.toString()))
            AppDelegate.showToast(this, getString(R.string.valid_phone_number))
        else if (et_Email.text.toString().isEmpty())
            AppDelegate.showToast(this, getString(R.string.enter_email))
        else if (!ValidationUtil.isEmailValid(et_Email.text.toString()))
            AppDelegate.showToast(this, getString(R.string.valid_email))
        else {
            AppDelegate.hideKeyBoard(this)
            if (NetworkUtils.isConnected()) {
                prepareData(true)
            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
        }
    }


    private fun checkValidation() {
        if (et_Mobile.text.toString().isEmpty())
            AppDelegate.showToast(this, getString(R.string.enter_phone_number))
        else if (!ValidationUtil.isPhoneValid(et_Mobile.text.toString()))
            AppDelegate.showToast(this, getString(R.string.valid_phone_number))
        else if (et_Email.text.toString().isEmpty())
            AppDelegate.showToast(this, getString(R.string.enter_email))
        else if (!ValidationUtil.isEmailValid(et_Email.text.toString()))
            AppDelegate.showToast(this, getString(R.string.valid_email))
        else if (et_Password.text.toString().length < 6)
            AppDelegate.showToast(this, getString(R.string.short_password))
        else if (!(et_Password.text.toString().matches(".*[A-Za-z]+.*[0-9]+.*".toRegex()) || et_Password.text.toString().matches(
                ".*[0-9]+.*[A-Za-z]+.*".toRegex()
            ))
        )
            AppDelegate.showToast(this, getString(R.string.invalid_password))
        else {
            AppDelegate.hideKeyBoard(this)
            if (NetworkUtils.isConnected()) {
                prepareData(false)

            } else
                Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
        }
    }

    private fun prepareData(isSocial: Boolean) {
        val signUpRequest = SignUpRequest()
        signUpRequest.invite_code = et_EnviteCode.text.toString()
        signUpRequest.name = ""
        signUpRequest.mobile_number = et_Mobile.text.toString()
        signUpRequest.email = et_Email.text.toString()
        signUpRequest.password = et_Password.text.toString()
        signUpRequest.language = FantasyApplication.getInstance().getLanguage()
        if (isSocial) {
            signUpRequest.fb_id = userData!!.fb_id
            signUpRequest.google_id = userData!!.google_id
            signUpRequest.name = userData!!.first_name
            callSocialSignUpApi(signUpRequest)
        } else
            callSignUpApi(signUpRequest)
    }

    private fun callSignUpApi(signUpRequest: SignUpRequest) {
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@SignUpActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .signup(signUpRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@SignUpActivity)
                if (response.response!!.status) {
                    AppDelegate.showToast(this@SignUpActivity, response.response!!.message)
                    startActivity(
                        Intent(this@SignUpActivity, OTPActivity::class.java)
                            .putExtra(IntentConstant.OTP, response.response!!.data!!.otp)
                            .putExtra(IntentConstant.MOBILE, response.response!!.data!!.phone)
                            .putExtra(IntentConstant.USER_ID, response.response!!.data!!.user_id)
                    )
                    finish()
                } else {
                    AppDelegate.showToast(this@SignUpActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@SignUpActivity)
            }
        }
    }

    private fun callSocialSignUpApi(signUpRequest: SignUpRequest) {
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@SignUpActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .social_signup(signUpRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@SignUpActivity)
                if (response.response!!.status) {
                    AppDelegate.showToast(this@SignUpActivity, response.response!!.message)
                    startActivity(
                        Intent(this@SignUpActivity, OTPActivity::class.java)
                            .putExtra(IntentConstant.OTP, response.response!!.data!!.otp)
                            .putExtra(IntentConstant.MOBILE, response.response!!.data!!.phone)
                            .putExtra(IntentConstant.USER_ID, response.response!!.data!!.user_id)
                    )
                    finish()
                } else {
                    AppDelegate.showToast(this@SignUpActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@SignUpActivity)
            }
        }
    }
}
