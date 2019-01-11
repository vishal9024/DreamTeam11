package os.com.ui.dashboard.more.activity


import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_webview.*
import os.com.AppBase.BaseActivity
import os.com.R
import os.com.utils.AppDelegate




class WebViewActivity : BaseActivity() {

    var slug: String = ""
    var url: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        slug = if (intent.hasExtra("PAGE_SLUG")) intent.getStringExtra("PAGE_SLUG") else ""
        url = if (intent.hasExtra("URL")) intent.getStringExtra("URL") else ""
        initView()
    }


    override fun onBackPressed() {
        finish()
    }

    private fun initView() {
        try {
            setSupportActionBar(toolbar)
            toolbarTitleTv.setText(slug);
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)


//            // Initialize Link by loading the Link initiaization URL in the Webview
//            if (url != "")
//                webView.loadUrl(url)

            webView.setWebViewClient(object : WebViewClient() {

                //If you will not use this method url links are opeen in new brower not in webview
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }

                //Show loader on url load
                override fun onLoadResource(view: WebView, url: String) {
//                    progress_bar.visibility= View.VISIBLE
                    AppDelegate.showProgressDialogCancelable(this@WebViewActivity)

                }

                override fun onPageFinished(view: WebView, url: String) {
                    try {
//                        progress_bar.visibility= View.GONE
                        AppDelegate.hideProgressDialog(this@WebViewActivity)
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                }

                override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                    AppDelegate.hideProgressDialog(this@WebViewActivity)
                }
            })
            val webSettings = webView.getSettings()
            webSettings.setJavaScriptEnabled(true)
            webSettings.setDomStorageEnabled(true)
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE)
            WebView.setWebContentsDebuggingEnabled(true)
            webView.loadUrl(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
