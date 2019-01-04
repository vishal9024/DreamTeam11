package os.com.ui.dashboard.more.activity


import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.content_webview.*
import os.com.AppBase.BaseActivity
import os.com.R

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
        setSupportActionBar(toolbar)
        toolbarTitleTv.setText(slug);
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val webSettings = webView.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webSettings.setDomStorageEnabled(true)
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE)
        WebView.setWebContentsDebuggingEnabled(true)

        // Initialize Link by loading the Link initiaization URL in the Webview
        webView.loadUrl("https://www.octalsoftware.com/")

    }


}
