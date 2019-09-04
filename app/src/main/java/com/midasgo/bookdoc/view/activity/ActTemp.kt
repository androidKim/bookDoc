package com.midasgo.bookdoc.view.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.midasgo.bookdoc.MyApp
import com.midasgo.bookdoc.R
import com.midasgo.bookdoc.remote_data_source.MyApi
import com.midasgo.bookdoc.remote_data_source.req_test
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

/*
액티비티 기본파일
 */
class ActTemp : AppCompatActivity() {
    /********************* define *********************/

    /********************* member *********************/
    private val disposable = CompositeDisposable()
    var mApp: MyApp? = null
    var mContext: Context? = null
    var mRequestManager: RequestManager? = null
    /********************* controller *********************/

    /********************* system function *********************/
    /****************** system function ******************/
    //---------------------------------------------------
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_temp)

        mContext = this
        mApp = application as MyApp
        if(mApp?.m_bInit == false)
            mApp?.init(mContext!!)

        mRequestManager = Glide.with(mContext!!)

        initValue()
        recvIntentData()
        initLayout()
    }
    //------------------------------------------------------
    //
    override fun onDestroy() {
        super.onDestroy()
        // Activity 가 종료되면 사용중인 통신을 중단합니다.
        disposable.clear()
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

    }

    //------------------------------------------------------
    //
    fun tempProc(){
        var param: req_test = req_test("test")
        disposable.add(mApp!!.mWebCtrl!!.provideMyApi(MyApi.SERVER_HOST).test(param)
            // REST API 를 통해 받은 응답에서 Result 값을 추출합니다.

            // 이 이후에 수행되는 코드는 모두 메인 스레드에서 실행합니다.
            .observeOn(AndroidSchedulers.mainThread())

            // 구독할 때 수행할 작업을 구현합니다.
            .doOnSubscribe {  }

            // 스트림이 종료될 때 수행할 작업을 구현합니다.
            .doOnTerminate {

            }

            // 옵서버블을 구독합니다.
            .subscribe({ it ->
                // response.body 를 받아 처리합니다.
                // 작업 중 오류가 발생하면 이 블록은 호출되지 않습니다.

            }) {
                // 에러블록
                // 네트워크 오류나 데이터 처리 오류 등
                // 작업이 정상적으로 완료되지 않았을 때 호출됩니다.

            })
    }
}
