package com.midasgo.bookdoc.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.midasgo.bookdoc.core.MyRoomDatabase
import com.midasgo.bookdoc.model.dao.WishDao
import com.midasgo.bookdoc.model.entity.WishEntity

class WishRepository(application: Application)
{
    private var wishDao: WishDao? = null
    private var wishList: LiveData<List<WishEntity>>? = null

    init {
        val roomDatabase = MyRoomDatabase.getDatabase(application)
        wishDao = roomDatabase?.wishDao()
        wishList = wishDao?.selectAll()
    }

    fun selectAll():LiveData<List<WishEntity>> {
        return wishList!!
    }
    fun deleteAll()
    {
        deleteAllAsyncTask(wishDao!!).execute()
    }

    fun insert(pInfo: WishEntity) {
        insertAsyncTask(wishDao!!).execute(pInfo)
    }

    fun update(pInfo: WishEntity) {
        updateAsyncTask(wishDao!!).execute(pInfo)
    }

    fun delete(pInfo: WishEntity){
        deleteAsynkTask(wishDao!!).execute(pInfo)
    }


    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: WishDao) : AsyncTask<WishEntity, Void, Void>() {
        override fun doInBackground(vararg params: WishEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    private class updateAsyncTask internal constructor(private val mAsyncTaskDao: WishDao) : AsyncTask<WishEntity, Void, Void>() {
        override fun doInBackground(vararg params: WishEntity): Void? {
            mAsyncTaskDao.update(params[0])
            return null
        }
    }

    private class deleteAsynkTask internal constructor(private val mAsyncTaskDao: WishDao) : AsyncTask<WishEntity, Void, Void>() {
        override fun doInBackground(vararg params: WishEntity): Void? {
            mAsyncTaskDao.delete(params[0])
            return null
        }
    }

    private class deleteAllAsyncTask internal constructor(private val mAsyncTaskDao: WishDao) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }
    }
}