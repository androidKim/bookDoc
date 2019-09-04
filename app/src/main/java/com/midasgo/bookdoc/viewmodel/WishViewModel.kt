package com.midasgo.bookdoc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.midasgo.bookdoc.model.entity.WishEntity
import com.midasgo.bookdoc.repository.WishRepository

class WishViewModel (application: Application): AndroidViewModel(application){

    var wishRepository: WishRepository? = null
    var wishList: LiveData<List<WishEntity>>? = null

    init {
        wishRepository = WishRepository(application)
        wishList = wishRepository!!.selectAll()
    }

    fun insert(pInfo: WishEntity) {
        wishRepository!!.insert(pInfo)
    }

    fun update(pInfo: WishEntity) {
        wishRepository!!.update(pInfo)
    }

    fun delete(pInfo: WishEntity){
        wishRepository!!.delete(pInfo)
    }

    fun deleteAll(){
        wishRepository!!.deleteAll()
    }

    fun selectAll() {
        wishRepository!!.selectAll()
    }
}