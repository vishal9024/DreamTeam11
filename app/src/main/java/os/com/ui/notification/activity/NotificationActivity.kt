package os.com.ui.notification.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.content_notifications.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.channel.NotificationCountChannel
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.notification.adapter.NotificationAdapter
import os.com.ui.notification.apiResponse.notificationResponse.Data
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class NotificationActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
//        when (view!!.id) {
//            R.id.btn_CreateTeam -> {
//            startActivity(Intent(this, ChooseTeamActivity::class.java))
//        }
//            R.id.txt_Signup -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
//            }
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.notification)
        setMenu(false, false, false, false, false)
        if (NetworkUtils.isConnected()) {
            callGetNotificationListApi()
        } else
            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()

//        btn_CreateTeam.setOnClickListener(this)
//        txt_Signup.setOnClickListener(this)
    }

    public fun callGetNotificationListApi() {
/* callApi = false */
        val loginRequest = HashMap<String, String>()
        if (pref!!.isLogin)
            loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
        loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@NotificationActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .notification_list(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@NotificationActivity)
                if (response.response!!.status) {
                    setAdapter(response.response!!.data)
                    GlobalScope.launch {
                        NotificationCountChannel.getInstance()
                            .notificationCountChannel.send(response.response!!.data!!.size)
                    }
                } else {
                    AppDelegate.showToast(this@NotificationActivity, response.response!!.message!!)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@NotificationActivity)
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(data: ArrayList<Data>?) {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Notifications!!.layoutManager = llm
        rv_Notifications!!.adapter = NotificationAdapter(this, data)
    }
}