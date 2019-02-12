package os.com.ui.invite.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_invite_friend_detail.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.ui.dashboard.profile.adapter.FriendListAdapter
import os.com.ui.invite.apiResponse.InviteFreindDetailResponse


class InviteFriendDetailActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
//            R.id.tv_how_it_work -> {
//                val intent = Intent(this, WebViewActivity::class.java)
//                intent.putExtra("PAGE_SLUG", "How it work")
//                intent.putExtra("URL", ApiConstant.getWebViewUrl()+ ApiConstant.how_it_works_tab)
//                startActivity(intent)
//            }
            R.id.imvEarnedInfo -> {
                SimpleTooltip.Builder(this)
                    .anchorView(view)
                    .text(resources.getString(R.string.earned_info_text))
                    .build()
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_friend_detail)
        initViews()
    }

    private var mData: InviteFreindDetailResponse.ResponseBean.DataBean? = null

    private fun initViews() {
        try {

            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.invited_friends)
            setMenu(false, false, false, false, false)
            imvEarnedInfo.setOnClickListener(this)
            if (intent.hasExtra("data"))
                mData = intent.getSerializableExtra("data") as InviteFreindDetailResponse.ResponseBean.DataBean?
            if (mData != null) {
                setData(mData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setData(data: InviteFreindDetailResponse.ResponseBean.DataBean?) {
        try {
            if (data != null) {
                var to_be_earnd = 0.0F
                var total_earnd = 0.0F
                var total_fields = 0.0F
                if (data.to_be_earnd != null) {
                    txtToBeEarnAmount.text = "₹ " + data.to_be_earnd
                    to_be_earnd = data.to_be_earnd.toFloat()
                }
                if (data.total_earnd != null) {
                    txtTotalAmount.text = "₹ " + data.total_earnd
                    total_earnd = data.total_earnd.toFloat()
                }
                if (data.total_fields != null) {
                    if (data.total_fields > 0) {
                        if (data.total_fields == 1)
                            txtFriendCount.text = "" + data.total_fields + " Friend Joined!"
                        else txtFriendCount.text = "" + data.total_fields + " Friends Joined!"
                        total_fields = data.total_fields.toFloat()
                    }
                }
                try {
                    var result = (to_be_earnd * total_fields) / total_earnd
                    crs_Progress.setMinValue(0f)
                    crs_Progress.setMaxValue(to_be_earnd)
                    crs_Progress.setMinStartValue(0f)
                    crs_Progress.setMaxStartValue(to_be_earnd / result)
                    crs_Progress.apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (data.total_earnd != null)
                    txtReceivedAmount.text = "₹ " + data.total_earnd
                if (data.to_be_earnd != null)
                    txtEarnAmount.text = "₹ " + (data.to_be_earnd * data.total_fields)
                if (data.friend_detail != null && data.friend_detail.size > 0)
                    setAdapter(data.friend_detail)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("WrongConstant")
    private fun setAdapter(list: MutableList<InviteFreindDetailResponse.ResponseBean.DataBean.FriendDetailBean>) {
        try {
            val llm = LinearLayoutManager(this)
            llm.orientation = LinearLayoutManager.VERTICAL
            rv_Contest!!.layoutManager = llm
            rv_Contest!!.adapter = FriendListAdapter(this, list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}