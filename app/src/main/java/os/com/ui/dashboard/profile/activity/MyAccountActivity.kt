package os.com.ui.dashboard.profile.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_my_account.*
import kotlinx.android.synthetic.main.app_toolbar.*
import os.com.AppBase.BaseActivity
import os.com.R

class MyAccountActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.cv_transactions -> {
            startActivity(Intent(this, RecentTansActivity::class.java))
        }
            R.id.cv_payments -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)
        initViews()
    }

    private fun initViews() {
        try{
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.my_account)
        setMenu(false,false,false,false,false)
        cv_transactions.setOnClickListener(this)
        cv_payments.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}