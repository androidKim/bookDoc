package com.midasgo.bookdoc.core.room.db_viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.midasgo.bookdoc.core.room.entity.BookEntity
import com.midasgo.bookdoc.core.room.repository.BookRepository

/*
ViewModel 내부에서 system service에 접근하거나 다른 이유로 context가 필요한 경우 AndroidViewModel을 상속받아 사용해야 합니다.
AndroidViewModel은 생성자로 application을 받으므로 context를 사용할 수 있다.
 */

class BookDbViewModel (application: Application) : AndroidViewModel(application) {

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

    fun select() {
        bookRepository!!.selectAll()
    }
}