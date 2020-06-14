package com.example.popupwikt

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import io.ktor.http.ContentType

import kotlinx.android.synthetic.main.activity_transparent.*

class TransparentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val text = getIntent().getCharSequenceArrayExtra(Intent.EXTRA_PROCESS_TEXT)
        setContentView(R.layout.activity_transparent)

        dialog(R.string.dialog_fire_missiles, R.string.dialog_fire_missiles)
    }

    private fun ContentType.Application.module(){
        routing{

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

}
