package com.midasgo.bookdoc.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BookViewModel() : DisposableViewModel() {
    private val _title = MutableLiveData<String>()
    private val _desc = MutableLiveData<String>()
    private val _readPage = MutableLiveData<String>()
    private val _totalPage = MutableLiveData<String>()
    private val _error = MutableLiveData<String>()

    //mutableLiveData를 immutable 하게 노출
    val title: LiveData<String> get() = _title
    val desc: LiveData<String> get() = _desc
    val readPage: LiveData<String> get() = _readPage
    val totalPage: LiveData<String> get() = _totalPage
    val error: LiveData<String> get() = _error

    fun changeTitle(str:String){
        _title.postValue(str)
    }

    fun changeDesc(str:String){
        _desc.postValue(str)
    }

    fun changeReadPage(str:String){
        _readPage.postValue(str)
    }

    fun changetotalPage(str:String){
        _totalPage.postValue(str)
    }
}