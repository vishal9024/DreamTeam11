package os.com.networkCall

import kotlinx.coroutines.Deferred
import os.com.ui.signup.apiRequest.SignUpRequest
import os.com.ui.signup.apiRequest.VerifyOtpRequest
import os.com.ui.signup.apiResponse.otpVerify.OtpVerifyResponse
import os.com.ui.signup.apiResponse.signup.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.signup)
    fun signup(@Body signupRequest: SignUpRequest): Deferred<SignUpResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.verify_otp)
    fun verify_otp(@Body signupRequest: VerifyOtpRequest): Deferred<OtpVerifyResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.resend_otp)
    fun resend_otp(@Body request: Map<String, String>): Deferred<OtpVerifyResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.login)
    fun login(@Body request: Map<String, String>): Deferred<OtpVerifyResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.social_login)
    fun social_login(@Body request: Map<String, String>): Deferred<OtpVerifyResponse>

    @Headers("Content-Type: application/json")

    @POST(ApiConstant.social_signup)
    fun social_signup(@Body signupRequest: SignUpRequest): Deferred<SignUpResponse>
}
