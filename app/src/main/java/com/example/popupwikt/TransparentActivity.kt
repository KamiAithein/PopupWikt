package com.example.popupwikt

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.ContentType

import kotlinx.android.synthetic.main.activity_transparent.*
import kotlinx.coroutines.*
import org.json.JSONObject

class TransparentActivity : AppCompatActivity() {

    private val wiktURL = "https://en.wikipedia.org/w/api.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = intent.extras?.get(Intent.EXTRA_PROCESS_TEXT).toString()
        setContentView(R.layout.activity_transparent)
        val html = runBlocking {
            queryWikt(text).readText()
        }
        dialog(text, html)

    }

    private suspend fun httpRequest(url: String): HttpResponse{
        val client = HttpClient()
        val htmlContent = client.get<HttpResponse>(url)
        client.close()
        return htmlContent
    }

    private suspend fun queryWikt(query: String): HttpResponse {
        return httpRequest("$wiktURL?action=query&list=search&srsearch=$query&format=json")
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

}
