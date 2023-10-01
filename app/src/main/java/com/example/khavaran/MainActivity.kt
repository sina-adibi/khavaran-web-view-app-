package com.example.khavaran

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.khavaran.ui.theme.KhavaranTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KhavaranTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.background
                ) {
                    WebViewPage(url = "http://khavaran-tmk.ir/")
                }
            }
        }
    }
}

@Composable
fun WebViewPage(url: String) {
    var backEnabled by remember { mutableStateOf(false) }
    val mutableStateTrigger = remember { mutableStateOf(false) }
    var webView: WebView? = null
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()

            settings.javaScriptEnabled = true

            settings.userAgentString = System.getProperty("http.agent")

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    if (view != null) {
                        backEnabled = view.canGoBack()
                    }
                }
            }

//            webViewClient= object :WebViewClient(){
//                override fun onReceivedError(
//                    view: WebView?,
//                    request: WebResourceRequest?,
//                    error: WebResourceError?
//                ) {
//                    super.onReceivedError(view, request, error)
//
//                    loadUrl("/home/sina/AndroidStudioProjects/khavaran/app/src/main/assets/404.html")
//                    mutableStateTrigger.value = true
//                }
//            }

            loadUrl(url)
            webView = this
        }
    }, update = {
        webView = it
        //it.loadUrl(url)
    })
    BackHandler(enabled = backEnabled) {
        webView?.goBack()
    }

    if (mutableStateTrigger.value){
        WebViewPage(url)
    }
}