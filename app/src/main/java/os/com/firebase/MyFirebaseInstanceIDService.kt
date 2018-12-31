package os.com.firebase

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import os.com.data.Prefs
import os.com.utils.AppDelegate

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        AppDelegate.LogGC(TAG + "Refreshed token: " + refreshedToken!!)
        Prefs(this).fcMtoken = (refreshedToken)
        Prefs(this).fcMtokeninTemp = (refreshedToken)
//        executeSignInAsync(this, refreshedToken)
    }

    companion object {
        private val TAG = "MyFireBaseIIDService"
    }
}