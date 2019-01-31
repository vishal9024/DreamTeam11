package os.com.networkCall

import kotlinx.coroutines.Deferred
import os.com.networkCall.responseModel.BaseResponse
import os.com.ui.addCash.apiRequest.GeneratePayTmCheckSumRequest
import os.com.ui.addCash.apiRequest.UpdateTransactionRequest
import os.com.ui.addCash.apiResponse.generatePaytmChecksumResponse.GeneratePaytmChecksumResponse
import os.com.ui.contest.apiResponse.getContestDetail.GetContestDetailResponse
import os.com.ui.contest.apiResponse.getContestList.GetContestResponse
import os.com.ui.contest.apiResponse.joinContestResponse.JoinContestResponse
import os.com.ui.contest.apiResponse.joinContestWalletAmountResponse.JoinContestWalletAmountResponse
import os.com.ui.contest.apiResponse.matchScoreResponse.MatchScoreResponse
import os.com.ui.createTeam.apiRequest.CreateTeamRequest
import os.com.ui.createTeam.apiRequest.SwitchTeamRequest
import os.com.ui.createTeam.apiResponse.createTeamResponse.CreateTeamResponse
import os.com.ui.createTeam.apiResponse.myTeamListResponse.GetTeamListResponse
import os.com.ui.createTeam.apiResponse.playerListResponse.GetPlayerListResponse
import os.com.ui.dashboard.home.apiResponse.getMatchList.GetMatchResponse
import os.com.ui.dashboard.profile.apiResponse.PersonalDetailResponse
import os.com.ui.dashboard.profile.apiResponse.ProfileResponse
import os.com.ui.invite.apiResponse.getContestInviteResponse.GetContestInviteResponse
import os.com.ui.joinedContest.apiResponse.getSeriesPlayerListResponse.GetSeriesPlayerListResponse
import os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse.JoinedFixtureListResponse
import os.com.ui.signup.apiRequest.SignUpRequest
import os.com.ui.signup.apiRequest.VerifyOtpRequest
import os.com.ui.signup.apiResponse.otpVerify.OtpVerifyResponse
import os.com.ui.signup.apiResponse.signup.SignUpResponse
import os.com.ui.winningBreakup.apiResponse.contestPriceBreakupResponse.PriceBreakUpResponse
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

    @POST(ApiConstant.getMatchList)
    fun getMatchList(@Body request: Map<String, String>): Deferred<GetMatchResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.profile)
    fun profile(@Body request: Map<String, String>): Deferred<ProfileResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.contest_list)
    fun getContestlist(@Body request: Map<String, String>): Deferred<GetContestResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.joined_contest_list)
    fun getJoinedContestlist(@Body request: Map<String, String>): Deferred<JoinedFixtureListResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.player_list)
    fun getPlayerList(@Body request: Map<String, String>): Deferred<GetPlayerListResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.create_team)
    fun create_team(@Body request: CreateTeamRequest): Deferred<CreateTeamResponse>


    @Headers("Content-Type: application/json")
    @POST(ApiConstant.personal_details)
    fun personal_details(@Body request: Map<String, String>): Deferred<PersonalDetailResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.update_personal_details)
    fun update_personal_details(@Body request: Map<String, String>): Deferred<PersonalDetailResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.change_pasword)
    fun change_pasword(@Body request: Map<String, String>): Deferred<ProfileResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.player_team_list)
    fun player_team_list(@Body request: Map<String, String>): Deferred<GetTeamListResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.join_contest_wallet_amount)
    fun join_contest_wallet_amount(@Body request: Map<String, String>): Deferred<JoinContestWalletAmountResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.join_contest)
    fun join_contest(@Body request: Map<String, String>): Deferred<JoinContestResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.switch_team)
    fun switch_team(@Body request: SwitchTeamRequest): Deferred<JoinContestResponse>


    @Headers("Content-Type: application/json")
    @POST(ApiConstant.contest_price_breakup)
    fun contest_price_breakup(@Body request: Map<String, String>): Deferred<PriceBreakUpResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.logout)
    fun logout(@Body request: Map<String, String>): Deferred<BaseResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.contest_detail)
    fun contest_detail(@Body request: Map<String, String>): Deferred<GetContestDetailResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.team_scores)
    fun match_scores(@Body request: Map<String, String>): Deferred<MatchScoreResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.apply_contest_invite_code)
    fun apply_contest_invite_code(@Body request: Map<String, String>): Deferred<GetContestInviteResponse>

    @POST(ApiConstant.joined_contest_matches)
    fun joined_contest_matches(@Body request: Map<String, String>): Deferred<GetMatchResponse>

    @POST(ApiConstant.getSeriesPlayerList)
    fun getSeriesPlayerList(@Body request: Map<String, String>): Deferred<GetSeriesPlayerListResponse>

    @POST(ApiConstant.generate_paytm_checksum)
    fun generate_paytm_checksum(@Body request: GeneratePayTmCheckSumRequest): Deferred<GeneratePaytmChecksumResponse>

    @POST(ApiConstant.update_transactions)
    fun update_transactions(@Body request: UpdateTransactionRequest): Deferred<GeneratePaytmChecksumResponse>

}
