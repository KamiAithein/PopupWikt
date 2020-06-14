package com.example.popupwikt

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.ContentType
import io.ktor.http.isSuccess

import kotlinx.android.synthetic.main.activity_transparent.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.StringReader

suspend fun httpRequest(url: String): HttpResponse {
    val client = HttpClient()
    val htmlContent = client.get<HttpResponse>(url)
    client.close()
    return htmlContent
}

class TransparentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = intent.extras?.get(Intent.EXTRA_PROCESS_TEXT).toString()
        setContentView(R.layout.activity_transparent)



        //Try to get actual page
        val response = runBlocking{
            pageWikt(text)
        }
        if(response.status.isSuccess()){
            val webview = findViewById<WebView>(R.id.webView)

            runBlocking{
                val respText = response.readText();
            }
            webview.webViewClient = WebViewClient()
            webview.settings.javaScriptEnabled = true
            webview.settings.domStorageEnabled = true
            webview.overScrollMode = WebView.OVER_SCROLL_NEVER
            webview.loadUrl(getPageUrl(text))

        }
        else{
            runBlocking{
                dialog(text, getSnippets(queryWikt(text).readText()).toString())
            }
        }

    }

    private fun dialog(title: Int, message: Int){
        val builder: AlertDialog.Builder? = this.let{AlertDialog.Builder(it)}

        builder?.setMessage(message)?.setTitle(title)

        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

    private fun dialog(title: String, message: String){
        val builder: AlertDialog.Builder? = this.let{AlertDialog.Builder(it)}

        builder?.setMessage(message)?.setTitle(title)

        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

    companion object WiktAccess{

        private val wiktURL = "https://en.wiktionary.org"
        private val wiktAPI = "$wiktURL/w/api.php"
        private val wiktPage = "$wiktURL/wiki"
        private val klax = Klaxon()



        private fun getPageUrl(page: String)
                = "$wiktPage/$page"

        private fun getQueryUrl(query: String)
                = "$wiktAPI?action=query&list=search&srsearch=$query&format=json"

        private suspend fun pageWikt(page: String): HttpResponse
                = httpRequest(getPageUrl(page))

        private suspend fun queryWikt(query: String): HttpResponse
                = httpRequest(getQueryUrl(query))

        /**
         * Gets snippets from a wiktionary query
         * @param rawQuery string query of wiktionary
         * @return listOf(rawQuery.filter(snippets))
         */
        private fun getSnippets(rawQuery: String)
                = ((klax.parseJsonObject(StringReader(rawQuery))
                ["query"] as JsonObject)
                ["search"] as JsonArray<*>)
                .flatMap{ (it as JsonObject).entries.filter {entries -> entries.key == "snippet" }}
    }
}
