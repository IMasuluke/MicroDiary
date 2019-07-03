package com.example.microdiary.Database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memos(
    @PrimaryKey var date: String,
    @ColumnInfo(name = "Memo") var Memo: String
)