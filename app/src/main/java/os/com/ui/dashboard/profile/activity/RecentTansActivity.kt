package os.com.ui.dashboard.profile.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_recent_trans.*
import kotlinx.android.synthetic.main.app_toolbar.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.profile.adapter.RecentTransAdapter
import os.com.ui.dashboard.profile.apiResponse.RecentTransactionResponse
import os.com.utils.AppDelegate
import java.util.*

class RecentTansActivity : BaseActivity(), View.OnClickListener {
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
        setContentView(R.layout.activity_recent_trans)
        initViews()
    }

    private fun initViews() {
        try {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.recent_transections)
        setMenu(false,false,false,false,false)
            rec_transaction_call()
//        btn_CreateTeam.setOnClickListener(this)
//        txt_Signup.setOnClickListener(this)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun rec_transaction_call() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@RecentTansActivity)
                try {
                    var map = HashMap<String, String>()
                    if (pref!!.isLogin)
                        map[Tags.user_id] = pref!!.userdata!!.user_id
                    else
                        map[Tags.user_id]= ""
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .transation_history(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response)
                    AppDelegate.hideProgressDialog(this@RecentTansActivity)
                    if (response.response!!.isStatus) {
                        if (response.response!!.data!=null && response.response!!.data!!.size>0) {
                            setAdapter(response.response!!.data)
                            rv_Contest.visibility=View.VISIBLE
                            txt_NotFoundData.visibility=View.GONE
                        }else{
                            rv_Contest.visibility=View.GONE
                            txt_NotFoundData.visibility=View.VISIBLE
                        }
                    } else {
                        logoutIfDeactivate(response.response!!.message)
                        AppDelegate.showToast(this@RecentTansActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@RecentTansActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(data: MutableList<RecentTransactionResponse.ResponseBean.DataBean>) {
        try{
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = RecentTransAdapter(this,data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}