package os.com.ui.addCash.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.application.FantasyApplication
import os.com.constant.AppRequestCodes
import os.com.constant.IntentConstant
import os.com.constant.IntentConstant.OFFER
import os.com.constant.IntentConstant.OFFER_BANNER
import os.com.constant.IntentConstant.TO_JOIN
import os.com.constant.Tags
import os.com.networkCall.ApiClient
import os.com.ui.addCash.apiRequest.GeneratePayTmCheckSumRequest
import os.com.ui.addCash.apiRequest.UpdateTransactionRequest
import os.com.ui.dashboard.home.apiResponse.bannerList.Offer
import os.com.ui.dashboard.profile.apiResponse.ApplyCouponCodeResponse.Data
import os.com.utils.AppDelegate
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random


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
                if (!et_addCash.text.toString().isEmpty())
                    checkPermission()
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
            showPaymentOption()
        }
    }

    private fun showPaymentOption(
    ) {
        val dialogue = Dialog(this)
        dialogue.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogue.setContentView(R.layout.dialogue_add_cash_option)
        dialogue.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialogue.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogue.setCancelable(true)
        dialogue.setCanceledOnTouchOutside(true)
        dialogue.setTitle(null)
        dialogue.txt_balance.text = getString(R.string.Rs) + " " + et_addCash.text.toString()
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
                    startPaytmTransaction(generatePayTmCheckSumRequest, response.response!!.data!!.checksum)
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
                if (AddTYPE.equals(OFFER)) {
                    updateTransactionRequest.coupon_id = data!!.coupon_id
                    updateTransactionRequest.discount_amount = discountedValue.toString()
                } else if (AddTYPE.equals(OFFER_BANNER)) {
                    updateTransactionRequest.coupon_id = data_OFFER!!.coupon_id
                    updateTransactionRequest.discount_amount = discountedValue.toString()
                }
                val request = ApiClient.client
                    .getRetrofitService()
                    .update_transactions(updateTransactionRequest)
                val response = request.await()
                AppDelegate.LogT("Response=>" + response);
                AppDelegate.hideProgressDialog(this@AddCashActivity)
                AppDelegate.showToast(this@AddCashActivity, response.response!!.message)
                if (response.response!!.status) {
                    if (AddTYPE == TO_JOIN) {
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        et_addCash.setText("")
                        finish()
                    }
                } else {
                }
            } catch (exception: Exception) {
                AppDelegate.hideProgressDialog(this@AddCashActivity)
            }
        }
    }

    override fun onBackPressed() {
        if (AddTYPE == TO_JOIN) {
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED, intent)
        }
        super.onBackPressed()
    }

    private fun getRendomOrderID(): String {
        val r = Random(System.currentTimeMillis())
        return ((1 + r.nextInt(2)) * 10000
                + r.nextInt(10000)).toString()
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

    var currentBalance = "0.0"
    var AddTYPE = IntentConstant.ADD
    var data: Data? = null
    var data_OFFER: Offer? = null
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
            currentBalance = intent.getStringExtra(IntentConstant.currentBalance)
            txt_amount.setText(getString(R.string.Rs) + " " + currentBalance)
            AddTYPE = intent.getIntExtra(IntentConstant.AddType, IntentConstant.ADD)
            if (AddTYPE.equals(IntentConstant.OFFER)) {
                data = intent.getParcelableExtra(Tags.DATA)
                ll_offer.visibility = VISIBLE
                AddTextChangeListener()
            } else if (AddTYPE.equals(IntentConstant.OFFER_BANNER)) {
                data_OFFER = intent.getParcelableExtra(Tags.DATA)
                ll_offer.visibility = VISIBLE
                AddTextChangeListener()
            } else if (AddTYPE.equals(IntentConstant.TO_JOIN)) {
                var toPay = intent.getStringExtra(IntentConstant.AMOUNT_TO_ADD)
                et_addCash.setText(toPay)
            } else
                et_addCash.setText("100")
        }
    }

    private fun AddTextChangeListener() {
        var min_amount = 0.0
        var max_discount = 0.0
        if (AddTYPE.equals(IntentConstant.OFFER)) {
            if (!data!!.min_amount.isEmpty())
                min_amount = data!!.min_amount.toDouble()
            if (!data!!.max_discount.isEmpty())
                max_discount = data!!.max_discount.toDouble()
            tv_offerLabel.text = "Minimum " + getString(R.string.Rs) + " " + min_amount +
                    " is reqired to avail this offer"
        } else if (AddTYPE.equals(IntentConstant.OFFER_BANNER)) {
            if (!data_OFFER!!.min_amount.isEmpty())
                min_amount = data_OFFER!!.min_amount.toDouble()
            if (!data_OFFER!!.max_discount.isEmpty())
                max_discount = data_OFFER!!.max_discount.toDouble()
            tv_offerLabel.text = "Minimum " + getString(R.string.Rs) + " " + min_amount +
                    " is reqired to avail this offer"
        }
        et_addCash.setText(min_amount.roundToInt().toString())

        et_addCash.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!et_addCash.text.toString().isEmpty() && et_addCash.text.toString().toFloat() >= min_amount) {
                    GlobalScope.launch(Dispatchers.Main) {
                        delay(200)
                        applyOffer(max_discount)
                    }
                } else {
                    txt_offer.text = getString(R.string.Rs) + " 0.0"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    var discountedValue = 0.0
    private fun applyOffer(max_discount: Double) {
        if (AddTYPE == OFFER) {
            if (data != null) {
                discountedValue = 0.0
                val discountAmount = data!!.discount_amount.toDouble()
                if (data!!.in_percentage) {
                    if (!et_addCash.text.toString().isEmpty())
                        discountedValue = (et_addCash.text.toString().toDouble().times(discountAmount)) / 100
                    else
                        discountedValue = 0.0
                    if (discountedValue > max_discount)
                        discountedValue = max_discount
                } else
                    discountedValue = discountAmount.toDouble()
                txt_offer.text = getString(R.string.Rs) + " " + discountedValue
            }
        } else if (AddTYPE == OFFER_BANNER) {
            if (data_OFFER != null) {
                discountedValue = 0.0
                val discountAmount = data_OFFER!!.discount_amount.toDouble()
                if (data_OFFER!!.in_percentage) {
                    if (!et_addCash.text.toString().isEmpty())
                        discountedValue = (et_addCash.text.toString().toDouble().times(discountAmount)) / 100
                    else
                        discountedValue = 0.0
                    if (discountedValue > max_discount)
                        discountedValue = max_discount
                } else
                    discountedValue = discountAmount
                txt_offer.text = getString(R.string.Rs) + " " + discountedValue
            }
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

    override fun onPaymentSuccess(paymentId: String) {
        Log.e("tag", "onPaymentSuccess$paymentId")
        try {
            val cal = Calendar.getInstance()
            val updateTransactionRequest = UpdateTransactionRequest()
            updateTransactionRequest.user_id = pref!!.userdata!!.user_id
            updateTransactionRequest.language = FantasyApplication.getInstance().getLanguage()
            updateTransactionRequest.order_id = "RAZ" + getRendomOrderID()
            updateTransactionRequest.txn_id = paymentId
            updateTransactionRequest.banktxn_id = getRendomBankTxnID()
            updateTransactionRequest.txn_date =
                    cal.time.toString()/*AppDelegate.convertTimestampToDate(System.currentTimeMillis())*/
            /*   DateFormat.getDateTimeInstance().format(Date());*/
            updateTransactionRequest.txn_amount = et_addCash.text.toString()
            updateTransactionRequest.currency = options!!.getString("currency")
            updateTransactionRequest.gateway_name = "Razorpay"
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