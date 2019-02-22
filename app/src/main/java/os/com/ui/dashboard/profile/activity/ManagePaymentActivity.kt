package os.com.ui.dashboard.profile.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_manage_payment.*
import kotlinx.android.synthetic.main.app_toolbar.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.ui.addCash.activity.AddCashInWalletActivity

class ManagePaymentActivity : BaseActivity(), View.OnClickListener {

    override fun onClick(view: View?) {
        try {
            when (view!!.id) {
                R.id.ll_paytm -> {
                    startActivity(Intent(this, AddCashInWalletActivity::class.java).putExtra(
                        IntentConstant.currentBalance,
                        currentBalance
                    ).putExtra(IntentConstant.PAYMENT_GATEWAY,AppRequestCodes.PAYTM))
                }
                R.id.ll_cashfree -> {
                    startActivity(Intent(this, AddCashInWalletActivity::class.java).putExtra(
                        IntentConstant.currentBalance,
                        currentBalance
                    ).putExtra(IntentConstant.PAYMENT_GATEWAY,AppRequestCodes.CASHFREE))

                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    var currentBalance = "0.0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_payment)
        initViews()
    }

    private fun initViews() {
        try {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            toolbarTitleTv.setText(R.string.manage_payment)
            currentBalance = intent.getStringExtra(IntentConstant.currentBalance)
            setMenu(false, false, false, false, false)
            ll_paytm.setOnClickListener(this)
            ll_cashfree.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}