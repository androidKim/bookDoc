package com.midasgo.bookdoc.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.databinding.ActLoginBinding
import io.reactivex.Observable


/*
login activity
 */
class ActLogin : AppCompatActivity() {
    /****************** define ******************/

    /****************** member ******************/
    private lateinit var binding: ActLoginBinding
    private var mContext: Context ?= null
    private var mApp: MyApp?= null
    /****************** system function ******************/
    //---------------------------------------------------
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.act_login)
        mContext = this
        mApp = application as MyApp
        if(mApp?.m_bInit == false)
            mApp?.init(mContext!!)
    }
    //---------------------------------------------------
    //
    override fun onStart() {
        super.onStart()

        val observable = startClickObservable()//시작 클릭
        observable
            .subscribe { query ->
                //최초실행여부 저장
                mApp?.mSpCtrl?.setSpIsFirstRun(true)
                var pIntent:Intent = Intent(mContext, ActMain::class.java)
                pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                pIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(pIntent)
            }
    }
    //---------------------------------------------------
    //
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    /****************** user function ******************/

    /********************* Obsevable Ui Event *********************/
    //---------------------------------------------------
    //시작 클릭 이벤트
    private fun startClickObservable(): Observable<String> {
        return Observable.create { emitter ->
            binding.lyBase.setOnClickListener {
                emitter.onNext("")
            }

            emitter.setCancellable {
                binding.lyBase.setOnClickListener(null)
            }
        }
    }
}
