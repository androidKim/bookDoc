package com.midasgo.bookdoc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.midasgo.bookdoc.model.entity.BookEntity
import com.midasgo.bookdoc.repository.BookRepository

class BookViewModel (application: Application): AndroidViewModel(application) {
    var bookRepository: BookRepository? = null
    var bookList: LiveData<List<BookEntity>>? = null

    init {
        bookRepository = BookRepository(application)
        bookList = bookRepository!!.selectAll()
    }

    fun insert(pInfo: BookEntity) {
        bookRepository!!.insert(pInfo)
    }

    fun update(pInfo: BookEntity) {
        bookRepository!!.update(pInfo)
    }

    fun delete(pInfo: BookEntity){
        bookRepository!!.delete(pInfo)
    }

    fun deleteAll(){
        bookRepository!!.deleteAll()
    }

    fun selectAll() {
        bookRepository!!.selectAll()
    }
}