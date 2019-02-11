package os.com.ui.splash.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.ui.dashboard.DashBoardActivity
import os.com.utils.AppDelegate


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkPermission()
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this!!,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS
                ), 10
            )
        } else {
            gotoNext()
        }
    }

    fun gotoNext() {
        Handler().postDelayed({
            try {
                when {
                    !pref!!.isLogin -> startActivity(Intent(this, WelcomeActivity::class.java))
                    else -> startActivity(Intent(this, DashBoardActivity::class.java))
                }
                finish()

            } catch (e: Exception) {
                AppDelegate.showToast(this, e.printStackTrace().toString())
            }
        }, 1000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        gotoNext()
    }
}
