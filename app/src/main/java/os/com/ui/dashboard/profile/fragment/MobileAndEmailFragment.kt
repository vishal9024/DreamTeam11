package os.com.ui.dashboard.profile.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_mobile_and_email.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import os.com.AppBase.BaseFragment
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.Tags
import os.com.data.Prefs
import os.com.model.FbDetails
import os.com.model.SocialModel
import os.com.networkCall.ApiClient
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class MobileAndEmailFragment : BaseFragment(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onClick(p0: View?) {
        try {
            when (p0!!.id) {
                R.id.btnEmailVerify -> {
                    if (TextUtils.isEmpty(edtEmail.text.toString().trim())) {
                        Toast.makeText(context!!, getString(R.string.enter_email_to_proceed), Toast.LENGTH_LONG).show()
                    } else {

                        verify_email(edtEmail.text.toString().trim())
                    }
                }
                R.id.facebookLoginButton -> {
                    faceBookLogin()
                }
                R.id.googleLoginButton -> {
                    googlePlusLogin()
                }
                R.id.txtSendAgain -> {
                    if (!TextUtils.isEmpty(edtEmail.text.toString().trim()))
                        verify_email(edtEmail.text.toString().trim())
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_mobile_and_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            Handler().postDelayed(Runnable {
                initViews()
            }, 10)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun withdraw_cash() {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(activity!!)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    val request = ApiClient.client
                        .getRetrofitService()
                        .withdraw_cash(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response)
                    AppDelegate.hideProgressDialog(activity!!)
                    if (response.response!!.isStatus) {
                        txtVerifiedMobileNumber.setText(response.response.data.mobile_no)
                        txtVerifiedEmail.setText(response.response.data.email)
                        if (response.response!!.data.isEmail_verify) {
                            llEmailVerified.visibility = View.VISIBLE
                            llMobileVerified.visibility = View.VISIBLE
                            cardViewBeforeEmailVerify.visibility = View.GONE
                        } else {
                            llMobileVerified.visibility = View.VISIBLE
                            cardViewAfterEmailVerify.visibility = View.GONE
                            cardViewBeforeEmailVerify.visibility = View.VISIBLE
                        }
                    } else {
                        AppDelegate.showToast(activity!!, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(activity!!)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun initViews() {
        try {
            btnEmailVerify.setOnClickListener(this);
            facebookLoginButton.setOnClickListener(this);
            googleLoginButton.setOnClickListener(this);
            txtSendAgain.setOnClickListener(this);
            withdraw_cash();
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    lateinit var socialModel: SocialModel
    var callbackManager: CallbackManager? = null
    var isCalledOnce: Boolean? = false
    private val RC_SIGN_IN = 9001
    internal var request_code = 102

    fun faceBookLogin() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "user_birthday", "email"))
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    AppDelegate.LogDB("login success" + loginResult.accessToken + "")
                    AppDelegate.LogT("onSuccess = " + loginResult.accessToken + "")
                    AppDelegate.showProgressDialog(activity!!)
                    var fb_LoginToken: String = loginResult.accessToken.toString()
                    val request = GraphRequest.newMeRequest(
                        loginResult.accessToken,
                        object : GraphRequest.GraphJSONObjectCallback {
                            override fun onCompleted(`object`: JSONObject, response: GraphResponse?) {
                                // Application code
                                if (response != null) {
                                    socialModel = SocialModel()
                                    socialModel = FbDetails().getFacebookDetail(response.jsonObject.toString())
                                    AppDelegate.LogT("Facebook details==" + socialModel + "")
                                    AppDelegate.hideProgressDialog(activity!!)
                                    Prefs(activity!!).putStringValueinTemp(Tags.social_id, socialModel.fb_id)
                                    if (NetworkUtils.isConnected()) {
                                        //checkUserVerify(socialModel)
                                        edtEmail.setText(socialModel!!.email_address)
                                        edtEmail.isEnabled = false
                                    } else
                                        Toast.makeText(
                                            activity!!,
                                            getString(R.string.error_network_connection),
                                            Toast.LENGTH_LONG
                                        ).show()


                                }
                            }
                        })
                    val parameters = Bundle()
                    parameters.putString(
                        "fields",
                        "first_name,last_name,email,id,name,gender,birthday,picture.type(large)"
                    )
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    AppDelegate.LogDB("login cancel")

                    if (AccessToken.getCurrentAccessToken() != null)
                        LoginManager.getInstance().logOut()
                    if (!isCalledOnce!!) {
                        isCalledOnce = true
                        faceBookLogin()
                    }
                }

                override fun onError(exception: FacebookException) {
                    AppDelegate.LogDB("login error = " + exception.message)
                    if (exception.message!!.contains("CONNECTION_FAILURE")) {
                        AppDelegate.hideProgressDialog(activity!!)
                    } else if (exception is FacebookAuthorizationException) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            LoginManager.getInstance().logOut()
                            if (!isCalledOnce!!) {
                                isCalledOnce = true
                                faceBookLogin()
                            }
                        }
                    }
                }
            })

    }

    private fun googlePlusLogin() {
        //  AppDelegate.LogT("getString(R.string.default_web_client_id==?"+getString(R.string.default_web_client_id))
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        if (mGoogleApiClient == null || !mGoogleApiClient!!.isConnected) {
            try {
                mGoogleApiClient = GoogleApiClient.Builder(activity!!)
                    .enableAutoManage(activity!!, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build()
            } catch (e: Exception) {
                AppDelegate.LogE(e)
            }
        }
        mAuth = FirebaseAuth.getInstance()
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (FacebookSdk.isInitialized()) {
            AppDelegate.LogT("INITIALIZED facebook sdk")
            disconnectFromFacebook()
        }
        if (mAuth != null)
            mAuth = null
    }


    fun disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return // already logged out
        }
        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, GraphRequest
            .Callback {
                @Override
                fun onCompleted(graphResponse: GraphResponse) {
                    LoginManager.getInstance().logOut()
                    AppDelegate.LogT("Logout from facebook")
                }
            }).executeAsync()
    }

    private var mAuth: FirebaseAuth? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        AppDelegate.LogT("firebaseAuthWithGoogle:" + acct!!.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    AppDelegate.LogT("signInWithCredential:success")
                    val user = mAuth!!.currentUser
                    socialModel = SocialModel()
                    socialModel.email_address = user?.email!!
//                  socialModel.socialType = "google"
                    socialModel.google_id = user.uid
                    socialModel.first_name = user.displayName!!
                    socialModel.image = user.photoUrl.toString()
                    if (NetworkUtils.isConnected()) {
                        edtEmail.setText(socialModel!!.email_address)
                        edtEmail.isEnabled = false
                    } else
                        Toast.makeText(
                            activity!!,
                            getString(R.string.error_network_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    signOut()
                } else {
                    AppDelegate.LogT("signInWithCredential:failure" + task.exception)
                    Toast.makeText(
                        activity!!, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun verify_email(email: String) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                AppDelegate.showProgressDialog(activity!!)
                try {
                    var map = HashMap<String, String>()
                    map[Tags.user_id] = pref!!.userdata!!.user_id
                    map[Tags.language] = FantasyApplication.getInstance().getLanguage()
                    map[Tags.email] = email
                    val request = ApiClient.client
                        .getRetrofitService()
                        .verify_email(map)
                    val response = request.await()
                    AppDelegate.LogT("Response=>" + response);
                    AppDelegate.hideProgressDialog(activity!!)
                    if (response.response!!.isStatus) {
                        AppDelegate.showToast(activity!!, response.response!!.message)
                        cardViewBeforeEmailVerify.visibility = View.GONE
                        cardViewAfterEmailVerify.visibility = View.VISIBLE
                    } else {
                        AppDelegate.showToast(activity!!, response.response!!.message)
                    }
                } catch (exception: Exception) {
                    AppDelegate.hideProgressDialog(activity!!)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun signOut() {
        if (mGoogleApiClient != null || mGoogleApiClient!!.isConnected) {
            mAuth!!.signOut()
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback {
            }
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            AppDelegate.LogT("resultCode==>" + resultCode)
            AppDelegate.LogT("data==>" + data)
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            try {
                AppDelegate.LogE("Error is: " + result.status)
                if (result.isSuccess) {
                    val account = result.signInAccount
                    firebaseAuthWithGoogle(account)
//                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//                try {
//                    val account = task.getResult(ApiException::class.java)
//                    firebaseAuthWithGoogle(account!!)

                } else {
                }
            } catch (e: ApiException) {
                AppDelegate.LogE(e.printStackTrace().toString())
            }
        } else {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }

    }


}