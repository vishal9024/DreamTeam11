package os.com.ui.splash.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_welcome.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.ui.dashboard.DashBoardActivity
import os.com.ui.login.activity.LoginActivity
import os.com.ui.signup.activity.SignUpActivity


class WelcomeActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.txt_EnterCode -> {
                txt_EnterCode.isEnabled = false
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            R.id.txt_referralCode -> {
                txt_referralCode.isEnabled = false
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            R.id.txt_Login -> {
                txt_Login.isEnabled = false
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.txt_AlreadyUser -> {
                txt_AlreadyUser.isEnabled = false
                startActivity(Intent(this, LoginActivity::class.java))
            }

            R.id.btn_LetsPlay -> {
                btn_LetsPlay.isEnabled = false
                val intent = (Intent(this, DashBoardActivity::class.java))
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        txt_EnterCode.isEnabled = true
        txt_Login.isEnabled = true
        btn_LetsPlay.isEnabled = true
        txt_AlreadyUser.isEnabled = true
        txt_referralCode.isEnabled = true
        if (pref!!.isLogin) {
            finishAffinity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        initViews()
    }

    private fun initViews() {
        txt_EnterCode.setOnClickListener(this)
        txt_Login.setOnClickListener(this)
        btn_LetsPlay.setOnClickListener(this)
        txt_AlreadyUser.setOnClickListener(this)
        txt_referralCode.setOnClickListener(this)
    }
}
