package os.com.networkCall

import os.com.BuildConfig

/**
 * Created by heena on 26/9/17.
 */

object ApiConstant {
    /*Base Url*/
//    https://72.octallabs.com/real11/WebServices/
    const val cashFantasyBaseURL = "http://192.168.1.67/real_11/WebServices/"
    const val CricsetBaseURL = "http://192.168.1.67/real_11/WebServices/"
    const val fSLBaseURL = "http://192.168.1.67/real_11/WebServices/"
    const val playing11BaseURL = "http://192.168.1.67/real_11/WebServices/"
    //    const  val real11BaseURL = "http://192.168.1.67/real_11/WebServices/"
    const val real11BaseURL = "https://72.octallabs.com/real11/WebServices/"
    const val realBashBaseURL = "http://192.168.1.67/real_11/WebServices/"
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

//    Fsl
//    https://72.octallabs.com/fsl/pages/index.html
//    https://72.octallabs.com/fsl/pages/point_system.html
//    https://72.octallabs.com/fsl/pages/static.html
//    https://72.octallabs.com/fsl/pages/static_tab.html
//
//    realBash
//    https://72.octallabs.com/realBash/pages/index.html
//    https://72.octallabs.com/realBash/pages/point_system.html
//    https://72.octallabs.com/realBash/pages/static.html
//    https://72.octallabs.com/realBash/pages/static_tab.html
//
//    playing11
//    https://72.octallabs.com/playing11/pages/index.html
//    https://72.octallabs.com/playing11/pages/point_system.html
//    https://72.octallabs.com/playing11/pages/static.html
//    https://72.octallabs.com/playing11/pages/static_tab.html
//
//    dreamstock11
//    https://72.octallabs.com/dreamstock11/pages/index.html
//    https://72.octallabs.com/dreamstock11/pages/point_system.html
//    https://72.octallabs.com/dreamstock11/pages/static.html
//    https://72.octallabs.com/dreamstock11/pages/static_tab.html
//
//    real11
//    https://72.octallabs.com/real11/pages/index.html
//    https://72.octallabs.com/real11/pages/point_system.html
//    https://72.octallabs.com/real11/pages/static.html
//    https://72.octallabs.com/real11/pages/static_tab.html
//
//    cashfantasy
//    https://72.octallabs.com/cashfantasy/pages/index.html
//    https://72.octallabs.com/cashfantasy/pages/point_system.html
//    https://72.octallabs.com/cashfantasy/pages/static.html
//    https://72.octallabs.com/cashfantasy/pages/static_tab.html

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

    const val signup = "signup"
    const val verify_otp = "verify-otp"
    const val resend_otp = "resend-otp"
    const val login = "login"
    const val social_login = "social-login"
    const val social_signup = "social-signup"
    const val getMatchList = "getMatchList"
    const val profile = "profile"
    const val personal_details = "personal_details"
    const val update_personal_details = "update_personal_details"
    const val change_pasword = "change_pasword"
}