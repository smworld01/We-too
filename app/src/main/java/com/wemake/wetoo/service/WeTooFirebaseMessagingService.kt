package com.wemake.wetoo.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wemake.wetoo.MainActivity
import com.wemake.wetoo.R


class WeTooFirebaseMessagingService : FirebaseMessagingService() {
    private val db = Firebase.firestore

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val uid = Firebase.auth.uid

        Log.e("test", token)
        if (uid != null) {
            db.collection("users").document(uid)
                .update(mapOf("fcm_token" to token))
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if(remoteMessage.data.isNotEmpty()){
            Log.i("바디: ", remoteMessage.data["body"].toString())
            Log.i("타이틀: ", remoteMessage.data["title"].toString())
            sendNotification(remoteMessage)
        }

        else {
            Log.i("수신에러: ", "data가 비어있습니다. 메시지를 수신하지 못했습니다.")
            Log.i("data값: ", remoteMessage.data.toString())
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시되도록 함
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        // 일회용 PendingIntent
        // PendingIntent : Intent 의 실행 권한을 외부의 어플리케이션에게 위임한다.
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Activity Stack 을 경로만 남긴다. A-B-C-D-B => A-B
        val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT)

        // 알림 채널 이름
        val channelId = "s"

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보와 작업을 지정한다.
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(remoteMessage.data["body"].toString()) // 제목
            .setContentText(remoteMessage.data["title"].toString()) // 메시지 내용
            .setAutoCancel(true)
            .setSound(soundUri) // 알림 소리
            .setContentIntent(pendingIntent) // 알림 실행 시 Intent
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널이 필요하다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(uniId, notificationBuilder.build())
    }
}