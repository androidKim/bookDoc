package com.midasgo.bookdoc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midasgo.bookdoc.remote_data_source.ApiProvider
import com.midasgo.bookdoc.remote_data_source.MyApi
import com.midasgo.bookdoc.structure.note
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class NoteBoardViewModel(application: Application): AndroidViewModel(application)
{
    private val disposable = CompositeDisposable()
    private val _noteList: MutableLiveData<List<note>> = MutableLiveData()
    private val _error:MutableLiveData<String> = MutableLiveData()

    private val noteList:LiveData<List<note>>get() = _noteList
    private val error:LiveData<String>get() = _error

    var mApiProvider:ApiProvider? = null

    init {
        mApiProvider = ApiProvider()
        getNoteListFromWeb()
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }

    private fun getNoteListFromWeb(){
        disposable.add(mApiProvider!!.provideMyApi(MyApi.SERVER_HOST).getNoteList()
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
                            var tempArr:ArrayList<note> = ArrayList()
                            val length:Int = it.body()!!.list.size - 1
                            for(i in 0..length){
                                val item: note = it.body()!!.list.get(i)
                                tempArr!!.add(item)
                            }
                            _noteList.postValue(tempArr)
                        }
                    }
                }) {
                    // 에러블록
                    // 네트워크 오류나 데이터 처리 오류 등
                    // 작업이 정상적으로 완료되지 않았을 때 호출됩니다.
                    _error.postValue(it.message)
                })
    }

    fun getList():LiveData<List<note>>{
        return noteList
    }

    fun errorShow():LiveData<String>{
        return error
    }

    fun refresh(){
        getNoteListFromWeb()
    }
}