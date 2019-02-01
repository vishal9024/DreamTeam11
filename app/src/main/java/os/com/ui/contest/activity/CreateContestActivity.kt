package os.com.ui.contest.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_create_contest.*
import os.com.AppBase.BaseActivity
import os.com.R

class CreateContestActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        try{
        when (view!!.id) {
            R.id.btn_CreateContest -> {
                startActivity(Intent(this, SelectWinnersContestActivity::class.java))
            }
        }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_contest)
        initViews()
    }

    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.create_contest)
            setMenu(false, false, false, false,false)
            btn_CreateContest.setOnClickListener(this)
            et_winning_amount.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    if (s.length!=0){
                        if (et_contest_size.text.toString()!="") {
                            val total=et_contest_size.text.toString().toInt() * s.toString().toInt()
                            txt_EntryFeeAmount.text=total.toString()
                        }
                    }
                }
            })
            et_contest_size.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    if (s.length!=0){
                        if (et_winning_amount.text.toString()!="") {
                            val total=et_winning_amount.text.toString().toInt() * s.toString().toInt()
                            txt_EntryFeeAmount.text=total.toString()
                        }
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}