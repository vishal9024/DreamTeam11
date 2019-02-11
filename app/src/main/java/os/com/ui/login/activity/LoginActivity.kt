package os.com.ui.login.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
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
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.IntentConstant
import os.com.constant.Tags
import os.com.data.Prefs
import os.com.model.FbDetails
import os.com.model.SocialModel
import os.com.networkCall.ApiClient
import os.com.ui.dashboard.DashBoardActivity
import os.com.ui.signup.activity.OTPActivity
import os.com.ui.signup.activity.SignUpActivity
import os.com.utils.AppDelegate
import os.com.utils.networkUtils.NetworkUtils
import java.util.*

class LoginActivity : BaseActivity(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_Next -> {
                if (validation())
                    if (NetworkUtils.isConnected()) {
                        callLoginApi()
                    } else
                        AppDelegate.showToast(this, getString(R.string.error_network_connection))
            }
            R.id.txt_Signup -> {
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }
            R.id.facebookLoginButton -> {
                faceBookLogin()
            }
            R.id.googleLoginButton -> {
                googlePlusLogin()
            }
        }
    }

    fun validation(): Boolean {
        if (et_email.text.toString().isEmpty()) {
            AppDelegate.showToast(this, getString(R.string.enter_phone_number))
            return false
        }
//        if (TextUtils.isDigitsOnly(et_email.text.toString())) {
//            if (!ValidationUtil.isPhoneValid(et_email.text.toString())) {
//                AppDelegate.showToast(this, getString(R.string.valid_phone_number))
//                return false
//            }
//        } else
//            if (!ValidationUtil.isEmailValid(et_email.text.toString())) {
//                AppDelegate.showToast(this, getString(R.string.valid_email))
//                return false
//            }

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.login)
        btn_Next.setOnClickListener(this)
        txt_Signup.setOnClickListener(this)
        facebookLoginButton.setOnClickListener(this)
        googleLoginButton.setOnClickListener(this)
    }

    private fun callLoginApi() {
        val loginRequest = HashMap<String, String>()
        loginRequest["user_name"] = et_email.text.toString()
        loginRequest["language"] = FantasyApplication.getInstance().getLanguage()
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@LoginActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .login(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@LoginActivity)
                if (response.response!!.status) {
                    AppDelegate.showToast(this@LoginActivity, response.response!!.message)
                    startActivity(
                        Intent(this@LoginActivity, OTPActivity::class.java)
                            .putExtra(IntentConstant.OTP, response.response!!.data!!.otp)
                            .putExtra(IntentConstant.MOBILE, response.response!!.data!!.phone)
                            .putExtra(IntentConstant.USER_ID, response.response!!.data!!.user_id)
                    )

                } else {
                    AppDelegate.showToast(this@LoginActivity, response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@LoginActivity)
            }
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
                    AppDelegate.showProgressDialog(this@LoginActivity)
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
                                    AppDelegate.hideProgressDialog(this@LoginActivity)
                                    Prefs(this@LoginActivity).putStringValueinTemp(Tags.social_id, socialModel.fb_id)
                                    if (NetworkUtils.isConnected()) {
                                        checkUserVerify(socialModel)
                                    } else
                                        Toast.makeText(
                                            this@LoginActivity,
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
                        AppDelegate.hideProgressDialog(this@LoginActivity)
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

    private var mAuth: FirebaseAuth? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        AppDelegate.LogT("firebaseAuthWithGoogle:" + acct!!.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
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
                        checkUserVerify(socialModel)
                    } else
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.error_network_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    signOut()
                } else {
                    AppDelegate.LogT("signInWithCredential:failure" + task.exception)
                    Toast.makeText(
                        this@LoginActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun checkUserVerify(socialModel: SocialModel) {
        val loginRequest = HashMap<String, String>()
        loginRequest["fb_id"] = socialModel.fb_id
        loginRequest["google_id"] = socialModel.google_id
        loginRequest["device_id"] = pref!!.fcMtokeninTemp
        loginRequest["device_type"] = Tags.device_type
//        loginRequest["name"] = socialModel.first_name
//        loginRequest["email"] = socialModel.email_address
//        loginRequest["mobile_number"] = ""
        loginRequest["language"] = FantasyApplication.getInstance().getLanguage()
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@LoginActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .social_login(loginRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@LoginActivity)
                if (response.response!!.status) {
//                    AppDelegate.showToast(this@LoginActivity, response.response!!.message)
                    pref!!.userdata = response.response!!.data
                    pref!!.isLogin = true
                    startActivity(
                        Intent(this@LoginActivity, DashBoardActivity::class.java)
                    )
                } else {
                    startActivity(
                        Intent(this@LoginActivity, SignUpActivity::class.java)
                            .putExtra(IntentConstant.DATA, socialModel)
                    )
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@LoginActivity)
            }
        }
    }

    private fun googlePlusLogin() {
//        AppDelegate.LogT("getString(R.string.default_web_client_id==?"+getString(R.string.default_web_client_id))
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        if (mGoogleApiClient == null || !mGoogleApiClient!!.isConnected) {
            try {
                mGoogleApiClient = GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this@LoginActivity /* OnConnectionFailedListener */)
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

    private fun signOut() {
        if (mGoogleApiClient != null || mGoogleApiClient!!.isConnected) {
            mAuth!!.signOut()
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback {
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
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