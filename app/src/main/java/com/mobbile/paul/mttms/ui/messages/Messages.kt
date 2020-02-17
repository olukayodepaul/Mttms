package com.mobbile.paul.mttms.ui.messages

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.mobbile.paul.mttms.R
import java.lang.Exception

class Messages : AppCompatActivity() {

    var bundle: Bundle? = null
    var topic = "message"
    var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

       // FirebaseMessaging.getInstance().subscribeToTopic(topic)
    }

    /*@SuppressLint("LongLogTag")
    override fun onResume() {
        super.onResume()

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(
            OnCompleteListener {task->
                if(!task.isSuccessful)
                    return@OnCompleteListener
                Log.d(TAG, "${task.result!!.token}")

                try {
                    bundle = intent.extras!!
                }catch (e: Exception){

                }

                if(bundle!=null){
                    Log.d(TAG,"${bundle!!.getString("title")} ${bundle!!.getString("body")}")
                }
            }
        )


    }*/

    companion object{
        val TAG = "HDCKJDWNKNWDKNJWDKNDWKJNC"
    }
}
