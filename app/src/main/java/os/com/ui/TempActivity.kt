package os.com.ui


import android.os.Bundle
import kotlinx.android.synthetic.main.app_toolbar.*
import os.com.AppBase.BaseActivity
import os.com.R




class TempActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)
        initView()
    }


    override fun onBackPressed() {
        finish()
    }

    private fun initView() {
        try {
            setSupportActionBar(toolbar)
            toolbarTitleTv.setText("Terms and conditions");
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
