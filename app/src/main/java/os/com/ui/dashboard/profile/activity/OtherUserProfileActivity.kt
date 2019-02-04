package os.com.ui.dashboard.profile.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_other_user_profile.*
import kotlinx.android.synthetic.main.toolbar_other_user_profile.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.ui.dashboard.profile.adapter.PerformanceAdapter

class OtherUserProfileActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
          try{
              when (view!!.id) {
                  R.id.cv_transactions -> {
                      startActivity(Intent(this, RecentTansActivity::class.java))
                  }
                  R.id.cv_payments -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
                  }
              }
          } catch (e: Exception) {
              e.printStackTrace()
          }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user_profile)
        initViews()
    }

    private fun initViews() {
        try{
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
//            tvtitle.setText(R.string.my_account)
        setMenu(false,false,false,false,false)
            setAdapter()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        try{
            val llm = LinearLayoutManager(this)
            llm.orientation = LinearLayoutManager.VERTICAL
            rv_List!!.layoutManager = llm
            rv_List!!.adapter = PerformanceAdapter(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}