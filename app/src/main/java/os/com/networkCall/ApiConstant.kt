package os.com.networkCall

import os.com.BuildConfig

object ApiConstant {
    /*Base Url*/
//    https://72.octallabs.com/real11/WebServices/
//    var BaseUrl="http://52.39.238.189/webServices/"
//    var BaseUrl="http://52.39.238.189/webServices/"
//    const val cashFantasyBaseURL = "https://72.octallabs.com/cashfantasy/WebServices/"
//    const val CricsetBaseURL = "http://192.168.1.67/real_11/WebServices/"
//    const val fSLBaseURL = "https://72.octallabs.com/fsl/WebServices/"
//    const val playing11BaseURL = "https://72.octallabs.com/playing11/WebServices/"
//    // const  val real11BaseURL = "http://192.168.1.67/real_11/WebServices/"
//    const val real11BaseURL = "http://real11.com/WebServices/"
//    const val realBashBaseURL = "https://72.octallabs.com/realBash/WebServices/"


    val cashFantasyBaseURL = "https://72.octallabs.com/cashfantasy/"
    const val CricsetBaseURL = "http://192.168.1.67/real_11/"
    const val fSLBaseURL = "http://fansuperleague.com/"
    const val playing11BaseURL = "http://3.17.90.89/"
    // const  val real11BaseURL = "http://192.168.1.67/real_11/WebServices/"
    const val real11BaseURL = "http://real11.com/"
    const val lucky11BaseURL = "http://18.224.67.198/"
    const val realBashBaseURL = "http://3.17.175.199/"
    fun getBaseUrl(): String {
        when {
            BuildConfig.APPLICATION_ID == "os.cashfantasy" -> return cashFantasyBaseURL + "WebServices/"
            BuildConfig.APPLICATION_ID == "os.cricset" -> return CricsetBaseURL + "WebServices/"
            BuildConfig.APPLICATION_ID == "os.fsl" -> return fSLBaseURL + "WebServices/"
            BuildConfig.APPLICATION_ID == "os.playing11" -> return playing11BaseURL + "WebServices/"
            BuildConfig.APPLICATION_ID == "os.real11" -> return real11BaseURL + "WebServices/"
            BuildConfig.APPLICATION_ID == "os.lucky11" -> return lucky11BaseURL + "WebServices/"
            BuildConfig.APPLICATION_ID == "os.realbash" -> return realBashBaseURL + "WebServices/"
        }
        return realBashBaseURL
    }

    fun getWebViewUrl(): String {
        when {
            BuildConfig.APPLICATION_ID == "os.cashfantasy" -> return cashFantasyBaseURL
            BuildConfig.APPLICATION_ID == "os.cricset" -> return CricsetBaseURL
            BuildConfig.APPLICATION_ID == "os.fsl" -> return fSLBaseURL
            BuildConfig.APPLICATION_ID == "os.playing11" -> return playing11BaseURL
            BuildConfig.APPLICATION_ID == "os.real11" -> return real11BaseURL
            BuildConfig.APPLICATION_ID == "os.lucky11" -> return lucky11BaseURL
            else -> BuildConfig.APPLICATION_ID == "os.realbash"
        }
        return realBashBaseURL
    }

    const val help = "pages/help"
    const val point_system = "pages/point-system"
    const val static = "pages/about_us"
    const val static_tab = "pages/static-tab"
    const val how_to_play_tab = "pages/how-to-play"
    const val how_it_works_tab = "pages/how-it-works"
    const val how_fair_play_tab = "pages/fair-play"
    const val legality_tab = "pages/legality"
    const val dream11_champions = "pages/dream11_champions"
    const val terms_conditions = "pages/terms_conditions"

    const val signup = "signup"
    const val verify_otp = "verify-otp"
    const val resend_otp = "resend-otp"
    const val login = "login"
    const val login_password = "login-password"
    const val social_login = "social-login"
    const val social_signup = "social-signup"
    const val getMatchList = "getMatchList"
    const val profile = "profile"
    const val contest_list = "contest-list"
    const val joined_contest_list = "joined-contest-list"
    const val player_list = "player-list"
    const val create_team = "create-team"
    const val personal_details = "personal_details"
    const val update_personal_details = "update_personal_details"
    const val change_pasword = "change_pasword"
    const val player_team_list = "player-team-list"
    const val dream_team = "dream-team"
    const val join_contest_wallet_amount = "join-contest-wallet-amount"
    const val join_contest = "joinContest"
    const val contest_price_breakup = "contest-price-breakup"
    const val logout = "logout"
    const val contest_detail = "contest-detail"
    const val switch_team = "switch-team"
    const val match_scores = "match-scores"
    const val apply_contest_invite_code = "apply-contest-invite-code"
    const val joined_contest_matches = "joined-contest-matches"
    const val team_scores = "team-scores"
    const val getSeriesPlayerList = "getSeriesPlayerList"
    const val generate_paytm_checksum = "generate-paytm-checksum"
    const val apply_coupon_code = "apply-coupon-code"
    const val avetar_list = "avetar-list"
    const val update_user_image = "update-user-image"
    const val update_transactions = "update-transactions"
    const val leaderboard = "leaderboard"
    const val replace_player = "replace-player"
    const val banner_list = "banner-list"
    const val notification_list = "notification-list"
    const val edit_user_team_name = "edit-user-team-name"
    const val seriesList = "seriesList"
    const val series_ranking = "series-ranking"
    const val team_states = "team-states"
    const val team_profile_comparision = "team-profile-comparision"
    const val friend_referal_detail = "friend-referal-detail"

    const val withdraw_cash = "withdraw-cash"
    const val verify_email = "verify-email"
    const val transation_history = "transation-history-new"
    const val bank_details = "bankDetails"
    const val verify_pan_detail = "verify-pan-detail"
    const val verify_bank_detail = "verify-bank-detail"
    const val user_account_datail = "user-account-datail"
    const val entryPerTeam = "entryPerTeam"
    const val contest_prize_breakup = "contest-prize-breakup"
    const val add_withdraw_request = "add-withdraw-request"
    const val create_contest = "create-contest"
    const val deleteNotifications = "deleteNotifications"
    const val forgot_password = "forgot-password"
    const val seriesPlayerDetail = "seriesPlayerDetail"
    const val befor_join_contest = "befor-join-contest"
    const val cashfreeToken = "cftoken/order"
}