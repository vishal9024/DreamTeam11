package os.com.constant

import os.com.BuildConfig

/**
 * Created by heenas on 3/23/2018.
 */

object AppRequestCodes {
    fun getLiveMerchantId(): String {
        when {
            BuildConfig.APPLICATION_ID == "os.cashfantasy" -> return "Amrita86845160897613"
            BuildConfig.APPLICATION_ID == "os.cricset" -> return "Amrita86845160897613"
            BuildConfig.APPLICATION_ID == "os.fsl" -> return "Amrita86845160897613"
            BuildConfig.APPLICATION_ID == "os.playing11" -> return "Amrita86845160897613"
            BuildConfig.APPLICATION_ID == "os.real11" -> return "Amrita86845160897613"
            BuildConfig.APPLICATION_ID == "os.lucky11" -> return "Amrita86845160897613"
            else -> BuildConfig.APPLICATION_ID == "os.realbash"
        }
        return "Amrita86845160897613"
    }

    fun getLiveCAshFreeClientID(): String {
        when {
            BuildConfig.APPLICATION_ID == "os.cashfantasy" -> return "3611aadc7217351f4350a2ff1163"
            BuildConfig.APPLICATION_ID == "os.cricset" -> return "3611aadc7217351f4350a2ff1163"
            BuildConfig.APPLICATION_ID == "os.fsl" -> return "3611aadc7217351f4350a2ff1163"
            BuildConfig.APPLICATION_ID == "os.playing11" -> return "3611aadc7217351f4350a2ff1163"
            BuildConfig.APPLICATION_ID == "os.real11" -> return "3611aadc7217351f4350a2ff1163"
            BuildConfig.APPLICATION_ID == "os.lucky11" -> return "3611aadc7217351f4350a2ff1163"
            else -> BuildConfig.APPLICATION_ID == "os.realbash"
        }
        return "3611aadc7217351f4350a2ff1163"
    }

    fun getLiveCAshFreeSecretKey(): String {
        when {
            BuildConfig.APPLICATION_ID == "os.cashfantasy" -> return "04028669eafd8dda9bfde9be1f5a61ea8e8dc869"
            BuildConfig.APPLICATION_ID == "os.cricset" -> return "04028669eafd8dda9bfde9be1f5a61ea8e8dc869"
            BuildConfig.APPLICATION_ID == "os.fsl" -> return "04028669eafd8dda9bfde9be1f5a61ea8e8dc869"
            BuildConfig.APPLICATION_ID == "os.playing11" -> return "04028669eafd8dda9bfde9be1f5a61ea8e8dc869"
            BuildConfig.APPLICATION_ID == "os.real11" -> return "04028669eafd8dda9bfde9be1f5a61ea8e8dc869"
            BuildConfig.APPLICATION_ID == "os.lucky11" -> return "04028669eafd8dda9bfde9be1f5a61ea8e8dc869"
            else -> BuildConfig.APPLICATION_ID == "os.realbash"
        }
        return "04028669eafd8dda9bfde9be1f5a61ea8e8dc869"
    }

    fun getLiveWebSiteUrl(): String {
        when {
            BuildConfig.APPLICATION_ID == "os.cashfantasy" -> return "APPSTAGING"
            BuildConfig.APPLICATION_ID == "os.cricset" -> return "APPSTAGING"
            BuildConfig.APPLICATION_ID == "os.fsl" -> return "APPSTAGING"
            BuildConfig.APPLICATION_ID == "os.playing11" -> return "APPSTAGING"
            BuildConfig.APPLICATION_ID == "os.real11" -> return "APPSTAGING"
            BuildConfig.APPLICATION_ID == "os.lucky11" -> return "APPSTAGING"
            else -> BuildConfig.APPLICATION_ID == "os.realbash"
        }
        return "APPSTAGING"
    }

    const val cashfreeBaseUrlTest = "https://test.cashfree.com/api/v2/"/*cftoken/order*/
    const val cashfreeBaseUrlLive = "https://api.cashfree.com/api/v2/" /*cftoken / order*/
    const val IFSCurl = "https://api.techm.co.in/api/v1/ifsc/"
    const val LIVE_CALLBACKURL_PAYTM = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="
    const val STAGING_CALLBACKURL_PAYTM = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="
    const val CLONE = 1
    const val EDIT = 2
    const val UPDATEVIEW = 3
    const val UPDATE_ACTIVITY = 5
    const val ADD_CASH = 7
    const val CREATE = 0
    const val JOIN = 11
    const val JOIN_PLUS = 12
    const val SWITCH = 13
    const val JOINED = 14
    const val INVITE_CONTEST = 16
    const val SELECT_SUBSTITUTE = 15

    const val CAMERA_REQUEST_CODE = 18
    const val GALARY_REQUEST_CODE = 19
    const val CREATE_CONTEST = 21
    const val ADD_CASH_CONTEST = 22
    const val SIGNUP = 23
    const val REFRESH_WALLET = 24

    const val PAYTM = 25
    const val CASHFREE = 26
    const val ADD_REMOVE_PLAYER=27

}