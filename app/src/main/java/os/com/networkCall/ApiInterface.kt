package os.com.networkCall

import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import os.com.networkCall.responseModel.BaseResponse
import os.com.ui.addCash.apiRequest.GeneratePayTmCheckSumRequest
import os.com.ui.addCash.apiRequest.UpdateTransactionRequest
import os.com.ui.addCash.apiResponse.generatePaytmChecksumResponse.GeneratePaytmChecksumResponse
import os.com.ui.contest.apiResponse.contestSizePriceBreakUp.ContestSizePriceBreakup
import os.com.ui.contest.apiResponse.entryFeeResponse.EntryFeeResponse
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
import os.com.ui.dashboard.home.apiResponse.bannerList.GetBannerResponse
import os.com.ui.dashboard.home.apiResponse.getMatchList.GetMatchResponse
import os.com.ui.dashboard.profile.apiResponse.*
import os.com.ui.dashboard.profile.apiResponse.ApplyCouponCodeResponse.ApplyCouponCodeResponse
import os.com.ui.invite.apiResponse.InviteFreindDetailResponse
import os.com.ui.invite.apiResponse.getContestInviteResponse.GetContestInviteResponse
import os.com.ui.joinedContest.apiResponse.DreamTeamResponse.DreamTeamResponse
import os.com.ui.joinedContest.apiResponse.getSeriesPlayerListResponse.GetSeriesPlayerListResponse
import os.com.ui.joinedContest.apiResponse.joinedContestFixtureListResponse.JoinedFixtureListResponse
import os.com.ui.joinedContest.apiResponse.viewTeamsResponse.ViewTeamResponse
import os.com.ui.notification.apiResponse.notificationResponse.NotificationResponse
import os.com.ui.signup.apiRequest.SignUpRequest
import os.com.ui.signup.apiRequest.VerifyOtpRequest
import os.com.ui.signup.apiResponse.otpVerify.OtpVerifyResponse
import os.com.ui.signup.apiResponse.signup.SignUpResponse
import retrofit2.http.*


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
    @POST(ApiConstant.dream_team)
    fun dream_team(@Body request: Map<String, String>): Deferred<DreamTeamResponse>


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

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.avetar_list)
    fun avetar_list(@Body request: Map<String, String>): Deferred<AvtarListResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.update_user_image)
    fun update_user_image(@Body request: Map<String, String>): Deferred<UpdateAvtarResponse>

    @POST(ApiConstant.apply_coupon_code)
    fun apply_coupon_code(@Body request: Map<String, String>): Deferred<ApplyCouponCodeResponse>

    @POST(ApiConstant.leaderboard)
    fun leaderboard(@Body request: Map<String, String>): Deferred<ViewTeamResponse>

    @POST(ApiConstant.replace_player)
    fun replace_player(@Body request: Map<String, String>): Deferred<BaseResponse>


    @POST(ApiConstant.banner_list)
    fun banner_list(@Body request: Map<String, String>): Deferred<GetBannerResponse>

    @POST(ApiConstant.notification_list)
    fun notification_list(@Body request: Map<String, String>): Deferred<NotificationResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.edit_user_team_name)
    fun edit_user_team_name(@Body request: Map<String, String>): Deferred<UpdateAvtarResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.seriesList)
    fun seriesList(@Body request: Map<String, String>): Deferred<SeriesListResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.series_ranking)
    fun series_ranking(@Body request: Map<String, String>): Deferred<SeriesRankingListResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.team_states)
    fun team_states(@Body request: Map<String, String>): Deferred<TeamStatesResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.team_profile_comparision)
    fun team_profile_comparision(@Body request: Map<String, String>): Deferred<OtherUserProfileResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.friend_referal_detail)
    fun friend_referal_detail(@Body request: Map<String, String>): Deferred<InviteFreindDetailResponse>

//    rajkumar sir
    @Multipart
    @POST(ApiConstant.verify_pan_detail)
    fun verify_pan_detail(@Part("data") requestBody: RequestBody, @Part file: MultipartBody.Part?): Deferred<VerifyPanResponse>

    @Multipart
    @POST(ApiConstant.verify_bank_detail)
    fun verify_bank_detail(@Part("data") requestBody: RequestBody, @Part file: MultipartBody.Part?): Deferred<VerifyBankAccountResponse>


    @Headers("Content-Type: application/json")
    @POST(ApiConstant.verify_email)
    fun verify_email(@Body request: Map<String, String>): Deferred<VerifyEmailResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.transation_history)
    fun transation_history(@Body request: Map<String, String>): Deferred<RecentTransactionResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.bank_details)
    fun bank_details(@Body request: Map<String, String>): Deferred<BankDetailResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.add_withdraw_request)
    fun add_withdraw_request(@Body request: Map<String, String>): Deferred<BankDetailResponse>



    @Headers("Content-Type: application/json")
    @POST(ApiConstant.withdraw_cash)
    fun withdraw_cash(@Body request: Map<String, String>): Deferred<WithdrawCashResponse>

    @Headers("Content-Type: application/json")
    @GET
    fun ifsc_code(@Url url: String): Deferred<IfscCodeReponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.user_account_datail)
    fun user_account_datail(@Body request: Map<String, String>): Deferred<MyAccountResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.entryPerTeam)
    fun entryPerTeam(@Body request: Map<String, String>): Deferred<EntryFeeResponse>

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.contest_price_breakup)
    fun contest_price_breakup(@Body request: Map<String, String>): Deferred<ContestSizePriceBreakup>

}
