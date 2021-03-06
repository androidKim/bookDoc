package com.midasgo.bookdoc.model.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.midasgo.bookdoc.model.entity.BookEntity

@Dao
interface BookDao {
    @Insert
    fun insert(pInfo: BookEntity)

    @Update
    fun update(pInfo: BookEntity)

    @Delete
    fun delete(pInfo: BookEntity)

    @Query("DELETE FROM tb_book")
    fun deleteAll()

    @Query("SELECT * FROM tb_book ORDER BY title ASC")
    fun selectAll(): LiveData<List<BookEntity>>
}