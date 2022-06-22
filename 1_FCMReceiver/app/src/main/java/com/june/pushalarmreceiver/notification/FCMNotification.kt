package com.june.pushalarmreceiver.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.june.pushalarmreceiver.R
import com.june.pushalarmreceiver.activity.MainActivity

class FCMNotification(private val context: Context) {
    companion object {
        const val CHANNEL_NAME = R.string.channel_name.toString()
        const val CHANNEL_DESCRIPTION = R.string.channel_description.toString()
        const val CHANNEL_ID = R.string.channel_id.toString()
        const val EXPANDABLE_EMOJI_SAMPLE = "\uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 \uD83D\uDE05 \uD83D\uDE02 \uD83E\uDD23 \uD83E\uDD72 \uD83D\uDE0A \uD83D\uDE07 \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0C \uD83D\uDE0D \uD83E\uDD70 \uD83D\uDE18 \uD83D\uDE17 \uD83D\uDE19 \uD83D\uDE1A \uD83D\uDE0B \uD83D\uDE1B \uD83D\uDE1D \uD83D\uDE1C \uD83E\uDD2A \uD83E\uDD28 \uD83E\uDDD0 \uD83E\uDD13 \uD83D\uDE0E \uD83E\uDD78 \uD83E\uDD29 \uD83E\uDD73 \uD83D\uDE0F \uD83D\uDE12 \uD83D\uDE1E \uD83D\uDE14 \uD83D\uDE1F \uD83D\uDE15 \uD83D\uDE41 \uD83D\uDE23 \uD83D\uDE16 \uD83D\uDE2B \uD83D\uDE29 \uD83E\uDD7A \uD83D\uDE22 \uD83D\uDE2D \uD83D\uDE24 \uD83D\uDE20 \uD83D\uDE21 \uD83E\uDD2C \uD83E\uDD2F \uD83D\uDE33 \uD83E\uDD75 \uD83E\uDD76 \uD83D\uDE31 \uD83D\uDE28 \uD83D\uDE30 \uD83D\uDE25 \uD83D\uDE13 \uD83E\uDD17 \uD83E\uDD14 \uD83E\uDD2D \uD83E\uDD2B \uD83E\uDD25 \uD83D\uDE36 \uD83D\uDE10 \uD83D\uDE11 \uD83D\uDE2C \uD83D\uDE44 \uD83D\uDE2F \uD83D\uDE26 \uD83D\uDE27 \uD83D\uDE2E \uD83D\uDE32 \uD83E\uDD71 \uD83D\uDE34 \uD83E\uDD24 \uD83D\uDE2A \uD83D\uDE35 \uD83E\uDD10 \uD83E\uDD74"
    }

    fun notificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //O 버전 이상 중요도 설정
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, /*중요도 수준*/
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESCRIPTION
            val notificationService = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationService.createNotificationChannel(channel)
        }
    }

    fun initNotification(remoteMessage: RemoteMessage) {
        val type : NotificationType? = remoteMessage.data["type"]
            //*NotificationType : NORMAL(title, id), EXPANDABLE(title, id), CUSTOM(title, id)
            ?.let { type -> //서버로 부터 넘어온 type : NORMAL, EXPANDABLE, CUSTOM
                NotificationType.valueOf(type) //valueOf() : 일치하는 enum constant 가 없으면 IllegalArgumentException 발생
            }
        val title : String = remoteMessage.data["title"] ?: "Empty Title"
        val message : String = remoteMessage.data["message"] ?: "Empty Message"

        if (type == null) { return }

        val notificationManager = NotificationManagerCompat.from(context)
        val notification = createNotification(type, title, message)
        notificationManager.notify(type.id, notification) //type 으로 알림을 구분하며 동일한 id 알림은 덮어 씌움
    }

    private fun createNotification(type: NotificationType, title: String, message: String): Notification {
        val pendingIntent = createPendingIntent(type)
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) //O 버전 이하 중요도 수준 설정
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
                            context.packageName,
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

    private fun createPendingIntent(type: NotificationType): PendingIntent {
        val intentForTab = Intent(context, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입") //type.title : 일반 알림, 확장형 알림, 커스텀 알림
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        return PendingIntent.getActivity(context, /*requestCode*/type.id, /*intent*/intentForTab,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}