package os.com.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.dashboard_activity.*
import kotlinx.android.synthetic.main.dashboard_fragment.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.ui.dashboard.home.fragment.HomeFragment
import os.com.ui.dashboard.more.fragment.MoreFragment
import os.com.ui.dashboard.myContest.fragment.MyContestFragment
import os.com.ui.dashboard.profile.fragment.ProfileFragment

/**
 * Created by heenas on 3/12/2018.
 */

class DashBoardActivity : BaseActivity(), View.OnClickListener,
    BottomNavigationView.OnNavigationItemSelectedListener/*, NavigationView.OnNavigationItemSelectedListener */ {
    override fun onClick(p0: View?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity)
        initView()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                if (supportFragmentManager.findFragmentById(R.id.container) !is HomeFragment) {
                    toolbar.visibility = View.VISIBLE
                    setTitleVisibility(false, true)
                    setMenu(true, false, false, false,false)
                    setTitleText(getString(R.string.home))
                    setFragment(HomeFragment(), R.id.container)
                }
                return true
            }

            R.id.navigation_mycontest -> {
                if (supportFragmentManager.findFragmentById(R.id.container) !is MyContestFragment) {
                    toolbar.visibility = View.VISIBLE
                    setMenu(true, false, false, false,false)
                    setTitleVisibility(true, false)
                    setTitleText(getString(R.string.my_contest))
                    setFragment(MyContestFragment(),R.id.container)
                }
                return true
            }

            R.id.navigation_profile -> {
                if (supportFragmentManager.findFragmentById(R.id.container) !is ProfileFragment) {
                    toolbar.visibility = View.GONE
                    setTitleVisibility(true, false)
                    setMenu(false, false, false, true,false)
                    setTitleText(getString(R.string.profile))
                    setFragment(ProfileFragment(), R.id.container)
                }
                return true
            }
            R.id.navigation_more -> {
                if (supportFragmentManager.findFragmentById(R.id.container) !is MoreFragment) {
                    toolbar.visibility = View.VISIBLE
                    setMenu(true, false, false, false,false)
                    setTitleText(getString(R.string.more))
                    setFragment(MoreFragment(), R.id.container)
                }
                return true
            }
        }
        return false
    }

    @SuppressLint("RestrictedApi")
    private fun removeShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0 until menuView.childCount) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                item.setShifting(false)
                // set once again checked value, so view will be updated
                item.setChecked(item.itemData.isChecked)
            }
        } catch (e: NoSuchFieldException) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field")
        } catch (e: IllegalAccessException) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode")
        }

    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.mipmap.menu)
        setTitleVisibility(false, true)
        setMenu(true, false, false, false,false)

        bottomNavigationView!!.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        removeShiftMode(bottomNavigationView)
        setFragment(HomeFragment(), R.id.container)
    }

//    fun updateNavigationView() {
//        nav_view.getHeaderView(0).txt_username.setText(Prefs.getInstance(this).userdata!!.first_name + " " + Prefs.getInstance(this).userdata!!.last_name)
//        ImageLoader.getInstance().displayImage(Prefs.getInstance(this).userdata!!.img, nav_view.getHeaderView(0).cimg_user, FantasyApplication.getInstance().options)
//    }

    fun setTitleVisibility(title: Boolean, icon: Boolean) {
        if (icon)
            img_AppIcon.visibility = VISIBLE
        else
            img_AppIcon.visibility = GONE

        if (title)
            toolbarTitle.visibility = VISIBLE
        else
            toolbarTitle.visibility = GONE
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
//    private fun setFragment(fragment: Fragment) {
//        val backStateName = fragment.javaClass.name
//
//        val manager = supportFragmentManager
//        val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
//
//        if (!fragmentPopped) { //fragment not in back stack, create it.
//            val ft = manager.beginTransaction()
//            ft.replace(R.id.container, fragment)
//            ft.addToBackStack(backStateName)
//            ft.commitAllowingStateLoss()
//        }
//    }
    private fun setFragment(fragment: Fragment, container: Int ) {
        val fragmentManager = supportFragmentManager
        fragmentManager
            .beginTransaction()
//            .setCustomAnimations(R.anim.bottom_in, R.anim.bottom_out)
            .replace(container, fragment)
//            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun onResume() {
        super.onResume()
        if (!pref!!.isLogin)
            bottomNavigationView.visibility = View.GONE
//        addressTv.text = pref!!.getStringValue(PrefConstant.KEY_SELECTED_STORE_NAME, getString(R.string.home))
//        updateNavigationView()
    }

    /* set title of action bar*/
    fun setTitleText(title: String) {
        toolbarTitle.text = title
    }
}
