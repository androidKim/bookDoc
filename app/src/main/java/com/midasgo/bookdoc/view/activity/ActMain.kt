package com.midasgo.bookdoc.view.activity

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.crashlytics.android.Crashlytics
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.com.midasgo.bookdoc.common.Constant
import com.midasgo.bookdoc.databinding.ActMainBinding
import com.midasgo.bookdoc.receiver.AlarmReceiver
import com.midasgo.bookdoc.view.fragment.*
import io.reactivex.Observable
import java.util.*


/*
메인화면(기록, 검색, 설정)
 */
class ActMain : AppCompatActivity() {
    /****************** member ******************/
    private lateinit var binding:ActMainBinding
    private var mContext:Context ?= null
    private var mApp: MyApp?= null

    private var bookListFragment:FragBookList?=null
    private var noteBoardFragment:FragNoteBoard?=null
    private var searchFragment:FragSearch?= null
    private var wishFragment:FragWish?=null
    private var settingFragment:FragSetting?=null
    /****************** system function ******************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.act_main)
        mContext = this
        mApp = application as MyApp
        if(mApp?.m_bInit == false)
            mApp?.init(mContext!!)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        recvIntentData()
        initLayout()

        setAlarmManager()//매일 주기적인 알람
    }

    override fun onStart() {
        super.onStart()

        val btnFloatingObservable = floatingBtnClickObservable()//floating action button click
        btnFloatingObservable
            .subscribe { query ->
                var pIntent: Intent = Intent(mContext, ActAddBook::class.java)
                startActivity(pIntent)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(resultCode)
        {
            Constant.FOR_RESULT_DELETE ->{
                bookListFragment = FragBookList()
                replaceFragment(bookListFragment!!)
            }
        }
    }
    /****************** user funtion ******************/
    private fun recvIntentData(){
        val pIntent:Intent = intent
        if(pIntent != null)
        {
            var channelId:String ?= pIntent.getStringExtra(Constant.INTENT_DATA_NOTIFICATION_CHANNEL_ID)
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager //노티피케이션 제거 nm.cancel(id);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.deleteNotificationChannel(channelId)
            }
            else{
                notificationManager.cancel(1)
            }
        }
    }
    private fun initLayout(){
        bookListFragment = FragBookList()//start fragment
        replaceFragment(bookListFragment!!)
    }

    private fun setAlarmManager(){
        /*
         FLAG_UPDATE_CURRENT : pending 인텐트가 이미 존재할 경우 extra data를 모두 대체한다.
         FLAG_CANCEL_CURRENT : pending 인텐트가 이미 존재할 경우 기존 pending인텐드를 cancel하고 다시 생성한다.
         FRAG_IMMUTABLE : 기존 pending인텐트가 변경되지않고, 새로 데이터를 추가한 pending인텐트를 보내도 무시한다.
         FRAG_NO_CREATE : pending인텐트가 기존에 존재하지않으면 Null return
         FRAG_ONE_SHOT : 한번만 사용할 수 있는 pending인텐트
          */

        val alarmIntent:Intent = Intent(this, AlarmReceiver::class.java)
        alarmIntent.putExtra(Constant.INTENT_DATA_ALARM_STATE, Constant.ALARM_ON)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        //매일 오후 6시..
        var calender: Calendar = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, 18)
        calender.set(Calendar.MINUTE, 0)
        calender.set(Calendar.SECOND, 0)

        /*
        RTC_WAKEUP : 인자로 넘긴 시간을 기준으로 알람이 동작 하여 pending intent 전달
        RTC : RTC_WAKEUP 와 같지만 sleep모들에 들어간 기기는 깨우지 않는다.
        ELAPSED_REALTIME_WAKEUP : 안드로이드 기계가 부팅된 기점으로  알람이 울린다.
        ELAPSED_REALTIME : ELAPSED_REALTIME_WAKEUP와 같지만 sleep모들에 들어간 기기는 깨우지 않는다.
         */
        val alarmManager: AlarmManager = mContext!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calender.timeInMillis, pendingIntent)
    }

    /****************** bottom navition ******************/
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_list -> {
                if (bookListFragment == null) {
                    bookListFragment = FragBookList()
                    addFragment(bookListFragment!!)
                }
                else
                {
                    supportFragmentManager.beginTransaction().show(bookListFragment!!).commit()
                }

                if(searchFragment != null)
                    supportFragmentManager.beginTransaction().hide(searchFragment!!).commit()

                if(wishFragment != null)
                    supportFragmentManager.beginTransaction().hide(wishFragment!!).commit()

                if(noteBoardFragment != null)
                    supportFragmentManager.beginTransaction().hide(noteBoardFragment!!).commit()

                if(settingFragment != null)
                    supportFragmentManager.beginTransaction().hide(settingFragment!!).commit()


                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                if (searchFragment == null) {
                    searchFragment = FragSearch()
                    addFragment(searchFragment!!)
                }
                else
                {
                    supportFragmentManager.beginTransaction().show(searchFragment!!).commit()
                }

                if(bookListFragment != null)
                    supportFragmentManager.beginTransaction().hide(bookListFragment!!).commit()

                if(wishFragment != null)
                    supportFragmentManager.beginTransaction().hide(wishFragment!!).commit()

                if(noteBoardFragment != null)
                    supportFragmentManager.beginTransaction().hide(noteBoardFragment!!).commit()

                if(settingFragment != null)
                    supportFragmentManager.beginTransaction().hide(settingFragment!!).commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_wish -> {
                if (wishFragment == null) {
                    wishFragment = FragWish()
                    addFragment(wishFragment!!)
                }
                else{
                    supportFragmentManager.beginTransaction().show(wishFragment!!).commit()
                }

                if(bookListFragment != null)
                    supportFragmentManager.beginTransaction().hide(bookListFragment!!).commit()

                if(searchFragment != null)
                    supportFragmentManager.beginTransaction().hide(searchFragment!!).commit()

                if(noteBoardFragment != null)
                    supportFragmentManager.beginTransaction().hide(noteBoardFragment!!).commit()

                if(settingFragment != null)
                    supportFragmentManager.beginTransaction().hide(settingFragment!!).commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_note_board -> {
                if (noteBoardFragment == null) {
                    noteBoardFragment = FragNoteBoard()
                    addFragment(noteBoardFragment!!)
                }
                else
                {
                    supportFragmentManager.beginTransaction().show(noteBoardFragment!!).commit()
                }

                if(bookListFragment != null)
                    supportFragmentManager.beginTransaction().hide(bookListFragment!!).commit()

                if(searchFragment != null)
                    supportFragmentManager.beginTransaction().hide(searchFragment!!).commit()

                if(wishFragment != null)
                    supportFragmentManager.beginTransaction().hide(wishFragment!!).commit()

                if(settingFragment != null)
                    supportFragmentManager.beginTransaction().hide(settingFragment!!).commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_setting -> {
                if (settingFragment == null) {
                    settingFragment = FragSetting()
                    addFragment(settingFragment!!)
                }else
                {
                    supportFragmentManager.beginTransaction().show(settingFragment!!).commit()
                }

                if(bookListFragment != null)
                    supportFragmentManager.beginTransaction().hide(bookListFragment!!).commit()

                if(searchFragment != null)
                    supportFragmentManager.beginTransaction().hide(searchFragment!!).commit()

                if(wishFragment != null)
                    supportFragmentManager.beginTransaction().hide(wishFragment!!).commit()

                if(noteBoardFragment != null)
                    supportFragmentManager.beginTransaction().hide(noteBoardFragment!!).commit()

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    //-------------------------------------
    //add fragment 재사용
    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .add(R.id.fragmentContainer, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

    //-------------------------------------
    //replace fragment  새로생성
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
            .replace(R.id.fragmentContainer, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

    /********************* Obsevable Ui Event *********************/
    //---------------------------------------------------
    //FloatingAction Button click
    private fun floatingBtnClickObservable(): Observable<String> {
        return Observable.create { emitter ->
            binding.btnFloating.setOnClickListener {
                emitter.onNext("")
            }

            emitter.setCancellable {
                binding.btnFloating.setOnClickListener(null)
            }
        }
    }
}
