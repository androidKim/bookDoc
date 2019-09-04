package com.midasgo.bookdoc.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.midasgo.bookdoc.structure.book

/*
db table
 */
@Entity(tableName = "tb_wish")
data class WishEntity (@PrimaryKey(autoGenerate = true) var id:Int,
                       @ColumnInfo(name = "title") var title: String,
                       @ColumnInfo(name = "desc") var desc: String,
                       @ColumnInfo(name = "read_page") var read_page: String,
                       @ColumnInfo(name = "total_page") var total_page: String,
                       @ColumnInfo(name = "reg_date") var reg_date: String,
                       @ColumnInfo(name = "img_url") var img_url: String)
{
    constructor():this(0,"","","","","","")
    companion object{
        fun getEntity(pInfo: book): WishEntity
        {
            val bookEntity: WishEntity =
                WishEntity()
            bookEntity.id = pInfo.id
            bookEntity.title = pInfo.title
            bookEntity.desc = pInfo.desc
            bookEntity.read_page = pInfo.read_page
            bookEntity.total_page = pInfo.total_page
            bookEntity.reg_date = pInfo.reg_date
            bookEntity.img_url = pInfo.img_url
            return bookEntity
        }
    }
}