package com.example.microdiary.Database.DAO

import androidx.room.*
import com.example.microdiary.Database.Entities.Memos

@Dao
interface MemoDAO {
    @Query("SELECT * FROM memos")
    fun getAll(): List<Memos>

    @Query("SELECT * FROM memos WHERE date LIKE :date")
    fun findByDate(date: String): Memos

    @Query("DELETE FROM memos")
    fun nukeTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg todo: Memos)

    @Delete
    fun delete(todo: Memos)

    @Update
    fun update(vararg todos: Memos)
}