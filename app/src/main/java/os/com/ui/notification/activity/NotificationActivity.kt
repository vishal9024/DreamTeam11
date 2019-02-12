package os.com.ui.notification.activity

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
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
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.notification.adapter.NotificationAdapter
import os.com.ui.notification.apiResponse.notificationResponse.Data
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class NotificationActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.toolbarClearAll -> {
                deleteNotification("all")
            }
        }
    }

    private fun deleteNotification(type: String) {
//        try {
//            val loginRequest = HashMap<String, String>()
//            if (pref!!.isLogin)
//                loginRequest[Tags.user_id] = pref!!.userdata!!.user_id
//            else
//                loginRequest[Tags.user_id] = ""
//            loginRequest[Tags.language] = FantasyApplication.getInstance().getLanguage()
//            GlobalScope.launch(Dispatchers.Main) {
//                AppDelegate.showProgressDialog(this@NotificationActivity)
//
//                val request = ApiClient.client
//                    .getRetrofitService()
//                    .notification_list(loginRequest)
//                val response = request.await()
//                AppDelegate.LogT("Response=>" + response);
//                AppDelegate.hideProgressDialog(this@NotificationActivity)
//                if (response.response!!.status) {
//                    setAdapter(response.response!!.data)
////                    if (!response.response!!.data!!.isEmpty())
////                        NotificationCountChannel.getInstance()
////                            .notificationCountChannel.send(response.response!!.data!!.size)
////                    else
////                        NotificationCountChannel.getInstance()
////                            .notificationCountChannel.send(151)
//                } else {
//                    AppDelegate.showToast(this@NotificationActivity, response.response!!.message!!)
//                }
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
        toolbarClearAll.setOnClickListener(this)
        initSwipe()
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
            loginRequest[Tags.user_id]= ""
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
    private fun setAdapter(data: ArrayList<Data>?) {
        mData = data!!
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Notifications!!.layoutManager = llm
        rv_Notifications!!.adapter = NotificationAdapter(this, data)
    }

    private val p = Paint()
    private var intentPn = 0
    private fun initSwipe() {
        try {
            val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
//                      val id = mData!!.get(position).getId()
                    if (direction == ItemTouchHelper.LEFT) {
//                    editMessageData(id, "single")
                    } else if (direction == ItemTouchHelper.RIGHT) {
//                    addNewDialog(clickListener, position)
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val icon: Bitmap
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        val itemView = viewHolder.itemView
                        val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                        val width = height / 3

                        if (dX > 0) {
                            p.setColor(resources.getColor(R.color.colorASPrimary))
                            val background =
                                RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                            c.drawRect(background, p)
                            icon = BitmapFactory.decodeResource(resources, R.mipmap.editwhite)
                            val icon_dest = RectF(
                                itemView.left.toFloat() + width,
                                itemView.top.toFloat() + width,
                                itemView.left.toFloat() + 2 * width,
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, icon_dest, p)
                        } else {
                            p.setColor(resources.getColor(R.color.colorASPrimary))
                            val background = RectF(
                                itemView.right.toFloat() + dX,
                                itemView.top.toFloat(),
                                itemView.right.toFloat(),
                                itemView.bottom.toFloat()
                            )
                            c.drawRect(background, p)
                            icon = BitmapFactory.decodeResource(resources, R.mipmap.delete)
                            val icon_dest = RectF(
                                itemView.right.toFloat() - 2 * width,
                                itemView.top.toFloat() + width,
                                itemView.right.toFloat() - width,
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, icon_dest, p)
                        }
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(rv_Notifications!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}