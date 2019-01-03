package os.com.ui.dashboard.profile.activity

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_my_account.*
import kotlinx.android.synthetic.main.app_toolbar.*
import os.com.AppBase.BaseActivity
import os.com.R

class ChangePasswordActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_ChangePassword -> {
//            startActivity(Intent(this, ChooseTeamActivity::class.java))
        }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        initViews()
    }

    private fun initViews() {
        try{
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.change_password)
        setMenu(false,false,false,false)
        btn_ChangePassword.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}