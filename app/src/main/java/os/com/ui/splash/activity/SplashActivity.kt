package os.com.ui.splash.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.ui.dashboard.DashBoardActivity
import os.com.utils.AppDelegate


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            try {
                when {
                    !pref!!.isLogin  -> startActivity(Intent(this, WelcomeActivity::class.java))
                    else -> startActivity(Intent(this, DashBoardActivity::class.java))
                }
                finish()

            } catch (e: Exception) {
                AppDelegate.showToast(this,e.printStackTrace().toString())
            }
        }, 1000)
    }
}
