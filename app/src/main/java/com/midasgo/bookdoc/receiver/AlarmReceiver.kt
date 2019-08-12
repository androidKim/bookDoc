package com.midasgo.bookdoc.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.service.AlarmService

class AlarmReceiver:BroadcastReceiver()
{
    override fun onReceive(context: Context?, p1: Intent?) {

        val serviceIntent:Intent = Intent(context, AlarmService::class.java)
        serviceIntent.putExtra(Constant.INTENT_DATA_ALARM_STATE, p1!!.getStringExtra(Constant.INTENT_DATA_ALARM_STATE))//alarm state

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context!!.startForegroundService(serviceIntent)
        }else{
            context!!.startService(serviceIntent)
        }

    }
}