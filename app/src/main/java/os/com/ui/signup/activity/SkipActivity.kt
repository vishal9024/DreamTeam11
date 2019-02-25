package os.com.ui.signup.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_skip.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.networkCall.ApiConstant
import os.com.ui.createTeam.apiRequest.CreateTeamRequest
import os.com.ui.dashboard.more.activity.WebViewActivity
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils


class SkipActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.btn_Join -> {
                    startActivityForResult(
                        Intent(this@SkipActivity, SignUpActivity::class.java)
                            .putExtra(IntentConstant.TYPE, true)
                        , AppRequestCodes.SIGNUP
                    )
                }
                R.id.txtFullPointSystem->{
                    val intent = Intent(this, WebViewActivity::class.java)
                    intent.putExtra("PAGE_SLUG", "Fantasy Point System")
                    intent.putExtra("URL", ApiConstant.getWebViewUrl() + ApiConstant.point_system)
                    startActivity(intent)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppRequestCodes.SIGNUP && resultCode == Activity.RESULT_OK) {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skip)
        initViews()
    }


    private fun initViews() {
        try {
            toolbarTitleTv.setText(getString(R.string.join_contest))
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            btn_Join.setOnClickListener(this)
            txtFullPointSystem.setOnClickListener(this)
            getIntentData()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    var creteTeamRequest: CreateTeamRequest = CreateTeamRequest()
    var date: String = ""
    var time: String = ""
    private fun getIntentData() {
        creteTeamRequest = intent.getParcelableExtra(IntentConstant.DATA)
        date = intent.getStringExtra("DATE")
        time = intent.getStringExtra("TIME")
        try {
            var strt_date = date.split("T")
            date = AppDelegate.convertTimeFormat(strt_date.get(0), "yyyy-MM-dd", "dd MMM, yyyy")
            time = AppDelegate.convertTimeFormat(time, "HH:mm", "hh:mm a")
        } catch (e: Exception) {

        }
        txt_Date.setText(date + ", " + time)
        if (NetworkUtils.isConnected()) {
            callJoinContestApi()
        } else
            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()

    }

    private fun callJoinContestApi() {
        try {
            val map = HashMap<String, String>()
            if (pref!!.isLogin)
                map[Tags.user_id] = pref!!.userdata!!.user_id
            else
                map[Tags.user_id] = ""
            map[Tags.language] = FantasyApplication.getInstance().getLanguage()
            map[Tags.match_id] = creteTeamRequest.match_id/*"13071965317"*/
            map[Tags.series_id] = creteTeamRequest.series_id/*"13071965317"*/

            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@SkipActivity)
                try {
                    val request = ApiClient.client
                        .getRetrofitService()
                        .befor_join_contest(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@SkipActivity)
                    if (response.response!!.status) {
                        txt_runPoints.setText(response.response!!.data!!.run + " " + getString(R.string.points))
                        txtCatchPoints.setText(response.response!!.data!!.catch + " " + getString(R.string.points))
                        txtWicketPoints.setText(response.response!!.data!!.wicket + " " + getString(R.string.points))
                    } else {
                        AppDelegate.showToast(this@SkipActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@SkipActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
