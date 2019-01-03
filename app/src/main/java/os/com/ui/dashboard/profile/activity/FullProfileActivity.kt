package os.com.ui.dashboard.profile.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_my_account.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_full_profile.*
import kotlinx.android.synthetic.main.content_myprofile.*
import okhttp3.internal.Util
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.ui.dashboard.profile.adapter.CustomSpinnerAdapter
import java.util.*

class FullProfileActivity : BaseActivity(), View.OnClickListener {

    private val yearList = ArrayList<String>()
    private var year = ""
    private val dobCalendar = Calendar.getInstance()
    private var dob: String? = null
    private var mAge: Int = 0

    internal var fromDate: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // TODO Auto-generated method stub
            dobCalendar.set(Calendar.YEAR, year)
            dobCalendar.set(Calendar.MONTH, monthOfYear)
            dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        et_dob.setText(""+year + "-" + monthOfYear + 1 + "-" +dayOfMonth);
//            dob = year.toString() + "-" + Util.setZeroBeforeNine(monthOfYear + 1) + "-" +
//                    Util.setZeroBeforeNine(dayOfMonth)

        }

    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.ll_male -> {
                    ll_male.background = resources.getDrawable(R.drawable.button_rounded_background_gender_selected)
                    imv_male.setImageResource(R.mipmap.logout)
                    ll_female.background = resources.getDrawable(R.drawable.button_rounded_background_gender_white)
                    imv_female.setImageResource(R.mipmap.logout)
                }
                R.id.ll_female -> {
                    ll_male.background = resources.getDrawable(R.drawable.button_rounded_background_gender_white)
                    imv_male.setImageResource(R.mipmap.logout)
                    ll_female.background = resources.getDrawable(R.drawable.button_rounded_background_gender_selected)
                    imv_female.setImageResource(R.mipmap.logout)
                }R.id.txt_change -> {
                startActivity(Intent(this, ChangePasswordActivity::class.java))
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

    private fun initYear() {
        try {
            yearList.add(resources.getString(R.string.select_state))
            for (i in 0..10) {
                yearList.add("Rajasthan")
            }
            val spinnerAdapter = CustomSpinnerAdapter(this, yearList)
            spn_state.setAdapter(spinnerAdapter)
            spn_state.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val layout = parent.getChildAt(0) as RelativeLayout
                    if (layout != null) {
                        val selectedText = layout.findViewById(R.id.txtItem) as TextView
                        selectedText?.setTextColor(Color.BLACK)
                        year = parent.getItemAtPosition(position).toString()
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
            initYear()
            ll_male.setOnClickListener(this)
            ll_female.setOnClickListener(this)
            et_Password.setOnClickListener(this)
            et_dob.setOnClickListener(this)
            txt_change.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}