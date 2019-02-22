package os.com.ui.addCash.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gocashfree.cashfreesdk.CFClientInterface
import com.gocashfree.cashfreesdk.CFPaymentService
import com.google.gson.JsonObject
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import kotlinx.android.synthetic.main.activity_add_balance.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.networkCall.ApiClient
import os.com.ui.addCash.apiRequest.GeneratePayTmCheckSumRequest
import os.com.ui.addCash.apiRequest.UpdateTransactionRequest
import os.com.utils.AppDelegate
import java.util.*
import kotlin.random.Random


class AddCashInWalletActivity : BaseActivity(), View.OnClickListener, CFClientInterface /*PaymentResultListener*/ {

    internal var orderId: String = ""
    internal var callbackURL: String = ""
    internal var mWalletBalance: String = ""
    internal var CHECKSUM = ""
    internal var amount = ""
    internal var options: JSONObject? = null
    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_addCash -> {
                if (!et_addCash.text.toString().isEmpty())
                    checkPermission()
                else
                    AppDelegate.showToast(this, getString(R.string.add_amount_validation))
            }
            R.id.txt_500 -> {
                et_addCash.setText("500")
                checkPermission()
            }
            R.id.txt_200 -> {
                et_addCash.setText("200")
                checkPermission()
            }
            R.id.txt_100 -> {
                et_addCash.setText("100")
                checkPermission()
            }
        }
    }

    fun checkPermission() {
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
            if (PAYMENT_GATEWAY_TYPE == AppRequestCodes.PAYTM)
                payUsingPaytm()
            else
                payUsingCashfree()
        }
    }

    var PAYMENT_GATEWAY_TYPE = AppRequestCodes.PAYTM
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (PAYMENT_GATEWAY_TYPE == AppRequestCodes.PAYTM)
                    payUsingPaytm()
                else
                    payUsingCashfree()
            } else {

            }
        }
    }

    private fun payUsingPaytm() {
        if (AppDelegate.isNetworkAvailable(this)) {
            try {
                AppDelegate.showProgressDialog(this)
                orderId = "PAY" + getRendomOrderID()
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
            AppDelegate.showProgressDialog(this@AddCashInWalletActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitService()
                    .generate_paytm_checksum(generatePayTmCheckSumRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@AddCashInWalletActivity)
                if (response.response!!.status) {
//                    generatePayTmCheckSumRequest.CHECKSUMHASH = response.response!!.data!!.checksum
                    startPaytmTransaction(generatePayTmCheckSumRequest, response.response!!.data!!.checksum)
                } else {
                    logoutIfDeactivate(response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@AddCashInWalletActivity)
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
                AppDelegate.showToast(this@AddCashInWalletActivity, inErrorMessage)
            }

            override fun someUIErrorOccurred(inErrorMessage: String) {
                AppDelegate.showToast(this@AddCashInWalletActivity, inErrorMessage)
            }

            override fun onErrorLoadingWebPage(iniErrorCode: Int, inErrorMessage: String, inFailingUrl: String) {
                AppDelegate.showToast(this@AddCashInWalletActivity, inErrorMessage)
            }

            override fun onBackPressedCancelTransaction() {}
            override fun onTransactionCancel(inErrorMessage: String, inResponse: Bundle) {
                AppDelegate.showToast(this@AddCashInWalletActivity, inErrorMessage)
            }
        })
    }

    private fun callUpdateTransactionApi(updateTransactionRequest: UpdateTransactionRequest) {
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@AddCashInWalletActivity)
            try {
                updateTransactionRequest.coupon_id = ""
                updateTransactionRequest.discount_amount = ""
                val request = ApiClient.client
                    .getRetrofitService()
                    .update_transactions(updateTransactionRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@AddCashInWalletActivity)
                AppDelegate.showToast(this@AddCashInWalletActivity, response.response!!.message)
                if (response.response!!.status) {
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    logoutIfDeactivate(response.response!!.message)
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@AddCashInWalletActivity)
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        super.onBackPressed()
    }

    private fun getRendomOrderID(): String {
        val r = Random(System.currentTimeMillis())
        return ((1 + r.nextInt(2)) * 10000
                + r.nextInt(10000)).toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_balance)
        initViews()
    }

    var currentBalance = "0.0"
    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        toolbarTitleTv.setText(R.string.add_cash)
        setMenu(false, false, false, false, false)
        btn_addCash.setOnClickListener(this)
        txt_100.setOnClickListener(this)
        txt_200.setOnClickListener(this)
        txt_500.setOnClickListener(this)
        if (intent != null) {
            PAYMENT_GATEWAY_TYPE = intent.getIntExtra(IntentConstant.PAYMENT_GATEWAY, AppRequestCodes.PAYTM)
            currentBalance = intent.getStringExtra(IntentConstant.currentBalance)
            txt_amount.setText(getString(R.string.Rs) + " " + currentBalance)
            et_addCash.setText("100")
        }
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

    private fun payUsingCashfree() {
        val orderId = getRendomOrderID()
        val cf_Request = JsonObject()
        cf_Request.addProperty("orderId", orderId)
        cf_Request.addProperty("orderCurrency", "INR")
        cf_Request.addProperty("orderAmount", java.lang.Double.parseDouble(et_addCash.getText().toString()))
        GlobalScope.launch(Dispatchers.Main) {
            AppDelegate.showProgressDialog(this@AddCashInWalletActivity)
            try {
                val request = ApiClient.client
                    .getRetrofitServiceCashFree()
                    .cashFreeToken(
                        AppRequestCodes.getLiveCAshFreeClientID(),
                        AppRequestCodes.getLiveCAshFreeSecretKey(),
                        cf_Request
                    )
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@AddCashInWalletActivity)
//                    generatePayTmCheckSumRequest.CHECKSUMHASH = response.response!!.data!!.checksum
                try {
                    if (response.status.equals("OK", ignoreCase = true)) {
                        val token = response.cftoken
                        val params = HashMap<String, String>()
                        params.put("appId", AppRequestCodes.getLiveCAshFreeClientID())
                        params.put("orderId", orderId)
                        params.put("orderCurrency", "INR")
                        params.put("orderAmount", et_addCash.getText().toString())
                        params.put("customerName", pref!!.userdata!!.first_name)
                        params.put("customerPhone", pref!!.userdata!!.phone)
                        params.put("customerEmail", pref!!.userdata!!.email)
                        triggerPayment(false, token, params)
                    } else {
                        AppDelegate.showToast(this@AddCashInWalletActivity, response.message)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@AddCashInWalletActivity)
            }
        }
    }

    private fun triggerPayment(isUpiIntent: Boolean, token: String, params: Map<String, String>) {
        val stage = "TEST"
        val cfPaymentService = CFPaymentService.getCFPaymentServiceInstance()
        cfPaymentService.setOrientation(0)
        cfPaymentService.setConfirmOnExit(false)
        if (isUpiIntent) {
            // Use the following method for initiating UPI Intent Payments
            cfPaymentService.upiPayment(this, params, token, this, stage)
        } else {
            // Use the following method for initiating regular Payments
            cfPaymentService.doPayment(this, params, token, this, stage)
        }
    }


    override fun onSuccess(map: Map<String, String>) {
        AppDelegate.LogT("onSuccess" + map)
        AppDelegate.showToast(this, "onSuccess-" + map["txMsg"])
        if (map["txStatus"].equals("SUCCESS", ignoreCase = true)) {
            val cal = Calendar.getInstance()
            val updateTransactionRequest = UpdateTransactionRequest()
            updateTransactionRequest.user_id = pref!!.userdata!!.user_id
            updateTransactionRequest.language = FantasyApplication.getInstance().getLanguage()
            updateTransactionRequest.order_id = map["orderId"]!!
            updateTransactionRequest.txn_id = map["referenceId"]!!
            updateTransactionRequest.banktxn_id = getRendomBankTxnID()
            updateTransactionRequest.txn_date =map["txTime"]!!
//                    cal.time.toString()/*AppDelegate.convertTimestampToDate(System.currentTimeMillis())*/
            /*   DateFormat.getDateTimeInstance().format(Date());*/
            updateTransactionRequest.txn_amount = map["orderAmount"]!!
            updateTransactionRequest.currency = "INR"
            updateTransactionRequest.gateway_name = "CASHFREE"
            updateTransactionRequest.checksum = getRendomChecksum()
            callUpdateTransactionApi(updateTransactionRequest)
        } else {
            AppDelegate.showToast(this, map["txMsg"]!!)
        }
    }

    override fun onFailure(map: Map<String, String>) {
        AppDelegate.LogE("onFailure" + map)
    }

    override fun onNavigateBack() {
        AppDelegate.LogE("onNavigateBack")
    }


}