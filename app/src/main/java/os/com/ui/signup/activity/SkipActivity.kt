package os.com.ui.signup.activity

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_skip.*
import kotlinx.android.synthetic.main.app_toolbar.*
import os.com.AppBase.BaseActivity
import os.com.R


class SkipActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.btn_Join -> {
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skip)
        initViews()
    }


    private fun initViews() {
        try {
            toolbarTitleTv.setText("")
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            btn_Join.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


//    private fun callJoinContestApi() {
//        try {
//            GlobalScope.launch(Dispatchers.Main) {
//                AppDelegate.showProgressDialog(this@SkipActivity)
//                try {
//                    val request = ApiClient.client
//                        .getRetrofitService()
//                        .signup()
//                    val response = request.await()
//                    AppDelegate.LogT("Response=>" + response);
//                    AppDelegate.hideProgressDialog(this@SkipActivity)
//                    if (response.response!!.status) {
//                        AppDelegate.showToast(this@SkipActivity, response.response!!.message)
//                        startActivity(
//                            Intent(this@SkipActivity, OTPActivity::class.java)
//                                .putExtra(IntentConstant.OTP, response.response!!.data!!.otp)
//                                .putExtra(IntentConstant.MOBILE, response.response!!.data!!.phone)
//                                .putExtra(IntentConstant.USER_ID, response.response!!.data!!.user_id)
//                        )
//                    } else {
//                        AppDelegate.showToast(this@SkipActivity, response.response!!.message)
//                    }
//                } catch (exception: Exception) {
//                    AppDelegate.hideProgressDialog(this@SkipActivity)
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }

}
