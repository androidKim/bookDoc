package com.midasgo.bookdoc.core.room.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.midasgo.bookdoc.core.MyRoomDatabase
import com.midasgo.bookdoc.core.room.dao.BookDao
import com.midasgo.bookdoc.core.room.entity.BookEntity

class BookRepository(application: Application)
{
    private var bookDao: BookDao? = null
    private var bookList: LiveData<List<BookEntity>>? = null

    init {
        val roomDatabase = MyRoomDatabase.getDatabase(application)
        bookDao = roomDatabase?.bookDao()!!
        bookList = bookDao?.selectAll()
    }

    fun selectAll():LiveData<List<BookEntity>> {
        return bookList!!
    }
    fun deleteAll()
    {
        deleteAllAsyncTask(bookDao!!).execute()
    }

    fun insert(pInfo: BookEntity) {
        insertAsyncTask(bookDao!!).execute(pInfo)
    }

    fun update(pInfo: BookEntity) {
        updateAsyncTask(bookDao!!).execute(pInfo)
    }

    fun delete(pInfo: BookEntity){
        deleteAsynkTask(bookDao!!).execute(pInfo)
    }


    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: BookDao) : AsyncTask<BookEntity, Void, Void>() {
        override fun doInBackground(vararg params: BookEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    private class updateAsyncTask internal constructor(private val mAsyncTaskDao: BookDao) : AsyncTask<BookEntity, Void, Void>() {
        override fun doInBackground(vararg params: BookEntity): Void? {
            mAsyncTaskDao.update(params[0])
            return null
        }
    }

    private class deleteAsynkTask internal constructor(private val mAsyncTaskDao: BookDao) : AsyncTask<BookEntity, Void, Void>() {
        override fun doInBackground(vararg params: BookEntity): Void? {
            mAsyncTaskDao.delete(params[0])
            return null
        }
    }

    private class deleteAllAsyncTask internal constructor(private val mAsyncTaskDao: BookDao) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }
    }
}