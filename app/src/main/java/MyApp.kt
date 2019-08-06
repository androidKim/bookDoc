package com.midasgo.bookdoc

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.midasgo.bookdoc.core.SharedPreferenceCtrl
import com.midasgo.bookdoc.core.api.ApiProvider
import com.midasgo.bookdoc.ui.act.ActLogin
import com.midasgo.bookdoc.ui.act.ActMain
import com.midasgo.bookdoc.ui.custom.dialog.MessageDialog
import com.midasgo.bookdoc.ui.custom.dialog.MessageDialogYn
import io.reactivex.disposables.CompositeDisposable

class MyApp : MultiDexApplication() {
    var mContext: Context? = null
    var m_bInit: Boolean = false
    var mSpCtrl: SharedPreferenceCtrl? = null
    var mWebCtrl: ApiProvider? = null
    var mVersionCode: String? = null
    private var disposable = CompositeDisposable()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
    }

    fun init(pContext: Context) {
        if (m_bInit == false) {
            mContext = pContext

            //
            mWebCtrl = ApiProvider()

            //sharedPreference..
            mSpCtrl = SharedPreferenceCtrl()
            mSpCtrl?.init(mContext!!)

            //버전
            try {
                val pInfo = mContext!!.packageManager.getPackageInfo(mContext!!.packageName, 0)
                mVersionCode = pInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            //localDb.

            //device size

            //packgename

            //etc..

            m_bInit = true
        }
    }

    //-----------------------------------------------
    //
    fun goMain(pContext: Context) {
        if (pContext == null)
            return

        var pIntent = Intent(pContext, ActMain::class.java)
        pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        pIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        pContext.startActivity(pIntent)
    }

    //-----------------------------------------------
    //
    fun logoutProc(pContext: Context) {
        if (pContext == null)
            return

        var pIntent = Intent(pContext, ActLogin::class.java)
        pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        pIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        pContext.startActivity(pIntent)

        mSpCtrl?.clearData()
    }

    /*********************** Dialog ***********************/
    //-----------------------------------------------
    //
    fun showMessageDialog(pContext: Context, msg: String, ok: String) {
        MessageDialog.Builder(pContext)
            .setMassage(msg)
            .setOkButton(ok)
            .show()
    }

    fun showMessageDialog(pContext: Context, msg: String, ok: String, listener:MessageDialog.ifCallback) {
       val dialog = MessageDialog.Builder(pContext)
            .setMassage(msg)
            .setOkButton(ok)
            .show()

        dialog.setListener(listener)
    }
    //-----------------------------------------------
    //
    fun showMessageDialogYn(pContext: Context, pCallback: MessageDialogYn.ifCallback, msg: String, ok: String, cancel: String) {
        MessageDialogYn.Builder(pContext, pCallback)
            .setMassage(msg)
            .setOkButton(ok)
            .setCancelButton(cancel)
            .show()
    }
}