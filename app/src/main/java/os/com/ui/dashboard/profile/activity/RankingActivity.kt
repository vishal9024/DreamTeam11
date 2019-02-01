package os.com.ui.dashboard.profile.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.activity_ranking.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.ui.dashboard.profile.adapter.CustomSpinnerAdapter
import os.com.ui.dashboard.profile.adapter.RankingAdapter
import java.util.*

class RankingActivity : BaseActivity(), View.OnClickListener {
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

    private val matchList = ArrayList<String>()
    private var match = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        initViews()
    }

    private fun initViews() {
        try {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.leaderboard)
        setMenu(false,false,false,false,false)
            initYear()
        setAdapter()
//        btn_CreateTeam.setOnClickListener(this)
//        txt_Signup.setOnClickListener(this)
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initYear() {
        try {
            matchList.add(resources.getString(R.string.select_match))
            for (i in 0..10) {
                matchList.add("Match 1")
            }
            val spinnerAdapter = CustomSpinnerAdapter(this, matchList)
            spn_match.setAdapter(spinnerAdapter)
            spn_match.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val layout = parent.getChildAt(0) as RelativeLayout
                    if (layout != null) {
                        val selectedText = layout.findViewById(R.id.txtItem) as TextView
                        selectedText?.setTextColor(Color.BLACK)
                        match = parent.getItemAtPosition(position).toString()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    @SuppressLint("WrongConstant")
    private fun setAdapter() {
        try{
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        rv_Contest!!.layoutManager = llm
        rv_Contest!!.adapter = RankingAdapter(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}