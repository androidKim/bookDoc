package com.midasgo.bookdoc.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R

/*
인트로
 */
class ActIntro : AppCompatActivity() {
    /****************** member ******************/
    var mContext:Context?=null
    var mApp: MyApp?=null
    /****************** system function ******************/
    //---------------------------------------------------
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_intro)
        mContext = this
        mApp = applicationContext as MyApp
        if(mApp?.m_bInit == false)
            mApp?.init(mContext!!)

        initValue()
        recvIntentData()
        initLayout()
    }

    /****************** user function ******************/
    //---------------------------------------------------
    //
    fun initValue(){

    }
    //---------------------------------------------------
    //
    fun recvIntentData(){

    }
    //---------------------------------------------------
    //
    fun initLayout(){
        var isFirstRun:Boolean = mApp?.mSpCtrl?.getSpIsFirstRun() as Boolean

        var pIntent:Intent
        if(isFirstRun)//최초실행여부
        {
            pIntent = Intent(mContext, ActMain::class.java)
        }
        else
        {
            pIntent = Intent(mContext, ActLogin::class.java)
        }

        pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        pIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(pIntent)
    }
}
