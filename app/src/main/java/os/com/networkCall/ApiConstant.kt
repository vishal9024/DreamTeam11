package os.com.networkCall

import os.com.BuildConfig

/**
 * Created by heena on 26/9/17.
 */

object ApiConstant {
    /*Base Url*/
//    https://72.octallabs.com/real11/WebServices/
//    var BaseUrl="http://52.39.238.189/webServices/"
    const val cashFantasyBaseURL = "https://72.octallabs.com/cashfantasy/WebServices/"
    const val CricsetBaseURL = "http://192.168.1.67/real_11/WebServices/"
    const val fSLBaseURL = "https://72.octallabs.com/fsl/WebServices/"
    const val playing11BaseURL = "https://72.octallabs.com/playing11/WebServices/"
    // const  val real11BaseURL = "http://192.168.1.67/real_11/WebServices/"
    const val real11BaseURL = "https://72.octallabs.com/real11/WebServices/"
    const val realBashBaseURL = "https://72.octallabs.com/realBash/WebServices/"
    fun getBaseUrl(): String {
        when {
            BuildConfig.APPLICATION_ID == "os.cashfantasy" -> return cashFantasyBaseURL
            BuildConfig.APPLICATION_ID == "os.cricset" -> return CricsetBaseURL
            BuildConfig.APPLICATION_ID == "os.fsl" -> return fSLBaseURL
            BuildConfig.APPLICATION_ID == "os.playing11" -> return playing11BaseURL
            BuildConfig.APPLICATION_ID == "os.real11" -> return real11BaseURL
            BuildConfig.APPLICATION_ID == "os.realbash" -> return realBashBaseURL
        }
        return realBashBaseURL
    }

    const val cashFantasyWebViewURL = "https://72.octallabs.com/cashfantasy/"
    const val CricsetWebViewURL = "https://72.octallabs.com/dreamstock11/"
    const val fSLWebViewURL = "https://72.octallabs.com/fsl/"
    const val playing11WebViewURL = "https://72.octallabs.com/playing11/"
    //    const  val real11BaseURL = "http://192.168.1.67/real_11/WebServices/"
    const val real11WebViewURL = "https://72.octallabs.com/real11/"
    const val realBashWebViewURL = "https://72.octallabs.com/realBash/"
    fun getWebViewUrl(): String {
        when {
            BuildConfig.APPLICATION_ID == "os.cashfantasy" -> return cashFantasyWebViewURL
            BuildConfig.APPLICATION_ID == "os.cricset" -> return CricsetWebViewURL
            BuildConfig.APPLICATION_ID == "os.fsl" -> return fSLWebViewURL
            BuildConfig.APPLICATION_ID == "os.playing11" -> return playing11WebViewURL
            BuildConfig.APPLICATION_ID == "os.real11" -> return real11WebViewURL
            else -> BuildConfig.APPLICATION_ID == "os.realbash"
        }
        return realBashWebViewURL
    }


    const val index = "pages/index.html"
    const val point_system = "pages/point_system.html"
    const val static = "pages/static.html"
    const val static_tab = "pages/static_tab.html"
    const val how_to_play_tab = "pages/how_to_play.html"
    const val how_it_works_tab = "pages/invite-friends-how-it-works.html"
    const val how_fair_play_tab = "pages/invite-friends-how-it-works-2.html"
    const val legality_tab = "pages/legality.html"

    const val signup = "signup"
    const val verify_otp = "verify-otp"
    const val resend_otp = "resend-otp"
    const val login = "login"
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
}