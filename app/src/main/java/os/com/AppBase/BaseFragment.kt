package os.com.AppBase

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Created by heenas on 2/21/2018.
 */
open class BaseFragment : Fragment() {

//    var pref: Prefs?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        pref=Prefs.getInstance(context)
//        updateMenuTitles()
    }


//    fun isNetworkAvailable() : Boolean{
//        if (NetworkUtils.isConnected()) {
//            return true
//        }
//        com.os.drewel.utill.Utils.getInstance().showToast(context,getString(R.string.error_network_connection))
//            return false
//
//    }
}