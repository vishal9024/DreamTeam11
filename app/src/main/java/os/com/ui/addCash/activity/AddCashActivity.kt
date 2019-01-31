package os.com.ui.addCash.activity

import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_add_balance.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.dialogue_add_cash_option.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.networkCall.ApiClient
import os.com.ui.addCash.apiRequest.GeneratePayTmCheckSumRequest
import os.com.ui.addCash.apiRequest.UpdateTransactionRequest
import os.com.utils.AppDelegate
import java.util.*


class AddCashActivity : BaseActivity(), View.OnClickListener, PaymentResultListener {

    internal var orderId: String = ""
    internal var callbackURL: String = ""
    internal var mWalletBalance: String = ""
    internal var CHECKSUM = ""
    internal var amount = ""
    internal var options: JSONObject? = null
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_addCash -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_SMS
                    ) !== PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS),
                        1
                    )
                } else {
                    showPaymentOption()
                }
            }
        }
    }

    private fun showPaymentOption(
    ) {
        val dialogue = Dialog(this)
        dialogue.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogue.setContentView(R.layout.dialogue_add_cash_option)
        dialogue.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialogue.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogue.setCancelable(false)
        dialogue.setCanceledOnTouchOutside(false)
        dialogue.setTitle(null)
        dialogue.ll_paytm.setOnClickListener {
            payUsingPaytm()
            dialogue.dismiss()
        }
        dialogue.ll_rajorpay.setOnClickListener {
            payUsingRazorPay()
            dialogue.dismiss()
        }
        dialogue.ll_paypal.setOnClickListener {
            dialogue.dismiss()
        }

        if (dialogue.isShowing)
            dialogue.dismiss()
        dialogue.show()
    }

   override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPaymentOption()
            } else {

            }
        }
    }
    private fun payUsingPaytm() {
        if (AppDelegate.isNetworkAvailable(this)) {
            try {
                AppDelegate.showProgressDialog(this)
                orderId = getRendomOrderID()
                callbackURL = AppRequestCodes.STAGING_CALLBACKURL_PAYTM + orderId + ""
                val generatePayTmCheckSumRequest = GeneratePayTmCheckSumRequest()
                generatePayTmCheckSumRequest.MID = AppRequestCodes.getLiveMerchantId().trim()
                generatePayTmCheckSumRequest.ORDER_ID = orderId.trim()
                generatePayTmCheckSumRequest.CUST_ID = pref!!.userdata!!.user_id.trim()
                generatePayTmCheckSumRequest.MOBILE_NO = pref!!.userdata!!.phone.trim()
                generatePayTmCheckSumRequest.EMAIL = pref!!.userdata!!.email.trim()
                generatePayTmCheckSumRequest.CHANNEL_ID = "WAP"
                generatePayTmCheckSumRequest.TXN_AMOUNT = et_addCash.text.toString().trim()
                generatePayTmCheckSumRequest.WEBSITE = AppRequestCodes.getLiveWebSiteUrl().trim()
                generatePayTmCheckSumRequest.INDUSTRY_TYPE_ID = "Retail".trim()
                generatePayTmCheckSumRequest.CALLBACK_URL = callbackURL.trim()
                callGeneratePaytmCheckSum(generatePayTmCheckSumRequest)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else
            AppDelegate.showToast(this, getString(R.string.error_network_connection))

    }

    private fun callGeneratePaytmCheckSum(generatePayTmCheckSumRequest: GeneratePayTmCheckSumRequest) {
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@AddCashActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .generate_paytm_checksum(generatePayTmCheckSumRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@AddCashActivity)
                if (response.response!!.status) {
//                    generatePayTmCheckSumRequest.CHECKSUMHASH = response.response!!.data!!.checksum
                    startPaytmTransaction(generatePayTmCheckSumRequest,response.response!!.data!!.checksum)
                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@AddCashActivity)
            }
        }
    }

    private fun startPaytmTransaction(
        generatePayTmCheckSumRequest: GeneratePayTmCheckSumRequest,
        checksum: String
    ) {
        val paytmPGService = PaytmPGService.getStagingService()
        val paramMap = HashMap<String, String>()
        try {
            paramMap["MID"] = generatePayTmCheckSumRequest.MID.trim()
            paramMap["ORDER_ID"] = generatePayTmCheckSumRequest.ORDER_ID.trim()
            paramMap["CUST_ID"] = generatePayTmCheckSumRequest.CUST_ID.trim()
            paramMap["MOBILE_NO"] = generatePayTmCheckSumRequest.MOBILE_NO.trim()
            paramMap["EMAIL"] = generatePayTmCheckSumRequest.EMAIL.trim()
            paramMap["CHANNEL_ID"] = generatePayTmCheckSumRequest.CHANNEL_ID.trim()
            paramMap["TXN_AMOUNT"] = generatePayTmCheckSumRequest.TXN_AMOUNT.trim()
            paramMap["WEBSITE"] = generatePayTmCheckSumRequest.WEBSITE.trim()
            paramMap["INDUSTRY_TYPE_ID"] = generatePayTmCheckSumRequest.INDUSTRY_TYPE_ID.trim()
            paramMap["CALLBACK_URL"] = generatePayTmCheckSumRequest.CALLBACK_URL.trim()
            paramMap["CHECKSUMHASH"] = checksum.trim()
            CHECKSUM = checksum

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val paytmOrder = PaytmOrder(paramMap)
        paytmPGService.initialize(paytmOrder, null)

        paytmPGService.startPaymentTransaction(this, true, true, object : PaytmPaymentTransactionCallback {
            override fun onTransactionResponse(inResponse: Bundle) {
                val updateTransactionRequest = UpdateTransactionRequest()
                updateTransactionRequest.user_id = pref!!.userdata!!.user_id
                updateTransactionRequest.language = FantasyApplication.getInstance().getLanguage()
                updateTransactionRequest.order_id = inResponse.getString("ORDERID")
                updateTransactionRequest.txn_id = inResponse.getString("TXNID")
                updateTransactionRequest.banktxn_id = inResponse.getString("BANKTXNID")
                updateTransactionRequest.txn_date = inResponse.getString("TXNDATE")
                updateTransactionRequest.txn_amount = inResponse.getString("TXNAMOUNT")
                updateTransactionRequest.currency = inResponse.getString("CURRENCY")
                updateTransactionRequest.gateway_name = inResponse.getString("GATEWAYNAME")
                updateTransactionRequest.checksum = checksum
                callUpdateTransactionApi(updateTransactionRequest)
            }

            override fun networkNotAvailable() {}
            override fun clientAuthenticationFailed(inErrorMessage: String) {
                AppDelegate.showToast(this@AddCashActivity, inErrorMessage)
            }

            override fun someUIErrorOccurred(inErrorMessage: String) {
                AppDelegate.showToast(this@AddCashActivity, inErrorMessage)
            }

            override fun onErrorLoadingWebPage(iniErrorCode: Int, inErrorMessage: String, inFailingUrl: String) {
                AppDelegate.showToast(this@AddCashActivity, inErrorMessage)
            }

            override fun onBackPressedCancelTransaction() {}
            override fun onTransactionCancel(inErrorMessage: String, inResponse: Bundle) {
                AppDelegate.showToast(this@AddCashActivity, inErrorMessage)
            }
        })
    }

    private fun callUpdateTransactionApi(updateTransactionRequest: UpdateTransactionRequest) {
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@AddCashActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .update_transactions(updateTransactionRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@AddCashActivity)
                AppDelegate.showToast(this@AddCashActivity, response.response!!.message)
                if (response.response!!.status) {

                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@AddCashActivity)
            }
        }
    }

    private fun getRendomOrderID(): String {
        val r = Random(System.currentTimeMillis())
        return ("PAY" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000))
    }

    private fun payUsingRazorPay() {
        Checkout.preload(applicationContext)
        val checkout = Checkout()
        val image = R.mipmap.ic_launcher // Can be any drawable
        checkout.setImage(image)
        try {
            options = JSONObject()
            options!!.put("name", getString(R.string.app_name))
            options!!.put("description", "Amount to be added")
            //You can omit the image option to fetch the image from dashboard
            //options.put("image", "http://18.223.243.107:3012/static/media/logo.0174d733.png");
            options!!.put("currency", "INR")
            options!!.put("amount", Integer.parseInt(et_addCash.getText().toString()) * 100)
            val preFill = JSONObject()
            preFill.put("email", pref!!.userdata!!.email)
            preFill.put("contact", pref!!.userdata!!.phone)
            options!!.put("prefill", preFill)
            Log.e("tag", "razor req." + options.toString())
            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_balance)
        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.add_cash)
        setMenu(false, false, false, false)
        btn_addCash.setOnClickListener(this)
    }

    private fun getRendomBankTxnID(): String {
        val r = Random(System.currentTimeMillis())
        return ("BANKTXNID" + (1 + r.nextInt(1)) * 10000
                + r.nextInt(10000))
    }

    private fun getRendomChecksum(): String {
        val r = Random(System.currentTimeMillis())
        return ("CHECKSUM" + (1 + r.nextInt(3)) * 10000
                + r.nextInt(10000))
    }

    override fun onPaymentSuccess(paymentId: String) {
        Log.e("tag", "onPaymentSuccess$paymentId")
        try {
            val updateTransactionRequest = UpdateTransactionRequest()
            updateTransactionRequest.user_id = pref!!.userdata!!.user_id
            updateTransactionRequest.language = FantasyApplication.getInstance().getLanguage()
            updateTransactionRequest.order_id =  getRendomOrderID()
            updateTransactionRequest.txn_id = paymentId
            updateTransactionRequest.banktxn_id = getRendomBankTxnID()
            updateTransactionRequest.txn_date = AppDelegate.convertTimestampToDate(System.currentTimeMillis())
            updateTransactionRequest.txn_amount = options!!.getString("amount")
            updateTransactionRequest.currency = options!!.getString("currency")
            updateTransactionRequest.gateway_name ="RAZORPAY"
            updateTransactionRequest.checksum = getRendomChecksum()
            callUpdateTransactionApi(updateTransactionRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPaymentError(i: Int, s: String) {
        Log.e("tag", "onPaymentError$s")
    }
}