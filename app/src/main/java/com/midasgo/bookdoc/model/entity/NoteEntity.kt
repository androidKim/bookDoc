package com.midasgo.bookdoc.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
db table
 */
@Entity(tableName = "tb_note")
data class NoteEntity (@PrimaryKey(autoGenerate = true) var id:Int,
                       @ColumnInfo(name = "title") var title: String,
                       @ColumnInfo(name = "contents") var contents: String,
                       @ColumnInfo(name = "book_key") var book_key: Int,
                       @ColumnInfo(name = "is_upload") var is_upload: Boolean)