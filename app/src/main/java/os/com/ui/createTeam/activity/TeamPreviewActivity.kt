package os.com.ui.createTeam.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_team_preview.*
import os.com.AppBase.BaseActivity
import os.com.BuildConfig
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.networkCall.ApiConstant
import os.com.ui.createTeam.apiResponse.PlayerListModel
import os.com.ui.createTeam.apiResponse.myTeamListResponse.PlayerRecord
import os.com.ui.createTeam.apiResponse.myTeamListResponse.Substitute
import os.com.ui.dashboard.home.apiResponse.getMatchList.Match
import os.com.ui.dashboard.more.activity.WebViewActivity
import os.com.ui.joinedContest.apiResponse.DreamTeamResponse.Data

class TeamPreviewActivity : BaseActivity(), View.OnClickListener {
    var playerList: MutableList<PlayerListModel> = ArrayList()

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.img_Edit -> {
                finish()
                if (Choose_C_VC_Activity.choose_C_VC_Activity != null) {
                    Choose_C_VC_Activity.choose_C_VC_Activity!!.finish()
                }
            }
            R.id.img_Close -> {
                finish()
            }
            R.id.pts -> {
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("PAGE_SLUG", "Fantasy Point System")
                intent.putExtra("URL", ApiConstant.getWebViewUrl() + ApiConstant.point_system)
                startActivity(intent)
            }
        }
    }

    var isEdit = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_preview)
        initViews()
    }

    var teamName = ""
    var match: Match? = null
    var matchType = IntentConstant.FIXTURE
    var substituteDetail: Substitute? = null
    var points = false
    var DreamTeam = false
    private fun initViews() {
        isEdit = intent.getIntExtra("show", 0)
        match = intent.getParcelableExtra(IntentConstant.MATCH)
        matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
        points = intent.getBooleanExtra("points", false)
        DreamTeam = intent.getBooleanExtra("DreamTeam", false)
//        if (points)
//            rl_bottom.visibility = VISIBLE
        if (DreamTeam)
            rl_bottom.visibility = VISIBLE
        pts.setOnClickListener(this)
        if (isEdit == 1) {
            img_Edit.visibility = View.GONE
            if (!DreamTeam) {
                var playerListPreview =
                    intent.getParcelableExtra<os.com.ui.createTeam.apiResponse.myTeamListResponse.Data>(IntentConstant.DATA)
                teamName = intent.getStringExtra("teamName")
                txt_TeamName.text = teamName
                var players: ArrayList<os.com.ui.createTeam.apiResponse.myTeamListResponse.PlayerRecord> =
                    intent.getParcelableArrayListExtra(IntentConstant.SELECT_PLAYER)
                substituteDetail = intent.getParcelableExtra("substitute")
                txt_totalPoints.setText(playerListPreview!!.total_points)
                setDataPreview(playerListPreview!!, players)
            } else {
                var playerListPreview =
                    intent.getParcelableExtra<os.com.ui.joinedContest.apiResponse.DreamTeamResponse.Data>(IntentConstant.DATA)
                teamName = intent.getStringExtra("teamName")
                txt_TeamName.text = teamName
                var players: ArrayList<os.com.ui.joinedContest.apiResponse.DreamTeamResponse.PlayerRecord> =
                    intent.getParcelableArrayListExtra(IntentConstant.SELECT_PLAYER)
                substituteDetail = intent.getParcelableExtra("substitute")
                txt_totalPoints.setText(playerListPreview!!.total_points)
                setDreamTeamPreview(playerListPreview, players)
            }
        } else {
            match = intent.getParcelableExtra(IntentConstant.MATCH)
            matchType = intent.getIntExtra(IntentConstant.CONTEST_TYPE, IntentConstant.FIXTURE)
            img_Edit.visibility = VISIBLE
            playerList = intent.getParcelableArrayListExtra(IntentConstant.DATA)
            setData(playerList as java.util.ArrayList<PlayerListModel>?)
        }
        img_Edit.setOnClickListener(this)
        img_Close.setOnClickListener(this)
    }

    fun getName(name: String): String {
        if (!name.isNullOrEmpty()) {

            var name = name.split(" ")
            var finalName = StringBuilder()
            if (!name.isNullOrEmpty())
                for (i in name.indices) {
                    if (name.size > 1 && i != name.lastIndex)
                        finalName.append(name[i].first()).append(" ")
                    if (i == name.lastIndex)
                        finalName.append(name.get(i))
                }
            else
                finalName.append(name)

            return finalName.toString()
        } else
            return ""
    }

    private var WK = 1
    private var BAT = 2
    private var AR = 3
    private var BOWLER = 4
    private var SUBSTITUTE = 5
    private fun setData(playerList: ArrayList<PlayerListModel>?) {
        for (dataMain in playerList!!)
            if (dataMain.type == WK) {
                for (data in dataMain.playerList!!) {
                    if (data.isCaptain) {
                        txt_wk_cvc.setText("C")
                        txt_wk_cvc.visibility = View.VISIBLE
                        txt_wk_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.isViceCaptain) {
                        txt_wk_cvc.setText("VC")
                        txt_wk_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        txt_wk_cvc.visibility = View.VISIBLE
                    }
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_wk1,
                        FantasyApplication.getInstance().options
                    )

                    txt_wk1.setText(getName(getName(data.player_record!!.player_name)))
                    if (points)
                        txt_wk_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                    else
                        txt_wk_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))
                }
            } else if (dataMain.type == BAT) {
                for (data in dataMain.playerList!!) {
                    if (rl_bat1.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_bat1_cvc.setText("C")
                            txt_bat1_cvc.visibility = View.VISIBLE
                            txt_bat1_cvc.setBackgroundResource(R.drawable.captain_selected)
                        } else if (data.isViceCaptain) {
                            txt_bat1_cvc.setText("VC")
                            txt_bat1_cvc.visibility = View.VISIBLE
                            txt_bat1_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_bat1.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_bat1,
                            FantasyApplication.getInstance().options
                        )
                        txt_bat1.setText(getName(data.player_record!!.player_name))
                        if (points)
                            txt_bat1_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                        else
                            txt_bat1_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))
                    } else if (rl_bat2.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_bat2_cvc.setText("C")
                            txt_bat2_cvc.visibility = View.VISIBLE
                            txt_bat2_cvc.setBackgroundResource(R.drawable.captain_selected)
                        } else if (data.isViceCaptain) {
                            txt_bat2_cvc.setText("VC")
                            txt_bat2_cvc.visibility = View.VISIBLE
                            txt_bat2_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_bat2.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_bat2,
                            FantasyApplication.getInstance().options
                        )
                        txt_bat2.setText(getName(data.player_record!!.player_name))
                        if (points)
                            txt_bat2_points.text = data.player_record!!.player_credit + " " + getString(R.string.Pts)
                        else
                            txt_bat2_points.text = data.player_record!!.player_credit + " " + getString(R.string.Cr)
                    } else if (rl_bat3.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_bat3_cvc.setText("C")
                            txt_bat3_cvc.visibility = View.VISIBLE
                            txt_bat3_cvc.setBackgroundResource(R.drawable.captain_selected)
                        } else if (data.isViceCaptain) {
                            txt_bat3_cvc.setText("VC")
                            txt_bat3_cvc.visibility = View.VISIBLE
                            txt_bat3_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_bat3.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_bat3,
                            FantasyApplication.getInstance().options
                        )
                        txt_bat3.setText(getName(data.player_record!!.player_name))
                        if (points)
                            txt_bat3_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                        else
                            txt_bat3_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))
                    } else if (rl_bat4.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_bat4_cvc.setText("C")
                            txt_bat4_cvc.visibility = View.VISIBLE
                            txt_bat4_cvc.setBackgroundResource(R.drawable.captain_selected)
                        } else if (data.isViceCaptain) {
                            txt_bat4_cvc.setText("VC")
                            txt_bat4_cvc.visibility = View.VISIBLE
                            txt_bat4_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_bat4.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_bat4,
                            FantasyApplication.getInstance().options
                        )
                        txt_bat4.setText(getName(data.player_record!!.player_name))
                        if (points)
                            txt_bat4_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                        else
                            txt_bat4_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))

                    } else if (rl_bat5.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_bat5_cvc.setText("C")
                            txt_bat5_cvc.visibility = View.VISIBLE
                            txt_bat5_cvc.setBackgroundResource(R.drawable.captain_selected)
                        } else if (data.isViceCaptain) {
                            txt_bat5_cvc.setText("VC")
                            txt_bat5_cvc.visibility = View.VISIBLE
                            txt_bat5_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_bat5.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_bat5,
                            FantasyApplication.getInstance().options
                        )
                        txt_bat5.setText(getName(data.player_record!!.player_name))
                        if (points)
                            txt_bat5_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                        else
                            txt_bat5_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))

                    }
                }
            } else if (dataMain.type == AR) {
                for (data in dataMain.playerList!!) {
                    if (rl_ar1.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_ar1_cvc.setText("C")
                            txt_ar1_cvc.visibility = View.VISIBLE
                            txt_ar1_cvc.setBackgroundResource(R.drawable.captain_selected)

                        } else if (data.isViceCaptain) {
                            txt_ar1_cvc.setText("VC")
                            txt_ar1_cvc.visibility = View.VISIBLE
                            txt_ar1_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_ar1.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_ar1,
                            FantasyApplication.getInstance().options
                        )
                        txt_ar1.setText(getName(data.player_record!!.player_name))
                        if (points)
                            txt_ar1_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                        else
                            txt_ar1_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))

                    } else if (rl_ar2.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_ar2_cvc.setText("C")
                            txt_ar2_cvc.visibility = View.VISIBLE
                            txt_ar2_cvc.setBackgroundResource(R.drawable.captain_selected)
                        } else if (data.isViceCaptain) {
                            txt_ar2_cvc.setText("VC")
                            txt_ar2_cvc.visibility = View.VISIBLE
                            txt_ar2_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_ar2.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_ar2,
                            FantasyApplication.getInstance().options
                        )
                        txt_ar2.setText(getName(data.player_record!!.player_name))
                        if (points)
                            txt_ar2_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                        else
                            txt_ar2_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))

                    } else if (rl_ar3.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_ar3_cvc.setText("C")
                            txt_ar3_cvc.visibility = View.VISIBLE
                            txt_ar3_cvc.setBackgroundResource(R.drawable.captain_selected)
                        } else if (data.isViceCaptain) {
                            txt_ar3_cvc.setText("VC")
                            txt_ar3_cvc.visibility = View.VISIBLE
                            txt_ar3_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_ar3.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_ar3,
                            FantasyApplication.getInstance().options
                        )
                        txt_ar3.setText(getName(data.player_record!!.player_name))
                        if (points)
                            txt_ar3_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                        else
                            txt_ar3_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))

                    }
                }
            } else if (dataMain.type == BOWLER
            ) {
                for (data in dataMain.playerList!!) {
                    if (rl_bowler1.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_bowler1_cvc.setText("C")
                            txt_bowler1_cvc.visibility = View.VISIBLE
                            txt_bowler1_cvc.setBackgroundResource(R.drawable.captain_selected)
                        } else if (data.isViceCaptain) {
                            txt_bowler1_cvc.setText("VC")
                            txt_bowler1_cvc.visibility = View.VISIBLE
                            txt_bowler1_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_bowler1.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_bowler1,
                            FantasyApplication.getInstance().options
                        )
                        txt_bowler1.setText(getName(data.player_record!!.player_name))
                        if (points)
                            txt_bowler1_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                        else
                            txt_bowler1_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))

                    } else if (rl_bowler2.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_bowler2_cvc.setText("C")
                            txt_bowler2_cvc.visibility = View.VISIBLE
                            txt_bowler2_cvc.setBackgroundResource(R.drawable.captain_selected)
                        } else if (data.isViceCaptain) {
                            txt_bowler2_cvc.setText("VC")
                            txt_bowler2_cvc.visibility = View.VISIBLE
                            txt_bowler2_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_bowler2.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_bowler2,
                            FantasyApplication.getInstance().options
                        )
                        txt_bowler2.setText(getName(data.player_record!!.player_name))
                        if (points)
                            txt_bowler2_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                        else
                            txt_bowler2_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))

                    } else if (rl_bowler3.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_bowler3_cvc.setText("C")
                            txt_bowler3_cvc.visibility = View.VISIBLE
                            txt_bowler3_cvc.setBackgroundResource(R.drawable.captain_selected)
                        } else if (data.isViceCaptain) {
                            txt_bowler3_cvc.setText("VC")
                            txt_bowler3_cvc.visibility = View.VISIBLE
                            txt_bowler3_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_bowler3.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_bowler3,
                            FantasyApplication.getInstance().options
                        )
                        txt_bowler3.setText(getName(data.player_record!!.player_name))
                        if (points)
                            txt_bowler3_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                        else
                            txt_bowler3_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))

                    } else if (rl_bowler4.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_bowler4_cvc.setText("C")
                            txt_bowler4_cvc.visibility = View.VISIBLE
                            txt_bowler4_cvc.setBackgroundResource(R.drawable.captain_selected)
                        } else if (data.isViceCaptain) {
                            txt_bowler4_cvc.setText("VC")
                            txt_bowler4_cvc.visibility = View.VISIBLE
                            txt_bowler4_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_bowler4.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_bowler4,
                            FantasyApplication.getInstance().options
                        )
                        txt_bowler4.setText(getName(data.player_record!!.player_name))

                        if (points)
                            txt_bowler4_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                        else
                            txt_bowler4_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))

                    } else if (rl_bowler5.visibility == View.GONE) {
                        if (data.isCaptain) {
                            txt_bowler5_cvc.text = "C"
                            txt_bowler5_cvc.visibility = View.VISIBLE
                            txt_bowler5_cvc.setBackgroundResource(R.drawable.captain_selected)
                        } else if (data.isViceCaptain) {
                            txt_bowler5_cvc.setText("VC")
                            txt_bowler5_cvc.visibility = View.VISIBLE
                            txt_bowler5_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                        }
                        rl_bowler5.visibility = VISIBLE
                        ImageLoader.getInstance().displayImage(
                            data.player_record!!.image,
                            cimg_bowler5,
                            FantasyApplication.getInstance().options
                        )
                        txt_bowler5.setText(getName(data.player_record!!.player_name))
                        if (points)
                            txt_bowler5_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                        else
                            txt_bowler5_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))

                    }
                }
            } else if ((BuildConfig.APPLICATION_ID == "os.real11" || BuildConfig.APPLICATION_ID == "os.cashfantasy") &&
                dataMain.type == SUBSTITUTE
            ) {
                for (data in dataMain.playerList!!) {
                    ll_substitute.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.player_record!!.image,
                        cimg_substitute,
                        FantasyApplication.getInstance().options
                    )
                    txt_substitute.setText(getName(data.player_record!!.player_name))
                    if (points)
                        txt_substitute_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Pts))
                    else
                        txt_substitute_points.setText(data.player_record!!.player_credit + " " + getString(R.string.Cr))

                }
            }
    }

    private fun setDreamTeamPreview(
        playerList: Data,
        players: ArrayList<os.com.ui.joinedContest.apiResponse.DreamTeamResponse.PlayerRecord>
    ) {
        if ((BuildConfig.APPLICATION_ID == "os.real11" || BuildConfig.APPLICATION_ID == "os.cashfantasy") &&
            substituteDetail != null
        ) {
            ll_substitute.visibility = GONE
            ImageLoader.getInstance().displayImage(
                substituteDetail!!.image,
                cimg_substitute,
                FantasyApplication.getInstance().options
            )
            if (!substituteDetail!!.name!!.isNullOrEmpty())
                txt_substitute.setText(getName(substituteDetail!!.name))
            if (points)
                txt_substitute_points.setText(substituteDetail!!.credits + " " + getString(R.string.Pts))
            else
                txt_substitute_points.setText(substituteDetail!!.credits + " " + getString(R.string.Cr))

        }
        for (data in players)
            if (data.role!!.contains("Wicketkeeper", true)) {
                if (data.player_id.equals(playerList.captain_player_id)) {
                    txt_wk_cvc.setText("C")
                    txt_wk_cvc.visibility = View.VISIBLE
                    txt_wk_cvc.setBackgroundResource(R.drawable.captain_selected)
                } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                    txt_wk_cvc.setText("VC")
                    txt_wk_cvc.visibility = View.VISIBLE
                    txt_wk_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                }
                ImageLoader.getInstance().displayImage(
                    data.image,
                    cimg_wk1,
                    FantasyApplication.getInstance().options
                )
                if (!data.name!!.isNullOrEmpty())
                    txt_wk1.setText(getName(data.name!!))
                if (data.in_dream_team)
                    img_dreamTeam_wk.visibility = View.VISIBLE
                if (points)
                    if (DreamTeam)
                        txt_wk_points.setText(data.point + " " + getString(R.string.Pts))
                    else
                        txt_wk_points.setText(data.credits + " " + getString(R.string.Pts))
                else
                    txt_wk_points.setText(data.credits + " " + getString(R.string.Cr))

            } else if (data.role!!.contains("Batsman", true)) {
                if (rl_bat1.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bat1_cvc.setText("C")
                        txt_bat1_cvc.visibility = View.VISIBLE
                        txt_bat1_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bat1_cvc.setText("VC")
                        txt_bat1_cvc.visibility = View.VISIBLE
                        txt_bat1_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bat1.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bat1,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_bat1.setText(getName(data.name!!))
                    if (data.in_dream_team)
                        img_dreamTeam_bat1.visibility = View.VISIBLE
                    if (points)
                        if (DreamTeam)
                            txt_bat1_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bat1_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bat1_points.setText(data.credits + " " + getString(R.string.Cr))

                } else if (rl_bat2.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bat2_cvc.setText("C")
                        txt_bat2_cvc.visibility = View.VISIBLE
                        txt_bat2_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bat2_cvc.setText("VC")
                        txt_bat2_cvc.visibility = View.VISIBLE
                        txt_bat2_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bat2.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bat2,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_bat2.setText(getName(data.name!!))
                    if (data.in_dream_team)
                        img_dreamTeam_bat2.visibility = View.VISIBLE
                    if (points)
                        if (DreamTeam)
                            txt_bat2_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bat2_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bat2_points.setText(data.credits + " " + getString(R.string.Cr))

                } else if (rl_bat3.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bat3_cvc.setText("C")
                        txt_bat3_cvc.visibility = View.VISIBLE
                        txt_bat3_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bat3_cvc.setText("VC")
                        txt_bat3_cvc.visibility = View.VISIBLE
                        txt_bat3_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bat3.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_bat3.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bat3,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_bat3.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bat3_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bat3_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bat3_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_bat4.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bat4_cvc.setText("C")
                        txt_bat4_cvc.visibility = View.VISIBLE
                        txt_bat4_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bat4_cvc.setText("VC")
                        txt_bat4_cvc.visibility = View.VISIBLE
                        txt_bat4_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bat4.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_bat4.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bat4,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_bat4.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bat4_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bat4_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bat4_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_bat5.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bat5_cvc.setText("C")
                        txt_bat5_cvc.visibility = View.VISIBLE
                        txt_bat5_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bat5_cvc.setText("VC")
                        txt_bat5_cvc.visibility = View.VISIBLE
                        txt_bat5_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bat5.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_bat5.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bat5,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_bat5.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bat5_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bat5_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bat5_points.setText(data.credits + " " + getString(R.string.Cr))
                }
            } else if (data.role!!.contains("Allrounder", true)) {
                if (rl_ar1.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_ar1_cvc.setText("C")
                        txt_ar1_cvc.visibility = View.VISIBLE
                        txt_ar1_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_ar1_cvc.setText("VC")
                        txt_ar1_cvc.visibility = View.VISIBLE
                        txt_ar1_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_ar1.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_ar1.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_ar1,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_ar1.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_ar1_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_ar1_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_ar1_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_ar2.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_ar2_cvc.setText("C")
                        txt_ar2_cvc.visibility = View.VISIBLE
                        txt_ar2_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_ar2_cvc.setText("VC")
                        txt_ar2_cvc.visibility = View.VISIBLE
                        txt_ar2_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_ar2.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_ar2.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_ar2,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_ar2.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_ar2_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_ar2_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_ar2_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_ar3.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_ar3_cvc.setText("C")
                        txt_ar3_cvc.visibility = View.VISIBLE
                        txt_ar3_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_ar3_cvc.setText("VC")
                        txt_ar3_cvc.visibility = View.VISIBLE
                        txt_ar3_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_ar3.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_ar3.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_ar3,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_ar3.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_ar3_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_ar3_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_ar3_points.setText(data.credits + " " + getString(R.string.Cr))
                }

            } else if (data.role!!.contains("Bowler", true)) {
                if (rl_bowler1.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bowler1_cvc.setText("C")
                        txt_bowler1_cvc.visibility = View.VISIBLE
                        txt_bowler1_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bowler1_cvc.setText("VC")
                        txt_bowler1_cvc.visibility = View.VISIBLE
                        txt_bowler1_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bowler1.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bowler1,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_bowler1.setText(getName(data.name!!))
                    if (data.in_dream_team)
                        img_dreamTeam_bowler1.visibility = View.VISIBLE
                    if (points)
                        if (DreamTeam)
                            txt_bowler1_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bowler1_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bowler1_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_bowler2.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bowler2_cvc.setText("C")
                        txt_bowler2_cvc.visibility = View.VISIBLE
                        txt_bowler2_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bowler2_cvc.setText("VC")
                        txt_bowler2_cvc.visibility = View.VISIBLE
                        txt_bowler2_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bowler2.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_bowler2.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bowler2,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_bowler2.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bowler2_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bowler2_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bowler2_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_bowler3.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bowler3_cvc.setText("C")
                        txt_bowler3_cvc.visibility = View.VISIBLE
                        txt_bowler3_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bowler3_cvc.setText("VC")
                        txt_bowler3_cvc.visibility = View.VISIBLE
                        txt_bowler3_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bowler3.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_bowler3.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bowler3,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_bowler3.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bowler3_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bowler3_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bowler3_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_bowler4.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bowler4_cvc.setText("C")
                        txt_bowler4_cvc.visibility = View.VISIBLE
                        txt_bowler4_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bowler4_cvc.setText("VC")
                        txt_bowler4_cvc.visibility = View.VISIBLE
                        txt_bowler4_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    if (data.in_dream_team)
                        img_dreamTeam_bowler4.visibility = View.VISIBLE
                    rl_bowler4.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bowler4,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_bowler4.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bowler4_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bowler4_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bowler4_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_bowler5.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bowler5_cvc.setText("C")
                        txt_bowler5_cvc.visibility = View.VISIBLE
                        txt_bowler5_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bowler5_cvc.setText("VC")
                        txt_bowler5_cvc.visibility = View.VISIBLE
                        txt_bowler5_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bowler5.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_bowler5.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bowler5,
                        FantasyApplication.getInstance().options
                    )
                    if (!data.name!!.isNullOrEmpty())
                        txt_bowler5.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bowler5_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bowler5_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bowler5_points.setText(data.credits + " " + getString(R.string.Cr))
                }

            }
    }

    private fun setDataPreview(
        playerList: os.com.ui.createTeam.apiResponse.myTeamListResponse.Data,
        players: ArrayList<PlayerRecord>
    ) {
        if ((BuildConfig.APPLICATION_ID == "os.real11" || BuildConfig.APPLICATION_ID == "os.cashfantasy") &&
            substituteDetail != null
        ) {
            if (points)
                ll_substitute.visibility = GONE
            else
                ll_substitute.visibility = VISIBLE
            ImageLoader.getInstance().displayImage(
                substituteDetail!!.image,
                cimg_substitute,
                FantasyApplication.getInstance().options
            )
            txt_substitute.setText(getName(substituteDetail!!.name))
            if (points)
                txt_substitute_points.setText(substituteDetail!!.credits + " " + getString(R.string.Pts))
            else
                txt_substitute_points.setText(substituteDetail!!.credits + " " + getString(R.string.Cr))

        }
        for (data in players)
            if (data.role!!.contains("Wicketkeeper", true)) {
                if (data.player_id.equals(playerList.captain_player_id)) {
                    txt_wk_cvc.setText("C")
                    txt_wk_cvc.visibility = View.VISIBLE
                    txt_wk_cvc.setBackgroundResource(R.drawable.captain_selected)
                } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                    txt_wk_cvc.setText("VC")
                    txt_wk_cvc.visibility = View.VISIBLE
                    txt_wk_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                }
                ImageLoader.getInstance().displayImage(
                    data.image,
                    cimg_wk1,
                    FantasyApplication.getInstance().options
                )
                txt_wk1.setText(getName(data.name!!))
                if (data.in_dream_team)
                    img_dreamTeam_wk.visibility = View.VISIBLE
                if (points)
                    if (DreamTeam)
                        txt_wk_points.setText(data.point + " " + getString(R.string.Pts))
                    else
                        txt_wk_points.setText(data.credits + " " + getString(R.string.Pts))
                else
                    txt_wk_points.setText(data.credits + " " + getString(R.string.Cr))

            } else if (data.role!!.contains("Batsman", true)) {
                if (rl_bat1.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bat1_cvc.setText("C")
                        txt_bat1_cvc.visibility = View.VISIBLE
                        txt_bat1_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bat1_cvc.setText("VC")
                        txt_bat1_cvc.visibility = View.VISIBLE
                        txt_bat1_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bat1.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bat1,
                        FantasyApplication.getInstance().options
                    )
                    txt_bat1.setText(getName(data.name!!))
                    if (data.in_dream_team)
                        img_dreamTeam_bat1.visibility = View.VISIBLE
                    if (points)
                        if (DreamTeam)
                            txt_bat1_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bat1_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bat1_points.setText(data.credits + " " + getString(R.string.Cr))

                } else if (rl_bat2.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bat2_cvc.setText("C")
                        txt_bat2_cvc.visibility = View.VISIBLE
                        txt_bat2_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bat2_cvc.setText("VC")
                        txt_bat2_cvc.visibility = View.VISIBLE
                        txt_bat2_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bat2.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bat2,
                        FantasyApplication.getInstance().options
                    )
                    txt_bat2.setText(getName(data.name!!))
                    if (data.in_dream_team)
                        img_dreamTeam_bat2.visibility = View.VISIBLE
                    if (points)
                        if (DreamTeam)
                            txt_bat2_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bat2_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bat2_points.setText(data.credits + " " + getString(R.string.Cr))

                } else if (rl_bat3.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bat3_cvc.setText("C")
                        txt_bat3_cvc.visibility = View.VISIBLE
                        txt_bat3_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bat3_cvc.setText("VC")
                        txt_bat3_cvc.visibility = View.VISIBLE
                        txt_bat3_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bat3.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_bat3.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bat3,
                        FantasyApplication.getInstance().options
                    )
                    txt_bat3.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bat3_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bat3_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bat3_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_bat4.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bat4_cvc.setText("C")
                        txt_bat4_cvc.visibility = View.VISIBLE
                        txt_bat4_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bat4_cvc.setText("VC")
                        txt_bat4_cvc.visibility = View.VISIBLE
                        txt_bat4_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bat4.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_bat4.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bat4,
                        FantasyApplication.getInstance().options
                    )
                    txt_bat4.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bat4_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bat4_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bat4_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_bat5.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bat5_cvc.setText("C")
                        txt_bat5_cvc.visibility = View.VISIBLE
                        txt_bat5_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bat5_cvc.setText("VC")
                        txt_bat5_cvc.visibility = View.VISIBLE
                        txt_bat5_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bat5.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_bat5.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bat5,
                        FantasyApplication.getInstance().options
                    )
                    txt_bat5.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bat5_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bat5_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bat5_points.setText(data.credits + " " + getString(R.string.Cr))
                }
            } else if (data.role!!.contains("Allrounder", true)) {
                if (rl_ar1.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_ar1_cvc.setText("C")
                        txt_ar1_cvc.visibility = View.VISIBLE
                        txt_ar1_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_ar1_cvc.setText("VC")
                        txt_ar1_cvc.visibility = View.VISIBLE
                        txt_ar1_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_ar1.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_ar1.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_ar1,
                        FantasyApplication.getInstance().options
                    )
                    txt_ar1.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_ar1_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_ar1_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_ar1_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_ar2.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_ar2_cvc.setText("C")
                        txt_ar2_cvc.visibility = View.VISIBLE
                        txt_ar2_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_ar2_cvc.setText("VC")
                        txt_ar2_cvc.visibility = View.VISIBLE
                        txt_ar2_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_ar2.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_ar2.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_ar2,
                        FantasyApplication.getInstance().options
                    )
                    txt_ar2.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_ar2_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_ar2_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_ar2_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_ar3.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_ar3_cvc.setText("C")
                        txt_ar3_cvc.visibility = View.VISIBLE
                        txt_ar3_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_ar3_cvc.setText("VC")
                        txt_ar3_cvc.visibility = View.VISIBLE
                        txt_ar3_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_ar3.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_ar3.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_ar3,
                        FantasyApplication.getInstance().options
                    )
                    txt_ar3.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_ar3_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_ar3_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_ar3_points.setText(data.credits + " " + getString(R.string.Cr))
                }

            } else if (data.role!!.contains("Bowler", true)) {
                if (rl_bowler1.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bowler1_cvc.setText("C")
                        txt_bowler1_cvc.visibility = View.VISIBLE
                        txt_bowler1_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bowler1_cvc.setText("VC")
                        txt_bowler1_cvc.visibility = View.VISIBLE
                        txt_bowler1_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bowler1.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bowler1,
                        FantasyApplication.getInstance().options
                    )
                    txt_bowler1.setText(getName(data.name!!))
                    if (data.in_dream_team)
                        img_dreamTeam_bowler1.visibility = View.VISIBLE
                    if (points)
                        if (DreamTeam)
                            txt_bowler1_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bowler1_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bowler1_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_bowler2.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bowler2_cvc.setText("C")
                        txt_bowler2_cvc.visibility = View.VISIBLE
                        txt_bowler2_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bowler2_cvc.setText("VC")
                        txt_bowler2_cvc.visibility = View.VISIBLE
                        txt_bowler2_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bowler2.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_bowler2.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bowler2,
                        FantasyApplication.getInstance().options
                    )
                    txt_bowler2.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bowler2_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bowler2_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bowler2_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_bowler3.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bowler3_cvc.setText("C")
                        txt_bowler3_cvc.visibility = View.VISIBLE
                        txt_bowler3_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bowler3_cvc.setText("VC")
                        txt_bowler3_cvc.visibility = View.VISIBLE
                        txt_bowler3_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bowler3.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_bowler3.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bowler3,
                        FantasyApplication.getInstance().options
                    )
                    txt_bowler3.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bowler3_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bowler3_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bowler3_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_bowler4.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bowler4_cvc.setText("C")
                        txt_bowler4_cvc.visibility = View.VISIBLE
                        txt_bowler4_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bowler4_cvc.setText("VC")
                        txt_bowler4_cvc.visibility = View.VISIBLE
                        txt_bowler4_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    if (data.in_dream_team)
                        img_dreamTeam_bowler4.visibility = View.VISIBLE
                    rl_bowler4.visibility = VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bowler4,
                        FantasyApplication.getInstance().options
                    )
                    txt_bowler4.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bowler4_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bowler4_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bowler4_points.setText(data.credits + " " + getString(R.string.Cr))
                } else if (rl_bowler5.visibility == View.GONE) {
                    if (data.player_id.equals(playerList.captain_player_id)) {
                        txt_bowler5_cvc.setText("C")
                        txt_bowler5_cvc.visibility = View.VISIBLE
                        txt_bowler5_cvc.setBackgroundResource(R.drawable.captain_selected)
                    } else if (data.player_id.equals(playerList.vice_captain_player_id)) {
                        txt_bowler5_cvc.setText("VC")
                        txt_bowler5_cvc.visibility = View.VISIBLE
                        txt_bowler5_cvc.setBackgroundResource(R.drawable.vicecaptain_selected)
                    }
                    rl_bowler5.visibility = VISIBLE
                    if (data.in_dream_team)
                        img_dreamTeam_bowler5.visibility = View.VISIBLE
                    ImageLoader.getInstance().displayImage(
                        data.image,
                        cimg_bowler5,
                        FantasyApplication.getInstance().options
                    )
                    txt_bowler5.setText(getName(data.name!!))
                    if (points)
                        if (DreamTeam)
                            txt_bowler5_points.setText(data.point + " " + getString(R.string.Pts))
                        else
                            txt_bowler5_points.setText(data.credits + " " + getString(R.string.Pts))
                    else
                        txt_bowler5_points.setText(data.credits + " " + getString(R.string.Cr))
                }

            }
    }
}