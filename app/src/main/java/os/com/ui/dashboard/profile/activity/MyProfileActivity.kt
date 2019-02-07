package os.com.ui.dashboard.profile.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.ui.dashboard.profile.fragment.ProfileFragment

class MyProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setMenu(false, false, false, true, false)
        setFragment(ProfileFragment(), R.id.container)
    }
    private fun setFragment(fragment: Fragment, container: Int) {
        val fragmentManager = supportFragmentManager
        fragmentManager
            .beginTransaction()
            .replace(container, fragment)
            .commitAllowingStateLoss()
    }

}