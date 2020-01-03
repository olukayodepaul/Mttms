package com.mobbile.paul.mttms



import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mobbile.paul.mttms.ui.messages.Messages

@SuppressLint("Registered")
class FireBaseIncomingMessages: FirebaseMessagingService() {

    var title = ""
    var body = ""
    val RC_INTERNET = 100
    val CHANNEL_ID = "messages"

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    //assuming the incoming message is a notification not a data
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val intent = Intent(this, Messages::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        if(p0.notification!=null) {
            body = p0.notification!!.body!!
            title = p0.notification!!.title!!
            sendMessage(title, body, intent)
        }
    }

    fun sendMessage(title:String, body:String, intent:Intent) {

        //call pending intent in the application
        val pendingindent:PendingIntent = PendingIntent.getActivity(this, RC_INTERNET,
            intent, PendingIntent.FLAG_ONE_SHOT)

        //choose ringing tone
        val ringingTone: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val audioAttributes:AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        //create a notification manager
        val notificationManeger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //for android 8 sdk
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel= NotificationChannel(CHANNEL_ID, "messages", NotificationManager.IMPORTANCE_HIGH )
            mChannel.description = "Message"
            mChannel.setSound(ringingTone, audioAttributes)
            notificationManeger.createNotificationChannel(mChannel)
        }

        //build a notification
        val notificationBuilder: Notification = NotificationCompat.Builder(baseContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.logos)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.logos))
            .setContentText(title)
            .setContentText(body)
            .setSound(ringingTone)
            .setContentIntent(pendingindent)
            .setAutoCancel(true)
            .build()
        notificationManeger.notify(RC_INTERNET, notificationBuilder)
    }


}