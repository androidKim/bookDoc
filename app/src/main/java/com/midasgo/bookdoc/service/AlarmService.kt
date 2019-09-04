package com.midasgo.bookdoc.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.view.activity.ActMain

/*
등록된 책중 랜덤으로 알림
 */
class AlarmService : Service() {

    /*
    BoundService타입 방식으로 사용
     */
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    /*
    service가 구동하면 최초에 한번만 실행
     */

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = "cannel_id"
            val channel = NotificationChannel(
                CHANNEL_ID,
                "channel_name",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val notificationIntent = Intent(this, ActMain::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationIntent.putExtra(Constant.INTENT_DATA_NOTIFICATION_CHANNEL_ID, CHANNEL_ID) //channel id 전
            }
            else
            {
                notificationIntent.putExtra(Constant.INTENT_DATA_NOTIFICATION_CHANNEL_ID, 1)
            }
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            var pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("책기록")
                .setContentText("독서를 하고 마음의 양식을!")
                .setSmallIcon(R.mipmap.app_icon)
                .setContentIntent(pendingIntent)
                .build()

            startForeground(1, notification)

        }
    }


    /*
    service가 생성되고 다음으로 실행되는 함수 onstart()함수였으나, onStartCommand()함수를 사용할 것을 권장
    ,서비스가 실행되고 있는 상태에서 또 서비스 시작을 할 경우 onStartCommand()함수를 탐
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        //val alarmState:String = intent!!.extras.getString(Constant.INTENT_DATA_ALARM_STATE)

        return START_NOT_STICKY

    }

    /*
    서비스 중지를 하는 경우 실행되는 함수
     */
    override fun onDestroy() {
        super.onDestroy()
    }

}
