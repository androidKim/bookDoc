package com.midasgo.bookdoc.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midasgo.bookdoc.remote_data_source.ApiProvider
import com.midasgo.bookdoc.remote_data_source.MyApi
import com.midasgo.bookdoc.structure.reply
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class ReplyListViewModel(application: Application): AndroidViewModel(application)
{
    private val disposable = CompositeDisposable()
    private val _replyList: MutableLiveData<List<reply>> = MutableLiveData()
    private val _isUpload: MutableLiveData<Boolean> = MutableLiveData()
    private val _success:MutableLiveData<String> = MutableLiveData()
    private val _error:MutableLiveData<String> = MutableLiveData()

    private val replyList:LiveData<List<reply>>get() = _replyList
    private val isUpload:LiveData<Boolean>get() = _isUpload
    private val error:LiveData<String>get() = _error
    private val success:LiveData<String>get() = _success

    var mApiProvider:ApiProvider? = null

    init {
        mApiProvider = ApiProvider()
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }


    private fun getReplyListFromWeb(note_id:Int){
        disposable.add(mApiProvider!!.provideMyApi(MyApi.SERVER_HOST).getReplyList(note_id)
                // REST API 를 통해 받은 응답에서 Result 값을 추출합니다.

                // 이 이후에 수행되는 코드는 모두 메인 스레드에서 실행합니다.
                .observeOn(AndroidSchedulers.mainThread())
                // 구독할 때 수행할 작업을 구현합니다.
                .doOnSubscribe {

                }
                // 스트림이 종료될 때 수행할 작업을 구현합니다.
                .doOnTerminate {

                }

                // 옵서버블을 구독합니다.
                .subscribe({ it ->
                    // response.body 를 받아 처리합니다.
                    // 작업 중 오류가 발생하면 이 블록은 호출되지 않습니다.
                    if(it.code() == 200)
                    {
                        if(it.body() != null)
                        {
                            var tempArr:ArrayList<reply> = ArrayList()
                            val length:Int = it.body()!!.list.size - 1
                            for(i in 0..length){
                                val item: reply = it.body()!!.list.get(i)
                                tempArr!!.add(item)
                            }
                            _replyList.postValue(tempArr)
                        }
                    }
                }) {
                    // 에러블록
                    // 네트워크 오류나 데이터 처리 오류 등
                    // 작업이 정상적으로 완료되지 않았을 때 호출됩니다.
                    _error.postValue(it.message)
                })
    }

    private fun setReplyToWeb(item:reply){
        disposable.add(mApiProvider!!.provideMyApi(MyApi.SERVER_HOST).setReply(item)
                // REST API 를 통해 받은 응답에서 Result 값을 추출합니다.

                // 이 이후에 수행되는 코드는 모두 메인 스레드에서 실행합니다.
                .observeOn(AndroidSchedulers.mainThread())
                // 구독할 때 수행할 작업을 구현합니다.
                .doOnSubscribe {

                }
                // 스트림이 종료될 때 수행할 작업을 구현합니다.
                .doOnTerminate {

                }

                // 옵서버블을 구독합니다.
                .subscribe({ it ->
                    // response.body 를 받아 처리합니다.
                    // 작업 중 오류가 발생하면 이 블록은 호출되지 않습니다.
                    if(it.code() == 200)
                    {
                        if(it.body() != null)
                        {
                            _success.postValue(it.body()!!.message)
                        }
                    }
                }) {
                    // 에러블록
                    // 네트워크 오류나 데이터 처리 오류 등
                    // 작업이 정상적으로 완료되지 않았을 때 호출됩니다.
                    _error.postValue(it.message)
                })
    }


    fun loadReplyList(note_id: Int){
        getReplyListFromWeb(note_id)
    }

    fun uploadReply(item:reply){
        setReplyToWeb(item)
    }

    fun selectReplyList():LiveData<List<reply>>{
        return replyList
    }

    fun getIsUpload():LiveData<Boolean>{
        return isUpload
    }

    fun errorShow():LiveData<String>{
        return error
    }

    fun successShow():LiveData<String>{
        return success
    }

    /*
    listener..
     */
    fun onClickUplaodReply(view: View){
        _isUpload.postValue(true)
    }
}