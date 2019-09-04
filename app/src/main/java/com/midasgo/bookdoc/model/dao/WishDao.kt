package com.midasgo.bookdoc.model.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.midasgo.bookdoc.model.entity.WishEntity

@Dao
interface WishDao {
    @Insert
    fun insert(pInfo: WishEntity)

    @Update
    fun update(pInfo: WishEntity)

    @Delete
    fun delete(pInfo: WishEntity)

    @Query("DELETE FROM tb_wish")
    fun deleteAll()

    @Query("SELECT * FROM tb_wish ORDER BY title ASC")
    fun selectAll(): LiveData<List<WishEntity>>
}