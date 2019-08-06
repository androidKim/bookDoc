package com.midasgo.bookdoc.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midasgo.bookdoc.core.model.documents

class SearchViewModel() : DisposableViewModel() {
    private val _array = MutableLiveData<ArrayList<documents>>()

    //mutableLiveData를 immutable 하게 노출
    val array: LiveData<ArrayList<documents>> get() = _array

    //
    fun changeList(list:ArrayList<documents>){
        _array.postValue(list)
    }
}