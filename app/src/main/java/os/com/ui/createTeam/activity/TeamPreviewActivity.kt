package os.com.ui.createTeam.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_team_preview.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.ui.createTeam.apiResponse.playerListResponse.Data
import os.com.ui.dashboard.DashBoardActivity

class TeamPreviewActivity : BaseActivity(), View.OnClickListener {
    var playerList: MutableList<Data> = ArrayList()
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.img_Edit -> {
                finish()
            }
            R.id.img_Close -> {
                val intent = Intent(this, DashBoardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }

    var isEdit = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_preview)
        initViews()
    }


    private fun initViews() {
        isEdit = intent.getIntExtra("show", 0)
        if (isEdit == 1){
            img_Edit.visibility = View.GONE
            rl_bat1.visibility = VISIBLE
            rl_bat2.visibility = VISIBLE
            rl_bat3.visibility = VISIBLE
            rl_bat4.visibility = VISIBLE
            rl_ar1.visibility = VISIBLE
            rl_ar2.visibility = VISIBLE
            rl_bowler1.visibility = VISIBLE
            rl_bowler2.visibility = VISIBLE
            rl_bowler3.visibility = VISIBLE
            rl_bowler4.visibility = VISIBLE
        }

        else {
            img_Edit.visibility = VISIBLE
            playerList = intent.getParcelableArrayListExtra(IntentConstant.DATA)
            setData(playerList as java.util.ArrayList<Data>?)
        }

        img_Edit.setOnClickListener(this)
        img_Close.setOnClickListener(this)
    }

    private fun setData(playerList: ArrayList<Data>?) {
        for (data in playerList!!)
            if (data.player_role.contains("Wicketkeeper", true)) {
                ImageLoader.getInstance().displayImage(
                    data.player_record!!.image,
                    cimg_wk1,
                    FantasyApplication.getInstance().options
                )
                txt_wk1.setText(data.player_record!!.player_name)
                txt_wk_points.setText(data.player_record!!.player_credit)

            } else if (data.player_role.contains("Batsman", true)) {
                if (rl_bat1.visibility == View.GONE) {
                    rl_bat1.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_bat1,
                        FantasyApplication.getInstance().options
                    )
                    txt_bat1.setText(data.player_record!!.player_name)
                    txt_bat1_points.setText(data.player_record!!.player_credit)
                } else if (rl_bat2.visibility == View.GONE) {
                    rl_bat2.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_bat2,
                        FantasyApplication.getInstance().options
                    )
                    txt_bat2.setText(data.player_record!!.player_name)
                    txt_bat2_points.setText(data.player_record!!.player_credit)
                } else if (rl_bat3.visibility == View.GONE) {
                    rl_bat3.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_bat3,
                        FantasyApplication.getInstance().options
                    )
                    txt_bat3.setText(data.player_record!!.player_name)
                    txt_bat3_points.setText(data.player_record!!.player_credit)
                } else if (rl_bat4.visibility == View.GONE) {
                    rl_bat4.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_bat4,
                        FantasyApplication.getInstance().options
                    )
                    txt_bat4.setText(data.player_record!!.player_name)
                    txt_bat4_points.setText(data.player_record!!.player_credit)
                } else if (rl_bat5.visibility == View.GONE) {
                    rl_bat5.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_bat5,
                        FantasyApplication.getInstance().options
                    )
                    txt_bat5.setText(data.player_record!!.player_name)
                    txt_bat5_points.setText(data.player_record!!.player_credit)
                }
            } else if (data.player_role.contains("Allrounder", true)) {
                if (rl_ar1.visibility == View.GONE) {
                    rl_ar1.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_ar1,
                        FantasyApplication.getInstance().options
                    )
                    txt_ar1.setText(data.player_record!!.player_name)
                    txt_ar1_points.setText(data.player_record!!.player_credit)
                } else if (rl_ar2.visibility == View.GONE) {
                    rl_ar2.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_ar2,
                        FantasyApplication.getInstance().options
                    )
                    txt_ar2.setText(data.player_record!!.player_name)
                    txt_ar2_points.setText(data.player_record!!.player_credit)
                } else if (rl_ar3.visibility == View.GONE) {
                    rl_ar3.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_ar3,
                        FantasyApplication.getInstance().options
                    )
                    txt_ar3.setText(data.player_record!!.player_name)
                    txt_ar3_points.setText(data.player_record!!.player_credit)
                }

            } else if (data.player_role.contains("Bowler", true)) {
                if (rl_bowler1.visibility == View.GONE) {
                    rl_bowler1.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_bowler1,
                        FantasyApplication.getInstance().options
                    )
                    txt_bowler1.setText(data.player_record!!.player_name)
                    txt_bowler1_points.setText(data.player_record!!.player_credit)
                } else if (rl_bowler2.visibility == View.GONE) {
                    rl_bowler2.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_bowler2,
                        FantasyApplication.getInstance().options
                    )
                    txt_bowler2.setText(data.player_record!!.player_name)
                    txt_bowler2_points.setText(data.player_record!!.player_credit)
                } else if (rl_bowler3.visibility == View.GONE) {
                    rl_bowler3.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_bowler3,
                        FantasyApplication.getInstance().options
                    )
                    txt_bowler3.setText(data.player_record!!.player_name)
                    txt_bowler3_points.setText(data.player_record!!.player_credit)
                } else if (rl_bowler4.visibility == View.GONE) {
                    rl_bowler4.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_bowler4,
                        FantasyApplication.getInstance().options
                    )
                    txt_bowler4.setText(data.player_record!!.player_name)
                    txt_bowler4_points.setText(data.player_record!!.player_credit)
                } else if (rl_bowler5.visibility == View.GONE) {
                    rl_bowler5.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_bowler5,
                        FantasyApplication.getInstance().options
                    )
                    txt_bowler5.setText(data.player_record!!.player_name)
                    txt_bowler5_points.setText(data.player_record!!.player_credit)
                }

            }


    }

}