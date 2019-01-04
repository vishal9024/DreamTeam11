package os.com.networkCall

import os.com.BuildConfig

/**
 * Created by heena on 26/9/17.
 */

object ApiConstant {
    /*Base Url*/
//    https://72.octallabs.com/real11/WebServices/
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
            else -> BuildConfig.APPLICATION_ID == "os.realbash"
        }
        return realBashBaseURL
    }

    const val signup = "signup"
    const val verify_otp = "verify-otp"
    const val resend_otp = "resend-otp"
    const val login = "login"
    const val social_login = "social-login"
    const val social_signup = "social-signup"
    const val getMatchList = "getMatchList"
    const val profile = "profile"
    const val contest_list = "contest-list"
    const val player_list = "player-list"
    const val create_team = "create-team"
}