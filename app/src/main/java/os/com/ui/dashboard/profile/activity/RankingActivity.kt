package os.com.ui.dashboard.profile.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.content_notifications.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.ui.dashboard.profile.adapter.RankingAdapter
import os.com.ui.notification.adapter.NotificationAdapter

class RankingActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try{
//        when (view!!.id) {
//            R.id.btn_CreateTeam -> {
//            startActivity(Intent(this, ChooseTeamActivity::class.java))
//        }
//            R.id.txt_Signup -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
//            }
//        }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        initViews()
    }

    private fun initViews() {
        try {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.leaderboard)
        setMenu(false,false,false,false)
        setAdapter()
//        btn_CreateTeam.setOnClickListener(this)
//        txt_Signup.setOnClickListener(this)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        try{
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = RankingAdapter(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}