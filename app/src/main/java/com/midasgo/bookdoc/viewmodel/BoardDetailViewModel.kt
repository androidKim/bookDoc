package com.midasgo.bookdoc.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midasgo.bookdoc.remote_data_source.ApiProvider
import com.midasgo.bookdoc.remote_data_source.MyApi
import com.midasgo.bookdoc.structure.note
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class BoardDetailViewModel(application: Application): AndroidViewModel(application)
{
    private val disposable = CompositeDisposable()
    private val _noteInfo:MutableLiveData<note> = MutableLiveData()
    private val _replyCount:MutableLiveData<Int> = MutableLiveData()
    private val _error:MutableLiveData<String> = MutableLiveData()
    private val _isGoReplyActivity:MutableLiveData<Boolean> = MutableLiveData()

    private val noteInfo:LiveData<note>get() = _noteInfo
    private val replyCount:LiveData<Int>get() = _replyCount
    private val error:LiveData<String>get() = _error
    private val isGoReplyActivity:LiveData<Boolean>get() = _isGoReplyActivity

    var mApiProvider:ApiProvider? = null

    init {
        mApiProvider = ApiProvider()
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    private fun getNoteInfoFromWeb(id:Int){
        disposable.add(mApiProvider!!.provideMyApi(MyApi.SERVER_HOST).getNoteItem(id)
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
                            var item:note = it.body()!!.note
                            _noteInfo.postValue(item)
                        }
                    }
                }) {
                    // 에러블록
                    // 네트워크 오류나 데이터 처리 오류 등
                    // 작업이 정상적으로 완료되지 않았을 때 호출됩니다.
                    _error.postValue(it.message)
                })
    }

    private fun getReplyCountFromWeb(note_id:Int){
        disposable.add(mApiProvider!!.provideMyApi(MyApi.SERVER_HOST).getReplyCount(note_id)
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
                            _replyCount.postValue(it.body()!!.count)
                        }
                    }
                }) {
                    // 에러블록
                    // 네트워크 오류나 데이터 처리 오류 등
                    // 작업이 정상적으로 완료되지 않았을 때 호출됩니다.
                    _error.postValue(it.message)
                })
    }

    fun loadNoteItem(id:Int){
        getNoteInfoFromWeb(id)
    }


    fun loadReplyCount(note_id: Int){
        getReplyCountFromWeb(note_id)
    }

    fun selectNoteItem():LiveData<note>{
        return noteInfo
    }

    fun selectReplyCount():LiveData<Int>{
        return replyCount
    }

    fun errorShow():LiveData<String>{
        return error
    }

    fun getGoReplyActivityStatus():LiveData<Boolean>{
        return isGoReplyActivity
    }

    /*
    listener..
     */
    fun onClickGoReply(view: View){
        _isGoReplyActivity.postValue(true)
    }
}