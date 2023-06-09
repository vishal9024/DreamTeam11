package os.com.ui.signup.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_otp.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.DashBoardActivity
import os.com.ui.signup.apiRequest.VerifyOtpRequest
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import os.com.utils.otpReceiver.OTPListener
import os.com.utils.otpReceiver.OtpReader


class OTPActivity : BaseActivity(), View.OnClickListener, OTPListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_Submit -> {
                if (otp_view.text.toString().isNotEmpty() && otp_view.text.toString().length == 6) {
                    var getOtp = otp_view.text.toString()
//                    if (getOtp == otp) {
                    if (AppDelegate.isNetworkAvailable(this))
                        prepareData()
//                    } else
//                        AppDelegate.showToast(this, getString(R.string.invalid_otp))
                } else
                    AppDelegate.showToast(this, getString(R.string.invalid_otp))

            }
            R.id.resendOTPLayout -> {
                if (NetworkUtils.isConnected()) {
                    callResendOtpApi()
                } else
                    AppDelegate.showToast(this, getString(R.string.error_network_connection))
            }

        }
    }

    private fun prepareData() {
        val verifyOtpRequest = VerifyOtpRequest()
        verifyOtpRequest.device_id = pref!!.fcMtokeninTemp
        verifyOtpRequest.otp = otp_view.text.toString()
        verifyOtpRequest.language = FantasyApplication.getInstance().getLanguage()
        verifyOtpRequest.device_type = Tags.device_type
        verifyOtpRequest.user_id = user_id
        verifyOtpRequest.is_signup = isSignUp
        callVarifyOtpApi(verifyOtpRequest)
    }

    var otp = ""
    var user_id = ""
    var phone = ""
    var isSignUp = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        initViews()
    }

    override fun otpReceived(smsText: String) {
        otp_view.setText(smsText)
    }

    var from = false
    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        from = intent.getBooleanExtra(IntentConstant.TYPE, false)
        isSignUp = intent.getBooleanExtra(IntentConstant.ISSIGNUP, false)
        toolbarTitleTv.setText(R.string.enter_otp)
        btn_Submit.setOnClickListener(this)
        otp = intent.getStringExtra(IntentConstant.OTP)
        phone = intent.getStringExtra(IntentConstant.MOBILE)
        user_id = intent.getStringExtra(IntentConstant.USER_ID)
        txt_VerifyNumberLabel.text = getString(R.string.otp_has_been_sent_to_your_mobile_number) + " " + phone
        resendOTPLayout.setOnClickListener(this)
        if (phone.contains("+91"))
            OtpReader.bind(this, phone.replace("+91", ""))
        else
            OtpReader.bind(this, phone)
//      otp_view.setText(otp)
        resendOTPLayout.isEnabled = false
//      showOTPDialog(otp)
//      setTimerForOTP()
        GlobalScope.launch(Dispatchers.Main) {
            setTimerForOTP()
        }
    }

    private fun callVarifyOtpApi(verifyOtpRequest: VerifyOtpRequest) {
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@OTPActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .verify_otp(verifyOtpRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@OTPActivity)
                if (response.response!!.status) {
                    AppDelegate.showToast(this@OTPActivity, response.response!!.message)
                    pref!!.userdata = response.response!!.data
                    pref!!.isLogin = true
                    if (from) {
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        startActivity(
                            Intent(
                                this@OTPActivity,
                                DashBoardActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        finish()
                    }
                } else {
                    AppDelegate.showToast(this@OTPActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@OTPActivity)
            }
        }
    }

    private fun callResendOtpApi() {
        val resendOTPRequest = HashMap<String, String>()
        resendOTPRequest["mobile_number"] = phone
        resendOTPRequest["language"] = FantasyApplication.getInstance().getLanguage()
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@OTPActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .resend_otp(resendOTPRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@OTPActivity)
                if (response.response!!.status) {
                    AppDelegate.showToast(this@OTPActivity, response.response!!.message)
//                    otp = response.response!!.data!!.otp
//                    showOTPDialog(response.response!!.data!!.otp)
                    resendOTPLayout.isEnabled = false
                    setTimerForOTP()
                } else {
                    AppDelegate.showToast(this@OTPActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@OTPActivity)
            }
        }
    }

    private suspend fun setTimerForOTP() {
        for (i in 60 downTo 0) {
            val remainingTime = i
            if (i == 0) {
                resendOTPTv.text = getString(R.string.resend_otp)
                resendOTPLayout.isEnabled = true
            } else {
                val resendTxt =
                    getString(R.string.resend_in) + " " + remainingTime.toString() + " " + getString(R.string.sec)
                resendOTPTv.text = resendTxt
            }
            delay(1000)
        }

    }

    private fun showOTPDialog(Otp: String) {
        val logoutAlertDialog = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle).create()
        logoutAlertDialog.setTitle(getString(R.string.app_name))
        logoutAlertDialog.setMessage(Html.fromHtml("Use <B>" + Otp + "</B> as your login OTP."))

        logoutAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok)) { dialog, id ->
            logoutAlertDialog.dismiss()
        }
        logoutAlertDialog.show()
    }
}
