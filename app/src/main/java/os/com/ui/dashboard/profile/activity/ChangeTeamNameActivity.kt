package os.com.ui.dashboard.profile.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_team_name.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class ChangeTeamNameActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.btn_ChangeTeamName -> {
                    //confirm password check
                   if (TextUtils.isEmpty(et_TeamName.text.toString()))
                        AppDelegate.showToast(this, getString(R.string.empty_team_name))
                    else if (et_TeamName.text.toString().length < 6)
                        AppDelegate.showToast(this, getString(R.string.short_team_name))
                    else {
                        AppDelegate.hideKeyBoard(this)
                        if (NetworkUtils.isConnected()) {
                            saveTeamName()
                        } else
                            Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_name)
        initViews()
    }

    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.select_your_team_name)
            setMenu(false, false, false, false,false)
            txt_teamname_info.text= getString( R.string.your_team_name_is_what_you)+" "+getString(R.string.app_name)+getString(R.string.choose_wisely)

            btn_ChangeTeamName.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveTeamName() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@ChangeTeamNameActivity)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.team_name] = et_TeamName.text.toString()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .edit_user_team_name(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@ChangeTeamNameActivity)
                    if (response.response!!.status) {
                        AppDelegate.showToast(this@ChangeTeamNameActivity, response.response!!.message)
                        finish()
                    } else {
                        AppDelegate.showToast(this@ChangeTeamNameActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@ChangeTeamNameActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}