package os.com.ui.notification.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.content_notifications.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.PrefConstant
import os.com.constant.Tags
import os.com.data.Prefs
import os.com.networkCall.ApiClient
import os.com.ui.notification.adapter.NotificationAdapter
import os.com.ui.notification.apiResponse.notificationResponse.Data
import os.com.utils.AppDelegate
import os.com.utils.AppDelegate.isNetworkAvailable
import os.com.utils.SwipeHelper
import os.com.utils.SwipeHelper.UnderlayButtonClickListener
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class NotificationActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.toolbarClearAll -> {
                showDeleteDialog(getString(R.string.delete_notificaions), 0, true)
            }
        }
    }

    private fun deleteNotification(position: Int, id: String, clearAll: Boolean) {
        try {
            val loginRequest = HashMap<String, String>()
            if (pref!!.isLogin)
                loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
            else
                loginRequest[Tags.user_id] = ""
            loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
            if (clearAll)
                loginRequest[Tags.notification_id] = ""
            else
                loginRequest[Tags.notification_id] = id
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@NotificationActivity)
                val request = ApiClient.client
                    .getRetrofitService()
                    .deleteNotifications(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@NotificationActivity)
                if (response.response!!.status!!) {
                    if (clearAll) {
                        toolbarClearAll.visibility = View.GONE
                        mData = ArrayList()
                        rv_Notifications!!.adapter = NotificationAdapter(this@NotificationActivity, mData)
                        rv_Notifications.visibility = GONE
                        txt_NotFoundData.visibility = View.VISIBLE
                    } else {
                        mData.removeAt(position)
                        rv_Notifications!!.adapter!!.notifyDataSetChanged()
                        try {
                            if (mData.isEmpty()) {
                                toolbarClearAll.visibility = View.GONE
                                rv_Notifications.visibility = GONE
                                txt_NotFoundData.visibility = View.VISIBLE
                            } else {
                                toolbarClearAll.visibility = View.VISIBLE
                                rv_Notifications.visibility = VISIBLE
                                txt_NotFoundData.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                        }
                    }
                    Prefs(this@NotificationActivity).putIntValue(PrefConstant.UNREAD_COUNT, 0)
//                    ShortcutBadger.applyCount(this, Prefs.getInstance(this).getIntValue(PrefConstant.UNREAD_COUNT, 0))

                } else {
                    AppDelegate.showToast(this@NotificationActivity, response.response!!.message!!)
                }
            }
        } catch (exception: Exception) {
            AppDelegate.hideProgressDialog(this@NotificationActivity)
        }
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
        toolbarClearAll.setOnClickListener(this)
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
        else
            loginRequest[Tags.user_id] = ""
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
                    mData = response.response!!.data!!
                    if (mData.size > 0) {
                        setAdapter()
                        toolbarClearAll.visibility = View.VISIBLE
                        rv_Notifications.visibility = View.VISIBLE
                        txt_NotFoundData.visibility = View.GONE
                    } else {
                        toolbarClearAll.visibility = View.GONE
                        rv_Notifications.visibility = View.GONE
                        txt_NotFoundData.visibility = View.VISIBLE
                    }
//                    if (!response.response!!.data!!.isEmpty())
//                        NotificationCountChannel.getInstance()
//                            .notificationCountChannel.send(response.response!!.data!!.size)
//                    else
//                        NotificationCountChannel.getInstance()
//                            .notificationCountChannel.send(151)
                } else {
                    logoutIfDeactivate(response.response!!.message!!)
                    AppDelegate.showToast(this@NotificationActivity, response.response!!.message!!)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@NotificationActivity)
            }
        }
    }

    private var mData = ArrayList<Data>()

    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Notifications!!.layoutManager = llm
        rv_Notifications!!.adapter = NotificationAdapter(this, mData)

        object : SwipeHelper(this, rv_Notifications) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder,
                underlayButtons: MutableList<SwipeHelper.UnderlayButton>
            ) {
                underlayButtons.add(SwipeHelper.UnderlayButton(
                    getString(R.string.delete),
                    0,
                    Color.parseColor("#eb011c"),
                    UnderlayButtonClickListener {
                        //Log.e("Position on delete", it.toString())
                        showDeleteDialog(getString(R.string.delete_notificaions), it, false)
                    }
                ))
            }
        }
    }

    private fun showDeleteDialog(message: String, position: Int, clearAll: Boolean) {
        val logoutAlertDialog = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle).create()
        logoutAlertDialog.setTitle(getString(R.string.app_name))
        logoutAlertDialog.setMessage(message)
        logoutAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes)) { dialog, id ->
            logoutAlertDialog.dismiss()
            if (isNetworkAvailable(this))
                deleteNotification(position, mData[position].id!!, clearAll)
        }
        logoutAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no)) { dialog, id ->
            logoutAlertDialog.dismiss()
            rv_Notifications!!.adapter!!.notifyDataSetChanged()
        }
        logoutAlertDialog.show()
    }

}