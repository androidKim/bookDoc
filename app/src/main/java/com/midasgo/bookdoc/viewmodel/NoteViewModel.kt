package com.midasgo.bookdoc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.midasgo.bookdoc.model.entity.NoteEntity
import com.midasgo.bookdoc.repository.NoteRepository

class NoteViewModel(application: Application): AndroidViewModel(application)
{
    var noteRepository: NoteRepository? = null
    var noteList: LiveData<List<NoteEntity>>? = null

    init {
        noteRepository = NoteRepository(application)
        noteList = noteRepository!!.selectAll()
    }

    fun insert(item: NoteEntity) {
        noteRepository!!.insert(item)
    }

    fun update(item: NoteEntity) {
        noteRepository!!.update(item)
    }

    fun delete(item: NoteEntity){
        noteRepository!!.delete(item)
    }

    fun deleteAll(){
        noteRepository!!.deleteAll()
    }

    fun selectAll() {
        noteRepository!!.selectAll()
    }

    fun selectItem(book_key: Int):NoteEntity {
        return noteRepository!!.selectItem(book_key)
    }
}