package os.com.ui.splash.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import os.com.AppBase.BaseActivity
import os.com.R


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            try {
                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
                finish()
            } catch (e: Exception) {
            }
        }, 2000)
//        if (BuildConfig.APPLICATION_ID == "os.cashfantasy") {
//            Handler().postDelayed({
//                try {
//                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
//                    finish()
//                } catch (e: Exception) {
//                }
//            }, 1000)
//        } else if (BuildConfig.APPLICATION_ID == "os.crickset") {
//            Handler().postDelayed({
//                try {
//                    startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
//                    finish()
//                } catch (e: Exception) {
//                }
//            }, 1000)
//        }

    }
}
