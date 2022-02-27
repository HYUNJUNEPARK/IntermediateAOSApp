package com.june.pushalarmreceiver.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.june.pushalarmreceiver.R
import com.june.pushalarmreceiver.activity.MainActivity
import com.june.pushalarmreceiver.notification.NotificationType

class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val CHANNEL_NAME ="Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Channel For Emoji Party"
        private const val CHANNEL_ID = "Channel Id"
        private const val EXPANDABLE_EMOJI_SAMPLE = "\uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83D\uDE02 \uD83E\uDD23 \uD83E\uDD72 \uD83D\uDE0A \uD83D\uDE07 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0C \uD83D\uDE0D \uD83E\uDD70 \uD83D\uDE18 \uD83D\uDE17 \uD83D\uDE19 \uD83D\uDE1A \uD83D\uDE0B \uD83D\uDE1B \uD83D\uDE1D \uD83D\uDE1C \uD83E\uDD2A \uD83E\uDD28 \uD83E\uDDD0 \uD83E\uDD13 \uD83D\uDE0E \uD83E\uDD78 \uD83E\uDD29 \uD83E\uDD73 \uD83D\uDE0F \uD83D\uDE12 \uD83D\uDE1E \uD83D\uDE14 \uD83D\uDE1F \uD83D\uDE15 \uD83D\uDE41 \uD83D\uDE23 \uD83D\uDE16 \uD83D\uDE2B \uD83D\uDE29 \uD83E\uDD7A \uD83D\uDE22 \uD83D\uDE2D \uD83D\uDE24 \uD83D\uDE20 \uD83D\uDE21 \uD83E\uDD2C \uD83E\uDD2F \uD83D\uDE33 \uD83E\uDD75 \uD83E\uDD76 \uD83D\uDE31 \uD83D\uDE28 \uD83D\uDE30 \uD83D\uDE25 \uD83D\uDE13 \uD83E\uDD17 \uD83E\uDD14 \uD83E\uDD2D \uD83E\uDD2B \uD83E\uDD25 \uD83D\uDE36 \uD83D\uDE10 \uD83D\uDE11 \uD83D\uDE2C \uD83D\uDE44 \uD83D\uDE2F \uD83D\uDE26 \uD83D\uDE27 \uD83D\uDE2E \uD83D\uDE32 \uD83E\uDD71 \uD83D\uDE34 \uD83E\uDD24 \uD83D\uDE2A \uD83D\uDE35 \uD83E\uDD10 \uD83E\uDD74"
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannel()

        //알림 컨텐츠
        val type = remoteMessage.data[/*NotificationType*/"type"]
            ?.let { NotificationType.valueOf(it) } //enum 이름과 동일한 값을 전달했을 때 그에 상응하는 enum 값을 it 으로 전달
        val title = remoteMessage.data["title"] ?: "Empty Title"
        val message = remoteMessage.data["message"] ?: "Empty Message"

        if (type == null) { return } //타입이 없으면 알림을 생성하지 않음

        val notificationManager = NotificationManagerCompat.from(this)
        val notification = createNotification(type, title, message)
        notificationManager.notify(type.id, notification) //아이디와 알림을 전달
        //동일한 아이디로 notify 하면 알림이 덮어씌워짐. 타입으로 알림 구분
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, /*중요도 수준*/NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESCRIPTION
            val notificationService = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationService.createNotificationChannel(channel)
        }
    }

    private fun createNotification(type: NotificationType, title: String, message: String): Notification {

        //알림 탭
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        //같은 액티비티가 있다면 스택으로 쌓지 않음. 화면이 떠있는 상태에서 알림을 눌렀을 때 같은 액티비티가 또 호출되면 어색함.
        //알림이 여러개 와도 팬딩인텐트를 여러개 생성하지 않음
        //마지막에 custom 알림이 오면 "${type.title} 타입" 은 커스텀으로 지정됨 -> 아래코드에서 해결
        }
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)
        //nomal, custom, expandable 에 한해서는 인텐트가 업데이트 됨
        //결과적으로 타입별로는 펜딩인텐트가 달라짐, 같은 타입은 같은 펜딩 이벤트를 사용
        /*
        팬딩 인텐트
        notificationManager 가 intent 를 보고 판단해 수행 가능할 때 처리
        */


        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //오래오 버전 이하 중요도 수준 설정(알림마다 별도로 설정)(오래오 이상에서는 채널에서 중요도 설정)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(EXPANDABLE_EMOJI_SAMPLE)
                )
            }
            NotificationType.CUSTOM -> {
                notificationBuilder
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.title, title)
                            setTextViewText(R.id.message, message)
                        }
                    )
            }
        }
        return notificationBuilder.build()
    }
}