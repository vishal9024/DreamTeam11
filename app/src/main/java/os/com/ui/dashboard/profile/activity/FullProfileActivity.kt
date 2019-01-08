package os.com.ui.dashboard.profile.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_full_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.profile.adapter.CustomSpinnerAdapter
import os.com.ui.dashboard.profile.apiResponse.PersonalDetailResponse
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*
import kotlin.collections.ArrayList

class FullProfileActivity : BaseActivity(), View.OnClickListener {

    private var stateList = ArrayList<String>()
    private var state = ""
    private var genderType = true
    private val dobCalendar = Calendar.getInstance()
    private var dob: String? = null
    private var mAge: Int = 0

    internal var fromDate: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            dobCalendar.set(Calendar.YEAR, year)
            dobCalendar.set(Calendar.MONTH, monthOfYear)
            dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            et_dob.setText("" +dayOfMonth+ "-" + monthOfYear + 1 + "-" + year);
//            dob = state.toString() + "-" + Util.setZeroBeforeNine(monthOfYear + 1) + "-" +
//                    Util.setZeroBeforeNine(dayOfMonth)

        }


    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.ll_male -> {
                    ll_male.background = resources.getDrawable(R.drawable.button_rounded_background_gender_selected)
                    imv_male.setImageResource(R.mipmap.maleselect)
                    ll_female.background = resources.getDrawable(R.drawable.button_rounded_background_gender_white)
                    imv_female.setImageResource(R.mipmap.female)
                    genderType = true
                }
                R.id.ll_female -> {
                    ll_male.background = resources.getDrawable(R.drawable.button_rounded_background_gender_white)
                    imv_male.setImageResource(R.mipmap.male)
                    ll_female.background = resources.getDrawable(R.drawable.button_rounded_background_gender_selected)
                    imv_female.setImageResource(R.mipmap.femaleselect)
                    genderType = false
                }
                R.id.txt_change -> {
                    startActivity(Intent(this, ChangePasswordActivity::class.java))
                }
                R.id.btn_UpdateProfile -> {
                    AppDelegate.hideKeyBoard(this)
                    updatePersonalDetail()
                }
                R.id.et_dob -> {
                    val fromDateDialog = DatePickerDialog(
                        this,
                        fromDate,
                        dobCalendar.get(Calendar.YEAR),
                        dobCalendar.get(Calendar.MONTH),
                        dobCalendar.get(Calendar.DAY_OF_MONTH)
                    )
                    fromDateDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
                    fromDateDialog.show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_profile)
        initViews()

    }

    private fun initState() {
        try {
            val stateArray = resources.getStringArray(R.array.state)
            for (i in stateArray.indices) {
                stateList.add(stateArray[i])
            }
            val spinnerAdapter = CustomSpinnerAdapter(this, stateList)
            spn_state.setAdapter(spinnerAdapter)
            spn_state.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val layout = parent.getChildAt(0) as RelativeLayout
                    if (layout != null) {
                        val selectedText = layout.findViewById(R.id.txtItem) as TextView
                        selectedText?.setTextColor(Color.BLACK)
                        state = parent.getItemAtPosition(position).toString()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.personal_detail)
            setMenu(false, false, false, false)
            initState()
            ll_male.setOnClickListener(this)
            ll_female.setOnClickListener(this)
            et_Password.setOnClickListener(this)
            et_dob.setOnClickListener(this)
//            txt_change.setOnClickListener(this)

            btn_UpdateProfile.setOnClickListener(this)
            if (pref!!.isLogin) {
                if (NetworkUtils.isConnected()) {
                    getPersonalDetail()
                } else
                    Toast.makeText(this, getString(R.string.error_network_connection), Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getPersonalDetail() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@FullProfileActivity)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .personal_details(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@FullProfileActivity)
                    if (response.response!!.status) {
                        if (response.response.data != null)
                            initData(response.response.data)
//                        AppDelegate.showToast(this@FullProfileActivity, response.response!!.message)
                    } else {
                        AppDelegate.showToast(this@FullProfileActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@FullProfileActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updatePersonalDetail() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(this@FullProfileActivity)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.name] = et_Name.text.toString()
                    map[Tags.email] = et_Email.text.toString()
                    map[Tags.mobile] = et_Mobile.text.toString()
                    map[Tags.date_of_birth] = et_dob.text.toString()
                    if (genderType)
                        map[Tags.gender] = "Male"
                    else
                        map[Tags.gender] = "Female"
                    map[Tags.address] = et_Address.text.toString()
                    map[Tags.city] = et_City.text.toString()
                    map[Tags.state] = state
                    map[Tags.pincode] = et_Pincode.text.toString()
                    map[Tags.country] = et_Country.text.toString()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .update_personal_details(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(this@FullProfileActivity)
                    if (response.response!!.status) {
//                        if (response.response.data != null)
//                            initData(response.response.data)
//                        AppDelegate.showToast(this@FullProfileActivity, response.response!!.message)
                        finish()
                    } else {
                        AppDelegate.showToast(this@FullProfileActivity, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(this@FullProfileActivity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var mData: PersonalDetailResponse.ResponseBean.DataBean? = null

    private fun initData(data: PersonalDetailResponse.ResponseBean.DataBean?) {
        try {
            mData = data
            if (mData != null) {
                if (mData!!.name != null && mData!!.name != "")
                    et_Name.setText(mData!!.name)
                if (mData!!.email != null && mData!!.email != "")
                    et_Email.setText(mData!!.email)
                if (mData!!.dob != null && mData!!.dob != "")
                    et_dob.setText(mData!!.dob)
                if (mData!!.phone != null && mData!!.phone != "")
                    et_Mobile.setText(mData!!.phone)
                if (mData!!.address != null && mData!!.address != "")
                    et_Address.setText(mData!!.address)
                if (mData!!.city != null && mData!!.city != "")
                    et_City.setText(mData!!.city)
                if (mData!!.pincode != null && mData!!.pincode != "")
                    et_Pincode.setText(mData!!.pincode)
                if (mData!!.country != null && mData!!.country != "")
                    et_Country.setText(mData!!.country)
                if (mData!!.gender != null && mData!!.gender != "") {
                    if (mData!!.gender == "Male") {
                        ll_male.background = resources.getDrawable(R.drawable.button_rounded_background_gender_selected)
                        imv_male.setImageResource(R.mipmap.maleselect)
                        ll_female.background = resources.getDrawable(R.drawable.button_rounded_background_gender_white)
                        imv_female.setImageResource(R.mipmap.female)
                        genderType = true
                    } else {
                        ll_male.background = resources.getDrawable(R.drawable.button_rounded_background_gender_white)
                        imv_male.setImageResource(R.mipmap.male)
                        ll_female.background =
                                resources.getDrawable(R.drawable.button_rounded_background_gender_selected)
                        imv_female.setImageResource(R.mipmap.femaleselect)
                        genderType = false
                    }
                }
                if (mData!!.state != null && mData!!.state != "") {
                    for (i in stateList!!.indices) {
                        if (stateList!![i].equals(
                                mData!!.state,
                                ignoreCase = true
                            )
                        ) {
                            spn_state.setSelection(i)
                            state = mData!!.state
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}