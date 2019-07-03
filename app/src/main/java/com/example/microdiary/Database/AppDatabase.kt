package com.example.microdiary.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.microdiary.Database.DAO.MemoDAO
import com.example.microdiary.Database.Entities.Memos

@Database(
    entities = [Memos::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun MemoDAO(): MemoDAO

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "memo-list.db")
            .build()
    }
}