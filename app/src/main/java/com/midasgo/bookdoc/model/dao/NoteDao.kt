package com.midasgo.bookdoc.model.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.midasgo.bookdoc.model.entity.NoteEntity

@Dao
interface NoteDao {
    @Insert
    fun insert(pInfo: NoteEntity)

    @Update
    fun update(pInfo: NoteEntity)

    @Delete
    fun delete(pInfo: NoteEntity)

    @Query("DELETE FROM tb_note")
    fun deleteAll()

    @Query("SELECT * FROM tb_note ORDER BY id ASC")
    fun selectAll(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM tb_note WHERE book_key = :book_key")
    fun selectItem(book_key:Int):NoteEntity
}