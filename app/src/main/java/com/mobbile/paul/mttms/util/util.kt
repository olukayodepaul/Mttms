package com.mobbile.paul.mttms.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import com.mobbile.paul.mttms.R

object Util{
    fun showSomeDialog(context: Context, msg: String, title: String?) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogDanger)
        builder.setMessage(msg)
            .setTitle(title)
            .setIcon(R.drawable.icons8_google_alerts_100)
            .setCancelable(false)
            .setNegativeButton("Ok") { _, _ ->
            }
        val dialog  = builder.create()
        dialog.show()
    }

    fun showMsgDialog(activity: Activity, context: Context,title: String, msg: String?) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogDanger)
        builder.setMessage(msg)
            .setTitle(title)
            .setCancelable(false)
            .setIcon(R.drawable.icons8_google_alerts_100)
            .setNegativeButton(
                "OK"
            ) { _, _ ->
                    var intent = Intent(context, activity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    context.startActivity(intent)
            }
        val dialog = builder.create()
        dialog.show()
    }
}

