package com.midasgo.bookdoc.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.midasgo.bookdoc.core.MyRoomDatabase
import com.midasgo.bookdoc.model.dao.NoteDao
import com.midasgo.bookdoc.model.entity.NoteEntity

class NoteRepository(application: Application)
{
    private var noteDao: NoteDao? = null
    private var noteList:LiveData<List<NoteEntity>>? = null

    init {
        val roomDatabase = MyRoomDatabase.getDatabase(application)
        noteDao = roomDatabase?.noteDao()
        noteList = noteDao?.selectAll()
    }

    fun selectAll():LiveData<List<NoteEntity>> {
        return noteList!!
    }

    fun selectItem(book_key:Int):NoteEntity{
        if(noteDao!!.selectItem(book_key) != null) {
            return noteDao!!.selectItem(book_key)!!
        }
        else {
            return NoteEntity(0,"","","",0,false)
        }
    }

    fun deleteAll()
    {
        deleteAllAsyncTask(noteDao!!).execute()
    }

    fun insert(pInfo: NoteEntity) {
        insertAsyncTask(noteDao!!).execute(pInfo)
    }

    fun update(pInfo: NoteEntity) {
        updateAsyncTask(noteDao!!).execute(pInfo)
    }

    fun delete(pInfo: NoteEntity){
        deleteAsynkTask(noteDao!!).execute(pInfo)
    }

    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: NoteDao) : AsyncTask<NoteEntity, Void, Void>() {
        override fun doInBackground(vararg params: NoteEntity): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    private class updateAsyncTask internal constructor(private val mAsyncTaskDao: NoteDao) : AsyncTask<NoteEntity, Void, Void>() {
        override fun doInBackground(vararg params: NoteEntity): Void? {
            mAsyncTaskDao.update(params[0])
            return null
        }
    }

    private class deleteAsynkTask internal constructor(private val mAsyncTaskDao: NoteDao) : AsyncTask<NoteEntity, Void, Void>() {
        override fun doInBackground(vararg params: NoteEntity): Void? {
            mAsyncTaskDao.delete(params[0])
            return null
        }
    }

    private class deleteAllAsyncTask internal constructor(private val mAsyncTaskDao: NoteDao) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }
    }
}