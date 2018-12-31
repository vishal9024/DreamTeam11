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
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            R.id.txt_Login -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.btn_LetsPlay -> {
                var intent=(Intent(this, DashBoardActivity::class.java))
//                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, img_AppIcon , "img_logo")
                startActivity(intent/*, options.toBundle()*/)
                finish()
            }
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
    }
}
