package os.com.ui.addCash.activity

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_balance.*
import kotlinx.android.synthetic.main.app_toolbar.*
import os.com.AppBase.BaseActivity
import os.com.R

class AddCashActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_addCash -> {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_balance)
        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.add_cash)
        setMenu(false, false, false, false)
        btn_addCash.setOnClickListener(this)
    }
}