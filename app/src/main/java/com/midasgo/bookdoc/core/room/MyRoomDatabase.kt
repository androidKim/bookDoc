package com.midasgo.bookdoc.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.midasgo.bookdoc.core.room.dao.BookDao
import com.midasgo.bookdoc.core.room.entity.BookEntity


@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class MyRoomDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    companion object
    {
        //SINGLETON
        var INSTANCE: MyRoomDatabase? = null

        fun getDatabase(context: Context): MyRoomDatabase {
            if (INSTANCE == null) {
                synchronized(MyRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                MyRoomDatabase::class.java, "my_db")
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}